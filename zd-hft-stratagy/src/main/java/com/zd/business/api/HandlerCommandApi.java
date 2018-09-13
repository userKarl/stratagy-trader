package com.zd.business.api;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.shanghaizhida.beans.CancelInfo;
import com.shanghaizhida.beans.CommandCode;
import com.shanghaizhida.beans.MarketInfo;
import com.shanghaizhida.beans.ModifyInfo;
import com.shanghaizhida.beans.NetInfo;
import com.shanghaizhida.beans.OrderResponseInfo;
import com.zd.business.common.BaseService;
import com.zd.business.common.CommonUtils;
import com.zd.business.constant.CheckEnum;
import com.zd.business.constant.RedisConst;
import com.zd.business.constant.TraderEnvEnum;
import com.zd.business.constant.order.BuySaleEnum;
import com.zd.business.constant.order.PriceTypeEnum;
import com.zd.business.entity.ContractOrder;
import com.zd.business.entity.TraderRef;
import com.zd.business.entity.ctp.Order;
import com.zd.config.Global;

@RestController
public class HandlerCommandApi {

	@Autowired
	private BaseService baseService;

	/**
	 * ZD期货 追单平主动腿 先撤被动腿挂单
	 * 
	 * @param localNo
	 * @return
	 */
	@GetMapping("/chasingEvenActive/{orderId}")
	public String chasingEvenActive(@PathVariable String orderId) {
		String orderResponseInfo = (String) baseService.getRedisService().hmGet(RedisConst.CONTRACTORDER_ORDERINFO,
				orderId);
		String traderRefStr = (String) baseService.getRedisService().hmGet(RedisConst.CONTRACTORDER_TRADERREF, orderId);
		String userId = (String) baseService.getRedisService().hmGet(RedisConst.CONTRACTORDER_USERID, orderId);
		String resp = "";
		try {
			TraderRef traderRef = new TraderRef();
			traderRef.MyReadString(traderRefStr);
			traderRef.setEvenActiveFlag(CheckEnum.YES.getCode());
			String localNo = baseService.generateLocalSystemCode();
			OrderResponseInfo mOrderResponseInfo = new OrderResponseInfo();
			mOrderResponseInfo.MyReadString(orderResponseInfo);
			CancelInfo cancelInfo = CommonUtils.generateCancelInfo(userId, mOrderResponseInfo);
			NetInfo ni = new NetInfo();
			ni.code = CommandCode.CANCEL;
			ni.accountNo = userId;
			ni.exchangeCode = TraderEnvEnum.ZD.getCode();
			ni.localSystemCode = TraderEnvEnum.ZD.toString();
			ni.systemCode = mOrderResponseInfo.systemNo;
			ni.infoT = cancelInfo.MyToString();
			baseService.getRedisService().hmSet(RedisConst.NETINFO_LOCALNO_TRADERREF, localNo, traderRef.MyToString());
			baseService.getRedisService().hmSet(RedisConst.ORDER_SYSTEMNO_CANCEL_LOCALNO, mOrderResponseInfo.systemNo,
					localNo);
			resp = "撤单";
			Global.orderEventProducer.onData(ni.MyToString());
		} catch (Exception e) {
			// TODO: handle exception
		}
		return resp;
	}

	/**
	 * 立即追单
	 * 被动腿计入20个滑点下单
	 * 被动腿为ZD国际期货
	 * @param orderId
	 * @return
	 */
	@GetMapping("/chasingOrderMarket/{orderId}")
	public String chasingOrderMarket(@PathVariable String orderId) {
		String orderResponseInfo = (String) baseService.getRedisService().hmGet(RedisConst.CONTRACTORDER_ORDERINFO,
				orderId);
		String traderRefStr = (String) baseService.getRedisService().hmGet(RedisConst.CONTRACTORDER_TRADERREF, orderId);
		String userId = (String) baseService.getRedisService().hmGet(RedisConst.CONTRACTORDER_USERID, orderId);
		String contractOrderInfo = (String) baseService.getRedisService().hmGet(RedisConst.CONTRACTORDERINFO, orderId);
		ContractOrder marketContractOrder=new ContractOrder();
		marketContractOrder.MyReadString(contractOrderInfo);
		String resp = "";
		try {
			TraderRef traderRef = new TraderRef();
			traderRef.MyReadString(traderRefStr);
			traderRef.setEvenActiveFlag(CheckEnum.YES.getCode());
			String localNo = baseService.generateLocalSystemCode();
			OrderResponseInfo mOrderResponseInfo = new OrderResponseInfo();
			mOrderResponseInfo.MyReadString(orderResponseInfo);
			BigDecimal counterPrice = BigDecimal.valueOf(0);// 对盘价
			BigDecimal newPrice = BigDecimal.valueOf(0);// 新的挂单价格
			MarketInfo marketInfo = Global.zdContractMap.get(mOrderResponseInfo.exchangeCode+"@"+mOrderResponseInfo.code);
			int decimals = CommonUtils.getDecimalsUnits(marketContractOrder.getUpperTick());
			if (BuySaleEnum.BUY.getCode().equals(marketContractOrder.getDirection())) {
				counterPrice = new BigDecimal(marketInfo.salePrice);
				newPrice = counterPrice.add(marketContractOrder.getUpperTick().multiply(BigDecimal.valueOf(20)))
						.setScale(decimals, BigDecimal.ROUND_DOWN);
			} else if (BuySaleEnum.SALE.getCode().equals(marketContractOrder.getDirection())) {
				counterPrice = new BigDecimal(marketInfo.buyPrice);
				newPrice = counterPrice.subtract(marketContractOrder.getUpperTick().multiply(BigDecimal.valueOf(20)))
						.setScale(decimals, BigDecimal.ROUND_UP);
			}
			ModifyInfo modifyInfo = CommonUtils.generateModify(userId, mOrderResponseInfo,
					mOrderResponseInfo.orderNumber, newPrice.toString(), PriceTypeEnum.LIMIT.getCode());
			NetInfo ni = new NetInfo();
			ni.code = CommandCode.MODIFY;
			ni.accountNo = userId;
			ni.exchangeCode = TraderEnvEnum.ZD.getCode();
			ni.localSystemCode = TraderEnvEnum.ZD.toString();
			ni.systemCode = mOrderResponseInfo.systemNo;
			ni.infoT = modifyInfo.MyToString();
			baseService.getRedisService().hmSet(RedisConst.NETINFO_LOCALNO_TRADERREF, localNo, traderRef.MyToString());
			baseService.getRedisService().hmSet(RedisConst.ORDER_SYSTEMNO_CANCEL_LOCALNO, mOrderResponseInfo.systemNo,
					localNo);
			resp = "改单";
			Global.orderEventProducer.onData(ni.MyToString());
		} catch (Exception e) {
			// TODO: handle exception
		}
		return resp;
	}
	
	/**
	 * 被动腿为CTP时追单
	 * 撤销被动腿挂单
	 * 平主动腿
	 * @param orderId
	 * @return
	 */
	@GetMapping("/ctpChasingEvenActive/{orderId}")
	public String ctpChasingEvenActive(@PathVariable String orderId) {
		String orderInfo = (String) baseService.getRedisService().hmGet(RedisConst.CONTRACTORDER_ORDERINFO,
				orderId);
		String traderRefStr = (String) baseService.getRedisService().hmGet(RedisConst.CONTRACTORDER_TRADERREF, orderId);
		String userId = (String) baseService.getRedisService().hmGet(RedisConst.CONTRACTORDER_USERID, orderId);
		String resp = "";
		try {
			TraderRef traderRef = new TraderRef();
			traderRef.MyReadString(traderRefStr);
			traderRef.setEvenActiveFlag(CheckEnum.YES.getCode());
			String localNo = baseService.generateLocalSystemCode();
			Order order = new Order();
			order.MyReadString(orderInfo);
			NetInfo ni = new NetInfo();
			ni.code = CommandCode.CANCEL;
			ni.accountNo = userId;
			ni.exchangeCode = TraderEnvEnum.CTP.getCode();
			ni.localSystemCode = TraderEnvEnum.CTP.toString();
			ni.infoT = order.MyToString();
			baseService.getRedisService().hmSet(RedisConst.NETINFO_LOCALNO_TRADERREF, localNo, traderRef.MyToString());
			baseService.getRedisService().hmSet(RedisConst.ORDER_SYSTEMNO_CANCEL_LOCALNO, order.getOrderID(),
					localNo);
			resp = "CTP撤单";
			Global.orderEventProducer.onData(ni.MyToString());
		} catch (Exception e) {
			// TODO: handle exception
		}
		return resp;
	}
	
}

package com.zd.business.entity;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.Lists;
import com.shanghaizhida.beans.AccountInfo;
import com.shanghaizhida.beans.AccountResponseInfo;
import com.shanghaizhida.beans.CancelResponseInfo;
import com.shanghaizhida.beans.CommandCode;
import com.shanghaizhida.beans.Constants;
import com.shanghaizhida.beans.CurrencyInfo;
import com.shanghaizhida.beans.ErrorCode;
import com.shanghaizhida.beans.FilledResponseInfo;
import com.shanghaizhida.beans.LoginResponseInfo;
import com.shanghaizhida.beans.NetInfo;
import com.shanghaizhida.beans.OrderResponseInfo;
import com.shanghaizhida.beans.OrderStatusInfo;
import com.zd.common.utils.StringUtils;
import com.zd.common.utils.calc.ArithDecimal;

import lombok.Data;

/**
 * 策略类 被动腿合约接收行情，根据公式计算出主动腿合约的下单价格
 * 
 * @author user
 *
 */
@Data
public class Stratagy {

	private String id; // 策略ID
	private String name; // 策略名称
	private String type; // 策略类型（M市商，A套利）
	private String status; // 策略状态
	private String expression; // 套利公式(四腿时在客户端输入分别为P1,P2,P3,P4;服务器接收到数据后需要将P1替换成m)
	private List<Contract> marketContractList;//被动腿合约
	private Contract activeContract; // 主动腿合约
	private MarketProvider mp; // 做市商
	private Arbitrage arbitrage;//套利
	
	/**
	 * 登陆返回数据(CurrencyNo,LoginResponseInfo) -用于在下单时获取用户类型和资金账户
	 */
	private HashMap<String, LoginResponseInfo> loginInfoMap;
	 
	/**
	 * 下单返回数据.委托返回数据保存(SystemNo,OrderResponseInfo)
	 */
	private HashMap<String, OrderResponseInfo> weituoInfoMap;

	/**
	 * 下单请求数据(LocalNo,OrderResponseInfo)
	 */
	private HashMap<String, OrderResponseInfo> xiadanInfoMap;

	/**
	 * 挂单数据(SystemNo,OrderResponseInfo)
	 */
	private HashMap<String, OrderResponseInfo> guadanInfoMap;

	/**
	 * 持仓合计(Contract,OrderStatusInfo)
	 */
	private HashMap<String, OrderStatusInfo> chicangInfoMap;

	/**
	 * 成交明细(FilledNo,FilledResponseInfo)
	 */
	private HashMap<String, FilledResponseInfo> chengjiaoInfoMap;

	/**
	 * 成交合计(Exchange+Code+buysell,FilledResponseInfo)
	 */
	HashMap<String, FilledResponseInfo> chengjiaoTotalInfoMap;

	/**
	 * 资金数据(accountNo,AccountResponseInfo)
	 */
	private HashMap<String, AccountResponseInfo> zijinInfoMap;

	/**
	 * 基币账户
	 */
	private AccountResponseInfo jibiInfo;

	/**
	 * 基币币种
	 */
	CurrencyInfo jibibizhong = new CurrencyInfo();
	
	public void MyReadString(String temp) {
		if (StringUtils.isNotBlank(temp)) {
			String[] split = temp.split("@", -1);
			if (split != null) {
				if (split.length > 0) {
					this.id = split[0];
				}
				if (split.length > 1) {
					this.name = split[1];
				}
				if (split.length > 2) {
					this.type = split[2];
				}
			}
		}
	}

	public String MyToString() {
		return String.join("@", Lists.newArrayList(this.id, this.name, this.type));
	}

	public void initList() {

		if (weituoInfoMap == null)
			weituoInfoMap = new HashMap<String, OrderResponseInfo>();
		else
			weituoInfoMap.clear();

		if (xiadanInfoMap == null)
			xiadanInfoMap = new HashMap<String, OrderResponseInfo>();
		else
			xiadanInfoMap.clear();

		if (guadanInfoMap == null)
			guadanInfoMap = new HashMap<String, OrderResponseInfo>();
		else
			guadanInfoMap.clear();

		if (chicangInfoMap == null)
			chicangInfoMap = new HashMap<String, OrderStatusInfo>();
		else
			chicangInfoMap.clear();

		if (chengjiaoInfoMap == null)
			chengjiaoInfoMap = new HashMap<String, FilledResponseInfo>();
		else
			chengjiaoInfoMap.clear();

		if (chengjiaoTotalInfoMap == null)
			chengjiaoTotalInfoMap = new HashMap<String, FilledResponseInfo>();
		else
			chengjiaoTotalInfoMap.clear();

		if (zijinInfoMap == null)
			zijinInfoMap = new HashMap<String, AccountResponseInfo>();
		else
			zijinInfoMap.clear();
		jibiInfo = null;
	}
	
	/**
	 * 分割消息体
	 *
	 * @param infoT
	 * @return
	 */
	private String[] infoTSplit(String infoT) {
		return infoT.split("\\^", -1);
	}

	/**
	 * 处理交易返回数据
	 *
	 * @param netInfo
	 *            消息内容
	 */
	public void traderInfoHandler(NetInfo netInfo) {

		// 下单返回
		if (CommandCode.ORDER.equals(netInfo.code)) {

			String errorMsg = "";

			// 下单成功,infoT中是正常的完整数据
			if (netInfo.errorCode.equals(ErrorCode.SUCCESS)) {
				OrderResponseInfo orderResponseInfo = new OrderResponseInfo();
				orderResponseInfo.MyReadString(netInfo.infoT);

				// orderResponseInfo.orderState =
				// Constants.tradeStatusByNum(orderResponseInfo.orderState);
				orderResponseInfo.orderState = Constants.TRADE_STATUS_YIPAIDUI;

				weituoInfoMap.put(orderResponseInfo.systemNo, orderResponseInfo);
				guadanInfoMap.put(orderResponseInfo.systemNo, orderResponseInfo);

			}
			// 下单出现异常.或失败时,order中返回的数据是空的,这时候需要从下单的时候保存的列表中取出来用
			else {
				errorMsg = ErrorCode.getErrorMsgByCode(netInfo.errorCode);

				// 如果order消息先来,systemno后来
				if (!weituoInfoMap.containsKey(netInfo.systemCode)) {
					if (xiadanInfoMap.containsKey(netInfo.localSystemCode)) {
						OrderResponseInfo info = xiadanInfoMap.get(netInfo.localSystemCode);

						if (Constants.TRADE_STATUS_ZIJIN_LESS.equals(errorMsg))
							info.orderState = Constants.TRADE_STATUS_ZIJIN_LESS;
						else
							info.orderState = Constants.TRADE_STATUS_ZHILING_FAIL;

						weituoInfoMap.put(netInfo.systemCode, xiadanInfoMap.remove(netInfo.localSystemCode));
					}
				} else {
					// 如果是正常情况,先来systemno 后来order 此时在systemno中已经将订单加进来了,只需要设置订单状态为指令失败就好了
					OrderResponseInfo info = weituoInfoMap.get(netInfo.systemCode);

					if (Constants.TRADE_STATUS_ZIJIN_LESS.equals(errorMsg))
						info.orderState = Constants.TRADE_STATUS_ZIJIN_LESS;
					else
						info.orderState = Constants.TRADE_STATUS_ZHILING_FAIL;
				}

				// 下单失败 移除
				guadanInfoMap.remove(netInfo.systemCode);

				// refreshYingSunList();
			}

			return;
		}
		// 最新定单状态信息返回
		else if (CommandCode.ORDERSTATUS.equals(netInfo.code)) {

			updateWeituoInfoByOrderStatusInfo(netInfo);

			OrderResponseInfo info = weituoInfoMap.get(netInfo.systemCode);

			updateGuadanByWeituoInfo(info);

			return;
		}
		// 成交数据返回
		else if (CommandCode.FILLEDCAST.equals(netInfo.code)) {

			FilledResponseInfo responseInfo = new FilledResponseInfo();
			responseInfo.MyReadString(netInfo.infoT);

			// 将成交数据插入成交列表(明细)
			chengjiaoInfoMap.put(responseInfo.filledNo, responseInfo);

			// 更新成交合计
			FilledResponseInfo totalInfo = new FilledResponseInfo();
			totalInfo.MyReadString(netInfo.infoT);
			updateChengjiaoTotalInfo(totalInfo);

			return;
		}
		// 持仓状态返回
		else if (CommandCode.HOLDSTATUS.equals(netInfo.code)) {

			OrderStatusInfo orderStatusInfo = new OrderStatusInfo();
			orderStatusInfo.MyReadString(netInfo.infoT);

			// 对持仓map加线程同步
			synchronized (chicangInfoMap) {
				updateChicangTotalByOrderStatusInfo(orderStatusInfo);
			}

			return;
		}
		// 最新账户资金信息返回
		else if (CommandCode.ACCOUNTLAST.equals(netInfo.code)) {

			AccountInfo responseInfo = new AccountInfo();
			responseInfo.MyReadString(netInfo.infoT);

			// 对资金map加线程同步
			synchronized (zijinInfoMap) {
				updateLastAccountByAccountInfo(responseInfo);

				updateBaseCurrenyByRate();
			}

			return;
		}
		// 撤单
		else if (CommandCode.CANCELCAST.equals(netInfo.code)) {

			// 撤单成功
			if (netInfo.errorCode.equals(ErrorCode.SUCCESS)) {
				CancelResponseInfo cancelResponseInfo = new CancelResponseInfo();
				cancelResponseInfo.MyReadString(netInfo.infoT);

				updateGuadanWeituoByCancelInfo(cancelResponseInfo);

			}
			// 撤单失败
			else {
			}

			return;
		}
		// 改单
		else if (CommandCode.MODIFY.equals(netInfo.code)) {

			// 改单成功
			if (netInfo.errorCode.equals(ErrorCode.SUCCESS)) {

				OrderResponseInfo responseInfo = new OrderResponseInfo();
				responseInfo.MyReadString(netInfo.infoT);

				if (weituoInfoMap.containsKey(responseInfo.systemNo)) {

					OrderResponseInfo orderInfo = weituoInfoMap.get(responseInfo.systemNo);

					orderInfo.filledNumber = responseInfo.filledNumber;
					orderInfo.orderNumber = responseInfo.orderNumber;
					orderInfo.orderPrice = responseInfo.orderPrice;
					orderInfo.triggerPrice = responseInfo.triggerPrice;
				}

				if (guadanInfoMap.containsKey(responseInfo.systemNo)) {

					OrderResponseInfo orderInfo = guadanInfoMap.get(responseInfo.systemNo);

					orderInfo.filledNumber = responseInfo.filledNumber;
					orderInfo.orderNumber = responseInfo.orderNumber;
					orderInfo.orderPrice = responseInfo.orderPrice;
					orderInfo.triggerPrice = responseInfo.triggerPrice;
				}
			}
			// 改单失败
			else {
			}

			return;
		}
		// 委托
		else if (CommandCode.ORDERSEARCH.equals(netInfo.code)) {

			String[] weituoList = infoTSplit(netInfo.infoT);

			// 当委托列表为空时,分割出来的list长度为1,且为空
			if (StringUtils.isNotBlank(weituoList[0])) {

				for (int i = 0; i < weituoList.length; i++) {
					OrderResponseInfo orderResponseInfo = new OrderResponseInfo();
					orderResponseInfo.MyReadString(weituoList[i]);

					orderResponseInfo.orderState = Constants.tradeStatusByNum(orderResponseInfo.orderState);

					weituoInfoMap.put(orderResponseInfo.systemNo, orderResponseInfo);

					// 委托单中存在没有完全成交的挂单 放入挂单列表
					if (Constants.TRADE_STATUS_BUFEN.equals(orderResponseInfo.orderState)
							|| Constants.TRADE_STATUS_YIPAIDUI.equals(orderResponseInfo.orderState)
							|| Constants.TRADE_STATUS_YIQINGQIU.equals(orderResponseInfo.orderState)) {

						guadanInfoMap.put(orderResponseInfo.systemNo, orderResponseInfo);

					}
				}

			}

			return;
		}
		// 挂单查询
		else if (CommandCode.SearchGuaDan.equals(netInfo.code)) {

			String[] guadanList = infoTSplit(netInfo.infoT);

			// 当挂单列表为空时,分割出来的list长度为1,且为空
			if (StringUtils.isNotBlank(guadanList[0])) {

				for (int i = 0; i < guadanList.length; i++) {
					OrderResponseInfo orderResponseInfo = new OrderResponseInfo();
					orderResponseInfo.MyReadString(guadanList[i]);

					guadanInfoMap.put(orderResponseInfo.systemNo, orderResponseInfo);
				}

			}

			return;
		}
		// 持仓合计
		else if (CommandCode.SearchHoldTotal.equals(netInfo.code)) {

			String[] chicangList = infoTSplit(netInfo.infoT);

			// 当持仓列表为空时,分割出来的list长度为1,且为空
			if (StringUtils.isNotBlank(chicangList[0])) {
				synchronized (chicangInfoMap) {
					chicangInfoMap.clear();
					for (int i = 0; i < chicangList.length; i++) {
						OrderStatusInfo statusInfo = new OrderStatusInfo();
						statusInfo.MyReadString(chicangList[i]);

						// synchronized (chicangInfoMap) {
						updateChicangTotalByOrderStatusInfo(statusInfo);
						// }
					}
				}
			}

			return;
		}
		// 成交查询
		else if (CommandCode.FILLEDSEARCH.equals(netInfo.code)) {

			String[] chengjiaoList = infoTSplit(netInfo.infoT);

			// 当持仓列表为空时,分割出来的list长度为1,且为空
			if (StringUtils.isNotBlank(chengjiaoList[0])) {

				for (int i = 0; i < chengjiaoList.length; i++) {
					FilledResponseInfo filledResponseInfo = new FilledResponseInfo();
					filledResponseInfo.MyReadString(chengjiaoList[i]);

					// 将成交数据插入成交列表(明细)
					chengjiaoInfoMap.put(filledResponseInfo.filledNo, filledResponseInfo);

					// 更新成交合计
					FilledResponseInfo filledTotalInfo = new FilledResponseInfo();
					filledTotalInfo.MyReadString(chengjiaoList[i]);
					updateChengjiaoTotalInfo(filledTotalInfo);
				}

			}

			return;
		}
		// 资金查询
		else if (CommandCode.ACCOUNTSEARCH.equals(netInfo.code)) {

			String[] zijinList = infoTSplit(netInfo.infoT);

			// 当持仓列表为空时,分割出来的list长度为1,且为空
			if (StringUtils.isNotBlank(zijinList[0])) {
				// 对资金map加线程同步
				synchronized (zijinInfoMap) {
					zijinInfoMap.clear();
					for (int i = 0; i < zijinList.length; i++) {
						AccountResponseInfo info = new AccountResponseInfo();
						info.MyReadString(zijinList[i]);

						zijinInfoMap.put(info.accountNo, info);
					}

					updateBaseCurrenyByRate();
				}
			}

			return;
		}
		// 币种对应汇率
		else if (CommandCode.CURRENCYLIST.equals(netInfo.code)) {

			String[] bizhongList = infoTSplit(netInfo.infoT);

			// 每次登陆时清空一下币种信息
			// bizhongInfoMap.clear();

			// 当持仓列表为空时,分割出来的list长度为1,且为空
			if (bizhongList.length == 1 && bizhongList[0].isEmpty())
				return;

			for (int i = 0; i < bizhongList.length; i++) {
				// LogUtil.e("sky----------bizhongList-----i = " + i + " bizhong = " +
				// bizhongList[i]);
				CurrencyInfo currencyInfo = new CurrencyInfo();
				currencyInfo.MyReadString(bizhongList[i]);

				if (currencyInfo.isBase == 1)
					jibibizhong = currencyInfo;
				// bizhongInfoMap.put(currencyInfo.currencyNo,currencyInfo);
			}

			//
			// this.notifyObservers(new TraderTag(TraderTag.TRADER_TYPE_ACCOUNT));
		}
	}

	/**
	 * 根据CancelResponseInfo更新挂单和委托数据
	 *
	 * @param cancelResponseInfo
	 *            撤单返回数据
	 */
	private void updateGuadanWeituoByCancelInfo(CancelResponseInfo cancelResponseInfo) {
		if (weituoInfoMap.containsKey(cancelResponseInfo.systemNo)) {

			OrderResponseInfo info = weituoInfoMap.get(cancelResponseInfo.systemNo);

			int filledNumber = StringUtils.isBlank(info.filledNumber) ? 0 : Integer.parseInt(info.filledNumber);
			int orderNumber = StringUtils.isBlank(info.orderNumber) ? 0 : Integer.parseInt(info.orderNumber);

			// 如果成交量>0 && < 订单数 此时撤单是已撤余单状态,否则就是已撤单
			if (filledNumber != 0 && filledNumber < orderNumber) {
				info.orderState = Constants.TRADE_STATUS_YICHEYUDAN;
			} else {
				info.orderState = Constants.TRADE_STATUS_YICHEDAN;
			}
		}

		// 已撤单订单直接从挂单列表移除,如果没有系统号对应订单,remove不操作
		guadanInfoMap.remove(cancelResponseInfo.systemNo);
	}

	/**
	 * 根据OrderStatusInfo 更新委托信息
	 *
	 * @param netInfo
	 *            包含systemno的完整消息体
	 */
	private void updateWeituoInfoByOrderStatusInfo(NetInfo netInfo) {
		OrderResponseInfo orderResponseInfo = null;

		OrderStatusInfo orderStatusInfo = new OrderStatusInfo();
		orderStatusInfo.MyReadString(netInfo.infoT);

		OrderResponseInfo myInfo = weituoInfoMap.get(netInfo.systemCode);

		if (myInfo != null) {

			if (StringUtils.isBlank(myInfo.orderNo)) {
				// 委托数据存在的时候,但是没有订单号,此时的数据是下单保存的数据,需要用OrderStatusInfo来更新一下
				myInfo.orderNo = orderStatusInfo.orderNo;
				myInfo.filledNumber = orderStatusInfo.filledNumber;
				myInfo.filledPrice = orderStatusInfo.filledAdvPrice;
				myInfo.statusSeq = orderStatusInfo.status;
			}

			orderResponseInfo = myInfo;
		} else {
			orderResponseInfo = new OrderResponseInfo();

			orderResponseInfo.code = orderStatusInfo.contractNo;
			orderResponseInfo.orderNo = orderStatusInfo.orderNo;
			orderResponseInfo.orderNumber = orderStatusInfo.orderNumber;
			orderResponseInfo.filledNumber = orderStatusInfo.filledNumber;
			orderResponseInfo.filledPrice = orderStatusInfo.filledAdvPrice;
			orderResponseInfo.statusSeq = orderStatusInfo.status;
			orderResponseInfo.accountNo = orderStatusInfo.accountNo;

			weituoInfoMap.put(netInfo.systemCode, orderResponseInfo);
		}

		int orderResponseOrderNumber = Integer
				.parseInt(StringUtils.isBlank(orderResponseInfo.orderNumber) ? "0" : orderResponseInfo.orderNumber);
		int orderStatusOrderNumber = Integer
				.parseInt(StringUtils.isBlank(orderStatusInfo.orderNumber) ? "0" : orderStatusInfo.orderNumber);

		int orderResponseFilledNumber = Integer
				.parseInt(StringUtils.isBlank(orderResponseInfo.filledNumber) ? "0" : orderResponseInfo.filledNumber);
		int orderStatusFilledNumber = Integer
				.parseInt(StringUtils.isBlank(orderStatusInfo.filledNumber) ? "0" : orderStatusInfo.filledNumber);

		if (orderResponseOrderNumber == 0 && orderStatusOrderNumber != 0) {
			orderResponseInfo.orderNumber = orderStatusInfo.orderNumber;
		}

		if (orderResponseFilledNumber > orderStatusFilledNumber) {
			// 委托数据中的成交量已经大于状态信息中的已成交量了，就不用再处理
			return;
		}

		// -1顺序号的数据必须更新（改单撤单失败返回的状态数据顺序号为-1）
		if (orderStatusInfo.status == -1) {

		} else {
			// 委托数据中的状态顺序号已经比新来的状态顺序号大了或者已经是-1了就不用更新了
			if (orderResponseInfo.statusSeq == -1 || orderResponseInfo.statusSeq > orderStatusInfo.status) {
				return;
			}
		}

		orderResponseInfo.statusSeq = orderStatusInfo.status;

		// 成交数量一样的时候，要么是成交的时候又撤单了，要么撤单的时候又成交了
		if (orderResponseFilledNumber == orderStatusFilledNumber) {
			if (orderStatusFilledNumber == 0) {
				if ("1".equals(orderStatusInfo.isCanceled)) {
					orderResponseInfo.orderState = Constants.TRADE_STATUS_YICHEDAN;
				} else {
					orderResponseInfo.orderState = Constants.TRADE_STATUS_YIPAIDUI;
				}
			} else {
				// 成交数=下单数时是完全成交
				if (orderResponseFilledNumber == orderResponseOrderNumber) {
					orderResponseInfo.orderState = Constants.TRADE_STATUS_WANQUAN;
				}
				// 否则就是已撤余单
				else {
					if ("1".equals(orderStatusInfo.isCanceled)) {
						orderResponseInfo.orderState = Constants.TRADE_STATUS_YICHEYUDAN;
					}
				}
			}
		}
		// 成交数有更新
		else {
			orderResponseInfo.filledPrice = orderStatusInfo.filledAdvPrice;
			orderResponseInfo.filledNumber = orderStatusInfo.filledNumber;

			orderResponseFilledNumber = Integer.parseInt(
					StringUtils.isBlank(orderResponseInfo.filledNumber) ? "0" : orderResponseInfo.filledNumber);

			// 原来是已撤余单状态
			if (orderResponseInfo.orderState == Constants.TRADE_STATUS_YICHEYUDAN) {
				// 变完全成交了
				if (orderResponseFilledNumber == orderResponseOrderNumber) {
					orderResponseInfo.orderState = Constants.TRADE_STATUS_WANQUAN;
				}
			}
			// 原来是已撤单状态
			else if (orderResponseInfo.orderState == Constants.TRADE_STATUS_YICHEDAN) {
				// 变完全成交了
				if (orderResponseFilledNumber == orderResponseOrderNumber) {
					orderResponseInfo.orderState = Constants.TRADE_STATUS_WANQUAN;
				}
				// 变已撤余单了
				else if (orderStatusFilledNumber > 0) {
					orderResponseInfo.orderState = Constants.TRADE_STATUS_YICHEYUDAN;
				}
			}
			// 原来不是撤单
			else {
				// 新数据是撤单
				if (orderStatusInfo.isCanceled == "1") {
					// 变完全成交了
					if (orderResponseFilledNumber == orderResponseOrderNumber) {
						orderResponseInfo.orderState = Constants.TRADE_STATUS_WANQUAN;
					} else {
						if (orderStatusFilledNumber > 0) {
							orderResponseInfo.orderState = Constants.TRADE_STATUS_YICHEYUDAN;
						} else {
							orderResponseInfo.orderState = Constants.TRADE_STATUS_YICHEDAN;
						}
					}
				}
				// 新数据不是撤单
				else {
					// 变完全成交了
					if (orderResponseFilledNumber == orderResponseOrderNumber) {
						orderResponseInfo.orderState = Constants.TRADE_STATUS_WANQUAN;
					} else {
						orderResponseInfo.orderState = Constants.TRADE_STATUS_BUFEN;
					}
				}
			}
		}
	}

	/**
	 * 根据UpdateWeituoInfoByOrderStatusInfo函数更新出来的委托信息更新挂单数据
	 *
	 * @param info
	 *            委托数据
	 */
	private void updateGuadanByWeituoInfo(OrderResponseInfo info) {

		if (info == null || StringUtils.isBlank(info.systemNo))
			return;

		// 找到对应的是哪个挂单
		OrderResponseInfo guadanInfo = null;

		guadanInfo = guadanInfoMap.get(info.systemNo);

		// 需要生成挂单数据
		if (guadanInfo == null && !info.orderState.equals(Constants.TRADE_STATUS_WANQUAN)
				&& !info.orderState.equals(Constants.TRADE_STATUS_YICHEDAN)
				&& !info.orderState.equals(Constants.TRADE_STATUS_YICHEYUDAN)
				&& !info.orderState.equals(Constants.TRADE_STATUS_ZHILING_FAIL)
				&& !info.orderState.equals(Constants.TRADE_STATUS_ZIJIN_LESS)) {

			guadanInfoMap.put(info.systemNo, info);

		} else {

			if (!info.orderState.equals(Constants.TRADE_STATUS_WANQUAN)
					&& !info.orderState.equals(Constants.TRADE_STATUS_YICHEDAN)
					&& !info.orderState.equals(Constants.TRADE_STATUS_YICHEYUDAN)
					&& !info.orderState.equals(Constants.TRADE_STATUS_ZHILING_FAIL)
					&& !info.orderState.equals(Constants.TRADE_STATUS_ZIJIN_LESS)) {

				guadanInfo.orderState = info.orderState;
				guadanInfo.filledPrice = info.filledPrice;
				guadanInfo.filledNumber = info.filledNumber;
				guadanInfo.triggerPrice = info.triggerPrice;

			} else {
				guadanInfoMap.remove(info.systemNo);
			}
		}
	}

	/**
	 * 根据OrderStatusInfo 更新持仓信息
	 *
	 * @param responseInfo
	 */
	private void updateChicangTotalByOrderStatusInfo(OrderStatusInfo responseInfo) {
		try {

			OrderStatusInfo chicangTotal = null;

			chicangTotal = chicangInfoMap.get(responseInfo.contractNo);

			// 已经有数据了，做更新即可
			if (chicangTotal != null) {

				// 状态更新就不用更新了
				if (chicangTotal.status > responseInfo.status) {
					return;
				}

				chicangTotal.status = responseInfo.status;

				int buyHoldNumber = StringUtils.isBlank(responseInfo.buyHoldNumber) ? 0
						: Integer.parseInt(responseInfo.buyHoldNumber);
				int saleHoldNumber = StringUtils.isBlank(responseInfo.saleHoldNumber) ? 0
						: Integer.parseInt(responseInfo.saleHoldNumber);

				if (buyHoldNumber == 0 && saleHoldNumber == 0) {
					chicangInfoMap.remove(responseInfo.contractNo);
					return;
				} else {

					chicangTotal.buyHoldPrice = responseInfo.buyHoldPrice;
					chicangTotal.buyHoldNumber = responseInfo.buyHoldNumber;

					chicangTotal.saleHoldPrice = responseInfo.saleHoldPrice;
					chicangTotal.saleHoldNumber = responseInfo.saleHoldNumber;

					if (buyHoldNumber > 0) {
						chicangTotal.buySale = Constants.TRADE_BUYSALE_BUY;
						chicangTotal.buyHoldOpenPrice = responseInfo.buyHoldOpenPrice;
						// 添加持买保证金
						chicangTotal.holdMarginBuy = responseInfo.holdMarginBuy;
					}

					if (saleHoldNumber > 0) {
						chicangTotal.buySale = Constants.TRADE_BUYSALE_SALE;
						chicangTotal.saleHoldOpenPrice = responseInfo.saleHoldOpenPrice;
						// 添加持卖保证金
						chicangTotal.holdMarginSale = responseInfo.holdMarginSale;
					}
				}
			}
			// 需要插入新数据
			else {

				int buyHoldNumber = StringUtils.isBlank(responseInfo.buyHoldNumber) ? 0
						: Integer.parseInt(responseInfo.buyHoldNumber);
				int saleHoldNumber = StringUtils.isBlank(responseInfo.saleHoldNumber) ? 0
						: Integer.parseInt(responseInfo.saleHoldNumber);

				if (buyHoldNumber == 0 && saleHoldNumber == 0) {
					return;
				}

				chicangTotal = new OrderStatusInfo();

				chicangTotal.contractNo = responseInfo.contractNo;
				chicangTotal.exchangeNo = responseInfo.exchangeNo;
				chicangTotal.accountNo = responseInfo.accountNo;

				chicangTotal.buyHoldPrice = responseInfo.buyHoldPrice;
				chicangTotal.buyHoldNumber = responseInfo.buyHoldNumber;

				chicangTotal.saleHoldPrice = responseInfo.saleHoldPrice;
				chicangTotal.saleHoldNumber = responseInfo.saleHoldNumber;

				if (buyHoldNumber > 0) {
					chicangTotal.buySale = Constants.TRADE_BUYSALE_BUY;
					chicangTotal.buyHoldOpenPrice = responseInfo.buyHoldOpenPrice;
					// 添加持买保证金
					chicangTotal.holdMarginBuy = responseInfo.holdMarginBuy;
				}

				if (saleHoldNumber > 0) {
					chicangTotal.buySale = Constants.TRADE_BUYSALE_SALE;
					chicangTotal.saleHoldOpenPrice = responseInfo.saleHoldOpenPrice;
					// 添加持卖保证金
					chicangTotal.holdMarginSale = responseInfo.holdMarginSale;
				}

				chicangTotal.status = responseInfo.status;

				// 添加最新价或结算价到持仓map
				chicangTotal.currPrice = responseInfo.currPrice;

				chicangInfoMap.put(responseInfo.contractNo, chicangTotal);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 根据AccountInfo更新资金信息
	 *
	 * @param responseInfo
	 */
	private void updateLastAccountByAccountInfo(AccountInfo responseInfo) {
		try {

			AccountResponseInfo info = null;

			info = zijinInfoMap.get(responseInfo.accountNo);

			if (info != null) {

				// 资金状态不是最新就不作更新
				if (responseInfo.status == 0 || responseInfo.status <= info.status) {
					return;
				} else {
					info.status = responseInfo.status;
				}

				// 今可用
				info.todayCanUse = Double
						.parseDouble(StringUtils.isBlank(responseInfo.available.trim()) ? "0" : responseInfo.available);
				// 冻结资金
				info.freezenMoney = Double.parseDouble(
						StringUtils.isBlank(responseInfo.frozenDeposit.trim()) ? "0" : responseInfo.frozenDeposit);
				// 入金
				info.inMoney = Double
						.parseDouble(StringUtils.isBlank(responseInfo.inMoney.trim()) ? "0" : responseInfo.inMoney);
				// 出金
				info.outMoney = Double
						.parseDouble(StringUtils.isBlank(responseInfo.outMoney.trim()) ? "0" : responseInfo.outMoney);
				// 手续费
				info.commission = Double
						.parseDouble(StringUtils.isBlank(responseInfo.fee.trim()) ? "0" : responseInfo.fee);
				// 平仓盈亏
				info.floatingProfit = Double.parseDouble(
						StringUtils.isBlank(responseInfo.expiredProfit.trim()) ? "0" : responseInfo.expiredProfit);
				// 保证金
				info.margin = Double
						.parseDouble(StringUtils.isBlank(responseInfo.deposit.trim()) ? "0" : responseInfo.deposit);
				// 今结存
				info.todayAmount = Double
						.parseDouble(StringUtils.isBlank(responseInfo.money.trim()) ? "0" : responseInfo.money);
				// 维持保证金
				info.keepDeposit = Double.parseDouble(
						StringUtils.isBlank(responseInfo.keepDeposit.trim()) ? "0" : responseInfo.keepDeposit);
				// 未结平盈
				info.unaccountProfit = Double.parseDouble(
						StringUtils.isBlank(responseInfo.unaccountProfit.trim()) ? "0" : responseInfo.unaccountProfit);
				// 期权权利金
				info.royalty = Double
						.parseDouble(StringUtils.isBlank(responseInfo.royalty.trim()) ? "0" : responseInfo.royalty);
				// 客户权益
				info.todayBalance = Double.parseDouble(
						StringUtils.isBlank(responseInfo.todayTotal.trim()) ? "0" : responseInfo.todayTotal);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 更新成交合计map
	 */
	private void updateChengjiaoTotalInfo(FilledResponseInfo info) {
		if (info != null) {
			int totalnumber = 0;
			double totalprice = 0;
			double totalcommsion = 0;

			int number = 0;
			double price = 0;
			double commsion = 0;

			if (chengjiaoTotalInfoMap.containsKey(info.exchangeCode + info.code + info.buySale)) {
				FilledResponseInfo totalinfo = chengjiaoTotalInfoMap.get(info.exchangeCode + info.code + info.buySale);
				totalnumber = StringUtils.isBlank(totalinfo.filledNumber) ? 0
						: Integer.parseInt(totalinfo.filledNumber);
				totalprice = StringUtils.isBlank(totalinfo.filledPrice) ? 0 : Double.parseDouble(totalinfo.filledPrice);
				totalcommsion = StringUtils.isBlank(totalinfo.commsion) ? 0 : Double.parseDouble(totalinfo.commsion);

				number = StringUtils.isBlank(info.filledNumber) ? 0 : Integer.parseInt(info.filledNumber);
				price = StringUtils.isBlank(info.filledPrice) ? 0 : Double.parseDouble(info.filledPrice);
				commsion = StringUtils.isBlank(info.commsion) ? 0 : Double.parseDouble(info.commsion);

				totalinfo.filledNumber = String.valueOf(totalnumber + number);
				totalinfo.filledPrice = String.valueOf(totalprice + (number * price));
				totalinfo.commsion = String.valueOf(totalcommsion + commsion);
			} else {
				totalnumber = StringUtils.isBlank(info.filledNumber) ? 0 : Integer.parseInt(info.filledNumber);
				totalprice = StringUtils.isBlank(info.filledPrice) ? 0 : Double.parseDouble(info.filledPrice);
				info.filledPrice = String.valueOf(totalprice * totalnumber);
				chengjiaoTotalInfoMap.put(info.exchangeCode + info.code + info.buySale, info);
			}

		}
	}

	/**
	 * 根据币种汇率和账户币种计算基币数据
	 */
	private void updateBaseCurrenyByRate() {

		if (zijinInfoMap.size() == 0)
			return;

		try {

			jibiInfo = new AccountResponseInfo();

			jibiInfo.accountNo = "基币";
			jibiInfo.currencyName = "基币";

			if (StringUtils.isNotBlank(jibibizhong.currencyNo))
				jibiInfo.currencyNo = "基币-" + jibibizhong.currencyNo;

			Iterator<Entry<String, AccountResponseInfo>> iterator = zijinInfoMap.entrySet().iterator();

			while (iterator.hasNext()) {
				Map.Entry<String, AccountResponseInfo> entry = (Map.Entry<String, AccountResponseInfo>) iterator.next();
				AccountResponseInfo info = entry.getValue();

				double currencyRate = Double
						.parseDouble(StringUtils.isBlank(info.currencyRate) ? "0" : info.currencyRate);

				if (StringUtils.isBlank(jibibizhong.currencyNo) && currencyRate == 1)
					jibiInfo.currencyNo = "基币-" + info.currencyNo;

				jibiInfo.todayBalance += ArithDecimal.mul(info.todayBalance, currencyRate);
				jibiInfo.todayCanUse += ArithDecimal.mul(info.todayCanUse, currencyRate);
				jibiInfo.margin += ArithDecimal.mul(info.margin, currencyRate);
				jibiInfo.floatingProfit += ArithDecimal.mul(info.floatingProfit, currencyRate);
				jibiInfo.inMoney += ArithDecimal.mul(info.inMoney, currencyRate);
				jibiInfo.outMoney += ArithDecimal.mul(info.outMoney, currencyRate);
				jibiInfo.riskRate += ArithDecimal.mul(info.riskRate, currencyRate);
				jibiInfo.keepDeposit += ArithDecimal.mul(info.keepDeposit, currencyRate);
				jibiInfo.oldCanUse += ArithDecimal.mul(info.oldCanUse, currencyRate);
				jibiInfo.oldBalance += ArithDecimal.mul(info.oldBalance, currencyRate);
				jibiInfo.oldAmount += ArithDecimal.mul(info.oldAmount, currencyRate);
				jibiInfo.todayAmount += ArithDecimal.mul(info.todayAmount, currencyRate);
				jibiInfo.freezenMoney += ArithDecimal.mul(info.freezenMoney, currencyRate);
				jibiInfo.profitRate += ArithDecimal.mul(info.profitRate, currencyRate);
				jibiInfo.unexpiredProfit += ArithDecimal.mul(info.unexpiredProfit, currencyRate);
				jibiInfo.unaccountProfit += ArithDecimal.mul(info.unaccountProfit, currencyRate);
				jibiInfo.royalty += ArithDecimal.mul(info.royalty, currencyRate);
				jibiInfo.netProfit += ArithDecimal.mul(info.netProfit, currencyRate);
				jibiInfo.credit += ArithDecimal.mul(info.credit, currencyRate);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

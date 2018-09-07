package com.zd.engine.main;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shanghaizhida.beans.CommandCode;
import com.shanghaizhida.beans.LoginInfo;
import com.shanghaizhida.beans.NetInfo;
import com.zd.config.Global;
import com.zd.constant.RespMessage;
import com.zd.constant.TraderEnvEnum;
import com.zd.engine.event.ZdEventDynamicHandlerAbstract;
import com.zd.mapper.TraderMapper;

import xyz.redtorch.trader.entity.Order;
import xyz.redtorch.trader.gateway.GatewaySetting;

public class OrderEventHandler extends ZdEventDynamicHandlerAbstract<OrderEvent> {

	private static final Logger logger = LoggerFactory.getLogger(OrderEventHandler.class);

	@Override
	public void onEvent(OrderEvent event, long sequence, boolean endOfBatch) throws Exception {
		try {
			NetInfo ni = event.getNetInfo();
			if (ni != null) {
//				String accountKey = ni.localSystemCode + "-" + ni.accountNo;
				// 登录
				if (CommandCode.LOGIN.equals(ni.code)) {
					LoginInfo login = new LoginInfo();
					login.MyReadString(ni.infoT);
					if (TraderEnvEnum.CTP.getCode().equals(ni.exchangeCode)) {
						GatewaySetting gatewaySetting = TraderMapper.accountTraderCTPMap.get(ni.accountNo);
						if (gatewaySetting == null) {
							gatewaySetting = new GatewaySetting();
							gatewaySetting.setBrokerID(Global.brokerId);
							gatewaySetting.setGatewayClassName(Global.gatewayClassName);
							gatewaySetting.setGatewayDisplayName(UUID.randomUUID().toString().replace("-", ""));
							gatewaySetting.setGatewayID(ni.localSystemCode + "-" + ni.accountNo);
							gatewaySetting.setMdAddress(Global.ctpMdAddress);
							gatewaySetting.setPassword(login.userPwd);
							gatewaySetting.setTdAddress(Global.ctpTdAddress);
							gatewaySetting.setUserID(login.userId);
							Global.tradingService.saveOrUpdateGatewaySetting(gatewaySetting);
							Global.tradingService.changeGatewayConnectStatus(gatewaySetting.getGatewayID());
						}
					}
				} else if (CommandCode.ORDER.equals(ni.code)) {
					// 下单
					if (TraderEnvEnum.CTP.getCode().equals(ni.exchangeCode)) {
						Order order = new Order();
						order.MyReadString(ni.infoT);
						GatewaySetting gatewaySetting = TraderMapper.accountTraderCTPMap.get(ni.accountNo);
						if (gatewaySetting != null) {
							order.setGatewayID(gatewaySetting.getGatewayID());
							Global.tradingService.sendOrder(order.getGatewayID(), order.getSymbol(), order.getPrice(),
									order.getTotalVolume(), order.getPriceType(), order.getDirection(),
									order.getOffset(),order.getOrderID());
						} else {
							ni.code = CommandCode.CTPERROR;
							ni.infoT = RespMessage.CTPUNLOGIN;
							TraderMapper.traderInfoQueue.add(ni.MyToString());
						}
					}
				} else if (CommandCode.CANCEL.equals(ni.code)) {
					// 撤单
					if (TraderEnvEnum.CTP.getCode().equals(ni.exchangeCode)) {
						Order order = new Order();
						order.MyReadString(ni.infoT);
						GatewaySetting gatewaySetting = TraderMapper.accountTraderCTPMap.get(ni.accountNo);
						if (gatewaySetting != null) {
							Global.tradingService.cancelOrder(order.getRtOrderID());
						} else {
							ni.code = CommandCode.CTPERROR;
							ni.infoT = RespMessage.CTPUNLOGIN;
							TraderMapper.traderInfoQueue.add(ni.MyToString());
						}
					}
				}else if(CommandCode.MODIFY.equals(ni.code)) {
					if (TraderEnvEnum.CTP.getCode().equals(ni.exchangeCode)) {
						ni.code = CommandCode.CTPERROR;
						ni.infoT = RespMessage.COMMANDERROR;
						TraderMapper.traderInfoQueue.add(ni.MyToString());
					}
				}
			}

		} catch (Exception e) {
			logger.error("消费者接收数据异常：{}", e);
		}
	}

}

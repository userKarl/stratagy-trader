package com.zd.business.engine.main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shanghaizhida.beans.CommandCode;
import com.shanghaizhida.beans.LoginInfo;
import com.shanghaizhida.beans.NetInfo;
import com.shanghaizhida.beans.OrderInfo;
import com.zd.business.constant.RespMessage;
import com.zd.business.constant.TraderEnvEnum;
import com.zd.business.engine.event.ZdEventDynamicHandlerAbstract;
import com.zd.business.service.TraderDataFeed;
import com.zd.config.Global;

import xyz.redtorch.trader.entity.Order;
import xyz.redtorch.trader.gateway.GatewaySetting;

public class OrderEventHandler extends ZdEventDynamicHandlerAbstract<OrderEvent> {

	private static final Logger logger = LoggerFactory.getLogger(OrderEventHandler.class);

	@Override
	public void onEvent(OrderEvent event, long sequence, boolean endOfBatch) throws Exception {
		try {
			NetInfo ni = event.getNetInfo();
			if (ni != null) {
				String accountKey = ni.localSystemCode + "-" + ni.accountNo;
				// 登录
				if (CommandCode.LOGIN.equals(ni.code)) {
					LoginInfo login = new LoginInfo();
					login.MyReadString(ni.infoT);
					// 判断该账户交易环境
					if (TraderEnvEnum.ZD.getCode().equals(ni.systemCode)) {
						// 判断该账户是否已经登录
						TraderDataFeed tdf = Global.accountTraderMap.get(accountKey);
						if (tdf != null) {
							tdf.stop();
						}
						tdf = new TraderDataFeed(Global.zdTraderHost, Global.zdTraderPort, login.userId, login.userPwd,
								ni.localSystemCode, ni.accountNo);
						Global.accountTraderMap.put(accountKey, tdf);
						tdf.start();
					} else if (TraderEnvEnum.CTP.getCode().equals(ni.systemCode)) {
						GatewaySetting gatewaySetting = Global.accountTraderCTPMap.get(accountKey);
						if (gatewaySetting == null) {
							gatewaySetting = new GatewaySetting();
							gatewaySetting.setBrokerID(Global.brokerId);
							gatewaySetting.setGatewayClassName(Global.gatewayClassName);
							gatewaySetting.setGatewayDisplayName(ni.localSystemCode);
							gatewaySetting.setGatewayID(accountKey);
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

					if (TraderEnvEnum.ZD.getCode().equals(ni.systemCode)) {
						OrderInfo oi = new OrderInfo();
						oi.MyReadString(ni.infoT);
						// 判断该账户是否已经登录
						TraderDataFeed tdf = Global.accountTraderMap.get(accountKey);
						if (tdf != null) {
							tdf.sendOrder(oi);
						} else {
							ni.code = CommandCode.CFLOGINERROR;
							ni.infoT = RespMessage.UNLOGIN;
							Global.traderInfoQueue.add(ni.MyToString());
						}
					} else if (TraderEnvEnum.CTP.getCode().equals(ni.systemCode)) {
						Order order = new Order();
						order.MyReadString(ni.infoT);
						GatewaySetting gatewaySetting = Global.accountTraderCTPMap.get(accountKey);
						if (gatewaySetting != null) {
							order.setGatewayID(gatewaySetting.getGatewayID());
							Global.tradingService.sendOrder(order.getGatewayID(), order.getSymbol(), order.getPrice(),
									order.getTotalVolume(), order.getPriceType(), order.getDirection(),
									order.getOffset());
						} else {
							ni.code = CommandCode.CTPERROR;
							ni.infoT = RespMessage.CTPUNLOGIN;
							Global.traderInfoQueue.add(ni.MyToString());
						}
					}
				} else if (CommandCode.CANCEL.equals(ni.code)) {
					// 撤单
					if (TraderEnvEnum.ZD.getCode().equals(ni.systemCode)) {

					} else if (TraderEnvEnum.CTP.getCode().equals(ni.systemCode)) {
						Order order = new Order();
						order.MyReadString(ni.infoT);
						GatewaySetting gatewaySetting = Global.accountTraderCTPMap.get(accountKey);
						if (gatewaySetting != null) {
							Global.tradingService.cancelOrder(order.getRtOrderID());
						} else {
							ni.code = CommandCode.CTPERROR;
							ni.infoT = RespMessage.CTPUNLOGIN;
							Global.traderInfoQueue.add(ni.MyToString());
						}
					}
				}
			}

		} catch (Exception e) {
			logger.error("消费者接收数据异常：{}", e);
		}
	}

}

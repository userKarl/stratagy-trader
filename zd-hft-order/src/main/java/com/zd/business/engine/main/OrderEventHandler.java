package com.zd.business.engine.main;

import java.util.UUID;

import com.shanghaizhida.beans.CancelInfo;
import com.shanghaizhida.beans.CommandCode;
import com.shanghaizhida.beans.LoginInfo;
import com.shanghaizhida.beans.ModifyInfo;
import com.shanghaizhida.beans.NetInfo;
import com.shanghaizhida.beans.OrderInfo;
import com.zd.business.constant.RespMessage;
import com.zd.business.constant.TraderEnvEnum;
import com.zd.business.engine.event.ZdEventDynamicHandlerAbstract;
import com.zd.business.service.TraderDataFeed;
import com.zd.config.Global;
import com.zd.mapper.TraderMapper;

import lombok.extern.slf4j.Slf4j;
import xyz.redtorch.trader.entity.Order;
import xyz.redtorch.trader.gateway.GatewaySetting;

@Slf4j
public class OrderEventHandler extends ZdEventDynamicHandlerAbstract<OrderEvent> {

	@Override
	public void onEvent(OrderEvent event, long sequence, boolean endOfBatch) throws Exception {
		try {
			NetInfo ni = event.getNetInfo();
			if (ni != null) {
				// 登录
				if (CommandCode.LOGIN.equals(ni.code)) {
					LoginInfo login = new LoginInfo();
					login.MyReadString(ni.infoT);
					// 判断该账户交易环境
					if (TraderEnvEnum.ZD.getCode().equals(ni.exchangeCode)) {
						// 判断该账户是否已经登录
						TraderDataFeed tdf = TraderMapper.accountTraderMap.get(ni.accountNo);
						if (tdf != null) {
							tdf.stop();
						}
						tdf = new TraderDataFeed(Global.zdTraderHost, Global.zdTraderPort, login.userId, login.userPwd,
								ni.accountNo, ni.localSystemCode);
						TraderMapper.accountTraderMap.put(ni.accountNo, tdf);
						tdf.start();
					} else if (TraderEnvEnum.CTP.getCode().equals(ni.exchangeCode)) {
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
					} else if (TraderEnvEnum.STOCK.getCode().equals(ni.exchangeCode)) {
						// TODO
					}
				} else if (CommandCode.ORDER.equals(ni.code)) {
					// 下单

					if (TraderEnvEnum.ZD.getCode().equals(ni.exchangeCode)) {
						OrderInfo oi = new OrderInfo();
						oi.MyReadString(ni.infoT);
						// 判断该账户是否已经登录
						TraderDataFeed tdf = TraderMapper.accountTraderMap.get(ni.accountNo);
						if (tdf != null) {
							tdf.sendOrder(oi);
						} else {
							ni.code = CommandCode.CFLOGINERROR;
							ni.infoT = RespMessage.UNLOGIN;
							TraderMapper.traderInfoQueue.add(ni.MyToString());
						}
					} else if (TraderEnvEnum.CTP.getCode().equals(ni.exchangeCode)) {
						Order order = new Order();
						order.MyReadString(ni.infoT);
						GatewaySetting gatewaySetting = TraderMapper.accountTraderCTPMap.get(ni.accountNo);
						if (gatewaySetting != null) {
							order.setGatewayID(gatewaySetting.getGatewayID());
							Global.tradingService.sendOrder(order.getGatewayID(), order.getSymbol(), order.getPrice(),
									order.getTotalVolume(), order.getPriceType(), order.getDirection(),
									order.getOffset(), order.getOrderID());
						} else {
							ni.code = CommandCode.CTPERROR;
							ni.infoT = RespMessage.CTPUNLOGIN;
							TraderMapper.traderInfoQueue.add(ni.MyToString());
						}
					} else if (TraderEnvEnum.STOCK.getCode().equals(ni.exchangeCode)) {
						// TODO
					}
				} else if (CommandCode.CANCEL.equals(ni.code)) {
					// 撤单
					if (TraderEnvEnum.ZD.getCode().equals(ni.exchangeCode)) {
						CancelInfo cancelInfo = new CancelInfo();
						cancelInfo.MyReadString(ni.infoT);
						TraderDataFeed tdf = TraderMapper.accountTraderMap.get(ni.accountNo);
						if (tdf != null) {
							tdf.sendCancel(cancelInfo);
						} else {
							ni.code = CommandCode.CFLOGINERROR;
							ni.infoT = RespMessage.UNLOGIN;
							TraderMapper.traderInfoQueue.add(ni.MyToString());
						}
					} else if (TraderEnvEnum.CTP.getCode().equals(ni.exchangeCode)) {
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
					} else if (TraderEnvEnum.STOCK.getCode().equals(ni.exchangeCode)) {
						// TODO
					}
				} else if (CommandCode.MODIFY.equals(ni.code)) {
					// 改单
					if (TraderEnvEnum.ZD.getCode().equals(ni.exchangeCode)) {
						ModifyInfo modifyInfo = new ModifyInfo();
						modifyInfo.MyReadString(ni.infoT);
						TraderDataFeed tdf = TraderMapper.accountTraderMap.get(ni.accountNo);
						if (tdf != null) {
							tdf.sendModify(modifyInfo, ni.localSystemCode, ni.systemCode);
						} else {
							ni.code = CommandCode.CFLOGINERROR;
							ni.infoT = RespMessage.UNLOGIN;
							TraderMapper.traderInfoQueue.add(ni.MyToString());
						}
					} else if (TraderEnvEnum.CTP.getCode().equals(ni.exchangeCode)) {
						ni.code = CommandCode.CTPERROR;
						ni.infoT = RespMessage.COMMANDERROR;
						TraderMapper.traderInfoQueue.add(ni.MyToString());
					} else if (TraderEnvEnum.STOCK.getCode().equals(ni.exchangeCode)) {
						// TODO
					}
				}
			}

		} catch (Exception e) {
			log.error("消费者接收数据异常：{}", e);
		}
	}

}

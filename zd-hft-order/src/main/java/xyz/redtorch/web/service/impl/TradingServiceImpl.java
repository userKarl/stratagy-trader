package xyz.redtorch.web.service.impl;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.shanghaizhida.beans.CommandCode;
import com.shanghaizhida.beans.NetInfo;
import com.zd.business.constant.TraderEnvEnum;
import com.zd.config.Global;

import xyz.redtorch.trader.base.RtConstant;
import xyz.redtorch.trader.engine.event.EventConstant;
import xyz.redtorch.trader.engine.event.FastEvent;
import xyz.redtorch.trader.engine.event.FastEventDynamicHandler;
import xyz.redtorch.trader.engine.event.FastEventDynamicHandlerAbstract;
import xyz.redtorch.trader.engine.event.FastEventEngine;
import xyz.redtorch.trader.engine.main.MainEngine;
import xyz.redtorch.trader.engine.main.impl.MainEngineImpl;
import xyz.redtorch.trader.entity.Account;
import xyz.redtorch.trader.entity.CancelOrderReq;
import xyz.redtorch.trader.entity.Contract;
import xyz.redtorch.trader.entity.LocalPositionDetail;
import xyz.redtorch.trader.entity.LogData;
import xyz.redtorch.trader.entity.Order;
import xyz.redtorch.trader.entity.OrderReq;
import xyz.redtorch.trader.entity.Position;
import xyz.redtorch.trader.entity.SubscribeReq;
import xyz.redtorch.trader.entity.Tick;
import xyz.redtorch.trader.entity.Trade;
import xyz.redtorch.trader.gateway.Gateway;
import xyz.redtorch.trader.gateway.GatewaySetting;
import xyz.redtorch.utils.CommonUtil;
import xyz.redtorch.web.service.TradingService;

/**
 * @author sun0x00@gmail.com
 */
@Service
public class TradingServiceImpl implements TradingService {

	private Logger log = LoggerFactory.getLogger(TradingServiceImpl.class);

	// 使用无大小限制的线程池,线程空闲60s会被释放
	ExecutorService executor = Executors.newCachedThreadPool();

	private MainEngine mainEngine = new MainEngineImpl();

	public TradingServiceImpl() {
		EventTransferTask eventTransferTask = new EventTransferTask();

		FastEventEngine.addHandler(mainEngine);
		FastEventEngine.addHandler(eventTransferTask);

	}

	@Override
	public String sendOrder(String gatewayID, String rtSymbol, double price, int volume, String priceType,
			String direction, String offset) {

		Contract contract = mainEngine.getContract(rtSymbol, gatewayID);
		if (contract != null) {
			OrderReq orderReq = new OrderReq();
			orderReq.setSymbol(contract.getSymbol());
			orderReq.setExchange(contract.getExchange());
			orderReq.setRtSymbol(contract.getRtSymbol());
			orderReq.setPrice(CommonUtil.rountToPriceTick(contract.getPriceTick(), price));
			orderReq.setVolume(volume);
			orderReq.setGatewayID(gatewayID);
			orderReq.setDirection(direction);
			orderReq.setOffset(offset);
			orderReq.setPriceType(priceType);

			return mainEngine.sendOrder(orderReq);
		} else {
			log.error("发单失败,未找到合约");
			return null;
		}

	}

	@Override
	public void cancelOrder(String rtOrderID) {

		Order order = mainEngine.getOrder(rtOrderID);
		if (order != null) {
			if (!RtConstant.STATUS_FINISHED.contains(order.getStatus())) {

				CancelOrderReq cancelOrderReq = new CancelOrderReq();

				cancelOrderReq.setSymbol(order.getSymbol());
				cancelOrderReq.setExchange(order.getExchange());

				cancelOrderReq.setFrontID(order.getFrontID());
				cancelOrderReq.setSessionID(order.getSessionID());
				cancelOrderReq.setOrderID(order.getOrderID());
				cancelOrderReq.setGatewayID(order.getGatewayID());

				mainEngine.cancelOrder(cancelOrderReq);

			}
		} else {
			log.error("无法撤单,未能找到委托ID {}", rtOrderID);
		}
	}

	@Override
	public void cancelAllOrders() {

		for (Order order : mainEngine.getWorkingOrders()) {
			System.out.println(order.getStatus());
			if (!RtConstant.STATUS_FINISHED.contains(order.getStatus())) {

				CancelOrderReq cancelOrderReq = new CancelOrderReq();

				cancelOrderReq.setSymbol(order.getSymbol());
				cancelOrderReq.setExchange(order.getExchange());

				cancelOrderReq.setFrontID(order.getFrontID());
				cancelOrderReq.setSessionID(order.getSessionID());
				cancelOrderReq.setOrderID(order.getOrderID());
				cancelOrderReq.setGatewayID(order.getGatewayID());

				mainEngine.cancelOrder(cancelOrderReq);

			}
		}
	}

	@Override
	public boolean subscribe(String rtSymbol, String gatewayID) {
		if (StringUtils.isEmpty(rtSymbol)) {
			return false;
		}
		SubscribeReq subscribeReq = new SubscribeReq();
		subscribeReq.setGatewayID(gatewayID);
		subscribeReq.setRtSymbol(rtSymbol);
		return mainEngine.subscribe(subscribeReq, "web-page-00");
	}

	@Override
	public boolean unsubscribe(String rtSymbol, String gatewayID) {
		SubscribeReq subscribeReq = new SubscribeReq();
		subscribeReq.setGatewayID(gatewayID);
		subscribeReq.setRtSymbol(rtSymbol);
		return mainEngine.unsubscribe(rtSymbol, gatewayID, "web-page-00");
	}

	@Override
	public List<Trade> getTrades() {
		return mainEngine.getTrades();
	}

	@Override
	public List<Order> getOrders() {
		return mainEngine.getOrders();
	}

	@Override
	public List<LocalPositionDetail> getLocalPositionDetails() {
		return mainEngine.getLocalPositionDetails();
	}

	@Override
	public List<Position> getPositions() {
		return mainEngine.getPositions();
	}

	@Override
	public List<Account> getAccounts() {
		return mainEngine.getAccounts();
	}

	@Override
	public List<Contract> getContracts() {
		return mainEngine.getContracts();
	}

	@Override
	public List<GatewaySetting> getGatewaySettings() {
		List<GatewaySetting> gatewaySettings = mainEngine.queryGatewaySettings();
		if (gatewaySettings != null) {
			for (GatewaySetting gatewaySetting : gatewaySettings) {
				Gateway gateway = mainEngine.getGateway(gatewaySetting.getGatewayID());

				gatewaySetting.setRuntimeStatus(false);
				if (gateway != null) {
					if (gateway.isConnected()) {
						gatewaySetting.setRuntimeStatus(true);
					}
				}
			}
		}
		return gatewaySettings;
	}

	@Override
	public void deleteGateway(String gatewayID) {
		mainEngine.deleteGateway(gatewayID);
	}

	@Override
	public void changeGatewayConnectStatus(String gatewayID) {
		Gateway gateway = mainEngine.getGateway(gatewayID);
		if (gateway != null) {
			if (gateway.isConnected()) {
				mainEngine.disconnectGateway(gatewayID);
			} else {
				gateway.connect();
			}
		} else {
			mainEngine.connectGateway(gatewayID);
		}

	}

	@Override
	public void saveOrUpdateGatewaySetting(GatewaySetting gatewaySetting) {
		if (StringUtils.isEmpty(gatewaySetting.getGatewayID())) {
			String[] tdAddressArray = gatewaySetting.getTdAddress().split("\\.");
			String tdAddressSuffix = tdAddressArray[tdAddressArray.length - 1].replaceAll(":", "\\.");
			gatewaySetting.setGatewayID(gatewaySetting.getBrokerID() + "." + gatewaySetting.getGatewayDisplayName()
					+ "." + tdAddressSuffix);
		} else {
			mainEngine.deleteGateway(gatewaySetting.getGatewayID());
		}
		mainEngine.saveGateway(gatewaySetting);
	}

	@Override
	public List<LogData> getLogDatas() {
		return mainEngine.getLogDatas();
	}

	class EventTransferTask extends FastEventDynamicHandlerAbstract implements FastEventDynamicHandler {

		public EventTransferTask() {
			subscribeEvent(EventConstant.EVENT_TICK);
			subscribeEvent(EventConstant.EVENT_TRADE);
			subscribeEvent(EventConstant.EVENT_ORDER);
			subscribeEvent(EventConstant.EVENT_POSITION);
			subscribeEvent(EventConstant.EVENT_ACCOUNT);
			subscribeEvent(EventConstant.EVENT_CONTRACT);
			subscribeEvent(EventConstant.EVENT_ERROR);
			subscribeEvent(EventConstant.EVENT_GATEWAY);
			subscribeEvent(EventConstant.EVENT_LOG);
			subscribeEvent(EventConstant.EVENT_LOG + "ZEUS|");
		}

		@Override
		public void onEvent(final FastEvent fastEvent, final long sequence, final boolean endOfBatch) throws Exception {

			if (!subscribedEventSet.contains(fastEvent.getEvent())) {
				return;
			}
			// 判断消息类型
			// 使用复杂的对比判断逻辑,便于扩展修改

			NetInfo ni = new NetInfo();
			ni.exchangeCode = TraderEnvEnum.CTP.getCode();

			if (EventConstant.EVENT_TICK.equals(fastEvent.getEventType())) {
				try {
					Tick tick = fastEvent.getTick();
				} catch (Exception e) {
					log.error("向客户端转发Tick发生异常!!!", e);
				}
			} else if (EventConstant.EVENT_TRADE.equals(fastEvent.getEventType())) {
				try {
					Trade trade = fastEvent.getTrade();
					ni.code = CommandCode.CTPTRADE;
					String[] split = trade.getGatewayID().split("-");
					ni.localSystemCode = split[0];
					ni.accountNo = split[1];
					ni.infoT = trade.MyToString();
					Global.traderInfoQueue.add(ni.MyToString());
				} catch (Exception e) {
					log.error("向客户端转发Trade发生异常!!!", e);
				}
			} else if (EventConstant.EVENT_ORDER.equals(fastEvent.getEventType())) {
				try {
					Order order = fastEvent.getOrder();
					ni.code = CommandCode.CTPORDER;
					String[] split = order.getGatewayID().split("-");
					ni.localSystemCode = split[0];
					ni.accountNo = split[1];
					ni.infoT = order.MyToString();
					Global.traderInfoQueue.add(ni.MyToString());
				} catch (Exception e) {
					log.error("向客户端转发Order发生异常!!!", e);
				}
			} else if (EventConstant.EVENT_CONTRACT.equals(fastEvent.getEventType())) {
				try {
					Contract contract = fastEvent.getContract();
				} catch (Exception e) {
					log.error("向客户端转发Contract发生异常!!!", e);
				}
			} else if (EventConstant.EVENT_POSITION.equals(fastEvent.getEventType())) {
				try {
					Position position = fastEvent.getPosition();
					ni.code = CommandCode.CTPPOSITION;
					String[] split = position.getGatewayID().split("-");
					ni.localSystemCode = split[0];
					ni.accountNo = split[1];
					ni.infoT = position.MyToString();
					Global.traderInfoQueue.add(ni.MyToString());
				} catch (Exception e) {
					log.error("向客户端转发Position发生异常!!!", e);
				}
			} else if (EventConstant.EVENT_ACCOUNT.equals(fastEvent.getEventType())) {
				try {
					Account account = fastEvent.getAccount();
					ni.code = CommandCode.CTPACCOUNT;
					String[] split = account.getGatewayID().split("-");
					ni.localSystemCode = split[0];
					ni.accountNo = split[1];
					ni.infoT = account.MyToString();
					Global.traderInfoQueue.add(ni.MyToString());
				} catch (Exception e) {
					log.error("向客户端转发Account发生异常!!!", e);
				}
			} else if (EventConstant.EVENT_LOG.equals(fastEvent.getEventType())) {
				try {
					LogData logData = fastEvent.getLogData();
					// 发送所有日志
				} catch (Exception e) {
					log.error("向客户端转发Order发生异常!!!", e);
				}
			} else {
				log.warn("主引擎未能识别的事件数据类型{}", JSON.toJSONString(fastEvent.getEvent()));
			}
		}

		@Override
		public void onStart() {

		}

		@Override
		public void onShutdown() {
			shutdownLatch.countDown();
		}

	}

}

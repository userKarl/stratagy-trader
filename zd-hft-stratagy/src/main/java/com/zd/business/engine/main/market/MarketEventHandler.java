package com.zd.business.engine.main.market;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import com.shanghaizhida.beans.MarketInfo;
import com.shanghaizhida.beans.NetInfo;
import com.zd.business.common.BaseService;
import com.zd.business.common.BeanUtils;
import com.zd.business.constant.CommandEnum;
import com.zd.business.constant.TraderEnvEnum;
import com.zd.business.engine.event.ZdEventDynamicHandlerAbstract;
import com.zd.business.entity.ctp.Tick;
import com.zd.business.mapper.TraderMapper;
import com.zd.business.service.thread.HandlerStratagyThread;
import com.zd.config.Global;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MarketEventHandler extends ZdEventDynamicHandlerAbstract<MarketEvent> {

	private HandlerStratagyThread handlerStratagyThread;

	private BaseService baseService;

	private Set<String> subscribedEventSet = new HashSet<>();

	public MarketEventHandler(BaseService baseService) {
		this.baseService = baseService;
	}

	public void init() {
		String id = UUID.randomUUID().toString().replace("-", "");
		this.handlerStratagyThread = new HandlerStratagyThread(id, baseService);
		this.handlerStratagyThread.start();
		TraderMapper.handlerStratagyThreadMap.put(id, this.handlerStratagyThread);
	}

	@Override
	public void onEvent(MarketEvent event, long sequence, boolean endOfBatch) throws Exception {
		try {
			// log.info("策略消费者接收到的数据：{}", event.getMarketInfo());
			String exchangeCode = "", code = "";
			NetInfo ni = new NetInfo();
			ni.MyReadString(event.getNetInfo());
			if (TraderEnvEnum.ZD.getCode().equals(ni.exchangeCode)
					|| TraderEnvEnum.STOCK.getCode().equals(ni.exchangeCode)) {
				MarketInfo marketInfo = new MarketInfo();
				marketInfo.MyReadString(ni.infoT);
				exchangeCode = marketInfo.exchangeCode;
				code = marketInfo.code;
			} else if (TraderEnvEnum.CTP.getCode().equals(ni.exchangeCode)) {
				Tick tick = new Tick();
				tick.MyReadString(ni.infoT);
				exchangeCode = tick.getExchange();
				code = tick.getSymbol();
				handlerCTPMarket(tick);
			}
			for (String s : subscribedEventSet) {
				if (s.equals(exchangeCode + "@" + code)) {
					handlerStratagyThread.getMarketQueue()
							.add(CommandEnum.STRATAGY_CACL.toString() + "-" + System.nanoTime());
				}
			}
		} catch (Exception e) {
			log.error("策略消费者接收行情异常：{}", e);
		}
	}

	public HandlerStratagyThread getHandlerStratagyThread() {
		return handlerStratagyThread;
	}

	public void setHandlerStratagyThread(HandlerStratagyThread handlerStratagyThread) {
		this.handlerStratagyThread = handlerStratagyThread;
	}

	public void handlerCTPMarket(Tick tick) {
		try {
			// 更新基本合约的行情数据
			String key = tick.getExchange() + "@" + tick.getSymbol();
			Tick tick2 = Global.ctpContractMap.get(key);
			if (tick2 != null) {
				BeanUtils.copyBeanNotNull2Bean(tick, tick2);
				Global.ctpContractMap.put(key, tick2);
			} else {
				Global.ctpContractMap.put(key, tick);
			}
		} catch (Exception e) {
			log.warn("更新CTP行情异常：{}", e);
		}
	}

	public Set<String> getSubscribedEventSet() {
		return subscribedEventSet;
	}

	public void subscribeEvent(String event) {
		subscribedEventSet.add(event);
	}

	public void unsubscribeEvent(String event) {
		subscribedEventSet.remove(event);

	}

	public BaseService getBaseService() {
		return baseService;
	}

	public void setBaseService(BaseService baseService) {
		this.baseService = baseService;
	}
}

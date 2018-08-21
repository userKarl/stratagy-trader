package com.zd.business.engine.main.market;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shanghaizhida.beans.MarketInfo;
import com.zd.business.engine.event.ZdEventDynamicHandlerAbstract;
import com.zd.business.service.thread.HandlerStratagyThread;

public class MarketEventHandler extends ZdEventDynamicHandlerAbstract<MarketEvent> {

	private static final Logger logger = LoggerFactory.getLogger(MarketEventHandler.class);

	private HandlerStratagyThread handlerStratagyThread;
	
	public void init() {
		this.handlerStratagyThread=new HandlerStratagyThread(UUID.randomUUID().toString());
		this.handlerStratagyThread.start();
	}

	@Override
	public void onEvent(MarketEvent event, long sequence, boolean endOfBatch) throws Exception {
		try {
			logger.info("策略消费者接收到的数据：{}", event.getMarketInfo());
			MarketInfo marketInfo = new MarketInfo();
			marketInfo.MyReadString(event.getMarketInfo());
			for(String s:subscribedEventSet) {
				if(s.equals(marketInfo.exchangeCode+"@"+marketInfo.code)) {
					handlerStratagyThread.getQueue().add(marketInfo);
				}
			}
		} catch (Exception e) {
			logger.error("策略消费者接收行情异常：{}", e);
		}
	}

	public HandlerStratagyThread getHandlerStratagyThread() {
		return handlerStratagyThread;
	}

	public void setHandlerStratagyThread(HandlerStratagyThread handlerStratagyThread) {
		this.handlerStratagyThread = handlerStratagyThread;
	}


}

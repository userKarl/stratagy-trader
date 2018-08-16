package com.zd.business.engine.main.market;

import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zd.business.engine.event.ZdEventDynamicHandlerAbstract;
import com.zd.business.entity.Stratagy;
import com.zd.config.Global;

public class MarketEventHandler extends ZdEventDynamicHandlerAbstract<MarketEvent>{
	
	private static final Logger logger = LoggerFactory.getLogger(MarketEventHandler.class);

	private String id;

	public MarketEventHandler(String id) {
		this.id = id;
	}

	// 策略容器
	private ConcurrentHashMap<String, Stratagy> stratagyConcurrentHashMap = new ConcurrentHashMap<>();


	@Override
	public void onEvent(MarketEvent event, long sequence, boolean endOfBatch) throws Exception {
		Global.orderEventProducer.onData(event.getNetInfo());
		logger.info("策略计算接收到的数据：{}",event.getNetInfo());
//		NetInfo ni=new NetInfo();
//		ni.code=CommandEnum.STRATAGY_STRIKE.toString();
//		// 循环计算集合中的状态为RUNNING的策略
//		for (Entry<String, Stratagy> entry : stratagyConcurrentHashMap.entrySet()) {
//			try {
//				Stratagy stratagy = entry.getValue();
//				if (StratagyStatusEnum.RUNNING.toString().equals(stratagy.getStatus())) {
//					// TODO
//					if(true) {
//						//策略触发
//						stratagy.setStatus(StratagyStatusEnum.STRIKE.toString());
//						ni.infoT=stratagy.MyToString();
//						Global.orderEventProducer.onData(ni.MyToString());
//					}
//				}
//			} catch (Exception e) {
//				logger.error("消费者{}，计算策略时异常：{}", id, e.getMessage());
//			} 
//
//		}
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public ConcurrentHashMap<String, Stratagy> getStratagyConcurrentHashMap() {
		return stratagyConcurrentHashMap;
	}

	public void setStratagyConcurrentHashMap(ConcurrentHashMap<String, Stratagy> stratagyConcurrentHashMap) {
		this.stratagyConcurrentHashMap = stratagyConcurrentHashMap;
	}

}

package com.zd.business.event;

import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lmax.disruptor.EventHandler;
import com.zd.business.constant.StratagyStatusEnum;
import com.zd.business.entity.Stratagy;

public class MarketEventHandler implements EventHandler<MarketEvent> {

	private static final Logger logger = LoggerFactory.getLogger(MarketEventHandler.class);

	protected final CountDownLatch shutdownLatch = new CountDownLatch(1);

	private String id;

	public MarketEventHandler(String id) {
		this.id = id;
	}

	// 策略容器
	private ConcurrentHashMap<String, Stratagy> stratagyConcurrentHashMap = new ConcurrentHashMap<>();

	public void awaitShutdown() throws InterruptedException {
		shutdownLatch.await();
	}

	public void onShutdown() {
		shutdownLatch.countDown();
	}

	@Override
	public void onEvent(MarketEvent event, long sequence, boolean endOfBatch) throws Exception {

		// 循环计算集合中的状态为RUNNING的策略
		for (Entry<String, Stratagy> entry : stratagyConcurrentHashMap.entrySet()) {
			try {
				Stratagy stratagy = entry.getValue();
				if (StratagyStatusEnum.RUNNING.toString().equals(stratagy.getStatus())) {
					// TODO
				}
			} catch (Exception e) {
				logger.error("消费者{}，计算策略时异常：{}", id, e.getMessage());
			} 

		}
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

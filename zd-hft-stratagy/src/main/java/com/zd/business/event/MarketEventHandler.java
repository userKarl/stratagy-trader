package com.zd.business.event;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lmax.disruptor.EventHandler;
import com.zd.business.entity.Stratagy;

public class MarketEventHandler implements EventHandler<MarketEvent>{

	private static final Logger logger=LoggerFactory.getLogger(MarketEventHandler.class);
	
	protected final CountDownLatch shutdownLatch = new CountDownLatch(1);

	private String id;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	//策略容器
	private ConcurrentHashMap<String,Stratagy> stratagyConcurrentHashMap=new ConcurrentHashMap<>();
	
	public void awaitShutdown() throws InterruptedException {
		shutdownLatch.await();
	}
	
	public void onShutdown() {
		shutdownLatch.countDown();
	}
	@Override
	public void onEvent(MarketEvent event, long sequence, boolean endOfBatch) throws Exception {

	}
	
}

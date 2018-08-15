package com.zd.business.event;

import java.util.concurrent.CountDownLatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lmax.disruptor.EventHandler;

public class MarketEventHandler implements EventHandler<MarketEvent>{

	private static final Logger logger=LoggerFactory.getLogger(MarketEventHandler.class);
	
	protected final CountDownLatch shutdownLatch = new CountDownLatch(1);
	
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

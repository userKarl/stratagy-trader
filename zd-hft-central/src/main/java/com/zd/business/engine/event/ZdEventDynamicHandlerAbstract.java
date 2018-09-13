package com.zd.business.engine.event;

import java.util.concurrent.CountDownLatch;

public abstract class ZdEventDynamicHandlerAbstract<T> implements ZdEventDynamicHandler<T> {

	protected final CountDownLatch shutdownLatch = new CountDownLatch(1);

	@Override
	public void onStart() {

	}

	@Override
	public void onShutdown() {
		shutdownLatch.countDown();
	}

	@Override
	public void awaitShutdown() throws InterruptedException {
		shutdownLatch.await();
	}

}

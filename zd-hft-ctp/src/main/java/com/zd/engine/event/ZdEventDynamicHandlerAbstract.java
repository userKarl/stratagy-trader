package com.zd.engine.event;

import java.util.Set;
import java.util.concurrent.CountDownLatch;

import com.google.common.collect.Sets;

public abstract class ZdEventDynamicHandlerAbstract<T> implements ZdEventDynamicHandler<T>{

	protected final CountDownLatch shutdownLatch = new CountDownLatch(1);
	protected Set<String> subscribedEventSet = Sets.newHashSet();
	
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

	
	@Override
	public Set<String> getSubscribedEventSet() {
		return subscribedEventSet;
	}
	
	@Override
	public void subscribeEvent(String event) {
		subscribedEventSet.add(event);
	}
	
	@Override
	public void unsubscribeEvent(String event) {
		subscribedEventSet.remove(event);
	}
	
}

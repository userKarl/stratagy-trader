package com.zd.engine.event;

import java.util.Set;

import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.LifecycleAware;


public interface ZdEventDynamicHandler<T> extends EventHandler<T>, LifecycleAware{

	void awaitShutdown()throws InterruptedException;
	
	public Set<String> getSubscribedEventSet();
	
	public void subscribeEvent(String event);
	
	public void unsubscribeEvent(String event);
}

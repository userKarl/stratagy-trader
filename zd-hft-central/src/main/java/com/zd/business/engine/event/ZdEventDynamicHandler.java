package com.zd.business.engine.event;

import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.LifecycleAware;

public interface ZdEventDynamicHandler<T> extends EventHandler<T>, LifecycleAware {

	void awaitShutdown() throws InterruptedException;

}

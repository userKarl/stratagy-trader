package com.zd.engine.main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.lmax.disruptor.util.DaemonThreadFactory;
import com.zd.engine.event.ZdEventEngine;

public class OrderEventEngine {

	private static final Logger logger=LoggerFactory.getLogger(OrderEventEngine.class);
	
	private static Disruptor<OrderEvent> disruptor;
	
	private static ZdEventEngine<OrderEvent> engine;
	
	static {
		disruptor = new Disruptor<OrderEvent>(new OrderEventFactory(), 65536,
				DaemonThreadFactory.INSTANCE, ProducerType.MULTI, new YieldingWaitStrategy());
		engine=new ZdEventEngine<OrderEvent>(disruptor,logger);
		engine.init();
	}
	
	public static OrderEventHandler addHandler() {
		OrderEventHandler handler=new OrderEventHandler();
		engine.addHandler(handler);
		return handler;
	}
	
	public static void removeHandler(OrderEventHandler handler) {
		engine.removeHandler(handler);
	}
	
	public static RingBuffer<OrderEvent> getRingBuffer(){
		return engine.getRingBuffer();
	}
}

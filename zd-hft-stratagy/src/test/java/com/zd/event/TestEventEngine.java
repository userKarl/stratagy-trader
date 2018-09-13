package com.zd.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.lmax.disruptor.util.DaemonThreadFactory;
import com.zd.business.engine.event.ZdEventEngine;
import com.zd.redis.RedisService;

public class TestEventEngine {

private static final Logger logger=LoggerFactory.getLogger(TestEventEngine.class);
	
	private static Disruptor<TestEvent> disruptor;
	
	private static ZdEventEngine<TestEvent> engine;
	
	static {
		disruptor = new Disruptor<TestEvent>(new TestEventFactory(), 65536,
				DaemonThreadFactory.INSTANCE, ProducerType.MULTI, new YieldingWaitStrategy());
		engine=new ZdEventEngine<TestEvent>(disruptor,logger);
		engine.init();
	}
	
	public static void addHandler(RedisService redisService) {
		engine.addHandler(new TestEventHandler(redisService));
	}
	
	public static void removeHandler(TestEventHandler handler) {
		engine.removeHandler(handler);
	}
	
	public static RingBuffer<TestEvent> getRingBuffer(){
		return engine.getRingBuffer();
	}
}

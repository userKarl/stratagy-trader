package com.zd.business.engine.main.central;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.lmax.disruptor.util.DaemonThreadFactory;
import com.zd.business.common.BaseService;
import com.zd.business.engine.event.ZdEventEngine;
import com.zd.redis.RedisService;

public class CentralEventEngine{

	private static final Logger logger=LoggerFactory.getLogger(CentralEventEngine.class);
	
	private static Disruptor<CentralEvent> disruptor;
	
	private static ZdEventEngine<CentralEvent> engine;
	
	static {
		disruptor = new Disruptor<CentralEvent>(new CentralEventFactory(), 65536,
				DaemonThreadFactory.INSTANCE, ProducerType.MULTI, new YieldingWaitStrategy());
		engine=new ZdEventEngine<CentralEvent>(disruptor,logger);
		engine.init();
	}
	
	public static void addHandler(RedisService redisService,BaseService baseService) {
		engine.addHandler(new CentralEventHandler(redisService,baseService));
	}
	
	public static void removeHandler(CentralEventHandler handler) {
		engine.removeHandler(handler);
	}
	
	public static RingBuffer<CentralEvent> getRingBuffer(){
		return engine.getRingBuffer();
	}
}

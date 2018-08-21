package com.zd.business.engine.main.market;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.lmax.disruptor.util.DaemonThreadFactory;
import com.zd.business.engine.event.ZdEventEngine;

public class MarketEventEngine {

	private static final Logger logger=LoggerFactory.getLogger(MarketEventEngine.class);
	
	private static Disruptor<MarketEvent> disruptor;
	
	private static ZdEventEngine<MarketEvent> engine;
	
	static {
		disruptor = new Disruptor<MarketEvent>(new MarketEventFactory(), 65536,
				DaemonThreadFactory.INSTANCE, ProducerType.MULTI, new YieldingWaitStrategy());
		engine=new ZdEventEngine<MarketEvent>(disruptor,logger);
		engine.init();
	}
	
	public static MarketEventHandler addHandler() {
		MarketEventHandler handler=new MarketEventHandler(UUID.randomUUID().toString());
		engine.addHandler(handler);
		return handler;
	}
	
	public static void removeHandler(MarketEventHandler handler) {
		engine.removeHandler(handler);
	}
	
	public static RingBuffer<MarketEvent> getRingBuffer(){
		return engine.getRingBuffer();
	}
}

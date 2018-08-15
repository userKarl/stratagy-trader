package com.zd.config;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.lmax.disruptor.BatchEventProcessor;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.zd.business.event.MarketEvent;
import com.zd.business.event.MarketEventHandler;

public class Global {

public static RingBuffer<MarketEvent> ringBuffer=null;
	
	public static Disruptor<MarketEvent> disruptor=null;
	
	public static ExecutorService executor = Executors.newCachedThreadPool();
	
	public static ConcurrentHashMap<MarketEventHandler, BatchEventProcessor<MarketEvent>> handlerProcessorMap = new ConcurrentHashMap<>();
	
}

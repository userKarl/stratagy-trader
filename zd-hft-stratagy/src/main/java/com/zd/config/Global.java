package com.zd.config;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.lmax.disruptor.BatchEventProcessor;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.zd.business.event.market.MarketEvent;
import com.zd.business.event.market.MarketEventHandler;

public class Global {

	/**
	 * Disruptor相关
	 */
	public static RingBuffer<MarketEvent> ringBuffer=null;
	
	public static Disruptor<MarketEvent> disruptor=null;
	
	public static ExecutorService executor = Executors.newCachedThreadPool();
	
	public static ConcurrentHashMap<String, BatchEventProcessor<MarketEvent>> handlerProcessorMap = new ConcurrentHashMap<>();
	
	
	//策略ID----消费者
	public static ConcurrentHashMap<String,MarketEventHandler> allEventConcurrentHashMap=new ConcurrentHashMap<>();
	
	//可用消费者
	public static ConcurrentHashMap<String,MarketEventHandler> availableEventConcurrentHashMap=new ConcurrentHashMap<>();
	
	//可开启的消费者个数
	public static final int TOTALCONSUMER=10;
	
	//每个消费者所计算的策略数
	public static final int TOTALSTRATAGYPERCONSUMER=20;
}

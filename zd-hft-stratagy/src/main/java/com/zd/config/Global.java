package com.zd.config;

import java.util.concurrent.ConcurrentHashMap;

import com.shanghaizhida.beans.MarketInfo;
import com.zd.business.engine.main.central.CentralEventProducer;
import com.zd.business.engine.main.market.MarketEventHandler;
import com.zd.business.engine.main.market.MarketEventProducer;
import com.zd.business.engine.main.order.OrderEventProducer;

public class Global {

	/**
	 * 行情Disruptor生产者
	 */
	public static MarketEventProducer marketEventProducer=null;
	
	/**
	 * 下单Disruptor生产者
	 */
	public static OrderEventProducer orderEventProducer=null;
	
	/**
	 * 中控Disruptor生产者
	 */
	public static CentralEventProducer centralEventProducer=null;
	
	//策略ID----消费者
	public static ConcurrentHashMap<String,MarketEventHandler> allEventConcurrentHashMap=new ConcurrentHashMap<>();
	
	//总的消费者
	public static ConcurrentHashMap<String,MarketEventHandler> eventConcurrentHashMap=new ConcurrentHashMap<>();
	
	//可用消费者
	public static ConcurrentHashMap<String,MarketEventHandler> availableEventConcurrentHashMap=new ConcurrentHashMap<>();
	
	//可开启的消费者个数
	public static final int TOTALCONSUMER=10;
	
	//每个消费者所计算的策略数
	public static final int TOTALSTRATAGYPERCONSUMER=20;
	
	/**
	 * 存放行情数据
	 */
	public static ConcurrentHashMap<String, MarketInfo> contractMap = new ConcurrentHashMap<String, MarketInfo>();

}

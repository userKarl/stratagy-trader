package com.zd.config;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.shanghaizhida.beans.MarketInfo;
import com.zd.business.engine.main.central.CentralEventProducer;
import com.zd.business.engine.main.market.MarketEventHandler;
import com.zd.business.engine.main.market.MarketEventProducer;
import com.zd.business.engine.main.order.OrderEventProducer;
import com.zd.business.service.thread.HandlerStratagyThread;

public class Global {

	/**
	 * 行情Disruptor生产者
	 */
	public static MarketEventProducer marketEventProducer = null;

	/**
	 * 下单Disruptor生产者
	 */
	public static OrderEventProducer orderEventProducer = null;

	/**
	 * 中控Disruptor生产者
	 */
	public static CentralEventProducer centralEventProducer = null;

	// 策略ID----消费者，key为策略ID
	public static ConcurrentHashMap<String, MarketEventHandler> allEventConcurrentHashMap = new ConcurrentHashMap<>();

	// 总的消费者，key为Disruptor消费者ID
	public static ConcurrentHashMap<String, MarketEventHandler> eventConcurrentHashMap = new ConcurrentHashMap<>();

	// 可用消费者，key为Disruptor消费者ID
	public static ConcurrentHashMap<String, MarketEventHandler> availableEventConcurrentHashMap = new ConcurrentHashMap<>();

	// 可开启的消费者个数
	public static final int TOTALCONSUMER = 10;

	// 每个消费者所计算的策略数
	public static final int TOTALSTRATAGYPERCONSUMER = 20;

	// 存放行情数据
	public static ConcurrentHashMap<String, MarketInfo> contractMap = new ConcurrentHashMap<String, MarketInfo>();

	// 处理策略计算的线程，key为Disruptor消费者ID
	public static ConcurrentHashMap<String, HandlerStratagyThread> handlerStratagyThreadMap = new ConcurrentHashMap<String, HandlerStratagyThread>();

	// 策略消费者的订阅行情队列，key为Disruptor消费者ID
	public static ConcurrentHashMap<String, ConcurrentLinkedQueue<String>> marketQueueMap = new ConcurrentHashMap<>();

}

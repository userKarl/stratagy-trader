package com.zd.config;

import java.util.concurrent.ConcurrentHashMap;

import com.shanghaizhida.beans.MarketInfo;
import com.zd.business.engine.main.central.CentralEventProducer;
import com.zd.business.engine.main.market.MarketEventProducer;
import com.zd.business.engine.main.order.OrderEventProducer;
import com.zd.business.entity.ctp.Tick;

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

	
	// 可开启的消费者个数
	public static final int TOTALCONSUMER = 10;

	// 每个消费者所计算的策略数
	public static final int TOTALSTRATAGYPERCONSUMER = 20;

	// 存放直达行情数据
	public static ConcurrentHashMap<String, MarketInfo> zdContractMap = new ConcurrentHashMap<String, MarketInfo>();
	
	//存放CTP行情数据
	public static ConcurrentHashMap<String, Tick> ctpContractMap = new ConcurrentHashMap<String, Tick>();

	//存放国际股票行情数据
	public static ConcurrentHashMap<String, MarketInfo> stockMap = new ConcurrentHashMap<String, MarketInfo>();
	
	//报单引用
	public static Integer orderRef=new Integer(25);
	
	public static Object object=new Object();
}

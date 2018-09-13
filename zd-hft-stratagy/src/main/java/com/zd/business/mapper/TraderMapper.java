package com.zd.business.mapper;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.shanghaizhida.beans.NetInfo;
import com.zd.business.engine.main.market.MarketEventHandler;
import com.zd.business.service.thread.HandlerStratagyThread;

public class TraderMapper {

	/**
	 * 
	 * 
	 * 1、确定Channel连接
	 * （1）每个用户只建立一个交易实例，当同一用户有多个连接时，该用户的交易数据发送至所有连接
	 * Map<accountNo,List<ChannelId>>
	 * 
	 * 2、确定所属策略
	 * (1)当交易实例中有交易数据返回时，需要确定该交易数据属于哪一个策略。
	 * (2)交易用户与消费者、策略绑定，当接收到交易数据时，解析NetInfo的accountNo
	 * 根据accountNo确定消费者，根据accountNo确定该消费者中的策略。
	 * (3)当一个用户有多个策略时，每个策略都会收到该用户的交易数据。
	 * (4)如果只需要统计策略所属的交易数据，只需与策略创建时的合约进行对比即可。
	 * 
	 * 
	 * 
	 * 
	 */
	
	// 策略ID----消费者，key为策略ID
	public static ConcurrentHashMap<String, MarketEventHandler> allEventConcurrentHashMap = new ConcurrentHashMap<>();

	// 总的消费者，key为Disruptor消费者ID
	public static ConcurrentHashMap<String, MarketEventHandler> eventConcurrentHashMap = new ConcurrentHashMap<>();

	// 可用消费者，key为Disruptor消费者ID
	public static ConcurrentHashMap<String, MarketEventHandler> availableEventConcurrentHashMap = new ConcurrentHashMap<>();
	
	// 处理策略计算的线程，key为HandlerStratagyThread的ID
	public static ConcurrentHashMap<String, HandlerStratagyThread> handlerStratagyThreadMap = new ConcurrentHashMap<String, HandlerStratagyThread>();

	// 策略消费者的订阅行情队列，key为Disruptor消费者ID
	public static ConcurrentHashMap<String, ConcurrentLinkedQueue<String>> marketQueueMap = new ConcurrentHashMap<>();

	//发送至下单服务器数据队列
	public static ConcurrentLinkedQueue<String> sendOrderInfoQueue=new ConcurrentLinkedQueue<>();
	
	//接收下单服务器数据队列
	public static ConcurrentLinkedQueue<NetInfo> resvOrderInfoQueue=new ConcurrentLinkedQueue<>();
		
}

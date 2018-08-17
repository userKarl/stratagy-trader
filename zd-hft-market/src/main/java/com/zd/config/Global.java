package com.zd.config;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Component;

import com.zd.business.engine.main.market.MarketEventHandler;
import com.zd.business.engine.main.market.MarketEventProducer;

import io.netty.channel.ChannelHandlerContext;

@Component
@EnableAutoConfiguration
public class Global {

	/**
	 * 行情Disruptor生产者
	 */
	public static MarketEventProducer marketEventProducer=null;
	
	//与一级行情服务器连接,正常情况下该map至多有一个值
	public static ConcurrentHashMap<String,ChannelHandlerContext> market01ChannelMap=new ConcurrentHashMap<String,ChannelHandlerContext>();
	
	//获取一级行情服务器连接的key
	public static final String MARKET01SERVERCHANNELKEY="MARKET01SERVERCHANNEL";
	
	//存放行情订阅者连接
	public static ConcurrentHashMap<String,ChannelHandlerContext> clientChannelMap=new ConcurrentHashMap<>();
	
	//每个行情订阅者对应一个Disruptor队列的消费者
	public static ConcurrentHashMap<String,MarketEventHandler> client2EventHandlerMap=new ConcurrentHashMap<>();
	
	//每个Disruptor队列的消费者对应一个行情订阅者连接
	public static ConcurrentHashMap<String,ChannelHandlerContext> eventHandler2clientMap=new ConcurrentHashMap<>();
	
	@Value("${netty.market01.server.host}")
	public String market01ServerHost;
	
	@Value("${netty.market01.server.port}")
	public int market01ServerPort;
	
	@Value("${netty.market02.server.host}")
	public String market02ServerHost;
	
	@Value("${netty.market02.server.port}")
	public int market02ServerPort;
}

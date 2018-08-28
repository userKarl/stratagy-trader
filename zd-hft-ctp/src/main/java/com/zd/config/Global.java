package com.zd.config;

import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Component;

import com.zd.engine.market.MarketEventHandler;
import com.zd.engine.market.MarketEventProducer;

import io.netty.channel.ChannelHandlerContext;
import xyz.redtorch.web.service.TradingService;

@Component
@EnableAutoConfiguration
public class Global {

	/**
	 * 行情Disruptor生产者
	 */
	public static MarketEventProducer marketEventProducer = null;

	// 行情订阅者连接
	public static ConcurrentHashMap<String, ChannelHandlerContext> clientChannelMap = new ConcurrentHashMap<>();

	// 每个行情订阅者对应一个Disruptor队列的消费者
	public static ConcurrentHashMap<String, MarketEventHandler> client2EventHandlerMap = new ConcurrentHashMap<>();

	// 每个Disruptor队列的消费者对应一个行情订阅者连接
	public static ConcurrentHashMap<String, ChannelHandlerContext> eventHandler2clientMap = new ConcurrentHashMap<>();

	public static TradingService tradingService = null;

	@Value("${netty.ctp.server.host}")
	public String marketCtpServerHost;

	@Value("${netty.ctp.server.port}")
	public int marketCtpServerPort;

}

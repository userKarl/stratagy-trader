package com.zd.config;

import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Component;

import io.netty.channel.ChannelHandlerContext;

@Component
@EnableAutoConfiguration
public class NettyGlobal {

	@Value("${netty.order.server.port}")
	public int nettyOrderServerPort;// 下单服务器端口

	@Value("${netty.order.server.host}")
	public String nettyOrderServerHost;// 下单服务器地址

	@Value("${netty.market.server.port}")
	public String nettyMarketServerPort;// 国际期货行情服务器端口

	@Value("${netty.market.server.host}")
	public String nettyMarketServerHost;// 国际期货行情服务器地址

	@Value("${netty.central.server.port}")
	public int nettyCentralServerPort;// 中控服务器端口

	@Value("${netty.central.server.host}")
	public String nettyCentralServerHost;// 中控服务器地址

	@Value("${netty.ctp.market.server.port}")
	public int nettyCTPMarketServerPort;// CTP行情服务器端口

	@Value("${netty.ctp.market.server.host}")
	public String nettyCTPMarketServerHost;// CTP行情服务器地址
	
	@Value("${netty.stock.market.server.port}")
	public String nettyStockMarketServerPort;// 国际股票行情服务器端口

	@Value("${netty.stock.market.server.host}")
	public String nettyStockMarketServerHost;// 国际股票行情服务器地址
	
	// 存放与中控服务器的连接，一个策略服务器只连接一个中控服务器，因此该Map中理论上至多有一个值
	public static ConcurrentHashMap<String, ChannelHandlerContext> centralServerChannalMap = new ConcurrentHashMap<>();

	// 与中控服务器连接的channel在centralServerChannalMap中的key
	public static final String CENTRALSERVERCHANNELKEY = "CENTRALSERVERCHANNEL";

	// 存放与下单服务器的连接，一个策略服务器只连接一个下单服务器，因此该Map中理论上至多有一个值
	public static ConcurrentHashMap<String, ChannelHandlerContext> orderServerChannalMap = new ConcurrentHashMap<>();

	// 与下单服务器连接的channel在orderServerChannalMap中的key
	public static final String ORDERSERVERCHANNELKEY = "ORDERSERVERCHANNEL";

	// 存放与CTP行情服务器的连接，一个策略服务器只连接一个CTP行情服务器，因此该Map中理论上至多有一个值
	public static ConcurrentHashMap<String, ChannelHandlerContext> ctpServerChannalMap = new ConcurrentHashMap<>();

	// 与CTP行情服务器连接的channel在centralServerChannalMap中的key
	public static final String CTPSERVERCHANNELKEY = "CTPSERVERCHANNEL";
}

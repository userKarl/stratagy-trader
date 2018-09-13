package com.zd.mapper;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.zd.business.service.TraderDataFeed;

import io.netty.channel.ChannelHandlerContext;
import xyz.redtorch.trader.gateway.GatewaySetting;

public class TraderMapper {

	// 交易账号--交易线程间映射(国际期货/股票交易)
	public static ConcurrentHashMap<String, TraderDataFeed> accountTraderMap = new ConcurrentHashMap<>();
	
	// CTP交易账号
	public static ConcurrentHashMap<String, GatewaySetting> accountTraderCTPMap = new ConcurrentHashMap<>();

	// 返回给客户端的交易数据
	public static ConcurrentLinkedQueue<String> traderInfoQueue = new ConcurrentLinkedQueue<>();

	// 尚未发送给客户端的交易数据（主要针对客户端断线）
	public static ConcurrentHashMap<String, List<String>> notSendTraderInfoMap = new ConcurrentHashMap<>();

	// 交易账户--客户端连接映射
	public static ConcurrentHashMap<String, List<ChannelHandlerContext>> accountChannelMap = new ConcurrentHashMap<>();
	
	//所有客户端连接
	public static ConcurrentHashMap<String, ChannelHandlerContext> allClientMap=new ConcurrentHashMap<>();
	
}

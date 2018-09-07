package com.zd.mapper;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import io.netty.channel.ChannelHandlerContext;
import xyz.redtorch.trader.gateway.GatewaySetting;

public class TraderMapper {

	// CTP交易账号
	public static ConcurrentHashMap<String, GatewaySetting> accountTraderCTPMap = new ConcurrentHashMap<>();

	// 返回给客户端的交易数据
	public static ConcurrentLinkedQueue<String> traderInfoQueue = new ConcurrentLinkedQueue<>();

	// 交易账户--客户端连接映射
	public static ConcurrentHashMap<String, List<ChannelHandlerContext>> accountChannelMap = new ConcurrentHashMap<>();
	
	//所有客户端连接
	public static ConcurrentHashMap<String, ChannelHandlerContext> allClientMap=new ConcurrentHashMap<>();
}

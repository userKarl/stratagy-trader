package com.zd.websocket;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

public class NettyWSGlobal {
	
	public static ChannelGroup group = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
	
	public static ConcurrentHashMap<String,ChannelGroup> groupMap=new ConcurrentHashMap<String,ChannelGroup>();
	
	public static ConcurrentHashMap<String, List<ChannelHandlerContext>> ctxMap=new ConcurrentHashMap<>();
	
	public static ConcurrentLinkedQueue<String> queue=new ConcurrentLinkedQueue<>(); 
}

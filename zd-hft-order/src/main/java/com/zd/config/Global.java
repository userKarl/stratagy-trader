package com.zd.config;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.zd.business.service.TraderDataFeed;

public class Global {

	//交易账号--交易线程间映射
	public static ConcurrentHashMap<String,TraderDataFeed> accountTraderMap=new ConcurrentHashMap<>();
	
	//交易信息队列，此队列的数据需要返回至中控服务器
	public static ConcurrentLinkedQueue<String> traderInfoQueue=new ConcurrentLinkedQueue<>();
}

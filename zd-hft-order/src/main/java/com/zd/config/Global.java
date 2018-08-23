package com.zd.config;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.zd.business.engine.main.OrderEventProducer;
import com.zd.business.service.TraderDataFeed;

import xyz.redtorch.trader.gateway.GatewaySetting;
import xyz.redtorch.web.service.TradingService;

public class Global {

	//交易账号--交易线程间映射(国际期货交易)
	public static ConcurrentHashMap<String,TraderDataFeed> accountTraderMap=new ConcurrentHashMap<>();
	
	//CTP交易账号
	public static ConcurrentHashMap<String,GatewaySetting> accountTraderCTPMap=new ConcurrentHashMap<>();
	
	//返回给客户端的交易数据
	public static ConcurrentLinkedQueue<String> traderInfoQueue=new ConcurrentLinkedQueue<>();
	
	public static OrderEventProducer orderEventProducer=null;
	
	//直达交易
	public static final String zdTraderHost="222.73.119.230";
	public static final String zdTraderPort="7003";
	
	//CTP交易
	public static final String ctpMdAddress="tcp://180.168.146.187:10011";
	public static final String ctpTdAddress="tcp://180.168.146.187:10001";
	public static final String brokerId="9999";
	public static final String gatewayClassName="xyz.redtorch.trader.gateway.ctp.CtpGateway";
	
	
	public static TradingService tradingService=null;
}

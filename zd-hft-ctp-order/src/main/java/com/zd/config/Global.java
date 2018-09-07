package com.zd.config;

import com.zd.engine.main.OrderEventProducer;

import xyz.redtorch.web.service.TradingService;

public class Global {

	public static OrderEventProducer orderEventProducer = null;

	// CTP交易
	public static final String ctpMdAddress = "tcp://180.168.146.187:10011";
	public static final String ctpTdAddress = "tcp://180.168.146.187:10001";
	public static final String brokerId = "9999";
	public static final String gatewayClassName = "xyz.redtorch.trader.gateway.ctp.CtpGateway";

	public static TradingService tradingService = null;
	
}

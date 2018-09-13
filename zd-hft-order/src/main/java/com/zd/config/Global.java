package com.zd.config;

import com.zd.business.engine.main.OrderEventProducer;

import xyz.redtorch.web.service.TradingService;

public class Global {

	public static OrderEventProducer orderEventProducer = null;

	// 直达国际期货交易
	public static final String zdTraderHost = "222.73.119.230";
	public static final String zdTraderPort = "7003";

	// 直达国际股票交易
	public static final String zdStockTraderHost = "222.73.119.230";
	public static final String zdStockTraderPort = "7012";

	// CTP交易
	public static final String ctpMdAddress = "tcp://180.168.146.187:10013";
	public static final String ctpTdAddress = "tcp://180.168.146.187:10003";
	public static final String brokerId = "9999";
	public static final String gatewayClassName = "xyz.redtorch.trader.gateway.ctp.CtpGateway";

	public static TradingService tradingService = null;
}

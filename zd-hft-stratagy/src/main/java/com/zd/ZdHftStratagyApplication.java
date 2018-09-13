package com.zd;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import com.zd.business.common.BaseService;
import com.zd.business.engine.main.central.CentralEventEngine;
import com.zd.business.engine.main.central.CentralEventProducer;
import com.zd.business.engine.main.market.MarketEventEngine;
import com.zd.business.engine.main.market.MarketEventProducer;
import com.zd.business.engine.main.order.OrderEventEngine;
import com.zd.business.engine.main.order.OrderEventProducer;
import com.zd.business.service.market.MarketDataFeed;
import com.zd.business.service.market.StockMarketDataFeed;
import com.zd.business.service.thread.ResvOrderThread;
import com.zd.config.Global;
import com.zd.config.NettyGlobal;
import com.zd.netty.NettyClient;
import com.zd.netty.ctp.CtpConnectionWatchdog;
import com.zd.netty.order.OrderConnectionWatchdog;
import com.zd.redis.RedisService;
import com.zd.websocket.NettyWSServer;
import com.zd.websocket.ReturnMarketInfoThread;

import io.netty.util.HashedWheelTimer;

@SpringBootApplication
@ComponentScan(basePackages = { "com.zd" })
public class ZdHftStratagyApplication implements CommandLineRunner {

	@Autowired
	private NettyGlobal nettyGlobal;

	@Autowired
	private RedisService redisService;

	@Autowired
	private BaseService baseService;

	public static void main(String[] args) {
		SpringApplication.run(ZdHftStratagyApplication.class, args);
	}

	@Override
	public void run(String... arg0) throws Exception {

		// 开启行情的Disruptor队列
		// MarketEventHandler marketEventHandler = MarketEventEngine.addHandler();
		// Global.eventConcurrentHashMap.put(marketEventHandler.getHandlerStratagyThread().getId(),
		// marketEventHandler);
		MarketEventProducer mep = new MarketEventProducer(MarketEventEngine.getRingBuffer());
		Global.marketEventProducer = mep;

		// 开启下单的Disruptor队列
		OrderEventEngine.addHandler();
		OrderEventProducer oep = new OrderEventProducer(OrderEventEngine.getRingBuffer());
		Global.orderEventProducer = oep;

		// 开启中控的Disruptor队列
		CentralEventEngine.addHandler(redisService, baseService);
		CentralEventProducer cep = new CentralEventProducer(CentralEventEngine.getRingBuffer());
		Global.centralEventProducer = cep;

		// 连接国际期货行情服务器
		MarketDataFeed mdf = new MarketDataFeed(nettyGlobal.nettyMarketServerHost, nettyGlobal.nettyMarketServerPort);
		mdf.start();

		// 连接国际股票行情服务器
		StockMarketDataFeed smdf = new StockMarketDataFeed(nettyGlobal.nettyStockMarketServerHost,
				nettyGlobal.nettyStockMarketServerPort);
		smdf.start();

		// 连接下单服务器
		NettyClient orderNettyClient = new NettyClient(nettyGlobal.nettyOrderServerHost,
				nettyGlobal.nettyOrderServerPort);
		orderNettyClient.start();
		OrderConnectionWatchdog orderWatchDog = new OrderConnectionWatchdog(orderNettyClient.getBootstrap(),
				new HashedWheelTimer(), orderNettyClient.getHost(), orderNettyClient.getPort());
		orderNettyClient.addHandler(orderWatchDog);

		// 连接CTP行情服务器
		NettyClient ctpNettyClient = new NettyClient(nettyGlobal.nettyCTPMarketServerHost,
				nettyGlobal.nettyCTPMarketServerPort);
		ctpNettyClient.start();
		CtpConnectionWatchdog ctpWatchDog = new CtpConnectionWatchdog(ctpNettyClient.getBootstrap(),
				new HashedWheelTimer(), ctpNettyClient.getHost(), ctpNettyClient.getPort());
		ctpNettyClient.addHandler(ctpWatchDog);

		// 解析下单服务器返回数据
		ResvOrderThread resvOrderThread = new ResvOrderThread(baseService);
		resvOrderThread.start();

		// // 连接中控服务器
		// NettyClient centralNettyClient = new
		// NettyClient(nettyGlobal.nettyCentralServerHost,
		// nettyGlobal.nettyCentralServerPort);
		// centralNettyClient.start();
		// CentralConnectionWatchdog centralWatchDog=new
		// CentralConnectionWatchdog(centralNettyClient.getBootstrap(),
		// new HashedWheelTimer(), centralNettyClient.getHost(),
		// centralNettyClient.getPort());
		// centralNettyClient.addHandler(centralWatchDog);

		new ReturnMarketInfoThread().start();

		new NettyWSServer().run();

		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				// mdf.stop();
				// orderNettyClient.stop();
				// centralNettyClient.stop();
			}
		});
	}
}

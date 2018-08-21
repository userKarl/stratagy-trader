package com.zd;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import com.zd.business.engine.main.central.CentralEventEngine;
import com.zd.business.engine.main.central.CentralEventProducer;
import com.zd.business.engine.main.market.MarketEventEngine;
import com.zd.business.engine.main.market.MarketEventHandler;
import com.zd.business.engine.main.market.MarketEventProducer;
import com.zd.business.engine.main.order.OrderEventEngine;
import com.zd.business.engine.main.order.OrderEventProducer;
import com.zd.business.service.market.MarketDataFeed;
import com.zd.config.Global;
import com.zd.config.NettyGlobal;

@SpringBootApplication
@ComponentScan(basePackages = { "com.zd" })
public class ZdHftStratagyApplication implements CommandLineRunner {

	@Autowired
	private NettyGlobal nettyGlobal;

	public static void main(String[] args) {
		SpringApplication.run(ZdHftStratagyApplication.class, args);
	}

	@Override
	public void run(String... arg0) throws Exception {

		// 开启行情的Disruptor队列
		MarketEventHandler marketEventHandler = MarketEventEngine.addHandler();
		Global.eventConcurrentHashMap.put(marketEventHandler.getId(), marketEventHandler);
		MarketEventProducer mep = new MarketEventProducer(MarketEventEngine.getRingBuffer());
		Global.marketEventProducer = mep;

		// 开启下单的Disruptor队列
		OrderEventEngine.addHandler();
		OrderEventProducer oep = new OrderEventProducer(OrderEventEngine.getRingBuffer());
		Global.orderEventProducer = oep;

		// 开启中控的Disruptor队列
		CentralEventEngine.addHandler();
		CentralEventProducer cep = new CentralEventProducer(CentralEventEngine.getRingBuffer());
		Global.centralEventProducer = cep;

		 // 连接行情服务器
		 MarketDataFeed mdf = new MarketDataFeed(nettyGlobal.nettyMarketServerHost, String.valueOf(nettyGlobal.nettyMarketServerPort));
		 mdf.start();
			
			
//		// 连接下单服务器
//		NettyClient orderNettyClient = new NettyClient(nettyGlobal.nettyOrderServerHost,
//				nettyGlobal.nettyOrderServerPort);
//		orderNettyClient.start();
//		CentralConnectionWatchdog orderWatchDog=new CentralConnectionWatchdog(orderNettyClient.getBootstrap(),
//				new HashedWheelTimer(), orderNettyClient.getHost(), orderNettyClient.getPort());
//		orderNettyClient.addHandler(orderWatchDog);
//		
//		
//		// 连接中控服务器
//		NettyClient centralNettyClient = new NettyClient(nettyGlobal.nettyCentralServerHost,
//				nettyGlobal.nettyCentralServerPort);
//		centralNettyClient.start();
//		CentralConnectionWatchdog centralWatchDog=new CentralConnectionWatchdog(centralNettyClient.getBootstrap(),
//				new HashedWheelTimer(), centralNettyClient.getHost(), centralNettyClient.getPort());
//		centralNettyClient.addHandler(centralWatchDog);

		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				mdf.stop();
//				orderNettyClient.stop();
//				centralNettyClient.stop();
			}
		});
	}
}

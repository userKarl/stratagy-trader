package com.zd;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import com.zd.config.Global;
import com.zd.config.NettyGlobal;
import com.zd.engine.main.OrderEventEngine;
import com.zd.engine.main.OrderEventProducer;
import com.zd.netty.NettyServer;
import com.zd.service.SendTraderInfoThread;

import io.netty.channel.ChannelFuture;
import xyz.redtorch.web.service.impl.TradingServiceImpl;

@SpringBootApplication
@ComponentScan(basePackages = { "com.zd", "xyz.redtorch" })
public class ZdHftCtpOrderApplication implements CommandLineRunner {

	@Autowired
	private NettyGlobal nettyGlobal;

	@Autowired
	private NettyServer nettyServer;

	@Autowired
	private TradingServiceImpl tradingService;
	
	public static void main(String[] args) {
		SpringApplication.run(ZdHftCtpOrderApplication.class, args);
	}

	@Override
	public void run(String... arg0) throws Exception {

		Global.tradingService=tradingService;
		
		// 开启Disruptor队列
		OrderEventEngine.addHandler();
		OrderEventProducer mep = new OrderEventProducer(OrderEventEngine.getRingBuffer());
		Global.orderEventProducer = mep;

		// 启动下单服务器
		Thread nettyServerThread = new Thread(new Runnable() {

			@Override
			public void run() {
				// 启动netty服务器
				ChannelFuture future = nettyServer.start(nettyGlobal.nettyCtpServerHost,
						nettyGlobal.nettyCtpServerPort);
				future.channel().closeFuture().syncUninterruptibly();
			}
		});
		nettyServerThread.start();

		Thread.sleep(3000);
		// 启动发送交易数据线程
		SendTraderInfoThread sendTraderInfoThread = new SendTraderInfoThread();
		sendTraderInfoThread.start();

		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				nettyServer.destroy();
				sendTraderInfoThread.stop();
				nettyServerThread.interrupt();
			}
		});
	}
}

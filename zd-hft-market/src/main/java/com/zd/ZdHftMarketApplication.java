package com.zd;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import com.zd.business.engine.main.market.MarketEventEngine;
import com.zd.business.engine.main.market.MarketEventProducer;
import com.zd.business.netty.client.NettyClient;
import com.zd.business.netty.server.NettyServer;
import com.zd.config.Global;

import io.netty.channel.ChannelFuture;

@SpringBootApplication
@ComponentScan(basePackages = { "com.zd" })
public class ZdHftMarketApplication implements CommandLineRunner{

	@Autowired
	private Global global;
	
	@Autowired
	private NettyClient nettyClient;
	
	@Autowired
	private NettyServer nettyServer;
	
	public static void main(String[] args) {
		SpringApplication.run(ZdHftMarketApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		// 开启行情的Disruptor队列
		MarketEventProducer mep=new MarketEventProducer(MarketEventEngine.getRingBuffer());
    	Global.marketEventProducer=mep;
    	
    	//连接一级行情服务器
    	nettyClient.start(global.market01ServerHost, global.market01ServerPort);
    	
    	//开启二级行情订阅服务器
    	Thread nettyServerThread=new Thread(new Runnable() {
			
			@Override
			public void run() {
				ChannelFuture future = nettyServer.start(global.market02ServerHost, global.market02ServerPort);
				future.channel().closeFuture().syncUninterruptibly();
			}
		});
    	nettyServerThread.start();
    	
    	Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				nettyClient.stop();
				nettyServer.destroy();
				nettyServerThread.interrupt();
			}
		});
	}
}

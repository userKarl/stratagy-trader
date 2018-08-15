package com.zd;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.lmax.disruptor.util.DaemonThreadFactory;
import com.zd.business.event.MarketEvent;
import com.zd.business.event.MarketEventFactory;
import com.zd.business.event.MarketEventProducer;
import com.zd.config.Global;
import com.zd.config.NettyGlobal;
import com.zd.netty.market.MarketNettyClient;
import com.zd.netty.order.OrderNettyClient;

@SpringBootApplication
@ComponentScan(basePackages = { "com.zd" })
public class ZdHftStratagyApplication implements CommandLineRunner{

	@Autowired
	private NettyGlobal nettyGlobal;
	
	@Autowired
	private MarketNettyClient marketNettyClient;
	
	@Autowired
	private OrderNettyClient orderNettyClient;
	
	public static void main(String[] args) {
		SpringApplication.run(ZdHftStratagyApplication.class, args);
	}

	@Override
	public void run(String... arg0) throws Exception {
		
		//开启Disruptor队列
		Disruptor<MarketEvent> disruptor = new Disruptor<MarketEvent>( new MarketEventFactory(), 65536,
				DaemonThreadFactory.INSTANCE,ProducerType.MULTI,new YieldingWaitStrategy());
    	RingBuffer<MarketEvent> ringBuffer = disruptor.start();
    	Global.ringBuffer=ringBuffer;
    	Global.disruptor=disruptor;
    	
    	//连接行情服务器
    	marketNettyClient.start(nettyGlobal.nettyMarketServerHost,nettyGlobal.nettyMarketServerPort,new MarketEventProducer(Global.ringBuffer));
    	
    	//连接下单服务器
    	orderNettyClient.start(nettyGlobal.nettyOrderServerHost, nettyGlobal.nettyOrderServerPort);
    	
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				marketNettyClient.stop();
				orderNettyClient.stop();
			}
		});
	}
}

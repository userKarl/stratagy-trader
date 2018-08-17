package com.zd;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import com.zd.business.engine.main.market.MarketEventEngine;
import com.zd.business.engine.main.market.MarketEventProducer;
import com.zd.config.Global;

@SpringBootApplication
@ComponentScan(basePackages = { "com.zd" })
public class ZdHftMarketApplication implements CommandLineRunner{

	public static void main(String[] args) {
		SpringApplication.run(ZdHftMarketApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		// 开启行情的Disruptor队列
		MarketEventProducer mep=new MarketEventProducer(MarketEventEngine.getRingBuffer());
    	Global.marketEventProducer=mep;
	}
}

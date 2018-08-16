package com.zd;

import java.util.UUID;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lmax.disruptor.BatchEventProcessor;
import com.shanghaizhida.beans.CommandCode;
import com.shanghaizhida.beans.NetInfo;
import com.zd.business.engine.main.central.CentralEventEngine;
import com.zd.business.engine.main.central.CentralEventProducer;
import com.zd.business.engine.main.market.MarketEvent;
import com.zd.business.engine.main.market.MarketEventEngine;
import com.zd.business.engine.main.market.MarketEventHandler;
import com.zd.business.engine.main.market.MarketEventProducer;
import com.zd.config.Global;

@RestController
public class TestController {

	@GetMapping("add")
	public void addHandler() {
		
//		BatchEventProcessor<MarketEvent> processor;
//		MarketEventHandler marketEventHandler = new MarketEventHandler(UUID.randomUUID().toString());
//    	processor = new BatchEventProcessor<MarketEvent>(Global.ringBuffer, Global.ringBuffer.newBarrier(),marketEventHandler);
//    	Global.ringBuffer.addGatingSequences(processor.getSequence());
//    	Global.handlerProcessorMap.put(marketEventHandler.getId(), processor);
//    	Global.executor.execute(processor);
    	
    }
	
	@GetMapping("sendMarket")
	public void sendMarket() {
		MarketEventProducer mep=new MarketEventProducer(MarketEventEngine.getRingBuffer());
		NetInfo ni=new NetInfo();
		ni.code=CommandCode.MARKET01;
		long l=System.nanoTime();
		ni.systemCode=""+l;
		mep.onData(ni.MyToString());
	}
	
	@GetMapping("sendCentral")
	public void sendCentral() {
		NetInfo ni=new NetInfo();
		ni.code=CommandCode.MARKET01;
		long l=System.nanoTime();
		ni.systemCode=""+l;
		new CentralEventProducer(CentralEventEngine.getRingBuffer()).onData(ni.MyToString());
	}
	
}

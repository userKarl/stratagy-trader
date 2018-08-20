package com.zd;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shanghaizhida.beans.CommandCode;
import com.shanghaizhida.beans.NetInfo;
import com.zd.business.engine.main.central.CentralEventEngine;
import com.zd.business.engine.main.central.CentralEventProducer;
import com.zd.business.engine.main.market.MarketEventEngine;
import com.zd.business.engine.main.market.MarketEventProducer;

@RestController
public class TestController {

	
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

package com.zd;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.zd.business.engine.main.market.MarketEventEngine;
import com.zd.business.engine.main.market.MarketEventHandler;
import com.zd.config.Global;

@RestController
public class TestController {

	@GetMapping("sub/{loopCount}/{symbols}")
	public void sub(@PathVariable Integer loopCount,@PathVariable String symbols) {
		for(int i=0;i<loopCount;i++) {
			String[] split = symbols.split(",");
			MarketEventHandler handler = MarketEventEngine.addHandler();
			for(String s:split) {
				handler.subscribeEvent(s);
			}
		}
		
	}
	
	@GetMapping("pub/{symbols}")
	public void pub(@PathVariable String symbols) {
		String[] split = symbols.split(",");
		for(String s:split) {
			Global.marketEventProducer.onData(s);
		}
	}
}

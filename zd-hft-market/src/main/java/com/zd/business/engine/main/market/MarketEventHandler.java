package com.zd.business.engine.main.market;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shanghaizhida.beans.NetInfo;
import com.zd.business.engine.event.ZdEventDynamicHandlerAbstract;

public class MarketEventHandler extends ZdEventDynamicHandlerAbstract<MarketEvent>{
	
	private static final Logger logger = LoggerFactory.getLogger(MarketEventHandler.class);

	private String id;

	public MarketEventHandler(String id) {
		this.id = id;
	}

	@Override
	public void onEvent(MarketEvent event, long sequence, boolean endOfBatch) throws Exception {
		NetInfo ni=new NetInfo();
		ni.MyReadString(event.getNetInfo());
		for(String s:subscribedEventSet) {
			if(s.equals(ni.code)) {
				logger.info("消费者 {}接收到 {} 的行情",id,s);
			}
		}
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	

}

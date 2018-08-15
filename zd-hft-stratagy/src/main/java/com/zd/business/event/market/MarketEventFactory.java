package com.zd.business.event.market;

import com.lmax.disruptor.EventFactory;

public class MarketEventFactory implements EventFactory<MarketEvent>{

	@Override
	public MarketEvent newInstance() {
		// TODO Auto-generated method stub
		return new MarketEvent();
	}

}

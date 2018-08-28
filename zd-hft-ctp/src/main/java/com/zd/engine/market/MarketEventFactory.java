package com.zd.engine.market;

import com.zd.engine.event.ZdEventFactory;

public class MarketEventFactory extends ZdEventFactory<MarketEvent> {

	@Override
	public MarketEvent newInstance() {
		return new MarketEvent();
	}

}

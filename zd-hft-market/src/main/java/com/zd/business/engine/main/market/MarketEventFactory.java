package com.zd.business.engine.main.market;

import com.zd.business.engine.event.ZdEventFactory;

public class MarketEventFactory extends ZdEventFactory<MarketEvent> {

	@Override
	public MarketEvent newInstance() {
		return new MarketEvent();
	}

}

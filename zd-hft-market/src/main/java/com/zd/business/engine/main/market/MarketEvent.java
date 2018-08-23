package com.zd.business.engine.main.market;

import com.zd.business.engine.event.ZdEvent;

public class MarketEvent extends ZdEvent{

	private String marketInfo;

	public String getMarketInfo() {
		return marketInfo;
	}

	public void setMarketInfo(String marketInfo) {
		this.marketInfo = marketInfo;
	}
}

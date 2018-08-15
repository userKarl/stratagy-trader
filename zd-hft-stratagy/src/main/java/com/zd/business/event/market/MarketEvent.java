package com.zd.business.event.market;

import com.shanghaizhida.beans.MarketInfo;

public class MarketEvent {

	private MarketInfo marketInfo;
	
	private String netInfo;
	
	public String getNetInfo() {
		return netInfo;
	}

	public void setNetInfo(String netInfo) {
		this.netInfo = netInfo;
	}

	public MarketInfo getMarketInfo() {
		return marketInfo;
	}

	public void setMarketInfo(MarketInfo marketInfo) {
		this.marketInfo = marketInfo;
	}

	
	
}

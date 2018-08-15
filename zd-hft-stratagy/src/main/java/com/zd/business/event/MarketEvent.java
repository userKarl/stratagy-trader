package com.zd.business.event;

import com.shanghaizhida.beans.MarketInfo;
import com.shanghaizhida.beans.NetInfo;

public class MarketEvent {

	private MarketInfo marketInfo;
	
	private NetInfo netInfo;
	
	public NetInfo getNetInfo() {
		return netInfo;
	}

	public void setNetInfo(NetInfo netInfo) {
		this.netInfo = netInfo;
	}

	public MarketInfo getMarketInfo() {
		return marketInfo;
	}

	public void setMarketInfo(MarketInfo marketInfo) {
		this.marketInfo = marketInfo;
	}

	
	
}

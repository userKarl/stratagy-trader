package com.zd.business.engine.main.market;

import com.zd.business.engine.event.ZdEvent;

import lombok.Data;

@Data
public class MarketEvent extends ZdEvent{

	private String marketInfo;
}

package com.zd.business.engine.main;

import com.zd.business.engine.event.ZdEventFactory;

public class OrderEventFactory extends ZdEventFactory<OrderEvent>{

	@Override
	public OrderEvent newInstance() {
		return new OrderEvent();
	}

}

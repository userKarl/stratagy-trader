package com.zd.engine.main;

import com.zd.engine.event.ZdEventFactory;

public class OrderEventFactory extends ZdEventFactory<OrderEvent>{

	@Override
	public OrderEvent newInstance() {
		return new OrderEvent();
	}

}

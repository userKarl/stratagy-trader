package com.zd.business.event.order;

import com.lmax.disruptor.EventFactory;

public class OrderEventFactory implements EventFactory<OrderEvent>{

	@Override
	public OrderEvent newInstance() {
		// TODO Auto-generated method stub
		return new OrderEvent();
	}

}

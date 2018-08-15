package com.zd.business.event.central;

import com.lmax.disruptor.EventFactory;

public class CentralEventFactory implements EventFactory<CentralEvent>{

	@Override
	public CentralEvent newInstance() {
		// TODO Auto-generated method stub
		return new CentralEvent();
	}

}

package com.zd.business.engine.main.central;

import com.zd.business.engine.event.ZdEventFactory;

public class CentralEventFactory extends ZdEventFactory<CentralEvent>{

	@Override
	public CentralEvent newInstance() {
		return new CentralEvent();
	}

}

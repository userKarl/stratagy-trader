package com.zd.event;

import com.zd.business.engine.event.ZdEventFactory;

public class TestEventFactory extends ZdEventFactory<TestEvent>{

	@Override
	public TestEvent newInstance() {
		return new TestEvent();
	}

}

package com.zd.business.engine.event;

import com.lmax.disruptor.RingBuffer;
import com.shanghaizhida.beans.NetInfo;

public abstract class ZdEventProducer<T> {

	protected final RingBuffer<T> ringBuffer;

	public ZdEventProducer(RingBuffer<T> ringBuffer) {
		this.ringBuffer = ringBuffer;
	}

	public abstract void onData(NetInfo netInfo);

}

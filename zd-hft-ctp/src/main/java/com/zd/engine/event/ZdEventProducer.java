package com.zd.engine.event;

import com.lmax.disruptor.RingBuffer;

public abstract class ZdEventProducer<T> {

	protected final RingBuffer<T> ringBuffer;

    public ZdEventProducer(RingBuffer<T> ringBuffer)
    {
        this.ringBuffer = ringBuffer;
    }
    
}

package com.zd.business.engine.main.market;

import com.lmax.disruptor.RingBuffer;
import com.zd.business.engine.event.ZdEventProducer;

public class MarketEventProducer extends ZdEventProducer<MarketEvent>{

	public MarketEventProducer(RingBuffer<MarketEvent> ringBuffer) {
		super(ringBuffer);
	}

	@Override
	public void onData(String netInfo) {
		 //1.可以把ringBuffer看做一个事件队列，那么next就是得到下面一个事件槽
        long sequence = ringBuffer.next();
        try {
            //2.用上面的索引取出一个空的事件用于填充（获取该序号对应的事件对象）
            MarketEvent event = ringBuffer.get(sequence);
            //3.获取要通过事件传递的业务数据
            event.setNetInfo(netInfo);
        } finally {
            //4.发布事件
            //注意，最后的 ringBuffer.publish 方法必须包含在 finally 中以确保必须得到调用；
            // 如果某个请求的 sequence 未被提交，将会堵塞后续的发布操作或者其它的 producer。
            ringBuffer.publish(sequence);
        }
	}

}

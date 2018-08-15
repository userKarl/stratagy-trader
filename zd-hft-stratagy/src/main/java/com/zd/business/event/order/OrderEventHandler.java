package com.zd.business.event.order;

import java.util.concurrent.CountDownLatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lmax.disruptor.EventHandler;
import com.zd.config.NettyGlobal;

import io.netty.channel.ChannelHandlerContext;

public class OrderEventHandler implements EventHandler<OrderEvent> {

	private static final Logger logger = LoggerFactory.getLogger(OrderEventHandler.class);

	protected final CountDownLatch shutdownLatch = new CountDownLatch(1);

	public void awaitShutdown() throws InterruptedException {
		shutdownLatch.await();
	}

	public void onShutdown() {
		shutdownLatch.countDown();
	}

	@Override
	public void onEvent(OrderEvent event, long sequence, boolean endOfBatch) throws Exception {
		try {
			//将数据发送至下单服务器
			ChannelHandlerContext ctx = NettyGlobal.orderServerChannalMap.get(NettyGlobal.ORDERSERVERCHANNELKEY);
			if(ctx!=null) {
				ctx.channel().writeAndFlush(event.getNetInfo());
			}
		} catch (Exception e) {
			logger.error("数据发送至下单服务器异常：{}",e.getMessage());
		}
		
	}

}

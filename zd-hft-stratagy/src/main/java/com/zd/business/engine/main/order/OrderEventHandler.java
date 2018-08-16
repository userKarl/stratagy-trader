package com.zd.business.engine.main.order;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zd.business.engine.event.ZdEventDynamicHandlerAbstract;

public class OrderEventHandler extends ZdEventDynamicHandlerAbstract<OrderEvent>{

	private static final Logger logger = LoggerFactory.getLogger(OrderEventHandler.class);
	
	@Override
	public void onEvent(OrderEvent event, long sequence, boolean endOfBatch) throws Exception {
		try {
			logger.info("准备发送给下单服务器的数据：{}",event.getNetInfo());
//			//将数据发送至下单服务器
//			ChannelHandlerContext ctx = NettyGlobal.orderServerChannalMap.get(NettyGlobal.ORDERSERVERCHANNELKEY);
//			if(ctx!=null) {
//				ctx.channel().writeAndFlush(event.getNetInfo());
//			}
		} catch (Exception e) {
			logger.error("数据发送至下单服务器异常：{}",e.getMessage());
		}
	}

}

package com.zd.netty;

import java.util.concurrent.TimeUnit;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.TimerTask;

@ChannelHandler.Sharable
public abstract class ConnectionWatchdog extends ChannelInboundHandlerAdapter
		implements TimerTask, ChannelHandlerHolder {

	@Override
	public ChannelHandler[] handlers() {
		return new ChannelHandler[]{
				new StringEncoder(),
				new DelimiterBasedFrameDecoder(1024*24, Unpooled.copiedBuffer("}".getBytes())),
				this,
				// 每隔30s的时间触发一次userEventTriggered的方法，并且指定IdleState的状态位是WRITER_IDLE
				new IdleStateHandler(0, 30, 0, TimeUnit.SECONDS),
				// 实现userEventTriggered方法，并在state是WRITER_IDLE的时候发送一个心跳包到sever端，告诉server端我还活着
				new ClientHandler()
				
		};
	}


}

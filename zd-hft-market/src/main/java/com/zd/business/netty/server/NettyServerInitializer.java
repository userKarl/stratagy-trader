package com.zd.business.netty.server;

import java.util.concurrent.TimeUnit;

import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;

public class NettyServerInitializer extends  ChannelInitializer<Channel>{

	@Override
	protected void initChannel(Channel ch) throws Exception {
		
		ChannelPipeline pipeline = ch.pipeline();
		//处理日志
		//pipeline.addLast(new LoggingHandler(LogLevel.INFO));
		
		//处理心跳
		pipeline.addLast(new IdleStateHandler(2, 0, 0, TimeUnit.SECONDS));
		pipeline.addLast(new DelimiterBasedFrameDecoder(1024*24, Unpooled.copiedBuffer("}".getBytes())));
		pipeline.addLast(new StringEncoder());
		pipeline.addLast(new ServerHandler());
		
	}

	

}

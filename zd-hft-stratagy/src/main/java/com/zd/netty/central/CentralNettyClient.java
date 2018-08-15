package com.zd.netty.central;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.HashedWheelTimer;

@Component
public class CentralNettyClient {
	
	private final static  Logger logger = LoggerFactory.getLogger(CentralNettyClient.class);

	private static EventLoopGroup nioEventLoopGroup;
	
	
	{
		nioEventLoopGroup = new NioEventLoopGroup();
	}
	
	public CentralNettyClient() {
		super();
	}
	
	
	public void start(String host, int port)  {
		logger.info("开启socket客户端，连接中控服务器...");
		// EventLoopGroup可以理解为是一个线程池，这个线程池用来处理连接、接受数据、发送数据
		Bootstrap bootstrap = new Bootstrap(); // 客户端引导类
		bootstrap.group(nioEventLoopGroup);// 多线程处理
		bootstrap.channel(NioSocketChannel.class);// 指定通道类型为NioServerSocketChannel，一种异步模式，OIO阻塞模式为OioServerSocketChannel
		bootstrap.remoteAddress(new InetSocketAddress(host, port));// 指定请求地址
		final CentralConnectionWatchdog watchDog = new CentralConnectionWatchdog(bootstrap, new HashedWheelTimer(), host, port) {
			
			@Override
			public ChannelHandler[] handlers() {
				return new ChannelHandler[]{
						new StringEncoder(),
						new StringDecoder(),
						this,
						// 每隔30s的时间触发一次userEventTriggered的方法，并且指定IdleState的状态位是WRITER_IDLE
						new IdleStateHandler(0, 30, 0, TimeUnit.SECONDS),
						// 实现userEventTriggered方法，并在state是WRITER_IDLE的时候发送一个心跳包到sever端，告诉server端我还活着
						new CentralClientHandler()
						
				};
			}
		};
		
		final ChannelFuture future;
		try {
			synchronized (bootstrap) {
				bootstrap.handler(new ChannelInitializer<NioSocketChannel>() {

					@Override
					protected void initChannel(NioSocketChannel ch) throws Exception {
						ch.pipeline().addLast(watchDog.handlers());
						ch.pipeline().addLast(new StringEncoder());
						ch.pipeline().addLast(new StringDecoder());
					}
					
				});
				future = bootstrap.connect().sync();// 链接服务器.调用sync()方法会同步阻塞
			}
			
			if (!future.isSuccess()) {
				logger.info("---- 连接服务器失败,2秒后重试 ---------port=" + port);
				future.channel().eventLoop().schedule(new Runnable() {
					@Override
					public void run() {
						start(host,port);
					}

				}, 2L, TimeUnit.SECONDS);
			}

		} catch (Exception e) {
			logger.error("exception happends e {}", e);
		}
		
		
	}
	
	public void stop() {
		nioEventLoopGroup.shutdownGracefully();
	}
	
}

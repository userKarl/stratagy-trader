package com.zd.netty;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class NettyClient {
	
	private final static  Logger logger = LoggerFactory.getLogger(NettyClient.class);

	private Bootstrap bootstrap;
	
	private static EventLoopGroup nioEventLoopGroup;
	
	private String host;
	private int port;
	
	{
		nioEventLoopGroup = new NioEventLoopGroup();
	}
	
	public NettyClient(String host, int port) {
		this.host=host;
		this.port=port;
	}
	
	
	public void start()  {
		logger.info("开启socket客户端，连接服务器...");
		// EventLoopGroup可以理解为是一个线程池，这个线程池用来处理连接、接受数据、发送数据
		bootstrap= new Bootstrap(); // 客户端引导类
		bootstrap.group(nioEventLoopGroup);// 多线程处理
		bootstrap.channel(NioSocketChannel.class);// 指定通道类型为NioServerSocketChannel，一种异步模式，OIO阻塞模式为OioServerSocketChannel
		bootstrap.remoteAddress(new InetSocketAddress(host, port));// 指定请求地址
		
		
		
	}
	
	public void addHandler(ConnectionWatchdog watchDog) {
		final ChannelFuture future;
		try {
			synchronized (bootstrap) {
				bootstrap.handler(new ChannelInitializer<NioSocketChannel>() {

					@Override
					protected void initChannel(NioSocketChannel ch) throws Exception {
						ch.pipeline().addLast(watchDog.handlers());
						ch.pipeline().addLast(new StringEncoder());
						ch.pipeline().addLast(new DelimiterBasedFrameDecoder(1024*24, Unpooled.copiedBuffer("}".getBytes())));
					}
					
				});
				future = bootstrap.connect().sync();// 链接服务器.调用sync()方法会同步阻塞
			}
			
			if (!future.isSuccess()) {
				logger.info("---- 连接服务器失败,2秒后重试 ---------port=" + port);
				future.channel().eventLoop().schedule(new Runnable() {
					@Override
					public void run() {
						start();
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


	public Bootstrap getBootstrap() {
		return bootstrap;
	}


	public void setBootstrap(Bootstrap bootstrap) {
		this.bootstrap = bootstrap;
	}


	public String getHost() {
		return host;
	}


	public void setHost(String host) {
		this.host = host;
	}


	public int getPort() {
		return port;
	}


	public void setPort(int port) {
		this.port = port;
	}
	
}

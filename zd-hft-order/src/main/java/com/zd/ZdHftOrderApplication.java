package com.zd;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import com.zd.business.service.SendTraderInfo2CentralThread;
import com.zd.config.NettyGlobal;
import com.zd.netty.client.NettyClient;
import com.zd.netty.server.NettyServer;

import io.netty.channel.ChannelFuture;

@SpringBootApplication
@ComponentScan(basePackages = { "com.zd" })
public class ZdHftOrderApplication implements CommandLineRunner{

	@Autowired
	private NettyGlobal nettyGlobal;
	
	@Autowired
	private NettyServer nettyServer;
	
	@Autowired
	private NettyClient nettyClient;
	
	public static void main(String[] args) {
		SpringApplication.run(ZdHftOrderApplication.class, args);
	}

	@Override
	public void run(String... arg0) throws Exception {
		//连接中控服务器
		nettyClient.start(nettyGlobal.nettyCentralServerHost, nettyGlobal.nettyCentralServerPort);
		
		//启动下单服务器
		new Thread(new Runnable() {

			@Override
			public void run() {
				// 启动netty服务器
				ChannelFuture future = nettyServer.start(nettyGlobal.nettyOrderServerHost, nettyGlobal.nettyOrderServerPort);
				future.channel().closeFuture().syncUninterruptibly();
			}
		}).start();
		
		
		Thread.sleep(3000);
		//启动发送交易数据线程
		new SendTraderInfo2CentralThread().start();
		
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				nettyClient.stop();
				nettyServer.destroy();
			}
		});
	}
}

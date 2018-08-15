package com.zd;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import com.zd.config.NettyGlobal;
import com.zd.netty.NettyServer;

import io.netty.channel.ChannelFuture;

@SpringBootApplication
@ComponentScan(basePackages = { "com.zd" })
public class ZdHftCentralApplication implements CommandLineRunner{

	@Autowired
	private NettyGlobal nettyGlobal;
	
	@Autowired
	private NettyServer nettyServer;
	
	public static void main(String[] args) {
		SpringApplication.run(ZdHftCentralApplication.class, args);
	}

	@Override
	public void run(String... arg0) throws Exception {
		// TODO Auto-generated method stub
		
		new Thread(new Runnable() {

			@Override
			public void run() {
				// 启动netty服务器
				ChannelFuture future = nettyServer.start(nettyGlobal.nettyCentralServerHost, nettyGlobal.nettyCentralServerPort);
				future.channel().closeFuture().syncUninterruptibly();
			}
		}).start();
		
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				nettyServer.destroy();
			}
		});
	}
}

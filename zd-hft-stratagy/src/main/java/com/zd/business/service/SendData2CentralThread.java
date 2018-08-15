package com.zd.business.service;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zd.business.common.CommonUtils;
import com.zd.config.NettyGlobal;

import io.netty.channel.ChannelHandlerContext;

/**
 * 发送数据至中控服务器
 * @author user
 *
 */
public class SendData2CentralThread implements Runnable{

	private static final Logger logger=LoggerFactory.getLogger(SendData2CentralThread.class);
	
	private volatile Thread thread;
	
	public void start() {
		thread=new Thread(this);
		thread.start();
	}
	
	public void stop() {
		thread.interrupt();
		thread=null;
	}
	
	@Override
	public void run() {
		logger.info("发送数据至中控服务器线程开启...");
		ChannelHandlerContext ctx = NettyGlobal.centralServerChannalMap.get(NettyGlobal.CENTRALSERVERCHANNELKEY);
		while(true) {
			try {
				String poll = NettyGlobal.returnData2CentralQueue.poll();
				if(StringUtils.isNotBlank(poll)) {
					if(StringUtils.isNotBlank(poll) && ctx!=null) {
						ctx.channel().writeAndFlush(CommonUtils.toCommandString(poll));
					}
				}
				Thread.sleep(10);
			} catch (InterruptedException e) {
				logger.error("发送数据至中控服务器异常：{}",e.getMessage());
			}
		}
	}

}

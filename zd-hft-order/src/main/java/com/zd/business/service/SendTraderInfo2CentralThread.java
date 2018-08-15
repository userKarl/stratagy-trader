package com.zd.business.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zd.common.CommonUtils;
import com.zd.common.utils.StringUtils;
import com.zd.config.Global;
import com.zd.config.NettyGlobal;

import io.netty.channel.ChannelHandlerContext;

/**
 * 将交易信息返回至中控服务器
 * @author user
 *
 */
public class SendTraderInfo2CentralThread implements Runnable{

	private static final Logger logger=LoggerFactory.getLogger(SendTraderInfo2CentralThread.class);
	
	private volatile Thread thread;
	
	public void start() {
		thread=new Thread(this);
		thread.start();
	}
	
	public void stop() {
		thread.interrupt();
		thread = null;
	}
	
	@Override
	public void run() {
		logger.info("返回交易数据至中控服务器线程启动...");
		ChannelHandlerContext ctx = NettyGlobal.centralServerChannalMap.get(NettyGlobal.CENTRALSERVERCHANNELKEY);
		while(true) {
			String poll = Global.traderInfoQueue.poll();
			if(StringUtils.isNotBlank(poll) && ctx!=null) {
				ctx.channel().writeAndFlush(CommonUtils.toCommandString(poll));
			}
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				logger.error("返回交易数据至中控服务器线程异常：{}",e.getMessage());
			}
		}
	}

}

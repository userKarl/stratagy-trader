package com.zd.business.service.thread;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shanghaizhida.beans.CommandCode;
import com.shanghaizhida.beans.NetInfo;
import com.zd.business.common.CommonUtils;
import com.zd.business.mapper.TraderMapper;
import com.zd.config.NettyGlobal;

import io.netty.channel.ChannelHandlerContext;

/**
 * 发送下单数据
 * @author user
 *
 */
public class SendOrderThread implements Runnable {

	private static final Logger logger = LoggerFactory.getLogger(SendOrderThread.class);

	private volatile Thread thread;
	
	private volatile boolean isStillRunning = false;

	public void start() {
		thread = new Thread(this);
		thread.start();
		isStillRunning = true;
	}

	public void stop() {
		if (thread != null) {
			isStillRunning = false;
			thread.interrupt();
			thread = null;
		}
	}
	
	@Override
	public void run() {
		NetInfo ni=new NetInfo();
		while(isStillRunning) {
			try {
				String orderInfo=TraderMapper.sendOrderInfoQueue.poll();
				ChannelHandlerContext ctx = NettyGlobal.orderServerChannalMap.get(NettyGlobal.ORDERSERVERCHANNELKEY);
				if(ctx!=null && StringUtils.isNotBlank(orderInfo)) {
					ni.code=CommandCode.ORDER;
					ni.infoT=orderInfo;
					ctx.channel().writeAndFlush(CommonUtils.toCommandString(ni.MyToString()));
				}
			} catch (Exception e) {
				logger.error("发送数据至下单服务器异常",e);
			}
		}
		
	}
}

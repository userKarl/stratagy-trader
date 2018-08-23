package com.zd.business.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shanghaizhida.beans.NetInfo;
import com.zd.common.CommonUtils;
import com.zd.common.utils.StringUtils;
import com.zd.config.Global;
import com.zd.config.NettyGlobal;

import io.netty.channel.ChannelHandlerContext;

/**
 * 将交易数据返回至策略服务器
 * @author user
 *
 */
public class SendTraderInfoThread implements Runnable{

	private static final Logger logger=LoggerFactory.getLogger(SendTraderInfoThread.class);
	
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
		try {
			logger.info("返回交易数据线程启动...");
			while(true) {
				String poll = Global.traderInfoQueue.poll();
				if(StringUtils.isNotBlank(poll)) {
					NetInfo ni=new NetInfo();
					ni.MyReadString(poll);
					String localSystemCode = ni.localSystemCode;
					ChannelHandlerContext ctx = NettyGlobal.clientMap.get(localSystemCode);
					if(ctx!=null) {
						ctx.channel().writeAndFlush(CommonUtils.toCommandString(poll));
					}
				}
				Thread.sleep(1);
			}
		} catch (Exception e) {
			logger.error("返回交易数据线程异常",e);
		}
		
	}

}

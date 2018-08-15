package com.zd.business.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shanghaizhida.beans.NetInfo;
import com.zd.business.constant.CommandEnum;
import com.zd.common.utils.StringUtils;
import com.zd.config.Global;
import com.zd.config.NettyGlobal;

/**
 * 处理中控服务器发送的数据
 * @author user
 *
 */
public class HandlerCentralDataThread implements Runnable{

	private static final Logger logger=LoggerFactory.getLogger(HandlerCentralDataThread.class);
	
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
		logger.info("处理中控服务器发送的数据...");
		while(true) {
			try {
				String poll = NettyGlobal.resvCentralDataQueue.poll();
				if(StringUtils.isNotBlank(poll)) {
					NetInfo ni=new NetInfo();
					ni.MyReadString(poll);
					if(CommandEnum.STRATAGY_ADD.toString().equals(ni.code)) {
						/**
						 * 创建新策略
						 */
						//判断总的消费者个数是否超过预设值
						if(Global.handlerProcessorMap.size()<Global.TOTALCONSUMER) {
							//判断当前是否有可用的消费者
							if(Global.availableEventConcurrentHashMap.size()>0) {
//								for() {
//									
//								}
							}
						}
					}
				}
				
				
				Thread.sleep(10);
			} catch (InterruptedException e) {
				logger.error("处理中控服务器发送的数据异常：{}",e.getMessage());
			}
		}
	}

}

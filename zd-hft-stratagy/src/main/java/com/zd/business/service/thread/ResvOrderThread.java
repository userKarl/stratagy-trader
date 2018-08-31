package com.zd.business.service.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shanghaizhida.beans.NetInfo;
import com.zd.business.entity.Stratagy;
import com.zd.business.mapper.TraderMapper;
import com.zd.common.utils.StringUtils;

/**
 * 接收下单数据
 * @author user
 *
 */
public class ResvOrderThread implements Runnable {

	private static final Logger logger = LoggerFactory.getLogger(ResvOrderThread.class);

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
		while(isStillRunning) {
			try {
				NetInfo ni=TraderMapper.resvOrderInfoQueue.poll();
				if(ni!=null) {
					//解析下单服务器返回的数据
					if(StringUtils.isNotBlank(ni.localSystemCode)) {
						String split[]=ni.localSystemCode.split("-");
						if(split!=null && split.length==2) {
							/**
							 * split[0] 消费者Id
							 * split[1] 策略Id
							 */
							HandlerStratagyThread handlerStratagyThread = TraderMapper.handlerStratagyThreadMap.get(split[0]);
							if(handlerStratagyThread!=null) {
								Stratagy stratagy = handlerStratagyThread.getStratagyConcurrentHashMap().get(split[1]);
								if(stratagy!=null) {
									stratagy.traderInfoHandler(ni);
								}
							}
						}
					}
				}
			} catch (Exception e) {
				logger.error("处理下单服务器返回数据异常",e);
			}
		}
	}

}

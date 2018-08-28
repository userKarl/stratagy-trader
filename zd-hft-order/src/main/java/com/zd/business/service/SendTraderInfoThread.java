package com.zd.business.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.shanghaizhida.beans.CommandCode;
import com.shanghaizhida.beans.NetInfo;
import com.zd.common.CommonUtils;
import com.zd.common.utils.StringUtils;
import com.zd.config.Global;
import com.zd.config.NettyGlobal;

import io.netty.channel.ChannelHandlerContext;

/**
 * 将交易数据返回至策略服务器
 * 
 * @author user
 *
 */
public class SendTraderInfoThread implements Runnable {

	private static final Logger logger = LoggerFactory.getLogger(SendTraderInfoThread.class);

	private volatile boolean isStillRunning = false;

	private volatile Thread thread;

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
		try {
			logger.info("返回交易数据线程启动...");
			while (isStillRunning) {
				String poll = Global.traderInfoQueue.poll();
				if (StringUtils.isNotBlank(poll)) {
					NetInfo ni = new NetInfo();
					ni.MyReadString(poll);
					String localSystemCode = ni.localSystemCode;
					ChannelHandlerContext ctx = NettyGlobal.clientMap.get(localSystemCode);
					if (ctx != null) {
						ctx.channel().writeAndFlush(CommonUtils.toCommandString(poll));
					} else {
						// 如果客户端连接已断开，则先将信息保存，等同一accountNo用户登录成功的时候，将该信息先返回
						if (!CommandCode.LOGIN.equals(ni.code)) {
							List<String> list = Global.notSendTraderInfoMap.get(ni.accountNo);
							if (list == null) {
								list = Lists.newArrayList();
							}
							list.add(poll);
							Global.notSendTraderInfoMap.put(ni.accountNo, list);
						}

					}
				}
				Thread.sleep(1);
			}
		} catch (Exception e) {
			logger.error("返回交易数据线程异常", e);
		}

	}

}

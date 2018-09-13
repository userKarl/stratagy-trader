package com.zd.websocket;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

public class ReturnMarketInfoThread implements Runnable {

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
				String poll=NettyWSGlobal.queue.poll();
				if(StringUtils.isNotBlank(poll)) {
					String[] split = poll.split("@");
					if(split!=null) {
						if(split.length==6) {
							String stratagyId=split[0];
							List<ChannelHandlerContext> list = NettyWSGlobal.ctxMap.get(stratagyId);
							for(ChannelHandlerContext ctx:list) {
								String stratagyName=split[1];
								String buyprice=split[2];
								String buyNums=split[3];
								String saleprice=split[4];
								String saleNums=split[5];
								String info=stratagyId+"@"+stratagyName+"@"+buyprice+"@"+buyNums+"@"+saleprice+"@"+saleNums;
								TextWebSocketFrame tws = new TextWebSocketFrame(info);
								ctx.channel().writeAndFlush(tws);
							}
						}
					}
				}
				Thread.sleep(1);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	}
}

package com.zd.netty.market;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shanghaizhida.beans.CommandCode;
import com.shanghaizhida.beans.NetInfo;
import com.zd.business.event.market.MarketEventProducer;
import com.zd.common.utils.StringUtils;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.Timeout;
import io.netty.util.Timer;
import io.netty.util.TimerTask;

@ChannelHandler.Sharable
public abstract class MarketConnectionWatchdog extends ChannelInboundHandlerAdapter
		implements TimerTask, MarketChannelHandlerHolder {

	private static final Logger logger = LoggerFactory.getLogger(MarketConnectionWatchdog.class);

	private Bootstrap bootstrap;
	private Timer timer;
	private final String host;
	private final int port;

	private volatile boolean reconnect = true;
	private int attempts;
	private volatile boolean Check = false;
	private volatile boolean disConnect = false;
	private volatile Channel channel;

	private volatile MarketEventProducer mep;

	public MarketConnectionWatchdog(Bootstrap boot, Timer timert, String host, int port, MarketEventProducer mep) {
		this.bootstrap = boot;
		this.timer = timert;
		this.host = host;
		this.port = port;
		this.mep = mep;
	}

	public boolean isReconnect() {
		return reconnect;
	}

	public void setReconnect(boolean reconnect) {
		this.reconnect = reconnect;
	}

	/**
	 * 建立连接
	 */
	@Override
	public void channelActive(final ChannelHandlerContext ctx) throws Exception {
		channel = ctx.channel();
		attempts = 0;
		reconnect = true;
		if (!Check) {
			Check = true;
			channel.eventLoop().scheduleWithFixedDelay(new Runnable() {
				@Override
				public void run() {
					if (disConnect) {
						channel.close();
						logger.warn("心跳检查失败,等待重连服务器---------");
					} else {
						logger.debug("心跳检查Successs");
					}
				}
			}, 5L, 5L, TimeUnit.SECONDS);
		}
		logger.info("Connects with {}.", channel);
		ctx.fireChannelActive();

	}

	/**
	 * 因为链路断掉之后，会触发channelInActive方法，进行重连 重连11次后 不再重连
	 */
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		disConnect = true;
		logger.info("Disconnects with {}, doReconnect = {},attemps == {}", ctx.channel(), reconnect, attempts);
		if (reconnect) {
			if (attempts < 12) {
				attempts++;
			} else {
				reconnect = false;
			}
			long timeout = 2 << attempts;
			logger.info("After {} seconds client will do reconnect", timeout);
			timer.newTimeout(this, timeout, TimeUnit.SECONDS);
		}
	}

	public void run(Timeout timeout) throws Exception {

		final ChannelFuture future;
		synchronized (bootstrap) {
			future = bootstrap.connect(host, port);
		}
		future.addListener(new ChannelFutureListener() {

			@Override
			public void operationComplete(final ChannelFuture f) throws Exception {
				boolean succeed = f.isSuccess();
				logger.warn("Reconnects with {}, {}.", host + ":" + port, succeed ? "succeed" : "failed");
				if (!succeed) {
					f.channel().pipeline().fireChannelInactive();
				} else {
					disConnect = false;
				}
			}
		});

	}

	/**
	 * 读取消息
	 */
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		try {
			String s = null;
			if (msg instanceof ByteBuf) {
				ByteBuf bb = (ByteBuf) msg;
				byte[] b = new byte[bb.readableBytes()];
				bb.readBytes(b);
				s = new String(b, "UTF-8");
			} else if (msg != null) {
				s = msg.toString();
			}

			NetInfo ni = new NetInfo();
			ni.MyReadString(s.substring(s.indexOf(")") + 1, s.length() - 1));

			if (StringUtils.isNotBlank(ni.infoT) && StringUtils.isNotBlank(ni.code)
					&& !CommandCode.HEARTBIT.equals(ni.code)) {
				mep.onData(ni);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("接收行情数据异常：{}", e.getMessage());
		}
	}

}

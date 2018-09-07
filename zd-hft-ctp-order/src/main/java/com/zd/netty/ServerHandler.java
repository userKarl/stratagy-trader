package com.zd.netty;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.shanghaizhida.beans.CommandCode;
import com.shanghaizhida.beans.NetInfo;
import com.zd.common.CommonUtils;
import com.zd.common.utils.StringUtils;
import com.zd.config.Global;
import com.zd.constant.RespMessage;
import com.zd.mapper.TraderMapper;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * 
 * @author user
 *
 */
public class ServerHandler extends ChannelInboundHandlerAdapter {

	private final static Logger logger = LoggerFactory.getLogger(ServerHandler.class);

	private volatile String lastAccountKey="";
	/**
	 * 客户端建立连接
	 */
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("客户端与服务端连接开启");
		TraderMapper.allClientMap.put(ctx.channel().id().toString(), ctx);
	}

	/**
	 * 客户端断开连接
	 */
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		// 移除连接
		logger.info("{} 断开连接", ctx.channel());
		TraderMapper.allClientMap.remove(ctx.channel().id().toString(), ctx);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		final String remoteAddress = RemotingUtil.parseRemoteAddress(ctx.channel());
		final String localAddress = RemotingUtil.parseLocalAddress(ctx.channel());
		logger.warn("ExceptionCaught in connection: local[{}], remote[{}], close the connection! Cause[{}:{}]",
				localAddress, remoteAddress, cause.getClass().getSimpleName(), cause.getMessage());
		ctx.channel().close();
	}

	/**
	 * 读取到消息
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
			ni.MyReadString(s.substring(s.indexOf(")") + 1, s.length()));

			if (StringUtils.isNotBlank(ni.infoT) && !CommandCode.HEARTBIT.equals(ni.code)) {
				logger.info("resv msg from client,{}",s);
				if (StringUtils.isNotBlank(ni.accountNo) && StringUtils.isNotBlank(ni.localSystemCode)) {
					String accountChannelKey = ni.localSystemCode + "-" + ni.accountNo;
					if(!lastAccountKey.equals(accountChannelKey)) {
						List<ChannelHandlerContext> list = TraderMapper.accountChannelMap.get(accountChannelKey);
						if(list==null) {
							list=Lists.newArrayList();
						}
						if(!list.contains(ctx)) {
							list.add(ctx);
							lastAccountKey=accountChannelKey;
						}
						TraderMapper.accountChannelMap.put(accountChannelKey,list);
					}
					Global.orderEventProducer.onData(ni);
				} else {
					NetInfo netInfo = new NetInfo();
					netInfo.code = CommandCode.CFLOGINERROR;
					netInfo.infoT = RespMessage.ACCOUNTNONULL;
					ctx.channel().writeAndFlush(CommonUtils.toCommandString(netInfo.MyToString()));
				}
			}
		} catch (Exception e) {
			logger.error("接收socket请求异常：{}", e.getMessage());
		}

	}

}

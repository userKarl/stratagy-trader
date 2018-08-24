package com.zd.netty.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shanghaizhida.beans.CommandCode;
import com.shanghaizhida.beans.NetInfo;
import com.zd.business.constant.RespMessage;
import com.zd.common.CommonUtils;
import com.zd.common.utils.StringUtils;
import com.zd.config.Global;
import com.zd.config.NettyGlobal;

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

	/**
	 * 客户端建立连接
	 */
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("客户端与服务端连接开启");
		NettyGlobal.clientMap.put(ctx.channel().id().toString(), ctx);
	}
	
	/**
	 * 客户端断开连接
	 */
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		// 移除连接
		NettyGlobal.clientMap.remove(ctx.channel().id().toString(), ctx);
		logger.info("{} 断开连接", ctx.channel());
		
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
			
			if(StringUtils.isNotBlank(ni.infoT) && !CommandCode.HEARTBIT.equals(ni.code)) {
				if(StringUtils.isNotBlank(ni.accountNo)) {
					//将该channel添加至localSystemCode，以便于得到交易回报时返回给相应的连接
					ni.localSystemCode=ctx.channel().id().toString();
					Global.orderEventProducer.onData(ni);
				}else {
					NetInfo netInfo = new NetInfo();
					netInfo.code=CommandCode.CFLOGINERROR;
					netInfo.infoT=RespMessage.ACCOUNTNONULL;
					ctx.channel().writeAndFlush(CommonUtils.toCommandString(netInfo.MyToString()));
				}
			}
		} catch (Exception e) {
			logger.error("接收socket请求异常：{}", e.getMessage());
		}


	}

}

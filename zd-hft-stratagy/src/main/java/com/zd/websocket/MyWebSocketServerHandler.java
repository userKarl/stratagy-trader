package com.zd.websocket;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;
import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Lists;
import com.shanghaizhida.beans.CommandCode;
import com.shanghaizhida.beans.NetInfo;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PingWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PongWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;
import io.netty.util.CharsetUtil;
public class MyWebSocketServerHandler extends
		SimpleChannelInboundHandler<Object> {
	private static final Logger logger = Logger
			.getLogger(WebSocketServerHandshaker.class.getName());
	private WebSocketServerHandshaker handshaker;
	
	public static ConcurrentHashMap<ChannelId,String> ctxMap=new ConcurrentHashMap<ChannelId,String>();
	
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		// 添加
//		NettyGlobal.group.add(ctx.channel());
		System.out.println("客户端与服务端连接开启");
	}
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		// 移除
		String key=ctxMap.get(ctx.channel().id());
		if(StringUtils.isNotBlank(key)){
			ChannelGroup group=NettyWSGlobal.groupMap.get(key);
			if(group!=null){
				group.remove(ctx.channel());
			}
		}else{
			NettyWSGlobal.group.remove(ctx);
		}
		
		System.out.println("客户端与服务端连接关闭");
	}
	
	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.flush();
	}
	private void handlerWebSocketFrame(ChannelHandlerContext ctx,
			WebSocketFrame frame) {
		// 判断是否关闭链路的指令
		if (frame instanceof CloseWebSocketFrame) {
			handshaker.close(ctx.channel(), (CloseWebSocketFrame) frame
					.retain());
		}
		// 判断是否ping消息
		if (frame instanceof PingWebSocketFrame) {
			ctx.channel().write(
					new PongWebSocketFrame(frame.content().retain()));
			return;
		}
		// 本例程仅支持文本消息，不支持二进制消息
		if (!(frame instanceof TextWebSocketFrame)) {
			System.out.println("本例程仅支持文本消息，不支持二进制消息");
			throw new UnsupportedOperationException(String.format(
					"%s frame types not supported", frame.getClass().getName()));
		}
		// 返回应答消息
		String request = ((TextWebSocketFrame) frame).text();
		System.out.println("服务端收到：" + request);
		List<ChannelHandlerContext> list = NettyWSGlobal.ctxMap.get(request);
		if(list==null) {
			list=Lists.newArrayList();
		}
		list.add(ctx);
		NettyWSGlobal.ctxMap.put(request, list);
		String info="服务端收到：" + request;
		TextWebSocketFrame tws = new TextWebSocketFrame(info);
		ctx.channel().writeAndFlush(tws);
		// 群发
//		NettyGlobal.group.writeAndFlush(tws);
		// 返回【谁发的发给谁】
		// ctx.channel().writeAndFlush(tws);
	}
	private void handleHttpRequest(ChannelHandlerContext ctx,
			FullHttpRequest req) {
		if (!req.decoderResult().isSuccess()
				|| (!"websocket".equals(req.headers().get("Upgrade")))) {
			sendHttpResponse(ctx, req, new DefaultFullHttpResponse(
					HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST));
			return;
		}
		WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(
				"ws://localhost:9353/websocket", null, false);
		handshaker = wsFactory.newHandshaker(req);
		if (handshaker == null) {
			WebSocketServerHandshakerFactory
					.sendUnsupportedVersionResponse(ctx.channel());
		} else {
			handshaker.handshake(ctx.channel(), req);
		}
	}
	private static void sendHttpResponse(ChannelHandlerContext ctx,
			FullHttpRequest req, DefaultFullHttpResponse res) {
		// 返回应答给客户端
		if (res.status().code() != 200) {
			ByteBuf buf = Unpooled.copiedBuffer(res.status().toString(),
					CharsetUtil.UTF_8);
			res.content().writeBytes(buf);
			buf.release();
		}
		// 如果是非Keep-Alive，关闭连接
		ChannelFuture f = ctx.channel().writeAndFlush(res);
		if (!isKeepAlive(req) || res.status().code() != 200) {
			f.addListener(ChannelFutureListener.CLOSE);
		}
	}
	private static boolean isKeepAlive(FullHttpRequest req) {
		return false;
	}
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		cause.printStackTrace();
		ctx.close();
	}
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
		if (msg instanceof FullHttpRequest) {
			handleHttpRequest(ctx, ((FullHttpRequest) msg));
		} else if (msg instanceof WebSocketFrame) {
			handlerWebSocketFrame(ctx, (WebSocketFrame) msg);
		}
	}
} 

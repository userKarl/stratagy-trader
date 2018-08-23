package com.zd.business.engine.main.market;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Sets;
import com.shanghaizhida.beans.MarketInfo;
import com.zd.business.common.CommonUtils;
import com.zd.business.engine.event.ZdEventDynamicHandlerAbstract;

import io.netty.channel.ChannelHandlerContext;

public class MarketEventHandler extends ZdEventDynamicHandlerAbstract<MarketEvent>{
	
	private static final Logger logger = LoggerFactory.getLogger(MarketEventHandler.class);

	private String id;//消费者ID

	private boolean isSubAll;//是否订阅全部行情
	
	private Set<String> unsubSet=Sets.newHashSet();//退订的合约集合，该集合只针对之前订阅全部行情时使用
	
	private ChannelHandlerContext ctx;
	
	public MarketEventHandler(String id) {
		this.id = id;
	}
	
	/**
	 * 退订行情，只针对之前订阅全部行情时使用
	 * @param symbol
	 */
	public void unsub(String symbol) {
		unsubSet.add(symbol);
	}
	
	/**
	 * 订阅行情，只针对之前订阅全部行情时使用
	 * @param symbol
	 */
	public void sub(String symbol) {
		unsubSet.remove(symbol);
	}

	@Override
	public void onEvent(MarketEvent event, long sequence, boolean endOfBatch) throws Exception {
		//广播行情
		try {
			MarketInfo mi=new MarketInfo();
			mi.MyReadString(event.getMarketInfo());
			if(ctx!=null) {
				if(isSubAll) {
					//订阅全部行情
					if(!unsubSet.contains(mi.code)) {
						ctx.channel().writeAndFlush(CommonUtils.toCommandString(event.getMarketInfo()));
					}
				}else {
					for(String s:subscribedEventSet) {
						if(s.equals(mi.code)) {
							ctx.channel().writeAndFlush(CommonUtils.toCommandString(event.getMarketInfo()));
						}
					}
				}
			}
		} catch (Exception e) {
			logger.error("消费行情数据异常：{}",e);
		}
		
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public boolean isSubAll() {
		return isSubAll;
	}

	public void setSubAll(boolean isSubAll) {
		this.isSubAll = isSubAll;
	}

	public ChannelHandlerContext getCtx() {
		return ctx;
	}

	public void setCtx(ChannelHandlerContext ctx) {
		this.ctx = ctx;
	}

	public Set<String> getUnsubSet() {
		return unsubSet;
	}

}

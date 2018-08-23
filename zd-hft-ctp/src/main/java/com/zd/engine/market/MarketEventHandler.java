package com.zd.engine.market;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zd.common.CommonUtils;
import com.zd.common.utils.json.JacksonUtil;
import com.zd.engine.event.ZdEventDynamicHandlerAbstract;
import com.zd.entity.CommandCode;
import com.zd.entity.NetInfo;

import io.netty.channel.ChannelHandlerContext;
import xyz.redtorch.trader.entity.Tick;

public class MarketEventHandler extends ZdEventDynamicHandlerAbstract<MarketEvent>{
	
	private static final Logger logger = LoggerFactory.getLogger(MarketEventHandler.class);

	private String id;//消费者ID

	private ChannelHandlerContext ctx;
	
	public MarketEventHandler(String id) {
		this.id = id;
	}

	@Override
	public void onEvent(MarketEvent event, long sequence, boolean endOfBatch) throws Exception {
		//广播行情
		try {
			String marketInfo = event.getMarketInfo();
			Tick tick = JacksonUtil.jsonToObj(marketInfo, Tick.class);
			NetInfo ni=new NetInfo();
			ni.code=CommandCode.MARKET02;
			ni.infoT=marketInfo;
			if(ctx!=null) {
				for(String s:subscribedEventSet) {
					if(s.equals(tick.getSymbol())) {
						String data=CommonUtils.toCommandString(ni.MyToString());
						ctx.channel().writeAndFlush(data);
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

	public ChannelHandlerContext getCtx() {
		return ctx;
	}

	public void setCtx(ChannelHandlerContext ctx) {
		this.ctx = ctx;
	}

}

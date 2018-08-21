package com.zd.business.engine.main.central;

import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shanghaizhida.beans.NetInfo;
import com.zd.business.common.CommonUtils;
import com.zd.business.constant.CommandEnum;
import com.zd.business.constant.MessageConst;
import com.zd.business.constant.StratagyStatusEnum;
import com.zd.business.engine.event.ZdEventDynamicHandlerAbstract;
import com.zd.business.engine.main.market.MarketEventEngine;
import com.zd.business.engine.main.market.MarketEventHandler;
import com.zd.business.entity.Stratagy;
import com.zd.common.utils.StringUtils;
import com.zd.config.Global;
import com.zd.config.NettyGlobal;

import io.netty.channel.ChannelHandlerContext;

public class CentralEventHandler extends ZdEventDynamicHandlerAbstract<CentralEvent>{

	private static final Logger logger = LoggerFactory.getLogger(CentralEventHandler.class);
	
	@Override
	public void onEvent(CentralEvent event, long sequence, boolean endOfBatch) throws Exception {
		try {
			logger.info("接收到中控服务器发送的数据：{}",event.getNetInfo());
			NetInfo ni=new NetInfo();
			ni.MyReadString(event.getNetInfo());
			Stratagy stratagy=new Stratagy();
			stratagy.MyReadString(ni.infoT);
			if(StringUtils.isNotBlank(stratagy.getId())) {
				throw new RuntimeException(MessageConst.STRATAGYNOTEXIST.getMsg());
			}
			ChannelHandlerContext ctx = NettyGlobal.centralServerChannalMap.get(NettyGlobal.CENTRALSERVERCHANNELKEY);
			String resp="";
			//--------开始新策略begin---------
			if(CommandEnum.STRATAGY_START.toString().equals(ni.code)) {
				//判断是否可以创建新的消费者
				if(Global.eventConcurrentHashMap.size()<Global.TOTALCONSUMER) {
					MarketEventHandler marketEventHandler = MarketEventEngine.addHandler();
					Global.eventConcurrentHashMap.put(marketEventHandler.getId(), marketEventHandler);
			    	//将策略添加至消费者中的策略集合
			    	marketEventHandler.getStratagyConcurrentHashMap().put(stratagy.getId(), stratagy);
			    	//将该消费者添加至可用消费者集合
			    	Global.availableEventConcurrentHashMap.put(marketEventHandler.getId(), marketEventHandler);
			    	//创建该策略与消费者的映射关系
			    	Global.allEventConcurrentHashMap.put(stratagy.getId(), marketEventHandler);
			    	resp=CommonUtils.formatMsg(ni.clientNo,CommandEnum.STRATAGY_START,MessageConst.STRATAGYSTART);
				}else {
					//如果不可以创建新的消费者，则判断是否有可用消费者
					if(Global.availableEventConcurrentHashMap.size()>0) {
						for(Entry<String,MarketEventHandler> entry:Global.availableEventConcurrentHashMap.entrySet()) {
							String consumerKey=entry.getKey();
							MarketEventHandler consumer=entry.getValue();
							ConcurrentHashMap<String, Stratagy> stratagyConcurrentHashMap=consumer.getStratagyConcurrentHashMap();
							stratagyConcurrentHashMap.put(stratagy.getId(), stratagy);
							if(stratagyConcurrentHashMap.size()>=Global.TOTALSTRATAGYPERCONSUMER) {
								//当策略数大于等于预设值时，将该消费者从可用消费者集合中移除
								Global.availableEventConcurrentHashMap.remove(consumerKey,consumer);
							}
							resp=CommonUtils.formatMsg(ni.clientNo,CommandEnum.STRATAGY_START,MessageConst.STRATAGYSTART);
							break;
						}
					}else {
						//如果没有可用消费者，则提示“策略过载”
						resp=CommonUtils.formatMsg(ni.clientNo,CommandEnum.SYSTEMEXCEPTION,MessageConst.CONSUMERFULL);
					}
				}
			}
			//--------开始新策略end---------
			
			
			//--------暂停策略begin---------
			else if(CommandEnum.STRATAGY_PAUSE.toString().equals(ni.code)) {
				String stratagyId=ni.infoT;
				MarketEventHandler marketEventHandler = Global.allEventConcurrentHashMap.get(stratagyId);
				if(marketEventHandler!=null) {
					ConcurrentHashMap<String, Stratagy> stratagyConcurrentHashMap = marketEventHandler.getStratagyConcurrentHashMap();
					Stratagy stratagy2 = stratagyConcurrentHashMap.get(stratagyId);
					if(stratagy2!=null) {
						stratagy2.setStatus(StratagyStatusEnum.PAUSE.toString());
						resp=CommonUtils.formatMsg(ni.clientNo,CommandEnum.STRATAGY_PAUSE,MessageConst.STRATAGYPAUSE);
//						NettyGlobal.returnData2CentralQueue.add();
					}else {
						//提示策略不存在
						resp=CommonUtils.formatMsg(ni.clientNo,CommandEnum.SYSTEMEXCEPTION,MessageConst.STRATAGYNOTEXIST);
//						NettyGlobal.returnData2CentralQueue.add();
					}
				}
			}
			//--------暂停策略end---------
			
			
			//--------停止策略begin---------
			else if(CommandEnum.STRATAGY_STOP.toString().equals(ni.code)) {
				String stratagyId=ni.infoT;
				MarketEventHandler marketEventHandler = Global.allEventConcurrentHashMap.get(stratagyId);
				if(marketEventHandler!=null) {
					ConcurrentHashMap<String, Stratagy> stratagyConcurrentHashMap = marketEventHandler.getStratagyConcurrentHashMap();
					Stratagy stratagy2 = stratagyConcurrentHashMap.get(stratagyId);
					if(stratagy2!=null) {
						stratagy2.setStatus(StratagyStatusEnum.STOP.toString());
						//将该策略从策略集合中移除
						stratagyConcurrentHashMap.remove(stratagyId,stratagy2);
						//如果该消费者不在可用消费者集合中，添加至可用消费者集合
						if(Global.availableEventConcurrentHashMap.get(marketEventHandler.getId())==null) {
							Global.availableEventConcurrentHashMap.put(marketEventHandler.getId(),marketEventHandler);
						}
						resp=CommonUtils.formatMsg(ni.clientNo,CommandEnum.STRATAGY_STOP,MessageConst.STRATAGYSTOP);
//						NettyGlobal.returnData2CentralQueue.add();
					}else {
						//提示策略不存在
						resp=CommonUtils.formatMsg(ni.clientNo,CommandEnum.SYSTEMEXCEPTION,MessageConst.STRATAGYNOTEXIST);
//						NettyGlobal.returnData2CentralQueue.add();
					}
				}
				//--------停止策略end---------
			}
			if(ctx!=null && StringUtils.isNotBlank(resp)) {
				ctx.channel().writeAndFlush(resp);
			}
		} catch (Exception e) {
			logger.error("处理中控服务器发送的数据异常：{}",e.getMessage());
		}
	}

}

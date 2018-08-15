package com.zd.business.event.central;

import java.util.UUID;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lmax.disruptor.BatchEventProcessor;
import com.lmax.disruptor.EventHandler;
import com.shanghaizhida.beans.NetInfo;
import com.zd.business.common.CommonUtils;
import com.zd.business.constant.CommandEnum;
import com.zd.business.constant.MessageConst;
import com.zd.business.constant.StratagyStatusEnum;
import com.zd.business.entity.Stratagy;
import com.zd.business.event.market.MarketEvent;
import com.zd.business.event.market.MarketEventHandler;
import com.zd.common.utils.StringUtils;
import com.zd.config.Global;
import com.zd.config.NettyGlobal;

import io.netty.channel.ChannelHandlerContext;

/**
 * （1）处理中控服务器发送的数据
 * （2）发送数据至中控服务器
 * @author user
 *
 */
public class CentralEventHandler implements EventHandler<CentralEvent> {

	private static final Logger logger = LoggerFactory.getLogger(CentralEventHandler.class);

	protected final CountDownLatch shutdownLatch = new CountDownLatch(1);

	private volatile String resp=null;
	
	public void awaitShutdown() throws InterruptedException {
		shutdownLatch.await();
	}

	public void onShutdown() {
		shutdownLatch.countDown();
	}

	@Override
	public void onEvent(CentralEvent event, long sequence, boolean endOfBatch) throws Exception {
		try {
			NetInfo ni=new NetInfo();
			ni.MyReadString(event.getNetInfo());
			Stratagy stratagy=new Stratagy();
			stratagy.MyReadString(ni.infoT);
			if(StringUtils.isNotBlank(stratagy.getId())) {
				throw new RuntimeException(MessageConst.STRATAGYNOTEXIST.getMsg());
			}
			ChannelHandlerContext ctx = NettyGlobal.centralServerChannalMap.get(NettyGlobal.CENTRALSERVERCHANNELKEY);

			//--------开始新策略begin---------
			if(CommandEnum.STRATAGY_START.toString().equals(ni.code)) {
				/**
				 * 开始新策略
				 */
				//判断当前是否有可用的消费者
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
//						NettyGlobal.returnData2CentralQueue.add();
						break;
					}
				}else {
					//如果没有可用消费者，判断总的消费者个数是否超过预设值
					if(Global.handlerProcessorMap.size()<Global.TOTALCONSUMER) {
						BatchEventProcessor<MarketEvent> processor;
						MarketEventHandler marketEventHandler = new MarketEventHandler(UUID.randomUUID().toString());
				    	processor = new BatchEventProcessor<MarketEvent>(Global.ringBuffer, Global.ringBuffer.newBarrier(),marketEventHandler);
				    	Global.ringBuffer.addGatingSequences(processor.getSequence());
				    	Global.handlerProcessorMap.put(marketEventHandler.getId(), processor);
				    	Global.executor.execute(processor);
				    	//将策略添加至消费者中的策略集合
				    	marketEventHandler.getStratagyConcurrentHashMap().put(stratagy.getId(), stratagy);
				    	//将该消费者添加至可用消费者集合
				    	Global.availableEventConcurrentHashMap.put(marketEventHandler.getId(), marketEventHandler);
				    	//创建该策略与消费者的映射关系
				    	Global.allEventConcurrentHashMap.put(stratagy.getId(), marketEventHandler);
				    	resp=CommonUtils.formatMsg(ni.clientNo,CommandEnum.STRATAGY_START,MessageConst.STRATAGYSTART);
//				    	NettyGlobal.returnData2CentralQueue.add();
					}else {
						//如果消费者个数大于等于预设值，则提示“运行策略过载”
						resp=CommonUtils.formatMsg(ni.clientNo,CommandEnum.SYSTEMEXCEPTION,MessageConst.CONSUMERFULL);
//						NettyGlobal.returnData2CentralQueue.add();
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
			}
			if(ctx!=null && StringUtils.isNotBlank(resp)) {
				ctx.channel().writeAndFlush(resp);
			}
		} catch (Exception e) {
			logger.error("处理中控服务器发送的数据异常：{}",e.getMessage());
		}
		
	}

}

package com.zd.business.service;

import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lmax.disruptor.BatchEventProcessor;
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

/**
 * 处理中控服务器发送的数据
 * 中控服务器只推送在web端设置为“开始”的策略
 * （1）开始新的策略时，需要推送完整的策略数据
 * （2）暂停策略时，只推送策略ID，此时集合中不会删除该策略
 * （3）停止策略时，只推送策略ID，此时集合会移除该策略
 * @author user
 *
 */
@Deprecated
public class HandlerCentralDataThread implements Runnable{

	private static final Logger logger=LoggerFactory.getLogger(HandlerCentralDataThread.class);
	
	private volatile Thread thread;
	
	public void start() {
		thread=new Thread(this);
		thread.start();
	}
	
	public void stop() {
		thread.interrupt();
		thread=null;
	}
	
	@Override
	public void run() {
		logger.info("处理中控服务器发送的数据...");
		while(true) {
			try {
				String poll = NettyGlobal.resvCentralDataQueue.poll();
				if(StringUtils.isNotBlank(poll)) {
					NetInfo ni=new NetInfo();
					ni.MyReadString(poll);
					Stratagy stratagy=new Stratagy();
					stratagy.MyReadString(ni.infoT);
					if(StringUtils.isNotBlank(stratagy.getId())) {
						continue;
					}
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
								NettyGlobal.returnData2CentralQueue.add(CommonUtils.formatMsg(ni.clientNo,CommandEnum.STRATAGY_START,MessageConst.STRATAGYSTART));
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
						    	NettyGlobal.returnData2CentralQueue.add(CommonUtils.formatMsg(ni.clientNo,CommandEnum.STRATAGY_START,MessageConst.STRATAGYSTART));
							}else {
								//如果消费者个数大于等于预设值，则提示“运行策略过载”
								NettyGlobal.returnData2CentralQueue.add(CommonUtils.formatMsg(ni.clientNo,CommandEnum.SYSTEMEXCEPTION,MessageConst.CONSUMERFULL));
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
								NettyGlobal.returnData2CentralQueue.add(CommonUtils.formatMsg(ni.clientNo,CommandEnum.STRATAGY_PAUSE,MessageConst.STRATAGYPAUSE));
							}else {
								//提示策略不存在
								NettyGlobal.returnData2CentralQueue.add(CommonUtils.formatMsg(ni.clientNo,CommandEnum.SYSTEMEXCEPTION,MessageConst.STRATAGYNOTEXIST));
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
								NettyGlobal.returnData2CentralQueue.add(CommonUtils.formatMsg(ni.clientNo,CommandEnum.STRATAGY_STOP,MessageConst.STRATAGYSTOP));
							}else {
								//提示策略不存在
								NettyGlobal.returnData2CentralQueue.add(CommonUtils.formatMsg(ni.clientNo,CommandEnum.SYSTEMEXCEPTION,MessageConst.STRATAGYNOTEXIST));
							}
						}
					}
					//--------停止策略end---------
				}
				
				
				Thread.sleep(10);
			} catch (InterruptedException e) {
				logger.error("处理中控服务器发送的数据异常：{}",e.getMessage());
			}
		}
	}

}

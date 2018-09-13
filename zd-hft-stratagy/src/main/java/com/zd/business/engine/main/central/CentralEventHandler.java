package com.zd.business.engine.main.central;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Lists;
import com.shanghaizhida.beans.CommandCode;
import com.shanghaizhida.beans.NetInfo;
import com.zd.business.common.BaseService;
import com.zd.business.common.CommonUtils;
import com.zd.business.common.JacksonUtil;
import com.zd.business.constant.CommandEnum;
import com.zd.business.constant.MessageConst;
import com.zd.business.constant.RedisConst;
import com.zd.business.constant.StratagyStatusEnum;
import com.zd.business.constant.SubMarketTypeEnum;
import com.zd.business.engine.event.ZdEventDynamicHandlerAbstract;
import com.zd.business.engine.main.market.MarketEventEngine;
import com.zd.business.engine.main.market.MarketEventHandler;
import com.zd.business.entity.Arbitrage;
import com.zd.business.entity.ArbitrageOrder;
import com.zd.business.entity.Contract;
import com.zd.business.entity.ContractOrder;
import com.zd.business.entity.Stratagy;
import com.zd.business.mapper.TraderMapper;
import com.zd.business.service.thread.HandlerStratagyThread;
import com.zd.config.Global;
import com.zd.config.NettyGlobal;
import com.zd.redis.RedisService;

import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CentralEventHandler extends ZdEventDynamicHandlerAbstract<CentralEvent>{

	private RedisService redisService;
	private BaseService baseService;
	
	public CentralEventHandler(RedisService redisService,BaseService baseService) {
		this.redisService=redisService;
		this.baseService=baseService;
	}
	
	@Override
	public void onEvent(CentralEvent event, long sequence, boolean endOfBatch) throws Exception {
		try {
			log.info("接收到中控服务器发送的数据：{}",event.getNetInfo());
			long l=System.nanoTime();
			NetInfo ni=new NetInfo();
			ni.MyReadString(event.getNetInfo());
			ChannelHandlerContext ctx = NettyGlobal.centralServerChannalMap.get(NettyGlobal.CENTRALSERVERCHANNELKEY);
			String resp="";
			if(CommandEnum.STRATAGY_START.toString().equals(ni.code)) {
				Stratagy stratagy =new Stratagy();
				String redisStr = (String)redisService.hmGet(RedisConst.STRATAGY, ni.infoT);//根据策略ID查找
				stratagy.MyReadString(redisStr);
				if(StringUtils.isBlank(stratagy.getId())) {
					throw new RuntimeException(MessageConst.STRATAGYNOTEXIST.getMsg());
				}else {
					stratagy.setStatus(StratagyStatusEnum.RUNNING.toString());
					redisService.hmSet(RedisConst.STRATAGY, stratagy.getId(), stratagy.MyToString());
				}
				log.info("策略：{}",JacksonUtil.objToJsonPretty(stratagy));
				List<String> symbols=Lists.newArrayList();
				//判断是否可以创建新的消费者
				if(TraderMapper.eventConcurrentHashMap.size()<Global.TOTALCONSUMER) {
					MarketEventHandler marketEventHandler = MarketEventEngine.addHandler(baseService);
					//订阅行情
					marketEventHandler.subscribeEvent(stratagy.getActiveContract().getExchangeCode()+"@"+stratagy.getActiveContract().getCode());
					symbols.add(stratagy.getActiveContract().getExchangeCode()+"@"+stratagy.getActiveContract().getCode());
					for(Contract contract:stratagy.getMarketContractList()) {
						marketEventHandler.subscribeEvent(contract.getExchangeCode()+"@"+contract.getCode());
						symbols.add(contract.getExchangeCode()+"@"+contract.getCode());
					}
					subCTPMarket(String.join(",", symbols));
					TraderMapper.eventConcurrentHashMap.put(marketEventHandler.getHandlerStratagyThread().getId(), marketEventHandler);
			    	//将策略添加至消费者中的策略集合
			    	marketEventHandler.getHandlerStratagyThread().getStratagyConcurrentHashMap().put(stratagy.getId(), stratagy);
			    	//将该消费者添加至可用消费者集合
			    	TraderMapper.availableEventConcurrentHashMap.put(marketEventHandler.getHandlerStratagyThread().getId(), marketEventHandler);
			    	//创建该策略与消费者的映射关系
			    	TraderMapper.allEventConcurrentHashMap.put(stratagy.getId(), marketEventHandler);
			    	resp=CommonUtils.formatMsg(ni.clientNo,CommandEnum.STRATAGY_START,MessageConst.STRATAGYSTART);
				}else {
					//如果不可以创建新的消费者，则判断是否有可用消费者
					if(TraderMapper.availableEventConcurrentHashMap.size()>0) {
						for(Entry<String,MarketEventHandler> entry:TraderMapper.availableEventConcurrentHashMap.entrySet()) {
							String consumerKey=entry.getKey();
							MarketEventHandler consumer=entry.getValue();
							ConcurrentHashMap<String, Stratagy> stratagyConcurrentHashMap=consumer.getHandlerStratagyThread().getStratagyConcurrentHashMap();
							stratagyConcurrentHashMap.put(stratagy.getId(), stratagy);
							//订阅行情
							consumer.subscribeEvent(stratagy.getActiveContract().getExchangeCode()+"@"+stratagy.getActiveContract().getCode());
							for(Contract contract:stratagy.getMarketContractList()) {
								consumer.subscribeEvent(contract.getExchangeCode()+"@"+contract.getCode());
								symbols.add(contract.getExchangeCode()+"@"+contract.getCode());
							}
							subCTPMarket(String.join(",", symbols));
							if(stratagyConcurrentHashMap.size()>=Global.TOTALSTRATAGYPERCONSUMER) {
								//当策略数大于等于预设值时，将该消费者从可用消费者集合中移除
								TraderMapper.availableEventConcurrentHashMap.remove(consumerKey,consumer);
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
			else if(CommandEnum.STRATAGY_PAUSE.toString().equals(ni.code)) {
				String stratagyId=ni.infoT;
				MarketEventHandler marketEventHandler = TraderMapper.allEventConcurrentHashMap.get(stratagyId);
				if(marketEventHandler!=null) {
					ConcurrentHashMap<String, Stratagy> stratagyConcurrentHashMap = marketEventHandler.getHandlerStratagyThread().getStratagyConcurrentHashMap();
					Stratagy stratagy2 = stratagyConcurrentHashMap.get(stratagyId);
					if(stratagy2!=null) {
						stratagy2.setStatus(StratagyStatusEnum.PAUSE.toString());
						redisService.hmSet(RedisConst.STRATAGY, stratagy2.getId(), stratagy2.MyToString());
						resp=CommonUtils.formatMsg(ni.clientNo,CommandEnum.STRATAGY_PAUSE,MessageConst.STRATAGYPAUSE);
//						NettyGlobal.returnData2CentralQueue.add();
					}else {
						//提示策略不存在
						resp=CommonUtils.formatMsg(ni.clientNo,CommandEnum.SYSTEMEXCEPTION,MessageConst.STRATAGYNOTEXIST);
//						NettyGlobal.returnData2CentralQueue.add();
					}
				}
			}
			else if(CommandEnum.STRATAGY_STOP.toString().equals(ni.code)) {
				String stratagyId=ni.infoT;
				MarketEventHandler marketEventHandler = TraderMapper.allEventConcurrentHashMap.get(stratagyId);
				if(marketEventHandler!=null) {
					ConcurrentHashMap<String, Stratagy> stratagyConcurrentHashMap = marketEventHandler.getHandlerStratagyThread().getStratagyConcurrentHashMap();
					Stratagy stratagy2 = stratagyConcurrentHashMap.get(stratagyId);
					if(stratagy2!=null) {
						stratagy2.setStatus(StratagyStatusEnum.STOP.toString());
						//将该策略从策略集合中移除
						stratagyConcurrentHashMap.remove(stratagyId,stratagy2);
						redisService.hmSet(RedisConst.STRATAGY, stratagy2.getId(), stratagy2.MyToString());
						//如果该消费者不在可用消费者集合中，添加至可用消费者集合
						if(TraderMapper.availableEventConcurrentHashMap.get(marketEventHandler.getHandlerStratagyThread().getId())==null) {
							TraderMapper.availableEventConcurrentHashMap.put(marketEventHandler.getHandlerStratagyThread().getId(),marketEventHandler);
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
			else if(CommandEnum.STRATAGY_ORDER.toString().equals(ni.code)) {
				ArbitrageOrder arbitrageOrder=new ArbitrageOrder();
				String redisStrArbitrageOrder=(String)redisService.hmGet(RedisConst.STRATAGY_ORDER, ni.infoT);
				arbitrageOrder.MyReadString(redisStrArbitrageOrder);
				if(StringUtils.isBlank(arbitrageOrder.getArbitrageOrderId())) {
					throw new RuntimeException(MessageConst.STRATAGYORDERNOTEXIST.getMsg());
				}
				ContractOrder activeContractOrder = arbitrageOrder.getActiveContractOrder();
				List<ContractOrder> marketContractOrderList = Lists.newArrayList();
				Arbitrage arbitrage=new Arbitrage();
				String redisStrArbitrage=(String)redisService.hmGet(RedisConst.STRATAGY_ARBITRAGE, arbitrageOrder.getArbitrageId());
				arbitrage.MyReadString(redisStrArbitrage);
				if(StringUtils.isBlank(arbitrage.getId())) {
					throw new RuntimeException(MessageConst.STRATAGYARBITRAGENOTEXIST.getMsg());
				}
				Map<String, ArbitrageOrder> orderMap = arbitrage.getOrderMap();
				orderMap.put(arbitrageOrder.getArbitrageOrderId(), arbitrageOrder);
				Stratagy stratagy =new Stratagy();
				String redisStr = (String)redisService.hmGet(RedisConst.STRATAGY, arbitrage.getStratagyId());
				stratagy.MyReadString(redisStr);
				if(StringUtils.isBlank(stratagy.getId())) {
					throw new RuntimeException(MessageConst.STRATAGYNOTEXIST.getMsg());
				}
				stratagy.setArbitrage(arbitrage);
				Contract activeContract=stratagy.getActiveContract();
				List<Contract> marketContractList = stratagy.getMarketContractList();
				activeContractOrder.setExchangeNo(activeContract.getExchangeCode());
				activeContractOrder.setCode(activeContract.getCode());
				activeContractOrder.setOrderId(CommonUtils.uuid());
				activeContractOrder.setNeedRequestNums(activeContract.getPerFillNums()*arbitrageOrder.getOrderNums());
				activeContractOrder.setRemainNums(activeContractOrder.getNeedRequestNums());
				activeContractOrder.setUpperTick(activeContract.getUpperTick());
				activeContractOrder.setEnv(activeContract.getEnv());
				activeContractOrder.setUserId(activeContract.getUserId());
				for(Contract marketContract:marketContractList) {
					ContractOrder marketContractOrder=new ContractOrder();
					marketContractOrder.setExchangeNo(marketContract.getExchangeCode());
					marketContractOrder.setCode(marketContract.getCode());
					marketContractOrder.setOrderId(CommonUtils.uuid());
					marketContractOrder.setNeedRequestNums(marketContract.getPerFillNums()*arbitrageOrder.getOrderNums());
					marketContractOrder.setRemainNums(marketContractOrder.getNeedRequestNums());
					marketContractOrder.setUpperTick(marketContract.getUpperTick());
					marketContractOrder.setEnv(marketContract.getEnv());
					marketContractOrder.setUserId(marketContract.getUserId());
					marketContractOrderList.add(marketContractOrder);
				}
				arbitrageOrder.setMarketContractOrderList(marketContractOrderList);
				redisService.hmSet(RedisConst.STRATAGY_ARBITRAGE, arbitrageOrder.getArbitrageId(),arbitrage.MyToString());
				MarketEventHandler marketEventHandler = TraderMapper.allEventConcurrentHashMap.get(stratagy.getId());
				if(marketEventHandler!=null) {
					HandlerStratagyThread handlerStratagyThread = marketEventHandler.getHandlerStratagyThread();
					if(handlerStratagyThread!=null) {
						ConcurrentHashMap<String, Stratagy> stratagyConcurrentHashMap = handlerStratagyThread.getStratagyConcurrentHashMap();
						if(stratagyConcurrentHashMap!=null) {
							stratagyConcurrentHashMap.put(stratagy.getId(), stratagy);
							redisService.hmSet(RedisConst.STRATAGY, stratagy.getId(), stratagy.MyToString());
						}
					}
				}
				
				//TODO
			}
			if(ctx!=null && StringUtils.isNotBlank(resp)) {
				ctx.channel().writeAndFlush(resp);
			}
			log.info("处理完中控服务器发送的指令耗时：{} ms",(System.nanoTime()-l)/1e6);
		} catch (Exception e) {
			log.error("处理中控服务器发送的数据异常：{}",e.getMessage());
		}
	}

	/**
	 * 订阅CTP行情
	 * @param symbols
	 */
	public void subCTPMarket(String symbols) {
		try {
			ChannelHandlerContext ctx = NettyGlobal.ctpServerChannalMap.get(NettyGlobal.CTPSERVERCHANNELKEY);
			if(ctx!=null) {
				NetInfo ni=new NetInfo();
				ni.code=CommandCode.MARKET02;
				ni.todayCanUse=SubMarketTypeEnum.ADD.getCode();
				ni.infoT=symbols;
				ctx.channel().writeAndFlush(CommonUtils.toCommandString(ni.MyToString()));
			}
			
		} catch (Exception e) {
			log.error("订阅CTP行情异常");
		}
		
	}
}

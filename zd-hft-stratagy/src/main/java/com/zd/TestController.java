package com.zd;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Lists;
import com.shanghaizhida.beans.CommandCode;
import com.shanghaizhida.beans.LoginInfo;
import com.shanghaizhida.beans.MarketInfo;
import com.shanghaizhida.beans.NetInfo;
import com.zd.business.common.CommonUtils;
import com.zd.business.constant.CheckEnum;
import com.zd.business.constant.CommandEnum;
import com.zd.business.constant.RedisConst;
import com.zd.business.constant.StratagyStatusEnum;
import com.zd.business.constant.StratagyTypeEnum;
import com.zd.business.constant.SubMarketTypeEnum;
import com.zd.business.constant.TraderEnvEnum;
import com.zd.business.constant.order.BuySaleEnum;
import com.zd.business.constant.order.PriceTypeEnum;
import com.zd.business.engine.main.central.CentralEventEngine;
import com.zd.business.engine.main.central.CentralEventProducer;
import com.zd.business.engine.main.market.MarketEventEngine;
import com.zd.business.engine.main.market.MarketEventProducer;
import com.zd.business.entity.Arbitrage;
import com.zd.business.entity.ArbitrageOrder;
import com.zd.business.entity.Contract;
import com.zd.business.entity.Stratagy;
import com.zd.config.Global;
import com.zd.config.NettyGlobal;
import com.zd.redis.RedisService;
import com.zd.websocket.NettyWSGlobal;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

@RestController
@RequestMapping("/test")
public class TestController {

	@Autowired
	private RedisService redisService;
	
	@GetMapping("subCTPMarket/{symbols}")
	public void subCTPMarket(@PathVariable String symbols) {
		ChannelHandlerContext ctx = NettyGlobal.ctpServerChannalMap.get(NettyGlobal.CTPSERVERCHANNELKEY);
		if(ctx!=null) {
			NetInfo ni=new NetInfo();
			ni.code=CommandCode.MARKET02;
			ni.todayCanUse=SubMarketTypeEnum.ADD.getCode();
			ni.infoT=symbols;
			ctx.channel().writeAndFlush(CommonUtils.toCommandString(ni.MyToString()));
		}
	}
	
	@GetMapping("loadAllStratagy")
	public void loadAllStratagy() {
		List<Object> list=redisService.values("STRATAGY");
		for(Object o:list) {
			Stratagy stratagy=new Stratagy();
			stratagy.MyReadString(o.toString());
			if(StratagyStatusEnum.RUNNING.toString().equals(stratagy.getStatus())) {
				NetInfo ni=new NetInfo();
				ni.code=CommandEnum.STRATAGY_START.toString();
				ni.infoT=stratagy.getId();
				Global.centralEventProducer.onData(ni.MyToString());
			}
		}
		
	}
	
	
	
	public void createStratagy() {
		Stratagy stratagy=new Stratagy();
		String stratagyId="testStratagy";
		stratagy.setId(stratagyId);
		stratagy.setName("测试策略");
		stratagy.setStatus(StratagyStatusEnum.INIT.toString());
		stratagy.setType(StratagyTypeEnum.A.toString());
		stratagy.setExpression("P1-P2");//(15*P1+P2-23/2.5)/(12.6-4)*2
		Contract activeContract=new Contract();
		activeContract.setCode("6A1809");
		activeContract.setExchangeCode("CME");
		activeContract.setDirection(BuySaleEnum.BUY.getCode());
		activeContract.setEnv(TraderEnvEnum.ZD.getCode());
		activeContract.setPerFillNums(1);
		activeContract.setUpperTick(new BigDecimal("0.001"));
		activeContract.setMinMatchNums(5);
		activeContract.setUserId("MN004257");
		activeContract.setUserPwd("888888");
		activeContract.setAccountNo("MN0002746");
		activeContract.setSlipPoint(10);
		activeContract.setPriceType(PriceTypeEnum.LIMIT.getCode());
		stratagy.setActiveContract(activeContract);
		
		Contract marketContract=new Contract();
		marketContract.setCode("BRN1811");
		marketContract.setExchangeCode("ICE");
		marketContract.setDirection(BuySaleEnum.SALE.getCode());
		marketContract.setEnv(TraderEnvEnum.ZD.getCode());
		marketContract.setPerFillNums(1);
		marketContract.setUpperTick(new BigDecimal("0.01"));
		marketContract.setMinMatchNums(5);
		marketContract.setSlipPoint(-15);
		marketContract.setUserId("MN004257");
		marketContract.setUserPwd("888888");
		marketContract.setAccountNo("MN0002746");
		
		marketContract.setPriceType(PriceTypeEnum.LIMIT.getCode());
		List<Contract> marketContractList=Lists.newArrayList();
		marketContractList.add(marketContract);
		stratagy.setMarketContractList(marketContractList);
		
		Arbitrage arbitrage=new Arbitrage();
		String id="testArbitrage";
		arbitrage.setId(id);
		arbitrage.setStratagyId(stratagyId);
		arbitrage.setName("测试套利");
		arbitrage.setDirection(BuySaleEnum.BUY.getCode());
		arbitrage.setMaxOnceActiveOrderNums(10);
		arbitrage.setMinMatchMultiple(BigDecimal.valueOf(1.5));
		arbitrage.setActiveRegion(0);
		arbitrage.setAutoChasing(CheckEnum.NO.getCode());
		arbitrage.setChasingLimit(5);
		arbitrage.setChasingPriceType(PriceTypeEnum.LIMIT.getCode());
		arbitrage.setChasingSlipPoint(2);
		arbitrage.setChasingStayInterval(2);
		arbitrage.setEvenActivePriceType(PriceTypeEnum.LIMIT.getCode());
		arbitrage.setEvenActiveSlipPoint(10);
		
		redisService.hmSet(RedisConst.STRATAGY_ARBITRAGE, id, arbitrage.MyToString());
		stratagy.setArbitrage(arbitrage);
		redisService.hmSet(RedisConst.STRATAGY, stratagyId, stratagy.MyToString());
	}
	
	public void createStratagy1() {
		Stratagy stratagy=new Stratagy();
		String stratagyId="testStratagy";
		stratagy.setId(stratagyId);
		stratagy.setName("测试策略");
		stratagy.setStatus(StratagyStatusEnum.INIT.toString());
		stratagy.setType(StratagyTypeEnum.A.toString());
		stratagy.setExpression("P1-P2");//(15*P1+P2-23/2.5)/(12.6-4)*2
		Contract activeContract=new Contract();
		activeContract.setCode("6A1809");
		activeContract.setExchangeCode("CME");
		activeContract.setDirection(BuySaleEnum.BUY.getCode());
		activeContract.setEnv(TraderEnvEnum.ZD.getCode());
		activeContract.setPerFillNums(1);
		activeContract.setUpperTick(new BigDecimal("0.001"));
		activeContract.setMinMatchNums(5);
		activeContract.setUserId("MN004257");
		activeContract.setUserPwd("888888");
		activeContract.setAccountNo("MN0002746");
		activeContract.setSlipPoint(10);
		activeContract.setPriceType(PriceTypeEnum.LIMIT.getCode());
		stratagy.setActiveContract(activeContract);
		
		Contract marketContract=new Contract();
		marketContract.setCode("BRN1812");
		marketContract.setExchangeCode("ICE");
		marketContract.setDirection(BuySaleEnum.SALE.getCode());
		marketContract.setEnv(TraderEnvEnum.ZD.getCode());
		marketContract.setPerFillNums(1);
		marketContract.setUpperTick(new BigDecimal("0.01"));
		marketContract.setMinMatchNums(5);
		marketContract.setSlipPoint(-15);
		marketContract.setUserId("MN004257");
		marketContract.setUserPwd("888888");
		marketContract.setAccountNo("MN0002746");
		
		marketContract.setPriceType(PriceTypeEnum.LIMIT.getCode());
		List<Contract> marketContractList=Lists.newArrayList();
		marketContractList.add(marketContract);
		stratagy.setMarketContractList(marketContractList);
		
		Arbitrage arbitrage=new Arbitrage();
		String id="testArbitrage";
		arbitrage.setId(id);
		arbitrage.setStratagyId(stratagyId);
		arbitrage.setName("测试套利");
		arbitrage.setDirection(BuySaleEnum.BUY.getCode());
		arbitrage.setMaxOnceActiveOrderNums(10);
		arbitrage.setMinMatchMultiple(BigDecimal.valueOf(1.5));
		arbitrage.setActiveRegion(0);
		arbitrage.setAutoChasing(CheckEnum.NO.getCode());
		arbitrage.setChasingLimit(5);
		arbitrage.setChasingPriceType(PriceTypeEnum.LIMIT.getCode());
		arbitrage.setChasingSlipPoint(2);
		arbitrage.setChasingStayInterval(2);
		arbitrage.setEvenActivePriceType(PriceTypeEnum.LIMIT.getCode());
		arbitrage.setEvenActiveSlipPoint(10);
		
		redisService.hmSet(RedisConst.STRATAGY_ARBITRAGE, id, arbitrage.MyToString());
		stratagy.setArbitrage(arbitrage);
		redisService.hmSet(RedisConst.STRATAGY, stratagyId, stratagy.MyToString());
	}
	
	public void createStratagy2() {
		Stratagy stratagy=new Stratagy();
		String stratagyId="testStratagy";
		stratagy.setId(stratagyId);
		stratagy.setName("测试策略");
		stratagy.setStatus(StratagyStatusEnum.INIT.toString());
		stratagy.setType(StratagyTypeEnum.A.toString());
		stratagy.setExpression("P1-P2");
		Contract activeContract=new Contract();
		activeContract.setCode("6A1809");
		activeContract.setExchangeCode("CME");
		activeContract.setDirection(BuySaleEnum.BUY.getCode());
		activeContract.setEnv(TraderEnvEnum.ZD.getCode());
		activeContract.setPerFillNums(1);
		activeContract.setUpperTick(new BigDecimal("0.001"));
		activeContract.setMinMatchNums(5);
		activeContract.setUserId("MN004257");
		activeContract.setUserPwd("888888");
		activeContract.setAccountNo("MN0002746");
		activeContract.setSlipPoint(10);
		activeContract.setPriceType(PriceTypeEnum.LIMIT.getCode());
		stratagy.setActiveContract(activeContract);
		
		Contract marketContract=new Contract();
		marketContract.setCode("6M1812");
		marketContract.setExchangeCode("CME");
		marketContract.setDirection(BuySaleEnum.SALE.getCode());
		marketContract.setEnv(TraderEnvEnum.ZD.getCode());
		marketContract.setPerFillNums(1);
		marketContract.setUpperTick(new BigDecimal("0.001"));
		marketContract.setMinMatchNums(5);
		marketContract.setSlipPoint(-15);
		marketContract.setUserId("MN004257");
		marketContract.setUserPwd("888888");
		marketContract.setAccountNo("MN0002746");
		
		marketContract.setPriceType(PriceTypeEnum.LIMIT.getCode());
		List<Contract> marketContractList=Lists.newArrayList();
		marketContractList.add(marketContract);
		stratagy.setMarketContractList(marketContractList);
		
		Arbitrage arbitrage=new Arbitrage();
		String id="testArbitrage";
		arbitrage.setId(id);
		arbitrage.setStratagyId(stratagyId);
		arbitrage.setName("测试套利");
		arbitrage.setDirection(BuySaleEnum.BUY.getCode());
		arbitrage.setMaxOnceActiveOrderNums(10);
		arbitrage.setMinMatchMultiple(BigDecimal.valueOf(1.5));
		arbitrage.setActiveRegion(0);
		arbitrage.setAutoChasing(CheckEnum.NO.getCode());
		arbitrage.setChasingLimit(5);
		arbitrage.setChasingPriceType(PriceTypeEnum.LIMIT.getCode());
		arbitrage.setChasingSlipPoint(2);
		arbitrage.setChasingStayInterval(2);
		arbitrage.setEvenActivePriceType(PriceTypeEnum.LIMIT.getCode());
		arbitrage.setEvenActiveSlipPoint(10);
		
		redisService.hmSet(RedisConst.STRATAGY_ARBITRAGE, id, arbitrage.MyToString());
		stratagy.setArbitrage(arbitrage);
		redisService.hmSet(RedisConst.STRATAGY, stratagyId, stratagy.MyToString());
	}
	
	public void createStratagyCTP() {
		Stratagy stratagy=new Stratagy();
		String stratagyId="testStratagy";
		stratagy.setId(stratagyId);
		stratagy.setName("测试策略");
		stratagy.setStatus(StratagyStatusEnum.INIT.toString());
		stratagy.setType(StratagyTypeEnum.A.toString());
		stratagy.setExpression("(15*P1+P2-23/2.5)/(12.6-4)*2");
		Contract activeContract=new Contract();
		activeContract.setCode("TA901");
		activeContract.setExchangeCode("CZCE");
		activeContract.setDirection(BuySaleEnum.BUY.getCode());
		activeContract.setEnv(TraderEnvEnum.CTP.getCode());
		activeContract.setPerFillNums(1);
		activeContract.setUpperTick(new BigDecimal("1"));
		activeContract.setMinMatchNums(1);
		activeContract.setUserPwd("1a1b1c1d1");
		activeContract.setUserId("122880");
		activeContract.setSlipPoint(5);
		activeContract.setPriceType(PriceTypeEnum.LIMIT.getCode());
		stratagy.setActiveContract(activeContract);
		
		Contract marketContract=new Contract();
		marketContract.setCode("TA903");
		marketContract.setExchangeCode("CZCE");
		marketContract.setDirection(BuySaleEnum.SALE.getCode());
		marketContract.setEnv(TraderEnvEnum.CTP.getCode());
		marketContract.setPerFillNums(1);
		marketContract.setUpperTick(new BigDecimal("1"));
		marketContract.setMinMatchNums(5);
		marketContract.setSlipPoint(-15);
		marketContract.setUserPwd("1a1b1c1d1");
		marketContract.setUserId("122880");
		
		marketContract.setPriceType(PriceTypeEnum.LIMIT.getCode());
		List<Contract> marketContractList=Lists.newArrayList();
		marketContractList.add(marketContract);
		stratagy.setMarketContractList(marketContractList);
		
		Arbitrage arbitrage=new Arbitrage();
		String id="testArbitrage";
		arbitrage.setId(id);
		arbitrage.setStratagyId(stratagyId);
		arbitrage.setName("测试套利");
		arbitrage.setDirection(BuySaleEnum.BUY.getCode());
		arbitrage.setMaxOnceActiveOrderNums(10);
		arbitrage.setMinMatchMultiple(BigDecimal.valueOf(1.5));
		arbitrage.setActiveRegion(0);
		arbitrage.setAutoChasing(CheckEnum.NO.getCode());
		arbitrage.setChasingLimit(5);
		arbitrage.setChasingPriceType(PriceTypeEnum.LIMIT.getCode());
		arbitrage.setChasingSlipPoint(2);
		arbitrage.setChasingStayInterval(2);
	
		redisService.hmSet(RedisConst.STRATAGY_ARBITRAGE, id, arbitrage.MyToString());
		stratagy.setArbitrage(arbitrage);
		redisService.hmSet(RedisConst.STRATAGY, stratagyId, stratagy.MyToString());
	}
	
	@GetMapping("startStratagy")
	public void startStratagy() throws InterruptedException {
		createStratagy1();
//		createStratagyCTP();
		NetInfo ni=new NetInfo();
		ni.code=CommandEnum.STRATAGY_START.toString();
		ni.infoT="testStratagy";
		Global.centralEventProducer.onData(ni.MyToString());
	}
	
	public void createArbitrageOrder(double price,String arbitrageOrderId) {
		ArbitrageOrder arbitrageOrder=new ArbitrageOrder();
		arbitrageOrder.setArbitrageId("testArbitrage");
		arbitrageOrder.setArbitrageOrderId(arbitrageOrderId);
		arbitrageOrder.setDirection(BuySaleEnum.BUY.getCode());
		arbitrageOrder.setOrderNums(2);
		arbitrageOrder.setOrderPrice(BigDecimal.valueOf(price));
		redisService.hmSet(RedisConst.STRATAGY_ORDER, arbitrageOrderId, arbitrageOrder.MyToString());
	}
	
	
	@GetMapping("arbitrageOrder/{price}/{arbitrageOrderId}")
	public void arbitrageOrder(@PathVariable double price,@PathVariable String arbitrageOrderId) throws InterruptedException {
		createArbitrageOrder(price,arbitrageOrderId);
		NetInfo ni=new NetInfo();
		ni.code=CommandEnum.STRATAGY_ORDER.toString();
		ni.infoT=arbitrageOrderId;
		Global.centralEventProducer.onData(ni.MyToString());
	}
	
	@GetMapping("l")
	public void l(Stratagy stratagy) throws Exception {
		NetInfo ni=new NetInfo();
		ni.code=CommandCode.LOGIN;
		LoginInfo login=new LoginInfo();
		login.userId="demo000604";
		login.userType="I";
		login.userPwd="888888";
		ni.infoT=login.MyToString();
		ni.systemCode="1";
		ni.localSystemCode="aaaa";
		System.out.println("向服务端发送数据："+CommonUtils.toCommandString(ni.MyToString()));
		ChannelHandlerContext ctx = NettyGlobal.orderServerChannalMap.get(NettyGlobal.ORDERSERVERCHANNELKEY);
		ctx.channel().writeAndFlush(CommonUtils.toCommandString(ni.MyToString()));
	}
	
	@GetMapping("zd/login")
	public void zdlogin() throws Exception {
		NetInfo ni=new NetInfo();
		ni.code=CommandCode.LOGIN;
		LoginInfo login=new LoginInfo();
		login.userId="MN004257";
		login.userType="I";
		login.userPwd="888888";
		ni.infoT=login.MyToString();
		ni.exchangeCode="1";
		ni.localSystemCode=TraderEnvEnum.ZD.toString();
		ni.accountNo="MN004257";
		System.out.println("向服务端发送数据："+CommonUtils.toCommandString(ni.MyToString()));
		ChannelHandlerContext ctx = NettyGlobal.orderServerChannalMap.get(NettyGlobal.ORDERSERVERCHANNELKEY);
		ctx.channel().writeAndFlush(CommonUtils.toCommandString(ni.MyToString()));
	}
	
	@GetMapping("zd/login1")
	public void zdlogin1() throws Exception {
		NetInfo ni=new NetInfo();
		ni.code=CommandCode.LOGIN;
		LoginInfo login=new LoginInfo();
		login.userId="demo003498";
		login.userType="I";
		login.userPwd="888888";
		ni.infoT=login.MyToString();
		ni.exchangeCode="1";
		ni.localSystemCode="ZD";
		ni.accountNo="demo003498";
		System.out.println("向服务端发送数据："+CommonUtils.toCommandString(ni.MyToString()));
		ChannelHandlerContext ctx = NettyGlobal.orderServerChannalMap.get(NettyGlobal.ORDERSERVERCHANNELKEY);
		ctx.channel().writeAndFlush(CommonUtils.toCommandString(ni.MyToString()));
	}
	
	@GetMapping("login")
	public void login() throws Exception {
		NetInfo ni=new NetInfo();
		ni.code=CommandCode.LOGIN;
		LoginInfo login=new LoginInfo();
		login.userId="122880";
		login.userType="I";
		login.userPwd="1a1b1c1d1";
		ni.infoT=login.MyToString();
		ni.exchangeCode="2";
		ni.localSystemCode="CTP";
		ni.accountNo="122880";
		System.out.println("向服务端发送数据："+CommonUtils.toCommandString(ni.MyToString()));
		ChannelHandlerContext ctx = NettyGlobal.orderServerChannalMap.get(NettyGlobal.ORDERSERVERCHANNELKEY);
		ctx.channel().writeAndFlush(CommonUtils.toCommandString(ni.MyToString()));
	}
	
	@GetMapping("login1")
	public void login1() throws Exception {
		NetInfo ni=new NetInfo();
		ni.code=CommandCode.LOGIN;
		LoginInfo login=new LoginInfo();
		login.userId="084127";
		login.userType="I";
		login.userPwd="1a1b1c1d1";
		ni.infoT=login.MyToString();
		ni.exchangeCode="2";
		ni.localSystemCode="CTP";
		ni.accountNo="084127";
		System.out.println("向服务端发送数据："+CommonUtils.toCommandString(ni.MyToString()));
		ChannelHandlerContext ctx = NettyGlobal.orderServerChannalMap.get(NettyGlobal.ORDERSERVERCHANNELKEY);
		ctx.channel().writeAndFlush(CommonUtils.toCommandString(ni.MyToString()));
	}
	
//	/**
//	 * 开始新的策略
//	 */
//	@GetMapping("startStratagy")
//	public void startStratagy() {
//		Stratagy stratagy=new Stratagy();
//		stratagy.setId(UUID.randomUUID().toString());
//		stratagy.setName(UUID.randomUUID().toString());
//		stratagy.setStatus(StratagyStatusEnum.RUNNING.toString());
//		stratagy.setType(StratagyTypeEnum.M.toString());
//		stratagy.setExpression("");
//		Contract marketContract=new Contract();
//		marketContract.setCode("6A1808");
//		marketContract.setExchangeCode("CME");
//		Contract activeContract=new Contract();
//		activeContract.setCode("6A1809");
//		activeContract.setExchangeCode("CME");
//		stratagy.setActiveContract(activeContract);
//		MarketProvider mp=new MarketProvider();
//		mp.setCurrBuyNum(0);
//		mp.setCurrSaleNum(0);
//		mp.setMaxBuyNum(1000500);
//		mp.setMaxOrderNum(50);
//		mp.setMaxSaleNum(1000500);
//		mp.setMinOrderNum(10);
//		mp.setPriceLevelLimit(5);
//		mp.setSpread(2);
//		stratagy.setMp(mp);
//		MarketEventHandler marketEventHandler = MarketEventEngine.addHandler();
//		TraderMapper.eventConcurrentHashMap.put(marketEventHandler.getHandlerStratagyThread().getId(), marketEventHandler);
//    	//将策略添加至消费者中的策略集合
//    	marketEventHandler.getHandlerStratagyThread().getStratagyConcurrentHashMap().put(stratagy.getId(), stratagy);
//    	//将该消费者添加至可用消费者集合
//    	TraderMapper.availableEventConcurrentHashMap.put(marketEventHandler.getHandlerStratagyThread().getId(), marketEventHandler);
//    	//创建该策略与消费者的映射关系
//    	TraderMapper.allEventConcurrentHashMap.put(stratagy.getId(), marketEventHandler);
//	}
//	
//	@GetMapping("startStratagy1")
//	public void startStratagy1() {
//		Stratagy stratagy=new Stratagy();
//		stratagy.setId(UUID.randomUUID().toString());
//		stratagy.setName(UUID.randomUUID().toString());
//		stratagy.setStatus(StratagyStatusEnum.RUNNING.toString());
//		stratagy.setType(StratagyTypeEnum.M.toString());
//		stratagy.setExpression("");
//		Contract marketContract=new Contract();
//		marketContract.setCode("6A18081");
//		marketContract.setExchangeCode("CME");
//		Contract activeContract=new Contract();
//		activeContract.setCode("6A18019");
//		activeContract.setExchangeCode("CME");
//		stratagy.setActiveContract(activeContract);
//		MarketProvider mp=new MarketProvider();
//		mp.setCurrBuyNum(0);
//		mp.setCurrSaleNum(0);
//		mp.setMaxBuyNum(1000500);
//		mp.setMaxOrderNum(50);
//		mp.setMaxSaleNum(1000500);
//		mp.setMinOrderNum(10);
//		mp.setPriceLevelLimit(5);
//		mp.setSpread(2);
//		stratagy.setMp(mp);
//		MarketEventHandler marketEventHandler = MarketEventEngine.addHandler();
//		TraderMapper.eventConcurrentHashMap.put(marketEventHandler.getHandlerStratagyThread().getId(), marketEventHandler);
//    	//将策略添加至消费者中的策略集合
//    	marketEventHandler.getHandlerStratagyThread().getStratagyConcurrentHashMap().put(stratagy.getId(), stratagy);
//    	//将该消费者添加至可用消费者集合
//    	TraderMapper.availableEventConcurrentHashMap.put(marketEventHandler.getHandlerStratagyThread().getId(), marketEventHandler);
//    	//创建该策略与消费者的映射关系
//    	TraderMapper.allEventConcurrentHashMap.put(stratagy.getId(), marketEventHandler);
//	}
	
	@GetMapping("sendMarket/{loopCount}")
	public void sendMarket(@PathVariable Integer loopCount) {
		for(int i=0;i<loopCount;i++) {
			MarketEventProducer mep=new MarketEventProducer(MarketEventEngine.getRingBuffer());
			MarketInfo mi=new MarketInfo();
			double buy[]=new double[5];
			double sale[]=new double[5];
			double d=BigDecimal.valueOf(Math.random()*100).setScale(1,BigDecimal.ROUND_HALF_UP).doubleValue();
			buy[0]=d;
			buy[1]=BigDecimal.valueOf(buy[0]-0.2).setScale(1,BigDecimal.ROUND_HALF_UP).doubleValue();
			buy[2]=BigDecimal.valueOf(buy[1]-0.2).setScale(1,BigDecimal.ROUND_HALF_UP).doubleValue();
			buy[3]=BigDecimal.valueOf(buy[2]-0.2).setScale(1,BigDecimal.ROUND_HALF_UP).doubleValue();
			buy[4]=BigDecimal.valueOf(buy[3]-0.2).setScale(1,BigDecimal.ROUND_HALF_UP).doubleValue();
			sale[0]=BigDecimal.valueOf(d+0.2).setScale(1,BigDecimal.ROUND_HALF_UP).doubleValue();
			sale[1]=BigDecimal.valueOf(sale[0]+0.2).setScale(1,BigDecimal.ROUND_HALF_UP).doubleValue();
			sale[2]=BigDecimal.valueOf(sale[1]+0.2).setScale(1,BigDecimal.ROUND_HALF_UP).doubleValue();
			sale[3]=BigDecimal.valueOf(sale[2]+0.2).setScale(1,BigDecimal.ROUND_HALF_UP).doubleValue();
			sale[4]=BigDecimal.valueOf(sale[3]+0.2).setScale(1,BigDecimal.ROUND_HALF_UP).doubleValue();
			
			mi.code="6A1808";
			mi.exchangeCode="CME";
			mi.buyPrice=""+buy[0];
			mi.buyPrice2=""+buy[1];
			mi.buyPrice3=""+buy[2];
			mi.buyPrice4=""+buy[3];
			mi.buyPrice5=""+buy[4];
			mi.buyNumber=""+BigDecimal.valueOf(Math.random()*500).setScale(0,BigDecimal.ROUND_HALF_UP).doubleValue();
			mi.buyNumber2=""+BigDecimal.valueOf(Math.random()*500).setScale(0,BigDecimal.ROUND_HALF_UP).doubleValue();
			mi.buyNumber3=""+BigDecimal.valueOf(Math.random()*500).setScale(0,BigDecimal.ROUND_HALF_UP).doubleValue();
			mi.buyNumber4=""+BigDecimal.valueOf(Math.random()*500).setScale(0,BigDecimal.ROUND_HALF_UP).doubleValue();
			mi.buyNumber5=""+BigDecimal.valueOf(Math.random()*500).setScale(0,BigDecimal.ROUND_HALF_UP).doubleValue();
			mi.salePrice=""+sale[0];
			mi.salePrice2=""+sale[1];
			mi.salePrice3=""+sale[2];
			mi.salePrice4=""+sale[3];
			mi.salePrice5=""+sale[4];
			mi.saleNumber=""+BigDecimal.valueOf(Math.random()*500).setScale(0,BigDecimal.ROUND_HALF_UP).doubleValue();
			mi.saleNumber2=""+BigDecimal.valueOf(Math.random()*500).setScale(0,BigDecimal.ROUND_HALF_UP).doubleValue();
			mi.saleNumber3=""+BigDecimal.valueOf(Math.random()*500).setScale(0,BigDecimal.ROUND_HALF_UP).doubleValue();
			mi.saleNumber4=""+BigDecimal.valueOf(Math.random()*500).setScale(0,BigDecimal.ROUND_HALF_UP).doubleValue();
			mi.saleNumber5=""+BigDecimal.valueOf(Math.random()*500).setScale(0,BigDecimal.ROUND_HALF_UP).doubleValue();
			/**
			14.5	469
			14.2	259
			13.8	486			
			13.6	156-50-34-50-22														
			13.4	120-35-50-35
		------------
			13.2	35-35
			13.1	169-50-35-50-34
			13		158-50-22
			12.8	249
			12.6	112
			*/
//			String data=mi.MyToString();
//			System.out.println(data);
//			String[] split = data.split("@",-1);
//			System.out.println(split.length);
			mep.onData(mi.MyToString());
		}
		
	}
	
	@GetMapping("sendCentral")
	public void sendCentral() {
		NetInfo ni=new NetInfo();
		ni.code=CommandCode.MARKET01;
		long l=System.nanoTime();
		ni.systemCode=""+l;
		new CentralEventProducer(CentralEventEngine.getRingBuffer()).onData(ni.MyToString());
	}
	
	@GetMapping("sendMarket2WS/{stratagyId}")
	public void sendMarket2WS(@PathVariable String stratagyId) {
		List<ChannelHandlerContext> list = NettyWSGlobal.ctxMap.get(stratagyId);
		for(ChannelHandlerContext ctx:list) {
			String stratagyName=CommonUtils.uuid();
			String buyprice=String.valueOf(Math.random()*100);
			String buyNums=String.valueOf(Math.random()*100);
			String saleprice=String.valueOf(Math.random()*100);
			String saleNums=String.valueOf(Math.random()*100);
			String info=stratagyId+"@"+stratagyName+"@"+buyprice+"@"+buyNums+"@"+saleprice+"@"+saleNums;
			TextWebSocketFrame tws = new TextWebSocketFrame(info);
			ctx.channel().writeAndFlush(tws);
		}
		
	}
}

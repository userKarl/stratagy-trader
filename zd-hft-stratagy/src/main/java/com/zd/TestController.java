package com.zd;

import java.math.BigDecimal;
import java.util.Map.Entry;
import java.util.UUID;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.shanghaizhida.beans.CommandCode;
import com.shanghaizhida.beans.LoginInfo;
import com.shanghaizhida.beans.MarketInfo;
import com.shanghaizhida.beans.NetInfo;
import com.zd.business.common.CommonUtils;
import com.zd.business.constant.StratagyStatusEnum;
import com.zd.business.constant.StratagyTypeEnum;
import com.zd.business.engine.main.central.CentralEventEngine;
import com.zd.business.engine.main.central.CentralEventProducer;
import com.zd.business.engine.main.market.MarketEventEngine;
import com.zd.business.engine.main.market.MarketEventHandler;
import com.zd.business.engine.main.market.MarketEventProducer;
import com.zd.business.entity.Contract;
import com.zd.business.entity.MarketProvider;
import com.zd.business.entity.Stratagy;
import com.zd.config.Global;
import com.zd.config.NettyGlobal;

import io.netty.channel.ChannelHandlerContext;

@RestController
public class TestController {

	@GetMapping("zd/login")
	public void zdlogin() throws Exception {
		NetInfo ni=new NetInfo();
		ni.code=CommandCode.LOGIN;
		LoginInfo login=new LoginInfo();
		login.userId="demo000604";
		login.userType="I";
		login.userPwd="888888";
		ni.infoT=login.MyToString();
		ni.systemCode="1";
		ni.localSystemCode="aaaa";
		ni.accountNo="demo000604";
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
		ni.systemCode="1";
		ni.localSystemCode="aaaa";
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
		ni.systemCode="2";
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
		ni.systemCode="2";
		ni.accountNo="084127";
		System.out.println("向服务端发送数据："+CommonUtils.toCommandString(ni.MyToString()));
		ChannelHandlerContext ctx = NettyGlobal.orderServerChannalMap.get(NettyGlobal.ORDERSERVERCHANNELKEY);
		ctx.channel().writeAndFlush(CommonUtils.toCommandString(ni.MyToString()));
	}
	
	/**
	 * 开始新的策略
	 */
	@GetMapping("startStratagy")
	public void startStratagy() {
		Stratagy stratagy=new Stratagy();
		stratagy.setId(UUID.randomUUID().toString());
		stratagy.setName(UUID.randomUUID().toString());
		stratagy.setStatus(StratagyStatusEnum.RUNNING.toString());
		stratagy.setType(StratagyTypeEnum.M.toString());
		stratagy.setExpression("");
		Contract marketContract=new Contract();
		marketContract.setCode("6A1808");
		marketContract.setExchangeCode("CME");
		Contract activeContract=new Contract();
		activeContract.setCode("6A1809");
		activeContract.setExchangeCode("CME");
		stratagy.setActiveContract(activeContract);
		stratagy.setMarketContract(marketContract);
		MarketProvider mp=new MarketProvider();
		mp.setCurrBuyNum(0);
		mp.setCurrSaleNum(0);
		mp.setMaxBuyNum(1000500);
		mp.setMaxOrderNum(50);
		mp.setMaxSaleNum(1000500);
		mp.setMinOrderNum(10);
		mp.setPriceLevelLimit(5);
		mp.setSpread(2);
		stratagy.setMp(mp);
		MarketEventHandler marketEventHandler = MarketEventEngine.addHandler();
		marketEventHandler.subscribeEvent(stratagy.getMarketContract().MyToString());
		Global.eventConcurrentHashMap.put(marketEventHandler.getHandlerStratagyThread().getId(), marketEventHandler);
    	//将策略添加至消费者中的策略集合
    	marketEventHandler.getHandlerStratagyThread().getStratagyConcurrentHashMap().put(stratagy.getId(), stratagy);
    	//将该消费者添加至可用消费者集合
    	Global.availableEventConcurrentHashMap.put(marketEventHandler.getHandlerStratagyThread().getId(), marketEventHandler);
    	//创建该策略与消费者的映射关系
    	Global.allEventConcurrentHashMap.put(stratagy.getId(), marketEventHandler);
	}
	
	@GetMapping("startStratagy1")
	public void startStratagy1() {
		Stratagy stratagy=new Stratagy();
		stratagy.setId(UUID.randomUUID().toString());
		stratagy.setName(UUID.randomUUID().toString());
		stratagy.setStatus(StratagyStatusEnum.RUNNING.toString());
		stratagy.setType(StratagyTypeEnum.M.toString());
		stratagy.setExpression("");
		Contract marketContract=new Contract();
		marketContract.setCode("6A18081");
		marketContract.setExchangeCode("CME");
		Contract activeContract=new Contract();
		activeContract.setCode("6A18019");
		activeContract.setExchangeCode("CME");
		stratagy.setActiveContract(activeContract);
		stratagy.setMarketContract(marketContract);
		MarketProvider mp=new MarketProvider();
		mp.setCurrBuyNum(0);
		mp.setCurrSaleNum(0);
		mp.setMaxBuyNum(1000500);
		mp.setMaxOrderNum(50);
		mp.setMaxSaleNum(1000500);
		mp.setMinOrderNum(10);
		mp.setPriceLevelLimit(5);
		mp.setSpread(2);
		stratagy.setMp(mp);
		MarketEventHandler marketEventHandler = MarketEventEngine.addHandler();
		marketEventHandler.subscribeEvent(stratagy.getMarketContract().MyToString());
		Global.eventConcurrentHashMap.put(marketEventHandler.getHandlerStratagyThread().getId(), marketEventHandler);
    	//将策略添加至消费者中的策略集合
    	marketEventHandler.getHandlerStratagyThread().getStratagyConcurrentHashMap().put(stratagy.getId(), stratagy);
    	//将该消费者添加至可用消费者集合
    	Global.availableEventConcurrentHashMap.put(marketEventHandler.getHandlerStratagyThread().getId(), marketEventHandler);
    	//创建该策略与消费者的映射关系
    	Global.allEventConcurrentHashMap.put(stratagy.getId(), marketEventHandler);
	}
	
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
	
}

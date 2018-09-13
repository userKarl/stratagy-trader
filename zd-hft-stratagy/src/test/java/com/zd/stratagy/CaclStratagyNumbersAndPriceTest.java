package com.zd.stratagy;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.assertj.core.util.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.shanghaizhida.beans.MarketInfo;
import com.shanghaizhida.beans.NetInfo;
import com.zd.business.constant.CommandEnum;
import com.zd.business.constant.RedisConst;
import com.zd.business.constant.StratagyStatusEnum;
import com.zd.business.constant.StratagyTypeEnum;
import com.zd.business.constant.TraderEnvEnum;
import com.zd.business.constant.order.BuySaleEnum;
import com.zd.business.entity.Arbitrage;
import com.zd.business.entity.Contract;
import com.zd.business.entity.Stratagy;
import com.zd.config.Global;
import com.zd.redis.RedisService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CaclStratagyNumbersAndPriceTest {

	@Autowired
	private RedisService redisService;
	
	/**
	 * 创建策略
	 */
	public void createStratagy() {
		Stratagy stratagy=new Stratagy();
		stratagy.setId(UUID.randomUUID().toString());
		stratagy.setName("测试策略");
		stratagy.setStatus(StratagyStatusEnum.INIT.toString());
		stratagy.setType(StratagyTypeEnum.A.toString());
		stratagy.setExpression("m+P2");
		Contract activeContract=new Contract();
		activeContract.setCode("6A1809");
		activeContract.setExchangeCode("CME");
		activeContract.setDirection(BuySaleEnum.BUY.getCode());
		activeContract.setEnv(TraderEnvEnum.ZD.getCode());
		activeContract.setPerFillNums(1);
		activeContract.setUpperTick(new BigDecimal("0.001"));
		activeContract.setMinMatchNums(5);
		stratagy.setActiveContract(activeContract);
		
		Contract marketContract=new Contract();
		marketContract.setCode("6A1811");
		marketContract.setExchangeCode("CME");
		marketContract.setDirection(BuySaleEnum.BUY.getCode());
		marketContract.setEnv(TraderEnvEnum.ZD.getCode());
		marketContract.setPerFillNums(1);
		marketContract.setUpperTick(new BigDecimal("0.0001"));
		marketContract.setMinMatchNums(5);
		List<Contract> marketContractList=Lists.newArrayList();
		marketContractList.add(marketContract);
		stratagy.setMarketContractList(marketContractList);
		
		Arbitrage arbitrage=new Arbitrage();
		arbitrage.setId(UUID.randomUUID().toString());
		arbitrage.setName("测试套利");
		arbitrage.setDirection(BuySaleEnum.BUY.getCode());
		stratagy.setArbitrage(arbitrage);
		redisService.hmSet(RedisConst.STRATAGY, "testStratagy", stratagy.MyToString());
	}
	
	/**
	 * 生产行情
	 * @throws InterruptedException 
	 */
	public void createMarket() throws InterruptedException {
		MarketInfo mi=new MarketInfo();
		mi.code="6A1809";
		mi.exchangeCode="CME";
		mi.buyNumber="10";
		mi.buyPrice="0.7012";
		mi.saleNumber="15";
		mi.salePrice="0.7215";
		Global.marketEventProducer.onData(mi.MyToString());
		
		Thread.sleep(10);
		MarketInfo mi2=new MarketInfo();
		mi2.code="6A1811";
		mi2.exchangeCode="CME";
		mi2.buyNumber="26";
		mi2.buyPrice="0.6806";
		mi2.saleNumber="18";
		mi2.salePrice="0.7018";
		Global.marketEventProducer.onData(mi2.MyToString());
	}
	
	/**
	 * 开始策略
	 * @throws InterruptedException 
	 */
	@Test
	public void startStratagy() throws InterruptedException {
		createStratagy();
		NetInfo ni=new NetInfo();
		ni.code=CommandEnum.STRATAGY_START.toString();
		ni.infoT="testStratagy";
		Global.centralEventProducer.onData(ni.MyToString());
		Thread.sleep(1000);
		createMarket();
	}
}

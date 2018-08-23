package com.zd;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.zd.config.Global;
import com.zd.engine.market.MarketEventEngine;
import com.zd.engine.market.MarketEventProducer;
import com.zd.netty.NettyServer;

import io.netty.channel.ChannelFuture;
import xyz.redtorch.trader.gateway.GatewaySetting;
import xyz.redtorch.web.service.TradingService;
import xyz.redtorch.web.vo.ResultVO;

/**
 * 感谢开源大神
 * https://github.com/sun0x00/RedTorch
 */
@SpringBootApplication
@RestController
@EnableAutoConfiguration(exclude={MongoAutoConfiguration.class})
@ComponentScan(basePackages= {"com.zd","xyz.redtorch"})
public class ZdHftCtpApplication implements CommandLineRunner{
	
	@Autowired
	private TradingService tradingService;
	
	@Autowired
	private NettyServer nettyServer;
	
	@Autowired
	private Global global;
	
	@RequestMapping(value = "/sendOrder",method = RequestMethod.POST)
	@ResponseBody
	public ResultVO sendOrder(@RequestBody JSONObject jsonObject) {

		ResultVO result = new ResultVO();
		
		String gatewayID = jsonObject.getString("gatewayID");
		String rtSymbol = jsonObject.getString("rtSymbol");
		double price = jsonObject.getDouble("price");
		int  volume = jsonObject.getInteger("volume");
		String priceType = jsonObject.getString("priceType");
		String direction = jsonObject.getString("direction");
		String offset = jsonObject.getString("offset");
		
		if(StringUtils.isEmpty(gatewayID)
				||StringUtils.isEmpty(rtSymbol)
				||StringUtils.isEmpty(priceType)
				||StringUtils.isEmpty(direction)
				||StringUtils.isEmpty(offset)
				|| volume<=0) {
			result.setResultCode(ResultVO.ERROR);
			result.setMessage("参数不正确");
			return result;
		}
		String orderID = tradingService.sendOrder(gatewayID, rtSymbol, price, volume, priceType, direction, offset);
		result.setData(orderID);
		
		return result;
	}
	
	@RequestMapping(value = "/cancelOrder",method = RequestMethod.POST)
	@ResponseBody
	public ResultVO cancelOrder(@RequestBody JSONObject jsonObject) {

		ResultVO result = new ResultVO();
		
		if(StringUtils.isEmpty(jsonObject.getString("rtOrderID"))) {
			result.setResultCode(ResultVO.ERROR);
		}else {
			tradingService.cancelOrder(jsonObject.getString("rtOrderID"));
		}
		
		return result;
	}
	
	@RequestMapping(value = "/cancelAllOrders",method = RequestMethod.POST)
	@ResponseBody
	public ResultVO cancelAllOrders(@RequestBody JSONObject jsonObject) {

		ResultVO result = new ResultVO();
		
		tradingService.cancelAllOrders();
		
		return result;
	}
	
	@RequestMapping(value = "/subscribe",method = RequestMethod.POST)
	@ResponseBody
	public ResultVO subscribe(@RequestBody JSONObject jsonObject) {

		ResultVO result = new ResultVO();
		
		if(!tradingService.subscribe(jsonObject.getString("rtSymbol"), jsonObject.getString("gatewayID"))) {
			result.setResultCode(ResultVO.ERROR);
		}
		
		return result;
	}
	@RequestMapping(value = "/unsubscribe",method = RequestMethod.POST)
	@ResponseBody
	public ResultVO unsubscribe(@RequestBody JSONObject jsonObject) {

		ResultVO result = new ResultVO();
		
		if(!tradingService.unsubscribe(jsonObject.getString("rtSymbol"), jsonObject.getString("gatewayID"))) {
			result.setResultCode(ResultVO.ERROR);
		}
		
		return result;
	}
	
	@RequestMapping(value = "/saveGateway",method = RequestMethod.POST)
	@ResponseBody
	public ResultVO saveGateway(@RequestBody JSONObject jsonObject) {

		ResultVO result = new ResultVO();
		
		GatewaySetting gatewaySetting = jsonObject.toJavaObject(GatewaySetting.class);
		
		tradingService.saveOrUpdateGatewaySetting(gatewaySetting);
		
		return result;
	}
	
	
	@RequestMapping("/zeus/loadStrategy")
	@ResponseBody
	public ResultVO zeusStrategyLoad() {

		ResultVO result = new ResultVO();
		
		tradingService.zeusLoadStrategy();
		
		//result.setData(tradingService.getGatewaySettings());
		
		return result;
	}
	@RequestMapping("/zeus/getStrategyInfos")
	@ResponseBody
	public ResultVO zeusGetStrategyInfos() {

		ResultVO result = new ResultVO();
		
		List<Map<String,Object>> strategyInfos = tradingService.zeusGetStrategyInfos();
		
		result.setData(strategyInfos);
		
		return result;
	}
	
	@RequestMapping(value="/zeus/changeStrategyStatus",method=RequestMethod.POST)
	@ResponseBody
	public ResultVO zeusChangeStrategyStatus(@RequestBody JSONObject jsonObject) {

		ResultVO result = new ResultVO();
		
		if(!jsonObject.containsKey("actionType")) {
			result.setResultCode(ResultVO.ERROR);
			return result;
		}
		
		String type = jsonObject.getString("actionType");
		
		String strategyID = jsonObject.getString("strategyID");
		
		if(!type.contains("All")) {
			if(StringUtils.isEmpty(strategyID)) {
				result.setResultCode(ResultVO.ERROR);
				return result;
			}
		}
		if("init".equals(type)) {
			tradingService.zeusInitStrategy(strategyID);
		}else if("start".equals(type)) {
			tradingService.zeusSartStrategy(strategyID);
		}else if("stop".equals(type)) {
			tradingService.zeusStopStrategy(strategyID);
		}else if("initAll".equals(type)) {
			tradingService.zeusInitAllStrategy();
		}else if("startAll".equals(type)) {
			tradingService.zeusSartAllStrategy();
		}else if("stopAll".equals(type)) {
			tradingService.zeusStopAllStrategy();
		}else if("reload".equals(type)) {
			tradingService.zeusReloadStrategy(strategyID);
		}
		
		return result;
	}
	
	@RequestMapping("/getGatewaySettings")
	@ResponseBody
	public ResultVO getGatewaySettings() {

		ResultVO result = new ResultVO();
		
		result.setData(tradingService.getGatewaySettings());
		
		return result;
	}
	
	@RequestMapping(value = "/deleteGateway",method= RequestMethod.POST)
	@ResponseBody
	public ResultVO deleteGateway(@RequestBody JSONObject jsonObject) {

		ResultVO result = new ResultVO();
		
		
		if(!jsonObject.containsKey("gatewayID")) {
			result.setResultCode(ResultVO.ERROR);
			return result;
		}
		
		tradingService.deleteGateway(jsonObject.getString("gatewayID"));
		
		return result;
	}
	@RequestMapping(value ="/changeGatewayConnectStatus",method= RequestMethod.POST)
	@ResponseBody
	public ResultVO changeGatewayConnectStatus(@RequestBody JSONObject jsonObject) {

		ResultVO result = new ResultVO();
		
		if(!jsonObject.containsKey("gatewayID")) {
			result.setResultCode(ResultVO.ERROR);
			return result;
		}
		tradingService.changeGatewayConnectStatus(jsonObject.getString("gatewayID"));
		
		return result;
	}
	
	
	@RequestMapping("/getAccounts")
	@ResponseBody
	public ResultVO getAccounts() {

		ResultVO result = new ResultVO();
		
		result.setData(tradingService.getAccounts());
		return result;
	}
	
	@RequestMapping("/getTrades")
	@ResponseBody
	public ResultVO getTrades() {

		ResultVO result = new ResultVO();
		
		result.setData(tradingService.getTrades());
		return result;
	}
	
	@RequestMapping("/getOrders")
	@ResponseBody
	public ResultVO getOrders() {

		ResultVO result = new ResultVO();
		
		result.setData(tradingService.getOrders());
		return result;
	}
	
	@RequestMapping("/getLocalPositionDetails")
	@ResponseBody
	public ResultVO getLocalPositionDetails() {

		ResultVO result = new ResultVO();

		result.setData(tradingService.getLocalPositionDetails());
		return result;
	}
	
	@RequestMapping("/getPositions")
	@ResponseBody
	public ResultVO getPositions() {

		ResultVO result = new ResultVO();
		
		result.setData(tradingService.getPositions());
		return result;
	}
	
	
	@RequestMapping("/getContracts")
	@ResponseBody
	public ResultVO getContracts() {

		ResultVO result = new ResultVO();

		result.setData(tradingService.getContracts());
		return result;
	}
	
	@RequestMapping("/getLogs")
	@ResponseBody
	public ResultVO getLogs() {

		ResultVO result = new ResultVO();
		result.setData(tradingService.getLogDatas());
		return result;
	}
	
    @Bean 
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
    	return new PropertySourcesPlaceholderConfigurer();
    }
	
	public static void main(String[] args) {
		SpringApplication.run(ZdHftCtpApplication.class, args);
		
	}

	@Override
	public void run(String... args) throws Exception {
		// 开启行情的Disruptor队列
		MarketEventProducer mep=new MarketEventProducer(MarketEventEngine.getRingBuffer());
    	Global.marketEventProducer=mep;
		    	
    	//开启二级行情订阅服务器
    	Thread nettyServerThread=new Thread(new Runnable() {
			
			@Override
			public void run() {
				ChannelFuture future = nettyServer.start(global.marketCtpServerHost, global.marketCtpServerPort);
				future.channel().closeFuture().syncUninterruptibly();
			}
		});
    	nettyServerThread.start();
    	
    	Global.tradingService=tradingService;
    	
		tradingService.changeGatewayConnectStatus("9999.simnow.187.10030");
	}
}

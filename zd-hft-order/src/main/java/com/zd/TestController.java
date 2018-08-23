package com.zd;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.shanghaizhida.beans.CommandCode;
import com.shanghaizhida.beans.NetInfo;
import com.shanghaizhida.beans.OrderInfo;
import com.zd.business.service.TraderDataFeed;
import com.zd.config.Global;
import com.zd.config.NettyGlobal;


@RestController
public class TestController {

	@Autowired
	private NettyGlobal nettyGlobal;
	
	@GetMapping("test")
	public void test() {
		System.out.println(nettyGlobal.nettyOrderServerPort);
	}
	
	
	@GetMapping("connect")
	public void connect(String s) {
		String account1="demo000604";
		TraderDataFeed tdf1=new TraderDataFeed("222.73.119.230", "7003", account1, "888888","");
		Global.accountTraderMap.put(account1, tdf1);
		tdf1.start();
		String account2="demo003498";
		TraderDataFeed tdf2=new TraderDataFeed("222.73.119.230", "7003", account2, "888888","");
		Global.accountTraderMap.put(account2, tdf2);
		tdf2.start();
	}
	
	@GetMapping("order/{userAccount}")
	public void order(@PathVariable String userAccount) {
		TraderDataFeed tdf = Global.accountTraderMap.get(userAccount);
		if(tdf!=null) {
			NetInfo niBuy=new NetInfo();
			niBuy.code=CommandCode.ORDER;
			OrderInfo oi=orderInfoBuy(tdf.getUserAccount(),tdf.getUserPassWd());
			niBuy.infoT=oi.MyToString();
			tdf.sendOrder(oi);
		}
	}
	
	public OrderInfo orderInfoBuy(String userAccount,String userPassWd) {
		OrderInfo orderInfo=new OrderInfo();
		orderInfo.userId = userAccount;
        orderInfo.tradePwd = userPassWd;
        orderInfo.FIsRiskOrder = "";
        orderInfo.userType = "I";
        orderInfo.exchangeCode = "CME";
        orderInfo.code = "6A1809";
        // 买还是卖：1=buy 2=sell ，修改此值实现买和卖
        orderInfo.buySale = "1"; //mBuySale;
        orderInfo.orderNumber = "1"; //mOrderNum;
        orderInfo.orderPrice = "0.6801"; //mPriceBuySell;
        orderInfo.tradeType = "";
        // 定单类型：1=限价单, 2=市价单，3=限价止损（stop to limit），4=止损（stop to market）
        orderInfo.priceType = "1";

        orderInfo.flID = "";
        orderInfo.strategyId = "";
        orderInfo.addReduce = "1";
        orderInfo.accountNo = "00023118";
		return orderInfo;
	}
}

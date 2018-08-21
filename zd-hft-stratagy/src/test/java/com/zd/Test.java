package com.zd;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.shanghaizhida.beans.NetInfo;
import com.shanghaizhida.beans.OrderInfo;

public class Test {

	private static final Logger logger = LoggerFactory.getLogger(Test.class);

	private static ConcurrentLinkedQueue<String> queue = new ConcurrentLinkedQueue<>();

	private static final String fIsRiskOrder = "";
	private static final String userType = "I";
	private static final String exchangeCode = "CME";
	private static final String code = "6A1808";

	public static void order() {
		for (int m = 0; m < 100; m++) {
			long l = System.nanoTime();
			List<String> list = Lists.newArrayList();

			double sale[] = { 13.4, 13.6, 13.8, 14.2, 14.5 };
			double buy[] = { 13.2, 13.1, 13, 12.8, 12.6 };

			double saleNum[] = { 120, 156, 486, 259, 469 };
			double buyNum[] = { 35, 169, 158, 249, 1112 };

			double spread = 2;
			for (int i = 0; i < sale.length; i++) {
				for (int j = 0; j < buy.length; j++) {
					if (sale[i] - buy[j] <= spread) {
						list.add(i + "@" + j);
					}
				}
			}

			double maxOrderNum = 50;// 最大下单量
			double minOrderNum = 10;// 预设的最小下单量
			double maxBuyNum = 500;// 最大多单持仓量
			double maxSaleNum = 500;// 最大空单持仓量
			double currBuyNum = 0;// 当前多单持仓量
			double currSaleNum = 0;// 当前空单持仓量

			for (String s : list) {
				// 剩余可下单总量
				double minRemainNum = Math.min(maxBuyNum - currBuyNum, maxSaleNum - currSaleNum);
				if (minRemainNum < minOrderNum) {
					// 单边下单
					continue;
				} else {
					// 双边下单
					String[] split = s.split("@");
					int saleIndex = Integer.parseInt(split[0]);
					int buyIndex = Integer.parseInt(split[1]);
					// logger.info("卖{}买{}",saleIndex+1,buyIndex+1);
					while (true) {
						/*
						 * 确定单次下单量
						 * 
						 */
						double min = Math.min(maxBuyNum - currBuyNum, maxSaleNum - currSaleNum);
						if (min < minOrderNum) {
							// 单边下单
							break;
						} else {
							double maxOrderNumLimit = 0;
							if (min > maxOrderNum) {
								maxOrderNumLimit = maxOrderNum;
							} else {
								maxOrderNumLimit = min;
							}
							double min2 = Math.min(buyNum[buyIndex], saleNum[saleIndex]);
							if (min2 < minOrderNum) {
								break;
							} else {
								double orderNum = 0;
								if (min2 > minOrderNum && min2 < maxOrderNumLimit) {
									orderNum = min2;
								} else if (min2 >= maxOrderNumLimit) {
									orderNum = maxOrderNumLimit;
								}
								currBuyNum += orderNum;
								currSaleNum += orderNum;
								buyNum[buyIndex] -= orderNum;
								saleNum[saleIndex] -= orderNum;
								OrderInfo oiBuy = generateOrder(fIsRiskOrder,userType,exchangeCode,code,"1", String.valueOf(orderNum),
										String.valueOf(buy[buyIndex]), "", "1", "1");
								OrderInfo oiSale = generateOrder(fIsRiskOrder,userType,exchangeCode,code,"2", String.valueOf(orderNum),
										String.valueOf(sale[saleIndex]), "", "1", "1");
								NetInfo ni = new NetInfo();
								ni.infoT = oiBuy.MyToString() + "," + oiSale.MyToString();
								queue.add(ni.MyToString());
								// logger.info("下单档位：卖{}买{}，下单量：{}，买持仓：{}，卖持仓{},卖档位剩余量：{},买档位剩余量：{}",saleIndex+1,buyIndex+1,min2,currBuyNum,currSaleNum,saleNum[saleIndex],buyNum[buyIndex]);
							}

						}

					}
				}
			}
			System.out.println("耗时：" + (System.nanoTime() - l) / 1e6 + " ms");
			// while (true) {
			// String poll = queue.poll();
			// if (StringUtils.isNotBlank(poll)) {
			// System.out.println(poll);
			// } else {
			// break;
			// }
			// }
		}

	}

	public static OrderInfo generateOrder(String fIsRiskOrder, String userType, String exchangeCode, String code,
			String buySale, String orderNumber, String orderPrice, String tradeType, String priceType,
			String addReduce) {
		OrderInfo orderInfo = new OrderInfo();
		orderInfo.FIsRiskOrder = fIsRiskOrder;
		orderInfo.userType = userType;
		orderInfo.exchangeCode = exchangeCode;
		orderInfo.code = code;
		// 买还是卖：1=buy 2=sell ，修改此值实现买和卖
		orderInfo.buySale = buySale; // mBuySale;
		orderInfo.orderNumber = orderNumber; // mOrderNum;
		orderInfo.orderPrice = orderPrice; // mPriceBuySell;
		orderInfo.tradeType = tradeType;
		// 定单类型：1=限价单, 2=市价单，3=限价止损（stop to limit），4=止损（stop to market）
		orderInfo.priceType = priceType;

		orderInfo.flID = "";
		orderInfo.strategyId = "";
		orderInfo.addReduce = addReduce;
		return orderInfo;
	}

	public static OrderInfo orderInfoBuy() {
		OrderInfo orderInfo = new OrderInfo();
		orderInfo.FIsRiskOrder = "";
		orderInfo.userType = "I";
		orderInfo.exchangeCode = "CME";
		orderInfo.code = "6A1808";
		// 买还是卖：1=buy 2=sell ，修改此值实现买和卖
		orderInfo.buySale = "1"; // mBuySale;
		orderInfo.orderNumber = "1"; // mOrderNum;
		orderInfo.orderPrice = "0.6801"; // mPriceBuySell;
		orderInfo.tradeType = "";
		// 定单类型：1=限价单, 2=市价单，3=限价止损（stop to limit），4=止损（stop to market）
		orderInfo.priceType = "1";

		orderInfo.flID = "";
		orderInfo.strategyId = "";
		orderInfo.addReduce = "1";
		orderInfo.accountNo = "00021344";
		return orderInfo;
	}

	public static OrderInfo orderInfoSale() {
		OrderInfo orderInfo = new OrderInfo();
		orderInfo.FIsRiskOrder = "";
		orderInfo.userType = "I";
		orderInfo.exchangeCode = "CME";
		orderInfo.code = "6A1808";
		// 买还是卖：1=buy 2=sell ，修改此值实现买和卖
		orderInfo.buySale = "2"; // mBuySale;
		orderInfo.orderNumber = "1"; // mOrderNum;
		orderInfo.orderPrice = "0.8801"; // mPriceBuySell;
		orderInfo.tradeType = "";
		// 定单类型：1=限价单, 2=市价单，3=限价止损（stop to limit），4=止损（stop to market）
		orderInfo.priceType = "1";

		orderInfo.flID = "";
		orderInfo.strategyId = "";
		orderInfo.addReduce = "1";
		orderInfo.accountNo = "00021416";
		return orderInfo;
	}

	public static void main(String[] args) throws InterruptedException {

		order();

	}
}

/**
 * 14.5 469 14.2 259 13.8 486 13.6 156-50-34-50-22 13.4 120-35-50-35
 * ------------ 13.2 35-35 13.1 169-50-35-50-34 13 158-50-22 12.8 249 12.6 112
 * 
 * 卖1买1 35 卖1买2 50 卖1买2 35 卖2买2 50 卖2买2 34 卖2买3 50 卖2买3 22
 * 
 * 
 * double saleNum[]= {120,156,486,259,469}; double buyNum[]=
 * {35,169,158,249,112};
 * 
 * double spread=0.6; double maxOrderNum=50;//最大下单量 double
 * minOrderNum=10;//预设的最小下单量 double maxBuyNum=500;//最大多单持仓量 double
 * maxSaleNum=500;//最大空单持仓量 double currBuyNum=0;//当前多单持仓量 double
 * currSaleNum=0;//当前空单持仓量
 * 
 * 
 * 
 */

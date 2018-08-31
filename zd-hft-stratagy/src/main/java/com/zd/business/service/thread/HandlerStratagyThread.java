package com.zd.business.service.thread;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.shanghaizhida.beans.MarketInfo;
import com.shanghaizhida.beans.NetInfo;
import com.shanghaizhida.beans.OrderInfo;
import com.zd.business.common.BigDeciamlAnalysize;
import com.zd.business.constant.CommandEnum;
import com.zd.business.constant.StratagyStatusEnum;
import com.zd.business.constant.StratagyTypeEnum;
import com.zd.business.constant.TraderEnvEnum;
import com.zd.business.constant.order.AddReduceEnum;
import com.zd.business.constant.order.BuySaleEnum;
import com.zd.business.constant.order.PriceTypeEnum;
import com.zd.business.constant.order.RiskOrderEnum;
import com.zd.business.constant.order.UserTypeEnum;
import com.zd.business.entity.Contract;
import com.zd.business.entity.MarketProvider;
import com.zd.business.entity.Stratagy;
import com.zd.business.entity.Tick;
import com.zd.config.Global;

/**
 * 处理策略计算
 * 
 * @author user
 *
 */
public class HandlerStratagyThread implements Runnable {

	private static final Logger logger = LoggerFactory.getLogger(HandlerStratagyThread.class);

	private volatile boolean isStillRunning = false;

	private volatile Thread thread;

	private ConcurrentLinkedQueue<String> queue = new ConcurrentLinkedQueue<>();

	private String id;

	private double buyAllPrice[] = new double[5];
	private double saleAllPrice[] = new double[5];
	private double buyAllNumber[] = new double[5];
	private double saleAllNumber[] = new double[5];

	public HandlerStratagyThread(String id) {
		this.id = id;
	}

	// 策略容器
	private ConcurrentHashMap<String, Stratagy> stratagyConcurrentHashMap = new ConcurrentHashMap<>();

	public void start() {
		thread = new Thread(this);
		thread.start();
		isStillRunning = true;
	}

	public void stop() {
		if (thread != null) {
			isStillRunning = false;
			thread.interrupt();
			thread = null;
		}
	}

	@Override
	public void run() {
		while (isStillRunning) {
			String command = queue.poll();
			if (StringUtils.isNotBlank(command)) {
				long l = System.nanoTime();
				// [start]计算策略
				if (command.startsWith(CommandEnum.STRATAGY_CACL.toString())) {
					for (Entry<String, Stratagy> entry : stratagyConcurrentHashMap.entrySet()) {
						try {
							Stratagy stratagy = entry.getValue();
							NetInfo ni = new NetInfo();
							ni.code = CommandEnum.STRATAGY_STRIKE.toString();
							if (!StratagyStatusEnum.STOP.toString().equals(stratagy.getStatus())
									&& !StratagyStatusEnum.PAUSE.toString().equals(stratagy.getStatus())) {
								// [start] 市商
								if (StratagyTypeEnum.M.toString().equals(stratagy.getType())) {
									// 根据套利公式和档位限制计算市商合约行情
									// TODO
									// MarketProvider mp = new MarketProvider();
									// MarketInfo mi = mp.getMarketInfo();
									MarketProvider mp = stratagy.getMp();
									MarketInfo mi = new MarketInfo();
									List<String> list = Lists.newArrayList();
									buyAllPrice[0] = Double.valueOf(mi.buyPrice);
									buyAllPrice[1] = Double.valueOf(mi.buyPrice2);
									buyAllPrice[2] = Double.valueOf(mi.buyPrice3);
									buyAllPrice[3] = Double.valueOf(mi.buyPrice4);
									buyAllPrice[4] = Double.valueOf(mi.buyPrice5);

									saleAllPrice[0] = Double.valueOf(mi.salePrice);
									saleAllPrice[1] = Double.valueOf(mi.salePrice2);
									saleAllPrice[2] = Double.valueOf(mi.salePrice3);
									saleAllPrice[3] = Double.valueOf(mi.salePrice4);
									saleAllPrice[4] = Double.valueOf(mi.salePrice5);

									// double buyAllPrice[] = { Double.valueOf(mi.buyPrice),
									// Double.valueOf(mi.buyPrice2),
									// Double.valueOf(mi.buyPrice3), Double.valueOf(mi.buyPrice4),
									// Double.valueOf(mi.buyPrice5) };
									// double saleAllPrice[] = { Double.valueOf(mi.salePrice),
									// Double.valueOf(mi.salePrice2),
									// Double.valueOf(mi.salePrice3), Double.valueOf(mi.salePrice4),
									// Double.valueOf(mi.salePrice5) };

									double buy[] = new double[mp.getPriceLevelLimit()];
									double sale[] = new double[mp.getPriceLevelLimit()];
									for (int i = 0; i < mp.getPriceLevelLimit(); i++) {
										buy[i] = buyAllPrice[i];
										sale[i] = saleAllPrice[i];
									}
									for (int i = 0; i < sale.length; i++) {
										for (int j = 0; j < buy.length; j++) {
											if (sale[i] - buy[j] < mp.getSpread()) {
												list.add(i + "@" + j);
											}
										}
									}
									if (list.size() > 0) {
										buyAllNumber[0] = Double.valueOf(mi.buyNumber);
										buyAllNumber[1] = Double.valueOf(mi.buyNumber2);
										buyAllNumber[2] = Double.valueOf(mi.buyNumber3);
										buyAllNumber[3] = Double.valueOf(mi.buyNumber4);
										buyAllNumber[4] = Double.valueOf(mi.buyNumber5);

										saleAllNumber[0] = Double.valueOf(mi.saleNumber);
										saleAllNumber[1] = Double.valueOf(mi.saleNumber2);
										saleAllNumber[2] = Double.valueOf(mi.saleNumber3);
										saleAllNumber[3] = Double.valueOf(mi.saleNumber4);
										saleAllNumber[4] = Double.valueOf(mi.saleNumber5);

										// double buyAllNumber[] = { Double.valueOf(mi.buyNumber),
										// Double.valueOf(mi.buyNumber2),
										// Double.valueOf(mi.buyNumber3), Double.valueOf(mi.buyNumber4),
										// Double.valueOf(mi.buyNumber5) };
										// double saleAllNumber[] = { Double.valueOf(mi.saleNumber),
										// Double.valueOf(mi.saleNumber2),
										// Double.valueOf(mi.saleNumber3), Double.valueOf(mi.saleNumber4),
										// Double.valueOf(mi.saleNumber5) };
										double buyNum[] = new double[mp.getPriceLevelLimit()];
										double saleNum[] = new double[mp.getPriceLevelLimit()];
										for (int i = 0; i < mp.getPriceLevelLimit(); i++) {
											buyNum[i] = buyAllNumber[i];
											saleNum[i] = saleAllNumber[i];
										}

										double maxOrderNum = mp.getMaxOrderNum();// 最大下单量
										double minOrderNum = mp.getMinOrderNum();// 最小下单量
										double maxBuyNum = mp.getMaxBuyNum();// 最大多单持仓量
										double maxSaleNum = mp.getMaxSaleNum();// 最大空单持仓量
										double currBuyNum = 0;// 当前多单持仓量
										double currSaleNum = 0;// 当前空单持仓量

										for (String s : list) {
											// 剩余可下单总量
											double minRemainNum = Math.min(maxBuyNum - currBuyNum,
													maxSaleNum - currSaleNum);
											if (minRemainNum < minOrderNum) {
												// 单边下单
												continue;
											} else {
												// 双边下单
												String[] split = s.split("@");
												int saleIndex = Integer.parseInt(split[0]);
												int buyIndex = Integer.parseInt(split[1]);
												while (true) {
													/*
													 * 确定单次下单量
													 * 
													 */
													double min = Math.min(maxBuyNum - currBuyNum,
															maxSaleNum - currSaleNum);
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
															if (orderNum > 0) {
																currBuyNum += orderNum;
																currSaleNum += orderNum;
																buyNum[buyIndex] -= orderNum;
																saleNum[saleIndex] -= orderNum;
																// 将下单信息发送至下单服务器
																OrderInfo oiBuy = generateOrder(
																		RiskOrderEnum.C.toString(),
																		UserTypeEnum.COMMON.toString(), mi.exchangeCode,
																		mi.code, BuySaleEnum.BUY.toString(),
																		String.valueOf(orderNum),
																		String.valueOf(buy[buyIndex]),
																		PriceTypeEnum.LIMIT.toString(),
																		AddReduceEnum.TAKE.toString());
																OrderInfo oiSale = generateOrder(
																		RiskOrderEnum.C.toString(),
																		UserTypeEnum.COMMON.toString(), mi.exchangeCode,
																		mi.code, BuySaleEnum.SALE.toString(),
																		String.valueOf(orderNum),
																		String.valueOf(sale[saleIndex]),
																		PriceTypeEnum.LIMIT.toString(),
																		AddReduceEnum.TAKE.toString());
																ni.infoT = oiBuy.MyToString() + ","
																		+ oiSale.MyToString();
																// Global.orderEventProducer.onData(ni.MyToString());
															} else {
																break;
															}

														}
													}

												}
											}
										}
									}
								}
								// [end]

								// [start] 套利
								else if (StratagyTypeEnum.A.toString().equals(stratagy.getType())) {
									List<String> caclBuyPriceList=Lists.newArrayList();//计算策略买价时各腿价格列表
									List<String> caclSalePriceList=Lists.newArrayList();//计算策略卖价时各腿价格列表
									/**
									 * 计算套利买卖量
									 */
									int acBuyNums=0;//主动腿经过计算的买量
									int acSaleNums=0;//主动腿经过计算的卖量
									int minBuyNums=acBuyNums;//主被动腿之间最小的买量
									int minSaleNums=acSaleNums;//主被动腿之间最小的卖量
									Contract arbitrageBuyContract=null;//套利买量最小的合约
									Contract arbitrageSaleContract=null;//套利卖量最小的合约
									//主动合约
									Contract activeContrct=stratagy.getActiveContract();
									arbitrageBuyContract=activeContrct;
									arbitrageSaleContract=activeContrct;
									if(activeContrct!=null) {
										if(TraderEnvEnum.ZD.toString().equals(activeContrct.getEnv())) {
											MarketInfo mi = Global.zdContractMap.get(activeContrct.getCode());
											acBuyNums=(Integer.parseInt(mi.buyNumber))/activeContrct.getPerFillNums();
											acSaleNums=(Integer.parseInt(mi.saleNumber))/activeContrct.getPerFillNums();
											activeContrct.setBuyPrice(new BigDecimal(mi.buyPrice));
											activeContrct.setSalePrice(new BigDecimal(mi.salePrice));
										}else if(TraderEnvEnum.CTP.getCode().equals(activeContrct.getEnv())){
											Tick tick=Global.ctpContractMap.get(activeContrct.getCode());
											acBuyNums=tick.getAskVolume1()/activeContrct.getPerFillNums();
											acSaleNums=tick.getBidVolume1()/activeContrct.getPerFillNums();
											activeContrct.setBuyPrice(BigDecimal.valueOf(tick.getAskPrice1()));
											activeContrct.setSalePrice(BigDecimal.valueOf(tick.getBidPrice1()));
										}
										if(stratagy.getArbitrage().getDirection().equals(activeContrct.getDirection())) {
											caclBuyPriceList.add(String.valueOf(activeContrct.getBuyPrice()));
											caclSalePriceList.add(String.valueOf(activeContrct.getSalePrice()));
										}else {
											caclBuyPriceList.add(String.valueOf(activeContrct.getSalePrice()));
											caclSalePriceList.add(String.valueOf(activeContrct.getBuyPrice()));
										}
									}
									List<Contract> marketContractList = stratagy.getMarketContractList();
									if(marketContractList!=null && marketContractList.size()>0) {
										for(Contract marketContract:marketContractList) {
											int mcBuyNums=1;
											int mcSaleNums=1;
											if(TraderEnvEnum.ZD.getCode().equals(marketContract.getEnv())) {
												MarketInfo mi = Global.zdContractMap.get(marketContract.getCode());
												//被动腿经过计算的买量
												mcBuyNums=(Integer.parseInt(mi.buyNumber))/marketContract.getPerFillNums();
												//被动腿经过计算的卖量
												mcSaleNums=(Integer.parseInt(mi.saleNumber))/marketContract.getPerFillNums();
												marketContract.setBuyPrice(new BigDecimal(mi.buyPrice));
												marketContract.setSalePrice(new BigDecimal(mi.salePrice));
											}else if(TraderEnvEnum.CTP.getCode().equals(marketContract.getEnv())){
												Tick tick=Global.ctpContractMap.get(marketContract.getCode());
												//被动腿经过计算的买量
												mcBuyNums=tick.getAskVolume1()/marketContract.getPerFillNums();
												//被动腿经过计算的卖量
												mcSaleNums=tick.getBidVolume1()/marketContract.getPerFillNums();
												marketContract.setBuyPrice(BigDecimal.valueOf(tick.getAskPrice1()));
												marketContract.setSalePrice(BigDecimal.valueOf(tick.getBidPrice1()));
											}
											marketContract.setBuyNums(mcBuyNums);
											marketContract.setSaleNums(mcSaleNums);
											if(mcBuyNums<minBuyNums) {
												minBuyNums=mcBuyNums;
												arbitrageBuyContract=marketContract;
											}
											if(mcSaleNums<minSaleNums) {
												minSaleNums=mcSaleNums;
												arbitrageSaleContract=marketContract;
											}
											if(stratagy.getArbitrage().getDirection().equals(marketContract.getDirection())) {
												caclBuyPriceList.add(String.valueOf(marketContract.getBuyPrice()));
												caclSalePriceList.add(String.valueOf(marketContract.getSalePrice()));
											}else {
												caclBuyPriceList.add(String.valueOf(marketContract.getSalePrice()));
												caclSalePriceList.add(String.valueOf(marketContract.getBuyPrice()));
											}
										}
										//根据套利买卖方向确定套利买卖量
										if(stratagy.getArbitrage().getDirection().equals(arbitrageBuyContract.getDirection())) {
											stratagy.getArbitrage().setBuyNums(arbitrageBuyContract.getBuyNums());
										}else {
											stratagy.getArbitrage().setBuyNums(arbitrageBuyContract.getSaleNums());
										}
										
										if(stratagy.getArbitrage().getDirection().equals(arbitrageSaleContract.getDirection())) {
											stratagy.getArbitrage().setSaleNums(arbitrageSaleContract.getSaleNums());
										}else {
											stratagy.getArbitrage().setSaleNums(arbitrageSaleContract.getBuyNums());
										}
									}
									
									/**
									 * 计算套利买卖价格
									 */
									if(StratagyStatusEnum.STRIKE.toString().equals(stratagy.getStatus())) {
										//策略已触发,直接根据策略公式计算行情价格
										String buyPrice=BigDeciamlAnalysize.dealEquation(BigDeciamlAnalysize.toSuffix(stratagy.getExpression(),caclBuyPriceList));
										String salePrice=BigDeciamlAnalysize.dealEquation(BigDeciamlAnalysize.toSuffix(stratagy.getExpression(),caclSalePriceList));
										stratagy.getArbitrage().setBuyPrice(new BigDecimal(buyPrice));
										stratagy.getArbitrage().setSalePrice(new BigDecimal(salePrice));
									}
									//TODO	
								}
								// [end]
							}
							logger.info("计算策略完成后耗时：{} ms", (System.nanoTime() - l) / 1e6);
							Thread.sleep(1);
						} catch (Exception e) {
							logger.error("策略计算线程异常：{}", e);
						}

					}

				}
				// [end]计算策略
				
			}
		}

	}

	public static OrderInfo generateOrder(String fIsRiskOrder, String userType, String exchangeCode, String code,
			String buySale, String orderNumber, String orderPrice, String priceType, String addReduce) {
		OrderInfo orderInfo = new OrderInfo();
		orderInfo.FIsRiskOrder = fIsRiskOrder;
		orderInfo.userType = userType;
		orderInfo.exchangeCode = exchangeCode;
		orderInfo.code = code;
		// 买还是卖：1=buy 2=sell ，修改此值实现买和卖
		orderInfo.buySale = buySale; // mBuySale;
		orderInfo.orderNumber = orderNumber; // mOrderNum;
		orderInfo.orderPrice = orderPrice; // mPriceBuySell;
		orderInfo.tradeType = "";
		// 定单类型：1=限价单, 2=市价单，3=限价止损（stop to limit），4=止损（stop to market）
		orderInfo.priceType = priceType;

		orderInfo.flID = "";
		orderInfo.strategyId = "";
		orderInfo.addReduce = addReduce;
		return orderInfo;
	}

	public ConcurrentLinkedQueue<String> getQueue() {
		return queue;
	}

	public void setQueue(ConcurrentLinkedQueue<String> queue) {
		this.queue = queue;
	}

	public ConcurrentHashMap<String, Stratagy> getStratagyConcurrentHashMap() {
		return stratagyConcurrentHashMap;
	}

	public void setStratagyConcurrentHashMap(ConcurrentHashMap<String, Stratagy> stratagyConcurrentHashMap) {
		this.stratagyConcurrentHashMap = stratagyConcurrentHashMap;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}

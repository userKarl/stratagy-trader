package com.zd.business.service.thread;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Lists;
import com.shanghaizhida.beans.CancelInfo;
import com.shanghaizhida.beans.CommandCode;
import com.shanghaizhida.beans.MarketInfo;
import com.shanghaizhida.beans.ModifyInfo;
import com.shanghaizhida.beans.NetInfo;
import com.shanghaizhida.beans.OrderInfo;
import com.shanghaizhida.beans.OrderResponseInfo;
import com.zd.business.common.BaseService;
import com.zd.business.common.BigDeciamalAnalysize;
import com.zd.business.common.CommonUtils;
import com.zd.business.constant.CommandEnum;
import com.zd.business.constant.RedisConst;
import com.zd.business.constant.RtConstant;
import com.zd.business.constant.StratagyStatusEnum;
import com.zd.business.constant.StratagyTypeEnum;
import com.zd.business.constant.TraderEnvEnum;
import com.zd.business.constant.TraderSymbolTypeEnum;
import com.zd.business.constant.order.AddReduceEnum;
import com.zd.business.constant.order.BuySaleEnum;
import com.zd.business.constant.order.PriceTypeEnum;
import com.zd.business.constant.order.UserTypeEnum;
import com.zd.business.entity.ArbitrageOrder;
import com.zd.business.entity.Contract;
import com.zd.business.entity.ContractOrder;
import com.zd.business.entity.Stratagy;
import com.zd.business.entity.TraderRef;
import com.zd.business.entity.ctp.Order;
import com.zd.business.entity.ctp.Tick;
import com.zd.config.Global;
import com.zd.websocket.NettyWSGlobal;

import lombok.extern.slf4j.Slf4j;

/**
 * 处理策略计算
 * 
 * @author user
 *
 */
@Slf4j
public class HandlerStratagyThread implements Runnable {

	private volatile boolean isStillRunning = false;

	private volatile Thread thread;

	private ConcurrentLinkedQueue<String> marketQueue = new ConcurrentLinkedQueue<>();

	private String id;

	private BaseService baseService;

	public HandlerStratagyThread(String id, BaseService baseService) {
		this.id = id;
		this.baseService = baseService;
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
			String command = marketQueue.poll();
			if (StringUtils.isNotBlank(command)) {
				// long l = System.nanoTime();
				// [start]计算策略
				if (command.startsWith(CommandEnum.STRATAGY_CACL.toString())) {
					boolean isStillCacling = true;
					for (Entry<String, Stratagy> entry : stratagyConcurrentHashMap.entrySet()) {
						try {
							Stratagy stratagy = entry.getValue();
							NetInfo ni = new NetInfo();
							ni.code = CommandEnum.STRATAGY_STRIKE.toString();
							if (!StratagyStatusEnum.STOP.toString().equals(stratagy.getStatus())
									&& !StratagyStatusEnum.PAUSE.toString().equals(stratagy.getStatus())) {

								// [start] 套利
								if (StratagyTypeEnum.A.toString().equals(stratagy.getType())) {
									List<String> caclBuyPriceList = Lists.newArrayList();// 计算策略买价时各腿价格列表
									List<String> caclSalePriceList = Lists.newArrayList();// 计算策略卖价时各腿价格列表
									List<String> currPriceList = Lists.newArrayList("m");// 主被动腿各合约的现价（用于推算主动腿价格，且主动腿值为m）
									// [start] 套利买卖量
									int acBuyNums = 0;// 主动腿经过计算的买量
									int acSaleNums = 0;// 主动腿经过计算的卖量
									int minBuyNums = 0;// 主被动腿之间最小的买量
									int minSaleNums = 0;// 主被动腿之间最小的卖量
									Contract arbitrageBuyContract = null;// 套利买量最小的合约
									Contract arbitrageSaleContract = null;// 套利卖量最小的合约
									// 主动合约
									Contract activeContract = stratagy.getActiveContract();
									if (activeContract != null) {
										if (TraderEnvEnum.ZD.getCode().equals(activeContract.getEnv())) {
											MarketInfo mi = Global.zdContractMap.get(
													activeContract.getExchangeCode() + "@" + activeContract.getCode());
											if (mi == null) {
												isStillCacling = false;
												continue;
											}
											activeContract.setBuyNums(Integer.parseInt(mi.buyNumber));
											activeContract.setSaleNums(Integer.parseInt(mi.saleNumber));
											acBuyNums = (Integer.parseInt(mi.buyNumber))
													/ activeContract.getPerFillNums();
											acSaleNums = (Integer.parseInt(mi.saleNumber))
													/ activeContract.getPerFillNums();
											if (!StratagyStatusEnum.STRIKE.toString().equals(stratagy.getStatus())) {
												if (activeContract.getBuyNums() >= activeContract.getMinMatchNums()) {
													activeContract.setBuyPrice(new BigDecimal(mi.buyPrice));
												} else {
													activeContract.setBuyPrice(new BigDecimal(mi.buyPrice)
															.add(new BigDecimal(stratagy.getArbitrage().getSafeDepth())
																	.multiply(activeContract.getUpperTick())));
												}
												if (activeContract.getSaleNums() >= activeContract.getMinMatchNums()) {
													activeContract.setSalePrice(new BigDecimal(mi.salePrice));
												} else {
													activeContract.setSalePrice(new BigDecimal(mi.salePrice).subtract(
															new BigDecimal(stratagy.getArbitrage().getSafeDepth())
																	.multiply(activeContract.getUpperTick())));
												}
											} else {
												activeContract.setBuyPrice(new BigDecimal(mi.buyPrice));
												activeContract.setSalePrice(new BigDecimal(mi.salePrice));
											}
										} else if (TraderEnvEnum.CTP.getCode().equals(activeContract.getEnv())) {
											Tick tick = Global.ctpContractMap.get(
													activeContract.getExchangeCode() + "@" + activeContract.getCode());
											if (tick == null) {
												isStillCacling = false;
												continue;
											}
											activeContract.setUpperLimit(BigDecimal.valueOf(tick.getUpperLimit()));
											activeContract.setLowerLimit(BigDecimal.valueOf(tick.getLowerLimit()));
											acBuyNums = tick.getAskVolume1() / activeContract.getPerFillNums();
											acSaleNums = tick.getBidVolume1() / activeContract.getPerFillNums();
											if (!StratagyStatusEnum.STRIKE.toString().equals(stratagy.getStatus())) {
												if (activeContract.getBuyNums() >= activeContract.getMinMatchNums()) {
													activeContract.setBuyPrice(BigDecimal.valueOf(tick.getAskPrice1()));
												} else {
													activeContract.setBuyPrice(BigDecimal.valueOf(tick.getAskPrice1())
															.add(new BigDecimal(stratagy.getArbitrage().getSafeDepth())
																	.multiply(activeContract.getUpperTick())));
												}
												if (activeContract.getSaleNums() >= activeContract.getMinMatchNums()) {
													activeContract
															.setSalePrice(BigDecimal.valueOf(tick.getBidPrice1()));
												} else {
													activeContract.setSalePrice(BigDecimal.valueOf(tick.getBidPrice1())
															.subtract(new BigDecimal(
																	stratagy.getArbitrage().getSafeDepth())
																			.multiply(activeContract.getUpperTick())));
												}
											} else {
												activeContract.setBuyPrice(BigDecimal.valueOf(tick.getAskPrice1()));
												activeContract.setSalePrice(BigDecimal.valueOf(tick.getBidPrice1()));
											}
										}
										if (stratagy.getArbitrage().getDirection()
												.equals(activeContract.getDirection())) {
											caclBuyPriceList.add(String.valueOf(activeContract.getBuyPrice()));
											caclSalePriceList.add(String.valueOf(activeContract.getSalePrice()));
										} else {
											caclBuyPriceList.add(String.valueOf(activeContract.getSalePrice()));
											caclSalePriceList.add(String.valueOf(activeContract.getBuyPrice()));
										}
										minBuyNums = acBuyNums;
										minSaleNums = acSaleNums;
										activeContract.setBuyNums(acBuyNums);
										activeContract.setSaleNums(acSaleNums);
										arbitrageBuyContract = activeContract;
										arbitrageSaleContract = activeContract;
									}
									if (!isStillCacling) {
										continue;
									}
									List<Contract> marketContractList = stratagy.getMarketContractList();
									if (marketContractList != null && marketContractList.size() > 0) {
										for (Contract marketContract : marketContractList) {
											int mcBuyNums = 1;
											int mcSaleNums = 1;
											if (TraderEnvEnum.ZD.getCode().equals(marketContract.getEnv())) {
												MarketInfo mi = Global.zdContractMap
														.get(marketContract.getExchangeCode() + "@"
																+ marketContract.getCode());
												if (mi == null) {
													isStillCacling = false;
													break;
												}
												currPriceList.add(mi.currPrice);
												marketContract.setBuyNums(Integer.parseInt(mi.buyNumber));
												marketContract.setSaleNums(Integer.parseInt(mi.saleNumber));
												// 被动腿经过计算的买量
												mcBuyNums = (Integer.parseInt(mi.buyNumber))
														/ marketContract.getPerFillNums();
												// 被动腿经过计算的卖量
												mcSaleNums = (Integer.parseInt(mi.saleNumber))
														/ marketContract.getPerFillNums();
												if (!StratagyStatusEnum.STRIKE.toString()
														.equals(stratagy.getStatus())) {
													if (marketContract.getBuyNums() >= marketContract
															.getMinMatchNums()) {
														marketContract.setBuyPrice(new BigDecimal(mi.buyPrice));
													} else {
														marketContract.setBuyPrice(new BigDecimal(mi.buyPrice).add(
																new BigDecimal(stratagy.getArbitrage().getSafeDepth())
																		.multiply(marketContract.getUpperTick())));
													}
													if (marketContract.getSaleNums() >= marketContract
															.getMinMatchNums()) {
														marketContract.setSalePrice(new BigDecimal(mi.salePrice));
													} else {
														marketContract.setSalePrice(
																new BigDecimal(mi.salePrice).subtract(new BigDecimal(
																		stratagy.getArbitrage().getSafeDepth())
																				.multiply(marketContract
																						.getUpperTick())));
													}

												} else {
													marketContract.setBuyPrice(new BigDecimal(mi.buyPrice));
													marketContract.setSalePrice(new BigDecimal(mi.salePrice));
												}
											} else if (TraderEnvEnum.CTP.getCode().equals(marketContract.getEnv())) {
												Tick tick = Global.ctpContractMap.get(marketContract.getExchangeCode()
														+ "@" + marketContract.getCode());
												if (tick == null) {
													isStillCacling = false;
													break;
												}
												marketContract.setUpperLimit(BigDecimal.valueOf(tick.getUpperLimit()));
												marketContract.setLowerLimit(BigDecimal.valueOf(tick.getLowerLimit()));
												currPriceList.add(String.valueOf(tick.getLastPrice()));
												// 被动腿经过计算的买量
												mcBuyNums = tick.getAskVolume1() / marketContract.getPerFillNums();
												// 被动腿经过计算的卖量
												mcSaleNums = tick.getBidVolume1() / marketContract.getPerFillNums();
												if (!StratagyStatusEnum.STRIKE.toString()
														.equals(stratagy.getStatus())) {
													if (marketContract.getBuyNums() >= marketContract
															.getMinMatchNums()) {
														marketContract
																.setBuyPrice(BigDecimal.valueOf(tick.getAskPrice1()));
													} else {
														marketContract
																.setBuyPrice(BigDecimal.valueOf(tick.getAskPrice1())
																		.add(new BigDecimal(
																				stratagy.getArbitrage().getSafeDepth())
																						.multiply(marketContract
																								.getUpperTick())));
													}
													if (marketContract.getSaleNums() >= marketContract
															.getMinMatchNums()) {
														marketContract
																.setSalePrice(BigDecimal.valueOf(tick.getBidPrice1()));
													} else {
														marketContract
																.setSalePrice(BigDecimal.valueOf(tick.getBidPrice1())
																		.subtract(new BigDecimal(
																				stratagy.getArbitrage().getSafeDepth())
																						.multiply(marketContract
																								.getUpperTick())));
													}

												} else {
													marketContract.setBuyPrice(BigDecimal.valueOf(tick.getAskPrice1()));
													marketContract
															.setSalePrice(BigDecimal.valueOf(tick.getBidPrice1()));
												}
											}
											marketContract.setBuyNums(mcBuyNums);
											marketContract.setSaleNums(mcSaleNums);
											if (mcBuyNums < minBuyNums) {
												minBuyNums = mcBuyNums;
												arbitrageBuyContract = marketContract;
											}
											if (mcSaleNums < minSaleNums) {
												minSaleNums = mcSaleNums;
												arbitrageSaleContract = marketContract;
											}
											if (stratagy.getArbitrage().getDirection()
													.equals(marketContract.getDirection())) {
												caclBuyPriceList.add(String.valueOf(marketContract.getBuyPrice()));
												caclSalePriceList.add(String.valueOf(marketContract.getSalePrice()));
											} else {
												caclBuyPriceList.add(String.valueOf(marketContract.getSalePrice()));
												caclSalePriceList.add(String.valueOf(marketContract.getBuyPrice()));
											}
										}
										if (!isStillCacling) {
											continue;
										}
										// 根据套利买卖方向确定套利买卖量
										if (stratagy.getArbitrage().getDirection()
												.equals(arbitrageBuyContract.getDirection())) {
											stratagy.getArbitrage().setBuyNums(arbitrageBuyContract.getBuyNums());
										} else {
											stratagy.getArbitrage().setBuyNums(arbitrageBuyContract.getSaleNums());
										}

										if (stratagy.getArbitrage().getDirection()
												.equals(arbitrageSaleContract.getDirection())) {
											stratagy.getArbitrage().setSaleNums(arbitrageSaleContract.getSaleNums());
										} else {
											stratagy.getArbitrage().setSaleNums(arbitrageSaleContract.getBuyNums());
										}
									} else {
										continue;
									}
									// [end]套利买卖量

									// if (stratagy.getArbitrage().getBuyNums() <= 0
									// || stratagy.getArbitrage().getSaleNums() <= 0) {
									// continue;
									// }
									// [start] 套利买卖价格
									String buyPrice = BigDeciamalAnalysize.dealEquation(
											BigDeciamalAnalysize.toSuffix(stratagy.getExpression(), caclBuyPriceList));
									String salePrice = BigDeciamalAnalysize.dealEquation(
											BigDeciamalAnalysize.toSuffix(stratagy.getExpression(), caclSalePriceList));
									stratagy.getArbitrage().setBuyPrice(new BigDecimal(buyPrice));
									stratagy.getArbitrage().setSalePrice(new BigDecimal(salePrice));
									// log.info("经过计算后的套利数据：{}", JacksonUtil.objToJsonPretty(stratagy));
									// TODO
									// 测试 ------将行情数据发送至页面
									String wsMarketInfo = stratagy.getId() + "@" + stratagy.getName() + "@"
											+ stratagy.getArbitrage().getBuyPrice() + "@"
											+ stratagy.getArbitrage().getBuyNums() + "@"
											+ stratagy.getArbitrage().getSalePrice() + "@"
											+ stratagy.getArbitrage().getSaleNums();
									NettyWSGlobal.queue.add(wsMarketInfo);
									
									
									// [end]套利买卖价格

									// [start] 循环策略内所有下单
									for (Entry<String, ArbitrageOrder> orderEntry : stratagy.getArbitrage()
											.getOrderMap().entrySet()) {
										ArbitrageOrder arbitrageOrder = orderEntry.getValue();
										ContractOrder activeContractOrder = arbitrageOrder.getActiveContractOrder();
										// 如果主动腿挂单和剩余挂单都为0，则跳过该委托
										if (activeContractOrder.getStayNums() <= 0
												&& activeContractOrder.getRemainNums() <= 0) {
											continue;
										}
										// [start] 策略触发逻辑
										boolean isStrike = false;
										// [start] 最小对盘量
										if (marketContractList != null && marketContractList.size() > 0) {
											for (Contract marketContract : marketContractList) {
												int percent = marketContract.getPerFillNums()
														/ activeContract.getPerFillNums();
												double minBuy = Math.min(stratagy.getArbitrage().getBuyNums() * percent,
														activeContractOrder.getRemainNums() * percent);
												double min2Buy = Math.min(minBuy,
														stratagy.getArbitrage().getMaxOnceActiveOrderNums() * percent
																/ activeContract.getPerFillNums());
												double minMatchBuy = BigDecimal.valueOf(min2Buy)
														.multiply(stratagy.getArbitrage().getMinMatchMultiple())
														.setScale(0, BigDecimal.ROUND_DOWN).doubleValue();

												double minSale = Math.min(
														stratagy.getArbitrage().getSaleNums() * percent,
														activeContractOrder.getRemainNums() * percent);
												double min2Sale = Math.min(minSale,
														stratagy.getArbitrage().getMaxOnceActiveOrderNums() * percent
																/ activeContract.getPerFillNums());
												double minMatchSale = BigDecimal.valueOf(min2Sale)
														.multiply(stratagy.getArbitrage().getMinMatchMultiple())
														.setScale(0, BigDecimal.ROUND_DOWN).doubleValue();

												if (BuySaleEnum.BUY.getCode().equals(marketContract.getDirection())) {
													// 合约为买
													if (stratagy.getArbitrage().getDirection()
															.equals(marketContract.getDirection())) {
														// 与套利方向一致，则判断买量
														activeContractOrder.setDirection(BuySaleEnum.BUY.getCode());
														if (marketContract.getBuyNums() >= minMatchBuy) {
															isStrike = true;
														} else {
															isStrike = false;
														}
													} else {
														// 相反，判断卖量
														activeContractOrder.setDirection(BuySaleEnum.SALE.getCode());
														if (marketContract.getSaleNums() >= minMatchSale) {
															isStrike = true;
														} else {
															isStrike = false;
														}
													}
												} else if (BuySaleEnum.SALE.getCode()
														.equals(marketContract.getDirection())) {
													// 合约为卖
													if (stratagy.getArbitrage().getDirection()
															.equals(marketContract.getDirection())) {
														activeContractOrder.setDirection(BuySaleEnum.SALE.getCode());
														// 与套利方向一致，则判断卖量
														if (marketContract.getSaleNums() >= minMatchSale) {
															isStrike = true;
														} else {
															isStrike = false;
														}
													} else {
														// 相反，判断买量
														activeContractOrder.setDirection(BuySaleEnum.BUY.getCode());
														if (marketContract.getBuyNums() >= minMatchBuy) {
															isStrike = true;
														} else {
															isStrike = false;
														}
													}
												}
											}
										}
										// [end] 最小对盘量

										// [start] 主动区域
										if (!isStrike) {
											// log.info("最小对盘量不满足，策略{}不触发", stratagy.getId());
											// 如果有主动腿挂单，则撤销该挂单
											if (TraderEnvEnum.ZD.getCode().equals(activeContract.getEnv())) {
												cancel(activeContractOrder);
											} else if (TraderEnvEnum.CTP.getCode().equals(activeContract.getEnv())) {
												ctpCancel(activeContractOrder);
											}
											continue;
										}
										// 推算主动腿价格
										BigDecimal activePrice = BigDeciamalAnalysize.cacl(stratagy.getExpression(),
												currPriceList, arbitrageOrder.getOrderPrice());
										int decimals = CommonUtils.getDecimalsUnits(activeContract.getUpperTick());
										if (BuySaleEnum.BUY.getCode().equals(activeContract.getDirection())) {
											activePrice = activePrice.setScale(decimals, BigDecimal.ROUND_DOWN);
											if (stratagy.getArbitrage().getActiveRegion() == 0) {
												// 取对盘价
												BigDecimal price = activeContract.getSalePrice();
												if (activePrice.compareTo(price) >= 0) {
													isStrike = true;
												} else {
													isStrike = false;
												}
											} else {
												// 取挂盘价，并加上主动区域
												BigDecimal price = activeContract.getBuyPrice();
												price.subtract(
														BigDecimal.valueOf(stratagy.getArbitrage().getActiveRegion())
																.multiply(activeContract.getUpperTick()));
												if (activePrice.compareTo(price) >= 0) {
													isStrike = true;
												} else {
													isStrike = false;
												}
											}

										} else if (BuySaleEnum.SALE.getCode().equals(activeContract.getDirection())) {
											activePrice = activePrice.setScale(decimals, BigDecimal.ROUND_UP);
											if (stratagy.getArbitrage().getActiveRegion() == 0) {
												// 取对盘价
												BigDecimal price = activeContract.getBuyPrice();
												if (activePrice.compareTo(price) <= 0) {
													isStrike = true;
												} else {
													isStrike = false;
												}
											} else {
												// 取挂盘价，并加上主动区域
												BigDecimal price = activeContract.getSalePrice();
												price.add(BigDecimal.valueOf(stratagy.getArbitrage().getActiveRegion())
														.multiply(activeContract.getUpperTick()));
												if (activePrice.compareTo(price) >= 0) {
													isStrike = true;
												} else {
													isStrike = false;
												}
											}
										}
										if (!isStrike) {
											// log.info("主动腿价格不满足，策略{}不触发", stratagy.getId());
											// 如果当前有主动腿挂单，则撤销
											if (TraderEnvEnum.ZD.getCode().equals(activeContract.getEnv())) {
												cancel(activeContractOrder);
											}
											continue;
										}
										// [start] 确定主动腿下单价格
										if (stratagy.getArbitrage().getActiveRegion() > 0) {
											// 主动区域大于0，则取计算出的价格
											activeContractOrder.setRequestPrice(activePrice);
										} else {
											// 否则，取经过滑点计算后的价格
											if (BuySaleEnum.BUY.getCode().equals(activeContractOrder.getDirection())) {
												activeContractOrder.setRequestPrice(activePrice
														.add(BigDecimal.valueOf(activeContract.getSlipPoint())
																.multiply(activeContract.getUpperTick())));
											} else if (BuySaleEnum.SALE.getCode()
													.equals(activeContractOrder.getDirection())) {
												activeContractOrder.setRequestPrice(activePrice
														.subtract(BigDecimal.valueOf(activeContract.getSlipPoint())
																.multiply(activeContract.getUpperTick())));
											}
										}
										// [end] 确定主动腿下单价格

										// [start] 确定主动腿下单量
										int minActiveContractOrderBuy = Math.min(
												stratagy.getArbitrage().getMaxOnceActiveOrderNums(),
												stratagy.getArbitrage().getBuyNums());
										int min2ActiveContractOrderBuy = Math.min(minActiveContractOrderBuy,
												activeContractOrder.getRemainNums());

										int minActiveContractOrderSale = Math.min(
												stratagy.getArbitrage().getMaxOnceActiveOrderNums(),
												stratagy.getArbitrage().getSaleNums());
										int min2ActiveContractOrderSale = Math.min(minActiveContractOrderSale,
												activeContractOrder.getRemainNums());

										if (BuySaleEnum.BUY.getCode().equals(arbitrageOrder.getDirection())) {
											if (stratagy.getArbitrage().getDirection()
													.equals(activeContract.getDirection())) {
												// 设置的套利方向与设置的主动腿方向一致
												if (stratagy.getArbitrage().getSaleNums() > 0) {
													if (min2ActiveContractOrderSale > 0) {
														activeContractOrder.setRequestNums(min2ActiveContractOrderSale);
													} else {
														isStrike = false;
													}
												} else {
													isStrike = false;
												}
											} else {
												if (stratagy.getArbitrage().getBuyNums() > 0) {
													if (min2ActiveContractOrderBuy > 0) {
														activeContractOrder.setRequestNums(min2ActiveContractOrderBuy);
													} else {
														isStrike = false;
													}
												} else {
													isStrike = false;
												}
											}
										} else if (BuySaleEnum.SALE.getCode().equals(arbitrageOrder.getDirection())) {
											if (stratagy.getArbitrage().getDirection()
													.equals(activeContract.getDirection())) {
												if (stratagy.getArbitrage().getBuyNums() > 0) {
													if (min2ActiveContractOrderBuy > 0) {
														activeContractOrder.setRequestNums(min2ActiveContractOrderBuy);
													} else {
														isStrike = false;
													}
												} else {
													isStrike = false;
												}
											} else {
												if (stratagy.getArbitrage().getSaleNums() > 0) {
													if (min2ActiveContractOrderSale > 0) {
														activeContractOrder.setRequestNums(min2ActiveContractOrderSale);
													} else {
														isStrike = false;
													}
												} else {
													isStrike = false;
												}
											}
										}
										// [end] 确定主动腿下单量
										if (!isStrike) {
											// log.info("主动腿数量小于0，策略{}不触发", stratagy.getId());
											continue;
										}
										if (activeContractOrder.getStayMap().size() == 0) {
											// 无主动腿挂单
											isStrike = true;
										} else {
											// 已有挂单，则改单或撤单
											if (TraderEnvEnum.ZD.getCode().equals(activeContract.getEnv())) {
												for (Entry<String, OrderResponseInfo> orderRespEntry : activeContractOrder
														.getStayMap().entrySet()) {
													OrderResponseInfo value = orderRespEntry.getValue();
													if (activeContractOrder.getRequestNums() > Integer
															.parseInt(value.orderNumber)) {
														// 下单量大于挂单量，只改价格
														if (activeContractOrder.getRequestPrice()
																.compareTo(new BigDecimal(value.orderPrice)) != 0) {
															modify(activeContract.getUserId(), value, value.orderNumber,
																	activeContractOrder.getRequestPrice().toString());
														}
													} else {
														String price = value.orderPrice;
														if (activeContractOrder.getRequestPrice()
																.compareTo(new BigDecimal(value.orderPrice)) != 0) {
															price = activeContractOrder.getRequestPrice().toString();
														}
														modify(activeContract.getUserId(), value,
																activeContractOrder.getRequestNums().toString(), price);
													}
													isStrike = false;
													break;
												}
											} else if (TraderEnvEnum.CTP.getCode().equals(activeContract.getEnv())) {
												ctpCancel(activeContractOrder);
											}
										}
										// [end] 主动区域

										// [start] 主动腿触发
										if (!isStrike) {
											// log.info("主动腿改单或撤单，策略{}不触发", stratagy.getId());
											continue;
										}
										// [end] 主动腿触发

										if (isStrike) {
											// 触发主动腿下单
											activeContractOrder.setRemainNums(activeContractOrder.getRemainNums()
													- activeContractOrder.getRequestNums());
											log.info("策略{}触发，主动腿下单：{}", stratagy.getId(),
													activeContractOrder.MyToString());
											/**
											 * 生成下单指令 如果交易环境是ZD，则将OrderInfo的localSystemCode赋值为：
											 * 该计算线程Id+"^"+策略Id+"^"+orderMap的key（即ArbitrageOrder的Id）+"^" +主被动类型
											 */
											NetInfo netInfo = new NetInfo();
											netInfo.code = CommandCode.ORDER;
											activeContractOrder.setOrderId(CommonUtils.uuid());
											TraderRef traderRef = new TraderRef();
											traderRef.setHandlerStratagyThreadId(this.id);
											traderRef.setStratagyId(stratagy.getId());
											traderRef.setArbitrageOrderId(arbitrageOrder.getArbitrageOrderId());
											traderRef.setContractOrderId(activeContractOrder.getOrderId());
											traderRef.setTraderSymbolType(TraderSymbolTypeEnum.ACTIVE.getCode());
											String localSystemCode = baseService.generateLocalSystemCode();
											if (TraderEnvEnum.ZD.getCode().equals(activeContract.getEnv())) {
												OrderInfo orderInfo = CommonUtils.generateOrder(
														activeContract.getUserId(), activeContract.getUserPwd(), "",
														UserTypeEnum.COMMON.getCode(),
														activeContractOrder.getExchangeNo(),
														activeContractOrder.getCode(),
														activeContractOrder.getDirection(),
														String.valueOf(activeContractOrder.getRequestNums()),
														String.valueOf(activeContractOrder.getRequestPrice()),
														PriceTypeEnum.LIMIT.getCode(), AddReduceEnum.TAKE.getCode(),
														activeContract.getAccountNo());
												orderInfo.localSystemCode = localSystemCode;
												netInfo.accountNo = orderInfo.userId;
												netInfo.infoT = orderInfo.MyToString();
												netInfo.exchangeCode = TraderEnvEnum.ZD.getCode();
												netInfo.localSystemCode = TraderEnvEnum.ZD.toString();
											} else if (TraderEnvEnum.CTP.getCode().equals(activeContract.getEnv())) {
												Order order = new Order();
												order.setDirection(activeContractOrder.getDirection());
												order.setExchange(activeContractOrder.getExchangeNo());
												order.setOffset(RtConstant.OFFSET_OPEN);
												if (activeContractOrder.getRequestPrice()
														.compareTo(activeContract.getUpperLimit()) > 0
														&& activeContract.getUpperLimit()
																.compareTo(BigDecimal.valueOf(0)) > 0) {
													order.setPrice(activeContract.getUpperLimit().doubleValue());
												} else if (activeContractOrder.getRequestPrice()
														.compareTo(activeContract.getLowerLimit()) < 0
														&& activeContract.getUpperLimit()
																.compareTo(BigDecimal.valueOf(0)) > 0) {
													order.setPrice(activeContract.getLowerLimit().doubleValue());
												} else {
													order.setPrice(activeContractOrder.getRequestPrice().doubleValue());
												}
												order.setPriceType(RtConstant.PRICETYPE_LIMITPRICE);
												order.setRtSymbol(activeContractOrder.getCode() + "."
														+ activeContractOrder.getExchangeNo());
												order.setSymbol(activeContractOrder.getCode());
												order.setTotalVolume(activeContractOrder.getRequestNums().intValue());
												order.setOrderID(localSystemCode);
												netInfo.accountNo = activeContract.getUserId();
												netInfo.infoT = order.MyToString();
												netInfo.exchangeCode = TraderEnvEnum.CTP.getCode();
												netInfo.localSystemCode = TraderEnvEnum.CTP.toString();
											}
											baseService.getRedisService().hmSet(RedisConst.NETINFO_LOCALNO_TRADERREF,
													localSystemCode, traderRef.MyToString());
											Global.orderEventProducer.onData(netInfo.MyToString());
										} else {
											// log.info("主动腿下单量不满足，策略{}不触发", stratagy.getId());
											continue;
										}
										// [end] 策略触发逻辑
									}
									// [end]循环策略内所有下单
								}
								// [end] 套利
							}
							// log.info("计算策略完成后耗时：{} ms", (System.nanoTime() - l) / 1e6);
						} catch (Exception e) {
							log.error("策略计算线程异常：{}", e);
						}

					}

				}
				// [end]计算策略

			}
		}

	}

	/**
	 * ZD期货主动腿撤单
	 * 
	 * @param activeContractOrder
	 */
	public void cancel(ContractOrder activeContractOrder) {
		try {
			if (activeContractOrder.getStayMap().size() > 0) {
				for (Entry<String, OrderResponseInfo> stayEntry : activeContractOrder.getStayMap().entrySet()) {
					OrderResponseInfo orderResponseInfo = stayEntry.getValue();
					CancelInfo cancelInfo = CommonUtils.generateCancelInfo(activeContractOrder.getUserId(),
							orderResponseInfo);
					NetInfo netInfo = new NetInfo();
					netInfo.code = CommandCode.CANCEL;
					netInfo.accountNo = activeContractOrder.getUserId();
					netInfo.exchangeCode = TraderEnvEnum.ZD.getCode();
					netInfo.localSystemCode = TraderEnvEnum.ZD.toString();
					netInfo.systemCode = orderResponseInfo.systemNo;
					netInfo.infoT = cancelInfo.MyToString();
					Global.orderEventProducer.onData(netInfo.MyToString());
				}
			}
		} catch (Exception e) {
			log.error("ZD期货撤单异常", e);
		}

	}

	/**
	 * ZD期货改单
	 * 
	 * @param value
	 * @param price
	 */
	public void modify(String userId, OrderResponseInfo value, String modifyNums, String price) {
		try {
			NetInfo netInfo = new NetInfo();
			ModifyInfo modifyInfo = CommonUtils.generateModify(userId, value, modifyNums, price,
					PriceTypeEnum.LIMIT.getCode());
			netInfo.code = CommandCode.MODIFY;
			netInfo.accountNo = userId;
			netInfo.exchangeCode = TraderEnvEnum.ZD.getCode();
			netInfo.localSystemCode = TraderEnvEnum.ZD.toString();
			netInfo.systemCode = value.systemNo;
			netInfo.infoT = modifyInfo.MyToString();
			Global.orderEventProducer.onData(netInfo.MyToString());
		} catch (Exception e) {
			log.error("ZD期货改单异常", e);
		}

	}

	/**
	 * CTP撤单
	 * 
	 * @param activeContractOrder
	 */
	public void ctpCancel(ContractOrder activeContractOrder) {
		if (activeContractOrder.getCtpStayMap().size() > 0) {
			for (Entry<String, Order> entry : activeContractOrder.getCtpStayMap().entrySet()) {
				Order order = entry.getValue();
				NetInfo netInfo = new NetInfo();
				netInfo.code = CommandCode.CANCEL;
				netInfo.accountNo = activeContractOrder.getUserId();
				netInfo.exchangeCode = TraderEnvEnum.CTP.getCode();
				netInfo.localSystemCode = TraderEnvEnum.CTP.toString();
				netInfo.infoT = order.MyToString();
				Global.orderEventProducer.onData(netInfo.MyToString());
			}
		}

	}

	public ConcurrentLinkedQueue<String> getMarketQueue() {
		return marketQueue;
	}

	public void setMarketQueue(ConcurrentLinkedQueue<String> marketQueue) {
		this.marketQueue = marketQueue;
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

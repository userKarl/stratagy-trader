package com.zd.business.service.thread;

import java.math.BigDecimal;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Lists;
import com.shanghaizhida.beans.CancelResponseInfo;
import com.shanghaizhida.beans.CommandCode;
import com.shanghaizhida.beans.FilledResponseInfo;
import com.shanghaizhida.beans.MarketInfo;
import com.shanghaizhida.beans.ModifyInfo;
import com.shanghaizhida.beans.NetInfo;
import com.shanghaizhida.beans.OrderInfo;
import com.shanghaizhida.beans.OrderResponseInfo;
import com.zd.business.common.BaseService;
import com.zd.business.common.BigDeciamalAnalysize;
import com.zd.business.common.CommonUtils;
import com.zd.business.common.JacksonUtil;
import com.zd.business.constant.CheckEnum;
import com.zd.business.constant.OrderMarketReasonTypeEnum;
import com.zd.business.constant.RedisConst;
import com.zd.business.constant.RtConstant;
import com.zd.business.constant.TraderEnvEnum;
import com.zd.business.constant.TraderSymbolTypeEnum;
import com.zd.business.constant.order.AddReduceEnum;
import com.zd.business.constant.order.BuySaleEnum;
import com.zd.business.constant.order.PriceTypeEnum;
import com.zd.business.constant.order.UserTypeEnum;
import com.zd.business.entity.Arbitrage;
import com.zd.business.entity.ArbitrageOrder;
import com.zd.business.entity.Contract;
import com.zd.business.entity.ContractOrder;
import com.zd.business.entity.Stratagy;
import com.zd.business.entity.TraderRef;
import com.zd.business.entity.ctp.Order;
import com.zd.business.entity.ctp.Tick;
import com.zd.business.entity.ctp.Trade;
import com.zd.business.mapper.TraderMapper;
import com.zd.config.Global;

import lombok.extern.slf4j.Slf4j;

/**
 * 接收下单数据
 * 
 * @author user
 *
 */
@Slf4j
public class ResvOrderThread implements Runnable {

	private volatile Thread thread;

	private volatile boolean isStillRunning = false;

	private BaseService baseService;

	public ResvOrderThread(BaseService baseService) {
		this.baseService = baseService;
	}

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
		log.info("解析下单服务器返回数据线程启动...");
		while (isStillRunning) {
			try {
				NetInfo ni = TraderMapper.resvOrderInfoQueue.poll();
				if (ni != null) {
					// 解析下单服务器返回的数据
					if (CommandCode.ORDER.equals(ni.code)
							|| (CommandCode.CTPORDER.equals(ni.code) && ni.infoT.contains(RtConstant.STATUS_UNKNOWN))) {
						// String orderKey = "";
						TraderRef traderRef = new TraderRef();
						int orderNums = 0;
						BigDecimal orderPrice = BigDecimal.valueOf(0);
						// 下单返回
						OrderResponseInfo orderResponseInfo = new OrderResponseInfo();
						Order order = new Order();
						if (TraderEnvEnum.ZD.toString().equals(ni.localSystemCode)) {
							orderResponseInfo.MyReadString(ni.infoT);
							String localSystemCode = orderResponseInfo.localNo;
							traderRef.MyReadString((String) (baseService.getRedisService()
									.hmGet(RedisConst.NETINFO_LOCALNO_TRADERREF, localSystemCode)));
							orderNums = Integer.parseInt(orderResponseInfo.orderNumber);
							orderPrice = new BigDecimal(orderResponseInfo.orderPrice);
							baseService.getRedisService().hmSet(RedisConst.LOCALNO_SYSTEMNO, localSystemCode,
									ni.systemCode);
							baseService.getRedisService().hmSet(RedisConst.SYSTEMNO_LOCALNO, ni.systemCode,
									localSystemCode);
							System.out.println("接收到下单返回数据：" + JacksonUtil.objToJson(orderResponseInfo));
						} else if (TraderEnvEnum.CTP.toString().equals(ni.localSystemCode)) {
							order.MyReadString(ni.infoT);
							if (!(RtConstant.OFFSET_OPEN.equals(order.getOffset())
									&& RtConstant.STATUS_UNKNOWN.equals(order.getStatus()))) {
								continue;
							}
							traderRef.MyReadString((String) (baseService.getRedisService()
									.hmGet(RedisConst.NETINFO_LOCALNO_TRADERREF, order.getOrderID())));
							orderNums = order.getTotalVolume();
							orderPrice = BigDecimal.valueOf(order.getPrice());
							baseService.getRedisService().hmSet(RedisConst.LOCALNO_SYSTEMNO, order.getOrderID(),
									ni.systemCode);
							baseService.getRedisService().hmSet(RedisConst.SYSTEMNO_LOCALNO, ni.systemCode,
									order.getOrderID());
							System.out.println("接收到下单返回数据：" + JacksonUtil.objToJson(order));
						}
						/**
						 * 生成下单指令 如果交易环境是ZD，则将OrderInfo的localSystemCode赋值为：
						 * 该计算线程Id+"^"+策略Id+"^"+orderMap的key（即ArbitrageOrder的Id）+"^" +主被动类型
						 */
						String handlerStratagyThreadId = traderRef.getHandlerStratagyThreadId();
						String stratagyId = traderRef.getStratagyId();
						String arbitrageOrderId = traderRef.getArbitrageOrderId();
						String contractOrderId = traderRef.getContractOrderId();
						String traderSymbolType = traderRef.getTraderSymbolType();
						HandlerStratagyThread handlerStratagyThread = TraderMapper.handlerStratagyThreadMap
								.get(handlerStratagyThreadId);
						if (handlerStratagyThread == null) {
							continue;
						}
						Stratagy stratagy = handlerStratagyThread.getStratagyConcurrentHashMap().get(stratagyId);
						if (stratagy == null) {
							continue;
						}
						ArbitrageOrder arbitrageOrder = stratagy.getArbitrage().getOrderMap().get(arbitrageOrderId);
						if (TraderSymbolTypeEnum.ACTIVE.getCode().equals(traderSymbolType)) {
							// 主动腿合约下单返回
							ContractOrder activeContractOrder = arbitrageOrder.getActiveContractOrder();
							activeContractOrder.setStayNums(activeContractOrder.getStayNums() + orderNums);
							if (activeContractOrder.getStayNums() > activeContractOrder.getRequestNums()) {
								activeContractOrder.setStayNums(activeContractOrder.getRequestNums());
							}
							activeContractOrder.setRequestPrice(orderPrice);
							if (TraderEnvEnum.ZD.toString().equals(ni.localSystemCode)) {
								activeContractOrder.getStayMap().put(orderResponseInfo.systemNo, orderResponseInfo);
							} else if (TraderEnvEnum.CTP.toString().equals(ni.localSystemCode)) {
								activeContractOrder.getCtpStayMap().put(order.getOrderID(), order);
							}

						} else if (TraderSymbolTypeEnum.MARKET.getCode().equals(traderSymbolType)) {
							// 被动腿下单返回
							List<ContractOrder> marketContractOrderList = arbitrageOrder.getMarketContractOrderList();
							for (ContractOrder marketContractOrder : marketContractOrderList) {
								if (contractOrderId.equals(marketContractOrder.getOrderId())) {
									marketContractOrder.setStayNums(marketContractOrder.getStayNums() + orderNums);
									if (marketContractOrder.getStayNums() > marketContractOrder.getRequestNums()) {
										marketContractOrder.setStayNums(marketContractOrder.getRequestNums());
									}
									marketContractOrder.setRequestPrice(orderPrice);
									if (TraderEnvEnum.ZD.toString().equals(ni.localSystemCode)) {
										marketContractOrder.getStayMap().put(orderResponseInfo.systemNo,
												orderResponseInfo);
									} else if (TraderEnvEnum.CTP.toString().equals(ni.localSystemCode)) {
										marketContractOrder.getCtpStayMap().put(order.getOrderID(), order);
									}

									// 开启追单定时任务
									try {
										Timer timer = new Timer();
										MarketChasingTimerTask marketChasingTimerTask = new MarketChasingTimerTask();
										marketChasingTimerTask.setMarketContractOrder(marketContractOrder);
										long startTime = System.nanoTime();
										marketChasingTimerTask.setStartTime(startTime);
										marketChasingTimerTask.setTimer(timer);
										marketChasingTimerTask.setOrderResponseInfo(orderResponseInfo);
										marketChasingTimerTask.setOrder(order);
										marketChasingTimerTask.setCount(0);
										marketChasingTimerTask.setTraderRef(traderRef);
										marketChasingTimerTask.setBaseService(baseService);
										marketChasingTimerTask.setArbitrage(stratagy.getArbitrage());
										timer.schedule(marketChasingTimerTask, 0,
												stratagy.getArbitrage().getChasingStayInterval() * 1000);
									} catch (Exception e) {
										log.error("追单异常:{},{}", marketContractOrder.getExchangeNo(),
												marketContractOrder.getCode());
									}
									break;
								}
							}
						}
					} else if (CommandCode.FILLEDCAST.equals(ni.code) || CommandCode.CTPTRADE.equals(ni.code)) {
						// 成交返回
						FilledResponseInfo filledResponseInfo = new FilledResponseInfo();
						Trade trade = new Trade();
						TraderRef traderRef = new TraderRef();
						int filledNumber = 0;
						BigDecimal filledPrice = BigDecimal.valueOf(0);
						String addReduce = "";// 下单方向
						if (TraderEnvEnum.ZD.toString().equals(ni.localSystemCode)) {
							filledResponseInfo.MyReadString(ni.infoT);
							String localSystemCode = filledResponseInfo.localNo;
							traderRef.MyReadString((String) (baseService.getRedisService()
									.hmGet(RedisConst.NETINFO_LOCALNO_TRADERREF, localSystemCode)));
							filledNumber = Integer.parseInt(filledResponseInfo.filledNumber);
							filledPrice = new BigDecimal(filledResponseInfo.filledPrice);
							addReduce = filledResponseInfo.addReduce;
							System.out.println("接收到成交返回数据：" + JacksonUtil.objToJson(filledResponseInfo));
						} else if (TraderEnvEnum.CTP.toString().equals(ni.localSystemCode)) {
							trade.MyReadString(ni.infoT);
							traderRef.MyReadString((String) (baseService.getRedisService()
									.hmGet(RedisConst.NETINFO_LOCALNO_TRADERREF, trade.getOrderID())));
							filledNumber = trade.getVolume();
							filledPrice = BigDecimal.valueOf(trade.getPrice());
							addReduce = trade.getOffset();
							System.out.println("接收到成交返回数据：" + JacksonUtil.objToJson(trade));
						}
						/**
						 * 生成下单指令 如果交易环境是ZD，则将OrderInfo的localSystemCode赋值为：
						 * 该计算线程Id+"^"+策略Id+"^"+orderMap的key（即ArbitrageOrder的Id）+"^"
						 * +ContractOrderId+"^"+主被动类型
						 */
						String handlerStratagyThreadId = traderRef.getHandlerStratagyThreadId();
						String stratagyId = traderRef.getStratagyId();
						String arbitrageOrderId = traderRef.getArbitrageOrderId();
						String contractOrderId = traderRef.getContractOrderId();
						String traderSymbolType = traderRef.getTraderSymbolType();
						HandlerStratagyThread handlerStratagyThread = TraderMapper.handlerStratagyThreadMap
								.get(handlerStratagyThreadId);
						if (handlerStratagyThread == null) {
							continue;
						}
						Stratagy stratagy = handlerStratagyThread.getStratagyConcurrentHashMap().get(stratagyId);
						if (stratagy == null) {
							continue;
						}
						ArbitrageOrder arbitrageOrder = stratagy.getArbitrage().getOrderMap().get(arbitrageOrderId);
						if (TraderSymbolTypeEnum.ACTIVE.getCode().equals(traderSymbolType)
								&& (AddReduceEnum.TAKE.getCode().equals(addReduce)
										|| RtConstant.OFFSET_OPEN.equals(addReduce))) {
							// 主动腿合约成交返回
							String activeDealId = CommonUtils.uuid();// 主动腿成交ID
							ContractOrder activeContractOrder = arbitrageOrder.getActiveContractOrder();
							activeContractOrder.setStayNums(activeContractOrder.getStayNums() - filledNumber);
							activeContractOrder.setDealNums(activeContractOrder.getDealNums() + filledNumber);
							activeContractOrder.setDealPrice(filledPrice);
							if (TraderEnvEnum.ZD.toString().equals(ni.localSystemCode)) {
								activeContractOrder.getDealMap().put(filledResponseInfo.systemNo, filledResponseInfo);
								activeContractOrder.getStayMap().remove(filledResponseInfo.systemNo);
								baseService.getRedisService().hmSet(RedisConst.DEALINFOKEY, activeDealId,
										filledResponseInfo.MyToString());
								baseService.getRedisService().hmSet(RedisConst.DEALINFOKEY,
										stratagy.getActiveContract().getUserId(),
										stratagy.getActiveContract().getUserPwd());
							} else if (TraderEnvEnum.CTP.toString().equals(ni.localSystemCode)) {
								activeContractOrder.getCtpTradeMap().put(trade.getOrderID(), trade);
								activeContractOrder.getCtpStayMap().remove(trade.getOrderID());
								baseService.getRedisService().hmSet(RedisConst.DEALINFOKEY, activeDealId,
										trade.MyToString());
							}
							// 进行被动腿下单
							boolean isOrderEnable = true;
							List<String> currPriceList = Lists
									.newArrayList(String.valueOf(activeContractOrder.getDealPrice()));// 主被动腿各合约的现价
							List<Contract> list = stratagy.getMarketContractList();// 策略的被动腿
							List<ContractOrder> marketContractOrderList = arbitrageOrder.getMarketContractOrderList();
							List<String> orderList = Lists.newArrayList();
							for (int i = 0; i < marketContractOrderList.size(); i++) {
								Contract contract = list.get(i);
								// 获取该合约的最新行情
								if (TraderEnvEnum.ZD.getCode().equals(contract.getEnv())) {
									MarketInfo mi = Global.zdContractMap
											.get(contract.getExchangeCode() + "@" + contract.getCode());
									if (mi == null) {
										isOrderEnable = false;
										break;
									} else {
										contract.setCurrPrice(new BigDecimal(mi.currPrice));
										contract.setBuyPrice(new BigDecimal(mi.buyPrice));
										contract.setSalePrice(new BigDecimal(mi.salePrice));
									}
								} else if (TraderEnvEnum.CTP.getCode().equals(contract.getEnv())) {
									Tick tick = Global.ctpContractMap
											.get(contract.getExchangeCode() + "@" + contract.getCode());
									if (tick == null) {
										isOrderEnable = false;
										break;
									} else {
										contract.setCurrPrice(BigDecimal.valueOf(tick.getLastPrice()));
										contract.setBuyPrice(BigDecimal.valueOf(tick.getAskPrice1()));
										contract.setSalePrice(BigDecimal.valueOf(tick.getBidPrice1()));
										contract.setUpperLimit(BigDecimal.valueOf(tick.getUpperLimit()));
										contract.setLowerLimit(BigDecimal.valueOf(tick.getLowerLimit()));
									}
								}
								ContractOrder contractOrder = marketContractOrderList.get(i);
								int decimals = CommonUtils.getDecimalsUnits(contract.getUpperTick());
								if (BuySaleEnum.BUY.getCode().equals(arbitrageOrder.getDirection())) {
									if (stratagy.getArbitrage().getDirection().equals(contract.getDirection())) {
										contractOrder.setDirection(BuySaleEnum.BUY.getCode());
									} else {
										contractOrder.setDirection(BuySaleEnum.SALE.getCode());
									}
								} else if (BuySaleEnum.SALE.getCode().equals(arbitrageOrder.getDirection())) {
									if (stratagy.getArbitrage().getDirection().equals(contract.getDirection())) {
										contractOrder.setDirection(BuySaleEnum.SALE.getCode());
									} else {
										contractOrder.setDirection(BuySaleEnum.BUY.getCode());
									}
								}
								// [start] 价格
								if (list.size() == 1) {
									String expression = stratagy.getExpression();
									BigDecimal value = arbitrageOrder.getOrderPrice();
									expression = expression.replace("P2", "m");
									contractOrder.setRequestPrice(
											BigDeciamalAnalysize.cacl(expression, currPriceList, value));
								}
								if (PriceTypeEnum.LIMIT.getCode().equals(contract.getPriceType())) {
									if (BuySaleEnum.BUY.getCode().equals(contractOrder.getDirection())) {
										contractOrder.setRequestPrice(contract.getSalePrice()
												.add(BigDecimal.valueOf(contract.getSlipPoint())
														.multiply(contract.getUpperTick()))
												.setScale(decimals, BigDecimal.ROUND_DOWN));
									} else if (BuySaleEnum.SALE.getCode().equals(contractOrder.getDirection())) {
										// 限价，则以行情对盘价减去滑点下单
										contractOrder.setRequestPrice(contract.getSalePrice()
												.subtract(BigDecimal.valueOf(contract.getSlipPoint())
														.multiply(contract.getUpperTick()))
												.setScale(decimals, BigDecimal.ROUND_DOWN));
									}
									contractOrder.setPriceType(PriceTypeEnum.LIMIT.getCode());
								} else if (PriceTypeEnum.MARKET.getCode().equals(contract.getPriceType())) {
									// 市价
									contractOrder.setPriceType(PriceTypeEnum.MARKET.getCode());
								} else if (PriceTypeEnum.CACL.getCode().equals(contract.getPriceType())) {
									// 以计算价格下单，只适用于两腿
									contractOrder.setPriceType(PriceTypeEnum.LIMIT.getCode());
								}
								// [end] 价格

								// [start] 量
								/**
								 * （1）根据主动腿成交量按照主被动腿比例计算被动腿下单量 （2）如果主动腿还有剩余可下单量，则直接计算，此时取min(计算结果，被动腿剩余可下单量)
								 * （3）如果主动腿没有剩余可下单量，则取被动腿的剩余可下单量
								 */
								if (activeContractOrder.getRemainNums() > 0) {
									int result = Integer.parseInt(filledResponseInfo.filledNumber)
											* contract.getPerFillNums() / stratagy.getActiveContract().getPerFillNums();
									int requestNums = Math.min(result, contractOrder.getRemainNums());
									contractOrder.setRequestNums(requestNums);
									contractOrder.setRemainNums(contractOrder.getRemainNums() - requestNums);
								} else {
									contractOrder.setRequestNums(contractOrder.getRemainNums());
									contractOrder.setRemainNums(0);
								}
								// [end] 量
								if (contractOrder.getRequestNums() <= 0
										|| contractOrder.getRequestPrice().compareTo(BigDecimal.valueOf(0)) <= 0) {
									continue;
								}
								NetInfo netInfo = new NetInfo();
								netInfo.code = CommandCode.ORDER;
								String localNo = baseService.generateLocalSystemCode();
								TraderRef traderRefMarket = new TraderRef();
								traderRefMarket.setHandlerStratagyThreadId(handlerStratagyThread.getId());
								traderRefMarket.setStratagyId(stratagy.getId());
								traderRefMarket.setArbitrageOrderId(arbitrageOrder.getArbitrageOrderId());
								traderRefMarket.setContractOrderId(contractOrder.getOrderId());
								traderRefMarket.setTraderSymbolType(TraderSymbolTypeEnum.MARKET.getCode());
								traderRefMarket.setActiveDealId(activeDealId);
								baseService.getRedisService().hmSet(RedisConst.NETINFO_LOCALNO_TRADERREF, localNo,
										traderRefMarket.MyToString());
								if (TraderEnvEnum.ZD.getCode().equals(contract.getEnv())) {
									OrderInfo orderInfo = CommonUtils.generateOrder(contract.getUserId(),
											contract.getUserPwd(), "", UserTypeEnum.COMMON.getCode(),
											contractOrder.getExchangeNo(), contractOrder.getCode(),
											contractOrder.getDirection(),
											String.valueOf(contractOrder.getRequestNums()),
											String.valueOf(contractOrder.getRequestPrice()),
											contractOrder.getPriceType(), AddReduceEnum.TAKE.getCode(),
											contract.getAccountNo());
									orderInfo.localSystemCode = localNo;
									netInfo.accountNo = orderInfo.userId;
									netInfo.infoT = orderInfo.MyToString();
									netInfo.exchangeCode = TraderEnvEnum.ZD.getCode();
									netInfo.localSystemCode = TraderEnvEnum.ZD.toString();
									orderList.add(netInfo.MyToString());
								} else if (TraderEnvEnum.CTP.getCode().equals(contract.getEnv())) {
									Order order = new Order();
									order.setDirection(contractOrder.getDirection());
									order.setExchange(contractOrder.getExchangeNo());
									order.setOffset(RtConstant.OFFSET_OPEN);
									if (contractOrder.getRequestPrice().compareTo(contract.getUpperLimit()) > 0
											&& contract.getUpperLimit().compareTo(BigDecimal.valueOf(0)) > 0) {
										order.setPrice(contract.getUpperLimit().doubleValue());
									} else if (contractOrder.getRequestPrice().compareTo(contract.getLowerLimit()) < 0
											&& contract.getUpperLimit().compareTo(BigDecimal.valueOf(0)) > 0) {
										order.setPrice(contract.getLowerLimit().doubleValue());
									} else {
										order.setPrice(contractOrder.getRequestPrice().doubleValue());
									}
									order.setPriceType(RtConstant.PRICETYPE_LIMITPRICE);
									order.setRtSymbol(contractOrder.getCode() + "." + contractOrder.getExchangeNo());
									order.setSymbol(contractOrder.getCode());
									order.setTotalVolume(contractOrder.getRequestNums().intValue());
									order.setOrderID(localNo);
									netInfo.accountNo = contract.getUserId();
									netInfo.infoT = order.MyToString();
									netInfo.exchangeCode = TraderEnvEnum.CTP.getCode();
									netInfo.localSystemCode = TraderEnvEnum.CTP.toString();
									orderList.add(netInfo.MyToString());
								}
							}
							if (!isOrderEnable) {
								continue;
							}
							for (String order : orderList) {
								Global.orderEventProducer.onData(order);
							}

						} else if (TraderSymbolTypeEnum.MARKET.getCode().equals(traderSymbolType)) {
							// 被动腿成交返回
							List<ContractOrder> marketContractOrderList = arbitrageOrder.getMarketContractOrderList();
							for (ContractOrder marketContractOrder : marketContractOrderList) {
								if (contractOrderId.equals(marketContractOrder.getOrderId())) {
									marketContractOrder.setStayNums(marketContractOrder.getStayNums() - filledNumber);
									marketContractOrder.setDealNums(marketContractOrder.getDealNums() + filledNumber);
									marketContractOrder.setDealPrice(filledPrice);
									if (TraderEnvEnum.ZD.toString().equals(ni.localSystemCode)) {
										marketContractOrder.getDealMap().put(filledResponseInfo.systemNo,
												filledResponseInfo);
										marketContractOrder.getStayMap().remove(filledResponseInfo.systemNo);
									} else if (TraderEnvEnum.CTP.toString().equals(ni.localSystemCode)) {
										marketContractOrder.getCtpTradeMap().put(trade.getOrderID(), trade);
										marketContractOrder.getCtpStayMap().remove(trade.getOrderID());
									}
									break;
								}
							}
						}
					} else if (CommandCode.CANCELCAST.equals(ni.code) || (CommandCode.CTPORDER.equals(ni.code)
							&& ni.infoT.contains(RtConstant.STATUS_CANCELLED))) {
						TraderRef traderRef = new TraderRef();
						int orderNums = 0;
						CancelResponseInfo cancelResponseInfo = new CancelResponseInfo();
						Order order = new Order();
						// 根据下单系统号找到撤销该挂单时的本地号
						String localNo = (String) baseService.getRedisService()
								.hmGet(RedisConst.ORDER_SYSTEMNO_CANCEL_LOCALNO, ni.systemCode);
						if (StringUtils.isNotBlank(localNo)) {
							if (TraderEnvEnum.ZD.toString().equals(ni.localSystemCode)) {
								cancelResponseInfo.MyReadString(ni.infoT);
								traderRef.MyReadString((String) baseService.getRedisService()
										.hmGet(RedisConst.NETINFO_LOCALNO_TRADERREF, localNo));
								orderNums = Integer.parseInt(cancelResponseInfo.orderNumber);
								System.out.println("接收到撤单返回数据：" + JacksonUtil.objToJson(cancelResponseInfo));
							} else if (TraderEnvEnum.CTP.toString().equals(ni.localSystemCode)) {
								order.MyReadString(ni.infoT);
								if (!(RtConstant.OFFSET_OPEN.equals(order.getOffset())
										&& RtConstant.STATUS_CANCELLED.equals(order.getStatus()))) {
									continue;
								}
								traderRef.MyReadString((String) baseService.getRedisService()
										.hmGet(RedisConst.NETINFO_LOCALNO_TRADERREF, order.getOrderID()));
								orderNums = order.getTotalVolume();
								System.out.println("接收到撤单返回数据：" + JacksonUtil.objToJson(order));
							}
						}
						/**
						 * 生成下单指令 如果交易环境是ZD，则将OrderInfo的localSystemCode赋值为：
						 * 该计算线程Id+"^"+策略Id+"^"+orderMap的key（即ArbitrageOrder的Id）+"^" +主被动类型
						 */
						String handlerStratagyThreadId = traderRef.getHandlerStratagyThreadId();
						String stratagyId = traderRef.getStratagyId();
						String arbitrageOrderId = traderRef.getArbitrageOrderId();
						String contractOrderId = traderRef.getContractOrderId();
						String traderSymbolType = traderRef.getTraderSymbolType();
						String activeDealId = traderRef.getActiveDealId();
						String evenActiveFlag = traderRef.getEvenActiveFlag();
						String orderMarketFlag = traderRef.getOrderMarketFlag();
						String orderMarketReason = traderRef.getOrderMarketReason();
						HandlerStratagyThread handlerStratagyThread = TraderMapper.handlerStratagyThreadMap
								.get(handlerStratagyThreadId);
						if (handlerStratagyThread == null) {
							continue;
						}
						Stratagy stratagy = handlerStratagyThread.getStratagyConcurrentHashMap().get(stratagyId);
						ArbitrageOrder arbitrageOrder = stratagy.getArbitrage().getOrderMap().get(arbitrageOrderId);
						if (TraderSymbolTypeEnum.ACTIVE.getCode().equals(traderSymbolType)) {
							// 主动腿合约撤单返回
							ContractOrder activeContractOrder = arbitrageOrder.getActiveContractOrder();
							activeContractOrder.setStayNums(activeContractOrder.getStayNums() - orderNums);
							activeContractOrder.setRemainNums(activeContractOrder.getRemainNums() + orderNums);
							activeContractOrder.setRequestPrice(BigDecimal.valueOf(0));
							if (TraderEnvEnum.ZD.toString().equals(ni.localSystemCode)) {
								activeContractOrder.getStayMap().remove(cancelResponseInfo.systemNo);
							} else if (TraderEnvEnum.CTP.toString().equals(ni.localSystemCode)) {
								activeContractOrder.getCtpStayMap().remove(order.getOrderID());
							}

						} else if (TraderSymbolTypeEnum.MARKET.getCode().equals(traderSymbolType)) {
							// 被动腿合约撤单返回
							List<ContractOrder> marketContractOrderList = arbitrageOrder.getMarketContractOrderList();
							for (ContractOrder marketContractOrder : marketContractOrderList) {
								if (contractOrderId.equals(marketContractOrder.getOrderId())) {
									marketContractOrder.setStayNums(marketContractOrder.getStayNums() - orderNums);
									marketContractOrder.setRemainNums(marketContractOrder.getRemainNums() + orderNums);
									if (TraderEnvEnum.ZD.toString().equals(ni.localSystemCode)) {
										marketContractOrder.getStayMap().remove(cancelResponseInfo.systemNo);
									} else if (TraderEnvEnum.CTP.toString().equals(ni.localSystemCode)) {
										marketContractOrder.getCtpStayMap().remove(order.getOrderID());
									}
									// 如果该被动腿撤单是由于主动进行主动腿平仓发生的，则需要找到该被动腿对应的主动腿进行平仓操作
									if (CheckEnum.YES.getCode().equals(evenActiveFlag)) {
										NetInfo netInfo = new NetInfo();
										netInfo.code = CommandCode.ORDER;
										// 首先根据主动腿成交Id找到该主动腿成交信息
										String dealInfo = (String) baseService.getRedisService()
												.hmGet(RedisConst.DEALINFOKEY, activeDealId);
										if (StringUtils.isNotBlank(dealInfo)) {
											String localSystemCode = baseService.generateLocalSystemCode();
											TraderRef traderRefEven = new TraderRef();
											traderRefEven.setHandlerStratagyThreadId(handlerStratagyThreadId);
											traderRefEven.setStratagyId(stratagy.getId());
											traderRefEven.setArbitrageOrderId(arbitrageOrder.getArbitrageOrderId());
											traderRefEven.setContractOrderId(CommonUtils.uuid());
											traderRefEven.setTraderSymbolType(TraderSymbolTypeEnum.ACTIVE.getCode());
											if (TraderEnvEnum.ZD.toString().equals(ni.localSystemCode)) {
												FilledResponseInfo filledResponseInfo = new FilledResponseInfo();
												filledResponseInfo.MyReadString(dealInfo);
												String userPwd = (String) baseService.getRedisService()
														.hmGet(RedisConst.DEALINFOKEY, filledResponseInfo.userId);
												String buySale = "";
												String orderPrice = "";
												MarketInfo marketInfo = Global.zdContractMap
														.get(filledResponseInfo.exchangeCode + "@"
																+ filledResponseInfo.code);
												if (BuySaleEnum.BUY.getCode().equals(filledResponseInfo.buySale)) {
													buySale = BuySaleEnum.SALE.getCode();
													orderPrice = String.valueOf(
															new BigDecimal(marketInfo.salePrice).subtract(BigDecimal
																	.valueOf(stratagy.getArbitrage()
																			.getEvenActiveSlipPoint())
																	.multiply(stratagy.getActiveContract()
																			.getUpperTick())));
												} else if (BuySaleEnum.SALE.getCode()
														.equals(filledResponseInfo.buySale)) {
													buySale = BuySaleEnum.BUY.getCode();
													orderPrice = String
															.valueOf(new BigDecimal(marketInfo.buyPrice).add(BigDecimal
																	.valueOf(stratagy.getArbitrage()
																			.getEvenActiveSlipPoint())
																	.multiply(stratagy.getActiveContract()
																			.getUpperTick())));
												}

												OrderInfo orderInfo = CommonUtils.generateOrder(
														filledResponseInfo.userId, userPwd, "",
														UserTypeEnum.COMMON.getCode(), filledResponseInfo.exchangeCode,
														filledResponseInfo.code, buySale,
														filledResponseInfo.filledNumber, orderPrice,
														stratagy.getArbitrage().getEvenActivePriceType(),
														AddReduceEnum.CLOSE.getCode(), filledResponseInfo.accountNo);
												netInfo.accountNo = filledResponseInfo.userId;
												netInfo.infoT = orderInfo.MyToString();
												netInfo.exchangeCode = TraderEnvEnum.ZD.getCode();
												netInfo.localSystemCode = TraderEnvEnum.ZD.toString();
												netInfo.systemCode = filledResponseInfo.systemNo;
											} else if (TraderEnvEnum.CTP.toString().equals(ni.localSystemCode)) {
												Trade trade = new Trade();
												trade.MyReadString(dealInfo);
												String buySale = "";
												BigDecimal orderPrice = BigDecimal.valueOf(0);
												Tick tick = Global.ctpContractMap
														.get(trade.getExchange() + "@" + trade.getSymbol());
												if (RtConstant.DIRECTION_LONG.equals(trade.getDirection())) {
													buySale = BuySaleEnum.SALE.getCode();
													orderPrice = new BigDecimal(tick.getBidPrice1()).subtract(BigDecimal
															.valueOf(stratagy.getArbitrage().getEvenActiveSlipPoint())
															.multiply(stratagy.getActiveContract().getUpperTick()));
												} else if (RtConstant.DIRECTION_SHORT.equals(trade.getDirection())) {
													buySale = BuySaleEnum.BUY.getCode();
													orderPrice = new BigDecimal(tick.getAskPrice1()).add(BigDecimal
															.valueOf(stratagy.getArbitrage().getEvenActiveSlipPoint())
															.multiply(stratagy.getActiveContract().getUpperTick()));
												}

												Order ctpOrder = new Order();
												order.setDirection(buySale);
												ctpOrder.setExchange(marketContractOrder.getExchangeNo());
												ctpOrder.setOffset(RtConstant.OFFSET_OPEN);
												if (orderPrice.compareTo(BigDecimal.valueOf(tick.getUpperLimit())) > 0
														&& BigDecimal.valueOf(tick.getUpperLimit())
																.compareTo(BigDecimal.valueOf(0)) > 0) {
													ctpOrder.setPrice(tick.getUpperLimit());
												} else if (orderPrice
														.compareTo(BigDecimal.valueOf(tick.getLowerLimit())) < 0
														&& BigDecimal.valueOf(tick.getUpperLimit())
																.compareTo(BigDecimal.valueOf(0)) > 0) {
													ctpOrder.setPrice(tick.getLowerLimit());
												} else {
													ctpOrder.setPrice(
															marketContractOrder.getRequestPrice().doubleValue());
												}
												ctpOrder.setPriceType(RtConstant.PRICETYPE_LIMITPRICE);
												ctpOrder.setRtSymbol(marketContractOrder.getCode() + "."
														+ marketContractOrder.getExchangeNo());
												ctpOrder.setSymbol(marketContractOrder.getCode());
												ctpOrder.setTotalVolume(
														marketContractOrder.getRequestNums().intValue());
												ctpOrder.setOrderID(localSystemCode);
												netInfo.accountNo = marketContractOrder.getUserId();
												netInfo.infoT = ctpOrder.MyToString();
												netInfo.exchangeCode = TraderEnvEnum.CTP.getCode();
												netInfo.localSystemCode = TraderEnvEnum.CTP.toString();
											}
											baseService.getRedisService().hmSet(RedisConst.NETINFO_LOCALNO_TRADERREF,
													localSystemCode, traderRefEven.MyToString());
											Global.orderEventProducer.onData(netInfo.MyToString());
										}
									}

									// 如果该被动腿撤单后，需要再下单
									if (CheckEnum.YES.getCode().equals(orderMarketFlag)) {
										if (OrderMarketReasonTypeEnum.AUTOCHASING.getCode().equals(orderMarketReason)) {
											// 自动追单中的被动腿先撤单再下单

											// TODO
										} else if (OrderMarketReasonTypeEnum.SYSTEMCHASING.getCode()
												.equals(orderMarketReason)) {
											// 系统追单中选择立即追单
											// TODO
										}

									}
									break;
								}
							}
						}
					}
				}
			} catch (Exception e) {
				log.error("处理下单服务器返回数据异常", e);
			}
		}
	}

}

/**
 *
 */
class MarketChasingTimerTask extends TimerTask {

	private Arbitrage arbitrage;
	private ContractOrder marketContractOrder;
	private long startTime;
	private Timer timer;
	private OrderResponseInfo orderResponseInfo;// ZD期货下单返回
	private Order order;// CTP下单返回
	private int count;
	private TraderRef traderRef;
	private BaseService baseService;

	public Arbitrage getArbitrage() {
		return arbitrage;
	}

	public void setArbitrage(Arbitrage arbitrage) {
		this.arbitrage = arbitrage;
	}

	public BaseService getBaseService() {
		return baseService;
	}

	public void setBaseService(BaseService baseService) {
		this.baseService = baseService;
	}

	public TraderRef getTraderRef() {
		return traderRef;
	}

	public void setTraderRef(TraderRef traderRef) {
		this.traderRef = traderRef;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public OrderResponseInfo getOrderResponseInfo() {
		return orderResponseInfo;
	}

	public void setOrderResponseInfo(OrderResponseInfo orderResponseInfo) {
		this.orderResponseInfo = orderResponseInfo;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public Timer getTimer() {
		return timer;
	}

	public void setTimer(Timer timer) {
		this.timer = timer;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public ContractOrder getMarketContractOrder() {
		return marketContractOrder;
	}

	public void setMarketContractOrder(ContractOrder marketContractOrder) {
		this.marketContractOrder = marketContractOrder;
	}

	@Override
	public void run() {
		long l = System.nanoTime();
		double interval = (l - startTime) / 1e9;
		BigDecimal requestPrice = marketContractOrder.getRequestPrice();// 挂单价格
		BigDecimal counterPrice = BigDecimal.valueOf(0);// 对盘价
		BigDecimal newPrice = BigDecimal.valueOf(0);// 新的挂单价格
		String contractOrderId = marketContractOrder.getOrderId();
		String orderInfo = "";
		String traderRefStr = traderRef.MyToString();
		String userId = marketContractOrder.getUserId();
		String contractKey = marketContractOrder.getExchangeNo() + "@" + marketContractOrder.getCode();
		int decimals = CommonUtils.getDecimalsUnits(marketContractOrder.getUpperTick());
		NetInfo ni = new NetInfo();
		if (TraderEnvEnum.ZD.getCode().equals(marketContractOrder.getEnv())) {
			orderInfo = orderResponseInfo.MyToString();
			MarketInfo marketInfo = Global.zdContractMap.get(contractKey);
			if (marketInfo == null) {
				return;
			}
			if (BuySaleEnum.BUY.getCode().equals(marketContractOrder.getDirection())) {
				counterPrice = new BigDecimal(marketInfo.salePrice);
				newPrice = counterPrice
						.add(marketContractOrder.getUpperTick()
								.multiply(BigDecimal.valueOf(arbitrage.getChasingSlipPoint())))
						.setScale(decimals, BigDecimal.ROUND_DOWN);
			} else if (BuySaleEnum.SALE.getCode().equals(marketContractOrder.getDirection())) {
				counterPrice = new BigDecimal(marketInfo.buyPrice);
				newPrice = counterPrice
						.subtract(marketContractOrder.getUpperTick()
								.multiply(BigDecimal.valueOf(arbitrage.getChasingSlipPoint())))
						.setScale(decimals, BigDecimal.ROUND_UP);
			}
		} else if (TraderEnvEnum.CTP.getCode().equals(marketContractOrder.getEnv())) {
			orderInfo = order.MyToString();
			Tick tick = Global.ctpContractMap.get(contractKey);
			if (tick == null) {
				return;
			}
			if (BuySaleEnum.BUY.getCode().equals(marketContractOrder.getDirection())) {
				counterPrice = new BigDecimal(tick.getBidPrice1());
				newPrice = counterPrice
						.add(marketContractOrder.getUpperTick()
								.multiply(BigDecimal.valueOf(arbitrage.getChasingSlipPoint())))
						.setScale(decimals, BigDecimal.ROUND_DOWN);
			} else if (BuySaleEnum.SALE.getCode().equals(marketContractOrder.getDirection())) {
				counterPrice = new BigDecimal(tick.getAskPrice1());
				newPrice = counterPrice
						.add(marketContractOrder.getUpperTick()
								.multiply(BigDecimal.valueOf(arbitrage.getChasingSlipPoint())))
						.setScale(decimals, BigDecimal.ROUND_UP);
			}
		}
		// 当挂单超过2秒，或者挂单价格与当前价格相差n个跳点时，以对盘价计入滑点后追单,追单10次，每次间隔1s
		if (marketContractOrder.getStayMap().size() > 0 || marketContractOrder.getCtpStayMap().size() > 0) {
			if (CheckEnum.YES.getCode().equals(arbitrage.getAutoChasing())) {
				// 开启自动追单
				if (count > 9) {
					// 追10次，之后进入系统追单逻辑
					systemChasing(interval, contractOrderId, orderInfo, traderRefStr, userId,
							marketContractOrder.MyToString());
					timer.cancel();
					timer = null;
					System.out.println("停止自动追单");
					return;
				}
				System.out.println("count:" + count);
				if ((l - startTime) / 1e9 > arbitrage.getChasingStayInterval()
						|| Math.abs(requestPrice.subtract(counterPrice).doubleValue()) >= marketContractOrder
								.getUpperTick().multiply(BigDecimal.valueOf(arbitrage.getChasingLimit()))
								.doubleValue()) {
					String orderId = baseService.generateLocalSystemCode();
					if (TraderEnvEnum.ZD.getCode().equals(marketContractOrder.getEnv())) {
						ModifyInfo modifyInfo = CommonUtils.generateModify(marketContractOrder.getUserId(),
								orderResponseInfo, marketContractOrder.getRequestNums().toString(), newPrice.toString(),
								arbitrage.getChasingPriceType());
						ni.code = CommandCode.MODIFY;
						ni.systemCode = orderResponseInfo.systemNo;
						ni.exchangeCode = TraderEnvEnum.ZD.getCode();
						ni.accountNo = marketContractOrder.getUserId();
						ni.infoT = modifyInfo.MyToString();
						ni.localSystemCode = TraderEnvEnum.ZD.toString();
						Global.orderEventProducer.onData(ni.MyToString());
					} else if (TraderEnvEnum.CTP.getCode().equals(marketContractOrder.getEnv())) {
						// 先撤再下
						ni.code = CommandCode.CANCEL;
						ni.exchangeCode = TraderEnvEnum.CTP.getCode();
						ni.accountNo = marketContractOrder.getUserId();
						ni.infoT = order.MyToString();
						ni.localSystemCode = TraderEnvEnum.CTP.toString();
						Global.orderEventProducer.onData(ni.MyToString());
						Order order2 = new Order();
						order2.setDirection(order.getDirection());
						order2.setExchange(order.getExchange());
						order2.setOffset(RtConstant.OFFSET_OPEN);
						order2.setPrice(newPrice.doubleValue());
						if (PriceTypeEnum.LIMIT.getCode().equals(arbitrage.getChasingPriceType())) {
							order2.setPriceType(RtConstant.PRICETYPE_LIMITPRICE);
						} else if (PriceTypeEnum.MARKET.getCode().equals(arbitrage.getChasingPriceType())) {
							order2.setPriceType(RtConstant.PRICETYPE_MARKETPRICE);
						}
						order2.setRtSymbol(order.getRtSymbol());
						order2.setSymbol(order.getSymbol());
						order2.setTotalVolume(order.getTotalVolume());
						order2.setOrderID(orderId);
						ni.accountNo = marketContractOrder.getUserId();
						ni.infoT = order2.MyToString();
						ni.exchangeCode = TraderEnvEnum.CTP.getCode();
						ni.localSystemCode = TraderEnvEnum.CTP.toString();
						Global.orderEventProducer.onData(ni.MyToString());
						traderRef.setOrderMarketFlag(CheckEnum.YES.getCode());
						traderRef.setOrderMarketReason(OrderMarketReasonTypeEnum.AUTOCHASING.getCode());
					}
					baseService.getRedisService().hmSet(RedisConst.NETINFO_LOCALNO_TRADERREF, orderId,
							traderRef.MyToString());
					System.out.println("挂单超过" + arbitrage.getChasingStayInterval() + "秒");
				}
				count++;
			} else {
				// 没有开启自动追单，则进入系统追单逻辑
				systemChasing(interval, contractOrderId, orderInfo, traderRefStr, userId,
						marketContractOrder.MyToString());
			}
		} else {
			timer.cancel();
			timer = null;
			System.out.println("停止自动追单");
		}

	}

	/**
	 * 系统追单逻辑
	 */
	public void systemChasing(double interval, String orderId, String orderInfo, String traderRef, String userId,
			String contractOrderInfo) {
		if (interval > 10) {
			baseService.getRedisService().hmSet(RedisConst.CONTRACTORDER_ORDERINFO, orderId, orderInfo);
			baseService.getRedisService().hmSet(RedisConst.CONTRACTORDER_TRADERREF, orderId, traderRef);
			baseService.getRedisService().hmSet(RedisConst.CONTRACTORDER_USERID, orderId, userId);
			baseService.getRedisService().hmSet(RedisConst.CONTRACTORDERINFO, orderId, contractOrderInfo);
		}
	}
}

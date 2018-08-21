package com.zd.business.engine.main.market;

import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.assertj.core.util.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shanghaizhida.beans.MarketInfo;
import com.shanghaizhida.beans.NetInfo;
import com.zd.business.constant.CommandEnum;
import com.zd.business.constant.StratagyStatusEnum;
import com.zd.business.constant.StratagyTypeEnum;
import com.zd.business.engine.event.ZdEventDynamicHandlerAbstract;
import com.zd.business.entity.MarketProvider;
import com.zd.business.entity.Stratagy;
import com.zd.config.Global;

public class MarketEventHandler extends ZdEventDynamicHandlerAbstract<MarketEvent> {

	private static final Logger logger = LoggerFactory.getLogger(MarketEventHandler.class);

	private String id;

	public MarketEventHandler(String id) {
		this.id = id;
	}

	// 策略容器
	private ConcurrentHashMap<String, Stratagy> stratagyConcurrentHashMap = new ConcurrentHashMap<>();

	@Override
	public void onEvent(MarketEvent event, long sequence, boolean endOfBatch) throws Exception {
		Thread.sleep(1000);
		logger.info("策略计算接收到的数据：{}", event.getNetInfo());
		NetInfo ni = new NetInfo();
		ni.code = CommandEnum.STRATAGY_STRIKE.toString();
		// 循环计算集合中的状态为RUNNING的策略
		for (Entry<String, Stratagy> entry : stratagyConcurrentHashMap.entrySet()) {
			try {
				Stratagy stratagy = entry.getValue();
				if (StratagyStatusEnum.RUNNING.toString().equals(stratagy.getStatus())) {
					// 市商套利
					if (StratagyTypeEnum.M.toString().equals(stratagy.getType())) {
						// 根据套利公式和档位限制计算市商合约行情
						// TODO
						MarketProvider mp = new MarketProvider();
						MarketInfo mi = mp.getMarketInfo();
						// 取多空剩余最大下单量的较小值，判断该值是否小于预设的最小下单量
						double minRemainNum = Math.min(mp.getMaxBuyNum() - mp.getCurrBuyNum(),
								mp.getMaxSaleNum() - mp.getCurrSaleNum());
						if (minRemainNum < mp.getMinOrderNum()) {
							// 单边下单
						} else {
							/**
							 *  双边下单
							 */
							// 确定最大下单量边界
							//minRemainNum大于预设最大下单量，则预设最大下单量为最大下单量边界
							//minRemainNum小于预设最大下单量，则minRemainNum为最大下单量边界
							double maxOrderNumLimit = 0;
							if (minRemainNum > mp.getMaxOrderNum()) {
								maxOrderNumLimit = mp.getMaxOrderNum();
							} else {
								maxOrderNumLimit = minRemainNum;
							}
							// 计算下单组合
							List<String> list = Lists.newArrayList();
							double buyAllPrice[] = { Double.valueOf(mi.buyPrice), Double.valueOf(mi.buyPrice2),
									Double.valueOf(mi.buyPrice3), Double.valueOf(mi.buyPrice4),
									Double.valueOf(mi.buyPrice5) };
							double saleAllPrice[] = { Double.valueOf(mi.salePrice), Double.valueOf(mi.salePrice2),
									Double.valueOf(mi.salePrice3), Double.valueOf(mi.salePrice4),
									Double.valueOf(mi.salePrice5) };
							double buy[]=new double[mp.getPriceLevelLimit()];
							double sale[]=new double[mp.getPriceLevelLimit()];
							for(int i=0;i<mp.getPriceLevelLimit();i++) {
								buy[i]=buyAllPrice[i];
								sale[i]=saleAllPrice[i];
							}
							for(int i=0;i<sale.length;i++) {
								for(int j=0;j<buy.length;j++) {
									if(sale[i]-buy[j]<mp.getSpread()) {
										list.add(i+"@"+j);
									}
								}
							}
							//计算下单量
							if(list.size()>0) {
								double buyAllNumber[] = { Double.valueOf(mi.buyNumber), Double.valueOf(mi.buyNumber2),
										Double.valueOf(mi.buyNumber3), Double.valueOf(mi.buyNumber4),
										Double.valueOf(mi.buyNumber5) };
								double saleAllNumber[] = { Double.valueOf(mi.saleNumber), Double.valueOf(mi.saleNumber2),
										Double.valueOf(mi.saleNumber3), Double.valueOf(mi.saleNumber4),
										Double.valueOf(mi.saleNumber5) };
							}
						}
					}
					if (true) {
						// 策略触发
						stratagy.setStatus(StratagyStatusEnum.STRIKE.toString());
						ni.infoT = stratagy.MyToString();
						Global.orderEventProducer.onData(ni.MyToString());
					}
				}
			} catch (Exception e) {
				logger.error("消费者{}，计算策略时异常：{}", id, e.getMessage());
			}

		}
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public ConcurrentHashMap<String, Stratagy> getStratagyConcurrentHashMap() {
		return stratagyConcurrentHashMap;
	}

	public void setStratagyConcurrentHashMap(ConcurrentHashMap<String, Stratagy> stratagyConcurrentHashMap) {
		this.stratagyConcurrentHashMap = stratagyConcurrentHashMap;
	}

}

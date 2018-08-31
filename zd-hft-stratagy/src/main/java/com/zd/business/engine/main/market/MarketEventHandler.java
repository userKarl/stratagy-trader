package com.zd.business.engine.main.market;

import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shanghaizhida.beans.MarketInfo;
import com.zd.business.constant.CommandEnum;
import com.zd.business.engine.event.ZdEventDynamicHandlerAbstract;
import com.zd.business.service.thread.HandlerStratagyThread;
import com.zd.config.Global;

public class MarketEventHandler extends ZdEventDynamicHandlerAbstract<MarketEvent> {

	private static final Logger logger = LoggerFactory.getLogger(MarketEventHandler.class);

	private HandlerStratagyThread handlerStratagyThread;
	
	public void init() {
		this.handlerStratagyThread=new HandlerStratagyThread(UUID.randomUUID().toString());
		this.handlerStratagyThread.start();
	}

	@Override
	public void onEvent(MarketEvent event, long sequence, boolean endOfBatch) throws Exception {
		try {
			logger.info("策略消费者接收到的数据：{}", event.getMarketInfo());
			MarketInfo marketInfo = new MarketInfo();
			marketInfo.MyReadString(event.getMarketInfo());
			for(String s:subscribedEventSet) {
				if(s.equals(marketInfo.exchangeCode+"@"+marketInfo.code)) {
					//更新内存中该合约行情
					handler(marketInfo);
					handlerStratagyThread.getQueue().add(CommandEnum.STRATAGY_CACL.toString()+"-"+System.nanoTime());
				}
			}
		} catch (Exception e) {
			logger.error("策略消费者接收行情异常：{}", e);
		}
	}

	public HandlerStratagyThread getHandlerStratagyThread() {
		return handlerStratagyThread;
	}

	public void setHandlerStratagyThread(HandlerStratagyThread handlerStratagyThread) {
		this.handlerStratagyThread = handlerStratagyThread;
	}

	/**
	 * 处理行情数据
	 * 
	 * @param msg
	 */
	public void handler(MarketInfo mi) {
		try {
			// 更新基本合约的行情数据
			String key = mi.getExchangeCode() + "===" + mi.getCode();
			MarketInfo cmi = Global.zdContractMap.get(key);
			if (cmi != null) {
				cmi = updateMarketData(mi, cmi);
				Global.zdContractMap.put(key, cmi);
			} else {
				Global.zdContractMap.put(key, mi);
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.warn("期货行情解析计算异常：{}", e.getMessage());
		}

	}

	/**
	 * 更新全局的行情数据
	 * 
	 * @param dataFrom
	 * @param dataTo
	 */
	private MarketInfo updateMarketData(MarketInfo dataFrom, MarketInfo dataTo) {
		// dataTo.code = dataFrom.code;
		dataTo.exchangeCode = dataFrom.exchangeCode;
		// 最高价
		if (StringUtils.isNotBlank(dataFrom.high)) {
			dataTo.high = dataFrom.high;
		}

		// 开盘价
		if (StringUtils.isNotBlank(dataFrom.open)) {
			dataTo.open = dataFrom.open;
		}
		// 最低价
		if (StringUtils.isNotBlank(dataFrom.low)) {
			dataTo.low = dataFrom.low;
		}
		// 成交量
		if (StringUtils.isNotBlank(dataFrom.filledNum.trim()) && !"0".equals(dataFrom.filledNum)) {
			dataTo.filledNum = dataFrom.filledNum;
		}

		// 持仓量
		if (StringUtils.isNotBlank(dataFrom.holdNum)) {
			dataTo.holdNum = dataFrom.holdNum;
		}
		// 昨结算
		if (StringUtils.isNotBlank(dataFrom.oldClose) && !"0".equals(dataFrom.oldClose)) {
			dataTo.oldClose = dataFrom.oldClose;
		}
		// 现量
		if (StringUtils.isNotBlank(dataFrom.currNumber)) {
			dataTo.currNumber = dataFrom.currNumber;
		}
		// 最新价
		if (StringUtils.isNotBlank(dataFrom.currPrice)) {
			dataTo.currPrice = dataFrom.currPrice;
		}
		// 更新时间
		if (StringUtils.isNotBlank(dataFrom.time)) {
			dataTo.time = dataFrom.time;
		}
		// 行情类型
		if (StringUtils.isNotBlank(dataFrom.type)) {
			dataTo.type = dataFrom.type;
		}
		if (StringUtils.isNotBlank(dataFrom.buyPrice) && StringUtils.isNotBlank(dataFrom.buyNumber)) {
			// 买量1
			dataTo.buyNumber = dataFrom.buyNumber;
			// 买量2
			dataTo.buyNumber2 = dataFrom.buyNumber2;
			// 买量3
			dataTo.buyNumber3 = dataFrom.buyNumber3;
			// 买量4
			dataTo.buyNumber4 = dataFrom.buyNumber4;
			// 买量5
			dataTo.buyNumber5 = dataFrom.buyNumber5;
			// 买价1
			dataTo.buyPrice = dataFrom.buyPrice;
			// 买价2
			dataTo.buyPrice2 = dataFrom.buyPrice2;
			// 买价3
			dataTo.buyPrice3 = dataFrom.buyPrice3;
			// 买价4
			dataTo.buyPrice4 = dataFrom.buyPrice4;
			// 买价5
			dataTo.buyPrice5 = dataFrom.buyPrice5;
		}

		// 隐藏买量
		dataTo.hideBuyNumber = dataFrom.hideBuyNumber;
		// 隐藏买价
		dataTo.hideBuyPrice = dataFrom.hideBuyPrice;

		if (StringUtils.isNotBlank(dataFrom.salePrice) && StringUtils.isNotBlank(dataFrom.saleNumber)) {
			// 卖量1
			dataTo.saleNumber = dataFrom.saleNumber;
			// 卖量2
			dataTo.saleNumber2 = dataFrom.saleNumber2;
			// 卖量3
			dataTo.saleNumber3 = dataFrom.saleNumber3;
			// 卖量4
			dataTo.saleNumber4 = dataFrom.saleNumber4;
			// 卖量5
			dataTo.saleNumber5 = dataFrom.saleNumber5;
			// 卖价1
			dataTo.salePrice = dataFrom.salePrice;
			// 卖价2
			dataTo.salePrice2 = dataFrom.salePrice2;
			// 卖价3
			dataTo.salePrice3 = dataFrom.salePrice3;
			// 卖价4
			dataTo.salePrice4 = dataFrom.salePrice4;
			// 卖价5
			dataTo.salePrice5 = dataFrom.salePrice5;
		}

		// 隐藏卖量
		dataTo.hideSaleNumber = dataFrom.hideSaleNumber;
		// 隐藏卖价
		dataTo.hideSalePrice = dataFrom.hideSalePrice;

		return dataTo;
	}

}

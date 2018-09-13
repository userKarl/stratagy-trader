package com.zd.business.service.market;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shanghaizhida.beans.CommandCode;
import com.shanghaizhida.beans.MarketInfo;
import com.shanghaizhida.beans.NetInfo;
import com.shanghaizhida.core.net.ConnectionStateListener;
import com.shanghaizhida.core.net.MarketClient;
import com.zd.business.constant.TraderEnvEnum;
import com.zd.config.Global;

/**
 * 国际股票行情接收线程
 *
 * @author xiang
 */
public class StockMarketDataFeed implements Runnable, ConnectionStateListener {

	private static final Logger logger = LoggerFactory.getLogger(StockMarketDataFeed.class);

	private volatile boolean isStillRunning = false;
	private volatile Thread thread = null;
	private static MarketClient ci;

	public String host;
	public String port;

	/**
	 * 构造函数
	 */
	public StockMarketDataFeed(String host, String port) {
		this.host = host;
		this.port = port;
	}

	/**
	 * 线程启动
	 */
	public void start() {
		try {
			if (ci != null)
				return;
			ci = new MarketClient(this.host, this.port);
			ci.setConnectionStateListener(this);
			ci.start();

			isStillRunning = true;

			thread = new Thread(this);
			thread.start();
			logger.info("股票行情线程启动");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 线程停止
	 */
	public void stop() {

		if (thread != null) {
			// 线程处于运行状态
			isStillRunning = false;
			if (ci != null) {
				logger.error("股票行情线程停止");
				ci.stop();
				ci = null;
			}
			thread.interrupt();
			thread = null;
			System.gc();
		}
		// LogWriteFactory.getInstances().log(MyLog.LVL_TRACE, "StockMarketDataFeed",
		// "stop", "国际股票行情线程停止");
	}

	@Override
	public void run() {
		while (isStillRunning) {

			byte[] rawMsg;
			try {

				rawMsg = ci.dataQueue.take();

				int dataLen = com.shanghaizhida.Utils.byteArrayToInt(rawMsg);
				String temp = new String(rawMsg, 8, dataLen);
				if (temp.indexOf("GETPRICE") != -1) {
				} else {
					NetInfo ni = new NetInfo();
					ni.MyReadString(temp);
					if (StringUtils.isNotBlank(ni.infoT) && StringUtils.isNotBlank(ni.code)
							&& CommandCode.MARKET01.equals(ni.code)) {
						handler(ni.infoT);
						ni.exchangeCode=TraderEnvEnum.STOCK.getCode();
						Global.marketEventProducer.onData(ni.MyToString());
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onConnectStateChange(int code, String text) {
		if (code == ConnectionStateListener.RECONNECTED) {
			logger.info("国际股票行情已连接");
			try {
				Thread.sleep(3000);
				ci.reqAllMarket(0);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (code == ConnectionStateListener.CONNECTION_LOST) {
			logger.error("国际股票行情已断开");
		} else if (code == ConnectionStateListener.SCOKET_CONNECTIONFAIL) {
			logger.error("国际股票行情连接创建失败！");
		}

	}
	
	/**
	 * 处理行情数据
	 * @param msg
	 */
	public void handler(String msg) {
		try {
			MarketInfo mi = new MarketInfo();
			mi.MyReadString(msg);
			String key = mi.getExchangeCode() + "@" + mi.getCode();
			MarketInfo smi = Global.stockMap.get(key);
			if (StringUtils.isNotBlank(mi.getExchangeCode()) && StringUtils.isNotBlank(mi.getCode())) {
				// 更新内存中的股票基本数据
				if (smi != null) {
					smi = updateMarketData(mi, smi);
					Global.stockMap.put(key, smi);
				} else {
					Global.stockMap.put(key, mi);
				}
				smi = Global.stockMap.get(key);

			}

			Global.stockMap.put(key, smi);
		} catch (Exception e) {
			e.printStackTrace();
			logger.warn("股票行情解析计算异常：{}", e.getMessage());
		}

	}
	
	/**
	 * 更新全局的行情数据
	 * 
	 * @param dataFrom
	 * @param dataTo
	 */
	private MarketInfo updateMarketData(MarketInfo dataFrom, MarketInfo dataTo) {
		dataTo.code = dataFrom.code;
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
		if (StringUtils.isNotBlank(dataFrom.filledNum.trim()) && !dataFrom.filledNum.equals("0")) {
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

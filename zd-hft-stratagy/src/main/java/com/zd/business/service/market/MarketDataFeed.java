package com.zd.business.service.market;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shanghaizhida.beans.CommandCode;
import com.shanghaizhida.beans.NetInfo;
import com.shanghaizhida.core.net.ConnectionStateListener;
import com.shanghaizhida.core.net.MarketClient;
import com.zd.config.Global;

/**
 * 国际期货行情接收线程
 *
 * @author xiang
 */
public class MarketDataFeed implements Runnable, ConnectionStateListener {

	private Logger logger = LoggerFactory.getLogger(MarketDataFeed.class);

	private volatile boolean isStillRunning = false;
	private volatile Thread thread = null;
	private static MarketClient ci;

	public String host;
	public String port;

	/**
	 * 构造函数
	 */
	public MarketDataFeed(String host, String port) {
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
			logger.info("期货行情线程启动");
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
				logger.error("期货行情线程停止");
				ci.stop();
				ci = null;
			}
			thread.interrupt();
			thread = null;
			System.gc();
		}
		// LogWriteFactory.getInstances().log(MyLog.LVL_TRACE, "StockMarketDataFeed",
		// "stop", "国际期货行情线程停止");
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
						// handler(ni.infoT);
						Global.marketEventProducer.onData(ni.infoT);
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
			logger.info("国际期货行情已连接");
			try {
				Thread.sleep(3000);
				ci.reqAllMarket(0);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (code == ConnectionStateListener.CONNECTION_LOST) {
			logger.error("国际期货行情已断开");
		} else if (code == ConnectionStateListener.SCOKET_CONNECTIONFAIL) {
			logger.error("国际期货行情连接创建失败！");
		}

	}

}

package com.shanghaizhida.core.net;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.shanghaizhida.CommonFunction;
import com.shanghaizhida.beans.CommandCode;
import com.shanghaizhida.beans.NetInfo;

public class MarketClient extends BaseClient {

	private static Logger logger=LoggerFactory.getLogger(MarketClient.class);
	
	private ConnectionStateListener connectionStateListner = null;
	public BlockingQueue<byte[]> dataQueue = null;

	public MarketClient(String hostIP, String hostPort) {
		super(hostIP, hostPort);
		dataQueue = new LinkedBlockingQueue<byte[]>();
	}

	@Override
	public void onMsgReady(byte[] rawMsg) {
		try {
			dataQueue.put(rawMsg);

			// int dataLen = Utils.byteArrayToInt(rawMsg);
			// String temp = new String(rawMsg, 8, dataLen);
			// System.out.println(temp);

		} catch (InterruptedException ie) {
			logger.warn("onMsgReady:{}", ie.toString());
		}
	}

	@Override
	public void onConnectStateChange(int code, String text) {
		if (connectionStateListner != null)
			connectionStateListner.onConnectStateChange(code, text);
	}

	public void setConnectionStateListener(ConnectionStateListener listener) {
		this.connectionStateListner = listener;
	}

	public void reqAllMarket(int interval) {
		reqMarket("all", null, interval);
	}

	/**
	 * @param interval
	 *            market server send data interval, in second; if 0 then means
	 *            no limit
	 */
	public void reqMarket(List<String> contracts, int interval) {
		reqMarket("specified", contracts, interval);
	}

	/**
	 * Subscribe more market data in addition to already existing
	 * 
	 * @param contracts
	 */
	public void reqAdditionalMarket(List<String> contracts, int interval) {
		reqMarket("additional", contracts, interval);
	}

	/**
	 * Subscribe market data of specified contracts, and auto un-subscribe
	 * needless contracts market
	 * 
	 * @param contracts
	 */
	public void reqSpecifiedMarket(List<String> contracts) {
		reqMarket("specified", contracts, 0);
	}

	/**
	 * 请求行情
	 * 
	 * @param reqType
	 *            请求类型 <br>
	 *            all 请求全部 ,<br>
	 *            specified 取消原来的请求,从新请求一个新的合约列表的行情,<br>
	 *            additional 增量请求
	 * @param contracts
	 *            请求行情的合约列表
	 * @param interval
	 *            行情发送间隔时间
	 */
	public void reqMarket(String reqType, List<String> contracts, int interval) {

		if (!"all".equals(reqType)) {
			if (contracts == null || contracts.size() == 0)
				return;
		}

		NetInfo netInfo = new NetInfo();
		netInfo.code = CommandCode.MARKET01;
		netInfo.systemCode = "" + interval;

		if ("all".equals(reqType)) {
			netInfo.todayCanUse = "++";
		} else {

			if ("specified".equals(reqType))
				netInfo.todayCanUse = "+T";
			else if ("additional".equals(reqType))
				netInfo.todayCanUse = "+";

			StringBuffer sb = new StringBuffer();
			int contractCount = contracts.size();
			for (int i = 0; i < contractCount - 1; i++) {
				sb.append(contracts.get(i)).append(";");
			}
			sb.append(contracts.get(contractCount - 1));
			netInfo.infoT = sb.toString();
		}

		byte[] reqBytes = CommonFunction.asciiStrToNetBytes(netInfo
				.MyToString());

		//if (errorLogger != null)
		//	errorLogger.log(ZDLogger.LVL_ERROR, "MarketClient", "reqMarket",
		//			"请求合约 > " + netInfo.MyToString());
		//System.out.println("请求合约 > " + netInfo.MyToString());
		sendData(reqBytes);
	}

	/**
	 * 用户请求所有可用的合约
	 */
	public void reqContracts() {

		NetInfo netInfo = new NetInfo();
		netInfo.code = CommandCode.GETTCONTRACT;
		netInfo.todayCanUse = "Byte";

		byte[] reqBytes = CommonFunction.asciiStrToNetBytes(netInfo
				.MyToString());
		sendData(reqBytes);
	}

	/**
	 * 请求昨结算
	 */
	public void reqOldPrice() {
		NetInfo netInfo = new NetInfo();
		netInfo.code = CommandCode.GetSettlePrice;
		netInfo.todayCanUse = "Byte";
		byte[] reqBytes = CommonFunction.asciiStrToNetBytes(netInfo
				.MyToString());

		//if (errorLogger != null)
		//	errorLogger.log(ZDLogger.LVL_ERROR, "MarketClient", "reqOldPrice",
		//			"请求结算 > " + netInfo.MyToString());
		sendData(reqBytes);
	}

	/**
	 * 请求股票成交明细
	 */
	public void reqStockDealDetail(String stockNo) {
		NetInfo netInfo = new NetInfo();
		netInfo.code = CommandCode.GetSettlePrice;
		netInfo.todayCanUse = "Byte";
		byte[] reqBytes = CommonFunction.asciiStrToNetBytes(netInfo
				.MyToString());

		sendData(reqBytes);
	}

	// public static void main(String[] args) {
	// MarketClient marketClient = new MarketClient("222.73.119.230", "9002",
	// new MyLog());
	//
	// try {
	//
	// marketClient.start();
	//
	// Thread.sleep(2000);
	//
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// List<String> contracts = new ArrayList<String>();
	// contracts.add("CME,6B1603");
	// // marketClient.reqMarket(contracts, 0);
	//
	// marketClient.reqSpecifiedMarket(contracts);
	// }
}

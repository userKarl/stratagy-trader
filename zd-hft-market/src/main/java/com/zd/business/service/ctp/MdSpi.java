package com.zd.business.service.ctp;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.zd.business.common.RtConstant;
import com.zd.jctp.CThostFtdcDepthMarketDataField;
import com.zd.jctp.CThostFtdcForQuoteRspField;
import com.zd.jctp.CThostFtdcMdApi;
import com.zd.jctp.CThostFtdcMdSpi;
import com.zd.jctp.CThostFtdcReqUserLoginField;
import com.zd.jctp.CThostFtdcRspInfoField;
import com.zd.jctp.CThostFtdcRspUserLoginField;
import com.zd.jctp.CThostFtdcSpecificInstrumentField;
import com.zd.jctp.CThostFtdcUserLogoutField;

/**
 * @author sun0x00@gmail.com
 */
public class MdSpi extends CThostFtdcMdSpi {

	Logger log = LoggerFactory.getLogger(MdSpi.class);

	private String mdAddress;
	// private String tdAddress;
	private String brokerID;
	private String userID;
	private String password;
	// private String autoCode;
	// private String userProductInfo;
	private String gatewayLogInfo;
	private String gatewayID;
	// private String gatewayDisplayName;
	private CtpGateway ctpGateway;
	private String tradingDayStr;

	private HashMap<String, String> contractExchangeMap;
	// private HashMap<String, Integer> contractSizeMap;

//	static {
//		try {
//			if (System.getProperties().getProperty("os.name").toUpperCase().indexOf("WINDOWS") != -1) {
//
//				String envTmpDir = System.getProperty("java.io.tmpdir");
//				String tempLibPath = envTmpDir + File.separator + "com" + File.separator + "jctp" + File.separator + "lib";
//				
//				CommonUtil.copyURLToFileForTmp(tempLibPath, MdSpi.class.getResource("/assembly/libiconv.dll"));
//				CommonUtil.copyURLToFileForTmp(tempLibPath, MdSpi.class.getResource("/assembly/thostmduserapi.dll"));
//				CommonUtil.copyURLToFileForTmp(tempLibPath,
//						MdSpi.class.getResource("/assembly/jctpmdapiv6v3v11x64.dll"));
//				CommonUtil.copyURLToFileForTmp(tempLibPath, MdSpi.class.getResource("/assembly/thosttraderapi.dll"));
//				CommonUtil.copyURLToFileForTmp(tempLibPath,
//						MdSpi.class.getResource("/assembly/jctptraderapiv6v3v11x64.dll"));
//				
//				System.load(tempLibPath + File.separator + "libiconv.dll");
//				System.load(tempLibPath + File.separator + "thostmduserapi.dll");
//				System.load(tempLibPath + File.separator + "jctpmdapiv6v3v11x64.dll");
//				System.load(tempLibPath + File.separator + "thosttraderapi.dll");
//				System.load(tempLibPath + File.separator + "jctptraderapiv6v3v11x64.dll");
//			} else {
//
//				String envTmpDir = "/tmp";
//				String tempLibPath = envTmpDir + File.separator + "com" + File.separator + "jctp" + File.separator + "lib";
//				
//				CommonUtil.copyURLToFileForTmp(tempLibPath, MdSpi.class.getResource("/assembly/libthostmduserapi.so"));
//				CommonUtil.copyURLToFileForTmp(tempLibPath, MdSpi.class.getResource("/assembly/libthosttraderapi.so"));
//				CommonUtil.copyURLToFileForTmp(tempLibPath,
//						MdSpi.class.getResource("/assembly/libjctpmdapiv6v3v11x64.so"));
//				CommonUtil.copyURLToFileForTmp(tempLibPath,
//						MdSpi.class.getResource("/assembly/libjctptraderapiv6v3v11x64.so"));
//
//				System.load(tempLibPath + File.separator + "libthostmduserapi.so");
//				System.load(tempLibPath + File.separator + "libjctpmdapiv6v3v11x64.so");
//				System.load(tempLibPath + File.separator + "libthosttraderapi.so");
//				System.load(tempLibPath + File.separator + "libjctptraderapiv6v3v11x64.so");
//			}
//		} catch (Exception e) {
//		}
//
//	}
	
//	public MdSpi(String mdAddress,String brokerID,String userID,String password,HashMap<String, String> contractExchangeMap) {
//
//		this.mdAddress = mdAddress;
//		// this.tdAddress = ctpGateway.getGatewaySetting().getTdAddress();
//		this.brokerID = brokerID;
//		this.userID = userID;
//		this.password = password;
//		// this.autoCode = ctpGateway.getGatewaySetting().getAuthCode();
//
//		this.contractExchangeMap = contractExchangeMap;
//		// this.contractSizeMap = ctpGateway.getContractSizeMap();
//
//	}

	MdSpi(CtpGateway ctpGateway) {

		this.ctpGateway = ctpGateway;
		this.mdAddress = ctpGateway.getGatewaySetting().getMdAddress();
		// this.tdAddress = ctpGateway.getGatewaySetting().getTdAddress();
		this.brokerID = ctpGateway.getGatewaySetting().getBrokerID();
		this.userID = ctpGateway.getGatewaySetting().getUserID();
		this.password = ctpGateway.getGatewaySetting().getPassword();
		// this.autoCode = ctpGateway.getGatewaySetting().getAuthCode();
		// this.gatewayDisplayName = ctpGateway.getGatewayDisplayName();

		this.contractExchangeMap = ctpGateway.getContractExchangeMap();
		// this.contractSizeMap = ctpGateway.getContractSizeMap();

	}
	
	private CThostFtdcMdApi cThostFtdcMdApi;

	private boolean connectProcessStatus = false; // 避免重复调用
	private boolean connectionStatus = false; // 前置机连接状态
	private boolean loginStatus = false; // 登陆状态

	/**
	 * 连接
	 */
	public synchronized void connect() {
		String logContent;
		if (isConnected() || connectProcessStatus) {
			return;
		}

		if (connectionStatus) {
			login();
			return;
		}
		if (cThostFtdcMdApi != null) {
			cThostFtdcMdApi.Release();
			cThostFtdcMdApi.delete();
			connectionStatus = false;
			loginStatus = false;

		}
		String envTmpDir = System.getProperty("java.io.tmpdir");
		String tempFilePath = envTmpDir + File.separator + "xyz" + File.separator + "redtorch" + File.separator + "api"
				+ File.separator + "jctp" + File.separator + "TEMP_CTP" + File.separator + "MD_";
		File tempFile = new File(tempFilePath);
		if (!tempFile.getParentFile().exists()) {
			try {
				FileUtils.forceMkdirParent(tempFile);
				logContent = gatewayLogInfo + "创建临时文件夹" + tempFile.getParentFile().getAbsolutePath();
				log.info(logContent);
			} catch (IOException e) {
				logContent = gatewayLogInfo + "创建临时文件夹失败" + tempFile.getParentFile().getAbsolutePath();
				log.error(logContent);
			}
		}
		logContent = gatewayLogInfo + "使用临时文件夹" + tempFile.getParentFile().getAbsolutePath();
		log.info(logContent);

		cThostFtdcMdApi = CThostFtdcMdApi.CreateFtdcMdApi(tempFile.getAbsolutePath());
		cThostFtdcMdApi.RegisterSpi(this);
		cThostFtdcMdApi.RegisterFront(mdAddress);
		cThostFtdcMdApi.Init();
		connectProcessStatus = true;

	}

	/**
	 * 关闭
	 */
	public synchronized void close() {
		if (!isConnected()) {
			return;
		}

		if (cThostFtdcMdApi != null) {
			cThostFtdcMdApi.Release();
			cThostFtdcMdApi.delete();
			connectionStatus = false;
			loginStatus = false;
			connectProcessStatus = false;
		}

	}

	/**
	 * 返回接口状态
	 * 
	 * @return
	 */
	public boolean isConnected() {
		return connectionStatus && loginStatus;
	}

	/**
	 * 获取交易日
	 * 
	 * @return
	 */
	public String getTradingDay() {
		return tradingDayStr;
	}

	/**
	 * 订阅行情
	 * 
	 * @param rtSymbol
	 */
	public void subscribe(String symbol) {
		if (isConnected()) {
			String[] symbolArray = new String[1];
			symbolArray[0] = symbol;
			cThostFtdcMdApi.SubscribeMarketData(symbolArray, 1);
		} else {
			String logContent = gatewayLogInfo + "无法订阅行情,行情服务器尚未连接成功";
			log.warn(logContent);
		}
	}

	/**
	 * 退订行情
	 */
	public void unSubscribe(String rtSymbol) {
		if (isConnected()) {
			String[] rtSymbolArray = new String[1];
			rtSymbolArray[0] = rtSymbol;
			cThostFtdcMdApi.UnSubscribeMarketData(rtSymbolArray, 1);
		} else {
			String logContent = gatewayLogInfo + "退订无效,行情服务器尚未连接成功";
			log.warn(logContent);
		}
	}

	private void login() {
		if (StringUtils.isEmpty(brokerID) || StringUtils.isEmpty(userID) || StringUtils.isEmpty(password)) {
			String logContent = gatewayLogInfo + "BrokerID UserID Password不允许为空";
			log.error(logContent);
			
			return;
		}
		// 登录
		CThostFtdcReqUserLoginField userLoginField = new CThostFtdcReqUserLoginField();
		userLoginField.setBrokerID(brokerID);
		userLoginField.setUserID(userID);
		userLoginField.setPassword(password);
		cThostFtdcMdApi.ReqUserLogin(userLoginField, 0);
	}

	// 前置机联机回报
	public void OnFrontConnected() {
		String logContent = gatewayLogInfo + "行情接口前置机已连接";
		log.info(logContent);
		
		// 修改前置机连接状态为true
		connectionStatus = true;
		connectProcessStatus = false;
		login();
	}

	// 前置机断开回报
	public void OnFrontDisconnected(int nReason) {
		String logContent = gatewayLogInfo + "行情接口前置机已断开,Reason:" + nReason;
		log.info(logContent);
		
		this.connectionStatus = false;
	}

	// 登录回报
	public void OnRspUserLogin(CThostFtdcRspUserLoginField pRspUserLogin, CThostFtdcRspInfoField pRspInfo,
			int nRequestID, boolean bIsLast) {
		if (pRspInfo.getErrorID() == 0) {
			log.info("{}OnRspUserLogin! TradingDay:{},SessionID:{},BrokerID:{},UserID:{}", gatewayLogInfo,
					pRspUserLogin.getTradingDay(), pRspUserLogin.getSessionID(), pRspUserLogin.getBrokerID(),
					pRspUserLogin.getUserID());
			// 修改登录状态为true
			this.loginStatus = true;
			tradingDayStr = pRspUserLogin.getTradingDay();
			log.info("{}获取到的交易日为{}", gatewayLogInfo, tradingDayStr);
			// 重新订阅之前的合约
			List<String> list=Lists.newArrayList();
			for(Entry<String,String> entry:contractExchangeMap.entrySet()) {
				list.add(entry.getValue());
			}
			String[] array=new String[list.size()];
			array=list.toArray(array);
			cThostFtdcMdApi.SubscribeMarketData(array, array.length + 1);
		} else {
			log.warn("{}行情接口登录回报错误! ErrorID:{},ErrorMsg:{}", gatewayLogInfo, pRspInfo.getErrorID(),
					pRspInfo.getErrorMsg());
		}

	}

	// 心跳警告
	public void OnHeartBeatWarning(int nTimeLapse) {
		String logContent = gatewayLogInfo + "行情接口心跳警告 nTimeLapse:" + nTimeLapse;
		log.warn(logContent);
		
	}

	// 登出回报
	public void OnRspUserLogout(CThostFtdcUserLogoutField pUserLogout, CThostFtdcRspInfoField pRspInfo, int nRequestID,
			boolean bIsLast) {
		if (pRspInfo.getErrorID() != 0) {
			log.info("{}OnRspUserLogout!ErrorID:{},ErrorMsg:{}", gatewayLogInfo, pRspInfo.getErrorID(),
					pRspInfo.getErrorMsg());
		} else {
			log.info("{}OnRspUserLogout!BrokerID:{},UserID:{}", gatewayLogInfo, pUserLogout.getBrokerID(),
					pUserLogout.getUserID());

		}
		this.loginStatus = false;
	}

	// 错误回报
	public void OnRspError(CThostFtdcRspInfoField pRspInfo, int nRequestID, boolean bIsLast) {
		String logContent = MessageFormat.format("{0}行情接口错误回报!ErrorID:{1},ErrorMsg:{2},RequestID:{3},isLast{4}",
				gatewayLogInfo, pRspInfo.getErrorID(), pRspInfo.getErrorMsg(), nRequestID, bIsLast);
		
		log.info(logContent);
	}

	// 订阅合约回报
	public void OnRspSubMarketData(CThostFtdcSpecificInstrumentField pSpecificInstrument,
			CThostFtdcRspInfoField pRspInfo, int nRequestID, boolean bIsLast) {
		if (pRspInfo.getErrorID() == 0) {
			String logContent = gatewayLogInfo + "OnRspSubMarketData! 订阅合约成功:" + pSpecificInstrument.getInstrumentID();
			
			log.info(logContent);
		} else {

			String logContent = gatewayLogInfo + "OnRspSubMarketData! 订阅合约失败,ErrorID：" + pRspInfo.getErrorID()
					+ "ErrorMsg:" + pRspInfo.getErrorMsg();
			
			log.warn(logContent);
		}
	}

	// 退订合约回报
	public void OnRspUnSubMarketData(CThostFtdcSpecificInstrumentField pSpecificInstrument,
			CThostFtdcRspInfoField pRspInfo, int nRequestID, boolean bIsLast) {
		if (pRspInfo.getErrorID() == 0) {
			String logContent = gatewayLogInfo + "OnRspUnSubMarketData! 退订合约成功:"
					+ pSpecificInstrument.getInstrumentID();
			
			log.info(logContent);
		} else {
			String logContent = gatewayLogInfo + "OnRspUnSubMarketData! 退订合约失败,ErrorID：" + pRspInfo.getErrorID()
					+ "ErrorMsg:" + pRspInfo.getErrorMsg();
			
			log.warn(logContent);
		}
	}

	// 合约行情推送
	public void OnRtnDepthMarketData(CThostFtdcDepthMarketDataField pDepthMarketData) {
		if (pDepthMarketData != null) {

//			// T2T Test
//			if("IH1805".equals(pDepthMarketData.getInstrumentID())) {
//				System.out.println("T2T-Tick-"+System.nanoTime());
//			}
			String symbol = pDepthMarketData.getInstrumentID();

			if (!contractExchangeMap.containsKey(symbol)) {
				String logContent = gatewayLogInfo + "收到合约" + symbol + "行情,但尚未获取到交易所信息,丢弃";
				
				log.info(logContent);
			}

			// 上期所 郑商所正常,大商所错误
			// TODO 大商所时间修正
			Long updateTime = Long.valueOf(pDepthMarketData.getUpdateTime().replaceAll(":", ""));
			Long updateMillisec = (long) pDepthMarketData.getUpdateMillisec();
			Long actionDay = Long.valueOf(pDepthMarketData.getActionDay());

			String updateDateTimeWithMS = (actionDay * 100 * 100 * 100 * 1000 + updateTime * 1000 + updateMillisec)
					+ "";

			DateTime dateTime;
			try {
				dateTime = RtConstant.DT_FORMAT_WITH_MS_INT_FORMATTER.parseDateTime(updateDateTimeWithMS);
			} catch (Exception e) {
				log.error("{}解析日期发生异常", gatewayLogInfo, e);
				return;
			}

			String exchange = contractExchangeMap.get(symbol);
			String rtSymbol = symbol + "." + exchange;
			String tradingDay = tradingDayStr;
			String actionDayStr = pDepthMarketData.getActionDay();
			String actionTime = dateTime.toString(RtConstant.T_FORMAT_WITH_MS_INT_FORMATTER);
			Integer status = 0;
			Double lastPrice = pDepthMarketData.getLastPrice();
			Integer lastVolume = 0;
			Integer volume = pDepthMarketData.getVolume();
			Double openInterest = pDepthMarketData.getOpenInterest();
			Long preOpenInterest = 0L;
			Double preClosePrice = pDepthMarketData.getPreClosePrice();
			Double preSettlePrice = pDepthMarketData.getPreSettlementPrice();
			Double openPrice = pDepthMarketData.getOpenPrice();
			Double highPrice = pDepthMarketData.getHighestPrice();
			Double lowPrice = pDepthMarketData.getLowestPrice();
			Double upperLimit = pDepthMarketData.getUpperLimitPrice();
			Double lowerLimit = pDepthMarketData.getLowerLimitPrice();
			Double bidPrice1 = pDepthMarketData.getBidPrice1();
			Double bidPrice2 = pDepthMarketData.getBidPrice2();
			Double bidPrice3 = pDepthMarketData.getBidPrice3();
			Double bidPrice4 = pDepthMarketData.getBidPrice4();
			Double bidPrice5 = pDepthMarketData.getBidPrice5();
			Double bidPrice6 = 0d;
			Double bidPrice7 = 0d;
			Double bidPrice8 = 0d;
			Double bidPrice9 = 0d;
			Double bidPrice10 = 0d;
			Double askPrice1 = pDepthMarketData.getAskPrice1();
			Double askPrice2 = pDepthMarketData.getAskPrice2();
			Double askPrice3 = pDepthMarketData.getAskPrice3();
			Double askPrice4 = pDepthMarketData.getAskPrice4();
			Double askPrice5 = pDepthMarketData.getAskPrice5();
			Double askPrice6 = 0d;
			Double askPrice7 = 0d;
			Double askPrice8 = 0d;
			Double askPrice9 = 0d;
			Double askPrice10 = 0d;
			Integer bidVolume1 = pDepthMarketData.getBidVolume1();
			Integer bidVolume2 = pDepthMarketData.getBidVolume2();
			Integer bidVolume3 = pDepthMarketData.getBidVolume3();
			Integer bidVolume4 = pDepthMarketData.getBidVolume4();
			Integer bidVolume5 = pDepthMarketData.getBidVolume5();
			Integer bidVolume6 = 0;
			Integer bidVolume7 = 0;
			Integer bidVolume8 = 0;
			Integer bidVolume9 = 0;
			Integer bidVolume10 = 0;
			Integer askVolume1 = pDepthMarketData.getAskVolume1();
			Integer askVolume2 = pDepthMarketData.getAskVolume2();
			Integer askVolume3 = pDepthMarketData.getAskVolume3();
			Integer askVolume4 = pDepthMarketData.getAskVolume4();
			Integer askVolume5 = pDepthMarketData.getAskVolume5();
			Integer askVolume6 = 0;
			Integer askVolume7 = 0;
			Integer askVolume8 = 0;
			Integer askVolume9 = 0;
			Integer askVolume10 = 0;

			String logContent =  "收到合约" + symbol + "行情";
			
			log.info(logContent);
		} else {
			log.warn("{}OnRtnDepthMarketData! 收到行情信息为空", gatewayLogInfo);
		}
	}

	// 订阅期权询价
	public void OnRspSubForQuoteRsp(CThostFtdcSpecificInstrumentField pSpecificInstrument,
			CThostFtdcRspInfoField pRspInfo, int nRequestID, boolean bIsLast) {
		log.info("{}OnRspSubForQuoteRsp!", gatewayLogInfo);
	}

	// 退订期权询价
	public void OnRspUnSubForQuoteRsp(CThostFtdcSpecificInstrumentField pSpecificInstrument,
			CThostFtdcRspInfoField pRspInfo, int nRequestID, boolean bIsLast) {
		log.info("{}OnRspUnSubForQuoteRsp!", gatewayLogInfo);
	}

	// 期权询价推送
	public void OnRtnForQuoteRsp(CThostFtdcForQuoteRspField pForQuoteRsp) {
		log.info("{}OnRspUnSubForQuoteRsp!", gatewayLogInfo);
	}
	
	public static void main(String[] args) {
//		String envTmpDir = System.getProperty("java.io.tmpdir");
//		System.out.println(envTmpDir);
//		URL resource = MdSpi.class.getResource("/assembly/libthostmduserapi.so");
//		System.out.println(resource);
		HashMap<String,String> contractExchangeMap=Maps.newHashMap();
		contractExchangeMap.put("IH1809", "IH1809");
//		new MdSpi("180.168.146.187:10013", "9999", "084127", "1a1b1c1d1", contractExchangeMap).connect();
		GatewaySetting gatewaySetting=new GatewaySetting();
		gatewaySetting.setMdAddress("180.168.146.187:10013");
		gatewaySetting.setBrokerID("9999");
		gatewaySetting.setUserID("084127");
		gatewaySetting.setPassword("1a1b1c1d1");
		CtpGateway ctpGateway=new CtpGateway(gatewaySetting);
		ctpGateway.setContractExchangeMap(contractExchangeMap);
		new MdSpi(ctpGateway).connect();
	}

}
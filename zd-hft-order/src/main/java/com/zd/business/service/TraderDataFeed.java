package com.zd.business.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.shanghaizhida.beans.AccountInfo;
import com.shanghaizhida.beans.AccountResponseInfo;
import com.shanghaizhida.beans.CancelInfo;
import com.shanghaizhida.beans.CancelResponseInfo;
import com.shanghaizhida.beans.CommandCode;
import com.shanghaizhida.beans.Constants;
import com.shanghaizhida.beans.CurrencyInfo;
import com.shanghaizhida.beans.ErrorCode;
import com.shanghaizhida.beans.FilledResponseInfo;
import com.shanghaizhida.beans.FilledSearchInfo;
import com.shanghaizhida.beans.LoginHistoryIpInfo;
import com.shanghaizhida.beans.LoginInfo;
import com.shanghaizhida.beans.LoginResponseInfo;
import com.shanghaizhida.beans.ModifyClientPWS;
import com.shanghaizhida.beans.ModifyInfo;
import com.shanghaizhida.beans.NetInfo;
import com.shanghaizhida.beans.OpenSearchInfo;
import com.shanghaizhida.beans.OrderInfo;
import com.shanghaizhida.beans.OrderResponseInfo;
import com.shanghaizhida.beans.OrderSearchInfo;
import com.shanghaizhida.beans.OrderStatusInfo;
import com.shanghaizhida.beans.QuestionInfo;
import com.shanghaizhida.beans.VerifyInfo;
import com.shanghaizhida.beans.YingSunDelRequestInfo;
import com.shanghaizhida.beans.YingSunListRequestInfo;
import com.shanghaizhida.beans.YingSunListResponseInfo;
import com.shanghaizhida.beans.YingSunRequestInfo;
import com.shanghaizhida.beans.YingSunResponseInfo;
import com.shanghaizhida.core.net.ConnectionStateListener;
import com.shanghaizhida.core.net.TradingClient;
import com.zd.common.utils.MacUtils;
import com.zd.common.utils.calc.ArithDecimal;
import com.zd.common.utils.datetime.TimeUtil;
import com.zd.config.Global;

/**
 * 交易服务器线程
 * @author user
 *
 */
public class TraderDataFeed implements Runnable, ConnectionStateListener {

	private static final Logger logger = LoggerFactory.getLogger(TraderDataFeed.class);

	private volatile boolean isStillRunning = false;

	private TradingClient tradeClient = null;

	private volatile Thread thread = null;

	private boolean isConnrcted = false;

	private String userAccount=null;
	
	private String userPassWd=null;
	
	private boolean ispasswdOverdue=false;
	
	private boolean isLogin=false;
	
	private List<LoginHistoryIpInfo> loginSameIpList=Lists.newArrayList();
	
	public String host;
	public String port;

	
	/**
	 * 构造函数
	 */
	public TraderDataFeed(String host, String port,String userAccount,String userPassWd) {
		this.host = host;
		this.port = port;
		this.userAccount=userAccount;
		this.userPassWd=userPassWd;
	}

	/**
	 * 登陆返回数据(CurrencyNo,LoginResponseInfo) -用于在下单时获取用户类型和资金账户
	 */
	private HashMap<String, LoginResponseInfo> loginInfoMap;

	/**
	 * 下单返回数据.委托返回数据保存(SystemNo,OrderResponseInfo)
	 */
	private HashMap<String, OrderResponseInfo> weituoInfoMap;

	/**
	 * 下单请求数据(LocalNo,OrderResponseInfo)
	 */
	private HashMap<String, OrderResponseInfo> xiadanInfoMap;

	/**
	 * 挂单数据(SystemNo,OrderResponseInfo)
	 */
	private HashMap<String, OrderResponseInfo> guadanInfoMap;

	/**
	 * 持仓合计(Contract,OrderStatusInfo)
	 */
	private HashMap<String, OrderStatusInfo> chicangInfoMap;

	/**
	 * 成交明细(FilledNo,FilledResponseInfo)
	 */
	private HashMap<String, FilledResponseInfo> chengjiaoInfoMap;

	/**
	 * 成交合计(Exchange+Code+buysell,FilledResponseInfo)
	 */
	HashMap<String, FilledResponseInfo> chengjiaoTotalInfoMap;

	/**
	 * 资金数据(accountNo,AccountResponseInfo)
	 */
	private HashMap<String, AccountResponseInfo> zijinInfoMap;

	/**
	 * 基币账户
	 */
	private AccountResponseInfo jibiInfo;

	/** 币种列表(currencyNo,CurrencyInfo) */
	// private HashMap<String,CurrencyInfo> bizhongInfoMap = new
	// HashMap<String,CurrencyInfo>();

	/**
	 * 基币币种
	 */
	CurrencyInfo jibibizhong = new CurrencyInfo();

	/**
	 * 止损止盈设置数据列表(yingsunNo,YingSunListResponseInfo)
	 */
	private HashMap<String, YingSunListResponseInfo> yingsunListMap;

	/**
	 * 止盈损失败数据
	 */
	private HashMap<String, YingSunResponseInfo> yingsunSetFailList;

	private long endtime = 0;

	// 安全认证添加-----20180306-----begin
	/**
	 * 记录登陆返回的用户手机号
	 */
	private String userMobile = "";
	/**
	 * 记录是否已经设置了密保问题答案
	 */
	private boolean hasSetQA = false;
	/**
	 * 记录密保问题列表
	 */
	private ArrayList<QuestionInfo> questionList;
	/**
	 * 记录是否新设备第一次登陆
	 */
	private boolean isFirstLogin = false;
	/**
	 * 是否已经绑定过设备mac地址
	 */
	private boolean existMac = false;
	// 安全认证添加-----20180306-----end

	private void initDateList() {
		// if (loginInfoMap == null)
		// loginInfoMap = new HashMap<String, LoginResponseInfo>();
		// else
		// loginInfoMap.clear();

		if (weituoInfoMap == null)
			weituoInfoMap = new HashMap<String, OrderResponseInfo>();
		else
			weituoInfoMap.clear();

		if (xiadanInfoMap == null)
			xiadanInfoMap = new HashMap<String, OrderResponseInfo>();
		else
			xiadanInfoMap.clear();

		if (guadanInfoMap == null)
			guadanInfoMap = new HashMap<String, OrderResponseInfo>();
		else
			guadanInfoMap.clear();

		if (chicangInfoMap == null)
			chicangInfoMap = new HashMap<String, OrderStatusInfo>();
		else
			chicangInfoMap.clear();

		if (chengjiaoInfoMap == null)
			chengjiaoInfoMap = new HashMap<String, FilledResponseInfo>();
		else
			chengjiaoInfoMap.clear();

		if (chengjiaoTotalInfoMap == null)
			chengjiaoTotalInfoMap = new HashMap<String, FilledResponseInfo>();
		else
			chengjiaoTotalInfoMap.clear();

		if (zijinInfoMap == null)
			zijinInfoMap = new HashMap<String, AccountResponseInfo>();
		else
			zijinInfoMap.clear();

		if (yingsunListMap == null)
			yingsunListMap = new HashMap<String, YingSunListResponseInfo>();
		else
			yingsunListMap.clear();

		if (yingsunSetFailList == null)
			yingsunSetFailList = new HashMap<String, YingSunResponseInfo>();
		else
			yingsunSetFailList.clear();

		jibiInfo = null;

		// 安全认证添加-----20180306-----begin
		if (questionList == null)
			questionList = new ArrayList<QuestionInfo>();

		isFirstLogin = false;
		// 安全认证添加-----20180306-----end
	}

	/**
	 * 线程启动
	 */
	public void start() {
		try {
			if (tradeClient != null) {
				return;
			}
			// 注册Log记录

			logger.info("创建国际期货交易连接" + this.host + ":" + this.port);

			tradeClient = new TradingClient(this.host, this.port);
			tradeClient.setConnectionStateListener(this);
			tradeClient.start();

			isStillRunning = true;
			thread = new Thread(this);
			thread.start();
		} catch (Exception e) {
			logger.error("创建国际期货交易连接异常");
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

			// 发送退出登录指令
			sendLoginOut();

			isConnrcted = false;

			if (logger != null)
				logger.info("TraderDataFeed stop");

			if (tradeClient != null) {
				logger.error("国际期货交易线程停止");
				tradeClient.stop();
				tradeClient = null;
			}
			thread.interrupt();
			thread = null;
		}
	}

	@Override
	public void run() {

		while (isStillRunning) {

			byte[] rawMsg;

			try {

				rawMsg = tradeClient.dataQueue.take();

				int dataLen = com.shanghaizhida.Utils.byteArrayToInt(rawMsg);
				String strNetData = new String(rawMsg, 8, dataLen);

				NetInfo netInfo = new NetInfo();
				netInfo.MyReadString(strNetData);
				//将信息返回给中控
				//TODO
//				if(CommandCode.LOGIN.equals(netInfo.code) 
//						|| CommandCode.LOGINRID.equals(netInfo.code)
//						|| CommandCode.LOGINMULTI.equals(netInfo.code)
//						|| CommandCode.ORDER.equals(netInfo.code)
//						|| CommandCode.ORDERSTATUS.equals(netInfo.code)
//						|| CommandCode.HOLDSTATUS.equals(netInfo.code)
//						|| CommandCode.ACCOUNTLAST.equals(netInfo.code)
//						|| CommandCode.CANCELCAST.equals(netInfo.code)
//						|| CommandCode.SearchGuaDan.equals(netInfo.code)
//						|| CommandCode.SearchHoldTotal.equals(netInfo.code)
//						|| CommandCode.CURRENCYLIST.equals(netInfo.code)
//						|| CommandCode.ACCOUNTSEARCH.equals(netInfo.code)
//						|| CommandCode.FILLEDCAST.equals(netInfo.code)
//						|| CommandCode.FILLEDSEARCH.equals(netInfo.code)
//						|| CommandCode.FILLEDINFO.equals(netInfo.code)) {
//					
//				}
				netInfo.clientNo=userAccount;
				Global.traderInfoQueue.add(netInfo.MyToString());
				
				if (logger != null) {
					// 修改密码的log不保存,包含用户敏感信息
					if (CommandCode.MODIFYPW.equals(netInfo.code)) {
						logger.info("TraderDataFeed ReciveMsg: " + CommandCode.MODIFYPW);
					} else {
						logger.info("TraderDataFeed ReciveMsg: " + strNetData);
					}
				}

				// System.out.println(strNetData);

				// 此处处理所有返回过来的消息
				traderInfoHandler(netInfo);

			} catch (InterruptedException e) {
				break;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 连接状态
	 *
	 * @return true 已连接<br>
	 *         false 未连接
	 */
	public boolean isConnrcted() {
		return isConnrcted;
	}

	@Override
	public void onConnectStateChange(int code, String text) {

		if (code == ConnectionStateListener.RECONNECTED) {

			logger.info("国际期货交易已连接,开始登陆：" + userAccount + "  " + userPassWd);
			// System.out.println("国际期货交易已连接");
			if (logger != null)
				logger.info("TraderDataFeed onConnectStateChange ConnectionStateListener.RECONNECTED");

			isConnrcted = true;

			if (StringUtils.isNotBlank(userAccount) && StringUtils.isNotBlank(userPassWd)) {
				// 初始化数据list
				// initDateList();

				// 开始登陆
				this.sendLogin(userAccount, userPassWd, MacUtils.getMac());
			}

			// 
			// this.notifyObservers(new TraderTag(TraderTag.TRADER_SOCKET_SUCCESS));

			endtime = 0;
		} else if (code == ConnectionStateListener.CONNECTION_LOST) {

			logger.error("国际期货交易已断开");
			// System.out.println("国际期货交易已断开");
			if (logger != null)
				logger.info("TraderDataFeed onConnectStateChange ConnectionStateListener.CONNECTION_LOST");

			isConnrcted = false;

			
		} else if (code == ConnectionStateListener.SCOKET_CONNECTIONFAIL) {

			logger.error("国际期货交易连接创建失败");
			// System.out.println("国际期货交易连接创建失败");

			isConnrcted = false;

			if (System.currentTimeMillis() - endtime > 30000) {
				if (logger != null)
					logger.info("TraderDataFeed onConnectStateChange ConnectionStateListener.SCOKET_CONNECTIONFAIL");
				
				endtime = System.currentTimeMillis();
			}
		}
	}

	/**
	 * 加载交易相关数据
	 */
	public void loadTraderDate() {
		this.sendQueueSearch(userAccount);
		this.sendOrderSearch(userAccount, null);
		this.sendFilledSearch(userAccount);
		this.sendHoldTotalSearch(userAccount);
		this.sendRate();
		this.sendAccountSearch(userAccount);
		this.sendYingSunSetListRequestMessage(userAccount);
		// 安全认证添加-----20180306-----begin
		if (questionList != null && questionList.isEmpty()) {
			this.sendQuestionListSearch();
		}
		// 安全认证添加-----20180306-----end
	}

	/**
	 * 刷新挂单数据
	 */
	public void refreshQueueList() {
		// 锁上挂单list.同时清空,reload
		synchronized (guadanInfoMap) {
			guadanInfoMap.clear();
			this.sendQueueSearch(userAccount);
		}
	}

	/**
	 * 刷新委托数据
	 */
	public void refreshOrderList() {
		// 锁上委托list.同时清空,reload
		synchronized (weituoInfoMap) {
			weituoInfoMap.clear();
			this.sendOrderSearch(userAccount, null);
		}
	}

	/**
	 * 刷新成交数据
	 */
	public void refreshFilledList() {
		// 锁上成交list.同时清空,reload
		synchronized (chengjiaoTotalInfoMap) {
			chengjiaoTotalInfoMap.clear();
		}
		synchronized (chengjiaoInfoMap) {
			chengjiaoInfoMap.clear();
			this.sendFilledSearch(userAccount);
		}
	}

	/**
	 * 刷新持仓合计数据
	 */
	public void refreshHoldTotalList() {
		logger.info("刷新持仓合计");
		// 锁上持仓合计list.同时清空,reload
		synchronized (chicangInfoMap) {
			// chicangInfoMap.clear();
			this.sendHoldTotalSearch(userAccount);
		}
	}

	/**
	 * 刷新资金数据
	 */
	public void refreshAccountList() {
		// 锁上资金list.同时清空,reload
		synchronized (zijinInfoMap) {
			// zijinInfoMap.clear();
			this.sendAccountSearch(userAccount);
		}
	}

	/**
	 * 刷新赢损数据
	 */
	public void refreshYingSunList() {
		// 锁上资金list.同时清空,reload
		synchronized (yingsunListMap) {
			yingsunListMap.clear();
			this.sendYingSunSetListRequestMessage(userAccount);
		}
	}

	/**
	 * 处理交易返回数据
	 *
	 * @param netInfo
	 *            消息内容
	 */
	private void traderInfoHandler(NetInfo netInfo) {

		logger.info("TraderDataFeed traderInfoHandler netInfo.code = begin-----" + netInfo.code + "-----end");

		// 登陆成功
		if (CommandCode.LOGIN.equals(netInfo.code)) {

			if (ErrorCode.SUCCESS.equals(netInfo.errorCode)) {
				if (loginInfoMap == null)
					loginInfoMap = new HashMap<String, LoginResponseInfo>();
				else
					loginInfoMap.clear();

				// 接收用户下的账户信息列表
				String[] moneyAccount = infoTSplit(netInfo.infoT);

				if (moneyAccount.length > 0 && !moneyAccount[0].equals("")) {

					LoginResponseInfo loginResonse = null;

					for (int i = 0; i < moneyAccount.length; i++) {
						loginResonse = new LoginResponseInfo();
						loginResonse.MyReadString(moneyAccount[i]);

						loginInfoMap.put(loginResonse.FCurrencyNo, loginResonse);
					}

					if (loginResonse != null) {
						userAccount = loginResonse.userId;

						// 密码是否过期
						if (StringUtils.isNotBlank(loginResonse.frontendPort)) {
							if (loginResonse.frontendPort.equals("1")) {
								ispasswdOverdue = true;
							} else {
								ispasswdOverdue = false;
							}
						}

						// 安全认证添加-----20180306-----begin
						// 记录用户手机号
						userMobile = loginResonse.userMobile;
						// 记录密保问题答案是否已经设置
						if (loginResonse.hasSetQA.equals("1")) {
							hasSetQA = true;
						} else {
							hasSetQA = false;
						}
						// 是否已经绑定过设备mac地址
						if (loginResonse.existMac.equals("1")) {
							existMac = true;
						} else {
							existMac = false;
						}
						if (loginResonse.isFirstLogin.equals("1") && !isLogin) {
							// 设置首次登陆标志
							isFirstLogin = true;
							// 发送获取密保认证问题列表
							sendQuestionListSearch();
//							return;
						}
						// 安全认证添加-----20180306-----end
					}

					// refreshHoldTotalList(); // liwei@20161010刚登陆时资金数据有可能不能马上同步，在此刷新一下持仓改善此问题
					// 初始化数据list
					initDateList();

					loadTraderDate();

					isLogin = true;
				}

			} else {

				userAccount = "";
				userPassWd = "";
				isLogin = false;

				// 错误消息
				String failMsg = ErrorCode.getErrorMsgByCode(netInfo.errorCode);

				logger.info("TraderDataFeed Login failmsg = begin-----" + failMsg + "-----end");
				// System.out.println(failMsg);

				
			}

			// 
			// this.notifyObservers(tag);

			return;
		}
		// 账户异地登陆，当前账户已退出
		else if (CommandCode.LOGINRID.equals(netInfo.code)) {

			this.stop();

			isLogin = false;

			userPassWd = "";

			// 清空登陆map
			if (loginInfoMap == null)
				loginInfoMap = new HashMap<String, LoginResponseInfo>();
			else
				loginInfoMap.clear();

			initDateList();

			
			return;
		}
		// 提示用户前面已经有人在别处登陆过的代码
		else if (CommandCode.LOGINMULTI.equals(netInfo.code)) {

			return;
		}
		// 订单系统号返回
		else if (CommandCode.SYSTEMNOGET.equals(netInfo.code)) {
			// 返回的代码为SUCCESS
			if (ErrorCode.SUCCESS.equals(netInfo.errorCode)) {
				// 正常数据返回,map中找不到这个系统号,且本地号能找到时移除本地号保存的数据到系统号保存数据列表
				if (!weituoInfoMap.containsKey(netInfo.systemCode)
						&& xiadanInfoMap.containsKey(netInfo.localSystemCode)) {

					OrderResponseInfo orderResponseInfo = xiadanInfoMap.remove(netInfo.localSystemCode);

					orderResponseInfo.systemNo = netInfo.systemCode;
					orderResponseInfo.localNo = netInfo.localSystemCode;
					orderResponseInfo.accountNo = netInfo.accountNo;

					weituoInfoMap.put(netInfo.systemCode, orderResponseInfo);
					guadanInfoMap.put(netInfo.systemCode, orderResponseInfo);
				}
			} else {
				logger.error("CommandCode.SYSTEMNOGET netInfo.errorCode = " + netInfo.errorCode);
			}

			
			return;
		}
		// 下单返回
		else if (CommandCode.ORDER.equals(netInfo.code)) {

			String errorMsg = "";

			// 下单成功,infoT中是正常的完整数据
			if (netInfo.errorCode.equals(ErrorCode.SUCCESS)) {
				OrderResponseInfo orderResponseInfo = new OrderResponseInfo();
				orderResponseInfo.MyReadString(netInfo.infoT);

				// orderResponseInfo.orderState =
				// Constants.tradeStatusByNum(orderResponseInfo.orderState);
				orderResponseInfo.orderState = Constants.TRADE_STATUS_YIPAIDUI;

				weituoInfoMap.put(orderResponseInfo.systemNo, orderResponseInfo);
				guadanInfoMap.put(orderResponseInfo.systemNo, orderResponseInfo);

			}
			// 下单出现异常.或失败时,order中返回的数据是空的,这时候需要从下单的时候保存的列表中取出来用
			else {
				errorMsg = ErrorCode.getErrorMsgByCode(netInfo.errorCode);

				// 如果order消息先来,systemno后来
				if (!weituoInfoMap.containsKey(netInfo.systemCode)) {
					if (xiadanInfoMap.containsKey(netInfo.localSystemCode)) {
						OrderResponseInfo info = xiadanInfoMap.get(netInfo.localSystemCode);

						if (Constants.TRADE_STATUS_ZIJIN_LESS.equals(errorMsg))
							info.orderState = Constants.TRADE_STATUS_ZIJIN_LESS;
						else
							info.orderState = Constants.TRADE_STATUS_ZHILING_FAIL;

						weituoInfoMap.put(netInfo.systemCode, xiadanInfoMap.remove(netInfo.localSystemCode));
					}
				} else {
					// 如果是正常情况,先来systemno 后来order 此时在systemno中已经将订单加进来了,只需要设置订单状态为指令失败就好了
					OrderResponseInfo info = weituoInfoMap.get(netInfo.systemCode);

					if (Constants.TRADE_STATUS_ZIJIN_LESS.equals(errorMsg))
						info.orderState = Constants.TRADE_STATUS_ZIJIN_LESS;
					else
						info.orderState = Constants.TRADE_STATUS_ZHILING_FAIL;
				}

				// 下单失败 移除
				guadanInfoMap.remove(netInfo.systemCode);

				// refreshYingSunList();
			}

			
			return;
		}
		// 最新定单状态信息返回
		else if (CommandCode.ORDERSTATUS.equals(netInfo.code)) {

			updateWeituoInfoByOrderStatusInfo(netInfo);

			OrderResponseInfo info = weituoInfoMap.get(netInfo.systemCode);

			updateGuadanByWeituoInfo(info);

			

			return;
		}
		// 成交数据返回
		else if (CommandCode.FILLEDCAST.equals(netInfo.code)) {

			FilledResponseInfo responseInfo = new FilledResponseInfo();
			responseInfo.MyReadString(netInfo.infoT);

			// 将成交数据插入成交列表(明细)
			chengjiaoInfoMap.put(responseInfo.filledNo, responseInfo);

			// 更新成交合计
			FilledResponseInfo totalInfo = new FilledResponseInfo();
			totalInfo.MyReadString(netInfo.infoT);
			updateChengjiaoTotalInfo(totalInfo);

			
			return;
		}
		// 持仓状态返回
		else if (CommandCode.HOLDSTATUS.equals(netInfo.code)) {

			OrderStatusInfo orderStatusInfo = new OrderStatusInfo();
			orderStatusInfo.MyReadString(netInfo.infoT);

			// 对持仓map加线程同步
			synchronized (chicangInfoMap) {
				updateChicangTotalByOrderStatusInfo(orderStatusInfo);
			}

			
			return;
		}
		// 最新账户资金信息返回
		else if (CommandCode.ACCOUNTLAST.equals(netInfo.code)) {

			AccountInfo responseInfo = new AccountInfo();
			responseInfo.MyReadString(netInfo.infoT);

			// 对资金map加线程同步
			synchronized (zijinInfoMap) {
				updateLastAccountByAccountInfo(responseInfo);

				updateBaseCurrenyByRate();
			}

			
			return;
		}
		// 撤单
		else if (CommandCode.CANCELCAST.equals(netInfo.code)) {

			// 撤单成功
			if (netInfo.errorCode.equals(ErrorCode.SUCCESS)) {
				CancelResponseInfo cancelResponseInfo = new CancelResponseInfo();
				cancelResponseInfo.MyReadString(netInfo.infoT);

				updateGuadanWeituoByCancelInfo(cancelResponseInfo);

			}
			// 撤单失败
			else {
			}

			
			return;
		}
		// 改单
		else if (CommandCode.MODIFY.equals(netInfo.code)) {

			// 改单成功
			if (netInfo.errorCode.equals(ErrorCode.SUCCESS)) {

				OrderResponseInfo responseInfo = new OrderResponseInfo();
				responseInfo.MyReadString(netInfo.infoT);

				if (weituoInfoMap.containsKey(responseInfo.systemNo)) {

					OrderResponseInfo orderInfo = weituoInfoMap.get(responseInfo.systemNo);

					orderInfo.filledNumber = responseInfo.filledNumber;
					orderInfo.orderNumber = responseInfo.orderNumber;
					orderInfo.orderPrice = responseInfo.orderPrice;
					orderInfo.triggerPrice = responseInfo.triggerPrice;
				}

				if (guadanInfoMap.containsKey(responseInfo.systemNo)) {

					OrderResponseInfo orderInfo = guadanInfoMap.get(responseInfo.systemNo);

					orderInfo.filledNumber = responseInfo.filledNumber;
					orderInfo.orderNumber = responseInfo.orderNumber;
					orderInfo.orderPrice = responseInfo.orderPrice;
					orderInfo.triggerPrice = responseInfo.triggerPrice;
				}
			}
			// 改单失败
			else {
			}

			
			return;
		}
		// 委托
		else if (CommandCode.ORDERSEARCH.equals(netInfo.code)) {

			String[] weituoList = infoTSplit(netInfo.infoT);

			// 当委托列表为空时,分割出来的list长度为1,且为空
			if (StringUtils.isNotBlank(weituoList[0])) {

				for (int i = 0; i < weituoList.length; i++) {
					OrderResponseInfo orderResponseInfo = new OrderResponseInfo();
					orderResponseInfo.MyReadString(weituoList[i]);

					orderResponseInfo.orderState = Constants.tradeStatusByNum(orderResponseInfo.orderState);

					weituoInfoMap.put(orderResponseInfo.systemNo, orderResponseInfo);

					// 委托单中存在没有完全成交的挂单 放入挂单列表
					if (Constants.TRADE_STATUS_BUFEN.equals(orderResponseInfo.orderState)
							|| Constants.TRADE_STATUS_YIPAIDUI.equals(orderResponseInfo.orderState)
							|| Constants.TRADE_STATUS_YIQINGQIU.equals(orderResponseInfo.orderState)) {

						guadanInfoMap.put(orderResponseInfo.systemNo, orderResponseInfo);

					}
				}

			}

			
			return;
		}
		// 挂单查询
		else if (CommandCode.SearchGuaDan.equals(netInfo.code)) {

			String[] guadanList = infoTSplit(netInfo.infoT);

			// 当挂单列表为空时,分割出来的list长度为1,且为空
			if (StringUtils.isNotBlank(guadanList[0])) {

				for (int i = 0; i < guadanList.length; i++) {
					OrderResponseInfo orderResponseInfo = new OrderResponseInfo();
					orderResponseInfo.MyReadString(guadanList[i]);

					guadanInfoMap.put(orderResponseInfo.systemNo, orderResponseInfo);
				}

			}

			
			return;
		}
		// 持仓合计
		else if (CommandCode.SearchHoldTotal.equals(netInfo.code)) {

			String[] chicangList = infoTSplit(netInfo.infoT);

			// 当持仓列表为空时,分割出来的list长度为1,且为空
			if (StringUtils.isNotBlank(chicangList[0])) {
				synchronized (chicangInfoMap) {
					chicangInfoMap.clear();
					for (int i = 0; i < chicangList.length; i++) {
						OrderStatusInfo statusInfo = new OrderStatusInfo();
						statusInfo.MyReadString(chicangList[i]);

						// synchronized (chicangInfoMap) {
						updateChicangTotalByOrderStatusInfo(statusInfo);
						// }
					}
				}
			}

			
			return;
		}
		// 成交查询
		else if (CommandCode.FILLEDSEARCH.equals(netInfo.code)) {

			String[] chengjiaoList = infoTSplit(netInfo.infoT);

			// 当持仓列表为空时,分割出来的list长度为1,且为空
			if (StringUtils.isNotBlank(chengjiaoList[0])) {

				for (int i = 0; i < chengjiaoList.length; i++) {
					FilledResponseInfo filledResponseInfo = new FilledResponseInfo();
					filledResponseInfo.MyReadString(chengjiaoList[i]);

					// 将成交数据插入成交列表(明细)
					chengjiaoInfoMap.put(filledResponseInfo.filledNo, filledResponseInfo);

					// 更新成交合计
					FilledResponseInfo filledTotalInfo = new FilledResponseInfo();
					filledTotalInfo.MyReadString(chengjiaoList[i]);
					updateChengjiaoTotalInfo(filledTotalInfo);
				}

			}

			
			return;
		}
		// 资金查询
		else if (CommandCode.ACCOUNTSEARCH.equals(netInfo.code)) {

			String[] zijinList = infoTSplit(netInfo.infoT);

			// 当持仓列表为空时,分割出来的list长度为1,且为空
			if (StringUtils.isNotBlank(zijinList[0])) {
				// 对资金map加线程同步
				synchronized (zijinInfoMap) {
					zijinInfoMap.clear();
					for (int i = 0; i < zijinList.length; i++) {
						AccountResponseInfo info = new AccountResponseInfo();
						info.MyReadString(zijinList[i]);

						zijinInfoMap.put(info.accountNo, info);
					}

					updateBaseCurrenyByRate();
				}
			}

			
			return;
		}
		// 币种对应汇率
		else if (CommandCode.CURRENCYLIST.equals(netInfo.code)) {

			String[] bizhongList = infoTSplit(netInfo.infoT);

			// 每次登陆时清空一下币种信息
			// bizhongInfoMap.clear();

			// 当持仓列表为空时,分割出来的list长度为1,且为空
			if (bizhongList.length == 1 && bizhongList[0].isEmpty())
				return;

			for (int i = 0; i < bizhongList.length; i++) {
				// LogUtil.e("sky----------bizhongList-----i = " + i + " bizhong = " +
				// bizhongList[i]);
				CurrencyInfo currencyInfo = new CurrencyInfo();
				currencyInfo.MyReadString(bizhongList[i]);

				if (currencyInfo.isBase == 1)
					jibibizhong = currencyInfo;
				// bizhongInfoMap.put(currencyInfo.currencyNo,currencyInfo);
			}

			// 
			// this.notifyObservers(new TraderTag(TraderTag.TRADER_TYPE_ACCOUNT));
		}

		// add by xiang 2015-11-30 start

		// 止损止盈设置 数据列表数据返回
		else if (CommandCode.GETYINGSUNLIST.equals(netInfo.code)) {

			String[] yingsunList = infoTSplit(netInfo.infoT);

			// 止损止盈设置列表为空时,分割出来的list长度为1,且为空
			if (StringUtils.isNotBlank(yingsunList[0])) {

				for (int i = 0; i < yingsunList.length; i++) {
					YingSunListResponseInfo yingSunListResponseInfo = new YingSunListResponseInfo();
					yingSunListResponseInfo.MyReadString(yingsunList[i]);

					yingsunListMap.put(yingSunListResponseInfo.yingsunNo, yingSunListResponseInfo);
				}

			}

			
			return;
		}

		// 当服务器断开，此时赢损触发会收到此指令，再次重新获取赢损list，刷新
		else if (CommandCode.ORDER004.equals(netInfo.code)) {

			// 服务器断开会返回这个error，接收它处理
			if (netInfo.errorCode.equals(ErrorCode.ERR_COMMON_0001)) {
				refreshYingSunList();
			}

			return;
		}

		// add by xiang 2015-11-30 end

		// 修改密码
		else if (CommandCode.MODIFYPW.equals(netInfo.code)) {
			// MODIFYPW@@@1@00000@@@@&000003@888888@1
			// MODIFYPW@@@1@修改密码出错@@@@&000003@12@7

			ModifyClientPWS info = new ModifyClientPWS();
			info.MyReadString(netInfo.infoT);

			// 修改成功
			if (ErrorCode.SUCCESS.equals(netInfo.errorCode)) {
				userPassWd = info.newPws;

			}
			// 修改失败
			else {
			}
			
		}
		// 登陆IP返回
		else if (CommandCode.GetLoginHistoryList.equals(netInfo.code)) {
			// LogHisLs@@@@00000@@@@@&192.168.1.140:5443@192.168.1.140:5443@2016-08-18
			// 13:51:58@1

			String[] ipList = infoTSplit(netInfo.infoT);

			// 当登陆ip列表为空时,分割出来的list长度为1,且为空
			if (StringUtils.isNotBlank(ipList[0])) {

				loginSameIpList.clear();

				for (int i = 0; i < ipList.length; i++) {
					LoginHistoryIpInfo ipInfo = new LoginHistoryIpInfo();
					ipInfo.MyReadString(ipList[i]);

					if (ipInfo.LoginType.equals("1")) {
						return;
					} else {
						loginSameIpList.add(ipInfo);
					}
				}
			}
			return;
		}
		// 安全认证添加-----20180306-----begin
		// 获取密保问题返回
		else if (CommandCode.GetVerifyQuestionList.equals(netInfo.code)) {

			// 获取成功
			if (ErrorCode.SUCCESS.equals(netInfo.errorCode)) {
				if (questionList == null)
					questionList = new ArrayList<QuestionInfo>();
				else
					questionList.clear();

				String[] list = infoTSplit(netInfo.infoT);
				if (StringUtils.isNotBlank(list[0])) {
					for (int i = 0; i < list.length; i++) {
						QuestionInfo info = new QuestionInfo();
						info.MyReadString(list[i]);

						questionList.add(info);
					}
				}

				if (isFirstLogin) {
				} else {
				}
			}
			// 获取失败
			else {
				// 错误消息
			}

			
		}
		// 发送手机验证码返回
		else if (CommandCode.ReqVerifyCode.equals(netInfo.code)) {

			// 获取成功
			if (ErrorCode.SUCCESS.equals(netInfo.errorCode)) {
			}
			// 获取失败
			else {
				// 错误消息
			}

			
		}
		// 安全认证返回
		else if (CommandCode.SafeVerify.equals(netInfo.code)) {

			// 获取成功
			if (ErrorCode.SUCCESS.equals(netInfo.errorCode)) {
				// 安全认证成功直接登录
				// this.sendLogin(userAccount, userPassWd, "123456");
				// 接收接收到认证返回消息，因为会返回两次，需要根据返回的消息，做分别处理
				String[] receiveInfo = infoTSplit(netInfo.infoT);

				if (receiveInfo.length > 0 && !receiveInfo[0].equals("")) {
					String temp = receiveInfo[0];
					String[] arrClass = temp.split("@");

					if (arrClass.length < 10) {
						// 这个返回的是认证的确认
						
					} else {
						// 这个返回的是登陆账户信息
						if (!isLogin) {
							// 初始化数据list
							initDateList();

							loadTraderDate();

							isLogin = true;

							
						}
					}
				} else {
					
				}
			}
			// 获取失败
			else {
				// 错误消息
				
			}
		}
		// 密保问题答案设置返回
		else if (CommandCode.SetVerifyQA.equals(netInfo.code)) {

			// 获取成功
			if (ErrorCode.SUCCESS.equals(netInfo.errorCode)) {
			}
			// 获取失败
			else {
			}

			
		}
		// 安全认证添加-----20180306-----end
		// 其他错误消息
		else {
			if (netInfo.errorCode.equals(ErrorCode.SUCCESS)) {
				return;
			}
			// 错误消息
			String failMsg = ErrorCode.getErrorMsgByCode(netInfo.errorCode);
			// System.out.println(failMsg);
			logger.error("TraderDataFeed traderInfoHandler failMsg = " + failMsg);

			
		}
	}

	/**
	 * 根据币种汇率和账户币种计算基币数据
	 */
	private void updateBaseCurrenyByRate() {

		if (zijinInfoMap.size() == 0)
			return;

		try {

			jibiInfo = new AccountResponseInfo();

			jibiInfo.accountNo = "基币";
			jibiInfo.currencyName = "基币";

			if (StringUtils.isNotBlank(jibibizhong.currencyNo))
				jibiInfo.currencyNo = "基币-" + jibibizhong.currencyNo;

			Iterator<Entry<String, AccountResponseInfo>> iterator = zijinInfoMap.entrySet().iterator();

			while (iterator.hasNext()) {
				Map.Entry<String, AccountResponseInfo> entry = (Map.Entry<String, AccountResponseInfo>) iterator.next();
				AccountResponseInfo info = entry.getValue();

				double currencyRate = Double
						.parseDouble(StringUtils.isBlank(info.currencyRate) ? "0" : info.currencyRate);

				if (StringUtils.isBlank(jibibizhong.currencyNo) && currencyRate == 1)
					jibiInfo.currencyNo = "基币-" + info.currencyNo;

				jibiInfo.todayBalance += ArithDecimal.mul(info.todayBalance, currencyRate);
				jibiInfo.todayCanUse += ArithDecimal.mul(info.todayCanUse, currencyRate);
				jibiInfo.margin += ArithDecimal.mul(info.margin, currencyRate);
				jibiInfo.floatingProfit += ArithDecimal.mul(info.floatingProfit, currencyRate);
				jibiInfo.inMoney += ArithDecimal.mul(info.inMoney, currencyRate);
				jibiInfo.outMoney += ArithDecimal.mul(info.outMoney, currencyRate);
				jibiInfo.riskRate += ArithDecimal.mul(info.riskRate, currencyRate);
				jibiInfo.keepDeposit += ArithDecimal.mul(info.keepDeposit, currencyRate);
				jibiInfo.oldCanUse += ArithDecimal.mul(info.oldCanUse, currencyRate);
				jibiInfo.oldBalance += ArithDecimal.mul(info.oldBalance, currencyRate);
				jibiInfo.oldAmount += ArithDecimal.mul(info.oldAmount, currencyRate);
				jibiInfo.todayAmount += ArithDecimal.mul(info.todayAmount, currencyRate);
				jibiInfo.freezenMoney += ArithDecimal.mul(info.freezenMoney, currencyRate);
				jibiInfo.profitRate += ArithDecimal.mul(info.profitRate, currencyRate);
				jibiInfo.unexpiredProfit += ArithDecimal.mul(info.unexpiredProfit, currencyRate);
				jibiInfo.unaccountProfit += ArithDecimal.mul(info.unaccountProfit, currencyRate);
				jibiInfo.royalty += ArithDecimal.mul(info.royalty, currencyRate);
				jibiInfo.netProfit += ArithDecimal.mul(info.netProfit, currencyRate);
				jibiInfo.credit += ArithDecimal.mul(info.credit, currencyRate);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 根据CancelResponseInfo更新挂单和委托数据
	 *
	 * @param cancelResponseInfo
	 *            撤单返回数据
	 */
	private void updateGuadanWeituoByCancelInfo(CancelResponseInfo cancelResponseInfo) {
		if (weituoInfoMap.containsKey(cancelResponseInfo.systemNo)) {

			OrderResponseInfo info = weituoInfoMap.get(cancelResponseInfo.systemNo);

			int filledNumber = StringUtils.isBlank(info.filledNumber) ? 0 : Integer.parseInt(info.filledNumber);
			int orderNumber = StringUtils.isBlank(info.orderNumber) ? 0 : Integer.parseInt(info.orderNumber);

			// 如果成交量>0 && < 订单数 此时撤单是已撤余单状态,否则就是已撤单
			if (filledNumber != 0 && filledNumber < orderNumber) {
				info.orderState = Constants.TRADE_STATUS_YICHEYUDAN;
			} else {
				info.orderState = Constants.TRADE_STATUS_YICHEDAN;
			}
		}

		// 已撤单订单直接从挂单列表移除,如果没有系统号对应订单,remove不操作
		guadanInfoMap.remove(cancelResponseInfo.systemNo);
	}

	/**
	 * 根据OrderStatusInfo 更新委托信息
	 *
	 * @param netInfo
	 *            包含systemno的完整消息体
	 */
	private void updateWeituoInfoByOrderStatusInfo(NetInfo netInfo) {
		OrderResponseInfo orderResponseInfo = null;

		OrderStatusInfo orderStatusInfo = new OrderStatusInfo();
		orderStatusInfo.MyReadString(netInfo.infoT);

		OrderResponseInfo myInfo = weituoInfoMap.get(netInfo.systemCode);

		if (myInfo != null) {

			if (StringUtils.isBlank(myInfo.orderNo)) {
				// 委托数据存在的时候,但是没有订单号,此时的数据是下单保存的数据,需要用OrderStatusInfo来更新一下
				myInfo.orderNo = orderStatusInfo.orderNo;
				myInfo.filledNumber = orderStatusInfo.filledNumber;
				myInfo.filledPrice = orderStatusInfo.filledAdvPrice;
				myInfo.statusSeq = orderStatusInfo.status;
			}

			orderResponseInfo = myInfo;
		} else {
			orderResponseInfo = new OrderResponseInfo();

			orderResponseInfo.code = orderStatusInfo.contractNo;
			orderResponseInfo.orderNo = orderStatusInfo.orderNo;
			orderResponseInfo.orderNumber = orderStatusInfo.orderNumber;
			orderResponseInfo.filledNumber = orderStatusInfo.filledNumber;
			orderResponseInfo.filledPrice = orderStatusInfo.filledAdvPrice;
			orderResponseInfo.statusSeq = orderStatusInfo.status;
			orderResponseInfo.accountNo = orderStatusInfo.accountNo;

			weituoInfoMap.put(netInfo.systemCode, orderResponseInfo);
		}

		int orderResponseOrderNumber = Integer
				.parseInt(StringUtils.isBlank(orderResponseInfo.orderNumber) ? "0" : orderResponseInfo.orderNumber);
		int orderStatusOrderNumber = Integer
				.parseInt(StringUtils.isBlank(orderStatusInfo.orderNumber) ? "0" : orderStatusInfo.orderNumber);

		int orderResponseFilledNumber = Integer
				.parseInt(StringUtils.isBlank(orderResponseInfo.filledNumber) ? "0" : orderResponseInfo.filledNumber);
		int orderStatusFilledNumber = Integer
				.parseInt(StringUtils.isBlank(orderStatusInfo.filledNumber) ? "0" : orderStatusInfo.filledNumber);

		if (orderResponseOrderNumber == 0 && orderStatusOrderNumber != 0) {
			orderResponseInfo.orderNumber = orderStatusInfo.orderNumber;
		}

		if (orderResponseFilledNumber > orderStatusFilledNumber) {
			// 委托数据中的成交量已经大于状态信息中的已成交量了，就不用再处理
			return;
		}

		// -1顺序号的数据必须更新（改单撤单失败返回的状态数据顺序号为-1）
		if (orderStatusInfo.status == -1) {

		} else {
			// 委托数据中的状态顺序号已经比新来的状态顺序号大了或者已经是-1了就不用更新了
			if (orderResponseInfo.statusSeq == -1 || orderResponseInfo.statusSeq > orderStatusInfo.status) {
				return;
			}
		}

		orderResponseInfo.statusSeq = orderStatusInfo.status;

		// 成交数量一样的时候，要么是成交的时候又撤单了，要么撤单的时候又成交了
		if (orderResponseFilledNumber == orderStatusFilledNumber) {
			if (orderStatusFilledNumber == 0) {
				if ("1".equals(orderStatusInfo.isCanceled)) {
					orderResponseInfo.orderState = Constants.TRADE_STATUS_YICHEDAN;
				} else {
					orderResponseInfo.orderState = Constants.TRADE_STATUS_YIPAIDUI;
				}
			} else {
				// 成交数=下单数时是完全成交
				if (orderResponseFilledNumber == orderResponseOrderNumber) {
					orderResponseInfo.orderState = Constants.TRADE_STATUS_WANQUAN;
				}
				// 否则就是已撤余单
				else {
					if ("1".equals(orderStatusInfo.isCanceled)) {
						orderResponseInfo.orderState = Constants.TRADE_STATUS_YICHEYUDAN;
					}
				}
			}
		}
		// 成交数有更新
		else {
			orderResponseInfo.filledPrice = orderStatusInfo.filledAdvPrice;
			orderResponseInfo.filledNumber = orderStatusInfo.filledNumber;

			orderResponseFilledNumber = Integer.parseInt(
					StringUtils.isBlank(orderResponseInfo.filledNumber) ? "0" : orderResponseInfo.filledNumber);

			// 原来是已撤余单状态
			if (orderResponseInfo.orderState == Constants.TRADE_STATUS_YICHEYUDAN) {
				// 变完全成交了
				if (orderResponseFilledNumber == orderResponseOrderNumber) {
					orderResponseInfo.orderState = Constants.TRADE_STATUS_WANQUAN;
				}
			}
			// 原来是已撤单状态
			else if (orderResponseInfo.orderState == Constants.TRADE_STATUS_YICHEDAN) {
				// 变完全成交了
				if (orderResponseFilledNumber == orderResponseOrderNumber) {
					orderResponseInfo.orderState = Constants.TRADE_STATUS_WANQUAN;
				}
				// 变已撤余单了
				else if (orderStatusFilledNumber > 0) {
					orderResponseInfo.orderState = Constants.TRADE_STATUS_YICHEYUDAN;
				}
			}
			// 原来不是撤单
			else {
				// 新数据是撤单
				if (orderStatusInfo.isCanceled == "1") {
					// 变完全成交了
					if (orderResponseFilledNumber == orderResponseOrderNumber) {
						orderResponseInfo.orderState = Constants.TRADE_STATUS_WANQUAN;
					} else {
						if (orderStatusFilledNumber > 0) {
							orderResponseInfo.orderState = Constants.TRADE_STATUS_YICHEYUDAN;
						} else {
							orderResponseInfo.orderState = Constants.TRADE_STATUS_YICHEDAN;
						}
					}
				}
				// 新数据不是撤单
				else {
					// 变完全成交了
					if (orderResponseFilledNumber == orderResponseOrderNumber) {
						orderResponseInfo.orderState = Constants.TRADE_STATUS_WANQUAN;
					} else {
						orderResponseInfo.orderState = Constants.TRADE_STATUS_BUFEN;
					}
				}
			}
		}
	}

	/**
	 * 根据UpdateWeituoInfoByOrderStatusInfo函数更新出来的委托信息更新挂单数据
	 *
	 * @param info
	 *            委托数据
	 */
	private void updateGuadanByWeituoInfo(OrderResponseInfo info) {

		if (info == null || StringUtils.isBlank(info.systemNo))
			return;

		// 找到对应的是哪个挂单
		OrderResponseInfo guadanInfo = null;

		guadanInfo = guadanInfoMap.get(info.systemNo);

		// 需要生成挂单数据
		if (guadanInfo == null && !info.orderState.equals(Constants.TRADE_STATUS_WANQUAN)
				&& !info.orderState.equals(Constants.TRADE_STATUS_YICHEDAN)
				&& !info.orderState.equals(Constants.TRADE_STATUS_YICHEYUDAN)
				&& !info.orderState.equals(Constants.TRADE_STATUS_ZHILING_FAIL)
				&& !info.orderState.equals(Constants.TRADE_STATUS_ZIJIN_LESS)) {

			guadanInfoMap.put(info.systemNo, info);

		} else {

			if (!info.orderState.equals(Constants.TRADE_STATUS_WANQUAN)
					&& !info.orderState.equals(Constants.TRADE_STATUS_YICHEDAN)
					&& !info.orderState.equals(Constants.TRADE_STATUS_YICHEYUDAN)
					&& !info.orderState.equals(Constants.TRADE_STATUS_ZHILING_FAIL)
					&& !info.orderState.equals(Constants.TRADE_STATUS_ZIJIN_LESS)) {

				guadanInfo.orderState = info.orderState;
				guadanInfo.filledPrice = info.filledPrice;
				guadanInfo.filledNumber = info.filledNumber;
				guadanInfo.triggerPrice = info.triggerPrice;

			} else {
				guadanInfoMap.remove(info.systemNo);
			}
		}
	}

	/**
	 * 根据OrderStatusInfo 更新持仓信息
	 *
	 * @param responseInfo
	 */
	private void updateChicangTotalByOrderStatusInfo(OrderStatusInfo responseInfo) {
		try {

			OrderStatusInfo chicangTotal = null;

			chicangTotal = chicangInfoMap.get(responseInfo.contractNo);

			// 已经有数据了，做更新即可
			if (chicangTotal != null) {

				// 状态更新就不用更新了
				if (chicangTotal.status > responseInfo.status) {
					return;
				}

				chicangTotal.status = responseInfo.status;

				int buyHoldNumber = StringUtils.isBlank(responseInfo.buyHoldNumber) ? 0
						: Integer.parseInt(responseInfo.buyHoldNumber);
				int saleHoldNumber = StringUtils.isBlank(responseInfo.saleHoldNumber) ? 0
						: Integer.parseInt(responseInfo.saleHoldNumber);

				if (buyHoldNumber == 0 && saleHoldNumber == 0) {
					chicangInfoMap.remove(responseInfo.contractNo);
					return;
				} else {

					chicangTotal.buyHoldPrice = responseInfo.buyHoldPrice;
					chicangTotal.buyHoldNumber = responseInfo.buyHoldNumber;

					chicangTotal.saleHoldPrice = responseInfo.saleHoldPrice;
					chicangTotal.saleHoldNumber = responseInfo.saleHoldNumber;

					if (buyHoldNumber > 0) {
						chicangTotal.buySale = Constants.TRADE_BUYSALE_BUY;
						chicangTotal.buyHoldOpenPrice = responseInfo.buyHoldOpenPrice;
						// 添加持买保证金
						chicangTotal.holdMarginBuy = responseInfo.holdMarginBuy;
					}

					if (saleHoldNumber > 0) {
						chicangTotal.buySale = Constants.TRADE_BUYSALE_SALE;
						chicangTotal.saleHoldOpenPrice = responseInfo.saleHoldOpenPrice;
						// 添加持卖保证金
						chicangTotal.holdMarginSale = responseInfo.holdMarginSale;
					}
				}
			}
			// 需要插入新数据
			else {

				int buyHoldNumber = StringUtils.isBlank(responseInfo.buyHoldNumber) ? 0
						: Integer.parseInt(responseInfo.buyHoldNumber);
				int saleHoldNumber = StringUtils.isBlank(responseInfo.saleHoldNumber) ? 0
						: Integer.parseInt(responseInfo.saleHoldNumber);

				if (buyHoldNumber == 0 && saleHoldNumber == 0) {
					return;
				}

				chicangTotal = new OrderStatusInfo();

				chicangTotal.contractNo = responseInfo.contractNo;
				chicangTotal.exchangeNo = responseInfo.exchangeNo;
				chicangTotal.accountNo = responseInfo.accountNo;

				chicangTotal.buyHoldPrice = responseInfo.buyHoldPrice;
				chicangTotal.buyHoldNumber = responseInfo.buyHoldNumber;

				chicangTotal.saleHoldPrice = responseInfo.saleHoldPrice;
				chicangTotal.saleHoldNumber = responseInfo.saleHoldNumber;

				if (buyHoldNumber > 0) {
					chicangTotal.buySale = Constants.TRADE_BUYSALE_BUY;
					chicangTotal.buyHoldOpenPrice = responseInfo.buyHoldOpenPrice;
					// 添加持买保证金
					chicangTotal.holdMarginBuy = responseInfo.holdMarginBuy;
				}

				if (saleHoldNumber > 0) {
					chicangTotal.buySale = Constants.TRADE_BUYSALE_SALE;
					chicangTotal.saleHoldOpenPrice = responseInfo.saleHoldOpenPrice;
					// 添加持卖保证金
					chicangTotal.holdMarginSale = responseInfo.holdMarginSale;
				}

				chicangTotal.status = responseInfo.status;

				// 添加最新价或结算价到持仓map
				chicangTotal.currPrice = responseInfo.currPrice;

				chicangInfoMap.put(responseInfo.contractNo, chicangTotal);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 根据AccountInfo更新资金信息
	 *
	 * @param responseInfo
	 */
	private void updateLastAccountByAccountInfo(AccountInfo responseInfo) {
		try {

			AccountResponseInfo info = null;

			info = zijinInfoMap.get(responseInfo.accountNo);

			if (info != null) {

				// 资金状态不是最新就不作更新
				if (responseInfo.status == 0 || responseInfo.status <= info.status) {
					return;
				} else {
					info.status = responseInfo.status;
				}

				// 今可用
				info.todayCanUse = Double
						.parseDouble(StringUtils.isBlank(responseInfo.available.trim()) ? "0" : responseInfo.available);
				// 冻结资金
				info.freezenMoney = Double.parseDouble(
						StringUtils.isBlank(responseInfo.frozenDeposit.trim()) ? "0" : responseInfo.frozenDeposit);
				// 入金
				info.inMoney = Double
						.parseDouble(StringUtils.isBlank(responseInfo.inMoney.trim()) ? "0" : responseInfo.inMoney);
				// 出金
				info.outMoney = Double
						.parseDouble(StringUtils.isBlank(responseInfo.outMoney.trim()) ? "0" : responseInfo.outMoney);
				// 手续费
				info.commission = Double
						.parseDouble(StringUtils.isBlank(responseInfo.fee.trim()) ? "0" : responseInfo.fee);
				// 平仓盈亏
				info.floatingProfit = Double.parseDouble(
						StringUtils.isBlank(responseInfo.expiredProfit.trim()) ? "0" : responseInfo.expiredProfit);
				// 保证金
				info.margin = Double
						.parseDouble(StringUtils.isBlank(responseInfo.deposit.trim()) ? "0" : responseInfo.deposit);
				// 今结存
				info.todayAmount = Double
						.parseDouble(StringUtils.isBlank(responseInfo.money.trim()) ? "0" : responseInfo.money);
				// 维持保证金
				info.keepDeposit = Double.parseDouble(
						StringUtils.isBlank(responseInfo.keepDeposit.trim()) ? "0" : responseInfo.keepDeposit);
				// 未结平盈
				info.unaccountProfit = Double.parseDouble(
						StringUtils.isBlank(responseInfo.unaccountProfit.trim()) ? "0" : responseInfo.unaccountProfit);
				// 期权权利金
				info.royalty = Double
						.parseDouble(StringUtils.isBlank(responseInfo.royalty.trim()) ? "0" : responseInfo.royalty);
				// 客户权益
				info.todayBalance = Double.parseDouble(
						StringUtils.isBlank(responseInfo.todayTotal.trim()) ? "0" : responseInfo.todayTotal);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 退出登录,本质上是断开连接,同时发送一个消息到TraderViewPageFragment,关闭交易界面
	 */
	public void loginOut() {

		this.stop();

		isLogin = false;

		// 退出登录 将行情权限设置为false 即不允许

		userPassWd = "";

		// 清空登陆map
		if (loginInfoMap == null)
			loginInfoMap = new HashMap<String, LoginResponseInfo>();
		else
			loginInfoMap.clear();

		initDateList();

		
	}

	/**
	 * 发送消息测试
	 *
	 * @param msg
	 */
	@Deprecated
	public void sendMsg(String msg) {
		if (tradeClient != null)
			tradeClient.sendAsciiMsg(msg);
	}

	/**
	 * 登陆
	 */
	public void sendLogin(String userid, String userpw) {

		try {

			LoginInfo loginInfo = new LoginInfo();
			loginInfo.userId = userid;
			loginInfo.userPwd = userpw;

			NetInfo netInfo = new NetInfo();
			netInfo.code = CommandCode.LOGIN;

			netInfo.todayCanUse = "C";
			netInfo.localSystemCode = "M";
			netInfo.systemCode = "pc_hft";
			netInfo.infoT = loginInfo.MyToString();

			if (tradeClient != null)
				tradeClient.sendAsciiMsg(netInfo.MyToString());

			if (logger != null)
				logger.info("TraderDataFeed SendMsg: " + netInfo.MyToString());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 退出登陆
	 */
	public void sendLoginOut() {
		if (isLogin) {
			NetInfo netInfo = new NetInfo();
			netInfo.code = CommandCode.UNLOGIN;

			netInfo.clientNo = userAccount;
			netInfo.todayCanUse = "0";

			if (tradeClient != null)
				tradeClient.sendAsciiMsg(netInfo.MyToString());

			if (logger != null)
				logger.info("TraderDataFeed SendMsg: " + netInfo.MyToString());
		}
	}

	/**
	 * 下单
	 *
	 * @param orderInfo
	 */
	public void sendOrder(OrderInfo orderInfo) {

		NetInfo netInfo = new NetInfo();
		netInfo.code = CommandCode.ORDER;

//		netInfo.localSystemCode = getLocalSystemCode();
		netInfo.localSystemCode =orderInfo.localSystemCode;
		netInfo.accountNo = orderInfo.accountNo;
		netInfo.exchangeCode = orderInfo.exchangeCode;
		netInfo.infoT = orderInfo.MyToString();

		saveOrderInfo(orderInfo, netInfo.localSystemCode);

		if (tradeClient != null)
			tradeClient.sendAsciiMsg(netInfo.MyToString());

		if (logger != null)
			logger.info("TraderDataFeed SendMsg: " + netInfo.MyToString());
	}

	/**
	 * 改单
	 *
	 * @param modifyInfo
	 */
	public void sendModify(ModifyInfo modifyInfo, String localSystemCode, String systemCode) {

		NetInfo netInfo = new NetInfo();
		netInfo.code = CommandCode.MODIFY;

		// 资金账号
		netInfo.accountNo = modifyInfo.accountNo;
		netInfo.exchangeCode = modifyInfo.exchangeCode;
		netInfo.clientNo = modifyInfo.userId;
		netInfo.localSystemCode = localSystemCode;
		// "20140318000002";
		netInfo.systemCode = systemCode;
		// "00000341100158";
		netInfo.infoT = modifyInfo.MyToString();

		if (tradeClient != null)
			tradeClient.sendAsciiMsg(netInfo.MyToString());

		if (logger != null)
			logger.info("TraderDataFeed SendMsg: " + netInfo.MyToString());
	}

	/**
	 * 撤单
	 *
	 * @param cancelInfo
	 */
	public void sendCancel(CancelInfo cancelInfo) {

		NetInfo netInfo = new NetInfo();
		netInfo.code = CommandCode.CANCEL;

		netInfo.accountNo = cancelInfo.accountNo;
		netInfo.systemCode = cancelInfo.systemNo;
		netInfo.exchangeCode = cancelInfo.exchangeCode;
		netInfo.infoT = cancelInfo.MyToString();

		if (tradeClient != null)
			tradeClient.sendAsciiMsg(netInfo.MyToString());

		if (logger != null)
			logger.info("TraderDataFeed SendMsg: " + netInfo.MyToString());
	}

	/**
	 * 挂单
	 *
	 * @param userId
	 */
	public void sendQueueSearch(String userId) {
		OrderSearchInfo orderSearchInfo = new OrderSearchInfo();
		orderSearchInfo.userId = userId;

		NetInfo netInfo = new NetInfo();
		// netInfo.code = CommandCode.ORDERSEARCH;
		// SearchGuaDan 在服务端做了处理，使用此命令时可以不用设置todayCanUse = "L";
		// 但是使用 ORDERSEARCH 来查询挂单时 必须设置
		netInfo.code = CommandCode.SearchGuaDan;

		// netInfo.infoT = orderSearchInfo.MyToString();
		netInfo.accountNo = orderSearchInfo.userId;

		// 只加载已请求，已排队，部分成交和自定义策略单的委托数据
		// netInfo.todayCanUse = "L";
		// 除已排队部分成交和自定义策略单数据外的最新委托数据最大加载数量
		// net.systemCode = MaxLoadOrderDataQuantity.ToString();
		netInfo.infoT = orderSearchInfo.MyToString();

		if (tradeClient != null)
			tradeClient.sendAsciiMsg(netInfo.MyToString());

		if (logger != null)
			logger.info("TraderDataFeed SendMsg: " + netInfo.MyToString());

	}

	/**
	 * 资金查询
	 *
	 * @param userId
	 */
	public void sendAccountSearch(String userId) {
		NetInfo netInfo = new NetInfo();
		netInfo.code = CommandCode.ACCOUNTSEARCH;
		netInfo.accountNo = userId;

		if (tradeClient != null)
			tradeClient.sendAsciiMsg(netInfo.MyToString());

		if (logger != null)
			logger.info("TraderDataFeed SendMsg: " + netInfo.MyToString());
	}

	/**
	 * 持仓明细查询请求
	 *
	 * @param userId
	 */
	public void sendHoldDetailSearch(String userId) {

		OpenSearchInfo info = new OpenSearchInfo();
		info.accountNo = userId;

		NetInfo netInfo = new NetInfo();
		netInfo.code = CommandCode.OPENDETAIL;

		netInfo.accountNo = userId;
		netInfo.infoT = info.MyToString();

		if (tradeClient != null)
			tradeClient.sendAsciiMsg(netInfo.MyToString());

		if (logger != null)
			logger.info("TraderDataFeed SendMsg: " + netInfo.MyToString());
	}

	/**
	 * 检索持仓合计数据
	 *
	 * @param userId
	 */
	public void sendHoldTotalSearch(String userId) {
		OpenSearchInfo info = new OpenSearchInfo();
		info.accountNo = userId;

		NetInfo netInfo = new NetInfo();
		netInfo.code = CommandCode.SearchHoldTotal;

		netInfo.accountNo = userId;
		netInfo.infoT = info.MyToString();

		if (tradeClient != null)
			tradeClient.sendAsciiMsg(netInfo.MyToString());

		if (logger != null)
			logger.info("TraderDataFeed SendMsg: " + netInfo.MyToString());
	}

	/**
	 * 成交单查询
	 *
	 * @param userId
	 */
	public void sendFilledSearch(String userId) {
		FilledSearchInfo info = new FilledSearchInfo();
		info.userId = userId;

		NetInfo netInfo = new NetInfo();
		netInfo.code = CommandCode.FILLEDSEARCH;
		netInfo.infoT = info.MyToString();

		if (tradeClient != null)
			tradeClient.sendAsciiMsg(netInfo.MyToString());

		if (logger != null)
			logger.info("TraderDataFeed SendMsg: " + netInfo.MyToString());
	}

	/**
	 * 委托单查询 MaxLoadOrderDataQuantity 为空 则加载全部委托数据
	 *
	 * @param userId
	 * @param MaxLoadOrderDataQuantity
	 *            除已排队,部分成交和自定义策略单数据外的最新委托数据最大加载数量
	 */
	public void sendOrderSearch(String userId, String MaxLoadOrderDataQuantity) {
		OrderSearchInfo info = new OrderSearchInfo();
		info.userId = userId;

		NetInfo net = new NetInfo();
		net.code = CommandCode.ORDERSEARCH;
		net.accountNo = info.userId;
		net.infoT = info.MyToString();

		if (MaxLoadOrderDataQuantity != null) {
			net.todayCanUse = "L";
			net.systemCode = MaxLoadOrderDataQuantity;
		}

		if (tradeClient != null)
			tradeClient.sendAsciiMsg(net.MyToString());

		if (logger != null)
			logger.info("TraderDataFeed SendMsg: " + net.MyToString());
	}

	/**
	 * 币种汇率列表请求
	 *
	 * @deprecated
	 */
	public void sendRate() {
		NetInfo net = new NetInfo();
		net.code = CommandCode.CURRENCYLIST;

		if (tradeClient != null)
			tradeClient.sendAsciiMsg(net.MyToString());
	}

	/**
	 * 发送取得止损止盈设置列表数据的消息请求
	 *
	 * @param userId
	 */
	public void sendYingSunSetListRequestMessage(String userId) {
		// 数据的请求
		NetInfo netInfo = new NetInfo();
		netInfo.code = CommandCode.GETYINGSUNLIST;
		YingSunListRequestInfo info = new YingSunListRequestInfo();
		info.userId = userId;
		netInfo.infoT = info.MyToString();
		netInfo.accountNo = userId;
		netInfo.errorCode = "R";

		if (tradeClient != null)
			tradeClient.sendAsciiMsg(netInfo.MyToString());

		if (logger != null)
			logger.info("TraderDataFeed SendMsg: " + netInfo.MyToString());
	}

	/**
	 * 赢损设置
	 *
	 * @param userId
	 * @param info
	 */
	public void sendYingSunSetOrderRequestMessage(String userId, YingSunRequestInfo info) {
		NetInfo netInfo = new NetInfo();
		netInfo.code = CommandCode.SETYINGSUN;
		netInfo.accountNo = userId;
		netInfo.infoT = info.MyToString();

		if (tradeClient != null)
			tradeClient.sendAsciiMsg(netInfo.MyToString());

		if (logger != null)
			logger.info("TraderDataFeed SendMsg: " + netInfo.MyToString());
	}

	/**
	 * 赢损设置删除
	 *
	 * @param userId
	 * @param yingsunNo
	 */
	public void sendYingSunDelRequestMessage(String userId, String yingsunNo) {
		YingSunDelRequestInfo delinfo = new YingSunDelRequestInfo();
		delinfo.yingsunNo = yingsunNo;

		NetInfo netInfo = new NetInfo();
		netInfo.code = CommandCode.DELYINGSUN;
		netInfo.accountNo = userId;
		netInfo.infoT = delinfo.MyToString();

		if (tradeClient != null)
			tradeClient.sendAsciiMsg(netInfo.MyToString());

		if (logger != null)
			logger.info("TraderDataFeed SendMsg: " + netInfo.MyToString());
	}

	/**
	 * 修改用户密码
	 * <p>
	 *
	 * @param oldPws
	 *            用户旧密码
	 * @param newPws
	 *            用户新密码
	 *            <p>
	 */
	public void sendModifyPWDMessage(String oldPws, String newPws) {
		ModifyClientPWS pws = new ModifyClientPWS();
		pws.userCode = userAccount;
		pws.oldPws = oldPws;
		pws.newPws = newPws;

		NetInfo netInfo = new NetInfo();
		netInfo.code = CommandCode.MODIFYPW;// 操作代码
		netInfo.infoT = pws.MyToString();

		if (tradeClient != null)
			tradeClient.sendAsciiMsg(netInfo.MyToString());

		if (logger != null)
			logger.info("TraderDataFeed SendMsg: " + CommandCode.MODIFYPW);
	}

	// 安全认证添加-----20180306-----begin
	/**
	 * 双重认证登陆
	 * <p>
	 *
	 * @param userid
	 *            用户ID
	 * @param userpw
	 *            用户密码
	 * @param androidid
	 *            手机唯一识别码
	 */
	public void sendLogin(String userid, String userpw, String androidid) {

		try {
			if (logger != null)
				logger.info("sendLogin : " + androidid);

			LoginInfo loginInfo = new LoginInfo();
			loginInfo.userId = userid;
			loginInfo.userPwd = userpw;
			loginInfo.macAddress = androidid;
			loginInfo.computerName = "server";

			NetInfo netInfo = new NetInfo();
			netInfo.code = CommandCode.LOGIN;

			netInfo.todayCanUse = "C";
			netInfo.localSystemCode = "";
			netInfo.systemCode = "pc_hft";
			netInfo.infoT = loginInfo.MyToString();

			if (tradeClient != null)
				tradeClient.sendAsciiMsg(netInfo.MyToString());

			if (logger != null)
				logger.info("TraderDataFeed SendMsg: " + CommandCode.LOGIN);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取密保问题列表
	 */
	public void sendQuestionListSearch() {

		NetInfo netInfo = new NetInfo();
		netInfo.code = CommandCode.GetVerifyQuestionList;

		if (tradeClient != null)
			tradeClient.sendAsciiMsg(netInfo.MyToString());

		if (logger != null)
			logger.info("TraderDataFeed SendMsg:" + netInfo.MyToString());
	}

	/**
	 * 手机验证码发送
	 */
	public void sendReqVerifyCode() {

		VerifyInfo verifyInfo = new VerifyInfo();
		verifyInfo.UserId = userAccount;
		verifyInfo.UserPwd = userPassWd;
		verifyInfo.type = "2";
		verifyInfo.mobile = userMobile;

		NetInfo netInfo = new NetInfo();
		netInfo.code = CommandCode.ReqVerifyCode;
		netInfo.systemCode = MacUtils.getMac();
		netInfo.infoT = verifyInfo.MyToString();

		if (tradeClient != null)
			tradeClient.sendAsciiMsg(netInfo.MyToString());

		if (logger != null)
			logger.info("TraderDataFeed SendMsg:" + netInfo.MyToString());
	}

	/**
	 * 安全认证
	 *
	 * @param type
	 *            1：密保认证；2：手机验证码认证
	 * @param question
	 *            密保问题编号
	 * @param answer
	 *            密保问题答案
	 * @param mobile
	 *            手机号
	 * @param verifycode
	 *            手机验证码
	 * @param savemac
	 *            是否记住设备
	 */
	public void sendSafeVerify(String type, String question, String answer, String mobile, String verifycode,
			String savemac) {

		VerifyInfo verifyInfo = new VerifyInfo();
		verifyInfo.UserId = userAccount;
		verifyInfo.UserPwd = userPassWd;
		verifyInfo.type = type;
		verifyInfo.QA = question;
		verifyInfo.answer = answer;
		verifyInfo.mobile = mobile;
		verifyInfo.verifyCode = verifycode;
		verifyInfo.saveMac = savemac;

		NetInfo netInfo = new NetInfo();
		netInfo.code = CommandCode.SafeVerify;
		netInfo.systemCode = MacUtils.getMac();
		netInfo.infoT = verifyInfo.MyToString();

		if (tradeClient != null)
			tradeClient.sendUTF8Msg(netInfo.MyToString());

		if (logger != null)
			logger.info("TraderDataFeed SendMsg:" + netInfo.MyToString());
	}

	/**
	 * 设置密保问题答案
	 *
	 * @param password
	 *            交易密码
	 * @param question
	 *            密保问题编号
	 * @param answer
	 *            密保问题答案
	 */
	public void sendSetVerifyQA(String password, String question, String answer) {

		VerifyInfo verifyInfo = new VerifyInfo();
		verifyInfo.UserId = userAccount;
		verifyInfo.UserPwd = password;
		verifyInfo.type = "1";
		verifyInfo.QA = question;
		verifyInfo.answer = answer;

		NetInfo netInfo = new NetInfo();
		netInfo.code = CommandCode.SetVerifyQA;
		netInfo.systemCode = MacUtils.getMac();
		netInfo.infoT = verifyInfo.MyToString();

		if (tradeClient != null)
			tradeClient.sendUTF8Msg(netInfo.MyToString());

		if (logger != null)
			logger.info("TraderDataFeed SendMsg:" + netInfo.MyToString());
	}
	// 安全认证添加-----20180306-----end

	/**
	 * 保存下单数据
	 *
	 * @param info
	 * @param localSystemCode
	 */
	private void saveOrderInfo(OrderInfo info, String localSystemCode) {
		OrderResponseInfo orderResponseInfo = new OrderResponseInfo();

		orderResponseInfo.localNo = localSystemCode;
		orderResponseInfo.accountNo = info.accountNo;

		orderResponseInfo.userId = info.userId;
		orderResponseInfo.FIsRiskOrder = info.FIsRiskOrder;
		orderResponseInfo.exchangeCode = info.exchangeCode;
		orderResponseInfo.code = info.code;
		orderResponseInfo.addReduce = info.addReduce;
		orderResponseInfo.orderNumber = info.orderNumber;
		orderResponseInfo.orderPrice = info.orderPrice;
		orderResponseInfo.buySale = info.buySale;
		orderResponseInfo.tradeType = info.tradeType;
		orderResponseInfo.priceType = info.priceType;
		orderResponseInfo.htsType = info.htsType;
		orderResponseInfo.triggerPrice = info.triggerPrice;
		orderResponseInfo.validDate = info.validDate;
		orderResponseInfo.strategyId = info.strategyId;
		orderResponseInfo.orderState = Constants.TRADE_STATUS_YIQINGQIU;
		orderResponseInfo.orderTime = TimeUtil.dateTime2Str(DateTime.now(), TimeUtil.DSPdaytimeFormat);
		orderResponseInfo.orderDate = TimeUtil.dateTime2Str(DateTime.now(), TimeUtil.DSpdayFormat);

		xiadanInfoMap.put(localSystemCode, orderResponseInfo);
	}

	/**
	 * 比特币已确认指令
	 *
	 * @param userId
	 */
	public void sendBitcoinIsCheck(String code, String userId) {
		NetInfo netInfo = new NetInfo();
		netInfo.code = "BTBCONFM";
		netInfo.todayCanUse = code;
		netInfo.accountNo = userId;

		if (tradeClient != null)
			tradeClient.sendAsciiMsg(netInfo.MyToString());

		if (logger != null)
			logger.info("SendMsg: " + netInfo.MyToString());
	}

	/**
	 * 本地系统码
	 *
	 * @return
	 */
//	private String getLocalSystemCode() {
//
//		if (localSystemNo == -1) {
//			SimpleDateFormat da = new SimpleDateFormat("yyyyMMdd");
//			String time = da.format(new Date()) + "8800001";
//			localSystemNo = Long.parseLong(time);
//		} else {
//			localSystemNo++;
//		}
//
//		return "NM" + localSystemNo + "";
//
//	}

	/**
	 * 分割消息体
	 *
	 * @param infoT
	 * @return
	 */
	private String[] infoTSplit(String infoT) {
		return infoT.split("\\^", -1);
	}

	/**
	 * 登陆返回map
	 */
	public HashMap<String, LoginResponseInfo> getLoginInfoMap() {
		return loginInfoMap;
	}

	/**
	 * 持仓map
	 */
	public HashMap<String, OrderStatusInfo> getChicangInfoMap() {
		return chicangInfoMap;
	}

	/**
	 * 更新成交合计map
	 */
	private void updateChengjiaoTotalInfo(FilledResponseInfo info) {
		if (info != null) {
			int totalnumber = 0;
			double totalprice = 0;
			double totalcommsion = 0;

			int number = 0;
			double price = 0;
			double commsion = 0;

			if (chengjiaoTotalInfoMap.containsKey(info.exchangeCode + info.code + info.buySale)) {
				FilledResponseInfo totalinfo = chengjiaoTotalInfoMap.get(info.exchangeCode + info.code + info.buySale);
				totalnumber = StringUtils.isBlank(totalinfo.filledNumber) ? 0
						: Integer.parseInt(totalinfo.filledNumber);
				totalprice = StringUtils.isBlank(totalinfo.filledPrice) ? 0 : Double.parseDouble(totalinfo.filledPrice);
				totalcommsion = StringUtils.isBlank(totalinfo.commsion) ? 0 : Double.parseDouble(totalinfo.commsion);

				number = StringUtils.isBlank(info.filledNumber) ? 0 : Integer.parseInt(info.filledNumber);
				price = StringUtils.isBlank(info.filledPrice) ? 0 : Double.parseDouble(info.filledPrice);
				commsion = StringUtils.isBlank(info.commsion) ? 0 : Double.parseDouble(info.commsion);

				totalinfo.filledNumber = String.valueOf(totalnumber + number);
				totalinfo.filledPrice = String.valueOf(totalprice + (number * price));
				totalinfo.commsion = String.valueOf(totalcommsion + commsion);
			} else {
				totalnumber = StringUtils.isBlank(info.filledNumber) ? 0 : Integer.parseInt(info.filledNumber);
				totalprice = StringUtils.isBlank(info.filledPrice) ? 0 : Double.parseDouble(info.filledPrice);
				info.filledPrice = String.valueOf(totalprice * totalnumber);
				chengjiaoTotalInfoMap.put(info.exchangeCode + info.code + info.buySale, info);
			}
			
		}
	}

	/**
	 * 资金Map
	 */
	public HashMap<String, AccountResponseInfo> getZijinInfoMap() {
		return zijinInfoMap;
	}

	/**
	 * 基币账户数据
	 */
	public AccountResponseInfo getJibiInfo() {
		return jibiInfo;
	}

	public HashMap<String, YingSunListResponseInfo> getYingsunListMap() {
		return yingsunListMap;
	}

	// 安全认证添加-----20180306-----begin
	/**
	 * 获取用户手机号
	 */
	public String getUserMobile() {
		return userMobile;
	}

	/**
	 * 获取用户是否设置了密保问题答案
	 */
	public boolean getHasSetQA() {
		return hasSetQA;
	}

	/**
	 * 设置用户已经设置了密保问题答案
	 */
	public void setHasSetQA(boolean hassetqa) {
		hasSetQA = hassetqa;
	}

	/**
	 * 获取用户是否已经绑定过设备mac地址
	 */
	public boolean getExistMac() {
		return existMac;
	}

	/**
	 * 获取密保问题列表
	 */
	public ArrayList<QuestionInfo> getQuestionList() {
		return questionList;
	}
	// 安全认证添加-----20180306-----end

	public HashMap<String, FilledResponseInfo> getChengjiaoInfoMap() {
		return chengjiaoInfoMap;
	}

	public void setChengjiaoInfoMap(HashMap<String, FilledResponseInfo> chengjiaoInfoMap) {
		this.chengjiaoInfoMap = chengjiaoInfoMap;
	}

	public HashMap<String, FilledResponseInfo> getChengjiaoTotalInfoMap() {
		return chengjiaoTotalInfoMap;
	}

	public void setChengjiaoTotalInfoMap(HashMap<String, FilledResponseInfo> chengjiaoTotalInfoMap) {
		this.chengjiaoTotalInfoMap = chengjiaoTotalInfoMap;
	}

	public void setChicangInfoMap(HashMap<String, OrderStatusInfo> chicangInfoMap) {
		this.chicangInfoMap = chicangInfoMap;
	}

	public boolean isIspasswdOverdue() {
		return ispasswdOverdue;
	}

	public void setIspasswdOverdue(boolean ispasswdOverdue) {
		this.ispasswdOverdue = ispasswdOverdue;
	}

	public String getUserAccount() {
		return userAccount;
	}

	public void setUserAccount(String userAccount) {
		this.userAccount = userAccount;
	}

	public String getUserPassWd() {
		return userPassWd;
	}

	public void setUserPassWd(String userPassWd) {
		this.userPassWd = userPassWd;
	}
	
	
}

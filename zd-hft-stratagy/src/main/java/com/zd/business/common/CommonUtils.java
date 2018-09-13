package com.zd.business.common;

import java.math.BigDecimal;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

import com.shanghaizhida.beans.CancelInfo;
import com.shanghaizhida.beans.ModifyInfo;
import com.shanghaizhida.beans.NetInfo;
import com.shanghaizhida.beans.OrderInfo;
import com.shanghaizhida.beans.OrderResponseInfo;
import com.zd.business.constant.CommandEnum;
import com.zd.business.constant.MessageConst;

public class CommonUtils {

	/**
	 * 将数据转化成NetInfo协议传输格式的数据
	 * 
	 * @param command
	 * @return
	 */
	public static String toCommandString(String command) {
		StringBuffer sb = new StringBuffer("");
		if (StringUtils.isNoneBlank(command)) {
			sb.append("{(len=").append(command.length()).append(")").append(command).append("}");
			return sb.toString();
		}
		return command;
	}

	/**
	 * 格式化系统异常数据
	 * 
	 * @param msg
	 * @return
	 */
	public static String formatMsg(String clientNo, CommandEnum command, MessageConst msg) {
		if (msg != null) {
			NetInfo netInfo = new NetInfo();
			netInfo.code = command.toString();
			netInfo.clientNo = clientNo;
			netInfo.errorCode = msg.getCode();
			netInfo.errorMsg = msg.getMsg();
			return netInfo.MyToString();
		}
		return "";
	}

	/**
	 * 获取小数位数
	 * 
	 * @return
	 */
	public static int getDecimalsUnits(BigDecimal value) {
		int i = 0;
		try {
			String s = value.toString();
			if (s.indexOf(".") != -1) {
				i = s.substring(s.indexOf("."), s.length() - 1).length();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return i;
	}

	public static String uuid() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}

	/**
	 * ZD下单/平仓
	 * 
	 * @param userId
	 * @param userPwd
	 * @param fIsRiskOrder
	 * @param userType
	 * @param exchangeCode
	 * @param code
	 * @param buySale
	 * @param orderNumber
	 * @param orderPrice
	 * @param priceType
	 * @param addReduce
	 * @param accountNo
	 * @return
	 */
	public static OrderInfo generateOrder(String userId, String userPwd, String fIsRiskOrder, String userType,
			String exchangeCode, String code, String buySale, String orderNumber, String orderPrice, String priceType,
			String addReduce, String accountNo) {
		OrderInfo orderInfo = new OrderInfo();
		orderInfo.userId = userId;
		orderInfo.tradePwd = userPwd;
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
		orderInfo.accountNo = accountNo;
		return orderInfo;
	}

	/**
	 * ZD撤单
	 * 
	 * @param mOrderResponseInfo
	 * @return
	 */
	public static CancelInfo generateCancelInfo(String userId,OrderResponseInfo mOrderResponseInfo) {
		CancelInfo cancelInfo = new CancelInfo();
		// 用户ID (selectedInfo.userId中保存的userid是前置返回的上手号,要用登陆id,或者是登陆返回list中的id)
		cancelInfo.userId = userId;
		/// 用户类型：1：一般用户；2：机构通用户；
		// cancelInfo.userType ="";
		/// 资金账号
		cancelInfo.accountNo = mOrderResponseInfo.accountNo;
		/// 交易密码
		// cancelInfo.tradePwd =orderResponseInfo.;
		/// 是否模拟用户：1：是；0 or other：不是
		// cancelInfo.isSimulation ="";
		/// 系统编号
		cancelInfo.systemNo = mOrderResponseInfo.systemNo;
		/// 定单号
		cancelInfo.orderNo = mOrderResponseInfo.orderNo;
		/// 交易所代码
		cancelInfo.exchangeCode = mOrderResponseInfo.exchangeCode;
		/// 合约代码
		cancelInfo.code = mOrderResponseInfo.code;
		/// 买还是卖：1=buy 2=sell
		cancelInfo.buySale = mOrderResponseInfo.buySale;
		/// 下单数
		cancelInfo.orderNumber = mOrderResponseInfo.orderNumber;
		/// 下单价格
		cancelInfo.orderPrice = mOrderResponseInfo.orderPrice;
		/// 已成交数
		cancelInfo.filledNumber = mOrderResponseInfo.filledNumber;
		/// 交易方式：1=regular 2=FOK 3=IOC
		cancelInfo.tradeType = mOrderResponseInfo.tradeType;
		/// 价格类型：1=limit order, 2=market order
		cancelInfo.priceType = mOrderResponseInfo.priceType;
		/// 0=regular 1=HTS
		cancelInfo.htsType = mOrderResponseInfo.htsType;
		/// 用户下单类型：C或是空客户下单；D：是del下单 R：强平下单（风控）
		cancelInfo.FIsRiskOrder = mOrderResponseInfo.FIsRiskOrder;
		return cancelInfo;
	}

	/**
	 * ZD改单
	 * 
	 * @param mOrderResponseInfo
	 * @param modifyNums
	 * @param modifyPrice
	 * @param priceType
	 * @return
	 */
	public static ModifyInfo generateModify(String userId, OrderResponseInfo mOrderResponseInfo, String modifyNums,
			String modifyPrice, String priceType) {
		ModifyInfo modifyInfo = new ModifyInfo();
		// 用户ID
		modifyInfo.userId = userId;

		// 用户类型：1：一般用户；2：机构通用户；
		// modifyInfo.userType = "";

		// 资金账号
		modifyInfo.accountNo = mOrderResponseInfo.accountNo;

		// 交易密码
		// modifyInfo.tradePwd = "";

		// 定单号
		modifyInfo.orderNo = mOrderResponseInfo.orderNo;

		// 交易所代码
		modifyInfo.exchangeCode = mOrderResponseInfo.exchangeCode;

		// 合约代码
		modifyInfo.code = mOrderResponseInfo.code;

		// 买还是卖：1=buy 2=sell
		modifyInfo.buySale = mOrderResponseInfo.buySale;

		// 下单数
		modifyInfo.orderNumber = mOrderResponseInfo.orderNumber;

		// 下单价格
		modifyInfo.orderPrice = mOrderResponseInfo.orderPrice;

		// 已成交数
		modifyInfo.filledNumber = mOrderResponseInfo.filledNumber;

		// 改单数
		modifyInfo.modifyNumber = modifyNums;

		// 改单价格
		modifyInfo.modifyPrice = modifyPrice;

		// 交易方式：1=regular 2=FOK 3=IOC
		modifyInfo.tradeType = "";

		// 价格类型：1=限价单, 2=市价单，3=限价止损（stop to limit），4=止损（stop to market）
		modifyInfo.priceType = priceType;

		// 用户下单类型：C或是空客户下单；D：是del下单 R：强平下单（风控）
		modifyInfo.FIsRiskOrder = "";

		// add by dragon 20121203 for 改单

		// 触发价格
		modifyInfo.triggerPrice = "";

		// 改单触发价格
		modifyInfo.modifyTriggerPrice = "";
		// 有效日期（1：当日有效；2：永久有效）
		modifyInfo.validDate = "2";
		return modifyInfo;
	}

}

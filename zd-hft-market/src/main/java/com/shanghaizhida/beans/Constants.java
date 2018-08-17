package com.shanghaizhida.beans;

/** 系统常量类 */
public class Constants {

	/** 标志有效 */
	public static String FLAG_ON = "1";

	/** 标志无效 */
	public static String FLAG_OFF = "0";

	/** 交易买卖的买:1 */
	public static String TRADE_BUYSALE_BUY = "买";

	/** 交易买卖的卖:2 */
	public static String TRADE_BUYSALE_SALE = "卖";

	/** 交易买卖的市价交易（正好是卖价交易就是卖，正好是买价交易就是买）:0 */
	public static String TRADE_BUYSALE_MARKET = "市价交易";

	/** 定单类型的限价 */
	public static String TRADE_ORDERTYPE_LIMIT = "限价";

	/** 定单类型的市价 */
	public static String TRADE_ORDERTYPE_MARKET = "市价";

	/** 交易类型的电子单 */
	public static String TRADE_TRADETYPE_MARKET = "电子单";

	/** 成交类型的正常交易 */
	public static String TRADE_FILLTYPE_REGULAR = "正常交易";

	/** 成交类型的完全或取消：要么全部成交，要么全部取消 */
	public static String TRADE_BUYSALE_FOK = "完全或取消";

	/** 成交类型的部分后取消：成交一部分后剩下的就全部取消 */
	public static String TRADE_BUYSALE_IOC = "部分后取消";

	/** 交易开平的开仓 */
	public static String TRADE_KAIPING_KAICANG = "开仓";

	/** 交易开平的平仓 */
	public static String TRADE_KAIPING_PINGCANG = "平仓";

	/** 交易开平的平今 */
	public static String TRADE_KAIPING_PINGJIN = "平今";

	/** 交易状态的已请求 */
	public static String TRADE_STATUS_YIQINGQIU = "已请求";

	/** 交易状态的待送出 */
	public static String TRADE_STATUS_DAISONGCHU = "待送出";

	/** 交易状态的待更改 */
	public static String TRADE_STATUS_DAIGENGGAI = "待更改";

	/** 交易状态的待撤单 */
	public static String TRADE_STATUS_DAICHEDAN = "待撤单";

	/** 交易状态的已排队 */
	public static String TRADE_STATUS_YIPAIDUI = "已排队";

	/** 交易状态的部分成交 */
	public static String TRADE_STATUS_BUFEN = "部分成交";

	/** 交易状态的完全成交 */
	public static String TRADE_STATUS_WANQUAN = "完全成交";

	/** 交易状态的已撤单 */
	public static String TRADE_STATUS_YICHEDAN = "已撤单";

	/** 交易状态的已撤余单 */
	public static String TRADE_STATUS_YICHEYUDAN = "已撤余单";

	/** 交易状态的埋单 */
	public static String TRADE_STATUS_MAIDAN = "埋单";

	/** 交易状态的埋单删除 */
	public static String TRADE_STATUS_MAIDAN_DEL = "埋单删除";

	/** 交易状态的自动单 */
	public static String TRADE_STATUS_ZIDONGDAN = "自动单";

	/** 交易状态的自动单删除 */
	public static String TRADE_STATUS_ZIDONGDAN_DEL = "自动单删除";

	/** 交易状态的条件单 */
	public static String TRADE_STATUS_TIAOJIANDAN = "条件单";

	/** 交易状态的条件单删除 */
	public static String TRADE_STATUS_TIAOJIANDAN_DEL = "条件单删除";

	/** 交易状态的资金不足 */
	public static String TRADE_STATUS_ZIJIN_LESS = "资金不足";

	/** 交易状态的指令失败 */
	public static String TRADE_STATUS_ZHILING_FAIL = "指令失败";

	/** 交易投保的投机 */
	public static String TRADE_TOUBAO_TOUJI = "投机";

	/** 交易投保的保值 */
	public static String TRADE_TOUBAO_BAOZHI = "保值";

	/** 交易平仓下单时自动撤单方式的逐笔撤单 */
	public static String TRADE_PINGCANG_CANCELMETHOD_ZHUBI = "逐笔撤单";

	/** 交易平仓下单时自动撤单方式的平满撤单 */
	public static String TRADE_PINGCANG_CANCELMETHOD_PINGMAN = "平满撤单";

	/** 交易组合类型的单腿合约 */
	public static String TRADE_ZUHETYPE_DANTUI_HEYUE = "单腿合约";

	/** 参数设置的快速平仓方式的分笔 */
	public static String TRADE_QUICK_PINGCANG_METHOD_FENBI = "分笔";

	/** 参数设置的快速平仓方式的全部 */
	public static String TRADE_QUICK_PINGCANG_METHOD_ALL = "全部";

	/** 参数设置的委托界面默认价格的最新价 */
	public static String TRADE_DEFAULT_PRICE_ZUIXIN = "最新价";

	/** 参数设置的委托界面默认价格的对盘价 */
	public static String TRADE_DEFAULT_PRICE_DUIPAN = "对盘价";

	/** 参数设置的委托界面默认价格的不默认 */
	public static String TRADE_DEFAULT_PRICE_NO = "不默认";

	/** 参数设置的委托界面默认价格的挂单价 */
	public static String TRADE_DEFAULT_PRICE_GUADAN = "挂单价";

	/** 交易的反馈信息的报单成功 */
	public static String TRADE_RESPONSE_SUCCESS = "报单成功";

	/** 交易的反馈信息的报单失败，客户持仓不足平 */
	public static String TRADE_RESPONSE_FAIL_CHICANG_LESS = "报单失败，客户持仓不足平";

	/** 交易的反馈信息的资金不足于开新仓 */
	public static String TRADE_RESPONSE_FAIL_MONEY_LESS = "资金不足于开新仓";

	/** 交易的反馈信息的撤单成功 */
	public static String TRADE_RESPONSE_SUCCESS_CANCELED = "撤单成功";

	/** 参数设置的持仓量显示的单笔持仓 */
	public static String TRADE_CHICANG_DANBI = "单笔持仓";

	/** 参数设置的持仓量显示的全部持仓 */
	public static String TRADE_CHICANG_QUANBU = "全部持仓";

	/** 参数设置的持仓量显示的不显持仓 */
	public static String TRADE_CHICANG_NO = "不显持仓";

	/** 参数设置的连接状态报警方式的声音报警 */
	public static String TRADE_NETSTATUS_ALERT_VOICE = "声音报警";

	/** 参数设置的连接状态报警方式的窗口报警 */
	public static String TRADE_NETSTATUS_ALERT_WINDOW = "窗口报警";

	/** 参数设置的连接状态报警方式的无需报警 */
	public static String TRADE_NETSTATUS_ALERT_NO = "无需报警";

	/** 参数设置的连接状态的正常 */
	public static String TRADE_NETSTATUS_OK = "正常";

	/** 参数设置的连接状态的断开 */
	public static String TRADE_NETSTATUS_FAIL = "断开";

	/** 合约类型的期货 */
	public static String TRADE_HEYUE_TYPE_QIHUO = "期货";

	/** 合约类型的期权 */
	public static String TRADE_HEYUE_TYPE_QIQUAN = "期权";

	/** 持仓类型的今仓 */
	public static String TRADE_CHICANG_TYPE_TODAY = "今仓";

	/** 持仓类型的昨仓 */
	public static String TRADE_CHICANG_TYPE_YESTODAY = "昨仓";

	/** 止损止盈触发的下单本地号起始字符 */
	public static String YINGSUN_ORDER_LOCALNO_START = "YS";

	/*----------------------------MessageServer Part----------------------------*/
	/** 消息服务器请求类型 */
	public static String MESSAGEREQ = "REQ-C";

	/**
	 * 订单状态（1：已请求；2：已排队；3：部分成交；4：完全成交；5：已撤余单；6：已撤单；7：指令失败）
	 * 
	 * @param numState
	 * @return
	 */
	public static String tradeStatusByNum(String numState) {

		if (numState.isEmpty())
			return numState;

/*
		int state = Integer.parseInt(numState);

		switch (state) {
		case 1:
			return TRADE_STATUS_YIQINGQIU;

		case 2:
			return TRADE_STATUS_YIPAIDUI;

		case 3:
			return TRADE_STATUS_BUFEN;

		case 4:
			return TRADE_STATUS_WANQUAN;

		case 5:
			return TRADE_STATUS_YICHEYUDAN;

		case 6:
			return TRADE_STATUS_YICHEDAN;

		case 7:
			return TRADE_STATUS_ZHILING_FAIL;

		case 8:
			return TRADE_STATUS_DAISONGCHU;

		case 9:
			return TRADE_STATUS_DAIGENGGAI;

		case A:
			return TRADE_STATUS_DAICHEDAN;

		default:
			return numState;
		}
*/
		if ("1".equals(numState)) {
			return TRADE_STATUS_YIQINGQIU;
		} else if ("2".equals(numState)) {
			return TRADE_STATUS_YIPAIDUI;
		} else if ("3".equals(numState)) {
			return TRADE_STATUS_BUFEN;
		} else if ("4".equals(numState)) {
			return TRADE_STATUS_WANQUAN;
		} else if ("5".equals(numState)) {
			return TRADE_STATUS_YICHEYUDAN;
		} else if ("6".equals(numState)) {
			return TRADE_STATUS_YICHEDAN;
		} else if ("7".equals(numState)) {
			return TRADE_STATUS_ZHILING_FAIL;
		} else if ("8".equals(numState)) {
			return TRADE_STATUS_DAISONGCHU;
		} else if ("9".equals(numState)) {
			return TRADE_STATUS_DAIGENGGAI;
		} else if ("A".equals(numState)) {
			return TRADE_STATUS_DAICHEDAN;
		} else {
			return numState;
		}
	}

	/**
	 * 根据买卖名字取得买卖代码
	 * 
	 * @param name
	 *            买卖名字（市价，买，卖）
	 * @return 0：市价；1：买；2：卖
	 */
	public static String getBuySaleByName(String name) {
		if ((name).equals(Constants.TRADE_BUYSALE_BUY)) {
			return "1";
		} else if ((name).equals(Constants.TRADE_BUYSALE_SALE)) {
			return "2";
		} else if ((name).equals(Constants.TRADE_BUYSALE_MARKET)) {
			return "0";
		} else// 默认是买
		{
			return "1";
		}
	}

	/**
	 * 根据买卖名字取得买卖代码
	 * 
	 * @param name
	 *            买卖名字（市价，买，卖）
	 * @return 0：市价；1：买；2：卖
	 */
	public static String getOrderTypeByCode(String code) {
		if ((code).equals("1")) {
			return Constants.TRADE_ORDERTYPE_LIMIT;
		} else if ((code).equals("2")) {
			return Constants.TRADE_ORDERTYPE_MARKET;
		}
		// 默认是限价
		return Constants.TRADE_ORDERTYPE_LIMIT;
	}

	/**
	 * 根据止损止盈设置状态代码取得名称
	 * 
	 * @param code
	 *            状态码
	 * @return 状态码对应的名称
	 */
	public static String getYingSunSetStatusNameByCode(String code) {
		try {
			if (code == null)
				return "";
			if (code.length() == 0)
				return "";
			if ("".equals(code))
				return "";

			if ("0".endsWith(code)) {
				return "已请求";
			} else if ("1".endsWith(code)) {
				return "未触发";
			} else if ("2".endsWith(code)) {
				return "已触发";
			} else if ("3".endsWith(code)) {
				return "已撤销";
			} else if ("4".endsWith(code)) {
				return "已部分成交";
			} else if ("5".endsWith(code)) {
				return "已完全成交";
			} else if ("6".endsWith(code)) {
				return "已清除";
			} else if ("7".endsWith(code)) {
				return "已失效";
			} else if ("8".endsWith(code)) {
				return "平仓指令成功";
			} else if ("9".endsWith(code)) {
				return "平仓指令失败";
			} else {
				return "";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return code;
	}

	/**
	 * 根据用户类型取得登录指令
	 * <p>
	 * 
	 * @param userType
	 * @return <p>
	 *         string
	 */
	public static String getLoginCodeByUserType(String userType) {
		try {
			if (userType == "0" || userType == "") {
				return CommandCode.LOGIN;
			} else {
				return CommandCode.LOGINHK;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
}
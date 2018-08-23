package com.zd.entity;

public class CommandCode {

	/**
	 * 根据指令代码取得指令名称
	 * 
	 * @param commandCode
	 * @return
	 */

	public static String getCommandNameByCode(String commandCode) {
		String name = "";

		if (commandCode == CommandCode.ACCOUNTLAST) {
			name = "更新资金";
		} else if (commandCode == CommandCode.ACCOUNTSEARCH) {
			name = "加载资金";
		} else if (commandCode == CommandCode.CANCEL) {
			name = "撤单";
		} else if (commandCode == CommandCode.CANCELCAST) {
			name = "撤单";
		} else if (commandCode == CommandCode.CHANGEDATE) {
			name = "调期";
		} else if (commandCode == CommandCode.CHANGELIST) {
			name = "加载调期";
		} else if (commandCode == CommandCode.ConditionDel) {
			name = "条件单删除";
		} else if (commandCode == CommandCode.ConditionSet) {
			name = "条件单设置";
		} else if (commandCode == CommandCode.CURRENCYLIST) {
			name = "加载币种";
		} else if (commandCode == CommandCode.DELYINGSUN) {
			name = "删除止损止盈设置";
		} else if (commandCode == CommandCode.EnFrozenLogin) {
			name = "冻结账户";
		} else if (commandCode == CommandCode.FILLEDCAST) {
			name = "定单成交";
		} else if (commandCode == CommandCode.FILLEDSEARCH) {
			name = "加载成交";
		} else if (commandCode == CommandCode.FROZENUSER) {
			name = "冻结账户";
		} else if (commandCode == CommandCode.FROZENUSERMONEY) {
			name = "冻结资金";
		} else if (commandCode == CommandCode.GetConditionList) {
			name = "加载条件单";
		} else if (commandCode == CommandCode.GETEXCHANGE) {
			name = "加载交易所";
		} else if (commandCode == CommandCode.GetMoneyApplyList) {
			name = "加载出入金申请";
		} else if (commandCode == CommandCode.GETSTRATEGY) {
			name = "加载策略";
		} else if (commandCode == CommandCode.GETSTRATEGYDETAIL) {
			name = "加载策略明细";
		} else if (commandCode == CommandCode.GETTCONTRACT) {
			name = "加载合约";
		} else if (commandCode == CommandCode.GETVERSION) {
			name = "加载版本号";
		} else if (commandCode == CommandCode.GETYINGSUNLIST) {
			name = "加载止损止盈设置";
		} else if (commandCode == CommandCode.LOGIN) {
			name = "登录";
		} else if (commandCode == CommandCode.LOGINMULTI) {
			name = "多处登录";
		} else if (commandCode == CommandCode.LOGINRID) {
			name = "多处登录";
		} else if (commandCode == CommandCode.MARKET01) {
			name = "请求行情";
		} else if (commandCode == CommandCode.MARKET02) {
			name = "请求二级行情";
		} else if (commandCode == CommandCode.MODIFY) {
			name = "改单";
		} else if (commandCode == CommandCode.MODIFYPW) {
			name = "修改密码";
		} else if (commandCode == CommandCode.MoneyApply) {
			name = "出入金申请";
		} else if (commandCode == CommandCode.OPENDETAIL) {
			name = "加载持仓";
		} else if (commandCode == CommandCode.ORDER) {
			name = "下单";
		} else if (commandCode == CommandCode.ORDER002) {
			name = "强平单";
		} else if (commandCode == CommandCode.ORDER003) {
			name = "改单";
		} else if (commandCode == CommandCode.ORDER004) {
			name = "下单";
		} else if (commandCode == CommandCode.ORDERSEARCH) {
			name = "加载定单";
		} else if (commandCode == CommandCode.SETYINGSUN) {
			name = "止损止盈设置";
		} else if (commandCode == CommandCode.UnFrozenLogin) {
			name = "解冻账户";
		} else if (commandCode == CommandCode.UNFROZENUSER) {
			name = "解冻账户";
		} else if (commandCode == CommandCode.UNFROZENUSERMONEY) {
			name = "解冻资金";
		} else if (commandCode == CommandCode.UNLOGIN) {
			name = "退出";
		} else if (commandCode == CommandCode.GetTradeTime) {
			name = "取得交易时间";
		} else if (commandCode == CommandCode.GetSettlePrice) {
			name = "取得昨结算价";
		} else if (commandCode == CommandCode.SearchGuaDan) {
			name = "加载挂单数据";
		} else if (commandCode == CommandCode.SearchHoldTotal) {
			name = "加载持仓合计数据";
		}
		return name;
	}

	/** 测试保持连接 */
	public static final String HEARTBIT = "TEST0001";

	/** 用户登录请求代码 */
	public static final String LOGIN = "LOGIN001";

	/** 股票用户登录请求代码 */
	public static String LOGINHK = "LOGINHK1";

	/** 用户退出请求代码 */
	public static final String UNLOGIN = "UNLOGIN1";

	/** 用户登录成功信息保存的指令代码 */
	public static final String LOGINSAVE = "LOGINSV1";

	/** 用户多处登录踢掉前一登陆的代码 */
	public static final String LOGINRID = "LOGIN002";

	/** 提示用户前面已经有人在别处登陆过的代码 */
	public static final String LOGINMULTI = "LOGIN003";

	/** 定单请求代码,及返回信息代码 */
	public static final String ORDER = "ORDER001";

	// public static final String ORDERSUCCESS = "ORDER002";
	// public static final String ORDERFAILED = "ORDER003";

	/** 强平下单请求代码 */
	public static final String ORDER002 = "ORDER002";

	/** 柜台改单请求代码 */
	public static final String ORDER003 = "ORDER003";

	/** 盈损单下单请求代码 */
	public static final String ORDER004 = "ORDER004";

	/** 定单状态请求代码 */
	public static final String ORDERSTATUS = "ORDERSTA";

	/** 定单查询请求代码 */
	public static final String ORDERSEARCH = "ORDERLST";

	/** 客户端改单请求代码,及返回信息代码 */
	public static final String MODIFY = "MODIFY01";

	/** 调期请求代码,及返回信息代码 */
	public static final String CHANGEDATE = "CHANGEDT";

	/** 调期明细列表请求代码,及返回信息代码 */
	public static final String CHANGELIST = "CHANGELS";

	/** 撤单请求代码 */
	public static final String CANCEL = "CANCEL01";

	/** 撤单广播返回信息代码 */
	public static final String CANCELCAST = "CANCST01";

	/** 成交广播返回信息代码 */
	public static final String FILLEDCAST = "FILCST01";

	/** 成交查询请求代码 */
	public static final String FILLEDSEARCH = "FILLLIST";

	/** 持仓查询请求代码 */
	public static final String OPENSEARCH = "OPENLIST";

	/** 持仓明细查询请求代码（包括昨仓和今仓的分笔持仓明细） */
	public static final String OPENDETAIL = "OPENDETL";

	/** 昨日持仓请求代码 */
	public static final String YHOLDSEARCH = "YHLDLIST";

	/** 持仓状态请求代码 */
	public static final String HOLDSTATUS = "HOLDSTAT";

	/** 账户资金查询请求代码 */
	public static final String ACCOUNTSEARCH = "DEPOSTAT";
	public static final String ACCOUNTSEARCHFORCHECK = "ACNTSRCH";
	public static final String ACCOUNTLOAD = "ACNTLOAD";
	public static final String ACCOUNTLAST = "ACNTLAST";

	/** 用户信息取得请求代码 */
	public static final String USERACCOUNTINFOLIST = "USERLIST";

	/** 系统编号取得请求代码 */
	public static final String SYSTEMNOGET = "SYSTEMNO";

	/** 用户请求所有可用的合约 */
	public static final String GETTCONTRACT = "CONTRACT";

	/** 用户请求所有可用的交易所信息 */
	public static final String GETEXCHANGE = "EXCHANGE";

	/** 请求读用户缺省的手续费、保证金 */
	public static final String GETDEFEEDEPO = "DEFEEDEP";

	/** 请求读用户交易手续费 */
	public static final String GETTDFEE = "TRADEFEE";

	/** 请求用户交易保证金 */
	public static final String GETTDDEPO = "TRADEDEP";

	/** 请求客户上手信息 */
	public static final String CLIENTUPPER = "CLNTUPPR";

	/** 请求客户上手信息 */
	public static final String TESTINFO = "TESTINFO";

	/** 请求客户的最大系统号 */
	public static final String GETSYSNO = "GETSYSNO";

	/** 请求行情 */
	public static final String MARKET01 = "MARKET01";
	
	/** 请求二级行情 */
	public static final String MARKET02 = "MARKET02";

	/** 修改用户密码 */
	public static final String MODIFYPW = "MODIFYPW";

	/** 成交情况 */
	public static final String FILLEDINFO = "FILLINFO";

	/** 排队情况 */
	public static final String WAITINFO = "WAITINFO";

	/** 上日结算价 */
	public static final String SETTLEPRICE = "STLPRICE";

	/** 出入金 */
	public static final String TCASH = "TCASH001";

	/** 冻结账户 */
	public static final String EnFrozenLogin = "FroLogin";

	/** 解冻账户 */
	public static final String UnFrozenLogin = "FreLogin";

	/** del下单 */
	public static final String delOrder = "DelOrder";

	/** 柜台生成新的密码 */
	public static final String GuitaiPws = "GuiTmpws";

	/** 币种信息列表请求代码 */
	public static final String CURRENCYLIST = "CRNYLIST";

	/** 柜台冻结客户不让继续交易 */
	public static final String FROZENUSER = "FROZUSER";

	/** 柜台解冻客户 */
	public static final String UNFROZENUSER = "UNFRUSER";

	/** 柜台冻结客户部分资金 */
	public static final String FROZENUSERMONEY = "FRUSERMN";

	/** 柜台解冻客户被冻结的资金 */
	public static final String UNFROZENUSERMONEY = "UNFRUSMN";

	/** 请求当天的LME合约的交割日 */
	public static final String GETDELIVERYDATE = "DELIVERY";

	/** 更新定单状态的请求 */
	public static final String UPDATESTATUS = "UPSTATUS";

	/** 请求生成成交编号 */
	public static final String GETFILLEDNO = "FILLEDNO";

	/** 止损止盈设置请求 */
	public static final String SETYINGSUN = "YSSET001";

	/** 止损止盈设置删除请求 */
	public static final String DELYINGSUN = "YSDEL001";

	/** 止损止盈设置列表数据取得请求 */
	public static final String GETYINGSUNLIST = "YSLIST01";

	/** 前置登录止损止盈服务器的请求，将前置的ID放到NetInfo的accountNo中发送 */
	public static final String LOGINYINGSUN = "LOGINYSQ";

	/** 发送消息 */
	public static final String SENDMSG = "SNDMSG01";

	/** 取得最新版本号 */
	public static final String GETVERSION = "GETVER01";

	/** 取得现有策略列表 */
	public static final String GETSTRATEGY = "GETSTRLS";

	/** 取得现有策略明细列表 */
	public static final String GETSTRATEGYDETAIL = "GETSTRDL";

	/** 请求客户日交易结算报表 */
	public static final String ReportDaily = "REPORTDA";

	/** 请求客户月交易结算报表 */
	public static final String ReportMonthly = "REPORTMO";

	/** 请求客户出入金申请数据列表 */
	public static final String GetMoneyApplyList = "APPMLIST";

	/** 客户出入金申请 */
	public static final String MoneyApply = "MONEYAPP";

	/** 请求客户条件单设置数据列表 */
	public static final String GetConditionList = "TJLIST01";

	/** 客户条件单设置请求（有条件单编号是修改，没有是新增） */
	public static final String ConditionSet = "TJSETREQ";

	/** 客户条件单设置删除 */
	public static final String ConditionDel = "TJDELREQ";

	/** 更新策略持仓 */
	public static final String UpdateStrategyHold = "UPDATESTRATEGYHOLD";

	/** 检索策略持仓 */
	public static final String SearchStrategyHold = "SEARCHSTRATEGYHOLD";

	/** 取得当前交易日的开盘和收盘时间 */
	public static final String GetTradeTime = "GETTTIME";

	/** 取得最新的昨结算价 */
	public static final String GetSettlePrice = "GETPRICE";

	/** 检索挂单数据 */
	public static final String SearchGuaDan = "SEARCHGD";

	/** 检索持仓合计数据 */
	public static final String SearchHoldTotal = "SEARCHHT";

	/** 取得股票交易所列表数据 */
	public static String GetExchangeStock = "ExchStLs";

	/** 取得股票合约列表数据 */
	public static String GetContractStock = "ContStLs";

	/** 取得股票委托列表数据 */
	public static String GetOrderListStock = "OrdeStLs";

	/** 取得股票成交列表数据 */
	public static String GetFilledListStock = "FillStLs";

	/** 取得股票持仓列表数据 */
	public static String GetHoldListStock = "HoldStLs";

	/** 取得股票资金列表数据 */
	public static String GetAccountListStock = "AccoStLs";

	/** 取得股票合约最小变动单位列表数据 */
	public static String GetContractStockUpperTick = "UptiStLs";

	/** 港股下单及返回指令 */
	public static String OrderStockHK = "OrdeStHK";

	/** 港股系统号返回指令 */
	public static String SystemNoStockHK = "SystStHK";

	/** 港股改单及返回指令 */
	public static String ModifyStockHK = "ModiStHK";

	/** 港股撤单及返回指令 */
	public static String CancelStockHK = "CancStHK";

	/** 港股成交返回指令 */
	public static String FilledStockHK = "FillStHK";

	/** 港股最新资金返回指令 */
	public static String AccountStockHK = "AccoStHK";

	/** 港股最新订单状态返回指令 */
	public static String StatusStockHK = "StatStHK";

	/** 港股最新持仓返回指令 */
	public static String HoldStockHK = "HoldStHK";

	/** 更新客户端版本指令 */
	public static String UpdateClient = "UpdtClet";

	/** 股票期货之间资金调拨指令 */
	public static String TransferMoney = "TranMony";

	/** 登陆IP信息返回 */
	public static String GetLoginHistoryList = "LogHisLs";

	/** 查询结算单是否确认 */
	public static String QuerySettleInfoConfirm = "QUERYSETTLEINFOCONFIRM";

	/** 结算单查询 */
	public static String SettleInfo = "SETTLEINFO";

	/** 结算单确认 */
	public static String SettleInfoConfirm = "SETTLEINFOCONFIRM";

	/** 国内期货下单返回 */
	public static final String ORDERINFO = "ORDERINFO";

	/** 国内期货订单错误返回 */
	public static final String ORDERACTIONERROR = "ORDERACTIONERROR";

	/** 国内期货成交返回 */
	public static String ChinaFuturesFilledResponse = "RETURNTRADE";

	/** 国内期货登录错误返回 */
	public static String CFLOGINERROR = "ERROR";

	// 用户资产分析用到的-----begin
	/** 资产信息请求 */
	public static String LOGINEA = "LOGINEA";

	/** 资金分析返回 */
	public static String UPDATESETTLEINFO = "UPDATESETTLEINFO";

	/** 品种盈亏返回 */
	public static String UPDATESETHisProfit = "UPDATESETHisProfit";

	/** 资金错误返回 */
	public static String LINKTIMEOUT = "LinkTimeOut";
	// 用户资产分析用到的-----end

	// 双重认证-----begin
	/** 认证问题列表数据请求 */
	public static String GetVerifyQuestionList = "VfQuList";

	/** 安全认证请求 */
	public static String SafeVerify = "SfVerify";

	/** 密保问题答案设置请求 */
	public static String SetVerifyQA = "SetVeriQA";

	/** 手机验证码发送请求 */
	public static String ReqVerifyCode = "RequVfCd";
	// 双重认证-----end
}

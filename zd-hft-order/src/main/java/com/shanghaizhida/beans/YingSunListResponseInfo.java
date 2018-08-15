package com.shanghaizhida.beans;

/**
 * 止损止盈设置列表数据请求返回信息的类
 * 
 * @author xiang <br>
 *         2015年12月2日 上午11:33:11
 * 
 */
public class YingSunListResponseInfo implements NetParent {

	/** 止损止盈编号 */
	public String yingsunNo = "";

	/** 设置人ID */
	public String userId = "";

	/**
	 * 盈损状态：0：已请求；1：未触发；2：已触发(已废除不用,改用8,9表示是否触发下单成功)；3：已撤销；4：已部分成交；
	 * 5：已完全成交；6：已清除；7：已失效；8：平仓指令成功；9：平仓指令失败
	 */
	public String status = "";

	/** 盈损本地号：用于止损止盈编号生成前找到这条数据 */
	public String localNo = "";

	/** 交易所代码 */
	public String exchangeNo = "";

	/** 合约代码 */
	public String contractNo = "";

	/**
	 * 合约中文名称 <br>
	 * add by xiang 2015-12-17
	 */
	public String contractName = "";

	/** 盈损持买持卖（针对持买还是持买数据而设置的）：1：持买；2：持卖 */
	public String buySale = "";

	/** 止损触发价格 */
	public String stopLossPrice = "";

	/** 平仓触发价格 */
	public String stopProfitPrice = "";

	/** 盈损触发类型：1：按最新价触发；2：按买价触发；3：按卖价触发 */
	public String triggerType = "";

	/** 止损超价点数 */
	public String stopLossDot = "";

	/** 平仓超价点数 */
	public String stopProfitDot = "";

	/** 触发后下单数：有足够持仓数才下单 */
	public String orderQuantity = "";

	/** 已成交数 */
	public String filledQuantity = "";

	/** 已撤单数 */
	public String canceledQuantity = "";

	/** 是否永久有效（0：仅当前交易日有效；1：永久有效） */
	public String isPermanent = "";

	/** 下单类型（1：限价单；2：市价单） */
	public String orderType = "";

	/** 止损触发后下单的价格 */
	public String stopLossOrderPrice = "";

	/** 平仓触发后下单的价格 */
	public String stopProfitOrderPrice = "";

	/** 平仓标志（0：持仓不足时清除；1：持仓不足时按当前持仓数平仓） */
	public String closeFlag = "";

	/** 追价止盈损标志（0：不追价止盈损；1：追价止盈损） */
	public String traceFlag = "";

	/** 盈利追价触发点数（每盈利指定倍数的最小价格跳点的价格就触发一次止损价的回撤） */
	public String openPrice = "";

	/** 止损追踪点数（止损价追价最小价格变动值的整数倍） */
	public String tracePriceDiff = "";

	/** 止盈损开始触发的时间点(yyyyMMdd hh:mm:ss) */
	public String activeDateTime = "";

	/** 触发条件：几次达到触发价后触发，默认一次 20140909 add */
	public String triggerCondition = "";

	public String MyToString() {
		String temp = this.yingsunNo + "@" + this.userId + "@" + this.status + "@" + this.localNo
				+ "@" + this.exchangeNo + "@" + this.contractNo + "@" + this.buySale
				+ "@" + this.stopLossPrice + "@" + this.stopProfitPrice + "@" + this.triggerType
				+ "@" + this.stopLossDot + "@" + this.stopProfitDot + "@" + this.orderQuantity
				+ "@" + this.filledQuantity + "@" + this.canceledQuantity + "@" + this.isPermanent
				+ "@" + this.orderType + "@" + this.stopLossOrderPrice + "@" + this.stopProfitOrderPrice
				+ "@" + this.closeFlag + "@" + this.traceFlag + "@" + this.openPrice + "@" + this.tracePriceDiff
				+ "@" + this.activeDateTime + "@" + this.triggerCondition;

		return temp;
	}

	public void MyReadString(String temp) {
		String[] arrClass = temp.split("@", -1);
		this.yingsunNo = arrClass[0];
		this.userId = arrClass[1];
		this.status = arrClass[2];
		this.localNo = arrClass[3];
		this.exchangeNo = arrClass[4];
		this.contractNo = arrClass[5];
		this.buySale = arrClass[6];
		this.stopLossPrice = arrClass[7];
		this.stopProfitPrice = arrClass[8];
		this.triggerType = arrClass[9];
		this.stopLossDot = arrClass[10];
		this.stopProfitDot = arrClass[11];
		this.orderQuantity = arrClass[12];
		this.filledQuantity = arrClass[13];
		this.canceledQuantity = arrClass[14];

		if (arrClass.length > 15) {
			this.isPermanent = arrClass[15];
		}
		if (arrClass.length > 16) {
			this.orderType = arrClass[16];
		}
		if (arrClass.length > 17) {
			this.stopLossOrderPrice = arrClass[17];
		}
		if (arrClass.length > 18) {
			this.stopProfitOrderPrice = arrClass[18];
		}
		if (arrClass.length > 19) {
			this.closeFlag = arrClass[19];
		}
		if (arrClass.length > 20) {
			this.traceFlag = arrClass[20];
		}
		if (arrClass.length > 21) {
			this.openPrice = arrClass[21];
		}
		if (arrClass.length > 22) {
			this.tracePriceDiff = arrClass[22];
		}
		if (arrClass.length > 23) {
			this.activeDateTime = arrClass[23];
		}
		if (arrClass.length > 24) {
			this.triggerCondition = arrClass[24];
		}
	}

	public String MyPropToString() {
		String temp = "yingsunNo@userId@status@localNo@exchangeNo@contractNo"
				+ "@buySale@stopLossPrice@stopProfitPrice@triggerType@stopLossDot@stopProfitDot"
				+ "@orderQuantity@filledQuantity@canceledQuantity@isPermanent@orderType"
				+ "@stopLossOrderPrice@stopProfitOrderPrice@closeFlag@traceFlag@openPrice@tracePriceDiff@activeDateTime"
				+ "@triggerCondition";
		return temp;
	}
}

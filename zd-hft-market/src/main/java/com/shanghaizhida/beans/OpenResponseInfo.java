package com.shanghaizhida.beans;

/**
 * 持仓查询请求返回信息的类
 * 
 * @author xiang
 * 
 */

public class OpenResponseInfo implements NetParent {

	// 资金账号
	public String accountNo = "";

	// 交易所代码
	public String exchangeCode = "";

	// 合约代码
	public String code = "";

	// 买还是卖：1=buy 2=sell
	public String buySale = "";

	// 持仓数
	public String openNumber = "";

	// 持仓均价
	public String openPrice = "";

	// 逐笔盈亏
	public String caseProfit = "";

	// 盯市盈亏
	public String marketProfit = "";

	// 单笔盈亏
	public String singleProfit = "";

	// 错误代码
	public String errorCode = "";

	// 持仓类型：0 昨仓； 1 今仓
	public String holdType = "";

	// 原始开仓均价
	public String initOpenPrice = "";

	public String MyToString() {
		String temp = this.accountNo + "@" + this.code + "@" + this.exchangeCode
				+ "@" + this.buySale + "@" + this.openNumber + "@" + this.openPrice
				+ "@" + this.caseProfit + "@" + this.marketProfit + "@" + this.singleProfit
				+ "@" + this.errorCode + "@" + this.holdType + "@" + this.initOpenPrice;

		return temp;
	}

	public void MyReadString(String temp) {
		String[] arrClass = temp.split("@");
		this.accountNo = arrClass[0];
		this.code = arrClass[1];
		this.exchangeCode = arrClass[2];
		this.buySale = arrClass[3];
		this.openNumber = arrClass[4];
		this.openPrice = arrClass[5];
		this.caseProfit = arrClass[6];
		this.marketProfit = arrClass[7];
		this.singleProfit = arrClass[8];
		this.errorCode = arrClass[9];
		this.holdType = arrClass[10];
		this.initOpenPrice = arrClass[11];
	}

	public String MyPropToString() {
		String temp = "accountNo@code@exchangeCode@buySale@openNumber"
				+ "@openPrice@caseProfit@marketProfit@singleProfit@errorCode@holdType@initOpenPrice";
		return temp;
	}

}

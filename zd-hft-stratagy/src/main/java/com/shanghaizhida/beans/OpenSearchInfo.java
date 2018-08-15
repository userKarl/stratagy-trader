package com.shanghaizhida.beans;

/**
 * 持仓查询请求信息的类
 * 
 * @author xiang
 * 
 */

public class OpenSearchInfo implements NetParent {

	/** 资金账号 */
	public String accountNo = "";

	/** 交易密码 */
	public String tradePwd = "";

	/** 交易日期 */
	public String tradeDate = "";

	/** 交易所代码 */
	public String exchangeCode = "";

	/** 合约代码 */
	public String code = "";

	/** 买还是卖：1=buy 2=sell */
	public String buySale = "";

	@Override
	public String MyToString() {
		String temp = this.accountNo + "@" + this.tradePwd + "@" + this.tradeDate
				+ "@" + this.exchangeCode + "@" + this.code + "@" + this.buySale;

		return temp;
	}

	@Override
	public void MyReadString(String temp) {
		String[] arrClass = temp.split("@");
		this.accountNo = arrClass[0];
		this.tradePwd = arrClass[1];
		this.tradeDate = arrClass[2];
		this.exchangeCode = arrClass[3];
		this.code = arrClass[4];
		this.buySale = arrClass[5];
	}

	@Override
	public String MyPropToString() {
		String temp = "accountNo@tradePwd@tradeDate@exchangeCode@code@buySale";
		return temp;
	}
}

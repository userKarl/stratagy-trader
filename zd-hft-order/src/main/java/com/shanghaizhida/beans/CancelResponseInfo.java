package com.shanghaizhida.beans;

/**
 * 撤单请求返回信息的类
 * 
 * @author xiang
 * 
 */
public class CancelResponseInfo implements NetParent {

	// 用户ID
	public String userId = "";

	// 资金账号
	public String accountNo = "";

	// 系统编号
	public String systemNo = "";

	// 定单号
	public String orderNo = "";

	// 撤单号
	public String cancelNo = "";

	// 交易所代码
	public String exchangeCode = "";

	// 合约代码
	public String code = "";

	// 买还是卖：1=buy 2=sell
	public String buySale = "";

	// 下单数
	public String orderNumber = "";

	// 下单价格
	public String orderPrice = "";

	// 已成交数
	public String filledNumber = "";

	// 已撤单数
	public String cancelNumber = "";

	// 交易方式：1=regular 2=FOK 3=IOC
	public String tradeType = "";

	// 价格类型：1=limit order, 2=market order
	public String priceType = "";

	// 0=regular 1=HTS
	public String htsType = "";

	// 撤单日期
	public String cancelDate = "";

	// 撤单时间
	public String cancelTime = "";

	// 错误代码
	public String errorCode = "";

	// 用户下单类型：C或是空客户下单；D：是del下单 R：强平下单（风控）
	public String FIsRiskOrder = "";

	@Override
	public String MyToString() {
		String temp = this.userId + "@" + this.accountNo + "@" + this.systemNo
				+ "@" + this.orderNo + "@" + this.cancelNo + "@" + exchangeCode
				+ "@" + this.code + "@" + this.buySale + "@" + this.orderNumber
				+ "@" + this.orderPrice + "@" + this.filledNumber + "@" + this.cancelNumber
				+ "@" + this.tradeType + "@" + this.priceType + "@" + this.htsType
				+ "@" + this.cancelDate + "@" + this.cancelTime + "@" + this.errorCode
				+ "@" + this.FIsRiskOrder;

		return temp;
	}

	@Override
	public void MyReadString(String temp) {
		String[] arrClass = temp.split("@");
		this.userId = arrClass[0];
		this.accountNo = arrClass[1];
		this.systemNo = arrClass[2];
		this.orderNo = arrClass[3];
		this.cancelNo = arrClass[4];
		this.exchangeCode = arrClass[5];
		this.code = arrClass[6];
		this.buySale = arrClass[7];
		this.orderNumber = arrClass[8];
		this.orderPrice = arrClass[9];
		this.filledNumber = arrClass[10];
		this.cancelNumber = arrClass[11];
		this.tradeType = arrClass[12];
		this.priceType = arrClass[13];
		this.htsType = arrClass[14];
		this.cancelDate = arrClass[15];
		this.cancelTime = arrClass[16];

		if (arrClass.length > 17) {
			this.errorCode = arrClass[17];
		}
		if (arrClass.length > 18) {
			this.FIsRiskOrder = arrClass[18];
		}
	}

	@Override
	public String MyPropToString() {
		String temp = "userId@accountNo@systemNo@orderNo@cancelNo@exchangeCode"
				+ "@code@buySale@orderNumber@orderPrice@filledNumber@cancelNumber"
				+ "@tradeType@priceType@htsType@cancelDate@cancelTime@errorCode@FIsRiskOrder";
		return temp;
	}
}

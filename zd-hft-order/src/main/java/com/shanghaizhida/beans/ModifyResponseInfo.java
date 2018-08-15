package com.shanghaizhida.beans;

/**
 * 改单请求返回信息的类
 * 
 * @author xiang
 * 
 */
public class ModifyResponseInfo implements NetParent {

	// 用户ID
	public String userId = "";

	// 资金账号
	public String accountNo = "";

	// 定单号
	public String orderNo = "";

	// 改单号
	public String modifyNo = "";

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

	// 改单数
	public String modifyNumber = "";

	// 改单价格
	public String modifyPrice = "";

	// 交易方式：1=regular 2=FOK 3=IOC
	public String tradeType = "";

	// 价格类型：1=limit order, 2=market order
	public String priceType = "";

	// 改单日期
	public String modifyDate = "";

	// 改单时间
	public String modifyTime = "";

	// 错误代码
	public String errorCode = "";

	// 用户下单类型：C或是空客户下单；D：是del下单 R：强平下单（风控）
	public String FIsRiskOrder = "";

	// add by dragon 20121203 for 改单

	// 触发价格
	public String triggerPrice = "";

	// 改单触发价格
	public String modifyTriggerPrice = "";

	@Override
	public String MyToString() {
		String temp = this.userId + "@" + this.accountNo
				+ "@" + this.orderNo + "@" + this.modifyNo + "@" + exchangeCode
				+ "@" + this.code + "@" + this.buySale + "@" + this.orderNumber
				+ "@"+ this.orderPrice + "@" + this.filledNumber + "@" + this.modifyNumber + "@" + this.modifyPrice
				+ "@" + this.tradeType + "@" + this.priceType
				+ "@" + this.modifyDate + "@" + this.modifyTime + "@" + this.errorCode
				+ "@" + this.FIsRiskOrder + "@" + this.triggerPrice + "@" + this.modifyTriggerPrice;

		return temp;
	}

	@Override
	public void MyReadString(String temp) {
		String[] arrClass = temp.split("@");
		this.userId = arrClass[0];
		this.accountNo = arrClass[1];
		this.orderNo = arrClass[2];
		this.modifyNo = arrClass[3];
		this.exchangeCode = arrClass[4];
		this.code = arrClass[5];
		this.buySale = arrClass[6];
		this.orderNumber = arrClass[7];
		this.orderPrice = arrClass[8];
		this.filledNumber = arrClass[9];
		this.modifyNumber = arrClass[10];
		this.modifyPrice = arrClass[11];
		this.tradeType = arrClass[12];
		this.priceType = arrClass[13];
		this.modifyDate = arrClass[14];
		this.modifyTime = arrClass[15];
		this.errorCode = arrClass[16];
		this.FIsRiskOrder = arrClass[17];

		if (arrClass.length > 18) {
			this.triggerPrice = arrClass[18];
		}
		if (arrClass.length > 19) {
			this.modifyTriggerPrice = arrClass[19];
		}
	}

	@Override
	public String MyPropToString() {
		String temp = "userId@accountNo@orderNo@modifyNo@exchangeCode"
				+ "@code@buySale@orderNumber@orderPrice@filledNumber@modifyNumber@modifyPrice"
				+ "@tradeType@priceType@modifyDate@modifyTime@errorCode"
				+ "@FIsRiskOrder@triggerPrice@modifyTriggerPrice";
		return temp;
	}
}

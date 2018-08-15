package com.shanghaizhida.beans;

/**
 * 改单请求信息的类
 * 
 * @author xiang
 * 
 */
public class ModifyInfo implements NetParent {

	// 用户ID
	public String userId = "";

	// 用户类型：1：一般用户；2：机构通用户；
	public String userType = "";

	// 资金账号
	public String accountNo = "";

	// 交易密码
	public String tradePwd = "";

	// 定单号
	public String orderNo = "";

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

	// 价格类型：1=限价单, 2=市价单，3=限价止损（stop to limit），4=止损（stop to market）
	public String priceType = "";

	// 用户下单类型：C或是空客户下单；D：是del下单 R：强平下单（风控）
	public String FIsRiskOrder = "";

	// add by dragon 20121203 for 改单

	// 触发价格
	public String triggerPrice = "";

	// 改单触发价格
	public String modifyTriggerPrice = "";

	// 有效日期（1：当日有效；2：永久有效）
	public String validDate = "";

	@Override
	public String MyToString() {
		String temp = this.userId + "@" + this.userType + "@" + this.accountNo
				+ "@" + this.tradePwd
				+ "@" + this.orderNo + "@" + this.exchangeCode + "@" + this.code
				+ "@" + this.buySale + "@" + this.orderNumber + "@" + this.orderPrice
				+ "@" + this.filledNumber + "@" + this.modifyNumber + "@" + this.modifyPrice
				+ "@" + this.tradeType + "@" + this.priceType
				+ "@" + this.FIsRiskOrder + "@" + this.triggerPrice + "@" + this.modifyTriggerPrice
				+ "@" + this.validDate;

		return temp;
	}

	@Override
	public void MyReadString(String temp) {
		String[] arrClass = temp.split("@");
		this.userId = arrClass[0];
		this.userType = arrClass[1];
		this.accountNo = arrClass[2];
		this.tradePwd = arrClass[3];
		this.orderNo = arrClass[4];
		this.exchangeCode = arrClass[5];
		this.code = arrClass[6];
		this.buySale = arrClass[7];
		this.orderNumber = arrClass[8];
		this.orderPrice = arrClass[9];
		this.filledNumber = arrClass[10];
		this.modifyNumber = arrClass[11];
		this.modifyPrice = arrClass[12];
		this.tradeType = arrClass[13];
		this.priceType = arrClass[14];
		this.FIsRiskOrder = arrClass[15];

		if (arrClass.length > 16) {
			this.triggerPrice = arrClass[16];
		}
		if (arrClass.length > 17) {
			this.modifyTriggerPrice = arrClass[17];
		}
		if (arrClass.length > 18) {
			this.validDate = arrClass[18];
		}
	}

	@Override
	public String MyPropToString() {
		String temp = "userId@userType@accountNo@tradePwd"
				+ "@orderNo@exchangeCode@code@buySale@orderNumber@orderPrice"
				+ "@filledNumber@modifyNumber@modifyPrice@tradeType@priceType"
				+ "@FIsRiskOrder@triggerPrice@modifyTriggerPrice@validDate";
		return temp;
	}
}

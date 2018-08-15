package com.shanghaizhida.beans;

/**
 * 取消订单信息类
 * 
 * @author xiang
 * 
 */
public class CancelInfo implements NetParent {

	// 用户ID
	public String userId = "";

	// 用户类型：1：一般用户；2：机构通用户；
	public String userType = "";

	// 资金账号
	public String accountNo = "";

	// 交易密码
	public String tradePwd = "";

	// 是否模拟用户：1：是；0 or other：不是
	public String isSimulation = "";

	// 系统编号
	public String systemNo = "";

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

	// 交易方式：1=regular 2=FOK 3=IOC
	public String tradeType = "";

	// 价格类型：1=limit order, 2=market order
	public String priceType = "";

	// 0=regular 1=HTS
	public String htsType = "";

	// 用户下单类型：C或是空客户下单；D：是del下单 R：强平下单（风控）
	public String FIsRiskOrder = "";

	@Override
	public String MyToString() {
		String temp = this.userId + "@" + this.userType + "@" + this.accountNo
				+ "@" + this.tradePwd + "@" + this.isSimulation + "@" + this.systemNo
				+ "@" + this.orderNo + "@" + this.exchangeCode + "@" + this.code
				+ "@" + this.buySale + "@" + this.orderNumber + "@" + this.orderPrice
				+ "@" + this.filledNumber + "@" + this.tradeType + "@" + this.priceType
				+ "@" + this.htsType + "@" + this.FIsRiskOrder;

		return temp;
	}

	@Override
	public void MyReadString(String temp) {
		String[] arrClass = temp.split("@",-1);
		this.userId = arrClass[0];
		this.userType = arrClass[1];
		this.accountNo = arrClass[2];
		this.tradePwd = arrClass[3];
		this.isSimulation = arrClass[4];
		this.systemNo = arrClass[5];
		this.orderNo = arrClass[6];
		this.exchangeCode = arrClass[7];
		this.code = arrClass[8];
		this.buySale = arrClass[9];
		this.orderNumber = arrClass[10];
		this.orderPrice = arrClass[11];
		this.filledNumber = arrClass[12];
		this.tradeType = arrClass[13];
		this.priceType = arrClass[14];
		this.htsType = arrClass[15];
		this.FIsRiskOrder = arrClass[16];
	}

	@Override
	public String MyPropToString() {
		String temp = "userId@userType@accountNo@tradePwd@isSimulation@systemNo"
				+ "@orderNo@exchangeCode@code@buySale@orderNumber@orderPrice"
				+ "@filledNumber@tradeType@priceType@htsType@FIsRiskOrder";
		return temp;
	}
}

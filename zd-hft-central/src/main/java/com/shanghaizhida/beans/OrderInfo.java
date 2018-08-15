package com.shanghaizhida.beans;

/**
 * 发送定单请求信息的类
 * 
 * @author xiang
 * 
 */
public class OrderInfo implements NetParent {

	/** 用户ID */
	public String userId = "";

	/** 用户类型：1：一般用户；2：机构通用户； */
	public String userType = "";

	/** 资金账号 */
	public String accountNo = "";

	/** 交易密码 */
	public String tradePwd = "";

	/** 用户下单类型：C或是空客户下单；D：是del下单 R：强平下单（风控）；Y：盈损单；T：条件单 */
	public String FIsRiskOrder = "";

	/** 交易所代码 */
	public String exchangeCode = "";

	/** 合约代码 */
	public String code = "";

	/** 买还是卖：1=buy 2=sell */
	public String buySale = "";

	/** 开仓还是平仓：1=开仓 2=平仓，3=平今，4=平昨 */
	public String addReduce = "";

	/** 下单数 */
	public String orderNumber = "";

	/** 下单价格 */
	public String orderPrice = "";

	/** 交易方式：1=regular 2=FOK 3=IOC */
	public String tradeType = "";

	/** 定单类型：1=限价单, 2=市价单，3=限价止损（stop to limit），4=止损（stop to market） */
	public String priceType = "";

	/** 0=regular 1=HTS */
	public String htsType = "";

	/** 强平编号 */
	public String flID = "";

	/** 触发价格 */
	public String triggerPrice = "";

	/** 有效日期（1=当日有效, 2=永久有效, 3=FAK, 4=FOK） */
	public String validDate = "";

	/** 策略ID 20130726 add */
	public String strategyId = "";

	/** 显示委托量 20150803 add 必须小于委托量 */
	public String MaxShow = "";

	/** 最小成交量 20150901 add 必须小于等于委托量 有效日期=4IOC时 MaxShow>=1小于委托量时是FOK，等于委托量时是FAK */
	public String MinQty = "";

	public String localSystemCode = "";
	
	@Override
	public String MyToString() {
		StringBuilder temp = new StringBuilder();
		temp.append(this.userId).append("@").append(this.userType).append("@").append(this.accountNo)
				.append("@").append(this.tradePwd).append("@").append(this.FIsRiskOrder).append("@").append(exchangeCode)
				.append("@").append(this.code).append("@").append(this.buySale).append("@").append(this.orderNumber)
				.append("@").append(this.orderPrice).append("@").append(this.tradeType).append("@").append(this.priceType)
				.append("@").append(this.htsType).append("@").append(this.flID).append("@").append(this.triggerPrice)
				.append("@").append(this.validDate).append("@").append(this.addReduce).append("@").append(this.strategyId)
				.append("@").append(this.MaxShow).append("@").append(this.MinQty).append("@").append(this.localSystemCode);

		return temp.toString();
	}

	@Override
	public void MyReadString(String temp) {
		try {
			String[] arrClass = temp.split("@");
			this.userId = arrClass[0];
			this.userType = arrClass[1];
			this.accountNo = arrClass[2];
			this.tradePwd = arrClass[3];
			this.FIsRiskOrder = arrClass[4];
			this.exchangeCode = arrClass[5];
			this.code = arrClass[6];
			this.buySale = arrClass[7];
			this.orderNumber = arrClass[8];
			this.orderPrice = arrClass[9];
			this.tradeType = arrClass[10];
			this.priceType = arrClass[11];
			this.htsType = arrClass[12];

			if (arrClass.length > 13) {
				this.flID = arrClass[13];
			}
			if (arrClass.length > 14) {
				this.triggerPrice = arrClass[14];
			}
			if (arrClass.length > 15) {
				this.validDate = arrClass[15];
			}
			if (arrClass.length > 16) {
				this.addReduce = arrClass[16];
			}
			if (arrClass.length > 17) {
				this.strategyId = arrClass[17];
			}
			if (arrClass.length > 18) {
				this.MaxShow = arrClass[18];
			}
			if (arrClass.length > 19) {
				this.MinQty = arrClass[19];
			}
			if (arrClass.length > 20) {
				this.localSystemCode = arrClass[20];
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	@Override
	public String MyPropToString() {
		try {
			String temp = "userId@userType@accountNo@tradePwd@FIsRiskOrder"
					+ "@exchangeCode@code@buySale@orderNumber@orderPrice@tradeType@priceType@htsType@flID@triggerPrice"
					+ "@validDate@addReduce@strategyId@MaxShow@MinQty@localSystemCode";
			return temp;
		} catch (Exception ex) {
			return ex.toString();
		}
	}

}

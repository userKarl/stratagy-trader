package com.shanghaizhida.beans;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * 定单请求返回信息的类
 * 
 * @author xiang
 * 
 */
public class OrderResponseInfo implements NetParent, Serializable {

	private static final long serialVersionUID = 1938098106628220269L;

	public String contractName = "";

	/** 用户ID */
	public String userId = "";

	/** 资金账号 */
	public String accountNo = "";

	/** 系统编号 */
	public String systemNo = "";

	/** 本地编号 */
	public String localNo = "";

	/** 定单号 */
	public String orderNo = "";

	/** 原定单号 */
	public String origOrderNo = "";

	/** 下单方式：1：定单；2：改单；3：撤单 */
	public String orderMethod = "";

	/** 下单接受类型：0：未接受；1：已接受；2：被拒绝了；3：撤单 */
	public String acceptType = "";

	/** 交易所代码 */
	public String exchangeCode = "";

	/** 合约代码 */
	public String code = "";

	/** 买还是卖：1=buy 2=sell */
	public String buySale = "";

	/** 下单数 */
	public String orderNumber = "";

	/** 下单价格 */
	public String orderPrice = "";

	/** 已成交数 */
	public String filledNumber = "";

	/** 成交均价 */
	public String filledPrice = "";

	/** 交易方式：1=regular 2=FOK 3=IOC */
	public String tradeType = "";

	/** 定单类型：1=限价单, 2=市价单，3=限价止损（stop to limit），4=止损（stop to market） */
	public String priceType = "";

	/** 0=regular 1=HTS */
	public String htsType = "";

	/** 下单日期 */
	public String orderDate = "";

	/** 下单时间 */
	public String orderTime = "";

	/** 错误代码 */
	public String errorCode = "";

	/** 订单状态（1：已请求；2：已排队；3：部分成交；4：完全成交；5：已撤余单；6：已撤单；7：指令失败） */
	public String orderState = "";

	/** 用户下单类型：C或是空客户下单；D：是del下单 R：强平下单（风控）；Y：盈损单；T：条件单 */
	public String FIsRiskOrder = "";

	/** 撤单的用户ID */
	public String cancelUserId = "";

	/** 触发价格 */
	public String triggerPrice = "";

	/** 有效日期（1=当日有效, 2=永久有效） */
	public String validDate = "";

	/** 开仓还是平仓：1=开仓 2=平仓，3=平今，4=平昨 */
	public String addReduce = "";

	/** 策略ID 20130726 add */
	public String strategyId = "";

	/** 撤单时间 (HH:mm:ss)20150715 add */
	public String cancelTime = "";

	/** 显示委托量 20150803 add 必须小于委托量 */
	public String MaxShow = "";

	/** 最小成交量 20150901 add 必须小于等于委托量 有效日期=4时 MaxShow>=1小于委托量时是FOK，等于委托量时是FAK */
	public String MinQty = "";

	public int statusSeq;

	public int dotNum = 4;

	@Override
	public String MyToString() {
		StringBuilder temp = new StringBuilder();
		temp.append(this.userId).append("@").append(this.accountNo).append("@").append(this.systemNo)
				.append("@").append(this.localNo).append("@").append(this.orderNo).append("@").append(this.origOrderNo)
				.append("@").append(this.orderMethod).append("@").append(this.acceptType).append("@").append(this.exchangeCode)
				.append("@").append(this.code).append("@").append(this.buySale).append("@").append(this.orderNumber)
				.append("@").append(this.orderPrice).append("@").append(this.filledNumber).append("@").append(this.filledPrice)
				.append("@").append(this.tradeType).append("@").append(this.priceType).append("@").append(this.htsType)
				.append("@").append(this.orderDate).append("@").append(this.orderTime).append("@").append(this.errorCode)
				.append("@").append(this.orderState).append("@").append(this.FIsRiskOrder).append("@").append(this.cancelUserId)
				.append("@").append(this.triggerPrice).append("@").append(this.validDate).append("@").append(this.addReduce)
				.append("@").append(this.strategyId).append("@").append(this.cancelTime).append("@").append(this.MaxShow)
				.append("@").append(this.MinQty);

		return temp.toString();
	}

	@Override
	public void MyReadString(String temp) {
		String[] arrClass = temp.split("@");
		this.userId = arrClass[0];
		this.accountNo = arrClass[1];
		this.systemNo = arrClass[2];
		this.localNo = arrClass[3];
		this.orderNo = arrClass[4];
		this.origOrderNo = arrClass[5];
		this.orderMethod = arrClass[6];
		this.acceptType = arrClass[7];
		this.exchangeCode = arrClass[8];
		this.code = arrClass[9];
		if (this.code.substring(this.code.length() - 3, this.code.length()).equals(".US")) {
			if (this.exchangeCode.equals("")) {
				this.exchangeCode="NASD";
			}
		}
		this.buySale = arrClass[10];
		this.orderNumber = arrClass[11];
		this.orderPrice = arrClass[12];
		this.filledNumber = arrClass[13];
		this.filledPrice = arrClass[14];
		this.tradeType = arrClass[15];
		this.priceType = arrClass[16];
		this.htsType = arrClass[17];
		this.orderDate = arrClass[18];
		this.orderTime = arrClass[19];
		this.errorCode = arrClass[20];

		if (arrClass.length > 21) {
			this.orderState = arrClass[21];
		}
		if (arrClass.length > 22) {
			this.FIsRiskOrder = arrClass[22];
		}
		if (arrClass.length > 23) {
			this.cancelUserId = arrClass[23];
		}
		if (arrClass.length > 24) {
			this.triggerPrice = arrClass[24];
		}
		if (arrClass.length > 25) {
			this.validDate = arrClass[25];
		}
		if (arrClass.length > 26) {
			this.addReduce = arrClass[26];
		}
		if (arrClass.length > 27) {
			this.strategyId = arrClass[27];
		}
		if (arrClass.length > 28) {
			this.cancelTime = arrClass[28];
		}
		if (arrClass.length > 29) {
			this.MaxShow = arrClass[29];
		}
		if (arrClass.length > 30) {
			this.MinQty = arrClass[30];
		}
	}

	@Override
	public String MyPropToString() {
		String temp = "userId@accountNo@systemNo@localNo@orderNo@origOrderNo"
				+ "@orderMethod@acceptType@exchangeCode@code@buySale@orderNumber"
				+ "@orderPrice@filledNumber@filledPrice@tradeType@priceType@htsType"
				+ "@orderDate@orderTime@errorCode@orderState@FIsRiskOrder@cancelUserId@triggerPrice"
				+ "@validDate@addReduce@strategyId@cancelTime@MaxShow@MinQty";
		return temp;
	}

	/**
	 * 对象拷贝
	 * 
	 * @return
	 * @throws Exception
	 */
	public Object deepCopy() throws Exception {
		// 将该对象序列化成流,因为写在流里的是对象的一个拷贝，而原对象仍然存在于JVM里面。所以利用这个特性可以实现对象的深拷贝
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(bos);
		oos.writeObject(this);

		// 将流序列化成对象
		ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
		ObjectInputStream ois = new ObjectInputStream(bis);
		return ois.readObject();
	}

    /**
     * 点价下单页面，处理买卖挂单同价格数量合并，重写hasCode和equals方法
     */
	@Override
	public int hashCode() {
		return (orderPrice + buySale).hashCode();
	}

	@Override
	public boolean equals(Object o) {
		return ((OrderResponseInfo) o).orderPrice.equals(orderPrice) && ((OrderResponseInfo) o).buySale.equals(buySale);
	}
}

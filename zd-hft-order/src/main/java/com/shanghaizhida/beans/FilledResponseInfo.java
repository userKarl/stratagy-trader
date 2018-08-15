package com.shanghaizhida.beans;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * 返回的成交信息类
 * 
 * @author xiang
 * 
 */

public class FilledResponseInfo implements NetParent, Serializable {

	private static final long serialVersionUID = 1L;

	public String contractName = "";

	/** 用户ID */
	public String userId = "";

	/** 资金账号 */
	public String accountNo = "";

	/** 成交编号（要包括7位的订单编号，一共11位） */
	public String filledNo = "";

	/** 定单号 */
	public String orderNo = "";

	/** 系统编号 */
	public String systemNo = "";

	/** 本地编号 */
	public String localNo = "";

	/** 交易所代码 */
	public String exchangeCode = "";

	/** 合约代码 */
	public String code = "";

	/** 买还是卖：1=buy 2=sell */
	public String buySale = "";

	/** 成交数 */
	public String filledNumber = "";

	/** 成交价格 */
	public String filledPrice = "";

	/** 成交日期(yyyy-MM-dd) */
	public String filledDate = "";

	/** 成交时间(hh:mm:ss) */
	public String filledTime = "";

	/** 成交手续费 */
	public String commsion = "";

	/** 委托数量 */
	public String HTSCode = "";

	/** 委托价格 */
	public String errorCode = "";

	/** 合约交割日期(yyyyMMdd) */
	public String deliveryDate = "";

	/** 成交类别(N：普通下单成交；C、T：调期模拟成交) */
	public String filledType = "";

	/** 定单类型（1=限价单, 2=市价单，3=限价止损（stop to limit），4=止损（stop to market） */
	public String orderType = "";

	/** 有效日期（1=当日有效, 2=永久有效） */
	public String validDate = "";

	/** 开仓还是平仓：1=开仓 2=平仓，3=平今，4=平昨 */
	public String addReduce = "";

	public int dotNum = 4;

	@Override
	public String MyToString() {
		String temp = this.userId + "@" + this.accountNo + "@" + this.filledNo
				+ "@" + this.orderNo + "@" + this.systemNo + "@" + this.localNo
				+ "@" + this.exchangeCode + "@" + this.code + "@" + this.buySale
				+ "@" + this.filledNumber + "@" + this.filledPrice + "@" + this.filledDate
				+ "@" + this.filledTime + "@" + this.commsion + "@" + this.HTSCode
				+ "@" + this.errorCode + "@" + this.deliveryDate + "@" + this.filledType
				+ "@" + this.orderType + "@" + this.validDate + "@" + this.addReduce;

		return temp;
	}

	@Override
	public void MyReadString(String temp) {
		String[] arrClass = temp.split("@");
		this.userId = arrClass[0];
		this.accountNo = arrClass[1];
		this.filledNo = arrClass[2];
		this.orderNo = arrClass[3];
		this.systemNo = arrClass[4];
		this.localNo = arrClass[5];
		this.exchangeCode = arrClass[6];
		this.code = arrClass[7];
		if (this.code.substring(this.code.length() - 3, this.code.length()).equals(".US")) {
			if (this.exchangeCode.equals("")) {
				this.exchangeCode="NASD";
			}
		}
		this.buySale = arrClass[8];
		this.filledNumber = arrClass[9];
		this.filledPrice = arrClass[10];
		this.filledDate = arrClass[11];
		this.filledTime = arrClass[12];
		this.commsion = arrClass[13];
		this.HTSCode = arrClass[14];
		this.errorCode = arrClass[15];
		this.deliveryDate = arrClass[16];
		this.filledType = arrClass[17];

		if (arrClass.length > 18) {
			this.orderType = arrClass[18];
		}
		if (arrClass.length > 19) {
			this.validDate = arrClass[19];
		}
		if (arrClass.length > 20) {
			this.addReduce = arrClass[20];
		}
	}

	@Override
	public String MyPropToString() {
		String temp = "userId@accountNo@filledNo@orderNo@systemNo@localNo"
				+ "@exchangeCode@code@buySale@filledNumber@filledPrice@filledDate"
				+ "@filledTime@commsion@HTSCode@errorCode@deliveryDate@filledType"
				+ "@orderType@validDate@addReduce";
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
}

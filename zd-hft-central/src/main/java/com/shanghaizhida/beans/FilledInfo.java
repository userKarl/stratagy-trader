package com.shanghaizhida.beans;

/**
 * 小帐户大帐户绑定信息
 * 
 * @author xiang
 * 
 */

public class FilledInfo implements NetParent {

	// 客户号
	public String clientNo = "";

	// 交易所No
	public String exchangeNo = "";

	// 商品ID
	public String code = "";

	// 序号
	public String index = "";

	// 单价
	public String matchPrice = "";

	// 结算价
	public String settlePrice = "";

	// 量
	public String matchVol = "";

	// 买或者卖
	public String type = "";
	// 20120321 0000610 jimmy add begin

	// 交割日
	public String expiryDate = "";

	// 20120321 0000610 jimmy add end
	//
	// // 系统编号
	//
	// public String systemNo ;

	@Override
	public String MyToString() {
		String temp = this.clientNo + "@" + this.exchangeNo + "@" + this.code
				+ "@" + this.index + "@" + this.matchPrice + "@" + this.settlePrice + "@" + this.matchVol
				// + "@" + this.type + "@" + systemNo;
				// 20120321 0000610 jimmy change begin
				// + "@" + this.type;
				+ "@" + this.type + "@" + this.expiryDate;
				// 20120321 0000610 jimmy change begin

		return temp;
	}

	@Override
	public void MyReadString(String temp) {
		String[] arrClass = temp.split("@");
		this.clientNo = arrClass[0];
		this.exchangeNo = arrClass[1];
		this.code = arrClass[2];
		this.index = arrClass[3];
		this.matchPrice = arrClass[4];
		this.settlePrice = arrClass[5];
		this.matchVol = arrClass[6];
		this.type = arrClass[7];
		// this.systemNo = arrClass[8];
		// 20120321 0000610 jimmy add begin
		this.expiryDate = arrClass[8];
		// 20120321 0000610 jimmy add end
	}

	@Override
	public String MyPropToString() {
		// return
		// "clientNo@exchangeNo@code@index@matchPrice@settlePrice@matchVol@type@systemNo";
		// 20120321 0000610 jimmy change begin
		// return
		// "clientNo@exchangeNo@code@index@matchPrice@settlePrice@matchVol@type";
		return "clientNo@exchangeNo@code@index@matchPrice@settlePrice@matchVol@type@expiryDate";
		// 20120321 0000610 jimmy change end

	}
}

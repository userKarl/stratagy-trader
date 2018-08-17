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
		if(arrClass.length>0) {
			this.clientNo = arrClass[0];
		}
		if(arrClass.length>1) {
			this.exchangeNo = arrClass[1];	
		}
		if(arrClass.length>2) {
			this.code = arrClass[2];
		}
		if(arrClass.length>3) {
			this.index = arrClass[3];
		}
		if(arrClass.length>4) {
			this.matchPrice = arrClass[4];
		}
		if(arrClass.length>5) {
			this.settlePrice = arrClass[5];
		}
		if(arrClass.length>6) {
			this.matchVol = arrClass[6];
		}
		if(arrClass.length>7) {
			this.type = arrClass[7];
		}
		if(arrClass.length>8) {
			this.expiryDate = arrClass[8];
		}
		
		// this.systemNo = arrClass[8];
		// 20120321 0000610 jimmy add begin
		
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

package com.shanghaizhida.beans;

/**
 * 币种信息
 * 
 * @author xiang
 * 
 */

public class CurrencyInfo implements NetParent {

	// 货币编号
	public String currencyNo = "";

	// 基币货币编号
	public int isBase;

	// 与基币的换算汇率
	public float rate;

	public String MyToString() {
		return this.currencyNo + "@" + this.isBase + "@" + this.rate;
	}

	public void MyReadString(String temp) {
		String[] arrClass = temp.split("@");
		this.currencyNo = arrClass[0];
		this.isBase = Integer.parseInt(arrClass[1]);
		this.rate = Float.parseFloat(arrClass[2]);
	}

	public String MyPropToString() {
		return "currencyNo@isBase@rate";
	}
}

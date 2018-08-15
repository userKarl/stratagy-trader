package com.shanghaizhida.beans;

/**
 * 可用合约请求信息类
 * 
 * @author xiang
 * 
 */

public class UseContract implements NetParent {

	// 商品编号合约NO
	public String code = "";

	// 交易所编号
	public String FExchangeNo = "";

	// 合约NO
	public String FContractNo = "";

	// 合约名
	public String ContractFName = "";

	// 商品编号
	public String FCommodityNo = "";

	// 商品名
	public String CommodityFName = "";

	// 商品类别
	public String FCommodityType = "";

	// 货币编号
	public String CommodityFCurrencyNo = "";

	// 货币名称
	public String CurrencyFName = "";

	// 点数
	public String FProductDot = "";

	// 最小变动单位
	public String FUpperTick = "";

	// 交易所名称
	public String FName = "";

	// 上日结算价
	public String FPrice = "";

	// 交易月 (yyyyMM)/交割日 (yyyyMMdd)
	public String FTradeMonth = "";

	// 行情小数点位数
	public int FDotNum;

	// 进阶单位
	public int FLowerTick;

	// add by dragon 20120813

	// 调期小数点位数
	public int FDotNum_Carry;

	// 调期最小变动单位
	public String FUpperTick_Carry = "";

	// 首次通知日 (yyyyMMdd)
	public String FFirstNoticeDay = "";

	// 冻结保证金百分比
	public String FFreezenPercent = "";

	// 冻结保证金固定值
	public String FFreezenMoney = "";

	// 固定手续费
	public String FFeeMoney = "";

	// 百分比手续费
	public String FFeePercent = "";

	// 现货商品昨结算价
	public String FPriceStrike = "";

	// 现货商品点值
	public String FProductDotStrike = "";

	// 现货商品最小变动单位
	public String FUpperTickStrike = "";

	// 最后交易日 (yyyyMMdd)
	public String FLastTradeDay = "";

	// 最后更新日 (yyyyMMdd)
	public String FLastUpdateDay = "";

	// 期权临界价格
	public String FCriticalPrice = "";

	// 期权临界价格以下的最小跳点
	public String FCriticalMinChangedPrice = "";

	@Override
	public String MyToString() {
		// modify for 0000516 20120910
		// 金额格式化
		String format = "0";
		for (int i = 0; i < this.FDotNum; i++) {
			if (i == 0) {
				format = format + ".0";
			} else {
				format = format + "0";
			}
		}

		String temp = this.code + "@" + this.FExchangeNo + "@" + this.FContractNo
				+ "@" + this.ContractFName + "@" + this.FCommodityNo + "@" + this.CommodityFName
				+ "@"+ this.FCommodityType + "@" + this.CommodityFCurrencyNo + "@" + this.CurrencyFName
				+ "@" + this.FProductDot + "@" + this.FUpperTick + "@" + this.FName + "@" + String.format(format)
				+ "@" + this.FTradeMonth + "@" + this.FDotNum + "@" + this.FLowerTick
				+ "@" + this.FDotNum_Carry + "@" + this.FUpperTick_Carry + "@" + this.FFirstNoticeDay
				+ "@" + this.FFreezenPercent + "@" + this.FFreezenMoney + "@" + this.FFeeMoney + "@" + this.FFeePercent
				+ "@" + this.FPriceStrike + "@" + this.FProductDotStrike + "@" + this.FUpperTickStrike
				+ "@" + this.FLastTradeDay + "@" + this.FLastUpdateDay + "@" + this.FCriticalPrice + "@" + this.FCriticalMinChangedPrice;

		return temp;
	}

	@Override
	public void MyReadString(String temp) {
		String[] arrClass = temp.split("@");
		this.code = arrClass[0];
		this.FExchangeNo = arrClass[1];
		this.FContractNo = arrClass[2];
		this.ContractFName = arrClass[3];
		this.FCommodityNo = arrClass[4];
		this.CommodityFName = arrClass[5];
		this.FCommodityType = arrClass[6];
		this.CommodityFCurrencyNo = arrClass[7];
		this.CurrencyFName = arrClass[8];
		this.FProductDot = arrClass[9];
		this.FUpperTick = arrClass[10];
		this.FName = arrClass[11];
		this.FPrice = arrClass[12];
		this.FTradeMonth = arrClass[13];
		this.FDotNum = Integer.parseInt(arrClass[14]);
		this.FLowerTick = Integer.parseInt(arrClass[15]);

		if (arrClass.length >= 17) {
			this.FDotNum_Carry = Integer.parseInt(arrClass[16]);
		}
		if (arrClass.length >= 18) {
			this.FUpperTick_Carry = arrClass[17];
		}
		if (arrClass.length >= 19) {
			this.FFirstNoticeDay = arrClass[18];
		}
		if (arrClass.length >= 20) {
			this.FFreezenPercent = arrClass[19];
		}
		if (arrClass.length >= 21) {
			this.FFreezenMoney = arrClass[20];
		}
		if (arrClass.length >= 22) {
			this.FFeeMoney = arrClass[21];
		}
		if (arrClass.length >= 23) {
			this.FFeePercent = arrClass[22];
		}
		if (arrClass.length >= 24) {
			this.FPriceStrike = arrClass[23];
		}
		if (arrClass.length >= 25) {
			this.FProductDotStrike = arrClass[24];
		}
		if (arrClass.length >= 26) {
			this.FUpperTickStrike = arrClass[25];
		}
		if (arrClass.length >= 27) {
			this.FLastTradeDay = arrClass[26];
		}
		if (arrClass.length >= 28) {
			this.FLastUpdateDay = arrClass[27];
		}
		if (arrClass.length >= 29) {
			this.FCriticalPrice = arrClass[28];
		}
		if (arrClass.length >= 30) {
			this.FCriticalMinChangedPrice = arrClass[29];
		}
	}

	@Override
	public String MyPropToString() {
		String temp = code + "@" + FExchangeNo + "@" + FContractNo + "@" + ContractFName + "@" + FCommodityNo + "@" + CommodityFName
				+ "@" + FCommodityType + "@" + CommodityFCurrencyNo + "@" + CurrencyFName + "@" + FProductDot + "@" + FUpperTick + "@" + FName + "@" + FPrice
				+ "@" + FTradeMonth + "@" + FDotNum + "@" + FLowerTick + "@" + FDotNum_Carry + "@" + FUpperTick_Carry + "@" + FFirstNoticeDay
				+ "@" + FFreezenPercent + "@" + FFreezenMoney + "@" + FFeeMoney + "@" + FFeePercent + "@" + FPriceStrike + "@" + FProductDotStrike + "@" + FUpperTickStrike
				+ "@" + FLastTradeDay + "@" + FLastUpdateDay + "@" + FCriticalPrice + "@" + FCriticalMinChangedPrice;
		return temp;
	}
}

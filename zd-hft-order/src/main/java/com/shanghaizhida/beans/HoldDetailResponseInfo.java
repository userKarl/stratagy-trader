package com.shanghaizhida.beans;

/**
 * 持仓明细请求返回数据类
 * 
 * @author xiang
 * 
 */

public class HoldDetailResponseInfo implements NetParent {

	// 成交日期（yyyyMMdd）
	public String FMatchDate = "";

	// 成交编号
	public String FMatchNo = "";

	// 客户编号
	public String FClientNo = "";

	// 交易所编号
	public String FExchangeNo = "";

	// 商品编号
	public String FCommodityNo = "";

	// 合约编号
	public String FContractNo = "";

	// 买卖方向（1：买；2：卖）
	public String FDirect = "";

	// 持仓数
	public String FHoldVol = "";

	// 开仓均价（原始开仓价）
	public String FHoldPrice = "";

	// 货币编号
	public String FCurrencyNo = "";

	// 结算后的持仓均价
	public String FForciblyClosePrice = "";

	// 资金账号
	public String FAccount = "";

	// 持仓类型（0：昨仓；1：今仓）
	public String FHoldType = "";

	// 合约交割日期(yyyyMMdd)
	public String deliveryDate = "";

	// 交易所名称(客户端自己取得)
	public String FExChangeName = "";

	// 货币名称(客户端自己取得)
	public String FCurrencyName = "";

	// 合约代码（商品编号+合约编号）(客户端自己取得)
	public String FCode = "";

	// 已经平仓了的数量
	public String pingcangVol = "0";

	@Override
	public String MyToString() {
		String temp = "";
		try {
			temp = this.FClientNo + "@" + this.FAccount + "@" + this.FExchangeNo
					+ "@" + this.FCurrencyNo
					+ "@" + this.FCommodityNo + "@" + this.FContractNo
					+ "@" + this.FMatchDate + "@" + this.FMatchNo + "@" + this.FDirect
					+ "@" + this.FHoldVol + "@" + this.FHoldPrice
					+ "@" + this.FForciblyClosePrice + "@" + this.FHoldType + "@" + this.deliveryDate;
		} catch (Exception ex) {
		}
		return temp;

	}

	@Override
	public void MyReadString(String temp) {
		try {
			String[] arrClass = temp.split("@");
			this.FClientNo = arrClass[0];
			this.FAccount = arrClass[1];
			this.FExchangeNo = arrClass[2];
			this.FCurrencyNo = arrClass[3];
			this.FCommodityNo = arrClass[4];
			this.FContractNo = arrClass[5];
			this.FMatchDate = arrClass[6];
			this.FMatchNo = arrClass[7];
			this.FDirect = arrClass[8];
			this.FHoldVol = arrClass[9];
			this.FHoldPrice = arrClass[10].equals("") ? "0" : arrClass[10];
			this.FForciblyClosePrice = arrClass[11].equals("") ? "0" : arrClass[11];
			this.FHoldType = arrClass[12];

			if (arrClass.length > 13) {
				this.deliveryDate = arrClass[13];
			}

			this.FCode = FCommodityNo + FContractNo;

		} catch (Exception ex) {
		}

	}

	@Override
	public String MyPropToString() {
		String temp = "";
		try {
			temp = "FClientNo@FAccount@FExchangeNo@FCurrencyNo"
					+ "@FCommodityNo@FContractNo@FMatchDate@FMatchNo@FDirect"
					+ "@FHoldVol@FHoldPrice@FForciblyClosePrice@FHoldType@deliveryDate";
		} catch (Exception ex) {
		}

		return temp;
	}
}

package com.shanghaizhida.beans;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * 股票持仓返回信息类
 * <p>
 * 
 * @author xiangchao
 * @date 2016年2月22日<br>
 * @version 1.0<br>
 */

public class HoldResponseInfoStock implements NetParent, Serializable {

	private static final long serialVersionUID = -7372363321910048851L;

	/** 用户ID */
	public String FClientNo = "";

	/** 交易所 */
	public String FExchangeNo = "";

	/** 证券代码 */
	public String FCommodityNo = "";

	/** 证券中文名称 */
	public String FCommodityName = "";

	/** 持仓方向（1：持买；2：持卖） */
	public String FDirect = "";

	/** 持仓成本价 */
	public String FHoldPrice = "";

	/** 可卖数量 */
	public String FCanTradeVol = "";

	/** 今买数量 */
	public String FTodayBuyVol = "";

	/** 冻结数量 */
	public String FFrozenVol = "";

	/** 持有期内买入金额总和 */
	public String FTotalBuyMoney = "";

	/** 持有期内卖出金额总和 */
	public String FTotalSellMoney = "";

	/** 持有期内买入数量 */
	public String FTotalBuyVol = "";

	/** 持有期内卖出数量 */
	public String FTotalSellVol = "";

	/** 首次开仓日期(yyyy-MM-dd) */
	public String FOpenDate = "";

	/** 平仓盈利 */
	public String FFlatProfit = "";

	/** 港股T+1数量 */
	public String FHkexT1 = "";

	/** 港股T+2数量 */
	public String FHkexT2 = "";

	/** 美股T+3数量 */
	public String FHkexT3 = "";

	/** 港股未交收数量=T1+T2 */
	public String FUnsettleVol = "";

	/** 港股已交收数量 */
	public String FSettledVol = "";

	/** 持仓数量 */
	public String FHoldVol = "";

	/** 今卖数量 */
	public String FTodaySaleVol = "";

	/** 卖空冻结资金 */
	public String FSellFrozenMoney = "";

	/** 开仓均价 */
	public String FOpenPrice = "";

	/** 顺序号 */
	public int FStatus;

	// add by xiang at 30160302
	public double currPrice;

	/** 浮盈 */
	public double floatProfit;
	/** 盈亏 */
	public double preFuYing;
	/** 按揭价值 */
	public double mortgageMoney;
	/** 市值 */
	public double marketValues;
	/**币种*/
	public String currencyNo;

	public String MyToString() {
		String temp = this.FClientNo + "@" + this.FExchangeNo + "@" + this.FCommodityNo
				+ "@" + this.FDirect + "@" + this.FHoldPrice + "@" + this.FCanTradeVol
				+ "@" + this.FTodayBuyVol + "@" + this.FFrozenVol + "@" + this.FTotalBuyMoney
				+ "@" + this.FTotalSellMoney + "@" + this.FTotalBuyVol + "@" + this.FTotalSellVol
				+ "@" + this.FOpenDate + "@" + this.FFlatProfit + "@" + this.FHkexT1
				+ "@" + this.FHkexT2 + "@" + this.FHkexT3 + "@" + this.FUnsettleVol + "@" + this.FSettledVol
				+ "@" + this.FHoldVol + "@" + this.FTodaySaleVol + "@" + this.FSellFrozenMoney
				+ "@" + this.FOpenPrice + "@" + this.FStatus;

		return temp;
	}

	public void MyReadString(String temp) {
		String[] arrClass = temp.split("@");
		this.FClientNo = arrClass[0];
		this.FExchangeNo = arrClass[1];
		this.FCommodityNo = arrClass[2];
		if (this.FCommodityNo.substring(this.FCommodityNo.length() - 3, this.FCommodityNo.length()).equals(".US")) {
			if (this.FExchangeNo.equals("")) {
				this.FExchangeNo="NASD";
			}
		}
		this.FDirect = arrClass[3];
		this.FHoldPrice = arrClass[4];
		this.FCanTradeVol = arrClass[5];
		this.FTodayBuyVol = arrClass[6];
		this.FFrozenVol = arrClass[7];
		this.FTotalBuyMoney = arrClass[8];
		this.FTotalSellMoney = arrClass[9];
		this.FTotalBuyVol = arrClass[10];
		this.FTotalSellVol = arrClass[11];
		this.FOpenDate = arrClass[12];
		this.FFlatProfit = arrClass[13];
		this.FHkexT1 = arrClass[14];
		this.FHkexT2 = arrClass[15];

		if (arrClass.length > 16) {
			this.FHkexT3 = arrClass[16];
		}
		if (arrClass.length > 17) {
			this.FUnsettleVol = arrClass[17];
		}
		if (arrClass.length > 18) {
			this.FSettledVol = arrClass[18];
		}
		if (arrClass.length > 19) {
			this.FHoldVol = arrClass[19];
		}
		if (arrClass.length > 20) {
			this.FTodaySaleVol = arrClass[20];
		}
		if (arrClass.length > 21) {
			this.FSellFrozenMoney = arrClass[21];
		}
		if (arrClass.length > 22) {
			this.FOpenPrice = arrClass[22];
		}
		if (arrClass.length > 23) {
			this.FStatus = Integer.parseInt(arrClass[23]);
		}
	}

	public String MyPropToString() {
		String temp = "FClientNo@FExchangeNo@FCommodityNo@FDirect@FHoldPrice@FCanTradeVol"
				+ "@FTodayBuyVol@FFrozenVol@FTotalBuyMoney@FTotalSellMoney@FTotalBuyVol@FTotalSellVol"
				+ "@FOpenDate@FFlatProfit@FHkexT1@FHkexT2@FHkexT3@FUnsettleVol@FSettledVol@FHoldVol"
				+ "@FTodaySaleVol@FSellFrozenMoney@FOpenPrice@FStatus";
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

	@Override
	public String toString() {
		return "HoldResponseInfoStock{" +
				"FClientNo='" + FClientNo + '\'' +
				", FExchangeNo='" + FExchangeNo + '\'' +
				", FCommodityNo='" + FCommodityNo + '\'' +
				", FCommodityName='" + FCommodityName + '\'' +
				", FDirect='" + FDirect + '\'' +
				", FHoldPrice='" + FHoldPrice + '\'' +
				", FCanTradeVol='" + FCanTradeVol + '\'' +
				", FTodayBuyVol='" + FTodayBuyVol + '\'' +
				", FFrozenVol='" + FFrozenVol + '\'' +
				", FTotalBuyMoney='" + FTotalBuyMoney + '\'' +
				", FTotalSellMoney='" + FTotalSellMoney + '\'' +
				", FTotalBuyVol='" + FTotalBuyVol + '\'' +
				", FTotalSellVol='" + FTotalSellVol + '\'' +
				", FOpenDate='" + FOpenDate + '\'' +
				", FFlatProfit='" + FFlatProfit + '\'' +
				", FHkexT1='" + FHkexT1 + '\'' +
				", FHkexT2='" + FHkexT2 + '\'' +
				", FHkexT3='" + FHkexT3 + '\'' +
				", FUnsettleVol='" + FUnsettleVol + '\'' +
				", FSettledVol='" + FSettledVol + '\'' +
				", FHoldVol='" + FHoldVol + '\'' +
				", FTodaySaleVol='" + FTodaySaleVol + '\'' +
				", FSellFrozenMoney='" + FSellFrozenMoney + '\'' +
				", FOpenPrice='" + FOpenPrice + '\'' +
				", FStatus='" + FStatus + '\'' +
				", currPrice=" + currPrice +
				", floatProfit=" + floatProfit +
				", preFuYing=" + preFuYing +
				", mortgageMoney=" + mortgageMoney +
				", marketValues=" + marketValues +
				'}';
	}
}

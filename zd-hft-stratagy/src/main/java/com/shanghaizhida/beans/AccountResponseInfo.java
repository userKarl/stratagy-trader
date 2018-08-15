package com.shanghaizhida.beans;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * 资金查询请求返回信息的类
 * 
 * @author xiang
 * 
 */

public class AccountResponseInfo implements NetParent, Serializable {

	private static final long serialVersionUID = 3793197910495592924L;

	/** 币种中文名称 */
	public String currencyName = "";

	/** 用户ID */
	public String userId = "";

	/** 客户权益 */
	public double balance;

	/** 入金 */
	public double inMoney;

	/** 出金 */
	public double outMoney;

	/** 今可用 */
	public double todayCanUse;

	/** 今结存 */
	public double todayAmount;

	/** 今权益 */
	public double todayBalance;

	/** 冻结资金 */
	public double freezenMoney;

	/** 手续费 */
	public double commission;

	/** 保证金 */
	public double margin;

	/** 昨可用 */
	public double oldCanUse;

	/** 昨结存 */
	public double oldAmount;

	/** 昨权益 */
	public double oldBalance;

	/** 今日平盈 */
	public double floatingProfit;

	/** 币种编号 */
	public String currencyNo = "";

	/** 货币与基本的汇率 */
	public String currencyRate = "";

	/** 未到期平盈 */
	public double unexpiredProfit;

	/** 未结平盈 */
	public double unaccountProfit;

	/** 维持保证金 */
	public double keepDeposit;

	/** 期权权利金 */
	public double royalty;

	/** 信任额度 */
	public double credit;

	/** 配资资金 */
	public double FAddCapital;

	/** 初始资金 */
	public double FIniEquity;

	/** 资金帐号 1 */
	public String accountNo = "";

	/** 按揭价值 20150610 added for 港股 */
	public double FMortgageMoney;

	/** 孖展上限额度 20150727 added for 港股   与算出的margin比较，谁小选谁*/
	public double MarginLimit;

	/** 借货价值 20150727 added for 港股 */
	public double BorrowValue;

	/** T1 20160219 added for 港股 */
	public double T1;

	/** T2 20160219 added for 港股 */
	public double T2;

	/** T3 20160219 added for 港股 */
	public double T3;

	/** Tn 20160219 added for 港股 */
	public double TN;

	/** 临时交易限额 */
	public double tradeLimit;

	/** 可取资金==min(T+0,T+0+T+1,T+0+T+1+T+2)-冻结资金(挂单) */
	public double FCanCashOut;

	/** 月存款利息 2016-06-10 added for 港股 */
	public double FAccruedCrInt;

	/** 月欠款利息 2016-06-10 added for 港股 */
	public double FAccruedDrInt;

	/** 跨市场资金限额（mantis8374）*/
	public double FCrossMax;

	/** 卖空冻结资金（mantis6868）*/
	public double SellFreezenMoney;

	/** 卖空利息（mantis6868）*/
	public double SellInterest;

	/** 需补按金（mantis6868）*/
	public double SellNeedAddMargin;

	/** 净盈利 1 */
	public double netProfit;

	/** 盈利率 1 */
	public double profitRate;

	/** 风险率 1 */
	public double riskRate;

	/** 浮盈 */
	public double Profit;

	/** 盯市盈亏 */
	public double marketProfit;

	/** 盈亏 */
	public double preFuYing;

	/** 按揭价值 */
	public double mortgageMoney;

	public int status;

	/** 期权价值 */
	public double optionvalue;

	// add by ccy in 20161212   股票里用到
	/** 可取资金 */
	public double canTakeOut;
	/** 投资组合总额 */
	public double investGroupAll;
	/** 可用购买力资金 */
	public double canUse;
	/** 资产总额 */
	public double totleFund;

	@Override
	public String MyToString() {
		String temp = this.userId + "@" + this.accountNo + "@" + this.oldCanUse
				+ "@" + this.oldBalance + "@" + this.oldAmount + "@" + this.inMoney
				+ "@" + this.outMoney + "@" + this.todayCanUse + "@" + this.todayBalance
				+ "@" + this.todayAmount + "@" + this.freezenMoney + "@" + this.margin
				+ "@" + this.commission + "@" + this.floatingProfit + "@" + this.netProfit
				+ "@" + this.profitRate + "@" + this.riskRate + "@" + this.unexpiredProfit
				+ "@" + this.currencyNo + "@" + this.currencyRate + "@" + this.unaccountProfit
				+ "@" + this.keepDeposit + "@" + this.royalty + "@" + this.credit
				+ "@" + this.FAddCapital + "@" + this.FIniEquity + "@" + this.FMortgageMoney
				+ "@" + this.MarginLimit + "@" + this.BorrowValue
				+ "@" + this.T1 + "@" + this.T2 + "@" + this.T3 + "@" + this.TN
				+ "@" + this.tradeLimit + "@" + this.FCanCashOut + "@" + this.FAccruedCrInt
				+ "@" + this.FAccruedDrInt + "@" + this.FCrossMax + "@" + this.SellFreezenMoney
				+ "@" + this.SellInterest + "@" + this.SellNeedAddMargin;

		return temp;
	}

	@Override
	public void MyReadString(String temp) {
		String[] arrClass = temp.split("@");
		this.userId = arrClass[0];
		this.accountNo = arrClass[1];
		this.oldCanUse = Double.parseDouble(arrClass[2].trim().trim().equals("") ? "0" : arrClass[2]);
		this.oldBalance = Double.parseDouble(arrClass[3].trim().equals("") ? "0" : arrClass[3]);
		this.oldAmount = Double.parseDouble(arrClass[4].trim().equals("") ? "0" : arrClass[4]);
		this.inMoney = Double.parseDouble(arrClass[5].trim().equals("") ? "0" : arrClass[5]);
		this.outMoney = Double.parseDouble(arrClass[6].trim().equals("") ? "0" : arrClass[6]);
		this.todayCanUse = Double.parseDouble(arrClass[7].trim().equals("") ? "0" : arrClass[7]);
		this.todayBalance = Double.parseDouble(arrClass[8].trim().equals("") ? "0" : arrClass[8]);
		this.todayAmount = Double.parseDouble(arrClass[9].trim().equals("") ? "0" : arrClass[9]);
		this.freezenMoney = Double.parseDouble(arrClass[10].trim().equals("") ? "0" : arrClass[10]);
		this.margin = Double.parseDouble(arrClass[11].trim().equals("") ? "0" : arrClass[11]);
		this.commission = Double.parseDouble(arrClass[12].trim().equals("") ? "0" : arrClass[12]);
		this.floatingProfit = Double.parseDouble(arrClass[13].trim().equals("") ? "0" : arrClass[13]);
		this.netProfit = Double.parseDouble(arrClass[14].trim().equals("") ? "0" : arrClass[14]);
		this.profitRate = Double.parseDouble(arrClass[15].trim().equals("") ? "0" : arrClass[15]);
		this.riskRate = Double.parseDouble(arrClass[16].trim().equals("") ? "0" : arrClass[16]);

		if (arrClass.length > 17) {
			this.unexpiredProfit = Double.parseDouble(arrClass[17].trim().equals("") ? "0" : arrClass[17]);
		}
		if (arrClass.length > 18) {
			this.currencyNo = arrClass[18];
		}
		if (arrClass.length > 19) {
			this.currencyRate = arrClass[19];
		}
		if (arrClass.length > 20) {
			this.unaccountProfit = Double.parseDouble(arrClass[20].trim().equals("") ? "0" : arrClass[20]);
		}
		if (arrClass.length > 21) {
			this.keepDeposit = Double.parseDouble(arrClass[21].trim().equals("") ? "0" : arrClass[21]);
		}
		if (arrClass.length > 22) {
			this.royalty = Double.parseDouble(arrClass[22].trim().equals("") ? "0" : arrClass[22]);
		}
		if (arrClass.length > 23) {
			this.credit = Double.parseDouble(arrClass[23].trim().equals("") ? "0" : arrClass[23]);
		}
		if (arrClass.length > 24) {
			this.FAddCapital = Double.parseDouble(arrClass[24].trim().equals("") ? "0" : arrClass[24]);
		}
		if (arrClass.length > 25) {
			this.FIniEquity = Double.parseDouble(arrClass[25].trim().equals("") ? "0" : arrClass[25]);
		}
		if (arrClass.length > 26) {
			this.FMortgageMoney = Double.parseDouble(arrClass[26].trim().equals("") ? "0" : arrClass[26]);
		}
		if (arrClass.length > 27) {
			this.MarginLimit = Double.parseDouble(arrClass[27].trim().equals("") ? "0" : arrClass[27]);
		}
		if (arrClass.length > 28) {
			this.BorrowValue = Double.parseDouble(arrClass[28].trim().equals("") ? "0" : arrClass[28]);
		}
		if (arrClass.length > 29) {
			this.T1 = Double.parseDouble(arrClass[29].trim().equals("") ? "0" : arrClass[29]);
		}
		if (arrClass.length > 30) {
			this.T2 = Double.parseDouble(arrClass[30].trim().equals("") ? "0" : arrClass[30]);
		}
		if (arrClass.length > 31) {
			this.T3 = Double.parseDouble(arrClass[31].trim().equals("") ? "0" : arrClass[31]);
		}
		if (arrClass.length > 32) {
			this.TN = Double.parseDouble(arrClass[32].trim().equals("") ? "0" : arrClass[32]);
		}
		if (arrClass.length > 33) {
			this.tradeLimit = Double.parseDouble(arrClass[33].trim().equals("") ? "0" : arrClass[33]);
		}
		if (arrClass.length > 34) {
			this.FCanCashOut = Double.parseDouble(arrClass[34].trim().equals("") ? "0" : arrClass[34]);
		}
		if (arrClass.length > 35) {
			this.FAccruedCrInt = Double.parseDouble(arrClass[35].trim().equals("") ? "0" : arrClass[35]);
		}
		if (arrClass.length > 36) {
			this.FAccruedDrInt = Double.parseDouble(arrClass[36].trim().equals("") ? "0" : arrClass[36]);
		}
		if (arrClass.length > 37) {
			this.FCrossMax = Double.parseDouble(arrClass[37].trim().equals("") ? "0" : arrClass[37]);
		}
		if (arrClass.length > 38) {
			this.SellFreezenMoney = Double.parseDouble(arrClass[38].trim().equals("") ? "0" : arrClass[38]);
		}
		if (arrClass.length > 39) {
			this.SellInterest = Double.parseDouble(arrClass[39].trim().equals("") ? "0" : arrClass[39]);
		}
		if (arrClass.length > 40) {
			this.SellNeedAddMargin = Double.parseDouble(arrClass[40].trim().equals("") ? "0" : arrClass[40]);
		}
	}

	@Override
	public String MyPropToString() {
		String temp = "userId@accountNo@oldCanUse@oldBalance@oldAmount@inMoney"
				+ "@outMoney@todayCanUse@todayBalance@todayAmount@freezenMoney@margin"
				+ "@commission@floatingProfit@netProfit@profitRate@riskRate@unexpiredProfit"
				+ "@currencyNo@currencyRate@unaccountProfit@keepDeposit@royalty@credit"
				+ "@FAddCapital@FIniEquity@FMortgageMoney@MarginLimit@BorrowValue@T1@T2@T3@TN"
				+ "@tradeLimit@FCanCashOut@FAccruedCrInt@FAccruedDrInt@FCrossMax@SellFreezenMoney"
				+ "@SellInterest@SellNeedAddMargin";
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
		return "AccountResponseInfo{" +
				"currencyName='" + currencyName + '\'' +
				", userId='" + userId + '\'' +
				", balance=" + balance +
				", inMoney=" + inMoney +
				", outMoney=" + outMoney +
				", todayCanUse=" + todayCanUse +
				", todayAmount=" + todayAmount +
				", todayBalance=" + todayBalance +
				", freezenMoney=" + freezenMoney +
				", commission=" + commission +
				", margin=" + margin +
				", oldCanUse=" + oldCanUse +
				", oldAmount=" + oldAmount +
				", oldBalance=" + oldBalance +
				", floatingProfit=" + floatingProfit +
				", currencyNo='" + currencyNo + '\'' +
				", currencyRate='" + currencyRate + '\'' +
				", unexpiredProfit=" + unexpiredProfit +
				", unaccountProfit=" + unaccountProfit +
				", keepDeposit=" + keepDeposit +
				", royalty=" + royalty +
				", credit=" + credit +
				", FAddCapital=" + FAddCapital +
				", FIniEquity=" + FIniEquity +
				", accountNo='" + accountNo + '\'' +
				", FMortgageMoney=" + FMortgageMoney +
				", MarginLimit=" + MarginLimit +
				", BorrowValue=" + BorrowValue +
				", T1=" + T1 +
				", T2=" + T2 +
				", T3=" + T3 +
				", TN=" + TN +
				", tradeLimit=" + tradeLimit +
				", FCanCashOut=" + FCanCashOut +
				", FAccruedCrInt=" + FAccruedCrInt +
				", FAccruedDrInt=" + FAccruedDrInt +
				", FCrossMax=" + FCrossMax +
				", SellFreezenMoney=" + SellFreezenMoney +
				", SellInterest=" + SellInterest +
				", SellNeedAddMargin=" + SellNeedAddMargin +
				", netProfit=" + netProfit +
				", profitRate=" + profitRate +
				", riskRate=" + riskRate +
				", Profit=" + Profit +
				", marketProfit=" + marketProfit +
				", preFuYing=" + preFuYing +
				", mortgageMoney=" + mortgageMoney +
				", status=" + status +
				", canTakeOut=" + canTakeOut +
				", investGroupAll=" + investGroupAll +
				", canUse=" + canUse +
				", totleFund=" + totleFund +
				'}';
	}
}

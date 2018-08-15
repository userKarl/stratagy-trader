package com.shanghaizhida.beans;

public class AccountInfo implements NetParent {

	/** 客户号 */
	public String clientNo = "";

	/** 资金帐号 */
	public String accountNo = "";

	/** 币种 */
	public String currencyNo = "";

	/** 今可用 */
	public String available = "";

	/** 昨可用 */
	public String yAvailable = "";

	/** 今可出 */
	public String canCashOut = "";

	/** 今结存 */
	public String money = "";

	/** 平仓盈亏 */
	public String expiredProfit = "";

	/** 冻结资金 */
	public String frozenDeposit = "";

	/** 手续费 */
	public String fee = "";

	/** 保证金 */
	public String deposit = "";

	/** 维持保证金 */
	public String keepDeposit = "";

	/** 状态 */
	public int status;

	/** 入金 */
	public String inMoney = "";

	/** 出金 */
	public String outMoney = "";

	/** 未到期平盈 */
	public String unexpiredProfit = "";

	/** 今权益 */
	public String todayTotal = "";

	/** 未结平盈 */
	public String unaccountProfit = "";

	/** 期权权利金 */
	public String royalty = "";

	// 2012-11-12 add by dragon for 695客户端与交易数据一致问题 begin

	/** 交易所代码 */
	public String exchangeNo = "";

	/** 合约代码 */
	public String contractNo = "";

	/** 定单号 */
	public String orderNo = "";

	/** 委托数量 */
	public String orderNumber = "";

	/** 已成交数量 */
	public String filledNumber = "";

	/** 成交均价 */
	public String filledAdvPrice = "";

	/** 持买数量 */
	public String buyHoldNumber = "";

	/** 持买开仓均价 */
	public String buyHoldOpenPrice = "";

	/** 持买均价 */
	public String buyHoldPrice = "";

	/** 持卖数量 */
	public String saleHoldNumber = "";

	/** 持卖开仓均价 */
	public String saleHoldOpenPrice = "";

	/** 持卖均价 */
	public String saleHoldPrice = "";

	/** 是否已经撤单（0：没有；1：已撤单） */
	public String isCanceled = "";

	/** 成交总的手续费 */
	public String filledTotalFee = "";

	// 2012-11-12 add by dragon for 695客户端与交易数据一致问题 end

	/** 信任额度 */
	public String credit = "";

	/** 孖展上限额度 20150727 added for 港股 */
	public String MarginLimit = "";

	/** 借货价值 20150727 added for 港股 **/
	public String BorrowValue = "";

	/** 按揭价值 20150727 added for 港股 */
	public String FMortgageMoney = "";

	/** T1 20160219 added for 港股 */
	public String T1 = "";

	/** T2 20160219 added for 港股 */
	public String T2 = "";

	/** T3 20160219 added for 港股 */
	public String T3 = "";

	/** Tn 20160219 added for 港股 */
	public String TN = "";

	/** 临时交易限额 */
	public String tradeLimit = "";

	/** 跨市场资金限额（mantis8374）*/
	public String FCrossMax = "";

	/** 卖空冻结资金（mantis6868）*/
	public String SellFreezenMoney = "";

	/** 卖空利息（mantis6868）*/
	public String SellInterest = "";

	/** 需补按金（mantis6868）*/
	public String SellNeedAddMargin = "";

	@Override
	public String MyToString() {
		return this.accountNo + "@" + this.currencyNo + "@" + this.available + "@" + this.yAvailable
				+ "@" + this.canCashOut + "@" + this.money
				+ "@" + this.expiredProfit + "@" + this.frozenDeposit
				+ "@" + this.fee + "@" + this.deposit
				+ "@" + this.keepDeposit + "@" + this.status
				+ "@" + this.inMoney + "@" + this.outMoney
				+ "@" + this.clientNo + "@" + this.unexpiredProfit
				+ "@" + this.todayTotal + "@" + this.unaccountProfit
				+ "@" + this.royalty + "@" + this.exchangeNo
				+ "@" + this.contractNo + "@" + this.orderNo + "@" + this.orderNumber
				+ "@" + this.filledNumber + "@" + this.filledAdvPrice + "@" + this.buyHoldNumber
				+ "@" + this.buyHoldOpenPrice + "@" + this.buyHoldPrice + "@" + this.saleHoldNumber
				+ "@" + this.saleHoldOpenPrice + "@" + this.saleHoldPrice + "@" + this.isCanceled
				+ "@" + this.filledTotalFee + "@" + this.credit + "@" + this.MarginLimit
				+ "@" + this.BorrowValue + "@" + this.FMortgageMoney
				+ "@" + this.T1 + "@" + this.T2 + "@" + this.T3 + "@" + this.TN
				+ "@" + this.tradeLimit + "@" + this.FCrossMax + "@" + this.SellFreezenMoney
				+ "@" + this.SellInterest + "@" + this.SellNeedAddMargin;
	}

	@Override
	public void MyReadString(String temp) {
		String[] arrClass = temp.split("@");
		this.accountNo = arrClass[0];
		this.currencyNo = arrClass[1];
		this.available = arrClass[2];
		this.yAvailable = arrClass[3];
		this.canCashOut = arrClass[4];
		this.money = arrClass[5];
		this.expiredProfit = arrClass[6];
		this.frozenDeposit = arrClass[7];
		this.fee = arrClass[8];
		this.deposit = arrClass[9];
		this.keepDeposit = arrClass[10];
		this.status = (int) Double.parseDouble(arrClass[11].trim().trim().equals("") ? "0" : arrClass[11]);
		this.inMoney = arrClass[12];
		this.outMoney = arrClass[13];
		this.clientNo = arrClass[14];

		if (arrClass.length > 15) {
			this.unexpiredProfit = arrClass[15];
		}
		if (arrClass.length > 16) {
			this.todayTotal = arrClass[16];
		}
		if (arrClass.length > 17) {
			this.unaccountProfit = arrClass[17];
		}
		if (arrClass.length > 18) {
			this.royalty = arrClass[18];
		}
		if (arrClass.length > 19) {
			this.exchangeNo = arrClass[19];
		}
		if (arrClass.length > 20) {
			this.contractNo = arrClass[20];
		}
		if (arrClass.length > 21) {
			this.orderNo = arrClass[21];
		}
		if (arrClass.length > 22) {
			this.orderNumber = arrClass[22];
		}
		if (arrClass.length > 23) {
			this.filledNumber = arrClass[23];
		}
		if (arrClass.length > 24) {
			this.filledAdvPrice = arrClass[24];
		}
		if (arrClass.length > 25) {
			this.buyHoldNumber = arrClass[25];
		}
		if (arrClass.length > 26) {
			this.buyHoldOpenPrice = arrClass[26];
		}
		if (arrClass.length > 27) {
			this.buyHoldPrice = arrClass[27];
		}
		if (arrClass.length > 28) {
			this.saleHoldNumber = arrClass[28];
		}
		if (arrClass.length > 29) {
			this.saleHoldOpenPrice = arrClass[29];
		}
		if (arrClass.length > 30) {
			this.saleHoldPrice = arrClass[30];
		}
		if (arrClass.length > 31) {
			this.isCanceled = arrClass[31];
		}
		if (arrClass.length > 32) {
			this.filledTotalFee = arrClass[32];
		}
		if (arrClass.length > 33) {
			this.credit = arrClass[33];
		}
		if (arrClass.length > 34) {
			this.MarginLimit = arrClass[34];
		}
		if (arrClass.length > 35) {
			this.BorrowValue = arrClass[35];
		}
		if (arrClass.length > 36) {
			this.FMortgageMoney = arrClass[36];
		}
		if (arrClass.length > 37) {
			this.T1 = arrClass[37];
		}
		if (arrClass.length > 38) {
			this.T2 = arrClass[38];
		}
		if (arrClass.length > 39) {
			this.T3 = arrClass[39];
		}
		if (arrClass.length > 40) {
			this.TN = arrClass[40];
		}
		if (arrClass.length > 41) {
			this.tradeLimit = arrClass[41];
		}
		if (arrClass.length > 42) {
			this.FCrossMax = arrClass[42];
		}
		if (arrClass.length > 43) {
			this.SellFreezenMoney = arrClass[43];
		}
		if (arrClass.length > 44) {
			this.SellInterest = arrClass[44];
		}
		if (arrClass.length > 45) {
			this.SellNeedAddMargin = arrClass[45];
		}
	}

	@Override
	public String MyPropToString() {
		return "accountNo@currencyNo@available@yAvailable@canCashOut@money"
				+ "@expiredProfit@frozenDeposit@fee@deposit@keepDeposit@status@inMoney"
				+ "@outMoney@clientNo@unexpiredProfit@todayTotal@unaccountProfit@royalty@exchangeNo@contractNo"
				+ "@orderNo@orderNumber@filledNumber@filledAdvPrice@buyHoldNumber@buyHoldOpenPrice"
				+ "@buyHoldPrice@saleHoldNumber@saleHoldOpenPrice@saleHoldPrice@isCanceled@filledTotalFee@credit"
				+ "@MarginLimit@BorrowValue@FMortgageMoney@T1@T2@T3@TN@tradeLimit@FCrossMax@SellFreezenMoney"
				+ "@SellInterest@SellNeedAddMargin";
	}
}

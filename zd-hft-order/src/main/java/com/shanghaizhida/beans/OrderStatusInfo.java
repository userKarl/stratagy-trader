package com.shanghaizhida.beans;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * 定单最新状态信息类（包括成交信息，合约持仓信息）
 * 
 * @author xiang
 * 
 */

public class OrderStatusInfo implements NetParent, Serializable {

	private static final long serialVersionUID = -302535761534310107L;

	public String contractName = "";

	// 交易所代码
	public String exchangeNo = "";

	// 合约代码
	public String contractNo = "";

	// 定单号
	public String orderNo = "";

	// 委托数量
	public String orderNumber = "";

	// 已成交数量
	public String filledNumber = "";

	// 成交均价
	public String filledAdvPrice = "";

	// 持买数量
	public String buyHoldNumber = "";

	// 持买开仓均价
	public String buyHoldOpenPrice = "";

	// 持买均价
	public String buyHoldPrice = "";

	// 持卖数量
	public String saleHoldNumber = "";

	// 持卖开仓均价
	public String saleHoldOpenPrice = "";

	// 持卖均价
	public String saleHoldPrice = "";

	// 是否已经撤单（0：没有；1：已撤单）
	public String isCanceled = "";

	// 成交总的手续费
	public String filledTotalFee = "";

	// 顺序号
	public int status;

	// 资金帐号
	public String accountNo = "";

	// 持仓类型（0：今仓；1：昨仓）
	public String holdType = "";

	// 持买保证金
	public String holdMarginBuy = "";

	// 持卖保证金
	public String holdMarginSale = "";

	// 浮盈
	public double floatProfit;

	// 盯市盈亏
	public double marketProfit;

	// 现价
	public double currPrice;

	// 买卖-持仓中
	public String buySale = "";

	// 期权权利金
	public double royalty;

	// 期权价值
	public double optionValue;

	// 期权净盈亏
	public double optionProfit;

	// 浮盈百分比
	public double profitPercent;

	// 对应币种
	public String currencyNo = "";

	@Override
	public String MyToString() {
		return this.exchangeNo
				+ "@" + this.contractNo + "@" + this.orderNo + "@" + this.orderNumber
				+ "@" + this.filledNumber + "@" + this.filledAdvPrice + "@" + this.buyHoldNumber
				+ "@" + this.buyHoldOpenPrice + "@" + this.buyHoldPrice + "@" + this.saleHoldNumber
				+ "@" + this.saleHoldOpenPrice + "@" + this.saleHoldPrice + "@" + this.isCanceled
				+ "@" + this.filledTotalFee + "@" + this.status + "@" + this.accountNo + "@" + this.holdType
				+ "@" + this.holdMarginBuy + "@" + this.holdMarginSale + "@" + this.currPrice + "@" + this.floatProfit;
	}

	@Override
	public void MyReadString(String temp) {
		String[] arrClass = temp.split("@");
		if (arrClass.length > 0) {
			this.exchangeNo = arrClass[0];
		}
		if (arrClass.length > 1) {
			this.contractNo = arrClass[1];
		}
		if (arrClass.length > 2) {
			this.orderNo = arrClass[2];
		}
		if (arrClass.length > 3) {
			this.orderNumber = arrClass[3];
		}
		if (arrClass.length > 4) {
			this.filledNumber = arrClass[4];
		}
		if (arrClass.length > 5) {
			this.filledAdvPrice = arrClass[5];
		}
		if (arrClass.length > 6) {
			this.buyHoldNumber = arrClass[6];
		}
		if (arrClass.length > 7) {
			this.buyHoldOpenPrice = arrClass[7];
		}
		if (arrClass.length > 8) {
			this.buyHoldPrice = arrClass[8];
		}
		if (arrClass.length > 9) {
			this.saleHoldNumber = arrClass[9];
		}
		if (arrClass.length > 10) {
			this.saleHoldOpenPrice = arrClass[10];
		}
		if (arrClass.length > 11) {
			this.saleHoldPrice = arrClass[11];
		}
		if (arrClass.length > 12) {
			this.isCanceled = arrClass[12];
		}
		if (arrClass.length > 13) {
			this.filledTotalFee = arrClass[13];
		}
		if (arrClass.length > 14) {
			this.status = Integer.parseInt(arrClass[14]);
		}
		if (arrClass.length > 15) {
			this.accountNo = arrClass[15];
		}
		if (arrClass.length > 16) {
			this.holdType = arrClass[16];
		}
		if (arrClass.length > 17) {
			this.holdMarginBuy = arrClass[17];
		}
		if (arrClass.length > 18) {
			this.holdMarginSale = arrClass[18];
		}
		if (arrClass.length > 19) {
			this.currPrice = Double.parseDouble(arrClass[19].trim().equals("") ? "0" : arrClass[19]);
		}
		if (arrClass.length > 20) {
			this.floatProfit = Double.parseDouble(arrClass[20].trim().equals("") ? "0" : arrClass[20]);
		}
	}

	@Override
	public String MyPropToString() {
		return "exchangeNo@contractNo"
				+ "@orderNo@orderNumber@filledNumber@filledAdvPrice@buyHoldNumber@buyHoldOpenPrice"
				+ "@buyHoldPrice@saleHoldNumber@saleHoldOpenPrice@saleHoldPrice@isCanceled@filledTotalFee"
				+ "@status@accountNo@holdType@holdMarginBuy@holdMarginSale@currPrice@floatProfit";
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
		return "OrderStatusInfo{" +
				"contractName='" + contractName + '\'' +
				", exchangeNo='" + exchangeNo + '\'' +
				", contractNo='" + contractNo + '\'' +
				", orderNo='" + orderNo + '\'' +
				", orderNumber='" + orderNumber + '\'' +
				", filledNumber='" + filledNumber + '\'' +
				", filledAdvPrice='" + filledAdvPrice + '\'' +
				", buyHoldNumber='" + buyHoldNumber + '\'' +
				", buyHoldOpenPrice='" + buyHoldOpenPrice + '\'' +
				", buyHoldPrice='" + buyHoldPrice + '\'' +
				", saleHoldNumber='" + saleHoldNumber + '\'' +
				", saleHoldOpenPrice='" + saleHoldOpenPrice + '\'' +
				", saleHoldPrice='" + saleHoldPrice + '\'' +
				", isCanceled='" + isCanceled + '\'' +
				", filledTotalFee='" + filledTotalFee + '\'' +
				", status=" + status +
				", accountNo='" + accountNo + '\'' +
				", holdType='" + holdType + '\'' +
				", holdMarginBuy='" + holdMarginBuy + '\'' +
				", holdMarginSale='" + holdMarginSale + '\'' +
				", floatProfit=" + floatProfit +
				", marketProfit=" + marketProfit +
				", currPrice=" + currPrice +
				", buySale='" + buySale + '\'' +
				'}';
	}
}

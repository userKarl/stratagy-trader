package com.shanghaizhida.beans;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import lombok.Data;

@Data
public class MarketInfo implements NetParent, Serializable {

	private static final long serialVersionUID = -6512340199930433591L;

	// 交易所代码
	public String exchangeCode = "";

	// 合约代码
	public String code = "";

	// 买价1
	public String buyPrice = "";

	// 买量1
	public String buyNumber = "";

	// 卖价1
	public String salePrice = "";

	// 卖量1
	public String saleNumber = "";

	// 最新价
	public String currPrice = "";

	// 现量
	public String currNumber = "";

	// 当天最高价
	public String high = "";

	// 当天最低价
	public String low = "";

	// 开盘价
	public String open = "";

	// 昨结算
	public String oldClose = "";

	// 当天结算价
	//public String close = "";

	// 行情时间
	public String time = "";

	// 成交量
	public String filledNum = "";

	// 持仓量
	public String holdNum = "";

	// 买价2
	public String buyPrice2 = "";

	// 买价3
	public String buyPrice3 = "";

	// 买价4
	public String buyPrice4 = "";

	// 买价5
	public String buyPrice5 = "";

	// 买量2
	public String buyNumber2 = "";

	// 买量3
	public String buyNumber3 = "";

	// 买量4
	public String buyNumber4 = "";

	// 买量5
	public String buyNumber5 = "";

	// 卖价2
	public String salePrice2 = "";

	// 卖价3
	public String salePrice3 = "";

	// 卖价4
	public String salePrice4 = "";

	// 卖价5
	public String salePrice5 = "";

	// 卖量2
	public String saleNumber2 = "";

	// 卖量3
	public String saleNumber3 = "";

	// 卖量4
	public String saleNumber4 = "";

	// 卖量5
	public String saleNumber5 = "";

	// 20171222 add by liwei begin
	// 行情来源（55 美股，50 港股，60 韩股，1 10 15 20 期货）
	public String dataSource = "";

	// 行情类型（Z Y 2）
	public String type = "";

	// 股票停牌flag
	public String stockSuspensionFlag = "";
	// 20171222 add by liwei end

	// ----------期货差异字段
	// 隐藏买价
	public String hideBuyPrice = "";

	// 隐藏买量
	public String hideBuyNumber = "";

	// 隐藏卖价
	public String hideSalePrice = "";

	// 隐藏卖量
	public String hideSaleNumber = "";

	// 行情区分
	//public String type = "";

	// 跌停价
	public String limitDownPrice = "";

	// 涨停价
	public String limitUpPrice = "";

	// 交易日
	//public String tradeDay = "";

	// ----------港股差异字段
	// 港股按盘价     对应期货的hideBuyPrice
	public String nominalPrice = "";

	// 港股成交数据的flag
	public String HKDealFlag = "";

	// 买价和买量6-10
	public String buyPrice6 = "";
	public String buyPrice7 = "";
	public String buyPrice8 = "";
	public String buyPrice9 = "";
	public String buyPrice10 = "";
	public String buyNumber6 = "";
	public String buyNumber7 = "";
	public String buyNumber8 = "";
	public String buyNumber9 = "";
	public String buyNumber10 = "";

	// 卖价和卖量6-10
	public String salePrice6 = "";
	public String salePrice7 = "";
	public String salePrice8 = "";
	public String salePrice9 = "";
	public String salePrice10 = "";
	public String saleNumber6 = "";
	public String saleNumber7 = "";
	public String saleNumber8 = "";
	public String saleNumber9 = "";
	public String saleNumber10 = "";

	// Add by chen on 20161208-----begin
	// 港交所股票行情 成交类型
	//public String tradeFlag = "";

	// 交易所数据时间戳
	//public String dataTimestamp = "";

	// 数据来源 考虑到不同交易所可能有不同数据时间戳格式，可以用该字段确定数据来源
	//public String dataSourceId = "";

	// 可卖空股数（美股行情用 mantis6868）
	//public String canSellVol = "";
	// Add by chen on 20161208-----end

	@Override
	public String MyToString() {
		return null;
	}

	private int hideBidPrxLvl;
	private int hideBidVolLvl;
	private int hideOfferPrxLvl;
	private int hideOfferVolLvl;

	public String getExchangeCode() {
		return exchangeCode;
	}

	public void setExchangeCode(String exchangeCode) {
		this.exchangeCode = exchangeCode;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getCurrPrice() {
		return currPrice;
	}

	public void setCurrPrice(String currPrice) {
		this.currPrice = currPrice;
	}

	public String getOldClose() {
		return oldClose;
	}

	public void setOldClose(String oldClose) {
		this.oldClose = oldClose;
	}

	public int getHideBidPrxLvl() {
		return hideBidPrxLvl;
	}

	public void setHideBidPrxLvl(int hideBidPrxLvl) {
		this.hideBidPrxLvl = hideBidPrxLvl;
	}

	public int getHideBidVolLvl() {
		return hideBidVolLvl;
	}

	public void setHideBidVolLvl(int hideBidVolLvl) {
		this.hideBidVolLvl = hideBidVolLvl;
	}

	public int getHideOfferPrxLvl() {
		return hideOfferPrxLvl;
	}

	public void setHideOfferPrxLvl(int hideOfferPrxLvl) {
		this.hideOfferPrxLvl = hideOfferPrxLvl;
	}

	public int getHideOfferVolLvl() {
		return hideOfferVolLvl;
	}

	public void setHideOfferVolLvl(int hideOfferVolLvl) {
		this.hideOfferVolLvl = hideOfferVolLvl;
	}

	@Override
	public void MyReadString(String temp) {

		char lastChar = temp.charAt(temp.length() - 1);
		temp = temp.substring(temp.indexOf("&") + 1, temp.length());
		String[] strArr = temp.split("@");
		if (lastChar == 'Y') {
			this.exchangeCode = strArr[0].trim();
			this.code = strArr[1].trim();
			this.currPrice = strArr[2].trim();
			this.currNumber = strArr[3].trim();
			this.filledNum = strArr[4].trim();
			this.time = strArr[5].trim();
			this.type = "Y";
/*
			// Add by chen on 20161208
			if(strArr.length >= 10){
				this.tradeFlag = strArr[6];
				this.dataTimestamp = strArr[7];
				this.dataSourceId = strArr[8];
				// add by 龙任刚 on 20170621 mantis6868
				if (strArr.length > 10) {
					this.canSellVol = strArr[9];
				}
			}
*/
		}
		// Bid book
		else if (lastChar == 'B') {
			this.exchangeCode = strArr[0].trim();
			this.code = strArr[1].trim();
			this.buyPrice = strArr[2].trim();
			this.buyNumber = strArr[3].trim();
			this.buyPrice2 = strArr[4].trim();
			this.buyNumber2 = strArr[5].trim();
			this.buyPrice3 = strArr[6].trim();
			this.buyNumber3 = strArr[7].trim();
			this.buyPrice4 = strArr[8].trim();
			this.buyNumber4 = strArr[9].trim();
			this.buyPrice5 = strArr[10].trim();
			this.buyNumber5 = strArr[11].trim();
			this.hideBuyPrice = strArr[12].trim();
			this.hideBuyNumber = strArr[13].trim();
			this.type = strArr[14].trim();
		}
		// Offer book
		else if (lastChar == 'O') {
			this.exchangeCode = strArr[0].trim();
			this.code = strArr[1].trim();
			this.salePrice = strArr[2].trim();
			this.saleNumber = strArr[3].trim();
			this.salePrice2 = strArr[4].trim();
			this.saleNumber2 = strArr[5].trim();
			this.salePrice3 = strArr[6].trim();
			this.saleNumber3 = strArr[7].trim();
			this.salePrice4 = strArr[8].trim();
			this.saleNumber4 = strArr[9].trim();
			this.salePrice5 = strArr[10].trim();
			this.saleNumber5 = strArr[11].trim();
			this.hideSalePrice = strArr[12].trim();
			this.hideSaleNumber = strArr[13].trim();
			this.type = strArr[14].trim();
		} else if (lastChar == 'M') { // Add by chen on 20161208
			this.exchangeCode = strArr[0].trim();
			this.code = strArr[1].trim();
			this.limitUpPrice = strArr[2].trim();
			this.limitDownPrice = strArr[3].trim();
			this.type = strArr[4].trim();
		} else {
			String[] arrClass = temp.split("@");
			this.exchangeCode = arrClass[0];
			this.code = arrClass[1].trim();
			this.buyPrice = arrClass[2].trim();
			this.buyNumber = arrClass[3].trim();
			this.salePrice = arrClass[4].trim();
			this.saleNumber = arrClass[5].trim();
			this.currPrice = arrClass[6].trim();
			this.currNumber = arrClass[7].trim();
			this.high = arrClass[8].trim();
			this.low = arrClass[9].trim();
			this.open = arrClass[10].trim();
			this.oldClose = arrClass[11].trim();
			//this.close = arrClass[12].trim();

			String[] otherPrx = arrClass[12].split(",");
			int idx = 1;
			if (otherPrx.length > 20) {
				//this.close = otherPrx[0];
				buyPrice6 = otherPrx[idx++];
				buyNumber6 = otherPrx[idx++];
				salePrice6 = otherPrx[idx++];
				saleNumber6 = otherPrx[idx++];

				buyPrice7 = otherPrx[idx++];
				buyNumber7 = otherPrx[idx++];
				salePrice7 = otherPrx[idx++];
				saleNumber7 = otherPrx[idx++];

				buyPrice8 = otherPrx[idx++];
				buyNumber8 = otherPrx[idx++];
				salePrice8 = otherPrx[idx++];
				saleNumber8 = otherPrx[idx++];

				buyPrice9 = otherPrx[idx++];
				buyNumber9 = otherPrx[idx++];
				salePrice9 = otherPrx[idx++];
				saleNumber9 = otherPrx[idx++];

				buyPrice10 = otherPrx[idx++];
				buyNumber10 = otherPrx[idx++];
				salePrice10 = otherPrx[idx++];
				saleNumber10 = otherPrx[idx++];
			}

			this.time = arrClass[13].trim();
			this.filledNum = arrClass[14].trim();
			this.holdNum = arrClass[15].trim();
			this.buyPrice2 = arrClass[16].trim();
			this.buyPrice3 = arrClass[17].trim();
			this.buyPrice4 = arrClass[18].trim();
			this.buyPrice5 = arrClass[19].trim();
			this.buyNumber2 = arrClass[20].trim();
			this.buyNumber3 = arrClass[21].trim();
			this.buyNumber4 = arrClass[22].trim();
			this.buyNumber5 = arrClass[23].trim();
			this.salePrice2 = arrClass[24].trim();
			this.salePrice3 = arrClass[25].trim();
			this.salePrice4 = arrClass[26].trim();
			this.salePrice5 = arrClass[27].trim();
			this.saleNumber2 = arrClass[28].trim();
			this.saleNumber3 = arrClass[29].trim();
			this.saleNumber4 = arrClass[30].trim();
			this.saleNumber5 = arrClass[31].trim();

			// 行情变化区分 20120830 add by dingting end
			// 20120925 add by dragon start
			if (arrClass.length > 32) {
				this.hideBuyPrice = arrClass[32].trim();
				// 港股按盘价
				this.nominalPrice = arrClass[32].trim();
			}
			if (arrClass.length > 33) {
				this.hideBuyNumber = arrClass[33].trim();
			}
			if (arrClass.length > 34) {
				this.hideSalePrice = arrClass[34].trim();
				// 股票停牌flag
				this.stockSuspensionFlag = arrClass[34].trim();
			}
			if (arrClass.length > 35) {
				this.hideSaleNumber = arrClass[35].trim();
			}
			// 20120925 add by dragon end

			// 港股成交数据的flag
			if (arrClass.length > 36) {
				this.HKDealFlag = arrClass[36].trim();
			}
			// 数据源（55 美股，50 港股，60 韩股，1 10 15 20 期货）
			if (arrClass.length > 38) {
				this.dataSource = arrClass[38].trim();
			}
			// 昨收盘价
			if (arrClass.length > 40) {
				this.oldClose = arrClass[40].trim();
			}

			// 行情变化区分 20120830 add by dingting start
			/*if (arrClass.length > 36) {
				this.type = arrClass[36].trim();
			}*/
			// add by chen on 20161208
			this.type = String.valueOf(lastChar);
/*
			if (arrClass.length >= 40) {
				this.tradeFlag = arrClass[36];
				this.dataTimestamp = arrClass[37];
				this.dataSourceId = arrClass[38];
				// add by 龙任刚 on 20170621 mantis6868
				if (arrClass.length > 40) {
					this.canSellVol = arrClass[39];
				}
			}
*/
		}

	}

	@Override
	public String MyPropToString() {
		return null;
	}

	public static String getContractCd(String msg) {

		// "...&CME@ES1312@..."

		int andIdx = msg.indexOf("&");
		int firstAtIdx = msg.indexOf("@", andIdx);
		if (firstAtIdx == -1)
			return null;

		int secondAtIdx = msg.indexOf("@", firstAtIdx + 1);
		if (secondAtIdx == -1)
			return null;

		return msg.substring(firstAtIdx + 1, secondAtIdx);
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

	public void mergeHidePrice(String prefix) {
		if (type.equals("B")) {
			mergeHideBidPrice(this, prefix);
		} else if (type.equals("O")) {
			mergeHideOfferPrice(this, prefix);
		} else if (type.equals("Z")) {
			mergeHideBidPrice(this, prefix);
			mergeHideOfferPrice(this, prefix);
		}
	}

	/**如果有隐藏买价的话，做处理*/
	public void mergeHideBidPrice(MarketInfo obj, String prefix) {
		obj.hideBidPrxLvl = -1;
		obj.hideBidVolLvl = -1;

		float bidHidePrx = 0;
		bidHidePrx = FloatTryParse(hideBuyPrice);
		if (bidHidePrx != 0) {

			float bidPrx0 = 0;
			float bidPrx1 = 0;
			float bidPrx2 = 0;
			float bidPrx3 = 0;
			float bidPrx4 = 0;

			bidPrx0 = FloatTryParse(buyPrice);
			bidPrx1 = FloatTryParse(buyPrice2);
			bidPrx2 = FloatTryParse(buyPrice3);
			bidPrx3 = FloatTryParse(buyPrice4);
			bidPrx4 = FloatTryParse(buyPrice5);

			int buyHideVol = 0;
			buyHideVol = IntTryParse(hideBuyNumber);

			if (bidHidePrx > bidPrx0) {
				obj.hideBidPrxLvl = 0;
				obj.hideBidVolLvl = 0;

				obj.buyPrice5 = buyPrice4;
				obj.buyNumber5 = buyNumber4;
				obj.buyPrice4 = buyPrice3;
				obj.buyNumber4 = buyNumber3;
				obj.buyPrice3 = buyPrice2;
				obj.buyNumber3 = buyNumber2;
				obj.buyPrice2 = buyPrice;
				obj.buyNumber2 = buyNumber;

				obj.buyPrice = prefix + hideBuyPrice;
				obj.buyNumber = prefix + hideBuyNumber;
			} else if (bidHidePrx == bidPrx0) {
				obj.hideBidVolLvl = 0;
				obj.buyNumber = (IntTryParse(buyNumber) + buyHideVol) + "";

				obj.buyNumber = prefix + obj.buyNumber;

				if (obj != this) {
					obj.buyPrice5 = buyPrice5;
					obj.buyNumber5 = buyNumber5;
					obj.buyPrice4 = buyPrice4;
					obj.buyNumber4 = buyNumber4;
					obj.buyPrice3 = buyPrice3;
					obj.buyNumber3 = buyNumber3;
					obj.buyPrice2 = buyPrice2;
					obj.buyNumber2 = buyNumber2;
					obj.buyPrice = buyPrice;
					// ...
				}
			} else if (bidHidePrx > bidPrx1 && bidHidePrx < bidPrx0) {
				obj.hideBidPrxLvl = 1;
				obj.hideBidVolLvl = 1;

				obj.buyPrice5 = buyPrice4;
				obj.buyNumber5 = buyNumber4;
				obj.buyPrice4 = buyPrice3;
				obj.buyNumber4 = buyNumber3;
				obj.buyPrice3 = buyPrice2;
				obj.buyNumber3 = buyNumber2;

				obj.buyPrice2 = prefix + hideBuyPrice;
				obj.buyNumber2 = prefix + hideBuyNumber;

				if (obj != this) {
					obj.buyPrice = buyPrice;
					obj.buyNumber = buyNumber;
				}

			} else if (bidHidePrx == bidPrx1) {
				obj.hideBidVolLvl = 1;
				obj.buyNumber2 = (IntTryParse(buyNumber2) + buyHideVol) + "";

				obj.buyNumber2 = prefix + buyNumber2;

				if (obj != this) {
					obj.buyPrice5 = buyPrice5;
					obj.buyNumber5 = buyNumber5;
					obj.buyPrice4 = buyPrice4;
					obj.buyNumber4 = buyNumber4;
					obj.buyPrice3 = buyPrice3;
					obj.buyNumber3 = buyNumber3;
					obj.buyPrice2 = buyPrice2;
					// ...
					obj.buyPrice = buyPrice;
					obj.buyNumber = buyNumber;
				}
			} else if (bidHidePrx > bidPrx2 && bidHidePrx < bidPrx1) {
				obj.hideBidPrxLvl = 2;
				obj.hideBidVolLvl = 2;

				obj.buyPrice5 = buyPrice4;
				obj.buyNumber5 = buyNumber4;
				obj.buyPrice4 = buyPrice3;
				obj.buyNumber4 = buyNumber3;

				obj.buyPrice3 = prefix + hideBuyPrice;
				obj.buyNumber3 = prefix + hideBuyNumber;

				if (obj != this) {
					obj.buyPrice2 = buyPrice2;
					obj.buyNumber2 = buyNumber2;
					obj.buyPrice = buyPrice;
					obj.buyNumber = buyNumber;
				}

			} else if (bidHidePrx == bidPrx2) {
				obj.hideBidVolLvl = 2;
				obj.buyNumber3 = (IntTryParse(buyNumber3) + buyHideVol) + "";

				obj.buyNumber3 = prefix + buyNumber3;

				if (obj != this) {
					obj.buyPrice5 = buyPrice5;
					obj.buyNumber5 = buyNumber5;
					obj.buyPrice4 = buyPrice4;
					obj.buyNumber4 = buyNumber4;
					obj.buyPrice3 = buyPrice3;
					// ...
					obj.buyPrice2 = buyPrice2;
					obj.buyNumber2 = buyNumber2;
					obj.buyPrice = buyPrice;
					obj.buyNumber = buyNumber;
				}
			} else if (bidHidePrx > bidPrx3 && bidHidePrx < bidPrx2) {
				obj.hideBidPrxLvl = 3;
				obj.hideBidVolLvl = 3;

				obj.buyPrice5 = buyPrice4;
				obj.buyNumber5 = buyNumber4;

				obj.buyPrice4 = prefix + hideBuyPrice;
				obj.buyNumber4 = prefix + hideBuyNumber;

				if (obj != this) {
					obj.buyPrice3 = buyPrice3;
					obj.buyNumber3 = buyNumber3;
					obj.buyPrice2 = buyPrice2;
					obj.buyNumber2 = buyNumber2;
					obj.buyPrice = buyPrice;
					obj.buyNumber = buyNumber;
				}

			} else if (bidHidePrx == bidPrx3) {
				obj.hideBidVolLvl = 3;
				obj.buyNumber4 = (IntTryParse(buyNumber4) + buyHideVol) + "";

				obj.buyNumber4 = prefix + buyNumber4;

				if (obj != this) {
					obj.buyPrice5 = buyPrice5;
					obj.buyNumber5 = buyNumber5;
					obj.buyPrice4 = buyPrice4;
					// ...
					obj.buyPrice3 = buyPrice3;
					obj.buyNumber3 = buyNumber3;
					obj.buyPrice2 = buyPrice2;
					obj.buyNumber2 = buyNumber2;
					obj.buyPrice = buyPrice;
					obj.buyNumber = buyNumber;
				}
			} else if (bidHidePrx > bidPrx4 && bidHidePrx < bidPrx3) {
				obj.hideBidPrxLvl = 4;
				obj.hideBidVolLvl = 4;

				obj.buyPrice5 = prefix + hideBuyPrice;
				obj.buyNumber5 = prefix + hideBuyNumber;

				if (obj != this) {
					obj.buyPrice4 = buyPrice4;
					obj.buyNumber4 = buyNumber4;
					obj.buyPrice3 = buyPrice3;
					obj.buyNumber3 = buyNumber3;
					obj.buyPrice2 = buyPrice2;
					obj.buyNumber2 = buyNumber2;
					obj.buyPrice = buyPrice;
					obj.buyNumber = buyNumber;
				}

			} else if (bidHidePrx == bidPrx4) {
				obj.hideBidVolLvl = 4;
				obj.buyNumber5 = (IntTryParse(buyNumber5) + buyHideVol) + "";
				obj.buyNumber5 = prefix + buyNumber5;

				if (obj != this) {
					obj.buyPrice5 = buyPrice5;
					// ...
					obj.buyPrice4 = buyPrice4;
					obj.buyNumber4 = buyNumber4;
					obj.buyPrice3 = buyPrice3;
					obj.buyNumber3 = buyNumber3;
					obj.buyPrice2 = buyPrice2;
					obj.buyNumber2 = buyNumber2;
					obj.buyPrice = buyPrice;
					obj.buyNumber = buyNumber;
				}
			}
		}
	}

	/**对隐藏卖价做处理*/
	public void mergeHideOfferPrice(MarketInfo obj, String prefix) {
		obj.hideOfferPrxLvl = -1;
		obj.hideOfferVolLvl = -1;

		float offerHidePrx = 0;
		offerHidePrx = FloatTryParse(hideSalePrice);
		if (offerHidePrx != 0) {

			float offerPrx0 = Float.MAX_VALUE;
			float offerPrx1 = Float.MAX_VALUE;
			float offerPrx2 = Float.MAX_VALUE;
			float offerPrx3 = Float.MAX_VALUE;
			float offerPrx4 = Float.MAX_VALUE;

			offerPrx0 = FloatTryParse(salePrice);
			offerPrx1 = FloatTryParse(salePrice2);
			offerPrx2 = FloatTryParse(salePrice3);
			offerPrx3 = FloatTryParse(salePrice4);
			offerPrx4 = FloatTryParse(salePrice5);

			int offerHideVol = 0;
			offerHideVol = IntTryParse(hideSaleNumber);

			if (offerHidePrx < offerPrx0 || IntTryParse(saleNumber) == 0) {
				obj.hideOfferPrxLvl = 0;
				obj.hideOfferVolLvl = 0;

				obj.salePrice5 = salePrice4;
				obj.saleNumber5 = saleNumber4;
				obj.salePrice4 = salePrice3;
				obj.saleNumber4 = saleNumber3;
				obj.salePrice3 = salePrice2;
				obj.saleNumber3 = saleNumber2;
				obj.salePrice2 = salePrice;
				obj.saleNumber2 = saleNumber;

				obj.salePrice = prefix + hideSalePrice;
				obj.saleNumber = prefix + hideSaleNumber;

			} else if (offerHidePrx == offerPrx0) {
				obj.hideOfferVolLvl = 0;
				obj.saleNumber = (IntTryParse(saleNumber) + offerHideVol) + "";
				obj.saleNumber = prefix + obj.saleNumber;

				if (obj != this) {
					obj.salePrice5 = salePrice5;
					obj.saleNumber5 = saleNumber5;
					obj.salePrice4 = salePrice4;
					obj.saleNumber4 = saleNumber4;
					obj.salePrice3 = salePrice3;
					obj.saleNumber3 = saleNumber3;
					obj.salePrice2 = salePrice2;
					obj.saleNumber2 = saleNumber2;
					obj.salePrice = salePrice;
					// ...
				}
			} else if ((offerHidePrx < offerPrx1 && offerHidePrx > offerPrx0)
					|| IntTryParse(saleNumber2) == 0) {
				obj.hideOfferPrxLvl = 1;
				obj.hideOfferVolLvl = 1;

				obj.salePrice5 = salePrice4;
				obj.saleNumber5 = saleNumber4;
				obj.salePrice4 = salePrice3;
				obj.saleNumber4 = saleNumber3;
				obj.salePrice3 = salePrice2;
				obj.saleNumber3 = saleNumber2;

				obj.salePrice2 = prefix + hideSalePrice;
				obj.saleNumber2 = prefix + hideSaleNumber;

				if (obj != this) {
					obj.salePrice = salePrice;
					obj.saleNumber = saleNumber;
				}

			} else if (offerHidePrx == offerPrx1) {
				obj.hideOfferVolLvl = 1;
				obj.saleNumber2 = (IntTryParse(saleNumber2) + offerHideVol)
						+ "";

				obj.saleNumber2 = prefix + saleNumber2;

				if (obj != this) {
					obj.salePrice5 = salePrice5;
					obj.saleNumber5 = saleNumber5;
					obj.salePrice4 = salePrice4;
					obj.saleNumber4 = saleNumber4;
					obj.salePrice3 = salePrice3;
					obj.saleNumber3 = saleNumber3;
					obj.salePrice2 = salePrice2;
					// ...
					obj.salePrice = salePrice;
					obj.saleNumber = saleNumber;
				}
			} else if ((offerHidePrx < offerPrx2 && offerHidePrx > offerPrx1)
					|| IntTryParse(saleNumber3) == 0) {
				obj.hideOfferPrxLvl = 2;
				obj.hideOfferVolLvl = 2;

				obj.salePrice5 = salePrice4;
				obj.saleNumber5 = saleNumber4;
				obj.salePrice4 = salePrice3;
				obj.saleNumber4 = saleNumber3;

				obj.salePrice3 = prefix + hideSalePrice;
				obj.saleNumber3 = prefix + hideSaleNumber;

				if (obj != this) {
					obj.salePrice2 = salePrice2;
					obj.saleNumber2 = saleNumber2;
					obj.salePrice = salePrice;
					obj.saleNumber = saleNumber;
				}

			} else if (offerHidePrx == offerPrx2) {
				obj.hideOfferVolLvl = 2;
				obj.saleNumber3 = (IntTryParse(saleNumber3) + offerHideVol)
						+ "";
				obj.saleNumber3 = prefix + saleNumber3;

				if (obj != this) {
					obj.salePrice5 = salePrice5;
					obj.saleNumber5 = saleNumber5;
					obj.salePrice4 = salePrice4;
					obj.saleNumber4 = saleNumber4;
					obj.salePrice3 = salePrice3;
					// ...
					obj.salePrice2 = salePrice2;
					obj.saleNumber2 = saleNumber2;
					obj.salePrice = salePrice;
					obj.saleNumber = saleNumber;
				}
			} else if ((offerHidePrx < offerPrx3 && offerHidePrx > offerPrx2)
					|| IntTryParse(saleNumber4) == 0) {
				obj.hideOfferPrxLvl = 3;
				obj.hideOfferVolLvl = 3;

				obj.salePrice5 = salePrice4;
				obj.saleNumber5 = saleNumber4;

				obj.salePrice4 = prefix + hideSalePrice;
				obj.saleNumber4 = prefix + hideSaleNumber;

				if (obj != this) {
					obj.salePrice3 = salePrice3;
					obj.saleNumber3 = saleNumber3;
					obj.salePrice2 = salePrice2;
					obj.saleNumber2 = saleNumber2;
					obj.salePrice = salePrice;
					obj.saleNumber = saleNumber;
				}
			} else if (offerHidePrx == offerPrx3) {
				obj.hideOfferVolLvl = 3;
				obj.saleNumber4 = (IntTryParse(saleNumber4) + offerHideVol)
						+ "";
				obj.saleNumber4 = prefix + saleNumber4;

				if (obj != this) {
					obj.salePrice5 = salePrice5;
					obj.saleNumber5 = saleNumber5;
					obj.salePrice4 = salePrice4;
					// ...
					obj.salePrice3 = salePrice3;
					obj.saleNumber3 = saleNumber3;
					obj.salePrice2 = salePrice2;
					obj.saleNumber2 = saleNumber2;
					obj.salePrice = salePrice;
					obj.saleNumber = saleNumber;
				}
			} else if ((offerHidePrx < offerPrx4 && offerHidePrx > offerPrx3)
					|| IntTryParse(saleNumber5) == 0) {
				obj.hideOfferPrxLvl = 4;
				obj.hideOfferVolLvl = 4;

				obj.salePrice5 = prefix + hideSalePrice;
				obj.saleNumber5 = prefix + hideSaleNumber;

				if (obj != this) {
					obj.salePrice4 = salePrice4;
					obj.saleNumber4 = saleNumber4;
					obj.salePrice3 = salePrice3;
					obj.saleNumber3 = saleNumber3;
					obj.salePrice2 = salePrice2;
					obj.saleNumber2 = saleNumber2;
					obj.salePrice = salePrice;
					obj.saleNumber = saleNumber;
				}

			} else if (offerHidePrx == offerPrx4) {
				obj.hideOfferVolLvl = 4;
				obj.saleNumber5 = (IntTryParse(saleNumber5) + offerHideVol)
						+ "";
				obj.saleNumber5 = prefix + saleNumber5;

				if (obj != this) {
					obj.salePrice5 = salePrice5;
					obj.salePrice4 = salePrice4;
					obj.saleNumber4 = saleNumber4;
					obj.salePrice3 = salePrice3;
					obj.saleNumber3 = saleNumber3;
					obj.salePrice2 = salePrice2;
					obj.saleNumber2 = saleNumber2;
					obj.salePrice = salePrice;
					obj.saleNumber = saleNumber;
				}
			}
		}
	}

	public Float FloatTryParse(String str) {
		if (!str.equals(""))
			return Float.parseFloat(str);
		else
			return 0f;
	}

	public int IntTryParse(String str) {
		if (!str.equals(""))
			return Integer.parseInt(str);
		else
			return 0;
	}

	@Override
	public String toString() {
		return "MarketInfo{" +
				"exchangeCode='" + exchangeCode + '\'' +
				", code='" + code + '\'' +
				", buyPrice='" + buyPrice + '\'' +
				", buyNumber='" + buyNumber + '\'' +
				", salePrice='" + salePrice + '\'' +
				", saleNumber='" + saleNumber + '\'' +
				", currPrice='" + currPrice + '\'' +
				", currNumber='" + currNumber + '\'' +
				", high='" + high + '\'' +
				", low='" + low + '\'' +
				", open='" + open + '\'' +
				", oldClose='" + oldClose + '\'' +
//				", close='" + close + '\'' +
				", time='" + time + '\'' +
				", filledNum='" + filledNum + '\'' +
				", holdNum='" + holdNum + '\'' +
				", buyPrice2='" + buyPrice2 + '\'' +
				", buyPrice3='" + buyPrice3 + '\'' +
				", buyPrice4='" + buyPrice4 + '\'' +
				", buyPrice5='" + buyPrice5 + '\'' +
				", buyNumber2='" + buyNumber2 + '\'' +
				", buyNumber3='" + buyNumber3 + '\'' +
				", buyNumber4='" + buyNumber4 + '\'' +
				", buyNumber5='" + buyNumber5 + '\'' +
				", salePrice2='" + salePrice2 + '\'' +
				", salePrice3='" + salePrice3 + '\'' +
				", salePrice4='" + salePrice4 + '\'' +
				", salePrice5='" + salePrice5 + '\'' +
				", saleNumber2='" + saleNumber2 + '\'' +
				", saleNumber3='" + saleNumber3 + '\'' +
				", saleNumber4='" + saleNumber4 + '\'' +
				", saleNumber5='" + saleNumber5 + '\'' +
				", hideBuyPrice='" + hideBuyPrice + '\'' +
				", hideBuyNumber='" + hideBuyNumber + '\'' +
				", hideSalePrice='" + hideSalePrice + '\'' +
				", hideSaleNumber='" + hideSaleNumber + '\'' +
				", type='" + type + '\'' +
//				", tradeFlag='" + tradeFlag + '\'' +
//				", dataTimestamp='" + dataTimestamp + '\'' +
				", dataSource='" + dataSource + '\'' +
				", limitUpPrice='" + limitUpPrice + '\'' +
				", limitDownPrice='" + limitDownPrice + '\'' +
				", hideBidPrxLvl=" + hideBidPrxLvl +
				", hideBidVolLvl=" + hideBidVolLvl +
				", hideOfferPrxLvl=" + hideOfferPrxLvl +
				", hideOfferVolLvl=" + hideOfferVolLvl +
				'}';
	}
}

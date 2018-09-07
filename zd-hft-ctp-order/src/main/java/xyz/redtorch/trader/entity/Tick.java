package xyz.redtorch.trader.entity;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;

import com.google.common.collect.Lists;

import xyz.redtorch.trader.base.RtConstant;


/**
 * @author sun0x00@gmail.com
 */
public class Tick implements Serializable {

	private static final long serialVersionUID = -2066668386737336931L;

	private String gatewayID; // 接口

	// 代码相关
	private String symbol = ""; // 代码
	private String exchange = ""; // 交易所代码
	private String rtSymbol = ""; // 系统中的唯一代码,通常是 合约代码.交易所代码

	private String tradingDay = ""; // 交易日
	private String actionDay = ""; // 业务发生日
	private String actionTime = ""; // 时间(HHMMSSmmm)
	private DateTime dateTime;

	private Integer status; // 状态

	// 成交数据
	private Double lastPrice = 0d; // 最新成交价
	private Integer lastVolume = 0; // 最新成交量
	private Integer volume = 0; // 今天总成交量
	private Double openInterest = 0d; // 持仓量

	private Long preOpenInterest = 0L;// 昨持仓
	private Double preClosePrice = 0d; // 前收盘价
	private Double preSettlePrice = 0d; // 昨结算

	// 常规行情
	private Double openPrice = 0d; // 今日开盘价
	private Double highPrice = 0d; // 今日最高价
	private Double lowPrice = 0d; // 今日最低价

	private Double upperLimit = 0d; // 涨停价
	private Double lowerLimit = 0d; // 跌停价

	private Double bidPrice1 = 0d;
	private Double bidPrice2 = 0d;
	private Double bidPrice3 = 0d;
	private Double bidPrice4 = 0d;
	private Double bidPrice5 = 0d;
	private Double bidPrice6 = 0d;
	private Double bidPrice7 = 0d;
	private Double bidPrice8 = 0d;
	private Double bidPrice9 = 0d;
	private Double bidPrice10 = 0d;

	private Double askPrice1 = 0d;
	private Double askPrice2 = 0d;
	private Double askPrice3 = 0d;
	private Double askPrice4 = 0d;
	private Double askPrice5 = 0d;
	private Double askPrice6 = 0d;
	private Double askPrice7 = 0d;
	private Double askPrice8 = 0d;
	private Double askPrice9 = 0d;
	private Double askPrice10 = 0d;

	private Integer bidVolume1 = 0;
	private Integer bidVolume2 = 0;
	private Integer bidVolume3 = 0;
	private Integer bidVolume4 = 0;
	private Integer bidVolume5 = 0;
	private Integer bidVolume6 = 0;
	private Integer bidVolume7 = 0;
	private Integer bidVolume8 = 0;
	private Integer bidVolume9 = 0;
	private Integer bidVolume10 = 0;

	private Integer askVolume1 = 0;
	private Integer askVolume2 = 0;
	private Integer askVolume3 = 0;
	private Integer askVolume4 = 0;
	private Integer askVolume5 = 0;
	private Integer askVolume6 = 0;
	private Integer askVolume7 = 0;
	private Integer askVolume8 = 0;
	private Integer askVolume9 = 0;
	private Integer askVolume10 = 0;

	public void MyReadString(String temp) {
		if (StringUtils.isNotBlank(temp)) {
			String[] split = temp.split("@");
			if (split != null) {
				if (split.length > 0) {
					this.gatewayID = split[0];
				}
				if (split.length > 1) {
					this.symbol = split[1];
				}
				if (split.length > 2) {
					this.exchange = split[2];
				}
				if (split.length > 3) {
					this.rtSymbol = split[3];
				}
				if (split.length > 4) {
					this.tradingDay = split[4];
				}
				if (split.length > 5) {
					this.actionDay = split[5];
				}
				if (split.length > 6) {
					this.actionTime = split[6];
				}
				if (split.length > 7) {
					this.dateTime = RtConstant.DT_FORMAT_WITH_MS_INT_FORMATTER.parseDateTime(split[7]);
				}
				if (split.length > 8) {
					this.status = Integer.parseInt(split[8]);
				}
				if (split.length > 9) {
					this.lastPrice = Double.parseDouble(split[9]);
				}
				if (split.length > 10) {
					this.lastVolume = Integer.parseInt(split[10]);
				}
				if (split.length > 11) {
					this.volume = Integer.parseInt(split[11]);
				}
				if (split.length > 12) {
					this.openInterest = Double.parseDouble(split[12]);
				}
				if (split.length > 13) {
					this.preOpenInterest = Long.parseLong(split[13]);
				}
				if (split.length > 14) {
					this.preClosePrice = Double.parseDouble(split[14]);
				}
				if (split.length > 15) {
					this.preSettlePrice = Double.parseDouble(split[15]);
				}
				if (split.length > 16) {
					this.openPrice = Double.parseDouble(split[16]);
				}
				if (split.length > 17) {
					this.highPrice = Double.parseDouble(split[17]);
				}
				if (split.length > 18) {
					this.lowPrice = Double.parseDouble(split[18]);
				}
				if (split.length > 19) {
					this.upperLimit = Double.parseDouble(split[19]);
				}
				if (split.length > 20) {
					this.lowerLimit = Double.parseDouble(split[20]);
				}
				if (split.length > 21) {
					this.bidPrice1 = Double.parseDouble(split[21]);
				}
				if (split.length > 22) {
					this.bidPrice2 = Double.parseDouble(split[22]);
				}
				if (split.length > 23) {
					this.bidPrice3 = Double.parseDouble(split[23]);
				}
				if (split.length > 24) {
					this.bidPrice4 = Double.parseDouble(split[24]);
				}
				if (split.length > 25) {
					this.bidPrice5 = Double.parseDouble(split[25]);
				}
				if (split.length > 26) {
					this.bidPrice6 = Double.parseDouble(split[26]);
				}
				if (split.length > 27) {
					this.bidPrice7 = Double.parseDouble(split[27]);
				}
				if (split.length > 28) {
					this.bidPrice8 = Double.parseDouble(split[28]);
				}
				if (split.length > 29) {
					this.bidPrice9 = Double.parseDouble(split[29]);
				}
				if (split.length > 30) {
					this.bidPrice10 = Double.parseDouble(split[30]);
				}
				if (split.length > 31) {
					this.askPrice1 = Double.parseDouble(split[31]);
				}
				if (split.length > 32) {
					this.askPrice2 = Double.parseDouble(split[32]);
				}
				if (split.length > 33) {
					this.askPrice3 = Double.parseDouble(split[33]);
				}
				if (split.length > 34) {
					this.askPrice4 = Double.parseDouble(split[34]);
				}
				if (split.length > 35) {
					this.askPrice5 = Double.parseDouble(split[35]);
				}
				if (split.length > 36) {
					this.askPrice6 = Double.parseDouble(split[36]);
				}
				if (split.length > 37) {
					this.askPrice7 = Double.parseDouble(split[37]);
				}
				if (split.length > 38) {
					this.askPrice8 = Double.parseDouble(split[38]);
				}
				if (split.length > 39) {
					this.askPrice9 = Double.parseDouble(split[39]);
				}
				if (split.length > 40) {
					this.askPrice10 = Double.parseDouble(split[40]);
				}
				if (split.length > 41) {
					this.bidVolume1 = Integer.parseInt(split[41]);
				}
				if (split.length > 42) {
					this.bidVolume2 = Integer.parseInt(split[42]);
				}
				if (split.length > 43) {
					this.bidVolume3 = Integer.parseInt(split[43]);
				}
				if (split.length > 44) {
					this.bidVolume4 = Integer.parseInt(split[44]);
				}
				if (split.length > 45) {
					this.bidVolume5 = Integer.parseInt(split[45]);
				}
				if (split.length > 46) {
					this.bidVolume6 = Integer.parseInt(split[46]);
				}
				if (split.length > 47) {
					this.bidVolume7 = Integer.parseInt(split[47]);
				}
				if (split.length > 48) {
					this.bidVolume8 = Integer.parseInt(split[48]);
				}
				if (split.length > 49) {
					this.bidVolume9 = Integer.parseInt(split[49]);
				}
				if (split.length > 50) {
					this.bidVolume10 = Integer.parseInt(split[50]);
				}
				if (split.length > 51) {
					this.askVolume1 = Integer.parseInt(split[51]);
				}
				if (split.length > 52) {
					this.askVolume2 = Integer.parseInt(split[52]);
				}
				if (split.length > 53) {
					this.askVolume3 = Integer.parseInt(split[53]);
				}
				if (split.length > 54) {
					this.askVolume4 = Integer.parseInt(split[54]);
				}
				if (split.length > 55) {
					this.askVolume5 = Integer.parseInt(split[55]);
				}
				if (split.length > 56) {
					this.askVolume6 = Integer.parseInt(split[56]);
				}
				if (split.length > 57) {
					this.askVolume7 = Integer.parseInt(split[57]);
				}
				if (split.length > 58) {
					this.askVolume8 = Integer.parseInt(split[58]);
				}
				if (split.length > 59) {
					this.askVolume9 = Integer.parseInt(split[59]);
				}
				if (split.length > 60) {
					this.askVolume10 = Integer.parseInt(split[60]);
				}

			}
		}
	}

	public String MyToString() {
		return String.join("@", Lists.newArrayList(this.gatewayID, this.symbol, this.exchange, this.rtSymbol,
				this.tradingDay, this.actionDay, this.actionTime,
				this.dateTime.toString(RtConstant.DT_FORMAT_WITH_MS_INT_FORMATTER), String.valueOf(this.status),
				String.valueOf(this.lastPrice), String.valueOf(this.lastVolume), String.valueOf(this.volume),
				String.valueOf(this.openInterest), String.valueOf(this.preOpenInterest),
				String.valueOf(this.preClosePrice), String.valueOf(this.preSettlePrice), String.valueOf(this.openPrice),
				String.valueOf(this.highPrice), String.valueOf(this.lowPrice), String.valueOf(this.upperLimit),
				String.valueOf(this.lowerLimit), String.valueOf(this.bidPrice1), String.valueOf(this.bidPrice2),
				String.valueOf(this.bidPrice3), String.valueOf(this.bidPrice4), String.valueOf(this.bidPrice5),
				String.valueOf(this.bidPrice6), String.valueOf(this.bidPrice7), String.valueOf(this.bidPrice8),
				String.valueOf(this.bidPrice9), String.valueOf(this.bidPrice10), String.valueOf(this.askPrice1),
				String.valueOf(this.askPrice2), String.valueOf(this.askPrice3), String.valueOf(this.askPrice4),
				String.valueOf(this.askPrice5), String.valueOf(this.askPrice6), String.valueOf(this.askPrice7),
				String.valueOf(this.askPrice8), String.valueOf(this.askPrice9), String.valueOf(this.askPrice10),
				String.valueOf(this.bidVolume1), String.valueOf(this.bidVolume2), String.valueOf(this.bidVolume3),
				String.valueOf(this.bidVolume4), String.valueOf(this.bidVolume5), String.valueOf(this.bidVolume6),
				String.valueOf(this.bidVolume7), String.valueOf(this.bidVolume8), String.valueOf(this.bidVolume9),
				String.valueOf(this.bidVolume10), String.valueOf(this.askVolume1), String.valueOf(this.askVolume2),
				String.valueOf(this.askVolume3), String.valueOf(this.askVolume4), String.valueOf(this.askVolume5),
				String.valueOf(this.askVolume6), String.valueOf(this.askVolume7), String.valueOf(this.askVolume8),
				String.valueOf(this.askVolume9), String.valueOf(this.askVolume10)));
	}

	public void setAllValue(String gatewayID, String symbol, String exchange, String rtSymbol, String tradingDay,
			String actionDay, String actionTime, DateTime dateTime, Integer status, Double lastPrice,
			Integer lastVolume, Integer volume, Double openInterest, Long preOpenInterest, Double preClosePrice,
			Double preSettlePrice, Double openPrice, Double highPrice, Double lowPrice, Double upperLimit,
			Double lowerLimit, Double bidPrice1, Double bidPrice2, Double bidPrice3, Double bidPrice4, Double bidPrice5,
			Double bidPrice6, Double bidPrice7, Double bidPrice8, Double bidPrice9, Double bidPrice10, Double askPrice1,
			Double askPrice2, Double askPrice3, Double askPrice4, Double askPrice5, Double askPrice6, Double askPrice7,
			Double askPrice8, Double askPrice9, Double askPrice10, Integer bidVolume1, Integer bidVolume2,
			Integer bidVolume3, Integer bidVolume4, Integer bidVolume5, Integer bidVolume6, Integer bidVolume7,
			Integer bidVolume8, Integer bidVolume9, Integer bidVolume10, Integer askVolume1, Integer askVolume2,
			Integer askVolume3, Integer askVolume4, Integer askVolume5, Integer askVolume6, Integer askVolume7,
			Integer askVolume8, Integer askVolume9, Integer askVolume10) {
		this.gatewayID = gatewayID;
		this.symbol = symbol;
		this.exchange = exchange;
		this.rtSymbol = rtSymbol;
		this.tradingDay = tradingDay;
		this.actionDay = actionDay;
		this.actionTime = actionTime;
		this.dateTime = dateTime;
		this.status = status;
		this.lastPrice = lastPrice;
		this.lastVolume = lastVolume;
		this.volume = volume;
		this.openInterest = openInterest;
		this.preOpenInterest = preOpenInterest;
		this.preClosePrice = preClosePrice;
		this.preSettlePrice = preSettlePrice;
		this.openPrice = openPrice;
		this.highPrice = highPrice;
		this.lowPrice = lowPrice;
		this.upperLimit = upperLimit;
		this.lowerLimit = lowerLimit;
		this.bidPrice1 = bidPrice1;
		this.bidPrice2 = bidPrice2;
		this.bidPrice3 = bidPrice3;
		this.bidPrice4 = bidPrice4;
		this.bidPrice5 = bidPrice5;
		this.bidPrice6 = bidPrice6;
		this.bidPrice7 = bidPrice7;
		this.bidPrice8 = bidPrice8;
		this.bidPrice9 = bidPrice9;
		this.bidPrice10 = bidPrice10;
		this.askPrice1 = askPrice1;
		this.askPrice2 = askPrice2;
		this.askPrice3 = askPrice3;
		this.askPrice4 = askPrice4;
		this.askPrice5 = askPrice5;
		this.askPrice6 = askPrice6;
		this.askPrice7 = askPrice7;
		this.askPrice8 = askPrice8;
		this.askPrice9 = askPrice9;
		this.askPrice10 = askPrice10;
		this.bidVolume1 = bidVolume1;
		this.bidVolume2 = bidVolume2;
		this.bidVolume3 = bidVolume3;
		this.bidVolume4 = bidVolume4;
		this.bidVolume5 = bidVolume5;
		this.bidVolume6 = bidVolume6;
		this.bidVolume7 = bidVolume7;
		this.bidVolume8 = bidVolume8;
		this.bidVolume9 = bidVolume9;
		this.bidVolume10 = bidVolume10;
		this.askVolume1 = askVolume1;
		this.askVolume2 = askVolume2;
		this.askVolume3 = askVolume3;
		this.askVolume4 = askVolume4;
		this.askVolume5 = askVolume5;
		this.askVolume6 = askVolume6;
		this.askVolume7 = askVolume7;
		this.askVolume8 = askVolume8;
		this.askVolume9 = askVolume9;
		this.askVolume10 = askVolume10;
	}

	public String getGatewayID() {
		return gatewayID;
	}

	public void setGatewayID(String gatewayID) {
		this.gatewayID = gatewayID;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public String getExchange() {
		return exchange;
	}

	public void setExchange(String exchange) {
		this.exchange = exchange;
	}

	public String getRtSymbol() {
		return rtSymbol;
	}

	public void setRtSymbol(String rtSymbol) {
		this.rtSymbol = rtSymbol;
	}

	public String getTradingDay() {
		return tradingDay;
	}

	public void setTradingDay(String tradingDay) {
		this.tradingDay = tradingDay;
	}

	public String getActionDay() {
		return actionDay;
	}

	public void setActionDay(String actionDay) {
		this.actionDay = actionDay;
	}

	public String getActionTime() {
		return actionTime;
	}

	public void setActionTime(String actionTime) {
		this.actionTime = actionTime;
	}

	public DateTime getDateTime() {
		return dateTime;
	}

	public void setDateTime(DateTime dateTime) {
		this.dateTime = dateTime;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Double getLastPrice() {
		return lastPrice;
	}

	public void setLastPrice(Double lastPrice) {
		this.lastPrice = lastPrice;
	}

	public Integer getLastVolume() {
		return lastVolume;
	}

	public void setLastVolume(Integer lastVolume) {
		this.lastVolume = lastVolume;
	}

	public Integer getVolume() {
		return volume;
	}

	public void setVolume(Integer volume) {
		this.volume = volume;
	}

	public Double getOpenInterest() {
		return openInterest;
	}

	public void setOpenInterest(Double openInterest) {
		this.openInterest = openInterest;
	}

	public Long getPreOpenInterest() {
		return preOpenInterest;
	}

	public void setPreOpenInterest(Long preOpenInterest) {
		this.preOpenInterest = preOpenInterest;
	}

	public Double getPreClosePrice() {
		return preClosePrice;
	}

	public void setPreClosePrice(Double preClosePrice) {
		this.preClosePrice = preClosePrice;
	}

	public Double getPreSettlePrice() {
		return preSettlePrice;
	}

	public void setPreSettlePrice(Double preSettlePrice) {
		this.preSettlePrice = preSettlePrice;
	}

	public Double getOpenPrice() {
		return openPrice;
	}

	public void setOpenPrice(Double openPrice) {
		this.openPrice = openPrice;
	}

	public Double getHighPrice() {
		return highPrice;
	}

	public void setHighPrice(Double highPrice) {
		this.highPrice = highPrice;
	}

	public Double getLowPrice() {
		return lowPrice;
	}

	public void setLowPrice(Double lowPrice) {
		this.lowPrice = lowPrice;
	}

	public Double getUpperLimit() {
		return upperLimit;
	}

	public void setUpperLimit(Double upperLimit) {
		this.upperLimit = upperLimit;
	}

	public Double getLowerLimit() {
		return lowerLimit;
	}

	public void setLowerLimit(Double lowerLimit) {
		this.lowerLimit = lowerLimit;
	}

	public Double getBidPrice1() {
		return bidPrice1;
	}

	public void setBidPrice1(Double bidPrice1) {
		this.bidPrice1 = bidPrice1;
	}

	public Double getBidPrice2() {
		return bidPrice2;
	}

	public void setBidPrice2(Double bidPrice2) {
		this.bidPrice2 = bidPrice2;
	}

	public Double getBidPrice3() {
		return bidPrice3;
	}

	public void setBidPrice3(Double bidPrice3) {
		this.bidPrice3 = bidPrice3;
	}

	public Double getBidPrice4() {
		return bidPrice4;
	}

	public void setBidPrice4(Double bidPrice4) {
		this.bidPrice4 = bidPrice4;
	}

	public Double getBidPrice5() {
		return bidPrice5;
	}

	public void setBidPrice5(Double bidPrice5) {
		this.bidPrice5 = bidPrice5;
	}

	public Double getBidPrice6() {
		return bidPrice6;
	}

	public void setBidPrice6(Double bidPrice6) {
		this.bidPrice6 = bidPrice6;
	}

	public Double getBidPrice7() {
		return bidPrice7;
	}

	public void setBidPrice7(Double bidPrice7) {
		this.bidPrice7 = bidPrice7;
	}

	public Double getBidPrice8() {
		return bidPrice8;
	}

	public void setBidPrice8(Double bidPrice8) {
		this.bidPrice8 = bidPrice8;
	}

	public Double getBidPrice9() {
		return bidPrice9;
	}

	public void setBidPrice9(Double bidPrice9) {
		this.bidPrice9 = bidPrice9;
	}

	public Double getBidPrice10() {
		return bidPrice10;
	}

	public void setBidPrice10(Double bidPrice10) {
		this.bidPrice10 = bidPrice10;
	}

	public Double getAskPrice1() {
		return askPrice1;
	}

	public void setAskPrice1(Double askPrice1) {
		this.askPrice1 = askPrice1;
	}

	public Double getAskPrice2() {
		return askPrice2;
	}

	public void setAskPrice2(Double askPrice2) {
		this.askPrice2 = askPrice2;
	}

	public Double getAskPrice3() {
		return askPrice3;
	}

	public void setAskPrice3(Double askPrice3) {
		this.askPrice3 = askPrice3;
	}

	public Double getAskPrice4() {
		return askPrice4;
	}

	public void setAskPrice4(Double askPrice4) {
		this.askPrice4 = askPrice4;
	}

	public Double getAskPrice5() {
		return askPrice5;
	}

	public void setAskPrice5(Double askPrice5) {
		this.askPrice5 = askPrice5;
	}

	public Double getAskPrice6() {
		return askPrice6;
	}

	public void setAskPrice6(Double askPrice6) {
		this.askPrice6 = askPrice6;
	}

	public Double getAskPrice7() {
		return askPrice7;
	}

	public void setAskPrice7(Double askPrice7) {
		this.askPrice7 = askPrice7;
	}

	public Double getAskPrice8() {
		return askPrice8;
	}

	public void setAskPrice8(Double askPrice8) {
		this.askPrice8 = askPrice8;
	}

	public Double getAskPrice9() {
		return askPrice9;
	}

	public void setAskPrice9(Double askPrice9) {
		this.askPrice9 = askPrice9;
	}

	public Double getAskPrice10() {
		return askPrice10;
	}

	public void setAskPrice10(Double askPrice10) {
		this.askPrice10 = askPrice10;
	}

	public Integer getBidVolume1() {
		return bidVolume1;
	}

	public void setBidVolume1(Integer bidVolume1) {
		this.bidVolume1 = bidVolume1;
	}

	public Integer getBidVolume2() {
		return bidVolume2;
	}

	public void setBidVolume2(Integer bidVolume2) {
		this.bidVolume2 = bidVolume2;
	}

	public Integer getBidVolume3() {
		return bidVolume3;
	}

	public void setBidVolume3(Integer bidVolume3) {
		this.bidVolume3 = bidVolume3;
	}

	public Integer getBidVolume4() {
		return bidVolume4;
	}

	public void setBidVolume4(Integer bidVolume4) {
		this.bidVolume4 = bidVolume4;
	}

	public Integer getBidVolume5() {
		return bidVolume5;
	}

	public void setBidVolume5(Integer bidVolume5) {
		this.bidVolume5 = bidVolume5;
	}

	public Integer getBidVolume6() {
		return bidVolume6;
	}

	public void setBidVolume6(Integer bidVolume6) {
		this.bidVolume6 = bidVolume6;
	}

	public Integer getBidVolume7() {
		return bidVolume7;
	}

	public void setBidVolume7(Integer bidVolume7) {
		this.bidVolume7 = bidVolume7;
	}

	public Integer getBidVolume8() {
		return bidVolume8;
	}

	public void setBidVolume8(Integer bidVolume8) {
		this.bidVolume8 = bidVolume8;
	}

	public Integer getBidVolume9() {
		return bidVolume9;
	}

	public void setBidVolume9(Integer bidVolume9) {
		this.bidVolume9 = bidVolume9;
	}

	public Integer getBidVolume10() {
		return bidVolume10;
	}

	public void setBidVolume10(Integer bidVolume10) {
		this.bidVolume10 = bidVolume10;
	}

	public Integer getAskVolume1() {
		return askVolume1;
	}

	public void setAskVolume1(Integer askVolume1) {
		this.askVolume1 = askVolume1;
	}

	public Integer getAskVolume2() {
		return askVolume2;
	}

	public void setAskVolume2(Integer askVolume2) {
		this.askVolume2 = askVolume2;
	}

	public Integer getAskVolume3() {
		return askVolume3;
	}

	public void setAskVolume3(Integer askVolume3) {
		this.askVolume3 = askVolume3;
	}

	public Integer getAskVolume4() {
		return askVolume4;
	}

	public void setAskVolume4(Integer askVolume4) {
		this.askVolume4 = askVolume4;
	}

	public Integer getAskVolume5() {
		return askVolume5;
	}

	public void setAskVolume5(Integer askVolume5) {
		this.askVolume5 = askVolume5;
	}

	public Integer getAskVolume6() {
		return askVolume6;
	}

	public void setAskVolume6(Integer askVolume6) {
		this.askVolume6 = askVolume6;
	}

	public Integer getAskVolume7() {
		return askVolume7;
	}

	public void setAskVolume7(Integer askVolume7) {
		this.askVolume7 = askVolume7;
	}

	public Integer getAskVolume8() {
		return askVolume8;
	}

	public void setAskVolume8(Integer askVolume8) {
		this.askVolume8 = askVolume8;
	}

	public Integer getAskVolume9() {
		return askVolume9;
	}

	public void setAskVolume9(Integer askVolume9) {
		this.askVolume9 = askVolume9;
	}

	public Integer getAskVolume10() {
		return askVolume10;
	}

	public void setAskVolume10(Integer askVolume10) {
		this.askVolume10 = askVolume10;
	}
}

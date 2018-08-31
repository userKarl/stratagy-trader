package com.zd.business.entity;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Lists;

/**
 * @author sun0x00@gmail.com
 * CTP
 */
public class Order implements Serializable {

	private static final long serialVersionUID = 7932302478961553376L;

	private String gatewayID; // 接口

	// 代码编号相关
	private String symbol; // 代码
	private String exchange; // 交易所代码
	private String rtSymbol; // 系统中的唯一代码,通常是 合约代码.交易所代码

	private String orderID; // 订单编号
	private String rtOrderID; // 订单在rt系统中的唯一编号,通常是 Gateway名.订单编号

	// 报单相关
	private String direction = ""; // 报单方向
	private String offset = ""; // 报单开平仓
	private double price = 0; // 报单价格
	private int totalVolume = 0; // 报单总数量
	private int tradedVolume = 0; // 报单成交数量
	private String status = ""; // 报单状态

	private String tradingDay = "";

	private String orderDate = ""; // 发单日期
	private String orderTime = ""; // 发单时间
	private String cancelTime = ""; // 撤单时间
	private String activeTime = ""; // 激活时间
	private String updateTime = ""; // 最后修改时间

	private String priceType = "";

	// CTP/LTS相关
	private int frontID = 0; // 前置机编号
	private int sessionID = 0; // 连接编号

	public Order() {
	}

	public String MyToString() {
		try {
			return String.join("@", Lists.newArrayList(this.gatewayID, this.symbol, this.exchange, this.rtSymbol,
					this.orderID, this.rtOrderID, this.direction, this.offset, String.valueOf(this.price),
					String.valueOf(this.totalVolume), String.valueOf(this.tradedVolume), this.status, this.tradingDay,
					this.orderDate, this.orderTime, this.cancelTime, this.activeTime, this.updateTime,
					String.valueOf(this.frontID), String.valueOf(this.sessionID), this.priceType));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void MyReadString(String temp) {
		try {
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
						this.orderID = split[4];
					}
					if (split.length > 5) {
						this.rtOrderID = split[5];
					}
					if (split.length > 6) {
						this.direction = split[6];
					}
					if (split.length > 7) {
						this.offset = split[7];
					}
					if (split.length > 8) {
						this.price = Double.parseDouble(split[8]);
					}
					if (split.length > 9) {
						this.totalVolume = Integer.parseInt(split[9]);
					}
					if (split.length > 10) {
						this.tradedVolume = Integer.parseInt(split[10]);
					}
					if (split.length > 11) {
						this.status = split[11];
					}
					if (split.length > 12) {
						this.tradingDay = split[12];
					}
					if (split.length > 13) {
						this.orderDate = split[13];
					}
					if (split.length > 14) {
						this.orderTime = split[14];
					}
					if (split.length > 15) {
						this.cancelTime = split[15];
					}
					if (split.length > 16) {
						this.activeTime = split[16];
					}
					if (split.length > 17) {
						this.updateTime = split[17];
					}
					if (split.length > 18) {
						this.frontID = Integer.parseInt(split[18]);
					}
					if (split.length > 19) {
						this.sessionID = Integer.parseInt(split[19]);
					}
					if (split.length > 20) {
						this.priceType = split[20];
					}
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void setAllValue(String gatewayID, String symbol, String exchange, String rtSymbol, String orderID,
			String rtOrderID, String direction, String offset, double price, int totalVolume, int tradedVolume,
			String status, String tradingDay, String orderDate, String orderTime, String cancelTime, String activeTime,
			String updateTime, int frontID, int sessionID) {
		this.gatewayID = gatewayID;
		this.symbol = symbol;
		this.exchange = exchange;
		this.rtSymbol = rtSymbol;
		this.orderID = orderID;
		this.rtOrderID = rtOrderID;
		this.direction = direction;
		this.offset = offset;
		this.price = price;
		this.totalVolume = totalVolume;
		this.tradedVolume = tradedVolume;
		this.status = status;
		this.tradingDay = tradingDay;
		this.orderDate = orderDate;
		this.orderTime = orderTime;
		this.cancelTime = cancelTime;
		this.activeTime = activeTime;
		this.updateTime = updateTime;
		this.frontID = frontID;
		this.sessionID = sessionID;
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

	public String getOrderID() {
		return orderID;
	}

	public void setOrderID(String orderID) {
		this.orderID = orderID;
	}

	public String getRtOrderID() {
		return rtOrderID;
	}

	public void setRtOrderID(String rtOrderID) {
		this.rtOrderID = rtOrderID;
	}

	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	public String getOffset() {
		return offset;
	}

	public void setOffset(String offset) {
		this.offset = offset;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getTotalVolume() {
		return totalVolume;
	}

	public void setTotalVolume(int totalVolume) {
		this.totalVolume = totalVolume;
	}

	public int getTradedVolume() {
		return tradedVolume;
	}

	public void setTradedVolume(int tradedVolume) {
		this.tradedVolume = tradedVolume;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTradingDay() {
		return tradingDay;
	}

	public void setTradingDay(String tradingDay) {
		this.tradingDay = tradingDay;
	}

	public String getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}

	public String getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(String orderTime) {
		this.orderTime = orderTime;
	}

	public String getCancelTime() {
		return cancelTime;
	}

	public void setCancelTime(String cancelTime) {
		this.cancelTime = cancelTime;
	}

	public int getFrontID() {
		return frontID;
	}

	public void setFrontID(int frontID) {
		this.frontID = frontID;
	}

	public int getSessionID() {
		return sessionID;
	}

	public void setSessionID(int sessionID) {
		this.sessionID = sessionID;
	}

	public String getActiveTime() {
		return activeTime;
	}

	public void setActiveTime(String activeTime) {
		this.activeTime = activeTime;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public String getPriceType() {
		return priceType;
	}

	public void setPriceType(String priceType) {
		this.priceType = priceType;
	}

}

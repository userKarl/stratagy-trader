package xyz.redtorch.trader.entity;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Lists;

/**
 * @author sun0x00@gmail.com
 */
public class Position implements Serializable {

	private static final long serialVersionUID = -7231668074296775467L;

	private String gatewayID = ""; // 接口

	// 代码编号相关
	private String symbol = ""; // 代码
	private String exchange = ""; // 交易所代码
	private String rtSymbol = ""; // 系统中的唯一代码,通常是 合约代码.交易所代码

	// 持仓相关
	private String direction = ""; // 持仓方向
	private int position = 0; // 持仓量
	private int frozen = 0; // 冻结数量
	private double price = 0; // 持仓均价
	private String rtPositionName = ""; // 持仓在系统中的唯一代码,通常是rtSymbol.方向
	private int ydPosition = 0; // 昨持仓
	private double positionProfit = 0; // 持仓盈亏

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
					this.direction = split[4];
				}
				if (split.length > 5) {
					this.position = Integer.parseInt(split[5]);
				}
				if (split.length > 6) {
					this.frozen = Integer.parseInt(split[6]);
				}
				if (split.length > 7) {
					this.price = Double.parseDouble(split[7]);
				}
				if (split.length > 8) {
					this.rtPositionName = split[8];
				}
				if (split.length > 9) {
					this.ydPosition = Integer.parseInt(split[9]);
				}
				if (split.length > 10) {
					this.positionProfit = Double.parseDouble(split[10]);
				}
			}
		}
	}

	public String MyToString() {
		this.rtPositionName = "";
		return String.join("@",
				Lists.newArrayList(this.gatewayID, this.symbol, this.exchange, this.rtSymbol, this.direction,
						String.valueOf(this.position), String.valueOf(this.frozen), String.valueOf(this.price),
						this.rtPositionName, String.valueOf(this.ydPosition), String.valueOf(this.positionProfit)));
	}

	public void setAllValue(String gatewayID, String symbol, String exchange, String rtSymbol, String direction,
			int position, int frozen, double price, String rtPositionName, int ydPosition, double positionProfit) {
		this.gatewayID = gatewayID;
		this.symbol = symbol;
		this.exchange = exchange;
		this.rtSymbol = rtSymbol;
		this.direction = direction;
		this.position = position;
		this.frozen = frozen;
		this.price = price;
		this.rtPositionName = rtPositionName;
		this.ydPosition = ydPosition;
		this.positionProfit = positionProfit;
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

	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public int getFrozen() {
		return frozen;
	}

	public void setFrozen(int frozen) {
		this.frozen = frozen;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getRtPositionName() {
		return rtPositionName;
	}

	public void setRtPositionName(String rtPositionName) {
		this.rtPositionName = rtPositionName;
	}

	public int getYdPosition() {
		return ydPosition;
	}

	public void setYdPosition(int ydPosition) {
		this.ydPosition = ydPosition;
	}

	public double getPositionProfit() {
		return positionProfit;
	}

	public void setPositionProfit(double positionProfit) {
		this.positionProfit = positionProfit;
	}
}

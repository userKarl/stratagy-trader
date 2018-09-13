package com.zd.business.entity;

import java.math.BigDecimal;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Lists;
import com.shanghaizhida.beans.FilledResponseInfo;
import com.shanghaizhida.beans.OrderResponseInfo;
import com.zd.business.entity.ctp.Order;
import com.zd.business.entity.ctp.Trade;

import lombok.Data;

/**
 * 合约下单
 * 
 * @author user
 *
 */
@Data
public class ContractOrder {

	private String orderId = "";// 下单Id
	private String exchangeNo = "";// 交易所
	private String code = "";// 合约代码
	private String direction = "";// 买卖方向
	private BigDecimal requestPrice = BigDecimal.valueOf(0);// 委托价格
	private Integer needRequestNums = 0;// 需要下单的总量
	private Integer requestNums = 0;// 单次委托数量
	private Integer stayNums = 0;// 当前挂单数量
	private Integer dealNums = 0;// 成交数量
	private Integer cancelNums = 0;// 撤销数量
	private BigDecimal dealPrice = BigDecimal.valueOf(0);// 成交价格
	private BigDecimal upperTick = BigDecimal.valueOf(0);// 跳点
	private String env = "";// 交易环境
	private String userId = "";// 交易账户ID

	/**
	 * 以下字段为功能需要，不加入传递数据中
	 */
	private String priceType = "";// 下单类型
	private Integer remainNums = 0;// 剩余可下单数量（=需要下单量-当前挂单量-成交量），初始值为needRequestNums
	private ConcurrentHashMap<String, OrderResponseInfo> stayMap = new ConcurrentHashMap<>();// ZD期货挂单Map
	private ConcurrentHashMap<String, FilledResponseInfo> dealMap = new ConcurrentHashMap<>();// ZD期货成交Map
	private ConcurrentHashMap<String, Order> ctpStayMap = new ConcurrentHashMap<>();// CTP期货挂单Map
	private ConcurrentHashMap<String, Trade> ctpTradeMap = new ConcurrentHashMap<>();// CTP期货成交Map

	public void MyReadString(String temp) {
		try {
			if (StringUtils.isNotBlank(temp)) {
				String[] split = temp.split("\\^");
				if (split != null) {
					if (split.length > 0) {
						this.exchangeNo = split[0];
					}
					if (split.length > 1) {
						this.code = split[1];
					}
					if (split.length > 2) {
						this.direction = split[2];
					}
					if (split.length > 3) {
						this.requestPrice = new BigDecimal(split[3]);
					}
					if (split.length > 4) {
						this.needRequestNums = Integer.parseInt(split[4]);
					}
					if (split.length > 5) {
						this.requestNums = Integer.parseInt(split[5]);
					}
					if (split.length > 6) {
						this.stayNums = Integer.parseInt(split[6]);
					}
					if (split.length > 7) {
						this.dealNums = Integer.parseInt(split[7]);
					}
					if (split.length > 8) {
						this.cancelNums = Integer.parseInt(split[8]);
					}
					if (split.length > 9) {
						this.dealPrice = new BigDecimal(split[9]);
					}
					if (split.length > 10) {
						this.upperTick = new BigDecimal(split[10]);
					}
					if (split.length > 11) {
						this.env = split[11];
					}
					if (split.length > 12) {
						this.userId = split[12];
					}
					if (split.length > 13) {
						this.orderId = split[13];
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public String MyToString() {
		return String.join("^",
				Lists.newArrayList(this.exchangeNo, this.code, this.direction, this.requestPrice.toString(),
						String.valueOf(this.needRequestNums), String.valueOf(this.requestNums),
						String.valueOf(this.stayNums), String.valueOf(this.dealNums), String.valueOf(this.cancelNums),
						this.dealPrice.toString(), this.upperTick.toString(), this.env, this.userId,this.orderId));
	}
}

package com.zd.business.entity;

import lombok.Data;

@Data
public class OrderInfo {

	private String symbol;//合约代码
	private String exchangeNo;//交易所
	private String price;//价格
	private String volume;//数量
	private String direction;//买卖方向
	private String priceType;//下单类型
}

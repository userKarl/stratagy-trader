package com.zd.business.entity;

import lombok.Data;

@Data
public class MarketProvider {

	private String marketInfo; // 行情数据
	private String priceLevelLimit; // 下单档位限制
	private String spread; // 买卖价差
	private String minOrderNum; // 最小下单量
	private String maxOrderNum; // 最大下单量
	private String maxBuyNum; // 最大买持仓量
	private String maxSaleNum; // 最大卖持仓量
	private String currBuyNum; // 当前买持仓量
	private String currSaleNum; // 当前卖持仓量
	
}

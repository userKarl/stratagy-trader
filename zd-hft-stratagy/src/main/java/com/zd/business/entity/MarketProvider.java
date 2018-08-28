package com.zd.business.entity;

import com.shanghaizhida.beans.MarketInfo;

import lombok.Data;

@Data
public class MarketProvider {

	private MarketInfo marketInfo; // 行情数据
	private int priceLevelLimit; // 下单档位限制
	private double spread; // 买卖价差
	private double minOrderNum; // 最小下单量
	private double maxOrderNum; // 最大下单量
	private double maxBuyNum; // 最大买持仓量
	private double maxSaleNum; // 最大卖持仓量
	private double currBuyNum; // 当前买持仓量
	private double currSaleNum; // 当前卖持仓量
}

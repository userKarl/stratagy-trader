package com.zd.business.entity;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class Arbitrage {

	private String id;//ID
	private String name;//名称
	private Integer buyNums;//买量
	private Integer saleNums;//卖量
	private BigDecimal buyPrice;//买价
	private BigDecimal salePrice;//卖价
	private BigDecimal currPrice;//最新价
	private Integer currNums;//现手
	private String direction;//买卖方向
}

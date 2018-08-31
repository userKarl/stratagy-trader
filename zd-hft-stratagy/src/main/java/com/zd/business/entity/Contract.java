package com.zd.business.entity;

import java.math.BigDecimal;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Lists;

import lombok.Data;

@Data
public class Contract {

	private String env;//交易环境
	private String exchangeCode;// 交易所
	private String code;// 合约代码
	private Integer perFillNums;// 每下一手策略套利单时的单腿下单量,即下单比例
	private String direction;//买卖方向
	private Integer buyNums;
	private BigDecimal buyPrice;
	private Integer saleNums;
	private BigDecimal salePrice;

	public String MyToString() {
		return String.join("@", Lists.newArrayList(this.exchangeCode, this.code));
	}

	public void MyReadString(String temp) {
		if (StringUtils.isNotBlank(temp)) {
			String[] split = temp.split("@", -1);
			if (split.length > 0) {
				this.exchangeCode = split[0];
			}
			if (split.length > 1) {
				this.code = split[1];
			}
		}
	}

}

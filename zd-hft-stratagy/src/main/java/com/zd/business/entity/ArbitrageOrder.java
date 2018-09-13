package com.zd.business.entity;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Lists;

import lombok.Data;

@Data
public class ArbitrageOrder {

	private String arbitrageOrderId;// 策略下单Id
	private String arbitrageId;// 套利策略Id
	private Integer orderNums = 0;// 下单数量
	private BigDecimal orderPrice = new BigDecimal(0);// 下单价格
	private String direction;// 下单方向
	private ContractOrder activeContractOrder = new ContractOrder();// 主动腿下单
	private List<ContractOrder> marketContractOrderList = Lists.newArrayList();// 被动腿下单

	public void MyReadString(String temp) {
		try {
			if (StringUtils.isNotBlank(temp)) {
				String[] split = temp.split("\\#");
				if (split != null) {
					if (split.length > 0) {
						this.arbitrageOrderId = split[0];
					}
					if (split.length > 1) {
						this.arbitrageId = split[1];
					}
					if (split.length > 2) {
						this.orderNums = Integer.parseInt(split[2]);
					}
					if (split.length > 3) {
						this.orderPrice = new BigDecimal(split[3]);
					}
					if (split.length > 4) {
						this.direction = split[4];
					}
					if (split.length > 5) {
						this.activeContractOrder.MyReadString(split[5]);
					}
					if (split.length > 6) {
						String[] split2 = split[6].split("\\~");
						if (split2 != null && split2.length > 0) {
							for (String s : split2) {
								ContractOrder contractOrder = new ContractOrder();
								contractOrder.MyReadString(s);
								this.marketContractOrderList.add(contractOrder);
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String MyToString() {
		StringBuilder sb = new StringBuilder();
		for (ContractOrder order : marketContractOrderList) {
			sb.append(order.MyToString()).append("~");
		}
		return String.join("#",
				Lists.newArrayList(this.arbitrageOrderId, this.arbitrageId, String.valueOf(this.orderNums),
						this.orderPrice.toString(), this.direction, this.activeContractOrder.MyToString(),
						sb.toString()));
	}
}

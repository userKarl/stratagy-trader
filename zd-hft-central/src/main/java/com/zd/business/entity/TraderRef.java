package com.zd.business.entity;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Lists;

import lombok.Data;

/**
 * NetInfo指令的localSystemCode所对应的报单类
 * @author user
 *
 */
@Data
public class TraderRef {

	private String handlerStratagyThreadId = "";//计算策略的线程Id
	private String stratagyId = "";//策略Id
	private String arbitrageOrderId = "";//套利下单Id
	private String contractOrderId = "";//套利下单合约Id
	private String traderSymbolType = "";//下单合约的主被动腿标记
	private String activeDealId="";//主动腿成交Id
	private String evenActiveFlag="";//是否平主动腿标记
	
	public void MyReadString(String temp) {
		try {
			if(StringUtils.isNotBlank(temp)) {
				String[] split = temp.split("\\^");
				if(split!=null) {
					if(split.length>0) {
						this.handlerStratagyThreadId=split[0];
					}
					if(split.length>1) {
						this.stratagyId=split[1];
					}
					if(split.length>2) {
						this.arbitrageOrderId=split[2];
					}
					if(split.length>3) {
						this.contractOrderId=split[3];
					}
					if(split.length>4) {
						this.traderSymbolType=split[4];
					}
					if(split.length>5) {
						this.activeDealId=split[5];
					}
					if(split.length>6) {
						this.evenActiveFlag=split[6];
					}
				}
			}
		} catch (Exception e) {
		}
	}
	
	public String MyToString() {
		return String.join("^", Lists.newArrayList(this.handlerStratagyThreadId,this.stratagyId,this.arbitrageOrderId,
				this.contractOrderId,this.traderSymbolType,this.activeDealId,this.evenActiveFlag));
	}
}

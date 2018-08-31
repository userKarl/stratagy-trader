package com.zd.business.entity;

import com.google.common.collect.Lists;
import com.zd.common.utils.StringUtils;

import lombok.Data;

@Data
public class OrderDTO {

	private String consumerId;//消费者Id
	private String stratagyId;//策略Id
	private String orderInfo;//下单信息，多个以“,”分割
	
	public void MyReadString(String temp) {
		if(StringUtils.isNotBlank(temp)) {
			String[] split = temp.split("@");
			if(split!=null) {
				if(split.length>0) {
					this.consumerId=split[0];
				}
				if(split.length>1) {
					this.stratagyId=split[1];
				}
				if(split.length>2) {
					this.orderInfo=split[2];
				}
			}
		}
	}
	
	public String MyToString() {
		return String.join("@", Lists.newArrayList(this.consumerId,this.stratagyId,this.orderInfo));
	}
}

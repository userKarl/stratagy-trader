package com.zd.business.entity;

import com.google.common.collect.Lists;
import com.zd.common.utils.StringUtils;

import lombok.Data;

/**
 * 策略类
 * 被动腿合约接收行情，根据公式计算出主动腿合约的下单价格
 * @author user
 *
 */
@Data
public class Stratagy {

	private String id;					//策略ID
	private String name;				//策略名称
	private String status;				//策略状态
	private String type;				//策略类型（M市商，A套利）
	private String expression;			//套利公式
	private Contract marketContract;	//被动腿合约
	private Contract activeContract;	//主动腿合约
	
	public void MyReadString(String temp) {
		if(StringUtils.isNotBlank(temp)) {
			String[] split = temp.split("@",-1);
			if(split!=null) {
				if(split.length>0) {
					this.id=split[0];
				}
				if(split.length>1) {
					this.name=split[1];
				}
				if(split.length>2) {
					this.name=split[2];
				}
			}
		}
	}
	
	public String MyToString() {
		return String.join("@",Lists.newArrayList(this.id,this.name,this.status));
	}
	
}

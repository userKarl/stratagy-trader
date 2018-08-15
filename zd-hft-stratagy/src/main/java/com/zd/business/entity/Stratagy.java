package com.zd.business.entity;

import com.google.common.collect.Lists;
import com.zd.common.utils.StringUtils;

import lombok.Data;

@Data
public class Stratagy {

	private String id;
	private String name;
	private String status;
	
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

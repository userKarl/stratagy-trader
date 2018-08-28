package com.zd.business.entity;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Lists;

import lombok.Data;

@Data
public class Contract {

	private String exchangeCode;
	private String code;

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

package com.zd.business.constant.order;

public enum UserTypeEnum {

	COMMON("1","一般用户"),
	ORGAN("2","机构用户");
	
	private String code;
	private String desc;
	
	private UserTypeEnum(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
}

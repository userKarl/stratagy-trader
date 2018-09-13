package com.zd.business.constant;

public enum OrderMarketReasonTypeEnum {

	AUTOCHASING("1","自动追单"),
	SYSTEMCHASING("2","系统追单");
	
	private String code;
	private String desc;
	private OrderMarketReasonTypeEnum(String code, String desc) {
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

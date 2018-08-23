package com.zd.entity;

public enum SubMarketTypeEnum {

	ALL("0","订阅全部行情"),
	ADD("1","追加行情"),
	UNSUB("2","退订行情"),
	NEW("3","清空之情的订阅列表，订阅新的行情");
	
	private String code;
	private String desc;
	
	private SubMarketTypeEnum(String code, String desc) {
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

package com.zd.business.constant;

public enum TraderSymbolTypeEnum {

	ACTIVE("1","主动腿合约"),
	MARKET("2","被动腿合约");
	
	private TraderSymbolTypeEnum(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}
	private String code;
	private String desc;
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

package com.zd.business.constant.order;

public enum PriceTypeEnum {

	LIMIT("1","限价单"),
	MARKET("2","市价单"),
	STOP2LIMIT("3","限价止损"),
	STOP2MARKET("4","止损");
	
	private String code;
	private String desc;
	
	private PriceTypeEnum(String code, String desc) {
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

package com.zd.business.constant.order;

public enum BuySaleEnum {

	BUY("1","买"),
	SALE("2","卖");
	
	private String code;
	private String desc;
	
	private BuySaleEnum(String code, String desc) {
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

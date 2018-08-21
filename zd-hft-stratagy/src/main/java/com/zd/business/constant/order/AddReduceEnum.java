package com.zd.business.constant.order;

public enum AddReduceEnum {

	TAKE("1","开仓"),
	CLOSE("2","平仓"),
	CLOSETODAY("3","平今"),
	CLOSEYESTERDAY("4","平昨");
	
	private String code;
	private String desc;
	
	private AddReduceEnum(String code, String desc) {
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

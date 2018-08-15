package com.shanghaizhida.beans;

/**
 * 成交查询请求信息的类
 * 
 * @author xiang
 * 
 */

public class FilledSearchInfo implements NetParent {

	/** 资金账号 */
	public String accountNo = "";

	/** 交易密码 */
	public String tradePwd = "";

	/** 成交编号（要包括7位的订单编号，一共11位） */
	public String filledNo = "";

	/** 成交时间（格式：yyyy-MM-dd hh:mm:ss） */
	public String filledDateTime = "";

	/** 用户ID */
	public String userId = "";

	@Override
	public String MyToString() {
		String temp = this.accountNo + "@" + this.tradePwd + "@" + this.filledNo + "@" + this.filledDateTime + "@" + this.userId;

		return temp;
	}

	@Override
	public void MyReadString(String temp) {
		String[] arrClass = temp.split("@");
		this.accountNo = arrClass[0];
		this.tradePwd = arrClass[1];
		this.filledNo = arrClass[2];
		this.filledDateTime = arrClass[3];
		this.userId = arrClass[4];
	}

	@Override
	public String MyPropToString() {
		return "accountNo@tradePwd@filledNo@filledDateTime@userId";
	}
}

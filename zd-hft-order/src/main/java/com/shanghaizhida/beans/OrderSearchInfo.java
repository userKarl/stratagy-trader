package com.shanghaizhida.beans;

/**
 * 定单查询请求信息类
 * 
 * @author xiang
 * 
 */

public class OrderSearchInfo implements NetParent {

	// 用户ID
	public String userId = "";

	// 用户类型：1：一般用户；2：机构通用户；
	public String userType = "";

	// 资金账号
	public String accountNo = "";

	// 交易密码
	public String tradePwd = "";

	// 是否模拟用户：1：是；0 or other：不是
	public String isSimulation = "";

	// 取得指定订单号以后的定单
	public String orderNo = "";

	// 取得指定订单时间以后的定单（格式：yyyy-MM-dd hh:mm:ss）
	public String orderDateTime = "";

	@Override
	public String MyToString() {
		String temp = this.userId + "@" + this.userType + "@" + this.accountNo
				+ "@" + this.tradePwd + "@" + this.isSimulation + "@" + this.orderNo + "@" + this.orderDateTime;

		return temp;
	}

	@Override
	public void MyReadString(String temp) {
		String[] arrClass = temp.split("@");
		this.userId = arrClass[0];
		this.userType = arrClass[1];
		this.accountNo = arrClass[2];
		this.tradePwd = arrClass[3];
		this.isSimulation = arrClass[4];
		this.orderNo = arrClass[5];
		this.orderDateTime = arrClass[6];
	}

	@Override
	public String MyPropToString() {
		String temp = "userId@userType@accountNo@tradePwd@isSimulation@orderNo@orderDateTime";
		return temp;
	}
}

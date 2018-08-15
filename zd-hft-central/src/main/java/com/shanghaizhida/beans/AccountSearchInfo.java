package com.shanghaizhida.beans;

/**
 * 资金查询请求信息的类
 * 
 * @author xiang
 * 
 */

public class AccountSearchInfo implements NetParent {

	// 用户ID
	public String userId = "";

	// 资金帐号
	public String accountNo = "";

	// 交易密码
	public String tradePwd = "";

	public String MyToString() {
		String temp = this.userId + "@" + this.accountNo + "@" + this.tradePwd;
		return temp;
	}

	public void MyReadString(String temp) {
		String[] arrClass = temp.split("@");
		this.userId = arrClass[0];
		this.accountNo = arrClass[1];
		this.tradePwd = arrClass[2];
	}

	public String MyPropToString() {
		return "userId@accountNo@tradePwd";
	}

}

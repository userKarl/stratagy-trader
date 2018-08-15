package com.shanghaizhida.beans;

public class LoginInfo implements NetParent {

	/** 用户ID */
	public String userId = "";

	/** 用户密码 */
	public String userPwd = "";

	/** 用户类型（0或者空：期货，1：港股，2：国内股票，3：美股） 20150821 add */
	public String userType = "";

	/** mac地址 */
	public String macAddress = "";

	/** 电脑名 */
	public String computerName = "";

	@Override
	public String MyToString() {
		String temp = this.userId + "@" + this.userPwd + "@" + this.userType + "@" + this.macAddress + "@" + this.computerName;
		return temp;
	}

	@Override
	public void MyReadString(String temp) {
		String[] arrClass = temp.split("@");
		this.userId = arrClass[0];
		this.userPwd = arrClass[1];

		if (arrClass.length > 2) {
			this.userType = arrClass[2];
		}
		if (arrClass.length > 3) {
			this.macAddress = arrClass[3];
		}
		if (arrClass.length > 4) {
			this.computerName = arrClass[4];
		}
	}

	@Override
	public String MyPropToString() {
		return "userId@userPwd@userType@macAddress@computerName";
	}
}

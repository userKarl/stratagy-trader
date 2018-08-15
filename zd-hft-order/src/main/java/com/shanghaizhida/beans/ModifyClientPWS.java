package com.shanghaizhida.beans;

/**
 * 用户修改密码发送请求类
 * <p>
 * 
 * @author xiangchao
 * @date 2016年3月4日<br>
 * @version 1.0<br>
 */
public class ModifyClientPWS implements NetParent {

	/** 用户代码 */
	public String userCode = "";

	/** 用户原密码 */
	public String oldPws = "";

	/** 用户新密码 */
	public String newPws = "";

	public String MyToString() {
		return this.userCode + "@" + this.oldPws + "@" + this.newPws;
	}

	public void MyReadString(String temp) {
		String[] arrClass = temp.split("@");
		this.userCode = arrClass[0];
		this.oldPws = arrClass[1];
		this.newPws = arrClass[2];
	}

	public String MyPropToString() {
		return "userCode@this.oldPws@this.newPws";
	}
}

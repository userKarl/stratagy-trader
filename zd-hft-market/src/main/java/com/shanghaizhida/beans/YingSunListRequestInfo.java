package com.shanghaizhida.beans;

public class YingSunListRequestInfo implements NetParent {
	/** 设置人 */
	public String userId = "";

	/** 最后一条数据的止损止盈设置编号 */
	public String yingsunNo = "";

	public String MyToString() {
		String temp = this.userId + "@" + this.yingsunNo;
		return temp;
	}

	public void MyReadString(String temp) {
		String[] arrClass = temp.split("@", -1);
		this.userId = arrClass[0];
		this.yingsunNo = arrClass[1];
	}

	public String MyPropToString() {
		String temp = "userId@yingsunNo";
		return temp;
	}
}

package com.shanghaizhida.beans;

/**
 * 止损止盈设置删除请求信息的类
 * 
 * @author xiang <br>
 *         2015年12月1日 下午3:49:50
 * 
 */

public class YingSunDelRequestInfo implements NetParent {

	/** 止损止盈编号 */
	public String yingsunNo = "";

	/** 前置机ID */
	public String frontId = "";

	public String MyToString() {
		String temp = this.yingsunNo + "@" + this.frontId;
		return temp;
	}

	public void MyReadString(String temp) {
		String[] arrClass = temp.split("@", -1);
		this.yingsunNo = arrClass[0];
		this.frontId = arrClass[1];
	}

	public String MyPropToString() {
		String temp = "yingsunNo@frontId";
		return temp;
	}
}

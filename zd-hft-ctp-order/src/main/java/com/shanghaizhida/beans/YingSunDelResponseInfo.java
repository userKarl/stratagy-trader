package com.shanghaizhida.beans;

/**
 * 止损止盈设置删除请求返回信息的类
 * 
 * @author xiang <br>
 *         2015年12月2日 下午3:39:27
 */

public class YingSunDelResponseInfo implements NetParent {

	/** 止损止盈编号 */
	public String yingsunNo = "";

	/**
	 * 状态：0：已请求；1：未触发；2：已触发(已废除不用,改用8,9表示是否触发下单成功)；3：已撤销；4：已部分成交
	 * ；5：已完全成交；6：已清除；7：已失效
	 */
	public String status = "";

	/** 结果信息 */
	public String result = "";

	public String MyToString() {
		String temp = this.yingsunNo + "@" + this.status + "@" + this.result;
		return temp;
	}

	public void MyReadString(String temp) {
		String[] arrClass = temp.split("@", -1);
		this.yingsunNo = arrClass[0];
		this.status = arrClass[1];
		this.result = arrClass[2];
	}

	public String MyPropToString() {
		String temp = "yingsunNo@status@result";
		return temp;
	}
}

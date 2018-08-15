package com.shanghaizhida.beans;

/**
 * 历史登陆IP类
 * <p>
 * 
 * @author liwei
 * @date 2016年8月18日<br>
 * @version 1.0<br>
 */
public class LoginHistoryIpInfo implements NetParent {

	/** IP和端口 */
	public String IpPort = "";

	/** 暂时无用 */
	public String IpPortLan = "";

	/** 上一次登陆时间 */
	public String LoginDateTime = "";

	/** 登录类型 1为上一次登陆 其他为同时登陆 */
	public String LoginType = "";

	public String MyToString() {
		return this.IpPort + "@" + this.IpPortLan + "@" + this.LoginDateTime + "@" + this.LoginType;
	}

	public void MyReadString(String temp) {
		String[] arrClass = temp.split("@");
		this.IpPort = arrClass[0];
		this.IpPortLan = arrClass[1];
		this.LoginDateTime = arrClass[2];
		this.LoginType = arrClass[3];
	}

	public String MyPropToString() {
		return "IpPort@IpPortLan@LoginDateTime@LoginType";
	}
}
package com.shanghaizhida.beans;

/**
 * 登陆返回信息类
 * 
 * @author xiang
 * 
 */
public class LoginResponseInfo implements NetParent {

	/** 用户ID */
	public String userId = "";

	/** 用户名 */
	public String userName = "";

	/** 用户类型：（1：期货，2：港股，3：美股，4：国内股票）
	 *  M:margin用户   C:现金用户
	 */
	public String userType = "";

	/** 登录密码 */
	public String loginPwd = "";

	/** 资金账号 */
	public String accountNo = "";

	/** 交易密码 */
	public String tradePwd = "";

	/** 是否模拟用户：1：是；0 or other：不是 */
	public String isSimulation = "";

	/** 前置机IP地址 */
	public String frontendIp = "";

	/** 前置机端口号(暂时没用.如果等于1表示密码过期) */
	public String frontendPort = "";

	/** 帐户币种 */
	public String FCurrencyNo = "";

	/** 用户状态 C消户 U在用 D冻结 */
	public String FState = "";

	/** 是否可以卖空期权：0：不可以；1：可以 */
	public String FSelAll = "";

	/** 是否可以下自定义策略单：0：不可以；1：可以 */
	public String FStrategy = "";

	/** 是否可以下国内单：0：不可以；1：可以 */
	public String FInner = "";

	/** 是否可以使用止损止盈和条件单功能：0：不可以；1：可以 */
	public String FYingSun = "";

	/** 是否可以使用炒单手功能：0：不可以；1：可以 */
	public String FChaoDan = "";

	/** 是否可以交易期权：0：不可以；1：可以 */
	public String FOption = "";

	/** 是否可以获取CME行情：0：不可以；1：可以   是否可以更新港股的权限 */
	public String FCMEMarket = "";

	/** 是否可以获取CME_COMEX行情：0：不可以；1：可以   是否可以更新美股的权限 */
	public String FCMECOMEXMarket = "";

	/** 是否可以获取CME_NYMEX行情：0：不可以；1：可以 */
	public String FCMENYMEXMarket = "";

	/** 是否可以获取CME_CBT行情：0：不可以；1：可以 */
	public String FCMECBTMarket = "";

	/** 是否可以获取ICE US(NYBOT，eCBOT)行情：0：不可以；1：可以 */
	public String ICEUSMarket = "";

	/** 是否可以获取ICE EC(ICE)行情：0：不可以；1：可以 */
	public String ICEECMarket = "";

	/** 是否可以获取ICE EF(Liffe)行情：0：不可以；1：可以 */
	public String ICEEFMarket = "";

	/** 是否可以获取港交所HKEX行情：0：不可以；1：可以 */
	public String FHKEXMarket = "";

	/** 是否新设备初次登录（1：是；0：否） */
	public String isFirstLogin = "";

	/** 用户手机号 */
	public String userMobile = "";

	/** 是否已经设置了密保问题答案（1：是；0：否） */
	public String hasSetQA = "";

	/** 是否已经绑定过设备mac地址（1：是；0 or other：不是） */
	public String existMac = "";

	/**上海能源可取资金系数*/
	public String ratioINE = "";

	/**是否可以获取Eurex行情：0：不可以；1：可以*/
	public String EurexMarket = "";

	@Override
	public String MyToString() {
		String temp = this.userId + "@" + this.userName + "@" + this.userType
				+ "@" + this.loginPwd + "@" + this.accountNo + "@" + this.tradePwd
				+ "@" + this.isSimulation + "@" + this.frontendIp + "@" + this.frontendPort
				+ "@" + this.FCurrencyNo + "@" + this.FState + "@" + this.FSelAll
				+ "@" + this.FStrategy + "@" + this.FInner + "@" + this.FYingSun
				+ "@" + this.FChaoDan + "@" + this.FOption + "@" + this.FCMEMarket
				+ "@" + this.FCMECOMEXMarket + "@" + this.FCMENYMEXMarket + "@" + this.FCMECBTMarket
				+ "@" + this.ICEUSMarket + "@" + this.ICEECMarket + "@" + this.ICEEFMarket + "@" + this.FHKEXMarket
				+ "@" + this.isFirstLogin + "@" + this.userMobile + "@" + this.hasSetQA + "@" + this.existMac
				+ "@" + this.ratioINE + "@" + this.EurexMarket;

		return temp;
	}

	@Override
	public void MyReadString(String temp) {
		String[] arrClass = temp.split("@");
		this.userId = arrClass[0];
		this.userName = arrClass[1];
		this.userType = arrClass[2];
		this.loginPwd = arrClass[3];
		this.accountNo = arrClass[4];
		this.tradePwd = arrClass[5];
		this.isSimulation = arrClass[6];
		this.frontendIp = arrClass[7];
		this.frontendPort = arrClass[8];
		this.FCurrencyNo = arrClass[9];
		this.FState = arrClass[10];
		this.FSelAll = arrClass[11];

		if (arrClass.length > 12) {
			this.FStrategy = arrClass[12];
		}
		if (arrClass.length > 13) {
			this.FInner = arrClass[13];
		}
		if (arrClass.length > 14) {
			this.FYingSun = arrClass[14];
		}
		if (arrClass.length > 15) {
			this.FChaoDan = arrClass[15];
		}
		if (arrClass.length > 16) {
			this.FOption = arrClass[16];
		}
		if (arrClass.length > 17) {
			this.FCMEMarket = arrClass[17];
		}
		if (arrClass.length > 18) {
			this.FCMECOMEXMarket = arrClass[18];
		}
		if (arrClass.length > 19) {
			this.FCMENYMEXMarket = arrClass[19];
		}
		if (arrClass.length > 20) {
			this.FCMECBTMarket = arrClass[20];
		}
		if (arrClass.length > 21) {
			this.ICEUSMarket = arrClass[21];
		}
		if (arrClass.length > 22) {
			this.ICEECMarket = arrClass[22];
		}
		if (arrClass.length > 23) {
			this.ICEEFMarket = arrClass[23];
		}
		if (arrClass.length > 24) {
			this.FHKEXMarket = arrClass[24];
		}
		if (arrClass.length > 25) {
			this.isFirstLogin = arrClass[25];
		}
		if (arrClass.length > 26) {
			this.userMobile = arrClass[26];
		}
		if (arrClass.length > 27) {
			this.hasSetQA = arrClass[27];
		}
		if (arrClass.length > 28) {
			this.existMac = arrClass[28];
		}
		if (arrClass.length > 29) {
			this.ratioINE = arrClass[29];
		}
		if (arrClass.length > 30) {
			this.EurexMarket = arrClass[30];
		}

	}

	@Override
	public String MyPropToString() {
		String temp = "userId@userName@userType@loginPwd@accountNo"
				+ "@tradePwd@isSimulation@frontendIp@frontendPort"
				+ "@FCurrencyNo@FState@FSelAll@FStrategy@FInner"
				+ "@FYingSun@FChaoDan@FOption@FCMEMarket@FCMECOMEXMarket@FCMENYMEXMarket@FCMECBTMarket"
				+ "@ICEUSMarket@ICEECMarket@ICEEFMarket@FHKEXMarket@isFirstLogin@userMobile@hasSetQA@existMac";
		return temp;
	}
}

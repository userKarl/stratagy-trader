package com.shanghaizhida.beans;

/**
 * 通知界面,通知栏传递的info
 * 
 * @author xiang <br>
 *         2015年12月8日 下午4:20:07
 * 
 */
public class YingSunSetStatusInfo implements NetParent {

	/** 止损止盈编号 */
	public String yingsunNo = "";

	/** 合约代码 */
	public String contractNo = "";

	/** 交易所编号 add by ccy on 20170210 */
	public String exchangeNo = "";

	/** 合约中文名称 */
	public String contractName = "";

	/**
	 * 盈损状态：0：已请求；1：未触发；2：已触发(已废除不用,改用8,9表示是否触发下单成功)；3：已撤销；4：已部分成交；
	 * 5：已完全成交；6：已清除；7：已失效；8：平仓指令成功；9：平仓指令失败
	 */
	public String status = "";

	/**
	 * 盈损状态中文：0：已请求；1：未触发；2：已触发(已废除不用,改用8,9表示是否触发下单成功)；3：已撤销；
	 * 4：已部分成交；5：已完全成交；6：已清除；7：已失效；8：平仓指令成功；9：平仓指令失败
	 */
	public String statusCHS = "";

	/** 是不是修改, 0 不是 ,1是(如果是修改状态是未触发,通知栏应该显示已修改) */
	public int isModify;

	@Override
	public String MyToString() {
		String temp = this.yingsunNo + "@" + this.contractNo + "@" + this.status + "@" + this.statusCHS + "@" + this.isModify;

		return temp;
	}

	@Override
	public void MyReadString(String temp) {
		String[] arrClass = temp.split("@", -1);
		this.yingsunNo = arrClass[0];
		this.contractNo = arrClass[1];
		this.status = arrClass[2];
		this.statusCHS = arrClass[3];
		this.isModify = Integer.parseInt(arrClass[4]);
	}

	@Override
	public String MyPropToString() {
		String temp = "yingsunNo@contractNo@status@statusCHS@isModify";
		return temp;
	}

}

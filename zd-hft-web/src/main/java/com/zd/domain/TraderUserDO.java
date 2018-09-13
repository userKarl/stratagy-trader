package com.zd.domain;

import java.io.Serializable;
import java.util.Date;



/**
 * 交易账户表
 * 
 * @author lipy
 * @email lipy336699@gmail.com
 * @date 2018-09-10 17:56:27
 */
public class TraderUserDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
	private String id;
	//交易账户所属系统用户
	private String sysUserId;
	//交易账户Id
	private String accountNo;
	//交易账户密码
	private String accountPwd;
	//交易环境，1：直达，2：CTP，3：现货交易所
	private String traderEnv;
	//
	private String remark;
	//
	private String delFlag;
	//
	private String createBy;
	//
	private Date createDate;
	//
	private String updateBy;
	//
	private Date updateDate;

	/**
	 * 设置：
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * 获取：
	 */
	public String getId() {
		return id;
	}
	/**
	 * 设置：交易账户所属系统用户
	 */
	public void setSysUserId(String sysUserId) {
		this.sysUserId = sysUserId;
	}
	/**
	 * 获取：交易账户所属系统用户
	 */
	public String getSysUserId() {
		return sysUserId;
	}
	/**
	 * 设置：交易账户Id
	 */
	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}
	/**
	 * 获取：交易账户Id
	 */
	public String getAccountNo() {
		return accountNo;
	}
	/**
	 * 设置：交易账户密码
	 */
	public void setAccountPwd(String accountPwd) {
		this.accountPwd = accountPwd;
	}
	/**
	 * 获取：交易账户密码
	 */
	public String getAccountPwd() {
		return accountPwd;
	}
	/**
	 * 设置：交易环境，1：直达，2：CTP，3：现货交易所
	 */
	public void setTraderEnv(String traderEnv) {
		this.traderEnv = traderEnv;
	}
	/**
	 * 获取：交易环境，1：直达，2：CTP，3：现货交易所
	 */
	public String getTraderEnv() {
		return traderEnv;
	}
	/**
	 * 设置：
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}
	/**
	 * 获取：
	 */
	public String getRemark() {
		return remark;
	}
	/**
	 * 设置：
	 */
	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}
	/**
	 * 获取：
	 */
	public String getDelFlag() {
		return delFlag;
	}
	/**
	 * 设置：
	 */
	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}
	/**
	 * 获取：
	 */
	public String getCreateBy() {
		return createBy;
	}
	/**
	 * 设置：
	 */
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	/**
	 * 获取：
	 */
	public Date getCreateDate() {
		return createDate;
	}
	/**
	 * 设置：
	 */
	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}
	/**
	 * 获取：
	 */
	public String getUpdateBy() {
		return updateBy;
	}
	/**
	 * 设置：
	 */
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	/**
	 * 获取：
	 */
	public Date getUpdateDate() {
		return updateDate;
	}
}

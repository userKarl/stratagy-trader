package com.zd.domain;

import java.io.Serializable;
import java.util.Date;



/**
 * 
 * 
 * @author lipy
 * @email lipy336699@gmail.com
 * @date 2018-09-07 17:31:49
 */
public class UserDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//用户id
	private Long userId;
	//用户名
	private String clientno;
	//
	private String name;
	//密码
	private String password;
	//
	private Long deptId;
	//邮箱
	private String email;
	//手机号
	private String mobile;
	//状态 0:禁用，1:正常
	private Integer status;
	//创建用户id
	private Long userIdCreate;
	//创建时间
	private Date gmtCreate;
	//修改时间
	private Date gmtModified;
	//性别
	private Long sex;
	//出身日期
	private Date birth;
	//
	private Long picId;
	//现居住地
	private String liveAddress;
	//爱好
	private String hobby;
	//省份
	private String province;
	//所在城市
	private String city;
	//所在地区
	private String district;
	//创建模块权限标识  T：有权限/F：无
	private String ispower;

	/**
	 * 设置：
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	/**
	 * 获取：
	 */
	public Long getUserId() {
		return userId;
	}

	public String getClientno() {
		return clientno;
	}

	public void setClientno(String clientno) {
		this.clientno = clientno;
	}

	/**
	 * 设置：
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * 获取：
	 */
	public String getName() {
		return name;
	}
	/**
	 * 设置：密码
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	/**
	 * 获取：密码
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * 设置：
	 */
	public void setDeptId(Long deptId) {
		this.deptId = deptId;
	}
	/**
	 * 获取：
	 */
	public Long getDeptId() {
		return deptId;
	}
	/**
	 * 设置：邮箱
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	/**
	 * 获取：邮箱
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * 设置：手机号
	 */
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	/**
	 * 获取：手机号
	 */
	public String getMobile() {
		return mobile;
	}
	/**
	 * 设置：状态 0:禁用，1:正常
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}
	/**
	 * 获取：状态 0:禁用，1:正常
	 */
	public Integer getStatus() {
		return status;
	}
	/**
	 * 设置：创建用户id
	 */
	public void setUserIdCreate(Long userIdCreate) {
		this.userIdCreate = userIdCreate;
	}
	/**
	 * 获取：创建用户id
	 */
	public Long getUserIdCreate() {
		return userIdCreate;
	}
	/**
	 * 设置：创建时间
	 */
	public void setGmtCreate(Date gmtCreate) {
		this.gmtCreate = gmtCreate;
	}
	/**
	 * 获取：创建时间
	 */
	public Date getGmtCreate() {
		return gmtCreate;
	}
	/**
	 * 设置：修改时间
	 */
	public void setGmtModified(Date gmtModified) {
		this.gmtModified = gmtModified;
	}
	/**
	 * 获取：修改时间
	 */
	public Date getGmtModified() {
		return gmtModified;
	}
	/**
	 * 设置：性别
	 */
	public void setSex(Long sex) {
		this.sex = sex;
	}
	/**
	 * 获取：性别
	 */
	public Long getSex() {
		return sex;
	}
	/**
	 * 设置：出身日期
	 */
	public void setBirth(Date birth) {
		this.birth = birth;
	}
	/**
	 * 获取：出身日期
	 */
	public Date getBirth() {
		return birth;
	}
	/**
	 * 设置：
	 */
	public void setPicId(Long picId) {
		this.picId = picId;
	}
	/**
	 * 获取：
	 */
	public Long getPicId() {
		return picId;
	}
	/**
	 * 设置：现居住地
	 */
	public void setLiveAddress(String liveAddress) {
		this.liveAddress = liveAddress;
	}
	/**
	 * 获取：现居住地
	 */
	public String getLiveAddress() {
		return liveAddress;
	}
	/**
	 * 设置：爱好
	 */
	public void setHobby(String hobby) {
		this.hobby = hobby;
	}
	/**
	 * 获取：爱好
	 */
	public String getHobby() {
		return hobby;
	}
	/**
	 * 设置：省份
	 */
	public void setProvince(String province) {
		this.province = province;
	}
	/**
	 * 获取：省份
	 */
	public String getProvince() {
		return province;
	}
	/**
	 * 设置：所在城市
	 */
	public void setCity(String city) {
		this.city = city;
	}
	/**
	 * 获取：所在城市
	 */
	public String getCity() {
		return city;
	}
	/**
	 * 设置：所在地区
	 */
	public void setDistrict(String district) {
		this.district = district;
	}
	/**
	 * 获取：所在地区
	 */
	public String getDistrict() {
		return district;
	}
	/**
	 * 设置：创建模块权限标识  T：有权限/F：无
	 */
	public void setIspower(String ispower) {
		this.ispower = ispower;
	}
	/**
	 * 获取：创建模块权限标识  T：有权限/F：无
	 */
	public String getIspower() {
		return ispower;
	}
}

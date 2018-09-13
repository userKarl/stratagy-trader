package com.zd.domain;

import java.io.Serializable;
import java.util.Date;


/**
 * 模块设置
 * 
 * @author lipy
 * @email lipy336699@gmail.com
 * @date 2018-09-06 10:40:27
 */
public class ModuleDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//主键ID
	private String id;
	//模块名称
	private String name;
	//子模块ID，多个以“,”分割
	private String parentIds;
	//授权(多个用逗号分隔，如：user:list,user:create)
	private String perms;
	//0：常用 1：基础 2：功能 3：逻辑 4：统计分析
	private String moduleType;
	//0:常用 1:免费  2: 未开放
	private String modulePower;
	//模块状态 ：T 已购买  F 未购买 
	private String moduleStatus;
	//备注
	private String remark;
	//删除标记，0正常1删除
	private String delFlag;
	//创建者
	private String createBy;
	//创建日期
	private Date createDate;
	//更新者
	private String updateBy;
	//更新日期
	private Date updateDate;

	private  String child_ids;
	/**
	 * 设置：主键ID
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * 获取：主键ID
	 */
	public String getId() {
		return id;
	}
	/**
	 * 设置：模块名称
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * 获取：模块名称
	 */
	public String getName() {
		return name;
	}

	public String getParentIds() {
		return parentIds;
	}

	public void setParentIds(String parentIds) {
		this.parentIds = parentIds;
	}

	/**
	 * 设置：授权(多个用逗号分隔，如：user:list,user:create)
	 */
	public void setPerms(String perms) {
		this.perms = perms;
	}
	/**
	 * 获取：授权(多个用逗号分隔，如：user:list,user:create)
	 */
	public String getPerms() {
		return perms;
	}

	/**
	 * 设置：0：常用 1：基础 2：功能 3：逻辑 4：统计分析
	 */
	public void setModuleType(String moduleType) {
		this.moduleType = moduleType;
	}
	/**
	 * 获取：0：常用 1：基础 2：功能 3：逻辑 4：统计分析
	 */
	public String getModuleType() {
		return moduleType;
	}
	/**
	 * 设置：0:常用 1:免费  2: 未开放
	 */
	public void setModulePower(String modulePower) {
		this.modulePower = modulePower;
	}
	/**
	 * 获取：0:常用 1:免费  2: 未开放
	 */
	public String getModulePower() {
		return modulePower;
	}
	/**
	 * 设置：模块状态 ：T 已购买  F 未购买 
	 */
	public void setModuleStatus(String moduleStatus) {
		this.moduleStatus = moduleStatus;
	}
	/**
	 * 获取：模块状态 ：T 已购买  F 未购买 
	 */
	public String getModuleStatus() {
		return moduleStatus;
	}
	/**
	 * 设置：备注
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}
	/**
	 * 获取：备注
	 */
	public String getRemark() {
		return remark;
	}
	/**
	 * 设置：删除标记，0正常1删除
	 */
	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}
	/**
	 * 获取：删除标记，0正常1删除
	 */
	public String getDelFlag() {
		return delFlag;
	}
	/**
	 * 设置：创建者
	 */
	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}
	/**
	 * 获取：创建者
	 */
	public String getCreateBy() {
		return createBy;
	}
	/**
	 * 设置：创建日期
	 */
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	/**
	 * 获取：创建日期
	 */
	public Date getCreateDate() {
		return createDate;
	}
	/**
	 * 设置：更新者
	 */
	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}
	/**
	 * 获取：更新者
	 */
	public String getUpdateBy() {
		return updateBy;
	}
	/**
	 * 设置：更新日期
	 */
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	/**
	 * 获取：更新日期
	 */
	public Date getUpdateDate() {
		return updateDate;
	}

	public String getChild_ids() {
		return child_ids;
	}

	public void setChild_ids(String child_ids) {
		this.child_ids = child_ids;
	}
}

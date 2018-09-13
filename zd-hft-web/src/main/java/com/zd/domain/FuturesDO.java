package com.zd.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;



/**
 * 
 * 
 * @author lipy
 * @email lipy336699@gmail.com
 * @date 2018-09-07 15:00:32
 */
public class FuturesDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//交易所
	private String exchangeNo;
	//交易所名
	private String exchangeName;
	//商品代码
	private String commodityNo;
	//商品名
	private String commodityName;
	//完整的期货合约代码
	private String code;
	//合约代码（YYMM）
	private String contractNo;
	//合约名称
	private String contractName;
	//商品类型
	private String futuresType;
	//每点价值
	private BigDecimal productDot;
	//跳点
	private BigDecimal upperTick;
	//更新日期(yyyyMMdd)
	private String regDate;
	//最后交易日(yyyyMMdd)
	private String expiryDate;
	//小数点位数
	private Integer dotNum;
	//货币
	private String currencyNo;
	//货币名
	private String currencyName;
	//进阶单位
	private Integer lowerTick;
	//备用交易所
	private String exchangeNo2;
	//保证金
	private BigDecimal deposit;
	//保证金百分比
	private BigDecimal depositPercent;
	//首次通知日
	private String firstNoticeDay;
	//
	private String createBy;
	//
	private Date createDate;
	//
	private String updateBy;
	//
	private Date updateDate;
	//内外盘标志
	private String commodityType;
	//拼音名字
	private String pyName;
	//删除标志
	private String delFlag;
	//最后交易日(过期日)
	private String lastTradeDay;

	/**
	 * 设置：交易所
	 */
	public void setExchangeNo(String exchangeNo) {
		this.exchangeNo = exchangeNo;
	}
	/**
	 * 获取：交易所
	 */
	public String getExchangeNo() {
		return exchangeNo;
	}
	/**
	 * 设置：交易所名
	 */
	public void setExchangeName(String exchangeName) {
		this.exchangeName = exchangeName;
	}
	/**
	 * 获取：交易所名
	 */
	public String getExchangeName() {
		return exchangeName;
	}
	/**
	 * 设置：商品代码
	 */
	public void setCommodityNo(String commodityNo) {
		this.commodityNo = commodityNo;
	}
	/**
	 * 获取：商品代码
	 */
	public String getCommodityNo() {
		return commodityNo;
	}
	/**
	 * 设置：商品名
	 */
	public void setCommodityName(String commodityName) {
		this.commodityName = commodityName;
	}
	/**
	 * 获取：商品名
	 */
	public String getCommodityName() {
		return commodityName;
	}
	/**
	 * 设置：完整的期货合约代码
	 */
	public void setCode(String code) {
		this.code = code;
	}
	/**
	 * 获取：完整的期货合约代码
	 */
	public String getCode() {
		return code;
	}
	/**
	 * 设置：合约代码（YYMM）
	 */
	public void setContractNo(String contractNo) {
		this.contractNo = contractNo;
	}
	/**
	 * 获取：合约代码（YYMM）
	 */
	public String getContractNo() {
		return contractNo;
	}
	/**
	 * 设置：合约名称
	 */
	public void setContractName(String contractName) {
		this.contractName = contractName;
	}
	/**
	 * 获取：合约名称
	 */
	public String getContractName() {
		return contractName;
	}
	/**
	 * 设置：商品类型
	 */
	public void setFuturesType(String futuresType) {
		this.futuresType = futuresType;
	}
	/**
	 * 获取：商品类型
	 */
	public String getFuturesType() {
		return futuresType;
	}
	/**
	 * 设置：每点价值
	 */
	public void setProductDot(BigDecimal productDot) {
		this.productDot = productDot;
	}
	/**
	 * 获取：每点价值
	 */
	public BigDecimal getProductDot() {
		return productDot;
	}
	/**
	 * 设置：跳点
	 */
	public void setUpperTick(BigDecimal upperTick) {
		this.upperTick = upperTick;
	}
	/**
	 * 获取：跳点
	 */
	public BigDecimal getUpperTick() {
		return upperTick;
	}
	/**
	 * 设置：更新日期(yyyyMMdd)
	 */
	public void setRegDate(String regDate) {
		this.regDate = regDate;
	}
	/**
	 * 获取：更新日期(yyyyMMdd)
	 */
	public String getRegDate() {
		return regDate;
	}
	/**
	 * 设置：最后交易日(yyyyMMdd)
	 */
	public void setExpiryDate(String expiryDate) {
		this.expiryDate = expiryDate;
	}
	/**
	 * 获取：最后交易日(yyyyMMdd)
	 */
	public String getExpiryDate() {
		return expiryDate;
	}
	/**
	 * 设置：小数点位数
	 */
	public void setDotNum(Integer dotNum) {
		this.dotNum = dotNum;
	}
	/**
	 * 获取：小数点位数
	 */
	public Integer getDotNum() {
		return dotNum;
	}
	/**
	 * 设置：货币
	 */
	public void setCurrencyNo(String currencyNo) {
		this.currencyNo = currencyNo;
	}
	/**
	 * 获取：货币
	 */
	public String getCurrencyNo() {
		return currencyNo;
	}
	/**
	 * 设置：货币名
	 */
	public void setCurrencyName(String currencyName) {
		this.currencyName = currencyName;
	}
	/**
	 * 获取：货币名
	 */
	public String getCurrencyName() {
		return currencyName;
	}
	/**
	 * 设置：进阶单位
	 */
	public void setLowerTick(Integer lowerTick) {
		this.lowerTick = lowerTick;
	}
	/**
	 * 获取：进阶单位
	 */
	public Integer getLowerTick() {
		return lowerTick;
	}
	/**
	 * 设置：备用交易所
	 */
	public void setExchangeNo2(String exchangeNo2) {
		this.exchangeNo2 = exchangeNo2;
	}
	/**
	 * 获取：备用交易所
	 */
	public String getExchangeNo2() {
		return exchangeNo2;
	}
	/**
	 * 设置：保证金
	 */
	public void setDeposit(BigDecimal deposit) {
		this.deposit = deposit;
	}
	/**
	 * 获取：保证金
	 */
	public BigDecimal getDeposit() {
		return deposit;
	}
	/**
	 * 设置：保证金百分比
	 */
	public void setDepositPercent(BigDecimal depositPercent) {
		this.depositPercent = depositPercent;
	}
	/**
	 * 获取：保证金百分比
	 */
	public BigDecimal getDepositPercent() {
		return depositPercent;
	}
	/**
	 * 设置：首次通知日
	 */
	public void setFirstNoticeDay(String firstNoticeDay) {
		this.firstNoticeDay = firstNoticeDay;
	}
	/**
	 * 获取：首次通知日
	 */
	public String getFirstNoticeDay() {
		return firstNoticeDay;
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
	/**
	 * 设置：内外盘标志
	 */
	public void setCommodityType(String commodityType) {
		this.commodityType = commodityType;
	}
	/**
	 * 获取：内外盘标志
	 */
	public String getCommodityType() {
		return commodityType;
	}
	/**
	 * 设置：拼音名字
	 */
	public void setPyName(String pyName) {
		this.pyName = pyName;
	}
	/**
	 * 获取：拼音名字
	 */
	public String getPyName() {
		return pyName;
	}
	/**
	 * 设置：删除标志
	 */
	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}
	/**
	 * 获取：删除标志
	 */
	public String getDelFlag() {
		return delFlag;
	}
	/**
	 * 设置：最后交易日(过期日)
	 */
	public void setLastTradeDay(String lastTradeDay) {
		this.lastTradeDay = lastTradeDay;
	}
	/**
	 * 获取：最后交易日(过期日)
	 */
	public String getLastTradeDay() {
		return lastTradeDay;
	}
}

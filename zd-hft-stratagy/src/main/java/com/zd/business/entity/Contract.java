package com.zd.business.entity;

import java.math.BigDecimal;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Lists;

import lombok.Data;

@Data
public class Contract {

	private String env = "";// 交易环境
	private String exchangeCode = "";// 交易所
	private String code = "";// 合约代码
	private Integer perFillNums = 0;// 每下一手策略套利单时的单腿下单量,即下单比例
	private String direction = "";// 买卖方向
	private Integer buyNums = 0;// 买量
	private BigDecimal buyPrice = BigDecimal.valueOf(0);// 买价
	private Integer saleNums = 0;// 卖量
	private BigDecimal salePrice = BigDecimal.valueOf(0);// 卖价
	private BigDecimal upperTick = BigDecimal.valueOf(0);// 跳点
	private Integer minMatchNums = 0;// 最小对盘挂单量
	private BigDecimal currPrice = BigDecimal.valueOf(0);// 当前价
	private String userId = "";// 该合约下单使用的交易账户
	private String userPwd = "";// 该合约下单使用的交易账户密码
	private String accountNo="";//该合约下单使用的资金账户
	private String priceType="";//下单类型
	private Integer slipPoint=0;//预设滑点
	private BigDecimal upperLimit=BigDecimal.valueOf(0);//涨停价
	private BigDecimal lowerLimit=BigDecimal.valueOf(0);//跌停价
	private Integer ChasingLimit=0;//价格追单时的点价差

	public String MyToString() {
		return String.join("^",
				Lists.newArrayList(this.env, this.exchangeCode, this.code, String.valueOf(this.perFillNums),
						this.direction, String.valueOf(this.buyNums), this.buyPrice.toString(),
						String.valueOf(this.saleNums), this.salePrice.toString(), this.upperTick.toString(),
						String.valueOf(this.minMatchNums), this.currPrice.toString(), this.userId, this.userPwd,
						this.accountNo,this.priceType,String.valueOf(this.slipPoint),this.upperLimit.toString(),
						this.lowerLimit.toString(),String.valueOf(this.ChasingLimit)));
	}

	public void MyReadString(String temp) {
		try {
			if (StringUtils.isNotBlank(temp)) {
				String[] split = temp.split("\\^");
				if (split.length > 0) {
					this.env = split[0];
				}
				if (split.length > 1) {
					this.exchangeCode = split[1];
				}
				if (split.length > 2) {
					this.code = split[2];
				}
				if (split.length > 3) {
					this.perFillNums = Integer.parseInt(split[3]);
				}
				if (split.length > 4) {
					this.direction = split[4];
				}
				if (split.length > 5) {
					this.buyNums = Integer.parseInt(split[5]);
				}
				if (split.length > 6) {
					this.buyPrice = new BigDecimal(split[6]);
				}
				if (split.length > 7) {
					this.saleNums = Integer.parseInt(split[7]);
				}
				if (split.length > 8) {
					this.salePrice = new BigDecimal(split[8]);
				}
				if (split.length > 9) {
					this.upperTick = new BigDecimal(split[9]);
				}
				if (split.length > 10) {
					this.minMatchNums = Integer.parseInt(split[10]);
				}
				if (split.length > 11) {
					this.currPrice = new BigDecimal(split[11]);
				}
				if (split.length > 12) {
					this.userId = split[12];
				}
				if (split.length > 13) {
					this.userPwd = split[13];
				}
				if (split.length > 14) {
					this.accountNo = split[14];
				}
				if (split.length > 15) {
					this.priceType = split[15];
				}
				if (split.length > 16) {
					this.slipPoint = Integer.parseInt(split[16]);
				}
				if (split.length > 17) {
					this.upperLimit = new BigDecimal(split[17]);
				}
				if (split.length > 18) {
					this.lowerLimit = new BigDecimal(split[18]);
				}
				if (split.length > 19) {
					this.ChasingLimit = Integer.parseInt(split[19]);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}

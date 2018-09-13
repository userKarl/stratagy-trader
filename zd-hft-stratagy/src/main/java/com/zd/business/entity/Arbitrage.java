package com.zd.business.entity;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import lombok.Data;

@Data
public class Arbitrage {

	private String id="";//ID
	private String stratagyId="";//策略Id
	private String name="";//名称
	private Integer buyNums=new Integer(0);//买量
	private Integer saleNums=new Integer(0);//卖量
	private BigDecimal buyPrice=BigDecimal.valueOf(0);//买价
	private BigDecimal salePrice=BigDecimal.valueOf(0);//卖价
	private BigDecimal currPrice=BigDecimal.valueOf(0);//最新价
	private Integer currNums=new Integer(0);//现手
	private String direction="";//买卖方向
	private Integer safeDepth=new Integer(0);//安全深度
	private BigDecimal minMatchMultiple=BigDecimal.valueOf(0);//最小对盘量倍数
	private Integer maxOnceActiveOrderNums=0;//主动腿单次最大下单量（主动腿拆单批量）
	private Integer activeRegion=0;//主动区域
	private Map<String,ArbitrageOrder> orderMap=Maps.newHashMap();//下单集合
	private String autoChasing="";//是否开启自动追单
	private Integer chasingStayInterval=0;//追单时，被动腿挂单时间间隔
	private Integer chasingSlipPoint = 0;// 追单时被动腿预设滑点
	private Integer ChasingLimit = 0;// 价格追单时的点价差
	private String chasingPriceType="";//追单时被动腿价格类型
	private String evenActivePriceType="";//追单平主动时的价格类型
	private Integer evenActiveSlipPoint=0;//追单平主动时滑点
	
	
	public String MyToString() {
		StringBuilder sb=new StringBuilder();
		for(Entry<String,ArbitrageOrder> entry:orderMap.entrySet()) {
			sb.append(entry.getValue().MyToString()).append(",");
		}
		return String.join("$", Lists.newArrayList(this.id,this.stratagyId,this.name,String.valueOf(this.buyNums),
				String.valueOf(this.saleNums),this.buyPrice.toString(),this.salePrice.toString(),
				this.currPrice.toString(),String.valueOf(this.currNums),this.direction,String.valueOf(this.safeDepth),
				this.minMatchMultiple.toString(),String.valueOf(this.maxOnceActiveOrderNums),String.valueOf(this.activeRegion),
				sb.toString(),this.autoChasing,String.valueOf(this.chasingStayInterval),String.valueOf(this.chasingSlipPoint),
				String.valueOf(this.ChasingLimit),this.chasingPriceType,this.evenActivePriceType,String.valueOf(this.evenActiveSlipPoint)));
	}
	
	public void MyReadString(String temp) {
		try {
			if(StringUtils.isNotBlank(temp)) {
				String split[]=temp.split("\\$");
				if(split!=null) {
					if(split.length>0) {
						this.id=split[0];
					}
					if(split.length>1) {
						this.stratagyId=split[1];
					}
					if(split.length>2) {
						this.name=split[2];
					}
					if(split.length>3) {
						this.buyNums=Integer.parseInt(split[3]);
					}
					if(split.length>4) {
						this.saleNums=Integer.parseInt(split[4]);
					}
					if(split.length>5) {
						this.buyPrice=new BigDecimal(split[5]);
					}
					if(split.length>6) {
						this.salePrice=new BigDecimal(split[6]);
					}
					if(split.length>7) {
						this.currPrice=new BigDecimal(split[7]);
					}
					if(split.length>8) {
						this.currNums=Integer.parseInt(split[8]);
					}
					if(split.length>9) {
						this.direction=split[9];
					}
					if(split.length>10) {
						this.safeDepth=Integer.parseInt(split[10]);
					}
					if(split.length>11) {
						this.minMatchMultiple=new BigDecimal(split[11]);
					}
					if(split.length>12) {
						this.maxOnceActiveOrderNums=Integer.parseInt(split[12]);
					}
					if(split.length>13) {
						this.activeRegion=Integer.parseInt(split[13]);
					}
					if(split.length>14) {
						if(StringUtils.isNotBlank(split[14])) {
							String[] split2 = split[14].split(",");
							for(String s:split2) {
								ArbitrageOrder arbitrageOrder=new ArbitrageOrder();
								arbitrageOrder.MyReadString(s);
								this.orderMap.put(arbitrageOrder.getArbitrageOrderId(), arbitrageOrder);
							}
						}
					}
					if(split.length>15) {
						this.autoChasing=split[15];
					}
					if(split.length>16) {
						this.chasingStayInterval=Integer.parseInt(split[16]);
					}
					if(split.length>17) {
						this.chasingSlipPoint=Integer.parseInt(split[17]);
					}
					if(split.length>18) {
						this.ChasingLimit=Integer.parseInt(split[18]);
					}
					if(split.length>19) {
						this.chasingPriceType=split[19];
					}
					if(split.length>20) {
						this.evenActivePriceType=split[20];
					}
					if(split.length>21) {
						this.evenActiveSlipPoint=Integer.parseInt(split[21]);
					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}

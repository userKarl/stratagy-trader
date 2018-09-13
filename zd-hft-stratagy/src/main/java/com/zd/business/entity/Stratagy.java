package com.zd.business.entity;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Lists;

import lombok.Data;

/**
 * 策略类 被动腿合约接收行情，根据公式计算出主动腿合约的下单价格
 * 
 * @author user
 *
 */
@Data
public class Stratagy {

	private String id=""; // 策略ID
	private String name=""; // 策略名称
	private String type=""; // 策略类型（M市商，A套利）
	private String status=""; // 策略状态
	private String expression=""; // 套利公式(四腿时在客户端输入分别为P1,P2,P3,P4;服务器接收到数据后需要将P1替换成m)
	private List<Contract> marketContractList=null;//被动腿合约
	private Contract activeContract=null; // 主动腿合约
	private Arbitrage arbitrage=null;//套利

	public void MyReadString(String temp) {
		try {
			if (StringUtils.isNotBlank(temp)) {
				String[] split = temp.split("@");
				if (split != null) {
					if (split.length > 0) {
						this.id = split[0];
					}
					if (split.length > 1) {
						this.name = split[1];
					}
					if (split.length > 2) {
						this.type = split[2];
					}
					if (split.length > 3) {
						this.status = split[3];
					}
					if (split.length > 4) {
						this.expression = split[4];
					}
					if (split.length > 5) {
						if(StringUtils.isNotBlank(split[5])) {
							String contracts[]=split[5].split("\\!");
							List<Contract> list=Lists.newArrayList();
							for(String s:contracts) {
								Contract contract=new Contract();
								contract.MyReadString(s);
								list.add(contract);
							}
							this.marketContractList = list;
						}
					}
					if (split.length > 6) {
						Contract contract=new Contract();
						contract.MyReadString(split[6]);
						this.activeContract = contract;
					}
					if (split.length > 7) {
						Arbitrage arbitrage=new Arbitrage();
						arbitrage.MyReadString(split[7]);
						this.arbitrage = arbitrage;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	public String MyToString() {
		StringBuilder sb=new StringBuilder();
		if(this.marketContractList!=null && this.marketContractList.size()>0) {
			for(Contract c:this.marketContractList) {
				sb.append(c.MyToString()).append("!");
			}
		}
		String activeContractStr="";
		String arbitrageStr="";
		if(this.activeContract!=null) {
			activeContractStr=this.activeContract.MyToString();
		}
		if(this.arbitrage!=null) {
			arbitrageStr=this.arbitrage.MyToString();
		}
		return String.join("@", Lists.newArrayList(this.id, this.name, this.type,this.status,this.expression,
				sb.toString(),activeContractStr,arbitrageStr));
	}

}

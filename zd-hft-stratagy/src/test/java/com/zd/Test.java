package com.zd;

import java.math.BigDecimal;
import java.util.List;

import com.google.common.collect.Lists;
import com.zd.business.common.BigDeciamalAnalysize;
import com.zd.business.entity.ContractOrder;

public class Test {

	public static void main(String[] args) {
		
		aaa();
		
	}
	public static void aaa() {
		for(int i=0;i<100;i++) {
			long l=System.nanoTime();
			ContractOrder co1=new ContractOrder();
			co1.setRequestPrice(BigDecimal.valueOf(1));
			ContractOrder co2=new ContractOrder();
			co2.setRequestPrice(BigDecimal.valueOf(2));
			ContractOrder co3=new ContractOrder();
			co3.setRequestPrice(BigDecimal.valueOf(3));
			ContractOrder co4=new ContractOrder();
			co4.setRequestPrice(BigDecimal.valueOf(4));
			List<ContractOrder> list=Lists.newArrayList(co2,co3,co4);
			
			BigDecimal value=BigDecimal.valueOf(18);
			List<String> currPriceList = Lists.newArrayList(co1.getRequestPrice().toString());
			String expression="P1+P2+P3+P4";
			if(list.size()==1) {
				expression=expression.replace("P2", "m");
				currPriceList.add("m");
				System.out.println("2腿时P2价格："+BigDeciamalAnalysize.cacl(expression, currPriceList, value));
			}else if(list.size()==2) {
				//计算P2价格
				expression=expression.replace("P2", "m");
				currPriceList.add("m");
				currPriceList.add(String.valueOf(list.get(1).getRequestPrice()));
				System.out.println("3腿时P2价格："+BigDeciamalAnalysize.cacl(expression, currPriceList, value));
				expression=expression.replace("m", "P2");
				currPriceList.remove(currPriceList.size()-1);
				currPriceList.remove(currPriceList.size()-1);
				//计算P3价格
				expression=expression.replace("P3", "m");
				currPriceList.add(String.valueOf(list.get(0).getRequestPrice()));
				currPriceList.add("m");
				System.out.println("3腿时P3价格："+BigDeciamalAnalysize.cacl(expression, currPriceList, value));
			}else if(list.size()==3) {
				//计算P2价格
				expression=expression.replace("P2", "m");
				currPriceList.add("m");
				currPriceList.add(String.valueOf(list.get(1).getRequestPrice()));
				currPriceList.add(String.valueOf(list.get(2).getRequestPrice()));
//				System.out.println("4腿时P2价格："+BigDeciamalAnalysize.cacl(expression, currPriceList, value));
				expression=expression.replace("m", "P2");
				currPriceList.remove(currPriceList.size()-1);
				currPriceList.remove(currPriceList.size()-1);
				currPriceList.remove(currPriceList.size()-1);
				//计算P3价格
				expression=expression.replace("P3", "m");
				currPriceList.add(String.valueOf(list.get(0).getRequestPrice()));
				currPriceList.add("m");
				currPriceList.add(String.valueOf(list.get(2).getRequestPrice()));
//				System.out.println("4腿时P3价格："+BigDeciamalAnalysize.cacl(expression, currPriceList, value));
				expression=expression.replace("m", "P3");
				currPriceList.remove(currPriceList.size()-1);
				currPriceList.remove(currPriceList.size()-1);
				currPriceList.remove(currPriceList.size()-1);
				//计算P4价格
				expression=expression.replace("P4", "m");
				currPriceList.add(String.valueOf(list.get(0).getRequestPrice()));
				currPriceList.add(String.valueOf(list.get(1).getRequestPrice()));
				currPriceList.add("m");
//				System.out.println("4腿时P4价格："+BigDeciamalAnalysize.cacl(expression, currPriceList, value));
			}
			
			System.out.println("耗时："+(System.nanoTime()-l)/1e6);
		}
	}
	
	public static void test() {
		long l=System.nanoTime();
		ContractOrder co1=new ContractOrder();
		co1.setRequestPrice(BigDecimal.valueOf(1));
		ContractOrder co2=new ContractOrder();
		co2.setRequestPrice(BigDecimal.valueOf(2));
		ContractOrder co3=new ContractOrder();
		co3.setRequestPrice(BigDecimal.valueOf(3));
		ContractOrder co4=new ContractOrder();
		co4.setRequestPrice(BigDecimal.valueOf(4));
		List<ContractOrder> list=Lists.newArrayList(co2,co3,co4);
		
		BigDecimal value=BigDecimal.valueOf(18);
		List<String> currPriceList = Lists.newArrayList(co1.getRequestPrice().toString());
		String expression="P1+P2+P3+P4";
		
		
		if(list.size()==1) {
			expression=expression.replace("P2", "m");
			currPriceList.add("m");
			System.out.println("2腿时P2价格："+BigDeciamalAnalysize.cacl(expression, currPriceList, value));
		}else if(list.size()==2) {
			//计算P2价格
			expression=expression.replace("P2", "m");
			currPriceList.add("m");
			currPriceList.add(String.valueOf(list.get(1).getRequestPrice()));
			System.out.println("3腿时P2价格："+BigDeciamalAnalysize.cacl(expression, currPriceList, value));
			expression=expression.replace("m", "P2");
			currPriceList.remove(currPriceList.size()-1);
			currPriceList.remove(currPriceList.size()-1);
			//计算P3价格
			expression=expression.replace("P3", "m");
			currPriceList.add(String.valueOf(list.get(0).getRequestPrice()));
			currPriceList.add("m");
			System.out.println("3腿时P3价格："+BigDeciamalAnalysize.cacl(expression, currPriceList, value));
		}else if(list.size()==3) {
			//计算P2价格
			expression=expression.replace("P2", "m");
			currPriceList.add("m");
			currPriceList.add(String.valueOf(list.get(1).getRequestPrice()));
			currPriceList.add(String.valueOf(list.get(2).getRequestPrice()));
//			System.out.println("4腿时P2价格："+BigDeciamalAnalysize.cacl(expression, currPriceList, value));
			expression=expression.replace("m", "P2");
			currPriceList.remove(currPriceList.size()-1);
			currPriceList.remove(currPriceList.size()-1);
			currPriceList.remove(currPriceList.size()-1);
			//计算P3价格
			expression=expression.replace("P3", "m");
			currPriceList.add(String.valueOf(list.get(0).getRequestPrice()));
			currPriceList.add("m");
			currPriceList.add(String.valueOf(list.get(2).getRequestPrice()));
//			System.out.println("4腿时P3价格："+BigDeciamalAnalysize.cacl(expression, currPriceList, value));
			expression=expression.replace("m", "P3");
			currPriceList.remove(currPriceList.size()-1);
			currPriceList.remove(currPriceList.size()-1);
			currPriceList.remove(currPriceList.size()-1);
			//计算P4价格
			expression=expression.replace("P4", "m");
			currPriceList.add(String.valueOf(list.get(0).getRequestPrice()));
			currPriceList.add(String.valueOf(list.get(1).getRequestPrice()));
			currPriceList.add("m");
//			System.out.println("4腿时P4价格："+BigDeciamalAnalysize.cacl(expression, currPriceList, value));
		}
		
		System.out.println("耗时："+(System.nanoTime()-l)/1e6);
	}
}

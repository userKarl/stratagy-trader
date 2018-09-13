package com.zd.business.constant;

public class RedisConst {

	public static final String STRATAGY="STRATAGY";//策略
	
	public static final String STRATAGY_ARBITRAGE="STRATAGY_ARBITRAGE";//套利
	
	public static final String STRATAGY_ORDER="STRATAGY_ORDER";//套利下单
	
	public static final String MAXORDERREFKEY="MAXORDERREFKEY";//最大报单ID
	
	public static final String DEALINFOKEY="DEALINFOKEY";//成交信息key
	
	public static final String LOCALNO_SYSTEMNO="LOCALNO_SYSTEMNO";//本地号与系统号映射
	
	public static final String SYSTEMNO_LOCALNO="SYSTEMNO_LOCALNO";//系统号与本地号映射
	
	public static final String ORDER_SYSTEMNO_CANCEL_LOCALNO="ORDER_SYSTEMNO_CANCEL_LOCALNO";//挂单系统号与撤单本地号映射
	
	//NetInfo指令的localSystemCode所对应的报单类
	public static final String NETINFO_LOCALNO_TRADERREF="NETINFO_LOCALNO_TRADERREF";
	
	//下单Id对应的报单类
	public static final String CONTRACTORDER_TRADERREF="ORDER_TRADERREF";
	
	//策略合约下单Id对应的挂单信息
	public static final String CONTRACTORDER_ORDERINFO="CONTRACTORDER_ORDERINFO";
	
	//策略合约下单Id对应的账号信息
	public static final String CONTRACTORDER_USERID="CONTRACTORDER_USERID";
	
	//策略合约下单信息
	public static final String CONTRACTORDERINFO="CONTRACTORDERINFO";
}

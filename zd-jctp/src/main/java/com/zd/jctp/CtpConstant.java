package com.zd.jctp;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import com.zd.common.ZdConstant;

/**
 * @author sun0x00@gmail.com
 */
public class CtpConstant {
	public static Map<String,Character> priceTypeMap = new HashMap<>();
	public static Map<Character,String> priceTypeMapReverse = new HashMap<>();
	
	public static Map<String,Character> directionMap = new HashMap<>();
	public static Map<Character,String> directionMapReverse = new HashMap<>();
	
	public static Map<String,Character> offsetMap = new HashMap<>();
	public static Map<Character,String> offsetMapReverse = new HashMap<>();
	
	public static Map<String,String> exchangeMap = new HashMap<>();
	public static Map<String,String> exchangeMapReverse = new HashMap<>();
	

	public static Map<String,Character> posiDirectionMap = new HashMap<>();
	public static Map<Character,String> posiDirectionMapReverse = new HashMap<>();
	
	public static Map<String,Character> productClassMap = new HashMap<>();
	public static Map<Character,String> productClassMapReverse = new HashMap<>();
	

	public static Map<String,Character> statusMap = new HashMap<>();
	public static Map<Character,String> statusMapReverse = new HashMap<>();
	
	static {
		
		// 价格类型映射
		priceTypeMap.put(ZdConstant.PRICETYPE_LIMITPRICE, jctptraderapiv6v3v11x64Constants.THOST_FTDC_OPT_LimitPrice);
		priceTypeMap.put(ZdConstant.PRICETYPE_MARKETPRICE, jctptraderapiv6v3v11x64Constants.THOST_FTDC_OPT_AnyPrice);
		priceTypeMapReverse = priceTypeMap.entrySet().stream().collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));
		
		// 方向类型映射
		directionMap.put(ZdConstant.DIRECTION_LONG, jctptraderapiv6v3v11x64Constants.THOST_FTDC_D_Buy);
		directionMap.put(ZdConstant.DIRECTION_SHORT, jctptraderapiv6v3v11x64Constants.THOST_FTDC_D_Sell);
		directionMapReverse = directionMap.entrySet().stream().collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));
		
		// 开平类型映射
		offsetMap.put(ZdConstant.OFFSET_OPEN, jctptraderapiv6v3v11x64Constants.THOST_FTDC_OF_Open);
		offsetMap.put(ZdConstant.OFFSET_CLOSE, jctptraderapiv6v3v11x64Constants.THOST_FTDC_OF_Close);
		offsetMap.put(ZdConstant.OFFSET_CLOSETODAY, jctptraderapiv6v3v11x64Constants.THOST_FTDC_OF_CloseToday);
		offsetMap.put(ZdConstant.OFFSET_CLOSEYESTERDAY, jctptraderapiv6v3v11x64Constants.THOST_FTDC_OF_CloseYesterday);
		offsetMapReverse = offsetMap.entrySet().stream().collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));
		
		// 交易所映射
		exchangeMap.put(ZdConstant.EXCHANGE_CFFEX, "CFFEX");
		exchangeMap.put(ZdConstant.EXCHANGE_SHFE, "SHFE");
		exchangeMap.put(ZdConstant.EXCHANGE_CZCE, "CZCE");
		exchangeMap.put(ZdConstant.EXCHANGE_DCE, "DCE");
		exchangeMap.put(ZdConstant.EXCHANGE_SSE, "SSE");
		exchangeMap.put(ZdConstant.EXCHANGE_SZSE, "SZSE");
		exchangeMap.put(ZdConstant.EXCHANGE_INE, "INE");
		exchangeMap.put(ZdConstant.EXCHANGE_UNKNOWN, "");
		exchangeMapReverse = exchangeMap.entrySet().stream().collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));
		
		//持仓类型映射
		posiDirectionMap.put(ZdConstant.DIRECTION_NET, jctptraderapiv6v3v11x64Constants.THOST_FTDC_PD_Net);
		posiDirectionMap.put(ZdConstant.DIRECTION_LONG, jctptraderapiv6v3v11x64Constants.THOST_FTDC_PD_Long);
		posiDirectionMap.put(ZdConstant.DIRECTION_SHORT, jctptraderapiv6v3v11x64Constants.THOST_FTDC_PD_Short);
		posiDirectionMapReverse = posiDirectionMap.entrySet().stream().collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));
		

		// 产品类型映射
		productClassMap.put(ZdConstant.PRODUCT_FUTURES, jctptraderapiv6v3v11x64Constants.THOST_FTDC_PC_Futures);
		productClassMap.put(ZdConstant.PRODUCT_OPTION, jctptraderapiv6v3v11x64Constants.THOST_FTDC_PC_Options);
		productClassMap.put(ZdConstant.PRODUCT_COMBINATION, jctptraderapiv6v3v11x64Constants.THOST_FTDC_PC_Combination);
		productClassMapReverse = productClassMap.entrySet().stream().collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));
//		v6.3.11不支持个股期权
//		productClassMapReverse.put(jctptraderapiv6v3v11x64Constants.THOST_FTDC_PC_ETFOption, ZdConstant.PRODUCT_OPTION);
//		productClassMapReverse.put(jctptraderapiv6v3v11x64Constants.THOST_FTDC_PC_S, ZdConstant.PRODUCT_EQUITY);
		
		// 委托状态映射
		statusMap.put(ZdConstant.STATUS_ALLTRADED, jctptraderapiv6v3v11x64Constants.THOST_FTDC_OST_AllTraded);
		statusMap.put(ZdConstant.STATUS_PARTTRADED, jctptraderapiv6v3v11x64Constants.THOST_FTDC_OST_PartTradedQueueing);
		statusMap.put(ZdConstant.STATUS_NOTTRADED, jctptraderapiv6v3v11x64Constants.THOST_FTDC_OST_NoTradeQueueing);
		statusMap.put(ZdConstant.STATUS_CANCELLED, jctptraderapiv6v3v11x64Constants.THOST_FTDC_OST_Canceled);
		statusMap.put(ZdConstant.STATUS_UNKNOWN, jctptraderapiv6v3v11x64Constants.THOST_FTDC_OST_Unknown);
		statusMapReverse = statusMap.entrySet().stream().collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));
		
		
		
	}
}

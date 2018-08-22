package com.zd.common;

import java.util.HashSet;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class ZdConstant {

		// 方向常量
		public static final String DIRECTION_NONE = "无方向";
		public static final String DIRECTION_LONG = "多";
		public static final String DIRECTION_SHORT = "空";
		public static final String DIRECTION_UNKNOWN = "未知";
		public static final String DIRECTION_NET = "净";
		public static final String DIRECTION_SELL = "卖出"; // IB接口
		public static final String DIRECTION_COVEREDSHORT = "备兑空"; // 证券期权

		// 开平常量
		public static final String OFFSET_NONE = "无开平";
		public static final String OFFSET_OPEN = "开仓";
		public static final String OFFSET_CLOSE = "平仓";
		public static final String OFFSET_CLOSETODAY = "平今";
		public static final String OFFSET_CLOSEYESTERDAY = "平昨";
		public static final String OFFSET_UNKNOWN = "未知";

		// 状态常量
		public static final String STATUS_NOTTRADED = "未成交";
		public static final String STATUS_PARTTRADED = "部分成交";
		public static final String STATUS_ALLTRADED = "全部成交";
		public static final String STATUS_CANCELLED = "已撤销";
		public static final String STATUS_REJECTED = "拒单";
		public static final String STATUS_UNKNOWN = "未知";
		
		public static HashSet<String> STATUS_FINISHED = new HashSet<String>() {
			private static final long serialVersionUID = 8777691797309945190L;
			{
				add(ZdConstant.STATUS_REJECTED);
				add(ZdConstant.STATUS_CANCELLED);
				add(ZdConstant.STATUS_ALLTRADED);
			}
		};
		
		public static HashSet<String> STATUS_WORKING = new HashSet<String>() {
			private static final long serialVersionUID = 909683985291870766L;
			{
				add(ZdConstant.STATUS_UNKNOWN);
				add(ZdConstant.STATUS_NOTTRADED);
				add(ZdConstant.STATUS_PARTTRADED);
			}
		};

		// 合约类型常量
		public static final String PRODUCT_EQUITY = "股票";
		public static final String PRODUCT_FUTURES = "期货";
		public static final String PRODUCT_OPTION = "期权";
		public static final String PRODUCT_INDEX = "指数";
		public static final String PRODUCT_COMBINATION = "组合";
		public static final String PRODUCT_FOREX = "外汇";
		public static final String PRODUCT_UNKNOWN = "未知";
		public static final String PRODUCT_SPOT = "现货";
		public static final String PRODUCT_DEFER = "延期";
		public static final String PRODUCT_ETF = "ETF";
		public static final String PRODUCT_WARRANT = "权证";
		public static final String PRODUCT_BOND = "债券";
		public static final String PRODUCT_NONE = "";

		// 价格类型常量
		public static final String PRICETYPE_LIMITPRICE = "限价";
		public static final String PRICETYPE_MARKETPRICE = "市价";
		public static final String PRICETYPE_FAK = "FAK";
		public static final String PRICETYPE_FOK = "FOK";

		// 期权类型
		public static final String OPTION_CALL = "看涨期权";
		public static final String OPTION_PUT = "看跌期权";

		// 交易所类型
		public static final String EXCHANGE_SSE = "SSE"; // 上交所
		public static final String EXCHANGE_SZSE = "SZSE"; // 深交所
		public static final String EXCHANGE_CFFEX = "CFFEX"; // 中金所
		public static final String EXCHANGE_SHFE = "SHFE"; // 上期所
		public static final String EXCHANGE_CZCE = "CZCE"; // 郑商所
		public static final String EXCHANGE_DCE = "DCE"; // 大商所
		public static final String EXCHANGE_SGE = "SGE"; // 上金所
		public static final String EXCHANGE_INE = "INE"; // 国际能源交易中心
		public static final String EXCHANGE_UNKNOWN = "UNKNOWN";// 未知交易所
		public static final String EXCHANGE_NONE = ""; // 空交易所
		public static final String EXCHANGE_HKEX = "HKEX"; // 港交所
		public static final String EXCHANGE_HKFE = "HKFE"; // 香港期货交易所

		public static final String EXCHANGE_SMART = "SMART"; // IB智能路由（股票、期权）
		public static final String EXCHANGE_NYMEX = "NYMEX"; // IB 期货
		public static final String EXCHANGE_GLOBEX = "GLOBEX"; // CME电子交易平台
		public static final String EXCHANGE_IDEALPRO = "IDEALPRO"; // IB外汇ECN

		public static final String EXCHANGE_CME = "CME"; // CME交易所
		public static final String EXCHANGE_ICE = "ICE"; // ICE交易所
		public static final String EXCHANGE_LME = "LME"; // LME交易所

		public static final String EXCHANGE_OANDA = "OANDA"; // OANDA外汇做市商
		public static final String EXCHANGE_FXCM = "FXCM"; // FXCM外汇做市商

		public static final String EXCHANGE_OKCOIN = "OKCOIN"; // OKCOIN比特币交易所
		public static final String EXCHANGE_HUOBI = "HUOBI"; // 火币比特币交易所
		public static final String EXCHANGE_LBANK = "LBANK"; // LBANK比特币交易所
		public static final String EXCHANGE_KORBIT = "KORBIT"; // KORBIT韩国交易所
		public static final String EXCHANGE_ZB = "ZB"; // 比特币中国比特币交易所
		public static final String EXCHANGE_OKEX = "OKEX"; // OKEX比特币交易所
		public static final String EXCHANGE_ZAIF = "ZAIF"; // ZAIF日本比特币交易所
		public static final String EXCHANGE_COINCHECK = "COINCHECK"; // COINCHECK日本比特币交易所

		// 货币类型
		public static final String CURRENCY_USD = "USD"; // 美元
		public static final String CURRENCY_CNY = "CNY"; // 人民币
		public static final String CURRENCY_HKD = "HKD"; // 港币
		public static final String CURRENCY_UNKNOWN = "UNKNOWN"; // 未知货币
		public static final String CURRENCY_NONE = ""; // 空货币

		// 接口类型
		public static final String GATEWAYTYPE_EQUITY = "equity"; // 股票、ETF、债券
		public static final String GATEWAYTYPE_FUTURES = "futures"; // 期货、期权、贵金属
		public static final String GATEWAYTYPE_INTERNATIONAL = "international"; // 外盘
		public static final String GATEWAYTYPE_BTC = "btc"; // 比特币
		public static final String GATEWAYTYPE_DATA = "data"; // 数据（非交易）

		public static final String RED_TORCH_DB_NAME = "redtorch_j_db";
		public static final String LOG_DEBUG = "DEBUG";
		public static final String LOG_INFO = "INFO";
		public static final String LOG_WARN = "WARN";
		public static final String LOG_ERROR = "ERROR";

		public static final String DT_FORMAT_WITH_MS = "yyyy-MM-dd HH:mm:ss.SSS";
		public static final String DT_FORMAT_WITH_MS_INT = "yyyyMMddHHmmssSSS";
		public static final String DT_FORMAT = "yyyy-MM-dd HH:mm:ss";
		public static final String DT_FORMAT_INT = "yyyyMMddHHmmss";

		public static final String T_FORMAT_WITH_MS_INT = "HHmmssSSS";
		public static final String T_FORMAT_WITH_MS = "HH:mm:ss.SSS";
		public static final String T_FORMAT_INT = "HHmmss";
		public static final String T_FORMAT = "HH:mm:ss";
		public static final String D_FORMAT_INT = "yyyyMMdd";
		public static final String D_FORMAT = "yyyy-MM-dd";

		public static final DateTimeFormatter DT_FORMAT_WITH_MS_FORMATTER = DateTimeFormat
				.forPattern(ZdConstant.DT_FORMAT_WITH_MS);
		public static final DateTimeFormatter DT_FORMAT_WITH_MS_INT_FORMATTER = DateTimeFormat
				.forPattern(ZdConstant.DT_FORMAT_WITH_MS_INT);
		public static final DateTimeFormatter DT_FORMAT_FORMATTER = DateTimeFormat.forPattern(ZdConstant.DT_FORMAT);
		public static final DateTimeFormatter DT_FORMAT_INT_FORMATTER = DateTimeFormat.forPattern(ZdConstant.DT_FORMAT_INT);

		public static final DateTimeFormatter T_FORMAT_WITH_MS_INT_FORMATTER = DateTimeFormat
				.forPattern(ZdConstant.T_FORMAT_WITH_MS_INT);
		public static final DateTimeFormatter T_FORMAT_WITH_MS_FORMATTER = DateTimeFormat.forPattern(ZdConstant.T_FORMAT_WITH_MS);
		public static final DateTimeFormatter T_FORMAT_INT_FORMATTER = DateTimeFormat.forPattern(ZdConstant.T_FORMAT_INT);
		public static final DateTimeFormatter T_FORMAT_FORMATTER = DateTimeFormat.forPattern(ZdConstant.T_FORMAT);

		public static final DateTimeFormatter D_FORMAT_INT_FORMATTER = DateTimeFormat.forPattern(ZdConstant.D_FORMAT_INT);
		public static final DateTimeFormatter D_FORMAT_FORMATTER = DateTimeFormat.forPattern(ZdConstant.D_FORMAT);
}

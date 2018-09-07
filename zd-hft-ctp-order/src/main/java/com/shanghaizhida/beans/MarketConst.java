package com.shanghaizhida.beans;

/**
 * 绘制图形时请求数据或其他地方用到的参数定义
 * 
 * @author xiang <br>
 *         2015年8月5日 上午11:09:07
 * 
 */
public class MarketConst {

	/** Tick线 */
	public static String TICK = "0";

	/** Time线 */
	public static String TIME = "1";

	/** 多日Time线 */
	public static String DAYS_TIME = "2";

	/** 30秒线 */
	public static String SEC30 = "3";

	/** 一分钟线 */
	public static String MIN01 = "4";

	/** 三分钟线 */
	public static String MIN03 = "5";

	/** 五分钟线 */
	public static String MIN05 = "6";

	/** 十分钟线 */
	public static String MIN10 = "7";

	/** 十五分钟线 */
	public static String MIN15 = "8";

	/** 三十分钟线 */
	public static String MIN30 = "9";

	/** 六十分钟线 */
	public static String MIN60 = "10";

	/** 120分钟线 */
	public static String MIN120 = "11";

	/** 180分钟线 */
	public static String MIN180 = "12";

	/** 240分钟线 */
	public static String MIN240 = "13";

	/** 日线 */
	public static String DAY = "14";

	/** 周线 */
	public static String WEEK = "15";

	/** 月线 */
	public static String MONTH = "16";

	/** 年线 */
	public static String YEAR = "17";

	/** 任意秒 */
	public static String ANY_SECONDS = "18";

	/** 任意分钟 */
	public static String ANY_MINUTES = "19";

	/** 任意天 */
	public static String ANY_DAYS = "20";

	/** 数据从文件取得 */
	public static String FILE_LOC_FILE = "1";

	/** 数据从消息服务器取得 */
	public static String FILE_LOC_MESSAGE = "2";

	/** 数据从行情服务器取得 */
	public static String FILE_LOC_MARKET = "3";

	/** 秒间隔 */
	public static String INETERVAL_SECOND = "1";

	/** 分钟间隔 */
	public static String INETERVAL_MINUTE = "2";

	/** 日间隔 */
	public static String INETERVAL_DAY = "3";

	/** 周间隔 */
	public static String INETERVAL_WEEK = "4";

	/** 月间隔 */
	public static String INETERVAL_MONTH = "5";

	/** 年间隔 */
	public static String INETERVAL_YEAR = "6";

	/** 消息服务器请求TIME-TIME格式 */
	public static String SEND_MESSAGE_TT = "TT:";

	/** 消息服务器请求TIME-OFFSET格式 */
	public static String SEND_MESSAGE_TO = "TO:";

	/** 交易日 */
	public static String TRADE_DAY = "MD";

	/** 压缩标志 */
	public static String COMPRESS = "-C";

	/** 夏令时 */
	public static String DST_SUMMER = "1";

	/** 冬令时 */
	public static String DST_WINTER = "2";

	/** 无冬夏令时 */
	public static String DST_NONE = "0";

	/** 夏令冬令信息 */
	public static String DST_INFO = "SW";

	/** 商品开闭市时间<商品交易时间> */
	public static String TRADE_TIME_INFO = "CS";

	/** 窗体最大化 */
	public static String WIN_MAX = "3";

	/** 窗体最小化 */
	public static String WIN_MIN = "1";

	/** 显示隐藏价格 */
	public static String SHOW_HPRICE = "1";

	/** 不显示隐藏价格 */
	public static String UNSHOW_HPRICE = "0";

	/** 商品开闭市时间 */
	public static String MAIN_CONTRACT_INFO = "MainC";

	/** 成交明细 */
	public static String TRANSACTION_DETAIL = "T";
}

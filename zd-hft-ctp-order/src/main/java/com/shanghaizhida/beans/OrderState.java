package com.shanghaizhida.beans;

public class OrderState {

	// 订单请求
	public static String OrderRequest = "1";

	// 订单排队
	public static String OrderWait = "2";

	// 部分成交
	public static String OrderPartSuccess = "3";

	// 全部成交
	public static String OrderAllSuccess = "4";

	// 部分撤单
	public static String OrderPartCancel = "5";

	// 全部撤单
	public static String OrderAllCancel = "6";

	// 订单请求失败
	public static String OrderRequestFailed = "7";

	// 订单请求
	public static int OrderRequestCd = 100001;

	// 订单排队
	public static int OrderWaitCd = 100002;

	// 部分成交
	public static int OrderPartSuccessCd = 100003;

	// 全部成交
	public static int OrderAllSuccessCd = 100004;

	// 部分撤单
	public static int OrderPartCancelCd = 100005;

	// 全部撤单
	public static int OrderAllCancelCd = 100006;

	// 订单请求失败
	public static int OrderRequestFailedCd = 100007;

}

package com.shanghaizhida.beans;

public class OrderTypeCheck {

	/**
	 * 
	 * @param exchangeNo
	 *            交易所
	 * @param commodityNo
	 *            品种
	 * @param orderType
	 *            ordertype的值：1=限价, 2=市价 ，3=限价止损；4=市价止损
	 * @param validate
	 *            有效日期的值：1：当日有效；2：永久有效
	 * @param IsMoNiFlag
	 *            是否模拟盘
	 * @param CanMarketOrderHKEXFlag
	 *            是否可下香港市价单
	 * @return
	 */
	public static boolean checkIsSupportOrderType(String exchangeNo,
			String commodityNo, int orderType, int validate,
			boolean IsMoNiFlag, boolean CanMarketOrderHKEXFlag) {

		boolean ret = true;

		try {
			// 永久有效的市价单都不支持
			if (validate == 2 && (orderType == 2 || orderType == 4)) {
				// 需支持CME永久有效的市价止损单类型
//				if (exchangeNo == "CME"
//						|| exchangeNo == "CME_CBT"
//						|| exchangeNo == "NYBOT"
//						|| exchangeNo == "BMD"
//						|| (exchangeNo == "eCBOT" && orderType == 2)
//						|| (exchangeNo == "TOCOM" && !commodityNo
//								.startsWith("RSS"))) {
//				} else {
//					return false;
//				}
			}

			if (exchangeNo.equals("CME")) {
			} else if (exchangeNo.equals("CME_CBT")) {
			} else if (exchangeNo.equals("eCBOT")) {

				// 小型黄金,不支持永久有效的限价止损单和市价止损单
				if (commodityNo.startsWith("C-YG")) {
					if (validate == 2) {
						if (orderType == 3 || orderType == 4) {
							ret = false;
						}
					}
				} else if (commodityNo.startsWith("C-YI"))// 小型白银,不支持永久有效的限价止损单和市价止损单
				{
					if (validate == 2) {
						if (orderType == 3 || orderType == 4) {
							ret = false;
						}
					}
				}

			} else if (exchangeNo.equals("NYBOT")) {

				// 只支持当日有效的限价单和限价止损单
				// if (commodityNo.startsWith("CC")// 可可
				// || commodityNo.startsWith("KC")// 咖啡
				// || commodityNo.startsWith("CT")// 棉花
				// || commodityNo.startsWith("SB")// 11号糖
				// || commodityNo.startsWith("DX"))// 美元指数
				// {
				// if (!(validate == 1 && (orderType == 1 || orderType == 3))) {
				// ret = false;
				// }
				// }

			} else if (exchangeNo.equals("ICE")) {

				// 只支持当日有效的限价市价限价止损单
//				if (commodityNo.startsWith("BRN")// 布兰特油
//						|| commodityNo.startsWith("GAS")// 柴油
//						|| commodityNo.startsWith("WBS"))// 西德州原油
//				{
					if (!(validate == 1 && (orderType == 3 || orderType == 1 || orderType == 2))) {
						ret = false;
					}
//				}

			} else if (exchangeNo.equals("XEurex")) {

				// 只支持当日有效的限价市价限价止损单
//				if (commodityNo.startsWith("FESX")// 道琼欧盟50指数
//						|| commodityNo.startsWith("FDAX"))// 德国DAX指数
//				{
					if (!(validate == 1 && (orderType == 1 || orderType == 2 || orderType == 3))) {
						ret = false;
					}
//				}

			} else if (exchangeNo.equals("SGXQ")) {

				// 不支持永久有效单
//				if (commodityNo.startsWith("JB")// 10年期小型日本國債
//						|| commodityNo.startsWith("TW")// 摩根台湾指数
//						|| commodityNo.startsWith("NK")// 日经平均指数225
//						|| commodityNo.startsWith("CN"))// 新加坡A50
//				{
					if (validate == 2) {
						ret = false;
					}
//				}

			} else if (exchangeNo.equals("TOCOM")) {

				// 橡胶,不支持永久有效单
				if (commodityNo.startsWith("RSS")) {
					if (validate == 2) {
						ret = false;
					}
				}

			} else if (exchangeNo.equals("HKEX")) {

				// 只支持当日的限价和限价止损单
				// if (commodityNo.startsWith("HHI")// 国企指数
				// || commodityNo.startsWith("HSI")// 恒指
				// || commodityNo.startsWith("CUS")// 人民币期货
				// || commodityNo.startsWith("MCH")// 小国企指数
				// || commodityNo.startsWith("MHI"))// 小恒指
				// {
				// if (!(validate == 1 && (orderType == 1 || orderType == 3))) {
				// ret = false;
				// // 如果是模拟盘，并且配置了可以下香港市场市价单的话可以下单
				// if (IsMoNiFlag && validate == 1
				// && (orderType == 2 || orderType == 4)) {
				// if (CanMarketOrderHKEXFlag) {
				// ret = true;
				// }
				// }
				// }
				// }

				// 只支持当日的限价单
				if (!(validate == 1 && orderType == 1)) {
					ret = false;
				}
			} else if (exchangeNo.equals("LME")) {

				// 只支持限价单和永久有效限价单
//				if (commodityNo.startsWith("CA")// 铜
//						|| commodityNo.startsWith("AH")// 铝
//						|| commodityNo.startsWith("PB")// 铅
//						|| commodityNo.startsWith("L-ZS")// 锌
//						|| commodityNo.startsWith("NI")// 镍
//						|| commodityNo.startsWith("SN"))// 锡
//				{
					if (orderType != 1) {
						ret = false;
					}
//				}

			} else if (exchangeNo.equals("Liffe")) {

				// 只支持当日的限价市价和永久的限价
//				if (commodityNo.startsWith("X-W")// 白糖
//						|| commodityNo.startsWith("L-Z")// 英国富时
//						|| commodityNo.startsWith("FCE"))// CAC40
//				{
					if (!(orderType == 1 || (validate == 1 && orderType == 2))) {
						ret = false;
					}
//				}

			} else if (exchangeNo.equals("KRX")) {

				// 只支持当日的限价和市价单
				if (!(validate == 1 && (orderType == 1 || orderType == 2))) {
					ret = false;
				}

			} else if (exchangeNo.equals("BMD")) {
			}

			// 国内盘
			// else if (exchangeNo.equals("SHFE")) {
			// ret = false;
			// } else if (exchangeNo.equals("CZCE")) {
			// ret = false;
			// } else if (exchangeNo.equals("DCE")) {
			// ret = false;
			// } else if (exchangeNo.equals("CFFEX")) {
			// ret = false;
			// }
		} catch (Exception ex) {
		}
		return ret;
	}
	
	/**
	 * 判断是否是韩国期权
	 * 
	 * @param exchangeNo
	 *            交易所
	 * @param commodityNo
	 *            品种
	 * @return
	 */
	public static boolean checkIsKRXOption(String exchangeNo, String commodityNo) {
		boolean ret = false;

		if (exchangeNo.equals("KRX") && (commodityNo.startsWith("201") || commodityNo.startsWith("301")))
			ret = true;

		return ret;
	}
}

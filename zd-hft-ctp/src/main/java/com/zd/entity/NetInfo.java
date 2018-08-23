package com.zd.entity;

/**
 * 功能：客户端、前置机、交易服务器之间进行通信的通用类<br>
 * 
 * @author xiang
 * 
 */
public class NetInfo implements NetParent {

	/** 识别是哪种类型通信的代码 */
	public String code = "";

	/** 本地系统编号，识别是系统号 */
	public String localSystemCode = "";

	/** 系统编号，识别是那个客户端 */
	public String systemCode = "";

	/** 可用资金 */
	public String todayCanUse = "";

	/** 通信内容的字符串 */
	public String infoT = "";

	/** 返回错误代码 */
	public String errorCode = "";

	/** 用户账户 */
	public String accountNo = "";

	/** 交易所代码 */
	public String exchangeCode = "";

	/** 返回错误信息 */
	public String errorMsg = "";

	/** 01股票数据 02板块数据 */
	public String clientNo = "";

	@Override
	public String MyToString() {

		StringBuilder sb = new StringBuilder();
		sb.append(this.code).append("@").append(this.localSystemCode)
				.append("@").append(this.systemCode).append("@")
				.append(this.todayCanUse).append("@").append(this.errorCode)
				.append("@").append(this.accountNo).append("@")
				.append(this.exchangeCode).append("@").append(this.errorMsg)
				.append("@").append(this.clientNo).append("&")
				.append(this.infoT);

		return sb.toString();
	}

	char secondDeli = (char) 1;

	@Override
	public void MyReadString(String temp) {
		String[] arrT;
		// 国内市场分隔符
		if (temp.indexOf(secondDeli) >= 0) {
			arrT = temp.split("" + secondDeli);
		} else {
			arrT = temp.split("&");
		}

		String classValue = arrT[0];

		if (arrT.length > 1)
			this.infoT = arrT[1];

		/** 防止infoT中有&符号 */
		if (arrT.length > 2) {
			if (temp.indexOf(secondDeli) >= 0) {
				for (int i = 2; i < arrT.length; i++) {
					this.infoT += "secondDeli" + arrT[i];
				}
			} else {
				for (int i = 2; i < arrT.length; i++) {
					this.infoT += "&" + arrT[i];
				}
			}
		}

		String[] arrClass = classValue.split("@", -1);
		if (arrClass.length > 0) {
			this.code = arrClass[0];
		}
		if (arrClass.length > 1) {
			this.localSystemCode = arrClass[1];
		}
		if (arrClass.length > 2) {
			this.systemCode = arrClass[2];
		}
		if (arrClass.length > 3) {
			this.todayCanUse = arrClass[3];
		}
		if (arrClass.length > 4) {
			this.errorCode = arrClass[4];
		}
		if (arrClass.length > 5) {
			this.accountNo = arrClass[5];
		}
		if (arrClass.length > 6) {
			this.exchangeCode = arrClass[6];
		}
		if (arrClass.length > 7) {
			this.errorMsg = arrClass[7];
		}
		if (arrClass.length > 8) {
			this.clientNo = arrClass[8];
		}
	}

	@Override
	public String MyPropToString() {
		return "code@localSystemCode@systemCode@todayCanUse@errorCode@accountNo@exchangeCode@errorMsg@clientNo@infoT";
	}

	public static String getCommandCode(byte[] data) throws Exception {
		int i = 8;
		for (; i < data.length; i++) {
			// char of '@'
			if ((char) (data[i]) == '@')
				break;
		}

		return new String(data, 8, i - 8, "US-ASCII");
	}

	@Override
	public String toString() {
		return "NetInfo{" +
				"code='" + code + '\'' +
				", localSystemCode='" + localSystemCode + '\'' +
				", systemCode='" + systemCode + '\'' +
				", todayCanUse='" + todayCanUse + '\'' +
				", infoT='" + infoT + '\'' +
				", errorCode='" + errorCode + '\'' +
				", accountNo='" + accountNo + '\'' +
				", exchangeCode='" + exchangeCode + '\'' +
				", errorMsg='" + errorMsg + '\'' +
				", clientNo='" + clientNo + '\'' +
				", secondDeli=" + secondDeli +
				'}';
	}
}

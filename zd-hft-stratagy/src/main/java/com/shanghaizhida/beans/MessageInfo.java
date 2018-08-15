package com.shanghaizhida.beans;

/**
 * 直达消息类
 * 
 * @author xiang <br>
 *         2015年8月5日 上午10:51:38
 * 
 */
public class MessageInfo implements NetParent {

	/**
	 * 消息类型</p>
	 * S： SettlPrx结算价<br>
	 * P： PrivateMsg个人消息<br>
	 * M: HistoryMarket历史行情数据<br>
	 * 0-9：PublicMsg公告消息<br>
	 **/
	public String msgType = "";

	/** 消息登录时间 */
	public String insertTime = "";

	/** 消息ID */
	public String msgID = "";

	/** 消息标题 */
	public String msgTitle = "";

	/** 消息内容长度（字符数） */
	public String msgLength = "";

	/** 消息内容 */
	public String msgDetail = "";

	public String MyToString() {
		StringBuilder sb = new StringBuilder();
		sb.append(msgType).append('@')
			.append(insertTime).append('@')
			.append(msgID).append('@')
			.append(msgTitle).append('@')
			.append(msgLength).append('@')
			.append(msgDetail);

		return sb.toString();
	}

	public void MyReadString(String temp) {
		String[] arrClass = temp.split("@", -1);

		this.msgType = arrClass[0];
		this.insertTime = arrClass[1];
		this.msgID = arrClass[2];

		if (arrClass.length > 3)
			this.msgTitle = arrClass[3];

		if (arrClass.length > 4)
			this.msgLength = arrClass[4];

		if (arrClass.length > 5)
			this.msgDetail = arrClass[5];

	}

	public String MyPropToString() {
		String temp = "msgType@insertTime@msgID@msgTitle@msgLength@msgDetail";
		return temp;
	}
}

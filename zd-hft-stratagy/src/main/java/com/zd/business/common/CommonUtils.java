package com.zd.business.common;

import com.shanghaizhida.beans.NetInfo;
import com.zd.business.constant.CommandEnum;
import com.zd.business.constant.MessageConst;
import com.zd.common.utils.StringUtils;

public class CommonUtils {

	/**
	 * 将数据转化成NetInfo协议传输格式的数据
	 * @param command
	 * @return
	 */
	public static String toCommandString(String command) {
		StringBuffer sb = new StringBuffer("");
		if (StringUtils.isNoneBlank(command)) {
			sb.append("{(len=").append(command.length()).append(")").append(command).append("}");
			return sb.toString();
		}
		return command;
	}
	
	/**
	 * 格式化系统异常数据
	 * @param msg
	 * @return
	 */
	public static String formatMsg(String clientNo,CommandEnum command,MessageConst msg) {
		if(msg!=null) {
			NetInfo netInfo=new NetInfo();
			netInfo.code=command.toString();
			netInfo.clientNo=clientNo;
			netInfo.errorCode=msg.getCode();
			netInfo.errorMsg=msg.getMsg();
			return netInfo.MyToString();
		}
		return "";
	}
	
	
}

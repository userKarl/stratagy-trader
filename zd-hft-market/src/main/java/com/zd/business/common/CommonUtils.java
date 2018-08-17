package com.zd.business.common;

import com.zd.common.utils.StringUtils;

public class CommonUtils {

	public static String toCommandString(String command) {
		StringBuffer sb = new StringBuffer("");
		if (StringUtils.isNoneBlank(command)) {
			sb.append("{(len=").append(command.length()).append(")").append(command).append("}");
			return sb.toString();
		}
		return command;
	}
}

package com.zd.common.utils;

import com.github.qcloudsms.SmsSingleSender;
import com.github.qcloudsms.SmsSingleSenderResult;

import java.util.ArrayList;

public class QcloudSendSms {

	public static SmsSingleSenderResult send(int appid, String appkey, String nationCode, String phoneNum, int tmplId, ArrayList<String> params) throws Exception{
		 //初始化单发
    	SmsSingleSender singleSender = new SmsSingleSender(appid, appkey);
    	SmsSingleSenderResult singleSenderResult;
    	 //指定模板单发
    	singleSenderResult = singleSender.sendWithParam(nationCode, phoneNum, tmplId, params, "", "", "");
    	return singleSenderResult;
	}
}

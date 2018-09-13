package com.zd.service;

import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.regex.Pattern;

import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson.JSON;
import com.github.qcloudsms.SmsSingleSenderResult;
import com.zd.common.utils.*;
import com.zd.dao.system.UserDao;
import com.zd.domain.UserDO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;

//@CacheConfig(cacheNames = "user")
@Transactional
@Service
public class UserService {
	private static final Logger logger = LoggerFactory.getLogger(UserService.class);
	@Autowired
	private UserDao userDao;
	@Autowired
	private RedisUtils redisUtils;

	public String login(String jsonDecode) {
		HashMap<String, Object> resultMap = new HashMap<>();
		HashMap<String, String> map = JacksonUtil.jsonToObj(jsonDecode, HashMap.class);
		String clientNo = map.get("clientNo");
		String password = map.get("password");
		String passwordEncrypt;
		try {
			passwordEncrypt = MD5Utils.encrypt(password);
		} catch (Exception e) {
			logger.error(e.toString());
			resultMap.put("result", false);
			resultMap.put("message", "系统异常");
			String jsonResult = JSON.toJSONString(resultMap);
			return jsonResult;
		}
		UserDO userDO = userDao.getusername(clientNo);
		if (userDO == null) {
			resultMap.put("result", false);
			resultMap.put("message", "请输入正确的账号");
			String jsonResult = JSON.toJSONString(resultMap);
			return jsonResult;
		}
		String fpassword = userDO.getPassword();
		if (passwordEncrypt.equals(fpassword)) {
			String token = null;
			try {
				token = JWT.createToken(clientNo);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				logger.error("exception when create token");
				resultMap.put("result", false);
				resultMap.put("message", "系统异常");
				String jsonResult = JSON.toJSONString(resultMap);
				return jsonResult;
			}
			// redis
			redisUtils.set("obh_" + clientNo, token, 1800000L);
			resultMap.put("result", true);
			resultMap.put("message", "登录成功");
			resultMap.put("token", token);
			resultMap.put("clientNo", clientNo);
			String jsonResult = JSON.toJSONString(resultMap);
			return jsonResult;
		} else {
			resultMap.put("result", false);
			resultMap.put("message", "请输入正确的密码");
			String jsonResult = JSON.toJSONString(resultMap);
			return jsonResult;
		}

	}

	public String Createuser(String json) {
		HashMap<String, Object> paramMap;
		Map<String, Object> map = new HashMap<String, Object>();
		HashMap<String, Object> resultMap = new HashMap<>();
		String pwd;
		String passwordEncrypt;
		String tel;
		String rValue;
		try {
			paramMap = JacksonUtil.jsonToObj(json, HashMap.class);
			tel = (String) paramMap.get("clientNo");
			rValue = (String) paramMap.get("returnValue");
			pwd = (String) paramMap.get("password");
			if (StringUtils.isBlank(redisUtils.get("daOpen_" + tel))) {
				resultMap.put("result", "false");
				resultMap.put("message", "验证码超时");
				return JSON.toJSONString(resultMap);
			}
			if (redisUtils.get("daOpen_" + tel).equals(rValue)) {
				UserDO userDO = new UserDO();
				map.put("mobile", tel);
				List<UserDO> list = userDao.list(map);
				if (list.size() > 0) {
					resultMap.put("result", "false");
					resultMap.put("message", "账号已存在");
					return JSON.toJSONString(resultMap);
				}
				passwordEncrypt = MD5Utils.encrypt(pwd);
				userDO.setMobile(tel);
				userDO.setClientno(RandomCode.getCode(6));
				// userDO.setUserId(Long.valueOf(RandomCode.getCode(6)));
				userDO.setIspower("F");
				userDO.setPassword(passwordEncrypt);
				int count = userDao.save(userDO);
				if (count > 0) {
					resultMap.put("result", "true");
					resultMap.put("message", "注册成功");
					return JSON.toJSONString(resultMap);
				} else {
					resultMap.put("result", "false");
					resultMap.put("message", "注册失败");
					return JSON.toJSONString(resultMap);
				}
			} else {
				resultMap.put("result", "false");
				resultMap.put("message", "验证码错误");
				return JSON.toJSONString(resultMap);
			}

		} catch (Exception e) {
			logger.error(e.toString());
			resultMap.put("result", "false");
			resultMap.put("message", "参数错误");
			return JSON.toJSONString(resultMap);
		}
	}

	public String sendMessage(HttpServletRequest request, String jsonDecode) {
		HashMap<String, String> map = new HashMap<>();
		HashMap<String, String> jsonMap = JacksonUtil.jsonToObj(jsonDecode, HashMap.class);
		String mobile = jsonMap.get("mobile");
		// 手机验证码校验
		String telphoneRegex = "^1[3|4|5|7|8][0-9]\\d{8}$";
		if (StringUtils.isBlank(mobile) || !Pattern.compile(telphoneRegex).matcher(mobile).find()) {
			map.put("result", "false");
			map.put("message", "请填写正确的手机号");
			String jsonString = com.alibaba.druid.support.json.JSONUtils.toJSONString(map);
			return jsonString;
		}
		// 生成随机数
		String randomCode = RandomCode.getCode(6);
		ArrayList<String> params = new ArrayList<>();
		params.add(randomCode);
		SmsSingleSenderResult smsSingleSenderResult = null;
		int result = 0;
		try {
			smsSingleSenderResult = QcloudSendSms.send(QcloudConfig.appid, QcloudConfig.appkey, QcloudConfig.nationCode,
					mobile, QcloudConfig.tmplId, params);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", "false");
			map.put("message", "发送失败,请重新尝试");
			String jsonString = com.alibaba.druid.support.json.JSONUtils.toJSONString(map);
			return jsonString;
		}
		result = smsSingleSenderResult.result;
		if (result == 0) {
			redisUtils.set("daOpen_" + mobile, randomCode, 1800l); // 验证码有效期半小时
		}
		System.out.println("OpenService.sendMessage" + randomCode);
		map.put("result", "true");
		map.put("message", "操作成功");
		String jsonString = JSONUtils.toJSONString(map);
		return jsonString;
	}

}

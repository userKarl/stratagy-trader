package com.zd.service.impl;

import com.alibaba.fastjson.JSON;
import com.zd.common.utils.JWT;
import com.zd.common.utils.JacksonUtil;
import com.zd.common.utils.MD5Utils;
import com.zd.common.utils.RedisUtils;
import com.zd.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zd.dao.system.TraderUserDao;
import com.zd.domain.TraderUserDO;
import com.zd.service.TraderUserService;



@Service
public class TraderUserServiceImpl implements TraderUserService {
	private static final Logger logger = LoggerFactory.getLogger(UserService.class);
	@Autowired
	private TraderUserDao traderUserDao;
	@Autowired
	private RedisUtils redisUtils;
	
	@Override
	public TraderUserDO get(String id){
		return traderUserDao.get(id);
	}
	
	@Override
	public List<TraderUserDO> list(Map<String, Object> map){
		return traderUserDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return traderUserDao.count(map);
	}
	
	@Override
	public int save(TraderUserDO traderUser){
		return traderUserDao.save(traderUser);
	}
	
	@Override
	public int update(TraderUserDO traderUser){
		return traderUserDao.update(traderUser);
	}
	
	@Override
	public int remove(String id){
		return traderUserDao.remove(id);
	}
	
	@Override
	public int batchRemove(String[] ids){
		return traderUserDao.batchRemove(ids);
	}

	@Override
	public String  loginTraderUser(String jsonDecode){
		HashMap<String, Object> resultMap = new HashMap<>();
		HashMap<String,String> map = JacksonUtil.jsonToObj(jsonDecode, HashMap.class);
		String clientNo = map.get("clientNo");
		String mobile = map.get("mobile");
		String password = map.get("password");
		String passwordEncrypt;
		try {
			passwordEncrypt = MD5Utils.encrypt(password);
		} catch (Exception e) {
			logger.error(e.toString());
			resultMap.put("result",false);
			resultMap.put("message","系统异常");
			String jsonResult = JSON.toJSONString(resultMap);
			return jsonResult;
		}
		TraderUserDO traderUserDO = traderUserDao.getAccountByuserId(clientNo,mobile);
		if(traderUserDO == null){
			resultMap.put("result",false);
			resultMap.put("message","请输入正确的账号");
			String jsonResult = JSON.toJSONString(resultMap);
			return jsonResult;
		}
		String fpassword = traderUserDO.getAccountPwd();
		if(passwordEncrypt.equals(fpassword)){
			String token = null;
			try {
				token = JWT.createToken(clientNo);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				logger.error("exception when create token");
				resultMap.put("result",false);
				resultMap.put("message","系统异常");
				String jsonResult = JSON.toJSONString(resultMap);
				return jsonResult;
			}
			//redis
			//redisUtils.set("obh_" + clientNo,token,1800000L);
			resultMap.put("result",true);
			resultMap.put("message","登录成功");
			//resultMap.put("token",token);
			resultMap.put("clientNo",clientNo);
			String jsonResult = JSON.toJSONString(resultMap);
			return jsonResult;
		}else{
			resultMap.put("result",false);
			resultMap.put("message","请输入正确的密码");
			String jsonResult = JSON.toJSONString(resultMap);
			return jsonResult;
		}
	}
	
}

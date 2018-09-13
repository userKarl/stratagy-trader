package com.zd.business.common;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zd.business.constant.RedisConst;
import com.zd.config.Global;
import com.zd.redis.RedisService;

@Service
public class BaseService {

	@Autowired
	private RedisService redisService;
	
	/**
	 * 生成localSystemCode
	 * 
	 * @return
	 */
	public String generateLocalSystemCode() {
		String localSystemCode = "";
		synchronized (Global.object) {
			String localNo =(String) redisService.get(RedisConst.MAXORDERREFKEY);
			if(StringUtils.isNotBlank(localNo)) {
				Global.orderRef = new Integer(localNo);
				Global.orderRef += 1;
			}else {
				Global.orderRef = new Integer(0);
			}
			localSystemCode = String.valueOf(Global.orderRef);
			Global.object.notifyAll();
		}
		redisService.set(RedisConst.MAXORDERREFKEY, localSystemCode);
		return localSystemCode;
	}
	
	
	public RedisService getRedisService() {
		return redisService;
	}
}

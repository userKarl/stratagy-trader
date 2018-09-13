package com.zd.service;

import com.zd.domain.TraderUserDO;

import java.util.List;
import java.util.Map;

/**
 * 交易账户表
 * 
 * @author lipy
 * @email lipy336699@gmail.com
 * @date 2018-09-10 17:56:27
 */
public interface TraderUserService {
	
	TraderUserDO get(String id);
	
	List<TraderUserDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(TraderUserDO traderUser);
	
	int update(TraderUserDO traderUser);
	
	int remove(String id);
	
	int batchRemove(String[] ids);

	String loginTraderUser(String json);
}

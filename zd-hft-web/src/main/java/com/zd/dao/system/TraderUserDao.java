package com.zd.dao.system;

import com.zd.domain.TraderUserDO;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 交易账户表
 * @author lipy
 * @email lipy336699@gmail.com
 * @date 2018-09-10 17:56:27
 */
@Mapper
public interface TraderUserDao {

	TraderUserDO get(String id);
	
	List<TraderUserDO> list(Map<String,Object> map);
	
	int count(Map<String,Object> map);
	
	int save(TraderUserDO traderUser);
	
	int update(TraderUserDO traderUser);
	
	int remove(String id);
	
	int batchRemove(String[] ids);

	TraderUserDO getAccountByuserId(@Param("accountNo") String accountNo,@Param("mobile") String mobile);
}

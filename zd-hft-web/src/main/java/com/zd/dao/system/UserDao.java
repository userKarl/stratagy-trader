package com.zd.dao.system;



import java.util.List;
import java.util.Map;

import com.zd.domain.UserDO;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface UserDao  {

	UserDO get(Long userId);

	UserDO getusername (String username);

	List<UserDO> list(Map<String,Object> map);
	
	int count(Map<String,Object> map);
	
	int save(UserDO user);
	
	int update(UserDO user);
	
	int remove(Long userId);
	
	int batchRemove(Long[] userIds);
	
	Long[] listAllDept();

}

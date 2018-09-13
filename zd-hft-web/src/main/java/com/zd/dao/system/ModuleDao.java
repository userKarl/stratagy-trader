package com.zd.dao.system;

import com.zd.domain.ModuleDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 模块设置
 * @author lipy
 * @email lipy336699@gmail.com
 * @date 2018-09-05 13:24:24
 */
@Mapper
public interface ModuleDao {

	ModuleDO get(String id);

	List<ModuleDO> list(Map<String, Object> map);

	List<ModuleDO> getList();
	int count(Map<String, Object> map);
	
	int save(ModuleDO module);
	
	int update(ModuleDO module);
	
	int remove(String id);
	
	int batchRemove(String[] ids);

	/**
	 * 根据账号获取用户拥有的模块
	 * @param
	 * @return
	 */
	List<ModuleDO> listModuleByUserId(Map<String, Object> map);

	/**
	 * 根据moduleId获取子模块
	 * @param moduleId
	 * @return
	 */
	List<ModuleDO> childlist(@Param("moduleId") String moduleId);
}

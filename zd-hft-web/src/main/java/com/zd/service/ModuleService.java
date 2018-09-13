package com.zd.service;


import com.zd.common.vo.ModuleResult;
import com.zd.domain.ModuleDO;
import com.zd.domain.Tree;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 模块设置
 * 
 * @author lipy
 * @email lipy336699@gmail.com
 * @date 2018-09-05 13:24:24
 */
public interface ModuleService {
	
	ModuleDO get(String id);
	
	List<ModuleDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	String save(String json);
	
	int update(ModuleDO module);
	
	int remove(String id);
	
	int batchRemove(String[] ids);

	ModuleResult listModule(String clientno);

	ModuleResult getAllModule();

	Set<String> listPerms(Long userId);
}

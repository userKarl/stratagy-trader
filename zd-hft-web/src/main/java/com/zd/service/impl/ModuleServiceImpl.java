package com.zd.service.impl;


import com.alibaba.fastjson.JSON;
import com.zd.common.utils.IdGen;
import com.zd.common.utils.JacksonUtil;
import com.zd.common.vo.ModuleResult;
import com.zd.common.vo.ModuleVo;
import com.zd.dao.system.ModuleDao;
import com.zd.dao.system.UserDao;
import com.zd.domain.ModuleDO;
import com.zd.domain.UserDO;
import com.zd.service.ModuleService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class ModuleServiceImpl implements ModuleService {
	/**
	 *
	 */
	@Autowired
	private ModuleDao moduleDao;

	@Autowired
    private UserDao userDao;

	@Override
	public ModuleDO get(String id){
		return moduleDao.get(id);
	}
	
	@Override
	public List<ModuleDO> list(Map<String, Object> map){
		return moduleDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return moduleDao.count(map);
	}
	
	@Override
	public String save(String json){
	    Map<String,Object> resultMap = new HashMap<String, Object>();
	    Map<String,Object> param = new HashMap<String, Object>();
		Map<String,Object> cliparam = new HashMap<String, Object>();
	    param = JacksonUtil.jsonToObj(json,HashMap.class);
	    String tel =(String)param.get("cientNo");
		cliparam.put("mobile",tel);
        List<UserDO> userDaos = userDao.list(cliparam);
        for (UserDO userDO : userDaos){
            if(userDO.getIspower().equals("T")){
				ModuleDO module = new ModuleDO();
                module.setId(IdGen.uuid());
                module.setName(param.get("name").toString());
                module.setParentIds(param.get("parentIds").toString());
                module.setModulePower(param.get("modulePower").toString());
                module.setModuleType(param.get("moduleType").toString());
                module.setRemark(param.get("remark").toString());
                module.setCreateBy(userDO.getName());
                module.setCreateDate(new Date());
				List<ModuleDO> moduleDOS = moduleDao.getList();
				for(ModuleDO moduleDO :moduleDOS){
					if(moduleDO.getName().equals(module.getName())){
						resultMap.put("result","false");
						resultMap.put("message","模板重复");
						return JSON.toJSONString(resultMap);
					}
				}
                moduleDao.save(module);
                resultMap.put("result","true");
                resultMap.put("message","创建成功");
                return JSON.toJSONString(resultMap);
            }
        }
        resultMap.put("result","false");
        resultMap.put("message","权限不足");
        return JSON.toJSONString(resultMap);

	}
	
	@Override
	public int update(ModuleDO module){
		return moduleDao.update(module);
	}
	
	@Override
	public int remove(String id){
		return moduleDao.remove(id);
	}
	
	@Override
	public int batchRemove(String[] ids){
		return moduleDao.batchRemove(ids);
	}

	@Override
	public ModuleResult listModule(String clientno) {
		ModuleResult moduleResult = new ModuleResult();
		HashMap<String,Object> modMap = new HashMap<String,Object>();
		Map<String, Object> pramMap = new HashMap<String, Object>();
		Map<String, Object> powerMap = new HashMap<String, Object>();
		pramMap.put("clientno",clientno);
		List<ModuleDO> moduleDOList =moduleDao.listModuleByUserId(pramMap);
		Map<String ,Object> Idmap = new HashMap<>();
		List<ModuleVo> listmoVo = new ArrayList<>();
		List<ModuleVo> listmoVo1 = new ArrayList<>();
		for (ModuleDO m1 : moduleDOList){
			Idmap.put(m1.getId().toString(),m1.getName().toString());
		}
		//  ModuleType 模块类型分类  1：基础 2：功能 3：逻辑 4：统计分析
		ModuleVo moduleVo1 =new ModuleVo();
		moduleVo1.setId("1-1");
		moduleVo1.setName("基础");
		pramMap.put("clientno",clientno);
		pramMap.put("moduleType","1");
		List<ModuleDO> modlist1 =moduleDao.listModuleByUserId(pramMap);
		this.setChild(modlist1);
		moduleVo1.setModuleDOList(modlist1);
		//功能
		ModuleVo moduleVo2 =new ModuleVo();
		moduleVo2.setId("1-2");
		moduleVo2.setName("功能");
		pramMap.put("clientno",clientno);
		pramMap.put("moduleType","2");
		List<ModuleDO> modlist2 =moduleDao.listModuleByUserId(pramMap);
		this.setChild(modlist2);
		moduleVo2.setModuleDOList(modlist2);
		//逻辑
		ModuleVo moduleVo3 =new ModuleVo();
		moduleVo3.setId("1-3");
		moduleVo3.setName("逻辑");
		pramMap.put("clientno",clientno);
		pramMap.put("moduleType","3");
		List<ModuleDO> modlist3 =moduleDao.listModuleByUserId(pramMap);
		this.setChild(modlist3);
		moduleVo3.setModuleDOList(modlist3);
		//统计分析
		ModuleVo moduleVo4 =new ModuleVo();
		moduleVo4.setId("1-4");
		pramMap.put("clientno",clientno);
		moduleVo4.setName("统计分析");
		pramMap.put("moduleType","4");
		List<ModuleDO> modlist4 =moduleDao.listModuleByUserId(pramMap);
		this.setChild(modlist4);
		moduleVo4.setModuleDOList(modlist4);
		//权限分类0:未开放 1:免费
		ModuleVo moduleVo5  = new ModuleVo();
		moduleVo5.setId("2-1");
		moduleVo5.setName("未开放");
        powerMap.put("clientno",clientno);
		powerMap.put("modulePower","0");
		List<ModuleDO> modlist5 =moduleDao.listModuleByUserId(powerMap);
		this.setChild(modlist5);
		moduleVo5.setModuleDOList(modlist5);
		//免费
		ModuleVo moduleVo6  = new ModuleVo();
		moduleVo6.setId("2-2");
		moduleVo6.setName("免费");
        powerMap.put("clientno",clientno);
		powerMap.put("modulePower","1");
		List<ModuleDO> modlist6 =moduleDao.listModuleByUserId(powerMap);
		this.setChild(modlist6);
		moduleVo6.setModuleDOList(modlist6);
		//moduleVo加到Listmovo
		listmoVo.add(moduleVo1);
		listmoVo.add(moduleVo2);
		listmoVo.add(moduleVo3);
		listmoVo.add(moduleVo4);
		listmoVo1.add(moduleVo5);
		listmoVo1.add(moduleVo6);
		modMap.put("按照模块类型分类",listmoVo);
		modMap.put("按照权限模块分类",listmoVo1);
		moduleResult.setIdList(Idmap);
		moduleResult.setModMap(modMap);
		return moduleResult;
		}

	@Override
	public ModuleResult getAllModule() {
		ModuleResult moduleResult = new ModuleResult();
		HashMap<String,Object> modMap = new HashMap<String,Object>();
		List<ModuleDO> moduleDOList =moduleDao.getList();
		Map<String ,Object> Idmap = new HashMap<>();
		List<ModuleVo> listmoVo = new ArrayList<>();
        List<ModuleVo> listmoVo1 = new ArrayList<>();
		for (ModuleDO m1 : moduleDOList){
            Idmap.put(m1.getId().toString(),m1.getName().toString());
		}
		Map<String, Object> pramMap = new HashMap<String, Object>();
		Map<String, Object> powerMap = new HashMap<String, Object>();
		//  ModuleType 模块类型分类  1：基础 2：功能 3：逻辑 4：统计分析
			ModuleVo moduleVo1 =new ModuleVo();
			moduleVo1.setId("1-1");
			moduleVo1.setName("基础");
			pramMap.put("moduleType","1");
			List<ModuleDO> modlist1 =moduleDao.list(pramMap);
			this.setChild(modlist1);
			moduleVo1.setModuleDOList(modlist1);
			//功能
			ModuleVo moduleVo2 =new ModuleVo();
			moduleVo2.setId("1-2");
			moduleVo2.setName("功能");
			pramMap.put("moduleType","2");
			List<ModuleDO> modlist2 =moduleDao.list(pramMap);
			this.setChild(modlist2);
			moduleVo2.setModuleDOList(modlist2);
			//逻辑
			ModuleVo moduleVo3 =new ModuleVo();
			moduleVo3.setId("1-3");
			moduleVo3.setName("逻辑");
			pramMap.put("moduleType","3");
			List<ModuleDO> modlist3 =moduleDao.list(pramMap);
			this.setChild(modlist3);
			moduleVo3.setModuleDOList(modlist3);
			//统计分析
			ModuleVo moduleVo4 =new ModuleVo();
			moduleVo4.setId("1-4");
			moduleVo4.setName("统计分析");
			pramMap.put("moduleType","4");
			List<ModuleDO> modlist4 =moduleDao.list(pramMap);
			this.setChild(modlist4);
			moduleVo4.setModuleDOList(modlist4);
			//权限分类0:未开放 1:免费
			ModuleVo moduleVo5  = new ModuleVo();
			moduleVo5.setId("2-1");
			moduleVo5.setName("未开放");
			powerMap.put("modulePower","0");
			List<ModuleDO> modlist5 =moduleDao.list(powerMap);
			this.setChild(modlist5);
			moduleVo5.setModuleDOList(modlist5);
			//免费
			ModuleVo moduleVo6  = new ModuleVo();
			moduleVo6.setId("2-2");
			moduleVo6.setName("免费");
			powerMap.put("modulePower","1");
			List<ModuleDO> modlist6 =moduleDao.list(powerMap);
			this.setChild(modlist6);
			moduleVo6.setModuleDOList(modlist6);
		//moduleVo加到Listmovo
		listmoVo.add(moduleVo1);
        listmoVo.add(moduleVo2);
        listmoVo.add(moduleVo3);
        listmoVo.add(moduleVo4);
        listmoVo1.add(moduleVo5);
        listmoVo1.add(moduleVo6);
        modMap.put("按照模块类型分类",listmoVo);
        modMap.put("按照权限模块分类",listmoVo1);
		moduleResult.setIdList(Idmap);
		moduleResult.setModMap(modMap);
		return moduleResult;
	}

    public void setChild(List<ModuleDO> list){
		for(ModuleDO moduleDO :list){
			List<ModuleDO> modList = moduleDao.childlist(moduleDO.getId());
			String childId = null;
			for (ModuleDO module: modList){
				if(StringUtils.isNotBlank(moduleDO.getChild_ids())){
					childId = moduleDO.getChild_ids()+","+module.getId();
				}else{
					childId = module.getId();
				}
				moduleDO.setChild_ids(childId);
			}
		}
	}
	@Override
	public Set<String> listPerms(Long userId) {
		List<String> perms =new ArrayList<>();
		Map<String ,Object> Idmap = new HashMap<>();
		List<ModuleDO> moduleDOList =	moduleDao.listModuleByUserId(Idmap);
			for (ModuleDO moduleDO : moduleDOList){
				perms.add(moduleDO.getPerms());
			}
		Set<String> permsSet = new HashSet<>();
		for (String perm : perms) {
			if (StringUtils.isNotBlank(perm)) {
				permsSet.addAll(Arrays.asList(perm.trim().split(",")));
			}
		}
		return permsSet;
	}

}

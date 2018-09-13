package com.zd.controller;


import com.zd.common.annotation.Log;
import com.zd.common.utils.PageUtils;
import com.zd.common.utils.Query;
import com.zd.common.utils.R;
import com.zd.common.vo.ModuleResult;
import com.zd.domain.ModuleDO;
import com.zd.domain.Tree;
import com.zd.service.ModuleService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 模块设置
 * 
 * @author lipy
 * @email lipy336699@gmail.com
 * @date 2018-09-05 13:24:24
 */
 
@Controller
@RequestMapping("/zd/module")
public class ModuleController {
	private String prefix="zd/module"  ;
	@Autowired
	private ModuleService moduleService;
	
	@GetMapping()
	@RequiresPermissions("zd:module:module")
	String Module(){
	    return prefix+"/module";
	}
	
	@ResponseBody
	@GetMapping("/list")
	@RequiresPermissions("zd:module:module")
	public PageUtils list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);
		List<ModuleDO> moduleList = moduleService.list(query);
		int total = moduleService.count(query);
		PageUtils pageUtils = new PageUtils(moduleList, total);
		return pageUtils;
	}
	
	@GetMapping("/add")
	@RequiresPermissions("zd:module:add")
	String add(){
	    return "zd/module/add";
	}

	@GetMapping("/edit/{id}")
	@RequiresPermissions("zd:module:edit")
	String edit(@PathVariable("id") String id, Model model){
		ModuleDO module = moduleService.get(id);
		model.addAttribute("module", module);
	    return "zd/module/edit";
	}
	
	/**
	 * 保存
	 */
	@ResponseBody
    @RequestMapping(value = "/save" ,method = {RequestMethod.POST, RequestMethod.GET}, produces = "application/json; charset=utf-8")
    @Log("创建模板")
	public String save(@RequestBody String json){


		return moduleService.save(json);
	}
	/**
	 * 修改
	 */
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("zd:module:edit")
	public R update( ModuleDO module){
		moduleService.update(module);
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/remove")
	@ResponseBody
	@RequiresPermissions("zd:module:remove")
	public R remove( String id){
		if(moduleService.remove(id)>0){
		return R.ok();
		}
		return R.error();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/batchRemove")
	@ResponseBody
	@RequiresPermissions("zd:module:batchRemove")
	public R remove(@RequestParam("ids[]") String[] ids){
		moduleService.batchRemove(ids);
		return R.ok();
	}



	@GetMapping("/listModule")
	@ResponseBody
	@Log("根据账号获取模块")
	ModuleResult listModule (String clientno){
		ModuleResult moduleResult = moduleService.listModule(clientno);
		return moduleResult;
	}
	@GetMapping("/getAllModule")
	@ResponseBody
	@Log("获取所有模块")
	ModuleResult getAllModule (){
		ModuleResult moduleResult = moduleService.getAllModule();
		return moduleResult;
	}
}

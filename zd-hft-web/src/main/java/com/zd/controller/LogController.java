package com.zd.controller;

import java.util.List;
import java.util.Map;

import com.zd.common.utils.PageUtils;
import com.zd.common.utils.Query;
import com.zd.common.utils.R;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zd.domain.LogDO;
import com.zd.service.LogService;


/**
 * 系统日志
 * 
 * @author lipy
 * @email lipy336699@gmail.com
 * @date 2018-09-11 17:33:48
 */
 
@Controller
@RequestMapping("/zd/log")
public class LogController {
	@Autowired
	private LogService logService;
	
	@GetMapping()
	@RequiresPermissions("zd:log:log")
	String Log(){
	    return "zd/log/log";
	}
	
	@ResponseBody
	@GetMapping("/list")
	@RequiresPermissions("zd:log:log")
	public PageUtils list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);
		List<LogDO> logList = logService.list(query);
		int total = logService.count(query);
		PageUtils pageUtils = new PageUtils(logList, total);
		return pageUtils;
	}
	
	@GetMapping("/add")
	@RequiresPermissions("zd:log:add")
	String add(){
	    return "zd/log/add";
	}

	@GetMapping("/edit/{id}")
	@RequiresPermissions("zd:log:edit")
	String edit(@PathVariable("id") Long id,Model model){
		LogDO log = logService.get(id);
		model.addAttribute("log", log);
	    return "zd/log/edit";
	}
	
	/**
	 * 保存
	 */
	@ResponseBody
	@PostMapping("/save")
	@RequiresPermissions("zd:log:add")
	public R save(LogDO log){
		if(logService.save(log)>0){
			return R.ok();
		}
		return R.error();
	}
	/**
	 * 修改
	 */
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("zd:log:edit")
	public R update( LogDO log){
		logService.update(log);
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/remove")
	@ResponseBody
	@RequiresPermissions("zd:log:remove")
	public R remove( Long id){
		if(logService.remove(id)>0){
		return R.ok();
		}
		return R.error();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/batchRemove")
	@ResponseBody
	@RequiresPermissions("zd:log:batchRemove")
	public R remove(@RequestParam("ids[]") Long[] ids){
		logService.batchRemove(ids);
		return R.ok();
	}
	
}

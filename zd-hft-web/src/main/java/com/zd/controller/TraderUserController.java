package com.zd.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import java.util.Map;

import com.zd.common.annotation.Log;
import com.zd.common.utils.PageUtils;
import com.zd.common.utils.Query;
import com.zd.common.utils.R;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.zd.domain.TraderUserDO;
import com.zd.service.TraderUserService;

/**
 * 交易账户表
 * 
 * @author lipy
 * @email lipy336699@gmail.com
 * @date 2018-09-10 17:56:27
 */
 
@Controller
@RequestMapping("/zd/traderUser")
public class TraderUserController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private TraderUserService traderUserService;
	
	@GetMapping()
	@RequiresPermissions("zd:traderUser:traderUser")
	String TraderUser(){
	    return "zd/traderUser/traderUser";
	}
	
	@ResponseBody
	@GetMapping("/list")
	@RequiresPermissions("zd:traderUser:traderUser")
	public PageUtils list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);
		List<TraderUserDO> traderUserList = traderUserService.list(query);
		int total = traderUserService.count(query);
		PageUtils pageUtils = new PageUtils(traderUserList, total);
		return pageUtils;
	}
	
	@GetMapping("/add")
	@RequiresPermissions("zd:traderUser:add")
	String add(){
	    return "zd/traderUser/add";
	}

	@GetMapping("/edit/{id}")
	@RequiresPermissions("zd:traderUser:edit")
	String edit(@PathVariable("id") String id,Model model){
		TraderUserDO traderUser = traderUserService.get(id);
		model.addAttribute("traderUser", traderUser);
	    return "zd/traderUser/edit";
	}
	
	/**
	 * 保存
	 */
	@ResponseBody
	@PostMapping("/save")
	@RequiresPermissions("zd:traderUser:add")
	public R save(TraderUserDO traderUser){
		if(traderUserService.save(traderUser)>0){
			return R.ok();
		}
		return R.error();
	}
	/**
	 * 修改
	 */
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("zd:traderUser:edit")
	public R update( TraderUserDO traderUser){
		traderUserService.update(traderUser);
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/remove")
	@ResponseBody
	@RequiresPermissions("zd:traderUser:remove")
	public R remove( String id){
		if(traderUserService.remove(id)>0){
		return R.ok();
		}
		return R.error();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/batchRemove")
	@ResponseBody
	@RequiresPermissions("zd:traderUser:batchRemove")
	public R remove(@RequestParam("ids[]") String[] ids){
		traderUserService.batchRemove(ids);
		return R.ok();
	}

	@RequestMapping(value = "/tradelogin" ,method = {RequestMethod.POST, RequestMethod.GET}, produces = "application/json; charset=utf-8")
	@ResponseBody
	@Log("交易账号登陆")
	public String login( @RequestBody String json)  {
		String jsonDecode = null;
		try {
			jsonDecode = URLDecoder.decode(json, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			logger.error(e.toString());
		}
		String result = traderUserService.loginTraderUser(jsonDecode);
		return result;
	}
	
}

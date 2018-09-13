package com.zd.controller;

import com.zd.common.annotation.Log;
import com.zd.common.utils.*;
import com.zd.service.FutureShareService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.zd.domain.FuturesDO;

/**
 * 
 * 
 * @author lipy
 * @email lipy336699@gmail.com
 * @date 2018-09-07 15:00:32
 */
 
@Controller
@RequestMapping("/zd/futures")
public class FuturesController {

	@Autowired
    private FutureShareService futureShareService;

	@ResponseBody
	@RequestMapping(value = "/list" ,method = {RequestMethod.POST, RequestMethod.GET}, produces = "application/json; charset=utf-8")
	@Log("查询期货列表")
	public String list(@RequestBody String json){
		//查询列表数据
		return futureShareService.getCodeByExchangeNo(json);
	}
}

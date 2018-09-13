package com.zd.controller;

import com.zd.common.annotation.Log;
import com.zd.service.StockShareService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/zd/stock")
public class StockController {

    @Autowired
    private StockShareService stockShareService;

    @ResponseBody
    @RequestMapping(value = "/list" ,method = {RequestMethod.POST, RequestMethod.GET}, produces = "application/json; charset=utf-8")
    @Log("查询股票列表")
    public String list(@RequestBody String json){
        //查询列表数据
        return stockShareService.getStockByExchangeNo(json);
    }

}

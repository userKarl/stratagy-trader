package com.zd.controller;

import com.alibaba.fastjson.JSON;
import com.zd.domain.TExchangeDo;
import com.zd.service.StockTExchangeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/zd/stockTExchange")
public class StockTExchangeController {

    @Autowired
    private StockTExchangeService stockTExchangeService;


    @ResponseBody
    @RequestMapping(value = "/list" ,method = {RequestMethod.POST, RequestMethod.GET}, produces = "application/json; charset=utf-8")
    public String list(String type){
        List<TExchangeDo> TExchangeDoList = null;
        if(type.equals("1")){
            TExchangeDoList = stockTExchangeService.list();
        }
        if(type.equals("2")){
            TExchangeDoList = stockTExchangeService.listforeign();
        }
        return JSON.toJSONString(TExchangeDoList);
    }

}

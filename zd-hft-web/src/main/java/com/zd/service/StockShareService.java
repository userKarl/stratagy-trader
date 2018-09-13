package com.zd.service;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import com.zd.common.utils.JacksonUtil;
import com.zd.common.utils.Page;
import com.zd.dao.stock.StockDoMapper;
import com.zd.domain.FutureDo;
import com.zd.domain.StockDo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StockShareService {

    @Autowired
    private StockDoMapper stockDoMapper;

    public  String getStockByExchangeNo(String json){
        Map<String, Object> params = JacksonUtil.jsonToObj(json,HashMap.class);
        //查询列表数据
        Page page = new Page(params);
        List<StockDo> stockList = stockDoMapper.getStockByExchangeNo(page);
        PageInfo<StockDo> StockDOPageInfo = new PageInfo<>(stockList);

        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("total",StockDOPageInfo.getTotal());
        resultMap.put("data",StockDOPageInfo.getList());
        return JSON.toJSONString(resultMap);
    }



}

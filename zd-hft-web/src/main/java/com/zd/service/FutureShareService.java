package com.zd.service;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import com.zd.common.utils.JacksonUtil;
import com.zd.common.utils.Page;
import com.zd.dao.foreign.FutureDoMapper;
import com.zd.domain.FutureDo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FutureShareService {

    @Autowired
    private FutureDoMapper futureDoMapper;


    public String getCodeByExchangeNo(String json){
        Map<String, Object> params = JacksonUtil.jsonToObj(json,HashMap.class);
        //查询列表数据
        Page page = new Page(params);
        List<FutureDo> futuresList = futureDoMapper.getCodeByExchangeNo(page);
        PageInfo<FutureDo> futuresDOPageInfo = new PageInfo<>(futuresList);
        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("total",futuresDOPageInfo.getTotal());
        resultMap.put("data",futuresDOPageInfo.getList());
        return JSON.toJSONString(resultMap);
    }

}

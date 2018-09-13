package com.zd.dao.stock;

import com.zd.domain.StockDo;

import java.util.List;
import java.util.Map;

public interface StockDoMapper {
    int insert(StockDo record);

    int insertSelective(StockDo record);

    List<StockDo> getStockByExchangeNo(Map<String,Object> map);
}
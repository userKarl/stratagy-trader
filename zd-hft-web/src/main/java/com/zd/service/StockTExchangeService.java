package com.zd.service;

import com.zd.dao.stock.StockDoMapper;
import com.zd.dao.stock.StockTExchangeDao;
import com.zd.domain.TExchangeDo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StockTExchangeService {
    @Autowired
    private StockTExchangeDao stockTExchangeDao;


    public List<TExchangeDo> list() {
        return  stockTExchangeDao.list();
    }


    public List<TExchangeDo> listforeign() {
        return stockTExchangeDao.listforeign();
    }
}

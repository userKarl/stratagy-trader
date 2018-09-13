package com.zd.service;

import com.zd.dao.foreign.FutureTExchangeDao;
import com.zd.dao.stock.StockDoMapper;
import com.zd.dao.stock.StockTExchangeDao;
import com.zd.domain.TExchangeDo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FutureTExchangeService {
    @Autowired
    private FutureTExchangeDao futureTExchangeDao;


    public List<TExchangeDo> list() {
        return  futureTExchangeDao.list();
    }


    public List<TExchangeDo> listforeign() {
        return futureTExchangeDao.listforeign();
    }
}

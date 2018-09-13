package com.zd.dao.foreign;


import com.zd.domain.FutureDo;

import java.util.List;
import java.util.Map;

public interface FutureDoMapper {

    int insert(FutureDo record);

    int insertSelective(FutureDo record);

    List<FutureDo> getCodeByExchangeNo(Map<String, Object> map);
}
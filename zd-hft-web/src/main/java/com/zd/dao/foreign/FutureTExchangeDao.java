package com.zd.dao.foreign;

import com.zd.domain.TExchangeDo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 
 * @author lipy
 * @email lipy336699@gmail.com
 * @date 2018-09-07 15:00:32
 */

@Mapper
public interface FutureTExchangeDao {

	List<TExchangeDo> list();

    List<TExchangeDo> listforeign();

}

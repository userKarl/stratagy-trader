package com.zd.business.engine.main;

import org.apache.commons.lang3.StringUtils;

import com.shanghaizhida.beans.CommandCode;
import com.shanghaizhida.beans.NetInfo;
import com.shanghaizhida.beans.OrderResponseInfo;
import com.zd.business.constant.RedisConst;
import com.zd.business.engine.event.ZdEventDynamicHandlerAbstract;
import com.zd.business.entity.TraderRef;
import com.zd.redis.RedisService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CentralEventHandler extends ZdEventDynamicHandlerAbstract<CentralEvent> {

	private RedisService redisSerivce;

	public CentralEventHandler(RedisService redisSerivce) {
		this.redisSerivce=redisSerivce;
	}

	@Override
	public void onEvent(CentralEvent event, long sequence, boolean endOfBatch) throws Exception {
		try {
			NetInfo netInfo = event.getNetInfo();
			TraderRef traderRef = new TraderRef();
			if (CommandCode.ORDER.equals(netInfo.code)) {
				OrderResponseInfo orderResponseInfo = new OrderResponseInfo();
				orderResponseInfo.MyReadString(netInfo.infoT);
				String localSystemCode = orderResponseInfo.localNo;
				traderRef.MyReadString(
						(String) redisSerivce.hmGet(RedisConst.NETINFO_LOCALNO_TRADERREF, localSystemCode));
			}
		} catch (Exception e) {
			log.error("中控服务器处理数据异常", e);
		}
	}

	
	public void handlerTraderInfo(TraderRef traderRef) {
		if(traderRef!=null) {
			String stratagyId = traderRef.getStratagyId();
			String webUserId = (String)redisSerivce.hmGet(RedisConst.STRATAGY_WEBUSER, stratagyId);
			if(StringUtils.isNotBlank(webUserId)) {
				//查找该web用户Id的所有Netty连接，然后将该用户的交易数据返回至这些连接
			}
		}
	}
}

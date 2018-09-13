package com.zd.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zd.TestGlobal;
import com.zd.business.engine.event.ZdEventDynamicHandlerAbstract;
import com.zd.redis.RedisService;

public class TestEventHandler extends ZdEventDynamicHandlerAbstract<TestEvent>{

	private static final Logger logger = LoggerFactory.getLogger(TestEventHandler.class);
	
	private RedisService redisService;
	
	public TestEventHandler(RedisService redisService) {
		this.redisService=redisService;
	}
	
	
	@Override
	public void onEvent(TestEvent event, long sequence, boolean endOfBatch) throws Exception {
		try {
			TestGlobal.a=(String) redisService.get("test");
			logger.info("获取到Redis中的值为：{}",redisService.get("test"));
		} catch (Exception e) {
			logger.error("处理中控服务器发送的数据异常：{}",e.getMessage());
		}
	}

}

package com.zd.event;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.zd.redis.RedisService;


@RunWith(SpringRunner.class)
@SpringBootTest
public class EventMainTest {

	@Autowired
	private RedisService redisService;
	
	@Test
	public void testIOC() throws InterruptedException {
		TestEventProducer tep = new TestEventProducer(TestEventEngine.getRingBuffer());
		TestEventEngine.addHandler(redisService);
		tep.onData("ad");
		Thread.sleep(1000);
		Assert.assertEquals((String)redisService.get("test"), "aaa");
	}
}

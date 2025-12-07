package com.github.chenyuxin.commonframework.testredis.component;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.github.chenyuxin.commonframework.redis.CommonRedisTemplate;


/**
 * 测试异步缓存
 */
@Component
public class AsyncCache {
	
	private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(AsyncCache.class);
	
	@Autowired CommonRedisTemplate redisTemplate;
	
	@Async
	public void cacheData(String key,Object obj) {
		boolean ok = redisTemplate.opsForValue().setIfAbsent(key, obj, Duration.ofHours(12));
		log.info("redis缓存:" + ok);
	}
	
	public Object cacheRead(String key) {
		return redisTemplate.opsForValue().get(key);
	}
	
	public boolean removeCacheData(String key) {
		if ( redisTemplate.hasKey(key) ) {
			return redisTemplate.delete(key);
		}
		return false;
	}
	
	
	public void test(String key) {
		redisTemplate.opsForValue().increment(key);
	}

}

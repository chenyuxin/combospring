package com.github.chenyuxin.commonframework.redis;

import org.springframework.data.redis.connection.DefaultStringRedisConnection;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

import com.alibaba.fastjson2.support.spring6.data.redis.FastJsonRedisSerializer;

/**
 * 通用的redis用例
 * </br>'String, Object'
 * @author chenyuxin
 *
 */
public class CommonRedisTemplate extends RedisTemplate<String, Object> {
	
	private static FastJsonRedisSerializer<Object> fastJsonRedisSerializer = new FastJsonRedisSerializer<Object>(Object.class);
	
	/**
	 * Constructs a new <code>StringRedisTemplate</code> instance. {@link #setConnectionFactory(RedisConnectionFactory)}
	 * and {@link #afterPropertiesSet()} still need to be called.
	 */
	public CommonRedisTemplate() {
		setKeySerializer(RedisSerializer.string());
		setValueSerializer(fastJsonRedisSerializer);
		setHashKeySerializer(RedisSerializer.string());
        setHashValueSerializer(fastJsonRedisSerializer);
	}

	/**
	 * Constructs a new <code>StringRedisTemplate</code> instance ready to be used.
	 *
	 * @param connectionFactory connection factory for creating new connections
	 */
	public CommonRedisTemplate(RedisConnectionFactory connectionFactory) {
		this();
		setConnectionFactory(connectionFactory);
		afterPropertiesSet();
	}

	protected RedisConnection preProcessConnection(RedisConnection connection, boolean existingConnection) {
		return new DefaultStringRedisConnection(connection);
	}

}


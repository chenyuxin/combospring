package com.github.chenyuxin.commonframework.redis;

import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * 通用的redis用例
 * </br>'String, Object'
 * @author chenyuxin
 */
public class CommonRedisTemplate extends RedisTemplate<String, Object> {
	
	/**
	 * Constructs a new <code>StringRedisTemplate</code> instance. {@link #setConnectionFactory(RedisConnectionFactory)}
	 * and {@link #afterPropertiesSet()} still need to be called.
	 */
	public CommonRedisTemplate() {
		setKeySerializer(RedisSerializer.string());
		setValueSerializer(RedisSerializer.json());
		setHashKeySerializer(RedisSerializer.string());
        setHashValueSerializer(RedisSerializer.json());
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

//	protected @NonNull RedisConnection preProcessConnection(@NonNull RedisConnection connection, boolean existingConnection) {
//		return new DefaultStringRedisConnection(connection);
//	}

}


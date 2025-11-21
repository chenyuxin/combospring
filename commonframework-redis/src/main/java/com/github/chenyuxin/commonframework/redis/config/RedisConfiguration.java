package com.github.chenyuxin.commonframework.redis.config;

import java.time.Duration;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnSingleCandidate;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.data.redis.autoconfigure.DataRedisAutoConfiguration;
import org.springframework.boot.data.redis.autoconfigure.DataRedisProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.github.chenyuxin.commonframework.redis.CommonRedisTemplate;
import com.github.chenyuxin.commonframework.redis.util.RedisConst;


/**
 * 使用配置类配置spring; 使用springboot启动时自动装配;
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(RedisOperations.class)
@EnableConfigurationProperties(DataRedisProperties.class)
@AutoConfigureAfter(DataRedisAutoConfiguration.class)
@EnableCaching//开启spring的cache
@EnableTransactionManagement//加上这个才能开启所谓的redis事务
public class RedisConfiguration {

	@Bean
	@ConditionalOnMissingBean
	CommonRedisTemplate commonRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
		CommonRedisTemplate commonRedisTemplate = new CommonRedisTemplate(redisConnectionFactory);
		commonRedisTemplate.setEnableTransactionSupport(true);//开启所谓的事务，同dataSrouceTrasationManager一起使用
		return commonRedisTemplate;
	}
	
	@Bean
	CacheManager redisCacheManager(RedisConnectionFactory redisConnectionFactory) {
		RedisSerializer<?> redisSerializer  = RedisSerializer.json();
		SerializationPair<?> serializationPair = SerializationPair.fromSerializer(redisSerializer);
		RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
				.entryTtl(Duration.ofMinutes(30))
				.serializeValuesWith(serializationPair)
				;
		
		return RedisCacheManager.builder(redisConnectionFactory)
					.withCacheConfiguration(RedisConst.cacheName, redisCacheConfiguration)
					.build();
	}
	
	
}
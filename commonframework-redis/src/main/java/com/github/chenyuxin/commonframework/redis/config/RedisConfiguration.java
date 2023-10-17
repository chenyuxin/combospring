package com.github.chenyuxin.commonframework.redis.config;

import java.time.Duration;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnSingleCandidate;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.alibaba.fastjson2.support.spring6.data.redis.FastJsonRedisSerializer;
import com.github.chenyuxin.commonframework.redis.CommonRedisTemplate;
import com.github.chenyuxin.commonframework.redis.util.RedisConst;


/**
 * 使用配置类配置spring; 使用springboot启动时自动装配;
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(RedisOperations.class)
@EnableConfigurationProperties(RedisProperties.class)
@Import(RedisAutoConfiguration.class)
@AutoConfigureAfter(RedisAutoConfiguration.class)
@EnableCaching//开启spring的cache
@EnableTransactionManagement//加上这个才能开启所谓的redis事务
public class RedisConfiguration {

	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnSingleCandidate(RedisConnectionFactory.class)
	public static CommonRedisTemplate commonRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
		CommonRedisTemplate commonRedisTemplate = new CommonRedisTemplate(redisConnectionFactory);
		commonRedisTemplate.setEnableTransactionSupport(true);//开启所谓的事务，同dataSrouceTrasationManager一起使用
		return commonRedisTemplate;
	}
	
	/**
	@Bean
	@ConditionalOnMissingBean(name = "redisTemplate")
	@ConditionalOnSingleCandidate(RedisConnectionFactory.class)
	public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
		RedisTemplate<Object, Object> template = new RedisTemplate<>();
		template.setConnectionFactory(redisConnectionFactory);
		template.setEnableTransactionSupport(true);
		return template;
	}
	*/
	
	@Bean
	public CacheManager redisCacheManager(RedisConnectionFactory redisConnectionFactory) {
		RedisSerializer<?> redisSerializer  = new FastJsonRedisSerializer<>(Object.class);
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
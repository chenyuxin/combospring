package com.github.chenyuxin.combo.redis.configuration;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnSingleCandidate;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.transaction.annotation.EnableTransactionManagement;


/**
 * 使用配置类配置spring; 使用springboot启动时自动装配;
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(RedisOperations.class)
@EnableConfigurationProperties(RedisProperties.class)
@Import(RedisAutoConfiguration.class)
@AutoConfigureAfter(RedisAutoConfiguration.class)
@EnableTransactionManagement//加上这个才能开启所谓的redis事务
public class RedisConfiguration {

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
	@ConditionalOnMissingBean
	@ConditionalOnSingleCandidate(RedisConnectionFactory.class)
	public static ComboRedisTemplate comboRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
		ComboRedisTemplate comboRedisTemplate = new ComboRedisTemplate(redisConnectionFactory);
		comboRedisTemplate.setEnableTransactionSupport(true);//开启所谓的事务，查资料说是同dataSrouceTrasationManager一起使用
		return comboRedisTemplate;
	}
	
	
	
}
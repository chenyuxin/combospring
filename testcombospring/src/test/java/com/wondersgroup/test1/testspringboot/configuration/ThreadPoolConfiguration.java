package com.wondersgroup.test1.testspringboot.configuration;

import java.util.concurrent.ThreadPoolExecutor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * 自定义线程池
 */
@Configuration
@EnableAsync
public class ThreadPoolConfiguration {
	
	/** 核心线程数(默认线程数) */
	private static final int corePoolsize = 10;
	
	/** 最大线程数 */
	private static final int maxPoolsize = 10;
	
	/** 允许线星空闲时间(单位:默认为秘) */
	private static final int keepAliveTime = 5;
	
	/** 缓冲队列大小 */
	private static final int queueCapacity = 200;
	
	/** 线程池名前缀 */
	private static final String threadNamePrefix = "mcp-Service-";
	
	@Bean("taskExecutor")
	public ThreadPoolTaskExecutor taskExecutor() {
		ThreadPoolTaskExecutor executor =  new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(corePoolsize) ;
		executor.setMaxPoolSize (maxPoolsize) ;
		executor.setQueueCapacity(queueCapacity) ;
		executor.setKeepAliveSeconds (keepAliveTime) ;
		executor.setThreadNamePrefix(threadNamePrefix) ;
		executor.setRejectedExecutionHandler (new ThreadPoolExecutor.CallerRunsPolicy());
		executor.initialize();
		return executor;
	}

}


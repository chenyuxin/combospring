package com.github.chenyuxin.commonframework.ftp.config;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.chenyuxin.commonframework.ftp.FtpTemplate;

@Configuration
@ConfigurationProperties(prefix = "commonframework.ftp.pool")
public class FtpPoolConfig {
	
	@Autowired FtpConfig ftpConfig;
	
	/**
	 * 连接池最大连接数
	 */
	private int maxActive = 10;
	
	/**
	 * 连接池最大阻塞等待时间
	 */
	private int maxWait = 6000;
	
	/**
	 * 连接池中的最大空闲连接
	 */
	private int maxIdle = 2;
	
	/**
	 * 连接池中的最小空闲连接
	 */
	private int minIdle = 2;
	
	public int getMaxActive() {
		return maxActive;
	}

	public void setMaxActive(int maxActive) {
		this.maxActive = maxActive;
	}

	public int getMaxWait() {
		return maxWait;
	}

	public void setMaxWait(int maxWait) {
		this.maxWait = maxWait;
	}

	public int getMaxIdle() {
		return maxIdle;
	}

	public void setMaxIdle(int maxIdle) {
		this.maxIdle = maxIdle;
	}
	
	public int getMinIdle() {
		return minIdle;
	}

	public void setMinIdle(int minIdle) {
		this.minIdle = minIdle;
	}
	
	@Bean
	public FtpTemplate ftpTemplate() {
		Map<String, GenericObjectPool<FTPClient>> ftpMap = new HashMap<>(ftpConfig.getFtps().size());
		
		ftpConfig.getFtps().forEach((ftpName,ftpInfo) ->{
			
			FtpClientPoolFactory ftpClientPoolFactory = new FtpClientPoolFactory(
					ftpInfo.getHost(),
					ftpInfo.getPort(),
					ftpInfo.getUsername(),
					ftpInfo.getPassword()
			);
			
			GenericObjectPoolConfig<FTPClient> poolconfig = new GenericObjectPoolConfig<>();
			poolconfig.setBlockWhenExhausted(false);
			poolconfig.setMaxTotal(maxActive);
			poolconfig.setMaxWait(Duration.ofMillis(maxWait));
			poolconfig.setMaxIdle(maxIdle);
			poolconfig.setMinIdle(minIdle);
			
			poolconfig.setTestOnBorrow(false);
			poolconfig.setTestOnCreate(false);
			poolconfig.setTestOnReturn(false);
			
			poolconfig.setTestWhileIdle(true);//空闲验证
			poolconfig.setBlockWhenExhausted(true);//对象池耗尽后是否阻塞
			
			poolconfig.setTimeBetweenEvictionRuns(Duration.ofSeconds(5));//5秒验证
			
			ftpMap.put(ftpName, new GenericObjectPool<FTPClient>(ftpClientPoolFactory,poolconfig));
			
		});
		
		FtpTemplate ftpTemplate = new FtpTemplate(ftpMap,ftpConfig.getDefualtFtp());
		
		return ftpTemplate;
	}
	
	
	


}

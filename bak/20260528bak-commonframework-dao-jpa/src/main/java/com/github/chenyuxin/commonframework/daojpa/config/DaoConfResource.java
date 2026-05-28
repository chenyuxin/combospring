package com.github.chenyuxin.commonframework.daojpa.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "commonspring.dao")
public class DaoConfResource {
	
	/**
	 * commonDao是否输出sql
	 */
	public static boolean showSql = false;

	public boolean isShowSql() {
		return DaoConfResource.showSql;
	}

	public void setShowSql(boolean showSql) {
		DaoConfResource.showSql = showSql;
	}

}

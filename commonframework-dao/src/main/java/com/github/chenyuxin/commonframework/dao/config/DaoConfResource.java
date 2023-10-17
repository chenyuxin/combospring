package com.github.chenyuxin.commonframework.dao.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "commonframework.dao")
public class DaoConfResource {
	
	/**
	 * commonDao是否输出sql
	 */
	private static boolean showSql = false;

	public boolean isShowSql() {
		return showSql;
	}

	public void setShowSql(boolean showSql) {
		DaoConfResource.showSql = showSql;
	}
	
	

}

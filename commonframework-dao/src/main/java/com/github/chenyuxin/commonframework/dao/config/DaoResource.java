package com.github.chenyuxin.commonframework.dao.config;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import com.alibaba.druid.DbType;
import com.alibaba.druid.pool.DruidDataSource;
import com.github.chenyuxin.commonframework.base.constant.StringPool;
import com.github.chenyuxin.commonframework.base.util.BaseUtil;
import com.github.chenyuxin.commonframework.dao.common.DaoConst;
import com.github.chenyuxin.commonframework.dao.common.TableType;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class DaoResource {
	
	//加载默认数据源
	@Autowired
	@Qualifier("NamedParameterJdbcTemplate")
	private NamedParameterJdbcTemplate jdbcTemplate;
	
	@Autowired
	private ApplicationContext applicationContext;
	
	@Autowired
	private DaoConfResource daoConfResource;
	
	
	
	
	/**
	 * 数据源缓存 
	 */
	private final static Map<String,NamedParameterJdbcTemplate> cacheJdbcTemplates = new ConcurrentHashMap<String,NamedParameterJdbcTemplate>();
	/**
	 * 使用自定义数据源，不传值使用默认的数据源
	 * @param dataSourceName 数据源在spring配置文件bean的id
	 */
	public NamedParameterJdbcTemplate moreJdbcTemplate(String dataSourceName) {
		if (StringPool.BLANK.equals(dataSourceName) || null == dataSourceName) {
			return jdbcTemplate;//dataSourceName为空时,返回默认的jdbcTemplate
		}
		NamedParameterJdbcTemplate currentJdbcTemplate = cacheJdbcTemplates.get(dataSourceName);
		if (null != currentJdbcTemplate) {
			return currentJdbcTemplate;
		} else {
			DataSource dataSource = (DataSource)applicationContext.getBean(dataSourceName); 
			//Assert.notNull(dataSource, "没有找到对应数据源获取失败");
			cacheJdbcTemplates.put(dataSourceName, new NamedParameterJdbcTemplate(dataSource));
			return cacheJdbcTemplates.get(dataSourceName);
		}
	}

	
	
	
	/**
	 * 根据配置是否,打印sql
	 * @param sql
	 * @param showSql
	 */
	public void printSql(String sql){
		if (daoConfResource.isShowSql()){
//			System.out.println("commonDao: ".concat(sql));
			//日志输出
			log.info("commonDao: " + sql);
		}
	}

	/**
	 * 验证是否需要查询结果缓存，返回查询结果缓存数据
	 * @param <T>
	 * @param sql
	 * @param paramMap
	 * @param dataSrouceName
	 * @return
	 */
	public <T> T getQureyDataCache(String sql,Map<String,Object> paramMap,String dataSourceName) {
		//TODO 使用外部redis缓存
//		if (!daoConfResource.isQureyDataCache()) {
//			return null;//不开启查询结果缓存
//		}
//		checkQureyDataCacheMap();//缓存map查看有无超时，移除超时缓存map
//		String key = getQueryKey(sql,paramMap,dataSourceName);
//		QureyData4Cache<?> QureyData4Cache = QureyDataCacheMap.get(key);
//		if (null != QureyData4Cache ) {
//			return QureyData4Cache.getQureyData();
//		} 
		return null;
	}
	
	/**
	 * 获取查询结果缓存key
	 * @param sql 
	 * @param paramMap
	 * @param dataSrouceName
	 * @return
	 */
	public String getQueryKey(String sql,Map<String,Object> paramMap,String dataSrouceName) {
		StringBuffer sBuffer = new StringBuffer(sql);
//		if (null != paramMap ) { 
//			if (!paramMap.isEmpty()) {
//				sBuffer.append(paramMap);
//			}
//		}
		Optional<Map<String,Object>> optional = Optional.ofNullable(paramMap);
		optional.ifPresent(p -> {
			if( !p.isEmpty() ) sBuffer.append(p);
		});
		
		if (null != dataSrouceName && !StringPool.BLANK.equals(dataSrouceName) ) {
			sBuffer.append(dataSrouceName);
		}
		return BaseUtil.getUUIDC64(sBuffer.toString());
	}



	
	/**
	 * 通过注册的数据源，获取数据库类型
	 * @param dataSourceName
	 * @return
	 */
	public DbType getDataBaseType(String dataSourceName) {
		DruidDataSource dds = (DruidDataSource)applicationContext.getBean(dataSourceName);
		return DbType.of(dds.getDbType());
	}
	
	/**
	 * 缓存默认数据源的 tableType
	 * @param tableType
	 * @param dataSourceName
	 */
	public void putTableTypeCache(TableType tableType, String dataSourceName) {
		if (dataSourceName.equals(DaoConst.defaultDataSourceName)) {//缓存默认数据源的 tableType
			TableType.putTableType(tableType);
		}
	}
	
	

}

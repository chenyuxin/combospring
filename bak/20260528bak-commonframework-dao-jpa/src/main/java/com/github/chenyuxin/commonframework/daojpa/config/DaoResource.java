package com.github.chenyuxin.commonframework.daojpa.config;

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
import com.github.chenyuxin.commonframework.daojpa.common.DaoConst;
import com.github.chenyuxin.commonframework.daojpa.common.TableType;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class DaoResource {

	// 加载默认数据源
	@Autowired
	@Qualifier("NamedParameterJdbcTemplate")
	private NamedParameterJdbcTemplate jdbcTemplate;

	@Autowired
	private ApplicationContext applicationContext;

	/**
	 * 数据源缓存
	 */
	private final static Map<String, NamedParameterJdbcTemplate> cacheJdbcTemplates = new ConcurrentHashMap<String, NamedParameterJdbcTemplate>();

	/**
	 * 使用自定义数据源，不传值使用默认的数据源
	 * 
	 * @param dataSourceName 数据源在spring配置文件bean的id
	 */
	public NamedParameterJdbcTemplate moreJdbcTemplate(String dataSourceName) {
		if (StringPool.BLANK.equals(dataSourceName) || null == dataSourceName) {
			return jdbcTemplate;// dataSourceName为空时,返回默认的jdbcTemplate
		}
		NamedParameterJdbcTemplate currentJdbcTemplate = cacheJdbcTemplates.get(dataSourceName);
		if (null != currentJdbcTemplate) {
			return currentJdbcTemplate;
		} else {
			DataSource dataSource = (DataSource) applicationContext.getBean(dataSourceName);
			// Assert.notNull(dataSource, "没有找到对应数据源获取失败");
			cacheJdbcTemplates.put(dataSourceName, new NamedParameterJdbcTemplate(dataSource));
			return cacheJdbcTemplates.get(dataSourceName);
		}
	}

	/**
	 * 实体管理器缓存
	 */
	private final static Map<String, jakarta.persistence.EntityManager> cacheEntityManagers = new ConcurrentHashMap<>();

	@Autowired
	private jakarta.persistence.EntityManager entityManager;

	/**
	 * 使用自定义数据源的EntityManager
	 * 
	 * @param dataSourceName 数据源name
	 * @return
	 */
	public jakarta.persistence.EntityManager moreEntityManager(String dataSourceName) {
		if (StringPool.BLANK.equals(dataSourceName) || null == dataSourceName
				|| DaoConst.defaultDataSourceName.equals(dataSourceName)) {
			return entityManager;
		}
		jakarta.persistence.EntityManager currentEntityManager = cacheEntityManagers.get(dataSourceName);
		if (null != currentEntityManager) {
			return currentEntityManager;
		} else {
			// Assumption: EntityManager bean name is dataSourceName + "EntityManager"
			String entityManagerBeanName = dataSourceName + DaoConst.EntityManager;
			try { // try getting by name directly if available
				currentEntityManager = (jakarta.persistence.EntityManager) applicationContext
						.getBean(entityManagerBeanName);
			} catch (Exception e) {
				log.warn("Could not find EntityManager bean named '{}'. Falling back to default or error.",
						entityManagerBeanName, e);
				throw new RuntimeException("Could not find EntityManager for dataSource: " + dataSourceName);
			}

			cacheEntityManagers.put(dataSourceName, currentEntityManager);
			return currentEntityManager;
		}
	}

	/**
	 * 根据配置是否,打印sql
	 * 
	 * @param sql sql语句
	 */
	public void printSql(String sql) {
		if (DaoConfResource.showSql) {
			log.info("commonDao: " + sql);
		}
	}

	/**
	 * 验证是否需要查询结果缓存，返回查询结果缓存数据
	 * 
	 * @param <T>
	 * @param sql
	 * @param paramMap
	 * @return
	 */
	public <T> T getQureyDataCache(String sql, Map<String, Object> paramMap, String dataSourceName) {
		// TODO 使用外部redis缓存
		// if (!daoConfResource.isQureyDataCache()) {
		// return null;//不开启查询结果缓存
		// }
		// checkQureyDataCacheMap();//缓存map查看有无超时，移除超时缓存map
		// String key = getQueryKey(sql,paramMap,dataSourceName);
		// QureyData4Cache<?> QureyData4Cache = QureyDataCacheMap.get(key);
		// if (null != QureyData4Cache ) {
		// return QureyData4Cache.getQureyData();
		// }
		return null;
	}

	/**
	 * 获取查询结果缓存key
	 * 
	 * @param sql
	 * @param paramMap
	 * @param dataSrouceName
	 * @return
	 */
	public String getQueryKey(String sql, Map<String, Object> paramMap, String dataSrouceName) {
		StringBuilder sBuilder = new StringBuilder(sql);
		// if (null != paramMap ) {
		// if (!paramMap.isEmpty()) {
		// sBuffer.append(paramMap);
		// }
		// }
		Optional<Map<String, Object>> optional = Optional.ofNullable(paramMap);
		optional.ifPresent(p -> {
			if (!p.isEmpty())
				sBuilder.append(p);
		});

		if (null != dataSrouceName && !StringPool.BLANK.equals(dataSrouceName)) {
			sBuilder.append(dataSrouceName);
		}
		return BaseUtil.getUUIDC64(sBuilder.toString());
	}

	/**
	 * 通过注册的数据源，获取数据库类型
	 * 
	 * @param dataSourceName
	 * @return
	 */
	public DbType getDataBaseType(String dataSourceName) {
		DruidDataSource dds = (DruidDataSource) applicationContext.getBean(dataSourceName);
		return DbType.of(dds.getDbType());
	}

	/**
	 * 缓存默认数据源的 tableType
	 * 
	 * @param tableType
	 * @param dataSourceName
	 */
	public void putTableTypeCache(TableType tableType, String dataSourceName) {
		if (dataSourceName.equals(DaoConst.defaultDataSourceName)) {// 缓存默认数据源的 tableType
			TableType.putTableType(tableType);
		}
	}

}

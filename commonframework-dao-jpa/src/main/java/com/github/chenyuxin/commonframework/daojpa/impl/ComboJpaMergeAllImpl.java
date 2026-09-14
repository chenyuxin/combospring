package com.github.chenyuxin.commonframework.daojpa.impl;

import java.util.Collection;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.chenyuxin.commonframework.daojpa.common.DaoConst;
import com.github.chenyuxin.commonframework.daojpa.config.DaoResource;
import com.github.chenyuxin.commonframework.daojpa.intf.ComboJpa;
import com.github.chenyuxin.commonframework.daojpa.jparun.JpaRunner;

@Service("mergeAll" + DaoConst.JPA_RUNNER_SUFFIX)
public class ComboJpaMergeAllImpl implements ComboJpa {

	private DaoResource daoResource;

	public ComboJpaMergeAllImpl(DaoResource daoResource) {
		this.daoResource = daoResource;
	}

	@Transactional
	@SuppressWarnings("unchecked")
	@Override
	public Object run(JpaRunner jpaRunner) {
		Collection<?> entityList = (Collection<?>) jpaRunner.getEntity();
		if (entityList.isEmpty()) {
			return com.github.chenyuxin.commonframework.daojpa.common.DaoUtil
					.daoMessage(DaoConst.mergeObj_SUCCESS_MESSAGE, 0);
		}

		java.util.List<?> list = new java.util.ArrayList<>(entityList);
		Class<?> clazz = list.get(0).getClass();
		com.github.chenyuxin.commonframework.daojpa.common.TableType tableType = jpaRunner.getTableType();
		String dataSourceName = jpaRunner.getDataSourceName();
		org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate currentJdbcTemplate = daoResource
				.moreJdbcTemplate(dataSourceName);

		if (null == tableType) {
			jakarta.persistence.Table table = clazz.getAnnotation(jakarta.persistence.Table.class);
			tableType = com.github.chenyuxin.commonframework.daojpa.common.TableType.of(table.name());
		}

		com.alibaba.druid.DbType dataBaseType = daoResource.getDataBaseType(dataSourceName);
		String[] fieldNameByIds = com.github.chenyuxin.commonframework.daojpa.common.DaoUtil.getIdsbyObj(clazz);
		java.util.Map<String, Object> paramMapFirst = com.github.chenyuxin.commonframework.daojpa.common.DaoUtil
				.object2Map(list.get(0));
		String sql = com.github.chenyuxin.commonframework.daojpa.common.sql.CommonSql.saveOrUpdateSql(
				paramMapFirst, tableType.getTableName(), dataBaseType, fieldNameByIds);

		daoResource.printSql(sql);

		java.util.Map<String, Object>[] paramMaps = new java.util.HashMap[list.size()];
		for (int i = 0; i < list.size(); i++) {
			paramMaps[i] = com.github.chenyuxin.commonframework.daojpa.common.DaoUtil.object2Map(list.get(i));
		}

		currentJdbcTemplate.batchUpdate(sql, paramMaps);
		daoResource.putTableTypeCache(tableType, dataSourceName);

		return com.github.chenyuxin.commonframework.daojpa.common.DaoUtil.daoMessage(DaoConst.mergeObj_SUCCESS_MESSAGE,
				list.size());
	}

}

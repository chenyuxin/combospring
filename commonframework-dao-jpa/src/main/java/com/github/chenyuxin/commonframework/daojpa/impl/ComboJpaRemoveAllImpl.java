package com.github.chenyuxin.commonframework.daojpa.impl;

import java.util.Collection;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.chenyuxin.commonframework.daojpa.common.DaoConst;
import com.github.chenyuxin.commonframework.daojpa.config.DaoResource;
import com.github.chenyuxin.commonframework.daojpa.intf.ComboJpa;
import com.github.chenyuxin.commonframework.daojpa.jparun.JpaRunner;

@Service("removeAll" + DaoConst.JPA_RUNNER_SUFFIX)
public class ComboJpaRemoveAllImpl implements ComboJpa {

	private DaoResource daoResource;

	public ComboJpaRemoveAllImpl(DaoResource daoResource) {
		this.daoResource = daoResource;
	}

	@SuppressWarnings("unchecked")
	@Transactional
	@Override
	public String run(JpaRunner jpaRunner) {
		Collection<?> entityList = (Collection<?>) jpaRunner.getEntity();
		if (entityList.isEmpty()) {
			return DaoConst.delObj_SUCCESS_MESSAGE;
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

		String[] fieldNameByIds = com.github.chenyuxin.commonframework.daojpa.common.DaoUtil.getIdsbyObj(clazz);
		String sql = com.github.chenyuxin.commonframework.daojpa.common.sql.CommonSql
				.deleteSql(tableType.getTableName(), fieldNameByIds);
		daoResource.printSql(sql);

		java.util.Map<String, Object>[] paramMaps = new java.util.HashMap[list.size()];
		for (int i = 0; i < list.size(); i++) {
			paramMaps[i] = com.github.chenyuxin.commonframework.daojpa.common.DaoUtil.object2Map(list.get(i));
		}

		currentJdbcTemplate.batchUpdate(sql, paramMaps);
		daoResource.putTableTypeCache(tableType, dataSourceName);

		return com.github.chenyuxin.commonframework.daojpa.common.DaoUtil.daoMessage(DaoConst.delObj_SUCCESS_MESSAGE,
				list.size());
	}

}

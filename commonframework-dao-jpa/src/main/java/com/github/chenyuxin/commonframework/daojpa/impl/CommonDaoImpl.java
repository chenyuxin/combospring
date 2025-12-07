package com.github.chenyuxin.commonframework.daojpa.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.github.chenyuxin.commonframework.daojpa.common.sql.CommonSql;
import com.alibaba.druid.DbType;
import com.github.chenyuxin.commonframework.daojpa.common.DaoConst;
import com.github.chenyuxin.commonframework.daojpa.common.DaoUtil;
import com.github.chenyuxin.commonframework.daojpa.common.TableType;
import com.github.chenyuxin.commonframework.daojpa.config.DaoResource;
import com.github.chenyuxin.commonframework.daojpa.intf.CommonDao;
import com.github.chenyuxin.commonframework.daojpa.option.DaoOptions;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Table;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class CommonDaoImpl implements CommonDao {

	private final DaoResource daoResource;

	@Autowired
	CommonDaoImpl(final DaoResource daoResource) {
		this.daoResource = daoResource;
	}

	private boolean isTable(TableType tableType, String dataSourceName) {
		NamedParameterJdbcTemplate currentJdbcTemplate = daoResource.moreJdbcTemplate(dataSourceName);
		String sql = "select count(1) from ".concat(tableType.getTableName()).concat(" where 1=0 ");
		daoResource.printSql(sql);
		try {
			currentJdbcTemplate.queryForObject(sql, new HashMap<>(), Integer.class);
		} catch (DataAccessException e) {
			TableType.delTableType(tableType);
			return false;// 表不存在
		}
		daoResource.putTableTypeCache(tableType, dataSourceName);
		return true;
	}

	@Override
	public boolean isTable(TableType tableType, Object... daoOptionsO) {
		DaoOptions daoOptions = new DaoOptions(daoOptionsO);
		return this.isTable(daoOptions.getTableType(), daoOptions.getDataSourceName());
	}

	private String delDropTable(TableType tableType, String dataSourceName) {
		NamedParameterJdbcTemplate currentJdbcTemplate = daoResource.moreJdbcTemplate(dataSourceName);
		String tableName = tableType.getTableName();
		String sql = "drop table ".concat(tableName);
		daoResource.printSql(sql);
		try {
			currentJdbcTemplate.update(sql, new HashMap<>());
		} catch (DataAccessException e) {
			log.debug(e.getMessage(), e);
			return tableName.concat(": ").concat(DaoConst.delDropTable_FAILED_MESSAGE);
		}
		// daoResource.checkQureyDataCacheMap(dataSourceName,tableName);//TODO 扩展redis缓存
		TableType.delTableType(tableType);// 表被drop后将不再缓存此表类型
		return tableName.concat(": ").concat(DaoConst.delDropTable_SUCCESS_MESSAGE);
	}

	@Override
	public String delDropTable(TableType tableType, Object... daoOptionsO) {
		DaoOptions daoOptions = new DaoOptions(daoOptionsO);
		return this.delDropTable(daoOptions.getTableType(), daoOptions.getDataSourceName());
	}

	private String useTable(Map<String, Object> paramMap, String dataSourceName, String ctrlSql,
			boolean isThrowException) {
		NamedParameterJdbcTemplate currentJdbcTemplate = daoResource.moreJdbcTemplate(dataSourceName);
		daoResource.printSql(ctrlSql);
		Integer rows = null;
		try {
			rows = currentJdbcTemplate.update(ctrlSql, paramMap);
		} catch (DataAccessException e) {
			if (isThrowException) {
				throw new RuntimeException(e.getMessage());
			} else {
				log.debug(e.getMessage(), e);
				// System.out.println(e.getMessage());
				// e.printStackTrace();
				return DaoConst.useTable_FAILED_MESSAGE;
			}
		}
		// daoResource.checkQureyDataCacheMap(dataSourceName,SqlReader.getTablesFromSql(ctrlSql));//TODO
		// 扩展redis缓存
		return DaoUtil.daoMessage(DaoConst.useTable_SUCCESS_MESSAGE, rows);

	}

	@Override
	public String useTable(String ctrlSql, Object... daoOptionsO) {
		DaoOptions daoOptions = new DaoOptions(daoOptionsO);
		return useTable(daoOptions.getParamMap(), daoOptions.getDataSourceName(), ctrlSql,
				daoOptions.isThrowException());
	}

	@SuppressWarnings("unchecked")
	private String saveObjList(List<?> list, TableType tableType, String dataSourceName, boolean isThrowException) {
		NamedParameterJdbcTemplate currentJdbcTemplate = daoResource.moreJdbcTemplate(dataSourceName);
		Class<?> clazz = list.get(0).getClass();
		// System.out.println(clazz.getSimpleName());
		String sql;
		Map<String, Object>[] paramMaps;
		if (list.get(0) instanceof Map map) {// 使用map的封装保存数据
			sql = CommonSql.insertSql4Map(map, tableType);
			paramMaps = list.toArray(new Map[list.size()]);
		} else {// 使用类的list保存数据
			if (null == tableType) {
				String tableName = clazz.getAnnotation(Table.class).name();
				tableType = TableType.of(tableName);
			}
			sql = CommonSql.insertSql(clazz, tableType);
			paramMaps = new HashMap[list.size()];
			for (int i = 0; i < list.size(); i++) {
				// paramMap = DaoUtil.setNull(list.get(i);
				paramMaps[i] = DaoUtil.object2Map(list.get(i));
			}
		}
		daoResource.printSql(sql);
		try {
			currentJdbcTemplate.batchUpdate(sql, paramMaps);
		} catch (DataAccessException e) {
			if (isThrowException) {
				throw new RuntimeException(e.getMessage());
			} else {
				// e.printStackTrace();
				// System.out.println(e.getMessage());
				log.debug(e.getMessage(), e);
				return DaoConst.saveObj_FAILED_MESSAGE.concat(": ").concat(e.getMessage());
			}
		}
		// daoResource.checkQureyDataCacheMap(dataSourceName,tableType.getTableName());//TODO
		// 扩展redis缓存
		daoResource.putTableTypeCache(tableType, dataSourceName);
		return DaoUtil.daoMessage(DaoConst.saveObj_SUCCESS_MESSAGE, list.size());// 添加作业数据量的返回
	}

	@SuppressWarnings("unchecked")
	private String saveObjSingle(Object object, TableType tableType, String dataSourceName, boolean isThrowException) {
		NamedParameterJdbcTemplate currentJdbcTemplate = daoResource.moreJdbcTemplate(dataSourceName);
		Class<?> clazz = object.getClass();
		Map<String, Object> paramMap;
		String sql;
		if (object instanceof Map map) {// 使用map的封装保存数据//使用map的封装保存数据
			sql = CommonSql.insertSql4Map(map, tableType);
			paramMap = map;
		} else {// 使用类的保存数据方式
			if (null == tableType) {
				String tableName = ((Table) clazz.getAnnotation(Table.class)).name();
				tableType = TableType.of(tableName);
			}
			sql = CommonSql.insertSql(clazz, tableType);
			// paramMap = DaoUtil.setNull(Object);
			paramMap = DaoUtil.object2Map(object);
		}
		daoResource.printSql(sql);
		try {
			currentJdbcTemplate.update(sql, paramMap);
		} catch (DataAccessException e) {
			if (isThrowException) {
				throw new RuntimeException(e.getMessage());
			} else {
				// e.printStackTrace();
				// System.out.println(e.getMessage());
				log.debug(e.getMessage(), e);
				return DaoConst.saveObj_FAILED_MESSAGE.concat(": ").concat(e.getMessage());
			}
		}
		// daoResource.checkQureyDataCacheMap(dataSourceName,tableType.getTableName());//TODO
		// 扩展redis缓存
		daoResource.putTableTypeCache(tableType, dataSourceName);
		return DaoConst.saveObj_SUCCESS_MESSAGE;
	}

	@Override
	public String saveObj(Object object, Object... daoOptionsO) {
		DaoOptions daoOptions = new DaoOptions(daoOptionsO);
		if (object instanceof List<?> list) {
			return saveObjList(list, daoOptions.getTableType(), daoOptions.getDataSourceName(),
					daoOptions.isThrowException());
		} else {
			return saveObjSingle(object, daoOptions.getTableType(), daoOptions.getDataSourceName(),
					daoOptions.isThrowException());
		}
	}

	@SuppressWarnings("unchecked")
	private String updateList(List<?> list, String[] fieldNameByIds, TableType tableType, String dataSourceName,
			boolean isThrowException) {
		NamedParameterJdbcTemplate currentJdbcTemplate = daoResource.moreJdbcTemplate(dataSourceName);
		Class<?> clazz = (Class<?>) list.get(0).getClass();
		String sql;
		Map<String, Object>[] paramMaps;

		if (list.get(0) instanceof Map map) {// 使用map的封装保存数据
			sql = CommonSql.updateSql(map, tableType.getTableName(), fieldNameByIds);
			paramMaps = list.toArray(new Map[list.size()]);
		} else {
			Map<String, Object> paramMap = DaoUtil.object2Map(list.get(0));
			if (null == tableType) {
				String tableName = clazz.getAnnotation(Table.class).name();
				tableType = TableType.of(tableName);// 将表名设置为本对象表名
			}
			sql = CommonSql.updateSql(paramMap, tableType.getTableName(), fieldNameByIds);
			paramMaps = new HashMap[list.size()];
			for (int i = 0; i < list.size(); i++) {
				// paramMap = DaoUtil.setNull(list.get(i);
				paramMaps[i] = DaoUtil.object2Map(list.get(i));
			}
		}
		daoResource.printSql(sql);
		try {
			currentJdbcTemplate.batchUpdate(sql, paramMaps);
		} catch (DataAccessException e) {
			if (isThrowException) {
				throw new RuntimeException(e.getMessage());
			} else {
				// System.out.println(e.getMessage());
				// e.printStackTrace();
				log.debug(e.getMessage(), e);
				return DaoConst.updateObj_FAILED_MESSAGE.concat(": ").concat(e.getMessage());
			}
		}
		// daoResource.checkQureyDataCacheMap(dataSourceName,tableType.getTableName());//TODO
		// 扩展redis缓存
		daoResource.putTableTypeCache(tableType, dataSourceName);
		return DaoUtil.daoMessage(DaoConst.updateObj_SUCCESS_MESSAGE, list.size());// 更新作业数据量的返回
	}

	@SuppressWarnings("unchecked")
	private String updateSingle(Object object, String[] fieldNameByIds, TableType tableType, String dataSourceName,
			boolean isThrowException) {
		NamedParameterJdbcTemplate currentJdbcTemplate = daoResource.moreJdbcTemplate(dataSourceName);
		Map<String, Object> paramMap;
		String sql;
		if (object instanceof Map map) {
			sql = CommonSql.updateSql(map, tableType.getTableName(), fieldNameByIds);
			paramMap = map;
		} else {
			paramMap = DaoUtil.object2Map(object);
			if (null == tableType) {
				Table table = object.getClass().getAnnotation(Table.class);
				tableType = TableType.of(table.name());
			}
			sql = CommonSql.updateSql(paramMap, tableType.getTableName(), fieldNameByIds);
		}
		daoResource.printSql(sql);
		try {
			currentJdbcTemplate.update(sql, paramMap);
		} catch (DataAccessException e) {
			if (isThrowException) {
				throw new RuntimeException(e.getMessage());
			} else {
				// System.out.println(e.getMessage());
				// e.printStackTrace();
				log.debug(e.getMessage(), e);
				return DaoConst.updateObj_FAILED_MESSAGE;
			}
		}
		// daoResource.checkQureyDataCacheMap(dataSourceName,tableType.getTableName());//TODO
		// 扩展redis缓存
		daoResource.putTableTypeCache(tableType, dataSourceName);
		return DaoConst.updateObj_SUCCESS_MESSAGE;
	}

	@Override
	public String updateObj(Object object, Object... daoOptionsO) {
		DaoOptions daoOptions = new DaoOptions(daoOptionsO);
		if (object instanceof List<?> list) {
			String[] fieldNameByIds = DaoUtil.getIdsbyObj(list.get(0).getClass());
			return updateList(list, fieldNameByIds, null, daoOptions.getDataSourceName(),
					daoOptions.isThrowException());
		} else {
			String[] fieldNameByIds = DaoUtil.getIdsbyObj(object.getClass());
			return updateSingle(object, fieldNameByIds, null, daoOptions.getDataSourceName(),
					daoOptions.isThrowException());
		}
	}

	@Override
	public String updateMap(Object object, String[] fieldNameByIds, TableType tableType, Object... daoOptionsO) {
		DaoOptions daoOptions = new DaoOptions(daoOptionsO, tableType);
		if (object instanceof List<?> list) {
			return updateList(list, fieldNameByIds, daoOptions.getTableType(), daoOptions.getDataSourceName(),
					daoOptions.isThrowException());
		} else {
			return updateSingle(object, fieldNameByIds, daoOptions.getTableType(), daoOptions.getDataSourceName(),
					daoOptions.isThrowException());
		}
	}

	@SuppressWarnings("unchecked")
	private String saveOrUpdateList(List<?> list, String[] fieldNameByIds, TableType tableType, String dataSourceName,
			boolean isThrowException) {
		NamedParameterJdbcTemplate currentJdbcTemplate = daoResource.moreJdbcTemplate(dataSourceName);
		DbType dataBaseType = daoResource.getDataBaseType(dataSourceName);// 获取数据库类型

		Class<?> clazz = list.get(0).getClass();
		String sql;
		Map<String, Object>[] paramMaps;

		if (list.get(0) instanceof Map map) {// 使用map的封装保存数据
			sql = CommonSql.saveOrUpdateSql(map, tableType.getTableName(), dataBaseType, fieldNameByIds);
			paramMaps = list.toArray(new Map[list.size()]);
		} else {
			Map<String, Object> paramMap = DaoUtil.object2Map(list.get(0));
			if (null == tableType) {
				String tableName = clazz.getAnnotation(Table.class).name();
				tableType = TableType.of(tableName);// 将表名设置为本对象表名
			}
			sql = CommonSql.saveOrUpdateSql(paramMap, tableType.getTableName(), dataBaseType, fieldNameByIds);
			paramMaps = new HashMap[list.size()];
			for (int i = 0; i < list.size(); i++) {
				// paramMap = DaoUtil.setNull(list.get(i);
				paramMaps[i] = DaoUtil.object2Map(list.get(i));
			}
		}
		daoResource.printSql(sql);
		try {
			currentJdbcTemplate.batchUpdate(sql, paramMaps);
		} catch (DataAccessException e) {
			if (isThrowException) {
				throw new RuntimeException(e.getMessage());
			} else {
				// System.out.println(e.getMessage());
				// e.printStackTrace();
				log.debug(e.getMessage(), e);
				return DaoConst.mergeObj_FAILED_MESSAGE.concat(": ").concat(e.getMessage());
			}
		}
		// daoResource.checkQureyDataCacheMap(dataSourceName,tableType.getTableName());//TODO
		// 扩展redis缓存
		daoResource.putTableTypeCache(tableType, dataSourceName);
		return DaoUtil.daoMessage(DaoConst.mergeObj_SUCCESS_MESSAGE, list.size());// 更新作业数据量的返回
	}

	@SuppressWarnings("unchecked")
	private String saveOrUpdateSingle(Object object, String[] fieldNameByIds, TableType tableType,
			String dataSourceName, boolean isThrowException) {
		NamedParameterJdbcTemplate currentJdbcTemplate = daoResource.moreJdbcTemplate(dataSourceName);
		DbType dataBaseType = daoResource.getDataBaseType(dataSourceName);// 获取数据库类型
		Map<String, Object> paramMap;
		String sql;
		if (object instanceof Map map) {
			sql = CommonSql.saveOrUpdateSql(map, tableType.getTableName(), dataBaseType, fieldNameByIds);
			paramMap = (Map<String, Object>) object;
		} else {
			paramMap = DaoUtil.object2Map(object);
			if (null == tableType) {
				Table table = object.getClass().getAnnotation(Table.class);
				tableType = TableType.of(table.name());
			}
			sql = CommonSql.saveOrUpdateSql(paramMap, tableType.getTableName(), dataBaseType, fieldNameByIds);
		}
		daoResource.printSql(sql);
		try {
			currentJdbcTemplate.update(sql, paramMap);
		} catch (DataAccessException e) {
			if (isThrowException) {
				throw new RuntimeException(e.getMessage());
			} else {
				// System.out.println(e.getMessage());
				// e.printStackTrace();
				log.debug(e.getMessage(), e);
				return DaoConst.mergeObj_FAILED_MESSAGE;
			}
		}
		// daoResource.checkQureyDataCacheMap(dataSourceName,tableType.getTableName());//TODO
		// 扩展redis缓存
		daoResource.putTableTypeCache(tableType, dataSourceName);
		return DaoConst.mergeObj_SUCCESS_MESSAGE;
	}

	@Override
	public String saveOrUpdateObj(Object object, Object... daoOptionsO) {
		DaoOptions daoOptions = new DaoOptions(daoOptionsO);
		if (object instanceof List<?> list) {
			String[] fieldNameByIds = DaoUtil.getIdsbyObj(list.get(0).getClass());
			return saveOrUpdateList(list, fieldNameByIds, daoOptions.getTableType(), daoOptions.getDataSourceName(),
					daoOptions.isThrowException());
		} else {
			String[] fieldNameByIds = DaoUtil.getIdsbyObj(object.getClass());
			return saveOrUpdateSingle(object, fieldNameByIds, daoOptions.getTableType(), daoOptions.getDataSourceName(),
					daoOptions.isThrowException());
		}
	}

	@Override
	public String saveOrUpdateMap(Object object, String[] fieldNameByIds, TableType tableType, Object... daoOptionsO) {
		DaoOptions daoOptions = new DaoOptions(daoOptionsO, tableType);
		if (object instanceof List<?> list) {
			return saveOrUpdateList(list, fieldNameByIds, daoOptions.getTableType(), daoOptions.getDataSourceName(),
					daoOptions.isThrowException());
		} else {
			return saveOrUpdateSingle(object, fieldNameByIds, daoOptions.getTableType(), daoOptions.getDataSourceName(),
					daoOptions.isThrowException());
		}
	}

	private String deleteObjSingle(Object object, TableType tableType, String dataSourceName,
			boolean isThrowException) {
		NamedParameterJdbcTemplate currentJdbcTemplate = daoResource.moreJdbcTemplate(dataSourceName);
		Map<String, Object> paramMap = DaoUtil.object2Map(object);
		String[] fieldNameByIds = DaoUtil.getIdsbyObj(object.getClass());
		if (null == tableType) {
			Table table = object.getClass().getAnnotation(Table.class);
			tableType = TableType.of(table.name());
		}
		String sql = CommonSql.deleteSql(tableType.getTableName(), fieldNameByIds);
		daoResource.printSql(sql);
		try {
			currentJdbcTemplate.update(sql, paramMap);
		} catch (DataAccessException e) {
			if (isThrowException) {
				throw new RuntimeException(e.getMessage());
			} else {
				// System.out.println(e.getMessage());
				// e.printStackTrace();
				log.debug(e.getMessage(), e);
				return DaoConst.delObj_FAILED_MESSAGE;
			}
		}
		// daoResource.checkQureyDataCacheMap(dataSourceName,tableType.getTableName());//TODO
		// 扩展redis缓存
		daoResource.putTableTypeCache(tableType, dataSourceName);
		return DaoConst.delObj_SUCCESS_MESSAGE;
	}

	@Override
	public String deleteObj(Object object, Object... daoOptionsO) {
		DaoOptions daoOptions = new DaoOptions(daoOptionsO);
		return deleteObjSingle(object, daoOptions.getTableType(), daoOptions.getDataSourceName(),
				daoOptions.isThrowException());
	}

	private String delTruncateTable(TableType tableType, String dataSourceName, boolean isThrowException) {
		NamedParameterJdbcTemplate currentJdbcTemplate = daoResource.moreJdbcTemplate(dataSourceName);
		String tableName = tableType.getTableName();
		String sql = "truncate table ".concat(tableName);
		daoResource.printSql(sql);
		try {
			currentJdbcTemplate.update(sql, new HashMap<>());
		} catch (DataAccessException e) {
			if (isThrowException) {
				throw new RuntimeException(e.getMessage());
			} else {
				// System.out.println(e.getMessage());
				// e.printStackTrace();
				log.debug(e.getMessage(), e);
				return DaoConst.truncateTable_FAILED_MESSAGE;
			}
		}
		// daoResource.checkQureyDataCacheMap(dataSourceName,tableName);
		daoResource.putTableTypeCache(tableType, dataSourceName);
		return DaoConst.truncateTable_SUCCESS_MESSAGE;
	}

	@Override
	public String delTruncateTable(TableType tableType, Object... daoOptionsO) {
		DaoOptions daoOptions = new DaoOptions(daoOptionsO, tableType);
		return delTruncateTable(daoOptions.getTableType(), daoOptions.getDataSourceName(),
				daoOptions.isThrowException());
	}

	private List<Map<String, Object>> selectObjMap(Map<String, Object> paramMap, String dataSourceName, String sql,
			boolean isThrowException) {
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> objList = (List<Map<String, Object>>) daoResource.getQureyDataCache(sql, paramMap,
				dataSourceName);
		if (null == objList) {
			NamedParameterJdbcTemplate currentJdbcTemplate = daoResource.moreJdbcTemplate(dataSourceName);
			daoResource.printSql(sql);
			try {
				objList = currentJdbcTemplate.queryForList(sql, paramMap);
			} catch (DataAccessException e) {
				if (isThrowException) {
					throw new RuntimeException(e.getMessage());
				} else {
					// System.out.println(e.getMessage());
					// e.printStackTrace();
					log.debug(e.getMessage(), e);
				}
			}
			// daoResource.putQureyDataCache(objList, sql, paramMap, dataSourceName);//TODO
			// 扩展redis缓存
		}
		return objList;
	}

	@Override
	public List<Map<String, Object>> selectObjMap(String sql, Object... daoOptionsO) {
		DaoOptions daoOptions = new DaoOptions(daoOptionsO);
		return selectObjMap(daoOptions.getParamMap(), daoOptions.getDataSourceName(), sql,
				daoOptions.isThrowException());
	}

	@Override
	public List<Map<String, Object>> selectObjMap(List<String> attributeNames, TableType tableType,
			Object... daoOptionsO) {
		DaoOptions daoOptions = new DaoOptions(daoOptionsO, tableType);
		Map<String, Object> paramMapDao = new HashMap<>();
		paramMapDao.putAll(daoOptions.getParamMap());
		String sql = CommonSql.selectSql4Map(attributeNames, daoOptions.getTableType().getTableName(), paramMapDao,
				daoOptions.getQueryConditions());
		List<Map<String, Object>> maps = selectObjMap(paramMapDao, daoOptions.getDataSourceName(), sql,
				daoOptions.isThrowException());
		if (null != maps) {
			daoResource.putTableTypeCache(tableType, daoOptions.getDataSourceName());
		}
		return maps;
	}

	@Override
	public Page<Map<String, Object>> selectObjMap(int currentPage, int pageSize, List<String> attributeNames,
			TableType tableType, Object... daoOptionsO) {
		DaoOptions daoOptions = new DaoOptions(daoOptionsO, tableType);
		daoResource.moreJdbcTemplate(daoOptions.getDataSourceName());// 用于加载数据库驱动名缓存
		DbType dataBaseType = daoResource.getDataBaseType(daoOptions.getDataSourceName());// 获取数据库类型
		Map<String, Object> paramMapDao = new HashMap<>();
		paramMapDao.putAll(daoOptions.getParamMap());
		String sql = CommonSql.selectSql4Map(attributeNames, daoOptions.getTableType().getTableName(),
				currentPage, pageSize, dataBaseType, paramMapDao, daoOptions.getQueryConditions());
		List<Map<String, Object>> objList = selectObjMap(paramMapDao, daoOptions.getDataSourceName(), sql,
				daoOptions.isThrowException());
		Long total = getRecords(tableType, daoOptions);// 返回记录数并缓存默认数据源的tableType
		return new PageImpl<>(objList, PageRequest.of(currentPage - 1, pageSize), total);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> Page<T> selectObj(int currentPage, int pageSize, Class<T> clazz, Object... daoOptionsO) {
		DaoOptions daoOptions = new DaoOptions(daoOptionsO);
		String dataSourceName = daoOptions.getDataSourceName();
		EntityManager currentEntityManager = daoResource.moreEntityManager(dataSourceName);
		@SuppressWarnings("rawtypes")
		JpaRepository repository = new SimpleJpaRepository(clazz, currentEntityManager);
		return repository.findAll(null, PageRequest.of(currentPage - 1, pageSize));
	}

	@Override
	public <T> Page<T> selectObj(String sql, int currentPage, int pageSize, Class<T> clazz, Object... daoOptions) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> List<T> selectObj(String sql, Class<T> clazz, Object... daoOptionsO) {
		DaoOptions daoOptions = new DaoOptions(daoOptionsO);
		NamedParameterJdbcTemplate currentJdbcTemplate = daoResource.moreJdbcTemplate(daoOptions.getDataSourceName());
		Map<String, Object> paramMapDao = new HashMap<>();
		paramMapDao.putAll(daoOptions.getParamMap());

		@SuppressWarnings("unchecked")
		List<T> objList = (List<T>) daoResource.getQureyDataCache(sql, paramMapDao, daoOptions.getDataSourceName());
		if (null == objList) {
			daoResource.printSql(sql);
			try {
				objList = currentJdbcTemplate.query(sql, paramMapDao, new BeanPropertyRowMapper<>(clazz));
			} catch (DataAccessException e) {
				if (daoOptions.isThrowException()) {
					throw new RuntimeException(e.getMessage());
				} else {
					// System.out.println(e.getMessage());
					// e.printStackTrace();
					log.debug(e.getMessage(), e);
				}
			}
			// daoResource.putQureyDataCache(objList, sql, paramMapDao,
			// daoOptions.getDataSourceName());
		}
		return objList;
	}

	@Override
	public <T> List<T> selectObj(Class<T> clazz, Object... daoOptionsO) {
		DaoOptions daoOptions = new DaoOptions(daoOptionsO);
		NamedParameterJdbcTemplate currentJdbcTemplate = daoResource.moreJdbcTemplate(daoOptions.getDataSourceName());
		Map<String, Object> paramMapDao = new HashMap<>();
		paramMapDao.putAll(daoOptions.getParamMap());
		String sql = CommonSql.selectSql(clazz, paramMapDao, daoOptions.getTableType(),
				daoOptions.getQueryConditions());

		@SuppressWarnings("unchecked")
		List<T> objList = (List<T>) daoResource.getQureyDataCache(sql, paramMapDao, daoOptions.getDataSourceName());
		if (null == objList) {
			daoResource.printSql(sql);
			try {
				objList = currentJdbcTemplate.query(sql, paramMapDao, new BeanPropertyRowMapper<>(clazz));
			} catch (DataAccessException e) {
				if (daoOptions.isThrowException()) {
					throw new RuntimeException(e.getMessage());
				} else {
					// System.out.println(e.getMessage());
					// e.printStackTrace();
					log.debug(e.getMessage(), e);
				}
			}
			// daoResource.putQureyDataCache(objList, sql, paramMapDao,
			// daoOptions.getDataSourceName());
		}
		return objList;
	}

	@Override
	public <T> T selectObjSingle(Class<T> clazz, Object... daoOptionsO) {
		DaoOptions daoOptions = new DaoOptions(daoOptionsO);
		NamedParameterJdbcTemplate currentJdbcTemplate = daoResource.moreJdbcTemplate(daoOptions.getDataSourceName());
		Map<String, Object> paramMapDao = new HashMap<>();
		paramMapDao.putAll(daoOptions.getParamMap());
		String sql = CommonSql.selectSql(clazz, paramMapDao, daoOptions.getTableType(),
				daoOptions.getQueryConditions());

		@SuppressWarnings("unchecked")
		T object = (T) daoResource.getQureyDataCache(sql, paramMapDao, daoOptions.getDataSourceName());
		if (null == object) {
			daoResource.printSql(sql);
			try {
				object = currentJdbcTemplate.queryForObject(sql, paramMapDao, new BeanPropertyRowMapper<>(clazz));
			} catch (DataAccessException e) {
				if (daoOptions.isThrowException()) {
					throw new RuntimeException(e.getMessage());
				} else {
					// System.out.println(e.getMessage());
					// e.printStackTrace();
					log.debug(e.getMessage(), e);
				}
			}
			// daoResource.putQureyDataCache(object, sql, paramMapDao,
			// daoOptions.getDataSourceName());
		}
		return object;
	}

	@Override
	public <T> T selectObjSingle(String sql, Class<T> clazz, Object... daoOptionsO) {
		DaoOptions daoOptions = new DaoOptions(daoOptionsO);
		NamedParameterJdbcTemplate currentJdbcTemplate = daoResource.moreJdbcTemplate(daoOptions.getDataSourceName());
		Map<String, Object> paramMapDao = new HashMap<>();
		paramMapDao.putAll(daoOptions.getParamMap());
		@SuppressWarnings("unchecked")
		T object = (T) daoResource.getQureyDataCache(sql, paramMapDao, daoOptions.getDataSourceName());
		if (null == object) {
			daoResource.printSql(sql);
			try {
				object = currentJdbcTemplate.queryForObject(sql, paramMapDao, new BeanPropertyRowMapper<>(clazz));
			} catch (DataAccessException e) {
				if (daoOptions.isThrowException()) {
					throw new RuntimeException(e.getMessage());
				} else {
					// System.out.println(e.getMessage());
					// e.printStackTrace();
					log.debug(e.getMessage(), e);
				}
			}
			// daoResource.putQureyDataCache(object, sql, paramMapDao,
			// daoOptions.getDataSourceName());
		}
		return object;
	}

	@Override
	public <T> T selectBaseObj(String sql, Class<T> clazz, Object... daoOptionsO) {
		DaoOptions daoOptions = new DaoOptions(daoOptionsO);
		NamedParameterJdbcTemplate currentJdbcTemplate = daoResource.moreJdbcTemplate(daoOptions.getDataSourceName());
		Map<String, Object> paramMapDao = new HashMap<>();
		paramMapDao.putAll(daoOptions.getParamMap());
		@SuppressWarnings("unchecked")
		T object = (T) daoResource.getQureyDataCache(sql, paramMapDao, daoOptions.getDataSourceName());
		if (null == object) {
			daoResource.printSql(sql);
			try {
				object = currentJdbcTemplate.queryForObject(sql, paramMapDao, clazz);
			} catch (DataAccessException e) {
				if (daoOptions.isThrowException()) {
					throw new RuntimeException(e.getMessage());
				} else {
					// System.out.println("selectBaseObj自定义sql查询获取基本类: ".concat(e.getMessage()));
					// e.printStackTrace();
					log.debug(e.getMessage(), e);
				}
			}
			// daoResource.putQureyDataCache(object, sql, paramMapDao,
			// daoOptions.getDataSourceName());
		}
		return object;
	}

	@Override
	public <T> List<T> selectBaseObjs(String sql, Class<T> clazz, Object... daoOptionsO) {
		DaoOptions daoOptions = new DaoOptions(daoOptionsO);
		NamedParameterJdbcTemplate currentJdbcTemplate = daoResource.moreJdbcTemplate(daoOptions.getDataSourceName());
		Map<String, Object> paramMapDao = new HashMap<>();
		paramMapDao.putAll(daoOptions.getParamMap());
		@SuppressWarnings("unchecked")
		List<T> objectList = (List<T>) daoResource.getQureyDataCache(sql, paramMapDao, daoOptions.getDataSourceName());
		if (null == objectList) {
			daoResource.printSql(sql);
			try {
				objectList = currentJdbcTemplate.queryForList(sql, paramMapDao, clazz);
			} catch (DataAccessException e) {
				if (daoOptions.isThrowException()) {
					throw new RuntimeException(e.getMessage());
				} else {
					// System.out.println("selectBaseObj自定义sql查询获取基本类: ".concat(e.getMessage()));
					// e.printStackTrace();
					log.debug(e.getMessage(), e);
				}
			}
			// daoResource.putQureyDataCache(objectList, sql, paramMapDao,
			// daoOptions.getDataSourceName());
		}
		return objectList;
	}

	@Override
	public Long getRecords(TableType tableType, Object... daoOptionsO) {
		DaoOptions daoOptions = new DaoOptions(daoOptionsO, tableType);
		Map<String, Object> paramMapDao = new HashMap<String, Object>();
		paramMapDao.putAll(daoOptions.getParamMap());
		String sql = CommonSql.getRecords(daoOptions.getTableType().getTableName(), paramMapDao,
				daoOptions.getQueryConditions());
		// 自定义通用查询条件 已经转换为了sql,并已经将参数变量放入paramMapDao
		Long records = selectBaseObj(sql, Long.class, paramMapDao);
		if (records != null) {// 返回记录数并缓存默认数据源的tableType
			daoResource.putTableTypeCache(tableType, daoOptions.getDataSourceName());
		}
		return records;
	}

}

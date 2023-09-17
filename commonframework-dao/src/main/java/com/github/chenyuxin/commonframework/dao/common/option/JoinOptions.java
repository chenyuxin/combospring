package com.github.chenyuxin.commonframework.dao.common.option;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.druid.sql.ast.statement.SQLJoinTableSource.JoinType;
import com.github.chenyuxin.commonframework.dao.common.sql.CommonSql;

/**
 * 关联查询配置
 */
public class JoinOptions {
	
	private Object currentTable;//当前表属性,对应@Table标注的java类class,或是需要查询的字段的List<String>数据库字段名,当为List时本表查询方式必须有TableType表名对象。
	private DaoOptions currentDaoOptions;//本表查询方式设置
	private String currentSql;//当前表的查询sql
	
	private JoinType joinType;//关联类型,当本属性为空时,本配置为查询主表,否则为从表。
	private Map<String, String> onCol = new HashMap<String, String>();// key为上个表的关联字段名,value为本表的关联字段名。
	
	private List<JoinOptions> apendJoinOptions = new ArrayList<JoinOptions>();//关联表,当本表为主表时或者还有从表时,继续向下关联的从表。
	
	private int joinOrder;//joinOrder序号,用于查询语句表的别名
	
	/**
	 * 关联表配置,关联类型为空的是主表,不为空的是从表
	 * @param currentTable 当前表属性
	 * @param joinType 关联类型
	 * @param daoOptions 本表查询方式设置
	 */
	public JoinOptions(Object currentTable, JoinType joinType, Object... daoOptions){
		this.currentTable = currentTable;
		this.joinType = joinType;
		this.currentDaoOptions = new DaoOptions(daoOptions);
		if ( currentTable instanceof Class<?> table ) {
			this.currentSql = CommonSql.selectSql(table, this.currentDaoOptions.getParamMap(), null, this.currentDaoOptions.getQueryConditions());
		} else if ( currentTable instanceof List ) {
			//TODO 
		}	
	}
	
	/**
	 * 关联表配置
	 * 没有joinType的关联配置(主表)
	 * @param currentTable 当前表属性
	 * @param daoOptions 本表查询方式设置
	 */
	public JoinOptions(Object currentTable, Object... daoOptions){
		this.currentTable = currentTable;
		this.joinType = null;
		this.currentDaoOptions = new DaoOptions(daoOptions);
		if ( currentTable instanceof Class<?> table ) {
			this.currentSql = CommonSql.selectSql(table, this.currentDaoOptions.getParamMap(), null, this.currentDaoOptions.getQueryConditions());
		} else if ( currentTable instanceof List ) {
			//TODO 
		}
	}
	
	public JoinOptions(){}

	public Object getCurrentTable() {
		return currentTable;
	}

	public void setCurrentTable(Object currentTable) {
		this.currentTable = currentTable;
	}

	public List<JoinOptions> getApendJoinOptions() {
		return apendJoinOptions;
	}

	/**
	 * 添加一个新的子关联配置到本关联配置，并返回这个子关联配置
	 * @param currentTable 子关联表属性
	 * @param joinType 子关联类型
	 * @param daoOptions 子关联表查询配置
	 * @return 子关联配置
	 */
	public JoinOptions apendJoinOptions(Object currentTable, JoinType joinType, Object... daoOptions) {
		JoinOptions childJoinOptions = new JoinOptions(currentTable, joinType, daoOptions);
		this.apendJoinOptions.add(childJoinOptions);
		return childJoinOptions;
	}
	
	public void apendJoinOptions(JoinOptions joinOptions) {
		this.apendJoinOptions.add(joinOptions);
	}
	
	public void setApendJoinOptions(List<JoinOptions> joinOptions) {
		this.apendJoinOptions = joinOptions;
	}

	public JoinType getJoinType() {
		return joinType;
	}

	public void setJoinType(JoinType joinType) {
		this.joinType = joinType;
	}

	public DaoOptions getCurrentDaoOptions() {
		return currentDaoOptions;
	}

	public void setCurrentDaoOptions(DaoOptions currentDaoOptions) {
		this.currentDaoOptions = currentDaoOptions;
	}

	/**
	 * key为上个表的关联字段名,value为本表的关联字段名
	 */
	public Map<String, String> getOnCol() {
		return onCol;
	}

	public void setOnCol(Map<String, String> onCol) {
		this.onCol = onCol;
	}
	
	/**
	 * 添加关联条件,数据库字段名
	 * @param key 为上个表的关联字段名
	 * @param value 为本表的关联字段名
	 */
	public void putOnCol(String key,String value){
		this.onCol.put(key, value);
	}

	/**
	 * 当前表的查询sql
	 * @return
	 */
	public String getCurrentSql() {
		return currentSql;
	}

	/**
	 * 获取joinOrder序号,用于查询语句表的别名
	 * @return
	 */
	public int getJoinOrder() {
		return joinOrder;
	}

	/**
	 * 配置joinOrder序号,用于查询语句表的别名
	 * @param joinOrder
	 */
	public void setJoinOrder(int joinOrder) {
		this.joinOrder = joinOrder;
	}
	
	
	

}


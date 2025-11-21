package com.github.chenyuxin.commonframework.dao.common.sql;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.expr.SQLPropertyExpr;
import com.alibaba.druid.sql.ast.statement.SQLSelect;
import com.alibaba.druid.sql.ast.statement.SQLSelectItem;
import com.alibaba.druid.sql.ast.statement.SQLSelectQueryBlock;
import com.alibaba.druid.sql.ast.statement.SQLSelectStatement;
import com.alibaba.druid.sql.parser.SQLParserUtils;
import com.alibaba.druid.sql.parser.SQLStatementParser;
import com.alibaba.druid.sql.parser.Token;

public class DaoSqlReader {

	/**
     * 获取sql语句中查询字段
     *
     * @param sql
     * @param jdbcType
     * @return
     */
    @SuppressWarnings("null")
	public static List<String> getSelectColumns(String sql, String jdbcType) { //类型转换
        List<String> columns = new ArrayList<String>();
        //格式化sql语句
//        String sql = SQLUtils.format(sqlOld, jdbcType);
        if (sql.contains(" * ")) {
            throw new RuntimeException("获取sql语句中查询字段getSelectColumns:不支持语句中带 '*' ，必须明确指定查询的列");
        }
        // parser得到AST
        SQLStatementParser parser = SQLParserUtils.createSQLStatementParser(
                sql, jdbcType);
        //只接受select 语句
        if (!Token.SELECT.equals(parser.getExprParser().getLexer().token()) 
        		//&& !Token.WITH.equals(parser.getExprParser().getLexer().token()) 
        		) {
            throw new RuntimeException("获取sql语句中查询字段getSelectColumns:不支持 " + parser.getExprParser().getLexer().token() + " 语法,仅支持 SELECT 语法");
        }
        List<SQLStatement> stmtList = parser.parseStatementList();
        if (stmtList.size() > 1) {
            throw new RuntimeException("获取sql语句中查询字段getSelectColumns:不支持多条SQL语句,当前是" + stmtList.size() + "条语句");
        }
        //接收查询字段
        List<SQLSelectItem> items = null;
        for (SQLStatement stmt : stmtList) {
            // stmt.accept(visitor);
            if (stmt instanceof SQLSelectStatement s) {
                SQLSelect sqlselect = s.getSelect();
                SQLSelectQueryBlock query = (SQLSelectQueryBlock) sqlselect.getQuery();
                items = query.getSelectList();
                //SQLTableSource tableSource = query.getFrom();
            }
        }
        for (SQLSelectItem s : items) {
            String column;
            if (StringUtils.isEmpty(s.getAlias())) {//判断是否有别名
            	SQLExpr expr = s.getExpr();
            	column = expr.toString();
            	if (expr instanceof SQLPropertyExpr e) {//一般属性
            		column = e.getName();
            	} else {//复杂sql组成的字段列
            		throw new RuntimeException("获取sql语句中查询字段getSelectColumns:复杂sql组成的字段列必须使用别名");
            	}
            	
            } else {
            	column = s.getAlias();
            }
            
            //防止字段重复
            if (!columns.contains(column)) {
                columns.add(column);
            }
        }
        return columns;
    }
    
    
    /**
	 * 将自定义查询sql替换成统计数据量的sql
	 * @param sql
	 * @param jdbcType 数据库类型
	 * @return
	 */
    public static String countSql(String sql, DbType dbType) {
    	if (sql.contains(" * ")) {
            throw new RuntimeException("获取sql语句中查询字段getSelectColumns:不支持语句中带 '*' ，必须明确指定查询的列");
        }
        // parser得到AST
        SQLStatementParser parser = SQLParserUtils.createSQLStatementParser(
                sql, dbType.name().toLowerCase());
        //只接受select 语句
        if (!Token.SELECT.equals(parser.getExprParser().getLexer().token()) 
        		//&& !Token.WITH.equals(parser.getExprParser().getLexer().token()) 
        		) {
            throw new RuntimeException("获取sql语句中查询字段getSelectColumns:不支持 " + parser.getExprParser().getLexer().token() + " 语法,仅支持 SELECT 语法");
        }
        List<SQLStatement> stmtList = parser.parseStatementList();
        if (stmtList.size() > 1) {
            throw new RuntimeException("获取sql语句中查询字段getSelectColumns:不支持多条SQL语句,当前是" + stmtList.size() + "条语句");
        }
        
        //接收查询
        String countSql = null;
        for (SQLStatement stmt : stmtList) {
            // stmt.accept(visitor);
            if (stmt instanceof SQLSelectStatement s) {
                SQLSelect sqlselect = s.getSelect();
                SQLSelectQueryBlock query = (SQLSelectQueryBlock) sqlselect.getQuery();
                //items = query.getSelectList();
                //SQLTableSource tableSource = query.getFrom();
                query.setOrderBy(null);
                query.getSelectList().clear();
                SQLExpr SQLExpr = SQLUtils.toSQLExpr("count(1)", dbType);
                query.addSelectItem(SQLExpr);
                countSql = sqlselect.toString();
            }
        }
    	
    	return countSql;
    }
    
    
}
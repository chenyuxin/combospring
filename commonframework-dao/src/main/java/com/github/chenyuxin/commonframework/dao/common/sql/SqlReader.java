package com.github.chenyuxin.commonframework.dao.common.sql;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.github.chenyuxin.commonframework.base.constant.StringPool;


/**
 * 解析sql，获取sql语句的表名
 * (未完全验证所有可执行sql)
 * 
 * by chenyuxin
 */
public class SqlReader {
	
	private static final String[] BEFOR_KEY = {" DELETE FROM ", " FROM ", " INTO ", " DELETE ", " TABLE ", " UPDATE " };
	
	private static final String[] SPLIT_KEY = { " ALTER COLUMN ",  " WHERE ", " SET ", " ON ", " VALUES ", " LIMIT ", " UNION ", " ADD ", " DROP ", " MODIFY ", "(" , ")" };
	private static final String[] SPLIT_KEY_JOIN = {","," JOIN "};
	 
	/**
	 * 通过sql获取表名（返回大写字母表名）
	 * @param sql 确认已经是通过数据库执行过的sql
	 * @return
	 */
	public static String[] getTablesFromSql(String sql) {
		//System.out.println(sql);
		sql = formatSQLtoUpperCase(sql);
		List<String> list = new ArrayList<String>();
		splitSQL(sql, list);
		//System.out.println(list);
		Set<String> tables = new HashSet<>();
		for (String str : list) {
			for (String key : SPLIT_KEY_JOIN) {
				if (str.contains(key)) {
					String[] tablesTemp = str.split(key);
					for (int i=0;i<tablesTemp.length;i++) {
						putTables(tablesTemp[i],tables);
					}
				} else {
					putTables(str,tables);
				}
			}
		}
		
		return tables.toArray(new String[tables.size()]);
	}
	
 
	private static void splitSQL(String sql,List<String> temp){
		int beforKeyNameIndex = -1;
		String beforKeyName = null;//取最前的一个BEFOR_KEY
		for (String bk : BEFOR_KEY) {
			if (sql.contains(bk)) {
				if(!isRealBk(bk,sql)) {
					continue;
				}
				int currentBkIndex = sql.indexOf(bk);
				if (beforKeyNameIndex == -1) {
					beforKeyNameIndex = currentBkIndex;
					beforKeyName = bk;
				} else if (currentBkIndex < beforKeyNameIndex && beforKeyNameIndex != -1) {
					beforKeyNameIndex = currentBkIndex;
					beforKeyName = bk;
				}
			}
		}
		
		if(!StringPool.BLANK.equals(beforKeyName) && null != beforKeyName) {
			sql = sql.substring(beforKeyNameIndex + beforKeyName.length());
			
			int SplitKeyNameIndex = -1;
			String SplitKeyName = null;//取最前的一个SPLIT_KEY
			for (String sk : SPLIT_KEY) {
				if (sql.contains(sk)) {
					int currentSkIndex = sql.indexOf(sk);
					if (SplitKeyNameIndex == -1) {
						SplitKeyNameIndex = currentSkIndex;
						SplitKeyName = sk;
					} else if (currentSkIndex < SplitKeyNameIndex && SplitKeyNameIndex != -1) {
						SplitKeyNameIndex = currentSkIndex;
						SplitKeyName = sk;
					}
					
					   
				}
			}
			if (!StringPool.BLANK.equals(sql.trim()) && null != sql ) {
				if (!StringPool.BLANK.equals(SplitKeyName) && null != SplitKeyName) {
					String tableName = sql.substring(0,SplitKeyNameIndex);
					if (!StringPool.BLANK.equals(tableName.trim())) {
						temp.add(tableName);
					}
					sql = sql.substring(SplitKeyNameIndex + SplitKeyName.length());
					splitSQL(sql, temp);
				} else {
					//System.out.println("no SplitKey:"+sql);
					temp.add(sql);
				}
			}	
		}
	}
 
	private static boolean isRealBk(String bk,String sql) {
		if (" UPDATE ".equals(bk)) {
			if (sql.contains(" ON DUPLICATE KEY UPDATE ")) {
				return sql.indexOf(" ON DUPLICATE KEY UPDATE ") + 17 != sql.indexOf(bk);
			}
		}
		if (" DELETE ".equals(bk)) {
			if (sql.contains(" DELETE FROM ")) {
				return sql.indexOf(" DELETE FROM ") != sql.indexOf(" DELETE ");
			}
		}
		
		return true;
	}


	/**
	 * 格式化整理sql,方便统一游标
	 * @param sql 大写sql用于本工具查找
	 * @return
	 */
	private static String formatSQLtoUpperCase(String sql) {
		sql= " ".concat(sql);
		sql = sql.replaceAll("\\s+", " ");
		sql = sql.replaceAll("\\s+,\\s+", ",");
		//System.out.println(sql.toUpperCase());
		return sql.toUpperCase();
	}
	
	/**
	 * 格式化整理sql,方便统一游标
	 * @param sql 不替换大小写，保留sql中的参数命名和别名的大小写状态
	 * @return
	 */
	public static String formatSQL(String sql) {
		sql= " ".concat(sql);
		sql = sql.replaceAll("\\s+", " ");
		sql = sql.replaceAll("\\s+,\\s+", ",");
		//System.out.println(sql.toUpperCase());
		return sql;
	}
	
	
	private static void putTables(String tableNameTemp,Set<String> tables) {
		String tableTemp = trimLeft(tableNameTemp);
		if (tableTemp.indexOf(" ") != -1) {
			tableTemp = tableTemp.substring(0, tableTemp.indexOf(" "));
		}
		/**
		 *
		if(tableTemp.contains(".")) {//移除带有.前缀用户的表名
			tableTemp = tableTemp.substring(tableTemp.indexOf(".")+1);
		}
		*/
		tables.add(tableTemp);
	}
	
	/**
     * 去左空格
     * @param str
     * @return
     */
    private static String trimLeft(String str) {
        if (str == null || StringPool.BLANK.equals(str) ) {
            return str;
        } else {
            return str.replaceAll("^[　 ]+", StringPool.BLANK);
        }
    }
    
    
    /**
     * 获取最外层查询语句主from的游标位置,游标指定到from的末尾
     * @param sql 执行了formatSQL格式化方法的 sql 
     * @return
     */
    public static int getCountFromCursor(String sql,int readCursor){
    	sql = formatSQLtoUpperCase(sql);
    	int fromCursor = readCursor;//游标
		//int readCursor = 0;//当前读游标
		int beforeBracketCursor = readCursor;//前括号游标
		int afterBracketCursor = readCursor;//后括号游标
		readCursor = sql.indexOf("FROM", readCursor+1);//从读到的位置开始找
		fromCursor = readCursor;
		a:
		while (true) {
			beforeBracketCursor= sql.indexOf("(", afterBracketCursor);
			if (beforeBracketCursor != -1 && beforeBracketCursor < readCursor) {//如果前括号的位置在当前FROM的前面找到
				afterBracketCursor = getAfterBracketCursor(beforeBracketCursor,sql);//得到前括号对应的后括号的位置
				if (afterBracketCursor != -1){
					if (afterBracketCursor > readCursor) {
						return getCountFromCursor(sql,readCursor+4);
					} else {
						continue a;
					}
				}
			}
			break;
		}
		return fromCursor+4;
    }
	
    /**
     * 获取前括号对应的后括号的游标  ( )
     * @param beforeBracketCursor 前括号游标
     * @param text
     * @return
     */
	private static int getAfterBracketCursor(int beforeBracketCursor,String text){
		int afterBracketCursor = text.indexOf(")", beforeBracketCursor);
		if (afterBracketCursor > beforeBracketCursor) {//找到有后括号
			int beforeBracketCursor2 = text.indexOf("(",beforeBracketCursor+1); //前括号的位置有没有其他前括号
			if (beforeBracketCursor2 > beforeBracketCursor && beforeBracketCursor2 < afterBracketCursor ) {
				afterBracketCursor = getAfterBracketCursor(afterBracketCursor+1, text);
			}
		} else if (afterBracketCursor == beforeBracketCursor) {
			//从有对应其它前括号的后括号开始找，刚好挨着后面一个后括号就是要找的对应前扩号的后括号游标
		} else {
			throw new RuntimeException("getAfterBracketCursor没有找对前括号对应的后括号");
		}
		return afterBracketCursor;
	}
	
    /**
     * 移除order by语句
     * @param sql
     * @return
     */
    public static String removeOrderBy(String sql) {
    	// https://blog.csdn.net/weixin_39968640/article/details/114914372
    	String regex  = "\\s+ORDER\\s+BY\\s+.*?\\s(DESC|ASC)|\\s+ORDER\\s+BY\\s+.*?\\s";

    	Pattern pattern = Pattern.compile(regex,Pattern.CASE_INSENSITIVE);
        StringBuffer sb = new StringBuffer();
        Matcher m = pattern.matcher(sql);
        while (m.find()) {
            m.appendReplacement(sb, StringPool.BLANK);
        }
        m.appendTail(sb);
        return sb.toString();
    }
    
    
    
    
    
//	public static void main(String[] args) {
////		String sql = "select a, b, c from user u,org o where u.id = o.uid and u.id in (select id from user_org uo)";
////		String sql = "select * FROM     u   order    by  b";
////		String sql = "select count(a) from `user`  u  ,   org  o  where u.a = o.b group by d";
////		String sql = "select a, b, c from user u,org o where u.id = o.uid and u.id in (select id from user_org uo LEFT JOIN table_o to where uo.id not exist (select id from org_user  ))";
////		String sql = "select a, b, c from user u INNER   JOIN   cen_user.org o on u.id = o.id where user.a = 'aaa'";
////		String sql = "insert into brmp_conf_origin_system_mdbase ( MODEL_TAB_NAME,MODEL_DESCRIPTION,MODEL_NAME,MODEL_ID,MODEL_CREATE_TIME,AUDIT_STATUS,DATA_NUM,MODEL_UPDETE_TIME,UPDATE_FREQUENCY,ORIGIN_SYSTEM_ID,STATUS ) values ( :modelTabName, :modelDescription, :modelName, :modelId, :modelCreateTime, :auditStatus, :dataNum, :modelUpdeteTime, :updateFrequency, :originSystemId, :status ) ON DUPLICATE KEY UPDATE MODEL_TAB_NAME = :modelTabName,MODEL_DESCRIPTION = :modelDescription,MODEL_NAME = :modelName,MODEL_CREATE_TIME = :modelCreateTime,AUDIT_STATUS = :auditStatus,DATA_NUM = :dataNum,MODEL_UPDETE_TIME = :modelUpdeteTime,UPDATE_FREQUENCY = :updateFrequency,ORIGIN_SYSTEM_ID = :originSystemId,STATUS = :status)" ;
////		String sql = "insert into brmp_conf_origin_system_model (ORIGIN_SYSTEM_ID,MODEL_ID,MODEL_COL_NAME,MODEL_COL_DISPLAY_NAME,MODEL_COL_TYPE,MODEL_COL_LENTH,MODEL_COL_DECIMAL_LENTH,DISPLAY_ORDER,PK,MODEL_COL_DIC,SPECIAL_FIELD) values (:originSystemId,:modelId,:modelColName,:modelColDisplayName,:modelColType,:modelColLenth,:modelColDecimalLenth,:displayOrder,:pk,:modelColDic,:specialField)"  ;
////		String sql = "select ORIGIN_SYSTEM_ID,ORIGIN_SYSTEM_NAME,ORIGIN_SYSTEM_CNAME,ORIGIN_SYSTEM_URL,USERNAME,PASSWORD,ENCRYPTION_TYPE from brmp_conf_origin_system_info where ORIGIN_SYSTEM_ID = :originSystemId"  ;
////		String sql = "delete   from brmp_conf_origin_system_model where model_id = :modelId";
//		String sql = "select xgbz as \"xgbz\" ,Id as \"Id\" ,originId as \"originId\" ,updateTime as \"updateTime\" ,nameID as \"nameID\"  from (select rownum as rowno, xgbz,Id,originId,updateTime,nameID from ( select xgbz,Id,originId,updateTime,nameID from MD_CB1AC05A525B4345920B_TEMP) where rownum <= 20 ) where rowno > 0 ";
//		
//		String[] tables = getTablesFromSql(sql);
//		for(String string : tables) {
//			System.out.println(string);
//		}
//	}
	
	
 
}
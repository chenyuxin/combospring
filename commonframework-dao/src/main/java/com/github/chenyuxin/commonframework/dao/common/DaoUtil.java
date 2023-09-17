package com.github.chenyuxin.commonframework.dao.common;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.github.chenyuxin.commonframework.base.constant.StringPool;
import com.github.chenyuxin.commonframework.dao.common.anotation.Column;
import com.github.chenyuxin.commonframework.dao.common.anotation.Id;

/**
 * Dao工具
 */
public class DaoUtil {
	
	/**
	 * 作用完成行数组合消息
	 * @param daoMessageString 执行消息
	 * @param rows 作用行数
	 * @return
	 */
	public static final String daoMessage(String daoMessageString,Integer rows) {
		return null==rows ? daoMessageString : daoMessageString+" rows:["+rows+"]";
	}
	
	/**
	 * 下划线转驼峰法
	 * @param line 源字符串
	 * @param smallCamel 大小驼峰,是否为小驼峰
	 * @return 转换后的字符串
	 */
	public static String underlineToCamel(String line,boolean smallCamel){
		if(line==null||StringPool.BLANK.equals(line)){
			return StringPool.BLANK;
		}
		StringBuffer sb=new StringBuffer();
		Pattern pattern=Pattern.compile(DaoConst.underlineToCamelRegex);
		Matcher matcher=pattern.matcher(line);
		while(matcher.find()){
			String word=matcher.group();
			sb.append(smallCamel&&matcher.start()==0?Character.toLowerCase(word.charAt(0)):Character.toUpperCase(word.charAt(0)));
			int index=word.lastIndexOf(StringPool.UNDERLINE);
			if(index>0){
				sb.append(word.substring(1, index).toLowerCase());
			}else{
				sb.append(word.substring(1).toLowerCase());
			}
		}
		return sb.toString();
	}
	
	/**
	 * 驼峰法转下划线
	 * @param line 源字符串
	 * @return 转换后的字符串
	 */
	public static String camelToUnderline(String line){
		if(line==null||StringPool.BLANK.equals(line)){
			return StringPool.BLANK;
		}
		line=String.valueOf(line.charAt(0)).toUpperCase().concat(line.substring(1));
		StringBuffer sb=new StringBuffer();
		Pattern pattern=Pattern.compile(DaoConst.camelToUnderlineRegex);
		Matcher matcher=pattern.matcher(line);
		while(matcher.find()){
			String word=matcher.group();
			sb.append(word.toUpperCase());
			sb.append(matcher.end()==line.length()?StringPool.BLANK:StringPool.UNDERLINE);
		}
		return sb.toString();
	}
	
	/**
	 * 类转map
	 * @param object
	 * @return
	 */
	public static Map<String,Object> object2Map(Object object){
		Map<String,Object> result=new HashMap<String,Object>();
		//获得类的的属性名 数组
		Field[]fields=object.getClass().getDeclaredFields();
		try {
			for (Field field : fields) {
				field.setAccessible(true);
				String name = new String(field.getName());
				
				Column column = field.getAnnotation(Column.class);
				if (null != column){
					if (StringPool.BLANK.equals(column.name())){//若字段名为""，则在数据库中不使用该字段
						continue;
					} else {
						name = column.name().toLowerCase();
					}	
				}
				
				result.put(name, field.get(object));
	 
			}
		}catch (Exception e){
			e.printStackTrace();
			return null;
		}
		return result;
	}
	
	/**
	 * 通过类获取id的属性名数组
	 * 方便用于delete和update
	 * @param <T>
	 * @param object
	 * @return
	 */
	public static <T> String[] getIdsbyObj(Class<T> clazz){
		Field[] fields = clazz.getDeclaredFields();
		List<String> fieldNameByIdsList = new ArrayList<String>();
		try {
			for (Field field : fields) {
				field.setAccessible(true);
				
				Id id = field.getAnnotation(Id.class);
				if (id == null){
					continue;
				}
				
				String name = new String(field.getName());
				Column column = field.getAnnotation(Column.class);
				if (null != column){
					if (StringPool.BLANK.equals(column.name())){//若字段名为""，则在数据库中不使用该字段
						continue;//不使用此字段也就这个id主键也不使用
					} else {
						name = column.name().toLowerCase();
					}	
				}
				fieldNameByIdsList.add(name);
			}
		}catch (Exception e){
			e.printStackTrace();
			return null;
		}
		
		String[] fieldNameByIds = fieldNameByIdsList.toArray(new String[fieldNameByIdsList.size()]);
		return fieldNameByIds;
	}
	
	
	
//	/**
//	 * 替换对象数据的空值 为默认值
//	 * @param <T>
//	 * @param obj javaBean对象
//	 * @param isNotNullValues 不同数据类型具有的不同替换值
//	 * @throws IllegalArgumentException
//	 * @throws IllegalAccessException
//	 * @throws ParseException
//	 */
//	public static <T> void setNullObject(T obj,Object... isNotNullValues) throws Exception {
//		if (null == isNotNullValues || Array.getLength(isNotNullValues) == 0) {
//			return;
//		}
//		Map<Class<?>, Object> notNullMap = new HashMap<Class<?>, Object>();
//		for (Object isNotNullValue : isNotNullValues) {
//			notNullMap.put(isNotNullValue.getClass(), isNotNullValue);
//		}
//		
//		Field[] fields = obj.getClass().getDeclaredFields();
//		for (Field field : fields) {
//			field.setAccessible(true);//不检查 直接取值
//			Type genericType = field.getGenericType();
//			if ( genericType.equals(String.class) ) {
//				String value = (String) field.get(obj);
//				if (null == value || StringPool.BLANK.equals(value)) {//将空值替换
//					field.set(obj, notNullMap.get(genericType));
//	  			}
//			} else if (null == field.get(obj)) {//如果是时间类型为空则返回一个默认日期，避免返回空字符串格式报错
//				field.set(obj,notNullMap.get(genericType));
//			}
//		}
//	}
	
//	/**
//     * Clob 转 String
//     */
//    public static String getString(Clob c) {
//        try {
//            return c.getSubString(1, (int) c.length());
//        } catch (Exception e) {
//            return null;
//        }
//    }
// 
//    /**
//     * String 转 Clob
//     */
//    public static Clob getClob(String s) {
//        try {
//            return new SerialClob(s.toCharArray());
//        } catch (Exception e) {
//            return null;
//        }
//    }
	
	
}
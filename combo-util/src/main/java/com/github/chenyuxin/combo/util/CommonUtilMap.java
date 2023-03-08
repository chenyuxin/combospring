package com.github.chenyuxin.combo.util;

import java.util.Map;

import com.github.chenyuxin.combo.base.constant.StringPool;
import com.github.chenyuxin.combo.base.type.other.FieldType;



/**
 * map操作相关工具类
 *
 */
public class CommonUtilMap {
	
	/**
	 * 获取map里的value
	 * @param <T> 
	 * @param <K>
	 * @param clazz 取值类型
	 * @param key 获取值的key
	 * @param map 获取值的map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T, K> T getValueOfMap(Class<T> clazz, K key, Map<K, ?> map ) {
		Object value =  map.get(key);
		if (null == value) {
			return null;
		} else {
			FieldType fieldType = FieldType.getFieldType(clazz);
			if (null == fieldType) {
				return (T) value;//非基本数据类型直接转对象
			} else {
				return (T) fieldType.parse(value);//转成对应基本数据类型
			}
		}
		
	}
	
	/**
	 * 获取map里的value,为null时或获取时异常则默认一个值
	 * @param <T>
	 * @param <K>
	 * @param defaultResult 不能取正常的值时默认一个值
	 * @param key 获取值的key
	 * @param map 获取值的map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T, K> T getValueOfMap(T defaultResult, K key, Map<K, ?> map) {
		try {
			Object value =  map.get(key);
			if (null == value) {
				return defaultResult;
			} else {
				FieldType fieldType = FieldType.getFieldType(defaultResult.getClass());
				if (null == fieldType) {
					return (T) value;//非基本数据类型直接转对象
				} else {
					return (T) fieldType.parse(value);//转成对应基本数据类型
				}
			}
		} catch (Exception e){
			return defaultResult;
		}
	}
	
	/**
	 * 获取map里的stingValue,为null时或获取时异常则默认值为StringPool.BLANK
	 * @param <K>
	 * @param key 获取值的key
	 * @param map 获取值的map
	 * @return
	 */
	public static <K> String getStringOfMap(K key,Map<K, ?> map) {
		return getValueOfMap(StringPool.BLANK,key,map);
	}

}

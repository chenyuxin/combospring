package com.github.chenyuxin.combo.util;

import java.lang.reflect.Field;
import java.util.Map;

import com.github.chenyuxin.combo.base.constant.StringPool;
import com.github.chenyuxin.combo.base.util.BaseUtil;
import com.github.chenyuxin.combo.base.util.DaoUtil;
import com.github.chenyuxin.combo.base.util.MyObj;


/**
 * 对象的属性值补全，替换工具类
 *
 */
public class CommonUtilReplaceObj {
	
	/**
	 * 深克隆一份新的数据对象  ！！！被复制的对象以及包含的所有对象 必须要有无参的构造方法！！！
	 * @param obj (因为是通过反射进行的深克隆，被复制的对象，以及这个对象的所有属性，<"无需"> 间接或者直接地实现 Serializable 接口。)
	 * @return
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	public static <T> T deepClone(T obj) throws Exception {
		return MyObj.deepClone(obj);
	}
	
	/**
	 * 补全对象obj1的数据，如果obj2有能补充的数据。(根据两对象的相同属性名补充)
	 * @param obj1  被操作的对象
	 * @param obj2  模板对象
	 */
	@SuppressWarnings("rawtypes")
	public static void supplement(Object obj1,Object obj2) {
		Class clz1 = obj1.getClass();
		Class clz2 = obj2.getClass();
		
		Field[] fields = clz1.getDeclaredFields();
		for (Field field : fields) {
			Field field2;
			try {
				field2 = clz2.getDeclaredField(field.getName());
				if (field2.getType() != field.getType()) {//字段类型不相同
					continue;
				}
				field.setAccessible(true);
				field2.setAccessible(true);
				
				if ( field.getType() == String.class ){
					if ( null == field.get(obj1) || StringPool.BLANK.equals((String)field.get(obj1)) ){
						field.set(obj1, field2.get(obj2));
					}
				} else {
					if (field.get(obj1) == null){
						field.set(obj1, field2.get(obj2));
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}
			
			
		}
		
	}
	
	
	/**
	 * 更新对象obj1的数据，如果obj2有能更新的数据，
	 * 不能更新则保留obj1原来的数据。(根据两对象的相同属性名更新)
	 * @param obj1  被操作的对象
	 * @param obj2  模板对象
	 */
	@SuppressWarnings("rawtypes")
	public static void supplementUpdate(Object obj1,Object obj2) {
		Class clz1 = obj1.getClass();
		Class clz2 = obj2.getClass();
		Field[] fields = clz1.getDeclaredFields();
		for (Field field : fields) {
			Field field2;
			try {
				field2 = clz2.getDeclaredField(field.getName());
				if (field2.getType() != field.getType()) {//字段类型不相同
					continue;
				}
				field.setAccessible(true);
				field2.setAccessible(true);
				
				if ( field.getType() == String.class ){
					if (null != field2.get(obj2) && !StringPool.BLANK.equals((String)field2.get(obj2))){
						field.set(obj1, field2.get(obj2));
					}
				} else {
					if (field2.get(obj2) != null){
						field.set(obj1, field2.get(obj2));
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}
			
		}
		
	}
	
	/**
	 * 更新对象obj1的数据，如果obj2有能更新的数据，
	 * 不能更新则保留obj1原来的数据。(根据两对象的相同属性名更新)
	 * @param obj1  被操作的对象
	 * @param obj2  模板对象
	 * @param excludesNames 不更新的字段，对象属性名
	 */
	@SuppressWarnings("rawtypes")
	public static void supplementExcludesUpdate(Object obj1, Object obj2, String... excludesNames) {
		Class clz1 = obj1.getClass();
		Class clz2 = obj2.getClass();
		Field[] fields = clz1.getDeclaredFields();
		a:
		for (Field field : fields) {
			for (String excludesName : excludesNames){
				if( excludesName.equals(field.getName()) ) {
					continue a;
				}
			}
			
			Field field2;
			try {
				field2 = clz2.getDeclaredField(field.getName());
				if (field2.getType() != field.getType()) {//字段类型不相同
					continue;
				}
				field.setAccessible(true);
				field2.setAccessible(true);
				
				if ( field.getType() == String.class ){
					if (null != field2.get(obj2) && !StringPool.BLANK.equals((String)field2.get(obj2))){
						field.set(obj1, field2.get(obj2));
					}
				} else {
					if (field2.get(obj2) != null){
						field.set(obj1, field2.get(obj2));
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}
			
		}
		
	}
	
	/**
	 * 只替换数组中对应的属性值的obj1的数据，从obj2赋值obj1。(根据两对象的相同属性名更新)
	 * @param obj1  被操作的对象
	 * @param obj2  模板对象
	 * @param propertyNames 只替换的属性名
	 */
	@SuppressWarnings("rawtypes")
	public static void replaceSelectProperty( Object obj1, Object obj2, String... propertyNames){
		Class clz1 = obj1.getClass();
		Class clz2 = obj2.getClass();
		for (String propertyName : propertyNames) {
			Field field1;
			Field field2;
			try {
				field1 = clz1.getDeclaredField(propertyName);
				field2 = clz2.getDeclaredField(propertyName);
				field1.setAccessible(true);
				field2.setAccessible(true);
				field1.set(obj1, field2.get(obj2));
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}
			
		}
		
	}
	
	/**
	 * 替换obj1属性的值，但不包括数组中的属性，从obj2赋值obj1。(根据两对象的相同属性名更新)
	 * @param obj1  被操作的对象
	 * @param obj2  模板对象
	 * @param excludesNames 不替换的属性名
	 */
	@SuppressWarnings("rawtypes")
	public static void replaceExcludesProperty( Object obj1, Object obj2, String... excludesNames){
		Class clz1 = obj1.getClass();
		Class clz2 = obj2.getClass();
		
		Field[] fields = clz1.getDeclaredFields();
		a:
		for (Field field : fields) {
			for (String excludesName : excludesNames){
				if( excludesName.equals(field.getName()) ) {
					continue a;
				}
			}
			
			Field field2;
			try {
				field2 = clz2.getDeclaredField(field.getName());
				
				field.setAccessible(true);
				field2.setAccessible(true);
				
				field.set(obj1, field2.get(obj2));
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}
			
		}
	}
	
	/**
	 * 替换obj1全部属性的值，从obj2赋值obj1。(根据两对象的相同属性名更新)
	 * @param obj1  被操作的对象
	 * @param obj2  模板对象
	 */
	public static void replaceAllProperty( Object obj1, Object obj2 ) {
		replaceExcludesProperty(obj1, obj2, StringPool.BLANK);
	}

	/**
	 * 替换map对应的指定属性的值，从obj2赋值obj1。(属性名可不同)
	 * @param obj1  被操作的对象
	 * @param obj2
	 * @param propertyMap map中key为obj1的属性名，value为obj2的属性名。
	 */
	@SuppressWarnings({ "rawtypes" })
	public static void replaceSpecifiedProperty( Object obj1, Object obj2, Map<String, String> propertyMap) {
		Class clz1 = obj1.getClass();
		Class clz2 = obj2.getClass();
		for ( Map.Entry<String, String> entry : propertyMap.entrySet() ) {
			Field field1;
			Field field2;
			try {
				field1 = clz1.getDeclaredField(entry.getKey());
				field2 = clz2.getDeclaredField(entry.getValue());
				field1.setAccessible(true);
				field2.setAccessible(true);
				field1.set(obj1, field2.get(obj2));
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}
		}
	}
	
	
	/**
	 * 替换对象数据的所有空值 为默认值
	 * @param <T>
	 * @param obj javaBean对象
	 * @param isNotNullValues 不同数据类型具有的不同替换值
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws ParseException
	 */
	public static <T> void setNullObject(T obj,Object... isNotNullValues) throws Exception {
		DaoUtil.setNullObject(obj,isNotNullValues);
	}
	
	
	/**
	 * 替换字符串的空值为指定值
	 * @param string 待判断的字符串
	 * @param isNotNullString 替换空值的默认字符串
	 * @return
	 */
	public static String setNullString(String string, String isNotNullString) {
		return BaseUtil.setNullString(string, isNotNullString);
	}
	
	/**
	 * 替换某类型的空值为指定值
	 * @param <T>
	 * @param obj 待判断的值
	 * @param isNotNullValue 替换空值的默认值
	 * @return
	 */
	public static <T> T setNull(T obj,T isNotNullValue) {
		return BaseUtil.setNull(obj,isNotNullValue);
	}
	
}

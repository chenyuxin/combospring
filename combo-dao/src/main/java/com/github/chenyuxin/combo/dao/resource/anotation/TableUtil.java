package com.github.chenyuxin.combo.dao.resource.anotation;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.chenyuxin.combo.base.constant.StringPool;

public class TableUtil {
	
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
	
	
	
	
}

package com.github.chenyuxin.commonframework.util.transform;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import lombok.extern.slf4j.Slf4j;

/**
 * 将对象与枚举值转换
 */
@Slf4j
public class TransformUtil {
	
	/**
	 * 将对象里标注有@Transform注解的字段值域转换
	 * @param <T>
	 * @param origin
	 * @throws Exception 
	 */
	public static <T> void transform(T origin) {
		Field[] fields = origin.getClass().getDeclaredFields();
		for (Field field : fields) {
			Transform transform = field.getAnnotation(Transform.class);
			if (null != transform) {
				field.setAccessible(true);
				boolean isTransform = false;
				try {
					Class<? extends Enum<?>> enumClass = transform.enumType();
					Enum<?>[] es = enumClass.getEnumConstants();
					for (Enum<?> e : es) {
						//System.out.println(e.name());
						Method getValueMethod =  e.getClass().getDeclaredMethod("getValue");
						Object value = getValueMethod.invoke(e);
						if (field.get(origin).equals(value) ) {
							field.set(origin, e.getClass().getDeclaredMethod("getName").invoke(e));
							isTransform = true;
							break;
						}
					}
				} catch (Exception e) {
					log.info("Transform值域转换出错:",e);
				} finally {
					if(transform.enableDefaultValue() && !isTransform) {
						try {
							field.set(origin, transform.defaultValue());
						} catch (Exception e) {	} 
					}
				}
				
			}
		}
		
	}

}

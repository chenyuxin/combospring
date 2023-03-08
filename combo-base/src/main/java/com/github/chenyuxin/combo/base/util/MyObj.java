package com.github.chenyuxin.combo.base.util;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 对象的一些操作
 */
public class MyObj {

	/**
	 * 深克隆一份新的数据对象     ！！！被复制的对象以及包含的所有对象 必须要有无参的构造方法！！！
	 * @param obj (因为是通过反射进行的深克隆，被复制的对象，以及这个对象的所有属性，<"无需"> 间接或者直接地实现 Serializable 接口。)
	 * @return
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	public static <T> T deepClone(T obj) {
//		String json = JSON.toJSONString(obj);
//		return (T) JSON.parseObject(json, new TypeReference<T>(){});
		
		
//		try {
//			ByteArrayOutputStream bos = new ByteArrayOutputStream();
//			ObjectOutputStream oos = new ObjectOutputStream(bos);
//			oos.writeObject(obj);
//			ByteArrayInputStream bais = new ByteArrayInputStream(bos.toByteArray());
//			ObjectInputStream ois = new ObjectInputStream(bais);
//			@SuppressWarnings("unchecked")
//			T t = (T) ois.readObject();
//			return t;
//		} catch (Exception e) {
//			System.out.println("克隆出错deepCloneException"+e.getStackTrace());
//			return null;
//		}
		
		return tierClone(obj);
		
	}
	
	/**
	 * 深克隆对象核心方法
	 * @param <T>
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked" })
	private static <T> T tierClone(T obj) {
		if (null == obj) {
			return null;
		}
		Class<T> clazz = (Class<T>) obj.getClass();
		if (clazz.isPrimitive() || clazz == String.class || clazz == Long.class || clazz == Boolean.class 
				|| clazz == Short.class || clazz == Integer.class || clazz == Character.class || clazz == Float.class 
				|| clazz == Double.class || clazz == Byte.class || clazz == BigDecimal.class || clazz == Class.class ) {
			return obj;
		}
		try {
			if (obj instanceof Date) {
				return clazz.getDeclaredConstructor(long.class).newInstance(((Date) obj).getTime());
			}
			if (clazz.isArray()) {
				return cloneArray(obj);
			} 
			//System.out.println(clazz.getName());
			if ( obj instanceof Map ) {
				return cloneMap(obj);
			} else if ( obj instanceof List ) {
				return cloneList(obj);
			} else {
				if (Modifier.isPrivate(clazz.getDeclaredConstructor().getModifiers())) {
					return obj;//私有化的构造方法说明是单例对象，直接返回单例对象。
				}
				T t = clazz.getDeclaredConstructor().newInstance();//对象必须要有无参的构造方法
				Field[] fields = t.getClass().getDeclaredFields();
				for (Field field : fields) {
					if (Modifier.isStatic(field.getModifiers())) {//静态类型引用地址唯一，不替换
						continue;
					} 
		//			else if (Modifier.isFinal(field.getModifiers())) {
		//				
		//			}
					field.setAccessible(true);
					field.set(t, tierClone(field.get(obj)));
				}	
				return t;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}	
		
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static <T> T cloneList(T obj) throws Exception {
		List objList = ((List) obj);
		List tList = objList.getClass().getDeclaredConstructor().newInstance();
		for (int i=0;i<objList.size();i++) {
			tList.add(tierClone(objList.get(i)));
		}
		return (T) tList;
	}


	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static <T> T cloneMap(T obj) throws Exception {
		Map objMap  = ((Map) obj );
		Map tMap = objMap.getClass().getDeclaredConstructor().newInstance();
		Iterator<Map.Entry> e = objMap.entrySet().iterator();
		while (e.hasNext()) {
			Map.Entry en = e.next();
			tMap.put(tierClone(en.getKey()), tierClone(en.getValue()));
		}
		return (T) tMap;
	}


	private static <T> T cloneArray(T obj) {
		int len = Array.getLength(obj);
		@SuppressWarnings("unchecked")
		T array = (T) Array.newInstance(obj.getClass().getComponentType(), len);
		for(int i = 0; i < len; i++){
			Array.set(array, i, tierClone(Array.get(obj, i)));
		}
		return array;
	}
	
	
	
	/**
	 * 执行某个对象的方法
	 * @param obj 执行对象
	 * @param methodName 执行对象的方法名
	 * @param args 执行方法的参数
	 * @return
	 * @throws InvocationTargetException 
	 * @throws IllegalArgumentException 
	 * @throws IllegalAccessException 
	 */
	public static Object invokeMethod(Object obj, String methodName, Object... args) throws Exception {
		Class<?>[] parameterTypes = new Class[args.length];
		for (int i = 0; i < args.length; i++) {
			if (null == args[i]) {
				throw new RuntimeException("参数中有null值,无法确定传参类型,尝试传入参数类型的执行方法， parameterType has null, no method can be invoke");
			} else {
				parameterTypes[i] = args[i].getClass();
			}
        }
		return invokeMethod(obj,methodName,parameterTypes, args);
	}
	
	
	/**
	 * 执行某个对象的方法
	 * @param obj 执行对象
	 * @param methodName 执行对象的方法名
	 * @param parameterTypes 参数类型
	 * @param args 执行方法的参数
	 * @return
	 * @throws Exception
	 */
	public static Object invokeMethod(Object obj, String methodName, Class<?>[] parameterTypes, Object... args) throws Exception {
		Class<?> clazz;
		if (obj instanceof Class) {
			clazz = (Class<?>) obj;
		} else {
			clazz = obj.getClass();
		}
		Method m = null;//循环获取到对应方法名的方法，无则获取父类方法。
		for (; clazz != Object.class && m == null; clazz = clazz.getSuperclass()) {
			try {
				m = clazz.getDeclaredMethod(methodName, parameterTypes);
				//System.out.println("method:"+m.getName());
			} catch (Exception e) {
				//如果没有获取到方法，什么都不做，输出null继续循环
			}
        }
		if (null == m) {
			throw new RuntimeException("没有找到可调用的方法 no method can be invoke");
		}
		m.setAccessible(true);//允许使用私有方法
		return m.invoke(obj,args);
	}
	

}

package com.github.chenyuxin.commonframework.xml;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.basic.BigDecimalConverter;
import com.thoughtworks.xstream.converters.basic.ByteConverter;
import com.thoughtworks.xstream.converters.basic.DateConverter;
import com.thoughtworks.xstream.converters.basic.DoubleConverter;
import com.thoughtworks.xstream.converters.basic.IntConverter;
import com.thoughtworks.xstream.converters.basic.LongConverter;
import com.thoughtworks.xstream.converters.basic.ShortConverter;
import com.thoughtworks.xstream.io.naming.NoNameCoder;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.XppDriver;
import com.thoughtworks.xstream.security.AnyTypePermission;

public class XmlBeanUtils {

	/**
	 * bean转为的xml格式
	 * @param bean
	 * @param aliasName
	 * @return
	 */
	public static String beanToXml(Object bean){
		XStream xStream = new XStream(new XppDriver(new NoNameCoder()));//使用NoNameCoder，是为了避免带下划线的字段，在生成xml时被自动替换为双下划线的问题。
		//xStream.autodetectAnnotations(true);
		xStream.processAnnotations(bean.getClass());
		xStream.setMode(XStream.NO_REFERENCES);
		//xStream.alias(bean.getClass().getSimpleName(), bean.getClass());//此处修改标签别名会引起xml转bean无法识别
		String s = xStream.toXML(bean);
		//System.out.println(s);
		return s;
	}
	
	/**
	 * 复杂格式转换
	 * @param <T>
	 * @param xml
	 * @param clazz
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T xmlToBean(String xml, Class<T> clazz){
		XStream xStream = new XStream(new DomDriver(xml, new NoNameCoder()));
		//XStream.setupDefaultSecurity(xStream);//安全防护
		xStream.addPermission(AnyTypePermission.ANY);//尽量限制所需的最低权限
		//xStream.allowTypes(new Class[]{clazz,City.class});//防护设置允许类
		xStream.processAnnotations(clazz);//对类开启注解
		xStream.registerConverter(new DoubleConverter());
		xStream.registerConverter(new ShortConverter());
		xStream.registerConverter(new BigDecimalConverter());
		xStream.registerConverter(new ByteConverter());
		xStream.registerConverter(new IntConverter());
		xStream.registerConverter(new LongConverter());
		xStream.registerConverter(new DateConverter());
		xStream.ignoreUnknownElements();
		return (T) xStream.fromXML(xml);
	}
	
}


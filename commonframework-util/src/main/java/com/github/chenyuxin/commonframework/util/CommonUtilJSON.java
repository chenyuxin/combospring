package com.github.chenyuxin.commonframework.util;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONWriter.Feature;

/**
 * JSON工具类
 *
 */
public class CommonUtilJSON {
	
	/**
	 * 使用此方法配置日期格式等设置
	 * 再调用fastjson的toJSONString方法
	 * 调用此方法等同全局配置
	 */
	public static String toJSONString(Object obj){
		//TypeUtils.compatibleWithFieldName =true;
		//return JSON.toJSONString(obj,SerializerFeature.WriteMapNullValue,SerializerFeature.WriteNullStringAsEmpty,SerializerFeature.WriteDateUseDateFormat);
		return JSON.toJSONString(obj,"yyyy-MM-dd HH:mm:ss",Feature.WriteMapNullValue,Feature.WriteNonStringValueAsString);
	}
	
	// Map<Integer, List<String>> entityMap = JSON.parseObject(Entity, new TypeReference<Map<Integer, List<String>>>() {});
	//我这里解析的key存储的是整型，value存储的是list类型，可以根据自己需要进行转换。
	//主要是new TypeReference<Map<Integer, List<String>>>() {} 这个类

	//或者是 自己新建一个类  比如
//	class MyMap  {
//		private Map<Integer,List<String>> map=new HashMap();
//		//getter setter 省略
//	}
//	//然后使用如下的方式进行转换。
//	MyMap entityMap = JSON.parseObject(Entity,MyMap.class);
//	Map<Integer, List<String>>  map=entityMap.getMap();//即可拿到


	
	/**
     * json的Key值转化为小写
     * @param json
     * @return
     */
    public static String transformLowerCase(String json){
        String regex = "[\\\"' ]*[^:\\\"' ]*[\\\"' ]*:";// (\{|\,)[a-zA-Z0-9_]+:

        Pattern pattern = Pattern.compile(regex);
        StringBuilder sb = new StringBuilder();
        Matcher m = pattern.matcher(json);
        while (m.find()) {
            m.appendReplacement(sb, m.group().toLowerCase());
        }
        m.appendTail(sb);
        return sb.toString();
    }
    
    /**
     * json的Key名称替换
     * @param json 原json
     * @param renameMap 需要替换的key名称对照Map,其中key为原名称,value为重命名名称
     * @return
     */
    public static String renameKey(String json,Map<String, String> renameMap) {
    	String regex = "[\\\"' ]*[^:\\\"' ]*[\\\"' ]*:";

        Pattern pattern = Pattern.compile(regex);
        StringBuilder sb = new StringBuilder();
        Matcher m = pattern.matcher(json);
        while (m.find()) {
        	String oldKeyName = m.group().substring(1,m.group().length()-2);
        	String newName = renameMap.get(oldKeyName);
        	if(newName != null && !"".equals(newName)) {
        		m.appendReplacement(sb, m.group().substring(0,1).concat(newName).concat(m.group().substring(m.group().length()-2)));
        	}
        }
        m.appendTail(sb);
        return sb.toString();
	}

}

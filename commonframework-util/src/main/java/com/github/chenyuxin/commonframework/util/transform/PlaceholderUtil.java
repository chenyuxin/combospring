package com.github.chenyuxin.commonframework.util.transform;

import java.util.Map;

import org.springframework.util.PropertyPlaceholderHelper;

/**
 * 占位符替换工具
 * <br>	默认使用 ${paramName} 占位符
 */
public class PlaceholderUtil {
	
	private static PropertyPlaceholderHelper pph = new PropertyPlaceholderHelper("${","}");
	
	/**
	 * 替换变量参数
	 * @param content 带有${XXX} 占位符 的内容
	 * @param paramMap 替换变量参数
	 * @return
	 */
	public static String replaceElementValue(String content, Map<String, String> paramMap) {
		return pph.replacePlaceholders(content, paramMap::get);
	}
	

}

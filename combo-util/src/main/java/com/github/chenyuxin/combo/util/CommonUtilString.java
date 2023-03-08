package com.github.chenyuxin.combo.util;

import com.github.chenyuxin.combo.base.constant.StringPool;


/**
 * 字符串 通用方法
 */
public class CommonUtilString {
	
	
	/**
     * 去左空格
     * @param str
     * @return
     */
    public static String trimLeft(String str) {
        if (str == null || str.equals(StringPool.BLANK)) {
            return str;
        } else {
            return str.replaceAll("^[　 ]+", StringPool.BLANK);
        }
    }
    
    /**
     * 去右空格
     * @param str
     * @return
     */
    public static String trimRight(String str) {
        if (str == null || str.equals(StringPool.BLANK)) {
            return str;
        } else {
            return str.replaceAll("[　 ]+$", StringPool.BLANK);
        }
    }
    
    /**
     * 去掉所有空格和制表符
     * @param str
     * @return
     */
    public static String trimAll(String str) {
    	if (str == null || str.equals(StringPool.BLANK)) {
            return str;
        } else {
        	return str.replaceAll("\\s*", StringPool.BLANK);
        }
	}
    
    /**
     * 字符串对象转为String型
     * @param object
     * @return
     */
    public static String parseString(Object object) {
    	return (null == object) ? null : object.toString();
    }
    
    
	/**
	 * 截取字符串，根据开始字符串和结束字符串获取。
	 * @param repString 源字符串
	 * @param beginString 开始字符串 (第一个)
	 * @param endString 结束字符串 (从开始字符串算起第一个)
	 * @return
	 */
	public static String subString(String repString, String beginString, String endString) {
		int begingIndex = repString.indexOf(beginString) + beginString.length();
		int endIndex = repString.indexOf(endString, begingIndex);
		return repString.substring(begingIndex, endIndex);
	}
	
	/**
	 * 截取字符串，开始游标和结束游标，结束大了不报错
	 * @param rep 源字符串
	 * @param begin 开始
	 * @param end 结束
	 * @return
	 */
	public static String subString(Object rep, int begin, int end) {
		String repStr = String.valueOf(rep);
		return repStr.substring(begin, end>repStr.length()?repStr.length():end);
	}
	
	
	
	

}

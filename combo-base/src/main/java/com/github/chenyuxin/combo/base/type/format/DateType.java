package com.github.chenyuxin.combo.base.type.format;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.github.chenyuxin.combo.base.constant.StringPool;
import com.github.chenyuxin.combo.base.type.Type;

public class DateType implements Format<String, Date>,Type{
	
	private volatile static DateType instance = null;
	
	private DateType() {
	}
	
	public static DateType getInstance() {  
        if (null == instance) {  
        	synchronized (DateType.class) {
    			if (null == instance) {  
    				instance = new DateType();  
    			}  
    		}  
        }  
        return instance;  
    }
	
	public static final String DEFAULT_FORMAT_TYPE = "yyyy-MM-dd HH:mm:ss";
	
	static final String[] parsePatterns = {DEFAULT_FORMAT_TYPE,"yyyy-MM-dd","yyyyMMdd"};//默认转换日期格式
	
	static final String typeName = "dateType";
	
	@Override
	public String getTypeName() {
		return typeName;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public DateType getType() {
		return getInstance();
	}

	@Override
	public String getFormatValue(Object oValue) {
		SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_FORMAT_TYPE);
		String format = sdf.format(oValue);
		format = format.replaceAll(" 00:00:00", StringPool.BLANK);
		return format;
	}
	
	/**
	 * 只在本次转换格式
	 */
	public String getFomatValue(Object oValue,String pattern) {
		if (null == oValue) {
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.format(oValue);
	}

	@Override
	public Date parse(String value) {
		return getParseValue(value, parsePatterns);
	}
	
	/**
	 * 自定义转换格式 解析字符串为Date
	 * @param str 
	 * @param parsePatterns
	 * @return
	 */
	public Date getParseValue(String str,String... parsePatterns) {
		if (str == null) {
            return null;
        }
        
        SimpleDateFormat parser = new SimpleDateFormat();
        final ParsePosition pos = new ParsePosition(0);
        
        for (final String parsePattern : parsePatterns) {

            String pattern = parsePattern;

            // LANG-530 - need to make sure 'ZZ' output doesn't get passed to SimpleDateFormat
            if (parsePattern.endsWith("ZZ")) {
                pattern = pattern.substring(0, pattern.length() - 1);
            }
            
            parser.applyPattern(pattern);
            pos.setIndex(0);

            String str2 = str;
            // LANG-530 - need to make sure 'ZZ' output doesn't hit SimpleDateFormat as it will ParseException
            if (parsePattern.endsWith("ZZ")) {
                str2 = str.replaceAll("([-+][0-9][0-9]):([0-9][0-9])$", "$1$2"); 
            }

            final Date date = parser.parse(str2, pos);
            if (date != null && pos.getIndex() == str2.length()) {
                return date;
            }
        }
        return null;
	}


}




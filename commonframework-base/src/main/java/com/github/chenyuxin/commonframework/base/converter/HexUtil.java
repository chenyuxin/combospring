package com.github.chenyuxin.commonframework.base.converter;

import com.github.chenyuxin.commonframework.base.constant.IdCodeConst;
import com.github.chenyuxin.commonframework.base.constant.StringPool;

/**
 * 16进制工具类
 */
public class HexUtil {
	
	/**
	 * byte数组转16进制大写字符串
	 */
	public static String toHexString(byte[] b) {
        return toHexString(b, true);
	}
	
	/**
	 * byte数组转16进制大写字符串
	 * @param b byte数组
	 * @param toUpperCase 是否大写
	 */
	public static String toHexString(byte[] b,boolean toUpperCase) {
        return toHexString(b, toUpperCase ? IdCodeConst.hexDigits : IdCodeConst.idBase64);
	}
	
	
	/**
	 * 16进制字符串转byte数组
	 * @param hexString
	 * @return
	 */
	public static byte[] hexStringToBytes(String hexString) {   
	    if (hexString == null || StringPool.BLANK.equals(hexString)) {   
	        return null;   
	    }   
	    hexString = hexString.toUpperCase();   
	    int length = hexString.length() / 2;   
	    byte[] b = new byte[length];   
	    for (int i = 0; i < length; i++) {   
	        int pos = i * 2;   
	        b[i] = (byte) (IdCodeConst.hexDigits.indexOf(hexString.charAt(pos)) << 4 | IdCodeConst.hexDigits.indexOf(hexString.charAt(pos + 1)));   
	    }   
	    return b;   
	} 
	
	
	
	
	
	/**
	 * byte数组转16进制字符串
	 */
	private static String toHexString(byte[] b,String hexDigits) {
		if (b == null || b.length == 0 ) { return null; }
		int j = b.length;  
        char chars[] = new char[j * 2];  
        int k = 0;  
        for (int i = 0; i < j; i++) {  
            byte byte0 = b[i];
            chars[k++] = hexDigits.charAt(byte0 >>> 4 & 0xf);
            chars[k++] = hexDigits.charAt(byte0 & 0xf);
        }
        return new String(chars);
	}


}

package com.github.chenyuxin.combo.base.util;

import java.util.UUID;

import com.github.chenyuxin.combo.base.constant.IdCodeConst;
import com.github.chenyuxin.combo.base.constant.RegexConst;
import com.github.chenyuxin.combo.base.constant.StringPool;

/**
 * 基本的工具类，供内部程序包使用<br>
 * 不提供API，外部使用请找CommonUtil等API
 */
public class BaseUtil {
	
	
	/**
	 * 替换字符串类型的空值为指定值
	 * @param string 待判断的字符串
	 * @param isNotNullString 替换空值的默认字符串
	 * @return
	 * @throws Exception
	 */
	public static String setNullString(String string, String isNotNullString) {
		if (null == string || StringPool.BLANK.equals(string) ) {
			string = isNotNullString;
		}
		return string;
	}
	
	/**
	 * 替换某类型的空值为指定值
	 * @param <T>
	 * @param obj 待判断的值
	 * @param isNotNullValue 替换空值的默认值
	 * @return
	 */
	public static <T> T setNull(T obj,T isNotNullValue) {
		if (null == obj) {
			obj = isNotNullValue;
		} else if (obj instanceof CharSequence c ) {
			if ( c.length() == 0 ) {
				obj = isNotNullValue;
			}
		}
		return obj;
	}
	
	/**
	 * 是否为Base64字符串
	 * @param str
	 * @return
	 */
	public static boolean isBase64(String str) {
		return str.matches(RegexConst.BASE64);
	}
	
	
	/**
     * 根据字符串生成唯一id<br>
     * 无`-`的UUID字符串
     * @param str
     * @return
     */
    public static String getUUIDC(String str) {
    	UUID uuid = UUID.nameUUIDFromBytes(str.getBytes(CharsetUtil.CHARSET_UTF_8));
    	return toStringUUID(uuid);
    }
    
    /**
     * 返回无`-`的UUID字符串
     * @param uuid
     * @return
     */
    public static String toStringUUID(UUID uuid) {
    	long mostSigBits = uuid.getMostSignificantBits();
    	long leastSigBits = uuid.getLeastSignificantBits();
    	
    	char[] buf = new char[32];
    	int mask = 15;//面具
    	
    	int mostSigBit1 = (int) (mostSigBits & 0x000000ffffffffL);//低32
    	int mostSigBit2 = (int) (mostSigBits >> 32);//高32
    	for (int i=7;i>=0;i--) {
    		buf[8 + i]  = IdCodeConst.idBase64.charAt(mostSigBit1 & mask);
    		mostSigBit1 >>= 4;
    	}
    	for (int i=7;i>=0;i--) {
    		buf[i]  = IdCodeConst.idBase64.charAt(mostSigBit2 & mask);
    		mostSigBit2 >>= 4;
    	}
    	
    	int leastSigBit1 = (int) (leastSigBits & 0x000000ffffffffL);//低32
    	int leastSigBit2 = (int) (leastSigBits >> 32);//高32
    	for (int i=7;i>=0;i--) {
    		buf[24 + i]  = IdCodeConst.idBase64.charAt(leastSigBit1 & mask);
    		leastSigBit1 >>= 4;
    	}
    	for (int i=7;i>=0;i--) {
    		buf[16 + i]  = IdCodeConst.idBase64.charAt(leastSigBit2 & mask);
    		leastSigBit2 >>= 4;
    	}
    	
    	return new String(buf);
    }
    
    /**
     * 根据字符串生成唯一id<br>
     * 自编64进制的UUID字符串
     * @param str
     * @return
     */
    public static String getUUIDC64(String str) {
    	UUID uuid = UUID.nameUUIDFromBytes(str.getBytes(CharsetUtil.CHARSET_UTF_8));
    	return toStringUUID64(uuid);
    }
    
    /**
     * 返回自编64进制的UUID字符串
     * @param uuid
     * @return 长度为22
     */
    public static String toStringUUID64(UUID uuid) {
    	long mostSigBits = uuid.getMostSignificantBits();
    	long leastSigBits = uuid.getLeastSignificantBits();
    	
    	char[] buf = new char[22];
    	
    	//前10位写入
        digits64(mostSigBits>>4,buf,0, 10);
        
        //中间2位写入
        long most4 = (mostSigBits & 15) << 8;
        long least8 = leastSigBits >>> 56;
        digits64(most4|least8 ,buf,10, 2);
        
        //后10位写入
        digits64(leastSigBits<<4,buf,12,10);
        
        return new String(buf);
    }
    
    
    
    /**
	 * 转换成64进制来表示
	 * @param val 二进制内容
	 * @param buf 要写入的字符缓冲区
	 * @param offset 目标缓冲区中起始位置的偏移量
	 * @param digits 要写的字符数
	 * @return
	 */
	private static void digits64(long val,char[] buf,int offset, int digits) {
        long hi = 1L << (digits * 6);//shift用6,64进制
        long binaryNum  = hi | (val & (hi - 1));
        formatUnsignedLong(binaryNum,6,buf,offset,digits);
    }
    
    
    
    /**
	 * Format a long (treated as unsigned) into a character buffer.
     * @param val the unsigned long to format
     * @param shift the log2 of the base to format in (6 for idbase64, 4 for hex, 3 for octal, 1 for binary)
     * @param buf the character buffer to write to
     * @param offset the offset in the destination buffer to start at
     * @param len the number of characters to write
     * @return the lowest character location used
	 */
	public static int formatUnsignedLong(long val,int shift, char[] buf, int offset, int len) {
		int charPos = len;
        int radix = 1 << shift;
        int mask = radix - 1;
        do {
            buf[offset + --charPos] =  IdCodeConst.idBase64.charAt((int) val & mask);
            val >>>= shift;
        } while (val != 0 && charPos > 0);
        
        return charPos;
    }
    
    
    
    
    
    
    
    

}
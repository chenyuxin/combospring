package com.github.chenyuxin.combo.util;

import java.util.UUID;

import com.github.chenyuxin.combo.base.constant.IdCodeConst;
import com.github.chenyuxin.combo.base.util.BaseUtil;

/**
 * uuid常用工具
 */
public class CommonUtilUUID {
	/**
	 * 随机生成唯一id<br>
	 * 无`-`的UUID字符串
	 * @return
	 */
    public static String getUUID(){
    	UUID uuid = UUID.randomUUID();
    	return BaseUtil.toStringUUID(uuid);
    }
    
    /**
     * 根据字符串生成唯一id<br>
     * 无`-`的UUID字符串
     * @param str
     * @return
     */
    public static String getUUIDC(String str) {
    	return BaseUtil.getUUIDC(str);
    }
    
    
    /**
	 * 随机生成唯一id<br>
	 * 自编64进制的UUID字符串
	 * @return
	 */
    public static String getUUID64(){
    	UUID uuid = UUID.randomUUID();
    	return BaseUtil.toStringUUID64(uuid);
    }
    
    /**
     * 根据字符串生成唯一id<br>
     * 自编64进制的UUID字符串
     * @param str
     * @return 长度22
     */
    public static String getUUIDC64(String str) {
    	return BaseUtil.getUUIDC64(str);
    }
    
    
    /**
     * 根据uuid字符串生成UUID对象
     * @param uuidStr 无`-`的uuid字符串
     * @return
     */
    public static UUID fromString(String uuidStr) {
    	long mostSigBits=0;
    	long leastSigBits=0;
    	int mask = 15;
    	for(int i=mask;i>=0;i--) {
    		int val = IdCodeConst.idBase64.indexOf(uuidStr.charAt(i));
    		mostSigBits |= (long)val<<((mask-i) << 2);
    	}
    	for(int i=mask;i>=0;i--) {
    		int val = IdCodeConst.idBase64.indexOf(uuidStr.charAt(i+16));
    		leastSigBits |= (long)val<<((mask-i) << 2);
    	}
    	return new UUID(mostSigBits, leastSigBits);
    }
    
    
    /**
	 * 自编64进制uuid转16进制uuid
	 * @param idBase64 自编64进制uuid
	 * @return
	 */
	public static String idbase64ToHex(String idBase64) {
		int mask = 15;//面具
		int hexI = 32;//原16进制uuid，长32
		char[] buf = new char[hexI];
		int i = 22;//自编64进制uuid，长22
		do {
			int val1 = IdCodeConst.idBase64.indexOf(idBase64.charAt(--i));
			int val2 = IdCodeConst.idBase64.indexOf(idBase64.charAt(--i));
			int val = val2 << 6 | val1;
			if (hexI==32) {
				val >>= 4;
			} else {
				buf[--hexI] = IdCodeConst.idBase64.charAt(val & mask);
				val >>= 4;
			}
			buf[--hexI] = IdCodeConst.idBase64.charAt(val & mask);
			val >>= 4;
			buf[--hexI] = IdCodeConst.idBase64.charAt(val & mask);
		} while(i>0);
		return new String(buf);
	}
	
	/**
	 * 16进制uuid转自编64进制uuid
	 * @param uuid32 16进制32位uuid
	 * @return
	 */
	public static String hexToIdbase64(String uuid32) {
		int mask = 63;//面具
		int idbase64I = 22;
		char[] buf = new char[idbase64I];
		int i = 32;
		do {
			int val1;
			if (i!=32) {
				val1 = IdCodeConst.idBase64.indexOf(uuid32.charAt(--i));
			} else {
				val1 = 0;
			}
			int val2 = IdCodeConst.idBase64.indexOf(uuid32.charAt(--i));
			int val3 = IdCodeConst.idBase64.indexOf(uuid32.charAt(--i));
			int val = val3 << 8 | val2 << 4 | val1;
			
			buf[--idbase64I] = IdCodeConst.idBase64.charAt(val & mask);
			val >>= 6;
			buf[--idbase64I] = IdCodeConst.idBase64.charAt(val & mask);
		} while (i>0);
		return new String(buf);
	}


}
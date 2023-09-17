package com.github.chenyuxin.commonframework.test.cipher;
  
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.crypto.Cipher;  
import javax.crypto.spec.IvParameterSpec;  
import javax.crypto.spec.SecretKeySpec;

import org.junit.jupiter.api.Test;

import com.github.chenyuxin.commonframework.base.converter.BASE64Util;

  
/** 
 * AESVi 是一种可逆加密算法，对用户的敏感信息加密处理 对原始数据进行AESVi加密后，在进行Base64编码转化； 
 */  
public class AESVi {  
    /* 
     * 加密用的Key 可以用26个字母和数字组成 此处使用AESVi-128-CBC加密模式，key需要为16位。 
     * a0b891c2d563e4f7  
     */  
    private static String ivParameter = "0T234!@^%5Wj89ab";//Wrong IV length: must be 16 bytes long
  
    /**
	 * 采用AESVi加密的，使用 passKey+接入系统中文名称 拼接字符串生成key
	 */
    public static String getKey(String s){
    	return MD5.EncoderByMd5(s).substring(3, 19);
    }
    
    /**
	 * AESVi变化key，需要保持系统日期时间准确。
	 * 每天(24点)后变化key，加点盐。
	 * @param s 使用接口的passKey字符串
	 * @return
	 */
	public static String getKey1(String s){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyddMM");
		Date date =new Date();
		String keyString = s.concat(sdf.format(date)).concat("Hh%6({$de");
		return MD5.EncoderByMd5(keyString).substring(6, 22);
	}
	
	/**
	 * 解密出错时尝试使用前一天的变化key
	 * AESVi变化key，需要保持系统日期时间准确。
	 * 每天(24点)后变化key，加点盐。
	 * @param s 使用接口的passKey字符串
	 * @return
	 */
	public static String getKey1yes(String s){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyddMM");
		Date date =new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_MONTH, -1);//昨天
		date = calendar.getTime();
		String keyString = s.concat(sdf.format(date)).concat("Hh%6({$de");
		return MD5.EncoderByMd5(keyString).substring(6, 22);
	}
    
    // 加密  
    public static String encrypt(String sSrc, String sKey){  
        String result = "";  
        try {  
            Cipher cipher;  
            cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");  
            byte[] raw = sKey.getBytes("utf-8");  
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");  
            IvParameterSpec iv = new IvParameterSpec(ivParameter.getBytes("utf-8"));// 使用CBC模式，需要一个向量iv，可增加加密算法的强度  
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);  
            byte[] encrypted = cipher.doFinal(sSrc.getBytes("utf-8")); 
            result = BASE64Util.encryptBASE64(encrypted);  // 此处使用BASE64做转码。  
        } catch (Exception e) {  
            e.printStackTrace();  
        }   
        return result;  
                  
    }  
  
    // 解密  
    public static String decrypt(String sSrc,String sKey){  
        try {  
            byte[] raw = sKey.getBytes("utf-8");  
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");  
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");  
            IvParameterSpec iv = new IvParameterSpec(ivParameter.getBytes("utf-8"));  
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);  
            byte[] encrypted1 = BASE64Util.decryptBASE64B(sSrc);// 先用base64解密  
            byte[] original = cipher.doFinal(encrypted1);  
            String originalString = new String(original, "utf-8");  
            return originalString;  
        } catch (Exception ex) {  
            ex.printStackTrace();  
            return "";
        }  
    }  
  
    @Test
	public void test(){  
	    // 需要加密的字串  
	    String cSrc = "就是你123";  
	    System.out.println(cSrc + "  长度为" + cSrc.length());  
	    
	    System.out.println("测试key:" + getKey("a"));
	    System.out.println("MD5 key:" + MD5.EncoderByMd5("a"));
	    // 加密  
	    long lStart = System.currentTimeMillis();
	    String enString = AESVi.encrypt(cSrc, getKey("a"));//IlYLSbnmO8HQkVThUdzXoA== 
	    System.out.println("加密后的字串是：" + enString + "长度为" + enString.length());  
	      
	    long lUseTime = System.currentTimeMillis() - lStart;  
	    System.out.println("加密耗时：" + lUseTime + "毫秒");  
	    // 解密  
	    lStart = System.currentTimeMillis();  
	    String DeString = AESVi.decrypt(enString, getKey("a"));  
	    System.out.println("解密后的字串是：" + DeString);  
	    lUseTime = System.currentTimeMillis() - lStart;  
	    System.out.println("解密耗时：" + lUseTime + "毫秒");  
    }  
    
    
    
    
}  
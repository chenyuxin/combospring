package com.github.chenyuxin.commonframework.test.cipher;
  
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.crypto.Cipher;  
import javax.crypto.spec.SecretKeySpec;

import org.junit.jupiter.api.Test;

import com.github.chenyuxin.commonframework.base.converter.BASE64Util;


/** 
 * AES 是一种可逆加密算法，对用户的敏感信息加密处理 对原始数据进行AES加密后，在进行Base64编码转化； 
 */  
public class AES {
	
	/**
	 * 根据自定义字符串生成加密key
	 * 接入数据采用AES加密的，使用接口的username+passKey拼接字符串生成key
	 * @param s 使用接口的username+passKey拼接字符串
	 * @return
	 */
	public static String getKey(String s){
		return MD5.EncoderByMd5(s).substring(2, 18);
	}
	
	/**
	 * AES变化key，需要保持系统日期时间准确。
	 * 每天(24点)后变化key，加点盐。
	 * @param s 使用接口的passKey字符串
	 * @return
	 */
	public static String getKey1(String s){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Date date =new Date();
		String keyString = s.concat(sdf.format(date)).concat("Hh%5({$de");
		return MD5.EncoderByMd5(keyString).substring(4, 20);
	}
	
	/**
	 * 解密出错时尝试使用前一天的变化key
	 * AES变化key，需要保持系统日期时间准确。
	 * 每天(24点)后变化key，加点盐。
	 * @param s 使用接口的passKey字符串
	 * @return
	 */
	public static String getKey1yes(String s){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Date date =new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_MONTH, -1);//昨天
		date = calendar.getTime();
		String keyString = s.concat(sdf.format(date)).concat("Hh%5({$de");
		return MD5.EncoderByMd5(keyString).substring(4, 20);
	}
	
    // 加密  
    public static String encrypt(String sSrc,String sKey){  
        String result = "";  
        try {  
            Cipher cipher = Cipher.getInstance("AES");  
            byte[] raw = sKey.getBytes("utf-8");  
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");  
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);  
            byte[] encrypted = cipher.doFinal(sSrc.getBytes("utf-8")); 
            result = BASE64Util.encryptBASE64(encrypted);  
        } catch (Exception e) {
            e.printStackTrace();  
        }   
        // 此处使用BASE64做转码。  
        return result;  
                  
    }  
  
    // 解密  
    public static String decrypt(String sSrc,String sKey){  
        try {  
            byte[] raw = sKey.getBytes("utf-8");  
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");  
            Cipher cipher = Cipher.getInstance("AES");  
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);  
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
        /* 
         * 加密用的Key 可以用26个字母和数字组成 此处使用AES-128加密模式，key需要为16位。 
         */  
        String testKey = getKey("中级工@单点A3");
        System.out.println("Key: "+testKey + ",lenth: "+ testKey.length());
	    // 需要加密的字串  
	    String cSrc = "就是你123";//Ez8UiBPqzTB6kCnOXaBRwg==
	    System.out.println(cSrc + "  长度为" + cSrc.length());  
	    // 加密  
	    long lStart = System.currentTimeMillis();  
	    String enString = AES.encrypt(cSrc,testKey);  
	    System.out.println("加密后的字串是：" + enString + "长度为" + enString.length());  
	      
	    long lUseTime = System.currentTimeMillis() - lStart;  
	    System.out.println("加密耗时：" + lUseTime + "毫秒");  
	    // 解密  
	    lStart = System.currentTimeMillis();  
	    String DeString = AES.decrypt(enString,testKey);  
	    System.out.println("解密后的字串是：" + DeString);  
	    lUseTime = System.currentTimeMillis() - lStart;  
	    System.out.println("解密耗时：" + lUseTime + "毫秒");  

	}  

}  
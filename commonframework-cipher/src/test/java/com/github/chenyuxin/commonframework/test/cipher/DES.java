package com.github.chenyuxin.commonframework.test.cipher;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;

import org.junit.jupiter.api.Test;

import com.github.chenyuxin.commonframework.base.converter.BASE64Util;


public class DES {
	/**
	 * 采用DES加密的，使用 passKey+username 拼接字符串直接为key
	 * 向量使用 DES.getsIv()
	 */
	private static String sKey = "012345678chenyux1qaz2wsEfgdf4235hx1wdgcg2dg#@13Cd";
	private static String sIv = "%Td(8@3b";//Wrong IV length: must be 8 bytes long
	public static String getsKey() {
		return sKey;
	}
	public static String getsIv() {
		return sIv;
	}
	
	
	/**
	 * DES变化key，需要保持系统日期时间准确。
	 * 每天(24点)后变化key，加点盐。
	 * @param s 使用接口的passKey字符串
	 * @return
	 */
	public static String getKey1(String s){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyddMM");
		Date date =new Date();
		String keyString = s.concat(sdf.format(date)).concat("Ha%6({$de");
		return MD5.EncoderByMd5(keyString);
	}
	
	/**
	 * 解密出错时尝试使用前一天的变化key
	 * DES变化key，需要保持系统日期时间准确。
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
		String keyString = s.concat(sdf.format(date)).concat("Ha%6({$de");
		return MD5.EncoderByMd5(keyString);
	}
	
	public static String encrypt(String str, String sKey,String sIv){        
		String result = "";
		byte[] bKey;
	    byte[] bIv;
		try {
			bKey = sKey.getBytes("utf-8");
			bIv = sIv.getBytes("utf-8");
			DESedeKeySpec keySpec=new DESedeKeySpec(bKey);  
			SecretKeyFactory keyFactory=SecretKeyFactory.getInstance("desede");              
			SecretKey key=keyFactory.generateSecret(keySpec);         
			Cipher cipher=Cipher.getInstance("desede/CBC/PKCS5Padding");  
			cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(bIv));  
			byte[] encrypted = cipher.doFinal(str.getBytes("utf-8"));  
			result = BASE64Util.encryptBASE64(encrypted);
       } catch (Exception e) {  
           e.printStackTrace();
       }  
       return result;  
	} 
	 
	public static String decrypt(String src, String sKey ,String sIv) {
       byte[] key;
       byte[] bIv;
       String result = "";
		try {
			key = sKey.getBytes("utf-8");
			bIv = sIv.getBytes("utf-8");
			IvParameterSpec iv = new IvParameterSpec(bIv);
			DESedeKeySpec desKey = new DESedeKeySpec(key);
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("desede");
			SecretKey securekey = keyFactory.generateSecret(desKey);
			Cipher cipher = Cipher.getInstance("desede/CBC/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, securekey, iv);
			byte[] encrypted1 = BASE64Util.decryptBASE64B(src);
			result = new String(cipher.doFinal(encrypted1),"utf-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	@Test
	public void test() {
		/*
		String a = DES.encrypt( "1231中国","kdsjflkasjdfklja",getsIv());
		System.out.println(a);
				System.out.println("解密："+DES.decrypt(a
				,"a03b6fe1ajikongshujuchuanshu"//getsKey()
				,getsIv()));
		 */
		String strEncode = """
			select * 
			from dual
			where 1=1 
		""";
		System.out.println("解密："+DES.decrypt(strEncode
				,"kdsjflkasjdfklja"//getsKey()
				,getsIv()));
	}
		
		

}


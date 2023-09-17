package com.github.chenyuxin.commonframework.test.cipher;

import java.lang.reflect.Field;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;


public class ThreeDES {
	
	private static final byte[] iv = new byte[]{1, 2, 3, 4, 5, 6, 7, 8};
	private static final String Algorithm = "desede";
	private static final byte[] keyBytes = new byte[]{17, 34, 79, 88, -120, 16, 64, 56, 40, 37, 121, 81, -53, -35, 85,
			102, 119, 41, 116, -104, 48, 64, 54, -30};
	public static byte[] getKey(){
		return keyBytes;
	}

	public static byte[] encode(byte[] keybyte, byte[] src) {
		try {
			SecretKeySpec e3 = new SecretKeySpec(keybyte, Algorithm);
			Cipher c1 = Cipher.getInstance("desede/CBC/PKCS5Padding");
			IvParameterSpec ips = new IvParameterSpec(iv);
			c1.init(1, e3, ips);
			return c1.doFinal(src);
		} catch (NoSuchAlgorithmException arg4) {
			arg4.printStackTrace();
		} catch (NoSuchPaddingException arg5) {
			arg5.printStackTrace();
		} catch (Exception arg6) {
			arg6.printStackTrace();
		}

		return new byte[0];
	}
	
	public static byte[] decrypt(byte[] keybyte, byte[] src) {
		try {
			IvParameterSpec ips = new IvParameterSpec(iv);
			SecretKeySpec e3 = new SecretKeySpec(keybyte, Algorithm);
			Cipher c1 = Cipher.getInstance("desede/CBC/PKCS5Padding");
			c1.init(2, e3, ips);
			return c1.doFinal(src);
		} catch (Exception e) {
			return new byte[0];
		}
	}

	
	public static String bytes2HexString(byte[] b) {
		StringBuffer stringBuffer = new StringBuffer("");   
	    if (b == null || b.length <= 0) {   
	        return "";   
	    } 
		for (int i = 0; i < b.length; i++) {
			String hex = Integer.toHexString(b[ i ] & 0xFF);
			if (hex.length() == 1) {
				stringBuffer.append(0);
			}
			stringBuffer.append(hex.toUpperCase());
		}
		return stringBuffer.toString();
	}
	
	public static byte[] hexStringToBytes(String hexString) {   
	    if (hexString == null || hexString.equals("")) {   
	        return new byte[0];  
	    }   
	    hexString = hexString.toUpperCase();   
	    int length = hexString.length() / 2;   
	    char[] hexChars = hexString.toCharArray();   
	    byte[] d = new byte[length];   
	    for (int i = 0; i < length; i++) {
	        int pos = i * 2;   
	        d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));   
	    }   
	    return d;   
	}
	
	private static byte charToByte(char c) {   
	    return (byte) "0123456789ABCDEF".indexOf(c);   
	}
	
	/**
	 * 封装好的解密密方法进行解密密
	 * @param enString 待加解密字符串
	 * @return
	 */
	public static String decrypt(String enString){
		return new String(decrypt(keyBytes, hexStringToBytes(enString)));
	}
	
	/**
	 * 封装好的加密方法进行加密
	 * @param cSrc 待加密字符串
	 * @return
	 */
	public static String encode(String cSrc){
		return ThreeDES.bytes2HexString(encode(keyBytes, cSrc.getBytes()));
	}
	
	/**
	 * 解密对象里面需要解密的 加密字段
	 * @param obj 包含加密字段的对象
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public static void decryptAllField(Object obj) throws IllegalArgumentException, IllegalAccessException{
		Field[] fields = obj.getClass().getDeclaredFields();
		for (Field field : fields) {
			field.setAccessible(true);
			if ( field.getType() == String.class ){//只对字符串类型字段进行操作
				String value = (String) field.get(obj);
				if (StringUtils.isNotBlank(value)){//只对有值的字段进行操作
					value = decrypt(value);
					if (StringUtils.isNotBlank(value)){//解密成功的，替换原加密字段。
						field.set(obj, value);
					}
				}
			}
		}
	}
	
	@Test
	public void test() {
		// 需要加密的字串  
	    String cSrc = "就是你@123";  
	    System.out.println(cSrc + "  长度为" + cSrc.length());  
	    // 加密  
	    long lStart = System.currentTimeMillis();  
	    String enString = ThreeDES.bytes2HexString(ThreeDES.encode(ThreeDES.getKey(), cSrc.getBytes()));
	    System.out.println("加密后的字串是：" + enString + "长度为" + enString.length());  
	      
	    long lUseTime = System.currentTimeMillis() - lStart;  
	    System.out.println("加密耗时：" + lUseTime + "毫秒");  
	    // 解密  
	    lStart = System.currentTimeMillis();  
	    //enString = "79FF260EF3667BA1994F4B41BB126EE6C6587F078A7C2F015D945D10DF05011D81DD555419D6B717C910E9D71DB0BCB33CE737543B0D0479DE82F97A092F38A06C02D7AEF5871DFAD788ECF6B8A02079";
	    String DeString = new String(ThreeDES.decrypt(ThreeDES.getKey(), ThreeDES.hexStringToBytes(enString)));
	    System.out.println("解密后的字串是：" + DeString);  
	    lUseTime = System.currentTimeMillis() - lStart;  
	    System.out.println("解密耗时：" + lUseTime + "毫秒");  
		
	}


	
}

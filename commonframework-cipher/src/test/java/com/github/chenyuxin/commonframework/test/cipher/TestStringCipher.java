package com.github.chenyuxin.commonframework.test.cipher;

import org.junit.jupiter.api.Test;

import com.github.chenyuxin.commonframework.base.constant.IdCodeConst;
import com.github.chenyuxin.commonframework.base.converter.ByteConverter;
import com.github.chenyuxin.commonframework.base.util.CharsetUtil;
import com.github.chenyuxin.commonframework.cipher.Cipher;


/**
 * 字符串级加密解密
 */
public class TestStringCipher {
	
	byte[] key 		= {17, 34, 79, 88, -120, 16, 64, 56, 40, 37, 121, 81, -53, -35, 85, 102};
	byte[] desKey 	= {17, 34, 79, 88, -120, 16, 64, 56, 40, 37, 121, 81, -53, -35, 85, 102, 119, 41, 116, -104, 48, 64, 54, -30};
	byte[] iv = {0,1,2,3,4,5,6,7,8,-1,-2,-3,-4,-5,-6,-7};
	byte[] desiv = {0,2,3,7,-2,-4,-6,-7};
	String oString = "就是中国@Ab123";
	
	@Test
	public void test1() throws Exception {
		
		byte[] enBytes = Cipher.AES.encrypt(oString.getBytes(CharsetUtil.UTF_8),key);
		System.out.println("BASE64密文：" + ByteConverter.BASE64.parse(enBytes) );
		System.out.println("16进制密文：" + ByteConverter.HEX.parse(enBytes) );
		
		String enString1 = Cipher.AES.encrypt(oString, key);
		System.out.println(enString1);
		String enString2 = Cipher.AES.encrypt(oString,key,ByteConverter.Hex);
		System.out.println(enString2);
		String enString3 = Cipher.AES.encrypt(oString,key,ByteConverter.HEX,CharsetUtil.CHARSET_ISO_8859_1);
		System.out.println(enString3);
		
		String deString2 = Cipher.AES.decrypt(enString2,key,ByteConverter.Hex);
		System.out.println("解密2:" + deString2);
		String deString3 = Cipher.AES.decrypt(enString3,key,ByteConverter.HEX,CharsetUtil.CHARSET_ISO_8859_1);
		System.out.println("解密3:" + deString3);
	}
	
	@Test
	public void test2() throws Exception {
		byte[] enBytes = Cipher.AESIv.encrypt(oString.getBytes(CharsetUtil.CHARSET_UTF_8),key,iv);
		System.out.println("BASE64密文：" + ByteConverter.BASE64.parse(enBytes) );
		System.out.println("16进制密文：" + ByteConverter.HEX.parse(enBytes) );
		
		String enString1 = Cipher.AESIv.encrypt(oString, key,iv);
		System.out.println(enString1);
		String enString2 = Cipher.AESIv.encrypt(oString,key,ByteConverter.Hex,iv);
		System.out.println(enString2);
		String enString3 = Cipher.AESIv.encrypt(oString,key,ByteConverter.HEX,iv,CharsetUtil.CHARSET_ISO_8859_1);
		System.out.println(enString3);
		
		String deString2 = Cipher.AESIv.decrypt(enString2,key,ByteConverter.Hex,iv,Cipher.isPrivateKey);
		System.out.println("解密2:" + deString2);
		String deString3 = Cipher.AESIv.decrypt(enString3,key,ByteConverter.HEX,iv,CharsetUtil.CHARSET_ISO_8859_1);
		System.out.println("解密3:" + deString3);
		
	}
	
	@Test
	public void testDes() throws Exception {
		byte[] enBytes = Cipher.DES.encrypt(oString.getBytes(CharsetUtil.UTF_8),desKey,desiv);
		System.out.println("BASE64密文：" + ByteConverter.BASE64.parse(enBytes));
		System.out.println("16进制密文：" + ByteConverter.HEX.parse(enBytes));
		
		String enString1 = Cipher.DES.encrypt(oString, desKey,desiv,Cipher.isPrivateKey);
		System.out.println(enString1);
		String enString2 = Cipher.DES.encrypt(oString,desKey,ByteConverter.Hex,desiv);
		System.out.println(enString2);
		String enString3 = Cipher.DES.encrypt(oString,ByteConverter.Hex.parse(desKey),desiv,ByteConverter.Hex,CharsetUtil.CHARSET_GBK);
		System.out.println(enString3);
		String enString4 = Cipher.DES.encrypt(oString,desKey,ByteConverter.HEX,desiv);
		System.out.println(enString4);
		
		String deString2 = Cipher.DES.decrypt(enString2,desKey,ByteConverter.Hex,desiv,Cipher.isPublicKey);
		System.out.println("解密2:" + deString2);
		String deString3 = Cipher.DES.decrypt(enString3,desKey,ByteConverter.HEX,desiv,CharsetUtil.CHARSET_GBK);
		System.out.println("解密3:" + deString3);
	}
	
//	@Test
//	public void testConstomSM4() throws Exception {
//		byte[] enBytes = Cipher.ConstomSM4.encrypt(oString.getBytes(CharsetUtil.CHARSET_UTF_8),key);
//		System.out.println("BASE64密文：" + ByteConverter.BASE64.parse(enBytes) );
//		System.out.println("16进制密文：" + ByteConverter.HEX.parse(enBytes) );
//		
//		String enString = Cipher.ConstomSM4.encrypt(oString, key);
//		System.out.println(enString);
//		enString = Cipher.ConstomSM4.encrypt(oString,key,ByteConverter.Hex);
//		System.out.println(enString);
//		enString = Cipher.ConstomSM4.encrypt(oString,ByteConverter.Hex.parse(key),ByteConverter.Hex);
//		System.out.println(enString);
//		enString = Cipher.ConstomSM4.encrypt(oString,key,ByteConverter.HEX,CharsetUtil.CHARSET_ISO_8859_1);
//		System.out.println(enString);
//	}
	
	@Test
	public void testRSA() throws Exception {
		byte[] publicKey =  ByteConverter.HEX.escape(TestAsymmetricKey.publicHexKey);
		byte[] privateKey =  ByteConverter.HEX.escape(TestAsymmetricKey.privateHexKey);
		byte[] enBytes = Cipher.RSA.encrypt(oString.getBytes(CharsetUtil.CHARSET_UTF_8),publicKey);
		System.out.println("BASE64密文：" + ByteConverter.BASE64.parse(enBytes) );
		System.out.println("16进制密文：" + ByteConverter.HEX.parse(enBytes) );
		
		String enString1 = Cipher.RSA.encrypt(oString, publicKey);
		System.out.println(enString1);
		String enString2 = Cipher.RSA.encrypt(oString,publicKey,ByteConverter.Hex);
		System.out.println(enString2);
		String enString3 = Cipher.RSA.encrypt(oString,TestAsymmetricKey.publicHexKey,ByteConverter.Hex);
		System.out.println(enString3);
		String enString4 = Cipher.RSA.encrypt(oString,privateKey,ByteConverter.HEX,Cipher.isPrivateKey);
		System.out.println(enString4);
		String enString5 = Cipher.RSA.encrypt(oString,TestAsymmetricKey.privateHexKey,Cipher.isPrivateKey,ByteConverter.Hex);
		System.out.println(enString5);
		
		
		String deCode1 = Cipher.RSA.decrypt(enString1,privateKey);
		System.out.println("解密1：" + deCode1);
		String deCode2 = Cipher.RSA.decrypt(enString2,TestAsymmetricKey.privateHexKey,ByteConverter.Hex);
		System.out.println("解密2：" + deCode2);
		String deCode3 = Cipher.RSA.decrypt(enString3,privateKey,ByteConverter.Hex,Cipher.isPrivateKey);
		System.out.println("解密3：" + deCode3);
		String deCode4 = Cipher.RSA.decrypt(enString4,TestAsymmetricKey.publicHexKey,ByteConverter.Hex,Cipher.isPublicKey);
		System.out.println("解密4：" + deCode4);
		String deCode5 = Cipher.RSA.decrypt(enString5,TestAsymmetricKey.publicHexKey,ByteConverter.Hex,Cipher.isPublicKey);
		System.out.println("解密5：" + deCode5);
		
	}
	
	@Test
	public void testDSA() throws Exception {
		String sign = Cipher.DSA.encrypt(oString,TestAsymmetricKey.DSAPrivateHexKey,ByteConverter.HEX);
		System.out.println(sign);
		String retult = Cipher.DSA.decrypt(oString, TestAsymmetricKey.DSAPublicHexKey, sign, ByteConverter.HEX);
		if (null == retult) {
	    	System.out.println("retult:内容有篡改");
	    } else {
	    	System.out.println("retult:内容比对一致");
	    }
		
		
		String sign1 = Cipher.DSA.encrypt(oString,ByteConverter.HEX.escape(TestAsymmetricKey.DSAPrivateHexKey));
		System.out.println(sign1);
		
		byte[] key = ByteConverter.HEX.escape(TestAsymmetricKey.DSAPublicHexKey);
		byte[] sign1Byte = ByteConverter.BASE64.escape(sign1);
		String retult1 = Cipher.DSA.decrypt(oString,key, sign1Byte);
		System.out.println(retult1);
		if (null == retult1) {
	    	System.out.println("retult1:内容有篡改");
	    } else {
	    	System.out.println("retult1:内容比对一致");
	    }
		String retult2 = Cipher.DSA.decrypt(oString,key, sign1);
		if (null == retult2) {
	    	System.out.println("retult2:内容有篡改");
	    } else {
	    	System.out.println("retult2:内容比对一致");
	    }
	}
	
	@Test
	public void testMD5() throws Exception {
		String r1 = Cipher.MD5.encrypt(oString,ByteConverter.HEX);
		System.out.println(r1);
		String r2 = Cipher.MD5.encrypt(oString);
		System.out.println(r2);
		String r3 = Cipher.MD5.encrypt(oString,ByteConverter.BASE64);
		System.out.println(r3);
		
		System.out.println(hexToIdbase64(r1.toLowerCase()));
		System.out.println(hexToIdbase64(r2));
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

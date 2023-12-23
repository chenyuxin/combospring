package com.github.chenyuxin.commonframework.test.cipher;

import org.junit.jupiter.api.Test;

import com.github.chenyuxin.commonframework.base.constant.StringPool;
import com.github.chenyuxin.commonframework.base.converter.BASE64Util;
import com.github.chenyuxin.commonframework.base.converter.ByteConverter;
import com.github.chenyuxin.commonframework.base.converter.HexUtil;
import com.github.chenyuxin.commonframework.base.util.CharsetUtil;
import com.github.chenyuxin.commonframework.cipher.Cipher;


/**
 * 测试加密
 */
public class TestCipher {
	
	@Test
	public void testMD5() {
		String str = "A就是中国@Ab123";
		
		long lStart1 = System.currentTimeMillis();
		System.out.println(MD5.EncoderByMd5(str));
		long lUseTime1 = System.currentTimeMillis() - lStart1;  
	    System.out.println("加密耗时：" + lUseTime1 + "毫秒"); 
	    
	    try {
			long lStart = System.currentTimeMillis();
			//byte[] md = Cipher.MD5.encrypt(str.getBytes(CharsetUtil.CHARSET_UTF_8));
			//byte[] md = Cipher.MD5.encrypt(str.getBytes());
			byte[] md = Cipher.MD5.encrypt(str.getBytes());
			//byte[] md = Cipher.SHA_256.encrypt(str.getBytes());
	        System.out.println(HexUtil.toHexString(md,false));
	        System.out.println(HexUtil.toHexString(md));
	        long lUseTime = System.currentTimeMillis() - lStart;  
		    System.out.println("加密耗时：" + lUseTime + "毫秒");  
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void testAES() {
		String str = "A就是中国@Ab123";
		String key = "1abb6bde829622bc32de845d507238a2";
		long lStart1 = System.currentTimeMillis();
		System.out.println(AES.encrypt(str, key));
		long lUseTime1 = System.currentTimeMillis() - lStart1;  
	    System.out.println("加密耗时：" + lUseTime1 + "毫秒"); 
	    
	    try {
			long lStart = System.currentTimeMillis();
			byte[] md = Cipher.AES.encrypt(str.getBytes(),key.getBytes());
			String cipher = BASE64Util.encryptBASE64(md);
			System.out.println(cipher);
	        long lUseTime = System.currentTimeMillis() - lStart;  
		    System.out.println("加密耗时：" + lUseTime + "毫秒");  
		    
		    byte[] oStr = Cipher.AES.decrypt(md,key.getBytes());
			System.out.println(new String(oStr));
		    
		    
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testAESIv() {
		String str = "A就是中国@Ab123";
		String key = "1abb6bde829622bc32de845d507238a2";
		String ivParameter = "0T234!@^%5Wj89ab";//Wrong IV length: must be 16 bytes long
		long lStart1 = System.currentTimeMillis();
		System.out.println(AESVi.encrypt(str, key));
		long lUseTime1 = System.currentTimeMillis() - lStart1;  
	    System.out.println("加密耗时：" + lUseTime1 + "毫秒"); 
	    
	    try {
			long lStart = System.currentTimeMillis();
			byte[] md = Cipher.AESIv.encrypt(str.getBytes(),key.getBytes(),ivParameter.getBytes());
			String cipher = BASE64Util.encryptBASE64(md);
			System.out.println(cipher);
	        long lUseTime = System.currentTimeMillis() - lStart;  
		    System.out.println("加密耗时：" + lUseTime + "毫秒");  
		    
		    byte[] oStr = Cipher.AESIv.decrypt(md,key.getBytes(),ivParameter.getBytes());
			System.out.println(new String(oStr));
		    
		    
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	@Test
	public void TestBase64() {
		String str = "就是中国@Ab123";
		str = BASE64Util.encryptBASE64(str);
		System.out.println(str);
	}
	
	@Test
	public void TestBase64d() {
		String string = "5bCx5piv5Lit5Zu9QEFiMTIz";
		string = BASE64Util.decryptBASE64(string);
		System.out.println(string);
	}
	

	@Test
	public void TestThreeDES(){
		
		// 需要加密的字串  
	    String cSrc = "[{\"XSE_YZ_Z\":\"testString1\",\"xgbz\":0,\"updateTime\":\"2019-09-01 13:22:01\",\"XSE_XM\":\"testString1\",\"XSE_XBDM\":\"testString1\",\"XSE_CSSJ\":\"2019-12-04 02:04:05\",\"XSE_YZ\":\"testString1\",\"XSE_TZ\":\"testString1\",\"XSE_SC\":\"testString1\",\"CSDD_S\":\"testString1\",\"ZCJGMC\":\"testString1\",\"MQ_XM\":\"testString1\",\"MQ_NL\":\"testString1\",\"MQ_GJ\":\"testString1\",\"MQ_MZ\":\"testString1\",\"MQ_ZZ\":\"testString1\",\"MQ_SFZJLX\":\"testString1\",\"MQ_SFZJHM\":\"testString1\",\"FQ_XM\":\"testString1\",\"QFJG_MC\":\"testString1\",\"QF_RQ\":\"2019-12-04 02:04:05\",\"CSYXZMBH\":\"testString1\",\"FQ_NL\":\"testString1\",\"FQ_GJ\":\"testString1\",\"FQ_MZ\":\"testString1\",\"FQ_ZZ\":\"testString1\",\"FQ_SFZJLX\":\"testString1\",\"FQ_SFZHM\":\"testString1\",\"CSYXZMQFID\":\"testString1\",\"ETGRBSH\":\"testString1\",\"BSHLB\":\"testString1\",\"XSE_BM\":\"testString1\",\"CSDD_SD\":\"testString1\",\"CSDD_XQ\":\"testString1\",\"CSD_QHDM\":\"testString1\",\"ZCJGDM\":\"testString1\",\"JSRY\":\"testString1\",\"MQ_BAH\":\"testString1\",\"MQ_CSRQ\":\"testString1\",\"MQ_HJ\":\"testString1\",\"MQ_HJSJDM\":\"testString1\",\"FQ_CSRQ\":\"testString1\",\"FQ_HJ\":\"testString1\",\"FQ_HJSJDM\":\"testString1\",\"LZR_XM\":\"testString1\",\"LZR_YXSEGX\":\"testString1\",\"LZR_SFZJLB\":\"testString1\",\"LZR_SFZJHM\":\"testString1\",\"QFJG_QHDM\":\"testString1\",\"QFJG_ZZJGDM\":\"testString1\",\"QFJG_LBDM\":\"testString1\",\"QF_RY\":\"testString1\",\"QF_LX\":\"testString1\",\"YZ_BH\":\"testString1\",\"YZ_JHQK\":\"testString1\",\"HFYY\":\"testString1\",\"BFYY\":\"testString1\",\"QF_BZ\":\"testString1\",\"FZR\":\"testString1\",\"TBR\":\"testString1\",\"LXDH\":\"testString1\",\"TBRQ\":\"testString1\",\"JLSJ_YWK\":\"2019-12-04 02:04:05\",\"BJSJ_YWK\":\"2019-12-04 02:04:05\",\"P_ORGTYPE\":\"testString1\",\"AREANAME\":\"testString1\",\"P_FLAG\":1}]";  
		System.out.println(cSrc + "  长度为" + cSrc.length());  
	    
	    String base64str = BASE64Util.encryptBASE64(cSrc, "utf-8");
	    System.out.println("BASE64:" + base64str);
	    
	    // 加密  
	    long lStart = System.currentTimeMillis();  
	    String enString = ThreeDES.bytes2HexString(ThreeDES.encode(ThreeDES.getKey(), cSrc.getBytes()));
	    System.out.println("加密后的字串是：" + enString + "长度为" + enString.length());  
	      
	    long lUseTime = System.currentTimeMillis() - lStart;  
	    System.out.println("加密耗时：" + lUseTime + "毫秒");  
	    // 解密  
	    lStart = System.currentTimeMillis();  
	    /**
		idCard=644cbab291fa37688e7320b01540246d23a26812b899cba7
		&medicalOrgId=5089c956d44684edc77eb1453c44c3a0
		&deptId=53ed97292039f9aff3df16ee08922683fb38aa0856bd71ae5ab58d11df35f425fd42ab8746cba412
		&jobNo=d2df3b57ef05396eee6e2205ef649ac1b1465263db83a5243a839298f77a360c1757879662cb49b2
		&passWord=6fc5e8d6144b9404
		&cardType=721ad0bd8af27029
		 */
	    enString = "53ed97292039f9aff3df16ee08922683fb38aa0856bd71ae5ab58d11df35f425fd42ab8746cba412";
	    String DeString = new String(ThreeDES.decrypt(ThreeDES.getKey(), ThreeDES.hexStringToBytes(enString)));
	    System.out.println("解密后的字串是：" + DeString);  
	    lUseTime = System.currentTimeMillis() - lStart;  
	    System.out.println("解密耗时：" + lUseTime + "毫秒");  
		
	}
	
	@Test
	public void TestDES(){
		String str = "就是中国@Ab123";
		String key = "012345678wonders1qaz2wsEfgdf4235hx1wdgcg2dg#@13Cd";
//		String key = "1abb6bde829622bc32de845d507238a2";
//		String iv = "%Td(8@3b";//Wrong IV length: must be 8 bytes long
//		String key = "Chris   ";
		String iv = "12345678";//Wrong IV length: must be 8 bytes long
		
		String enString = DES.encrypt(str, key ,  iv );
		System.out.println(enString);
		
		
		try {
			long lStart = System.currentTimeMillis();
			byte[] md = Cipher.DES.encrypt(str.getBytes(),key.getBytes(),iv.getBytes());
			String cipher = BASE64Util.encryptBASE64(md);
			System.out.println(cipher);
	        long lUseTime = System.currentTimeMillis() - lStart;  
		    System.out.println("加密耗时：" + lUseTime + "毫秒");  
		    
		    byte[] oStr = Cipher.DES.decrypt(md,key.getBytes(),iv.getBytes());
			System.out.println(new String(oStr));
		    
		    
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testThreeDes(){
		String string = "就是中国@Ab123";
		String enString = ThreeDES.encode(string);
		System.out.println(enString);
		
		byte[] iv = new byte[]{1, 2, 3, 4, 5, 6, 7, 8};
		byte[] key = new byte[]{17, 34, 79, 88, -120, 16, 64, 56, 40, 37, 121, 81, -53, -35, 85,
				102, 119, 41, 116, -104, 48, 64, 54, -30};
		System.out.println("Hexkey:" + HexUtil.toHexString(key));
		System.out.println("Base64key:" + BASE64Util.encryptBASE64(key));
		System.out.println("iv:" + HexUtil.toHexString(iv));
		System.out.println("Base64Ib:" + BASE64Util.encryptBASE64(iv));
		try {
			long lStart = System.currentTimeMillis();
			byte[] md = Cipher.ThreeDES.encrypt(string.getBytes(),key,iv);
			//String cipher = BASE64.encryptBASE64(md);
			String cipher = HexUtil.toHexString(md);
			System.out.println(cipher);
	        long lUseTime = System.currentTimeMillis() - lStart;  
		    System.out.println("加密耗时：" + lUseTime + "毫秒");  
		    byte[] oStr = Cipher.ThreeDES.decrypt(md,key,iv);
			System.out.println(new String(oStr,CharsetUtil.CHARSET_UTF_8));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		String enCode = "CE4940F4149B5365F4C2B6992B70E6B6147136A7660A04E6BC31AF40B22EE4D015C4D84889BC64E92187BCCF7371085EF9E44B2B9B690B56BDDCC9E80EDB729A6018F6EBF4DE05B0EC805EAF71F697EA";
		String oString = StringPool.BLANK;
		try {
			oString = new String(Cipher.ThreeDES.decrypt(HexUtil.hexStringToBytes(enCode), key,iv),CharsetUtil.CHARSET_UTF_8);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(oString);
	}
	
//	private static byte[] MySM4key = new byte[]{17, 34, 79, 88, -120, 16, 64, 56, 40, 37, 121, 81, -53, -35, 85, 102};
	
//	@Test
//	public void testSM4(){
//		String enString = "79FF260EF3667BA1994F4B41BB126EE6C6587F078A7C2F015D945D10DF05011D81DD555419D6B717C910E9D71DB0BCB33CE737543B0D0479DE82F97A092F38A06C02D7AEF5871DFAD788ECF6B8A02079";
//		//String enString ="79FF260EF3667BA1994F4B41BB126EE6160B870DF3C17C883631BF60D9588BE9";
//		//String enString ="C41D868B258804F75054A2A1E590A132F5D27565521B6B81A194385456E8CD25A2D6B41CC7980D40D324F299BC617DC123B86F837354607315C45D581C41E9DA142BD40C44B0B4EAE6BC21CDC0599732CD6CA8742C1665E21835F49A09B4849A30C7B67BA46DBB4B70D0B25EA5F43A571D8084F077616EF153FFBFD732085A15BB62C29B7F7E92075487FB92DD24920F5FE304A13A281D7C5D0FBEEF213F7939676C881A0B0993EEA4275F7996AA4DAEDBA72125FA0029380498600871A62A2AA57FAE0EC64F462888A77A7D4A9FE8F9181ACE5A55392F1EAE1E7A799C49582BD5AB69281DAD17ACCA374DD3BFBB816530EA738F5B8CBDCF7B24BE177841559C029B9DDA1EF36DE44ABB6F5D683B13E9FC2466890738C5CFE51D1AA072D88C221C7ED595C4C3156BEB5F43E14649645CD8F43E2DCF725506A77BC5231173FFFFF69B3C67DFF549DF765C254E1CF202D2EB4C8EF34288F5D924B49BC6B326410AAE7A40BDA8D3251D20E9EDAF31BD52F04EF31BFDC7B10B708ABF2D86D45908F2EEC5E8A4D83D2701245DC3BED7DFB8798525E01B3D22254E7749D7A35E2C74E2C5CAA0750E53C2D2879B41AD8BB759AC51E02147DB4CE5EC341969A5B6B98D7410134D467C5C8B09AB01D1C803BDB522BA7FFDB8B31E95137E66A051382B97137AFAB56D1CD0884854BC1F86E3856A1B123E2B858D83D1B2D371C82D94A1782137EC430DEC883093D6DB090B6A9D4F944C166EE6A56A87E8646F7C4EFE643C3135826D5ACD28B533B0D49BC1208B90267A6CAE6337966EDD34C3F142F5276E93F412A73E46D1CADA981863129AE79C39FFC0C65EC6B67948AF356D2075E88D6C6107857171E93C10D72D8CF3961CF2021CD0F649F8C6A4706BD1EA4CAE3FEB936A2659F429349BA226EE4DB326166E278B90594F9D537D56022836EF8A0FC910B2FAA7A2E4BD2A7BBAF93FD758FC93BBE75CD61CB8ECBD4FBFE58D051CBE023EB8D97DEEE5E86921738DE20D0DF4EAB7BB2D57180185782180DD1D86A8FE99ED1566DA5846B549D891E97782A210FFC2E61C4E9DDA678D463497BE006BCD65E31492B8BFCD7A9E675BFB5C9A7D4B2120162CF3924B4083F68A1ED4AE65BA6290";
//		//String enString = "79FF260EF3667BA1994F4B41BB126EE6E1091B42F21B36811A64ABB3D0EC6619CD3D193A2A9E612737B80E3E27B432BC763C7B210C7625C3FD180806C132B7165DA0EEDD4ECE2F9BE89D7EA8E6B5130208924EDB07005440158E0B3D48DC1FA2BFD09B26851608702DBE0B3529D76DDC0D6CE54D8AB7A2511D22E0419499AF814C4F018C29BBD2958727BA9E4FB78FC73AE972B37035F276F9D1AE3B3AB83AA977BADCEF4AC5963F07A6459DD9519D46D6F1F676267A1852453CB4FD3DDCD342B44EA53F607688638EECFB94DA5F9059F8034F9BBE941DAFF71511678F9CB3EEB07E2260DAC9BA5CDAEC458E22856AD2D28E39798C0A0F3FA0F8436F0647642410C15EF79BFAE933A4345DA60007A9B9BB7BBB15941727CE8A7B4F47F21B4B21AC942A0CA6D682E3F5336B29B23E2D3F2F2F3ACE8F17A7B9A1991C85542024DEC269F799312EA7B5E06B1D8BA739667889834564F9918B238CBA5A3B7E941690DADDEDC33FD3C07844A2E4BD48C93653D337FD299054E6C56300C06529E14F022D756EBD3F6784E3CD604BBE2FCCB0C4D4A39492FC939E2C9C242AFCA120C67B277088FA676FA33C6937161BA8E3C57A0FA92C46861FF3A11ED9CE61A58A5592869952A1D74E70F49196DFAFCCEF4052";
//		//String enString = "C41D868B258804F75054A2A1E590A13293B783EDB3AA44CA518247CAD500FACFE0C68F52B337420C0661B6F8031A6E66CC8211C9E4FE28BDED93219752934124B0CBE5454C213E94EC5F337F26876815002AA5D30ACEA652F049C9A9707602B90537083398EA7DEF7EAC12C3C12FDDE02C7173D0A6D2E6ACC7527AFB660B37AB3BC7036376D6E4AB6417B70098736DE0E03DC3DF81C4A436C9F4C6A992B89BA51E0F16FF72F540F123203B823555587B96AEF70C9D97B54427D5885C56229BC8C58CAE4C17614FC6E976DF18F226C829370310880AE5C3CA32F4DAE2FAE399BBEEB49F51C60804FD7557C0E72E36B569D44692B20010833A2CE86F2C5E06AD666513680E5B8490018AA447741BC76C1C3A41BCF0D228A366C55C62D3F5DB627395B069E6026F1C76717B5687201970057C7CD6ABD9C36AE1FB11471928AC3DC2351F3B22FB01B1D067B3FD8F0227A15DC7DF927E3FD34D6702F83D58A32F8FF3C677BCD847D281DF8CF030D5FCDFD3D9CC10114808F5EBDB449E42BE8A8A3957688D2288FE34C3DA15BFB66FDA9627EE799FE2ED795BC6193F8ECC7439A36DCA361DD0762C2DF8C5CE9A3BBC527EA11AF00F0EE9A675BB11870040749A81E9B5";
//		String decode = StringPool.BLANK;
//		try {
//			byte[] oStr = Cipher.ConstomSM4.decrypt(HexUtil.hexStringToBytes(enString),WDSM4key);
//			decode = new String(oStr,CharsetUtil.CHARSET_UTF_8);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		System.out.println(decode);
//	}
	
//	@Test
//	public void testSM4e(){
//		//String xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?><YL_ACTIVE_ROOT><CFDDM>3103</CFDDM><YLJGDM>1</YLJGDM><ZJHM>33050219511019001X</ZJHM><XM>王小二</XM><XB>男</XB><CSRQ>19511019</CSRQ><KH>J03658498</KH><KLX>0</KLX><JZLX>100</JZLX><YYKSBM>3.01</YYKSBM><YBKSBM>123456</YBKSBM><YYYSGH>1001</YYYSGH><YBYSGH>111111</YBYSGH><YSXM>张三</YSXM><AGENTIP>10.1.7.11</AGENTIP><AGENTMAC>ff-ff-ff-ff-ff</AGENTMAC><ZD><ITEM><ZDBM>B16.905</ZDBM><ZDMC>乙型病毒性肝炎</ZDMC><BMLX>01</BMLX></ITEM></ZD><GM><ITEM><GMLX>3</GMLX><GMBM>P</GMBM><GMMC>青霉素过敏</GMMC></ITEM></GM><YP><ITEM><BMLX>10</BMLX><YPMC>甲磺酸多沙唑嗪控释片</YPMC><YYYPDM>210000</YYYPDM><YBYPDM>X01518300010010</YBYPDM></ITEM></YP></YL_ACTIVE_ROOT>";
//		//String xml = "<YL_ACTIVE_ROOT><CFDDM>3103</CFDDM><KH>513031195403226806</KH><KLX>01</KLX><YLJGDM>45071756X</YLJGDM><JZLX>100</JZLX><YYKSBM>0303</YYKSBM><YBKSBM>0303</YBKSBM><YYYSGH>222222</YYYSGH><YBYSGH>222222</YBYSGH><YSXM>神内医生</YSXM><AGENTIP>10.1.7.11</AGENTIP><AGENTMAC>ff-ff-ff-ff-ff</AGENTMAC><FWXM><ITEM><FWLB>3</FWLB><BMLX>10</BMLX><YBXMDM>EABJT001</YBXMDM><JCBWBM></JCBWBM><JCBW>胸部正、侧位</JCBW></ITEM></FWXM></YL_ACTIVE_ROOT>";
//		//String xml = "<YL_ACTIVE_ROOT><CFDDM>3104</CFDDM><YLJGDM>451266334</YLJGDM><ZJHM>513031195011296709</ZJHM><XM>王小二</XM><XB>男</XB><CSRQ>19511019</CSRQ><KH>513031195011296709</KH><KLX>01</KLX><JZLX>100</JZLX><YYKSBM>3.01</YYKSBM><YBKSBM>123456</YBKSBM><YYYSGH>1001</YYYSGH><YBYSGH>111111</YBYSGH><YSXM>张三</YSXM><AGENTIP>10.1.7.11</AGENTIP><AGENTMAC>ff-ff-ff-ff-ff</AGENTMAC><ZD><ITEM><ZDBM>B16.905</ZDBM><ZDMC>乙型病毒性肝炎</ZDMC><BMLX>01</BMLX></ITEM></ZD><GM><ITEM><GMLX>3</GMLX><GMBM>P</GMBM><GMMC>青霉素过敏</GMMC></ITEM></GM><YP><ITEM><BMLX>10</BMLX><YPMC>就是一个药</YPMC><YYYPDM>210000</YYYPDM><YBYPDM>10008238</YBYPDM></ITEM></YP><FWXM><ITEM><FWLB>3</FWLB><BMLX>10</BMLX><YBXMDM>EABVT001</YBXMDM><JCBWBM>761</JCBWBM><JCBW>腰椎正侧位</JCBW></ITEM></FWXM></YL_ACTIVE_ROOT>";
//		String xml = "就是中国@Ab123";
//		
//		//byte[] iv = new byte[]{1, 2, 3, 4, 5, 6, 7, 8};
//		//byte[] key = new byte[]{17, 34, 79, 88, -120, 16, 64, 56, 40, 37, 121, 81, -53, -35, 85,102, 119, 41, 116, -104, 48, 64, 54, -30};
//		
//		try {
//			long lStart = System.currentTimeMillis();
//			byte[] md = Cipher.ConstomSM4.encrypt(xml.getBytes(),MySM4key);
//			//String cipher = BASE64.encryptBASE64(md);
//			String cipher = HexUtil.toHexString(md);
//			System.out.println(cipher);
//	        long lUseTime = System.currentTimeMillis() - lStart;  
//		    System.out.println("加密耗时：" + lUseTime + "毫秒");  
//		    byte[] oStr = Cipher.ConstomSM4.decrypt(md,MySM4key);
//			System.out.println(new String(oStr,CharsetUtil.CHARSET_UTF_8));
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		
//		
//		try {
//			long lStart = System.currentTimeMillis();
//			byte[] md = Cipher.ConstomSM4.encrypt(xml.getBytes(),MySM4key);
//			//String cipher = BASE64.encryptBASE64(md);
//			String cipher = HexUtil.toHexString(md);
//			System.out.println(cipher);
//	        long lUseTime = System.currentTimeMillis() - lStart;  
//		    System.out.println("加密耗时：" + lUseTime + "毫秒");  
//		    byte[] oStr = Cipher.ConstomSM4.decrypt(md,MySM4key);
//			System.out.println(new String(oStr,CharsetUtil.CHARSET_UTF_8));
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		
//		try {
//			long lStart = System.currentTimeMillis();
//			byte[] md = Cipher.SM4.encrypt(xml.getBytes(),MySM4key);
//			//String cipher = BASE64.encryptBASE64(md);
//			String cipher = HexUtil.toHexString(md);
//			System.out.println(cipher);
//	        long lUseTime = System.currentTimeMillis() - lStart;  
//		    System.out.println("加密耗时：" + lUseTime + "毫秒");  
//		    byte[] oStr = Cipher.SM4.decrypt(md,MySM4key);
//			System.out.println(new String(oStr,CharsetUtil.CHARSET_UTF_8));
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		
//		
//	}
	
	@Test
	public void testRSA() throws Exception {
		String string = "就是中国@Ab123";
		byte [] publicKey = ByteConverter.HEX.escape(TestAsymmetricKey.publicHexKey);
		byte [] privateKey = ByteConverter.HEX.escape(TestAsymmetricKey.privateHexKey);
		
		long lStart = System.currentTimeMillis();
		
		byte[] md = Cipher.RSA.encrypt(string.getBytes(CharsetUtil.CHARSET_UTF_8),publicKey);
		String cipher = HexUtil.toHexString(md);
		System.out.println(cipher);
		String cipherBase64 = BASE64Util.encryptBASE64(md);
		System.out.println(cipherBase64);
		
		long lUseTime = System.currentTimeMillis() - lStart;  
	    System.out.println("加密耗时：" + lUseTime + "毫秒");  
	    byte[] oStr = Cipher.RSA.decrypt(md,privateKey);
		System.out.println(new String(oStr,CharsetUtil.CHARSET_UTF_8));
		
	}
	
	@Test
	public void testDSA() throws Exception {
		String string = "就是中国@Ab123";
		byte [] publicKey = ByteConverter.HEX.escape(TestAsymmetricKey.DSAPublicHexKey);
		byte [] privateKey = ByteConverter.HEX.escape(TestAsymmetricKey.DSAPrivateHexKey);
		
		long lStart = System.currentTimeMillis();
		
		byte[] sign = Cipher.DSA.encrypt(string.getBytes(CharsetUtil.CHARSET_UTF_8),privateKey);
		String cipher = HexUtil.toHexString(sign);
		System.out.println(cipher);
		String cipherBase64 = BASE64Util.encryptBASE64(sign);
		System.out.println(cipherBase64);
		
		long lUseTime = System.currentTimeMillis() - lStart;  
	    System.out.println("加密耗时：" + lUseTime + "毫秒");  
	    
	    String string2 = "就是中国@Ab123";
	    string2 += "篡改内容";//注释篡改，校验结果内容一致。
	    byte[] oStr = Cipher.DSA.decrypt(string2.getBytes(CharsetUtil.CHARSET_UTF_8),publicKey,sign);
	    if (null == oStr) {
	    	System.out.println("内容有篡改");
	    } else {
	    	System.out.println("内容比对一致");
	    }
	}
	
	


}
package com.github.chenyuxin.commonframework.base.constant;

/**
 * 正则常量
 */
public interface RegexConst {
	
	/**
	 * 中文
	 */
	String CHINESE = "[\u4E00-\u9FFF]+";
	
	/**
	 * 18位身份证
	 */
	String IDCARD18 = "[1-9]\\d{5}[1-2]\\d{3}((0\\d)|(1[0-2]))(([012]\\d)|3[0-1])\\d{3}(\\d|X|x)";
	
	/**
	 * 15位身份证
	 */
	String IDCARD15 = "(1[1-5]|2[1-3]|3[1-7]|4[1-6]|5[0-4]|6[1-5])[0-9]{4}[1-9][0-9]((01|03|05|07|08|10|12)(0[1-9]|[12][0-9]|3[01])|(01|04|06|09|11)(0[1-9]|[12][0-9]|30)|(02)(0[1-9]|[12][0-9]))[0-9]{3}";
	
	/**
	 * 身份证<br>
	 * 包含15位和18位
	 */
	String IDCARD = "(" + RegexConst.IDCARD18 + ")|(" + RegexConst.IDCARD15 + ")";
	
	/**
	 * 固定电话
	 */
	String TEL = "(0[0-9]{2,3}-)?([1-9][0-9]{6,7})";
	
	/**
	 * 手机
	 * @更新时间 2022-02-09 18:21:20
	 */
	String PHONE = "((13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\\d{8})";
	
	/**
	 * 住地
	 */
	String ADDRESS = "([\\u4E00-\\u9FFF-Za-z0-9_]+(省|市|区|县|道|路|街|号)){2,}";
	
	/**
	 * 电子邮箱
	 */
	String EMAIL = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";
	
	/**
	 * 银行卡号
	 */
	String BANKCARDNUM = "\\d{16,19}";
	
	/**
	 * IPv4
	 */
	String IPV4 = "((25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\.){3}(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)";
	
	/**
	 * IP v6
	 */
	String IPV6 = "(([0-9a-fA-F]{1,4}:){7}[0-9a-fA-F]{1,4}|([0-9a-fA-F]{1,4}:){1,7}:|([0-9a-fA-F]{1,4}:){1,6}:[0-9a-fA-F]{1,4}|([0-9a-fA-F]{1,4}:){1,5}(:[0-9a-fA-F]{1,4}){1,2}|([0-9a-fA-F]{1,4}:){1,4}(:[0-9a-fA-F]{1,4}){1,3}|([0-9a-fA-F]{1,4}:){1,3}(:[0-9a-fA-F]{1,4}){1,4}|([0-9a-fA-F]{1,4}:){1,2}(:[0-9a-fA-F]{1,4}){1,5}|[0-9a-fA-F]{1,4}:((:[0-9a-fA-F]{1,4}){1,6})|:((:[0-9a-fA-F]{1,4}){1,7}|:)|fe80:(:[0-9a-fA-F]{0,4}){0,4}%[0-9a-zA-Z]+|::(ffff(:0{1,4})?:)?((25[0-5]|(2[0-4]|1?[0-9])?[0-9])\\.){3}(25[0-5]|(2[0-4]|1?[0-9])?[0-9])|([0-9a-fA-F]{1,4}:){1,4}:((25[0-5]|(2[0-4]|1?[0-9])?[0-9])\\.){3}(25[0-5]|(2[0-4]|1?[0-9])?[0-9]))";
	
	/**
	 * 英文字母 、数字和下划线
	 */
	String GENERAL = "\\w+";
	
	/**
	 * 字母和数字 
	 */
	String NUM_WORD = "[a-z0-9A-Z]+";
	
	/**
	 * 数字
	 */
	String NUMBERS = "\\d+";
	
	/**
	 * 字母
	 */
	String WORD = "[a-zA-Z]+";
	
	/**
	 * 中文字、英文字母、数字和下划线
	 */
	String GENERAL_WITH_CHINESE = "[\u4E00-\u9FFF\\w]+";
	
	/**
	 *  日期<br>支持格式:<br>
	 	YYYY-MM-DD <br>
		YYYY/MM/DD <br>
		YYYY_MM_DD <br>
		YYYY.MM.DD <br>
	 */
	String DATE = "((^((1[8-9]\\d{2})|([2-9]\\d{3}))([-\\/\\._])(10|12|0?[13578])([-\\/\\._])(3[01]|[12][0-9]|0?[1-9])$)|(^((1[8-9]\\d{2})|([2-9]\\d{3}))([-\\/\\._])(11|0?[469])([-\\/\\._])(30|[12][0-9]|0?[1-9])$)|(^((1[8-9]\\d{2})|([2-9]\\d{3}))([-\\/\\._])(0?2)([-\\/\\._])(2[0-8]|1[0-9]|0?[1-9])$)|(^([2468][048]00)([-\\/\\._])(0?2)([-\\/\\._])(29)$)|(^([3579][26]00)([-\\/\\._])(0?2)([-\\/\\._])(29)$)|(^([1][89][0][48])([-\\/\\._])(0?2)([-\\/\\._])(29)$)|(^([2-9][0-9][0][48])([-\\/\\._])(0?2)([-\\/\\._])(29)$)|(^([1][89][2468][048])([-\\/\\._])(0?2)([-\\/\\._])(29)$)|(^([2-9][0-9][2468][048])([-\\/\\._])(0?2)([-\\/\\._])(29)$)|(^([1][89][13579][26])([-\\/\\._])(0?2)([-\\/\\._])(29)$)|(^([2-9][0-9][13579][26])([-\\/\\._])(0?2)([-\\/\\._])(29)$))";
	/**
	 * 限定格式为yyyyMMdd的日期
	 */
	String yyyyMMdd = "((^((1[8-9]\\d{2})|([2-9]\\d{3}))(10|12|0?[13578])(3[01]|[12][0-9]|0?[1-9])$)|(^((1[8-9]\\d{2})|([2-9]\\d{3}))(11|0?[469])(30|[12][0-9]|0?[1-9])$)|(^((1[8-9]\\d{2})|([2-9]\\d{3}))(0?2)(2[0-8]|1[0-9]|0?[1-9])$)|(^([2468][048]00)(0?2)(29)$)|(^([3579][26]00)(0?2)(29)$)|(^([1][89][0][48])(0?2)(29)$)|(^([2-9][0-9][0][48])(0?2)(29)$)|(^([1][89][2468][048])(0?2)(29)$)|(^([2-9][0-9][2468][048])(0?2)(29)$)|(^([1][89][13579][26])(0?2)(29)$)|(^([2-9][0-9][13579][26])(0?2)(29)$))";
	/**
	 * 限定格式为yyyyMM的日期
	 */
	String yyyyMM = "^(\\d{4})(0[0-9]|10|11|12)$";
	
	/**
	 * QQ号码
	 */
	String QQ = "[1-9][0-9]{4,14}";
	
	/**
	 * 带`-`的UUID
	 */
	String UUID36 = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$";
	
	/**
	 * 32位UUID
	 */
	String UUID32 = "[0-9a-fA-F]{32}";
	
	/**
	 * BASE64字符串
	 */
	String BASE64 = "([A-Za-z0-9+/]{4})*([A-Za-z0-9+/]{4}|[A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{2}==)";
	
	/**
	 * 16进制字符串
	 */
	String HEX = "[a-fA-F0-9]+";
	
	
	
	
	
	
	/**
	 * URI<br>
	 * 定义见：https://www.ietf.org/rfc/rfc3986.html#appendix-B
	 */
	String URI = "(([^:/?#]+):)?(//([^/?#]*))?([^?#]*)(\\?([^#]*))?(#(.*))";
	/**
	 * URL
	 */
	String URL = "[a-zA-Z]+://[\\w-+&@#/%?=~_|!:,.;]*[\\w-+&@#/%=~_|]";
	/**
	 * Http URL（来自：http://urlregex.com/）<br>
	 * 此正则同时支持FTP、File等协议的URL
	 */
	String URL_HTTP = "(https?|ftp|file)://[\\w-+&@#/%?=~_|!:,.;]*[\\w-+&@#/%=~_|]";
	
	/**
	 * MAC地址正则
	 */
	String MAC_ADDRESS = "((?:[a-fA-F0-9]{1,2}[:-]){5}[a-fA-F0-9]{1,2})|0x(\\d{12}).+ETHER";
	
	/**
	 * 时间正则
	 */
	String TIME = "\\d{1,2}:\\d{1,2}(:\\d{1,2})?";
	/**
	 * 中国车牌号码（兼容新能源车牌）
	 */
	String PLATE_NUMBER =
			//https://gitee.com/loolly/hutool/issues/I1B77H?from=project-issue
			"(([京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领][A-Z](([0-9]{5}[ABCDEFGHJK])|([ABCDEFGHJK]([A-HJ-NP-Z0-9])[0-9]{4})))|" +
					//https://gitee.com/loolly/hutool/issues/I1BJHE?from=project-issue
					"([京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领]\\d{3}\\d{1,3}[领])|" +
					"([京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领][A-Z][A-HJ-NP-Z0-9]{4}[A-HJ-NP-Z0-9挂学警港澳使领]))";

	/**
	 * 社会统一信用代码
	 * <pre>
	 * 第一部分：登记管理部门代码1位 (数字或大写英文字母)
	 * 第二部分：机构类别代码1位 (数字或大写英文字母)
	 * 第三部分：登记管理机关行政区划码6位 (数字)
	 * 第四部分：主体标识码（组织机构代码）9位 (数字或大写英文字母)
	 * 第五部分：校验码1位 (数字或大写英文字母)
	 * </pre>
	 */
	String CREDIT_CODE = "[0-9A-HJ-NPQRTUWXY]{2}\\d{6}[0-9A-HJ-NPQRTUWXY]{10}";
	/**
	 * 车架号
	 * 别名：车辆识别代号 车辆识别码
	 * eg:LDC613P23A1305189
	 * eg:LSJA24U62JG269225
	 * 十七位码、车架号
	 * 车辆的唯一标示
	 */
	String CAR_VIN = "[A-Za-z0-9]{17}";
	/**
	 * 驾驶证  别名：驾驶证档案编号、行驶证编号
	 * eg:430101758218
	 * 12位数字字符串
	 * 仅限：中国驾驶证档案编号
	 */
	String CAR_DRIVING_LICENCE = "[0-9]{12}";
	
}

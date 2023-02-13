package com.github.chenyuxin.combo.base.util;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.charset.UnsupportedCharsetException;

import com.github.chenyuxin.combo.base.constant.StringPool;

/**
 * 字符集工具类
 */
public class CharsetUtil {
	/**
	 * US-ASCII
	 */
	public static final String US_ASCII = "US-ASCII";
	/**
	 * ISO-8859-1
	 */
	public static final String ISO_8859_1 = "ISO-8859-1";
	/**
	 * UTF-8
	 */
	public static final String UTF_8 = "UTF-8";
	/**
	 * GBK
	 */
	public static final String GBK = "GBK";

	/**
	 * US-ASCII
	 */
	public static final Charset CHARSET_US_ASCII = StandardCharsets.US_ASCII;
	/**
	 * ISO-8859-1
	 */
	public static final Charset CHARSET_ISO_8859_1 = StandardCharsets.ISO_8859_1;
	/**
	 * UTF-8
	 */
	public static final Charset CHARSET_UTF_8 = StandardCharsets.UTF_8;
	/**
	 * GBK
	 */
	public static final Charset CHARSET_GBK;

	static {
		//避免不支持GBK的系统中运行报错 issue#731
		Charset _CHARSET_GBK = null;
		try {
			_CHARSET_GBK = Charset.forName(GBK);
		} catch (UnsupportedCharsetException e) {
			//ignore
		}
		CHARSET_GBK = _CHARSET_GBK;
	}

	/**
	 * 转换为Charset对象
	 *
	 * @param charsetName 字符集，为空则返回默认UTF-8字符集
	 * @return Charset
	 * @throws UnsupportedCharsetException 编码不支持
	 */
	public static Charset charset(String charsetName) throws UnsupportedCharsetException {
		return Charset.forName(BaseUtil.setNullString(charsetName, UTF_8));
	}


	/**
	 * 转换字符串的字符集编码
	 *
	 * @param source      字符串
	 * @param srcCharset  源字符集，默认ISO-8859-1
	 * @param destCharset 目标字符集，默认UTF-8
	 * @return 转换后的字符集
	 */
	public static String convert(String source, String srcCharset, String destCharset) {
		return convert(source, Charset.forName(srcCharset), Charset.forName(destCharset));
	}

	/**
	 * 转换字符串的字符集编码<br>
	 * 当以错误的编码读取为字符串时，打印字符串将出现乱码。<br>
	 * 此方法用于纠正因读取使用编码错误导致的乱码问题。<br>
	 * 例如，在Servlet请求中客户端用GBK编码了请求参数，我们使用UTF-8读取到的是乱码，此时，使用此方法即可还原原编码的内容
	 * <pre>
	 * 客户端 -》 GBK编码 -》 Servlet容器 -》 UTF-8解码 -》 乱码
	 * 乱码 -》 UTF-8编码 -》 GBK解码 -》 正确内容
	 * </pre>
	 *
	 * @param source      字符串
	 * @param srcCharset  源字符集，默认ISO-8859-1
	 * @param destCharset 目标字符集，默认UTF-8
	 * @return 转换后的字符集
	 */
	public static String convert(String source, Charset srcCharset, Charset destCharset) {
		if (null == srcCharset) {
			srcCharset = StandardCharsets.ISO_8859_1;
		}
		if (null == destCharset) {
			destCharset = StandardCharsets.UTF_8;
		}
		if ( null == source || StringPool.BLANK.equals(source) || srcCharset.equals(destCharset)) {
			return source;
		}
		return new String(source.getBytes(srcCharset), destCharset);
	}

	/**
	 * 系统默认字符集编码
	 *
	 * @return 系统字符集编码
	 */
	public static String defaultCharsetName() {
		return defaultCharset().name();
	}

	/**
	 * 系统默认字符集编码
	 *
	 * @return 系统字符集编码
	 */
	public static Charset defaultCharset() {
		return Charset.defaultCharset();
	}

}
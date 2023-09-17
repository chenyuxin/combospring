package com.github.chenyuxin.commonframework.xml;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

public class XmlUtils {
	
	/**
	 * 正则匹配xml的声明头部
	 */
	public static final String xmlHeadRegex = "(<\\?xml).*?(\\?>)";
	//public static final String xmlHeadRegex = "<\\?xml\s+version=('|\").*?('|\")\s+(?:encoding=('|\").*?('|\"))?\\?>";
	
	/**
	 * 支持无root节点的xml,转为多个xml解析
	 * @param xml
	 * @return
	 * @throws DocumentException
	 */
	public static List<String> moreRootElementXML(String xml) throws DocumentException {
		List<String> moreRootElementXML = new ArrayList<String>();
		String xmlA = "<root>".concat(xml).concat("</root>");
        Document document = reader.read(new StringReader(xmlA));
    	List<Element> moreRootElement = document.getRootElement().elements();
    	for(Element element : moreRootElement) {
    		moreRootElementXML.add(element.asXML());
    		//System.out.println(element.asXML());
    	}
		return moreRootElementXML;
	}
	
	
	
	private static final SAXReader reader = new SAXReader();// 拿取解析器
	private static final OutputFormat format = new OutputFormat("    ", true);//默认格式化
	static {
		// xml的输出编码
    	format.setEncoding("utf-8");
    	// xml声明与内容是否添加空行
    	format.setNewLineAfterDeclaration(false);
    	// 是否设置xml声明头部
    	format.setSuppressDeclaration(false);
    	// 是否分行
    	format.setNewlines(true);
    	// 去除空行
    	format.setTrimText(true);
	}

	/**
     * 格式化xml,显示为容易看的XML格式
     *
     * @param xml 需要格式化的xml字符串
     * @return
	 * @throws DocumentException 
	 * @throws IOException 
     */
    public static String formatXML(String xml) throws DocumentException, IOException{
    	StringBuffer requestXML = new StringBuffer();
    	List<String> moreRootElementXML = moreRootElementXML(xml);
    	for (String moreXml : moreRootElementXML) {
            Document document = reader.read(new StringReader(moreXml));
            if (null != document) {
                StringWriter stringWriter = new StringWriter();
                XMLWriter writer = new XMLWriter(stringWriter, format);
                writer.write(document.getRootElement());  
                writer.flush();
                writer.close();
                requestXML.append(stringWriter.getBuffer().toString());
            }
    	}
    	return requestXML.toString();
    	
    }
    
    /**
     * xml文档格式化字符串
     * @param document
     * @return
     * @throws IOException
     */
    public static String formatXML(Document document) throws IOException {
    	StringWriter stringWriter = new StringWriter();
        XMLWriter writer = new XMLWriter(stringWriter, format);
        writer.write(document.getRootElement());
        writer.flush();
        writer.close();
        return stringWriter.getBuffer().toString();
    }
    
    
    /**
	 * Document对象子节点设置setText时不能用null对象，用此方法替换null值
	 * @param textValue
	 * @return
	 */
	public static String setNullDocumentText(Object textValue) {
		return null == textValue ? "" : String.valueOf(textValue);
	}
    
}


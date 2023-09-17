package com.github.chenyuxin.commonframework.xml;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

public class XmlMapUtils {

	/**
     * (多层)xml格式字符串转换为map
     *
     * @param xml xml字符串
     * @return 第一个为Root节点，Root节点之后为Root的元素，如果为多层，可以通过key获取下一层Map
	 * @throws DocumentException 
     */
    public static Map<String, Object> xmlToMap(String xml) throws DocumentException {
        Document doc = null;
        List<String> moreRootElementXML = null;
        Map<String, Object> map = new HashMap<String, Object>();
        Set<String> currentElementName = new HashSet<String>();//当前元素标签名集合，判断是否有重复的标签
    	//xml = xml.replace("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", "");
    	xml = xml.replaceAll(XmlUtils.xmlHeadRegex, "");//去除xml声明头部
    	moreRootElementXML = XmlUtils.moreRootElementXML(xml);
        for (String xmlA : moreRootElementXML) {
        	doc = DocumentHelper.parseText(xmlA);
            if (null == doc) {
                continue;
            }
            // 获取根元素
            Element rootElement = doc.getRootElement();
            if ( currentElementName.contains(rootElement.getName()) ) {//有相同名称的节点转为List
            	Object obj = map.get(rootElement.getName());
            	if (obj instanceof List) {//存在List说明之前已有重复节点，现在已经转为List
            		recursionXml(rootElement,obj);//往list里面继续递归
            	} else {//不存在List，是刚发现的重复，把节点转成list放入
            		List<Object> list = new ArrayList<Object>();
            		list.add(obj);
            		recursionXml(rootElement,list);//往list里面继续递归
            		map.put(rootElement.getName(),list);//节点放入成List
            	}
            } else {
            	recursionXml(rootElement,map);
            }
            currentElementName.add(rootElement.getName());
        }
        return map;
    }

    /**
     * 递归调用
     * @param element
     * @param out
     */
    @SuppressWarnings("unchecked")
	private static void recursionXml(Element element, Object out) {
        // 得到根元素下的子元素列表
		List<Element> childElements = element.elements();
		// innermap用于存储子元素的属性名和属性值
        Map<String, Object> innermap = new HashMap<String, Object>();
        //当前元素标签名集合，判断是否有重复的标签
        Set<String> currentElementName = new HashSet<String>();
        if (out instanceof List) {
        	if (childElements.size() == 0) {
        		((List<Object>) out).add(element.getTextTrim());
        	} else {
        		checkToMap(childElements, currentElementName, innermap);
        		((List<Object>) out).add(innermap);
        	}
        } if ( out instanceof Map) {
        	if (childElements.size() == 0) {
                // 如果没有子元素,则将其存储进map中
            	((Map<String, Object>) out).put(element.getName(), element.getTextTrim());
            } else {
            	checkToMap(childElements, currentElementName, innermap);
                ((Map<String, Object>) out).put(element.getName(), innermap);
            }
        }
        
        
    }
    
    private static void checkToMap(List<Element> childElements,Set<String> currentElementName,Map<String, Object> map){
    	for(Element childElement : childElements){
	    	if ( currentElementName.contains(childElement.getName()) ) {//有相同名称的节点转为List
	        	Object obj = map.get(childElement.getName());
	        	if (obj instanceof List) {//存在List说明之前已有重复节点，现在已经转为List
	        		recursionXml(childElement,obj);//往list里面继续递归
	        	} else {//不存在List，是刚发现的重复，把节点转成list放入
	        		List<Object> list = new ArrayList<Object>();
	        		list.add(obj);
	        		recursionXml(childElement,list);//往list里面继续递归
	        		map.put(childElement.getName(),list);//节点放入成List
	        	}
	        } else {
	        	recursionXml(childElement,map);
	        }
	        currentElementName.add(childElement.getName());
    	}
    }
    
    
    
    
    
    
    /**
     * (多层)map转换为xml格式字符串
     *
     * @param map 需要转换为xml的map
     * @return xml字符串
     * @throws IOException 
     * @throws DocumentException 
     */
    public static String mapToXml(Map<String, Object> map) throws DocumentException, IOException{
        Document doc = DocumentHelper.createDocument();
        doc.addElement("root");
        String xml = recursionToXml(doc.getRootElement(), map);
        return XmlUtils.formatXML(xml);
    }

    
    
    
    /**
     * multilayerToXml递归调用
     *
     * @param element 节点元素
     * @param map 需要转换为xml的map
     * @return xml字符串
     */
    @SuppressWarnings("unchecked")
    private static String recursionToXml(Element panrentElement, Map<String, Object> map) {
    	StringBuffer xml = new StringBuffer();
    	Iterator<Map.Entry<String, Object>> iterator = map.entrySet().iterator();
    	while (iterator.hasNext()) {
    		Map.Entry<String, Object> entry = iterator.next();
    		Object obj = entry.getValue();
    		if (obj instanceof List) {
    			List<Object> list = (List<Object>)obj;
    			for (int i=0;i<list.size();i++) {
    				Element xmlElement = panrentElement.addElement(entry.getKey());
                    if (list.get(i) instanceof Map) {
                    	recursionToXml(xmlElement, (Map<String, Object>)list.get(i));
                    } else {
                        String value = list.get(i) == null ? "" : list.get(i).toString();
                        xmlElement.addText(value);
                    }
                    xml.append(xmlElement.asXML());
    			}
    		} else {
    			Element xmlElement = panrentElement.addElement(entry.getKey());
                if (obj instanceof Map) {
                	recursionToXml(xmlElement, (Map<String, Object>)obj);
                } else {
                    String value = obj == null ? "" : obj.toString();
                    xmlElement.addText(value);
                }
                xml.append(xmlElement.asXML());
			}
    		
		}
        return xml.toString();
    }
    
}

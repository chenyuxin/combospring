package com.github.chenyuxin.combo.redis.util;

import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.JSONReader.Feature;

public class MyFastJsonRedisSerializer implements RedisSerializer<Object> {
	
	//private final static ParserConfig defaultRedisConfig = new ParserConfig();

    public byte[] serialize(Object object) throws SerializationException {
        if (object == null) {
            return new byte[0];
        }
        try {
        	return JSON.toJSONBytes(object, JSONWriter.Feature.WriteMapNullValue,JSONWriter.Feature.WriteNulls);
        	//return JSON.toJSONBytes(object, SerializerFeature.WriteMapNullValue,SerializerFeature.WriteNullStringAsEmpty,SerializerFeature.WriteDateUseDateFormat);
        } catch (Exception ex) {
            throw new SerializationException("Could not serialize: " + ex.getMessage(), ex);
        }
    }

    public Object deserialize(byte[] bytes) throws SerializationException {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        try {
            return JSON.parseObject(bytes,Feature.UseNativeObject);
        } catch (Exception ex) {
            throw new SerializationException("Could not deserialize: " + ex.getMessage(), ex);
        }
    }

}

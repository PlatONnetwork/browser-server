package com.platon.browser.serializer;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

/**
 * 	数组返回空值处理序列化
 *  @file NullArrayJsonSerializer.java
 *  @description 
 *	@author zhangrj
 *  @data 2019年8月31日
 */
public class NullArrayJsonSerializer extends JsonSerializer<Object>{
	@Override
    public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
        if (value == null) {
        	/**
    		 * 	判断数组为null则填写空数组。
    		 */
            jgen.writeStartArray();
            jgen.writeEndArray();
        } else {
        	/**
    		 * 	判断数组不为空，填写原值。
    		 */
            jgen.writeObject(value);
        }
    }
}

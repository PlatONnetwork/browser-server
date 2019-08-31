package com.platon.browser.serializer;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

/**
 * 	String返回空值处理序列化
 *  @file NullStringJsonSerializer.java
 *  @description 
 *	@author zhangrj
 *  @data 2019年8月31日
 */
public class NullStringJsonSerializer extends JsonSerializer<Object> {
	@Override
    public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
        if (value == null) {
        	/**
    		 * 	判断数组为null则填写""。
    		 */
            jgen.writeString("");
        }
    }
}

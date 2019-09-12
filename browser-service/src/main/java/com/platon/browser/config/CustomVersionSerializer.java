package com.platon.browser.config;

import java.io.IOException;
import java.math.BigInteger;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.platon.browser.util.VerUtil;

/**
 *  返回版本转化实现类
 *  @file CustomVersionSerializer.java
 *  @description 使用方式在具体的get方法上添加@JsonSerialize(using = CustomVersionSerializer.class)
 *	@author zhangrj
 *  @data 2019年9月9日
 */
public class CustomVersionSerializer  extends JsonSerializer<String>{

	@Override
	public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
		if(StringUtils.isNotBlank(value)) {
			gen.writeString(VerUtil.toVersion(new BigInteger(value)));
		}
	}

}
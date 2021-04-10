package com.platon.browser.config.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.math.BigDecimal;

/**
 *  返回百分比转化实现类
 *  @file CustomRateSerializer.java
 *  @description 使用方式在具体的get方法上添加@JsonSerialize(using = CustomRateSerializer.class)
 *	@author zhangrj
 *  @data 2019年8月31日
 */
public class CustomRateSerializer  extends JsonSerializer<String>{

	@Override
	public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
		/**	百分比统一转换，保留两位小数 */
		if(StringUtils.isNotBlank(value)) {
			String transEner = new BigDecimal(value).multiply(new BigDecimal("100")).setScale(2) + "%";
			gen.writeString(transEner);
		}  else {
			gen.writeString("");
		}
	}

}

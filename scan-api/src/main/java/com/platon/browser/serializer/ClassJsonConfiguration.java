package com.platon.browser.serializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

/**
 * 
 *  @file ClassJsonConfiguration.java
 *  @description 申明类json配置文件，统一为返回的空值进行处理
 *	@author zhangrj
 *  @data 2019年8月31日
 */
@Configuration
public class ClassJsonConfiguration {

    @Bean
    public MappingJackson2HttpMessageConverter mappingJacksonHttpMessageConverter() {

    	/**
    	 * 申明converter，从而获取mapper
    	 */
        final MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();

        ObjectMapper mapper = converter.getObjectMapper();

        /**
         * 	 为mapper注册一个带有SerializerModifier的Factory，此modifier主要做的事情为：判断序列化类型，根据类型指定为null时的值
         */
        mapper.setSerializerFactory(mapper.getSerializerFactory().withSerializerModifier(new CustomBeanSerializerModifier()));

        return converter;
    }

}

package com.platon.browser.config.json;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.platon.browser.utils.ChainVersionUtil;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.math.BigInteger;

/**
 * 返回版本转化实现类
 *
 * @author zhangrj
 * @file CustomVersionSerializer.java
 * @description 使用方式在具体的get方法上添加@JsonSerialize(using = CustomVersionSerializer.class)
 * @data 2019年9月9日
 */
public class CustomVersionSerializer extends JsonSerializer<String> {

    @Override
    public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (StrUtil.contains(value, ".")) {
            gen.writeString(value);
        } else if (StringUtils.isNotBlank(value)) {
            gen.writeString(ChainVersionUtil.toStringVersion(new BigInteger(value)));
        } else {
            gen.writeString("");
        }
    }

}

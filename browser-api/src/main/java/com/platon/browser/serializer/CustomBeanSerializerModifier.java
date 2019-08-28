package com.platon.browser.serializer;

import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;

/**
 * 
 * @author Administrator
 *
 */
public class CustomBeanSerializerModifier extends BeanSerializerModifier {
	private JsonSerializer<Object> _nullArrayJsonSerializer = new NullArrayJsonSerializer();

    private JsonSerializer<Object> _nullStringJsonSerializer = new NullStringJsonSerializer();

    private JsonSerializer<Object> _nullIntegerJsonSerializer = new NullIntegerJsonSerializer();

    @Override
    public List<BeanPropertyWriter> changeProperties(SerializationConfig config, BeanDescription beanDesc,
                                                     List<BeanPropertyWriter> beanProperties) {
        // 循环所有的beanPropertyWriter
        for (int i = 0; i < beanProperties.size(); i++) {
            BeanPropertyWriter writer = beanProperties.get(i);
            // 判断字段的类型，如果是array，list，set则注册nullSerializer
            if (isArrayType(writer)) {
                //给writer注册一个自己的nullSerializer
                writer.assignNullSerializer(this.defaultNullArrayJsonSerializer());
            }
            if (isStringType(writer)) {
                writer.assignNullSerializer(this.defaultNullStringJsonSerializer());
            }

            if (isNumberType(writer)) {
                writer.assignNullSerializer(this.defaultNullIntegerJsonSerializer());
            }
        }
        return beanProperties;
    }

    // 判断是什么类型
    protected boolean isArrayType(BeanPropertyWriter writer) {
        Class<?> clazz = writer.getPropertyType();
        return clazz.isArray() || clazz.equals(List.class) || clazz.equals(Set.class);
    }

    protected boolean isStringType(BeanPropertyWriter writer) {
        Class<?> clazz = writer.getPropertyType();
        return clazz.equals(String.class);
    }

    protected boolean isNumberType(BeanPropertyWriter writer) {
        Class<?> clazz = writer.getPropertyType();
        return clazz.equals(Integer.class) || clazz.equals(int.class)|| clazz.equals(Long.class)
        		|| clazz.equals(long.class)|| clazz.equals(Double.class)|| clazz.equals(Double.class);
    }

    protected JsonSerializer<Object> defaultNullArrayJsonSerializer() {
        return _nullArrayJsonSerializer;
    }

    protected JsonSerializer<Object> defaultNullStringJsonSerializer() {
        return _nullStringJsonSerializer;
    }

    protected JsonSerializer<Object> defaultNullIntegerJsonSerializer() {
        return _nullIntegerJsonSerializer;
    }

}

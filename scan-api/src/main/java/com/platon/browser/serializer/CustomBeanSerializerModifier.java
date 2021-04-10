package com.platon.browser.serializer;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;

import java.util.List;
import java.util.Set;

/**
 * json序列化编辑器
 *  @file CustomBeanSerializerModifier.java
 *  @description 
 *	@author zhangrj
 *  @data 2019年8月31日
 */
public class CustomBeanSerializerModifier extends BeanSerializerModifier {
	/**
	 * 	申明全局空数组序列化对象
	 */
	private JsonSerializer<Object> nullArrayJsonSerializer = new NullArrayJsonSerializer();
	/**
	 * 	申明全局空String序列化对象
	 */
    private JsonSerializer<Object> nullStringJsonSerializer = new NullStringJsonSerializer();
    /**
	 * 	申明全局空数字序列化对象
	 */
    private JsonSerializer<Object> nullIntegerJsonSerializer = new NullIntegerJsonSerializer();

    @Override
    public List<BeanPropertyWriter> changeProperties(SerializationConfig config, BeanDescription beanDesc,
                                                     List<BeanPropertyWriter> beanProperties) {
        /**
         * 	 循环所有的beanPropertyWriter
         */
        for (int i = 0; i < beanProperties.size(); i++) {
            BeanPropertyWriter writer = beanProperties.get(i);
            /**
             * 	 判断字段的类型，如果是array，list，set则注册nullSerializer
             */
            if (isArrayType(writer)) {
                /**
                 * 	给writer注册一个自己的nullSerializer
                 */
                writer.assignNullSerializer(this.defaultNullArrayJsonSerializer());
            }
            /**
             * 	 判断字段的类型，如果是String则注册nullSerializer
             */
            if (isStringType(writer)) {
                writer.assignNullSerializer(this.defaultNullStringJsonSerializer());
            }
            /**
             * 	 判断字段的类型，如果是数字则注册nullSerializer
             */
            if (isNumberType(writer)) {
                writer.assignNullSerializer(this.defaultNullIntegerJsonSerializer());
            }
        }
        return beanProperties;
    }

    /**
     * 	 判断是数组类型
     */
    protected boolean isArrayType(BeanPropertyWriter writer) {
        Class<?> clazz = writer.getType().getRawClass();
        return clazz.isArray() || clazz.equals(List.class) || clazz.equals(Set.class);
    }

    /**
     * 	判断String类型
     * @method isStringType
     */
    protected boolean isStringType(BeanPropertyWriter writer) {
        Class<?> clazz = writer.getType().getRawClass();
        return clazz.equals(String.class);
    }

    /**
     * 	判断数字类型
     * @method isNumberType
     */
    protected boolean isNumberType(BeanPropertyWriter writer) {
		Class<?> clazz = writer.getType().getRawClass();
        return clazz.equals(Integer.class) || clazz.equals(int.class)|| clazz.equals(Long.class)
        		|| clazz.equals(long.class)|| clazz.equals(Double.class)|| clazz.equals(double.class);
    }

    protected JsonSerializer<Object> defaultNullArrayJsonSerializer() {
        return nullArrayJsonSerializer;
    }

    protected JsonSerializer<Object> defaultNullStringJsonSerializer() {
        return nullStringJsonSerializer;
    }

    protected JsonSerializer<Object> defaultNullIntegerJsonSerializer() {
        return nullIntegerJsonSerializer;
    }

}

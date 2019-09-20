package com.platon.browser.util;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 转换对象类
 *  @file BeanConvertUtil.java
 *  @description 
 *	@author zhangrj
 *  @data 2019年8月31日
 */
public class BeanConvertUtil {
    private BeanConvertUtil(){}
    /**
     * bean对象转换
     *
     * @param o
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T beanConvert(Object o, Class<T> clazz) {
        if (null == o) {
            return null;
        }
        return JSON.parseObject(JSON.toJSONString(o), clazz);
    }

    /**
     * list 对象转换
     *
     * @param list
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T, E> List<T> listConvert(List<E> list, Class<T> clazz) {
        List<T> returnList = new ArrayList<>();
        list.forEach(o -> returnList.add(beanConvert(o, clazz)));
        return returnList;
    }

    public static Map<?, ?> objectToMap(Object object) {
        return beanConvert(object, Map.class);
    }

    public static <T> T mapToObject(Map<String, Object> map, Class<T> clazz) {
        return JSON.parseObject(JSON.toJSONString(map), clazz);
    }
}
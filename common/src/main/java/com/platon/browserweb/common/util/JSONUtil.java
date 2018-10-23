package com.platon.browserweb.common.util;

import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.PropertyFilter;

/**
 * @Author cxf
 * @Since 2018/7/12
 */
public final class JSONUtil {

	private JSONUtil() {
		super();
	}

	public static String bean2Json(Object object) {
		return JSON.toJSONString(object, true);
	}

	public static <T> T json2Bean(String msg, Class<T> clazz) {
		return JSON.parseObject(msg, clazz);
	}
	
	public static String bean2JsonWithFilter(Object object) {
		return JSON.toJSONString(object, filter);
	}
	
	public static <T> List<T> json2ArrayBean(String msg, Class<T> clazz) {
		return JSON.parseArray(msg, clazz);
	}
	
	/**
	 * 序列化成JSON字符串， 无需prettyFormat格式化
	 * @param object
	 * @return
	 */
	public static String bean2JsonWithoutFmt(Object object) {
		return JSON.toJSONString(object, false);
	}
	
	protected static PropertyFilter filter = new PropertyFilter() {
	    //过滤不需要的字段
	    public boolean apply(Object source, String name, Object value) {

	        if(value instanceof List){
	            return true;
	        }

	        if(value == null){
	            return false;
	        }
	        
	        if("primaryKey".equals(name)){
	        	return false;
	        }
	        return true;

	        /*if(value instanceof List){
	            if(value == null || ((List)value).size()==0){
	                return false;
	            }
	        }
	        if(value instanceof Map){
	            if(value == null || ((Map)value).isEmpty()){
	                return false;
	            }
	        }
	        return true;*/
	    }
	};
}

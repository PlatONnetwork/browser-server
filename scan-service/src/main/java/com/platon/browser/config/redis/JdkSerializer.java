//package com.platon.browser.config.redis;
//
//import com.platon.browser.exception.BusinessException;
//import lombok.extern.slf4j.Slf4j;
//
//import java.io.ByteArrayInputStream;
//import java.io.ByteArrayOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.ObjectInputStream;
//import java.io.ObjectOutputStream;
//import java.io.OutputStream;
//import java.io.Serializable;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
///**
// * 反序列化工具
// *  @file JdkSerializer.java
// *  @description
// *	@author zhangrj
// *  @data 2019年11月13日
// */
//@Slf4j
//public class JdkSerializer<T> implements JedisSerializer<T> {
//
//	@SuppressWarnings("unchecked")
//	public static void main(String[] args) {
//		JdkSerializer<Object> a = new JdkSerializer<>();
//
//		List<Map<String, Integer>> serialize = new ArrayList<>();
//		Map<String, Integer> m = new HashMap<>();
//		m.put("A", 123);
//		serialize.add(m);
//		byte[] str = a.serialize(serialize);
//		Object deserialize = a.deserialize(str);
//		if(deserialize != null){
//			log.error("{}",((List<Map<String, Integer>>)deserialize).get(0));
//		}
//
//	}
//
//	public byte[] serialize(T t) {
//		ByteArrayOutputStream byteStream = new ByteArrayOutputStream(256);
//		try  {
//			serialize(t, byteStream);
//			return byteStream.toByteArray();
//		}catch (IOException ex) {
//			log.error("",ex);
//			throw new BusinessException("序列化失败");
//		}
//	}
//
//	@SuppressWarnings("unchecked")
//	public T deserialize(byte[] bytes) {
//		ByteArrayInputStream byteStream = new ByteArrayInputStream(bytes);
//		try {
//			return (T) deserialize(byteStream);
//		}
//		catch (IOException ex) {
//			throw new BusinessException("反序列化失败");
//		}
//	}
//
//
//	private void serialize(Object object, OutputStream outputStream) throws IOException {
//		if (!(object instanceof Serializable)) {
//			throw new IllegalArgumentException(getClass().getSimpleName() + " requires a Serializable payload " +
//					"but received an object of type [" + object.getClass().getName() + "]");
//		}
//		ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
//		objectOutputStream.writeObject(object);
//		objectOutputStream.flush();
//	}
//
//	private Object deserialize(InputStream inputStream) throws IOException {
//		ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
//		try {
//			return objectInputStream.readObject();
//		}
//		catch (ClassNotFoundException ex) {
//			throw new IOException("Failed to deserialize object type", ex);
//		}
//	}
//}

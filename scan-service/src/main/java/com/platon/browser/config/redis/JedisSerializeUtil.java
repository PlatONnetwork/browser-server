//package com.platon.browser.config.redis;
//
//import lombok.extern.slf4j.Slf4j;
//
//import java.io.ByteArrayInputStream;
//import java.io.ByteArrayOutputStream;
//import java.io.ObjectInputStream;
//import java.io.ObjectOutputStream;
//
//
///**
// * 序列化工具
// * @author Jungle
// */
//@Slf4j
//public class JedisSerializeUtil {
//
//	// 序列化
//	public static byte[] serialize(Object object) {
//		byte[] bytes ={};
//		ObjectOutputStream objectOutputStream = null;
//		ByteArrayOutputStream byteArrayOutputStream = null;
//		try {
//			byteArrayOutputStream = new ByteArrayOutputStream();
//			objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
//			objectOutputStream.writeObject(object);
//			return byteArrayOutputStream.toByteArray();
//		} catch (Exception e) {
//			log.error("",e);
//		}
//		return bytes;
//	}
//
//	// 反序列化
//	public static Object deSeialize(byte[] bytes) {
//		ByteArrayInputStream byteArrayOutputStream = null;
//		try {
//			byteArrayOutputStream = new ByteArrayInputStream(bytes);
//			ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayOutputStream);
//			return objectInputStream.readObject();
//		} catch (Exception e) {
//			log.error("deserialize exception");
//		}
//		return null;
//	}
//
//	public static void main(String[] args) {
//		Object str = "tobytes";
//		log.debug("{}",JedisSerializeUtil.deSeialize(JedisSerializeUtil.serialize(str)));
//	}
//}

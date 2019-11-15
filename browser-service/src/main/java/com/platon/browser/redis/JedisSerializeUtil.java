package com.platon.browser.redis;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


/**
 * 序列化工具
 * @author Jungle
 */
public class JedisSerializeUtil {

	// 序列化
	public static byte[] serialize(Object object) {
		ObjectOutputStream objectOutputStream = null;
		ByteArrayOutputStream byteArrayOutputStream = null;
		try {
			byteArrayOutputStream = new ByteArrayOutputStream();
			objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
			objectOutputStream.writeObject(object);
			byte[] bytes = byteArrayOutputStream.toByteArray();
			return bytes;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// 反序列化
	public static Object deSeialize(byte[] bytes) {
		ByteArrayInputStream byteArrayOutputStream = null;
		try {
			byteArrayOutputStream = new ByteArrayInputStream(bytes);
			ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayOutputStream);
			return objectInputStream.readObject();
		} catch (Exception e) {
			System.out.println("deserialize exception");

		}
		return null;
	}

	public static void main(String[] args) {
		Object str = "tobytes";
		System.out.print(JedisSerializeUtil.deSeialize(JedisSerializeUtil.serialize(str)));
	}
}
//package com.platon.browser.config.redis;
//
//import redis.clients.jedis.JedisCluster;
//
///**
// * 无结果的返回回调
// *  @file JedisNoResultCall.java
// *  @description
// *	@author zhangrj
// *  @data 2019年11月13日
// */
//public abstract class JedisNoResultCall implements JedisCallback<Object>{
//
//	public Object doInRedis(JedisCluster jedisCluster) {
//		action(jedisCluster);
//		return null;
//	}
//
//	abstract void action(JedisCluster jedisCluster);
//
//}

package com.platon.browser.util;

import static org.junit.Assert.*;

import java.util.Map;

import org.junit.Test;

import com.platon.browser.exception.HttpRequestException;

public class HttpUtilTest {

	@Test
	public void testPost() throws HttpRequestException {
		String url = "http://192.168.16.173:9061/browser-server/address/details";
		String param = "{\"address\":\"0x2e95e3ce0a54951eb9a99152a6d5827872dfb4fd\"}";
		Map<?,?> resp = HttpUtil.post(url,param,Map.class);
		assertTrue((Integer)resp.get("code")==0);
	}

	@Test
	public void testGet() throws HttpRequestException {
//		String url = "http://192.168.16.173";
//		String resp = HttpUtil.get(url,String.class);
//		System.out.println(resp);
	}

}

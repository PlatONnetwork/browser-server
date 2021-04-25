//package com.platon.browser.util;
//
//import static org.junit.Assert.*;
//
//import java.util.Map;
//
//import org.junit.Test;
//
//import com.platon.browser.exception.HttpRequestException;
//
//public class HttpUtilTest {
//
//	@Test
//	public void testPost() throws HttpRequestException {
//		String url = "http://192.168.16.173:9061/browser-server/address/details";
//		String param = "{\"address\":\"0x2e95e3ce0a54951eb9a99152a6d5827872dfb4fd\"}";
//		Map<?,?> resp = HttpUtil.post(url,param,Map.class);
//		assertTrue((Integer)resp.get("code")==0);
//		url = "http://192.168.16.255:9061/browser-server/address/details";
//		try {
//			resp = HttpUtil.post(url,param,Map.class);
//		} catch (Exception e) {
//			System.out.println(e.getMessage());
//			assertTrue(e instanceof HttpRequestException);
//		}
//
//		url = "http://192.168.16.173/static/images/herder-logo-a.png";
//		try {
//			resp = HttpUtil.post(url,param,Map.class);
//		} catch (Exception e) {
//			System.out.println(e.getMessage());
//			assertTrue(e instanceof HttpRequestException);
//		}
//	}
//
//	@Test
//	public void testGet() throws HttpRequestException {
//		String url = "http://192.168.16.173";
//		String resp = HttpUtil.get(url,String.class);
//		assertTrue(resp!=null);
////		try {
////			url = "http://192.168.16.173/download?address=0xba336581bc500d838fc93368986b06bb91152df3&exportname=account";
////			HttpUtil.get(url,Object.class);
////		} catch (Exception e) {
////
////		}
//	}
//
//}

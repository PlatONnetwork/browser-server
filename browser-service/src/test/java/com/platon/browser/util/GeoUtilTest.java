package com.platon.browser.util;

import static org.junit.Assert.*;

import org.junit.Test;

import com.maxmind.geoip2.model.CityResponse;
import com.platon.browser.util.GeoUtil;

public class GeoUtilTest {
	
	@Test
	public void testIpLocation() {
		GeoUtil.IpLocation il = new GeoUtil.IpLocation();
		il.setCountryCode("0");
        il.setLocation("");
        il.setLatitude("0");
        il.setLongitude("0");
        assertTrue("0".equals(il.getCountryCode()));
	}

	@Test
	public void testGetIpLocation() {
		// GeoUtil.IpLocation(ip=14.215.177.39, countryCode=CN, location=China Zhaoqing, longitude=112.4597, latitude=23.0512)
		GeoUtil.IpLocation ipLocaltion = GeoUtil.getIpLocation("14.215.177.39");
		assertTrue("CN".equals(ipLocaltion.getCountryCode()));
		ipLocaltion = GeoUtil.getIpLocation("14.215.177.266");
		assertTrue("0".equals(ipLocaltion.getCountryCode()));
	}

	@Test
	public void testGetResponse() {
		CityResponse resp = GeoUtil.getResponse("14.215.177.39");
		assertTrue("CN".equals(resp.getCountry().getIsoCode()));
		resp = GeoUtil.getResponse("255.215.177.252");
		assertNull(resp);
	}

	@Test
	public void testIpCheck() {
		try {
			GeoUtil.ipCheck("");
		} catch (Exception e) {
			assertTrue(e instanceof RuntimeException);
		}
		GeoUtil.ipCheck("14.215.177.39");
		assertTrue(true);
		try {
			GeoUtil.ipCheck("14.215.177");
		} catch (Exception e) {
			assertTrue(e instanceof RuntimeException);
		}
	}

}

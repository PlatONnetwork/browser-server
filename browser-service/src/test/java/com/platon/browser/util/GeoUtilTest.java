package com.platon.browser.util;

import static org.junit.Assert.*;

import org.junit.Test;

import com.maxmind.geoip2.model.CityResponse;
import com.platon.browser.util.GeoUtil;

public class GeoUtilTest {

	@Test
	public void testGetIpLocation() {
		// GeoUtil.IpLocation(ip=14.215.177.39, countryCode=CN, location=China Zhaoqing, longitude=112.4597, latitude=23.0512)
		GeoUtil.IpLocation ipLocaltion = GeoUtil.getIpLocation("14.215.177.39");
		assertTrue("CN".equals(ipLocaltion.getCountryCode()));
	}

	@Test
	public void testGetResponse() {
		CityResponse resp = GeoUtil.getResponse("14.215.177.39");
		assertTrue("CN".equals(resp.getCountry().getIsoCode()));
	}

	@Test
	public void testIpCheck() {
		assertTrue(GeoUtil.ipCheck("14.215.177.39"));
	}

}

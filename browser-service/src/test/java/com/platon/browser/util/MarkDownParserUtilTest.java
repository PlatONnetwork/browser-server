package com.platon.browser.util;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import com.platon.browser.util.MarkDownParserUtil;

public class MarkDownParserUtilTest {

	@Test
	public void testHttpGet() throws IOException {
		String str = MarkDownParserUtil.httpGet("http://www.baidu.com");
		assertNotNull(str);
	}

	@Test
	public void testAcquireMD() throws IOException {
		String url = "https://github.com/danielgogo/PIPs/blob/master/PIP-3.md";
		String str = MarkDownParserUtil.acquireMD(url);
		assertNotNull(str);
	}

	@Test
	public void testParserMD() throws IOException {
		String url = "https://github.com/danielgogo/PIPs/blob/master/PIP-3.md";
		String str = MarkDownParserUtil.acquireMD(url);
		str = MarkDownParserUtil.parserMD(str);
		assertNotNull(str);
	}

}

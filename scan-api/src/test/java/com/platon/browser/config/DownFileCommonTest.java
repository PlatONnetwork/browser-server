package com.platon.browser.config;

import org.easymock.EasyMock;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.Silent.class)
public class DownFileCommonTest {

	@Test
	public void testDownload() throws IOException {
		DownFileCommon downFileCommon = new DownFileCommon();
		HttpServletResponse httpServletResponse = EasyMock.createMock(HttpServletResponse.class);
		byte[] data = new byte[0];
		try {
			downFileCommon.download(httpServletResponse, "1.csv", 0, data);
		} catch (Exception e) {
			assertEquals(e.getMessage(), null);
		}

	}
}

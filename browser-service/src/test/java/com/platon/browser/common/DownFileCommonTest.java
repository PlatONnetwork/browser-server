package com.platon.browser.common;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.Silent.class)
public class DownFileCommonTest {

	@Mock
	private DownFileCommon downFileCommon;
	
	@Test
	public void testDownload() throws IOException {
		HttpServletResponse httpServletResponse = Mockito.mock(HttpServletResponse.class);
		byte[] data = new byte[0];
		downFileCommon.download(httpServletResponse, "1.csv", 0, data);
	}
}

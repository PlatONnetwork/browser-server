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
	public void testAcquireMD() throws Exception {
		String url = "https://github.com/danielgogo/PIPs/blob/master/PIP-3.md";
		String str = MarkDownParserUtil.acquireMD(url);
		assertNotNull(str);
	}

	@Test
	public void testParserMD() throws IOException {
		String str = "<table data-table-type=\"yaml-metadata\">\r\n" + 
				"  <thead>\r\n" + 
				"  <tr>\r\n" + 
				"  <th>PIP</th>\r\n" + 
				"  <th>Topic</th>\r\n" + 
				"  <th>Author</th>\r\n" + 
				"  <th>Status</th>\r\n" + 
				"  <th>Type</th>\r\n" + 
				"  <th>Description</th>\r\n" + 
				"  <th>Created</th>\r\n" + 
				"  </tr>\r\n" + 
				"  </thead>\r\n" + 
				"  <tbody>\r\n" + 
				"  <tr>\r\n" + 
				"  <td><div>3</div></td>\r\n" + 
				"  <td><div>Topic of 3</div></td>\r\n" + 
				"  <td><div>vivi</div></td>\r\n" + 
				"  <td><div>Processing/Rejected/Approved</div></td>\r\n" + 
				"  <td><div>Finance</div></td>\r\n" + 
				"  <td><div>Description of 3</div></td>\r\n" + 
				"  <td><div>2019-08-20</div></td>\r\n" + 
				"  </tr>\r\n" + 
				"  </tbody>\r\n" + 
				"</table>";
		str = MarkDownParserUtil.parserMD(str);
		assertNotNull(str);
	}

}

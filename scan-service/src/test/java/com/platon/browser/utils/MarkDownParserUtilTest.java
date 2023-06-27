package com.platon.browser.utils;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class MarkDownParserUtilTest {

	@Test
	public void testacquireMD() throws Exception {
		String str = MarkDownParserUtil.acquireMD("http://192.168.16.173");
		assertNull(str);
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

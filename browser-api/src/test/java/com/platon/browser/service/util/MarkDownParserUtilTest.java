package com.platon.browser.service.util;
import static org.junit.Assert.*;

import com.platon.browser.util.MarkDownParserUtil;
import org.junit.Test;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: 王章雄
 * Email:wangzhangxiong@juzix.net
 * Date: 2019/8/19
 * Time: 20:27
 * Desc:
 */
public class MarkDownParserUtilTest {
    @Test
    public void requireMD() throws IOException {
        String fileName="https://github.com/ethereum/EIPs/blob/master/EIPS/eip-100.md";
        String result=MarkDownParserUtil.acquireMD(fileName);
        assertNotNull(result);
    }
    @Test
    public void parserMD() throws IOException {
        String fileName="https://github.com/ethereum/EIPs/blob/master/EIPS/eip-100.md";
        String mdText=MarkDownParserUtil.acquireMD(fileName);
        String text=MarkDownParserUtil.parserMD(mdText);
        assertEquals(text, "{\"author\":\"Vitalik Buterin (@vbuterin)\",\"created\":\"2016-04-28\",\"title\":\"Change difficulty adjustment to target mean block time including uncles\",\"type\":\"Standards Track\",\"category\":\"Core\",\"eip\":\"100\",\"status\":\"Final\"}");

    }
}


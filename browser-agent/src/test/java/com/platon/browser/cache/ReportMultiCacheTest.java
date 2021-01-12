package com.platon.browser.cache;

import com.platon.browser.AgentTestBase;
import com.platon.browser.dao.param.ppos.Report;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.Silent.class)
public class ReportMultiCacheTest extends AgentTestBase {

    @Spy
    private ReportMultiSignParamCache reportMultiSignParamCache;
    @Test
    public void test(){
    	Report report = Report.builder().nodeId("0x1").build();
    	reportMultiSignParamCache.addReport(report);

    	List<Report> reports = new ArrayList<Report>();
    	reports.add(report);
    	reportMultiSignParamCache.init(reports);
    	reportMultiSignParamCache.getNodeIdList();
    	reportMultiSignParamCache.getReportList("0x1");
    	reportMultiSignParamCache.remove("0x1");
        assertTrue(true);
    }
}

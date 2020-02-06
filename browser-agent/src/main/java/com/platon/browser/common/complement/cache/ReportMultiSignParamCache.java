package com.platon.browser.common.complement.cache;

import com.platon.browser.complement.dao.param.slash.Report;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 双签举报cdov 缓存
 */
@Component
public class ReportMultiSignParamCache {
    // <节点ID,<举报参数列表>>
    private static final Map<String, List<Report>> cache = new HashMap<>();

    public List<Report> getReportList(String nodeId){
        return cache.get(nodeId);
    }

    /**
     * 添加节点
     * @param report
     */
    public void addReport(Report report){
        List<Report> reportList = cache.get(report.getNodeId());
        if(reportList==null){
            reportList = new ArrayList<>();
            cache.put(report.getNodeId(),reportList);
        }
        reportList.add(report);
    }

    public void init(List<Report> reportList) {
        if(reportList.isEmpty()) return;
        reportList.forEach(report -> addReport(report));
    }

    /**
     * 取所有被举报节点的nodeId
     * @return
     */
    public List<String> getNodeIdList(){
        return new ArrayList<>(cache.keySet());
    }

    public void remove(String nodeId) {
        cache.remove(nodeId);
    }
}

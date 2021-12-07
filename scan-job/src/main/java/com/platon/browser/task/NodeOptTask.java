package com.platon.browser.task;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import com.platon.browser.dao.entity.NOptBak;
import com.platon.browser.dao.entity.NOptBakExample;
import com.platon.browser.dao.entity.PointLog;
import com.platon.browser.dao.mapper.NOptBakMapper;
import com.platon.browser.dao.mapper.PointLogMapper;
import com.platon.browser.service.elasticsearch.EsNodeOptService;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Component
public class NodeOptTask {

    @Resource
    private PointLogMapper pointLogMapper;

    @Resource
    private NOptBakMapper nOptBakMapper;

    @Resource
    private EsNodeOptService esNodeOptService;

    /**
     * 节点操作备份表迁移到ES任务
     *
     * @param :
     * @return: void
     * @date: 2021/12/1
     */
    @XxlJob("nodeOptMoveToESJobHandler")
    @Transactional(rollbackFor = {Exception.class, Error.class})
    public void nodeOptMoveToES() throws Exception {
        try {
            int batchSize = Convert.toInt(XxlJobHelper.getJobParam(), 10);
            PointLog pointLog = pointLogMapper.selectByPrimaryKey(1);
            NOptBakExample nOptBakExample = new NOptBakExample();
            nOptBakExample.setOrderByClause("id");
            long maxPosition = Convert.toInt(pointLog.getPosition(), 0) + batchSize;
            nOptBakExample.createCriteria().andIdGreaterThan(Convert.toLong(pointLog.getPosition())).andIdLessThanOrEqualTo(maxPosition);
            List<NOptBak> nOptBakList = nOptBakMapper.selectByExample(nOptBakExample);
            if (CollUtil.isNotEmpty(nOptBakList)) {
                Set<NOptBak> nodeOpts = new HashSet<>(nOptBakList);
                esNodeOptService.save(nodeOpts);
                NOptBak lastNOptBak = CollUtil.getLast(nOptBakList);
                pointLog.setPosition(lastNOptBak.getId().toString());
                pointLogMapper.updateByPrimaryKeySelective(pointLog);
            }
            XxlJobHelper.handleSuccess("节点操作备份表迁移到ES成功");
        } catch (Exception e) {
            log.error("节点操作备份表迁移到ES异常", e);
            throw e;
        }
    }

}

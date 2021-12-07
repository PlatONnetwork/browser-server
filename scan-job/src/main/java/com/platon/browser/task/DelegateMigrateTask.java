package com.platon.browser.task;

import com.platon.browser.bean.CustomDelegation;
import com.platon.browser.dao.entity.Delegation;
import com.platon.browser.dao.entity.DelegationExample;
import com.platon.browser.dao.mapper.DelegationMapper;
import com.platon.browser.service.elasticsearch.EsDelegationService;
import com.platon.browser.utils.AppStatusUtil;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 委托表中的历史数据迁移至ES任务
 *
 * @date: 2021/11/30
 */
@Component
@Slf4j
public class DelegateMigrateTask {

    @Resource
    private DelegationMapper delegationMapper;

    @Resource
    private EsDelegationService esDelegationService;

    @Transactional(rollbackFor = {Exception.class, Error.class})
    @XxlJob("delegateMigrateJobHandler")
    public void delegateMigrate() throws Exception {
        // 只有程序正常运行才执行任务
        if (AppStatusUtil.isRunning()) start();
    }

    protected void start() throws Exception {
        try {
            DelegationExample delegationExample = new DelegationExample();
            delegationExample.createCriteria().andIsHistoryEqualTo(CustomDelegation.YesNoEnum.YES.getCode());
            List<Delegation> delegationList = delegationMapper.selectByExample(delegationExample);
            if (delegationList.isEmpty()) return;
            Set<Delegation> delegationSet = new HashSet<>(delegationList);
            esDelegationService.save(delegationSet);
            delegationMapper.batchDeleteIsHistory(delegationList);
            XxlJobHelper.handleSuccess("委托历史迁移到ES完成");
        } catch (Exception e) {
            log.error("委托历史迁移到ES异常", e);
            throw e;
        }
    }

}

package com.platon.browser.task;

import com.platon.browser.service.elasticsearch.EsDelegationService;
import com.platon.browser.utils.AppStatusUtil;
import com.platon.browser.dao.entity.Delegation;
import com.platon.browser.dao.entity.DelegationExample;
import com.platon.browser.dao.mapper.DelegationMapper;
import com.platon.browser.bean.CustomDelegation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @Auther: dongqile
 * @Date: 2019/11/6
 * @Description: 委托表中的历史数据迁移至ES任务
 */
@Component
@Slf4j
public class DelegateMigrateTask {

    @Resource
    private DelegationMapper delegationMapper;

    @Resource
    private EsDelegationService esDelegationService;

    @Transactional(rollbackFor = {Exception.class, Error.class})
    @Scheduled(cron = "0/30  * * * * ?")
    public void delegateMigrate() {
        // 只有程序正常运行才执行任务
        if (AppStatusUtil.isRunning())
            start();
    }

    protected void start() {
        try {
            DelegationExample delegationExample = new DelegationExample();
            delegationExample.createCriteria().andIsHistoryEqualTo(CustomDelegation.YesNoEnum.YES.getCode());
            List<Delegation> delegationList = delegationMapper.selectByExample(delegationExample);
            if (delegationList.isEmpty())
                return;
            Set<Delegation> delegationSet = new HashSet<>(delegationList);
            esDelegationService.save(delegationSet);
            delegationMapper.batchDeleteIsHistory(delegationList);
            log.debug("委托历史迁移到ES完成");
        } catch (Exception e) {
            log.error("委托历史迁移到ES异常", e);
        }
    }

}

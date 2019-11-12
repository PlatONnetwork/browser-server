package com.platon.browser.task;

import com.platon.browser.common.service.elasticsearch.EsDelegationService;
import com.platon.browser.common.utils.AppStatusUtil;
import com.platon.browser.dao.entity.Delegation;
import com.platon.browser.dao.entity.DelegationExample;
import com.platon.browser.dao.mapper.DelegationMapper;
import com.platon.browser.dto.CustomDelegation;
import com.platon.browser.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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

    @Autowired
    private DelegationMapper delegationMapper;
    @Autowired
    private EsDelegationService esDelegationService;

    @Transactional
    @Scheduled(cron = "0/30  * * * * ?")
    void cron () {
        // 只有程序正常运行才执行任务
        if(AppStatusUtil.isRunning()) start();
    }

    protected void start () {
        try {
            DelegationExample delegationExample = new DelegationExample();
            delegationExample.createCriteria().andIsHistoryEqualTo(CustomDelegation.YesNoEnum.YES.getCode());
            List<Delegation> delegationList = delegationMapper.selectByExample(delegationExample);
            if(delegationList.isEmpty()) return;

            Set<Delegation> delegationSet = new HashSet<>(delegationList);
            esDelegationService.save(delegationSet);
            delegationMapper.deleteByExample(delegationExample);

            log.debug("Migrate delegate history to ElasticSearch finished!");
        } catch (Exception e) {
            String error = e.getMessage();
            log.error("{}",error);
            throw new BusinessException(error);
        }
    }
}

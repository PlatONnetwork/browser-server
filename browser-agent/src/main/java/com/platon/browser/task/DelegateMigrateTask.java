package com.platon.browser.task;

import com.platon.browser.common.service.elasticsearch.EsDelegationService;
import com.platon.browser.dao.entity.Delegation;
import com.platon.browser.dao.entity.DelegationExample;
import com.platon.browser.dao.mapper.DelegationMapper;
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

    //只查询委托历史列表
    private static final int isHistory = 1;

    @Scheduled(cron = "0/30  * * * * ?")
    private void cron () throws InterruptedException {
        start();
    }

    protected void start () throws InterruptedException {
        try {
            DelegationExample delegationExample = new DelegationExample();
            delegationExample.createCriteria().andIsHistoryEqualTo(isHistory);
            List <Delegation> delegationList = delegationMapper.selectByExample(delegationExample);
            if (delegationList.size() > 0 && null != delegationList) {
                Syn(delegationList);
            }
            log.debug("[DelegateHistorySyn Syn()] Syn delegate get to ES finish!!");
            return;
        } catch (Exception e) {
            //e.printStackTrace();
            String error = e.getMessage();
            log.error("{}",error);
            throw new BusinessException(error);
        }
    }

    @Transactional
    void Syn ( List <Delegation> list ) throws Exception {
        Set <Delegation> delegationSet = new HashSet <>(list);
        esDelegationService.save(delegationSet);
        DelegationExample delegationExample = new DelegationExample();
        delegationExample.createCriteria().andIsHistoryEqualTo(isHistory);
        delegationMapper.deleteByExample(delegationExample);
        log.debug("[DelegateHistorySyn Syn()] Syn transactional finish!!");
    }
}

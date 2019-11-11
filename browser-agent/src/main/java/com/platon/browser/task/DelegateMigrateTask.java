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


    @Scheduled(cron = "0/30  * * * * ?")
    private void cron () throws InterruptedException {
        // 只有程序正常运行才执行任务
        if(!AppStatusUtil.isRunning()) return;
        start();
    }

    protected void start () throws InterruptedException {

        try {
            DelegationExample delegationExample = new DelegationExample();
            delegationExample.createCriteria().andIsHistoryEqualTo(CustomDelegation.YesNoEnum.YES.getCode());
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
        delegationExample.createCriteria().andIsHistoryEqualTo(CustomDelegation.YesNoEnum.YES.getCode());
        delegationMapper.deleteByExample(delegationExample);
        log.debug("[DelegateHistorySyn Syn()] Syn transactional finish!!");
    }
}

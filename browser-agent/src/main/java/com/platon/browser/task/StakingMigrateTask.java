package com.platon.browser.task;

import com.platon.browser.dao.entity.Staking;
import com.platon.browser.dao.entity.StakingExample;
import com.platon.browser.dao.entity.StakingHistory;
import com.platon.browser.dao.mapper.CustomStakingHistoryMapper;
import com.platon.browser.dao.mapper.StakingMapper;
import com.platon.browser.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @Auther: dongqile
 * @Date: 2019/11/6
 * @Description: 质押表中的历史数据迁移至ES任务
 */
@Component
@Slf4j
public class StakingMigrateTask {

    @Autowired
    private StakingMapper stakingMapper;

    @Autowired
    private CustomStakingHistoryMapper customStakingHistoryMapper;

    @Scheduled(cron = "0/30  * * * * ?")
    private void cron () throws InterruptedException {
        start();
    }

    protected void start () throws InterruptedException {
        try {
            StakingExample stakingExample = new StakingExample();
            stakingExample.createCriteria().andStatusEqualTo(3);
            List <Staking> stakingList = stakingMapper.selectByExample(stakingExample);
            Set <StakingHistory> stakingHistoryList = new HashSet <>();
            if(stakingList.size() > 0){
                stakingList.forEach(staking -> {
                   StakingHistory stakingHistory = new StakingHistory();
                   BeanUtils.copyProperties(staking,stakingHistory);
                    stakingHistoryList.add(stakingHistory);
                });
                customStakingHistoryMapper.batchInsertOrUpdateSelective(stakingHistoryList, StakingHistory.Column.values());
            }
            log.debug("[StakingHistorySyn Syn()] Syn StakingHistory finish!!");
            return;
        }catch (Exception e){
            e.printStackTrace();
            String error = e.getMessage();
            log.error("{}",error);
            throw new BusinessException(error);
        }
    }

}
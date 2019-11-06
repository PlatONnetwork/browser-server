package com.platon.browser.task;

import com.platon.browser.dao.entity.Staking;
import com.platon.browser.dao.entity.StakingExample;
import com.platon.browser.dao.entity.StakingHistory;
import com.platon.browser.dao.mapper.StakingHistoryMapper;
import com.platon.browser.dao.mapper.StakingMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: dongqile
 * @Date: 2019/11/6
 * @Description: 定时同步质押历史数据至质押历史表
 */
@Component
@Slf4j
public class StakingHistorySyn {

    @Autowired
    private StakingMapper stakingMapper;

    @Autowired
    private StakingHistoryMapper stakingHistoryMapper;

    @Scheduled(cron = "0/5  * * * * ?")
    private void cron () throws InterruptedException {
        start();
    }

    protected void start () throws InterruptedException {
        try {
            StakingExample stakingExample = new StakingExample();
            stakingExample.createCriteria().andStatusEqualTo(3);
            List <Staking> stakingList = stakingMapper.selectByExample(stakingExample);
            if(stakingList.size() > 0){
                List <StakingHistory> stakingHistoryList = new ArrayList <>();
                BeanUtils.copyProperties(stakingList,stakingHistoryList);
                stakingHistoryMapper.batchInsert(stakingHistoryList);
            }
            log.info("[StakingHistorySyn] ");
            return;
        }catch (Exception e){
            log.error("",e.getMessage());
        }
    }

}
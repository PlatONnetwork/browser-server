package com.platon.browser.task;

import com.platon.browser.dao.custommapper.CustomStakingHistoryMapper;
import com.platon.browser.dao.entity.Staking;
import com.platon.browser.dao.entity.StakingExample;
import com.platon.browser.dao.entity.StakingHistory;
import com.platon.browser.dao.mapper.StakingMapper;
import com.platon.browser.utils.AppStatusUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @Auther: dongqile
 * @Date: 2019/11/6
 * @Description: 质押表中的历史数据迁移至数据库任务
 */
@Component
@Slf4j
public class StakingMigrateTask {

    @Resource
    private StakingMapper stakingMapper;

    @Resource
    private CustomStakingHistoryMapper customStakingHistoryMapper;

    @Scheduled(cron = "0/30  * * * * ?")
    void stakingMigrate() {
        // 只有程序正常运行才执行任务
        if (AppStatusUtil.isRunning())
            start();
    }

    protected void start() {
        try {
            StakingExample stakingExample = new StakingExample();
            stakingExample.createCriteria().andStatusEqualTo(3);
            List<Staking> stakingList = stakingMapper.selectByExample(stakingExample);
            Set<StakingHistory> stakingHistoryList = new HashSet<>();
            if (!stakingList.isEmpty()) {
                stakingList.forEach(staking -> {
                    StakingHistory stakingHistory = new StakingHistory();
                    BeanUtils.copyProperties(staking, stakingHistory);
                    stakingHistoryList.add(stakingHistory);
                });
                customStakingHistoryMapper.batchInsertOrUpdateSelective(stakingHistoryList, StakingHistory.Column.values());
            }
            log.debug("[StakingHistorySyn Syn()] Syn StakingHistory finish!!");
        } catch (Exception e) {
            log.error("", e);
        }
    }

}
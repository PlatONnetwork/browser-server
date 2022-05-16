package com.platon.browser.task;

import com.platon.browser.dao.custommapper.CustomStakingHistoryMapper;
import com.platon.browser.dao.entity.Staking;
import com.platon.browser.dao.entity.StakingExample;
import com.platon.browser.dao.entity.StakingHistory;
import com.platon.browser.dao.mapper.StakingMapper;
import com.platon.browser.utils.AppStatusUtil;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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

    /**
     * 质押表中的历史数据迁移至数据库任务
     * 每30秒执行一次
     *
     * @param :
     * @return: void
     * @date: 2021/12/15
     */
    @XxlJob("stakingMigrateJobHandler")
    @Transactional(rollbackFor = {Exception.class, Error.class})
    void stakingMigrate() {
        // 只有程序正常运行才执行任务
        if (AppStatusUtil.isRunning()) start();
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
            XxlJobHelper.handleSuccess("质押表中的历史数据迁移至数据库任务成功");
        } catch (Exception e) {
            log.error("质押表中的历史数据迁移至数据库任务异常", e);
            throw e;
        }
    }

}
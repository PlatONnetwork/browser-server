package com.platon.browser.task;

import com.platon.browser.dao.custommapper.CustomStakingHistoryMapper;
import com.platon.browser.utils.AppStatusUtil;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @Auther: dongqile
 * @Date: 2019/11/6
 * @Description: 质押表中的历史数据迁移至数据库任务
 */
@Component
@Slf4j
public class StakingMigrateTask {

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
            /**
             *  把staking表中，已经退出的质押（status=3)的记录，备份到staking_history表中，然后从staking表中删除
             */
            customStakingHistoryMapper.backupQuitedStaking();
            XxlJobHelper.handleSuccess("已经退出的质押记录迁移至历史表成功");
        } catch (Exception e) {
            log.error("质押表中的历史数据迁移至数据库任务异常", e);
            throw e;
        }
    }

}

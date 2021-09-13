package com.platon.browser.task;

import com.platon.browser.client.PlatOnClient;
import com.platon.browser.config.DisruptorConfig;
import com.platon.browser.config.TaskConfig;
import com.platon.browser.handler.PersistenceEventHandler;
import com.platon.browser.utils.AppStatusUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Auther: chendongming@matrixelements.com
 * @Date: 2019/11/16
 * @Description: 批次处理相关变量动态调整任务
 */
@Component
@Slf4j
public class BatchSizeAdjustTask {

    @Resource
    private PlatOnClient platOnClient;

    @Resource
    private PersistenceEventHandler persistenceEventHandler;

    @Resource
    private DisruptorConfig disruptorConfig;

    @Resource
    private TaskConfig taskConfig;

    @Scheduled(cron = "0/30 * * * * ?")
    public void batchSizeAdjust() {
        // 只有程序正常运行才执行任务
        if (AppStatusUtil.isRunning())
            start();
    }

    protected void start() {
        try {
            long chainBlockNumber = platOnClient.getLatestBlockNumber().longValue();
            long appBlockNumber = persistenceEventHandler.getMaxBlockNumber();
            if (chainBlockNumber - appBlockNumber < taskConfig.getGapForAdjust()) {
                log.info("---------------已追上链,调整批量大小为{}---------------", taskConfig.getEsRedisCatchupBatchSize());
                disruptorConfig.setPersistenceBatchSize(taskConfig.getEsRedisCatchupBatchSize());
            } else {
                log.info("---------------未追上链,调整批量大小为{}---------------", taskConfig.getEsRedisNotCatchupBatchSize());
                disruptorConfig.setPersistenceBatchSize(taskConfig.getEsRedisNotCatchupBatchSize());
            }
        } catch (Exception e) {
            log.error("批次处理相关变量动态调整出错:", e);
        }
    }

}

package com.platon.browser.task;

import com.platon.browser.client.PlatOnClient;
import com.platon.browser.common.utils.AppStatusUtil;
import com.platon.browser.persistence.queue.handler.PersistenceEventHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @Auther: chendongming@juzix.net
 * @Date: 2019/11/16
 * @Description: 批次处理相关变量动态调整任务
 */
@Component
@Slf4j
public class BatchVariableDynamicAdjustTask {

    @Autowired
    private PlatOnClient platOnClient;
    @Autowired
    private PersistenceEventHandler persistenceEventHandler;

    @Scheduled(cron = "0/30 * * * * ?")
    public void cron () {
        // 只有程序正常运行才执行任务
        if(AppStatusUtil.isRunning()) start();
    }

    protected void start () {
        try {
            long chainBlockNumber = platOnClient.getLatestBlockNumber().longValue();
            long appBlockNumber = persistenceEventHandler.getMaxBlockNumber();
            if(chainBlockNumber-appBlockNumber<10) {
                log.info("-----------------------------------------已追上链,调整批量大小为1-----------------------------------------");
                persistenceEventHandler.setBatchSize(1);
            }
            if(chainBlockNumber-appBlockNumber>=10) {
                log.info("-----------------------------------------未追上链,调整批量大小为10-----------------------------------------");
                persistenceEventHandler.setBatchSize(10);
            }
        } catch (Exception e) {
            log.error("批次处理相关变量动态调整出错:",e);
        }
    }
}

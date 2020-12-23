package com.platon.browser.task;

import com.platon.browser.client.PlatOnClient;
import com.platon.browser.common.utils.AppStatusUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @Auther: chendongming@matrixelements.com
 * @Date: 2019/11/16
 * @Description: web3j实例存活监控任务
 */
@Component
@Slf4j
public class PlatOnClientMonitorTask {

    @Autowired
    private PlatOnClient platOnClient;

    @Scheduled(cron = "0/10 * * * * ?")
    public void cron () {
        // 只有程序正常运行才执行任务
        if(AppStatusUtil.isRunning()) start();
    }

    protected void start () {
        try {
            platOnClient.updateCurrentWeb3jWrapper();
        } catch (Exception e) {
            log.error("detect exception:{}", e);
        }
    }
}

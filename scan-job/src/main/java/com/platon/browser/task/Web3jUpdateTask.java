package com.platon.browser.task;

import com.platon.browser.client.PlatOnClient;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;

//2023/03/31 lvxiaoyi 此任务不需要了,PlatOnClient有回调函数更新可用web3jWrapper
@Deprecated
//@Component
@Slf4j
public class Web3jUpdateTask {

    @Resource
    private PlatOnClient platOnClient;

    //@Scheduled(cron = "0/10 * * * * ?")
    public void cron() {
        try {
            platOnClient.updateCurrentWeb3jWrapper();
        } catch (Exception e) {
            log.error("detect exception:{}", e);
        }
    }

}

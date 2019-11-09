package com.platon.browser.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @Auther: dongqile
 * @Date: 2019/11/6
 * @Description: TODO: 提案相关信息更新任务
 *  keybase获取节点头像
 */
@Component
@Slf4j
public class ProposalUpdateTask {


    @Scheduled(cron = "0/30  * * * * ?")
    private void cron () throws InterruptedException {

    }

}

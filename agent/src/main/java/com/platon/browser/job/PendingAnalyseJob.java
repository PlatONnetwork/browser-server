package com.platon.browser.job;

import com.platon.browser.filter.PendingFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * User: dongqile
 * Date: 2018/10/25
 * Time: 18:07
 */
@Component
public class PendingAnalyseJob {

    /**
     * 挂起交易同步任务
     * 1.根据web3j配置文件获取节点信息
     * 2.构建web3jclient
     * 3.同步链上挂起交易列表
     * 4.数据整合推送至rabbitMQ队列
     */

    private static Logger log = LoggerFactory.getLogger(PendingAnalyseJob.class);

    @Value("${chain.id}")
    private String chainId;

    @Autowired
    private PendingFilter pendingFilter;

    @Scheduled(cron = "0/1 * * * * ?")
    protected void doJob() {
        log.debug("**************PendingTx Analysis start ***************");
        try {
            pendingFilter.analysis();
            log.debug("**************PendingTx Analysis end ***************");
        } catch (Exception e) {
            log.error("PendingTxSynchronizeJob Exception", e.getMessage());
        }
    }
}

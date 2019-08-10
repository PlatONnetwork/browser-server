package com.platon.browser.job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * User: dongqile
 * Date: 2019/1/22
 * Time: 11:55
 */
@Component
public class Web3DetectJob {
    private static Logger logger = LoggerFactory.getLogger(Web3DetectJob.class);




    /**
     * 同步节点web3j探测任务
     */
    @Scheduled(cron = "0/30 * * * * ?")
    protected void analyseNode () {
        logger.debug("*** In the Web3DetectJob *** ");
        try {
           // chainsConfig.updateValidWeb3jList();
        } catch (Exception e) {
            logger.error("Web3DetectJob Exception:{}", e.getMessage());
            e.printStackTrace();
        }
        logger.debug("*** End the Web3DetectJob *** ");
    }


}
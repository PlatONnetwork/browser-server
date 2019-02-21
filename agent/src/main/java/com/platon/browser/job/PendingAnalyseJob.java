package com.platon.browser.job;

import com.platon.browser.client.PlatonClient;
import com.platon.browser.filter.PendingFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.web3j.protocol.core.methods.response.EthPendingTransactions;
import org.web3j.protocol.core.methods.response.Transaction;

import java.util.List;

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

    private static Logger logger = LoggerFactory.getLogger(PendingAnalyseJob.class);
    @Autowired
    private PendingFilter pendingFilter;

    @Autowired
    private PlatonClient platon;

    @Value("${platon.chain.active}")
    private String chainId;

    /**
     * 分析待处理交易
     */
    @Scheduled(cron = "0/1 * * * * ?")
    protected void analysePending() {

        logger.debug("*** In the PendingAnalyseJob *** ");
        try {
            EthPendingTransactions ethPendingTransactions = platon.getWeb3j(chainId).ethPendingTx().send();
            List<Transaction> transactions = ethPendingTransactions.getTransactions();
            pendingFilter.analyse(transactions ,chainId ,platon);
            logger.debug("**************PendingTx Analysis end ***************");
        } catch (Exception e) {
            logger.error("PendingTxSynchronizeJob Exception:{}", e.getMessage());
        }
        logger.debug("*** End the PendingAnalyseJob *** ");
    }
}

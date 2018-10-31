package com.platon.browser.agent.job;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.platon.browser.common.client.Web3jClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StopWatch;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.Transaction;
import rx.Observable;

/**
 * User: dongqile
 * Date: 2018/10/25
 * Time: 18:07
 */
public class PendingTxSynchrinizeJob extends AbstractTaskJob{

    /**
     * 挂起交易同步任务
     * 1.根据web3j配置文件获取节点信息
     * 2.构建web3jclient
     *
     */

    private static Logger log = LoggerFactory.getLogger(PendingTxSynchrinizeJob.class);


    @Override
    protected void doJob ( ShardingContext shardingContext ) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        try {
            Web3j web3j = Web3jClient.getWeb3jClient();
        }catch (Exception e){
            log.error(e.getMessage());
        } finally {
            stopWatch.stop();
            log.info("PendingTxSynchrinizeJob-->{}", stopWatch.shortSummary());
        }
    }
}
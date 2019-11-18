package com.platon.browser.client;

import com.alibaba.fastjson.JSON;
import com.platon.browser.exception.ConfigLoadingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.web3j.platon.bean.EconomicConfig;
import org.web3j.platon.bean.Node;
import org.web3j.platon.contracts.*;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.math.BigInteger;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * User: dongqile
 * Date: 2018/10/25
 * Time: 14:42
 */
@Slf4j
@Component
public class PlatOnClient {
    // 交易输入参数并行解码线程池
    static ExecutorService LOG_DECODE_EXECUTOR;

    // 交易输入参数并行解码线程数
    @Value("${platon.txLogDecodeThreadNum}")
    private int logDecodeThreadNum;
    @Autowired
    private RetryableClient retryableClient;

    @Autowired
    private SpecialApi specialApi;
    public DelegateContract getDelegateContract(){return retryableClient.getDelegateContract();}
    public NodeContract getNodeContract(){return retryableClient.getNodeContract();}
    public ProposalContract getProposalContract(){return retryableClient.getProposalContract();}
    public RestrictingPlanContract getRestrictingPlanContract(){return retryableClient.getRestrictingPlanContract();}
    public SlashContract getSlashContract(){return retryableClient.getSlashContract();}
    public StakingContract getStakingContract(){return retryableClient.getStakingContract();}

    @PostConstruct
    private void init() throws ConfigLoadingException {
        LOG_DECODE_EXECUTOR=Executors.newFixedThreadPool(logDecodeThreadNum);
        retryableClient.init();
    }

    public void updateCurrentWeb3jWrapper(){
        retryableClient.updateCurrentWeb3jWrapper();
    }

    public Web3jWrapper getWeb3jWrapper(){
        return retryableClient.getWeb3jWrapper();
    }

    public ReceiptResult getReceiptResult(Long blockNumber) throws IOException, InterruptedException {
        ReceiptResult receiptResult = specialApi.getReceiptResult(retryableClient.getWeb3jWrapper(),BigInteger.valueOf(blockNumber));
        receiptResult.resolve(blockNumber);
        return receiptResult;
    }

    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE,backoff=@Backoff(value=3000L))
    public EconomicConfig getEconomicConfig() throws Exception {
        try {
            EconomicConfig ec = retryableClient.getWeb3jWrapper().getWeb3j().getEconomicConfig().send().getEconomicConfig();
            String msg = JSON.toJSONString(ec,true);
            log.info("链上配置:{}",msg);
            return ec;
        } catch (Exception e) {
            retryableClient.updateCurrentWeb3jWrapper();
            log.error("获取链上配置出错({}),将重试!", e.getMessage());
            throw e;
        }
    }

    public BigInteger getLatestBlockNumber() throws IOException {
        return retryableClient.getWeb3jWrapper().getWeb3j().platonBlockNumber().send().getBlockNumber();
    }

    public List<Node> getLatestValidators() throws Exception {
        return getNodeContract().getValidatorList().send().data;
    }

    public List<Node> getLatestVerifiers() throws Exception {
        return getNodeContract().getVerifierList().send().data;
    }
}

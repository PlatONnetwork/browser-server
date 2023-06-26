package com.platon.browser.client;

import com.alibaba.fastjson.JSON;
import com.platon.browser.bean.ReceiptResult;
import com.platon.browser.exception.BusinessException;
import com.platon.browser.exception.ConfigLoadingException;
import com.platon.contracts.ppos.*;
import com.platon.contracts.ppos.dto.resp.GovernParam;
import com.platon.contracts.ppos.dto.resp.Node;
import com.platon.protocol.core.DefaultBlockParameterName;
import com.platon.protocol.core.methods.response.bean.EconomicConfig;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
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
    @Getter
    @Setter
    private ExecutorService logDecodeExecutor;

    // 交易输入参数并行解码线程数
    @Value("${platon.txLogDecodeThreadNum}")
    private int logDecodeThreadNum;

    @Resource
    private RetryableClient retryableClient;

    @Resource
    private SpecialApi specialApi;

    public DelegateContract getDelegateContract() {
        return retryableClient.getDelegateContract();
    }

    public NodeContract getNodeContract() {
        return retryableClient.getNodeContract();
    }

    public ProposalContract getProposalContract() {
        return retryableClient.getProposalContract();
    }

    public RestrictingPlanContract getRestrictingPlanContract() {
        return retryableClient.getRestrictingPlanContract();
    }

    public SlashContract getSlashContract() {
        return retryableClient.getSlashContract();
    }

    public StakingContract getStakingContract() {
        return retryableClient.getStakingContract();
    }

    public RewardContract getRewardContract() {
        return retryableClient.getRewardContract();
    }

    @PostConstruct
    private void init() throws ConfigLoadingException {
        logDecodeExecutor = Executors.newFixedThreadPool(logDecodeThreadNum);
        retryableClient.init();
    }

    public void updateCurrentWeb3jWrapper() {
        retryableClient.updateCurrentWeb3jWrapper();
    }

    public Web3jWrapper getWeb3jWrapper() {
        return retryableClient.getWeb3jWrapper();
    }

    public ReceiptResult getReceiptResult(Long blockNumber) throws IOException, InterruptedException {
        ReceiptResult receiptResult = specialApi.getReceiptResult(retryableClient.getWeb3jWrapper(), BigInteger.valueOf(blockNumber));
        receiptResult.resolve(blockNumber, logDecodeExecutor);
        return receiptResult;
    }

    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE, backoff = @Backoff(value = 3000L))
    public EconomicConfig getEconomicConfig() throws ConfigLoadingException {
        try {
            EconomicConfig ec = retryableClient.getWeb3jWrapper().getWeb3j().getEconomicConfig().send().getEconomicConfig();
            String msg = JSON.toJSONString(ec);
            log.info("链上配置:{}", msg);
            return ec;
        } catch (Exception e) {
            retryableClient.updateCurrentWeb3jWrapper();
            log.error("获取链上配置出错({}),将重试!", e.getMessage());
            throw new ConfigLoadingException(e.getMessage());
        }
    }

    public void updateContract() {
        retryableClient.updateContract();
    }

    public BigInteger getLatestBlockNumber() throws IOException {
        return retryableClient.getWeb3jWrapper().getWeb3j().platonBlockNumber().send().getBlockNumber();
    }

    @Cacheable("addressCodeFromChain")
    public String getAddressCode(String address) throws IOException {
        return retryableClient.getWeb3jWrapper().getWeb3j().platonGetCode(address, DefaultBlockParameterName.LATEST).send().getCode();
    }

    public List<Node> getLatestValidators() {
        try {
            return getNodeContract().getValidatorList().send().getData();
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }
    }

    public List<Node> getLatestVerifiers() {
        try {
            return getNodeContract().getVerifierList().send().getData();
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }
    }

    public List<GovernParam> getGovernParamValue(String module) {
        try {
            return getProposalContract().getParamList(module).send().getData();
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }
    }

}

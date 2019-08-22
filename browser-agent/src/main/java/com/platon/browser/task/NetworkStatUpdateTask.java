package com.platon.browser.task;

import com.platon.browser.client.PlatonClient;
import com.platon.browser.engine.BlockChain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.web3j.platon.contracts.RestrictingPlanContract;
import org.web3j.platon.contracts.StakingContract;
import org.web3j.protocol.core.DefaultBlockParameter;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map;

/**
 * User: dongqile
 * Date: 2019/8/20
 * Time: 10:08
 */
@Component
public class NetworkStatUpdateTask {
    private static Logger logger = LoggerFactory.getLogger(NetworkStatUpdateTask.class);
    @Autowired
    private BlockChain blockChain;
    @Autowired
    private PlatonClient platonClient;

    @Scheduled(cron = "0/1 * * * * ?")
    protected void start () {
        try {
            //从配置文件中获取到每个增发周期对应的基金会补充金额
            Map <Integer,BigDecimal> foundationSubsidiesMap = blockChain.getChainConfig().getFoundationSubsidies();
            //判断当前为哪一个增发周期，获取当前增发周期基金会补充的金额
            BigDecimal foundationValue = foundationSubsidiesMap.get(blockChain.getAddIssueEpoch().toString());
            //获取初始发行金额
            BigDecimal iniValue = blockChain.getChainConfig().getInitIssueAmount();
            //获取增发比例
            BigDecimal addIssueRate = blockChain.getChainConfig().getAddIssueRate();
            //获取激励池地址
            String developerIncentiveFundAccountAddr = blockChain.getChainConfig().getDeveloperIncentiveFundAccountAddr();
            //rpc查询实时激励池余额
            BigInteger incentivePoolAccountBalance = platonClient.getWeb3j().platonGetBalance(developerIncentiveFundAccountAddr,
                    DefaultBlockParameter.valueOf(BigInteger.valueOf(blockChain.getCurBlock().getBlockNumber().longValue()))).send().getBalance();
            //年份增发量 = (1+增发比例)的增发年份次方
            BigDecimal circulationByYear = BigDecimal.ONE.add(addIssueRate).pow(blockChain.getAddIssueEpoch().intValue());
            //计算发行量 = 初始发行量 * 年份增发量 - 实时激励池余额 + 第N年基金会补发量
            BigDecimal circulation = iniValue.multiply(circulationByYear).subtract(new BigDecimal(incentivePoolAccountBalance)).add(foundationValue);

            //rpc获取锁仓余额
            BigInteger lockContractBalance = platonClient.getWeb3j().platonGetBalance(RestrictingPlanContract.RESTRICTING_PLAN_CONTRACT_ADDRESS,
                    DefaultBlockParameter.valueOf(BigInteger.valueOf(blockChain.getCurBlock().getBlockNumber().longValue()))).send().getBalance();
            //rpc获取质押余额
            BigInteger stakingContractBalance = platonClient.getWeb3j().platonGetBalance(StakingContract.STAKING_CONTRACT_ADDRESS,
                    DefaultBlockParameter.valueOf(BigInteger.valueOf(blockChain.getCurBlock().getBlockNumber().longValue()))).send().getBalance();

            //计算流通量
            BigDecimal turnoverValue = circulation.add(new BigDecimal(lockContractBalance)).add(new BigDecimal(stakingContractBalance));

            //数据回填内存中
            blockChain.NETWORK_STAT_CACHE.setIssueValue(circulation.toString());
            blockChain.NETWORK_STAT_CACHE.setTurnValue(turnoverValue.toString());
            blockChain.exportResult().getNetworkStatResult().stageUpdateNetworkStat(blockChain.NETWORK_STAT_CACHE);
        }catch (Exception e){
           logger.error("[CirculationAndTurnoverSyn] syn balance exception {}",e.getMessage());
        }

    }

}

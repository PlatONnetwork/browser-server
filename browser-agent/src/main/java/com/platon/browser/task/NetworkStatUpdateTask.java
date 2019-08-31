package com.platon.browser.task;

import com.platon.browser.client.PlatonClient;
import com.platon.browser.dto.CustomBlock;
import com.platon.browser.engine.BlockChain;
import com.platon.browser.enums.InnerContractAddrEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.web3j.platon.contracts.RestrictingPlanContract;
import org.web3j.platon.contracts.StakingContract;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.utils.Convert;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map;

import static com.platon.browser.engine.BlockChain.STAGE_DATA;

/**
 * @Auther: dongqile
 * @Date: 2019/8/17 20:09
 * @Description: 区块链统计信息更新任务
 */
@Component
public class NetworkStatUpdateTask {
    private static Logger logger = LoggerFactory.getLogger(NetworkStatUpdateTask.class);
    @Autowired
    private BlockChain blockChain;
    @Autowired
    private PlatonClient platonClient;

    @Scheduled(cron = "0/5  * * * * ?")
    protected void start () {
        CustomBlock curBlock = blockChain.getCurBlock();
        if(curBlock==null) return;
        try {
            //从配置文件中获取到每个增发周期对应的基金会补充金额
            Map <Integer, BigDecimal> foundationSubsidiesMap = blockChain.getChainConfig().getFoundationSubsidies();
            //判断当前为哪一个增发周期，获取当前增发周期基金会补充的金额
            BigDecimal foundationValue = foundationSubsidiesMap.get(blockChain.getAddIssueEpoch().intValue());
            //获取初始发行金额
            BigDecimal iniValue = blockChain.getChainConfig().getInitIssueAmount();
            BigDecimal iniValueVon = Convert.toVon(iniValue, Convert.Unit.LAT);
            //获取增发比例
            BigDecimal addIssueRate = blockChain.getChainConfig().getAddIssueRate();
            //获取激励池地址
            String incentivePoolAccountAddr = InnerContractAddrEnum.INCENTIVE_POOL_CONTRACT.address;
            //rpc查询实时激励池余额
            BigInteger incentivePoolAccountBalance = platonClient.getWeb3j().platonGetBalance(incentivePoolAccountAddr,
                    DefaultBlockParameter.valueOf(BigInteger.valueOf(curBlock.getBlockNumber().longValue()))).send().getBalance();
            //年份增发量 = (1+增发比例)的增发年份次方
            BigDecimal circulationByYear = BigDecimal.ONE.add(addIssueRate).pow(blockChain.getAddIssueEpoch().intValue());
            //计算发行量 = 初始发行量 * 年份增发量 - 实时激励池余额 + 第N年基金会补发量
            BigDecimal circulation = iniValueVon.multiply(circulationByYear).subtract(new BigDecimal(incentivePoolAccountBalance)).add(foundationValue == null ? BigDecimal.ZERO : foundationValue);
            //rpc获取锁仓余额
            BigInteger lockContractBalance = platonClient.getWeb3j().platonGetBalance(InnerContractAddrEnum.RESTRICTING_PLAN_CONTRACT.address,
                    DefaultBlockParameter.valueOf(BigInteger.valueOf(blockChain.getCurBlock().getBlockNumber().longValue()))).send().getBalance();
            //rpc获取质押余额
            BigInteger stakingContractBalance = platonClient.getWeb3j().platonGetBalance(InnerContractAddrEnum.STAKING_CONTRACT.address,
                    DefaultBlockParameter.valueOf(BigInteger.valueOf(blockChain.getCurBlock().getBlockNumber().longValue()))).send().getBalance();

            //计算流通量
            BigDecimal turnoverValue = circulation.subtract(new BigDecimal(lockContractBalance)).subtract(new BigDecimal(stakingContractBalance)).subtract(new BigDecimal(incentivePoolAccountBalance));

            //数据回填内存中
            blockChain.NETWORK_STAT_CACHE.setIssueValue(circulation.setScale(0,BigDecimal.ROUND_DOWN).toString());
            blockChain.NETWORK_STAT_CACHE.setTurnValue(turnoverValue.setScale(0,BigDecimal.ROUND_DOWN).toString());
            STAGE_DATA.getNetworkStatStage().updateNetworkStat(blockChain.NETWORK_STAT_CACHE);
        } catch (Exception e) {
            logger.error("计算发行量和流通量出错:{}", e.getMessage());
        }
    }
}

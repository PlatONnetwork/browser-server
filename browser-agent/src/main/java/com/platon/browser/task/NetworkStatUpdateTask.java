package com.platon.browser.task;

import com.platon.browser.client.PlatOnClient;
import com.platon.browser.config.BlockChainConfig;
import com.platon.browser.dao.entity.NetworkStat;
import com.platon.browser.dao.mapper.CustomNetworkStatMapper;
import com.platon.browser.dto.CustomBlock;
import com.platon.browser.dto.CustomNetworkStat;
import com.platon.browser.engine.BlockChain;
import com.platon.browser.engine.cache.CacheHolder;
import com.platon.browser.engine.stage.NetworkStatStage;
import com.platon.browser.enums.InnerContractAddrEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.utils.Convert;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.Map;

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
    private BlockChainConfig chainConfig;
    @Autowired
    private PlatOnClient client;
    @Autowired
    private CacheHolder cacheHolder;
    @Autowired
    private CustomNetworkStatMapper customNetworkStatMapper;

    private NetworkStatStage networkStatStage = new NetworkStatStage();

    @Scheduled(cron = "0/5  * * * * ?")
    private void cron(){start();}

    protected void start () {
        CustomNetworkStat networkStatCache = cacheHolder.getNetworkStatCache();

        CustomBlock curBlock = blockChain.getCurBlock();
        BlockChainConfig blockChainConfig = blockChain.getChainConfig();
        BigInteger addIssueEpoch = blockChain.getAddIssueEpoch();
        if(curBlock==null) return;
        try {
            //从配置文件中获取到每个增发周期对应的基金会补充金额
            Map <Integer, BigDecimal> foundationSubsidiesMap = blockChainConfig.getFoundationSubsidies();
            //判断当前为哪一个增发周期，获取当前增发周期基金会补充的金额
            BigDecimal foundationValueLAT = foundationSubsidiesMap.get(addIssueEpoch.intValue());
            BigDecimal foundationValueVon = Convert.toVon(foundationValueLAT,Convert.Unit.LAT);
            //获取初始发行金额
            BigDecimal iniValue = blockChainConfig.getInitIssueAmount();
            BigDecimal iniValueVon = Convert.toVon(iniValue, Convert.Unit.LAT);
            //获取增发比例
            BigDecimal addIssueRate = blockChainConfig.getAddIssueRate();
            //获取激励池地址
            String incentivePoolAccountAddr = InnerContractAddrEnum.INCENTIVE_POOL_CONTRACT.getAddress();
            //rpc查询实时激励池余额
            BigInteger incentivePoolAccountBalance = getBalance(incentivePoolAccountAddr,curBlock.getBlockNumber());
            //年份增发量 = (1+增发比例)的增发年份次方
            BigDecimal circulationByYear = BigDecimal.ONE.add(addIssueRate).pow(addIssueEpoch.intValue());
            //计算发行量 = 初始发行量 * 年份增发量 - 实时激励池余额 + 第N年基金会补发量
            BigDecimal circulation = iniValueVon.multiply(circulationByYear).subtract(new BigDecimal(incentivePoolAccountBalance)).add(foundationValueVon == null ? BigDecimal.ZERO : foundationValueVon);
            //rpc获取锁仓余额
            BigInteger lockContractBalance = getBalance(InnerContractAddrEnum.RESTRICTING_PLAN_CONTRACT.getAddress(),curBlock.getBlockNumber());
            //rpc获取质押余额
            BigInteger stakingContractBalance = getBalance(InnerContractAddrEnum.STAKING_CONTRACT.getAddress(),curBlock.getBlockNumber());
            //计算流通量
            BigDecimal turnoverValue = circulation.subtract(new BigDecimal(lockContractBalance)).subtract(new BigDecimal(stakingContractBalance)).subtract(new BigDecimal(incentivePoolAccountBalance));
            //数据回填内存中
            networkStatCache.setIssueValue(circulation.setScale(0, RoundingMode.FLOOR).toString());
            networkStatCache.setTurnValue(turnoverValue.setScale(0,RoundingMode.FLOOR).toString());
            networkStatStage.updateNetworkStat(networkStatCache);
            customNetworkStatMapper.batchInsertOrUpdateSelective(networkStatStage.exportNetworkStat(), NetworkStat.Column.values());
            networkStatStage.clear();
        } catch (Exception e) {
            logger.error("计算发行量和流通量出错:{}", e.getMessage());
        }
    }

    /**
     * 根据地址和区块号取余额
     * @param address
     * @param blockNumber
     * @return
     * @throws IOException
     */
    public BigInteger getBalance(String address,BigInteger blockNumber) throws IOException {
        return client.getWeb3j().platonGetBalance(address,DefaultBlockParameter.valueOf(BigInteger.valueOf(blockNumber.longValue()))).send().getBalance();
    }
}

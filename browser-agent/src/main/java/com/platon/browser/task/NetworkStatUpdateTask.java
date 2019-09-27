package com.platon.browser.task;

import com.platon.browser.client.PlatOnClient;
import com.platon.browser.config.BlockChainConfig;
import com.platon.browser.engine.BlockChain;
import com.platon.browser.enums.InnerContractAddrEnum;
import com.platon.browser.exception.GracefullyShutdownException;
import com.platon.browser.task.bean.TaskNetworkStat;
import com.platon.browser.task.cache.NetworkStatTaskCache;
import com.platon.browser.util.GracefullyUtil;
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
import java.util.concurrent.TimeUnit;

/**
 * @Auther: dongqile
 * @Date: 2019/8/17 20:09
 * @Description: 区块链统计信息更新任务
 */
@Component
public class NetworkStatUpdateTask {
    private static Logger logger = LoggerFactory.getLogger(NetworkStatUpdateTask.class);
    @Autowired
    private BlockChain bc;
    @Autowired
    private BlockChainConfig chainConfig;
    @Autowired
    private PlatOnClient client;
    @Autowired
    private NetworkStatTaskCache taskCache;


    @Scheduled(cron = "0/5  * * * * ?")
    private void cron(){start();}

    protected void start () {
        if(bc.getCurBlock()==null) return;
        try {
            TaskNetworkStat cache = taskCache.get();
            BigInteger blockNumber = bc.getCurBlock().getBlockNumber();

            //从配置文件中获取到每个增发周期对应的基金会补充金额
            Map<Integer, BigDecimal> subsidiesMap = chainConfig.getFoundationSubsidies();
            int curIssueEpoch=bc.getAddIssueEpoch().intValue();
            int subsidiesSize=subsidiesMap.size();

            // 基金会补贴部分
            BigDecimal foundationAmount = BigDecimal.ZERO;
            // 前subsidiesSize年才有补贴，其余时候为0
            if(curIssueEpoch<=subsidiesSize) foundationAmount = subsidiesMap.get(curIssueEpoch);

            //获取初始发行金额
            BigDecimal initIssueAmount = chainConfig.getInitIssueAmount();
            initIssueAmount = Convert.toVon(initIssueAmount, Convert.Unit.LAT);
            //获取增发比例
            BigDecimal addIssueRate = chainConfig.getAddIssueRate();

            //rpc查询实时激励池余额
            BigInteger incentivePoolAccountBalance = getBalance(InnerContractAddrEnum.INCENTIVE_POOL_CONTRACT.getAddress(),blockNumber);

            //年份增发量 = (1+增发比例)的增发年份次方
            BigDecimal circulationByYear = BigDecimal.ONE.add(addIssueRate).pow(curIssueEpoch);
            //计算发行量 = 初始发行量 * 年份增发量 - 实时激励池余额 + 第N年基金会补贴
            // 发行量
            BigDecimal circulation = initIssueAmount
                    .multiply(circulationByYear)
                    .subtract(new BigDecimal(incentivePoolAccountBalance))
                    .add(foundationAmount);

            //rpc获取锁仓余额
            BigInteger restrictingContractBalance = getBalance(InnerContractAddrEnum.RESTRICTING_PLAN_CONTRACT.getAddress(), bc.getCurBlock().getBlockNumber());
            //rpc获取质押余额
            BigInteger stakingContractBalance = getBalance(InnerContractAddrEnum.STAKING_CONTRACT.getAddress(), bc.getCurBlock().getBlockNumber());
            //计算流通量
            BigDecimal turnoverValue = circulation
                    .subtract(new BigDecimal(restrictingContractBalance))
                    .subtract(new BigDecimal(stakingContractBalance))
                    .subtract(new BigDecimal(incentivePoolAccountBalance));
            //数据回填内存中
            cache.setIssueValue(circulation.setScale(0, RoundingMode.FLOOR).toString());
            cache.setTurnValue(turnoverValue.setScale(0,RoundingMode.FLOOR).toString());
            cache.setCurrentNumber(blockNumber.longValue());
            cache.setMerged(false);
        } catch (Exception e) {
            logger.error("计算发行量和流通量出错:{}", e.getMessage());
        }
        try {
            GracefullyUtil.monitor();
        } catch (GracefullyShutdownException e) {
            Thread.currentThread().interrupt();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据地址和区块号取余额
     * @param address
     * @param blockNumber
     * @return
     * @throws IOException
     */
    public BigInteger getBalance(String address,BigInteger blockNumber) throws InterruptedException {
        while (true)try {
            return client.getWeb3j().platonGetBalance(address,DefaultBlockParameter.valueOf(BigInteger.valueOf(blockNumber.longValue()))).send().getBalance();
        }catch (Exception e){
            logger.error("查询地址[{}]在区块[{}]的余额失败,将重试:{}",address,blockNumber,e);
            TimeUnit.SECONDS.sleep(1L);
        }
    }
}

package com.platon.browser.common.service.erc;

import com.platon.browser.dao.entity.Erc20Token;
import com.platon.browser.dao.mapper.Erc20TokenMapper;
import com.platon.browser.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * 奖励计算服务
 * 1、根据区块号计算周期切换相关值：
 * 名称/含义                                                                   变量名称
 * 当前增发周期开始时的激励池余额 IB                                             inciteBalance
 * 当前增发周期开始时的激励池余额分给区块奖励部分 BR=IB*区块奖励比例               inciteAmount4Block
 * 当前增发周期每个区块奖励值 BR/增发周期区块总数                                 blockReward
 * 当前增发周期开始时的激励池余额分给质押奖励部分 SR=IB*质押奖励比例               inciteAmount4Stake
 * 当前增发周期的每个结算周期质押奖励值 SSR=SR/一个增发周期包含的结算周期数        settleStakeReward
 * 当前结算周期每个节点的质押奖励值 PerNodeSR=SSR/当前结算周期实际验证人数         stakeReward
 * 当前共识周期验证人                                                            curValidators
 * 当前结算周期验证人                                                            curVerifiers
 */
@Slf4j
@Service
public class Erc20RetryService {
    @Autowired
    private Erc20TokenMapper erc20TokenMapper;

    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE)
    public Erc20Token getErc20Token ( String contractAddress ) {
        Erc20Token erc20Token = erc20TokenMapper.selectByAddress(contractAddress);
        if (null == erc20Token) {
            try {
                TimeUnit.SECONDS.sleep(5L);
            } catch (InterruptedException e) {
                throw new BusinessException("合约查询线程被中断！");
            }
            throw new BusinessException("合约【"+contractAddress+"】未入库，稍后重试...");
        }
        return erc20Token;
    }
}

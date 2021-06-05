package com.platon.browser.dao.param.ppos;

import com.platon.browser.dao.param.BusinessParam;
import com.platon.browser.enums.BusinessType;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.math.BigInteger;

@Data
@Builder
@Accessors(chain = true)
public class DelegateExit implements BusinessParam {
    @Data
    @Builder
    public static class DelegateBalance{
        //当前犹豫金额
        private BigDecimal delegateHes;
        //当前锁定金额
        private BigDecimal delegateLocked;
        //待领取金额
        private BigDecimal delegateReleased;
    }
    @Data
    @Builder
    public static class NodeDecrease{
        //当前犹豫金额
        private BigDecimal delegateHes;
        //当前锁定金额
        private BigDecimal delegateLocked;
        //待领取金额
        private BigDecimal delegateReleased;
    }
    //节点id
    private String nodeId;
    //真实退出委托金额
    private BigDecimal realRefundAmount;
    //节点质押快高
    private BigInteger stakingBlockNumber;
    //最新委托阈值
    private BigDecimal minimumThreshold;
    //交易所在块高
    private BigInteger blockNumber;
    //交易发送方
    private String txFrom;
    // 委托记录中剩余的金额
    @Builder.Default
    private DelegateBalance balance= DelegateBalance.builder().build();
    @Builder.Default
    private NodeDecrease decrease= NodeDecrease.builder().build();
    //当前是否为历史
    private int codeIsHistory;
    //节点是否退出
    private boolean codeNodeIsLeave;
    //全部退出时获得的奖励
    private BigDecimal delegateReward;


    @Override
    public BusinessType getBusinessType() {
        return BusinessType.DELEGATE_EXIT;
    }
}

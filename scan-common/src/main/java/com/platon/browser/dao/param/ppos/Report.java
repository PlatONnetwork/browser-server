package com.platon.browser.dao.param.ppos;

import com.platon.browser.dao.param.BusinessParam;
import com.platon.browser.enums.BusinessType;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

@Data
@Builder
@Accessors(chain = true)
public class Report implements BusinessParam {
    //举报证据
    private String slashData;
    //节点Id
    private String nodeId;
    //交易hash
    private String txHash;
    //时间
    private Date time;
    //通过（block_number/每个结算周期出块数）向上取整
    private int settingEpoch;
    //质押交易所在块高
    private BigInteger stakingBlockNum;
    //双签惩罚比例
    private BigDecimal slashRate;
    //惩罚金分配给举报人比例
    private BigDecimal slash2ReportRate;
    //交易发送者
    private String benefitAddr;
    //双签惩罚后剩下的质押金额，因为双签处罚后节点就被置为退出中，所有金额都会移动到待赎回字段中
    private BigDecimal codeRemainRedeemAmount;
    //奖励的金额
    private BigDecimal codeRewardValue;
    //节点状态
    private int codeStatus;
    //当前退出中
    private int codeStakingReductionEpoch;
    //惩罚的金额
    private BigDecimal codeSlashValue;
    //解质押需要经过的结算周期数
    private int unStakeFreezeDuration;
    //解质押冻结的最后一个区块：理论结束块与投票结束块中的最大者
    private BigInteger unStakeEndBlock;
    //双签的区块
    private Long blockNum;

    @Override
    public BusinessType getBusinessType() {
        return BusinessType.REPORT;
    }
}

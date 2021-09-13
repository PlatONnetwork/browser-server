package com.platon.browser.dao.param.ppos;

import com.platon.browser.dao.param.BusinessParam;
import com.platon.browser.enums.BusinessType;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigInteger;
import java.util.Date;

@Data
@Builder
@Accessors(chain = true)
public class ProposalParameter implements BusinessParam {
    //节点id
    private String nodeId;
    //pIDID替换
    private String pIDID;
    //提案url
    private String url;
    //PIP-pip_id
    private String pipNum;
    //投票结束快高
    private BigInteger endVotingBlock;
    //生效块高
    private BigInteger activeBlock;
    //提案主题
    private String topic;
    //提案描述
    private String description;
    //参数模块
    private String module;
    //参数名称
    private String name;
    //原参数值
    private String staleValue;
    //参数值
    private String newValue;
    //交易hash
    private String txHash;
    //区块高度
    private BigInteger blockNumber;
    //时间
    private Date timestamp;
    //质押名称
    private String stakingName;

    @Override
    public BusinessType getBusinessType() {
        return BusinessType.PROPOSAL_PARAMETER;
    }
}

package com.platon.browser.common.complement.dto.epoch;

import java.math.BigInteger;
import java.util.List;

import com.platon.browser.common.complement.dto.BusinessParam;
import com.platon.browser.common.enums.BusinessType;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Auther: dongqile
 * @Date: 2019/11/2
 * @Description: 共识周期切换参数入库
 */
@Data
@Builder
@Accessors(chain = true)
public class Consensus  extends BusinessParam {


    /**
     * 节点Id
     */
    private String nodeId;

    /**
     * 每个验证人期望出块数 共识周期出块数/当轮验证人数量
     */
    private BigInteger expectBlockNum;

    /**
     * 当前共识周期验证人
     */
    private List<String> validatorList;

    @Override
    public BusinessType getBusinessType () {
        return BusinessType.CONSENSUS_EPOCH;
    }
}
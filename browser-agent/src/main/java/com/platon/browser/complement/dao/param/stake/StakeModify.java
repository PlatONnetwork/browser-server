package com.platon.browser.complement.dao.param.stake;

import com.platon.browser.complement.dao.param.BusinessParam;
import com.platon.browser.common.enums.BusinessType;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import java.math.BigInteger;

/**
 * @description: 修改验证人 入库参数
 * @author: chendongming@juzix.net
 * @create: 2019-11-04 17:58:27
 **/
@Data
@Slf4j
@Builder
@Accessors(chain = true)
public class StakeModify implements BusinessParam {
    /**
     * 节点Id
     */
    private String nodeId;

    /**
     * 节点名称
     */
    private String nodeName;

    /**
     * 第三方社交软件关联id
     */
    private String externalId;

    /**
     * 收益地址
     */
    private String benefitAddr;

    /**
     * 节点的第三方主页
     */
    private String webSite;

    /**
     * 节点的描述
     */
    private String details;
    
    /**
     * 是否为链初始化时内置的候选人: 1是, 2否
     */
    private int isInit;

    /**
     * 质押所在区块号
     */
    private BigInteger stakingBlockNum;

    @Override
    public BusinessType getBusinessType() {
        return BusinessType.STAKE_MODIFY;
    }
}

package com.platon.browser.common.complement.dto.stake;

import com.platon.browser.common.complement.dto.BusinessParam;
import com.platon.browser.common.enums.BusinessType;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

/**
 * @description: 创建质押/创建验证人 入库参数
 * @author: chendongming@juzix.net
 * @create: 2019-11-04 17:58:27
 **/
@Data
@Slf4j
@Builder
@Accessors(chain = true)
public class StakeCreate extends BusinessParam {
    private String nodeId;
    private BigDecimal stakingHes;
    private String nodeName;
    private String externalId;
    private String benefitAddr;
    private String programVersion;
    private String bigVersion;
    private String webSite;
    private String details;
    private int isInit;
    private BigInteger stakingBlockNum;
    private int stakingTxIndex;
    private String stakingAddr;
    private Date joinTime;
    private String txHash;

    @Override
    public BusinessType getBusinessType() {
        return BusinessType.STAKE_CREATE;
    }
}

package com.platon.browser.common.complement.dto.stake;

import com.platon.browser.common.complement.dto.BusinessParam;
import com.platon.browser.common.enums.BusinessType;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import java.math.BigInteger;
import java.util.Date;

/**
 * @description: 修改验证人 入库参数
 * @author: chendongming@juzix.net
 * @create: 2019-11-04 17:58:27
 **/
@Data
@Slf4j
@Builder
@Accessors(chain = true)
public class StakeModify extends BusinessParam {
    private String nodeId;
    private String nodeName;
    private String externalId;
    private String benefitAddr;
    private String webSite;
    private String details;
    private BigInteger stakingBlockNum;
    private BigInteger bNum;
    private Date time;
    private int isInit;
    private String txHash;

    @Override
    public BusinessType getBusinessType() {
        return BusinessType.STAKE_MODIFY;
    }
}

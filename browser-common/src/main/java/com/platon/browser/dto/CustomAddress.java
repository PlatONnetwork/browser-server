package com.platon.browser.dto;

import com.platon.browser.dao.entity.Address;
import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

/**
 * @Auther: Chendongming
 * @Date: 2019/8/14 12:02
 * @Description: 地址实体扩展类
 */
@Data
public class CustomAddress extends Address {

    public CustomAddress(){
        super();
        Date date = new Date();
        this.setUpdateTime(date);
        this.setCreateTime(date);
        /* 初始化默认值 */
        this.setBalance(BigDecimal.ZERO);
        this.setRestrictingBalance(BigDecimal.ZERO);
        this.setStakingValue(BigDecimal.ZERO);
        this.setDelegateValue(BigDecimal.ZERO);
        this.setRedeemedValue(BigDecimal.ZERO);
        this.setTxQty(BigInteger.ZERO.intValue());
        this.setTransferQty(BigInteger.ZERO.intValue());
        this.setStakingQty(BigInteger.ZERO.intValue());
        this.setDelegateQty(BigInteger.ZERO.intValue());
        this.setProposalQty(BigInteger.ZERO.intValue());
        this.setCandidateCount(BigInteger.ZERO.intValue());
        this.setDelegateHes(BigDecimal.ZERO);
        this.setDelegateLocked(BigDecimal.ZERO);
        this.setContractName("");
        this.setContractCreate("");
        this.setContractCreatehash("");
    }
}

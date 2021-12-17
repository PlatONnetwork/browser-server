package com.platon.browser.v0150.dao;

import com.platon.browser.v0150.bean.AdjustParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.transaction.annotation.Transactional;

@Mapper
public interface StakingDelegateBalanceAdjustmentMapper {
    /**
     * 质押相关表调账
     */
    @Transactional(rollbackFor = {Exception.class, Error.class})
    void adjustStakingData(@Param("adjustParam") AdjustParam adjustParam);

    /**
     * 委托相关表调账
     */
    @Transactional(rollbackFor = {Exception.class, Error.class})
    void adjustDelegateData(@Param("adjustParam") AdjustParam adjustParam);
}
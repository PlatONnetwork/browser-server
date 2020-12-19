package com.platon.browser.adjustment.dao;

import com.platon.browser.adjustment.bean.AdjustParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Mapper
public interface AdjustmentMapper {
    /**
     * 质押相关表调账
     * @param adjustParams
     */
    @Transactional
    void adjustStakingData(@Param("adjustParams")List<AdjustParam> adjustParams);

    /**
     * 委托相关表调账
     * @param adjustParams
     */
    @Transactional
    void adjustDelegateData(@Param("adjustParams")List<AdjustParam> adjustParams);
}
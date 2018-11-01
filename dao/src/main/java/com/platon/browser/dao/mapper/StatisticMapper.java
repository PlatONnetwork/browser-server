package com.platon.browser.dao.mapper;

import com.platon.browser.dao.entity.HomeBlock;
import com.platon.browser.dao.entity.TpsCountParam;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

public interface StatisticMapper {
    List<HomeBlock> blockList(@Param("chainId") String chainId);
    long countAddress(@Param("chainId") String chainId);
    long countTransactionBlock(@Param("chainId") String chainId);
    long countTransactionIn24Hours(@Param("chainId") String chainId);
    long countTransactionInXMinute(@Param("param")TpsCountParam param);
    BigDecimal countAvgTransactionPerBlock(@Param("chainId") String chainId);
}
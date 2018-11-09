package com.platon.browser.dao.mapper;

import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

public interface StatisticMapper {
    long countAddress(@Param("chainId") String chainId);
    long countTransactionBlock(@Param("chainId") String chainId);
    long countTransactionIn24Hours(@Param("chainId") String chainId);
    BigDecimal countAvgTransactionPerBlock(@Param("chainId") String chainId);
}
package com.platon.browser.dao.mapper;

import com.platon.browser.common.dto.agent.StaticticsDto;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

public interface CustomStatisticsMapper {
    long countAddress(@Param("chainId") String chainId);
    long countTransactionBlock(@Param("chainId") String chainId);
    long countTransactionIn24Hours(@Param("chainId") String chainId);
    BigDecimal countAvgTransactionPerBlock(@Param("chainId") String chainId);
    long maxBlockNumber(@Param("chainId") String chainId);

    List<StaticticsDto> selectNodeInfo( @Param("chainId") String chainId);
}
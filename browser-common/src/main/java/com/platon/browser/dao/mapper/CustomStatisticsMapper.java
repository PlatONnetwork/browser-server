package com.platon.browser.dao.mapper;

import com.platon.browser.dto.agent.StatisticsDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface CustomStatisticsMapper {
    long countAddress(@Param("chainId") String chainId);
    long countTransactionBlock(@Param("chainId") String chainId);
    long countTransactionIn24Hours(@Param("chainId") String chainId);
    BigDecimal countAvgTransactionPerBlock(@Param("chainId") String chainId);
    long maxBlockNumber(@Param("chainId") String chainId);

    List<StatisticsDto> selectNodeInfo(@Param("chainId") String chainId);
}
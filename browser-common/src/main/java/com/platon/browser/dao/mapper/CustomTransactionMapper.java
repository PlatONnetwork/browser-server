package com.platon.browser.dao.mapper;

import com.platon.browser.dao.entity.PageParam;
import com.platon.browser.dao.entity.Transaction;
import com.platon.browser.dao.entity.TransactionWithBLOBs;
import com.platon.browser.dto.app.transaction.AppTransactionSummaryDto;
import com.platon.browser.dto.app.transaction.AppTransactionDto;
import com.platon.browser.dto.app.transaction.AppVoteTransactionDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CustomTransactionMapper {
    List<TransactionWithBLOBs> selectByPageWithBLOBs(@Param("page") PageParam page);
    List<Transaction> selectByPage(@Param("page") PageParam page);

    List<AppTransactionDto> selectByChainIdAndAddressAndBeginSequenceAndDirection(
            @Param("chainId") String chainId,
            @Param("walletAddrs") List<String> walletAddrs,
            @Param("beginSequence") Long beginSequence,
            @Param("direction") String direction,
            @Param("listSize") Integer listSize
    );

    List<AppVoteTransactionDto> selectByChainIdAndTxTypeAndNodeIdAndAddressesAndBeginSequenceAndDirection(
            @Param("chainId") String chainId,
            @Param("txType") String txType,
            @Param("nodeId") String nodeId,
            @Param("walletAddrs") List<String> walletAddrs,
            @Param("beginSequence") long beginSequence,
            @Param("direction") String direction,
            @Param("listSize") int listSize);

    List<AppTransactionSummaryDto> summaryByAddress(
            @Param("chainId") String chainId,
            @Param("txType") String txType,
            @Param("walletAddrs") List<String> walletAddrs
    );

    long getTotalVoteCountByNodeIds(
            @Param("chainId")String chainId,
            @Param("nodeIds") List<String> nodeIds
    );
}

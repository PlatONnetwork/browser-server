package com.platon.browser.dao.mapper;

import com.platon.browser.dao.entity.PageParam;
import com.platon.browser.dao.entity.Transaction;
import com.platon.browser.dao.entity.TransactionWithBLOBs;
import com.platon.browser.dto.app.transaction.TransactionDto;
import com.platon.browser.dto.app.transaction.VoteTransactionDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.net.Inet4Address;
import java.util.List;

@Mapper
public interface CustomTransactionMapper {
    List<TransactionWithBLOBs> selectByPageWithBLOBs(@Param("page") PageParam page);
    List<Transaction> selectByPage(@Param("page") PageParam page);

    List<TransactionDto> selectByChainIdAndAddressAndBeginSequence(
            @Param("chainId") String chainId,
            @Param("address") String address,
            @Param("beginSequence") Long beginSequence,
            @Param("listSize") Integer listSize
    );

    List<VoteTransactionDto> selectByChainIdAndTxTypeAndNodeIdAndAddressesAndBeginSequence(
            @Param("chainId") String chainId,
            @Param("txType") String txType,
            @Param("nodeId") String nodeId,
            @Param("walletAddrs") List<String> walletAddrs,
            @Param("beginSequence") long beginSequence,
            @Param("listSize") int listSize);
}

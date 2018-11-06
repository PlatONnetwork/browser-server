package com.platon.browser.dao.mapper;

import org.apache.ibatis.annotations.Param;

/*
* 
* User: dongqile
* Date: 2018/11/6
* Time: 15:13
*/
public interface CalculateMapper {

    /*
    *  合约、账户统计
    */
    long countTransactionOrContract(@Param("receiveType") String receiveType,
                          @Param("chainId") String chainId, @Param("from") String from, @Param("to") String to);

    /*
    *  区块统计
    */
    long countBlock(@Param("hash") String hash,
                          @Param("chainId") String chainId);

    /*
    *  交易统计
    */
    long countTransaction(@Param("hash") String hash,
                          @Param("chainId") String chainId);
    /*
    *  挂起交易统计
    */
    long countPendingTx (@Param("hash") String hash,
                          @Param("chainId") String chainId);
}
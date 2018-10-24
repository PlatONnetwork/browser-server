package com.platon.browser.dao.mapper;

import com.platon.browser.dao.entity.Transaction;
import com.platon.browser.dao.entity.TransactionExample;
import com.platon.browser.dao.entity.TransactionWithBLOBs;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TransactionMapper {
    long countByExample(TransactionExample example);

    int deleteByExample(TransactionExample example);

    int deleteByPrimaryKey(String hash);

    int insert(TransactionWithBLOBs record);

    int insertSelective(TransactionWithBLOBs record);

    List<TransactionWithBLOBs> selectByExampleWithBLOBs(TransactionExample example);

    List<Transaction> selectByExample(TransactionExample example);

    TransactionWithBLOBs selectByPrimaryKey(String hash);

    int updateByExampleSelective(@Param("record") TransactionWithBLOBs record, @Param("example") TransactionExample example);

    int updateByExampleWithBLOBs(@Param("record") TransactionWithBLOBs record, @Param("example") TransactionExample example);

    int updateByExample(@Param("record") Transaction record, @Param("example") TransactionExample example);

    int updateByPrimaryKeySelective(TransactionWithBLOBs record);

    int updateByPrimaryKeyWithBLOBs(TransactionWithBLOBs record);

    int updateByPrimaryKey(Transaction record);
}
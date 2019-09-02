package com.platon.browser.dao.mapper;

import com.platon.browser.dao.entity.TransactionWithBLOBs;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Set;

@Mapper
public interface CustomTransactionMapper {

    int batchInsertOrUpdateSelective(@Param("list") Set<TransactionWithBLOBs> list, @Param("selective") TransactionWithBLOBs.Column... selective);
}

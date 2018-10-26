package com.platon.browser.dao.mapper;

import com.platon.browser.dao.entity.AllTransaction;
import com.platon.browser.dao.entity.AllTransactionExample;
import com.platon.browser.dao.entity.AllTransactionWithBLOBs;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface AllTransactionMapper {
    long countByExample(AllTransactionExample example);

    int deleteByExample(AllTransactionExample example);

    int insert(AllTransactionWithBLOBs record);

    int insertSelective(AllTransactionWithBLOBs record);

    List<AllTransactionWithBLOBs> selectByExampleWithBLOBs(AllTransactionExample example);

    List<AllTransaction> selectByExample(AllTransactionExample example);

    int updateByExampleSelective(@Param("record") AllTransactionWithBLOBs record, @Param("example") AllTransactionExample example);

    int updateByExampleWithBLOBs(@Param("record") AllTransactionWithBLOBs record, @Param("example") AllTransactionExample example);

    int updateByExample(@Param("record") AllTransaction record, @Param("example") AllTransactionExample example);
}
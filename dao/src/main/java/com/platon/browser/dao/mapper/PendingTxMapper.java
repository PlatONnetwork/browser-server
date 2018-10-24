package com.platon.browser.dao.mapper;

import com.platon.browser.dao.entity.PendingTx;
import com.platon.browser.dao.entity.PendingTxExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface PendingTxMapper {
    long countByExample(PendingTxExample example);

    int deleteByExample(PendingTxExample example);

    int deleteByPrimaryKey(String hash);

    int insert(PendingTx record);

    int insertSelective(PendingTx record);

    List<PendingTx> selectByExampleWithBLOBs(PendingTxExample example);

    List<PendingTx> selectByExample(PendingTxExample example);

    PendingTx selectByPrimaryKey(String hash);

    int updateByExampleSelective(@Param("record") PendingTx record, @Param("example") PendingTxExample example);

    int updateByExampleWithBLOBs(@Param("record") PendingTx record, @Param("example") PendingTxExample example);

    int updateByExample(@Param("record") PendingTx record, @Param("example") PendingTxExample example);

    int updateByPrimaryKeySelective(PendingTx record);

    int updateByPrimaryKeyWithBLOBs(PendingTx record);

    int updateByPrimaryKey(PendingTx record);
}
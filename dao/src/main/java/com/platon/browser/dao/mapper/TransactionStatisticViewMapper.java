package com.platon.browser.dao.mapper;

import com.platon.browser.dao.entity.TransactionStatisticView;
import com.platon.browser.dao.entity.TransactionStatisticViewExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TransactionStatisticViewMapper {
    long countByExample(TransactionStatisticViewExample example);

    int deleteByExample(TransactionStatisticViewExample example);

    int insert(TransactionStatisticView record);

    int insertSelective(TransactionStatisticView record);

    List<TransactionStatisticView> selectByExample(TransactionStatisticViewExample example);

    int updateByExampleSelective(@Param("record") TransactionStatisticView record, @Param("example") TransactionStatisticViewExample example);

    int updateByExample(@Param("record") TransactionStatisticView record, @Param("example") TransactionStatisticViewExample example);
}
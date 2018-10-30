package com.platon.browser.dao.mapper;

import com.platon.browser.dao.entity.StatisticTransactionView;
import com.platon.browser.dao.entity.StatisticTransactionViewExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface StatisticTransactionViewMapper {
    long countByExample(StatisticTransactionViewExample example);

    int deleteByExample(StatisticTransactionViewExample example);

    int insert(StatisticTransactionView record);

    int insertSelective(StatisticTransactionView record);

    List<StatisticTransactionView> selectByExample(StatisticTransactionViewExample example);

    int updateByExampleSelective(@Param("record") StatisticTransactionView record, @Param("example") StatisticTransactionViewExample example);

    int updateByExample(@Param("record") StatisticTransactionView record, @Param("example") StatisticTransactionViewExample example);
}
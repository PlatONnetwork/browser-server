package com.platon.browser.dao.mapper;

import com.platon.browser.dao.entity.AvgTransactionView;
import com.platon.browser.dao.entity.AvgTransactionViewExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface AvgTransactionViewMapper {
    long countByExample(AvgTransactionViewExample example);

    int deleteByExample(AvgTransactionViewExample example);

    int insert(AvgTransactionView record);

    int insertSelective(AvgTransactionView record);

    List<AvgTransactionView> selectByExample(AvgTransactionViewExample example);

    int updateByExampleSelective(@Param("record") AvgTransactionView record, @Param("example") AvgTransactionViewExample example);

    int updateByExample(@Param("record") AvgTransactionView record, @Param("example") AvgTransactionViewExample example);
}
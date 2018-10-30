package com.platon.browser.dao.mapper;

import com.platon.browser.dao.entity.DistinctAddressView;
import com.platon.browser.dao.entity.DistinctAddressViewExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface DistinctAddressViewMapper {
    long countByExample(DistinctAddressViewExample example);

    int deleteByExample(DistinctAddressViewExample example);

    int insert(DistinctAddressView record);

    int insertSelective(DistinctAddressView record);

    List<DistinctAddressView> selectByExample(DistinctAddressViewExample example);

    int updateByExampleSelective(@Param("record") DistinctAddressView record, @Param("example") DistinctAddressViewExample example);

    int updateByExample(@Param("record") DistinctAddressView record, @Param("example") DistinctAddressViewExample example);
}
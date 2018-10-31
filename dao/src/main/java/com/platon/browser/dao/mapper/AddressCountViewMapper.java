package com.platon.browser.dao.mapper;

import com.platon.browser.dao.entity.AddressCountView;
import com.platon.browser.dao.entity.AddressCountViewExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface AddressCountViewMapper {
    long countByExample(AddressCountViewExample example);

    int deleteByExample(AddressCountViewExample example);

    int insert(AddressCountView record);

    int insertSelective(AddressCountView record);

    List<AddressCountView> selectByExample(AddressCountViewExample example);

    int updateByExampleSelective(@Param("record") AddressCountView record, @Param("example") AddressCountViewExample example);

    int updateByExample(@Param("record") AddressCountView record, @Param("example") AddressCountViewExample example);
}
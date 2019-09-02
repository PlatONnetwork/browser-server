package com.platon.browser.dao.mapper;

import com.platon.browser.dao.entity.Address;
import com.platon.browser.dto.CustomAddress;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

@Mapper
public interface CustomAddressMapper {
    List<CustomAddress> selectAll();
    int batchInsertOrUpdateSelective(@Param("list") Set<Address> list, @Param("selective") Address.Column... selective);
}

package com.platon.browser.dao.mapper;

import com.platon.browser.dao.entity.InternalAddress;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

public interface CustomInternalAddressMapper {
    int batchInsertOrUpdateSelective(@Param("list") Collection<InternalAddress> list, @Param("selective") InternalAddress.Column... selective);
}
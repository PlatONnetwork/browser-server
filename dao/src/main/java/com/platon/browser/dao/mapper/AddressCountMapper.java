package com.platon.browser.dao.mapper;

import com.platon.browser.dao.entity.AddressCountParam;
import org.apache.ibatis.annotations.Param;

public interface AddressCountMapper {
    long countByParam(@Param("param") AddressCountParam param);
}
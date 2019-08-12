package com.platon.browser.dao.mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CustomBlockMapper {
    Long selectMaxBlockNumber();
}

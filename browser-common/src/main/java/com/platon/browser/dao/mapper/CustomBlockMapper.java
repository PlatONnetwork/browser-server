package com.platon.browser.dao.mapper;

import com.platon.browser.dao.entity.Block;
import com.platon.browser.dao.entity.BlockExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CustomBlockMapper {
    Long selectMaxBlockNumber();
}

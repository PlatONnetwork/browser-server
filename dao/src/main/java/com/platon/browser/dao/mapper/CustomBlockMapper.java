package com.platon.browser.dao.mapper;

import com.platon.browser.dao.entity.CustomBlock;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CustomBlockMapper {
    List<CustomBlock> selectByChainId(@Param("chainId") String chainId);
}
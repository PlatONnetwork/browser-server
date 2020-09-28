package com.platon.browser.dao.mapper;

import com.platon.browser.dao.entity.Erc20Token;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CustomErc20TokenMapper {

    int batchUpdate(@Param("list") List<Erc20Token> list);

}
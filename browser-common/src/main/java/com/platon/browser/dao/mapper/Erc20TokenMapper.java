package com.platon.browser.dao.mapper;

import com.platon.browser.dao.entity.Erc20Token;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface Erc20TokenMapper {

    int insert(Erc20Token record);

    Erc20Token selectByPrimaryKey(Long id);

    Erc20Token selectByAddress(@Param("address") String address);

    List<Erc20Token> listErc20Token(Map params);

    int totalErc20Token(Map params);

    int updateByPrimaryKeySelective(Erc20Token record);

    int batchInsert(@Param("list") List<Erc20Token> list);

}
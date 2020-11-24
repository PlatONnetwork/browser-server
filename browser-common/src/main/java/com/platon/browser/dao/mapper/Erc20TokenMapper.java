package com.platon.browser.dao.mapper;

import com.platon.browser.dao.entity.Erc20Token;
import com.platon.browser.dao.entity.Erc20TokenExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface Erc20TokenMapper {

    int insert(Erc20Token record);

    Erc20Token selectByPrimaryKey(Long id);

    Erc20Token selectByAddress(@Param("address") String address);

    List<Erc20Token> selectByExample(Erc20TokenExample example);

    List<Erc20Token> listErc20Token(Map params);

    List<Erc20Token> listErc20TokenIds(Map params);

    List<Erc20Token> listErc20TokenByIds(List<Long> list);

    int totalErc20Token(Map params);

    int updateByPrimaryKeySelective(Erc20Token record);

    int batchInsert(@Param("list") List<Erc20Token> list);

    int batchUpdateTxCount(@Param("list") List<Erc20Token> list);

}
package com.platon.browser.dao.mapper;

import com.platon.browser.dao.entity.Erc20TokenDetailWithBLOBs;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface Erc20TokenDetailMapper {

    int insert(Erc20TokenDetailWithBLOBs record);

    Erc20TokenDetailWithBLOBs selectByPrimaryKey(Long id);

    Erc20TokenDetailWithBLOBs selectByAddress(@Param("contract") String contract);

    int updateByPrimaryKeySelective(Erc20TokenDetailWithBLOBs record);

    int updateByPrimaryKeyWithBLOBs(Erc20TokenDetailWithBLOBs record);

    int batchInsert(@Param("list") List<Erc20TokenDetailWithBLOBs> list);

}
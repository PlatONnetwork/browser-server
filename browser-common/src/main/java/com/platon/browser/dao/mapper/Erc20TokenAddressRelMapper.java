package com.platon.browser.dao.mapper;

import com.platon.browser.dao.entity.Erc20TokenAddressRel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface Erc20TokenAddressRelMapper {

    int insert(Erc20TokenAddressRel record);

    Erc20TokenAddressRel selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Erc20TokenAddressRel record);

    int batchInsert(@Param("list") List<Erc20TokenAddressRel> list);
}
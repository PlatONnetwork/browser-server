package com.platon.browser.dao.mapper;

import com.platon.browser.dao.entity.Erc20TokenAddressRel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CustomErc20TokenAddressRelMapper {

    List<Erc20TokenAddressRel> selectData(@Param("list") List<Erc20TokenAddressRel> list);

    int updateAddressData(@Param("list") List<Erc20TokenAddressRel> list);
    
}
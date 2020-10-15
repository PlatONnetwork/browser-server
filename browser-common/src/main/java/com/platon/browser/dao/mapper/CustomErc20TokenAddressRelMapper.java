package com.platon.browser.dao.mapper;

import com.github.pagehelper.Page;
import com.platon.browser.dao.entity.Erc20TokenAddressRel;
import com.platon.browser.dto.CustomErc20TokenAddressRel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CustomErc20TokenAddressRelMapper {

    List<Erc20TokenAddressRel> selectExistData(@Param("list") List<Erc20TokenAddressRel> list);

    Page<CustomErc20TokenAddressRel> selectByAddress(@Param("address") String address);

    int updateAddressData(@Param("list") List<Erc20TokenAddressRel> list);

    int updateAddressBalance(@Param("list") List<Erc20TokenAddressRel> list);
}
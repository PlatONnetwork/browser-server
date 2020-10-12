package com.platon.browser.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.platon.browser.dao.entity.Erc20TokenAddressRel;

@Mapper
public interface CustomErc20TokenAddressRelMapper {

    int updateAddressData(List<Erc20TokenAddressRel> list);
}
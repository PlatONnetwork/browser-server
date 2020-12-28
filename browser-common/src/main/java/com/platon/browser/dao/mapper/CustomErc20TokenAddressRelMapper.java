package com.platon.browser.dao.mapper;

import com.github.pagehelper.Page;
import com.platon.browser.dao.entity.Erc20TokenAddressRel;
import com.platon.browser.bean.CustomErc20TokenAddressRel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Mapper
public interface CustomErc20TokenAddressRelMapper {

    List<Erc20TokenAddressRel> selectExistData(@Param("list") List<Erc20TokenAddressRel> list);

    Page<CustomErc20TokenAddressRel> selectByAddress(@Param("address") String address);

    int updateAddressData(@Param("list") List<Erc20TokenAddressRel> list);

    int updateAddressBalance(@Param("list") List<Erc20TokenAddressRel> list);

    List<Erc20TokenAddressRel> listErc20TokenAddressRelIds(Map params);

    List<Erc20TokenAddressRel> listErc20TokenAddressRelByIds(List<Long> list);

    int countByContract(Map params);

    int countByAddress(Map params);

    BigDecimal sumBalanceByContract(String contractAddress);
}
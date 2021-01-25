package com.platon.browser.dao.mapper;

import com.platon.browser.bean.CustomToken;
import com.platon.browser.bean.CustomTokenInventory;
import com.platon.browser.dao.entity.TokenInventory;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CustomTokenInventoryMapper {
    int batchInsertOrUpdateSelective(@Param("list") List<TokenInventory> list, @Param("selective") TokenInventory.Column... selective);

}
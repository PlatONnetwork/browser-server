package com.platon.browser.dao.mapper;

import com.github.pagehelper.Page;
import com.platon.browser.bean.CustomToken;
import com.platon.browser.bean.CustomTokenHolder;
import com.platon.browser.dao.entity.TokenHolder;
import com.platon.browser.dao.entity.TokenHolderExample;
import com.platon.browser.dao.entity.TokenHolderKey;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CustomTokenHolderMapper {
    Page<CustomTokenHolder> selectListByParams(@Param("tokenAddress")String tokenAddress, @Param("address")String address);
}
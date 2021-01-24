package com.platon.browser.dao.mapper;

import com.github.pagehelper.Page;
import com.platon.browser.bean.CustomToken;
import com.platon.browser.bean.CustomTokenDetail;
import com.platon.browser.dao.entity.Token;
import com.platon.browser.dao.entity.TokenExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
public interface CustomTokenMapper {

    Page<CustomToken> selectListByType(@Param("type")String type);

    CustomTokenDetail selectDetailByAddress(@Param("address")String address);

    int batchInsertOrUpdateSelective(@Param("list") List<Token> list, @Param("selective") Token.Column... selective);
}
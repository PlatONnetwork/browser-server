package com.platon.browser.converter;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import com.platon.browser.elasticsearch.dto.ESTokenTransferRecord;
import com.platon.browser.res.staking.QueryInnerTxByAddrResp;

/**
 * @program: browser-server
 * @description: 返回对象转换类
 * @author: Rongjin Zhang
 * @create: 2020-09-22 16:30
 */
@Mapper
public interface QueryInnerTxByAddrRespConverter {
    QueryInnerTxByAddrRespConverter INSTANCE = Mappers.getMapper(QueryInnerTxByAddrRespConverter.class);

    @Mappings({@Mapping(source = "tto", target = "to"), @Mapping(source = "name", target = "tokenName"),
        @Mapping(source = "contract", target = "tokenAddr"), @Mapping(source = "bTime", target = "time"),
        @Mapping(target = "nowTime", expression = "java(new java.util.Date().getTime())")})
    QueryInnerTxByAddrResp domain2dto(ESTokenTransferRecord person);

    List<QueryInnerTxByAddrResp> domain2dto(List<ESTokenTransferRecord> innerList);

}

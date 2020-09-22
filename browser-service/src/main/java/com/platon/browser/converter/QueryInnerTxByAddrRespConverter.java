package com.platon.browser.converter;

import com.platon.browser.elasticsearch.dto.InnerTx;
import com.platon.browser.res.staking.QueryInnerTxByAddrResp;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @program: browser-server
 * @description: 返回对象转换类
 * @author: Rongjin Zhang
 * @create: 2020-09-22 16:30
 */
@Mapper
public interface QueryInnerTxByAddrRespConverter {
    QueryInnerTxByAddrRespConverter INSTANCE = Mappers.getMapper(QueryInnerTxByAddrRespConverter.class);

    @Mappings({
            @Mapping(target = "nowTime", expression = "java(new java.util.Date().getTime())")
    })
    QueryInnerTxByAddrResp domain2dto(InnerTx person);

    List<QueryInnerTxByAddrResp> domain2dto(List<InnerTx> innerList);

}

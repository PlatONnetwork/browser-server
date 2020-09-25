package com.platon.browser.converter;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.platon.browser.dto.TransferEvent;
import com.platon.browser.erc.client.ERC20Client;

/**
 * @program: browser-server
 * @description: 转账事件转换
 * @author: Rongjin Zhang
 * @create: 2020-09-23 12:04
 */
@Mapper
public interface TransferEventConverter {

    TransferEventConverter INSTANCE = Mappers.getMapper(TransferEventConverter.class);

    TransferEvent domain2dto(ERC20Client.TransferEventResponse person);

    List<TransferEvent> domain2dto(List<ERC20Client.TransferEventResponse> innerList);
}

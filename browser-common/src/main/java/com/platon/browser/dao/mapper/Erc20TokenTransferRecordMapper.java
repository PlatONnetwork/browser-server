package com.platon.browser.dao.mapper;

import com.platon.browser.dao.entity.Erc20TokenTransferRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface Erc20TokenTransferRecordMapper {

    int insert(Erc20TokenTransferRecord record);

    int updateByPrimaryKeySelective(Erc20TokenTransferRecord record);

    int batchInsert(@Param("list") List<Erc20TokenTransferRecord> list);

}
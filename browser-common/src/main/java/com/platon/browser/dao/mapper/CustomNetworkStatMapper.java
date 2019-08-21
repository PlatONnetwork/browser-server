package com.platon.browser.dao.mapper;

import com.platon.browser.dao.entity.NetworkStat;
import com.platon.browser.dao.entity.NodeOpt;
import com.platon.browser.dto.CustomNodeOpt;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

@Mapper
public interface CustomNetworkStatMapper {
    int batchInsertOrUpdateSelective ( @Param("list") Set <NetworkStat> list, @Param("selective") NetworkStat.Column... selective );
}

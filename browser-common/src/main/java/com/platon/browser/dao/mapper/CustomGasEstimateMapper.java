package com.platon.browser.dao.mapper;

import com.platon.browser.dao.entity.GasEstimate;
import com.platon.browser.dao.entity.GasEstimateExample;
import com.platon.browser.dao.entity.GasEstimateKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
public interface CustomGasEstimateMapper {
    int batchInsertOrUpdateSelective(@Param("list") List<GasEstimate> list, @Param("selective") GasEstimate.Column... selective);
}
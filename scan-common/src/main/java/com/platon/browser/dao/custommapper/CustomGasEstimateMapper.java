package com.platon.browser.dao.custommapper;

import com.platon.browser.dao.entity.GasEstimate;

import java.util.List;

public interface CustomGasEstimateMapper {
    /**
     * 新增Gas_Estimate记录（epoch=0)
     * 当主键存在时，重置epoch = 0
     * @param list
     * @return
     */
    int batchInsertOrResetEpoch(List<GasEstimate> list);

    int resetEpoch(List<GasEstimate> estimates);

    int increaseEpoch(List<GasEstimate> estimates);

    List<GasEstimate> listHashCodeEmpty();

    int updateHashCodeEmpty(List<GasEstimate> estimates);
}

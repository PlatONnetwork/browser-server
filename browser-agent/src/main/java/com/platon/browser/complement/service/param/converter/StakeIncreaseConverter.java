package com.platon.browser.complement.service.param.converter;

import com.platon.browser.common.collection.dto.CollectionTransaction;
import com.platon.browser.common.complement.dto.BusinessParam;
import com.platon.browser.common.complement.dto.stake.StakeIncrease;
import com.platon.browser.param.StakeIncreaseParam;
import com.platon.browser.utils.HexTool;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * @description: 增持质押业务参数转换器
 * @author: chendongming@juzix.net
 * @create: 2019-11-04 17:58:27
 **/
@Service
public class StakeIncreaseConverter extends BusinessParamConverter {

    @Override
    public BusinessParam convert(CollectionTransaction tx) {
        // 增持质押
        StakeIncreaseParam txParam = tx.getTxParam(StakeIncreaseParam.class);
        BusinessParam businessParam= StakeIncrease.builder().build();
        BeanUtils.copyProperties(txParam,businessParam);
        // 更新节点缓存
        updateNodeCache(HexTool.prefix(txParam.getNodeId()),txParam.getNodeName());
        return businessParam;
    }
}

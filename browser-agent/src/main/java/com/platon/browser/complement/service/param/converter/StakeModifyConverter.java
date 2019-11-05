package com.platon.browser.complement.service.param.converter;

import com.platon.browser.common.collection.dto.CollectionTransaction;
import com.platon.browser.common.complement.dto.stake.StakeModify;
import com.platon.browser.param.StakeModifyParam;
import com.platon.browser.utils.HexTool;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * @description: 修改验证人业务参数转换器
 * @author: chendongming@juzix.net
 * @create: 2019-11-04 17:58:27
 **/
@Service
public class StakeModifyConverter extends BusinessParamConverter<StakeModify> {

    @Override
    public StakeModify convert(CollectionTransaction tx) {
        // 修改质押信息
        StakeModifyParam txParam = tx.getTxParam(StakeModifyParam.class);
        StakeModify businessParam= StakeModify.builder()
                .nodeId(txParam.getNodeId())
                .benefitAddr(txParam.getBenefitAddress())
                .bNum(txParam.getBlockNumber())
                .details(txParam.getDetails())
                .externalId(txParam.getExternalId())
                // TODO: 修改质押时如何判断当前质押是否是内置节点
                //.isInit()
                .build();
        BeanUtils.copyProperties(txParam,businessParam);
        // 更新节点缓存
        updateNodeCache(HexTool.prefix(txParam.getNodeId()),txParam.getNodeName());
        return businessParam;
    }
}

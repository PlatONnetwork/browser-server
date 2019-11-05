package com.platon.browser.complement.service.param.converter;

import com.platon.browser.common.collection.dto.CollectionTransaction;
import com.platon.browser.common.complement.dto.stake.StakeExit;
import com.platon.browser.param.StakeExitParam;
import com.platon.browser.utils.HexTool;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * @description: 退出质押业务参数转换器
 * @author: chendongming@juzix.net
 * @create: 2019-11-04 17:58:27
 **/
@Service
public class StakeExitConverter extends BusinessParamConverter<StakeExit> {

    @Override
    public StakeExit convert(CollectionTransaction tx) {
        // 撤销质押
        StakeExitParam txParam = tx.getTxParam(StakeExitParam.class);
        StakeExit businessParam= StakeExit.builder()
                .bNum(txParam.getStakingBlockNum())
                .nodeId(txParam.getNodeId())
                .stakingBlockNum(txParam.getStakingBlockNum())
                .txHash(tx.getHash())
                .time(tx.getTime())
                // TODO: 补充质押退出时所在的结算周期
                //.stakingReductionEpoch()
                .build();
        BeanUtils.copyProperties(txParam,businessParam);
        // 更新节点缓存
        updateNodeCache(HexTool.prefix(txParam.getNodeId()),txParam.getNodeName());
        return businessParam;
    }
}

package com.platon.browser.complement.service.param.converter;

import java.math.BigInteger;

import org.springframework.stereotype.Service;

import com.platon.browser.common.collection.dto.CollectionTransaction;
import com.platon.browser.common.complement.dto.stake.StakeExit;
import com.platon.browser.param.StakeExitParam;

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
        		.nodeId(txParam.getNodeId())
        		.txHash(tx.getHash())
        		.stakingBlockNum(txParam.getStakingBlockNum())
        		.time(tx.getTime())
                .bNum(BigInteger.valueOf(tx.getNum()))
                .build();
        return businessParam;
    }
}

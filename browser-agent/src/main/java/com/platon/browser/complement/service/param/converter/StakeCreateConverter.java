package com.platon.browser.complement.service.param.converter;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.springframework.stereotype.Service;

import com.platon.browser.common.collection.dto.CollectionTransaction;
import com.platon.browser.common.complement.dto.stake.StakeCreate;
import com.platon.browser.param.StakeCreateParam;
import com.platon.browser.utils.HexTool;
import com.platon.browser.utils.VerUtil;


/**
 * @description: 创建验证人业务参数转换器
 * @author: chendongming@juzix.net
 * @create: 2019-11-04 17:58:27
 **/
@Service
public class StakeCreateConverter extends BusinessParamConverter<StakeCreate> {


    @Override
    public StakeCreate convert(CollectionTransaction tx) {
        StakeCreateParam txParam = tx.getTxParam(StakeCreateParam.class);
        BigInteger bigVersion = VerUtil.transferBigVersion(txParam.getProgramVersion());
        StakeCreate businessParam= StakeCreate.builder()
        		.nodeId(txParam.getNodeId())
        		.stakingHes(new BigDecimal(txParam.getAmount()))
        		.nodeName(txParam.getNodeName())
        		.externalId(txParam.getExternalId())
        		.benefitAddr(txParam.getBenefitAddress())
        		.programVersion(txParam.getProgramVersion().toString())
        		.bigVersion(bigVersion.toString())
        		.webSite(txParam.getWebsite())
        		.details(txParam.getDetails())
        		.isInit(isInit(txParam.getBenefitAddress())) 
        		.stakingBlockNum(BigInteger.valueOf(tx.getNum()))
        		.stakingTxIndex(tx.getIndex())
        		.stakingAddr(tx.getFrom())
        		.joinTime(tx.getTime())
        		.txHash(tx.getHash())               
                .build();
        updateNodeCache(HexTool.prefix(txParam.getNodeId()),txParam.getNodeName());
        return businessParam;
    }
}

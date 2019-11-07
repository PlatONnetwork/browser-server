package com.platon.browser.complement.service.param.converter;

import com.platon.browser.common.collection.dto.CollectionTransaction;
import com.platon.browser.common.complement.dto.BusinessParam;
import com.platon.browser.common.complement.dto.stake.StakeCreate;
import com.platon.browser.param.StakeCreateParam;
import com.platon.browser.utils.VerUtil;
import com.platon.browser.utils.HexTool;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;


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
        txParam.setBlockNumber(BigInteger.valueOf(tx.getNum()));
        BigInteger bigVersion = VerUtil.transferBigVersion(txParam.getProgramVersion());
        StakeCreate businessParam= StakeCreate.builder()
                .benefitAddr(txParam.getBenefitAddress())
                .stakingHes(new BigDecimal(txParam.getAmount()))
                .webSite(txParam.getWebsite())
                .stakingBlockNum(txParam.getBlockNumber())
                .txHash(tx.getHash())
                .stakingTxIndex(tx.getIndex())
                .joinTime(tx.getTime())
                .stakingAddr(tx.getFrom())
                .bigVersion(bigVersion.toString())
                .programVersion(txParam.getProgramVersion().toString())
                .isInit(BusinessParam.YesNoEnum.NO.getCode())
                .build();
        BeanUtils.copyProperties(txParam,businessParam);
        updateNodeCache(HexTool.prefix(txParam.getNodeId()),txParam.getNodeName());
        return businessParam;
    }
}

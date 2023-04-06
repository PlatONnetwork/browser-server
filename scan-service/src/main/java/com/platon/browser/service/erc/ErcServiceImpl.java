package com.platon.browser.service.erc;

import cn.hutool.core.text.StrFormatter;
import cn.hutool.core.util.ObjectUtil;
import com.platon.browser.enums.ErcTypeEnum;
import com.platon.browser.v0152.contract.ErcContract;
import com.platon.browser.v0152.service.ErcDetectService;
import com.platon.tx.exceptions.PlatonCallException;
import com.platon.tx.exceptions.PlatonCallTimeoutException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigInteger;

@Slf4j
@Component
public class ErcServiceImpl {
    @Resource
    private ErcDetectService ercDetectService;
    /**
     * 获取供应总量
     *
     * @param contractAddress 合约地址
     * @return java.math.BigInteger
     * @date 2021/1/18
     */
    public BigInteger getTotalSupply(ErcTypeEnum ercTypeEnum, String contractAddress, BigInteger currentBlockNumber) {
        BigInteger totalSupply = BigInteger.ZERO;
        try {
            // lvxiaoyi, 2023/03/29，获取token的totalSupply，无需ErcContractId。
            // ErcContractId ercContractId = ercDetectService.getContractId(contractAddress);
            ErcContract ercContract = ercDetectService.getErcContract(ercTypeEnum, contractAddress, currentBlockNumber);
            if (ObjectUtil.isNotNull(ercContract)) {
                totalSupply = ercContract.totalSupply().send();
            }
        } catch (Exception e) {
            log.warn(StrFormatter.format("从特殊节点获取供应总量异常,contractAddress：{},块高:{}", contractAddress, currentBlockNumber), e);
        }
        return totalSupply;
    }

    /**
     * 获取TokenURI
     *
     * @param contractAddress 合约地址
     * @param tokenId         token id
     * @param blockNumber:    块高
     * @return: java.lang.String
     * @date: 2022/2/10
     */
    public String getTokenURI(ErcTypeEnum ercTypeEnum, String contractAddress, BigInteger tokenId, BigInteger blockNumber) {
        String tokenURI = "";
        try {
            ErcContract ercContract = ercDetectService.getErcContract(ercTypeEnum, contractAddress, blockNumber);
            if (ObjectUtil.isNotNull(ercContract)) {
                tokenURI = ercContract.getTokenURI(tokenId).send();
            }
        } catch (PlatonCallException e) {
            log.warn(StrFormatter.format("getTokenURI异常，token_address：{},token_id:{},msg:{}", contractAddress, tokenId, e.getMsg()), e);
        } catch (PlatonCallTimeoutException e) {
            log.warn(StrFormatter.format("getTokenURI异常，token_address：{},token_id:{},msg:{}", contractAddress, tokenId, e.getMsg()), e);
        } catch (Exception e) {
            log.warn(StrFormatter.format("getTokenURI异常，token_address：{},token_id:{}", contractAddress, tokenId), e);
        }
        return tokenURI;
    }

}

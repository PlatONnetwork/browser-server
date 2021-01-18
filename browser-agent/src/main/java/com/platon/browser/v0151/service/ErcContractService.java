package com.platon.browser.v0151.service;

import com.platon.browser.bean.CollectionTransaction;
import com.platon.browser.v0151.analyzer.Erc20TxAnalyzer;
import com.platon.browser.v0151.analyzer.Erc721TxAnalyzer;
import com.platon.browser.v0151.bean.ErcContractId;
import com.platon.browser.v0151.bean.ErcToken;
import com.platon.browser.v0151.cache.ErcCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * Erc合约相关服务
 */
@Slf4j
@Service
public class ErcContractService {
    @Resource
    private ErcDetectService ercDetectService;
    @Resource
    private ErcCache ercCache;
    @Resource
    private Erc20TxAnalyzer erc20TxAnalyzer;
    @Resource
    private Erc721TxAnalyzer erc721TxAnalyzer;
    /**
     * 分析合约地址
     * @param contractAddress
     */
    private ErcToken updateErcCache(String contractAddress) {
        ErcToken token = new ErcToken();
        token.setAddress(contractAddress);
        Date date = new Date();
        token.setCreateTime(date);
        token.setUpdateTime(date);
        ErcContractId contractId = ercDetectService.getContractId(contractAddress);
        BeanUtils.copyProperties(contractId,token);
        token.setTypeEnum(contractId.getTypeEnum());
        token.setType(contractId.getTypeEnum().name().toLowerCase());
        switch (contractId.getTypeEnum()){
            case ERC20:
                token.setIsSupportErc20(true);
                token.setIsSupportErc165(false);
                token.setIsSupportErc721(false);
                token.setIsSupportErc721Enumeration(token.getIsSupportErc721());
                token.setIsSupportErc721Metadata(token.getIsSupportErc721());
                ercCache.getTokenMap().put(contractAddress,token);
                ercCache.getErc20AddressCache().add(contractAddress);
                break;
            case ERC721:
                token.setIsSupportErc20(false);
                token.setIsSupportErc165(true);
                token.setIsSupportErc721(true);
                token.setIsSupportErc721Enumeration(ercDetectService.isSupportErc721Enumerable(contractAddress));
                token.setIsSupportErc721Metadata(ercDetectService.isSupportErc721Metadata(contractAddress));
                ercCache.getTokenMap().put(contractAddress,token);
                ercCache.getErc721AddressCache().add(contractAddress);
                break;
            default:
        }
        return token;
    }

    /**
     * 分析ERC合约交易信息
     * @param transaction
     * @param contractAddress
     */
    public void analyze(CollectionTransaction transaction,String contractAddress) {
        // 先更新缓存
        ErcToken token = updateErcCache(contractAddress);
        // 根据ERC类型调用各自的分析器
        switch (token.getTypeEnum()){
            case ERC20:
                erc20TxAnalyzer.analyze(token);
                break;
            case ERC721:
                erc721TxAnalyzer.analyze(token);
                break;
            default:
        }
    }
}

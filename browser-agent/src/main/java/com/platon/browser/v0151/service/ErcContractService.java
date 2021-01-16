package com.platon.browser.v0151.service;

import com.platon.browser.v0151.analyzer.Erc20TxAnalyzer;
import com.platon.browser.v0151.analyzer.Erc721TxAnalyzer;
import com.platon.browser.v0151.bean.Erc20ContractId;
import com.platon.browser.v0151.bean.ErcToken;
import com.platon.browser.v0151.cache.ErcCache;
import com.platon.browser.v0151.enums.ErcTypeEnum;
import lombok.extern.slf4j.Slf4j;
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
     * @throws Exception
     */
    private ErcToken updateErcCache(String contractAddress) throws Exception {
        boolean isErc721 = ercDetectService.isSupportErc721(contractAddress);
        ErcToken token = new ErcToken();
        token.setTypeEnum(ErcTypeEnum.UNKNOWN);
        token.setAddress(contractAddress);
        Date date = new Date();
        token.setCreateTime(date);
        token.setUpdateTime(date);
        if(isErc721){
            // ERC721 Token
            token.setTypeEnum(ErcTypeEnum.ERC721);
            token.setType(ErcTypeEnum.ERC721.name().toLowerCase());
            token.setIsSupportErc20(false);
            token.setIsSupportErc165(true);
            token.setIsSupportErc721(true);
            token.setIsSupportErc721Enumeration(ercDetectService.isSupportErc721Enumerable(contractAddress));
            token.setIsSupportErc721Metadata(ercDetectService.isSupportErc721Metadata(contractAddress));
            // TODO: 解析出ERC721 Token相关信息
            ercCache.getTokenMap().put(contractAddress,token);
            ercCache.getErc721AddressCache().add(contractAddress);
        } else {
            //
            Erc20ContractId contractId = ercDetectService.detectErc20ContractId(contractAddress);
            if(contractId.isSupportErc20()){
                // 解析ERC20相关信息
                token.setTypeEnum(ErcTypeEnum.ERC20);
                token.setType(ErcTypeEnum.ERC20.name().toLowerCase());
                token.setIsSupportErc20(true);
                token.setIsSupportErc165(false);
                token.setIsSupportErc721(false);
                token.setIsSupportErc721Enumeration(token.getIsSupportErc721());
                token.setIsSupportErc721Metadata(token.getIsSupportErc721());
                token.setName(contractId.getName());
                token.setSymbol(contractId.getSymbol());
                token.setDecimal(contractId.getDecimal());
                token.setTotalSupply(contractId.getTotalSupply());
                ercCache.getTokenMap().put(contractAddress,token);
                ercCache.getErc20AddressCache().add(contractAddress);
            }
        }
        return token;
    }

    public void analyze(String contractAddress) throws Exception {
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

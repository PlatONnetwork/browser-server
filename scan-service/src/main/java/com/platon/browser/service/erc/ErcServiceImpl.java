package com.platon.browser.service.erc;

import cn.hutool.core.text.StrFormatter;
import cn.hutool.core.util.ObjectUtil;
import com.platon.browser.client.PlatOnClient;
import com.platon.browser.enums.ErcTypeEnum;
import com.platon.browser.exception.BusinessException;
import com.platon.browser.v0152.bean.ErcContractId;
import com.platon.browser.v0152.contract.Erc20Contract;
import com.platon.browser.v0152.contract.Erc721Contract;
import com.platon.browser.v0152.contract.ErcContract;
import com.platon.browser.v0152.service.ErcDetectService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class ErcServiceImpl {

    @Resource
    private PlatOnClient platOnClient;

    @Resource
    private ErcDetectService ercDetectService;

    /**
     * 获取地址代币余额, ERC20为金额，ERC721为tokenId数
     *
     * @param tokenAddress 合约地址
     * @param type         合约类型
     * @param account      用户地址
     * @return java.math.BigInteger
     * @date 2021/1/20
     */
    public BigInteger getBalance(String tokenAddress, ErcTypeEnum type, String account) {
        BigInteger balance = BigInteger.ZERO;
        try {
            ErcContract ercContract = getErcContract(tokenAddress, type);
            if (ObjectUtil.isNotNull(ercContract)) {
                balance = ercContract.balanceOf(account).send();
            }
        } catch (Exception e) {
            log.warn(StrFormatter.format("获取地址代币余额异常,contractAddress:{},account:{}", tokenAddress, account), e);
            try {
                TimeUnit.MILLISECONDS.sleep(200);
            } catch (InterruptedException interruptedException) {
                log.warn("InterruptedException异常", interruptedException);
            }
            throw new BusinessException("查询Token余额失败！");
        }
        return balance;
    }

    /**
     * 获取供应总量
     *
     * @param contractAddress 合约地址
     * @return java.math.BigInteger
     * @date 2021/1/18
     */
    public BigInteger getTotalSupply(String contractAddress) {
        BigInteger totalSupply = null;
        try {
            ErcContractId ercContractId = ercDetectService.getContractId(contractAddress);
            ErcContract ercContract = getErcContract(contractAddress, ercContractId.getTypeEnum());
            if (ObjectUtil.isNotNull(ercContract)) {
                totalSupply = ercContract.totalSupply().send();
            }
        } catch (Exception e) {
            log.warn(StrFormatter.format("获取供应总量异常,contractAddress：{}", contractAddress), e);
        }
        return totalSupply;
    }

    /**
     * 根据contractAddress和ercTypeEnum获取对应类型的ErcContract
     *
     * @param contractAddress 合约地址
     * @param ercTypeEnum     合约类型
     * @return com.platon.browser.v0151.contract.ErcContract
     * @date 2021/1/18
     */
    private ErcContract getErcContract(String contractAddress, ErcTypeEnum ercTypeEnum) {
        ErcContract ercContract = null;
        if (ErcTypeEnum.ERC20.equals(ercTypeEnum)) {
            ercContract = Erc20Contract.load(contractAddress, platOnClient.getWeb3jWrapper().getWeb3j(), ErcDetectService.CREDENTIALS, ErcDetectService.GAS_PROVIDER);
        } else if (ErcTypeEnum.ERC721.equals(ercTypeEnum)) {
            ercContract = Erc721Contract.load(contractAddress, platOnClient.getWeb3jWrapper().getWeb3j(), ErcDetectService.CREDENTIALS, ErcDetectService.GAS_PROVIDER);
        }
        return ercContract;
    }

    /**
     * 根据contractAddress和ercTypeEnum获取对应类型的ErcContract
     *
     * @param contractAddress: 合约地址
     * @param ercTypeEnum:     合约类型
     * @param blockNumber:     块高
     * @return: com.platon.browser.v0152.contract.ErcContract
     * @date: 2021/10/25
     */
    private ErcContract getErcContract(String contractAddress, ErcTypeEnum ercTypeEnum, BigInteger blockNumber) {
        ErcContract ercContract = null;
        if (ErcTypeEnum.ERC20.equals(ercTypeEnum)) {
            ercContract = Erc20Contract.load(contractAddress, platOnClient.getWeb3jWrapper().getWeb3j(), ErcDetectService.CREDENTIALS, ErcDetectService.GAS_PROVIDER, blockNumber);
        } else if (ErcTypeEnum.ERC721.equals(ercTypeEnum)) {
            ercContract = Erc721Contract.load(contractAddress, platOnClient.getWeb3jWrapper().getWeb3j(), ErcDetectService.CREDENTIALS, ErcDetectService.GAS_PROVIDER, blockNumber);
        }
        return ercContract;
    }

    /**
     * 获取历史块高的erc20的合约余额
     *
     * @param tokenAddress:
     * @param account:
     * @param blockNumber:
     * @return: java.math.BigInteger
     * @date: 2021/10/25
     */
    public BigInteger getErc20HistoryBalance(String tokenAddress, String account, BigInteger blockNumber) {
        BigInteger balance = BigInteger.ZERO;
        try {
            ErcContract ercContract = getErcContract(tokenAddress, ErcTypeEnum.ERC20, blockNumber);
            if (ObjectUtil.isNotNull(ercContract)) {
                balance = ercContract.balanceOf(account).send();
            }
        } catch (Exception e) {
            log.warn(StrFormatter.format("获取地址代币余额异常,contractAddress:{},account:{}", tokenAddress, account), e);
            try {
                TimeUnit.MILLISECONDS.sleep(200);
            } catch (InterruptedException interruptedException) {
                log.warn("InterruptedException异常", interruptedException);
            }
            throw new BusinessException("查询Token余额失败！");
        }
        return balance;
    }

    /**
     * 获取TokenURI
     *
     * @param contractAddress 合约地址
     * @param tokenId         token id
     * @return java.lang.String
     * @date 2021/1/18
     */
    public String getTokenURI(String contractAddress, BigInteger tokenId) {
        String tokenURI = "";
        try {
            ErcContract ercContract = getErcContract(contractAddress, ErcTypeEnum.ERC721);
            if (ObjectUtil.isNotNull(ercContract)) {
                tokenURI = ercContract.getTokenURI(tokenId).send();
            }
        } catch (Exception e) {
            log.warn(StrFormatter.format("getTokenURI异常，token_address：{},token_id:{}", contractAddress, tokenId), e);
        }
        return tokenURI;
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
    public String getTokenURI(String contractAddress, BigInteger tokenId, BigInteger blockNumber) {
        String tokenURI = "";
        try {
            ErcContract ercContract = getErcContract(contractAddress, ErcTypeEnum.ERC721, blockNumber);
            if (ObjectUtil.isNotNull(ercContract)) {
                tokenURI = ercContract.getTokenURI(tokenId).send();
            }
        } catch (Exception e) {
            log.warn(StrFormatter.format("getTokenURI异常，token_address：{},token_id:{}", contractAddress, tokenId), e);
        }
        return tokenURI;
    }

}

package com.platon.browser.v0151.service;

import com.alaya.crypto.Credentials;
import com.alaya.crypto.Keys;
import com.alaya.protocol.core.DefaultBlockParameterName;
import com.alaya.protocol.core.methods.request.Transaction;
import com.alaya.protocol.core.methods.response.PlatonCall;
import com.alaya.tx.exceptions.ContractCallException;
import com.platon.browser.client.PlatOnClient;
import com.platon.browser.service.erc20.Erc20Contract;
import com.platon.browser.utils.NetworkParams;
import com.platon.browser.v0151.bean.Erc20ContractId;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;

/**
 * Erc探测服务
 */
@Slf4j
@Service
public class ErcDetectService {
    @Resource
    private PlatOnClient platOnClient;
    // 检测输入数据
    private String detectInputData(String contractAddress,String inputData) throws Exception {
        Transaction transaction = Transaction.createEthCallTransaction(Credentials.create(Keys.createEcKeyPair()).getAddress(), contractAddress,inputData);
        PlatonCall platonCall = platOnClient.getWeb3jWrapper().getWeb3j().platonCall(transaction, DefaultBlockParameterName.LATEST).send();
        return platonCall.getResult();
    }
    // 是否支持Erc165标准
    public boolean isSupportErc165(String contractAddress) throws Exception {
        String result = detectInputData(contractAddress,"0x01ffc9a701ffc9a700000000000000000000000000000000000000000000000000000000");
        if(!"0x0000000000000000000000000000000000000000000000000000000000000001".equals(result)){
            return false;
        }
        result = detectInputData(contractAddress,"0x01ffc9a7ffffffff00000000000000000000000000000000000000000000000000000000");
        return "0x0000000000000000000000000000000000000000000000000000000000000000".equals(result);
    }
    public boolean isSupportErc721Metadata(String contractAddress) throws Exception {
        // 支持erc721，则必定要支持erc165
        if(!isSupportErc165(contractAddress)) return false;
        String result = detectInputData(contractAddress,"0x01ffc9a75b5e139f00000000000000000000000000000000000000000000000000000000");
        return "0x0000000000000000000000000000000000000000000000000000000000000001".equals(result);
    }
    public boolean isSupportErc721Enumerable(String contractAddress) throws Exception {
        // 支持erc721，则必定要支持erc165
        if(!isSupportErc165(contractAddress)) return false;
        String result = detectInputData(contractAddress,"0x01ffc9a7780e9d6300000000000000000000000000000000000000000000000000000000");
        return "0x0000000000000000000000000000000000000000000000000000000000000001".equals(result);
    }
    // 是否Erc721合约
    public boolean isSupportErc721(String contractAddress) throws Exception {
        // 支持erc721，则必定要支持erc165
        if(!isSupportErc165(contractAddress)) return false;
        String result = detectInputData(contractAddress,"0x01ffc9a780ac58cd00000000000000000000000000000000000000000000000000000000");
        return "0x0000000000000000000000000000000000000000000000000000000000000001".equals(result);
    }

    // 获取Erc20合约标识
    public Erc20ContractId getErc20ContractId(String contractAddress){
        Erc20ContractId contractId = detectErc20ContractId(contractAddress);
        return contractId;
    }

    // 是否Erc20合约
    public boolean isSupportErc20(String contractAddress){
        Erc20ContractId contractId = getErc20ContractId(contractAddress);
        return contractId.isSupportErc20();
    }

    // 检测Erc20标识信息
    public Erc20ContractId detectErc20ContractId(String contractAddress){
        Erc20ContractId contractId = new Erc20ContractId();
        if (StringUtils.isBlank(contractAddress)) throw new RuntimeException("contractAddress can not be null!");
        Erc20Contract erc20Contract = new Erc20Contract(contractAddress, platOnClient.getWeb3jWrapper().getWeb3j(),
                Credentials.create("4484092b68df58d639f11d59738983e2b8b81824f3c0c759edd6773f9adadfe7"),
                NetworkParams.getChainId());
        try {
            try {
                contractId.setName(erc20Contract.name().send());
            } catch (Exception e) {
                log.error(" erc get name error", e);
            }
            try {
                contractId.setSymbol(erc20Contract.symbol().send());
            } catch (Exception e) {
                log.error(" erc get symbol error", e);
            }
            try {
                contractId.setDecimal(erc20Contract.decimals().send().intValue());
            } catch (Exception e) {
                log.error(" erc get decimal error", e);
            }
            try {
                contractId.setTotalSupply(new BigDecimal(erc20Contract.totalSupply().send()));
            } catch (Exception e) {
                log.error(" erc get totalSupply error", e);
            }
        } catch (ContractCallException e) {
            log.error(" not erc contract,{}", contractAddress, e);
        }
        return contractId;
    }
}

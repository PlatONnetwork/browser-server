package com.platon.browser.v0152.service;

import com.platon.crypto.Credentials;
import com.platon.crypto.Keys;
import com.platon.protocol.core.DefaultBlockParameterName;
import com.platon.protocol.core.methods.request.Transaction;
import com.platon.protocol.core.methods.response.PlatonCall;
import com.platon.protocol.core.methods.response.TransactionReceipt;
import com.platon.tx.exceptions.ContractCallException;
import com.platon.tx.gas.ContractGasProvider;
import com.platon.tx.gas.GasProvider;
import com.platon.browser.client.PlatOnClient;
import com.platon.browser.config.BlockChainConfig;
import com.platon.browser.exception.BusinessException;
import com.platon.browser.v0152.bean.ErcContractId;
import com.platon.browser.v0152.contract.Erc20Contract;
import com.platon.browser.v0152.contract.Erc721Contract;
import com.platon.browser.v0152.contract.ErcContract;
import com.platon.browser.v0152.enums.ErcTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.List;

/**
 * Erc探测服务
 */
@Slf4j
@Service
public class ErcDetectService {

    public static final Credentials CREDENTIALS = Credentials.create("4484092b68df58d639f11d59738983e2b8b81824f3c0c759edd6773f9adadfe7");

    private static final BigInteger GAS_LIMIT = BigInteger.valueOf(2104836);

    private static final BigInteger GAS_PRICE = BigInteger.valueOf(100000000000L);

    public static final GasProvider GAS_PROVIDER = new ContractGasProvider(GAS_PRICE, GAS_LIMIT);

    @Resource
    private PlatOnClient platOnClient;

    @Resource
    private BlockChainConfig chainConfig;

    // 检测输入数据
    private String detectInputData(String contractAddress, String inputData) {
        Transaction transaction = null;
        try {
            transaction = Transaction.createEthCallTransaction(Credentials.create(Keys.createEcKeyPair()).getAddress(), contractAddress, inputData);
        } catch (InvalidAlgorithmParameterException | NoSuchAlgorithmException | NoSuchProviderException e) {
            log.error("", e);
            throw new BusinessException(e.getMessage());
        }
        PlatonCall platonCall = null;
        try {
            platonCall = platOnClient.getWeb3jWrapper().getWeb3j().platonCall(transaction, DefaultBlockParameterName.LATEST).send();
        } catch (IOException e) {
            log.error("", e);
            throw new BusinessException(e.getMessage());
        }
        return platonCall.getResult();
    }

    // 是否支持Erc165标准
    private boolean isSupportErc165(String contractAddress) {
        String result = detectInputData(contractAddress, "0x01ffc9a701ffc9a700000000000000000000000000000000000000000000000000000000");
        if (!"0x0000000000000000000000000000000000000000000000000000000000000001".equals(result)) {
            return false;
        }
        result = detectInputData(contractAddress, "0x01ffc9a7ffffffff00000000000000000000000000000000000000000000000000000000");
        return "0x0000000000000000000000000000000000000000000000000000000000000000".equals(result);
    }

    public boolean isSupportErc721Metadata(String contractAddress) {
        // 支持erc721，则必定要支持erc165
        if (!isSupportErc165(contractAddress))
            return false;
        String result = detectInputData(contractAddress, "0x01ffc9a75b5e139f00000000000000000000000000000000000000000000000000000000");
        return "0x0000000000000000000000000000000000000000000000000000000000000001".equals(result);
    }

    public boolean isSupportErc721Enumerable(String contractAddress) {
        // 支持erc721，则必定要支持erc165
        if (!isSupportErc165(contractAddress))
            return false;
        String result = detectInputData(contractAddress, "0x01ffc9a7780e9d6300000000000000000000000000000000000000000000000000000000");
        return "0x0000000000000000000000000000000000000000000000000000000000000001".equals(result);
    }

    // 是否Erc721合约
    private boolean isSupportErc721(String contractAddress) {
        // 支持erc721，则必定要支持erc165
        if (!isSupportErc165(contractAddress))
            return false;
        String result = detectInputData(contractAddress, "0x01ffc9a780ac58cd00000000000000000000000000000000000000000000000000000000");
        return "0x0000000000000000000000000000000000000000000000000000000000000001".equals(result);
    }

    private ErcContractId getErc20ContractId(String contractAddress) {
        ErcContract ercContract = Erc20Contract.load(contractAddress, platOnClient.getWeb3jWrapper().getWeb3j(), CREDENTIALS, GAS_PROVIDER);
        ErcContractId contractId = resolveContractId(ercContract);
        contractId.setTypeEnum(ErcTypeEnum.ERC20);
        return contractId;
    }

    private ErcContractId getErc721ContractId(String contractAddress) {
        ErcContract ercContract = Erc721Contract.load(contractAddress, platOnClient.getWeb3jWrapper().getWeb3j(),
                CREDENTIALS,
                GAS_PROVIDER);
        ErcContractId contractId = resolveContractId(ercContract);
        contractId.setTypeEnum(ErcTypeEnum.ERC721);
        return contractId;
    }

    // 检测Erc20合约标识
    private ErcContractId resolveContractId(ErcContract ercContract) {
        ErcContractId contractId = new ErcContractId();
        try {
            try {
                contractId.setName(ercContract.name().send());
            } catch (Exception e) {
                log.warn("erc get name error");
                log.debug("", e);
            }
            try {
                contractId.setSymbol(ercContract.symbol().send());
            } catch (Exception e) {
                log.warn("erc get symbol error");
                log.debug("", e);
            }
            try {
                contractId.setDecimal(ercContract.decimals().send().intValue());
            } catch (Exception e) {
                log.warn("erc get decimal error");
                log.debug("", e);
            }
            try {
                contractId.setTotalSupply(new BigDecimal(ercContract.totalSupply().send()));
            } catch (Exception e) {
                log.warn("erc get totalSupply error");
                log.debug("", e);
            }
        } catch (ContractCallException e) {
            log.error(" not erc contract,{}", ercContract, e);
        }
        return contractId;
    }

    public ErcContractId getContractId(String contractAddress) {
        // 先检测是否支持ERC721
        boolean isErc721 = isSupportErc721(contractAddress);
        ErcContractId contractId;
        if (isErc721) {
            // 取ERC721合约信息
            contractId = getErc721ContractId(contractAddress);
        } else {
            // 不是ERC721，则检测是否是ERC20
            contractId = getErc20ContractId(contractAddress);
            if (StringUtils.isBlank(contractId.getName()) || StringUtils.isBlank(contractId.getSymbol()) | contractId.getDecimal() == null || contractId.getTotalSupply() == null) {
                // name/symbol/decimals/totalSupply 其中之一为空，则判定为未知类型
                contractId.setTypeEnum(ErcTypeEnum.UNKNOWN);
            }
        }
        return contractId;
    }

    public List<ErcContract.ErcTxEvent> getErc20TxEvents(TransactionReceipt receipt) {
        ErcContract ercContract = Erc20Contract.load(receipt.getContractAddress(), platOnClient.getWeb3jWrapper().getWeb3j(),
                CREDENTIALS,
                GAS_PROVIDER);
        return ercContract.getTxEvents(receipt);
    }

    public List<ErcContract.ErcTxEvent> getErc721TxEvents(TransactionReceipt receipt) {
        ErcContract ercContract = Erc721Contract.load(receipt.getContractAddress(), platOnClient.getWeb3jWrapper().getWeb3j(),
                CREDENTIALS,
                GAS_PROVIDER);
        return ercContract.getTxEvents(receipt);
    }

}

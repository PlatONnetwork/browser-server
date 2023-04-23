package com.platon.browser.v0152.service;

import cn.hutool.core.util.StrUtil;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.platon.browser.bean.CommonConstant;
import com.platon.browser.client.PlatOnClient;
import com.platon.browser.config.BlockChainConfig;
import com.platon.browser.enums.ErcTypeEnum;
import com.platon.browser.exception.BusinessException;
import com.platon.browser.v0152.bean.ErcContractId;
import com.platon.browser.v0152.contract.Erc1155Contract;
import com.platon.browser.v0152.contract.Erc20Contract;
import com.platon.browser.v0152.contract.Erc721Contract;
import com.platon.browser.v0152.contract.ErcContract;
import com.platon.crypto.Credentials;
import com.platon.crypto.Keys;
import com.platon.parameters.NetworkParameters;
import com.platon.protocol.core.DefaultBlockParameter;
import com.platon.protocol.core.Response;
import com.platon.protocol.core.methods.request.Transaction;
import com.platon.protocol.core.methods.response.PlatonCall;
import com.platon.protocol.core.methods.response.TransactionReceipt;
import com.platon.tx.exceptions.ContractCallException;
import com.platon.tx.exceptions.PlatonCallTimeoutException;
import com.platon.tx.gas.ContractGasProvider;
import com.platon.tx.gas.GasProvider;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
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

    @Resource
    private BlockChainConfig chainConfig;

    public static Credentials CREDENTIALS;

    private static final BigInteger GAS_LIMIT = BigInteger.valueOf(2104836);

    private static final BigInteger GAS_PRICE = BigInteger.valueOf(100000000000L);

    public static final GasProvider GAS_PROVIDER = new ContractGasProvider(GAS_PRICE, GAS_LIMIT);

    @Resource
    private PlatOnClient platOnClient;

    private static Cache<String, ErcContract> ercContractCache = Caffeine.newBuilder()
            .maximumSize(1000)
            .build();

    @PostConstruct
    public void init() {
        NetworkParameters.init(chainConfig.getChainId(), chainConfig.getAddressPrefix());
        CREDENTIALS = Credentials.create("4484092b68df58d639f11d59738983e2b8b81824f3c0c759edd6773f9adadfe7");
    }


    /**
     *
     * @param ercTypeEnum
     * @param contractAddress
     * @param blockNumber
     * @return 如果不符合ERC标准，则返回null
     */
    public ErcContract getErcContract(ErcTypeEnum ercTypeEnum, String contractAddress, BigInteger blockNumber) {
        ErcContract ercContract = ercContractCache.getIfPresent(contractAddress);
        if(ercContract==null) {
            if (ErcTypeEnum.ERC20.equals(ercTypeEnum)) {
                ercContract = Erc20Contract.load(contractAddress, platOnClient.getWeb3jWrapper().getWeb3j(), ErcDetectService.CREDENTIALS, ErcDetectService.GAS_PROVIDER, blockNumber);
            } else if (ErcTypeEnum.ERC721.equals(ercTypeEnum)) {
                ercContract = Erc721Contract.load(contractAddress, platOnClient.getWeb3jWrapper().getWeb3j(), ErcDetectService.CREDENTIALS, ErcDetectService.GAS_PROVIDER, blockNumber);
            } else if (ErcTypeEnum.ERC1155.equals(ercTypeEnum)) {
                ercContract = Erc1155Contract.load(contractAddress, platOnClient.getWeb3jWrapper().getWeb3j(), ErcDetectService.CREDENTIALS, ErcDetectService.GAS_PROVIDER, blockNumber);
            }
            if (ercContract != null) {
                ercContractCache.put(contractAddress, ercContract);
            }
        }
        return ercContract;
    }

    /**
     * 检测输入数据--不带重试机制
     *
     * @param contractAddress
     * @param inputData
     * @return java.lang.String
     * @date 2021/4/30
     */
    @Retryable(value = PlatonCallTimeoutException.class, maxAttempts = CommonConstant.reTryNum)
    private String detectInputData(String contractAddress, String inputData, BigInteger blockNumber) throws PlatonCallTimeoutException {
        Transaction transaction = null;
        PlatonCall platonCall = null;
        try {
            transaction = Transaction.createEthCallTransaction(Credentials.create(Keys.createEcKeyPair()).getAddress(), contractAddress, inputData);
        } catch (InvalidAlgorithmParameterException | NoSuchAlgorithmException | NoSuchProviderException e) {
            log.error(StrUtil.format("合约地址[{}]检测输入数据异常", contractAddress), e);
            throw new BusinessException(e.getMessage());
        }
        try {
            platonCall = platOnClient.getWeb3jWrapper().getWeb3j().platonCall(transaction, DefaultBlockParameter.valueOf(blockNumber)).send();
            if (platonCall.hasError()) {
                Response.Error error = platonCall.getError();
                String message = error.getMessage();
                String lowMessage = !StrUtil.isBlank(message) ? message.toLowerCase() : null;
                // 包含timeout则抛超时异常，其他错误则直接抛出runtime异常
                if (!StrUtil.isBlank(lowMessage) && lowMessage.contains("timeout")) {
                    log.error("合约地址[{}]检测输入数据超时异常.error_code[{}],error_msg[{}]", contractAddress, error.getCode(), error.getMessage());
                    throw new PlatonCallTimeoutException(error.getCode(), error.getMessage(), platonCall);
                }
            }
        } catch (PlatonCallTimeoutException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }
        return platonCall.getResult();
    }

    /**
     * 重试完成还是不成功，会回调该方法
     *
     * @param e:
     * @return: void
     * @date: 2022/5/6
     */
    @Recover
    public String recoverDetectInputData(Exception e) {
        log.error("重试完成还是业务失败，请联系管理员处理");
        return null;
    }

    /**
     * 是否支持Erc165标准
     *
     * @param contractAddress:
     * @param blockNumber:
     * @return: boolean
     * @date: 2021/9/18
     */
    private boolean isSupportErc165(String contractAddress, BigInteger blockNumber) throws PlatonCallTimeoutException {
        String result = detectInputData(contractAddress, "0x01ffc9a701ffc9a700000000000000000000000000000000000000000000000000000000", blockNumber);
        if (!"0x0000000000000000000000000000000000000000000000000000000000000001".equals(result)) {
            return false;
        }
        result = detectInputData(contractAddress, "0x01ffc9a7ffffffff00000000000000000000000000000000000000000000000000000000", blockNumber);
        return "0x0000000000000000000000000000000000000000000000000000000000000000".equals(result);
    }

    public boolean isSupportErc721Metadata(String contractAddress, BigInteger blockNumber) throws PlatonCallTimeoutException {
        // 支持erc721，则必定要支持erc165
        if (!isSupportErc165(contractAddress, blockNumber)) {
            log.debug("该合约[{}]不支持erc165", contractAddress);
            return false;
        }
        String result = detectInputData(contractAddress, "0x01ffc9a75b5e139f00000000000000000000000000000000000000000000000000000000", blockNumber);
        return "0x0000000000000000000000000000000000000000000000000000000000000001".equals(result);
    }


    public boolean isSupportErc721Enumerable(String contractAddress, BigInteger blockNumber) throws PlatonCallTimeoutException {
        // 支持erc721，则必定要支持erc165
        if (!isSupportErc165(contractAddress, blockNumber)) {
            log.debug("该合约[{}]不支持erc165", contractAddress);
            return false;
        }
        String result = detectInputData(contractAddress, "0x01ffc9a7780e9d6300000000000000000000000000000000000000000000000000000000", blockNumber);
        return "0x0000000000000000000000000000000000000000000000000000000000000001".equals(result);
    }


    /**
     * 是否支持Erc721合约
     *
     * @param contractAddress:
     * @param blockNumber:
     * @return: boolean
     * @date: 2022/1/14
     */
    private boolean isSupportErc721(String contractAddress, BigInteger blockNumber) throws PlatonCallTimeoutException {
        // 支持erc721，则必定要支持erc165
        if (!isSupportErc165(contractAddress, blockNumber)) {
            log.debug("该合约[{}]不支持erc165", contractAddress);
            return false;
        }
        String result = detectInputData(contractAddress, "0x01ffc9a780ac58cd00000000000000000000000000000000000000000000000000000000", blockNumber);
        return "0x0000000000000000000000000000000000000000000000000000000000000001".equals(result);
    }

    /**
     * 是否支持Erc1155合约
     *
     * @param contractAddress:
     * @param blockNumber:
     * @return: boolean
     * @date: 2022/2/5
     */
    private boolean isSupportErc1155(String contractAddress, BigInteger blockNumber) throws PlatonCallTimeoutException {
        // 支持erc1155，则必定要支持erc165
        if (!isSupportErc165(contractAddress, blockNumber)) {
            log.debug("该合约[{}]不支持erc165", contractAddress);
            return false;
        }
        String result = detectInputData(contractAddress, "0x01ffc9a7d9b67a2600000000000000000000000000000000000000000000000000000000", blockNumber);
        return "0x0000000000000000000000000000000000000000000000000000000000000001".equals(result);
    }

    public Boolean isSupportErc1155Metadata(String contractAddress, BigInteger blockNumber) throws PlatonCallTimeoutException {
        // 支持erc1155，则必定要支持erc165
        if (!isSupportErc165(contractAddress, blockNumber)) {
            log.debug("该合约[{}]不支持erc165", contractAddress);
            return false;
        }
        String result = detectInputData(contractAddress, "0x01ffc9a70e89341c00000000000000000000000000000000000000000000000000000000", blockNumber);
        return "0x0000000000000000000000000000000000000000000000000000000000000001".equals(result);
    }


    // 检测Erc20合约标识
    private ErcContractId resolveContractId(ErcContract ercContract) throws PlatonCallTimeoutException {
        ErcContractId contractId = new ErcContractId();
        try {
            try {
                contractId.setName(ercContract.name().send());
            } catch (PlatonCallTimeoutException e) {
                log.error("ERC获取name超时异常", e);
                throw e;
            } catch (Exception e) {
                log.warn("erc get name error", e);
            }
            try {
                contractId.setSymbol(ercContract.symbol().send());
            } catch (PlatonCallTimeoutException e) {
                log.error("ERC获取symbol超时异常", e);
                throw e;
            } catch (Exception e) {
                log.warn("erc get symbol error", e);
            }
            try {
                contractId.setDecimal(ercContract.decimals().send().intValue());
            } catch (PlatonCallTimeoutException e) {
                log.error("ERC获取decimal超时异常", e);
                throw e;
            } catch (Exception e) {
                log.warn("erc get decimal error", e);
            }
            try {
                contractId.setTotalSupply(new BigDecimal(ercContract.totalSupply().send()));
            } catch (PlatonCallTimeoutException e) {
                log.error("ERC获取totalSupply超时异常", e);
                throw e;
            } catch (Exception e) {
                log.warn("erc get totalSupply error", e);
            }
        } catch (PlatonCallTimeoutException e) {
            throw e;
        } catch (ContractCallException e) {
            log.error(" not erc contract,{}", ercContract, e);
        }
        return contractId;
    }

    /**
     *
     * @param contractAddress
     * @param blockNumber
     * @return 如果不符合ERC标准，则返回null
     * @throws PlatonCallTimeoutException
     */
    @Retryable(value = PlatonCallTimeoutException.class, maxAttempts = CommonConstant.reTryNum)
    public ErcContractId getContractId(String contractAddress, BigInteger blockNumber) throws PlatonCallTimeoutException {
        ErcTypeEnum ercType = ErcTypeEnum.ERC20;
        try {
            // 先检测是否支持ERC721
            if (isSupportErc721(contractAddress, blockNumber)) {
                ercType = ErcTypeEnum.ERC721;
            }else if (isSupportErc1155(contractAddress, blockNumber)){
                ercType = ErcTypeEnum.ERC1155;
            }

            ErcContract ercContract = getErcContract(ercType, contractAddress, blockNumber);
            if(ercContract==null){
                return null;
                //throw new BusinessException("cannot find the Contract on the Chain");
            }
            ErcContractId contractId = resolveContractId(ercContract);


            if (ercType == ErcTypeEnum.ERC20) {
                if (StringUtils.isBlank(contractId.getName()) || StringUtils.isBlank(contractId.getSymbol()) | contractId.getDecimal() == null || contractId.getTotalSupply() == null) {
                    // name/symbol/decimals/totalSupply 其中之一为空，则判定为未知类型
                    //contractId.setTypeEnum(ErcTypeEnum.UNKNOWN);
                    return null;
                }
            }

            contractId.setTypeEnum(ercType);
            return contractId;

        } catch (PlatonCallTimeoutException e) {
            log.error("获取合约[{}]id超时异常", contractAddress);
            throw e;
        } catch (Exception e) {
            log.error(StrUtil.format("获取合约[{}]id异常", contractAddress), e);
            throw e;
        }
    }

    /**
     * 重试完成还是不成功，会回调该方法
     *
     * @param e:
     * @return: void
     * @date: 2022/5/6
     */
    @Recover
    public ErcContractId recover(Exception e) {
        log.error("重试完成还是业务失败，请联系管理员处理");
        return null;
    }

    public List<ErcContract.ErcTxEvent> getErc20TransferTxEvents(TransactionReceipt receipt, BigInteger blockNumber) {
        //ErcContract ercContract = Erc20Contract.load(receipt.getContractAddress(), platOnClient.getWeb3jWrapper().getWeb3j(), CREDENTIALS, GAS_PROVIDER, blockNumber);
        ErcContract ercContract = getErcContract(ErcTypeEnum.ERC20, receipt.getContractAddress(), blockNumber);
        if (ercContract==null){
            log.error("can not load ERC20 contract：{} at blockNumber: {}", receipt.getContractAddress(), blockNumber);
            throw new RuntimeException("can not load ERC20 contract");
        }
        return ercContract.getTxEvents(receipt);
    }

    public List<ErcContract.ErcTxEvent> getErc721TransferTxEvents(TransactionReceipt receipt, BigInteger blockNumber) {
        //ErcContract ercContract = Erc721Contract.load(receipt.getContractAddress(), platOnClient.getWeb3jWrapper().getWeb3j(), CREDENTIALS, GAS_PROVIDER, blockNumber);
        ErcContract ercContract = getErcContract(ErcTypeEnum.ERC721, receipt.getContractAddress(), blockNumber);
        if (ercContract==null){
            log.error("can not load ERC721 contract：{} at blockNumber: {}", receipt.getContractAddress(), blockNumber);
            throw new RuntimeException("can not load ERC721 contract");
        }
        return ercContract.getTxEvents(receipt);
    }

    public List<ErcContract.ErcTxEvent> getErc1155TransferTxEvents(TransactionReceipt receipt, BigInteger blockNumber) {
        //ErcContract ercContract = Erc1155Contract.load(receipt.getContractAddress(), platOnClient.getWeb3jWrapper().getWeb3j(), CREDENTIALS, GAS_PROVIDER, blockNumber);
        ErcContract ercContract = getErcContract(ErcTypeEnum.ERC1155, receipt.getContractAddress(), blockNumber);
        if (ercContract==null){
            log.error("can not load ERC1155 contract：{} at blockNumber: {}", receipt.getContractAddress(), blockNumber);
            throw new RuntimeException("can not load ERC1155 contract");
        }
        return ercContract.getTxEvents(receipt);
    }
}

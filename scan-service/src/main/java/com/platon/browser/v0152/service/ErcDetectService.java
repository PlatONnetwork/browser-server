package com.platon.browser.v0152.service;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.platon.browser.bean.ContractInfo;
import com.platon.browser.client.PlatOnClient;
import com.platon.browser.config.BlockChainConfig;
import com.platon.browser.enums.ErcTypeEnum;
import com.platon.browser.v0152.bean.ErcContractId;
import com.platon.browser.v0152.contract.Erc1155Contract;
import com.platon.browser.v0152.contract.Erc20Contract;
import com.platon.browser.v0152.contract.Erc721Contract;
import com.platon.browser.v0152.contract.ErcContract;
import com.platon.crypto.Credentials;
import com.platon.parameters.NetworkParameters;
import com.platon.protocol.core.methods.response.TransactionReceipt;
import com.platon.tx.exceptions.PlatonCallTimeoutException;
import com.platon.tx.gas.ContractGasProvider;
import com.platon.tx.gas.GasProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Recover;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.BigInteger;
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
     * 实例化ercContract，并放入cache，以便后续在链上调用合约的方法
     *
     * @param ercTypeEnum
     * @param contractAddress
     * @param blockNumber
     * @return 如果不符合ERC标准，则返回null
     */
    public ErcContract getErcContract(ErcTypeEnum ercTypeEnum, String contractAddress, BigInteger blockNumber){
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

    public ErcContractId resolveContractId(ErcContract ercContract) {
        ErcContractId contractId = new ErcContractId();
        //只要是erc token，都尝试获取以下基本属性，是为了兼容早期非标准的erc token
        try {
            contractId.setName(ercContract.name().send());
        } catch (Exception e) {
            log.warn("获取ERC Token Name出错", e);
        }
        try {
            contractId.setSymbol(ercContract.symbol().send());
        } catch (Exception e) {
            log.warn("获取ERC Token Symbol出错", e);
        }
        try {
            contractId.setDecimal(ercContract.decimals().send().intValue());

        } catch (Exception e) {
            log.warn("获取ERC Token Decimal出错", e);
        }
        try {
            contractId.setTotalSupply(new BigDecimal(ercContract.totalSupply().send()));
        }catch (Exception e){
            log.warn("获取ERC Token TotalSupply出错", e);
        }

        return contractId;
    }

    // 检测Erc20合约标识
    /*private ErcContractId resolveContractId(ErcContract ercContract) throws PlatonCallTimeoutException {
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
    }*/
    public ErcContractId getErcContractId(String contractAddress, BigInteger blockNumber, ErcTypeEnum ercType) throws PlatonCallTimeoutException {
        ErcContract ercContract = null;
        switch (ercType) {
            case ERC20:
                ercContract = Erc20Contract.load(contractAddress, platOnClient.getWeb3jWrapper().getWeb3j(), ErcDetectService.CREDENTIALS, ErcDetectService.GAS_PROVIDER, blockNumber);
                break;
            case ERC721:
                ercContract = Erc721Contract.load(contractAddress, platOnClient.getWeb3jWrapper().getWeb3j(), ErcDetectService.CREDENTIALS, ErcDetectService.GAS_PROVIDER, blockNumber);
                break;
            case ERC1155:
                ercContract = Erc1155Contract.load(contractAddress, platOnClient.getWeb3jWrapper().getWeb3j(), ErcDetectService.CREDENTIALS, ErcDetectService.GAS_PROVIDER, blockNumber);
                break;
        }
        ercContractCache.put(contractAddress, ercContract);

        ErcContractId contractId = resolveContractId(ercContract);
        contractId.setTypeEnum(ercType);

        return contractId;
    }

    public ErcContractId getErcContractIdFromContractInfo(ErcTypeEnum ercTypeEnum, ContractInfo contractInfo) throws PlatonCallTimeoutException {
        ErcContractId contractId = new ErcContractId();
        contractId.setName(contractInfo.getTokenName());
        contractId.setTypeEnum(ercTypeEnum);
        contractId.setSymbol(contractInfo.getTokenSymbol());
        contractId.setDecimal(contractInfo.getTokenDecimals());
        contractId.setTotalSupply(contractInfo.getTokenTotalSupply());
        return contractId;
    }

    /*public ErcContractId getErcContractId(String contractAddress, BigInteger blockNumber, String binHexNo0x) throws PlatonCallTimeoutException {
        if (isSupportErc721(contractAddress, blockNumber)) {
            return getErc721ContractId(contractAddress, blockNumber);
        } else if (isSupportErc1155(contractAddress, blockNumber)) {
            return getErc1155ContractId(contractAddress, blockNumber);
        } else {
            return getErc20ContractId(contractAddress, blockNumber, binHexNo0x);
        }
    }*/

    /**
     * 获取erc token合约的name,symbol/decimals/totalSupply等属性，并构建响应的ErcContract对象，放入ercContractCache缓存,为后续解析此token的交易做准备
     *
     * @param contractAddress   erc token地址
     * @param blockNumber        erc token创建块高
     * @return  如果不是erc20合约，即不能获取erc token合约的name,symbol/decimals/totalSupply等属性,则返回null
     */
    /*private ErcContractId getErc20ContractId(String contractAddress, BigInteger blockNumber, String binHexNo0x) throws PlatonCallTimeoutException {
        //  有可能contractAddress的合约，本身就是erc20，也可能是某个erc20合约的代理
        // 不管如何，假设contractAddress是erc20，并尝试加载合约并请求name,symbol/decimals/totalSupply等属性，只要能获取到这些属性，则认为是erc20；否则就不是erc20（即使从bin判断就是erc20合约）
        Erc20Contract erc20Contract = Erc20Contract.load(contractAddress, platOnClient.getWeb3jWrapper().getWeb3j(), ErcDetectService.CREDENTIALS, ErcDetectService.GAS_PROVIDER, blockNumber);
        ErcContractId contractId = resolveContractId(erc20Contract);
        if (StringUtils.isBlank(contractId.getName()) || StringUtils.isBlank(contractId.getSymbol()) || contractId.getDecimal() == null || contractId.getTotalSupply() == null) {
            return null;
        }else{
            ercContractCache.put(contractAddress, erc20Contract);
            contractId.setTypeEnum(ErcTypeEnum.ERC20);
            return contractId;
        }
    }*/

    /**
     * 从bin判断是否实现了erc20标准。
     * @param binHex
     * @return
     */
    /*private boolean isSupportErc20(String binHex){
        if (binHex.contains("06fdde03") && binHex.contains("95d89b41") && binHex.contains("313ce567") && binHex.contains("18160ddd")) {
            return true;
        }else{
            return false;
        }
    }*/
    /**
     * 获取erc token合约的name,symbol/decimals/totalSupply等属性，并构建响应的ErcContract对象，放入ercContractCache缓存,为后续解析此token的交易做准备
     *
     * @param contractAddress   erc token地址
     * @param blockNumber        erc token创建块高
     * @return  如果不能获取erc token合约的name,symbol/decimals/totalSupply等属性,则返回null
     */
    /*private ErcContractId getErc721ContractId(String contractAddress, BigInteger blockNumber) throws PlatonCallTimeoutException {
        Erc721Contract  ercContract = Erc721Contract.load(contractAddress, platOnClient.getWeb3jWrapper().getWeb3j(), ErcDetectService.CREDENTIALS, ErcDetectService.GAS_PROVIDER, blockNumber);
        ErcContractId contractId = resolveContractId(ercContract);
        contractId.setTypeEnum(ErcTypeEnum.ERC721);

        ercContractCache.put(contractAddress, ercContract);
        return contractId;
    }*/
    /**
     * 获取erc token合约的name,symbol/decimals/totalSupply等属性，并构建响应的ErcContract对象，放入ercContractCache缓存,为后续解析此token的交易做准备
     *
     * @param contractAddress   erc token地址
     * @param blockNumber        erc token创建块高
     * @return  如果不能获取erc token合约的name,symbol/decimals/totalSupply等属性,则返回null
     */
    /*private ErcContractId getErc1155ContractId(String contractAddress, BigInteger blockNumber) throws PlatonCallTimeoutException {
        Erc1155Contract  ercContract = Erc1155Contract.load(contractAddress, platOnClient.getWeb3jWrapper().getWeb3j(), ErcDetectService.CREDENTIALS, ErcDetectService.GAS_PROVIDER, blockNumber);
        ErcContractId contractId = resolveContractId(ercContract);
        contractId.setTypeEnum(ErcTypeEnum.ERC1155);

        ercContractCache.put(contractAddress, ercContract);
        return contractId;
    }*/

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

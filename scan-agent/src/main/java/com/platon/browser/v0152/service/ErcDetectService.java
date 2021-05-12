package com.platon.browser.v0152.service;

import cn.hutool.core.util.StrUtil;
import com.platon.browser.client.PlatOnClient;
import com.platon.browser.exception.BusinessException;
import com.platon.browser.v0152.bean.ErcContractId;
import com.platon.browser.v0152.contract.Erc20Contract;
import com.platon.browser.v0152.contract.Erc721Contract;
import com.platon.browser.v0152.contract.ErcContract;
import com.platon.browser.v0152.enums.ErcTypeEnum;
import com.platon.browser.v0152.retry.ErcBackOffPolicy;
import com.platon.browser.v0152.retry.ErcRetryPolicy;
import com.platon.crypto.Credentials;
import com.platon.crypto.Keys;
import com.platon.protocol.core.DefaultBlockParameterName;
import com.platon.protocol.core.Response;
import com.platon.protocol.core.methods.request.Transaction;
import com.platon.protocol.core.methods.response.PlatonCall;
import com.platon.protocol.core.methods.response.TransactionReceipt;
import com.platon.tx.exceptions.ContractCallException;
import com.platon.tx.exceptions.PlatonCallException;
import com.platon.tx.exceptions.PlatonCallTimeoutException;
import com.platon.tx.gas.ContractGasProvider;
import com.platon.tx.gas.GasProvider;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

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

    /**
     * 最大重试次数
     */
    private static final int retryMaxNum = 3;

    /**
     * 检测输入数据---带重试机制
     * 超时异常--无限重试，业务异常--重试3次
     *
     * @param contractAddress 合约地址
     * @param inputData       input数据
     * @return java.lang.String
     * @author huangyongpeng@matrixelements.com
     * @date 2021/4/28
     */
    private String detectInputData(String contractAddress, String inputData) {
        String res = "";
        try {
            AtomicLong inputMark = new AtomicLong(0);
            RetryTemplate retryTemplate = new RetryTemplate();
            retryTemplate.setRetryPolicy(ErcRetryPolicy.factory());
            retryTemplate.setBackOffPolicy(ErcBackOffPolicy.factory());
            res = retryTemplate.execute((RetryCallback<String, Throwable>) context -> {
                        // 需要重试的逻辑代码
                        String data = "";
                        try {
                            if (inputMark.intValue() <= retryMaxNum) {
                                if (context.getRetryCount() > 0) {
                                    log.warn("当前重试次数为{},标识为{}", context.getRetryCount(), inputMark.intValue());
                                }
                                data = detectInputDataWithNotRetry(contractAddress, inputData);
                            } else {
                                log.warn("合约地址[{}]检测输入，重试超过3次，将不再重试", contractAddress);
                            }
                        } catch (PlatonCallTimeoutException e) {
                            throw e;
                        } catch (Exception e) {
                            inputMark.incrementAndGet();
                            throw e;
                        }
                        return data;
                    }, context -> {
                        // 重试失败后执行的代码
                        log.error("第[{}]次重试失败", context.getRetryCount() + 1);
                        return "";
                    }
            );
        } catch (Throwable throwable) {
            log.error("重试异常", throwable);
        }
        return res;
    }

    /**
     * 检测输入数据--不带重试机制
     *
     * @param contractAddress
     * @param inputData
     * @return java.lang.String
     * @author huangyongpeng@matrixelements.com
     * @date 2021/4/30
     */
    private String detectInputDataWithNotRetry(String contractAddress, String inputData) throws PlatonCallTimeoutException {
        log.info("合约地址[{}]检测输入数据[{}]", contractAddress, inputData);
        Transaction transaction = null;
        PlatonCall platonCall = null;
        try {
            transaction = Transaction.createEthCallTransaction(Credentials.create(Keys.createEcKeyPair()).getAddress(), contractAddress, inputData);
        } catch (InvalidAlgorithmParameterException | NoSuchAlgorithmException | NoSuchProviderException e) {
            log.error(StrUtil.format("合约地址[{}]检测输入数据异常", contractAddress), e);
            throw new BusinessException(e.getMessage());
        }
        try {
            platonCall = platOnClient.getWeb3jWrapper().getWeb3j().platonCall(transaction, DefaultBlockParameterName.LATEST).send();
            if (platonCall.hasError()) {
                Response.Error error = platonCall.getError();
                String message = error.getMessage();
                String lowMessage = !StrUtil.isBlank(message) ? message.toLowerCase() : null;
                // 包含timeout则抛超时异常，其他错误则直接抛出runtime异常
                if (!StrUtil.isBlank(lowMessage) && lowMessage.contains("timeout")) {
                    log.error("合约地址[{}]检测输入数据异常.error_code[{}],error_msg[{}]", contractAddress, error.getCode(), error.getMessage());
                    throw new PlatonCallTimeoutException(error.getCode(), error.getMessage(), platonCall);
                } else {
                    log.error("合约地址[{}]检测输入数据异常.error_code[{}],error_msg[{}]", contractAddress, error.getCode(), error.getMessage());
                    throw new PlatonCallException(error.getCode(), error.getMessage(), platonCall);
                }
            }
        } catch (PlatonCallTimeoutException e) {
            throw e;
        } catch (Exception e) {
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

    private ErcContractId getErc20ContractId(String contractAddress) throws PlatonCallTimeoutException {
        ErcContract ercContract = Erc20Contract.load(contractAddress, platOnClient.getWeb3jWrapper().getWeb3j(), CREDENTIALS, GAS_PROVIDER);
        ErcContractId contractId = resolveContractId(ercContract);
        contractId.setTypeEnum(ErcTypeEnum.ERC20);
        return contractId;
    }

    private ErcContractId getErc721ContractId(String contractAddress) throws PlatonCallTimeoutException {
        ErcContract ercContract = Erc721Contract.load(contractAddress, platOnClient.getWeb3jWrapper().getWeb3j(),
                CREDENTIALS,
                GAS_PROVIDER);
        ErcContractId contractId = resolveContractId(ercContract);
        contractId.setTypeEnum(ErcTypeEnum.ERC721);
        return contractId;
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
                log.warn("erc get name error");
                log.debug("", e);
            }
            try {
                contractId.setSymbol(ercContract.symbol().send());
            } catch (PlatonCallTimeoutException e) {
                log.error("ERC获取symbol超时异常", e);
                throw e;
            } catch (Exception e) {
                log.warn("erc get symbol error");
                log.debug("", e);
            }
            try {
                contractId.setDecimal(ercContract.decimals().send().intValue());
            } catch (PlatonCallTimeoutException e) {
                log.error("ERC获取decimal超时异常", e);
                throw e;
            } catch (Exception e) {
                log.warn("erc get decimal error");
                log.debug("", e);
            }
            try {
                contractId.setTotalSupply(new BigDecimal(ercContract.totalSupply().send()));
            } catch (PlatonCallTimeoutException e) {
                log.error("ERC获取totalSupply超时异常", e);
                throw e;
            } catch (Exception e) {
                log.warn("erc get totalSupply error");
                log.debug("", e);
            }
        } catch (PlatonCallTimeoutException e) {
            throw e;
        } catch (ContractCallException e) {
            log.error(" not erc contract,{}", ercContract, e);
        }
        return contractId;
    }

    public ErcContractId getContractId(String contractAddress) {
        ErcContractId res = null;
        try {
            AtomicLong contractIdMark = new AtomicLong(0);
            RetryTemplate retryTemplate = new RetryTemplate();
            retryTemplate.setRetryPolicy(ErcRetryPolicy.factory());
            retryTemplate.setBackOffPolicy(ErcBackOffPolicy.factory());
            res = retryTemplate.execute((RetryCallback<ErcContractId, Throwable>) context -> {
                        // 需要重试的逻辑代码
                        ErcContractId ercContractId = null;
                        try {
                            if (contractIdMark.intValue() <= retryMaxNum) {
                                if (context.getRetryCount() > 0) {
                                    log.warn("当前重试次数为{}，标识为[{}]", context.getRetryCount(), contractIdMark.intValue());
                                }
                                ercContractId = getContractIdWithNotRetry(contractAddress);
                            } else {
                                log.warn("合约地址[{}]获取合约id，重试超过3次，将不再重试", contractAddress);
                                ercContractId = new ErcContractId();
                                ercContractId.setTypeEnum(ErcTypeEnum.UNKNOWN);
                            }
                        } catch (PlatonCallTimeoutException e) {
                            throw e;
                        } catch (Exception e) {
                            contractIdMark.incrementAndGet();
                            throw e;
                        }
                        return ercContractId;
                    }, context -> {
                        // 重试失败后执行的代码
                        log.error("第[{}]次重试失败，重试标识为{}", context.getRetryCount() + 1, contractIdMark);
                        ErcContractId contractId = new ErcContractId();
                        contractId.setTypeEnum(ErcTypeEnum.UNKNOWN);
                        return contractId;
                    }
            );
        } catch (Throwable throwable) {
            log.error("重试异常", throwable);
        }
        return res;
    }

    public ErcContractId getContractIdWithNotRetry(String contractAddress) throws PlatonCallTimeoutException {
        log.info("获取合约地址[{}]id", contractAddress);
        ErcContractId contractId = null;
        try {
            // 先检测是否支持ERC721
            boolean isErc721 = isSupportErc721(contractAddress);
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
        } catch (PlatonCallTimeoutException e) {
            throw e;
        } catch (Exception e) {
            throw e;
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

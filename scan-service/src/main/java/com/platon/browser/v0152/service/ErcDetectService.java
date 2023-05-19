package com.platon.browser.v0152.service;

import cn.hutool.core.util.StrUtil;
import com.platon.browser.bean.CommonConstant;
import com.platon.browser.client.PlatOnClient;
import com.platon.browser.config.BlockChainConfig;
import com.platon.browser.enums.ErcTypeEnum;
import com.platon.browser.exception.BusinessException;
import com.platon.browser.v0152.bean.ErcContractId;
import com.platon.browser.v0152.contract.*;
import com.platon.crypto.Credentials;
import com.platon.crypto.Keys;
import com.platon.parameters.NetworkParameters;
import com.platon.protocol.core.DefaultBlockParameter;
import com.platon.protocol.core.DefaultBlockParameterName;
import com.platon.protocol.core.Response;
import com.platon.protocol.core.methods.request.Transaction;
import com.platon.protocol.core.methods.response.PlatonCall;
import com.platon.protocol.core.methods.response.TransactionReceipt;
import com.platon.tuples.generated.Tuple3;
import com.platon.tuples.generated.Tuple4;
import com.platon.tuples.generated.Tuple5;
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

    @PostConstruct
    public void init() {
        NetworkParameters.init(chainConfig.getChainId(), chainConfig.getAddressPrefix());
        CREDENTIALS = Credentials.create("4484092b68df58d639f11d59738983e2b8b81824f3c0c759edd6773f9adadfe7");
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
    private String detectInputData(String contractAddress, String inputData) throws PlatonCallTimeoutException {
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
     * @return: boolean
     * @date: 2021/9/18
     */
    private boolean isSupportErc165(String contractAddress) throws PlatonCallTimeoutException {
        String result = detectInputData(contractAddress, "0x01ffc9a701ffc9a700000000000000000000000000000000000000000000000000000000");
        if (!"0x0000000000000000000000000000000000000000000000000000000000000001".equals(result)) {
            return false;
        }
        result = detectInputData(contractAddress, "0x01ffc9a7ffffffff00000000000000000000000000000000000000000000000000000000");
        return "0x0000000000000000000000000000000000000000000000000000000000000000".equals(result);
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

    public boolean isSupportErc721MetadataOnly(String contractAddress, BigInteger blockNumber) throws PlatonCallTimeoutException {
        String result = detectInputData(contractAddress, "0x01ffc9a75b5e139f00000000000000000000000000000000000000000000000000000000", blockNumber);
        return "0x0000000000000000000000000000000000000000000000000000000000000001".equals(result);
    }

    public boolean isSupportErc721Enumerable(String contractAddress) throws PlatonCallTimeoutException {
        // 支持erc721，则必定要支持erc165
        if (!isSupportErc165(contractAddress)) {
            log.info("该合约[{}]不支持erc165", contractAddress);
            return false;
        }
        String result = detectInputData(contractAddress, "0x01ffc9a7780e9d6300000000000000000000000000000000000000000000000000000000");
        return "0x0000000000000000000000000000000000000000000000000000000000000001".equals(result);
    }


    public boolean isSupportErc721EnumerableOnly(String contractAddress, BigInteger blockNumber) throws PlatonCallTimeoutException {
        String result = detectInputData(contractAddress, "0x01ffc9a7780e9d6300000000000000000000000000000000000000000000000000000000", blockNumber);
        return "0x0000000000000000000000000000000000000000000000000000000000000001".equals(result);
    }

    /**
     * 是否支持Erc721合约
     *
     * @param contractAddress:
     * @return: boolean
     * @date: 2022/1/14
     */
    private boolean isSupportErc721(String contractAddress) throws PlatonCallTimeoutException {
        // 支持erc721，则必定要支持erc165
        if (!isSupportErc165(contractAddress)) {
            log.info("该合约[{}]不支持erc165", contractAddress);
            return false;
        }
        String result = detectInputData(contractAddress, "0x01ffc9a780ac58cd00000000000000000000000000000000000000000000000000000000");
        return "0x0000000000000000000000000000000000000000000000000000000000000001".equals(result);
    }


    private boolean isSupportErc721Only(String contractAddress, BigInteger blockNumber) throws PlatonCallTimeoutException {
        String result = detectInputData(contractAddress, "0x01ffc9a780ac58cd00000000000000000000000000000000000000000000000000000000", blockNumber);
        return "0x0000000000000000000000000000000000000000000000000000000000000001".equals(result);
    }

    /**
     * 是否支持Erc1155合约
     *
     * @param contractAddress:
     * @return: boolean
     * @date: 2022/2/5
     */
    private boolean isSupportErc1155(String contractAddress) throws PlatonCallTimeoutException {
        // 支持erc1155，则必定要支持erc165
        if (!isSupportErc165(contractAddress)) {
            log.info("该合约[{}]不支持erc165", contractAddress);
            return false;
        }
        String result = detectInputData(contractAddress, "0x01ffc9a7d9b67a2600000000000000000000000000000000000000000000000000000000");
        return "0x0000000000000000000000000000000000000000000000000000000000000001".equals(result);
    }

    private boolean isSupportErc1155Only(String contractAddress, BigInteger blockNumber) throws PlatonCallTimeoutException {
        String result = detectInputData(contractAddress, "0x01ffc9a7d9b67a2600000000000000000000000000000000000000000000000000000000", blockNumber);
        return "0x0000000000000000000000000000000000000000000000000000000000000001".equals(result);
    }

    public Boolean isSupportErc1155MetadataOnly(String contractAddress, BigInteger blockNumber) throws PlatonCallTimeoutException {
        String result = detectInputData(contractAddress, "0x01ffc9a70e89341c00000000000000000000000000000000000000000000000000000000", blockNumber);
        return "0x0000000000000000000000000000000000000000000000000000000000000001".equals(result);
    }

    private ErcContractId getErc20ContractId(String contractAddress) throws PlatonCallTimeoutException {
        ErcContract ercContract = Erc20Contract.load(contractAddress, platOnClient.getWeb3jWrapper().getWeb3j(), CREDENTIALS, GAS_PROVIDER);
        ErcContractId contractId = resolveContractId(ercContract);
        contractId.setTypeEnum(ErcTypeEnum.ERC20);
        return contractId;
    }

    private ErcContractId getErc721ContractId(String contractAddress) throws PlatonCallTimeoutException {
        ErcContract ercContract = Erc721Contract.load(contractAddress, platOnClient.getWeb3jWrapper().getWeb3j(), CREDENTIALS, GAS_PROVIDER);
        ErcContractId contractId = resolveContractId(ercContract);
        contractId.setTypeEnum(ErcTypeEnum.ERC721);
        return contractId;
    }

    private ErcContractId getErc1155ContractId(String contractAddress) throws PlatonCallTimeoutException {
        ErcContract ercContract = Erc721Contract.load(contractAddress, platOnClient.getWeb3jWrapper().getWeb3j(), CREDENTIALS, GAS_PROVIDER);
        ErcContractId contractId = resolveContractId(ercContract);
        contractId.setTypeEnum(ErcTypeEnum.ERC1155);
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
                contractId.setTotalSupply(ercContract.totalSupply().send().toString());
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

    @Retryable(value = PlatonCallTimeoutException.class, maxAttempts = CommonConstant.reTryNum)
    public ErcContractId getContractId(String contractAddress) throws PlatonCallTimeoutException {
        ErcContractId contractId = null;
        try {
            // 先检测是否支持ERC721
            boolean isErc721 = isSupportErc721(contractAddress);
            if (isErc721) {
                // 取ERC721合约信息
                log.info("该合约[{}]是721合约", contractAddress);
                return getErc721ContractId(contractAddress);
            }


            boolean isErc1155 = isSupportErc1155(contractAddress);
            if (isErc1155) {
                // 取ERC721合约信息
                log.info("该合约[{}]是1155合约", contractAddress);
                return getErc1155ContractId(contractAddress);
            }

            // 不是ERC721，则检测是否是ERC20
            log.info("该合约[{}]不是721合约，开始检测是否是ERC20", contractAddress);
            contractId = getErc20ContractId(contractAddress);
            if (StringUtils.isBlank(contractId.getName()) || StringUtils.isBlank(contractId.getSymbol()) | contractId.getDecimal() == null || contractId.getTotalSupply() == null) {
                // name/symbol/decimals/totalSupply 其中之一为空，则判定为未知类型
                contractId.setTypeEnum(ErcTypeEnum.UNKNOWN);
            }

        } catch (PlatonCallTimeoutException e) {
            log.error("获取合约[{}]id超时异常", contractAddress);
            throw e;
        } catch (Exception e) {
            log.error(StrUtil.format("获取合约[{}]id异常", contractAddress), e);
            throw e;
        }
        return contractId;
    }

    @Retryable(value = PlatonCallTimeoutException.class, maxAttempts = CommonConstant.reTryNum)
    public ErcContractId getContractId(String contractAddress, BigInteger blockNumber) throws Exception {
        try {
            Tuple5<BigInteger, String, String, BigInteger, BigInteger> ercInfo;
            if(blockNumber.compareTo(chainConfig.getDetectContractBlockNumber()) > 0){
                ercInfo = getErcInfoForContract(contractAddress, blockNumber);
            } else {
                ercInfo = getErcInfoForLocal(contractAddress, blockNumber);
            }
            return ercInfo2ErcContractId(ercInfo);
        } catch (PlatonCallTimeoutException e) {
            log.error("获取合约[{}]id超时异常", contractAddress);
            throw e;
        } catch (Exception e) {
            log.error(StrUtil.format("获取合约[{}]id异常", contractAddress), e);
            throw e;
        }
    }

    /**
     * info contract :                  [ 0, '', '', 0, BigNumber { value: "0" } ]
     * info address :                   [ 0, '', '', 0, BigNumber { value: "0" } ]
     * info ERC20 :                     [ 1, 'MyToken', 'MTK', 18, BigNumber { value: "0" } ]
     * info ERC721 :                    [ 6, '', '', 0, BigNumber { value: "0" } ]
     * info ERC721MetaData :            [ 14, 'MockERC721MetaData', 'MTK', 0, BigNumber { value: "0" } ]
     * info ERC721MetaDataEnumerable :  [ 30, 'MyToken', 'MTK', 0, BigNumber { value: "0" } ]
     * info ERC1155 :                   [ 34, '', '', 0, BigNumber { value: "0" } ]
     * info ERC1155MetaData :           [ 98, '', '', 0, BigNumber { value: "0" } ]
     * info ERC1155Customize :          [ 98, 'MockERC1155Customize', 'MockERC1155Customize', 0,  BigNumber { value: "0" }]
     *
     * @param ercInfo
     * @return
     */
    private ErcContractId ercInfo2ErcContractId(Tuple5<BigInteger, String, String, BigInteger, BigInteger> ercInfo) {
        int bitmap = ercInfo.getValue1().intValue();
        ErcContractId ercContractId = new ErcContractId();
        if(bitmap > 30){
            ercContractId.setTypeEnum(ErcTypeEnum.ERC1155);
        } else if (bitmap > 1 ) {
            ercContractId.setTypeEnum(ErcTypeEnum.ERC721);
        } else if (bitmap > 0 ) {
            ercContractId.setTypeEnum(ErcTypeEnum.ERC20);
        } else {
            ercContractId.setTypeEnum(ErcTypeEnum.UNKNOWN);
        }

        if(bitmap == 1){
            ercContractId.setIsSupportErc20(true);
        } else {
            ercContractId.setIsSupportErc20(false);
        }

        if((bitmap & 2) == 2){
            ercContractId.setIsSupportErc165(true);
        } else {
            ercContractId.setIsSupportErc165(false);
        }

        if((bitmap & 4) == 4){
            ercContractId.setIsSupportErc721(true);
        } else {
            ercContractId.setIsSupportErc721(false);
        }

        if((bitmap & 8) == 8){
            ercContractId.setIsSupportErc721Metadata(true);
        } else {
            ercContractId.setIsSupportErc721Metadata(false);
        }

        if((bitmap & 16) == 16){
            ercContractId.setIsSupportErc721Enumeration(true);
        } else {
            ercContractId.setIsSupportErc721Enumeration(false);
        }

        if((bitmap & 32) == 32){
            ercContractId.setIsSupportErc1155(true);
        } else {
            ercContractId.setIsSupportErc1155(false);
        }

        if((bitmap & 64) == 64){
            ercContractId.setIsSupportErc1155Metadata(true);
        } else {
            ercContractId.setIsSupportErc1155Metadata(false);
        }
        ercContractId.setName(ercInfo.getValue2());
        ercContractId.setSymbol(ercInfo.getValue3() );
        ercContractId.setDecimal(ercInfo.getValue4().intValue());
        ercContractId.setTotalSupply(ercInfo.getValue5().toString());
        return ercContractId;
    }

    private Tuple5<BigInteger, String, String, BigInteger, BigInteger> getErcInfoForLocal(String contractAddress, BigInteger blockNumber) throws Exception {
        BigInteger decimals = BigInteger.ZERO;
        BigInteger totalSupply = BigInteger.ZERO;

        Tuple4<BigInteger, String, String, BigInteger> check721 = getErc721InfoForLocal(contractAddress, blockNumber);
        if((check721.getValue1().intValue() & 4) == 4){
            return new Tuple5<>(check721.getValue1(), check721.getValue2(), check721.getValue3(), decimals, check721.getValue4());
        }

        Tuple3<BigInteger, String, String> check1155 = getErc1155InfoForLocal(contractAddress, blockNumber);
        if((check1155.getValue1().intValue() & 32) == 32){
            return new Tuple5<>(check1155.getValue1(), check1155.getValue2(), check1155.getValue3(), decimals, totalSupply);
        }

        return getErc20InfoForLocal(contractAddress, blockNumber);
    }

    private Tuple5<BigInteger, String, String, BigInteger, BigInteger> getErc20InfoForLocal(String contractAddress, BigInteger blockNumber) throws Exception {
        int bitmap = 1;
        ErcContract contract = loadErc20Contract(contractAddress, blockNumber);

        BigInteger decimals = BigInteger.ZERO;
        try {
            decimals = getDecimals(contract, true);
        } catch (PlatonCallTimeoutException e) {
            throw e;
        } catch (Exception e) {
            bitmap = 0;
        }

        String name = "";
        if(bitmap == 1){
            try {
                name = getName(contract, true);
            } catch (PlatonCallTimeoutException e) {
                throw e;
            } catch (Exception e) {
                bitmap = 0;
            }
        }

        String symbol = "";
        if(bitmap == 1){
            try {
                symbol = getSymbol(contract, true);
            } catch (PlatonCallTimeoutException e) {
                throw e;
            } catch (Exception e) {
                bitmap = 0;
            }
        }

        BigInteger totalSupply = BigInteger.ZERO;
        if(bitmap == 1){
            try {
                totalSupply = getTotalSupply(contract, true);
            } catch (PlatonCallTimeoutException e) {
                throw e;
            } catch (Exception e) {
                bitmap = 0;
            }
        }

        return new Tuple5<>(BigInteger.valueOf(bitmap), name, symbol, decimals, totalSupply);
    }

    private Tuple3<BigInteger, String, String> getErc1155InfoForLocal(String contractAddress, BigInteger blockNumber) throws Exception {
        int bitmap = 0;
        bitmap =  bitmap | (isSupportErc165(contractAddress, blockNumber) ? 2 : 0);

        if((bitmap & 2) == 2){
            bitmap = bitmap | (isSupportErc1155Only(contractAddress, blockNumber) ? 32 : 0);
        }

        boolean is1155 = (bitmap & 32) == 32;

        if(is1155){
            bitmap = bitmap | (isSupportErc1155MetadataOnly(contractAddress, blockNumber) ? 64 : 0);
        }

        String name = "";
        String symbol = "";
        ErcContract contract = loadErc1155Contract(contractAddress, blockNumber);
        if(is1155){
            name = getName(contract, false);
            symbol = getSymbol(contract, false);
        }
        return new Tuple3<>(BigInteger.valueOf(bitmap), name, symbol);
    }


    private Tuple4<BigInteger, String, String, BigInteger> getErc721InfoForLocal(String contractAddress, BigInteger blockNumber) throws Exception {
        int bitmap = 0;
        bitmap =  bitmap | (isSupportErc165(contractAddress, blockNumber) ? 2 : 0);

        if((bitmap & 2) == 2){
            bitmap = bitmap | (isSupportErc721Only(contractAddress, blockNumber) ? 4 : 0);
        }

        if((bitmap & 4) == 4){
            bitmap = bitmap | (isSupportErc721MetadataOnly(contractAddress, blockNumber) ? 8 : 0);
            bitmap = bitmap | (isSupportErc721EnumerableOnly(contractAddress, blockNumber) ? 16 : 0);
        }

        String name = "";
        String symbol = "";
        ErcContract contract = loadErc721Contract(contractAddress, blockNumber);
        if((bitmap & 8) == 8){
            name = getName(contract, false);
            symbol = getSymbol(contract, false);
        }
        BigInteger totalSupply = BigInteger.ZERO;
        if((bitmap & 16) == 16){
            totalSupply = getTotalSupply(contract, false);
        }
        return new Tuple4<>(BigInteger.valueOf(bitmap), name, symbol, totalSupply);
    }

    private BigInteger getDecimals(ErcContract contract, boolean throwException) throws Exception{
        try {
            return contract.decimals().send();
        } catch (PlatonCallTimeoutException e) {
            log.error("ERC获取decimals超时异常", e);
            throw e;
        } catch (Exception e) {
            log.warn("erc get decimals error", e);
            if(throwException){
                throw e;
            } else {
                return BigInteger.ZERO;
            }
        }
    }

    private BigInteger getTotalSupply(ErcContract contract, boolean throwException) throws Exception{
        try {
            return contract.totalSupply().send();
        } catch (PlatonCallTimeoutException e) {
            log.error("ERC获取totalSupply超时异常", e);
            throw e;
        } catch (Exception e) {
            log.warn("erc get totalSupply error", e);
            if(throwException){
                throw e;
            } else {
                return BigInteger.ZERO;
            }
        }
    }


    private String getSymbol(ErcContract contract, boolean throwException) throws Exception{
        try {
            return contract.symbol().send();
        } catch (PlatonCallTimeoutException e) {
            log.error("ERC获取symbol超时异常", e);
            throw e;
        } catch (Exception e) {
            log.warn("erc get symbol error", e);
            if(throwException){
                throw e;
            } else {
                return "";
            }
        }
    }


    private String getName(ErcContract contract, boolean throwException) throws Exception{
        try {
            return contract.name().send();
        } catch (PlatonCallTimeoutException e) {
            log.error("ERC获取name超时异常", e);
            throw e;
        } catch (Exception e) {
            log.warn("erc get name error", e);
            if(throwException){
                throw e;
            } else {
                return "";
            }
        }
    }

    private Tuple5<BigInteger, String, String, BigInteger, BigInteger> getErcInfoForContract(String contractAddress, BigInteger blockNumber) throws Exception {
        return loadPScanQueryFacadeContract(contractAddress, blockNumber).ercInfo(contractAddress).send();
    }

    private PScanQueryFacadeContract loadPScanQueryFacadeContract(String contractAddress, BigInteger blockNumber) {
        PScanQueryFacadeContract contract = PScanQueryFacadeContract.load(chainConfig.getDetectContractAddress(), platOnClient.getWeb3jWrapper().getWeb3j(), CREDENTIALS, GAS_PROVIDER);
        contract.setDefaultBlockParameter(DefaultBlockParameter.valueOf(blockNumber));
        return contract;
    }
    private ErcContract loadErc20Contract(String contractAddress, BigInteger blockNumber) {
        return Erc20Contract.load(contractAddress, platOnClient.getWeb3jWrapper().getWeb3j(), CREDENTIALS, GAS_PROVIDER, blockNumber);
    }

    private ErcContract loadErc721Contract(String contractAddress, BigInteger blockNumber) {
        return Erc721Contract.load(contractAddress, platOnClient.getWeb3jWrapper().getWeb3j(), CREDENTIALS, GAS_PROVIDER, blockNumber);
    }

    private ErcContract loadErc1155Contract(String contractAddress, BigInteger blockNumber) {
        return Erc1155Contract.load(contractAddress, platOnClient.getWeb3jWrapper().getWeb3j(), CREDENTIALS, GAS_PROVIDER, blockNumber);
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

    public List<ErcContract.ErcTxEvent> getErc20TxEvents(TransactionReceipt receipt, BigInteger blockNumber) {
        return loadErc20Contract(receipt.getContractAddress(), blockNumber).getTxEvents(receipt);
    }

    public List<ErcContract.ErcTxEvent> getErc721TxEvents(TransactionReceipt receipt, BigInteger blockNumber) {
        return loadErc721Contract(receipt.getContractAddress(), blockNumber).getTxEvents(receipt);
    }

    public List<ErcContract.ErcTxEvent> getErc1155TxEvents(TransactionReceipt receipt, BigInteger blockNumber) {
        return loadErc1155Contract(receipt.getContractAddress(), blockNumber).getTxEvents(receipt);
    }
}

package com.platon.browser.service.erc;

import cn.hutool.core.text.StrFormatter;
import cn.hutool.core.util.ObjectUtil;
import com.platon.bech32.Bech32;
import com.platon.browser.client.PlatOnClient;
import com.platon.browser.config.BlockChainConfig;
import com.platon.browser.dao.entity.Token1155Holder;
import com.platon.browser.dao.entity.TokenHolder;
import com.platon.browser.dao.entity.TokenHolderKey;
import com.platon.browser.enums.ErcTypeEnum;
import com.platon.browser.v0152.contract.ErcContract;
import com.platon.browser.v0152.service.ErcDetectService;
import com.platon.protocol.core.DefaultBlockParameterName;
import com.platon.protocol.core.methods.request.Transaction;
import com.platon.protocol.core.methods.response.PlatonCall;
import com.platon.tx.exceptions.ContractCallException;
import com.platon.tx.exceptions.PlatonCallException;
import com.platon.tx.exceptions.PlatonCallTimeoutException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.Utils;
import org.web3j.abi.datatypes.*;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.utils.Numeric;

import javax.annotation.Resource;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class ErcServiceImpl {

    @Resource
    private PlatOnClient platOnClient;

    @Resource
    private ErcDetectService ercDetectService;

    @Resource
    private BlockChainConfig blockChainConfig;

    /**
     * 批量查询合约方法 balanceOf(owner)，适用于prc20 & prc721
     *
     * @param tokenHolderKeyList  tokenAddress 和 address 必须传
     * @return 返回增加 balance 字段
     * @throws IOException 网络异常，可重试
     */
    public List<TokenHolder> batchBalanceOfOwner(List<TokenHolder> tokenHolderKeyList) throws IOException {
        List<TokenHolder> tokenHolderList = new ArrayList<>();
        List<Call3> call3List = tokenHolderKeyList.stream()
                .map(this::toCall3)
                .collect(Collectors.toList());
        Function function = new Function("aggregate3",
                Collections.singletonList(new DynamicArray<Call3>(Call3.class, call3List)),
                Collections.singletonList(new TypeReference<DynamicArray<Result>>() {
                }));

        PlatonCall ethCall = platOnClient.getWeb3jWrapper().getWeb3j().platonCall(Transaction.createEthCallTransaction(Address.DEFAULT.getValue(), blockChainConfig.getMultiCallContractAddress(),  FunctionEncoder.encode(function)),
                        DefaultBlockParameterName.LATEST)
                        .send();
        if(ethCall.hasError()){
            throw new RuntimeException("查询合约失败(MultiCall3)  msg = " + ethCall.getError().getMessage() + " code = " + ethCall.getError().getCode() );
        }
        List resultList = executeCallSingleValueReturn(FunctionReturnDecoder.decode(ethCall.getValue(), function.getOutputParameters()), List.class);
        for (int i = 0; i < tokenHolderKeyList.size(); i++) {
            TokenHolderKey tokenHolderKey = tokenHolderKeyList.get(i);
            TokenHolder tokenHolder = new TokenHolder();
            tokenHolder.setTokenAddress(tokenHolderKey.getTokenAddress());
            tokenHolder.setAddress(tokenHolderKey.getAddress());
            if(resultList.get(i) instanceof  Result){
                Result result = (Result) resultList.get(i);
                if ( result.success ){
                    List<BigInteger> balanceList = convertToNative(FunctionReturnDecoder.decode(Numeric.toHexString(result.returnData), Utils.convert( Collections.singletonList(new TypeReference<Uint256>() {}))));
                    if(balanceList.size() == 1){
                        tokenHolder.setBalance(balanceList.get(0).toString());
                    }
                }
            }
            tokenHolderList.add(tokenHolder);
        }
        return tokenHolderList;
    }

    /**
     * 批量查询合约方法 balanceOf(owner, id)，适用于prc1155
     *
     * @param tokenHolderKeyList  tokenAddress 和 address 和 tokenId 必须传
     * @return 返回增加 balance 字段
     * @throws IOException 网络异常，可重试
     */
    public List<Token1155Holder> batchBalanceOfOwnerAndId(List<Token1155Holder> tokenHolderKeyList) throws IOException {
        List<Token1155Holder> tokenHolderList = new ArrayList<>();
        List<Call3> call3List = tokenHolderKeyList.stream()
                .map(this::toCall3)
                .collect(Collectors.toList());
        Function function = new Function("aggregate3",
                Collections.singletonList(new DynamicArray<Call3>(Call3.class, call3List)),
                Collections.singletonList(new TypeReference<DynamicArray<Result>>() {
                }));

        PlatonCall ethCall = platOnClient.getWeb3jWrapper().getWeb3j().platonCall(Transaction.createEthCallTransaction(Address.DEFAULT.getValue(), blockChainConfig.getMultiCallContractAddress(),  FunctionEncoder.encode(function)),
                        DefaultBlockParameterName.LATEST)
                .send();
        if(ethCall.hasError()){
            throw new RuntimeException("查询合约失败(MultiCall3)  msg = " + ethCall.getError().getMessage() + " code = " + ethCall.getError().getCode() );
        }
        List resultList = executeCallSingleValueReturn(FunctionReturnDecoder.decode(ethCall.getValue(), function.getOutputParameters()), List.class);
        for (int i = 0; i < tokenHolderKeyList.size(); i++) {
            Token1155Holder tokenHolderKey = tokenHolderKeyList.get(i);
            Token1155Holder tokenHolder = new Token1155Holder();
            tokenHolder.setTokenAddress(tokenHolderKey.getTokenAddress());
            tokenHolder.setAddress(tokenHolderKey.getAddress());
            tokenHolder.setTokenId(tokenHolderKey.getTokenId());
            if(resultList.get(i) instanceof  Result){
                Result result = (Result) resultList.get(i);
                if ( result.success ){
                    List<BigInteger> balanceList = convertToNative(FunctionReturnDecoder.decode(Numeric.toHexString(result.returnData), Utils.convert( Collections.singletonList(new TypeReference<Uint256>() {}))));
                    if(balanceList.size() == 1){
                        tokenHolder.setBalance(balanceList.get(0).toString());
                    }
                }
            }
            tokenHolderList.add(tokenHolder);
        }
        return tokenHolderList;
    }




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

    public static class Result extends DynamicStruct {
        public Boolean success;

        public byte[] returnData;

        public Result(Boolean success, byte[] returnData) {
            super(new Bool(success),
                    new DynamicBytes(returnData));
            this.success = success;
            this.returnData = returnData;
        }

        public Result(Bool success, DynamicBytes returnData) {
            super(success, returnData);
            this.success = success.getValue();
            this.returnData = returnData.getValue();
        }
    }

    public static class Call3 extends DynamicStruct {
        public String target;

        public Boolean allowFailure;

        public byte[] callData;

        public Call3(String target, Boolean allowFailure, byte[] callData) {
            super(new Address(target),
                    new Bool(allowFailure),
                    new DynamicBytes(callData));
            this.target = target;
            this.allowFailure = allowFailure;
            this.callData = callData;
        }

        public Call3(Address target, Bool allowFailure, DynamicBytes callData) {
            super(target, allowFailure, callData);
            this.target = target.getValue();
            this.allowFailure = allowFailure.getValue();
            this.callData = callData.getValue();
        }
    }

    @SuppressWarnings("unchecked")
    private static <S extends Type, T> List<T> convertToNative(List<S> arr) {
        List<T> out = new ArrayList<>();
        for (final S s : arr) {
            if (StructType.class.isAssignableFrom(s.getClass())) {
                out.add((T) s);
            } else {
                out.add((T) s.getValue());
            }
        }
        return out;
    }

    private  <T extends Type, R> R executeCallSingleValueReturn(List<Type> values, Class<R> returnType) throws IOException {
        T result = executeCallSingleValueReturn(values);
        if (result == null) {
            throw new ContractCallException("Empty value (0x) returned from contract");
        }

        Object value = result.getValue();
        if (returnType.isAssignableFrom(result.getClass())) {
            return (R) result;
        } else if (returnType.isAssignableFrom(value.getClass())) {
            return (R) value;
        } else if (result.getClass().equals(Address.class) && returnType.equals(String.class)) {
            return (R) result.toString(); // cast isn't necessary
        } else {
            throw new ContractCallException(
                    "Unable to convert response: "
                            + value
                            + " to expected type: "
                            + returnType.getSimpleName());
        }
    }

    private <T extends Type> T executeCallSingleValueReturn(List<Type> values) {
        if (!values.isEmpty()) {
            return (T) values.get(0);
        } else {
            return null;
        }
    }

    private Call3 toCall3(TokenHolderKey tokenHolderKey){
        Function balanceOfFunction = new org.web3j.abi.datatypes.Function("balanceOf",
                Collections.singletonList(new Address(160, Bech32.addressDecodeHex(tokenHolderKey.getAddress()))),
                Collections.singletonList(new TypeReference<Uint256>() {
                }));
        return new Call3(Bech32.addressDecodeHex(tokenHolderKey.getTokenAddress()), true, Numeric.hexStringToByteArray(FunctionEncoder.encode(balanceOfFunction)));
    }

    private Call3 toCall3(Token1155Holder tokenHolderKey){
        Function balanceOfFunction = new org.web3j.abi.datatypes.Function("balanceOf",
                Arrays.asList(new Address(160,  Bech32.addressDecodeHex(tokenHolderKey.getAddress())), new Uint256(new BigInteger(tokenHolderKey.getTokenId()))),
                Collections.singletonList(new TypeReference<Uint256>() {
                }));
        return new Call3(Bech32.addressDecodeHex(tokenHolderKey.getTokenAddress()), true, Numeric.hexStringToByteArray(FunctionEncoder.encode(balanceOfFunction)));
    }
}

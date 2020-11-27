package com.platon.browser.erc.wrapper;

import com.alaya.abi.solidity.TypeReference;
import com.alaya.abi.solidity.datatypes.Address;
import com.alaya.abi.solidity.datatypes.Event;
import com.alaya.abi.solidity.datatypes.Utf8String;
import com.alaya.abi.solidity.datatypes.generated.*;
import com.alaya.crypto.Credentials;
import com.alaya.protocol.Web3j;
import com.alaya.protocol.core.methods.response.Log;
import com.alaya.protocol.core.methods.response.TransactionReceipt;
import com.alaya.tx.Contract;
import com.alaya.tx.TransactionManager;
import com.alaya.tx.gas.GasProvider;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ExtendEvent extends Contract {

    private static final  String BINARY = "";

    // event TokenCreated(string name, string symbol, uint8 decimals, uint256 cap);
    public static final Event TOKEN_CREATED_EVENT =
            new Event("TokenCreated", Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {},
                    new TypeReference<Utf8String>() {},
                    new TypeReference<Uint8>() {},
                    new TypeReference<Uint256>() {}));

    public ExtendEvent(String contractAddress, Web3j web3j, Credentials credentials, GasProvider gasProvider, long chainId) {
        super(BINARY, contractAddress, web3j, credentials, gasProvider, chainId);
    }

    public ExtendEvent(String contractAddress, Web3j web3j, TransactionManager transactionManager, GasProvider gasProvider, long chainId) {
        super(BINARY, contractAddress, web3j, transactionManager, gasProvider, chainId);
    }

    public List<TokenCreatedResponse> getTransferEvents(final TransactionReceipt transactionReceipt) {
        final List<EventValuesWithLog> valueList = this.extractEventParametersWithLog(TOKEN_CREATED_EVENT, transactionReceipt);
        final ArrayList<TokenCreatedResponse> responses = new ArrayList<>(valueList.size());
        for (final EventValuesWithLog eventValues : valueList) {
            final TokenCreatedResponse typedResponse = new TokenCreatedResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.address = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.name = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.symbol = (String) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.value = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static class TokenCreatedResponse {
        private Log log;
        private String address;
        private String name;
        private String symbol;
        private BigInteger value;

        public Log getLog() {
            return log;
        }

        public void setLog(Log log) {
            this.log = log;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSymbol() {
            return symbol;
        }

        public void setSymbol(String symbol) {
            this.symbol = symbol;
        }

        public BigInteger getValue() {
            return value;
        }

        public void setValue(BigInteger value) {
            this.value = value;
        }
    }

    /**
     * 特定解析任意Event中包含的合约地址
     *
     * @param event              待解析的事件
     * @param transactionReceipt 交易回执（从该回执中进行解析）
     * @param addressIndex       合约地址所在事件的索引位（分为indexed|非indexed）
     * @param isIndexed          是否为索引标识位
     * @return 多个代币合约地址的列表
     */
    public List<TokenContractResponse> getContractAddressList(final Event event,
                                                              final TransactionReceipt transactionReceipt,
                                                              final int addressIndex,
                                                              final boolean isIndexed) {
        final List<EventValuesWithLog> valueList = this.extractEventParametersWithLog(event, transactionReceipt);
        final ArrayList<TokenContractResponse> responses = new ArrayList<>(valueList.size());
        for (final EventValuesWithLog eventValues : valueList) {
            final TokenContractResponse typedResponse = new TokenContractResponse();
            typedResponse.log = eventValues.getLog();
            if (isIndexed) {
                typedResponse.address = (String) eventValues.getIndexedValues().get(addressIndex).getValue();
            } else {
                typedResponse.address = (String) eventValues.getNonIndexedValues().get(addressIndex).getValue();
            }
            responses.add(typedResponse);
        }
        return responses;
    }

    public static class TokenContractResponse {
        private Log log;
        private String address;

        public Log getLog() {
            return log;
        }

        public void setLog(Log log) {
            this.log = log;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }
    }

    public static Event buildEvent(String eventName, List<EventDefine> eventType) {
        List<TypeReference<?>> parameters = new ArrayList<>();
        if (null == eventType || eventType.isEmpty()) {
            return new Event(eventName, Arrays.<TypeReference<?>>asList());
        }
        for (EventDefine define : eventType) {
            if (define.getType().equalsIgnoreCase("string")) {
                parameters.add(new TypeReference<Utf8String>(define.isIndexed()) {
                });
            }
            if (define.getType().equalsIgnoreCase("address")) {
                parameters.add(new TypeReference<Address>(define.isIndexed()) {
                });
            }
            if (define.getType().equalsIgnoreCase("uint8")) {
                parameters.add(new TypeReference<Uint8>(define.isIndexed()) {
                });
            }
            if (define.getType().equalsIgnoreCase("uint256")) {
                parameters.add(new TypeReference<Uint256>(define.isIndexed()) {
                });
            }
            if (define.getType().equalsIgnoreCase("uint128")) {
                parameters.add(new TypeReference<Uint128>(define.isIndexed()) {
                });
            }
            if (define.getType().equalsIgnoreCase("int8")) {
                parameters.add(new TypeReference<Int8>(define.isIndexed()) {
                });
            }
            if (define.getType().equalsIgnoreCase("int256")) {
                parameters.add(new TypeReference<Int256>(define.isIndexed()) {
                });
            }
        }
        return new Event(eventName, parameters);
    }

    public static class EventDefine {
        private String type;
        private boolean indexed;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public boolean isIndexed() {
            return indexed;
        }

        public void setIndexed(boolean indexed) {
            this.indexed = indexed;
        }
    }

    /**
     * 一个完整的Event的定义
     */
    public static class EventWrapper {
        private String eventName;
        private List<EventDefine> eventDefineList;
        private int addressIndex;

        public EventWrapper() {
        }

        public EventWrapper(String eventName, List<EventDefine> eventDefineList, int addressIndex) {
            this.eventName = eventName;
            this.eventDefineList = eventDefineList;
            this.addressIndex = addressIndex;
        }

        public List<EventDefine> getEventDefineList() {
            return eventDefineList;
        }

        public void setEventDefineList(List<EventDefine> eventDefineList) {
            this.eventDefineList = eventDefineList;
        }

        public int getAddressIndex() {
            return addressIndex;
        }

        public void setAddressIndex(int addressIndex) {
            this.addressIndex = addressIndex;
        }

        public String getEventName() {
            return eventName;
        }

        public void setEventName(String eventName) {
            this.eventName = eventName;
        }
    }
}

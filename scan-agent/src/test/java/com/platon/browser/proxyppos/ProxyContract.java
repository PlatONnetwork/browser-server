package com.platon.browser.proxyppos;

import com.platon.abi.solidity.EventEncoder;
import com.platon.abi.solidity.TypeReference;
import com.platon.abi.solidity.datatypes.*;
import com.platon.crypto.Credentials;
import com.platon.protocol.Web3j;
import com.platon.protocol.core.DefaultBlockParameter;
import com.platon.protocol.core.RemoteCall;
import com.platon.protocol.core.methods.request.PlatonFilter;
import com.platon.protocol.core.methods.response.Log;
import com.platon.protocol.core.methods.response.TransactionReceipt;
import com.platon.tx.Contract;
import com.platon.tx.TransactionManager;
import com.platon.tx.gas.GasProvider;
import rx.Observable;
import rx.functions.Func1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://github.com/PlatONnetwork/client-sdk-java/releases">platon-web3j command line tools</a>,
 * or the com.alaya.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/PlatONnetwork/client-sdk-java/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 0.13.0.7.
 */
public class ProxyContract extends Contract {
    private static final String BINARY = "608060405234801561001057600080fd5b50610357806100206000396000f3fe60806040526004361061001e5760003560e01c8063b745845114610020575b005b34801561002c57600080fd5b5061001e6004803603608081101561004357600080fd5b81019060208101813564010000000081111561005e57600080fd5b82018360208201111561007057600080fd5b8035906020019184600183028401116401000000008311171561009257600080fd5b91908080601f01602080910402602001604051908101604052809392919081815260200183838082843760009201919091525092956001600160a01b038535169590949093506040810192506020013590506401000000008111156100f657600080fd5b82018360208201111561010857600080fd5b8035906020019184600183028401116401000000008311171561012a57600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600092019190915250929550505090356001600160a01b031691506101769050565b83516000606081808460208a01828a5af161018d57fe5b3d9150816040519080825280601f01601f1916602001820160405280156101bb576020820181803883390190505b5090503d6000602083013e84516000606081808460208b01828b5af16101dd57fe5b3d9150816040519080825280601f01601f19166020018201604052801561020b576020820181803883390190505b5090503d6000602083013e7fc50b5eb72ef1545a2d013d3b01503b8313a92b71496c125395943121105190028482604051808060200180602001838103835285818151815260200191508051906020019080838360005b8381101561027a578181015183820152602001610262565b50505050905090810190601f1680156102a75780820380516001836020036101000a031916815260200191505b50838103825284518152845160209182019186019080838360005b838110156102da5781810151838201526020016102c2565b50505050905090810190601f1680156103075780820380516001836020036101000a031916815260200191505b5094505050505060405180910390a15050505050505050505056fea265627a7a72315820dc3fba07b3d8b0743495ef16e74fceec232891f68f039d4ff79c6e1d82caaf9e64736f6c634300050d0032";

    public static final String FUNC_PROXYSEND = "proxySend";

    public static final Event PROXYEVENT_EVENT = new Event("ProxyEvent",
            Arrays.<TypeReference<?>>asList(new TypeReference<DynamicBytes>() {}, new TypeReference<DynamicBytes>() {}));
    ;

    protected ProxyContract(String contractAddress, Web3j web3j, Credentials credentials, GasProvider contractGasProvider, Long chainId) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    protected ProxyContract(String contractAddress, Web3j web3j, TransactionManager transactionManager, GasProvider contractGasProvider, Long chainId) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public List<ProxyEventEventResponse> getProxyEventEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(PROXYEVENT_EVENT, transactionReceipt);
        ArrayList<ProxyEventEventResponse> responses = new ArrayList<ProxyEventEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            ProxyEventEventResponse typedResponse = new ProxyEventEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.oneEvent = (byte[]) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.twoEvent = (byte[]) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<ProxyEventEventResponse> proxyEventEventObservable(PlatonFilter filter) {
        return web3j.platonLogObservable(filter).map(new Func1<Log, ProxyEventEventResponse>() {
            @Override
            public ProxyEventEventResponse call(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(PROXYEVENT_EVENT, log);
                ProxyEventEventResponse typedResponse = new ProxyEventEventResponse();
                typedResponse.log = log;
                typedResponse.oneEvent = (byte[]) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.twoEvent = (byte[]) eventValues.getNonIndexedValues().get(1).getValue();
                return typedResponse;
            }
        });
    }

    public Observable<ProxyEventEventResponse> proxyEventEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        PlatonFilter filter = new PlatonFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(PROXYEVENT_EVENT));
        return proxyEventEventObservable(filter);
    }

    public RemoteCall<TransactionReceipt> proxySend(byte[] oneData, String oneAddr, byte[] twoData, String twoAddr) {
        final Function function = new Function(
                FUNC_PROXYSEND, 
                Arrays.<Type>asList(new DynamicBytes(oneData),
                new Address(oneAddr),
                new DynamicBytes(twoData),
                new Address(twoAddr)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public static RemoteCall<ProxyContract> deploy(Web3j web3j, Credentials credentials, GasProvider contractGasProvider, Long chainId) {
        return deployRemoteCall(ProxyContract.class, web3j, credentials, contractGasProvider, BINARY,  "");
    }

    public static RemoteCall<ProxyContract> deploy(Web3j web3j, TransactionManager transactionManager, GasProvider contractGasProvider, Long chainId) {
        return deployRemoteCall(ProxyContract.class, web3j, transactionManager, contractGasProvider, BINARY,  "");
    }

    public static ProxyContract load(String contractAddress, Web3j web3j, Credentials credentials, GasProvider contractGasProvider, Long chainId) {
        return new ProxyContract(contractAddress, web3j, credentials, contractGasProvider, chainId);
    }

    public static ProxyContract load(String contractAddress, Web3j web3j, TransactionManager transactionManager, GasProvider contractGasProvider, Long chainId) {
        return new ProxyContract(contractAddress, web3j, transactionManager, contractGasProvider, chainId);
    }

    public static class ProxyEventEventResponse {
        public Log log;

        public byte[] oneEvent;

        public byte[] twoEvent;
    }
}

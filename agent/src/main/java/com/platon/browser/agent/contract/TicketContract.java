package com.platon.browser.agent.contract;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.PlatOnContract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;
import rx.Observable;
import rx.functions.Func1;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version none.
 */
public class TicketContract extends PlatOnContract {
    private static final String ABI = "[\t{\t\t\t\"name\":\"VoteTicket\",\t\t\t\"inputs\":[\t\t\t\t{\t\t\t\t\t\"name\":\"count\",\t\t\t\t\t\"type\":\"uint64\"\t\t\t\t},\t\t\t\t{\t\t\t\t\t\"name\":\"price\",\t\t\t\t\t\"type\":\"uint256\"\t\t\t\t},\t\t\t\t{\t\t\t\t\t\"name\":\"nodeId\",\t\t\t\t\t\"type\":\"string\"\t\t\t\t}\t\t\t],\t\t\t\"outputs\":[\t\t\t\t{\t\t\t\t\t\"name\":\"\",\t\t\t\t\t\"type\":\"string\"\t\t\t\t}\t\t\t],\t\t\t\"constant\":\"false\",\t\t\t\"type\":\"function\"\t},\t{\t\t\t\"name\":\"GetTicketDetail\",\t\t\t\"inputs\":[\t\t\t\t{\t\t\t\t\t\"name\":\"ticketId\",\t\t\t\t\t\"type\":\"string\"\t\t\t\t}\t\t\t\t\t\t\t],\t\t\t\"outputs\":[\t\t\t\t{\t\t\t\t\t\"name\":\"\",\t\t\t\t\t\"type\":\"string\"\t\t\t\t}\t\t\t],\t\t\t\"constant\":\"true\",\t\t\t\"type\":\"function\"\t},\t{\t\t\t\"name\":\"GetBatchTicketDetail\",\t\t\t\"inputs\":[\t\t\t\t{\t\t\t\t\t\"name\":\"ticketIds\",\t\t\t\t\t\"type\":\"string[]\"\t\t\t\t}\t\t\t\t\t\t\t],\t\t\t\"outputs\":[\t\t\t\t{\t\t\t\t\t\"name\":\"\",\t\t\t\t\t\"type\":\"string\"\t\t\t\t}\t\t\t],\t\t\t\"constant\":\"true\",\t\t\t\"type\":\"function\"\t},\t{\t\t\t\"name\":\"GetCandidateTicketIds\",\t\t\t\"inputs\":[\t\t\t\t{\t\t\t\t\t\"name\":\"nodeId\",\t\t\t\t\t\"type\":\"string\"\t\t\t\t}\t\t\t\t\t\t\t],\t\t\t\"outputs\":[\t\t\t\t{\t\t\t\t\t\"name\":\"\",\t\t\t\t\t\"type\":\"string\"\t\t\t\t}\t\t\t],\t\t\t\"constant\":\"true\",\t\t\t\"type\":\"function\"\t},\t{\t\t\t\"name\":\"GetCandidateEpoch\",\t\t\t\"inputs\":[\t\t\t\t{\t\t\t\t\t\"name\":\"nodeId\",\t\t\t\t\t\"type\":\"string\"\t\t\t\t}\t\t\t\t\t\t\t],\t\t\t\"outputs\":[\t\t\t\t{\t\t\t\t\t\"name\":\"\",\t\t\t\t\t\"type\":\"string\"\t\t\t\t}\t\t\t],\t\t\t\"constant\":\"true\",\t\t\t\"type\":\"function\"\t},\t{\t\t\t\"name\":\"GetPoolRemainder\",\t\t\t\"inputs\":[],\t\t\t\"outputs\":[\t\t\t\t{\t\t\t\t\t\"name\":\"\",\t\t\t\t\t\"type\":\"string\"\t\t\t\t}\t\t\t],\t\t\t\"constant\":\"true\",\t\t\t\"type\":\"function\"\t},\t{\t\t\t\"name\":\"GetTicketPrice\",\t\t\t\"inputs\":[],\t\t\t\"outputs\":[\t\t\t\t{\t\t\t\t\t\"name\":\"\",\t\t\t\t\t\"type\":\"string\"\t\t\t\t}\t\t\t],\t\t\t\"constant\":\"true\",\t\t\t\"type\":\"function\"\t},\t{\"name\":\"VoteTicketEvent\",\"inputs\":[{\"type\":\"string\"}],\"type\":\"event\"}]";

    public static final String FUNC_VOTETICKET = "VoteTicket";

    public static final String FUNC_GETTICKETDETAIL = "GetTicketDetail";

    public static final String FUNC_GETBATCHTICKETDETAIL = "GetBatchTicketDetail";

    public static final String FUNC_GETCANDIDATETICKETIDS = "GetCandidateTicketIds";

    public static final String FUNC_GETCANDIDATEEPOCH = "GetCandidateEpoch";

    public static final String FUNC_GETPOOLREMAINDER = "GetPoolRemainder";

    public static final String FUNC_GETTICKETPRICE = "GetTicketPrice";

    public static final Event VOTETICKETEVENT_EVENT = new Event("VoteTicketEvent", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
    ;

    @Deprecated
    protected TicketContract(String contractBinary, String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(contractBinary, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected TicketContract(String contractBinary, String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(contractBinary, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected TicketContract(String contractBinary, String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(contractBinary, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected TicketContract(String contractBinary, String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(contractBinary, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public RemoteCall<TransactionReceipt> VoteTicket(BigInteger count, BigInteger price, String nodeId) {
        final Function function = new Function(
                FUNC_VOTETICKET, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint64(count), 
                new org.web3j.abi.datatypes.generated.Uint256(price), 
                new Utf8String(nodeId)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public static String VoteTicketData(BigInteger count, BigInteger price, String nodeId) {
        final Function function = new Function(
                FUNC_VOTETICKET, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint64(count), 
                new org.web3j.abi.datatypes.generated.Uint256(price), 
                new Utf8String(nodeId)),
                Collections.<TypeReference<?>>emptyList());
        return getInvokeData(function);
    }

    public static BigInteger VoteTicketGasLimit(Web3j web3j, String estimateGasFrom, String estimateGasTo, BigInteger count, BigInteger price, String nodeId) throws IOException {
        String ethEstimateGasData = VoteTicketData(count, price, nodeId);
        return estimateGasLimit(web3j,estimateGasFrom,estimateGasTo,ethEstimateGasData);
    }

    public RemoteCall<String> GetTicketDetail(String ticketId) {
        final Function function = new Function(FUNC_GETTICKETDETAIL, 
                Arrays.<Type>asList(new Utf8String(ticketId)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<String> GetBatchTicketDetail(List<String> ticketIds) {
        final Function function = new Function(FUNC_GETBATCHTICKETDETAIL, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.DynamicArray<Utf8String>(
                        org.web3j.abi.Utils.typeMap(ticketIds, Utf8String.class))),
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<String> GetCandidateTicketIds(String nodeId) {
        final Function function = new Function(FUNC_GETCANDIDATETICKETIDS, 
                Arrays.<Type>asList(new Utf8String(nodeId)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<String> GetCandidateEpoch(String nodeId) {
        final Function function = new Function(FUNC_GETCANDIDATEEPOCH, 
                Arrays.<Type>asList(new Utf8String(nodeId)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<String> GetPoolRemainder() {
        final Function function = new Function(FUNC_GETPOOLREMAINDER, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<String> GetTicketPrice() {
        final Function function = new Function(FUNC_GETTICKETPRICE, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public List<VoteTicketEventEventResponse> getVoteTicketEventEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(VOTETICKETEVENT_EVENT, transactionReceipt);
        ArrayList<VoteTicketEventEventResponse> responses = new ArrayList<VoteTicketEventEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            VoteTicketEventEventResponse typedResponse = new VoteTicketEventEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.param1 = (String) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<VoteTicketEventEventResponse> voteTicketEventEventObservable(EthFilter filter) {
        return web3j.ethLogObservable(filter).map(new Func1<Log, VoteTicketEventEventResponse>() {
            @Override
            public VoteTicketEventEventResponse call(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(VOTETICKETEVENT_EVENT, log);
                VoteTicketEventEventResponse typedResponse = new VoteTicketEventEventResponse();
                typedResponse.log = log;
                typedResponse.param1 = (String) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public Observable<VoteTicketEventEventResponse> voteTicketEventEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(VOTETICKETEVENT_EVENT));
        return voteTicketEventEventObservable(filter);
    }

    public static RemoteCall<TicketContract> deploy(Web3j web3j, Credentials credentials, String contractBinary, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(TicketContract.class, web3j, credentials, contractGasProvider, contractBinary, ABI, "");
    }

    @Deprecated
    public static RemoteCall<TicketContract> deploy(Web3j web3j, Credentials credentials, String contractBinary, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(TicketContract.class, web3j, credentials, gasPrice, gasLimit, contractBinary, ABI, "");
    }

    public static RemoteCall<TicketContract> deploy(Web3j web3j, TransactionManager transactionManager, String contractBinary, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(TicketContract.class, web3j, transactionManager, contractGasProvider, contractBinary, ABI, "");
    }

    @Deprecated
    public static RemoteCall<TicketContract> deploy(Web3j web3j, TransactionManager transactionManager, String contractBinary, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(TicketContract.class, web3j, transactionManager, gasPrice, gasLimit, contractBinary, ABI, "");
    }

    @Deprecated
    public static TicketContract load(String contractBinary, String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new TicketContract(contractBinary, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static TicketContract load(String contractBinary, String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new TicketContract(contractBinary, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static TicketContract load(String contractBinary, String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new TicketContract(contractBinary, contractAddress, web3j, credentials, contractGasProvider);
    }

    public static TicketContract load(String contractBinary, String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new TicketContract(contractBinary, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static String getDeployData(String contractBinary) {
        return getDeployData(contractBinary, ABI);
    }

    public static BigInteger getDeployGasLimit(Web3j web3j, String estimateGasFrom, String estimateGasTo, String contractBinary) throws IOException {
        return getDeployGasLimit(web3j, estimateGasFrom, estimateGasTo, contractBinary, ABI);
    }

    public static class VoteTicketEventEventResponse {
        public Log log;

        public String param1;
    }
}

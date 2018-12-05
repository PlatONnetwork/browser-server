package com.platon.browser.agent.contract;

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
public class CandidateConstract extends PlatOnContract {
    private static final String BINARY = "";

    private static final String ABI = "[\r\n"
            + "\t{\r\n"
            + "\t\t\t\"name\": \"CandidateDeposit\",\r\n"
            + "\t\t\t\"inputs\": [\r\n"
            + "\t\t\t\t{\r\n"
            + "\t\t\t\t\t\"name\": \"nodeId\",\r\n"
            + "\t\t\t\t\t\"type\": \"string\"\r\n"
            + "\t\t\t\t},\r\n"
            + "\t\t\t\t{\r\n"
            + "\t\t\t\t\t\"name\": \"owner\",\r\n"
            + "\t\t\t\t\t\"type\": \"string\"\r\n"
            + "\t\t\t\t},\r\n"
            + "\t\t\t\t{\r\n"
            + "\t\t\t\t\t\"name\": \"fee\",\r\n"
            + "\t\t\t\t\t\"type\": \"uint64\"\r\n"
            + "\t\t\t\t},\r\n"
            + "\t\t\t\t{\r\n"
            + "\t\t\t\t\t\"name\": \"host\",\r\n"
            + "\t\t\t\t\t\"type\": \"string\"\r\n"
            + "\t\t\t\t},\r\n"
            + "\t\t\t\t{\r\n"
            + "\t\t\t\t\t\"name\": \"port\",\r\n"
            + "\t\t\t\t\t\"type\": \"string\"\r\n"
            + "\t\t\t\t},\r\n"
            + "\t\t\t\t{\r\n"
            + "\t\t\t\t\t\"name\": \"extra\",\r\n"
            + "\t\t\t\t\t\"type\": \"string\"\r\n"
            + "\t\t\t\t}\r\n"
            + "\t\t\t\t\r\n"
            + "\t\t\t],\r\n"
            + "\t\t\t\"outputs\": [\r\n"
            + "\t\t\t\t{\r\n"
            + "\t\t\t\t\t\"name\": \"\",\r\n"
            + "\t\t\t\t\t\"type\": \"string\"\r\n"
            + "\t\t\t\t}\r\n"
            + "\t\t\t],\r\n"
            + "\t\t\t\"constant\": \"false\",\r\n"
            + "\t\t\t\"type\": \"function\"\r\n"
            + "\t},\r\n"
            + "\t{\r\n"
            + "\t\t\t\"name\": \"CandidateApplyWithdraw\",\r\n"
            + "\t\t\t\"inputs\": [\r\n"
            + "\t\t\t\t{\r\n"
            + "\t\t\t\t\t\"name\": \"nodeId\",\r\n"
            + "\t\t\t\t\t\"type\": \"string\"\r\n"
            + "\t\t\t\t},\r\n"
            + "\t\t\t\t{\r\n"
            + "\t\t\t\t\t\"name\": \"withdraw\",\r\n"
            + "\t\t\t\t\t\"type\": \"uint256\"\r\n"
            + "\t\t\t\t}\r\n"
            + "\t\t\t\t\r\n"
            + "\t\t\t],\r\n"
            + "\t\t\t\"outputs\": [\r\n"
            + "\t\t\t\t{\r\n"
            + "\t\t\t\t\t\"name\": \"\",\r\n"
            + "\t\t\t\t\t\"type\": \"string\"\r\n"
            + "\t\t\t\t}\r\n"
            + "\t\t\t],\r\n"
            + "\t\t\t\"constant\": \"false\",\r\n"
            + "\t\t\t\"type\": \"function\"\r\n"
            + "\t},\r\n"
            + "\t{\r\n"
            + "\t\t\t\"name\": \"CandidateWithdraw\",\r\n"
            + "\t\t\t\"inputs\": [\r\n"
            + "\t\t\t\t{\r\n"
            + "\t\t\t\t\t\"name\": \"nodeId\",\r\n"
            + "\t\t\t\t\t\"type\": \"string\"\r\n"
            + "\t\t\t\t}\r\n"
            + "\t\t\t],\r\n"
            + "\t\t\t\"outputs\": [\r\n"
            + "\t\t\t\t{\r\n"
            + "\t\t\t\t\t\"name\": \"\",\r\n"
            + "\t\t\t\t\t\"type\": \"string\"\r\n"
            + "\t\t\t\t}\r\n"
            + "\t\t\t],\r\n"
            + "\t\t\t\"constant\": \"false\",\r\n"
            + "\t\t\t\"type\": \"function\"\r\n"
            + "\t},\r\n"
            + "\t{\r\n"
            + "\t\t\t\"name\": \"SetCandidateExtra\",\r\n"
            + "\t\t\t\"inputs\": [\r\n"
            + "\t\t\t\t{\r\n"
            + "\t\t\t\t\t\"name\": \"nodeId\",\r\n"
            + "\t\t\t\t\t\"type\": \"string\"\r\n"
            + "\t\t\t\t},\r\n"
            + "\t\t\t\t{\r\n"
            + "\t\t\t\t\t\"name\": \"extra\",\r\n"
            + "\t\t\t\t\t\"type\": \"string\"\r\n"
            + "\t\t\t\t}\r\n"
            + "\t\t\t],\r\n"
            + "\t\t\t\"outputs\": [\r\n"
            + "\t\t\t\t{\r\n"
            + "\t\t\t\t\t\"name\": \"\",\r\n"
            + "\t\t\t\t\t\"type\": \"string\"\r\n"
            + "\t\t\t\t}\r\n"
            + "\t\t\t],\r\n"
            + "\t\t\t\"constant\": \"false\",\r\n"
            + "\t\t\t\"type\": \"function\"\r\n"
            + "\t},\r\n"
            + "\t{\r\n"
            + "\t\t\t\"name\": \"CandidateWithdrawInfos\",\r\n"
            + "\t\t\t\"inputs\": [\r\n"
            + "\t\t\t\t{\r\n"
            + "\t\t\t\t\t\"name\": \"nodeId\",\r\n"
            + "\t\t\t\t\t\"type\": \"string\"\r\n"
            + "\t\t\t\t}\r\n"
            + "\t\t\t],\r\n"
            + "\t\t\t\"outputs\": [\r\n"
            + "\t\t\t\t{\r\n"
            + "\t\t\t\t\t\"name\": \"\",\r\n"
            + "\t\t\t\t\t\"type\": \"string\"\r\n"
            + "\t\t\t\t}\r\n"
            + "\t\t\t],\r\n"
            + "\t\t\t\"constant\": \"true\",\r\n"
            + "\t\t\t\"type\": \"function\"\r\n"
            + "\t},\r\n"
            + "\t{\r\n"
            + "\t\t\t\"name\": \"CandidateDetails\",\r\n"
            + "\t\t\t\"inputs\": [\r\n"
            + "\t\t\t\t{\r\n"
            + "\t\t\t\t\t\"name\": \"nodeId\",\r\n"
            + "\t\t\t\t\t\"type\": \"string\"\r\n"
            + "\t\t\t\t}\r\n"
            + "\t\t\t\t\r\n"
            + "\t\t\t],\r\n"
            + "\t\t\t\"outputs\": [\r\n"
            + "\t\t\t\t{\r\n"
            + "\t\t\t\t\t\"name\": \"\",\r\n"
            + "\t\t\t\t\t\"type\": \"string\"\r\n"
            + "\t\t\t\t}\r\n"
            + "\t\t\t],\r\n"
            + "\t\t\t\"constant\": \"true\",\r\n"
            + "\t\t\t\"type\": \"function\"\r\n"
            + "\t},\r\n"
            + "\t{\r\n"
            + "\t\t\t\"name\": \"CandidateList\",\r\n"
            + "\t\t\t\"inputs\": [],\r\n"
            + "\t\t\t\"outputs\": [\r\n"
            + "\t\t\t\t{\r\n"
            + "\t\t\t\t\t\"name\": \"\",\r\n"
            + "\t\t\t\t\t\"type\": \"string\"\r\n"
            + "\t\t\t\t}\r\n"
            + "\t\t\t],\r\n"
            + "\t\t\t\"constant\": \"true\",\r\n"
            + "\t\t\t\"type\": \"function\"\r\n"
            + "\t},\r\n"
            + "\t{\r\n"
            + "\t\t\t\"name\": \"VerifiersList\",\r\n"
            + "\t\t\t\"inputs\": [],\r\n"
            + "\t\t\t\"outputs\": [\r\n"
            + "\t\t\t\t{\r\n"
            + "\t\t\t\t\t\"name\": \"\",\r\n"
            + "\t\t\t\t\t\"type\": \"string\"\r\n"
            + "\t\t\t\t}\r\n"
            + "\t\t\t],\r\n"
            + "\t\t\t\"constant\": \"true\",\r\n"
            + "\t\t\t\"type\": \"function\"\r\n"
            + "\t},\r\n"
            + "\t{\r\n"
            + "        \"name\": \"CandidateDepositEvent\",\r\n"
            + "        \"inputs\": [\r\n"
            + "            {\r\n"
            + "                \"type\": \"string\"\r\n"
            + "            }\r\n"
            + "        ],\r\n"
            + "        \"type\": \"event\"\r\n"
            + "    },\r\n"
            + "\t{\r\n"
            + "        \"name\": \"CandidateApplyWithdrawEvent\",\r\n"
            + "        \"inputs\": [\r\n"
            + "            {\r\n"
            + "                \"type\": \"string\"\r\n"
            + "            }\r\n"
            + "        ],\r\n"
            + "        \"type\": \"event\"\r\n"
            + "    },\r\n"
            + "\t{\r\n"
            + "        \"name\": \"CandidateWithdrawEvent\",\r\n"
            + "        \"inputs\": [\r\n"
            + "            {\r\n"
            + "                \"type\": \"string\"\r\n"
            + "            }\r\n"
            + "        ],\r\n"
            + "        \"type\": \"event\"\r\n"
            + "    },\r\n"
            + "\t{\r\n"
            + "        \"name\": \"SetCandidateExtraEvent\",\r\n"
            + "        \"inputs\": [\r\n"
            + "            {\r\n"
            + "                \"type\": \"string\"\r\n"
            + "            }\r\n"
            + "        ],\r\n"
            + "        \"type\": \"event\"\r\n"
            + "    }\r\n"
            + "]";

    public static final String FUNC_CANDIDATEDEPOSIT = "CandidateDeposit";

    public static final String FUNC_CANDIDATEAPPLYWITHDRAW = "CandidateApplyWithdraw";

    public static final String FUNC_CANDIDATEWITHDRAW = "CandidateWithdraw";

    public static final String FUNC_SETCANDIDATEEXTRA = "SetCandidateExtra";

    public static final String FUNC_CANDIDATEWITHDRAWINFOS = "CandidateWithdrawInfos";

    public static final String FUNC_CANDIDATEDETAILS = "CandidateDetails";

    public static final String FUNC_CANDIDATELIST = "CandidateList";

    public static final String FUNC_VERIFIERSLIST = "VerifiersList";

    public static final Event CANDIDATEDEPOSITEVENT_EVENT = new Event("CandidateDepositEvent", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
    ;

    public static final Event CANDIDATEAPPLYWITHDRAWEVENT_EVENT = new Event("CandidateApplyWithdrawEvent", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
    ;

    public static final Event CANDIDATEWITHDRAWEVENT_EVENT = new Event("CandidateWithdrawEvent", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
    ;

    public static final Event SETCANDIDATEEXTRAEVENT_EVENT = new Event("SetCandidateExtraEvent", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
    ;

    @Deprecated
    protected CandidateConstract(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected CandidateConstract(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected CandidateConstract(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected CandidateConstract(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public RemoteCall<TransactionReceipt> CandidateDeposit(String nodeId, String owner, BigInteger fee, String host, String port, String extra) {
        final Function function = new Function(
                FUNC_CANDIDATEDEPOSIT, 
                Arrays.<Type>asList(new Utf8String(nodeId),
                new Utf8String(owner),
                new org.web3j.abi.datatypes.generated.Uint64(fee), 
                new Utf8String(host),
                new Utf8String(port),
                new Utf8String(extra)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> CandidateApplyWithdraw(String nodeId, BigInteger withdraw) {
        final Function function = new Function(
                FUNC_CANDIDATEAPPLYWITHDRAW, 
                Arrays.<Type>asList(new Utf8String(nodeId),
                new org.web3j.abi.datatypes.generated.Uint256(withdraw)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> CandidateWithdraw(String nodeId) {
        final Function function = new Function(
                FUNC_CANDIDATEWITHDRAW, 
                Arrays.<Type>asList(new Utf8String(nodeId)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> SetCandidateExtra(String nodeId, String extra) {
        final Function function = new Function(
                FUNC_SETCANDIDATEEXTRA, 
                Arrays.<Type>asList(new Utf8String(nodeId),
                new Utf8String(extra)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<String> CandidateWithdrawInfos(String nodeId) {
        final Function function = new Function(FUNC_CANDIDATEWITHDRAWINFOS, 
                Arrays.<Type>asList(new Utf8String(nodeId)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<String> CandidateDetails(String nodeId) {
        final Function function = new Function(FUNC_CANDIDATEDETAILS, 
                Arrays.<Type>asList(new Utf8String(nodeId)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<String> CandidateList() {
        final Function function = new Function(FUNC_CANDIDATELIST, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<String> VerifiersList() {
        final Function function = new Function(FUNC_VERIFIERSLIST, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public List<CandidateDepositEventEventResponse> getCandidateDepositEventEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(CANDIDATEDEPOSITEVENT_EVENT, transactionReceipt);
        ArrayList<CandidateDepositEventEventResponse> responses = new ArrayList<CandidateDepositEventEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            CandidateDepositEventEventResponse typedResponse = new CandidateDepositEventEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.param1 = (String) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<CandidateDepositEventEventResponse> candidateDepositEventEventObservable(EthFilter filter) {
        return web3j.ethLogObservable(filter).map(new Func1<Log, CandidateDepositEventEventResponse>() {
            @Override
            public CandidateDepositEventEventResponse call(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(CANDIDATEDEPOSITEVENT_EVENT, log);
                CandidateDepositEventEventResponse typedResponse = new CandidateDepositEventEventResponse();
                typedResponse.log = log;
                typedResponse.param1 = (String) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public Observable<CandidateDepositEventEventResponse> candidateDepositEventEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(CANDIDATEDEPOSITEVENT_EVENT));
        return candidateDepositEventEventObservable(filter);
    }

    public List<CandidateApplyWithdrawEventEventResponse> getCandidateApplyWithdrawEventEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(CANDIDATEAPPLYWITHDRAWEVENT_EVENT, transactionReceipt);
        ArrayList<CandidateApplyWithdrawEventEventResponse> responses = new ArrayList<CandidateApplyWithdrawEventEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            CandidateApplyWithdrawEventEventResponse typedResponse = new CandidateApplyWithdrawEventEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.param1 = (String) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<CandidateApplyWithdrawEventEventResponse> candidateApplyWithdrawEventEventObservable(EthFilter filter) {
        return web3j.ethLogObservable(filter).map(new Func1<Log, CandidateApplyWithdrawEventEventResponse>() {
            @Override
            public CandidateApplyWithdrawEventEventResponse call(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(CANDIDATEAPPLYWITHDRAWEVENT_EVENT, log);
                CandidateApplyWithdrawEventEventResponse typedResponse = new CandidateApplyWithdrawEventEventResponse();
                typedResponse.log = log;
                typedResponse.param1 = (String) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public Observable<CandidateApplyWithdrawEventEventResponse> candidateApplyWithdrawEventEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(CANDIDATEAPPLYWITHDRAWEVENT_EVENT));
        return candidateApplyWithdrawEventEventObservable(filter);
    }

    public List<CandidateWithdrawEventEventResponse> getCandidateWithdrawEventEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(CANDIDATEWITHDRAWEVENT_EVENT, transactionReceipt);
        ArrayList<CandidateWithdrawEventEventResponse> responses = new ArrayList<CandidateWithdrawEventEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            CandidateWithdrawEventEventResponse typedResponse = new CandidateWithdrawEventEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.param1 = (String) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<CandidateWithdrawEventEventResponse> candidateWithdrawEventEventObservable(EthFilter filter) {
        return web3j.ethLogObservable(filter).map(new Func1<Log, CandidateWithdrawEventEventResponse>() {
            @Override
            public CandidateWithdrawEventEventResponse call(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(CANDIDATEWITHDRAWEVENT_EVENT, log);
                CandidateWithdrawEventEventResponse typedResponse = new CandidateWithdrawEventEventResponse();
                typedResponse.log = log;
                typedResponse.param1 = (String) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public Observable<CandidateWithdrawEventEventResponse> candidateWithdrawEventEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(CANDIDATEWITHDRAWEVENT_EVENT));
        return candidateWithdrawEventEventObservable(filter);
    }

    public List<SetCandidateExtraEventEventResponse> getSetCandidateExtraEventEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(SETCANDIDATEEXTRAEVENT_EVENT, transactionReceipt);
        ArrayList<SetCandidateExtraEventEventResponse> responses = new ArrayList<SetCandidateExtraEventEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            SetCandidateExtraEventEventResponse typedResponse = new SetCandidateExtraEventEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.param1 = (String) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<SetCandidateExtraEventEventResponse> setCandidateExtraEventEventObservable(EthFilter filter) {
        return web3j.ethLogObservable(filter).map(new Func1<Log, SetCandidateExtraEventEventResponse>() {
            @Override
            public SetCandidateExtraEventEventResponse call(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(SETCANDIDATEEXTRAEVENT_EVENT, log);
                SetCandidateExtraEventEventResponse typedResponse = new SetCandidateExtraEventEventResponse();
                typedResponse.log = log;
                typedResponse.param1 = (String) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public Observable<SetCandidateExtraEventEventResponse> setCandidateExtraEventEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(SETCANDIDATEEXTRAEVENT_EVENT));
        return setCandidateExtraEventEventObservable(filter);
    }

    public static RemoteCall<CandidateConstract> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(CandidateConstract.class, web3j, credentials, contractGasProvider, BINARY, ABI, "");
    }

    @Deprecated
    public static RemoteCall<CandidateConstract> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(CandidateConstract.class, web3j, credentials, gasPrice, gasLimit, BINARY, ABI, "");
    }

    public static RemoteCall<CandidateConstract> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(CandidateConstract.class, web3j, transactionManager, contractGasProvider, BINARY, ABI, "");
    }

    @Deprecated
    public static RemoteCall<CandidateConstract> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(CandidateConstract.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, ABI, "");
    }

    @Deprecated
    public static CandidateConstract load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new CandidateConstract(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static CandidateConstract load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new CandidateConstract(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static CandidateConstract load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new CandidateConstract(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static CandidateConstract load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new CandidateConstract(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static class CandidateDepositEventEventResponse {
        public Log log;

        public String param1;
    }

    public static class CandidateApplyWithdrawEventEventResponse {
        public Log log;

        public String param1;
    }

    public static class CandidateWithdrawEventEventResponse {
        public Log log;

        public String param1;
    }

    public static class SetCandidateExtraEventEventResponse {
        public Log log;

        public String param1;
    }
}

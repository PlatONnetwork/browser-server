package com.platon.browser.v0152.contract;


import com.platon.abi.solidity.EventEncoder;
import com.platon.abi.solidity.TypeReference;
import com.platon.abi.solidity.datatypes.*;
import com.platon.abi.solidity.datatypes.generated.Uint256;
import com.platon.abi.solidity.datatypes.generated.Uint8;
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

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://github.com/PlatONnetwork/client-sdk-java/releases">platon-web3j command line tools</a>,
 * or the com.platon.codegen.SolidityFunctionWrapperGenerator in the
 * <a href="https://github.com/PlatONnetwork/client-sdk-java/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 1.1.0.0.
 */
public class Erc1155Contract extends Contract implements ErcContract {
    private static final String BINARY = "608060405234801561001057600080fd5b50336000806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550610b45806100606000396000f3fe608060405234801561001057600080fd5b50600436106100a4576000357c0100000000000000000000000000000000000000000000000000000000900480634e1273f4116100785780634e1273f41461037a578063a22cb4651461049d578063e985e9c5146104ed578063f242432a14610569576100a4565b8062fdd58e146100a957806301ffc9a71461010b5780630e89341c146101705780632eb2c2d614610217575b600080fd5b6100f5600480360360408110156100bf57600080fd5b81019080803573ffffffffffffffffffffffffffffffffffffffff16906020019092919080359060200190929190505050610636565b6040518082815260200191505060405180910390f35b6101566004803603602081101561012157600080fd5b8101908080357bffffffffffffffffffffffffffffffffffffffffffffffffffffffff19169060200190929190505050610642565b604051808215151515815260200191505060405180910390f35b61019c6004803603602081101561018657600080fd5b8101908080359060200190929190505050610774565b6040518080602001828103825283818151815260200191508051906020019080838360005b838110156101dc5780820151818401526020810190506101c1565b50505050905090810190601f1680156102095780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b610378600480360360a081101561022d57600080fd5b81019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190803573ffffffffffffffffffffffffffffffffffffffff1690602001909291908035906020019064010000000081111561028a57600080fd5b82018360208201111561029c57600080fd5b803590602001918460208302840111640100000000831117156102be57600080fd5b9091929391929390803590602001906401000000008111156102df57600080fd5b8201836020820111156102f157600080fd5b8035906020019184602083028401116401000000008311171561031357600080fd5b90919293919293908035906020019064010000000081111561033457600080fd5b82018360208201111561034657600080fd5b8035906020019184600183028401116401000000008311171561036857600080fd5b909192939192939050505061079a565b005b6104466004803603604081101561039057600080fd5b81019080803590602001906401000000008111156103ad57600080fd5b8201836020820111156103bf57600080fd5b803590602001918460208302840111640100000000831117156103e157600080fd5b90919293919293908035906020019064010000000081111561040257600080fd5b82018360208201111561041457600080fd5b8035906020019184602083028401116401000000008311171561043657600080fd5b9091929391929390505050610886565b6040518080602001828103825283818151815260200191508051906020019060200280838360005b8381101561048957808201518184015260208101905061046e565b505050509050019250505060405180910390f35b6104eb600480360360408110156104b357600080fd5b81019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190803515159060200190929190505050610905565b005b61054f6004803603604081101561050357600080fd5b81019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190803573ffffffffffffffffffffffffffffffffffffffff169060200190929190505050610972565b604051808215151515815260200191505060405180910390f35b610634600480360360a081101561057f57600080fd5b81019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190803573ffffffffffffffffffffffffffffffffffffffff1690602001909291908035906020019092919080359060200190929190803590602001906401000000008111156105f057600080fd5b82018360208201111561060257600080fd5b8035906020019184600183028401116401000000008311171561062457600080fd5b909192939192939050505061097e565b005b60006001905092915050565b60006301ffc9a77c010000000000000000000000000000000000000000000000000000000002827bffffffffffffffffffffffffffffffffffffffffffffffffffffffff191614806106d55750630e89341c7c010000000000000000000000000000000000000000000000000000000002827bffffffffffffffffffffffffffffffffffffffffffffffffffffffff1916145b80610721575063d9b67a267c010000000000000000000000000000000000000000000000000000000002827bffffffffffffffffffffffffffffffffffffffffffffffffffffffff1916145b8061076d5750634e2312e07c010000000000000000000000000000000000000000000000000000000002827bffffffffffffffffffffffffffffffffffffffffffffffffffffffff1916145b9050919050565b60606101406040519081016040528061010f8152602001610a0b61010f91399050919050565b8673ffffffffffffffffffffffffffffffffffffffff168873ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff167f4a39dc06d4c0dbc64b70af90fd698a233a518aa5d07e595d983b8c0526c8f7fb898989896040518080602001806020018381038352878782818152602001925060200280828437600081840152601f19601f8201169050808301925050508381038252858582818152602001925060200280828437600081840152601f19601f820116905080830192505050965050505050505060405180910390a45050505050505050565b606080858590506040519080825280602002602001820160405280156108bb5781602001602082028038833980820191505090505b50905060008090505b868690508110156108f857600182828151811015156108df57fe5b90602001906020020181815250508060010190506108c4565b5080915050949350505050565b8173ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff167f17307eab39ab6107e8899845ad3d59bd9653f200f220920489ca2b5937696c3183604051808215151515815260200191505060405180910390a35050565b60006001905092915050565b8473ffffffffffffffffffffffffffffffffffffffff168673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff167fc3d58168c5ae7397731d063d5bbf3d657854427343f4c083240f7aacaa2d0f628787604051808381526020018281526020019250505060405180910390a450505050505056fe7b227469746c65223a2244514173736574222c2274797065223a226f626a656374222c2270726f70657274696573223a7b226e616d65223a7b2274797065223a22737472696e67222c226465736372697074696f6e223a224451426f6f6b227d2c226465736372697074696f6e223a7b2274797065223a22737472696e67222c226465736372697074696f6e223a224451426f6f6b2d6465736372697074696f6e227d2c22696d616765223a7b2274797065223a22737472696e6722202c226465736372697074696f6e223a2268747470733a2f2f7374617469632e7170617373706f72742e696f2f7265736f75726365732f696d616765732f6e66742f7170617373706f72742e706e67227d7d7da165627a7a72305820974cadb2f7e372929a6fe5ff79d98a6d144ee33e154b3943d49fd1958b3116370029";

    public static final String FUNC_BALANCEOF = "balanceOf";

    public static final String FUNC_SUPPORTSINTERFACE = "supportsInterface";

    public static final String FUNC_URI = "uri";

    public static final String FUNC_SAFEBATCHTRANSFERFROM = "safeBatchTransferFrom";

    public static final String FUNC_BALANCEOFBATCH = "balanceOfBatch";

    public static final String FUNC_SETAPPROVALFORALL = "setApprovalForAll";

    public static final String FUNC_ISAPPROVEDFORALL = "isApprovedForAll";

    public static final String FUNC_SAFETRANSFERFROM = "safeTransferFrom";

    public static final String FUNC_NAME = "name";

    public static final String FUNC_SYMBOL = "symbol";

    public static final String FUNC_TOTALSUPPLY = "totalSupply";

    public static final String FUNC_DECIMALS = "decimals";

    public static final Event TRANSFERSINGLE_EVENT = new Event("TransferSingle",
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {
            }, new TypeReference<Address>(true) {
            }, new TypeReference<Address>(true) {
            }, new TypeReference<Uint256>() {
            }, new TypeReference<Uint256>() {
            }));
    ;

    public static final Event TRANSFERBATCH_EVENT = new Event("TransferBatch",
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {
            }, new TypeReference<Address>(true) {
            }, new TypeReference<Address>(true) {
            }, new TypeReference<DynamicArray<Uint256>>() {
            }, new TypeReference<DynamicArray<Uint256>>() {
            }));
    ;

    public static final Event APPROVALFORALL_EVENT = new Event("ApprovalForAll",
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {
            }, new TypeReference<Address>(true) {
            }, new TypeReference<Bool>() {
            }));
    ;

    public static final Event URI_EVENT = new Event("URI",
            Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {
            }, new TypeReference<Uint256>(true) {
            }));
    ;

    protected Erc1155Contract(String contractAddress, Web3j web3j, Credentials credentials, GasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    protected Erc1155Contract(String contractAddress, Web3j web3j, TransactionManager transactionManager, GasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    @Override
    public RemoteCall<BigInteger> balanceOf(String _owner, BigInteger _id) {
        final Function function = new Function(FUNC_BALANCEOF,
                Arrays.<Type>asList(new Address(_owner),
                        new Uint256(_id)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {
                }));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<Boolean> supportsInterface(byte[] interfaceID) {
        final Function function = new Function(FUNC_SUPPORTSINTERFACE,
                Arrays.<Type>asList(new com.platon.abi.solidity.datatypes.generated.Bytes4(interfaceID)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {
                }));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteCall<String> uri(BigInteger _id) {
        final Function function = new Function(FUNC_URI,
                Arrays.<Type>asList(new Uint256(_id)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {
                }));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<TransactionReceipt> safeBatchTransferFrom(String _from, String _to, List<BigInteger> _ids, List<BigInteger> _values, byte[] _data) {
        final Function function = new Function(
                FUNC_SAFEBATCHTRANSFERFROM,
                Arrays.<Type>asList(new Address(_from),
                        new Address(_to),
                        new DynamicArray<>(
                                Uint256.class,
                                com.platon.abi.solidity.Utils.typeMap(_ids, Uint256.class)),
                        new DynamicArray<>(
                                Uint256.class,
                                com.platon.abi.solidity.Utils.typeMap(_values, Uint256.class)),
                        new DynamicBytes(_data)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<List> balanceOfBatch(List<String> _owners, List<BigInteger> _ids) {
        final Function function = new Function(FUNC_BALANCEOFBATCH,
                Arrays.<Type>asList(new DynamicArray<>(
                                Address.class,
                                com.platon.abi.solidity.Utils.typeMap(_owners, Address.class)),
                        new DynamicArray<>(
                                Uint256.class,
                                com.platon.abi.solidity.Utils.typeMap(_ids, Uint256.class))),
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicArray<Uint256>>() {
                }));
        return new RemoteCall<List>(
                () -> {
                    List<Type> result = (List<Type>) executeCallSingleValueReturn(function, List.class);
                    return convertToNative(result);
                });
    }

    public RemoteCall<TransactionReceipt> setApprovalForAll(String _operator, Boolean _approved) {
        final Function function = new Function(
                FUNC_SETAPPROVALFORALL,
                Arrays.<Type>asList(new Address(_operator),
                        new Bool(_approved)),
                Collections.emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<Boolean> isApprovedForAll(String _owner, String _operator) {
        final Function function = new Function(FUNC_ISAPPROVEDFORALL,
                Arrays.<Type>asList(new Address(_owner),
                        new Address(_operator)),
                Arrays.asList(new TypeReference<Bool>() {
                }));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteCall<TransactionReceipt> safeTransferFrom(String _from, String _to, BigInteger _id, BigInteger _value, byte[] _data) {
        final Function function = new Function(
                FUNC_SAFETRANSFERFROM,
                Arrays.<Type>asList(new Address(_from),
                        new Address(_to),
                        new Uint256(_id),
                        new Uint256(_value),
                        new DynamicBytes(_data)),
                Collections.emptyList());
        return executeRemoteCallTransaction(function);
    }

    public static RemoteCall<Erc1155Contract> deploy(Web3j web3j, Credentials credentials, GasProvider contractGasProvider) {
        return deployRemoteCall(Erc1155Contract.class, web3j, credentials, contractGasProvider, BINARY, "");
    }

    public static RemoteCall<Erc1155Contract> deploy(Web3j web3j, TransactionManager transactionManager, GasProvider contractGasProvider) {
        return deployRemoteCall(Erc1155Contract.class, web3j, transactionManager, contractGasProvider, BINARY, "");
    }

    public List<TransferSingleEventResponse> getTransferSingleEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(TRANSFERSINGLE_EVENT, transactionReceipt);
        ArrayList<TransferSingleEventResponse> responses = new ArrayList<>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            TransferSingleEventResponse typedResponse = new TransferSingleEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse._operator = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse._from = (String) eventValues.getIndexedValues().get(1).getValue();
            typedResponse._to = (String) eventValues.getIndexedValues().get(2).getValue();
            typedResponse._id = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse._value = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<TransferSingleEventResponse> transferSingleEventObservable(PlatonFilter filter) {
        return web3j.platonLogObservable(filter).map(log -> {
            EventValuesWithLog eventValues = extractEventParametersWithLog(TRANSFERSINGLE_EVENT, log);
            TransferSingleEventResponse typedResponse = new TransferSingleEventResponse();
            typedResponse.log = log;
            typedResponse._operator = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse._from = (String) eventValues.getIndexedValues().get(1).getValue();
            typedResponse._to = (String) eventValues.getIndexedValues().get(2).getValue();
            typedResponse._id = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse._value = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            return typedResponse;
        });
    }

    public Observable<TransferSingleEventResponse> transferSingleEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        PlatonFilter filter = new PlatonFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(TRANSFERSINGLE_EVENT));
        return transferSingleEventObservable(filter);
    }

    public List<TransferBatchEventResponse> getTransferBatchEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(TRANSFERBATCH_EVENT, transactionReceipt);
        ArrayList<TransferBatchEventResponse> responses = new ArrayList<>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            TransferBatchEventResponse typedResponse = new TransferBatchEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse._operator = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse._from = (String) eventValues.getIndexedValues().get(1).getValue();
            typedResponse._to = (String) eventValues.getIndexedValues().get(2).getValue();
            typedResponse._ids = (List<BigInteger>) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse._values = (List<BigInteger>) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<TransferBatchEventResponse> transferBatchEventObservable(PlatonFilter filter) {
        return web3j.platonLogObservable(filter).map(log -> {
            EventValuesWithLog eventValues = extractEventParametersWithLog(TRANSFERBATCH_EVENT, log);
            TransferBatchEventResponse typedResponse = new TransferBatchEventResponse();
            typedResponse.log = log;
            typedResponse._operator = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse._from = (String) eventValues.getIndexedValues().get(1).getValue();
            typedResponse._to = (String) eventValues.getIndexedValues().get(2).getValue();
            typedResponse._ids = (List<BigInteger>) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse._values = (List<BigInteger>) eventValues.getNonIndexedValues().get(1).getValue();
            return typedResponse;
        });
    }

    public Observable<TransferBatchEventResponse> transferBatchEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        PlatonFilter filter = new PlatonFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(TRANSFERBATCH_EVENT));
        return transferBatchEventObservable(filter);
    }

    public List<ApprovalForAllEventResponse> getApprovalForAllEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(APPROVALFORALL_EVENT, transactionReceipt);
        ArrayList<ApprovalForAllEventResponse> responses = new ArrayList<>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            ApprovalForAllEventResponse typedResponse = new ApprovalForAllEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse._owner = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse._operator = (String) eventValues.getIndexedValues().get(1).getValue();
            typedResponse._approved = (Boolean) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<ApprovalForAllEventResponse> approvalForAllEventObservable(PlatonFilter filter) {
        return web3j.platonLogObservable(filter).map(log -> {
            EventValuesWithLog eventValues = extractEventParametersWithLog(APPROVALFORALL_EVENT, log);
            ApprovalForAllEventResponse typedResponse = new ApprovalForAllEventResponse();
            typedResponse.log = log;
            typedResponse._owner = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse._operator = (String) eventValues.getIndexedValues().get(1).getValue();
            typedResponse._approved = (Boolean) eventValues.getNonIndexedValues().get(0).getValue();
            return typedResponse;
        });
    }

    public Observable<ApprovalForAllEventResponse> approvalForAllEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        PlatonFilter filter = new PlatonFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(APPROVALFORALL_EVENT));
        return approvalForAllEventObservable(filter);
    }

    public List<URIEventResponse> getURIEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(URI_EVENT, transactionReceipt);
        ArrayList<URIEventResponse> responses = new ArrayList<>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            URIEventResponse typedResponse = new URIEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse._id = (BigInteger) eventValues.getIndexedValues().get(0).getValue();
            typedResponse._value = (String) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<URIEventResponse> uRIEventObservable(PlatonFilter filter) {
        return web3j.platonLogObservable(filter).map(log -> {
            EventValuesWithLog eventValues = extractEventParametersWithLog(URI_EVENT, log);
            URIEventResponse typedResponse = new URIEventResponse();
            typedResponse.log = log;
            typedResponse._id = (BigInteger) eventValues.getIndexedValues().get(0).getValue();
            typedResponse._value = (String) eventValues.getNonIndexedValues().get(0).getValue();
            return typedResponse;
        });
    }

    public Observable<URIEventResponse> uRIEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        PlatonFilter filter = new PlatonFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(URI_EVENT));
        return uRIEventObservable(filter);
    }

    public static Erc1155Contract load(String contractAddress, Web3j web3j, Credentials credentials, GasProvider contractGasProvider) {
        return new Erc1155Contract(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static Erc1155Contract load(String contractAddress, Web3j web3j, Credentials credentials, GasProvider contractGasProvider, BigInteger blockNumber) {
        Erc1155Contract erc1155Contract = new Erc1155Contract(contractAddress, web3j, credentials, contractGasProvider);
        erc1155Contract.setDefaultBlockParameter(DefaultBlockParameter.valueOf(blockNumber));
        return erc1155Contract;
    }

    public static Erc1155Contract load(String contractAddress, Web3j web3j, TransactionManager transactionManager, GasProvider contractGasProvider) {
        return new Erc1155Contract(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    @Override
    public RemoteCall<String> name() {
        final Function function = new Function(FUNC_NAME, Collections.emptyList(), Collections.singletonList(new TypeReference<Utf8String>() {
        }));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    @Override
    public RemoteCall<String> symbol() {
        final Function function = new Function(FUNC_SYMBOL, Collections.emptyList(), Collections.singletonList(new TypeReference<Utf8String>() {
        }));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    @Override
    public RemoteCall<BigInteger> decimals() {
        final Function function = new Function(FUNC_DECIMALS, Collections.emptyList(), Collections.singletonList(new TypeReference<Uint8>() {
        }));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }


    @Override
    public RemoteCall<BigInteger> totalSupply() {
        final Function function = new Function(FUNC_TOTALSUPPLY, Collections.emptyList(), Collections.singletonList(new TypeReference<Uint256>() {
        }));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    /**
     * 获取单个时间的
     *
     * @param transactionReceipt
     * @return
     */
    @Override
    public List<ErcTxEvent> getTxEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(TRANSFERSINGLE_EVENT, transactionReceipt);
        ArrayList<ErcTxEvent> responses = new ArrayList<>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            // 不是ERC1155 标准事件
            if (eventValues.getIndexedValues().size() != 3) {
                continue;
            }
            // 不是ERC1155 标准事件
            if (eventValues.getNonIndexedValues().size() != 2) {
                continue;
            }
            // tokenId 不存在事件，说明事件不是标准的事件
            BigInteger tokenId = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            if (tokenId == null) {
                continue;
            }

            ErcTxEvent response = new ErcTxEvent();
            response.setLog(eventValues.getLog());
            response.setOperator((String) eventValues.getIndexedValues().get(0).getValue());
            response.setFrom((String) eventValues.getIndexedValues().get(1).getValue());
            response.setTo((String) eventValues.getIndexedValues().get(2).getValue());

            response.setTokenId(tokenId);
            response.setValue((BigInteger) eventValues.getNonIndexedValues().get(1).getValue());
            responses.add(response);
        }

        /**
         * 获取批量转账的事件
         */
        List<EventValuesWithLog> batchValueList = extractEventParametersWithLog(TRANSFERBATCH_EVENT, transactionReceipt);
        for (EventValuesWithLog eventValues : batchValueList) {
            Log log = eventValues.getLog();

            // 不是ERC1155 标准事件
            if (eventValues.getIndexedValues().size() != 3) {
                continue;
            }
            // 不是ERC1155 标准事件
            if (eventValues.getNonIndexedValues().size() != 2) {
                continue;
            }

            String operator = (String) eventValues.getIndexedValues().get(0).getValue();
            String from = (String) eventValues.getIndexedValues().get(1).getValue();
            String to = (String) eventValues.getIndexedValues().get(2).getValue();

            List<Uint256> tokenIds = (List<Uint256>) eventValues.getNonIndexedValues().get(0).getValue();

            List<Uint256> values = (List<Uint256>) eventValues.getNonIndexedValues().get(1).getValue();

            /**
             * token和value的数量要一才能一一对应
             */
            if (tokenIds.size() == values.size()) {
                for (int i = 0; i < tokenIds.size(); ++i) {
                    ErcTxEvent response = new ErcTxEvent();
                    response.setLog(log);
                    response.setOperator(operator);
                    response.setFrom(from);
                    response.setTo(to);

                    Uint256 uToken = tokenIds.get(i);
                    Uint256 uValue = values.get(i);

                    response.setTokenId(uToken.getValue());
                    response.setValue(uValue.getValue());
                    responses.add(response);
                }
            }
        }
        return responses;
    }


    @Override
    public RemoteCall<String> getTokenURI(BigInteger _tokenId) {
        final Function function = new Function(FUNC_URI, Collections.singletonList(new Uint256(_tokenId)), Collections.singletonList(new TypeReference<Utf8String>() {
        }));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public static class TransferSingleEventResponse {
        public Log log;

        public String _operator;

        public String _from;

        public String _to;

        public BigInteger _id;

        public BigInteger _value;
    }

    public static class TransferBatchEventResponse {
        public Log log;

        public String _operator;

        public String _from;

        public String _to;

        public List<BigInteger> _ids;

        public List<BigInteger> _values;
    }

    public static class ApprovalForAllEventResponse {
        public Log log;

        public String _owner;

        public String _operator;

        public Boolean _approved;
    }

    public static class URIEventResponse {
        public Log log;

        public BigInteger _id;

        public String _value;
    }
}

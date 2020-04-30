package com.platon.browser.evm.bean;

import org.web3j.abi.EventEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.*;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.request.PlatonFilter;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.GasProvider;
import rx.Observable;
import rx.functions.Func1;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 0.7.5.0.
 */
public class PressureContract extends Contract {
    private static final String BINARY = "608060405234801561001057600080fd5b50610e58806100206000396000f30060806040526004361061006c576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff168062d90d831461007157806353ed514314610117578063960384a0146101a7578063e51ace1614610224578063f813c17f1461028d575b600080fd5b34801561007d57600080fd5b5061009c6004803603810190808035906020019092919050505061031d565b6040518080602001828103825283818151815260200191508051906020019080838360005b838110156100dc5780820151818401526020810190506100c1565b50505050905090810190601f1680156101095780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b34801561012357600080fd5b5061012c6103d8565b6040518080602001828103825283818151815260200191508051906020019080838360005b8381101561016c578082015181840152602081019050610151565b50505050905090810190601f1680156101995780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b3480156101b357600080fd5b5061020e600480360381019080803590602001908201803590602001908080601f016020809104026020016040519081016040528093929190818152602001838380828437820191505050505050919291929050505061065a565b6040518082815260200191505060405180910390f35b34801561023057600080fd5b5061028b600480360381019080803590602001908201803590602001908080601f01602080910402602001604051908101604052809392919081815260200183838082843782019150505050505091929192905050506106ce565b005b34801561029957600080fd5b506102a2610928565b6040518080602001828103825283818151815260200191508051906020019080838360005b838110156102e25780820151818401526020810190506102c7565b50505050905090810190601f16801561030f5780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b60018181548110151561032c57fe5b906000526020600020016000915090508054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156103d05780601f106103a5576101008083540402835291602001916103d0565b820191906000526020600020905b8154815290600101906020018083116103b357829003601f168201915b505050505081565b606080606060006040805190810160405280600181526020017f3a0000000000000000000000000000000000000000000000000000000000000081525092506040805190810160405280600181526020017f3b000000000000000000000000000000000000000000000000000000000000008152509150600090505b6001805490508110156106545761053561051e60018381548110151561047657fe5b906000526020600020018054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156105145780601f106104e957610100808354040283529160200191610514565b820191906000526020600020905b8154815290600101906020018083116104f757829003601f168201915b5050505050610a94565b61052786610a94565b610ac290919063ffffffff16565b935061055a61054384610a94565b61054c86610a94565b610ac290919063ffffffff16565b93506106106105f96105f4600060018581548110151561057657fe5b9060005260206000200160405180828054600181600116156101000203166002900480156105db5780601f106105b95761010080835404028352918201916105db565b820191906000526020600020905b8154815290600101906020018083116105c7575b5050915050908152602001604051809103902054610b44565b610a94565b61060286610a94565b610ac290919063ffffffff16565b935060018080549050038110156106475761064461062d83610a94565b61063686610a94565b610ac290919063ffffffff16565b93505b8080600101915050610454565b50505090565b600080826040518082805190602001908083835b602083101515610693578051825260208201915060208101905060208303925061066e565b6001836020036101000a0380198251168184511680821785525050505050509050019150509081526020016040518091039020549050919050565b600080826040518082805190602001908083835b60208310151561070757805182526020820191506020810190506020830392506106e2565b6001836020036101000a0380198251168184511680821785525050505050509050019150509081526020016040518091039020541415610800576000816040518082805190602001908083835b6020831015156107795780518252602082019150602081019050602083039250610754565b6001836020036101000a03801982511681845116808217855250505050505090500191505090815260200160405180910390206000815480929190600101919050555060018190806001815401808255809150509060018203906000526020600020016000909192909190915090805190602001906107f9929190610d6d565b505061087c565b6000816040518082805190602001908083835b6020831015156108385780518252602082019150602081019050602083039250610813565b6001836020036101000a0380198251168184511680821785525050505050509050019150509081526020016040518091039020600081548092919060010191905055505b7fd33a39e57a0bed7d65b7c55fda9cdef6524bb56330d94f58a0635b4a4c7cb2ef816001604051808060200183151515158152602001828103825284818151815260200191508051906020019080838360005b838110156108ea5780820151818401526020810190506108cf565b50505050905090810190601f1680156109175780820380516001836020036101000a031916815260200191505b50935050505060405180910390a150565b60608060006040805190810160405280600181526020017f7c000000000000000000000000000000000000000000000000000000000000008152509150600090505b600180549050811015610a8f57610a4b610a3460018381548110151561098c57fe5b906000526020600020018054600181600116156101000203166002900480601f016020809104026020016040519081016040528092919081815260200182805460018160011615610100020316600290048015610a2a5780601f106109ff57610100808354040283529160200191610a2a565b820191906000526020600020905b815481529060010190602001808311610a0d57829003601f168201915b5050505050610a94565b610a3d85610a94565b610ac290919063ffffffff16565b92506001808054905003811015610a8257610a7f610a6883610a94565b610a7185610a94565b610ac290919063ffffffff16565b92505b808060010191505061096a565b505090565b610a9c610ded565b600060208301905060408051908101604052808451815260200182815250915050919050565b606080600083600001518560000151016040519080825280601f01601f191660200182016040528015610b045781602001602082028038833980820191505090505b509150602082019050610b208186602001518760000151610d22565b610b398560000151820185602001518660000151610d22565b819250505092915050565b6060600060606000806060600060649550856040519080825280601f01601f191660200182016040528015610b885781602001602082028038833980820191505090505b509450600093505b600088141515610c2757600a88811515610ba657fe5b069250600a88811515610bb557fe5b049750826030017f0100000000000000000000000000000000000000000000000000000000000000028585806001019650815181101515610bf257fe5b9060200101907effffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff1916908160001a905350610b90565b600184016040519080825280601f01601f191660200182016040528015610c5d5781602001602082028038833980820191505090505b509150600090505b8381111515610d145784818503815181101515610c7e57fe5b9060200101517f010000000000000000000000000000000000000000000000000000000000000090047f0100000000000000000000000000000000000000000000000000000000000000028282815181101515610cd757fe5b9060200101907effffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff1916908160001a9053508080600101915050610c65565b819650505050505050919050565b60005b602082101515610d4a5782518452602084019350602083019250602082039150610d25565b6001826020036101000a0390508019835116818551168181178652505050505050565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f10610dae57805160ff1916838001178555610ddc565b82800160010185558215610ddc579182015b82811115610ddb578251825591602001919060010190610dc0565b5b509050610de99190610e07565b5090565b604080519081016040528060008152602001600081525090565b610e2991905b80821115610e25576000816000905550600101610e0d565b5090565b905600a165627a7a72305820ed1ac00ae197134654afb331be226cfcfad77e8c071854202bfffa52d1aafd2a0029";

    public static final String FUNC_NODEIDS = "nodeids";

    public static final String FUNC_GETALL = "getAll";

    public static final String FUNC_GETVALUE = "getValue";

    public static final String FUNC_RECORD = "record";

    public static final String FUNC_GETNODEIDLIST = "getNodeidList";

    public static final Event SETSUCCESS_EVENT = new Event("SetSuccess",
            Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Bool>() {}));
    ;

    @Deprecated
    protected PressureContract(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected PressureContract(String contractAddress, Web3j web3j, Credentials credentials, GasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected PressureContract(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected PressureContract(String contractAddress, Web3j web3j, TransactionManager transactionManager, GasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public RemoteCall<String> nodeids(BigInteger param0) {
        final Function function = new Function(FUNC_NODEIDS,
                Arrays.<Type>asList(new Uint256(param0)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<String> getAll() {
        final Function function = new Function(FUNC_GETALL,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<BigInteger> getValue(String nodeid) {
        final Function function = new Function(FUNC_GETVALUE,
                Arrays.<Type>asList(new Utf8String(nodeid)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<TransactionReceipt> record(String nodeid) {
        final Function function = new Function(
                FUNC_RECORD,
                Arrays.<Type>asList(new Utf8String(nodeid)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<String> getNodeidList() {
        final Function function = new Function(FUNC_GETNODEIDLIST,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public List<SetSuccessEventResponse> getSetSuccessEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(SETSUCCESS_EVENT, transactionReceipt);
        ArrayList<SetSuccessEventResponse> responses = new ArrayList<SetSuccessEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            SetSuccessEventResponse typedResponse = new SetSuccessEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.nodeid = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.flag = (Boolean) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<SetSuccessEventResponse> setSuccessEventObservable(PlatonFilter filter) {
        return web3j.platonLogObservable(filter).map(new Func1<Log, SetSuccessEventResponse>() {
            @Override
            public SetSuccessEventResponse call(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(SETSUCCESS_EVENT, log);
                SetSuccessEventResponse typedResponse = new SetSuccessEventResponse();
                typedResponse.log = log;
                typedResponse.nodeid = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.flag = (Boolean) eventValues.getNonIndexedValues().get(1).getValue();
                return typedResponse;
            }
        });
    }

    public Observable<SetSuccessEventResponse> setSuccessEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        PlatonFilter filter = new PlatonFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(SETSUCCESS_EVENT));
        return setSuccessEventObservable(filter);
    }

    public static RemoteCall<PressureContract> deploy(Web3j web3j, Credentials credentials, GasProvider contractGasProvider) {
        return deployRemoteCall(PressureContract.class, web3j, credentials, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<PressureContract> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(PressureContract.class, web3j, credentials, gasPrice, gasLimit, BINARY, "");
    }

    public static RemoteCall<PressureContract> deploy(Web3j web3j, TransactionManager transactionManager, GasProvider contractGasProvider) {
        return deployRemoteCall(PressureContract.class, web3j, transactionManager, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<PressureContract> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(PressureContract.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "");
    }

    @Deprecated
    public static PressureContract load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new PressureContract(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static PressureContract load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new PressureContract(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static PressureContract load(String contractAddress, Web3j web3j, Credentials credentials, GasProvider contractGasProvider) {
        return new PressureContract(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static PressureContract load(String contractAddress, Web3j web3j, TransactionManager transactionManager, GasProvider contractGasProvider) {
        return new PressureContract(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static class SetSuccessEventResponse {
        public Log log;

        public String nodeid;

        public Boolean flag;
    }
}

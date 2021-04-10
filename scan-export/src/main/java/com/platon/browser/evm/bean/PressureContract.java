package com.platon.browser.evm.bean;

import com.platon.abi.solidity.EventEncoder;
import com.platon.abi.solidity.FunctionEncoder;
import com.platon.abi.solidity.TypeReference;
import com.platon.abi.solidity.datatypes.*;
import com.platon.abi.solidity.datatypes.generated.Uint256;
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

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the com.alaya.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 0.7.5.0.
 */
public class PressureContract extends Contract {
    private static final String BINARY = "608060405234801561001057600080fd5b506040516040806109ce833981018060405281019080805190602001909291908051906020019092919050505033600460006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550816002819055508060038190555050506109318061009d6000396000f30060806040526004361061008d576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff168062d90d8314610092578063083c632314610138578063421ec101146101635780636a134fa71461019a5780637f74619d146101b1578063960384a014610208578063a690327814610285578063e51ace16146102b0575b600080fd5b34801561009e57600080fd5b506100bd60048036038101908080359060200190929190505050610319565b6040518080602001828103825283818151815260200191508051906020019080838360005b838110156100fd5780820151818401526020810190506100e2565b50505050905090810190601f16801561012a5780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b34801561014457600080fd5b5061014d6103d4565b6040518082815260200191505060405180910390f35b34801561016f57600080fd5b5061019860048036038101908080359060200190929190803590602001909291905050506103da565b005b3480156101a657600080fd5b506101af610443565b005b3480156101bd57600080fd5b506101c661054c565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b34801561021457600080fd5b5061026f600480360381019080803590602001908201803590602001908080601f0160208091040260200160405190810160405280939291908181526020018383808284378201915050505050509192919290505050610572565b6040518082815260200191505060405180910390f35b34801561029157600080fd5b5061029a6105e6565b6040518082815260200191505060405180910390f35b3480156102bc57600080fd5b50610317600480360381019080803590602001908201803590602001908080601f01602080910402602001604051908101604052809392919081815260200183838082843782019150505050505091929192905050506105ec565b005b60018181548110151561032857fe5b906000526020600020016000915090508054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156103cc5780601f106103a1576101008083540402835291602001916103cc565b820191906000526020600020905b8154815290600101906020018083116103af57829003601f168201915b505050505081565b60035481565b3373ffffffffffffffffffffffffffffffffffffffff16600460009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16141561043f5781600281905550806003819055505b5050565b60003373ffffffffffffffffffffffffffffffffffffffff16600460009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16141561054957600090505b600180549050811015610548576000806001838154811015156104bf57fe5b9060005260206000200160405180828054600181600116156101000203166002900480156105245780601f10610502576101008083540402835291820191610524565b820191906000526020600020905b815481529060010190602001808311610510575b505091505090815260200160405180910390208190555080806001019150506104a0565b5b50565b600460009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681565b600080826040518082805190602001908083835b6020831015156105ab5780518252602082019150602081019050602083039250610586565b6001836020036101000a0380198251168184511680821785525050505050509050019150509081526020016040518091039020549050919050565b60025481565b600254431015801561060057506003544311155b1561085d57600080826040518082805190602001908083835b60208310151561063e5780518252602082019150602081019050602083039250610619565b6001836020036101000a0380198251168184511680821785525050505050509050019150509081526020016040518091039020541415610737576000816040518082805190602001908083835b6020831015156106b0578051825260208201915060208101905060208303925061068b565b6001836020036101000a0380198251168184511680821785525050505050509050019150509081526020016040518091039020600081548092919060010191905055506001819080600181540180825580915050906001820390600052602060002001600090919290919091509080519060200190610730929190610860565b50506107b3565b6000816040518082805190602001908083835b60208310151561076f578051825260208201915060208101905060208303925061074a565b6001836020036101000a0380198251168184511680821785525050505050509050019150509081526020016040518091039020600081548092919060010191905055505b7fd33a39e57a0bed7d65b7c55fda9cdef6524bb56330d94f58a0635b4a4c7cb2ef816001604051808060200183151515158152602001828103825284818151815260200191508051906020019080838360005b83811015610821578082015181840152602081019050610806565b50505050905090810190601f16801561084e5780820380516001836020036101000a031916815260200191505b50935050505060405180910390a15b50565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f106108a157805160ff19168380011785556108cf565b828001600101855582156108cf579182015b828111156108ce5782518255916020019190600101906108b3565b5b5090506108dc91906108e0565b5090565b61090291905b808211156108fe5760008160009055506001016108e6565b5090565b905600a165627a7a723058200c42491f43cd381738eab81febf25e96e77ebeba08740dc7275de096c69b5d620029";

    public static final String FUNC_NODEIDS = "nodeids";

    public static final String FUNC_ENDBLOCK = "endBlock";

    public static final String FUNC_SETBEGINANDENDBLOCK = "setBeginAndEndBlock";

    public static final String FUNC_CLEARMAP = "clearMap";

    public static final String FUNC_CONTRACTCREATER = "contractCreater";

    public static final String FUNC_GETVALUE = "getValue";

    public static final String FUNC_BEGINBLOCK = "beginBlock";

    public static final String FUNC_RECORD = "record";

    public static final Event SETSUCCESS_EVENT = new Event("SetSuccess",
            Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Bool>() {}));
    ;

    protected PressureContract(String contractAddress, Web3j web3j, Credentials credentials, GasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
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

    public RemoteCall<BigInteger> endBlock() {
        final Function function = new Function(FUNC_ENDBLOCK,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<TransactionReceipt> setBeginAndEndBlock(BigInteger _beginBlock, BigInteger _endBlock) {
        final Function function = new Function(
                FUNC_SETBEGINANDENDBLOCK,
                Arrays.<Type>asList(new Uint256(_beginBlock),
                new Uint256(_endBlock)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> clearMap() {
        final Function function = new Function(
                FUNC_CLEARMAP,
                Arrays.<Type>asList(),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<String> contractCreater() {
        final Function function = new Function(FUNC_CONTRACTCREATER,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<BigInteger> getValue(String nodeid) {
        final Function function = new Function(FUNC_GETVALUE,
                Arrays.<Type>asList(new Utf8String(nodeid)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<BigInteger> beginBlock() {
        final Function function = new Function(FUNC_BEGINBLOCK,
                Arrays.<Type>asList(),
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

    public static RemoteCall<PressureContract> deploy(Web3j web3j, Credentials credentials, GasProvider contractGasProvider, BigInteger _beginBlock, BigInteger _endBlock,Long chainId) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new Uint256(_beginBlock),
                new Uint256(_endBlock)));
        return deployRemoteCall(PressureContract.class, web3j, credentials, contractGasProvider, BINARY, encodedConstructor);
    }

    public static RemoteCall<PressureContract> deploy(Web3j web3j, TransactionManager transactionManager, GasProvider contractGasProvider, BigInteger _beginBlock, BigInteger _endBlock,Long chainId) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new Uint256(_beginBlock),
                new Uint256(_endBlock)));
        return deployRemoteCall(PressureContract.class, web3j, transactionManager, contractGasProvider, BINARY, encodedConstructor);
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

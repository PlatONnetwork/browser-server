package com.platon.browser.v0152.contract;

import com.platon.abi.solidity.EventEncoder;
import com.platon.abi.solidity.FunctionEncoder;
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
import org.apache.http.MethodNotSupportedException;
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
 * or the com.alaya.codegen.SolidityFunctionWrapperGenerator in the
 * <a href="https://github.com/PlatONnetwork/client-sdk-java/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 0.13.2.1.
 */
public class Erc20Contract extends Contract implements ErcContract {

    private static final String BINARY = "60806040526007805460ff60a81b1960ff60a01b19909116600160a01b171690553480156200002d57600080fd5b506040516200110c3803806200110c833981810160405260a08110156200005357600080fd5b8151602083018051604051929492938301929190846401000000008211156200007b57600080fd5b9083019060208201858111156200009157600080fd5b8251640100000000811182820188101715620000ac57600080fd5b82525081516020918201929091019080838360005b83811015620000db578181015183820152602001620000c1565b50505050905090810190601f168015620001095780820380516001836020036101000a031916815260200191505b506040818152602083015192018051929491939192846401000000008211156200013257600080fd5b9083019060208201858111156200014857600080fd5b82516401000000008111828201881017156200016357600080fd5b82525081516020918201929091019080838360005b838110156200019257818101518382015260200162000178565b50505050905090810190601f168015620001c05780820380516001836020036101000a031916815260200191505b5060405260209081015160008890558651909350620001e6925060019187019062000291565b506002805460ff191660ff851617905581516200020b90600390602085019062000291565b50600780546001600160a01b038084166001600160a01b031992831617909255600680549091163317808255821660009081526004602090815260408083208a9055925483518a8152935194169391927fddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef929081900390910190a3505050505062000336565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f10620002d457805160ff191683800117855562000304565b8280016001018555821562000304579182015b8281111562000304578251825591602001919060010190620002e7565b506200031292915062000316565b5090565b6200033391905b808211156200031257600081556001016200031d565b90565b610dc680620003466000396000f3fe608060405234801561001057600080fd5b506004361061012c5760003560e01c806370a08231116100ad578063a9059cbb11610071578063a9059cbb1461031b578063af24d25c14610347578063be9a65551461034f578063dd62ed3e14610357578063f2c298be146103855761012c565b806370a08231146102c057806375f12b21146102e6578063835fc6ca146102ee5780638da5cb5b1461030b57806395d89b41146103135761012c565b8063313ce567116100f4578063313ce5671461024857806340ac254e146102665780634e485c521461028a57806358a639ba14610292578063670d14b21461029a5761012c565b806306fdde031461013157806307da68f5146101ae578063095ea7b3146101b857806318160ddd146101f857806323b872dd14610212575b600080fd5b61013961042b565b6040805160208082528351818301528351919283929083019185019080838360005b8381101561017357818101518382015260200161015b565b50505050905090810190601f1680156101a05780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b6101b66104b9565b005b6101e4600480360360408110156101ce57600080fd5b506001600160a01b0381351690602001356104e5565b604080519115158252519081900360200190f35b6102006105a5565b60408051918252519081900360200190f35b6101e46004803603606081101561022857600080fd5b506001600160a01b038135811691602081013590911690604001356105ac565b610250610765565b6040805160ff9092168252519081900360200190f35b61026e61076e565b604080516001600160a01b039092168252519081900360200190f35b6101b661077d565b6101e46107a9565b610139600480360360208110156102b057600080fd5b50356001600160a01b03166107b9565b610200600480360360208110156102d657600080fd5b50356001600160a01b0316610821565b6101e461083c565b6101b66004803603602081101561030457600080fd5b503561084c565b61026e6108d7565b6101396108e6565b6101e46004803603604081101561033157600080fd5b506001600160a01b038135169060200135610940565b6101b6610a65565b6101b6610a8b565b6102006004803603604081101561036d57600080fd5b506001600160a01b0381358116916020013516610ab1565b6101b66004803603602081101561039b57600080fd5b8101906020810181356401000000008111156103b657600080fd5b8201836020820111156103c857600080fd5b803590602001918460018302840111640100000000831117156103ea57600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600092019190915250929550610adc945050505050565b6003805460408051602060026001851615610100026000190190941693909304601f810184900484028201840190925281815292918301828280156104b15780601f10610486576101008083540402835291602001916104b1565b820191906000526020600020905b81548152906001019060200180831161049457829003601f168201915b505050505081565b6006546001600160a01b031633146104d057600080fd5b6007805460ff60a81b1916600160a81b179055565b600754600090600160a81b900460ff1615801561053557506006546001600160a01b031633148061052057506007546001600160a01b031633145b806105355750600754600160a01b900460ff16155b61053e57600080fd5b3360008181526005602090815260408083206001600160a01b03881680855290835292819020869055805186815290519293927f8c5be1e5ebec7d5bd14f71427d1e84f3dd0314c0f7b2291e5b200ac8c7c3b925929181900390910190a350600192915050565b6000545b90565b600754600090600160a81b900460ff161580156105fc57506006546001600160a01b03163314806105e757506007546001600160a01b031633145b806105fc5750600754600160a01b900460ff16155b61060557600080fd5b6001600160a01b03841660009081526004602052604090205482111561062a57600080fd5b6001600160a01b038416600090815260056020908152604080832033845290915290205482111561065a57600080fd5b6001600160a01b038416600090815260056020908152604080832033845290915290205461068e908363ffffffff610bbf16565b6001600160a01b0385166000818152600560209081526040808320338452825280832094909455918152600490915220546106cf908363ffffffff610bbf16565b6001600160a01b038086166000908152600460205260408082209390935590851681522054610704908363ffffffff610c0816565b6001600160a01b0380851660008181526004602090815260409182902094909455805186815290519193928816927fddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef92918290030190a35060019392505050565b60025460ff1681565b6007546001600160a01b031681565b6006546001600160a01b0316331461079457600080fd5b6007805460ff60a01b1916600160a01b179055565b600754600160a01b900460ff1681565b60086020908152600091825260409182902080548351601f6002600019610100600186161502019093169290920491820184900484028101840190945280845290918301828280156104b15780601f10610486576101008083540402835291602001916104b1565b6001600160a01b031660009081526004602052604090205490565b600754600160a81b900460ff1681565b6006546001600160a01b0316331461086357600080fd5b8047101561087057600080fd5b604051339082156108fc029083906000818181858888f1935050505015801561089d573d6000803e3d6000fd5b5060408051828152905133917fb4214c8c54fc7442f36d3682f59aebaf09358a4431835b30efb29d52cf9e1e91919081900360200190a250565b6006546001600160a01b031681565b60018054604080516020600284861615610100026000190190941693909304601f810184900484028201840190925281815292918301828280156104b15780601f10610486576101008083540402835291602001916104b1565b600754600090600160a81b900460ff1615801561099057506006546001600160a01b031633148061097b57506007546001600160a01b031633145b806109905750600754600160a01b900460ff16155b61099957600080fd5b336000908152600460205260409020548211156109b557600080fd5b336000908152600460205260409020546109d5908363ffffffff610bbf16565b33600090815260046020526040808220929092556001600160a01b03851681522054610a07908363ffffffff610c0816565b6001600160a01b0384166000818152600460209081526040918290209390935580518581529051919233927fddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef9281900390910190a350600192915050565b6006546001600160a01b03163314610a7c57600080fd5b6007805460ff60a01b19169055565b6006546001600160a01b03163314610aa257600080fd5b6007805460ff60a81b19169055565b6001600160a01b03918216600090815260056020908152604080832093909416825291909152205490565b600754600160a81b900460ff1615610af357600080fd5b60c881511115610b0257600080fd5b3360009081526008602090815260409091208251610b2292840190610cf9565b50604080516020808252835181830152835133937fd80364ba2cbb1e827ab8adac9651cdfc27fb7b61c0a95663cb80b82d7636ad229386939092839283019185019080838360005b83811015610b82578181015183820152602001610b6a565b50505050905090810190601f168015610baf5780820380516001836020036101000a031916815260200191505b509250505060405180910390a250565b6000610c0183836040518060400160405280601e81526020017f536166654d6174683a207375627472616374696f6e206f766572666c6f770000815250610c62565b9392505050565b600082820183811015610c01576040805162461bcd60e51b815260206004820152601b60248201527f536166654d6174683a206164646974696f6e206f766572666c6f770000000000604482015290519081900360640190fd5b60008184841115610cf15760405162461bcd60e51b81526004018080602001828103825283818151815260200191508051906020019080838360005b83811015610cb6578181015183820152602001610c9e565b50505050905090810190601f168015610ce35780820380516001836020036101000a031916815260200191505b509250505060405180910390fd5b505050900390565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f10610d3a57805160ff1916838001178555610d67565b82800160010185558215610d67579182015b82811115610d67578251825591602001919060010190610d4c565b50610d73929150610d77565b5090565b6105a991905b80821115610d735760008155600101610d7d56fea265627a7a72315820769cc2acc0d4cfcc83148f93b7e2a916a2522106bbc879b9df730a67c9f6b2a064736f6c63430005110032";

    public static final String FUNC_ALLOWANCE = "allowance";

    public static final String FUNC_APPROVE = "approve";

    public static final String FUNC_BALANCEOF = "balanceOf";

    public static final String FUNC_CLOSETRANSFER = "closeTransfer";

    public static final String FUNC_DECIMALS = "decimals";

    public static final String FUNC_KEYS = "keys";

    public static final String FUNC_NAME = "name";

    public static final String FUNC_OPENTRANSFER = "openTransfer";

    public static final String FUNC_OWNER = "owner";

    public static final String FUNC_REGISTER = "register";

    public static final String FUNC_REWARDVALUEADDR = "rewardValueAddr";

    public static final String FUNC_START = "start";

    public static final String FUNC_STOP = "stop";

    public static final String FUNC_STOPPED = "stopped";

    public static final String FUNC_SYMBOL = "symbol";

    public static final String FUNC_TOTALSUPPLY = "totalSupply";

    public static final String FUNC_TRANSFER = "transfer";

    public static final String FUNC_TRANSFERCLOSED = "transferClosed";

    public static final String FUNC_TRANSFERFROM = "transferFrom";

    public static final String FUNC_WITHDRAWAL = "withdrawal";

    public static final Event APPROVAL_EVENT = new Event("Approval", Arrays.asList(new TypeReference<Address>(true) {
    }, new TypeReference<Address>(true) {
    }, new TypeReference<Uint256>() {
    }));

    public static final Event LOGREGISTER_EVENT = new Event("LogRegister", Arrays.asList(new TypeReference<Address>(true) {
    }, new TypeReference<Utf8String>() {
    }));

    public static final Event LOGWITHDRAWAL_EVENT = new Event("LogWithdrawal", Arrays.asList(new TypeReference<Address>(true) {
    }, new TypeReference<Uint256>() {
    }));

    public static final Event TRANSFER_EVENT = new Event("Transfer", Arrays.asList(new TypeReference<Address>(true) {
    }, new TypeReference<Address>(true) {
    }, new TypeReference<Uint256>() {
    }));

    protected Erc20Contract(String contractAddress, Web3j web3j, Credentials credentials, GasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    protected Erc20Contract(String contractAddress, Web3j web3j, TransactionManager transactionManager, GasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<Erc20Contract> deploy(Web3j web3j, Credentials credentials, GasProvider contractGasProvider, BigInteger _supply, String _symbol, BigInteger _decimals, String _name, String _rewardValueAddr) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.asList(new Uint256(_supply), new Utf8String(_symbol), new Uint8(_decimals), new Utf8String(_name), new Address(_rewardValueAddr)));
        return deployRemoteCall(Erc20Contract.class, web3j, credentials, contractGasProvider, BINARY, encodedConstructor);
    }

    public static RemoteCall<Erc20Contract> deploy(Web3j web3j, TransactionManager transactionManager, GasProvider contractGasProvider, BigInteger _supply, String _symbol, BigInteger _decimals, String _name, String _rewardValueAddr) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.asList(new Uint256(_supply), new Utf8String(_symbol), new Uint8(_decimals), new Utf8String(_name), new Address(_rewardValueAddr)));
        return deployRemoteCall(Erc20Contract.class, web3j, transactionManager, contractGasProvider, BINARY, encodedConstructor);
    }

    public List<ApprovalEventResponse> getApprovalEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(APPROVAL_EVENT, transactionReceipt);
        ArrayList<ApprovalEventResponse> responses = new ArrayList<>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            ApprovalEventResponse typedResponse = new ApprovalEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse._owner = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse._spender = (String) eventValues.getIndexedValues().get(1).getValue();
            typedResponse._value = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<ApprovalEventResponse> approvalEventObservable(PlatonFilter filter) {
        return web3j.platonLogObservable(filter).map(log -> {
            EventValuesWithLog eventValues = extractEventParametersWithLog(APPROVAL_EVENT, log);
            ApprovalEventResponse typedResponse = new ApprovalEventResponse();
            typedResponse.log = log;
            typedResponse._owner = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse._spender = (String) eventValues.getIndexedValues().get(1).getValue();
            typedResponse._value = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            return typedResponse;
        });
    }

    public Observable<ApprovalEventResponse> approvalEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        PlatonFilter filter = new PlatonFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(APPROVAL_EVENT));
        return approvalEventObservable(filter);
    }

    public List<LogRegisterEventResponse> getLogRegisterEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(LOGREGISTER_EVENT, transactionReceipt);
        ArrayList<LogRegisterEventResponse> responses = new ArrayList<>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            LogRegisterEventResponse typedResponse = new LogRegisterEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.user = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.key = (String) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<LogRegisterEventResponse> logRegisterEventObservable(PlatonFilter filter) {
        return web3j.platonLogObservable(filter).map(log -> {
            EventValuesWithLog eventValues = extractEventParametersWithLog(LOGREGISTER_EVENT, log);
            LogRegisterEventResponse typedResponse = new LogRegisterEventResponse();
            typedResponse.log = log;
            typedResponse.user = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.key = (String) eventValues.getNonIndexedValues().get(0).getValue();
            return typedResponse;
        });
    }

    public Observable<LogRegisterEventResponse> logRegisterEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        PlatonFilter filter = new PlatonFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(LOGREGISTER_EVENT));
        return logRegisterEventObservable(filter);
    }

    public List<LogWithdrawalEventResponse> getLogWithdrawalEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(LOGWITHDRAWAL_EVENT, transactionReceipt);
        ArrayList<LogWithdrawalEventResponse> responses = new ArrayList<>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            LogWithdrawalEventResponse typedResponse = new LogWithdrawalEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.user = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse._value = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<LogWithdrawalEventResponse> logWithdrawalEventObservable(PlatonFilter filter) {
        return web3j.platonLogObservable(filter).map(log -> {
            EventValuesWithLog eventValues = extractEventParametersWithLog(LOGWITHDRAWAL_EVENT, log);
            LogWithdrawalEventResponse typedResponse = new LogWithdrawalEventResponse();
            typedResponse.log = log;
            typedResponse.user = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse._value = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            return typedResponse;
        });
    }

    public Observable<LogWithdrawalEventResponse> logWithdrawalEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        PlatonFilter filter = new PlatonFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(LOGWITHDRAWAL_EVENT));
        return logWithdrawalEventObservable(filter);
    }

    @Override
    public List<ErcTxEvent> getTxEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(TRANSFER_EVENT, transactionReceipt);
        ArrayList<ErcTxEvent> responses = new ArrayList<>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            ErcTxEvent resp = new ErcTxEvent();
            resp.setLog(eventValues.getLog());
            resp.setFrom((String) eventValues.getIndexedValues().get(0).getValue());
            resp.setTo((String) eventValues.getIndexedValues().get(1).getValue());
            resp.setValue((BigInteger) eventValues.getNonIndexedValues().get(0).getValue());
            responses.add(resp);
        }
        return responses;
    }

    @Override
    public RemoteCall<String> getTokenURI(BigInteger tokenId) throws MethodNotSupportedException {
        throw new MethodNotSupportedException("ARC20 NOT SUPPORT METHOD [getTokenURI]");
    }

    public Observable<ErcTxEvent> transferEventObservable(PlatonFilter filter) {
        return web3j.platonLogObservable(filter).map(log -> {
            EventValuesWithLog eventValues = extractEventParametersWithLog(TRANSFER_EVENT, log);
            ErcTxEvent resp = new ErcTxEvent();
            resp.setLog(log);
            resp.setFrom((String) eventValues.getIndexedValues().get(0).getValue());
            resp.setTo((String) eventValues.getIndexedValues().get(1).getValue());
            resp.setValue((BigInteger) eventValues.getNonIndexedValues().get(0).getValue());
            return resp;
        });
    }

    public Observable<ErcTxEvent> transferEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        PlatonFilter filter = new PlatonFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(TRANSFER_EVENT));
        return transferEventObservable(filter);
    }

    public RemoteCall<BigInteger> allowance(String _owner, String _spender) {
        final Function function = new Function(FUNC_ALLOWANCE, Arrays.asList(new Address(_owner), new Address(_spender)), Arrays.asList(new TypeReference<Uint256>() {
        }));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<TransactionReceipt> approve(String _spender, BigInteger _value) {
        final Function function = new Function(FUNC_APPROVE, Arrays.<Type>asList(new Address(_spender), new Uint256(_value)), Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    @Override
    public RemoteCall<BigInteger> balanceOf(String _owner) {
        final Function function = new Function(FUNC_BALANCEOF, Collections.singletonList(new Address(_owner)), Collections.singletonList(new TypeReference<Uint256>() {
        }));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<TransactionReceipt> closeTransfer() {
        final Function function = new Function(FUNC_CLOSETRANSFER, Collections.emptyList(), Collections.emptyList());
        return executeRemoteCallTransaction(function);
    }

    @Override
    public RemoteCall<BigInteger> decimals() {
        final Function function = new Function(FUNC_DECIMALS, Collections.emptyList(), Collections.singletonList(new TypeReference<Uint8>() {
        }));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<String> keys(String param0) {
        final Function function = new Function(FUNC_KEYS, Collections.singletonList(new Address(param0)), Collections.singletonList(new TypeReference<Utf8String>() {
        }));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    @Override
    public RemoteCall<String> name() {
        final Function function = new Function(FUNC_NAME, Collections.emptyList(), Collections.singletonList(new TypeReference<Utf8String>() {
        }));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<TransactionReceipt> openTransfer() {
        final Function function = new Function(FUNC_OPENTRANSFER, Collections.emptyList(), Collections.emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<String> owner() {
        final Function function = new Function(FUNC_OWNER, Collections.emptyList(), Collections.singletonList(new TypeReference<Address>() {
        }));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<TransactionReceipt> register(String key) {
        final Function function = new Function(FUNC_REGISTER, Collections.singletonList(new Utf8String(key)), Collections.emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<String> rewardValueAddr() {
        final Function function = new Function(FUNC_REWARDVALUEADDR, Collections.emptyList(), Collections.singletonList(new TypeReference<Address>() {
        }));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<TransactionReceipt> start() {
        final Function function = new Function(FUNC_START, Collections.emptyList(), Collections.emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> stop() {
        final Function function = new Function(FUNC_STOP, Collections.emptyList(), Collections.emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<Boolean> stopped() {
        final Function function = new Function(FUNC_STOPPED, Collections.emptyList(), Collections.singletonList(new TypeReference<Bool>() {
        }));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    @Override
    public RemoteCall<String> symbol() {
        final Function function = new Function(FUNC_SYMBOL, Collections.emptyList(), Collections.singletonList(new TypeReference<Utf8String>() {
        }));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    @Override
    public RemoteCall<BigInteger> totalSupply() {
        final Function function = new Function(FUNC_TOTALSUPPLY, Arrays.<Type>asList(), Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {
        }));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<TransactionReceipt> transfer(String _to, BigInteger _value) {
        final Function function = new Function(FUNC_TRANSFER, Arrays.asList(new Address(_to), new Uint256(_value)), Collections.emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<Boolean> transferClosed() {
        final Function function = new Function(FUNC_TRANSFERCLOSED, Collections.emptyList(), Collections.singletonList(new TypeReference<Bool>() {
        }));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteCall<TransactionReceipt> transferFrom(String _from, String _to, BigInteger _value) {
        final Function function = new Function(FUNC_TRANSFERFROM, Arrays.asList(new Address(_from), new Address(_to), new Uint256(_value)), Collections.emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> withdrawal(BigInteger value) {
        final Function function = new Function(FUNC_WITHDRAWAL, Collections.singletonList(new Uint256(value)), Collections.emptyList());
        return executeRemoteCallTransaction(function);
    }

    public static Erc20Contract load(String contractAddress, Web3j web3j, Credentials credentials, GasProvider contractGasProvider) {
        return new Erc20Contract(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static Erc20Contract load(String contractAddress, Web3j web3j, Credentials credentials, GasProvider contractGasProvider, BigInteger blockNumber) {
        Erc20Contract Erc20Contract = new Erc20Contract(contractAddress, web3j, credentials, contractGasProvider);
        Erc20Contract.setDefaultBlockParameter(DefaultBlockParameter.valueOf(blockNumber));
        return Erc20Contract;
    }

    public static Erc20Contract load(String contractAddress, Web3j web3j, TransactionManager transactionManager, GasProvider contractGasProvider) {
        return new Erc20Contract(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static class ApprovalEventResponse {

        public Log log;

        public String _owner;

        public String _spender;

        public BigInteger _value;

    }

    public static class LogRegisterEventResponse {

        public Log log;

        public String user;

        public String key;

    }

    public static class LogWithdrawalEventResponse {

        public Log log;

        public String user;

        public BigInteger _value;

    }

}

package com.platon.browser.v0151.contract;

import com.alaya.abi.solidity.EventEncoder;
import com.alaya.abi.solidity.TypeReference;
import com.alaya.abi.solidity.datatypes.*;
import com.alaya.abi.solidity.datatypes.generated.Uint256;
import com.alaya.crypto.Credentials;
import com.alaya.protocol.Web3j;
import com.alaya.protocol.core.DefaultBlockParameter;
import com.alaya.protocol.core.RemoteCall;
import com.alaya.protocol.core.methods.request.PlatonFilter;
import com.alaya.protocol.core.methods.response.Log;
import com.alaya.protocol.core.methods.response.TransactionReceipt;
import com.alaya.tx.Contract;
import com.alaya.tx.gas.GasProvider;
import lombok.Data;
import org.apache.http.MethodNotSupportedException;
import rx.Observable;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @program: browser-server
 * @description:
 * @author: Rongjin Zhang
 * @create: 2020-09-23 10:11
 */
public class Erc20Contract extends Contract implements ErcContract {

    public static final String FUNC_ADDBLACKLIST = "addBlackList";

    public static final String FUNC_APPROVE = "approve";

    public static final String FUNC_DEPRECATE = "deprecate";

    public static final String FUNC_DESTROYBLACKFUNDS = "destroyBlackFunds";

    public static final String FUNC_ISSUE = "issue";

    public static final String FUNC_PAUSE = "pause";

    public static final String FUNC_REDEEM = "redeem";

    public static final String FUNC_REMOVEBLACKLIST = "removeBlackList";

    public static final String FUNC_SETPARAMS = "setParams";

    public static final String FUNC_TRANSFER = "transfer";

    public static final String FUNC_TRANSFERFROM = "transferFrom";

    public static final String FUNC_TRANSFEROWNERSHIP = "transferOwnership";

    public static final String FUNC_UNPAUSE = "unpause";

    public static final String FUNC__TOTALSUPPLY = "_totalSupply";

    public static final String FUNC_ALLOWANCE = "allowance";

    public static final String FUNC_ALLOWED = "allowed";

    public static final String FUNC_BALANCEOF = "balanceOf";

    public static final String FUNC_BALANCES = "balances";

    public static final String FUNC_BASISPOINTSRATE = "basisPointsRate";

    public static final String FUNC_DECIMALS = "decimals";

    public static final String FUNC_DEPRECATED = "deprecated";

    public static final String FUNC_GETBLACKLISTSTATUS = "getBlackListStatus";

    public static final String FUNC_GETOWNER = "getOwner";

    public static final String FUNC_ISBLACKLISTED = "isBlackListed";

    public static final String FUNC_MAX_UINT = "MAX_UINT";

    public static final String FUNC_MAXIMUMFEE = "maximumFee";

    public static final String FUNC_NAME = "name";

    public static final String FUNC_OWNER = "owner";

    public static final String FUNC_PAUSED = "paused";

    public static final String FUNC_SYMBOL = "symbol";

    public static final String FUNC_TOTALSUPPLY = "totalSupply";

    public static final String FUNC_UPGRADEDADDRESS = "upgradedAddress";

    public static final Event ISSUE_EVENT =
        new Event("Issue", Collections.singletonList(new TypeReference<Uint256>() {
        }));;

    public static final Event REDEEM_EVENT =
        new Event("Redeem", Collections.singletonList(new TypeReference<Uint256>() {
        }));;

    public static final Event DEPRECATE_EVENT =
        new Event("Deprecate", Collections.singletonList(new TypeReference<Address>() {
        }));;

    public static final Event PARAMS_EVENT = new Event("Params",
        Arrays.asList(new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));;

    public static final Event DESTROYEDBLACKFUNDS_EVENT = new Event("DestroyedBlackFunds",
        Arrays.asList(new TypeReference<Address>() {}, new TypeReference<Uint256>() {}));;

    public static final Event ADDEDBLACKLIST_EVENT =
        new Event("AddedBlackList", Collections.singletonList(new TypeReference<Address>() {
        }));;

    public static final Event REMOVEDBLACKLIST_EVENT =
        new Event("RemovedBlackList", Collections.singletonList(new TypeReference<Address>() {
        }));;

    public static final Event APPROVAL_EVENT =
        new Event("Approval", Arrays.asList(new TypeReference<Address>(true) {},
            new TypeReference<Address>(true) {}, new TypeReference<Uint256>() {}));;

    public static final Event TRANSFER_EVENT =
        new Event("Transfer", Arrays.asList(new TypeReference<Address>(true) {},
            new TypeReference<Address>(true) {}, new TypeReference<Uint256>() {}));;

    public static final Event PAUSE_EVENT = new Event("Pause", Collections.emptyList());;

    public static final Event UNPAUSE_EVENT = new Event("Unpause", Collections.emptyList());

    public Erc20Contract(final String contractAddress, final Web3j web3j, final Credentials credentials,
                         final GasProvider gasProvider, final long chainId) {
        super("", contractAddress, web3j, credentials, gasProvider, chainId);
    }

    public Erc20Contract(final String contractAddress, final Web3j web3j, final Credentials credentials,
                         final long chainId) {
        super("", contractAddress, web3j, credentials, null, chainId);
    }

    public List<IssueEventResponse> getIssueEvents(final TransactionReceipt transactionReceipt) {
        final List<EventValuesWithLog> valueList = this.extractEventParametersWithLog(ISSUE_EVENT, transactionReceipt);
        final ArrayList<IssueEventResponse> responses = new ArrayList<>(valueList.size());
        for (final EventValuesWithLog eventValues : valueList) {
            final IssueEventResponse typedResponse = new IssueEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.amount = (BigInteger)eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<IssueEventResponse> issueEventObservable(final PlatonFilter filter) {
        return this.web3j.platonLogObservable(filter).map(log -> {
            final EventValuesWithLog eventValues = Erc20Contract.this.extractEventParametersWithLog(ISSUE_EVENT, log);
            final IssueEventResponse typedResponse = new IssueEventResponse();
            typedResponse.log = log;
            typedResponse.amount = (BigInteger)eventValues.getNonIndexedValues().get(0).getValue();
            return typedResponse;
        });
    }

    public Observable<IssueEventResponse> issueEventObservable(final DefaultBlockParameter startBlock,
        final DefaultBlockParameter endBlock) {
        final PlatonFilter filter = new PlatonFilter(startBlock, endBlock, this.getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(ISSUE_EVENT));
        return this.issueEventObservable(filter);
    }

    public List<RedeemEventResponse> getRedeemEvents(final TransactionReceipt transactionReceipt) {
        final List<EventValuesWithLog> valueList = this.extractEventParametersWithLog(REDEEM_EVENT, transactionReceipt);
        final ArrayList<RedeemEventResponse> responses = new ArrayList<>(valueList.size());
        for (final EventValuesWithLog eventValues : valueList) {
            final RedeemEventResponse typedResponse = new RedeemEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.amount = (BigInteger)eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<RedeemEventResponse> redeemEventObservable(final PlatonFilter filter) {
        return this.web3j.platonLogObservable(filter).map(log -> {
            final EventValuesWithLog eventValues = Erc20Contract.this.extractEventParametersWithLog(REDEEM_EVENT, log);
            final RedeemEventResponse typedResponse = new RedeemEventResponse();
            typedResponse.log = log;
            typedResponse.amount = (BigInteger)eventValues.getNonIndexedValues().get(0).getValue();
            return typedResponse;
        });
    }

    public Observable<RedeemEventResponse> redeemEventObservable(final DefaultBlockParameter startBlock,
        final DefaultBlockParameter endBlock) {
        final PlatonFilter filter = new PlatonFilter(startBlock, endBlock, this.getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(REDEEM_EVENT));
        return this.redeemEventObservable(filter);
    }

    public List<DeprecateEventResponse> getDeprecateEvents(final TransactionReceipt transactionReceipt) {
        final List<EventValuesWithLog> valueList = this.extractEventParametersWithLog(DEPRECATE_EVENT, transactionReceipt);
        final ArrayList<DeprecateEventResponse> responses = new ArrayList<>(valueList.size());
        for (final EventValuesWithLog eventValues : valueList) {
            final DeprecateEventResponse typedResponse = new DeprecateEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.newAddress = (String)eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<DeprecateEventResponse> deprecateEventObservable(final PlatonFilter filter) {
        return this.web3j.platonLogObservable(filter).map(log -> {
            final EventValuesWithLog eventValues = Erc20Contract.this.extractEventParametersWithLog(DEPRECATE_EVENT, log);
            final DeprecateEventResponse typedResponse = new DeprecateEventResponse();
            typedResponse.log = log;
            typedResponse.newAddress = (String)eventValues.getNonIndexedValues().get(0).getValue();
            return typedResponse;
        });
    }

    public Observable<DeprecateEventResponse> deprecateEventObservable(final DefaultBlockParameter startBlock,
        final DefaultBlockParameter endBlock) {
        final PlatonFilter filter = new PlatonFilter(startBlock, endBlock, this.getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(DEPRECATE_EVENT));
        return this.deprecateEventObservable(filter);
    }

    public List<ParamsEventResponse> getParamsEvents(final TransactionReceipt transactionReceipt) {
        final List<EventValuesWithLog> valueList = this.extractEventParametersWithLog(PARAMS_EVENT, transactionReceipt);
        final ArrayList<ParamsEventResponse> responses = new ArrayList<>(valueList.size());
        for (final EventValuesWithLog eventValues : valueList) {
            final ParamsEventResponse typedResponse = new ParamsEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.feeBasisPoints = (BigInteger)eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.maxFee = (BigInteger)eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<ParamsEventResponse> paramsEventObservable(final PlatonFilter filter) {
        return this.web3j.platonLogObservable(filter).map(log -> {
            final EventValuesWithLog eventValues = Erc20Contract.this.extractEventParametersWithLog(PARAMS_EVENT, log);
            final ParamsEventResponse typedResponse = new ParamsEventResponse();
            typedResponse.log = log;
            typedResponse.feeBasisPoints = (BigInteger)eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.maxFee = (BigInteger)eventValues.getNonIndexedValues().get(1).getValue();
            return typedResponse;
        });
    }

    public Observable<ParamsEventResponse> paramsEventObservable(final DefaultBlockParameter startBlock,
        final DefaultBlockParameter endBlock) {
        final PlatonFilter filter = new PlatonFilter(startBlock, endBlock, this.getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(PARAMS_EVENT));
        return this.paramsEventObservable(filter);
    }

    public List<DestroyedBlackFundsEventResponse>
        getDestroyedBlackFundsEvents(final TransactionReceipt transactionReceipt) {
        final List<EventValuesWithLog> valueList =
                this.extractEventParametersWithLog(DESTROYEDBLACKFUNDS_EVENT, transactionReceipt);
        final ArrayList<DestroyedBlackFundsEventResponse> responses =
                new ArrayList<>(valueList.size());
        for (final EventValuesWithLog eventValues : valueList) {
            final DestroyedBlackFundsEventResponse typedResponse = new DestroyedBlackFundsEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse._blackListedUser = (String)eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse._balance = (BigInteger)eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<DestroyedBlackFundsEventResponse> destroyedBlackFundsEventObservable(final PlatonFilter filter) {
        return this.web3j.platonLogObservable(filter).map(log -> {
            final EventValuesWithLog eventValues = Erc20Contract.this.extractEventParametersWithLog(DESTROYEDBLACKFUNDS_EVENT, log);
            final DestroyedBlackFundsEventResponse typedResponse = new DestroyedBlackFundsEventResponse();
            typedResponse.log = log;
            typedResponse._blackListedUser = (String)eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse._balance = (BigInteger)eventValues.getNonIndexedValues().get(1).getValue();
            return typedResponse;
        });
    }

    public Observable<DestroyedBlackFundsEventResponse> destroyedBlackFundsEventObservable(
        final DefaultBlockParameter startBlock, final DefaultBlockParameter endBlock) {
        final PlatonFilter filter = new PlatonFilter(startBlock, endBlock, this.getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(DESTROYEDBLACKFUNDS_EVENT));
        return this.destroyedBlackFundsEventObservable(filter);
    }

    public List<AddedBlackListEventResponse> getAddedBlackListEvents(final TransactionReceipt transactionReceipt) {
        final List<EventValuesWithLog> valueList =
                this.extractEventParametersWithLog(ADDEDBLACKLIST_EVENT, transactionReceipt);
        final ArrayList<AddedBlackListEventResponse> responses =
                new ArrayList<>(valueList.size());
        for (final EventValuesWithLog eventValues : valueList) {
            final AddedBlackListEventResponse typedResponse = new AddedBlackListEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse._user = (String)eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<AddedBlackListEventResponse> addedBlackListEventObservable(final PlatonFilter filter) {
        return this.web3j.platonLogObservable(filter).map(log -> {
            final EventValuesWithLog eventValues = Erc20Contract.this.extractEventParametersWithLog(ADDEDBLACKLIST_EVENT, log);
            final AddedBlackListEventResponse typedResponse = new AddedBlackListEventResponse();
            typedResponse.log = log;
            typedResponse._user = (String)eventValues.getNonIndexedValues().get(0).getValue();
            return typedResponse;
        });
    }

    public Observable<AddedBlackListEventResponse> addedBlackListEventObservable(final DefaultBlockParameter startBlock,
        final DefaultBlockParameter endBlock) {
        final PlatonFilter filter = new PlatonFilter(startBlock, endBlock, this.getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(ADDEDBLACKLIST_EVENT));
        return this.addedBlackListEventObservable(filter);
    }

    public List<RemovedBlackListEventResponse> getRemovedBlackListEvents(final TransactionReceipt transactionReceipt) {
        final List<EventValuesWithLog> valueList =
                this.extractEventParametersWithLog(REMOVEDBLACKLIST_EVENT, transactionReceipt);
        final ArrayList<RemovedBlackListEventResponse> responses =
                new ArrayList<>(valueList.size());
        for (final EventValuesWithLog eventValues : valueList) {
            final RemovedBlackListEventResponse typedResponse = new RemovedBlackListEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse._user = (String)eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<RemovedBlackListEventResponse> removedBlackListEventObservable(final PlatonFilter filter) {
        return this.web3j.platonLogObservable(filter).map(log -> {
            final EventValuesWithLog eventValues = Erc20Contract.this.extractEventParametersWithLog(REMOVEDBLACKLIST_EVENT, log);
            final RemovedBlackListEventResponse typedResponse = new RemovedBlackListEventResponse();
            typedResponse.log = log;
            typedResponse._user = (String)eventValues.getNonIndexedValues().get(0).getValue();
            return typedResponse;
        });
    }

    public Observable<RemovedBlackListEventResponse>
        removedBlackListEventObservable(final DefaultBlockParameter startBlock, final DefaultBlockParameter endBlock) {
        final PlatonFilter filter = new PlatonFilter(startBlock, endBlock, this.getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(REMOVEDBLACKLIST_EVENT));
        return this.removedBlackListEventObservable(filter);
    }

    public List<ApprovalEventResponse> getApprovalEvents(final TransactionReceipt transactionReceipt) {
        final List<EventValuesWithLog> valueList = this.extractEventParametersWithLog(APPROVAL_EVENT, transactionReceipt);
        final ArrayList<ApprovalEventResponse> responses = new ArrayList<>(valueList.size());
        for (final EventValuesWithLog eventValues : valueList) {
            final ApprovalEventResponse typedResponse = new ApprovalEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.owner = (String)eventValues.getIndexedValues().get(0).getValue();
            typedResponse.spender = (String)eventValues.getIndexedValues().get(1).getValue();
            typedResponse.value = (BigInteger)eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<ApprovalEventResponse> approvalEventObservable(final PlatonFilter filter) {
        return this.web3j.platonLogObservable(filter).map(log -> {
            final EventValuesWithLog eventValues = Erc20Contract.this.extractEventParametersWithLog(APPROVAL_EVENT, log);
            final ApprovalEventResponse typedResponse = new ApprovalEventResponse();
            typedResponse.log = log;
            typedResponse.owner = (String)eventValues.getIndexedValues().get(0).getValue();
            typedResponse.spender = (String)eventValues.getIndexedValues().get(1).getValue();
            typedResponse.value = (BigInteger)eventValues.getNonIndexedValues().get(0).getValue();
            return typedResponse;
        });
    }

    public Observable<ApprovalEventResponse> approvalEventObservable(final DefaultBlockParameter startBlock,
        final DefaultBlockParameter endBlock) {
        final PlatonFilter filter = new PlatonFilter(startBlock, endBlock, this.getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(APPROVAL_EVENT));
        return this.approvalEventObservable(filter);
    }

    @Override
    public List<ErcTxEvent> getTxEvents(final TransactionReceipt transactionReceipt) {
        final List<EventValuesWithLog> valueList = this.extractEventParametersWithLog(TRANSFER_EVENT, transactionReceipt);
        final ArrayList<ErcTxEvent> responses = new ArrayList<>(valueList.size());
        for (final EventValuesWithLog eventValues : valueList) {
            final ErcTxEvent response = new ErcTxEvent();
            response.setLog(eventValues.getLog());
            response.setFrom((String)eventValues.getIndexedValues().get(0).getValue());
            response.setTo((String)eventValues.getIndexedValues().get(1).getValue());
            response.setValue((BigInteger)eventValues.getNonIndexedValues().get(0).getValue());
            responses.add(response);
        }
        return responses;
    }

    @Override
    public RemoteCall<String> getTokenURI(BigInteger tokenId) throws MethodNotSupportedException {
        throw new MethodNotSupportedException("ERC20 NOT SUPPORT THIS METHOD!");
    }

    public Observable<ErcTxEvent> transferEventObservable(final PlatonFilter filter) {
        return this.web3j.platonLogObservable(filter).map(log -> {
            final EventValuesWithLog eventValues = Erc20Contract.this.extractEventParametersWithLog(TRANSFER_EVENT, log);
            final ErcTxEvent response = new ErcTxEvent();
            response.setLog(log);
            response.setFrom((String)eventValues.getIndexedValues().get(0).getValue());
            response.setTo((String)eventValues.getIndexedValues().get(1).getValue());
            response.setValue((BigInteger)eventValues.getNonIndexedValues().get(0).getValue());
            return response;
        });
    }

    public Observable<ErcTxEvent> transferEventObservable(final DefaultBlockParameter startBlock,
        final DefaultBlockParameter endBlock) {
        final PlatonFilter filter = new PlatonFilter(startBlock, endBlock, this.getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(TRANSFER_EVENT));
        return this.transferEventObservable(filter);
    }

    public List<PauseEventResponse> getPauseEvents(final TransactionReceipt transactionReceipt) {
        final List<EventValuesWithLog> valueList = this.extractEventParametersWithLog(PAUSE_EVENT, transactionReceipt);
        final ArrayList<PauseEventResponse> responses = new ArrayList<>(valueList.size());
        for (final EventValuesWithLog eventValues : valueList) {
            final PauseEventResponse typedResponse = new PauseEventResponse();
            typedResponse.log = eventValues.getLog();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<PauseEventResponse> pauseEventObservable(final PlatonFilter filter) {
        return this.web3j.platonLogObservable(filter).map(log -> {
            final EventValuesWithLog eventValues = Erc20Contract.this.extractEventParametersWithLog(PAUSE_EVENT, log);
            final PauseEventResponse typedResponse = new PauseEventResponse();
            typedResponse.log = log;
            return typedResponse;
        });
    }

    public Observable<PauseEventResponse> pauseEventObservable(final DefaultBlockParameter startBlock,
        final DefaultBlockParameter endBlock) {
        final PlatonFilter filter = new PlatonFilter(startBlock, endBlock, this.getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(PAUSE_EVENT));
        return this.pauseEventObservable(filter);
    }

    public List<UnpauseEventResponse> getUnpauseEvents(final TransactionReceipt transactionReceipt) {
        final List<EventValuesWithLog> valueList = this.extractEventParametersWithLog(UNPAUSE_EVENT, transactionReceipt);
        final ArrayList<UnpauseEventResponse> responses = new ArrayList<>(valueList.size());
        for (final EventValuesWithLog eventValues : valueList) {
            final UnpauseEventResponse typedResponse = new UnpauseEventResponse();
            typedResponse.log = eventValues.getLog();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<UnpauseEventResponse> unpauseEventObservable(final PlatonFilter filter) {
        return this.web3j.platonLogObservable(filter).map(log -> {
            final EventValuesWithLog eventValues = Erc20Contract.this.extractEventParametersWithLog(UNPAUSE_EVENT, log);
            final UnpauseEventResponse typedResponse = new UnpauseEventResponse();
            typedResponse.log = log;
            return typedResponse;
        });
    }

    public Observable<UnpauseEventResponse> unpauseEventObservable(final DefaultBlockParameter startBlock,
        final DefaultBlockParameter endBlock) {
        final PlatonFilter filter = new PlatonFilter(startBlock, endBlock, this.getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(UNPAUSE_EVENT));
        return this.unpauseEventObservable(filter);
    }

    public RemoteCall<BigInteger> _totalSupply() {
        final Function function = new Function(FUNC__TOTALSUPPLY, Collections.emptyList(),
                Collections.singletonList(new TypeReference<Uint256>() {
                }));
        return this.executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<BigInteger> allowance(final String _owner, final String _spender) {
        final Function function =
            new Function(FUNC_ALLOWANCE, Arrays.asList(new Address(_owner), new Address(_spender)),
                    Collections.singletonList(new TypeReference<Uint256>() {
                    }));
        return this.executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<BigInteger> allowed(final String param0, final String param1) {
        final Function function =
            new Function(FUNC_ALLOWED, Arrays.asList(new Address(param0), new Address(param1)),
                    Collections.singletonList(new TypeReference<Uint256>() {
                    }));
        return this.executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<BigInteger> balanceOf(final String who) {
        final Function function = new Function(FUNC_BALANCEOF, Collections.singletonList(new Address(who)),
                Collections.singletonList(new TypeReference<Uint256>() {
                }));
        return this.executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<BigInteger> balances(final String param0) {
        final Function function = new Function(FUNC_BALANCES, Collections.singletonList(new Address(param0)),
                Collections.singletonList(new TypeReference<Uint256>() {
                }));
        return this.executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<BigInteger> basisPointsRate() {
        final Function function = new Function(FUNC_BASISPOINTSRATE, Collections.emptyList(),
                Collections.singletonList(new TypeReference<Uint256>() {
                }));
        return this.executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }
    @Override
    public RemoteCall<BigInteger> decimals() {
        final Function function = new Function(FUNC_DECIMALS, Collections.emptyList(),
                Collections.singletonList(new TypeReference<Uint256>() {
                }));
        return this.executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<Boolean> deprecated() {
        final Function function = new Function(FUNC_DEPRECATED, Collections.emptyList(),
                Collections.singletonList(new TypeReference<Bool>() {
                }));
        return this.executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteCall<Boolean> getBlackListStatus(final String _maker) {
        final Function function = new Function(FUNC_GETBLACKLISTSTATUS, Collections.singletonList(new Address(_maker)),
                Collections.singletonList(new TypeReference<Bool>() {
                }));
        return this.executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteCall<String> getOwner() {
        final Function function = new Function(FUNC_GETOWNER, Collections.emptyList(),
                Collections.singletonList(new TypeReference<Address>() {
                }));
        return this.executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<Boolean> isBlackListed(final String param0) {
        final Function function = new Function(FUNC_ISBLACKLISTED, Collections.singletonList(new Address(param0)),
                Collections.singletonList(new TypeReference<Bool>() {
                }));
        return this.executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteCall<BigInteger> MAX_UINT() {
        final Function function = new Function(FUNC_MAX_UINT, Collections.emptyList(),
                Collections.singletonList(new TypeReference<Uint256>() {
                }));
        return this.executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<BigInteger> maximumFee() {
        final Function function = new Function(FUNC_MAXIMUMFEE, Collections.emptyList(),
                Collections.singletonList(new TypeReference<Uint256>() {
                }));
        return this.executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }
    @Override
    public RemoteCall<String> name() {
        final Function function = new Function(FUNC_NAME, Collections.emptyList(),
                Collections.singletonList(new TypeReference<Utf8String>() {
                }));
        return this.executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<String> owner() {
        final Function function = new Function(FUNC_OWNER, Collections.emptyList(),
                Collections.singletonList(new TypeReference<Address>() {
                }));
        return this.executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<Boolean> paused() {
        final Function function = new Function(FUNC_PAUSED, Collections.emptyList(),
                Collections.singletonList(new TypeReference<Bool>() {
                }));
        return this.executeRemoteCallSingleValueReturn(function, Boolean.class);
    }
    @Override
    public RemoteCall<String> symbol() {
        final Function function = new Function(FUNC_SYMBOL, Collections.emptyList(),
                Collections.singletonList(new TypeReference<Utf8String>() {
                }));
        return this.executeRemoteCallSingleValueReturn(function, String.class);
    }
    @Override
    public RemoteCall<BigInteger> totalSupply() {
        final Function function = new Function(FUNC_TOTALSUPPLY, Collections.emptyList(),
                Collections.singletonList(new TypeReference<Uint256>() {
                }));
        return this.executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<String> upgradedAddress() {
        final Function function = new Function(FUNC_UPGRADEDADDRESS, Collections.emptyList(),
                Collections.singletonList(new TypeReference<Address>() {
                }));
        return this.executeRemoteCallSingleValueReturn(function, String.class);
    }

    @Data
    public static class IssueEventResponse {
        private Log log;
        private BigInteger amount;
    }
    @Data
    public static class RedeemEventResponse {
        private Log log;
        private BigInteger amount;
    }
    @Data
    public static class DeprecateEventResponse {
        private Log log;
        private String newAddress;
    }
    @Data
    public static class ParamsEventResponse {
        private Log log;
        private BigInteger feeBasisPoints;
        private BigInteger maxFee;
    }
    @Data
    public static class DestroyedBlackFundsEventResponse {
        private Log log;
        private String _blackListedUser;
        private BigInteger _balance;
    }
    @Data
    public static class AddedBlackListEventResponse {
        private Log log;
        private String _user;
    }
    @Data
    public static class RemovedBlackListEventResponse {
        private Log log;
        private String _user;
    }
    @Data
    public static class ApprovalEventResponse {
        private Log log;
        private String owner;
        private String spender;
        private BigInteger value;
    }

    @Data
    public static class PauseEventResponse {
        private Log log;
    }
    @Data
    public static class UnpauseEventResponse {
        private Log log;
    }
}

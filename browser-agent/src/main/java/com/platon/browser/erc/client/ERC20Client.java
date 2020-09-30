package com.platon.browser.erc.client;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
import org.web3j.tx.gas.GasProvider;

import rx.Observable;
import rx.functions.Func1;

/**
 * @program: browser-server
 * @description:
 * @author: Rongjin Zhang
 * @create: 2020-09-23 10:11
 */
public class ERC20Client extends Contract {

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
        new Event("Issue", Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));;

    public static final Event REDEEM_EVENT =
        new Event("Redeem", Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));;

    public static final Event DEPRECATE_EVENT =
        new Event("Deprecate", Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));;

    public static final Event PARAMS_EVENT = new Event("Params",
        Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));;

    public static final Event DESTROYEDBLACKFUNDS_EVENT = new Event("DestroyedBlackFunds",
        Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Uint256>() {}));;

    public static final Event ADDEDBLACKLIST_EVENT =
        new Event("AddedBlackList", Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));;

    public static final Event REMOVEDBLACKLIST_EVENT =
        new Event("RemovedBlackList", Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));;

    public static final Event APPROVAL_EVENT =
        new Event("Approval", Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {},
            new TypeReference<Address>(true) {}, new TypeReference<Uint256>() {}));;

    public static final Event TRANSFER_EVENT =
        new Event("Transfer", Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {},
            new TypeReference<Address>(true) {}, new TypeReference<Uint256>() {}));;

    public static final Event PAUSE_EVENT = new Event("Pause", Arrays.<TypeReference<?>>asList());;

    public static final Event UNPAUSE_EVENT = new Event("Unpause", Arrays.<TypeReference<?>>asList());

    public ERC20Client(final String contractAddress, final Web3j web3j, final Credentials credentials,
        final GasProvider gasProvider, final long chainId) {
        super("", contractAddress, web3j, credentials, gasProvider, chainId);
    }

    public ERC20Client(final String contractAddress, final Web3j web3j, final Credentials credentials,
        final long chainId) {
        super("", contractAddress, web3j, credentials, null, chainId);
    }

    public List<IssueEventResponse> getIssueEvents(final TransactionReceipt transactionReceipt) {
        final List<EventValuesWithLog> valueList = this.extractEventParametersWithLog(ISSUE_EVENT, transactionReceipt);
        final ArrayList<IssueEventResponse> responses = new ArrayList<IssueEventResponse>(valueList.size());
        for (final EventValuesWithLog eventValues : valueList) {
            final IssueEventResponse typedResponse = new IssueEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.amount = (BigInteger)eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<IssueEventResponse> issueEventObservable(final PlatonFilter filter) {
        return this.web3j.platonLogObservable(filter).map(new Func1<Log, IssueEventResponse>() {
            @Override
            public IssueEventResponse call(final Log log) {
                final EventValuesWithLog eventValues = ERC20Client.this.extractEventParametersWithLog(ISSUE_EVENT, log);
                final IssueEventResponse typedResponse = new IssueEventResponse();
                typedResponse.log = log;
                typedResponse.amount = (BigInteger)eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
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
        final ArrayList<RedeemEventResponse> responses = new ArrayList<RedeemEventResponse>(valueList.size());
        for (final EventValuesWithLog eventValues : valueList) {
            final RedeemEventResponse typedResponse = new RedeemEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.amount = (BigInteger)eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<RedeemEventResponse> redeemEventObservable(final PlatonFilter filter) {
        return this.web3j.platonLogObservable(filter).map(new Func1<Log, RedeemEventResponse>() {
            @Override
            public RedeemEventResponse call(final Log log) {
                final EventValuesWithLog eventValues = ERC20Client.this.extractEventParametersWithLog(REDEEM_EVENT, log);
                final RedeemEventResponse typedResponse = new RedeemEventResponse();
                typedResponse.log = log;
                typedResponse.amount = (BigInteger)eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
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
        final ArrayList<DeprecateEventResponse> responses = new ArrayList<DeprecateEventResponse>(valueList.size());
        for (final EventValuesWithLog eventValues : valueList) {
            final DeprecateEventResponse typedResponse = new DeprecateEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.newAddress = (String)eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<DeprecateEventResponse> deprecateEventObservable(final PlatonFilter filter) {
        return this.web3j.platonLogObservable(filter).map(new Func1<Log, DeprecateEventResponse>() {
            @Override
            public DeprecateEventResponse call(final Log log) {
                final EventValuesWithLog eventValues = ERC20Client.this.extractEventParametersWithLog(DEPRECATE_EVENT, log);
                final DeprecateEventResponse typedResponse = new DeprecateEventResponse();
                typedResponse.log = log;
                typedResponse.newAddress = (String)eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
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
        final ArrayList<ParamsEventResponse> responses = new ArrayList<ParamsEventResponse>(valueList.size());
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
        return this.web3j.platonLogObservable(filter).map(new Func1<Log, ParamsEventResponse>() {
            @Override
            public ParamsEventResponse call(final Log log) {
                final EventValuesWithLog eventValues = ERC20Client.this.extractEventParametersWithLog(PARAMS_EVENT, log);
                final ParamsEventResponse typedResponse = new ParamsEventResponse();
                typedResponse.log = log;
                typedResponse.feeBasisPoints = (BigInteger)eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.maxFee = (BigInteger)eventValues.getNonIndexedValues().get(1).getValue();
                return typedResponse;
            }
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
            new ArrayList<DestroyedBlackFundsEventResponse>(valueList.size());
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
        return this.web3j.platonLogObservable(filter).map(new Func1<Log, DestroyedBlackFundsEventResponse>() {
            @Override
            public DestroyedBlackFundsEventResponse call(final Log log) {
                final EventValuesWithLog eventValues = ERC20Client.this.extractEventParametersWithLog(DESTROYEDBLACKFUNDS_EVENT, log);
                final DestroyedBlackFundsEventResponse typedResponse = new DestroyedBlackFundsEventResponse();
                typedResponse.log = log;
                typedResponse._blackListedUser = (String)eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse._balance = (BigInteger)eventValues.getNonIndexedValues().get(1).getValue();
                return typedResponse;
            }
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
            new ArrayList<AddedBlackListEventResponse>(valueList.size());
        for (final EventValuesWithLog eventValues : valueList) {
            final AddedBlackListEventResponse typedResponse = new AddedBlackListEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse._user = (String)eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<AddedBlackListEventResponse> addedBlackListEventObservable(final PlatonFilter filter) {
        return this.web3j.platonLogObservable(filter).map(new Func1<Log, AddedBlackListEventResponse>() {
            @Override
            public AddedBlackListEventResponse call(final Log log) {
                final EventValuesWithLog eventValues = ERC20Client.this.extractEventParametersWithLog(ADDEDBLACKLIST_EVENT, log);
                final AddedBlackListEventResponse typedResponse = new AddedBlackListEventResponse();
                typedResponse.log = log;
                typedResponse._user = (String)eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
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
            new ArrayList<RemovedBlackListEventResponse>(valueList.size());
        for (final EventValuesWithLog eventValues : valueList) {
            final RemovedBlackListEventResponse typedResponse = new RemovedBlackListEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse._user = (String)eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<RemovedBlackListEventResponse> removedBlackListEventObservable(final PlatonFilter filter) {
        return this.web3j.platonLogObservable(filter).map(new Func1<Log, RemovedBlackListEventResponse>() {
            @Override
            public RemovedBlackListEventResponse call(final Log log) {
                final EventValuesWithLog eventValues = ERC20Client.this.extractEventParametersWithLog(REMOVEDBLACKLIST_EVENT, log);
                final RemovedBlackListEventResponse typedResponse = new RemovedBlackListEventResponse();
                typedResponse.log = log;
                typedResponse._user = (String)eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
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
        final ArrayList<ApprovalEventResponse> responses = new ArrayList<ApprovalEventResponse>(valueList.size());
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
        return this.web3j.platonLogObservable(filter).map(new Func1<Log, ApprovalEventResponse>() {
            @Override
            public ApprovalEventResponse call(final Log log) {
                final EventValuesWithLog eventValues = ERC20Client.this.extractEventParametersWithLog(APPROVAL_EVENT, log);
                final ApprovalEventResponse typedResponse = new ApprovalEventResponse();
                typedResponse.log = log;
                typedResponse.owner = (String)eventValues.getIndexedValues().get(0).getValue();
                typedResponse.spender = (String)eventValues.getIndexedValues().get(1).getValue();
                typedResponse.value = (BigInteger)eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public Observable<ApprovalEventResponse> approvalEventObservable(final DefaultBlockParameter startBlock,
        final DefaultBlockParameter endBlock) {
        final PlatonFilter filter = new PlatonFilter(startBlock, endBlock, this.getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(APPROVAL_EVENT));
        return this.approvalEventObservable(filter);
    }

    public List<TransferEventResponse> getTransferEvents(final TransactionReceipt transactionReceipt) {
        final List<EventValuesWithLog> valueList = this.extractEventParametersWithLog(TRANSFER_EVENT, transactionReceipt);
        final ArrayList<TransferEventResponse> responses = new ArrayList<TransferEventResponse>(valueList.size());
        for (final EventValuesWithLog eventValues : valueList) {
            final TransferEventResponse typedResponse = new TransferEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.from = (String)eventValues.getIndexedValues().get(0).getValue();
            typedResponse.to = (String)eventValues.getIndexedValues().get(1).getValue();
            typedResponse.value = (BigInteger)eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<TransferEventResponse> transferEventObservable(final PlatonFilter filter) {
        return this.web3j.platonLogObservable(filter).map(new Func1<Log, TransferEventResponse>() {
            @Override
            public TransferEventResponse call(final Log log) {
                final EventValuesWithLog eventValues = ERC20Client.this.extractEventParametersWithLog(TRANSFER_EVENT, log);
                final TransferEventResponse typedResponse = new TransferEventResponse();
                typedResponse.log = log;
                typedResponse.from = (String)eventValues.getIndexedValues().get(0).getValue();
                typedResponse.to = (String)eventValues.getIndexedValues().get(1).getValue();
                typedResponse.value = (BigInteger)eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public Observable<TransferEventResponse> transferEventObservable(final DefaultBlockParameter startBlock,
        final DefaultBlockParameter endBlock) {
        final PlatonFilter filter = new PlatonFilter(startBlock, endBlock, this.getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(TRANSFER_EVENT));
        return this.transferEventObservable(filter);
    }

    public List<PauseEventResponse> getPauseEvents(final TransactionReceipt transactionReceipt) {
        final List<EventValuesWithLog> valueList = this.extractEventParametersWithLog(PAUSE_EVENT, transactionReceipt);
        final ArrayList<PauseEventResponse> responses = new ArrayList<PauseEventResponse>(valueList.size());
        for (final EventValuesWithLog eventValues : valueList) {
            final PauseEventResponse typedResponse = new PauseEventResponse();
            typedResponse.log = eventValues.getLog();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<PauseEventResponse> pauseEventObservable(final PlatonFilter filter) {
        return this.web3j.platonLogObservable(filter).map(new Func1<Log, PauseEventResponse>() {
            @Override
            public PauseEventResponse call(final Log log) {
                final EventValuesWithLog eventValues = ERC20Client.this.extractEventParametersWithLog(PAUSE_EVENT, log);
                final PauseEventResponse typedResponse = new PauseEventResponse();
                typedResponse.log = log;
                return typedResponse;
            }
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
        final ArrayList<UnpauseEventResponse> responses = new ArrayList<UnpauseEventResponse>(valueList.size());
        for (final EventValuesWithLog eventValues : valueList) {
            final UnpauseEventResponse typedResponse = new UnpauseEventResponse();
            typedResponse.log = eventValues.getLog();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<UnpauseEventResponse> unpauseEventObservable(final PlatonFilter filter) {
        return this.web3j.platonLogObservable(filter).map(new Func1<Log, UnpauseEventResponse>() {
            @Override
            public UnpauseEventResponse call(final Log log) {
                final EventValuesWithLog eventValues = ERC20Client.this.extractEventParametersWithLog(UNPAUSE_EVENT, log);
                final UnpauseEventResponse typedResponse = new UnpauseEventResponse();
                typedResponse.log = log;
                return typedResponse;
            }
        });
    }

    public Observable<UnpauseEventResponse> unpauseEventObservable(final DefaultBlockParameter startBlock,
        final DefaultBlockParameter endBlock) {
        final PlatonFilter filter = new PlatonFilter(startBlock, endBlock, this.getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(UNPAUSE_EVENT));
        return this.unpauseEventObservable(filter);
    }

    public RemoteCall<BigInteger> _totalSupply() {
        final Function function = new Function(FUNC__TOTALSUPPLY, Arrays.<Type>asList(),
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return this.executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<BigInteger> allowance(final String _owner, final String _spender) {
        final Function function =
            new Function(FUNC_ALLOWANCE, Arrays.<Type>asList(new Address(_owner), new Address(_spender)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return this.executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<BigInteger> allowed(final String param0, final String param1) {
        final Function function =
            new Function(FUNC_ALLOWED, Arrays.<Type>asList(new Address(param0), new Address(param1)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return this.executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<BigInteger> balanceOf(final String who) {
        final Function function = new Function(FUNC_BALANCEOF, Arrays.<Type>asList(new Address(who)),
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return this.executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<BigInteger> balances(final String param0) {
        final Function function = new Function(FUNC_BALANCES, Arrays.<Type>asList(new Address(param0)),
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return this.executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<BigInteger> basisPointsRate() {
        final Function function = new Function(FUNC_BASISPOINTSRATE, Arrays.<Type>asList(),
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return this.executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<BigInteger> decimals() {
        final Function function = new Function(FUNC_DECIMALS, Arrays.<Type>asList(),
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return this.executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<Boolean> deprecated() {
        final Function function = new Function(FUNC_DEPRECATED, Arrays.<Type>asList(),
            Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return this.executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteCall<Boolean> getBlackListStatus(final String _maker) {
        final Function function = new Function(FUNC_GETBLACKLISTSTATUS, Arrays.<Type>asList(new Address(_maker)),
            Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return this.executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteCall<String> getOwner() {
        final Function function = new Function(FUNC_GETOWNER, Arrays.<Type>asList(),
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return this.executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<Boolean> isBlackListed(final String param0) {
        final Function function = new Function(FUNC_ISBLACKLISTED, Arrays.<Type>asList(new Address(param0)),
            Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return this.executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteCall<BigInteger> MAX_UINT() {
        final Function function = new Function(FUNC_MAX_UINT, Arrays.<Type>asList(),
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return this.executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<BigInteger> maximumFee() {
        final Function function = new Function(FUNC_MAXIMUMFEE, Arrays.<Type>asList(),
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return this.executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<String> name() {
        final Function function = new Function(FUNC_NAME, Arrays.<Type>asList(),
            Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return this.executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<String> owner() {
        final Function function = new Function(FUNC_OWNER, Arrays.<Type>asList(),
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return this.executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<Boolean> paused() {
        final Function function = new Function(FUNC_PAUSED, Arrays.<Type>asList(),
            Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return this.executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteCall<String> symbol() {
        final Function function = new Function(FUNC_SYMBOL, Arrays.<Type>asList(),
            Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return this.executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<BigInteger> totalSupply() {
        final Function function = new Function(FUNC_TOTALSUPPLY, Arrays.<Type>asList(),
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return this.executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<String> upgradedAddress() {
        final Function function = new Function(FUNC_UPGRADEDADDRESS, Arrays.<Type>asList(),
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return this.executeRemoteCallSingleValueReturn(function, String.class);
    }

    public static class IssueEventResponse {
        public Log log;

        public BigInteger amount;
    }

    public static class RedeemEventResponse {
        public Log log;

        public BigInteger amount;
    }

    public static class DeprecateEventResponse {
        public Log log;

        public String newAddress;
    }

    public static class ParamsEventResponse {
        public Log log;

        public BigInteger feeBasisPoints;

        public BigInteger maxFee;
    }

    public static class DestroyedBlackFundsEventResponse {
        public Log log;

        public String _blackListedUser;

        public BigInteger _balance;
    }

    public static class AddedBlackListEventResponse {
        public Log log;

        public String _user;
    }

    public static class RemovedBlackListEventResponse {
        public Log log;

        public String _user;
    }

    public static class ApprovalEventResponse {
        public Log log;

        public String owner;

        public String spender;

        public BigInteger value;
    }

    public static class TransferEventResponse {
        public Log log;

        public String from;

        public String to;

        public BigInteger value;
    }

    public static class PauseEventResponse {
        public Log log;
    }

    public static class UnpauseEventResponse {
        public Log log;
    }
}

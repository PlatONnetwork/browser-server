package com.platon.browser.erc.client;

import com.alaya.abi.solidity.datatypes.Event;
import com.alaya.crypto.Credentials;
import com.alaya.protocol.core.methods.response.TransactionReceipt;
import com.alaya.tx.ReadonlyTransactionManager;
import com.alaya.tx.exceptions.ContractCallException;
import com.alaya.tx.gas.DefaultGasProvider;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.platon.browser.client.PlatOnClient;
import com.platon.browser.common.complement.cache.AddressCache;
import com.platon.browser.common.service.erc.Erc20RetryService;
import com.platon.browser.common.utils.CalculateUtils;
import com.platon.browser.config.BlockChainConfig;
import com.platon.browser.dao.entity.Erc20Token;
import com.platon.browser.dao.entity.Erc20TokenAddressRel;
import com.platon.browser.dao.mapper.Erc20TokenMapper;
import com.platon.browser.dto.ERCData;
import com.platon.browser.dto.TransferEvent;
import com.platon.browser.elasticsearch.dto.Transaction;
import com.platon.browser.erc.ERCInterface;
import com.platon.browser.erc.wrapper.ExtendEvent;
import com.platon.browser.param.Erc20Param;
import com.platon.browser.utils.NetworkParms;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @program: browser-server
 * @description:
 * @author: Rongjin Zhang
 * @create: 2020-09-23 10:36
 */
@Slf4j
@Component
public class ERCClient implements ERCInterface {

    @Autowired
    private PlatOnClient platOnClient;

    @Autowired
    private Erc20TokenMapper erc20TokenMapper;

    @Autowired
    private Erc20RetryService erc20RetryService;

    @Autowired
    private BlockChainConfig blockChainConfig;

    private ERC20Client init(String contractAddress) {
        if (StringUtils.isBlank(contractAddress)) {
            throw new RuntimeException("contractAddress is not null");
        }
        ERC20Client erc20Client = new ERC20Client(contractAddress, this.platOnClient.getWeb3jWrapper().getWeb3j(),
            Credentials.create("4484092b68df58d639f11d59738983e2b8b81824f3c0c759edd6773f9adadfe7"),
            NetworkParms.getChainId());
        return erc20Client;
    }

    @Override
    public String getName(String contractAddress) {
        ERC20Client erc20Client = this.init(contractAddress);
        String name = "";
        try {
            name = erc20Client.name().send();
        } catch (ContractCallException e) {
            log.debug(" not erc contract,{}", contractAddress);
        } catch (Exception e) {
            log.debug(" erc get name error", e);
        }
        return name;
    }

    @Override
    public String getSymbol(String contractAddress) {
        ERC20Client erc20Client = this.init(contractAddress);
        String symbol = "";
        try {
            symbol = erc20Client.symbol().send();
        } catch (ContractCallException e) {
            log.debug(" not erc contract,{}", contractAddress);
        } catch (Exception e) {
            log.error(" erc get symbol error", e);
        }
        return symbol;
    }

    @Override
    public BigInteger getDecimals(String contractAddress) {
        ERC20Client erc20Client = this.init(contractAddress);
        BigInteger decimal = null;
        try {
            decimal = erc20Client.decimals().send();
        } catch (ContractCallException e) {
            log.debug(" not erc contract,{}", contractAddress);
        } catch (Exception e) {
            log.error(" erc get decimal error", e);
        }
        return decimal;
    }

    @Override
    public BigInteger getTotalSupply(String contractAddress) {
        ERC20Client erc20Client = this.init(contractAddress);
        BigInteger totalSupply = null;
        try {
            totalSupply = erc20Client.totalSupply().send();
        } catch (ContractCallException e) {
            log.debug(" not erc contract,{}", contractAddress);
        } catch (Exception e) {
            log.error(" erc get totalSupply error", e);
        }
        return totalSupply;
    }

    @Override
    public ERCData getErcData(String contractAddress) {
        String name = this.getName(contractAddress);
        if (StringUtils.isBlank(name)) {
            return null;
        }
        String symbol = this.getSymbol(contractAddress);
        BigInteger decimals = this.getDecimals(contractAddress);
        BigInteger totalSupply = this.getTotalSupply(contractAddress);
        if (StringUtils.isBlank(symbol) || decimals == null || totalSupply == null) {
            return null;
        }
        ERCData ercData = new ERCData();
        ercData.setName(name);
        ercData.setDecimal(decimals.intValue());
        ercData.setSymbol(symbol);
        ercData.setTotalSupply(totalSupply);
        return ercData;
    }

    @Override
    public List<TransferEvent> getTransferEvents(TransactionReceipt transactionReceipt) {
        ERC20Client erc20Client = this.init(transactionReceipt.getContractAddress());
        List<TransferEvent> transferEvents = new ArrayList<>();
        try {
            List<ERC20Client.TransferEventResponse> transferEventResponses =
                erc20Client.getTransferEvents(transactionReceipt);
            transferEventResponses.forEach(transferEventRespon -> {
                TransferEvent transferEvent = new TransferEvent();
                transferEvent.setFrom(transferEventRespon.from);
                transferEvent.setTo(transferEventRespon.to);
                transferEvent.setValue(transferEventRespon.value);
                transferEvent.setLog(transferEventRespon.log);
                transferEvents.add(transferEvent);
            });
        } catch (Exception e) {
            log.error(" erc get transferEvents error", e);
        }
        return transferEvents;
    }

    @Override
    public List<TransferEvent> getApprovalEvents(TransactionReceipt transactionReceipt) {
        ERC20Client erc20Client = this.init(transactionReceipt.getContractAddress());
        List<TransferEvent> transferEvents = null;
        try {
            List<ERC20Client.ApprovalEventResponse> approvalEventResponses =
                erc20Client.getApprovalEvents(transactionReceipt);
        } catch (Exception e) {
            log.error(" erc get transferEvents error", e);
        }
        return transferEvents;
    }

    @Override
    public void initContractData(List<Transaction> transactions, AddressCache addressCache) {
        Map<String, Erc20Token> erc20Tokens = new ConcurrentHashMap<>();
        transactions.stream().filter(transaction -> transaction.getEsTokenTransferRecords().size() > 0)
            .forEach(transaction -> {
                List<Erc20Param> erc20Params = new ArrayList<>();
                transaction.getEsTokenTransferRecords().stream().forEach(esTokenTransferRecord -> {
                    // 存量的erc20参数，提高访问速度
                    Erc20Token erc20Token = erc20Tokens.get(esTokenTransferRecord.getContract());

                    BigDecimal totalSupply = BigDecimal.ZERO;
                    // 先缓存中获取
                    if (erc20Token == null) {
                        erc20Token = addressCache.getErc20Token(esTokenTransferRecord.getContract());
                        // 防止参数erc数据为null
                        if (null == erc20Token || StringUtils.isBlank(erc20Token.getName())) {
                            erc20Token = null;
                        } else {
                            erc20Tokens.put(esTokenTransferRecord.getContract(), erc20Token);
                        }
                    }
                    // 如果数据不存在再从从数据库中获取数据
                    if (erc20Token == null) {
                        erc20Token = erc20RetryService.getErc20Token(esTokenTransferRecord.getContract());
                        if (null != erc20Token) {
                            erc20Tokens.put(esTokenTransferRecord.getContract(), erc20Token);
                        }
                    }
                    //补充交易中的合约信息
                    if (erc20Token != null) {
                        esTokenTransferRecord.setDecimal(erc20Token.getDecimal());
                        esTokenTransferRecord.setName(erc20Token.getName());
                        esTokenTransferRecord.setSymbol(erc20Token.getSymbol());
                        totalSupply = erc20Token.getTotalSupply();
                    }
                    esTokenTransferRecord.setTxFee(transaction.getCost());
                    // 更新token交易数
                    addressCache.updateTokenAddress(esTokenTransferRecord.getFrom());
                    addressCache.updateTokenAddress(esTokenTransferRecord.getTto());
                    // 更新erc交易数
                    addressCache.updateErcTx(esTokenTransferRecord.getContract());

                    //对token交易的from和to进行补充，后续在StatisticsAddressConverter中进行入库。
                    if (esTokenTransferRecord.getFrom().equals(esTokenTransferRecord.getTto())) {
                        //如果from和to都是同一个则存储一份即可
                        Erc20TokenAddressRel erc20TokenAddressRelFrom =
                                Erc20TokenAddressRel.builder().decimal(esTokenTransferRecord.getDecimal())
                                        .contract(esTokenTransferRecord.getContract()).name(esTokenTransferRecord.getName())
                                        .symbol(esTokenTransferRecord.getSymbol()).address(esTokenTransferRecord.getFrom())
                                        .balance(BigDecimal.ZERO).updateTime(new Date()).totalSupply(totalSupply).txCount(1)
                                        .build();
                        addressCache.putErc20TokenAddressRelMap(UUID.randomUUID().toString(),
                                erc20TokenAddressRelFrom);
                    } else {
                        //from和to不一样则都需要存储
                        Erc20TokenAddressRel erc20TokenAddressRelTo =
                                Erc20TokenAddressRel.builder().decimal(esTokenTransferRecord.getDecimal())
                                        .contract(esTokenTransferRecord.getContract()).name(esTokenTransferRecord.getName())
                                        .symbol(esTokenTransferRecord.getSymbol()).address(esTokenTransferRecord.getTto())
                                        .balance(new BigDecimal(esTokenTransferRecord.getTValue())).updateTime(new Date()).totalSupply(totalSupply).txCount(1).build();
                        addressCache.putErc20TokenAddressRelMap("to" + UUID.randomUUID().toString(),
                                erc20TokenAddressRelTo);
                        Erc20TokenAddressRel erc20TokenAddressRelFrom =
                                Erc20TokenAddressRel.builder().decimal(esTokenTransferRecord.getDecimal())
                                        .contract(esTokenTransferRecord.getContract()).name(esTokenTransferRecord.getName())
                                        .symbol(esTokenTransferRecord.getSymbol()).address(esTokenTransferRecord.getFrom())
                                        .balance(new BigDecimal(esTokenTransferRecord.getTValue()).negate()).updateTime(new Date()).totalSupply(totalSupply).txCount(1)
                                        .build();
                        addressCache.putErc20TokenAddressRelMap("form" + UUID.randomUUID().toString(),
                                erc20TokenAddressRelFrom);
                    }


                    // 存储erc20参数到交易的info中
                    Erc20Param erc20Param = Erc20Param.builder().innerContractAddr(esTokenTransferRecord.getContract())
                            .innerContractName(esTokenTransferRecord.getName()).innerFrom(esTokenTransferRecord.getFrom())
                            .innerSymbol(esTokenTransferRecord.getSymbol()).innerTo(esTokenTransferRecord.getTto())
                            .innerDecimal(String.valueOf(esTokenTransferRecord.getDecimal()))
                            .innerValue(esTokenTransferRecord.getTValue()).build();
                    erc20Params.add(erc20Param);
                });
                transaction.setInfo(JSONObject.toJSONString(erc20Params));
            });
    }

    @Override
    public List<String> getContractFromReceiptByEvents(TransactionReceipt transactionReceipt) {
        List<ExtendEvent.EventWrapper> eventWrappers = CalculateUtils.buildEvents(blockChainConfig);
        if (null == eventWrappers || eventWrappers.size() == 0) {
            return null;
        }
        List<String> contractList = new ArrayList<>();
        ExtendEvent extendEvent = new ExtendEvent("", this.platOnClient.getWeb3jWrapper().getWeb3j(),
                new ReadonlyTransactionManager(this.platOnClient.getWeb3jWrapper().getWeb3j(), ""),
                new DefaultGasProvider(),
                NetworkParms.getChainId());
        for (int i = 0; i < eventWrappers.size(); i++) {
            ExtendEvent.EventWrapper eventWrapper = eventWrappers.get(i);
            Event event = ExtendEvent.buildEvent(eventWrapper.getEventName(), eventWrapper.getEventDefineList());
            // 根据地址位置，判定是否为 indexed
            boolean indexed = eventWrapper.getEventDefineList().get(eventWrapper.getAddressIndex()).isIndexed();
            List<ExtendEvent.TokenContractResponse> tokenContractResponseList = extendEvent.getContractAddressList(event, transactionReceipt, eventWrapper.getAddressIndex(), indexed);
            if (tokenContractResponseList != null && tokenContractResponseList.size() != 0) {
                for (int n = 0; n < tokenContractResponseList.size(); n++) {
                    contractList.add(tokenContractResponseList.get(n).getAddress());
                }
            }
        }
        log.info("根据事件解析代币地址，获取到的合约地址为：" + JSON.toJSONString(contractList));
        return contractList;
    }

}
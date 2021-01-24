package com.platon.browser.utils;

import com.alaya.contracts.ppos.dto.common.ErrorCode;
import com.alaya.protocol.core.DefaultBlockParameter;
import com.alaya.protocol.core.methods.response.Log;
import com.alaya.protocol.core.methods.response.PlatonGetCode;
import com.alaya.rlp.solidity.RlpDecoder;
import com.alaya.rlp.solidity.RlpList;
import com.alaya.rlp.solidity.RlpString;
import com.alaya.rlp.solidity.RlpType;
import com.alaya.utils.Numeric;
import com.alibaba.fastjson.JSON;
import com.platon.browser.bean.*;
import com.platon.browser.cache.AddressCache;
import com.platon.browser.cache.PPosInvokeContractInputCache;
import com.platon.browser.client.PlatOnClient;
import com.platon.browser.client.SpecialApi;
import com.platon.browser.decoder.PPOSTxDecodeResult;
import com.platon.browser.decoder.PPOSTxDecodeUtil;
import com.platon.browser.decoder.TxInputDecodeResult;
import com.platon.browser.decoder.TxInputDecodeUtil;
import com.platon.browser.elasticsearch.dto.Block;
import com.platon.browser.elasticsearch.dto.Transaction;
import com.platon.browser.enums.ContractDescEnum;
import com.platon.browser.enums.ContractTypeEnum;
import com.platon.browser.enums.InnerContractAddrEnum;
import com.platon.browser.exception.BeanCreateOrUpdateException;
import com.platon.browser.exception.BlankResponseException;
import com.platon.browser.exception.ContractInvokeException;
import com.platon.browser.param.DelegateExitParam;
import com.platon.browser.param.DelegateRewardClaimParam;
import com.platon.browser.param.TxParam;
import org.slf4j.Logger;
import org.springframework.beans.BeanUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 虚拟交易工具
 */
public class TransactionUtil {
    /**
     * 根据合约内部调用PPOS的输入信息生成虚拟PPOS交易列表
     * 
     * @param block
     *            合约调用交易所在区块信息
     * @param parentTx
     *            合约调用交易本身
     * @param invokeContractInput
     *            合约内部调用PPOS操作的输入信息
     * @return
     */
    public static List<Transaction> getVirtualTxList(Block block, Transaction parentTx,
        PPosInvokeContractInput invokeContractInput) {
        List<Transaction> transactionList = new ArrayList<>();
        if (invokeContractInput == null)
            return transactionList;
        List<TransData> trans = invokeContractInput.getTransDatas();
        for (int i = 0; i < trans.size(); i++) {
            TransData tran = trans.get(i);
            PPOSTxDecodeResult result =
                PPOSTxDecodeUtil.decode(tran.getInput(), Collections.emptyList());
            if (result.getTypeEnum() == null) {
                continue;
            }
            Transaction tx = new Transaction();
            BeanUtils.copyProperties(parentTx, tx);
            tx.setStatus(parentTx.getStatus());
            tx.setFrom(invokeContractInput.getFrom());
            tx.setTo(invokeContractInput.getTo());
            tx.setToType(Transaction.ToTypeEnum.INNER_CONTRACT.getCode());
            tx.setHash(parentTx.getHash() + "-" + i);
            tx.setType(result.getTypeEnum().getCode());
            tx.setIndex(i);
            tx.setInput(tran.getInput());
            tx.setInfo(result.getParam().toJSONString());
            tx.setSeq((long)i);
            transactionList.add(tx);

            if (Integer.parseInt(tran.getCode()) > 0) {
                // 虚拟交易失败,交易状态码设置为失败
                tx.setStatus(Transaction.StatusEnum.FAILURE.getCode());
            }
        }
        return transactionList;
    }

    /**
     * 获得真实交易解除委托时所提取的委托收益
     */
    public static BigInteger getDelegateReward(List<Log> logs) {
        if (logs == null || logs.isEmpty())
            return BigInteger.ZERO;
        return getDelegateReward(logs.get(0));
    }

    /**
     * 通用解委托奖励日志数据解析
     *
     * @param log
     * @return
     */
    private static BigInteger getDelegateReward(Log log) {
        if (log == null)
            return BigInteger.ZERO;
        String logData = log.getData();
        if (null == logData || "".equals(logData))
            return BigInteger.ZERO;

        RlpList rlp = RlpDecoder.decode(Numeric.hexStringToByteArray(logData));
        List<RlpType> rlpList = ((RlpList) (rlp.getValues().get(0))).getValues();
        String decodedStatus = new String(((RlpString) rlpList.get(0)).getBytes());
        int statusCode = Integer.parseInt(decodedStatus);

        if (statusCode != ErrorCode.SUCCESS)
            return BigInteger.ZERO;

        return ((RlpString)(RlpDecoder.decode(((RlpString)rlpList.get(1)).getBytes())).getValues().get(0))
            .asPositiveBigInteger();
    }

    /**
     * 处理虚拟交易
     * 
     * @param block
     * @param specialApi
     * @param platOnClient
     * @param contractInvokeTx
     * @param contractInvokeTxReceipt
     * @param logger
     * @return
     * @throws ContractInvokeException
     * @throws BlankResponseException
     */
    public static List<Transaction> processVirtualTx(CollectionBlock block, SpecialApi specialApi,
        PlatOnClient platOnClient, CollectionTransaction contractInvokeTx, Receipt contractInvokeTxReceipt,
        Logger logger) throws ContractInvokeException, BlankResponseException {
        if (!PPosInvokeContractInputCache.hasCache(block.getNum())) {
            // 如果当前交易所在块的PPOS调用合约输入信息不存在，则查询特殊节点，并更新缓存
            List<PPosInvokeContractInput> inputs = specialApi
                .getPPosInvokeInfo(platOnClient.getWeb3jWrapper().getWeb3j(), BigInteger.valueOf(block.getNum()));
            logger.debug("更新缓存-PPos调用合约输入参数：{}", JSON.toJSONString(inputs, true));
            List<PPosInvokeContractInput> ppremoveList = new ArrayList<>();
            for (PPosInvokeContractInput input : inputs) {
                List<TransData> removeList = new ArrayList<>();
                for (TransData transData : input.getTransDatas()) {
                    if (transData.getCode().length() > 6) {
                        removeList.add(transData);
                    }
                }
                input.getTransDatas().removeAll(removeList);
                if (input.getTransDatas().isEmpty()) {
                    ppremoveList.add(input);
                }
            }
            inputs.removeAll(ppremoveList);
            if (inputs.size() > 0)
                PPosInvokeContractInputCache.update(block.getNum(), inputs);
        }

        // 取出当前普通合约调用交易内部调用PPOS操作的输入参数
        PPosInvokeContractInput input =
            PPosInvokeContractInputCache.getPPosInvokeContractInput(contractInvokeTx.getHash());
        // 使用普通合约内部调用的输入数据构造虚拟PPOS交易列表(包括成功和失败的PPOS调用)
        List<Transaction> virtualTxList = getVirtualTxList(block, contractInvokeTx, input);
        if (!virtualTxList.isEmpty()) {
            for (int i = 0; i < virtualTxList.size(); i++) {
                Transaction vt = virtualTxList.get(i);
                // 交易失败，跳过
                if (vt.getStatus() != Transaction.StatusEnum.SUCCESS.getCode())
                    continue;
                /**
                 * 合约代理PPOS时，回执中的logs的内部结构： List- - 虚拟交易1的log - ... - 虚拟交易n的log - 合约调用的log
                 */
                Log log = contractInvokeTxReceipt.getLogs().get(i);
                if (vt.getTypeEnum() == Transaction.TypeEnum.DELEGATE_EXIT) {
                    // 解委托
                    BigInteger reward;
                    try {
                        reward = getDelegateReward(log);
                    } catch (Exception e) {
                        reward = BigInteger.ZERO;
                    }
                    // 对vt的info字段进行解析，添加reward字段后重新序列化后回填到vt中
                    DelegateExitParam delegateExitParam = vt.getTxParam(DelegateExitParam.class);
                    BigDecimal rewardAmount = new BigDecimal(reward);
                    delegateExitParam.setReward(rewardAmount);
                    vt.setInfo(delegateExitParam.toJSONString());
                }

                if (vt.getTypeEnum() == Transaction.TypeEnum.CLAIM_REWARDS) {
                    // 领取委托奖励
                    DelegateRewardClaimParam delegateRewardClaimParam =
                        DelegateRewardClaimParam.builder().rewardList(new ArrayList<>()).build();
                    List<Log> logs = Arrays.asList(log);
                    PPOSTxDecodeResult result = PPOSTxDecodeUtil.decode(vt.getInput(), logs);
                    TxParam param = result.getParam();
                    if (param != null) {
                        delegateRewardClaimParam = (DelegateRewardClaimParam)param;
                    }
                    vt.setInfo(delegateRewardClaimParam.toJSONString());
                }
            }
        }
        // 把成功的虚拟交易过滤出来
        List<Transaction> successVirtualTransactions = new ArrayList<>();
        virtualTxList.forEach(vt -> {
            if (vt.getStatus() == Transaction.StatusEnum.SUCCESS.getCode()) {
                successVirtualTransactions.add(vt);
            }
        });
        return successVirtualTransactions;
    }

    /**
     * 内置合约调用交易,解析补充信息
     */
    public static void resolveInnerContractInvokeTxComplementInfo(CollectionTransaction tx, List<Log> logs,
        ComplementInfo ci) throws BeanCreateOrUpdateException {
        PPOSTxDecodeResult decodedResult;
        try {
            // 解析交易的输入及交易回执log信息
            decodedResult = PPOSTxDecodeUtil.decode(tx.getInput(), logs);
            ci.setType(decodedResult.getTypeEnum().getCode());
            ci.setInfo(decodedResult.getParam().toJSONString());
            ci.setToType(Transaction.ToTypeEnum.INNER_CONTRACT.getCode());
            ci.setContractType(ContractTypeEnum.INNER.getCode());
            ci.setMethod(null);
            ci.setBinCode(null);
        } catch (Exception e) {
            throw new BeanCreateOrUpdateException("交易[hash:" + tx.getHash() + "]的参数解析出错:" + e.getMessage());
        }
    }

    /**
     * 获取合约的Bin代码
     * 
     * @param platOnClient
     * @param contractAddress
     * @return
     * @throws BeanCreateOrUpdateException
     */
    public static String getContractBinCode(CollectionTransaction tx, PlatOnClient platOnClient, String contractAddress,
        Logger logger) throws BeanCreateOrUpdateException {
        try {
            PlatonGetCode platonGetCode = platOnClient.getWeb3jWrapper().getWeb3j()
                .platonGetCode(contractAddress, DefaultBlockParameter.valueOf(BigInteger.valueOf(tx.getNum()))).send();
            return platonGetCode.getCode();
        } catch (Exception e) {
            platOnClient.updateCurrentWeb3jWrapper();
            String error = "获取合约代码出错[" + contractAddress + "]:" + e.getMessage();
            logger.error("{}", error);
            throw new BeanCreateOrUpdateException(error);
        }
    }

    /**
     * 创建普通合约交易,解析补充信息
     * 
     * @param contractAddress
     * @param ci
     * @throws IOException
     */
    public static void resolveGeneralContractCreateTxComplementInfo(CollectionTransaction tx, String contractAddress,
        PlatOnClient platOnClient, ComplementInfo ci, Logger logger) throws BeanCreateOrUpdateException {
        ci.setInfo("");
        ci.setBinCode(getContractBinCode(tx, platOnClient, contractAddress, logger));
        // 解码合约创建交易前缀，用于区分EVM||WASM
        TxInputDecodeResult decodedResult = TxInputDecodeUtil.decode(tx.getInput());
        ci.setType(decodedResult.getTypeEnum().getCode());
        ci.setToType(Transaction.ToTypeEnum.ACCOUNT.getCode());
        ci.setContractType(ContractTypeEnum.UNKNOWN.getCode());
        if (decodedResult.getTypeEnum() == Transaction.TypeEnum.EVM_CONTRACT_CREATE) {
            ci.setToType(Transaction.ToTypeEnum.EVM_CONTRACT.getCode());
            ci.setContractType(ContractTypeEnum.EVM.getCode());
        }
        if (decodedResult.getTypeEnum() == Transaction.TypeEnum.WASM_CONTRACT_CREATE) {
            ci.setToType(Transaction.ToTypeEnum.WASM_CONTRACT.getCode());
            ci.setContractType(ContractTypeEnum.WASM.getCode());
        }
    }

    /**
     * 调用普通合约交易,解析补充信息
     * 
     * @param ci
     * @throws IOException
     */
    public static void resolveGeneralContractInvokeTxComplementInfo(CollectionTransaction tx, PlatOnClient platOnClient,
        ComplementInfo ci, ContractTypeEnum contractTypeEnum, Logger logger) throws BeanCreateOrUpdateException {
        ci.setInfo("");
        ci.setBinCode(getContractBinCode(tx, platOnClient, tx.getTo(), logger));
        // TODO: 解析出调用合约方法名
        String txInput = tx.getInput();
        // ci.method = getGeneralContractMethod();

        ci.setContractType(contractTypeEnum.getCode());
        if (contractTypeEnum == ContractTypeEnum.EVM) {
            ci.setToType(Transaction.ToTypeEnum.EVM_CONTRACT.getCode());
        }
        if (contractTypeEnum == ContractTypeEnum.WASM) {
            ci.setToType(Transaction.ToTypeEnum.WASM_CONTRACT.getCode());
        }
        ci.setType(Transaction.TypeEnum.CONTRACT_EXEC.getCode());
        if (contractTypeEnum == ContractTypeEnum.ERC20_EVM) {
            ci.setToType(Transaction.ToTypeEnum.ERC20_CONTRACT.getCode());
            ci.setType(Transaction.TypeEnum.ERC20_CONTRACT_EXEC.getCode());
        }

    }

    /**
     * 发起普通交易,解析补充信息
     * 
     * @param ci
     */
    public static void resolveGeneralTransferTxComplementInfo(CollectionTransaction tx, ComplementInfo ci,
        AddressCache addressCache) {
        ci.setType(Transaction.TypeEnum.TRANSFER.getCode());
        ci.setContractType(null);
        ci.setMethod(null);
        ci.setInfo("{}");
        ci.setBinCode(null);
        // 需要根据交易的to地址是否是什么类型的地址
        String toAddress = tx.getTo();
        if (InnerContractAddrEnum.getAddresses().contains(toAddress)) {
            ci.setToType(Transaction.ToTypeEnum.INNER_CONTRACT.getCode());
            ci.setContractType(ContractTypeEnum.INNER.getCode());
            ci.setMethod(ContractDescEnum.getMap().get(toAddress).getContractName());
        } else if (addressCache.isEvmContractAddress(toAddress)) {
            ci.setToType(Transaction.ToTypeEnum.EVM_CONTRACT.getCode());
            ci.setContractType(ContractTypeEnum.EVM.getCode());
        } else if (addressCache.isWasmContractAddress(toAddress)) {
            ci.setToType(Transaction.ToTypeEnum.WASM_CONTRACT.getCode());
            ci.setContractType(ContractTypeEnum.WASM.getCode());
        } else {
            ci.setToType(Transaction.ToTypeEnum.ACCOUNT.getCode());
        }
    }

    /**
     * 解析erc合约交易
     * 
     * @param tx
     * @param ci
     * @param contractAddress
     */
/*    public static void resolveErcContract(CollectionTransaction tx, ComplementInfo ci, String contractAddress,
                                          Erc20ResolveServiceImpl erc20ResolveService, AddressCache addressCache) {
        ERCData ercData = erc20ResolveService.getErcData(contractAddress);
        if (ercData != null && ci.getContractType() == ContractTypeEnum.EVM.getCode()) {
            ci.setContractType(ContractTypeEnum.ERC20_EVM.getCode());
            ci.setToType(Transaction.ToTypeEnum.ERC20_CONTRACT.getCode());
            ci.setType(Transaction.TypeEnum.ERC20_CONTRACT_CREATE.getCode());
            addressCache.createFirstErc20(contractAddress, tx.getFrom(), tx.getHash(), tx.getTime(),
                CustomErc20Token.TypeEnum.EVM.getCode(), ercData);
        }
    }*/

    /**
     * 解析token交易
     * @param tx
     * @param ci
     * @param logs
     * @param addressCache
     * @return
     */
/*    public static List<OldErcTx> resolveInnerToken(CollectionTransaction tx, ComplementInfo ci,
                                                   List<Log> logs, Erc20ServiceImpl erc20Service, AddressCache addressCache, String contractAddress) {
        TransactionReceipt transactionReceipt = new TransactionReceipt();
        transactionReceipt.setLogs(logs);
        transactionReceipt.setContractAddress(contractAddress);
        List<TransferEvent> transferEvents = erc20Service.getTransferEvents(transactionReceipt);
        List<OldErcTx> oldErcTxes = new ArrayList<>();
        if(transferEvents == null || transferEvents.size() == 0){
            return oldErcTxes;
        }
        AtomicInteger i = new AtomicInteger();
        transferEvents.forEach(transferEvent -> {
            // 仅添加与指定的合约地址相同的记录
            if (transferEvent.getLog().getAddress().equalsIgnoreCase(contractAddress)) {
                // 转换参数进行设置内部交易
                OldErcTx oldErcTx =
                        OldErcTx.builder().from(transferEvent.getFrom()).tto(transferEvent.getTo())
                                .tValue(transferEvent.getValue().toString()).bn(tx.getNum()).hash(tx.getHash())
                                .contract(contractAddress).result(1).bTime(tx.getTime()).value(tx.getValue())
                                .info(transferEvent.getLog().getData()).ctime(new Date()).build();
                oldErcTx.setFromType(addressCache.getTypeData(transferEvent.getFrom()));
                oldErcTx.setToType(addressCache.getTypeData(transferEvent.getTo()));
                i.getAndIncrement();
                oldErcTxes.add(oldErcTx);
            }
        });
        return oldErcTxes;
    }*/
}

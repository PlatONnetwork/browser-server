package com.platon.browser.utils;

import com.alibaba.fastjson.JSON;
import com.platon.bech32.Bech32;
import com.platon.browser.bean.ComplementInfo;
import com.platon.browser.bean.PPosInvokeContractInput;
import com.platon.browser.bean.Receipt;
import com.platon.browser.bean.TransData;
import com.platon.browser.cache.NewAddressCache;
import com.platon.browser.cache.PPosInvokeContractInputCache;
import com.platon.browser.client.PlatOnClient;
import com.platon.browser.client.SpecialApi;
import com.platon.browser.decoder.PPOSTxDecodeResult;
import com.platon.browser.decoder.PPOSTxDecodeUtil;
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
import com.platon.contracts.ppos.dto.common.ErrorCode;
import com.platon.protocol.core.DefaultBlockParameter;
import com.platon.protocol.core.methods.response.Log;
import com.platon.protocol.core.methods.response.PlatonGetCode;
import com.platon.rlp.solidity.RlpDecoder;
import com.platon.rlp.solidity.RlpList;
import com.platon.rlp.solidity.RlpString;
import com.platon.rlp.solidity.RlpType;
import com.platon.utils.Numeric;
import lombok.extern.slf4j.Slf4j;

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

@Slf4j
public class TransactionUtil {

    /**
     * vrf内置合约
     */
    private static final String VRF_ADDRESS = "0x3000000000000000000000000000000000000001";

    /**
     * 根据合约内部调用PPOS的输入信息生成虚拟PPOS交易列表
     *
     * @param block               合约调用交易所在区块信息
     * @param parentTx            合约调用交易本身
     * @param invokeContractInput 合约内部调用PPOS操作的输入信息
     * @return
     */
    public static List<Transaction> getVirtualTxList(Block block, Transaction parentTx, PPosInvokeContractInput invokeContractInput, NewAddressCache newAddressCache) {
        List<Transaction> transactionList = new ArrayList<>();
        if (invokeContractInput == null) {
            return transactionList;
        }
        // 如果是vrf内置合约则跳过
        if (Bech32.addressDecodeHex(invokeContractInput.getTo()).equalsIgnoreCase(VRF_ADDRESS)) {
            return transactionList;
        }
        List<TransData> trans = invokeContractInput.getTransDatas();
        for (int i = 0; i < trans.size(); i++) {
            TransData tran = trans.get(i);
            PPOSTxDecodeResult result = PPOSTxDecodeUtil.decode(tran.getInput(), Collections.emptyList());
            if (result.getTypeEnum() == null) {
                continue;
            }
            Transaction virtualTx = new Transaction();
            //todo
            //BeanUtils.copyProperties(parentTx, virtualTx);
            virtualTx.setTime(parentTx.getTime());
            virtualTx.setContractType(ContractTypeEnum.INNER.getCode());
            virtualTx.setNonce(parentTx.getNonce());
            virtualTx.setContractAddress(parentTx.getContractAddress());
            virtualTx.setCost(parentTx.getCost());
            virtualTx.setGasLimit(parentTx.getGasLimit());
            virtualTx.setGasPrice(parentTx.getGasPrice());
            virtualTx.setGasUsed(parentTx.getGasUsed());
            virtualTx.setCreTime(parentTx.getCreTime());

            //2023/04/14 lvxiaoyi 如何用户合约发起内置合约调用时，是用其它私钥前面的话（按理不可能把私钥放到用户合约中）
            //这里可以设置下以防万一
            newAddressCache.addCommonAddressToBlockCtx(invokeContractInput.getFrom());

            virtualTx.setFrom(invokeContractInput.getFrom());
            virtualTx.setTo(invokeContractInput.getTo());
            //2023/04/14 lvxiaoyi to地址肯定是内置合约地址，无需再加入地址缓存
            virtualTx.setToType(Transaction.ToTypeEnum.INNER_CONTRACT.getCode());
            virtualTx.setHash(parentTx.getHash() + "-" + i);
            virtualTx.setType(result.getTypeEnum().getCode());
            virtualTx.setIndex(i);
            virtualTx.setInput(tran.getInput());
            virtualTx.setInfo(result.getParam().toJSONString());
            virtualTx.setSeq((long) i);

            virtualTx.setStatus(parentTx.getStatus());
            if (Integer.parseInt(tran.getCode()) > 0) {
                // 虚拟交易失败,交易状态码设置为失败
                virtualTx.setStatus(Transaction.StatusEnum.FAILURE.getCode());
            }
            transactionList.add(virtualTx);
        }
        return transactionList;
    }

    /**
     * 获得真实交易解除委托时所提取的委托收益
     */
    public static BigInteger getDelegateReward(List<Log> logs) {
        if (logs == null || logs.isEmpty()) {
            return BigInteger.ZERO;
        }
        return getDelegateReward(logs.get(0));
    }

    /**
     * 通用解委托奖励日志数据解析
     *
     * @param log
     * @return
     */
    private static BigInteger getDelegateReward(Log log) {
        if (log == null) {
            return BigInteger.ZERO;
        }
        String logData = log.getData();
        if (null == logData || "".equals(logData)) {
            return BigInteger.ZERO;
        }

        RlpList rlp = RlpDecoder.decode(Numeric.hexStringToByteArray(logData));
        List<RlpType> rlpList = ((RlpList) (rlp.getValues().get(0))).getValues();
        String decodedStatus = new String(((RlpString) rlpList.get(0)).getBytes());
        int statusCode = Integer.parseInt(decodedStatus);

        if (statusCode != ErrorCode.SUCCESS) {
            return BigInteger.ZERO;
        }

        return ((RlpString) (RlpDecoder.decode(((RlpString) rlpList.get(1)).getBytes())).getValues().get(0)).asPositiveBigInteger();
    }

    /**
     * 处理用户合约调用PPOS合约的交易
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
    public static List<Transaction> processVirtualTx(Block block, SpecialApi specialApi, PlatOnClient platOnClient, com.platon.browser.elasticsearch.dto.Transaction contractInvokeTx, Receipt contractInvokeTxReceipt, NewAddressCache newAddressCache) throws ContractInvokeException, BlankResponseException {
        if (!PPosInvokeContractInputCache.hasCache(block.getNum())) {
            // 如果当前交易所在块的PPOS调用合约输入信息不存在，则查询特殊节点，并更新缓存
            List<PPosInvokeContractInput> inputs = specialApi.getPPosInvokeInfo(platOnClient.getWeb3jWrapper().getWeb3j(), BigInteger.valueOf(block.getNum()));
            log.debug("更新缓存-PPos调用合约输入参数：{}", JSON.toJSONString(inputs, true));
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
            if (inputs.size() > 0) {
                PPosInvokeContractInputCache.update(block.getNum(), inputs);
            }
        }

        // 取出当前普通合约调用交易内部调用PPOS操作的输入参数
        PPosInvokeContractInput input = PPosInvokeContractInputCache.getPPosInvokeContractInput(contractInvokeTx.getHash());
        // 使用普通合约内部调用的输入数据构造虚拟PPOS交易列表(包括成功和失败的PPOS调用)
        List<Transaction> virtualTxList = getVirtualTxList(block, contractInvokeTx, input, newAddressCache);
        if (!virtualTxList.isEmpty()) {
            for (int i = 0; i < virtualTxList.size(); i++) {
                Transaction vt = virtualTxList.get(i);
                // 交易失败，跳过
                if (vt.getStatus() != Transaction.StatusEnum.SUCCESS.getCode()) {
                    continue;
                }
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
                    DelegateRewardClaimParam delegateRewardClaimParam = DelegateRewardClaimParam.builder().rewardList(new ArrayList<>()).build();
                    List<Log> logs = Arrays.asList(log);
                    PPOSTxDecodeResult result = PPOSTxDecodeUtil.decode(vt.getInput(), logs);
                    TxParam param = result.getParam();
                    if (param != null) {
                        delegateRewardClaimParam = (DelegateRewardClaimParam) param;
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
    public static void resolveInnerContractInvokeTxComplementInfo(com.platon.browser.elasticsearch.dto.Transaction tx, List<Log> logs, ComplementInfo ci) {
        // 解析交易的输入及交易回执log信息
        PPOSTxDecodeResult decodedResult = PPOSTxDecodeUtil.decode(tx.getInput(), logs);
        ci.setType(decodedResult.getTypeEnum().getCode());
        ci.setInfo(decodedResult.getParam().toJSONString());
        ci.setToType(Transaction.ToTypeEnum.INNER_CONTRACT.getCode());
        ci.setContractType(ContractTypeEnum.INNER.getCode());
        ci.setMethod(null);
        ci.setBinCode(null);
    }

    /**
     * 获取合约的Bin代码
     *
     * @param platOnClient
     * @param contractAddress
     * @return
     * @throws BeanCreateOrUpdateException
     */
    public static String getContractBinCode(com.platon.browser.elasticsearch.dto.Transaction tx, PlatOnClient platOnClient, String contractAddress) throws IOException {
        PlatonGetCode platonGetCode = platOnClient.getWeb3jWrapper().getWeb3j().platonGetCode(contractAddress, DefaultBlockParameter.valueOf(BigInteger.valueOf(tx.getNum()))).send();
        return platonGetCode.getCode();
    }

    public static String getContractBinCode( PlatOnClient platOnClient, String contractAddress, Long blockNumber) throws BeanCreateOrUpdateException {
        try {
            PlatonGetCode platonGetCode = platOnClient.getWeb3jWrapper().getWeb3j().platonGetCode(contractAddress, DefaultBlockParameter.valueOf(BigInteger.valueOf(blockNumber))).send();
            return platonGetCode.getCode();
        } catch (Exception e) {
            platOnClient.updateCurrentWeb3jWrapper();
            String error = "获取合约代码出错[" + contractAddress + "]:" + e.getMessage();
            log.error("{}", error);
            throw new BeanCreateOrUpdateException(error);
        }
    }

    /**
     * 创建普通合约,解析补充信息
     *
     * @param result
     * @param contractAddress
     * @param platOnClient
     * @param ci
     * @param contractTypeEnum
     * @return void
     * @date 2021/4/20
     */
    public static void resolveGeneralContractCreateTxComplementInfo(com.platon.browser.elasticsearch.dto.Transaction result, String contractAddress, PlatOnClient platOnClient, ComplementInfo ci, ContractTypeEnum contractTypeEnum) {
        ci.setInfo("");
        //String binCode = "0x00";
        //ci.setBinCode(binCode);
        //ci.setBinCode(TransactionUtil.getContractBinCode(result, platOnClient, result.getContractAddress()));

        ci.setContractType(contractTypeEnum.getCode());
        if (contractTypeEnum == ContractTypeEnum.ERC20_EVM) {
            ci.setType(com.platon.browser.elasticsearch.dto.Transaction.TypeEnum.ERC20_CONTRACT_CREATE.getCode());
            ci.setToType(com.platon.browser.elasticsearch.dto.Transaction.ToTypeEnum.ERC20_CONTRACT.getCode());
        } else if (contractTypeEnum == ContractTypeEnum.ERC721_EVM) {
            ci.setType(com.platon.browser.elasticsearch.dto.Transaction.TypeEnum.ERC721_CONTRACT_CREATE.getCode());
            ci.setToType(com.platon.browser.elasticsearch.dto.Transaction.ToTypeEnum.ERC721_CONTRACT.getCode());
        } else if (contractTypeEnum == ContractTypeEnum.ERC1155_EVM) {
            ci.setType(com.platon.browser.elasticsearch.dto.Transaction.TypeEnum.ERC1155_CONTRACT_CREATE.getCode());
            ci.setToType(com.platon.browser.elasticsearch.dto.Transaction.ToTypeEnum.ERC1155_CONTRACT.getCode());
        } else if (contractTypeEnum == ContractTypeEnum.WASM) {
            ci.setType(com.platon.browser.elasticsearch.dto.Transaction.TypeEnum.WASM_CONTRACT_CREATE.getCode());
            ci.setToType(com.platon.browser.elasticsearch.dto.Transaction.ToTypeEnum.WASM_CONTRACT.getCode());
        } else {
            ci.setType(com.platon.browser.elasticsearch.dto.Transaction.TypeEnum.EVM_CONTRACT_CREATE.getCode());
            ci.setToType(com.platon.browser.elasticsearch.dto.Transaction.ToTypeEnum.EVM_CONTRACT.getCode());
        }
        ci.setContractType(contractTypeEnum.getCode());

    }

    public static Transaction.TypeEnum convert2CreatingContractTxType(ContractTypeEnum contractTypeEnum){
        if (contractTypeEnum == ContractTypeEnum.ERC20_EVM) {
            return com.platon.browser.elasticsearch.dto.Transaction.TypeEnum.ERC20_CONTRACT_CREATE;
        } else if (contractTypeEnum == ContractTypeEnum.ERC721_EVM) {
            return com.platon.browser.elasticsearch.dto.Transaction.TypeEnum.ERC721_CONTRACT_CREATE;
        } else if (contractTypeEnum == ContractTypeEnum.ERC1155_EVM) {
            return com.platon.browser.elasticsearch.dto.Transaction.TypeEnum.ERC1155_CONTRACT_CREATE;
        } else if (contractTypeEnum == ContractTypeEnum.WASM) {
            return com.platon.browser.elasticsearch.dto.Transaction.TypeEnum.WASM_CONTRACT_CREATE;
        } else {
            return com.platon.browser.elasticsearch.dto.Transaction.TypeEnum.EVM_CONTRACT_CREATE;
        }
    }

    public static Transaction.ToTypeEnum convert2ToType(ContractTypeEnum contractTypeEnum){
        if (contractTypeEnum == ContractTypeEnum.ERC20_EVM) {
            return com.platon.browser.elasticsearch.dto.Transaction.ToTypeEnum.ERC20_CONTRACT;
        } else if (contractTypeEnum == ContractTypeEnum.ERC721_EVM) {
            return com.platon.browser.elasticsearch.dto.Transaction.ToTypeEnum.ERC721_CONTRACT;
        } else if (contractTypeEnum == ContractTypeEnum.ERC1155_EVM) {
            return com.platon.browser.elasticsearch.dto.Transaction.ToTypeEnum.ERC1155_CONTRACT;
        } else if (contractTypeEnum == ContractTypeEnum.WASM) {
            return com.platon.browser.elasticsearch.dto.Transaction.ToTypeEnum.WASM_CONTRACT;
        } else {
            return com.platon.browser.elasticsearch.dto.Transaction.ToTypeEnum.EVM_CONTRACT;
        }
    }

    public static Transaction.TypeEnum convert2InvokingContractTxType(ContractTypeEnum contractTypeEnum){
        if (contractTypeEnum == ContractTypeEnum.ERC20_EVM) {
            return com.platon.browser.elasticsearch.dto.Transaction.TypeEnum.ERC20_CONTRACT_EXEC;
        } else if (contractTypeEnum == ContractTypeEnum.ERC721_EVM) {
            return com.platon.browser.elasticsearch.dto.Transaction.TypeEnum.ERC721_CONTRACT_EXEC;
        } else if (contractTypeEnum == ContractTypeEnum.ERC1155_EVM) {
            return com.platon.browser.elasticsearch.dto.Transaction.TypeEnum.ERC1155_CONTRACT_EXEC;
        } else if (contractTypeEnum == ContractTypeEnum.WASM) {
            return com.platon.browser.elasticsearch.dto.Transaction.TypeEnum.CONTRACT_EXEC;
        } else {
            return com.platon.browser.elasticsearch.dto.Transaction.TypeEnum.CONTRACT_EXEC;
        }
    }

    /**
     * 调用普通合约,解析补充信息
     *
     * @param tx
     * @param platOnClient
     * @param ci
     * @param contractTypeEnum
     * @return void
     * @date 2021/4/20
     */
    public static void resolveGeneralContractInvokeTxComplementInfo(Block collectionBlock, com.platon.browser.elasticsearch.dto.Transaction tx, PlatOnClient platOnClient, ComplementInfo ci, ContractTypeEnum contractTypeEnum) {
        ci.setInfo("");
        // 2023/04/12 lvxiaoyi: 合约调用，不需要查询binCode，合约的bincCode只需要在合约创建时调用一次即可。如果合约销毁，则在receipt中有被销毁的地址列表

        // TODO: 解析出调用合约方法名
        String txInput = tx.getInput();
        // ci.method = getGeneralContractMethod();

        ci.setContractType(contractTypeEnum.getCode());
        if (contractTypeEnum == ContractTypeEnum.EVM) {
            ci.setToType(Transaction.ToTypeEnum.EVM_CONTRACT.getCode());
            ci.setType(Transaction.TypeEnum.CONTRACT_EXEC.getCode());
        }
        if (contractTypeEnum == ContractTypeEnum.WASM) {
            ci.setToType(Transaction.ToTypeEnum.WASM_CONTRACT.getCode());
            ci.setType(Transaction.TypeEnum.CONTRACT_EXEC.getCode());
        }
        if (contractTypeEnum == ContractTypeEnum.ERC20_EVM) {
            ci.setToType(Transaction.ToTypeEnum.ERC20_CONTRACT.getCode());
            ci.setType(Transaction.TypeEnum.ERC20_CONTRACT_EXEC.getCode());
        }
        if (contractTypeEnum == ContractTypeEnum.ERC721_EVM) {
            ci.setToType(Transaction.ToTypeEnum.ERC721_CONTRACT.getCode());
            ci.setType(Transaction.TypeEnum.ERC721_CONTRACT_EXEC.getCode());
        }
        if (contractTypeEnum == ContractTypeEnum.ERC1155_EVM) {
            ci.setToType(Transaction.ToTypeEnum.ERC1155_CONTRACT.getCode());
            ci.setType(Transaction.TypeEnum.ERC1155_CONTRACT_EXEC.getCode());
        }
    }

    /**
     * 发起普通交易,解析补充信息
     *
     * @param ci
     */
    public static void resolveGeneralTransferTxComplementInfo(com.platon.browser.elasticsearch.dto.Transaction tx, ComplementInfo ci, NewAddressCache newAddressCache) {
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
            return;
        }
        if (newAddressCache.isEvmContractAddress(toAddress)) {
            ci.setToType(Transaction.ToTypeEnum.EVM_CONTRACT.getCode());
            ci.setContractType(ContractTypeEnum.EVM.getCode());
            return;
        }
        if (newAddressCache.isWasmContractAddress(toAddress)) {
            ci.setToType(Transaction.ToTypeEnum.WASM_CONTRACT.getCode());
            ci.setContractType(ContractTypeEnum.WASM.getCode());
            return;
        }
        if (newAddressCache.isErc20ContractAddress(toAddress)) {
            ci.setToType(Transaction.ToTypeEnum.ERC20_CONTRACT.getCode());
            ci.setContractType(ContractTypeEnum.ERC20_EVM.getCode());
            return;
        }
        if (newAddressCache.isErc721ContractAddress(toAddress)) {
            ci.setToType(Transaction.ToTypeEnum.ERC721_CONTRACT.getCode());
            ci.setContractType(ContractTypeEnum.ERC721_EVM.getCode());
            return;
        }
        if (newAddressCache.isErc1155ContractAddress(toAddress)) {
            ci.setToType(Transaction.ToTypeEnum.ERC1155_CONTRACT.getCode());
            ci.setContractType(ContractTypeEnum.ERC1155_EVM.getCode());
            return;
        }
        ci.setToType(Transaction.ToTypeEnum.ACCOUNT.getCode());
    }

}

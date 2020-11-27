package com.platon.browser.common.collection.dto;

import com.platon.browser.client.PlatOnClient;
import com.platon.browser.client.Receipt;
import com.platon.browser.client.SpecialApi;
import com.platon.browser.common.complement.cache.AddressCache;
import com.platon.browser.common.utils.TransactionUtil;
import com.platon.browser.elasticsearch.dto.ESTokenTransferRecord;
import com.platon.browser.elasticsearch.dto.Transaction;
import com.platon.browser.enums.ContractTypeEnum;
import com.platon.browser.enums.InnerContractAddrEnum;
import com.platon.browser.erc.ERCInterface;
import com.platon.browser.exception.BeanCreateOrUpdateException;
import com.platon.browser.exception.BlankResponseException;
import com.platon.browser.exception.ContractInvokeException;
import com.platon.browser.param.DelegateExitParam;
import com.platon.browser.param.DelegateRewardClaimParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

@Slf4j
public class CollectionTransaction extends Transaction {
    private CollectionTransaction() {}

    public static CollectionTransaction newInstance() {
        Date date = new Date();
        CollectionTransaction transaction = new CollectionTransaction();
        transaction.setCreTime(date).setUpdTime(date).setCost(BigDecimal.ZERO.toString())
            .setGasLimit(BigDecimal.ZERO.toString()).setGasPrice(BigDecimal.ZERO.toString())
            .setGasUsed(BigDecimal.ZERO.toString()).setStatus(StatusEnum.FAILURE.getCode())
            .setValue(BigDecimal.ZERO.toString()).setIndex(0);
        return transaction;
    }

    CollectionTransaction updateWithBlock(CollectionBlock block) {
        this.setTime(block.getTime());
        return this;
    }

    CollectionTransaction updateWithRawTransaction(org.web3j.protocol.core.methods.response.Transaction transaction) {
        this.setNum(transaction.getBlockNumber().longValue()).setBHash(transaction.getBlockHash())
            .setHash(transaction.getHash()).setValue(transaction.getValue().toString())
            .setIndex(transaction.getTransactionIndex().intValue()).setGasPrice(transaction.getGasPrice().toString())
            .setInput(transaction.getInput()).setTo(transaction.getTo()).setFrom(transaction.getFrom())
            .setGasLimit(transaction.getGas().toString()).setNonce(transaction.getNonce().toString());
        return this;
    }

    // 交易解析阶段，维护自身的普通合约地址列表，其初始化数据来自地址缓存
    // <普通合约地址,合约类型枚举>
    private static final Map<String, ContractTypeEnum> GENERAL_CONTRACT_ADDRESS_2_TYPE_MAP = new HashMap<>();

    public static Map<String, ContractTypeEnum> getGeneralContractAddressCache() {
        return Collections.unmodifiableMap(GENERAL_CONTRACT_ADDRESS_2_TYPE_MAP);
    }

    public static void setGeneralContractAddressCache(String key, ContractTypeEnum contractTypeEnum) {
        GENERAL_CONTRACT_ADDRESS_2_TYPE_MAP.put(key, contractTypeEnum);
    }

    private void initGeneralContractCache(AddressCache addressCache) {
        if (GENERAL_CONTRACT_ADDRESS_2_TYPE_MAP.isEmpty()) {
            addressCache.getEvmContractAddressCache()
                .forEach(address -> GENERAL_CONTRACT_ADDRESS_2_TYPE_MAP.put(address, ContractTypeEnum.EVM));
            addressCache.getWasmContractAddressCache()
                .forEach(address -> GENERAL_CONTRACT_ADDRESS_2_TYPE_MAP.put(address, ContractTypeEnum.WASM));
            addressCache.getEvmErc20ContractAddressCache()
                .forEach(address -> GENERAL_CONTRACT_ADDRESS_2_TYPE_MAP.put(address, ContractTypeEnum.ERC20_EVM));
        }
    }

    CollectionTransaction updateWithBlockAndReceipt(CollectionBlock block, Receipt receipt, PlatOnClient platOnClient,
        AddressCache addressCache, SpecialApi specialApi, ERCInterface ercInterface)
        throws BeanCreateOrUpdateException, ContractInvokeException, BlankResponseException {
        // 使用地址缓存初始化普通合约缓存信息
        this.initGeneralContractCache(addressCache);

        // ============需要通过解码补充的交易信息============
        ComplementInfo ci = new ComplementInfo();

        String inputWithoutPrefix = StringUtils.isNotBlank(this.getInput()) ? this.getInput().replace("0x", "") : "";
        if (InnerContractAddrEnum.getAddresses().contains(this.getTo()) && StringUtils.isNotBlank(inputWithoutPrefix)) {
            // 如果to地址是内置合约地址，则解码交易输入
            TransactionUtil.resolveInnerContractInvokeTxComplementInfo(this, receipt.getLogs(), ci);
        } else {

            // 检测是否包指定代币的创建事件
            TransactionReceipt transactionReceipt = new TransactionReceipt();
            transactionReceipt.setLogs(receipt.getLogs());
            List<String> contractAddress = ercInterface.getContractFromReceiptByEvents(transactionReceipt);
            if (null != contractAddress && !contractAddress.isEmpty()) {
                for (String address : contractAddress) {
                    ComplementInfo complementInfo = new ComplementInfo();
                    // 如果to地址为空则是普通合约创建
                    TransactionUtil.resolveGeneralContractCreateTxComplementInfo(this, address, platOnClient, complementInfo, log);
                    TransactionUtil.resolveErcContract(this, complementInfo, address, ercInterface, addressCache);
                    addressCache.updateFirst(address, complementInfo);
                    // 把合约地址添加至缓存
                    GENERAL_CONTRACT_ADDRESS_2_TYPE_MAP.put(address, ContractTypeEnum.getEnum(complementInfo.contractType));
                }
            }

            if (StringUtils.isBlank(this.getTo())) {
                // 如果to地址为空则是普通合约创建
                TransactionUtil.resolveGeneralContractCreateTxComplementInfo(this, receipt.getContractAddress(), platOnClient, ci, log);

                // 把回执里的合约地址回填到交易的to字段
                this.setTo(receipt.getContractAddress());
                TransactionUtil.resolveErcContract(this, ci, receipt.getContractAddress(), ercInterface, addressCache);
                addressCache.updateFirst(receipt.getContractAddress(), ci);
                // 把合约地址添加至缓存
                GENERAL_CONTRACT_ADDRESS_2_TYPE_MAP.put(this.getTo(), ContractTypeEnum.getEnum(ci.contractType));
            } else {
                if (GENERAL_CONTRACT_ADDRESS_2_TYPE_MAP.containsKey(this.getTo()) && inputWithoutPrefix.length() >= 8) {
                    // 如果是普通合约调用（EVM||WASM）
                    ContractTypeEnum contractTypeEnum = GENERAL_CONTRACT_ADDRESS_2_TYPE_MAP.get(this.getTo());
                    TransactionUtil.resolveGeneralContractInvokeTxComplementInfo(this, platOnClient, ci,
                        contractTypeEnum, log);
                    this.setStatus(receipt.getStatus()); // 普通合约调用的交易是否成功只看回执的status,不用看log中的状态
                    if (this.getStatus() == StatusEnum.SUCCESS.getCode()) {
                        // 普通合约调用成功, 取成功的代理PPOS虚拟交易列表
                        List<Transaction> successVirtualTransactions =
                            TransactionUtil.processVirtualTx(block, specialApi, platOnClient, this, receipt, log);
                        // 把成功的虚拟交易挂到当前普通合约交易上
                        this.setVirtualTransactions(successVirtualTransactions);
                        // 如果是erc20地址则需要查询转账记录
                        // 跨合约调用：需要取回执中的地址判定是否有代币交易
                        List<String> uniAddressList = new ArrayList<>();
                        receipt.getLogs().forEach(log -> {
                            if (StringUtils.isEmpty(log.getAddress())) {
                                return;
                            }
                            if (!uniAddressList.contains(log.getAddress())) {
                                uniAddressList.add(log.getAddress());
                            }
                        });
                        // 提取产生Event的合约地址，并进行解析
                        uniAddressList.forEach(addr -> {
                            if (addressCache.isEvmErc20ContractAddress(addr)) {
                                List<ESTokenTransferRecord> erc20Tokens = TransactionUtil
                                        .resolveInnerToken(this, ci, receipt.getLogs(), ercInterface, addressCache, addr);
                                if (!erc20Tokens.isEmpty()) {
                                    this.getEsTokenTransferRecords().addAll((erc20Tokens));
                                }
                            }
                        });
                    }
                } else {
                    BigInteger value =
                        StringUtils.isNotBlank(this.getValue()) ? new BigInteger(this.getValue()) : BigInteger.ZERO;
                    if (value.compareTo(BigInteger.ZERO) >= 0) {
                        // 如果输入为空且value大于0，则是普通转账
                        TransactionUtil.resolveGeneralTransferTxComplementInfo(this, ci, addressCache);
                    }
                }
            }
        }

        if (ci.type == null) {
            throw new BeanCreateOrUpdateException(
                "交易类型为空,遇到未知交易:[blockNumber=" + this.getNum() + ",txHash=" + this.getHash() + "]");
        }
        if (ci.toType == null) {
            throw new BeanCreateOrUpdateException(
                "To地址为空:[blockNumber=" + this.getNum() + ",txHash=" + this.getHash() + "]");
        }

        // 默认取状态字段作为交易成功与否的状态
        int status = receipt.getStatus();
        if (InnerContractAddrEnum.getAddresses().contains(this.getTo()) && ci.type != TypeEnum.TRANSFER.getCode()) {
            // 如果接收者为内置合约且不为转账, 取日志中的状态作为交易成功与否的状态
            status = receipt.getLogStatus();
        }

        // 交易信息
        this.setGasUsed(receipt.getGasUsed().toString())
            .setCost(this.decimalGasUsed().multiply(this.decimalGasPrice()).toString())
            .setFailReason(receipt.getFailReason()).setStatus(status).setSeq(this.getNum() * 10000 + this.getIndex())
            .setInfo(ci.info).setType(ci.type).setToType(ci.toType).setContractAddress(receipt.getContractAddress())
            .setContractType(ci.contractType).setBin(ci.binCode).setMethod(ci.method);

        // 累加总交易数
        block.setTxQty(block.getTxQty() + 1);
        // 累加具体业务交易数
        switch (this.getTypeEnum()) {
            case TRANSFER: // 转账交易，from地址转账交易数加一
                block.setTranQty(block.getTranQty() + 1);
                break;
            case STAKE_CREATE:// 创建验证人
            case STAKE_INCREASE:// 增加自有质押
            case STAKE_MODIFY:// 编辑验证人
            case STAKE_EXIT:// 退出验证人
            case REPORT:// 举报验证人
                block.setSQty(block.getSQty() + 1);
                break;
            case DELEGATE_CREATE:// 发起委托
                block.setDQty(block.getDQty() + 1);
                break;
            case DELEGATE_EXIT:// 撤销委托
                if (status == Receipt.getSuccess()) {
                    // 成功的领取交易才解析info回填
                    // 设置委托奖励提取额
                    DelegateExitParam param = this.getTxParam(DelegateExitParam.class);
                    BigDecimal reward = new BigDecimal(TransactionUtil.getDelegateReward(receipt.getLogs()));
                    param.setReward(reward);
                    this.setInfo(param.toJSONString());
                }
                block.setDQty(block.getDQty() + 1);
                break;
            case CLAIM_REWARDS: // 领取委托奖励
                DelegateRewardClaimParam param =
                    DelegateRewardClaimParam.builder().rewardList(new ArrayList<>()).build();
                if (status == Receipt.getSuccess()) {
                    // 成功的领取交易才解析info回填
                    param = this.getTxParam(DelegateRewardClaimParam.class);
                }
                this.setInfo(param.toJSONString());
                block.setDQty(block.getDQty() + 1);
                break;
            case PROPOSAL_TEXT:// 创建文本提案
            case PROPOSAL_UPGRADE:// 创建升级提案
            case PROPOSAL_PARAMETER:// 创建参数提案
            case PROPOSAL_VOTE:// 提案投票
            case PROPOSAL_CANCEL:// 取消提案
            case VERSION_DECLARE:// 版本声明
                block.setPQty(block.getPQty() + 1);
                break;
            default:
        }
        // 累加当前交易的手续费到当前区块的txFee
        block.setTxFee(block.decimalTxFee().add(this.decimalCost()).toString());
        // 累加当前交易的能量限制到当前区块的txGasLimit
        block.setTxGasLimit(block.decimalTxGasLimit().add(this.decimalGasLimit()).toString());
        return this;
    }
}

package com.platon.browser.analyzer;

import cn.hutool.core.collection.CollUtil;
import com.platon.browser.bean.CollectionTransaction;
import com.platon.browser.bean.ComplementInfo;
import com.platon.browser.bean.Receipt;
import com.platon.browser.cache.AddressCache;
import com.platon.browser.client.PlatOnClient;
import com.platon.browser.client.SpecialApi;
import com.platon.browser.decoder.TxInputDecodeResult;
import com.platon.browser.decoder.TxInputDecodeUtil;
import com.platon.browser.elasticsearch.dto.Block;
import com.platon.browser.enums.ContractTypeEnum;
import com.platon.browser.enums.InnerContractAddrEnum;
import com.platon.browser.exception.BeanCreateOrUpdateException;
import com.platon.browser.exception.BlankResponseException;
import com.platon.browser.exception.ContractInvokeException;
import com.platon.browser.param.DelegateExitParam;
import com.platon.browser.param.DelegateRewardClaimParam;
import com.platon.browser.utils.AddressUtil;
import com.platon.browser.utils.TransactionUtil;
import com.platon.browser.v0152.analyzer.ErcCache;
import com.platon.browser.v0152.analyzer.ErcTokenAnalyzer;
import com.platon.browser.v0152.bean.ErcToken;
import com.platon.browser.v0152.enums.ErcTypeEnum;
import com.platon.protocol.core.methods.response.Transaction;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

/**
 * 交易分析器
 */
@Slf4j
@Component
public class TransactionAnalyzer {

    @Resource
    private PlatOnClient platOnClient;

    @Resource
    private AddressCache addressCache;

    @Resource
    private ErcCache ercCache;

    @Resource
    private SpecialApi specialApi;

    @Resource
    private ErcTokenAnalyzer ercTokenAnalyzer;

    // 交易解析阶段，维护自身的普通合约地址列表，其初始化数据来自地址缓存和erc緩存
    // <普通合约地址,合约类型枚举>
    private static final Map<String, ContractTypeEnum> GENERAL_CONTRACT_ADDRESS_2_TYPE_MAP = new HashMap<>();

    public static Map<String, ContractTypeEnum> getGeneralContractAddressCache() {
        return Collections.unmodifiableMap(GENERAL_CONTRACT_ADDRESS_2_TYPE_MAP);
    }

    public static void setGeneralContractAddressCache(String key, ContractTypeEnum contractTypeEnum) {
        GENERAL_CONTRACT_ADDRESS_2_TYPE_MAP.put(key, contractTypeEnum);
    }

    /**
     * 使用地址缓存初始化普通合约缓存信息
     *
     * @param
     * @return void
     * @date 2021/4/20
     */
    private void initGeneralContractCache() {
        if (GENERAL_CONTRACT_ADDRESS_2_TYPE_MAP.isEmpty()) {
            addressCache.getEvmContractAddressCache().forEach(address -> GENERAL_CONTRACT_ADDRESS_2_TYPE_MAP.put(address, ContractTypeEnum.EVM));
            addressCache.getWasmContractAddressCache().forEach(address -> GENERAL_CONTRACT_ADDRESS_2_TYPE_MAP.put(address, ContractTypeEnum.WASM));
            ercCache.getErc20AddressCache().forEach(address -> GENERAL_CONTRACT_ADDRESS_2_TYPE_MAP.put(address, ContractTypeEnum.ERC20_EVM));
            ercCache.getErc721AddressCache().forEach(address -> GENERAL_CONTRACT_ADDRESS_2_TYPE_MAP.put(address, ContractTypeEnum.ERC721_EVM));
        }
    }

    /**
     * 交易解析
     *
     * @param collectionBlock 区块
     * @param rawTransaction  交易
     * @param receipt         交易回执
     * @return com.platon.browser.bean.CollectionTransaction
     * @date 2021/4/20
     */
    public CollectionTransaction analyze(Block collectionBlock, Transaction rawTransaction, Receipt receipt) throws BeanCreateOrUpdateException, ContractInvokeException, BlankResponseException {
        CollectionTransaction result = CollectionTransaction.newInstance().updateWithBlock(collectionBlock).updateWithRawTransaction(rawTransaction);
        log.info("当前区块[{}]交易[{}]解析开始...", collectionBlock.getNum(), result.getHash());
        // 使用地址缓存初始化普通合约缓存信息
        initGeneralContractCache();

        // ============需要通过解码补充的交易信息============
        ComplementInfo ci = new ComplementInfo();

        // 新创建合约处理
        if (CollUtil.isNotEmpty(receipt.getContractCreated())) {
            receipt.getContractCreated().forEach(contract -> {
                // solidity 类型 erc20 或 721 token检测及入口
                ErcToken ercToken = ercTokenAnalyzer.resolveToken(contract.getAddress(), BigInteger.valueOf(collectionBlock.getNum()));
                // solidity or wasm
                TxInputDecodeResult txInputDecodeResult = TxInputDecodeUtil.decode(result.getInput());
                // 内存中更新地址类型
                ContractTypeEnum contractTypeEnum;
                if (ercToken.getTypeEnum() == ErcTypeEnum.ERC20 && txInputDecodeResult.getTypeEnum() == com.platon.browser.elasticsearch.dto.Transaction.TypeEnum.EVM_CONTRACT_CREATE) {
                    contractTypeEnum = ContractTypeEnum.ERC20_EVM;
                } else if (ercToken.getTypeEnum() == ErcTypeEnum.ERC721 && txInputDecodeResult.getTypeEnum() == com.platon.browser.elasticsearch.dto.Transaction.TypeEnum.EVM_CONTRACT_CREATE) {
                    contractTypeEnum = ContractTypeEnum.ERC721_EVM;
                } else if (txInputDecodeResult.getTypeEnum() == com.platon.browser.elasticsearch.dto.Transaction.TypeEnum.WASM_CONTRACT_CREATE) {
                    contractTypeEnum = ContractTypeEnum.WASM;
                } else {
                    contractTypeEnum = ContractTypeEnum.EVM;
                }
                GENERAL_CONTRACT_ADDRESS_2_TYPE_MAP.put(contract.getAddress(), contractTypeEnum);
                log.info("当前交易[{}]合约地址[{}]的合约类型为[{}]", result.getHash(), contract.getAddress(), contractTypeEnum);
                if (!AddressUtil.isAddrZero(contract.getAddress())) {
                    // 补充address
                    addressCache.updateFirst(contract.getAddress(), contractTypeEnum);
                } else {
                    log.error("该地址{}是0地址,不加载到地址缓存中", contract.getAddress());
                }
            });
        }

        // 处理交易信息
        String inputWithoutPrefix = StringUtils.isNotBlank(result.getInput()) ? result.getInput().replace("0x", "") : "";
        if (InnerContractAddrEnum.getAddresses().contains(result.getTo()) && StringUtils.isNotBlank(inputWithoutPrefix)) {
            // 如果to地址是内置合约地址，则解码交易输入
            TransactionUtil.resolveInnerContractInvokeTxComplementInfo(result, receipt.getLogs(), ci);
            log.info("当前交易[{}]为内置合约,from[{}],to[{}],解码交易输入", result.getHash(), result.getFrom(), result.getTo());
        } else {
            // to地址为空 或者 contractAddress有值时代表交易为创建合约
            if (StringUtils.isBlank(result.getTo())) {
                TransactionUtil.resolveGeneralContractCreateTxComplementInfo(result,
                                                                             receipt.getContractAddress(),
                                                                             platOnClient,
                                                                             ci,
                                                                             log,
                                                                             GENERAL_CONTRACT_ADDRESS_2_TYPE_MAP.get(receipt.getContractAddress()));
                result.setTo(receipt.getContractAddress());
                log.info("当前交易[{}]为创建合约,from[{}],to[{}],type为[{}],toType[{}],contractType为[{}]",
                         result.getHash(),
                         result.getFrom(),
                         result.getTo(),
                         ci.getType(),
                         ci.getToType(),
                         ci.getContractType());
            } else {
                if (GENERAL_CONTRACT_ADDRESS_2_TYPE_MAP.containsKey(result.getTo()) && inputWithoutPrefix.length() >= 8) {
                    // 如果是普通合约调用（EVM||WASM）
                    ContractTypeEnum contractTypeEnum = GENERAL_CONTRACT_ADDRESS_2_TYPE_MAP.get(result.getTo());
                    TransactionUtil.resolveGeneralContractInvokeTxComplementInfo(result, platOnClient, ci, contractTypeEnum, log);
                    // 普通合约调用的交易是否成功只看回执的status,不用看log中的状态
                    result.setStatus(receipt.getStatus());
                    if (result.getStatus() == com.platon.browser.elasticsearch.dto.Transaction.StatusEnum.SUCCESS.getCode()) {
                        // 普通合约调用成功, 取成功的代理PPOS虚拟交易列表
                        List<com.platon.browser.elasticsearch.dto.Transaction> successVirtualTransactions = TransactionUtil.processVirtualTx(collectionBlock,
                                                                                                                                             specialApi,
                                                                                                                                             platOnClient,
                                                                                                                                             result,
                                                                                                                                             receipt,
                                                                                                                                             log);
                        // 把成功的虚拟交易挂到当前普通合约交易上
                        result.setVirtualTransactions(successVirtualTransactions);
                    }
                    log.info("当前交易[{}]为普通合约调用,from[{}],to[{}],type为[{}],toType[{}],虚拟交易数为[{}]",
                             result.getHash(),
                             result.getFrom(),
                             result.getTo(),
                             ci.getType(),
                             ci.getToType(),
                             result.getVirtualTransactions().size());
                } else {
                    BigInteger value = StringUtils.isNotBlank(result.getValue()) ? new BigInteger(result.getValue()) : BigInteger.ZERO;
                    if (value.compareTo(BigInteger.ZERO) >= 0) {
                        // 如果输入为空且value大于0，则是普通转账
                        TransactionUtil.resolveGeneralTransferTxComplementInfo(result, ci, addressCache);
                        log.info("当前交易[{}]为普通转账,from[{}],to[{}],转账金额为[{}]", result.getHash(), result.getFrom(), result.getTo(), value);
                    }
                }
            }
        }

        if (ci.getType() == null) {
            throw new BeanCreateOrUpdateException("交易类型为空,遇到未知交易:[blockNumber=" + result.getNum() + ",txHash=" + result.getHash() + "]");
        }
        if (ci.getToType() == null) {
            throw new BeanCreateOrUpdateException("To地址为空:[blockNumber=" + result.getNum() + ",txHash=" + result.getHash() + "]");
        }

        // 默认取状态字段作为交易成功与否的状态
        int status = receipt.getStatus();
        if (InnerContractAddrEnum.getAddresses().contains(result.getTo()) && ci.getType() != com.platon.browser.elasticsearch.dto.Transaction.TypeEnum.TRANSFER.getCode()) {
            // 如果接收者为内置合约且不为转账, 取日志中的状态作为交易成功与否的状态
            status = receipt.getLogStatus();
        }

        // 交易信息
        result.setGasUsed(receipt.getGasUsed().toString())
              .setCost(result.decimalGasUsed().multiply(result.decimalGasPrice()).toString())
              .setFailReason(receipt.getFailReason())
              .setStatus(status)
              .setSeq(result.getNum() * 100000 + result.getIndex())
              .setInfo(ci.getInfo())
              .setType(ci.getType())
              .setToType(ci.getToType())
              .setContractAddress(receipt.getContractAddress())
              .setContractType(ci.getContractType())
              .setBin(ci.getBinCode())
              .setMethod(ci.getMethod());
        ercTokenAnalyzer.resolveTx(collectionBlock, result, receipt);

        // 累加总交易数
        collectionBlock.setTxQty(collectionBlock.getTxQty() + 1);
        // 累加具体业务交易数
        switch (result.getTypeEnum()) {
            case TRANSFER: // 转账交易，from地址转账交易数加一
                collectionBlock.setTranQty(collectionBlock.getTranQty() + 1);
                break;
            case STAKE_CREATE:// 创建验证人
            case STAKE_INCREASE:// 增加自有质押
            case STAKE_MODIFY:// 编辑验证人
            case STAKE_EXIT:// 退出验证人
            case REPORT:// 举报验证人
                collectionBlock.setSQty(collectionBlock.getSQty() + 1);
                break;
            case DELEGATE_CREATE:// 发起委托
                collectionBlock.setDQty(collectionBlock.getDQty() + 1);
                break;
            case DELEGATE_EXIT:// 撤销委托
                if (status == Receipt.SUCCESS) {
                    // 成功的领取交易才解析info回填
                    // 设置委托奖励提取额
                    DelegateExitParam param = result.getTxParam(DelegateExitParam.class);
                    BigDecimal reward = new BigDecimal(TransactionUtil.getDelegateReward(receipt.getLogs()));
                    param.setReward(reward);
                    result.setInfo(param.toJSONString());
                }
                collectionBlock.setDQty(collectionBlock.getDQty() + 1);
                break;
            case CLAIM_REWARDS: // 领取委托奖励
                DelegateRewardClaimParam param = DelegateRewardClaimParam.builder().rewardList(new ArrayList<>()).build();
                if (status == Receipt.SUCCESS) {
                    // 成功的领取交易才解析info回填
                    param = result.getTxParam(DelegateRewardClaimParam.class);
                }
                result.setInfo(param.toJSONString());
                collectionBlock.setDQty(collectionBlock.getDQty() + 1);
                break;
            case PROPOSAL_TEXT:// 创建文本提案
            case PROPOSAL_UPGRADE:// 创建升级提案
            case PROPOSAL_PARAMETER:// 创建参数提案
            case PROPOSAL_VOTE:// 提案投票
            case PROPOSAL_CANCEL:// 取消提案
            case VERSION_DECLARE:// 版本声明
                collectionBlock.setPQty(collectionBlock.getPQty() + 1);
                break;
            default:
        }
        // 累加当前交易的手续费到当前区块的txFee
        String txFee = collectionBlock.decimalTxFee().add(result.decimalCost()).toString();
        log.info("当前区块[{}]交易[{}]:区块累计手续费[{}]=累计手续费[{}]+交易成本[{}](gas燃料[{}] * gas价格[{}])",
                 collectionBlock.getNum(),
                 result.getHash(),
                 txFee,
                 collectionBlock.decimalTxFee(),
                 result.decimalCost(),
                 result.decimalGasUsed(),
                 result.decimalGasPrice());
        collectionBlock.setTxFee(txFee);
        // 累加当前交易的能量限制到当前区块的txGasLimit
        collectionBlock.setTxGasLimit(collectionBlock.decimalTxGasLimit().add(result.decimalGasLimit()).toString());
        return result;
    }

}

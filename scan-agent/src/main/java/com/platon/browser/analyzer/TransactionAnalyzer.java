package com.platon.browser.analyzer;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.platon.browser.bean.*;
import com.platon.browser.cache.NewAddressCache;
import com.platon.browser.client.PlatOnClient;
import com.platon.browser.client.SpecialApi;
import com.platon.browser.dao.entity.Address;
import com.platon.browser.dao.entity.Token;
import com.platon.browser.dao.mapper.AddressMapper;
import com.platon.browser.dao.mapper.TokenMapper;
import com.platon.browser.elasticsearch.dto.Block;
import com.platon.browser.enums.AddressTypeEnum;
import com.platon.browser.enums.ContractTypeEnum;
import com.platon.browser.enums.InnerContractAddrEnum;
import com.platon.browser.param.DelegateExitParam;
import com.platon.browser.param.DelegateRewardClaimParam;
import com.platon.browser.utils.TransactionUtil;
import com.platon.browser.v0152.analyzer.ErcTokenAnalyzer;
import com.platon.browser.v0152.service.ErcDetectService;
import com.platon.protocol.core.methods.response.Transaction;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

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
    private NewAddressCache newAddressCache;

    @Resource
    private SpecialApi specialApi;

    @Resource
    private ErcTokenAnalyzer ercTokenAnalyzer;

    @Resource
    private ErcDetectService ercDetectService;
    @Resource
    private AddressMapper addressMapper;

    @Resource
    private TokenMapper tokenMapper;

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
    /*private void initGeneralContractCache() {
        if (GENERAL_CONTRACT_ADDRESS_2_TYPE_MAP.isEmpty()) {
            addressCache.getEvmContractAddressCache().forEach(address -> GENERAL_CONTRACT_ADDRESS_2_TYPE_MAP.put(address, ContractTypeEnum.EVM));
            addressCache.getWasmContractAddressCache().forEach(address -> GENERAL_CONTRACT_ADDRESS_2_TYPE_MAP.put(address, ContractTypeEnum.WASM));
            ercCache.getErc20AddressCache().forEach(address -> GENERAL_CONTRACT_ADDRESS_2_TYPE_MAP.put(address, ContractTypeEnum.ERC20_EVM));
            ercCache.getErc721AddressCache().forEach(address -> GENERAL_CONTRACT_ADDRESS_2_TYPE_MAP.put(address, ContractTypeEnum.ERC721_EVM));
            ercCache.getErc1155AddressCache().forEach(address -> GENERAL_CONTRACT_ADDRESS_2_TYPE_MAP.put(address, ContractTypeEnum.ERC1155_EVM));
        }
    }*/

    /**
     * 交易解析
     *
     * @param collectionBlock 区块
     * @param rawTransaction  交易
     * @param receipt         交易回执
     * @return com.platon.browser.bean.CollectionTransaction
     * @date 2021/4/20
     */
    public com.platon.browser.elasticsearch.dto.Transaction analyze(Block collectionBlock, Transaction rawTransaction, Receipt receipt) throws Exception {
        log.debug("开始分析区块交易，块高：{}", collectionBlock.getNum());
        StopWatch watch = new StopWatch("分析区块交易");

        com.platon.browser.elasticsearch.dto.Transaction  dtoTransaction = DtoTransactionUtil.newDtoTransaction();
        DtoTransactionUtil.updateWithRawTransaction(rawTransaction, dtoTransaction);
        dtoTransaction.setTime(collectionBlock.getTime());

        // 使用地址缓存初始化普通合约缓存信息
        //initGeneralContractCache();

        //首先把from加入相关地址列表（此时不知from是否是新地址，所以设置pending标志）
        newAddressCache.addPendingAddressToBlockCtx(dtoTransaction.getFrom());


        // 新创建合约处理
        // todo: 2023/05/04 lvxiaoyi
        //  在特殊节点中，会采集opCreate /opCreate2 操作码中的新建合约地址
        //  但是这两个操作码对scan的影响不同：
        //  opCreate操作码新建的合约地址，肯定是在scan没有出现过的
        //  而opCreate2操作码新建合约的地址，可能在之前，就给这个地址转账过，即scan上可能已有此地址。
        //  不过目前，特殊节点采集新建合约地址时，没有区分这两种情况，造成receipt.getContractCreated()返回的地址并不一定都是新地址
        if (CollUtil.isNotEmpty(receipt.getContractCreated())) {
            log.debug("新建合约的交易回执：{}", JSON.toJSONString(receipt));

            for(ContractInfo contract : receipt.getContractCreated()){
                //合约是新建的，因此获取binCode
                watch.start("获取新建合约的binCode");
                // todo: 2023/05/04 lvxiaoyi 考虑在getTransactionReceipt时，随同contractCreated一起返回bincode
                String binCode = TransactionUtil.getContractBinCode(dtoTransaction, platOnClient, contract.getAddress());
                watch.stop();
                if(StringUtils.isBlank(binCode) || binCode.equalsIgnoreCase("0x")){
                    log.warn("发现bin为空的新合约地址:{}", contract.getAddress());
                    break;
                }


                ContractTypeEnum contractType = ercDetectService.getContractType(contract.getAddress(), BigInteger.valueOf(collectionBlock.getNum()), binCode, dtoTransaction.getInput());

                //解析token
                if(contractType.equals(ContractTypeEnum.ERC20_EVM) || contractType.equals(ContractTypeEnum.ERC721_EVM) || contractType.equals(ContractTypeEnum.ERC1155_EVM) ){
                    // solidity 类型 erc20 或 721 token检测及入口
                    watch.start("解析新建合约的具体类型");
                    Token token = ercTokenAnalyzer.resolveNewToken(contractType.convertToErcType(), contract.getAddress(), BigInteger.valueOf(collectionBlock.getNum()));
                    watch.stop();
                }

                CustomAddress relatedAddress = CustomAddress.createNewAccountAddress(contract.getAddress());
                //设置地址类型
                relatedAddress.setType(contractType.convertToAddressType().getCode());
                relatedAddress.setOption(CustomAddress.Option.NEW); //todo: 2023/05/04 lvxiaoyi 返回的地址并不一定都是新地址
                relatedAddress.setContractBin(binCode);
                relatedAddress.setContractType(contractType);
                relatedAddress.setContractCreate(dtoTransaction.getFrom());
                relatedAddress.setContractCreatehash(dtoTransaction.getHash());

                //把新建的合约地址保存在当前block的上下文中
                newAddressCache.addNewContractAddressToBlockCtx(relatedAddress);
            }
        }

        // 销毁合约处理
        if (CollUtil.isNotEmpty(receipt.getContractSuicided())) {
            log.info("销毁合约的交易回执：{}", JSON.toJSONString(receipt));
            for(ContractInfo contract : receipt.getContractSuicided()){
                newAddressCache.addSuicidedAddressToBlockCtx(contract.getAddress());
            }
        }


        ComplementInfo ci = new ComplementInfo();
        // 处理交易信息
        String inputWithoutPrefix = StringUtils.isNotBlank(dtoTransaction.getInput()) ? dtoTransaction.getInput().replace("0x", "") : "";
        if (InnerContractAddrEnum.getAddresses().contains(dtoTransaction.getTo()) && StringUtils.isNotBlank(inputWithoutPrefix)) {
            // 如果to地址是内置合约地址，则解码交易输入。并且不需要加入相关地址缓存
            watch.start("内置合约处理");

            TransactionUtil.resolveInnerContractInvokeTxComplementInfo(dtoTransaction, receipt.getLogs(), ci);
            watch.stop();
            log.debug("当前交易[{}]为内置合约,from[{}],to[{}],解码交易输入", dtoTransaction.getHash(), dtoTransaction.getFrom(), dtoTransaction.getTo());
        } else {
            // to地址为空 或者 contractAddress有值时代表交易为创建合约。此时新合约地址、类型已经在前面的逻辑中加入相关地址缓存了
            if (StringUtils.isBlank(dtoTransaction.getTo())) {
                ContractTypeEnum contractType = newAddressCache.getContractType(receipt.getContractAddress());
                if (contractType == null) {
                    log.error("can not find the contract type: {}", receipt.getContractAddress());
                    throw new RuntimeException("can not find the contract type");
                }
                TransactionUtil.resolveGeneralContractCreateTxComplementInfo(dtoTransaction,
                        receipt.getContractAddress(),
                        platOnClient,
                        ci,
                        contractType);
                dtoTransaction.setTo(receipt.getContractAddress());

                log.debug("当前交易[{}]为创建合约,from[{}],to[{}],type为[{}],toType[{}],contractType为[{}]",
                        dtoTransaction.getHash(),
                        dtoTransaction.getFrom(),
                        dtoTransaction.getTo(),
                        dtoTransaction.getType(),
                        dtoTransaction.getToType(),
                        dtoTransaction.getContractType());
            } else {
                ContractTypeEnum contractType = newAddressCache.getContractType(dtoTransaction.getTo());
                if (contractType != null && inputWithoutPrefix.length() >= 8) {
                    //因为是合约调用，所以to地址肯定在以前的区块新建了，因此不需要把to加入相关地址列表

                    // 如果是普通合约调用（EVM||WASM）
                    TransactionUtil.resolveGeneralContractInvokeTxComplementInfo(collectionBlock, dtoTransaction, platOnClient, ci, contractType);

                    // 普通合约调用的交易是否成功只看回执的status,不用看log中的状态
                    dtoTransaction.setStatus(receipt.getStatus());
                    if (dtoTransaction.getStatus() == com.platon.browser.elasticsearch.dto.Transaction.StatusEnum.SUCCESS.getCode()) {
                        // 普通合约调用成功, 取成功的代理PPOS虚拟交易列表
                        watch.start("普通合约调用结果成功，解析token内部虚拟交易");
                        // todo:
                        // 2023/04/25 lvxiaoyi 每次合约调用成功后，都会rpc调用特殊节点，这个影响同步性能，参考：com.platon.browser.bean.Receipt.contractSuicided的使用，添加一个proxiedPPosTxList
                        // 通过 platon_getTransactionByBlock 来返回内置的ppos交易。
                        // 所谓的VirtualTx，实际上是指ppos交易，不过这些ppos交易，不是EOA账户直接调用ppos合约的，而是通过一个用户合约来调用ppos合约
                        // proxiedPPosTxList
                        List<com.platon.browser.elasticsearch.dto.Transaction> successVirtualTransactions = TransactionUtil.processVirtualTx(collectionBlock,
                                specialApi,
                                platOnClient,
                                dtoTransaction,
                                receipt,
                                newAddressCache);
                        // 把成功的虚拟交易挂到当前普通合约交易上
                        dtoTransaction.setVirtualTransactions(successVirtualTransactions);
                        watch.stop();
                    }
                    log.debug("当前交易[{}]为普通合约调用,from[{}],to[{}],type为[{}],toType[{}],虚拟交易数为[{}]",
                            dtoTransaction.getHash(),
                            dtoTransaction.getFrom(),
                            dtoTransaction.getTo(),
                            ci.getType(),
                            ci.getToType(),
                            dtoTransaction.getVirtualTransactions().size());
                } else {
                    BigInteger value = StringUtils.isNotBlank(dtoTransaction.getValue()) ? new BigInteger(dtoTransaction.getValue()) : BigInteger.ZERO;
                    if (value.compareTo(BigInteger.ZERO) >= 0) {
                        newAddressCache.addPendingAddressToBlockCtx(dtoTransaction.getTo());

                        // 如果输入为空且value大于0，则是普通转账
                        TransactionUtil.resolveGeneralTransferTxComplementInfo(dtoTransaction, ci, newAddressCache);
                        log.debug("当前交易[{}]为普通转账,from[{}],to[{}],转账金额为[{}]", dtoTransaction.getHash(), dtoTransaction.getFrom(), dtoTransaction.getTo(), value);
                    }
                }
            }
        }

        if (ci.getType() == null) {
            log.error("交易处理后，交易类型为空，块高：{} txHash:{}", dtoTransaction.getNum(), dtoTransaction.getHash());
            throw new RuntimeException("交易处理后，交易类型为空");
        }
        if (ci.getToType() == null) {
            log.error("交易处理后，to地址类型为空，块高：{} txHash:{}", dtoTransaction.getNum(), dtoTransaction.getHash());
            throw new RuntimeException("交易处理后，to地址类型为空");
        }

        // 默认取状态字段作为交易成功与否的状态
        int status = receipt.getStatus();
        if (InnerContractAddrEnum.getAddresses().contains(dtoTransaction.getTo()) && ci.getType() != com.platon.browser.elasticsearch.dto.Transaction.TypeEnum.TRANSFER.getCode()) {
            // 如果接收者为内置合约且不为转账, 取日志中的状态作为交易成功与否的状态
            status = receipt.getLogStatus();
        }

        // 交易信息
        dtoTransaction.setGasUsed(receipt.getGasUsed().toString())
              .setCost(dtoTransaction.decimalGasUsed().multiply(dtoTransaction.decimalGasPrice()).toString())
              .setFailReason(receipt.getFailReason())
              .setStatus(status)
              .setSeq(dtoTransaction.getNum() * 100000 + dtoTransaction.getIndex())
              .setInfo(ci.getInfo())
              .setType(ci.getType())
              .setToType(ci.getToType())
              .setContractAddress(receipt.getContractAddress())
              .setContractType(ci.getContractType())
                // 2023/04/10 lvxiaoyi
                // todo: 用bin=0x来判断是否是销毁合约的交易，有问题：
                // 如果一个区块两笔交易发给同一个合约地址，第一个交易是正常的token转账，第二个交易是自杀交易；那么这里就会把第一、第二笔交易认为是自杀交易。
                // 需要修改特殊节点，有特殊节点才能知道哪笔交易是销毁操作
              //.setBin(ci.getBinCode())
              .setMethod(ci.getMethod());
        //
        // 重要：
        // 解析token交易，得到token的holder，以及holder的持有余额，交易次数等，把这些信息都写入token_holder表
        watch.start("解析token交易");
        ercTokenAnalyzer.resolveTokenTransferTx(collectionBlock, dtoTransaction, receipt);
        watch.stop();

        watch.start("解析交易后处理");
        // 累加总交易数
        collectionBlock.setTxQty(collectionBlock.getTxQty() + 1);
        // 累加具体业务交易数
        switch (dtoTransaction.getTypeEnum()) {
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
                    DelegateExitParam param = dtoTransaction.getTxParam(DelegateExitParam.class);
                    BigDecimal reward = new BigDecimal(TransactionUtil.getDelegateReward(receipt.getLogs()));
                    param.setReward(reward);
                    dtoTransaction.setInfo(param.toJSONString());
                }
                collectionBlock.setDQty(collectionBlock.getDQty() + 1);
                break;
            case CLAIM_REWARDS: // 领取委托奖励
                DelegateRewardClaimParam param = DelegateRewardClaimParam.builder().rewardList(new ArrayList<>()).build();
                if (status == Receipt.SUCCESS) {
                    // 成功的领取交易才解析info回填
                    param = dtoTransaction.getTxParam(DelegateRewardClaimParam.class);
                }
                dtoTransaction.setInfo(param.toJSONString());
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
        String txFee = collectionBlock.decimalTxFee().add(dtoTransaction.decimalCost()).toString();
        log.debug("当前区块[{}]交易[{}]:区块累计手续费[{}]=累计手续费[{}]+交易成本[{}](gas燃料[{}] * gas价格[{}])",
                 collectionBlock.getNum(),
                 dtoTransaction.getHash(),
                 txFee,
                 collectionBlock.decimalTxFee(),
                 dtoTransaction.decimalCost(),
                 dtoTransaction.decimalGasUsed(),
                 dtoTransaction.decimalGasPrice());
        collectionBlock.setTxFee(txFee);
        // 累加当前交易的能量限制到当前区块的txGasLimit
        collectionBlock.setTxGasLimit(collectionBlock.decimalTxGasLimit().add(dtoTransaction.decimalGasLimit()).toString());
        proxyContract(dtoTransaction.getHash());
        watch.stop();
        log.debug("结束分析区块交易，块高：{}，耗时统计：{}", collectionBlock.getNum(), watch.prettyPrint());
        return dtoTransaction;
    }

    /**
     * 针对特殊合约修改，仅对主网生效
     *
     * @param :
     * @return: void
     * @date: 2022/2/9
     */
    private void proxyContract(String txHash) {
        // 创建合约后的第一条交易，会设置合约的721属性
        if ("0x908a9f487a1c9d39a17afe1a868705eec9b0ec899998eb7036412634388755ad".equalsIgnoreCase(txHash)) {
            Address address = addressMapper.selectByPrimaryKey("lat1w9ys9726hyhqk9yskffgly08xanpzdgqvp2sz6");
            if (ObjectUtil.isNotNull(address)) {
                Address newAddress = new Address();
                newAddress.setAddress(address.getAddress());
                newAddress.setType(AddressTypeEnum.ERC721_EVM_CONTRACT.getCode());
                addressMapper.updateByPrimaryKeySelective(newAddress);
                Token token = new Token();
                token.setAddress("lat1w9ys9726hyhqk9yskffgly08xanpzdgqvp2sz6");
                token.setType("erc721");
                token.setName("QPassport");
                token.setSymbol("QPT");
                token.setTotalSupply("0");
                token.setDecimal(0);
                token.setIsSupportErc165(true);
                token.setIsSupportErc20(false);
                token.setIsSupportErc721(true);
                token.setIsSupportErc721Enumeration(true);
                token.setIsSupportErc721Metadata(true);
                token.setIsSupportErc1155(false);
                token.setIsSupportErc1155Metadata(false);
                token.setTokenTxQty(0);
                token.setHolder(0);
                token.setContractDestroyBlock(0L);
                token.setContractDestroyUpdate(false);
                tokenMapper.insertSelective(token);
                // 重置此地址的合约类型缓存
                newAddressCache.addAddressTypeCache("lat1w9ys9726hyhqk9yskffgly08xanpzdgqvp2sz6", AddressTypeEnum.ERC721_EVM_CONTRACT);
                /*ercCache.init();
                addressCache.getEvmContractAddressCache().remove("lat1w9ys9726hyhqk9yskffgly08xanpzdgqvp2sz6");*/
            } else {
                log.error("找不到该代理合约地址{}", "lat1w9ys9726hyhqk9yskffgly08xanpzdgqvp2sz6");
            }
        }
    }

}

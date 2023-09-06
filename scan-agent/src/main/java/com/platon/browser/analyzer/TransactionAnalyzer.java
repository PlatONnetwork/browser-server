package com.platon.browser.analyzer;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSON;
import com.platon.browser.bean.*;
import com.platon.browser.cache.NewAddressCache;
import com.platon.browser.client.PlatOnClient;
import com.platon.browser.client.SpecialApi;
import com.platon.browser.dao.entity.Token;
import com.platon.browser.dao.entity.TxTransferBak;
import com.platon.browser.dao.mapper.AddressMapper;
import com.platon.browser.dao.mapper.TokenMapper;
import com.platon.browser.elasticsearch.dto.Block;
import com.platon.browser.enums.ContractTypeEnum;
import com.platon.browser.enums.ErcTypeEnum;
import com.platon.browser.enums.InnerContractAddrEnum;
import com.platon.browser.param.DelegateExitParam;
import com.platon.browser.param.DelegateRewardClaimParam;
import com.platon.browser.utils.TransactionUtil;
import com.platon.browser.v0152.analyzer.ErcTokenAnalyzer;
import com.platon.browser.v0152.bean.ErcContractId;
import com.platon.browser.v0152.contract.ErcContract;
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
     *  特别是把PPOS交易，解析成dto.Transaction，后续有PPOSService继续处理，包括两种类型的PPOS交易，一是直接发给内置合约的ppos交易，二是有用户合约调用内置合约而形成的ppos交易。
     *
     * @param collectionBlock 区块
     * @param rawTransaction  交易
     * @param receipt         交易回执
     * @return com.platon.browser.bean.CollectionTransaction
     * @date 2021/4/20
     */
    public com.platon.browser.elasticsearch.dto.Transaction analyze(Block collectionBlock, Transaction rawTransaction, Receipt receipt) throws Exception {
        log.debug("开始分析区块交易，块高：{}, receipt:{}", collectionBlock.getNum(), JSON.toJSONString(receipt));
        StopWatch watch = new StopWatch("分析区块交易");

        com.platon.browser.elasticsearch.dto.Transaction  dtoTransaction = DtoTransactionUtil.newDtoTransaction();
        DtoTransactionUtil.updateWithRawTransaction(rawTransaction, dtoTransaction);
        dtoTransaction.setTime(collectionBlock.getTime());

        // 使用地址缓存初始化普通合约缓存信息
        //initGeneralContractCache();

        //首先把from加入相关地址列表（此时不知from是否是新地址，所以设置pending标志）
        newAddressCache.addCommonAddressToBlockCtx(dtoTransaction.getFrom());


        // 新创建合约处理
        // todo: 2023/05/04 lvxiaoyi
        //  在特殊节点中，会采集opCreate /opCreate2 操作码中的新建合约地址
        //  但是这两个操作码对scan的影响不同：
        //  opCreate操作码新建的合约地址，肯定是在scan没有出现过的
        //  而opCreate2操作码新建合约的地址，可能在之前，就给这个地址转账过，即scan上可能已有此地址。
        //  不过目前，特殊节点采集新建合约地址时，没有区分这两种情况，造成receipt.getContractCreated()返回的地址并不一定都是新地址
        if (CollUtil.isNotEmpty(receipt.getContractCreated()) ) { //合约发布交易没有成功的话，特殊节点不会采集此合约的地址
            log.info("交易回执中的新建成功的合约：{}", JSON.toJSONString(receipt.getContractCreated()));

            for(ContractInfo contract : receipt.getContractCreated()){
                //合约是新建的，因此获取binCode

                // 2023/05/04 lvxiaoyi 在getTransactionReceipt时，随同contractCreated一起返回bincode
                // 2023/07/24 lvxiaoyi 拆分address表的contract_bin，拆分到单独的contract_bin表，
                //String binCode = TransactionUtil.getContractBinCode(dtoTransaction, platOnClient, contract.getAddress());
                String binCode = contract.getBin();
                ContractTypeEnum contractType = ContractTypeEnum.getEnum(contract.getContractType());

                ErcTypeEnum ercType =  contractType.convertToErcType();



                if (ercType != null){
                    ErcContract ercContract = ercDetectService.getErcContract(ercType, contract.getAddress(), BigInteger.valueOf(collectionBlock.getNum()));
                    if (ercContract != null){
                        ErcContractId ercContractId = ercDetectService.resolveContractId(ercContract);
                        ercContractId.setTypeEnum(ercType);
                        //是关注的erc token, 则去链上获取token的基本资料：name / symbol等，并增加到token表中，以及缓存中
                        //todo: ercContractCache /tokenCache要统一起来
                        Token token = ercTokenAnalyzer.resolveNewToken(contract.getAddress(),  BigInteger.valueOf(collectionBlock.getNum()), ercContractId, contract, false);
                    }

                }

                CustomAddress relatedAddress = CustomAddress.createDefaultAccountAddress(contract.getAddress(), CustomAddress.Option.PENDING);
                //设置地址类型
                relatedAddress.setType(contractType.convertToAddressType().getCode());
                //relatedAddress.setOption(CustomAddress.Option.NEW);
                relatedAddress.setContractBin(binCode);
                relatedAddress.setContractType(contractType);
                relatedAddress.setContractCreate(dtoTransaction.getFrom());
                relatedAddress.setContractCreatehash(dtoTransaction.getHash());

                //把新建的合约地址保存在当前block的上下文中
                // todo: 2023/05/04 lvxiaoyi 这个地址有可能是存在的（参考create2地址算法）
                newAddressCache.addCreatedContractAddressToBlockCtx(relatedAddress);


            }
        }

        // 销毁合约处理
        if (CollUtil.isNotEmpty(receipt.getContractSuicided())) {
            log.info("交易回执的销毁合约：{}", JSON.toJSONString(receipt.getContractSuicided()));
            for(ContractInfo contract : receipt.getContractSuicided()){
                newAddressCache.addSuicidedAddressToBlockCtx(contract.getAddress());
            }
        }

        // 合约内部转账记录
        // 都是指LAT的转账：1.调用合约时，带有VALUE值；2.合约内部向其它地址转账；3.合约销毁时的转账
        // 原始交易成功，非常规转账才会成功;
        if(CollUtil.isNotEmpty(receipt.getEmbedTransfers())){
            log.info("交易回执的非常规转账：{}", JSON.toJSONString(receipt.getEmbedTransfers()));
            receipt.getEmbedTransfers().forEach(embedTransfer -> {
                newAddressCache.addCommonAddressToBlockCtx(embedTransfer.getFrom());
                newAddressCache.addCommonAddressToBlockCtx(embedTransfer.getTo());
            });

            List<TxTransferBak> embedTransferTxList = resolveEmbedTransferTx(collectionBlock, dtoTransaction, receipt);

            dtoTransaction.setTransferTxList(embedTransferTxList);
            dtoTransaction.setTransferTxInfo(JSON.toJSONString(embedTransferTxList));
        }


        // 识别到的合约代理调用
        if (CollUtil.isNotEmpty(receipt.getProxyPatterns())) {
            log.info("交易回执的合约代理：{}", JSON.toJSONString(receipt.getProxyPatterns()));
            for(ProxyPattern proxyPattern : receipt.getProxyPatterns()){
                // 发现代理模式之前：
                // address 表中 proxy地址的type = evm, impl地址的type = erc20 / erc721 / erc1155
                // token 表中, impl地址存在，而proxy地址不存在

                // 发现代理关系之后，需要做的改变：
                // address 表中 proxy地址的type改成impl地址的type； impl地址的类型改成proxy的类型
                // token 表中，把proxy地址新增进去，type/name/symbol/decimals/totalSupply 都是impl的属性值；把impl的地址删除
                // 要注意，代理地址可以代理成另一个实现地址

                ContractInfo proxyContract = proxyPattern.getProxy();
                //特殊节点只是从bincode来分析得到合约类型，缺省就是EVM合约
                ContractTypeEnum proxyContractType = ContractTypeEnum.getEnum(proxyContract.getContractType());
                ErcTypeEnum proxyErcType = proxyContractType.convertToErcType();
                if (proxyErcType!=null){
                    log.warn("识别到虚假合约代理:{}", JSON.toJSONString(proxyPattern));
                    continue;
                }

                //implContract 包含着token.name, symbol, decimals, totalSupply等信息
                ContractInfo implContract = proxyPattern.getImplementation();
                ContractTypeEnum implContractType = ContractTypeEnum.getEnum(implContract.getContractType());
                ErcTypeEnum implErcType = implContractType.convertToErcType();
                if (implErcType==null){
                    log.warn("识别到虚假合约代理:{}", JSON.toJSONString(proxyPattern));
                    continue;
                }

                //交换合约类型
                proxyContract.setContractType(implContractType.getCode());
                implContract.setContractType(proxyContractType.getCode());


                CustomAddress proxyAddress = CustomAddress.createDefaultAccountAddress(proxyContract.getAddress(), CustomAddress.Option.PENDING);
                //交换proxy/impl的地址类型，合约类型
                proxyAddress.setType(implContractType.convertToAddressType().getCode());
                proxyAddress.setContractType(implContractType);
                newAddressCache.addModifiedAddressTypeToBlockCtx(proxyAddress);


                CustomAddress implAddress = CustomAddress.createDefaultAccountAddress(implContract.getAddress(), CustomAddress.Option.PENDING);
                //交换proxy/impl的地址类型，合约类型
                implAddress.setType(proxyContractType.convertToAddressType().getCode());
                implAddress.setContractType(proxyContractType);
                newAddressCache.addModifiedAddressTypeToBlockCtx(implAddress);


                //实现地址不再表示为一个token，从token表中删除
                tokenMapper.deleteByPrimaryKey(implAddress.getAddress());

                //proxy要当作一个新的token，token属性是impl的属性（特殊节点采集）（有可能已经代理过其它实现地址，这次是代理另一个实现地址）
                ErcContract finalProxyErcContract = ercDetectService.getErcContract(implErcType, proxyContract.getAddress(), BigInteger.valueOf(collectionBlock.getNum()));
                ErcContractId finalProxyErcContractId = ercDetectService.getErcContractIdFromContractInfo(implErcType, implContract);
                //是关注的erc token, 则去链上获取token的基本资料：name / symbol等，并增加到token表中，以及缓存中
                //todo: ercContractCache /tokenCache要统一起来


                //要把impl 当作 一个新的合约，但是合约地址是 proxy的地址
                Token token = ercTokenAnalyzer.resolveNewToken(proxyContract.getAddress(), BigInteger.valueOf(collectionBlock.getNum()), finalProxyErcContractId, implContract, true);

                // 更新token表
                // address 表有专门的地址类型修改批量更新：
                // 参考：com.platon.browser.analyzer.statistic.StatisticsAddressAnalyzer.analyze
                //ercTokenAnalyzer.updateProxyToken(proxyContract, implContract);
            }
        }

        // 识别到的隐式PPOS调用（用户合约内发起的调用）
        if ( CollUtil.isNotEmpty(receipt.getImplicitPPOSTxs())) {
        //if (receipt.getStatus() == Receipt.SUCCESS && CollUtil.isNotEmpty(receipt.getImplicitPPOSTxs())) {
            log.info("交易回执的隐式PPOS交易：{}", JSON.toJSONString(receipt.getImplicitPPOSTxs()));
            //todo:
        }

        ComplementInfo ci = new ComplementInfo();
        // 处理交易信息
        String inputWithoutPrefix = StringUtils.isNotBlank(dtoTransaction.getInput()) ? dtoTransaction.getInput().replace("0x", "") : "";
        if (InnerContractAddrEnum.getAddresses().contains(dtoTransaction.getTo()) && StringUtils.isNotBlank(inputWithoutPrefix)) {
            // 如果to地址是内置合约地址，则解码交易输入。并且不需要加入相关地址缓存
            watch.start("内置合约处理");
            //解析出内置合约执行的错误代码
            TransactionUtil.resolveInnerContractInvokeTxComplementInfo(dtoTransaction, receipt.getLogs(), ci);
            watch.stop();
            log.debug("当前交易[{}]为内置合约,from[{}],to[{}],解码交易输入", dtoTransaction.getHash(), dtoTransaction.getFrom(), dtoTransaction.getTo());
        } else {
            // to地址为空 或者 contractAddress有值时代表交易为创建合约。此时新合约地址、类型已经在前面的逻辑中加入相关地址缓存了
            if (StringUtils.isBlank(dtoTransaction.getTo())) {

                ContractTypeEnum contractType = ContractTypeEnum.EVM;
                if (receipt.getStatus() == Receipt.SUCCESS){
                    contractType = newAddressCache.getContractType(receipt.getContractAddress());
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
                        ci.getType(),
                        ci.getToType(),
                        ci.getContractType());

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

                        if (CollUtil.isNotEmpty(receipt.getImplicitPPOSTxs())) {
                            //用户合约调用，产生了PPOS交易，则把成功的ppos交易过滤出来
                            List<com.platon.browser.elasticsearch.dto.Transaction> successVirtualTransactions =  TransactionUtil.processImplicitPPOSTx(collectionBlock,
                                    receipt.getImplicitPPOSTxs(),
                                    dtoTransaction,
                                    receipt,
                                    newAddressCache);
                            dtoTransaction.setVirtualTransactions(successVirtualTransactions);
                        }


                        // todo:
                        // 2023/04/25 lvxiaoyi 每次合约调用成功后，都会rpc调用特殊节点，这个影响同步性能，参考：com.platon.browser.bean.Receipt.contractSuicided的使用，添加一个proxiedPPosTxList
                        // 通过 platon_getTransactionByBlock 来返回内置的ppos交易。
                        // 所谓的VirtualTx，实际上是指ppos交易，不过这些ppos交易，不是EOA账户直接调用ppos合约的，而是通过一个用户合约来调用ppos合约
                        // proxiedPPosTxList
                       /* List<com.platon.browser.elasticsearch.dto.Transaction> successVirtualTransactions = TransactionUtil.processVirtualTx(collectionBlock,
                                specialApi,
                                platOnClient,
                                dtoTransaction,
                                receipt,
                                newAddressCache);
                        // 把成功的虚拟交易挂到当前普通合约交易上
                        dtoTransaction.setVirtualTransactions(successVirtualTransactions);*/
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
                        newAddressCache.addCommonAddressToBlockCtx(dtoTransaction.getTo());

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
            //status = receipt.getLogStatus();
            //todo: 如果交易失败，但是业务成功，会有这样的case吗？那最终的状态应该是什么呢？
            //如果是内置合约交易，前面已经对log解码过了
            status =  ci.getInnerContractTxErrCode()==0 ? Receipt.SUCCESS : Receipt.FAILURE ;
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
        watch.stop();
        log.debug("结束分析区块交易，块高：{}，耗时统计：{}", collectionBlock.getNum(), watch.prettyPrint());
        return dtoTransaction;
    }

    private List<TxTransferBak> resolveEmbedTransferTx(Block collectionBlock, com.platon.browser.elasticsearch.dto.Transaction tx, Receipt receipt) {
        List<TxTransferBak> transferBakList = new ArrayList<>();
        for (EmbedTransfer embedTransfer : receipt.getEmbedTransfers()) {
            if(embedTransfer.getAmount() == null || StringUtils.isBlank(embedTransfer.getTo()) || StringUtils.isBlank(embedTransfer.getFrom())){
                log.warn("当前交易[{}]的合约内部转账记录不全, embedTransfer = [{}]", tx.getHash(), embedTransfer);
                continue;
            }
            TxTransferBak transferBak =  new TxTransferBak();
            transferBak.setSeq(collectionBlock.getSeq().incrementAndGet());
            transferBak.setFrom(embedTransfer.getFrom());
            transferBak.setFromType(newAddressCache.getAddressType(embedTransfer.getFrom()).getCode());
            transferBak.setTo(embedTransfer.getTo());
            transferBak.setToType(newAddressCache.getAddressType(embedTransfer.getTo()).getCode());
            transferBak.setValue(embedTransfer.getAmount().toBigInteger().toString());
            transferBak.setHash(tx.getHash());
            transferBak.setBn(collectionBlock.getNum());
            transferBak.setbTime(tx.getTime());
            transferBakList.add(transferBak);
        }
        return transferBakList;
    }

}

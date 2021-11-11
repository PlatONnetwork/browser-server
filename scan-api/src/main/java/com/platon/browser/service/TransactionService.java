package com.platon.browser.service;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.platon.browser.bean.CustomStaking;
import com.platon.browser.bean.keybase.KeyBaseUserInfo;
import com.platon.browser.cache.TransactionCacheDto;
import com.platon.browser.config.BlockChainConfig;
import com.platon.browser.config.DownFileCommon;
import com.platon.browser.constant.Browser;
import com.platon.browser.dao.entity.*;
import com.platon.browser.dao.mapper.AddressMapper;
import com.platon.browser.dao.mapper.ProposalMapper;
import com.platon.browser.dao.mapper.StakingMapper;
import com.platon.browser.dao.mapper.TokenInventoryMapper;
import com.platon.browser.elasticsearch.dto.Block;
import com.platon.browser.elasticsearch.dto.DelegationReward;
import com.platon.browser.elasticsearch.dto.DelegationReward.Extra;
import com.platon.browser.elasticsearch.dto.ErcTx;
import com.platon.browser.elasticsearch.dto.Transaction;
import com.platon.browser.elasticsearch.dto.Transaction.StatusEnum;
import com.platon.browser.elasticsearch.dto.Transaction.ToTypeEnum;
import com.platon.browser.elasticsearch.dto.Transaction.TypeEnum;
import com.platon.browser.enums.AddressTypeEnum;
import com.platon.browser.enums.I18nEnum;
import com.platon.browser.enums.RedeemStatusEnum;
import com.platon.browser.enums.ReqTransactionTypeEnum;
import com.platon.browser.param.*;
import com.platon.browser.param.claim.Reward;
import com.platon.browser.request.PageReq;
import com.platon.browser.request.newtransaction.TransactionDetailsReq;
import com.platon.browser.request.newtransaction.TransactionListByAddressRequest;
import com.platon.browser.request.newtransaction.TransactionListByBlockRequest;
import com.platon.browser.request.staking.QueryClaimByStakingReq;
import com.platon.browser.response.RespPage;
import com.platon.browser.response.account.AccountDownload;
import com.platon.browser.response.staking.QueryClaimByStakingResp;
import com.platon.browser.response.transaction.*;
import com.platon.browser.service.elasticsearch.EsDelegationRewardRepository;
import com.platon.browser.service.elasticsearch.EsTransactionRepository;
import com.platon.browser.service.elasticsearch.bean.ESResult;
import com.platon.browser.service.elasticsearch.query.ESQueryBuilderConstructor;
import com.platon.browser.service.elasticsearch.query.ESQueryBuilders;
import com.platon.browser.utils.*;
import com.platon.utils.Convert;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 交易方法逻辑实现
 *
 * @author zhangrj
 * @file TransactionServiceImpl.java
 * @description
 * @data 2019年8月31日
 */
@Service
public class TransactionService {

    private final Logger logger = LoggerFactory.getLogger(TransactionService.class);

    @Resource
    private EsTransactionRepository ESTransactionRepository;

    @Resource
    private EsDelegationRewardRepository ESDelegationRewardRepository;

    @Resource
    private I18nUtil i18n;

    @Resource
    private StakingMapper stakingMapper;

    @Resource
    private ProposalMapper proposalMapper;

    @Resource
    private TokenInventoryMapper tokenInventoryMapper;

    @Resource
    private StatisticCacheService statisticCacheService;

    @Resource
    private BlockChainConfig blockChainConfig;

    @Resource
    private CommonService commonService;

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @Resource
    private DownFileCommon downFileCommon;

    @Resource
    private AddressMapper addressMapper;

    private static final String ERROR_TIPS = "获取区块错误。";

    @Value("${platon.valueUnit}")
    private String valueUnit;

    public RespPage<TransactionListResp> getTransactionList(PageReq req) {
        RespPage<TransactionListResp> result = new RespPage<>();
        /** 分页查询redis交易数据 */
        TransactionCacheDto transactionCacheDto = this.statisticCacheService.getTransactionCache(req.getPageNo(), req.getPageSize());
        List<Transaction> items = transactionCacheDto.getTransactionList();
        /**
         * 数据转换
         */
        List<TransactionListResp> lists = this.transferList(items);
        NetworkStat networkStat = this.statisticCacheService.getNetworkStatCache();
        result.init(lists, null == networkStat.getTxQty() ? 0 : networkStat.getTxQty(), transactionCacheDto.getPage().getTotalCount(), transactionCacheDto.getPage().getTotalPages());
        return result;
    }

    public RespPage<TransactionListResp> getTransactionListByBlock(TransactionListByBlockRequest req) {
        RespPage<TransactionListResp> result = new RespPage<>();
        ESQueryBuilderConstructor constructor = new ESQueryBuilderConstructor();
        constructor.must(new ESQueryBuilders().term("num", req.getBlockNumber()));
        ESResult<Transaction> items = new ESResult<>();
        if (req.getTxType() != null && !req.getTxType().isEmpty()) {
            constructor.must(new ESQueryBuilders().terms("type", ReqTransactionTypeEnum.getTxType(req.getTxType())));
        }
        constructor.setDesc("seq");
        constructor.setUnmappedType("long");
        constructor.setResult(new String[]{"hash", "time", "status", "from", "to", "value", "num", "type", "toType", "cost", "failReason"});
        /** 根据区块号和类型分页查询交易信息 */
        try {
            items = this.ESTransactionRepository.search(constructor, Transaction.class, req.getPageNo(), req.getPageSize());
        } catch (Exception e) {
            this.logger.error(ERROR_TIPS, e);
            return result;
        }
        List<TransactionListResp> lists = this.transferList(items.getRsData());
        /** 统计交易信息 */
        Page<?> page = new Page<>(req.getPageNo(), req.getPageSize());
        result.init(page, lists);
        result.setTotalCount(items.getTotal());
        return result;
    }

    public RespPage<TransactionListResp> getTransactionListByAddress(TransactionListByAddressRequest req) {
        RespPage<TransactionListResp> result = new RespPage<>();

        ESQueryBuilderConstructor constructor = new ESQueryBuilderConstructor();

        ESResult<Transaction> items = new ESResult<>();
        if (req.getTxType() != null && !req.getTxType().isEmpty()) {
            constructor.must(new ESQueryBuilders().terms("type", ReqTransactionTypeEnum.getTxType(req.getTxType())));
        }
        constructor.buildMust(new BoolQueryBuilder().should(QueryBuilders.termQuery("from", req.getAddress())).should(QueryBuilders.termQuery("to", req.getAddress())));
        constructor.setDesc("seq");
        constructor.setUnmappedType("long");
        constructor.setResult(new String[]{"hash", "time", "status", "from", "to", "value", "num", "type", "toType", "cost", "failReason"});
        try {
            items = this.ESTransactionRepository.search(constructor, Transaction.class, req.getPageNo(), req.getPageSize());
        } catch (Exception e) {
            this.logger.error(ERROR_TIPS, e);
            return result;
        }
        List<TransactionListResp> lists = this.transferList(items.getRsData());
        Page<?> page = new Page<>(req.getPageNo(), req.getPageSize());
        result.init(page, lists);
        result.setTotalCount(items.getTotal());
        return result;
    }

    private List<TransactionListResp> transferList(List<Transaction> items) {
        List<TransactionListResp> lists = new LinkedList<>();
        for (Transaction transaction : items) {
            TransactionListResp transactionListResp = new TransactionListResp();
            BeanUtils.copyProperties(transaction, transactionListResp);
            transactionListResp.setTxHash(transaction.getHash());
            transactionListResp.setActualTxCost(new BigDecimal(transaction.getCost()));
            transactionListResp.setBlockNumber(transaction.getNum());
            transactionListResp.setReceiveType(String.valueOf(transaction.getToType()));
            /**
             * wasm也是合约创建
             */
            if (transaction.getType() == TypeEnum.WASM_CONTRACT_CREATE.getCode()) {
                transactionListResp.setTxType(String.valueOf(TypeEnum.EVM_CONTRACT_CREATE.getCode()));
            } else {
                transactionListResp.setTxType(String.valueOf(transaction.getType()));
            }
            transactionListResp.setServerTime(new Date().getTime());
            transactionListResp.setTimestamp(transaction.getTime().getTime());
            transactionListResp.setValue(new BigDecimal(transaction.getValue()));
            /**
             * 失败信息国际化
             */
            I18nEnum i18nEnum = I18nEnum.getEnum("CODE" + transaction.getFailReason());
            if (i18nEnum != null) {
                transactionListResp.setFailReason(this.i18n.i(i18nEnum));
            }
            if (StatusEnum.FAILURE.getCode() == transaction.getStatus()) {
                transactionListResp.setTxReceiptStatus(0);
            } else {
                transactionListResp.setTxReceiptStatus(transaction.getStatus());
            }
            lists.add(transactionListResp);
        }
        return lists;
    }

    public AccountDownload transactionListByAddressDownload(String address, Long date, String local, String timeZone) {
        AccountDownload accountDownload = new AccountDownload();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date currentServerTime = new Date();
        this.logger.info("导出地址交易列表数据起始日期：{},结束日期：{}", dateFormat.format(date), dateFormat.format(currentServerTime));

        /** 限制最多导出3万条记录 */

        ESQueryBuilderConstructor constructor = new ESQueryBuilderConstructor();
        constructor.must(new ESQueryBuilders().range("time", new Date(date).getTime(), currentServerTime.getTime()));
        constructor.buildMust(new BoolQueryBuilder().should(QueryBuilders.termQuery("from", address)).should(QueryBuilders.termQuery("to", address)));
        ESResult<Transaction> items = new ESResult<>();
        constructor.setDesc("seq");
        constructor.setUnmappedType("long");
        constructor.setResult(new String[]{"hash", "time", "status", "from", "to", "value", "num", "type", "toType", "cost"});
        try {
            items = this.ESTransactionRepository.search(constructor, Transaction.class, 1, 30000);
        } catch (Exception e) {
            this.logger.error(ERROR_TIPS, e);
            return accountDownload;
        }
        List<Object[]> rows = new ArrayList<>();
        items.getRsData().forEach(transaction -> {
            /**
             * 判断是否为to地址 如果为to地址则导出报表为收入金额 如果为from地址则导出报表为支出金额
             */
            boolean toIsAddress = address.equals(transaction.getTo());
            String valueIn = toIsAddress ? transaction.getValue() : "0";
            String valueOut = !toIsAddress ? transaction.getValue() : "0";
            Object[] row = {transaction.getHash(), transaction.getNum(), DateUtil.timeZoneTransfer(transaction.getTime(), "0", timeZone),
                    /**
                     * 枚举类型名称需要对应
                    */
                    this.i18n.getMessageForStr(Transaction.TypeEnum.getEnum(transaction.getType()).toString(), local), transaction.getFrom(), transaction.getTo(),
                    /** 数值von转换成lat，并保留十八位精确度 */
                    HexUtil.append(EnergonUtil.format(Convert.fromVon(valueIn, Convert.Unit.KPVON).setScale(18, RoundingMode.DOWN), 18)), HexUtil.append(EnergonUtil.format(Convert.fromVon(valueOut,
                                                                                                                                                                                            Convert.Unit.KPVON)
                                                                                                                                                                                   .setScale(18,
                                                                                                                                                                                             RoundingMode.DOWN),
                                                                                                                                                                            18)), HexUtil.append(
                    EnergonUtil.format(Convert.fromVon(transaction.getCost(), Convert.Unit.KPVON).setScale(18, RoundingMode.DOWN), 18))};
            rows.add(row);
        });
        String[] headers = {this.i18n.i(I18nEnum.DOWNLOAD_ACCOUNT_CSV_HASH, local), this.i18n.i(I18nEnum.DOWNLOAD_BLOCK_CSV_NUMBER, local), this.i18n.i(I18nEnum.DOWNLOAD_BLOCK_CSV_TIMESTAMP,
                                                                                                                                                        local), this.i18n.i(I18nEnum.DOWNLOAD_ACCOUNT_CSV_TYPE,
                                                                                                                                                                            local), this.i18n.i(I18nEnum.DOWNLOAD_ACCOUNT_CSV_FROM,
                                                                                                                                                                                                local), this.i18n.i(
                I18nEnum.DOWNLOAD_ACCOUNT_CSV_TO,
                local), this.i18n.i(I18nEnum.DOWNLOAD_ACCOUNT_CSV_VALUE_IN, local) + "(" + valueUnit + ")", this.i18n.i(I18nEnum.DOWNLOAD_ACCOUNT_CSV_VALUE_OUT,
                                                                                                                        local) + "(" + valueUnit + ")", this.i18n.i(I18nEnum.DOWNLOAD_ACCOUNT_CSV_FEE,
                                                                                                                                                                    local) + "(" + valueUnit + ")"};
        return this.downFileCommon.writeDate("Transaction-" + address + "-" + date + ".CSV", rows, headers);
    }

    public TransactionDetailsResp transactionDetails(TransactionDetailsReq req) {
        TransactionDetailsResp resp = new TransactionDetailsResp();
        /** 根据hash查询具体的交易数据 */
        Transaction transaction = null;
        try {
            transaction = this.ESTransactionRepository.get(req.getTxHash(), Transaction.class);
        } catch (Exception e) {
            this.logger.error(ERROR_TIPS, e);
            return resp;
        }
        if (transaction != null) {
            BeanUtils.copyProperties(transaction, resp);
            resp.setActualTxCost(new BigDecimal(transaction.getCost()));
            resp.setBlockNumber(transaction.getNum());
            resp.setGasLimit(transaction.getGasLimit());
            resp.setGasUsed(transaction.getGasUsed());
            /**
             * wasm也是合约创建
             */
            if (transaction.getType() == TypeEnum.WASM_CONTRACT_CREATE.getCode()) {
                resp.setTxType(String.valueOf(TypeEnum.EVM_CONTRACT_CREATE.getCode()));
            } else {
                resp.setTxType(String.valueOf(transaction.getType()));
            }
            resp.setTxHash(transaction.getHash());
            resp.setTimestamp(transaction.getTime().getTime());
            resp.setServerTime(new Date().getTime());
            resp.setTxInfo(transaction.getInfo());
            resp.setGasPrice(new BigDecimal(transaction.getGasPrice()));
            resp.setValue(new BigDecimal(transaction.getValue()));
            /**
             * 设置合约类型
             */
            if (transaction.getToType() == ToTypeEnum.ACCOUNT.getCode()) {
                resp.setReceiveType("2");
            } else {
                resp.setReceiveType("1");
            }
            resp.setContractName(transaction.getMethod());
            /**
             * 失败信息国际化
             */
            String name = "CODE" + transaction.getFailReason();
            // 针对304013特殊处理
            if ("304013".equalsIgnoreCase(transaction.getFailReason()) && TypeEnum.DELEGATE_CREATE.getCode() == transaction.getType()) {
                name = "DELETEGATE_CODE304013";
            }
            I18nEnum i18nEnum = I18nEnum.getEnum(name);
            if (i18nEnum != null) {
                resp.setFailReason(this.i18n.i(i18nEnum));
            }
            if (StatusEnum.FAILURE.getCode() == transaction.getStatus()) {
                resp.setTxReceiptStatus(0);
            } else {
                resp.setTxReceiptStatus(transaction.getStatus());
            }
            List<Block> blocks = this.statisticCacheService.getBlockCache(0, 1);
            /** 确认区块数等于当前区块书减去交易区块数 */
            if (!blocks.isEmpty()) {
                resp.setConfirmNum(String.valueOf(blocks.get(0).getNum() - transaction.getNum()));
            } else {
                resp.setConfirmNum("0");
            }

            /** 如果数据值为null 则置为空 */
            if ("null".equals(transaction.getInfo())) {
                resp.setTxInfo("0x");
            }
            /*
             * "first":false,            //是否第一条记录
             * "last":true,              //是否最后一条记录
             */
            resp.setFirst(false);
            if (transaction.getId() == 1) {
                resp.setFirst(true);
            } else {
                /**
                 * 根据id查询是否有上一条数据交易数据
                 */
                ESQueryBuilderConstructor constructor = new ESQueryBuilderConstructor();
                constructor.must(new ESQueryBuilders().term("id", transaction.getId() - 1));
                constructor.setResult(new String[]{"hash"});
                ESResult<Transaction> first = new ESResult<>();
                try {
                    first = this.ESTransactionRepository.search(constructor, Transaction.class, 1, 1);
                } catch (Exception e) {
                    this.logger.error("获取交易错误。", e);
                    return resp;
                }
                if (first.getTotal() > 0l) {
                    resp.setPreHash(first.getRsData().get(0).getHash());
                }
            }

            resp.setLast(true);
            /**
             * 根据id查询是否有下一条
             */
            ESQueryBuilderConstructor constructor = new ESQueryBuilderConstructor();
            constructor.must(new ESQueryBuilders().term("id", transaction.getId() + 1));
            constructor.setResult(new String[]{"hash"});
            ESResult<Transaction> last = new ESResult<>();
            try {
                last = this.ESTransactionRepository.search(constructor, Transaction.class, 1, 1);
            } catch (Exception e) {
                this.logger.error("获取交易错误。", e);
                return resp;
            }
            if (last.getTotal() > 0) {
                resp.setLast(false);
                resp.setNextHash(last.getRsData().get(0).getHash());
            }

            String txInfo = transaction.getInfo();
            /** 根据不同交易类型判断逻辑 */
            if (StringUtils.isNotBlank(txInfo) && (!"null".equals(txInfo) && (!"{}".equals(txInfo)))) {
                switch (Transaction.TypeEnum.getEnum(transaction.getType())) {
                    /** 创建验证人 */
                    case STAKE_CREATE:
                        try {
                            StakeCreateParam createValidatorParam = JSON.parseObject(txInfo, StakeCreateParam.class);
                            resp.setBenefitAddr(createValidatorParam.getBenefitAddress());
                            resp.setNodeId(createValidatorParam.getNodeId());
                            resp.setNodeName(createValidatorParam.getNodeName());
                            resp.setExternalId(createValidatorParam.getExternalId());
                            resp.setWebsite(createValidatorParam.getWebsite());
                            resp.setDetails(createValidatorParam.getDetails());
                            resp.setProgramVersion(createValidatorParam.getProgramVersion().toString());
                            resp.setTxAmount(createValidatorParam.getAmount());
                            resp.setExternalUrl(this.getStakingUrl(createValidatorParam.getExternalId(), resp.getTxReceiptStatus()));
                            resp.setDelegationRatio(new BigDecimal(createValidatorParam.getDelegateRewardPer()).divide(Browser.PERCENTAGE).toString());
                        } catch (Exception e) {
                            logger.error("创建验证人交易信息解析异常", e);
                        }
                        break;
                    /**
                     * 编辑验证人
                     */
                    case STAKE_MODIFY:
                        try {
                            StakeModifyParam editValidatorParam = JSON.parseObject(txInfo, StakeModifyParam.class);
                            resp.setBenefitAddr(editValidatorParam.getBenefitAddress());
                            resp.setNodeId(editValidatorParam.getNodeId());
                            resp.setExternalId(editValidatorParam.getExternalId());
                            resp.setWebsite(editValidatorParam.getWebsite());
                            resp.setDetails(editValidatorParam.getDetails());
                            resp.setNodeName(this.commonService.getNodeName(editValidatorParam.getNodeId(), editValidatorParam.getNodeName()));
                            resp.setExternalUrl(this.getStakingUrl(editValidatorParam.getExternalId(), resp.getTxReceiptStatus()));
                            String delegationRatio = null;
                            if (editValidatorParam.getDelegateRewardPer() != null) {
                                delegationRatio = new BigDecimal(editValidatorParam.getDelegateRewardPer()).divide(Browser.PERCENTAGE).toString();
                            }
                            resp.setDelegationRatio(delegationRatio);
                        } catch (Exception e) {
                            logger.error("编辑验证人交易信息解析异常", e);
                        }
                        break;
                    /**
                     * 增加质押
                     */
                    case STAKE_INCREASE:
                        try {
                            StakeIncreaseParam increaseStakingParam = JSON.parseObject(txInfo, StakeIncreaseParam.class);
                            resp.setNodeId(increaseStakingParam.getNodeId());
                            resp.setTxAmount(increaseStakingParam.getAmount());
                            /**
                             * 节点名称设置
                             */
                            resp.setNodeName(this.commonService.getNodeName(increaseStakingParam.getNodeId(), increaseStakingParam.getNodeName()));
                        } catch (Exception e) {
                            logger.error("增加质押交易信息解析异常", e);
                        }
                        break;
                    /**
                     * 退出验证人
                     */
                    case STAKE_EXIT:
                        try {
                            // nodeId + nodeName + applyAmount + redeemLocked + redeemStatus + redeemUnLockedBlock
                            StakeExitParam exitValidatorParam = JSON.parseObject(txInfo, StakeExitParam.class);
                            resp.setNodeId(exitValidatorParam.getNodeId());
                            resp.setNodeName(this.commonService.getNodeName(exitValidatorParam.getNodeId(), exitValidatorParam.getNodeName()));
                            resp.setApplyAmount(exitValidatorParam.getAmount());
                            StakingKey stakingKeyE = new StakingKey();
                            stakingKeyE.setNodeId(exitValidatorParam.getNodeId());
                            stakingKeyE.setStakingBlockNum(exitValidatorParam.getStakingBlockNum().longValue());
                            Staking staking = this.stakingMapper.selectByPrimaryKey(stakingKeyE);
                            if (staking != null) {
                                resp.setRedeemLocked(staking.getStakingReduction());
                                // 只有已退出，则金额才会退回到账户
                                if (staking.getStatus() == CustomStaking.StatusEnum.EXITED.getCode()) {
                                    resp.setRedeemStatus(RedeemStatusEnum.EXITED.getCode());
                                } else {
                                    resp.setRedeemStatus(RedeemStatusEnum.EXITING.getCode());
                                }
                                resp.setRedeemUnLockedBlock(exitValidatorParam.getWithdrawBlockNum().toString());
                            }
                        } catch (Exception e) {
                            logger.error("退出验证人交易信息解析异常", e);
                        }
                        break;
                    /**
                     * 委托
                     */
                    case DELEGATE_CREATE:
                        try {
                            DelegateCreateParam delegateParam = JSON.parseObject(txInfo, DelegateCreateParam.class);
                            resp.setNodeId(delegateParam.getNodeId());
                            resp.setTxAmount(delegateParam.getAmount());
                            resp.setNodeName(this.commonService.getNodeName(delegateParam.getNodeId(), delegateParam.getNodeName()));
                        } catch (Exception e) {
                            logger.error("委托交易信息解析异常", e);
                        }
                        break;
                    /**
                     * 委托赎回
                     */
                    case DELEGATE_EXIT:
                        try {
                            // nodeId + nodeName + applyAmount + redeemLocked + redeemStatus
                            // 通过txHash关联un_delegation表
                            DelegateExitParam unDelegateParam = JSON.parseObject(txInfo, DelegateExitParam.class);
                            resp.setNodeId(unDelegateParam.getNodeId());
                            resp.setApplyAmount(unDelegateParam.getRealAmount());
                            resp.setTxAmount(unDelegateParam.getReward());
                            resp.setNodeName(this.commonService.getNodeName(unDelegateParam.getNodeId(), unDelegateParam.getNodeName()));
                        } catch (Exception e) {
                            logger.error("委托赎回交易信息解析异常", e);
                        }
                        break;
                    /**
                     * 文本提案
                     */
                    case PROPOSAL_TEXT:
                        try {
                            ProposalTextParam createProposalTextParam = JSON.parseObject(txInfo, ProposalTextParam.class);
                            if (StringUtils.isNotBlank(createProposalTextParam.getPIDID())) {
                                resp.setPipNum("PIP-" + createProposalTextParam.getPIDID());
                            }
                            resp.setNodeId(createProposalTextParam.getVerifier());
                            resp.setProposalHash(req.getTxHash());
                            resp.setNodeName(this.commonService.getNodeName(createProposalTextParam.getVerifier(), createProposalTextParam.getNodeName()));
                            /** 如果数据库有值，以数据库为准 */
                            this.transferTransaction(resp, req.getTxHash());
                        } catch (Exception e) {
                            logger.error("文本提案交易信息解析异常", e);
                        }
                        break;
                    /**
                     * 升级提案
                     */
                    case PROPOSAL_UPGRADE:
                        try {
                            ProposalUpgradeParam createProposalUpgradeParam = JSON.parseObject(txInfo, ProposalUpgradeParam.class);
                            resp.setProposalNewVersion(String.valueOf(createProposalUpgradeParam.getNewVersion()));
                            if (StringUtils.isNotBlank(createProposalUpgradeParam.getPIDID())) {
                                resp.setPipNum("PIP-" + createProposalUpgradeParam.getPIDID());
                            }
                            resp.setNodeId(createProposalUpgradeParam.getVerifier());
                            resp.setProposalHash(req.getTxHash());
                            resp.setNodeName(this.commonService.getNodeName(createProposalUpgradeParam.getVerifier(), createProposalUpgradeParam.getNodeName()));
                            /** 如果数据库有值，以数据库为准 */
                            this.transferTransaction(resp, req.getTxHash());
                        } catch (Exception e) {
                            logger.error("升级提案交易信息解析异常", e);
                        }
                        break;
                    /**
                     * 参数提案
                     */
                    case PROPOSAL_PARAMETER:
                        try {
                            ProposalParameterParam proposalParameterParam = JSON.parseObject(txInfo, ProposalParameterParam.class);
                            if (StringUtils.isNotBlank(proposalParameterParam.getPIDID())) {
                                resp.setPipNum("PIP-" + proposalParameterParam.getPIDID());
                            }
                            resp.setNodeId(proposalParameterParam.getVerifier());
                            resp.setProposalHash(req.getTxHash());
                            resp.setNodeName(this.commonService.getNodeName(proposalParameterParam.getVerifier(), proposalParameterParam.getNodeName()));
                            /** 如果数据库有值，以数据库为准 */
                            this.transferTransaction(resp, req.getTxHash());
                        } catch (Exception e) {
                            logger.error("参数提案交易信息解析异常", e);
                        }
                        break;
                    /**
                     * 取消提案
                     */
                    case PROPOSAL_CANCEL:
                        try {
                            ProposalCancelParam cancelProposalParam = JSON.parseObject(txInfo, ProposalCancelParam.class);
                            if (StringUtils.isNotBlank(cancelProposalParam.getPIDID())) {
                                resp.setPipNum("PIP-" + cancelProposalParam.getPIDID());
                            }
                            resp.setNodeId(cancelProposalParam.getVerifier());
                            resp.setProposalHash(req.getTxHash());
                            resp.setNodeName(this.commonService.getNodeName(cancelProposalParam.getVerifier(), cancelProposalParam.getNodeName()));
                            /** 如果数据库有值，以数据库为准 */
                            this.transferTransaction(resp, req.getTxHash());
                        } catch (Exception e) {
                            logger.error("取消提案交易信息解析异常", e);
                        }
                        break;
                    /**
                     * 提案投票
                     */
                    case PROPOSAL_VOTE:
                        try {
                            // nodeId + nodeName + txType + proposalUrl + proposalHash + proposalNewVersion + proposalOption
                            ProposalVoteParam votingProposalParam = JSON.parseObject(txInfo, ProposalVoteParam.class);
                            resp.setNodeId(votingProposalParam.getVerifier());
                            resp.setProposalOption(votingProposalParam.getProposalType());
                            resp.setProposalHash(votingProposalParam.getProposalId());
                            resp.setProposalNewVersion(votingProposalParam.getProgramVersion());
                            resp.setNodeName(this.commonService.getNodeName(votingProposalParam.getVerifier(), votingProposalParam.getNodeName()));
                            if (StringUtils.isNotBlank(votingProposalParam.getPIDID())) {
                                resp.setPipNum("PIP-" + votingProposalParam.getPIDID());
                            }
                            resp.setVoteStatus(votingProposalParam.getOption());
                            /**
                             * 获取提案信息
                             */
                            Proposal proposal = this.proposalMapper.selectByPrimaryKey(votingProposalParam.getProposalId());
                            if (proposal != null) {
                                resp.setPipNum(proposal.getPipNum());
                                resp.setProposalTitle(Browser.INQUIRY.equals(proposal.getTopic()) ? "" : proposal.getTopic());
                                resp.setProposalUrl(proposal.getUrl());
                                resp.setProposalOption(String.valueOf(proposal.getType()));
                            }
                        } catch (Exception e) {
                            logger.error("提案投票交易信息解析异常", e);
                        }
                        break;
                    /**
                     * 版本申明
                     */
                    case VERSION_DECLARE:
                        try {
                            VersionDeclareParam declareVersionParam = JSON.parseObject(txInfo, VersionDeclareParam.class);
                            resp.setNodeId(declareVersionParam.getActiveNode());
                            resp.setDeclareVersion(String.valueOf(declareVersionParam.getVersion()));
                            resp.setNodeName(this.commonService.getNodeName(declareVersionParam.getActiveNode(), declareVersionParam.getNodeName()));
                        } catch (Exception e) {
                            logger.error("版本申明交易信息解析异常", e);
                        }
                        break;
                    /**
                     * 举报双签
                     */
                    case REPORT:
                        try {
                            ReportParam reportValidatorParam = JSON.parseObject(txInfo, ReportParam.class);
                            List<TransactionDetailsEvidencesResp> transactionDetailsEvidencesResps = new ArrayList<>();
                            TransactionDetailsEvidencesResp transactionDetailsEvidencesResp = new TransactionDetailsEvidencesResp();
                            transactionDetailsEvidencesResp.setVerify(reportValidatorParam.getVerify());
                            transactionDetailsEvidencesResp.setNodeName(this.commonService.getNodeName(reportValidatorParam.getVerify(), reportValidatorParam.getNodeName()));
                            resp.setEvidence(reportValidatorParam.getData());
                            transactionDetailsEvidencesResps.add(transactionDetailsEvidencesResp);
                            /**
                             * 查看举报之后是否退出来判断是否交易正确
                             */
                            resp.setReportStatus(transaction.getStatus() == 1 ? 2 : 1);
                            resp.setReportRewards(transaction.getStatus() == StatusEnum.SUCCESS.getCode() ? reportValidatorParam.getReward() : BigDecimal.ZERO);
                            resp.setReportType(reportValidatorParam.getType().intValue());
                            resp.setEvidences(transactionDetailsEvidencesResps);
                        } catch (Exception e) {
                            logger.error("举报双签交易信息解析异常", e);
                        }
                        break;
                    /**
                     * 创建锁仓
                     */
                    case RESTRICTING_CREATE:
                        try {
                            // RPAccount + value + RPPlan
                            RestrictingCreateParam createRestrictingParam = JSON.parseObject(txInfo, RestrictingCreateParam.class);
                            List<TransactionDetailsRPPlanResp> rpPlanResps = new ArrayList<>();
                            resp.setRPAccount(createRestrictingParam.getAccount());
                            BigDecimal amountSum = new BigDecimal(0);
                            for (RestrictingCreateParam.RestrictingPlan p : createRestrictingParam.getPlans()) {
                                TransactionDetailsRPPlanResp transactionDetailsRPPlanResp = new TransactionDetailsRPPlanResp();
                                amountSum = amountSum.add(p.getAmount());
                                transactionDetailsRPPlanResp.setAmount(p.getAmount());
                                transactionDetailsRPPlanResp.setEpoch(String.valueOf(p.getEpoch()));
                                /**
                                 * 锁仓周期对应快高 结算周期数 * epoch + number,如果不是整数倍则为：结算周期 * （epoch-1） + 多余的数目
                                 */
                                BigInteger number;
                                long remainder = transaction.getNum() % this.blockChainConfig.getSettlePeriodBlockCount().longValue();
                                if (remainder == 0l) {
                                    number = this.blockChainConfig.getSettlePeriodBlockCount().multiply(p.getEpoch()).add(BigInteger.valueOf(transaction.getNum()));
                                } else {
                                    number = this.blockChainConfig.getSettlePeriodBlockCount()
                                                                  .multiply(p.getEpoch().subtract(BigInteger.ONE))
                                                                  .add(BigInteger.valueOf(transaction.getNum()))
                                                                  .add(this.blockChainConfig.getSettlePeriodBlockCount().subtract(BigInteger.valueOf(remainder)));
                                }
                                transactionDetailsRPPlanResp.setBlockNumber(String.valueOf(number));
                                rpPlanResps.add(transactionDetailsRPPlanResp);
                            }
                            // 累加
                            resp.setRPNum(amountSum);
                            resp.setRPPlan(rpPlanResps);
                        } catch (Exception e) {
                            logger.error("创建锁仓交易信息解析异常", e);
                        }
                        break;
                    /**
                     * 领取奖励
                     */
                    case CLAIM_REWARDS:
                        try {
                            DelegateRewardClaimParam delegateRewardClaimParam = JSON.parseObject(txInfo, DelegateRewardClaimParam.class);
                            List<TransactionDetailsRewardsResp> rewards = new ArrayList<>();
                            BigDecimal rewardSum = BigDecimal.ZERO;
                            Map<String, Reward> rewrdsMap = new HashMap<String, Reward>();
                            /**
                             * 对相同的nodeId的奖励需要进行累加，不同的可以直接设置返回
                             */
                            for (Reward reward : delegateRewardClaimParam.getRewardList()) {
                                if (rewrdsMap.containsKey(reward.getNodeId())) {
                                    Reward reward2 = rewrdsMap.get(reward.getNodeId());
                                    reward2.setReward(reward2.getReward().add(reward.getReward()));
                                } else {
                                    rewrdsMap.put(reward.getNodeId(), reward);
                                }
                            }
                            for (String nodeId : rewrdsMap.keySet()) {
                                Reward reward = rewrdsMap.get(nodeId);
                                TransactionDetailsRewardsResp transactionDetailsRewardsResp = new TransactionDetailsRewardsResp();
                                transactionDetailsRewardsResp.setVerify(reward.getNodeId());
                                transactionDetailsRewardsResp.setNodeName(reward.getNodeName());
                                transactionDetailsRewardsResp.setReward(reward.getReward());
                                rewardSum = rewardSum.add(reward.getReward());
                                rewards.add(transactionDetailsRewardsResp);
                            }
                            resp.setTxAmount(rewardSum);
                            resp.setRewards(rewards);
                        } catch (Exception e) {
                            logger.error("领取奖励交易信息解析异常", e);
                        }
                        break;
                }
            }
            //补充填充合约的相关数据
            switch (Transaction.TypeEnum.getEnum(transaction.getType())) {
                /**
                 * 合约
                 */
                case EVM_CONTRACT_CREATE:
                case WASM_CONTRACT_CREATE:
                case ERC20_CONTRACT_CREATE:
                case ERC721_CONTRACT_CREATE:
                    /**
                     * to地址设置为合约地址
                     */
                    resp.setTo(transaction.getContractAddress());
                    resp.setTxInfo(transaction.getInput());
                    break;
                case CONTRACT_EXEC:
                case ERC20_CONTRACT_EXEC:
                case ERC721_CONTRACT_EXEC:
                    //获取arc20交易进行转换
                    List<ErcTx> erc20List = JSONObject.parseArray(transaction.getErc20TxInfo(), ErcTx.class);
                    if (erc20List != null) {
                        List<Arc20Param> arc20Params = new ArrayList<>();
                        erc20List.forEach(erc20 -> {
                            // 精度转换
                            int decimal = Integer.parseInt(String.valueOf(erc20.getDecimal()));
                            BigDecimal afterConverValue = ConvertUtil.convertByFactor(new BigDecimal(erc20.getValue()), decimal);
                            Arc20Param arc20Param = Arc20Param.builder()
                                                              .innerContractAddr(erc20.getContract())
                                                              .innerContractName(erc20.getName())
                                                              .innerDecimal(String.valueOf(erc20.getDecimal()))
                                                              .innerFrom(erc20.getFrom())
                                                              .fromType(erc20.getFromType())
                                                              .innerSymbol(erc20.getSymbol())
                                                              .innerTo(erc20.getTo())
                                                              .toType(erc20.getToType())
                                                              .innerValue(afterConverValue.toString())
                                                              .build();
                            arc20Params.add(arc20Param);
                        });
                        resp.setErc20Params(arc20Params);
                    }
                    //获取arc721交易进行转换
                    List<ErcTx> erc721List = JSONObject.parseArray(transaction.getErc721TxInfo(), ErcTx.class);
                    if (erc721List != null) {
                        List<Arc721Param> arc721Params = new ArrayList<>();
                        erc721List.forEach(erc721 -> {
                            // 精度转换
                            int decimal = Integer.parseInt(String.valueOf(erc721.getDecimal()));
                            BigDecimal afterConverValue = ConvertUtil.convertByFactor(new BigDecimal(erc721.getValue()), decimal);
                            Arc721Param arc721Param = Arc721Param.builder()
                                                                 .innerContractAddr(erc721.getContract())
                                                                 .innerContractName(erc721.getName())
                                                                 .innerDecimal(String.valueOf(erc721.getDecimal()))
                                                                 .innerFrom(erc721.getFrom())
                                                                 .fromType(erc721.getFromType())
                                                                 .innerSymbol(erc721.getSymbol())
                                                                 .innerTo(erc721.getTo())
                                                                 .toType(erc721.getToType())
                                                                 .innerValue(afterConverValue.toString())
                                                                 .build();
                            //查询对应的图片进行回填
                            TokenInventoryKey tokenInventoryKey = new TokenInventoryKey();
                            tokenInventoryKey.setTokenAddress(erc721.getContract());
                            tokenInventoryKey.setTokenId(erc721.getValue());
                            TokenInventory tokenInventory = tokenInventoryMapper.selectByPrimaryKey(tokenInventoryKey);
                            if (tokenInventory != null) {
                                // 默认取中等缩略图
                                String image = "";
                                if (StrUtil.isNotEmpty(tokenInventory.getMediumImage())) {
                                    image = tokenInventory.getMediumImage();
                                } else {
                                    image = tokenInventory.getImage();
                                }
                                arc721Param.setInnerImage(image);
                            }
                            arc721Params.add(arc721Param);
                        });
                        resp.setErc721Params(arc721Params);
                    }
                    resp.setTxInfo(transaction.getInput());
                    break;
            }
        }
        return resp;
    }

    /**
     * 提案信息统一转换
     *
     * @param resp
     * @param hash
     * @return
     * @method transferTransaction
     */
    private TransactionDetailsResp transferTransaction(TransactionDetailsResp resp, String hash) {
        Proposal proposal = this.proposalMapper.selectByPrimaryKey(hash);
        if (proposal != null) {
            resp.setNodeId(proposal.getNodeId());
            resp.setNodeName(proposal.getNodeName());
            resp.setPipNum(proposal.getPipNum());
            resp.setProposalTitle(Browser.INQUIRY.equals(proposal.getTopic()) ? "" : proposal.getTopic());
            resp.setProposalStatus(proposal.getStatus());
            resp.setProposalOption(String.valueOf(proposal.getType()));
            resp.setProposalNewVersion(proposal.getNewVersion());
            resp.setProposalUrl(proposal.getUrl());
        }
        return resp;
    }

    /**
     * 统一设置验证人keybaseurl
     *
     * @param externalId
     * @param txReceiptStatus
     * @return
     * @method getStakingUrl
     */
    private String getStakingUrl(String externalId, Integer txReceiptStatus) {

        String keyBaseUrl = this.blockChainConfig.getKeyBase();
        String keyBaseApi = this.blockChainConfig.getKeyBaseApi();
        String defaultBaseUrl = this.blockChainConfig.getKeyBase();
        /**
         * 如果externalId为空就不返回给前端url，反转跳转
         */
        if (StringUtils.isNotBlank(externalId)) {
            /**
             * 如果为失败的交易直接设置默认的url然后跳出
             */
            if (txReceiptStatus == Transaction.StatusEnum.FAILURE.getCode()) {
                return defaultBaseUrl;
            }
            /**
             * 检查redis是否已经存储
             */
            String userName = redisTemplate.opsForValue().get(externalId);
            if (StringUtils.isNotBlank(userName)) {
                defaultBaseUrl += userName;
                return defaultBaseUrl;
            }
            String url = keyBaseUrl.concat(keyBaseApi.concat(externalId));
            try {
                KeyBaseUserInfo keyBaseUser = HttpUtil.get(url, KeyBaseUserInfo.class);
                userName = KeyBaseAnalysis.getKeyBaseUseName(keyBaseUser);
            } catch (Exception e) {
                this.logger.error("getStakingUrl error.externalId:{},txReceiptStatus:{},error:{}", externalId, txReceiptStatus, e.getMessage());
                return defaultBaseUrl;
            }
            if (StringUtils.isNotBlank(userName)) {
                /**
                 * 设置redis
                 */
                redisTemplate.opsForValue().set(externalId, userName);
                defaultBaseUrl += userName;
            }
            return defaultBaseUrl;
        }
        return null;
    }

    public RespPage<QueryClaimByAddressResp> queryClaimByAddress(TransactionListByAddressRequest req) {
        RespPage<QueryClaimByAddressResp> result = new RespPage<>();
        /** 根据地址查询具体的领取奖励数据 */
        ESQueryBuilderConstructor constructor = new ESQueryBuilderConstructor();
        constructor.must(new ESQueryBuilders().term("addr", req.getAddress()));
        constructor.setDesc("time");
        ESResult<DelegationReward> delegationRewards = null;
        try {
            delegationRewards = this.ESDelegationRewardRepository.search(constructor, DelegationReward.class, req.getPageNo(), req.getPageSize());
        } catch (Exception e) {
            this.logger.error(ERROR_TIPS, e);
            return result;
        }
        if (delegationRewards == null) {
            // 防止空指异常
            delegationRewards = new ESResult<>();
            delegationRewards.setTotal(0L);
            delegationRewards.setRsData(Collections.emptyList());
        }
        List<QueryClaimByAddressResp> queryClaimByAddressResps = new ArrayList<>();
        for (DelegationReward delegationReward : delegationRewards.getRsData()) {
            QueryClaimByAddressResp queryClaimByAddressResp = new QueryClaimByAddressResp();
            queryClaimByAddressResp.setTxHash(delegationReward.getHash());
            queryClaimByAddressResp.setTimestamp(delegationReward.getTime().getTime());
            List<TransactionDetailsRewardsResp> rewardsDetails = new ArrayList<>();
            /**
             * 解析json获取具体提取奖励的数据
             */
            List<Extra> extras = JSONObject.parseArray(delegationReward.getExtra(), DelegationReward.Extra.class);
            BigDecimal allRewards = BigDecimal.ZERO;
            /**
             * 设置每一个领取奖励从哪个节点上获取
             */
            Map<String, Extra> rewrdsMap = new HashMap<String, Extra>();
            for (Extra extra : extras) {
                /**
                 * 对相同的nodeId的奖励需要进行累加，不同的可以直接设置返回
                 */
                if (rewrdsMap.containsKey(extra.getNodeId())) {
                    Extra reward2 = rewrdsMap.get(extra.getNodeId());
                    reward2.setReward(new BigDecimal(reward2.getReward()).add(new BigDecimal(reward2.getReward())).toString());
                } else {
                    rewrdsMap.put(extra.getNodeId(), extra);
                }
            }
            for (String nodeId : rewrdsMap.keySet()) {
                Extra extra = rewrdsMap.get(nodeId);
                TransactionDetailsRewardsResp transactionDetailsRewardsResp = new TransactionDetailsRewardsResp();
                transactionDetailsRewardsResp.setVerify(extra.getNodeId());
                transactionDetailsRewardsResp.setNodeName(extra.getNodeName());
                transactionDetailsRewardsResp.setReward(new BigDecimal(extra.getReward()));
                allRewards = allRewards.add(new BigDecimal(extra.getReward()));
                rewardsDetails.add(transactionDetailsRewardsResp);
            }
            queryClaimByAddressResp.setRewardsDetails(rewardsDetails);
            /**
             * 根据交易累加所有的奖励
             */
            queryClaimByAddressResp.setAllRewards(allRewards);
            queryClaimByAddressResps.add(queryClaimByAddressResp);
        }

        result.init(queryClaimByAddressResps, delegationRewards.getTotal(), delegationRewards.getTotal(), 0l);
        return result;
    }

    public RespPage<QueryClaimByStakingResp> queryClaimByStaking(QueryClaimByStakingReq req) {
        /** 根据地址查询具体的领取奖励数据 */
        ESQueryBuilderConstructor constructor = new ESQueryBuilderConstructor();
        constructor.must(new ESQueryBuilders().fuzzy("extraClean", req.getNodeId()));
        constructor.setDesc("time");
        ESResult<DelegationReward> delegationRewards = null;
        try {
            delegationRewards = this.ESDelegationRewardRepository.search(constructor, DelegationReward.class, req.getPageNo(), req.getPageSize());
        } catch (Exception e) {
            this.logger.error(ERROR_TIPS, e);
        }
        if (delegationRewards == null) {
            // 防止空指异常
            delegationRewards = new ESResult<>();
            delegationRewards.setTotal(0L);
            delegationRewards.setRsData(Collections.emptyList());
        }
        List<QueryClaimByStakingResp> queryClaimByStakingResps = new ArrayList<>();
        for (DelegationReward delegationReward : delegationRewards.getRsData()) {
            QueryClaimByStakingResp queryClaimByStakingResp = new QueryClaimByStakingResp();
            BeanUtils.copyProperties(delegationReward, queryClaimByStakingResp);
            Address address = addressMapper.selectByPrimaryKey(queryClaimByStakingResp.getAddr());
            queryClaimByStakingResp.setAddrType(CommonUtil.ofNullable(() -> address.getType()).orElse(AddressTypeEnum.ACCOUNT.getCode()));
            queryClaimByStakingResp.setTime(delegationReward.getTime().getTime());
            /**
             * 解析json获取具体提取奖励的数据
             */
            List<Extra> extras = JSONObject.parseArray(delegationReward.getExtra(), DelegationReward.Extra.class);
            BigDecimal allRewards = BigDecimal.ZERO;
            /**
             * 累积交易的所有领取奖励
             */
            for (Extra extra : extras) {
                /**
                 * 只有地址相同的才需要累积
                 */
                if (req.getNodeId().equals(extra.getNodeId())) {
                    allRewards = allRewards.add(new BigDecimal(extra.getReward()));
                }
            }
            /**
             * 根据交易累加所有的奖励
             */
            queryClaimByStakingResp.setReward(allRewards);
            queryClaimByStakingResps.add(queryClaimByStakingResp);
        }
        RespPage<QueryClaimByStakingResp> result = new RespPage<>();
        result.init(queryClaimByStakingResps, delegationRewards.getTotal(), delegationRewards.getTotal(), 0l);
        return result;
    }

}

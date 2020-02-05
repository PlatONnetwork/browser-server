package com.platon.browser.now.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.platon.browser.common.BrowserConst;
import com.platon.browser.config.BlockChainConfig;
import com.platon.browser.config.RedisFactory;
import com.platon.browser.dao.entity.*;
import com.platon.browser.dao.mapper.ProposalMapper;
import com.platon.browser.dao.mapper.SlashMapper;
import com.platon.browser.dao.mapper.StakingMapper;
import com.platon.browser.dto.CustomStaking;
import com.platon.browser.dto.account.AccountDownload;
import com.platon.browser.dto.elasticsearch.ESResult;
import com.platon.browser.dto.keybase.KeyBaseUser;
import com.platon.browser.dto.transaction.TransactionCacheDto;
import com.platon.browser.elasticsearch.DelegationRewardESRepository;
import com.platon.browser.elasticsearch.TransactionESRepository;
import com.platon.browser.elasticsearch.dto.Block;
import com.platon.browser.elasticsearch.dto.DelegationReward;
import com.platon.browser.elasticsearch.dto.DelegationReward.Extra;
import com.platon.browser.elasticsearch.dto.Transaction;
import com.platon.browser.elasticsearch.dto.Transaction.StatusEnum;
import com.platon.browser.elasticsearch.service.impl.ESQueryBuilderConstructor;
import com.platon.browser.elasticsearch.service.impl.ESQueryBuilders;
import com.platon.browser.enums.I18nEnum;
import com.platon.browser.enums.RedeemStatusEnum;
import com.platon.browser.enums.ReqTransactionTypeEnum;
import com.platon.browser.now.service.CommonService;
import com.platon.browser.now.service.TransactionService;
import com.platon.browser.now.service.cache.StatisticCacheService;
import com.platon.browser.param.*;
import com.platon.browser.param.claim.Reward;
import com.platon.browser.req.PageReq;
import com.platon.browser.req.newtransaction.TransactionDetailsReq;
import com.platon.browser.req.newtransaction.TransactionListByAddressRequest;
import com.platon.browser.req.newtransaction.TransactionListByBlockRequest;
import com.platon.browser.req.staking.QueryClaimByStakingReq;
import com.platon.browser.res.RespPage;
import com.platon.browser.res.staking.QueryClaimByStakingResp;
import com.platon.browser.res.transaction.QueryClaimByAddressResp;
import com.platon.browser.res.transaction.TransactionDetailsEvidencesResp;
import com.platon.browser.res.transaction.TransactionDetailsRPPlanResp;
import com.platon.browser.res.transaction.TransactionDetailsResp;
import com.platon.browser.res.transaction.TransactionDetailsRewardsResp;
import com.platon.browser.res.transaction.TransactionListResp;
import com.platon.browser.util.*;
import com.platon.browser.utils.HexTool;
import com.univocity.parsers.csv.CsvWriter;
import com.univocity.parsers.csv.CsvWriterSettings;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.utils.Convert;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 交易方法逻辑实现
 *  @file TransactionServiceImpl.java
 *  @description
 *	@author zhangrj
 *  @data 2019年8月31日
 */
@Service
public class TransactionServiceImpl implements TransactionService {

    private final Logger logger = LoggerFactory.getLogger(TransactionServiceImpl.class);
    @Autowired
    private TransactionESRepository transactionESRepository;
    @Autowired
    private DelegationRewardESRepository delegationRewardESRepository;
    @Autowired
    private I18nUtil i18n;
    @Autowired
    private StakingMapper stakingMapper;
    @Autowired
    private SlashMapper slashMapper;
    @Autowired
    private ProposalMapper proposalMapper;
    @Autowired
    private StatisticCacheService statisticCacheService;
    @Autowired
    private BlockChainConfig blockChainConfig;
    @Autowired
	private CommonService commonService;
    @Autowired
    private RedisFactory redisFactory;
    private static final String ERROR_TIPS="获取区块错误。";

    @Override
    public RespPage<TransactionListResp> getTransactionList( PageReq req) {
        RespPage<TransactionListResp> result = new RespPage<>();
        /** 分页查询redis交易数据 */
        TransactionCacheDto transactionCacheDto = statisticCacheService.getTransactionCache(req.getPageNo(), req.getPageSize());
        List<Transaction> items = transactionCacheDto.getTransactionList();
        /**
         * 数据转换
         */
        List<TransactionListResp> lists = this.transferList(items);
        NetworkStat networkStat = statisticCacheService.getNetworkStatCache();
        result.init(lists, networkStat.getTxQty(), transactionCacheDto.getPage().getTotalCount(), transactionCacheDto.getPage().getTotalPages());
        return result;
    }

    @Override
    public RespPage<TransactionListResp> getTransactionListByBlock(TransactionListByBlockRequest req) {
        RespPage<TransactionListResp> result = new RespPage<>();
        ESQueryBuilderConstructor constructor = new ESQueryBuilderConstructor();
		constructor.must(new ESQueryBuilders().term("num", req.getBlockNumber()));
		ESResult<Transaction> items = new ESResult<>();
		if (req.getTxType() != null && !req.getTxType().isEmpty()) {
			constructor.must(new ESQueryBuilders().terms("type", ReqTransactionTypeEnum.getTxType(req.getTxType())));
		}
		constructor.setDesc("seq");
		/** 根据区块号和类型分页查询交易信息 */
		try {
			items = transactionESRepository.search(constructor, Transaction.class, req.getPageNo(),req.getPageSize());
		} catch (IOException e) {
			logger.error(ERROR_TIPS, e);
		}
        List<TransactionListResp> lists = this.transferList(items.getRsData());
        /** 统计交易信息 */
        Page<?> page = new Page<>(req.getPageNo(),req.getPageSize());
        result.init(page, lists);
        result.setTotalCount(items.getTotal());
        return result;
    }

    @Override
    public RespPage<TransactionListResp> getTransactionListByAddress(TransactionListByAddressRequest req) {
        RespPage<TransactionListResp> result = new RespPage<>();
		
		ESQueryBuilderConstructor constructor = new ESQueryBuilderConstructor();
		
		ESResult<Transaction> items = new ESResult<>();
		if (req.getTxType() != null && !req.getTxType().isEmpty()) {
			constructor.must(new ESQueryBuilders().terms("type", ReqTransactionTypeEnum.getTxType(req.getTxType())));
		}
		constructor.buildMust(new BoolQueryBuilder().should(QueryBuilders.termQuery("from", req.getAddress()))
				.should(QueryBuilders.termQuery("to", req.getAddress())));
		constructor.setDesc("seq");
		try {
			items = transactionESRepository.search(constructor, Transaction.class, req.getPageNo(),req.getPageSize());
		} catch (IOException e) {
			logger.error(ERROR_TIPS, e);
		}
        List<TransactionListResp> lists = this.transferList(items.getRsData());
        Page<?> page = new Page<>(req.getPageNo(),req.getPageSize());
        result.init(page, lists);
        result.setTotalCount(items.getTotal());
        return result;
    }

    private List<TransactionListResp> transferList(List<Transaction> items) {
    	List<TransactionListResp> lists = new LinkedList<>();
    	for (Transaction transaction:items) {
        	TransactionListResp transactionListResp = new TransactionListResp();
        	BeanUtils.copyProperties(transaction, transactionListResp);
            transactionListResp.setTxHash(transaction.getHash());
            transactionListResp.setActualTxCost(new BigDecimal(transaction.getCost()));
            transactionListResp.setBlockNumber(transaction.getNum());
            transactionListResp.setReceiveType(String.valueOf(transaction.getToType()));
            transactionListResp.setTxType(String.valueOf(transaction.getType()));
            transactionListResp.setServerTime(new Date().getTime());
            transactionListResp.setTimestamp(transaction.getTime().getTime());
            transactionListResp.setValue(new BigDecimal(transaction.getValue()));
            /**
    		 * 失败信息国际化
    		 */
            I18nEnum i18nEnum = I18nEnum.getEnum("CODE" + transaction.getFailReason());
    		if(i18nEnum != null) {
    			transactionListResp.setFailReason(i18n.i(i18nEnum));
    		}
            if(StatusEnum.FAILURE.getCode() == transaction.getStatus()) {
            	transactionListResp.setTxReceiptStatus(0);
    		} else {
    			transactionListResp.setTxReceiptStatus(transaction.getStatus());
    		}
            lists.add(transactionListResp);
        }
    	return lists;
    }

    public AccountDownload transactionListByAddressDownload(String address, Long date,String local, String timeZone) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date currentServerTime = new Date();
        String msg = dateFormat.format(currentServerTime);
        logger.info("导出地址交易列表数据起始日期：{},结束日期：{}", date, msg);

        /** 限制最多导出3万条记录 */
		
		ESQueryBuilderConstructor constructor = new ESQueryBuilderConstructor();
		constructor.must(new ESQueryBuilders().range("time", new Date(date).getTime(), currentServerTime.getTime()));
		constructor.buildMust(new BoolQueryBuilder().should(QueryBuilders.termQuery("from", address))
				.should(QueryBuilders.termQuery("to", address)));
		ESResult<Transaction> items = new ESResult<>();
		constructor.setDesc("seq");
		try {
			items = transactionESRepository.search(constructor, Transaction.class, 1, 3000);
		} catch (IOException e) {
			logger.error(ERROR_TIPS, e);
		}
        List<Object[]> rows = new ArrayList<>();
        items.getRsData().forEach(transaction -> {
        	/**
        	 * 判断是否为to地址
        	 * 如果为to地址则导出报表为收入金额
        	 * 如果为from地址则导出报表为支出金额
        	*/
            boolean toIsAddress = address.equals(transaction.getTo());
            String valueIn = toIsAddress? transaction.getValue() : "0";
            String valueOut = !toIsAddress? transaction.getValue() : "0";
            Object[] row = {
                    transaction.getHash(),
                    transaction.getNum(),
                    DateUtil.timeZoneTransfer(transaction.getTime(), "0", timeZone),
                    /**
                     * 枚举类型名称需要对应
                     */
                    i18n.getMessageForStr(Transaction.TypeEnum.getEnum(transaction.getType()).toString(), local),
                    transaction.getFrom(),
                    transaction.getTo(),
                    /** 数值von转换成lat，并保留十八位精确度 */
                    HexTool.append(EnergonUtil.format(Convert.fromVon(valueIn, Convert.Unit.LAT).setScale(18,RoundingMode.DOWN), 18)),
                    HexTool.append(EnergonUtil.format(Convert.fromVon(valueOut, Convert.Unit.LAT).setScale(18,RoundingMode.DOWN), 18)),
                    HexTool.append(EnergonUtil.format(Convert.fromVon(transaction.getCost(), Convert.Unit.LAT).setScale(18,RoundingMode.DOWN), 18))
            };
            rows.add(row);
        });
        /** 初始化输出流对象 */
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
        	/** 设置导出的csv头，防止乱码 */
        	byteArrayOutputStream.write(new byte[] { (byte) 0xEF, (byte) 0xBB,(byte) 0xBF });
        } catch (IOException e) {
        	logger.error("输出数据错误:",e);
        }
        Writer outputWriter = new OutputStreamWriter(byteArrayOutputStream, StandardCharsets.UTF_8);
        logger.error("download Charset.defaultCharset():{}", StandardCharsets.UTF_8);
        CsvWriter writer = new CsvWriter(outputWriter, new CsvWriterSettings());
        /** 设置导出表的表头 */
        writer.writeHeaders(
                i18n.i(I18nEnum.DOWNLOAD_ACCOUNT_CSV_HASH, local),
                i18n.i(I18nEnum.DOWNLOAD_BLOCK_CSV_NUMBER, local),
                i18n.i(I18nEnum.DOWNLOAD_BLOCK_CSV_TIMESTAMP, local),
                i18n.i(I18nEnum.DOWNLOAD_ACCOUNT_CSV_TYPE, local),
                i18n.i(I18nEnum.DOWNLOAD_ACCOUNT_CSV_FROM, local),
                i18n.i(I18nEnum.DOWNLOAD_ACCOUNT_CSV_TO, local),
                i18n.i(I18nEnum.DOWNLOAD_ACCOUNT_CSV_VALUE_IN, local),
                i18n.i(I18nEnum.DOWNLOAD_ACCOUNT_CSV_VALUE_OUT, local),
                i18n.i(I18nEnum.DOWNLOAD_ACCOUNT_CSV_FEE, local)
        );
        writer.writeRowsAndClose(rows);
        /** 设置返回对象  */
        AccountDownload accountDownload = new AccountDownload();
        accountDownload.setData(byteArrayOutputStream.toByteArray());
        accountDownload.setFilename("Transaction-" + address + "-" + date + ".CSV");
        accountDownload.setLength(byteArrayOutputStream.size());
        return accountDownload;
    }

    @Override
    public TransactionDetailsResp transactionDetails( TransactionDetailsReq req) {
    	/** 根据hash查询具体的交易数据 */
		Transaction transaction = null;
		try {
			transaction = transactionESRepository.get(req.getTxHash(), Transaction.class);
		} catch (IOException e) {
			logger.error(ERROR_TIPS, e);
		}
    	TransactionDetailsResp resp = new TransactionDetailsResp();
    	if(transaction != null) {
    		BeanUtils.copyProperties(transaction, resp);
    		resp.setActualTxCost(new BigDecimal(transaction.getCost()));
    		resp.setBlockNumber(transaction.getNum());
    		resp.setGasLimit(transaction.getGasLimit());
    		resp.setGasUsed(transaction.getGasUsed());
    		resp.setTxType(String.valueOf(transaction.getType()));
    		resp.setTxHash(transaction.getHash());
    		resp.setTimestamp(transaction.getTime().getTime());
    		resp.setServerTime(new Date().getTime());
    		resp.setTxInfo(transaction.getInfo());
    		resp.setGasPrice(new BigDecimal(transaction.getGasPrice()));
    		resp.setValue(new BigDecimal(transaction.getValue()));
			resp.setReceiveType(String.valueOf(transaction.getToType()));
			resp.setContractName(transaction.getMethod());
    		/**
    		 * 失败信息国际化
    		 */
    		I18nEnum i18nEnum = I18nEnum.getEnum("CODE" + transaction.getFailReason());
    		if(i18nEnum != null) {
    			resp.setFailReason(i18n.i(i18nEnum));
    		}
    		if(StatusEnum.FAILURE.getCode() == transaction.getStatus()) {
    			resp.setTxReceiptStatus(0);
    		} else {
    			resp.setTxReceiptStatus(transaction.getStatus());
    		}
    		List<Block> blocks = statisticCacheService.getBlockCache(0, 1);
    		/** 确认区块数等于当前区块书减去交易区块数  */
    		if(!blocks.isEmpty()) {
    			resp.setConfirmNum(String.valueOf(blocks.get(0).getNum()-transaction.getNum()));
    		} else {
    			resp.setConfirmNum("0");
			}

    		/** 如果数据值为null 则置为空 */
    		if("null".equals(transaction.getInfo())) {
    			resp.setTxInfo("0x");
    		}
    		/*
    		 * "first":false,            //是否第一条记录
    		 * "last":true,              //是否最后一条记录
    		 */
    		resp.setFirst(false);
    		if(transaction.getId() == 1) {
    			resp.setFirst(true);
    		} else {
    			/** 
    			 * 根据id查询是否有上一条数据交易数据 
    			*/
    			ESQueryBuilderConstructor constructor = new ESQueryBuilderConstructor();
        		constructor.must(new ESQueryBuilders().term("id", transaction.getId()-1));
        		ESResult<Transaction> first = new ESResult<>();
        		try {
        			first = transactionESRepository.search(constructor, Transaction.class, 1, 1);
        		} catch (IOException e) {
        			logger.error("获取交易错误。", e);
        		}
        		if(first.getTotal() > 0l) {
        			resp.setPreHash(first.getRsData().get(0).getHash());
        		}
    		}
    		
    		resp.setLast(true);
    		/**
    		 *  根据id查询是否有下一条
    		 * */
    		ESQueryBuilderConstructor constructor = new ESQueryBuilderConstructor();
    		constructor.must(new ESQueryBuilders().term("id", transaction.getId()+1));
    		ESResult<Transaction> last = new ESResult<>();
    		try {
    			last = transactionESRepository.search(constructor, Transaction.class, 1, 1);
    		} catch (IOException e) {
    			logger.error("获取交易错误。", e);
    		}
    		if(last.getTotal() > 0) {
    			resp.setLast(false);
    			resp.setNextHash(last.getRsData().get(0).getHash());
    		}
    		
    		String txInfo = transaction.getInfo();
    		/** 根据不同交易类型判断逻辑 */
    		if(StringUtils.isNotBlank(txInfo) || (!"null".equals(txInfo))) {
	    		switch (Transaction.TypeEnum.getEnum(transaction.getType())) {
		    		/** 创建验证人 */
					case STAKE_CREATE:
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
						resp.setDelegationRatio(new BigDecimal(createValidatorParam.getDelegateRewardPer()).divide(BrowserConst.PERCENTAGE).toString());
						break;
					/**
					 * 编辑验证人
					 */
					case STAKE_MODIFY:
						StakeModifyParam editValidatorParam = JSON.parseObject(txInfo, StakeModifyParam.class);
						resp.setBenefitAddr(editValidatorParam.getBenefitAddress());
						resp.setNodeId(editValidatorParam.getNodeId());
						resp.setExternalId(editValidatorParam.getExternalId());
						resp.setWebsite(editValidatorParam.getWebsite());
						resp.setDetails(editValidatorParam.getDetails());
						resp.setNodeName(commonService.getNodeName(editValidatorParam.getNodeId(), editValidatorParam.getNodeName()));
						resp.setExternalUrl(this.getStakingUrl(editValidatorParam.getExternalId(), resp.getTxReceiptStatus()));
						resp.setDelegationRatio(new BigDecimal(editValidatorParam.getDelegateRewardPer()).divide(BrowserConst.PERCENTAGE).toString());
						break;
					/**
					 * 增加质押
					 */
					case STAKE_INCREASE:
						StakeIncreaseParam increaseStakingParam = JSON.parseObject(txInfo, StakeIncreaseParam.class);
						resp.setNodeId(increaseStakingParam.getNodeId());
						resp.setTxAmount(increaseStakingParam.getAmount());
						/**
						 * 节点名称设置
						 */
						resp.setNodeName(commonService.getNodeName(increaseStakingParam.getNodeId(), increaseStakingParam.getNodeName()));
						break;
					/**
					 * 退出验证人
					 */
					case STAKE_EXIT:
						// nodeId + nodeName + applyAmount + redeemLocked + redeemStatus + redeemUnLockedBlock
						StakeExitParam exitValidatorParam = JSON.parseObject(txInfo, StakeExitParam.class);
						resp.setNodeId(exitValidatorParam.getNodeId());
						resp.setNodeName(commonService.getNodeName(exitValidatorParam.getNodeId(), exitValidatorParam.getNodeName()));
						resp.setApplyAmount(exitValidatorParam.getAmount());
						StakingKey stakingKeyE = new StakingKey();
						stakingKeyE.setNodeId(exitValidatorParam.getNodeId());
						stakingKeyE.setStakingBlockNum(exitValidatorParam.getStakingBlockNum().longValue());
						Staking staking = stakingMapper.selectByPrimaryKey(stakingKeyE);
						if(staking!=null) {
							resp.setRedeemLocked(staking.getStakingReduction());
							//只有已退出，则金额才会退回到账户
							if(staking.getStatus() == CustomStaking.StatusEnum.EXITED.getCode()) {
								resp.setRedeemStatus(RedeemStatusEnum.EXITED.getCode());
							} else {
								resp.setRedeemStatus(RedeemStatusEnum.EXITING.getCode());
							}
							/*
							 * 提案交易所在块高%共识周期块数,交易所在第几个共识轮
							 */
				            BigDecimal[] belongToConList = new BigDecimal(resp.getBlockNumber())
				            		.divideAndRemainder(new BigDecimal(blockChainConfig.getSettlePeriodBlockCount()));
				            BigDecimal belongToCon = belongToConList[0];
				            /**
				             * 预计退出区块数=（math.ceil（现有区块/结算周期区块数） + 锁定周期数） +* 结算周期区块数
				             */
							BigDecimal blockNum = (belongToCon.add(new BigDecimal(1)).add(new BigDecimal(blockChainConfig.getUnStakeRefundSettlePeriodCount())))
									.multiply(new BigDecimal(blockChainConfig.getSettlePeriodBlockCount()));
							resp.setRedeemUnLockedBlock(blockNum.toString());
						}
						break;
						/**
						 * 委托
						 */
					case DELEGATE_CREATE:
						DelegateCreateParam delegateParam = JSON.parseObject(txInfo, DelegateCreateParam.class);
						resp.setNodeId(delegateParam.getNodeId());
						resp.setTxAmount(delegateParam.getAmount());
						resp.setNodeName(commonService.getNodeName(delegateParam.getNodeId(), delegateParam.getNodeName()));
						break;
					/**
					 * 委托赎回
					 */
					case DELEGATE_EXIT:
						// nodeId + nodeName + applyAmount + redeemLocked + redeemStatus
						// 通过txHash关联un_delegation表
						DelegateExitParam unDelegateParam = JSON.parseObject(txInfo, DelegateExitParam.class);
						resp.setNodeId(unDelegateParam.getNodeId());
						resp.setApplyAmount(unDelegateParam.getAmount());
						resp.setTxAmount(unDelegateParam.getReward());
						resp.setNodeName(commonService.getNodeName(unDelegateParam.getNodeId(), unDelegateParam.getNodeName()));
						break;
						/**
						 * 文本提案
						 */
					case PROPOSAL_TEXT:
						ProposalTextParam createProposalTextParam = JSON.parseObject(txInfo, ProposalTextParam.class);
						if(StringUtils.isNotBlank(createProposalTextParam.getPIDID())) {
							resp.setPipNum("PIP-" + createProposalTextParam.getPIDID());
						}
						resp.setNodeId(createProposalTextParam.getVerifier());
						resp.setProposalHash(req.getTxHash());
						resp.setNodeName(commonService.getNodeName(createProposalTextParam.getVerifier(), createProposalTextParam.getNodeName()));
						/** 如果数据库有值，以数据库为准 */
						this.transferTransaction(resp, req.getTxHash());
						break;
						/**
						 * 升级提案
						 */
					case PROPOSAL_UPGRADE:
						ProposalUpgradeParam createProposalUpgradeParam = JSON.parseObject(txInfo, ProposalUpgradeParam.class);
						resp.setProposalNewVersion(String.valueOf(createProposalUpgradeParam.getNewVersion()));
						if(StringUtils.isNotBlank(createProposalUpgradeParam.getPIDID())) {
							resp.setPipNum("PIP-" + createProposalUpgradeParam.getPIDID());
						}
						resp.setNodeId(createProposalUpgradeParam.getVerifier());
						resp.setProposalHash(req.getTxHash());
						resp.setNodeName(commonService.getNodeName(createProposalUpgradeParam.getVerifier(), createProposalUpgradeParam.getNodeName()));
						/** 如果数据库有值，以数据库为准 */
						this.transferTransaction(resp, req.getTxHash());
						break;
						/**
						 * 参数提案
						 */
					case PROPOSAL_PARAMETER:
						ProposalParameterParam proposalParameterParam = JSON.parseObject(txInfo, ProposalParameterParam.class);
						if(StringUtils.isNotBlank(proposalParameterParam.getPIDID())) {
							resp.setPipNum("PIP-" + proposalParameterParam.getPIDID());
						}
						resp.setNodeId(proposalParameterParam.getVerifier());
						resp.setProposalHash(req.getTxHash());
						resp.setNodeName(commonService.getNodeName(proposalParameterParam.getVerifier(), proposalParameterParam.getNodeName()));
						/** 如果数据库有值，以数据库为准 */
						this.transferTransaction(resp, req.getTxHash());
						break;
						/**
						 * 取消提案
						 */
					case PROPOSAL_CANCEL:
						ProposalCancelParam cancelProposalParam = JSON.parseObject(txInfo, ProposalCancelParam.class);
						if(StringUtils.isNotBlank(cancelProposalParam.getPIDID())) {
							resp.setPipNum("PIP-" + cancelProposalParam.getPIDID());
						}
						resp.setNodeId(cancelProposalParam.getVerifier());
						resp.setProposalHash(req.getTxHash());
						resp.setNodeName(commonService.getNodeName(cancelProposalParam.getVerifier(), cancelProposalParam.getNodeName()));
						/** 如果数据库有值，以数据库为准 */
						this.transferTransaction(resp, req.getTxHash());
						break;
						/**
						 * 提案投票
						 */
					case PROPOSAL_VOTE:
						// nodeId + nodeName + txType + proposalUrl + proposalHash + proposalNewVersion +  proposalOption
						ProposalVoteParam votingProposalParam = JSON.parseObject(txInfo, ProposalVoteParam.class);
						resp.setNodeId(votingProposalParam.getVerifier());
						resp.setProposalOption(votingProposalParam.getProposalType());
						resp.setProposalHash(votingProposalParam.getProposalId());
						resp.setProposalNewVersion(votingProposalParam.getProgramVersion());
						resp.setNodeName(commonService.getNodeName(votingProposalParam.getVerifier(), votingProposalParam.getNodeName()));
						if(StringUtils.isNotBlank(votingProposalParam.getPIDID())) {
							resp.setPipNum("PIP-" + votingProposalParam.getPIDID());
						}
						resp.setVoteStatus(votingProposalParam.getOption());
						/**
						 * 获取提案信息
						 */
						Proposal proposal = proposalMapper.selectByPrimaryKey(votingProposalParam.getProposalId());
						if(proposal != null) {
							resp.setPipNum(proposal.getPipNum());
							resp.setProposalTitle(BrowserConst.INQUIRY.equals(proposal.getTopic())?"":proposal.getTopic());
							resp.setProposalUrl(proposal.getUrl());
							resp.setProposalOption(String.valueOf(proposal.getType()));
						}
						break;
						/**
						 * 版本申明
						 */
					case VERSION_DECLARE:
						VersionDeclareParam declareVersionParam = JSON.parseObject(txInfo, VersionDeclareParam.class);
						resp.setNodeId(declareVersionParam.getActiveNode());
						resp.setDeclareVersion(String.valueOf(declareVersionParam.getVersion()));
						resp.setNodeName(commonService.getNodeName(declareVersionParam.getActiveNode(), declareVersionParam.getNodeName()));
						break;
						/**
						 * 举报双签
						 */
					case REPORT:
						ReportParam reportValidatorParam = JSON.parseObject(txInfo, ReportParam.class);
						List<TransactionDetailsEvidencesResp> transactionDetailsEvidencesResps = new ArrayList<>();
						TransactionDetailsEvidencesResp transactionDetailsEvidencesResp = new TransactionDetailsEvidencesResp();
						transactionDetailsEvidencesResp.setVerify(reportValidatorParam.getVerify());
						transactionDetailsEvidencesResp.setNodeName(commonService.getNodeName(reportValidatorParam.getVerify(), reportValidatorParam.getNodeName()));
						resp.setEvidence(reportValidatorParam.getData());
						transactionDetailsEvidencesResps.add(transactionDetailsEvidencesResp);
						Slash slash = slashMapper.selectByPrimaryKey(req.getTxHash());
						if(slash != null) {
							resp.setReportRewards(slash.getReward());
							/**
							 * 查看举报之后是否退出来判断是否交易正确
							 */
							resp.setReportStatus(slash.getIsQuit() == 1?2:1);
						}
						resp.setReportType(reportValidatorParam.getType().intValue());
						resp.setEvidences(transactionDetailsEvidencesResps);
						break;
						/**
						 * 创建锁仓
						 */
					case RESTRICTING_CREATE:
						// RPAccount + value + RPPlan
						RestrictingCreateParam createRestrictingParam = JSON.parseObject(txInfo, RestrictingCreateParam.class);
						List<TransactionDetailsRPPlanResp> rpPlanResps = new ArrayList<>();
						resp.setRPAccount(createRestrictingParam.getAccount());
						BigDecimal amountSum = new BigDecimal(0);
						for(RestrictingCreateParam.RestrictingPlan p:createRestrictingParam.getPlans()) {
							TransactionDetailsRPPlanResp transactionDetailsRPPlanResp = new TransactionDetailsRPPlanResp();
							amountSum = amountSum.add(p.getAmount());
							transactionDetailsRPPlanResp.setAmount(p.getAmount());
							transactionDetailsRPPlanResp.setEpoch(p.getEpoch());
							//锁仓周期对应快高  结算周期 * epoch
							transactionDetailsRPPlanResp.setBlockNumber(blockChainConfig.getSettlePeriodBlockCount()
									.multiply(new BigInteger(String.valueOf(p.getEpoch()))).longValue());
							rpPlanResps.add(transactionDetailsRPPlanResp);
						}
						//累加
						resp.setRPNum(amountSum);
						resp.setRPPlan(rpPlanResps);
						break;
						/**
						 * 领取奖励
						 */
					case CLAIM_REWARDS:
						DelegateRewardClaimParam delegateRewardClaimParam  = JSON.parseObject(txInfo, DelegateRewardClaimParam.class);
						List<TransactionDetailsRewardsResp> rewards = new ArrayList<>();
						BigDecimal rewardSum = BigDecimal.ZERO;
						for(Reward reward:delegateRewardClaimParam.getRewardList()) {
							TransactionDetailsRewardsResp transactionDetailsRewardsResp = new TransactionDetailsRewardsResp();
							transactionDetailsRewardsResp.setVerify(reward.getNodeId());
							transactionDetailsRewardsResp.setNodeName(reward.getNodeName());
							transactionDetailsRewardsResp.setReward(reward.getReward());
							rewardSum = rewardSum.add(reward.getReward());
							rewards.add(transactionDetailsRewardsResp);
						}
						resp.setTxAmount(rewardSum);
						resp.setRewards(rewards);
						break;
						/**
						 * 合约创建
						 */
					case CONTRACT_CREATE:
						/**
						 * to地址设置为合约地址
						 */
						resp.setTo(transaction.getContractAddress());
						resp.setTxInfo(transaction.getInput());
						break;
						/**
						 * 合约执行
						 */
					case CONTRACT_EXEC:
						resp.setTxInfo(transaction.getInput());
						break;
				default:
					break;
				}
    		}
    	}
    	return resp;
    }

    /**
     * 提案信息统一转换
     * @method transferTransaction
     * @param resp
     * @param hash
     * @return
     */
    private TransactionDetailsResp transferTransaction(TransactionDetailsResp resp, String hash) {
    	Proposal proposal = proposalMapper.selectByPrimaryKey(hash);
		if(proposal != null) {
			resp.setNodeId(proposal.getNodeId());
			resp.setNodeName(proposal.getNodeName());
			resp.setPipNum(proposal.getPipNum());
			resp.setProposalTitle(BrowserConst.INQUIRY.equals(proposal.getTopic())?"":proposal.getTopic());
			resp.setProposalStatus(proposal.getStatus());
			resp.setProposalOption(String.valueOf(proposal.getType()));
			resp.setProposalNewVersion(proposal.getNewVersion());
			resp.setProposalUrl(proposal.getUrl());
		}
		return resp;
    }

    /**
     * 统一设置验证人keybaseurl
     * @method getStakingUrl
     * @param externalId
     * @param txReceiptStatus
     * @return
     */
    private String getStakingUrl(String externalId,Integer txReceiptStatus) {

    	String keyBaseUrl = blockChainConfig.getKeyBase();
        String keyBaseApi = blockChainConfig.getKeyBaseApi();
        String defaultBaseUrl = blockChainConfig.getKeyBase();
    	/**
		 * 如果externalId为空就不返回给前端url，反转跳转
		 */
		if(StringUtils.isNotBlank(externalId)) {
			/**
			 * 如果为失败的交易直接设置默认的url然后跳出
			 */
			if(txReceiptStatus == Transaction.StatusEnum.FAILURE.getCode()) {
				return defaultBaseUrl;
			}
			/**
			 * 检查redis是否已经存储
			 */
			String userName = redisFactory.createRedisCommands().get(externalId);
			if(StringUtils.isNotBlank(userName)) { 
				defaultBaseUrl += userName;
				return defaultBaseUrl;
			}
			String url = keyBaseUrl.concat(keyBaseApi.concat(externalId));
			try {
				KeyBaseUser keyBaseUser = HttpUtil.get(url,KeyBaseUser.class);
				userName = KeyBaseAnalysis.getKeyBaseUseName(keyBaseUser);
			} catch (Exception e) {
				logger.error("getStakingUrl error.externalId:{},txReceiptStatus:{},error:{}",externalId, txReceiptStatus, e.getMessage());
				return defaultBaseUrl;
			}
			if(StringUtils.isNotBlank(userName)) { 
				/**
				 * 设置redis
				 */
				redisFactory.createRedisCommands().set(externalId, userName);
				defaultBaseUrl += userName;
			}
			return defaultBaseUrl;
		}
    	return null;
    }

	@Override
	public RespPage<QueryClaimByAddressResp> queryClaimByAddress(TransactionListByAddressRequest req) {
		/** 根据地址查询具体的领取奖励数据 */
		ESQueryBuilderConstructor constructor = new ESQueryBuilderConstructor();
		constructor.must(new ESQueryBuilders().term("addr", req.getAddress()));
		constructor.setDesc("time");
		ESResult<DelegationReward> delegationRewards = null;
		try {
			delegationRewards = delegationRewardESRepository.search(constructor, DelegationReward.class, req.getPageNo(), req.getPageSize());
		} catch (Exception e) {
			logger.error(ERROR_TIPS, e);
		}
		if(delegationRewards==null){
			// 防止空指异常
			delegationRewards = new ESResult<>();
			delegationRewards.setTotal(0L);
			delegationRewards.setRsData(Collections.emptyList());
		}
		List<QueryClaimByAddressResp> queryClaimByAddressResps = new ArrayList<>();
		for(DelegationReward delegationReward:delegationRewards.getRsData()) {
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
			for(Extra extra : extras) {
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
		
		RespPage<QueryClaimByAddressResp> result = new RespPage<>();
		result.init(queryClaimByAddressResps, delegationRewards.getTotal(), delegationRewards.getTotal(), 0l);
		return result;
	}

	@Override
	public RespPage<QueryClaimByStakingResp> queryClaimByStaking(QueryClaimByStakingReq req) {
		/** 根据地址查询具体的领取奖励数据 */
		ESQueryBuilderConstructor constructor = new ESQueryBuilderConstructor();
		constructor.must(new ESQueryBuilders().fuzzy("extra", req.getNodeId()));
		constructor.setDesc("time");
		ESResult<DelegationReward> delegationRewards = null;
		try {
			delegationRewards = delegationRewardESRepository.search(constructor, DelegationReward.class, req.getPageNo(), req.getPageSize());
		} catch (Exception e) {
			logger.error(ERROR_TIPS, e);
		}
		if(delegationRewards==null){
			// 防止空指异常
			delegationRewards = new ESResult<>();
			delegationRewards.setTotal(0L);
			delegationRewards.setRsData(Collections.emptyList());
		}
		List<QueryClaimByStakingResp> queryClaimByStakingResps = new ArrayList<>();
		for(DelegationReward delegationReward:delegationRewards.getRsData()) {
			QueryClaimByStakingResp queryClaimByStakingResp = new QueryClaimByStakingResp();
			BeanUtils.copyProperties(delegationReward, queryClaimByStakingResp);
			queryClaimByStakingResp.setTime(delegationReward.getTime().getTime());
			/**
			 * 解析json获取具体提取奖励的数据
			 */
			List<Extra> extras = JSONObject.parseArray(delegationReward.getExtra(), DelegationReward.Extra.class);
			BigDecimal allRewards = BigDecimal.ZERO;
			/**
			 * 累积交易的所有领取奖励
			 */
			for(Extra extra : extras) {
				/**
				 * 只有地址相同的才需要累积
				 */
				if(req.getNodeId().equals(extra.getNodeId())) {
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

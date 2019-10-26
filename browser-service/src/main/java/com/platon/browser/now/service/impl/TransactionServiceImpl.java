package com.platon.browser.now.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.platon.browser.config.BlockChainConfig;
import com.platon.browser.dao.entity.*;
import com.platon.browser.dao.mapper.*;
import com.platon.browser.dto.CustomStaking;
import com.platon.browser.dto.CustomTransaction;
import com.platon.browser.dto.CustomTransaction.TxReceiptStatusEnum;
import com.platon.browser.dto.CustomVoteProposal;
import com.platon.browser.dto.account.AccountDownload;
import com.platon.browser.dto.keybase.KeyBaseUser;
import com.platon.browser.dto.transaction.TransactionCacheDto;
import com.platon.browser.enums.I18nEnum;
import com.platon.browser.enums.NavigateEnum;
import com.platon.browser.enums.RedeemStatusEnum;
import com.platon.browser.enums.ReqTransactionTypeEnum;
import com.platon.browser.now.service.TransactionService;
import com.platon.browser.now.service.cache.StatisticCacheService;
import com.platon.browser.param.*;
import com.platon.browser.req.PageReq;
import com.platon.browser.req.newtransaction.TransactionDetailNavigateReq;
import com.platon.browser.req.newtransaction.TransactionDetailsReq;
import com.platon.browser.req.newtransaction.TransactionListByAddressRequest;
import com.platon.browser.req.newtransaction.TransactionListByBlockRequest;
import com.platon.browser.res.RespPage;
import com.platon.browser.res.transaction.TransactionDetailsEvidencesResp;
import com.platon.browser.res.transaction.TransactionDetailsRPPlanResp;
import com.platon.browser.res.transaction.TransactionDetailsResp;
import com.platon.browser.res.transaction.TransactionListResp;
import com.platon.browser.util.*;
import com.univocity.parsers.csv.CsvWriter;
import com.univocity.parsers.csv.CsvWriterSettings;
import org.apache.commons.lang3.StringUtils;
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
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

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
    private TransactionMapper transactionMapper;
    @Autowired
    private I18nUtil i18n;
    @Autowired
    private UnDelegationMapper unDelegationMapper;
    @Autowired
    private StakingMapper stakingMapper;
    @Autowired
    private SlashMapper slashMapper;
    @Autowired
    private ProposalMapper proposalMapper;
    @Autowired
    private CustomVoteMapper customVoteMapper;
    @Autowired
    private StatisticCacheService statisticCacheService;
    @Autowired
    private BlockChainConfig blockChainConfig;

    @Override
    public RespPage<TransactionListResp> getTransactionList( PageReq req) {
        RespPage<TransactionListResp> result = new RespPage<>();
        /** 分页查询redis交易数据 */
        TransactionCacheDto transactionCacheDto = statisticCacheService.getTransactionCache(req.getPageNo(), req.getPageSize());
        List<TransactionWithBLOBs> items = transactionCacheDto.getTransactionList();
        List<TransactionListResp> lists = this.tranferList(items);
        Page<?> page = new Page<>(req.getPageNo(),req.getPageSize());
        result.init(page, lists);
        result.setTotalCount(transactionCacheDto.getPage().getTotalCount());
        result.setTotalPages(transactionCacheDto.getPage().getTotalPages());
        return result;
    }

    @Override
    public RespPage<TransactionListResp> getTransactionListByBlock(TransactionListByBlockRequest req) {
        RespPage<TransactionListResp> result = new RespPage<>();
        TransactionExample transactionExample = new TransactionExample();
        TransactionExample.Criteria criteria = transactionExample.createCriteria()
                .andBlockNumberEqualTo(req.getBlockNumber().longValue());
        if (req.getTxType() != null && !req.getTxType().isEmpty()) {
            criteria.andTxTypeIn(ReqTransactionTypeEnum.getTxType(req.getTxType()));
        }
        PageHelper.startPage(req.getPageNo(),req.getPageSize());
        /** 根据区块号和类型分页查询交易信息 */
        List<TransactionWithBLOBs> items = transactionMapper.selectByExampleWithBLOBs(transactionExample);
        List<TransactionListResp> lists = this.tranferList(items);
        /** 统计交易信息 */
        long count  = transactionMapper.countByExample(transactionExample);
        Page<?> page = new Page<>(req.getPageNo(),req.getPageSize());
        result.init(page, lists);
        result.setTotalCount(count);
        return result;
    }

    @Override
    public RespPage<TransactionListResp> getTransactionListByAddress(TransactionListByAddressRequest req) {
        RespPage<TransactionListResp> result = new RespPage<>();

        TransactionExample transactionExample = new TransactionExample();
        transactionExample.setOrderByClause(" sequence desc");
        /** 地址信息可能是from也可能是to */
        TransactionExample.Criteria first = transactionExample.createCriteria()
                .andFromEqualTo(req.getAddress());
        TransactionExample.Criteria second = transactionExample.createCriteria()
                .andToEqualTo(req.getAddress());
        if (req.getTxType() != null && !req.getTxType().isEmpty()) {
        	first.andTxTypeIn(ReqTransactionTypeEnum.getTxType(req.getTxType()));
        	second.andTxTypeIn(ReqTransactionTypeEnum.getTxType(req.getTxType()));
        }
        PageHelper.startPage(req.getPageNo(),req.getPageSize());
        transactionExample.or(second);
        /** 根据地址查询分页交易信息 */
        List<TransactionWithBLOBs> items = transactionMapper.selectByExampleWithBLOBs(transactionExample);
        List<TransactionListResp> lists = this.tranferList(items);
        /** 查询总数 */
        long count  = transactionMapper.countByExample(transactionExample);
        Page<?> page = new Page<>(req.getPageNo(),req.getPageSize());
        result.init(page, lists);
        result.setTotalCount(count);
        return result;
    }

    private List<TransactionListResp> tranferList(List<TransactionWithBLOBs> items) {
    	List<TransactionListResp> lists = new LinkedList<>();
    	for (TransactionWithBLOBs transaction:items) {
        	TransactionListResp transactionListResp = new TransactionListResp();
        	BeanUtils.copyProperties(transaction, transactionListResp);
            transactionListResp.setTxHash(transaction.getHash());
            transactionListResp.setServerTime(new Date().getTime());
            transactionListResp.setTimestamp(transaction.getTimestamp().getTime());
            lists.add(transactionListResp);
        }
    	return lists;
    }

    public AccountDownload transactionListByAddressDownload(String address, Long date,String local, String timeZone) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date currentServerTime = new Date();
        String msg = dateFormat.format(currentServerTime);
        logger.info("导出地址交易列表数据起始日期：{},结束日期：{}", date, msg);
        TransactionExample transactionExample = new TransactionExample();
        transactionExample.setOrderByClause(" sequence desc");
        /** 根据地址查询交易，则可能是from也可能是to */
        TransactionExample.Criteria first = transactionExample.createCriteria();
        TransactionExample.Criteria second = transactionExample.createCriteria();
        first.andFromEqualTo(address);
        second.andToEqualTo(address);
    	first.andCreateTimeBetween(new Date(date), currentServerTime);
    	second.andCreateTimeBetween(new Date(date), currentServerTime);

        /** 限制最多导出3万条记录 */
        PageHelper.startPage(1,30000);
        transactionExample.or(second);
        List<Transaction> items = transactionMapper.selectByExample(transactionExample);
        List<Object[]> rows = new ArrayList<>();
        items.forEach(transaction -> {
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
                    transaction.getBlockNumber(),
                    DateUtil.timeZoneTransfer(transaction.getTimestamp(), "0", timeZone),
                    i18n.getMessageForStr(CustomTransaction.TxTypeEnum.getEnum(transaction.getTxType()).toString(), local),
                    transaction.getFrom(),
                    transaction.getTo(),
                    /** 数值von转换成lat，并保留十八位精确度 */
                    EnergonUtil.format(Convert.fromVon(valueIn, Convert.Unit.LAT).setScale(18,RoundingMode.DOWN), 18),
                    EnergonUtil.format(Convert.fromVon(valueOut, Convert.Unit.LAT).setScale(18,RoundingMode.DOWN), 18),
                    EnergonUtil.format(Convert.fromVon(transaction.getActualTxCost(), Convert.Unit.LAT).setScale(18,RoundingMode.DOWN), 18)
            };
            rows.add(row);
        });
        /** 初始化输出流对象 */
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Writer outputWriter = new OutputStreamWriter(byteArrayOutputStream, Charset.defaultCharset());
        try {
        	/** 设置导出的csv头，防止乱码 */
            outputWriter.write(new String(new byte[] { (byte) 0xEF, (byte) 0xBB,(byte) 0xBF }));
        } catch (IOException e) {
        	logger.error("输出数据错误:",e);
        }
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
    	TransactionWithBLOBs transaction = transactionMapper.selectByPrimaryKey(req.getTxHash());
    	TransactionDetailsResp resp = new TransactionDetailsResp();
    	if(transaction!=null) {
    		BeanUtils.copyProperties(transaction, resp);
    		resp.setTxHash(transaction.getHash());
    		resp.setTimestamp(transaction.getTimestamp().getTime());
    		resp.setServerTime(new Date().getTime());
    		List<Block> blocks = statisticCacheService.getBlockCache(0, 1);
    		/** 确认区块数等于当前区块书减去交易区块数  */
    		if(!blocks.isEmpty()) {
    			resp.setConfirmNum(String.valueOf(blocks.get(0).getNumber()-transaction.getBlockNumber()));
    		} else {
    			resp.setConfirmNum("0");
			}
    		
    		/** 暂时只有账户合约 */
    		resp.setReceiveType("account");
    		/** 如果数据值为null 则置为空 */
    		if("null".equals(transaction.getTxInfo())) {
    			resp.setTxInfo("0x");
    		}
    		/*
    		 * "first":false,            //是否第一条记录
    		 * "last":true,              //是否最后一条记录
    		 */
    		TransactionExample condition = new TransactionExample();
    		condition.createCriteria().andSequenceLessThan(transaction.getSequence());
    		condition.setOrderByClause("sequence DESC");
    		PageHelper.startPage(1,1);
    		/** 倒序查询交易数据，判断是否第一条交易记录，不为第一条则设置对应父hash */
    		List<Transaction> transactionList = transactionMapper.selectByExample(condition);
    		if(transactionList.isEmpty()){
    			resp.setFirst(true);
    		}else {
    			resp.setPreHash(transactionList.get(0).getHash());
    		}
    		condition = new TransactionExample();
    		condition.createCriteria().andSequenceGreaterThan(transaction.getSequence());
    		condition.setOrderByClause("sequence ASC");
    		PageHelper.startPage(1,1);
    		/** 倒序查询交易数据，判断是否第一条交易记录，不为第一条则设置对应下一个hash */
    		transactionList = transactionMapper.selectByExample(condition);
    		if(transactionList.isEmpty()){
    			resp.setLast(true);
    		}else {
    			resp.setNextHash(transactionList.get(0).getHash());
    		}
    		String txInfo = transaction.getTxInfo();
    		/** 根据不同交易类型判断逻辑 */
    		if(StringUtils.isNotBlank(txInfo) || (!"null".equals(txInfo))) {
	    		switch (CustomTransaction.TxTypeEnum.getEnum(transaction.getTxType())) {
		    		/** 创建验证人 */
					case CREATE_VALIDATOR:
						CreateValidatorParam createValidatorParam = JSONObject.parseObject(txInfo, CreateValidatorParam.class);
						resp.setTxAmount(createValidatorParam.getAmount());
						resp.setBenefitAddr(createValidatorParam.getBenefitAddress());
						resp.setNodeId(createValidatorParam.getNodeId());
						resp.setNodeName(createValidatorParam.getNodeName());
						resp.setExternalId(createValidatorParam.getExternalId());
						resp.setWebsite(createValidatorParam.getWebsite());
						resp.setDetails(createValidatorParam.getDetails());
						resp.setProgramVersion(createValidatorParam.getProgramVersion());
						resp.setTxAmount(createValidatorParam.getAmount());
						resp.setExternalUrl(this.getStakingUrl(createValidatorParam.getExternalId(), resp.getTxReceiptStatus()));
						break;
					//编辑验证人
					case EDIT_VALIDATOR:
						EditValidatorParam editValidatorParam = JSONObject.parseObject(txInfo, EditValidatorParam.class);
						resp.setBenefitAddr(editValidatorParam.getBenefitAddress());
						resp.setNodeId(editValidatorParam.getNodeId());
						resp.setExternalId(editValidatorParam.getExternalId());
						resp.setWebsite(editValidatorParam.getWebsite());
						resp.setDetails(editValidatorParam.getDetails());
						resp.setNodeName(this.setStakingName(editValidatorParam.getNodeId(), editValidatorParam.getNodeName()));
						resp.setExternalUrl(this.getStakingUrl(editValidatorParam.getExternalId(), resp.getTxReceiptStatus()));
						break;
					//增加质押
					case INCREASE_STAKING:
						IncreaseStakingParam increaseStakingParam = JSONObject.parseObject(txInfo, IncreaseStakingParam.class);
						resp.setNodeId(increaseStakingParam.getNodeId());
						resp.setTxAmount(increaseStakingParam.getAmount());
						resp.setNodeName(this.setStakingName(increaseStakingParam.getNodeId(), increaseStakingParam.getNodeName()));
						break;
					//退出验证人
					case EXIT_VALIDATOR:
						// nodeId + nodeName + applyAmount + redeemLocked + redeemStatus + redeemUnLockedBlock
						ExitValidatorParam exitValidatorParam = JSONObject.parseObject(txInfo, ExitValidatorParam.class);
						resp.setNodeId(exitValidatorParam.getNodeId());
						resp.setNodeName(this.setStakingName(exitValidatorParam.getNodeId(), exitValidatorParam.getNodeName()));
						resp.setApplyAmount(exitValidatorParam.getAmount());
						StakingKey stakingKeyE = new StakingKey();
						stakingKeyE.setNodeId(exitValidatorParam.getNodeId());
						if(StringUtils.isNotBlank(exitValidatorParam.getStakingBlockNum())) {
							stakingKeyE.setStakingBlockNum(Long.valueOf(exitValidatorParam.getStakingBlockNum()));
						}
						Staking staking = stakingMapper.selectByPrimaryKey(stakingKeyE);
						if(staking!=null) {
							resp.setRedeemLocked(staking.getStakingReduction());
							//只有已退出，则金额才会退回到账户
							if(staking.getStatus() == CustomStaking.StatusEnum.EXITED.getCode()) {
								resp.setRedeemStatus(RedeemStatusEnum.EXITED.getCode());
							} else {
								resp.setRedeemStatus(RedeemStatusEnum.EXITING.getCode());
							}
							//（staking_reduction_epoch  + 节点质押退回锁定周期） * 结算周期区块数(C) + 现有区块数
							BigDecimal blockNum = (new BigDecimal(staking.getStakingReductionEpoch()).add(new BigDecimal(blockChainConfig.getUnStakeRefundSettlePeriodCount())))
									.multiply(new BigDecimal(blockChainConfig.getSettlePeriodBlockCount())).add(new BigDecimal(resp.getBlockNumber()));
							resp.setRedeemUnLockedBlock(blockNum.toString());
						}
						break;
						//委托
					case DELEGATE:
						DelegateParam delegateParam = JSONObject.parseObject(txInfo, DelegateParam.class);
						resp.setNodeId(delegateParam.getNodeId());
						resp.setTxAmount(delegateParam.getAmount());
						resp.setNodeName(this.setStakingName(delegateParam.getNodeId(), delegateParam.getNodeName()));
						break;
					//委托赎回
					case UN_DELEGATE:
						// nodeId + nodeName + applyAmount + redeemLocked + redeemStatus
						// 通过txHash关联un_delegation表
						UnDelegateParam unDelegateParam = JSONObject.parseObject(txInfo, UnDelegateParam.class);
						resp.setNodeId(unDelegateParam.getNodeId());
						resp.setApplyAmount(unDelegateParam.getAmount());
						resp.setTxAmount(unDelegateParam.getAmount());
						resp.setNodeName(this.setStakingName(unDelegateParam.getNodeId(), unDelegateParam.getNodeName()));
						UnDelegation unDelegation = unDelegationMapper.selectByPrimaryKey(req.getTxHash());
						if(unDelegation!=null) {
							resp.setApplyAmount(unDelegation.getApplyAmount());
							resp.setRedeemLocked(unDelegation.getRedeemLocked());
							resp.setRedeemStatus(unDelegation.getStatus());
						}
						break;
					case CREATE_PROPOSAL_TEXT:
						CreateProposalTextParam createProposalTextParam = JSONObject.parseObject(txInfo, CreateProposalTextParam.class);
						if(StringUtils.isNotBlank(createProposalTextParam.getPIDID())) {
							resp.setPipNum("PIP-" + createProposalTextParam.getPIDID());
						}
						resp.setNodeId(createProposalTextParam.getVerifier());
						resp.setProposalHash(req.getTxHash());
						resp.setNodeName(this.setStakingName(createProposalTextParam.getVerifier(), createProposalTextParam.getNodeName()));
						/** 如果数据库有值，以数据库为准 */
						this.transferTransaction(resp, req.getTxHash());
						break;
					case CREATE_PROPOSAL_UPGRADE:
						CreateProposalUpgradeParam createProposalUpgradeParam = JSONObject.parseObject(txInfo, CreateProposalUpgradeParam.class);
						resp.setProposalNewVersion(String.valueOf(createProposalUpgradeParam.getNewVersion()));
						if(StringUtils.isNotBlank(createProposalUpgradeParam.getPIDID())) {
							resp.setPipNum("PIP-" + createProposalUpgradeParam.getPIDID());
						}
						resp.setNodeId(createProposalUpgradeParam.getVerifier());
						resp.setProposalHash(req.getTxHash());
						resp.setNodeName(this.setStakingName(createProposalUpgradeParam.getVerifier(), createProposalUpgradeParam.getNodeName()));
						/** 如果数据库有值，以数据库为准 */
						this.transferTransaction(resp, req.getTxHash());
						break;
					case CREATE_PROPOSAL_PARAMETER:
					case CANCEL_PROPOSAL:
						CancelProposalParam cancelProposalParam = JSONObject.parseObject(txInfo, CancelProposalParam.class);
						if(StringUtils.isNotBlank(cancelProposalParam.getPIDID())) {
							resp.setPipNum("PIP-" + cancelProposalParam.getPIDID());
						}
						resp.setNodeId(cancelProposalParam.getVerifier());
						resp.setProposalHash(req.getTxHash());
						resp.setNodeName(this.setStakingName(cancelProposalParam.getVerifier(), cancelProposalParam.getNodeName()));
						/** 如果数据库有值，以数据库为准 */
						this.transferTransaction(resp, req.getTxHash());
						break;
					case VOTING_PROPOSAL:
						// nodeId + nodeName + txType + proposalUrl + proposalHash + proposalNewVersion +  proposalOption
						VotingProposalParam votingProposalParam = JSONObject.parseObject(txInfo, VotingProposalParam.class);
						resp.setNodeId(votingProposalParam.getVerifier());
						resp.setProposalOption(votingProposalParam.getProposalType());
						resp.setProposalHash(votingProposalParam.getProposalId());
						resp.setProposalNewVersion(votingProposalParam.getProgramVersion());
						resp.setNodeName(this.setStakingName(votingProposalParam.getVerifier(), votingProposalParam.getNodeName()));
						if(StringUtils.isNotBlank(votingProposalParam.getPIDID())) {
							resp.setPipNum("PIP-" + votingProposalParam.getPIDID());
						}
						resp.setVoteStatus(votingProposalParam.getOption());
						CustomVoteProposal customVoteProposal = customVoteMapper.selectVotePropal(req.getTxHash());
						if(customVoteProposal != null) {
							resp.setNodeId(customVoteProposal.getVerifier());
							resp.setNodeName(customVoteProposal.getVerifierName());
							resp.setProposalOption(customVoteProposal.getType());
							resp.setProposalHash(customVoteProposal.getProposalHash());
							resp.setProposalNewVersion(customVoteProposal.getNewVersion());
							resp.setPipNum(customVoteProposal.getPipNum());
							resp.setProposalTitle(customVoteProposal.getTopic());
							resp.setProposalUrl(customVoteProposal.getUrl());
							resp.setVoteStatus(customVoteProposal.getOption());
						}
						/**
						 * 失败情况下需要到提案上获取提案信息
						 */
						if(resp.getTxReceiptStatus().intValue() == TxReceiptStatusEnum.FAILURE.getCode()) {
							Proposal proposal = proposalMapper.selectByPrimaryKey(votingProposalParam.getProposalId());
							if(proposal != null) {
								resp.setPipNum(proposal.getPipNum());
								resp.setProposalTitle(proposal.getTopic());
								resp.setProposalUrl(proposal.getUrl());
								resp.setProposalOption(proposal.getType());
							}
						}
						break;
						//版本申明
					case DECLARE_VERSION:
						DeclareVersionParam declareVersionParam = JSONObject.parseObject(txInfo, DeclareVersionParam.class);
						resp.setNodeId(declareVersionParam.getActiveNode());
						resp.setDeclareVersion(String.valueOf(declareVersionParam.getVersion()));
						resp.setNodeName(this.setStakingName(declareVersionParam.getActiveNode(), declareVersionParam.getNodeName()));
						if(StringUtils.isNotBlank(declareVersionParam.getNodeName())) {
							resp.setNodeName(declareVersionParam.getNodeName());
						} else {
							StakingExample stakingExample = new StakingExample();
							stakingExample.setOrderByClause(" staking_block_num desc");
							stakingExample.createCriteria().andNodeIdEqualTo(declareVersionParam.getActiveNode());
							List<Staking> stakings = stakingMapper.selectByExample(stakingExample);
							if(!stakings.isEmpty()) {
								resp.setNodeName(stakings.get(0).getStakingName());
							}
						}
						break;
					case REPORT_VALIDATOR:
						ReportValidatorParam reportValidatorParam = JSONObject.parseObject(txInfo, ReportValidatorParam.class);
						List<TransactionDetailsEvidencesResp> transactionDetailsEvidencesResps = new ArrayList<>();
						TransactionDetailsEvidencesResp transactionDetailsEvidencesResp = new TransactionDetailsEvidencesResp();
						transactionDetailsEvidencesResp.setVerify(reportValidatorParam.getVerify());
						transactionDetailsEvidencesResp.setNodeName(this.setStakingName(reportValidatorParam.getVerify(), reportValidatorParam.getNodeName()));
						resp.setEvidence(reportValidatorParam.getData());
						transactionDetailsEvidencesResps.add(transactionDetailsEvidencesResp);
						Slash slash = slashMapper.selectByPrimaryKey(req.getTxHash());
						if(slash != null) {
							resp.setReportRewards(slash.getReward());
							resp.setReportStatus(slash.getStatus());
						}
						resp.setReportType(reportValidatorParam.getType().intValue());
						resp.setEvidences(transactionDetailsEvidencesResps);
						break;
					case CREATE_RESTRICTING:
						// RPAccount + value + RPPlan
						CreateRestrictingParam createRestrictingParam = JSONObject.parseObject(txInfo, CreateRestrictingParam.class);
						List<TransactionDetailsRPPlanResp> rpPlanResps = new ArrayList<>();
						resp.setRPAccount(createRestrictingParam.getAccount());
						BigDecimal amountSum = new BigDecimal(0);
						for(PlanParam p:createRestrictingParam.getPlan()) {
							TransactionDetailsRPPlanResp transactionDetailsRPPlanResp = new TransactionDetailsRPPlanResp();
							amountSum = amountSum.add(new BigDecimal(p.getAmount()));
							transactionDetailsRPPlanResp.setAmount(p.getAmount());
							transactionDetailsRPPlanResp.setEpoch(p.getEpoch());
							//锁仓周期对应快高  结算周期 * epoch
							transactionDetailsRPPlanResp.setBlockNumber(blockChainConfig.getSettlePeriodBlockCount()
									.multiply(new BigInteger(String.valueOf(p.getEpoch()))).longValue());
							rpPlanResps.add(transactionDetailsRPPlanResp);
						}
						//累加
						resp.setRPNum(amountSum.toString());
						resp.setRPPlan(rpPlanResps);
						break;
				default:
					break;
				}
    		}
    	}
    	return resp;
    }

    @Override
    public TransactionListResp transactionDetailNavigate( TransactionDetailNavigateReq req) {
    	TransactionListResp resp = new TransactionListResp();
    	/** 根据hash查询交易具体指 */
    	TransactionWithBLOBs currentDetail = transactionMapper.selectByPrimaryKey(req.getTxHash());
    	if(currentDetail != null) {
	    	TransactionExample condition = new TransactionExample();
	    	TransactionExample.Criteria criteria = condition.createCriteria();
	    	/** 区分是上一条数据还是下一条数据 */
			NavigateEnum navigateEnum = NavigateEnum.valueOf(req.getDirection().toUpperCase());
			if (navigateEnum == NavigateEnum.PREV) {
				criteria.andSequenceLessThan(currentDetail.getSequence());
				condition.setOrderByClause("sequence desc");
			} else if (navigateEnum == NavigateEnum.NEXT) {
				criteria.andSequenceGreaterThan(currentDetail.getSequence());
				condition.setOrderByClause("sequence asc");
			}
	    	PageHelper.startPage(1,1);
	    	List<TransactionWithBLOBs> transactions = transactionMapper.selectByExampleWithBLOBs(condition);
	    	if(!transactions.isEmpty()){
	    		/** 获取数据第一条进行转换对象 */
	        	TransactionWithBLOBs transaction = transactions.get(0);
	        	BeanUtils.copyProperties(transaction, resp);
	        	resp.setTxHash(transaction.getHash());
	        	resp.setServerTime(new Date().getTime());
	        	resp.setTimestamp(transaction.getTimestamp().getTime());
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
			resp.setNodeId(proposal.getVerifier());
			resp.setNodeName(proposal.getVerifierName());
			resp.setPipNum(proposal.getPipNum());
			resp.setProposalTitle(proposal.getTopic());
			resp.setProposalStatus(proposal.getStatus());
			resp.setProposalOption(proposal.getType());
			resp.setProposalNewVersion(proposal.getNewVersion());
			resp.setProposalUrl(proposal.getUrl());
		}
		return resp;
    }
    
    /**
     * 统一设置验证人名称
     * @method setStakingName
     * @param nodeId
     * @param nodeName
     * @return
     */
    private String setStakingName(String nodeId,String nodeName) {
    	/**
    	 * 当nodeId为空或者nodeName不为空则直接返回name
    	 */
    	if(StringUtils.isNotBlank(nodeName) || StringUtils.isBlank(nodeId)) {
    		return nodeName;
    	}
    	StakingExample stakingExample = new StakingExample();
    	stakingExample.setOrderByClause(" staking_block_num desc");
    	StakingExample.Criteria criteria = stakingExample.createCriteria();
    	criteria.andNodeIdEqualTo(nodeId);
    	List<Staking> stakings = stakingMapper.selectByExample(stakingExample);
    	if(stakings != null && !stakings.isEmpty()) {
    		return stakings.get(0).getStakingName();
    	}
    	return nodeName;
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
			if(txReceiptStatus.intValue() == TxReceiptStatusEnum.FAILURE.getCode()) {
				return defaultBaseUrl;
			}
			
			String url = keyBaseUrl.concat(keyBaseApi.concat(externalId));
            String userName = "";
			try {
				KeyBaseUser keyBaseUser = HttpUtil.get(url,KeyBaseUser.class);
				userName = KeyBaseAnalysis.getKeyBaseUseName(keyBaseUser);
			} catch (Exception e) {
				logger.error("getStakingUrl error.externalId:{},txReceiptStatus:{},error:{}",externalId, txReceiptStatus, e.getMessage());
				return defaultBaseUrl;
			}
			if(StringUtils.isNotBlank(userName)) {
				defaultBaseUrl += userName;
			}
			return defaultBaseUrl;
		}
    	return null;
    }

}

package com.platon.browser.now.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.platon.browser.config.BlockChainConfig;
import com.platon.browser.dao.entity.*;
import com.platon.browser.dao.mapper.ProposalMapper;
import com.platon.browser.dao.mapper.SlashMapper;
import com.platon.browser.dao.mapper.StakingMapper;
import com.platon.browser.dao.mapper.TransactionMapper;
import com.platon.browser.dao.mapper.UnDelegationMapper;
import com.platon.browser.dto.CustomProposal.TypeEnum;
import com.platon.browser.dto.CustomTransaction;
import com.platon.browser.dto.RespPage;
import com.platon.browser.dto.account.AccountDownload;
import com.platon.browser.dto.transaction.TransactionCacheDto;
import com.platon.browser.enums.RetEnum;
import com.platon.browser.enums.StakingStatus;
import com.platon.browser.exception.BusinessException;
import com.platon.browser.now.service.TransactionService;
import com.platon.browser.now.service.cache.StatisticCacheService;
import com.platon.browser.param.CreateRestrictingParam;
import com.platon.browser.param.CreateValidatorParam;
import com.platon.browser.param.DeclareVersionParam;
import com.platon.browser.param.DelegateParam;
import com.platon.browser.param.EditValidatorParam;
import com.platon.browser.param.EvidencesParam;
import com.platon.browser.param.ExitValidatorParam;
import com.platon.browser.param.IncreaseStakingParam;
import com.platon.browser.param.PlanParam;
import com.platon.browser.param.ReportValidatorParam;
import com.platon.browser.param.UnDelegateParam;
import com.platon.browser.redis.dto.TransactionRedis;
import com.platon.browser.req.PageReq;
import com.platon.browser.req.newtransaction.TransactionDetailNavigateReq;
import com.platon.browser.req.newtransaction.TransactionDetailsReq;
import com.platon.browser.req.newtransaction.TransactionListByAddressRequest;
import com.platon.browser.req.newtransaction.TransactionListByBlockRequest;
import com.platon.browser.res.transaction.TransactionDetailsEvidencesResp;
import com.platon.browser.res.transaction.TransactionDetailsRPPlanResp;
import com.platon.browser.res.transaction.TransactionDetailsResp;
import com.platon.browser.res.transaction.TransactionListResp;
import com.platon.browser.enums.I18nEnum;
import com.platon.browser.util.EnergonUtil;
import com.platon.browser.enums.NavigateEnum;
import com.platon.browser.enums.ReqTransactionTypeEnum;
import com.platon.browser.util.I18nUtil;
import com.univocity.parsers.csv.CsvWriter;
import com.univocity.parsers.csv.CsvWriterSettings;
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
import java.util.*;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final Logger logger = LoggerFactory.getLogger(TransactionServiceImpl.class);
    @Autowired
    private TransactionMapper transactionMapper;
    @Autowired
    private I18nUtil i18n;
//    @Autowired
//    private BlockMapper blockMapper;
    @Autowired
    private UnDelegationMapper unDelegationMapper;
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
 
//    private TransactionDetail loadDetail(TransactionDetailReq req){
//        TransactionExample condition = new TransactionExample();
//        condition.createCriteria().andHashEqualTo(req.getTxHash());
//        List<TransactionWithBLOBs> transactions = transactionMapper.selectByExampleWithBLOBs(condition);
//        if (transactions.size()>1){
//            logger.error("duplicate transaction: transaction hash {}",req.getTxHash());
//            throw new BusinessException(RetEnum.RET_FAIL.getCode(), i18n.i(I18nEnum.TRANSACTION_ERROR_DUPLICATE));
//        }
//        if(transactions.size()==0){
//            logger.error("invalid transaction hash {}",req.getTxHash());
//            throw new BusinessException(RetEnum.RET_FAIL.getCode(),i18n.i(I18nEnum.TRANSACTION_ERROR_NOT_EXIST));
//        }
//        TransactionDetail returnData = new TransactionDetail();
//        TransactionWithBLOBs initData = transactions.get(0);
//        returnData.init(initData);
//
//        String nodeId=returnData.getNodeId();
//        if(StringUtils.isBlank(nodeId)){
//            BlockExample blockExample = new BlockExample();
//            blockExample.createCriteria().andNumberEqualTo(initData.getBlockNumber());
//            List<Block> blocks = blockMapper.selectByExample(blockExample);
//            if(blocks.size()>0){
//                Block block = blocks.get(0);
//                if(StringUtils.isNotBlank(block.getNodeId())){
//                    nodeId=block.getNodeId();
//                    returnData.setNodeId(nodeId);
//                }
//            }
//        }
//
////        if(StringUtils.isNotBlank(nodeId)){
////            Map<String,String> nameMap = nodeService.getNodeNameMap(req.getCid(),Arrays.asList(returnData.getNodeId()));
////            returnData.setNodeName(nameMap.get(returnData.getNodeId()));
////        }
//
//        return returnData;
//    }
//
//    @Override
//    public TransactionDetail getDetail(TransactionDetailReq req) {
//        TransactionDetail transactionDetail = loadDetail(req);
//        return transactionDetail;
//    }

    @Override
    public RespPage<TransactionListResp> getTransactionList( PageReq req) {
        RespPage<TransactionListResp> result = new RespPage<>();
        List<TransactionListResp> lists = new LinkedList<>();
        TransactionCacheDto transactionCacheDto = statisticCacheService.getTransactionCache(req.getPageNo(), req.getPageSize());
        List<TransactionRedis> items = transactionCacheDto.getTransactionRedisList();
        TransactionListResp transactionListResp = new TransactionListResp();
        for (TransactionRedis transactionRedis:items) {
        	BeanUtils.copyProperties(transactionRedis, transactionListResp);
            transactionListResp.setValue(EnergonUtil.format(Convert.fromVon(transactionRedis.getValue(), Convert.Unit.LAT).setScale(18,RoundingMode.DOWN)));
            transactionListResp.setActualTxCost(EnergonUtil.format(Convert.fromVon(transactionRedis.getActualTxCost(), Convert.Unit.LAT).setScale(18,RoundingMode.DOWN)));
            transactionListResp.setServerTime(new Date().getTime());
            transactionListResp.setTimestamp(transactionRedis.getTimestamp().getTime());
            lists.add(transactionListResp);
        }
        Page<?> page = new Page<>(req.getPageNo(),req.getPageSize());
        result.init(page, lists);
        result.setTotalCount(transactionCacheDto.getPage().getTotalCount());
        result.setTotalPages(transactionCacheDto.getPage().getTotalPages());
        return result;
    }

    @Override
    public RespPage<TransactionListResp> getTransactionListByBlock(TransactionListByBlockRequest req) {
        RespPage<TransactionListResp> result = new RespPage<>();
        List<TransactionListResp> lists = new LinkedList<>();
        TransactionExample transactionExample = new TransactionExample();
        TransactionExample.Criteria criteria = transactionExample.createCriteria()
                .andBlockNumberEqualTo(req.getBlockNumber().longValue());
        if (req.getTxType() != null && !req.getTxType().isEmpty()) {
            criteria.andTxTypeIn(ReqTransactionTypeEnum.getTxType(req.getTxType()));
        }
        PageHelper.startPage(req.getPageNo(),req.getPageSize());
        List<TransactionWithBLOBs> items = transactionMapper.selectByExampleWithBLOBs(transactionExample);
        TransactionListResp transactionListResp = new TransactionListResp();
        for (TransactionWithBLOBs transaction:items) {
        	BeanUtils.copyProperties(transaction, transactionListResp);
            transactionListResp.setTxHash(transaction.getHash());
            transactionListResp.setValue(EnergonUtil.format(Convert.fromVon(transaction.getValue(), Convert.Unit.LAT).setScale(18,RoundingMode.DOWN)));
            transactionListResp.setActualTxCost(EnergonUtil.format(Convert.fromVon(transaction.getActualTxCost(), Convert.Unit.LAT).setScale(18,RoundingMode.DOWN)));
            transactionListResp.setServerTime(new Date().getTime());
            transactionListResp.setTimestamp(transaction.getTimestamp().getTime());
            lists.add(transactionListResp);
        }
        long count  = transactionMapper.countByExample(transactionExample);
        Page<?> page = new Page<>(req.getPageNo(),req.getPageSize());
        result.init(page, lists);
        result.setTotalCount(count);
        return result;
    }

    @Override
    public RespPage<TransactionListResp> getTransactionListByAddress(TransactionListByAddressRequest req) {
        RespPage<TransactionListResp> result = new RespPage<>();
        List<TransactionListResp> lists = new LinkedList<>();
        TransactionExample transactionExample = new TransactionExample();
        TransactionExample.Criteria first = transactionExample.createCriteria()
                .andFromEqualTo(req.getAddress());
        TransactionExample.Criteria second = transactionExample.createCriteria()
                .andToEqualTo(req.getAddress());
        if (req.getTxType() != null && !req.getTxType().isEmpty()) {
        	first.andTxTypeIn(ReqTransactionTypeEnum.getTxType(req.getTxType()));
        	second.andTxTypeIn(ReqTransactionTypeEnum.getTxType(req.getTxType()));
        }
        PageHelper.startPage(req.getPageNo(),req.getPageSize());
        transactionExample.or(first);
        transactionExample.or(second);
        List<TransactionWithBLOBs> items = transactionMapper.selectByExampleWithBLOBs(transactionExample);
        TransactionListResp transactionListResp = new TransactionListResp();
        for (TransactionWithBLOBs transaction:items) {
        	BeanUtils.copyProperties(transaction, transactionListResp);
            transactionListResp.setTxHash(transaction.getHash());
            transactionListResp.setValue(EnergonUtil.format(Convert.fromVon(transaction.getValue(), Convert.Unit.LAT).setScale(18,RoundingMode.DOWN)));
            transactionListResp.setActualTxCost(EnergonUtil.format(Convert.fromVon(transaction.getActualTxCost(), Convert.Unit.LAT).setScale(18,RoundingMode.DOWN)));
            transactionListResp.setServerTime(new Date().getTime());
            transactionListResp.setTimestamp(transaction.getTimestamp().getTime());
            lists.add(transactionListResp);
        }
        long count  = transactionMapper.countByExample(transactionExample);
        Page<?> page = new Page<>(req.getPageNo(),req.getPageSize());
        result.init(page, lists);
        result.setTotalCount(count);
        return result;
    }

    public AccountDownload transactionListByAddressDownload(String address, String date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date currentServerTime = new Date();
        logger.info("导出地址交易列表数据起始日期：{},结束日期：{}", date, simpleDateFormat.format(currentServerTime));
        TransactionExample transactionExample = new TransactionExample();
        TransactionExample.Criteria first = transactionExample.createCriteria()
                .andFromEqualTo(address);
        TransactionExample.Criteria second = transactionExample.createCriteria()
                .andToEqualTo(address);
        // 限制最多导出3万条记录
        PageHelper.startPage(1,30000);
        transactionExample.or(first);
        transactionExample.or(second);
        List<Transaction> items = transactionMapper.selectByExample(transactionExample);
        List<Object[]> rows = new ArrayList<>();

        items.forEach(transaction -> {

            boolean toIsAddress = address.equals(transaction.getTo());
            String valueIn = toIsAddress? transaction.getValue() : "";
            String valueOut = !toIsAddress? transaction.getValue() : "";

            Object[] row = {
                    transaction.getHash(),
                    transaction.getBlockNumber(),
                    transaction.getTimestamp(),
                    TypeEnum.getEnum(transaction.getTxType()).getDesc(),
                    transaction.getFrom(),
                    transaction.getTo(),
                    EnergonUtil.format(Convert.fromVon(valueIn, Convert.Unit.LAT).setScale(18,RoundingMode.DOWN)),
                    EnergonUtil.format(Convert.fromVon(valueOut, Convert.Unit.LAT).setScale(18,RoundingMode.DOWN)),
                    transaction.getGasUsed()
            };
            rows.add(row);
        });

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Writer outputWriter = new OutputStreamWriter(byteArrayOutputStream, Charset.defaultCharset());
        try {
            outputWriter.write(new String(new byte[] { (byte) 0xEF, (byte) 0xBB,(byte) 0xBF }));
        } catch (IOException e) {
            e.printStackTrace();
        }
        CsvWriter writer = new CsvWriter(outputWriter, new CsvWriterSettings());
        writer.writeHeaders(
                i18n.i(I18nEnum.DOWNLOAD_ACCOUNT_CSV_HASH),
                i18n.i(I18nEnum.DOWNLOAD_BLOCK_CSV_NUMBER),
                i18n.i(I18nEnum.DOWNLOAD_BLOCK_CSV_TIMESTAMP),
                i18n.i(I18nEnum.DOWNLOAD_ACCOUNT_CSV_TYPE),
                i18n.i(I18nEnum.DOWNLOAD_ACCOUNT_CSV_FROM),
                i18n.i(I18nEnum.DOWNLOAD_ACCOUNT_CSV_TO),
                i18n.i(I18nEnum.DOWNLOAD_ACCOUNT_CSV_VALUE_IN),
                i18n.i(I18nEnum.DOWNLOAD_ACCOUNT_CSV_VALUE_OUT),
                i18n.i(I18nEnum.DOWNLOAD_ACCOUNT_CSV_FEE)
        );
        writer.writeRowsAndClose(rows);
        AccountDownload accountDownload = new AccountDownload();
        accountDownload.setData(byteArrayOutputStream.toByteArray());
        accountDownload.setFilename("Transaction-" + address + "-" + date + ".CSV");
        accountDownload.setLength(byteArrayOutputStream.size());
        return accountDownload;
    }

    @Override
    public TransactionDetailsResp transactionDetails( TransactionDetailsReq req) {
    	TransactionWithBLOBs transaction = transactionMapper.selectByPrimaryKey(req.getTxHash());
    	TransactionDetailsResp resp = new TransactionDetailsResp();
    	if(transaction!=null) {
    		BeanUtils.copyProperties(transaction, resp);
    		resp.setTxHash(transaction.getHash());
    		resp.setTimestamp(transaction.getTimestamp().getTime());
    		resp.setServerTime(new Date().getTime());
    		// "confirmNum":444,         //区块确认数
    		TransactionCacheDto transactionCacheDto = statisticCacheService.getTransactionCache(0, 1);
    		List<TransactionRedis> items = transactionCacheDto.getTransactionRedisList();
    		resp.setConfirmNum(String.valueOf(items.get(0).getBlockNumber()-transaction.getBlockNumber()));
    		/*
    		 * "first":false,            //是否第一条记录
    		 * "last":true,              //是否最后一条记录
    		 */
    		TransactionExample condition = new TransactionExample();
    		condition.createCriteria().andSequenceLessThan(transaction.getSequence());
    		PageHelper.startPage(1,1);
    		List<Transaction> transactionList = transactionMapper.selectByExample(condition);
    		if(transactionList.size()==0){
    			resp.setFirst(true);
    		}
    		condition = new TransactionExample();
    		condition.createCriteria().andSequenceGreaterThan(transaction.getSequence());
    		PageHelper.startPage(1,1);
    		transactionList = transactionMapper.selectByExample(condition);
    		if(transactionList.size()==0){
    			resp.setLast(true);
    		}
    		//暂时只有账户合约
    		resp.setReceiveType("account");
    		String txInfo = transaction.getTxInfo();
    		switch (CustomTransaction.TxTypeEnum.getEnum(Integer.parseInt(transaction.getTxType()))) {
	    		//创建验证人
				case CREATE_VALIDATOR:
					CreateValidatorParam createValidatorParam = JSONObject.parseObject(txInfo, CreateValidatorParam.class);
					resp.setBenefitAddr(createValidatorParam.getBenefitAddress());
					resp.setNodeId(createValidatorParam.getNodeId());
					resp.setNodeName(createValidatorParam.getNodeName());
					resp.setExternalId(createValidatorParam.getExternalId());
					resp.setWebsite(createValidatorParam.getWebsite());
					resp.setDetails(createValidatorParam.getDetails());
					resp.setProgramVersion(createValidatorParam.getProgramVersion());
					resp.setValue(createValidatorParam.getAmount());
					break;
				//编辑验证人
				case EDIT_VALIDATOR:
					EditValidatorParam editValidatorParam = JSONObject.parseObject(txInfo, EditValidatorParam.class);
					resp.setBenefitAddr(editValidatorParam.getBenefitAddress());
					resp.setNodeId(editValidatorParam.getNodeId());
					resp.setNodeName(editValidatorParam.getNodeName());
					resp.setExternalId(editValidatorParam.getExternalId());
					resp.setWebsite(editValidatorParam.getWebsite());
					resp.setDetails(editValidatorParam.getDetails());
					break;
				//增加质押
				case INCREASE_STAKING:
					IncreaseStakingParam increaseStakingParam = JSONObject.parseObject(txInfo, IncreaseStakingParam.class);
					resp.setNodeId(increaseStakingParam.getNodeId());
					resp.setNodeName(increaseStakingParam.getNodeName());
					resp.setValue(increaseStakingParam.getAmount());
					break;
				//退出验证人
				case EXIT_VALIDATOR:
					// nodeId + nodeName + applyAmount + redeemLocked + redeemStatus + redeemUnLockedBlock
					ExitValidatorParam exitValidatorParam = JSONObject.parseObject(txInfo, ExitValidatorParam.class);
					resp.setNodeId(exitValidatorParam.getNodeId());
					StakingKey stakingKey = new StakingKey();
					stakingKey.setNodeId(exitValidatorParam.getNodeId());
					stakingKey.setStakingBlockNum(transaction.getBlockNumber());
					Staking staking = stakingMapper.selectByPrimaryKey(stakingKey);
					if(staking!=null) {
						resp.setNodeName(staking.getStakingName());
						//升级金额等于锁定加犹豫金额
						BigDecimal sum = new BigDecimal(staking.getStakingHas()).add(new BigDecimal(staking.getStakingLocked()));
						resp.setApplyAmount(EnergonUtil.format(Convert.fromVon(sum, Convert.Unit.LAT).setScale(18,RoundingMode.DOWN)));
						resp.setRedeemLocked(EnergonUtil.format(Convert.fromVon(staking.getStakingReduction(), Convert.Unit.LAT).setScale(18,RoundingMode.DOWN)));
						//只有已退出，则金额才会退回到账户
						resp.setRedeemStatus(staking.getStatus() == StakingStatus.EXITED.getCode()?2:1);
						//（staking_reduction_epoch  + 节点质押退回锁定周期） * 结算周期区块数(C)
						BigDecimal blockNum = (new BigDecimal(staking.getStakingReductionEpoch()).add(new BigDecimal(blockChainConfig.getUnstakeRefundSettlePeriodCount())))
								.multiply(new BigDecimal(blockChainConfig.getSettlePeriodBlockCount()));
						resp.setRedeemUnLockedBlock(blockNum.toString());
					}
					break;
					//委托
				case DELEGATE:
					DelegateParam delegateParam = JSONObject.parseObject(txInfo, DelegateParam.class);
					resp.setNodeId(delegateParam.getNodeId());
					resp.setNodeName(delegateParam.getNodeName());
					break;
				//委托赎回
				case UN_DELEGATE:
					// nodeId + nodeName + applyAmount + redeemLocked + redeemStatus
					// 通过txHash关联un_delegation表
					UnDelegateParam unDelegateParam = JSONObject.parseObject(txInfo, UnDelegateParam.class);
					resp.setNodeId(unDelegateParam.getNodeId());
					resp.setNodeName(unDelegateParam.getNodeName());
					UnDelegation unDelegation = unDelegationMapper.selectByPrimaryKey(req.getTxHash());
					if(unDelegation!=null) {
						resp.setApplyAmount(EnergonUtil.format(Convert.fromVon(unDelegation.getApplyAmount(), Convert.Unit.LAT).setScale(18,RoundingMode.DOWN)));
						resp.setRedeemLocked(EnergonUtil.format(Convert.fromVon(unDelegation.getRedeemLocked(), Convert.Unit.LAT).setScale(18,RoundingMode.DOWN)));
						resp.setRedeemStatus(unDelegation.getStatus());
					}
					break;
				case CREATE_PROPOSAL_TEXT:
//					CreateProposalTextParam createProposalTextParam = JSONObject.parseObject(txInfo, CreateProposalTextParam.class);
//					resp.setNodeId(createProposalTextParam);
//					resp.setProposalHash(createProposalTextParam.getPIDID());
//					break;
				case CREATE_PROPOSAL_UPGRADE:
//					CreateProposalUpgradeParam createProposalUpgradeParam = JSONObject.parseObject(txInfo, CreateProposalUpgradeParam.class);
	//				resp.setNodeId(createProposalTextParam);
//					resp.setProposalNewVersion(String.valueOf(createProposalUpgradeParam.getNewVersion()));
//					break;
				case CREATE_PROPOSAL_PARAMETER:
					// nodeId + nodeName + txType + proposalUrl + proposalHash + proposalNewVersion
					//TODO 暂无
					break;
				case VOTING_PROPOSAL:
					// nodeId + nodeName + txType + proposalUrl + proposalHash + proposalNewVersion +  proposalOption
//					VotingProposalParam votingProposalParam = JSONObject.parseObject(txInfo, VotingProposalParam.class);
//					resp.setProposalHash(votingProposalParam.getProposalId());
//					resp.setProposalOption(votingProposalParam.getOption());
					Proposal proposal = proposalMapper.selectByPrimaryKey(req.getTxHash());
					if(proposal != null) {
						resp.setProposalHash(proposal.getPipNum());
						resp.setPipNum(proposal.getPipId());
						resp.setProposalTitle(proposal.getTopic());
						resp.setProposalStatus(proposal.getStatus());
						resp.setProposalOption(proposal.getType());
						resp.setProposalNewVersion(proposal.getNewVersion());
					}
					break;
				case DECLARE_VERSION:
					DeclareVersionParam declareVersionParam = JSONObject.parseObject(txInfo, DeclareVersionParam.class);
					resp.setNodeId(declareVersionParam.getActiveNode());
//					resp.setNodeName(txInfoJson.getString("nodeName"));
					resp.setDeclareVersion(String.valueOf(declareVersionParam.getVersion()));
					break;
				case REPORT_VALIDATOR:
					ReportValidatorParam reportValidatorParam = JSONObject.parseObject(txInfo, ReportValidatorParam.class);
					List<TransactionDetailsEvidencesResp> transactionDetailsEvidencesResps = new ArrayList<TransactionDetailsEvidencesResp>();
					for(EvidencesParam evidencesParam: reportValidatorParam.getData()) {
						TransactionDetailsEvidencesResp transactionDetailsEvidencesResp = new TransactionDetailsEvidencesResp();
						transactionDetailsEvidencesResp.setNodeName(evidencesParam.getNodeName());
						transactionDetailsEvidencesResp.setVerify(evidencesParam.getVerify());
						transactionDetailsEvidencesResps.add(transactionDetailsEvidencesResp);
					}
					Slash slash = slashMapper.selectByPrimaryKey(req.getTxHash());
					if(slash != null) {
						resp.setReportRewards(slash.getReward());
						resp.setRedeemStatus(slash.getStatus());
						resp.setEvidence(slash.getData());
					}
					resp.setReportType(1);
					resp.setEvidences(transactionDetailsEvidencesResps);
					break;
				case CREATE_RESTRICTING:
					// RPAccount + value + RPPlan
					CreateRestrictingParam createRestrictingParam = JSONObject.parseObject(txInfo, CreateRestrictingParam.class);
					List<TransactionDetailsRPPlanResp> rpPlanResps = new ArrayList<TransactionDetailsRPPlanResp>();
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
    	return resp;
    }

    @Override
    public TransactionListResp transactionDetailNavigate( TransactionDetailNavigateReq req) {
    	TransactionWithBLOBs currentDetail = transactionMapper.selectByPrimaryKey(req.getTxHash());
    	TransactionExample condition = new TransactionExample();
    	TransactionExample.Criteria criteria = condition.createCriteria();
    	switch (NavigateEnum.valueOf(req.getDirection().toUpperCase())){
    	case PREV:
	    	criteria.andSequenceLessThan(currentDetail.getSequence());
	    	condition.setOrderByClause("sequence desc");
	    	break;
    	case NEXT:
	    	criteria.andSequenceGreaterThan(currentDetail.getSequence());
	    	condition.setOrderByClause("sequence asc");
	    	break;
    	}
    	PageHelper.startPage(1,1);
    	List<TransactionWithBLOBs> transactions = transactionMapper.selectByExampleWithBLOBs(condition);
    	if(transactions.size()==0){
    		logger.error("invalid transaction hash {}",req.getTxHash());
    		throw new BusinessException(RetEnum.RET_FAIL.getCode(), i18n.i(I18nEnum.TRANSACTION_ERROR_NOT_EXIST));
    	}

    	TransactionWithBLOBs transaction = transactions.get(0);
    	TransactionListResp resp = new TransactionListResp();
    	BeanUtils.copyProperties(transaction, resp);
    	resp.setTxHash(transaction.getHash());
    	resp.setServerTime(new Date().getTime());
    	resp.setTimestamp(transaction.getTimestamp().getTime());
    	return resp;
    }

}

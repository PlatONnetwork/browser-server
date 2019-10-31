package com.platon.browser.now.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.platon.browser.config.BlockChainConfig;
import com.platon.browser.dao.entity.NetworkStat;
import com.platon.browser.dao.entity.Proposal;
import com.platon.browser.dao.entity.ProposalExample;
import com.platon.browser.dao.entity.Transaction;
import com.platon.browser.dao.mapper.ProposalMapper;
import com.platon.browser.dao.mapper.TransactionMapper;
import com.platon.browser.dto.CustomProposal;
import com.platon.browser.enums.ErrorCodeEnum;
import com.platon.browser.enums.I18nEnum;
import com.platon.browser.enums.RetEnum;
import com.platon.browser.now.service.ProposalService;
import com.platon.browser.now.service.cache.StatisticCacheService;
import com.platon.browser.req.PageReq;
import com.platon.browser.req.proposal.ProposalDetailRequest;
import com.platon.browser.res.BaseResp;
import com.platon.browser.res.RespPage;
import com.platon.browser.res.proposal.ProposalDetailsResp;
import com.platon.browser.res.proposal.ProposalListResp;
import com.platon.browser.util.BeanConvertUtil;
import com.platon.browser.util.I18nUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * Created with IntelliJ IDEA.
 * User: 王章雄
 * Email:wangzhangxiong@juzix.net
 * Date: 2019/8/20
 * Time: 15:47
 * Desc:
 */
@Service
public class ProposalServiceImpl implements ProposalService {
    Logger logger = LoggerFactory.getLogger(ProposalServiceImpl.class);
    @Autowired
    private I18nUtil i18n;
    @Autowired
    private ProposalMapper proposalMapper;
    @Autowired
    private TransactionMapper transactionMapper;
    @Autowired
    private StatisticCacheService statisticCacheService;
    @Autowired
    private BlockChainConfig blockChainConfig;


    @Override
    public RespPage<ProposalListResp> list(PageReq req) {
        RespPage<ProposalListResp> respPage = new RespPage<>();
        respPage.setTotalCount(0);
        respPage.setTotalPages(0);
        req = req == null ? new PageReq() : req;
        Page<?> page = PageHelper.startPage(req.getPageNo(), req.getPageSize(), true);
        /** 暂时不显示总人数为0的数据，不然页面展示投票百分比事会出错   */
        ProposalExample proposalExample = new ProposalExample();
        ProposalExample.Criteria criteria = proposalExample.createCriteria();
        criteria.andAccuVerifiersNotEqualTo(0l);
        List<Proposal> list = proposalMapper.selectByExample(proposalExample);
        /** 分页查询提案数据 */
        if (!CollectionUtils.isEmpty(list)) {
            List<ProposalListResp> listResps = new ArrayList<>(list.size());
            for (Proposal proposal : list) {
                ProposalListResp proposalListResp = BeanConvertUtil.beanConvert(proposal, ProposalListResp.class);
                proposalListResp.setProposalHash(proposal.getHash());
                /** 获取统计的最新块高 */
                NetworkStat networkStatRedis = statisticCacheService.getNetworkStatCache();
                if (networkStatRedis != null) {
                    proposalListResp.setCurBlock(String.valueOf(networkStatRedis.getCurNumber()));
                }
                listResps.add(proposalListResp);
            }
            respPage.setData(listResps);
            respPage.setTotalPages(page.getPages());
            respPage.setTotalCount(page.getTotal());
        }
        return respPage;
    }

    @Override
    public BaseResp<ProposalDetailsResp> get(ProposalDetailRequest req) {
    	/** 根据hash查询提案 */
        Proposal proposal = proposalMapper.selectByPrimaryKey(req.getProposalHash());
        if (Objects.isNull(proposal)) {
            logger.error("## ERROR # get record not exist proposalHash:{}", req.getProposalHash());
            return BaseResp.build(ErrorCodeEnum.RECORD_NOT_EXIST.getCode(), i18n.i(I18nEnum.RECORD_NOT_EXIST, req.getProposalHash()), null);
        }
        ProposalDetailsResp proposalDetailsResp = BeanConvertUtil.beanConvert(proposal, ProposalDetailsResp.class);
        proposalDetailsResp.setProposalHash(req.getProposalHash());
        proposalDetailsResp.setNodeId(proposal.getNodeId());
        proposalDetailsResp.setNodeName(proposal.getNodeName());
        NetworkStat networkStat = statisticCacheService.getNetworkStatCache();
        proposalDetailsResp.setCurBlock(String.valueOf(networkStat.getCurNumber()));
        /** 不同的类型有不同的通过率 */
        switch (CustomProposal.TypeEnum.getEnum(String.valueOf(proposal.getType()))) {
			case TEXT:
				proposalDetailsResp.setSupportRateThreshold(blockChainConfig.getMinProposalTextSupportRate().toString());
				break;
			case UPGRADE:
				proposalDetailsResp.setSupportRateThreshold(blockChainConfig.getMinProposalUpgradePassRate().toString());
				break;
			case CANCEL:
				proposalDetailsResp.setSupportRateThreshold(blockChainConfig.getMinProposalCancelSupportRate().toString());
				break;
			default:
				break;
		}
        /** 不为文本提案则有生效时间 */
        if(!CustomProposal.TypeEnum.TEXT.getCode().equals(proposalDetailsResp.getType())){
	        BigDecimal actvieTime = (new BigDecimal(proposalDetailsResp.getActiveBlock()).subtract(new BigDecimal(proposal.getBlockNumber())))
	        		.multiply(new BigDecimal(blockChainConfig.getBlockInterval())).multiply(new BigDecimal(1000))
	        		.add(new BigDecimal(proposal.getTimestamp().getTime()));
	        proposalDetailsResp.setActiveBlockTime(actvieTime.longValue());
        }
        /** 结束时间预估：（生效区块-当前区块）*出块间隔 + 现有时间 */
        BigDecimal endTime = (new BigDecimal(proposalDetailsResp.getEndVotingBlock()).subtract(new BigDecimal(proposal.getBlockNumber())))
        		.multiply(new BigDecimal(blockChainConfig.getBlockInterval())).multiply(new BigDecimal(1000))
        		.add(new BigDecimal(proposal.getTimestamp().getTime()));
        proposalDetailsResp.setEndVotingBlockTime(endTime.longValue());
        /** 查询hash获取对应的交易区块号 */
        Transaction transaction = transactionMapper.selectByPrimaryKey(req.getProposalHash());
        if(transaction != null) {
        	proposalDetailsResp.setInBlock(transaction.getNum());
        }
        return BaseResp.build(RetEnum.RET_SUCCESS.getCode(), i18n.i(I18nEnum.SUCCESS), proposalDetailsResp);
    }

}

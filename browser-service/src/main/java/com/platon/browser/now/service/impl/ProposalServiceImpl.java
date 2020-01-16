package com.platon.browser.now.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.platon.browser.common.BrowserConst;
import com.platon.browser.config.BlockChainConfig;
import com.platon.browser.dao.entity.NetworkStat;
import com.platon.browser.dao.entity.Proposal;
import com.platon.browser.dao.entity.ProposalExample;
import com.platon.browser.dao.mapper.ProposalMapper;
import com.platon.browser.dto.CustomProposal;
import com.platon.browser.elasticsearch.BlockESRepository;
import com.platon.browser.elasticsearch.dto.Block;
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
import com.platon.browser.util.ConvertUtil;
import com.platon.browser.util.I18nUtil;
import com.platon.browser.utils.VerUtil;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
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
    private StatisticCacheService statisticCacheService;
    @Autowired
    private BlockChainConfig blockChainConfig;
    @Autowired
    private BlockESRepository blockESRepository;


    @Override
    public RespPage<ProposalListResp> list(PageReq req) {
        RespPage<ProposalListResp> respPage = new RespPage<>();
        req = req == null ? new PageReq() : req;
        Page<?> page = PageHelper.startPage(req.getPageNo(), req.getPageSize(), true);
        /** 暂时不显示总人数为0的数据，不然页面展示投票百分比事会出错   */
        ProposalExample proposalExample = new ProposalExample();
        proposalExample.setOrderByClause(" timestamp desc");
        ProposalExample.Criteria criteria = proposalExample.createCriteria();
        criteria.andAccuVerifiersNotEqualTo(0l);
        List<Proposal> list = proposalMapper.selectByExample(proposalExample);
        /** 分页查询提案数据 */
        if (!CollectionUtils.isEmpty(list)) {
            List<ProposalListResp> listResps = new ArrayList<>(list.size());
            for (Proposal proposal : list) {
            	/**
            	 * 循环转换数据
            	 */
            	ProposalListResp proposalListResp = new ProposalListResp();
                BeanUtils.copyProperties(proposal, proposalListResp);
                proposalListResp.setTopic(BrowserConst.INQUIRY.equals(proposal.getTopic())?"":proposal.getTopic());
                proposalListResp.setProposalHash(proposal.getHash());
                proposalListResp.setEndVotingBlock(String.valueOf(proposal.getEndVotingBlock()));
                proposalListResp.setType(String.valueOf(proposal.getType()));
                proposalListResp.setStatus(String.valueOf(proposal.getStatus()));
                proposalListResp.setTimestamp(proposal.getTimestamp().getTime());
                proposalListResp.setInBlock(proposal.getBlockNumber());
                /** 获取统计的最新块高 */
                NetworkStat networkStatRedis = statisticCacheService.getNetworkStatCache();
                if (networkStatRedis != null) {
                    proposalListResp.setCurBlock(String.valueOf(networkStatRedis.getCurNumber()));
                }
                listResps.add(proposalListResp);
            }
            respPage.init(listResps, page.getTotal(), page.getTotal(), page.getPages());
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
        ProposalDetailsResp proposalDetailsResp = new ProposalDetailsResp();
        BeanUtils.copyProperties(proposal, proposalDetailsResp);
        proposalDetailsResp.setTopic(BrowserConst.INQUIRY.equals(proposal.getTopic())?"":proposal.getTopic());
        proposalDetailsResp.setProposalHash(req.getProposalHash());
        proposalDetailsResp.setNodeId(proposal.getNodeId());
        proposalDetailsResp.setNodeName(proposal.getNodeName());
        proposalDetailsResp.setDescription(BrowserConst.INQUIRY.equals(proposal.getDescription())?"":proposal.getDescription());
        proposalDetailsResp.setCanceledTopic(BrowserConst.INQUIRY.equals(proposal.getCanceledTopic())?"":proposal.getCanceledTopic());
        proposalDetailsResp.setEndVotingBlock(String.valueOf(proposal.getEndVotingBlock()));
        proposalDetailsResp.setAccuVerifiers(String.valueOf(proposal.getAccuVerifiers()));
        proposalDetailsResp.setAbstentions(proposal.getAbstentions().intValue());
        proposalDetailsResp.setActiveBlock(String.valueOf(proposal.getActiveBlock()));
        proposalDetailsResp.setNays(proposal.getNays().intValue());
        proposalDetailsResp.setTimestamp(proposal.getTimestamp().getTime());
        proposalDetailsResp.setYeas(proposal.getYeas().intValue());
        NetworkStat networkStat = statisticCacheService.getNetworkStatCache();
        proposalDetailsResp.setCurBlock(String.valueOf(networkStat.getCurNumber()));
        /** 不同的类型有不同的通过率 */
        switch (CustomProposal.TypeEnum.getEnum(proposal.getType())) {
        	/**
         	* 文本提案
         	*/
			case TEXT:
				proposalDetailsResp.setSupportRateThreshold(blockChainConfig.getMinProposalTextSupportRate().toString());
				proposalDetailsResp.setParticipationRate(blockChainConfig.getMinProposalTextParticipationRate().toString());
				break;
			/**
         	* 升级提案
         	*/
			case UPGRADE:
				proposalDetailsResp.setSupportRateThreshold(blockChainConfig.getMinProposalUpgradePassRate().toString());
				break;
			/**
         	* 参数提案
         	*/
			case PARAMETER:
				proposalDetailsResp.setParamName(ConvertUtil.captureName(proposal.getName()));
				proposalDetailsResp.setCurrentValue(proposal.getStaleValue());
				proposalDetailsResp.setNewValue(proposal.getNewValue());
				proposalDetailsResp.setSupportRateThreshold(blockChainConfig.getParamProposalSupportRate().toString());
				proposalDetailsResp.setParticipationRate(blockChainConfig.getParamProposalVoteRate().toString());
				break;
			/**
         	* 取消提案
         	*/
			case CANCEL:
				 /**
		         * 如果被取消的提案标题没有就查询对应的提案确认返回的交易标题
		         */
		        if(StringUtils.isBlank(proposalDetailsResp.getCanceledTopic())) {
		        	Proposal cancelProposal = proposalMapper.selectByPrimaryKey(proposal.getCanceledPipId());
		        	if (cancelProposal != null && CustomProposal.TypeEnum.getEnum(cancelProposal.getType()) == CustomProposal.TypeEnum.UPGRADE) {
                            proposalDetailsResp.setCanceledTopic("版本升级-V" + VerUtil.toVersion(new BigInteger(cancelProposal.getNewVersion())));
		        	}
		        }
				proposalDetailsResp.setSupportRateThreshold(blockChainConfig.getMinProposalCancelSupportRate().toString());
				proposalDetailsResp.setParticipationRate(blockChainConfig.getMinProposalCancelParticipationRate().toString());
				break;
			default:
				break;
		}
        /**
         * 如果结束区块跟当前区块差值大于0则按照区块时间进行计算，否则直接获取已结束区块的时间
         */
        BigDecimal diff = new BigDecimal(proposalDetailsResp.getEndVotingBlock()).subtract(new BigDecimal(proposalDetailsResp.getCurBlock()));
        Block block;
        if(diff.compareTo(BigDecimal.ZERO) > 0) {
        	/** 结束时间预估：（生效区块-当前区块）*出块间隔 + 区块现有时间 */
    		try {
    			block = blockESRepository.get(proposalDetailsResp.getCurBlock(), Block.class);
                BigDecimal endTime = diff.multiply(new BigDecimal(networkStat.getAvgPackTime())).add(new BigDecimal(block.getTime().getTime()));
                proposalDetailsResp.setEndVotingBlockTime(endTime.longValue());
    		} catch (IOException e) {
    			logger.error("获取区块错误。", e);
    		}
        } else {
    		try {
    			block = blockESRepository.get(proposalDetailsResp.getEndVotingBlock(), Block.class);
                proposalDetailsResp.setEndVotingBlockTime(block.getTime().getTime());
    		} catch (IOException e) {
    			logger.error("获取区块错误。", e);
    		}
        }
        
    	proposalDetailsResp.setInBlock(proposal.getBlockNumber());
        return BaseResp.build(RetEnum.RET_SUCCESS.getCode(), i18n.i(I18nEnum.SUCCESS), proposalDetailsResp);
    }

}

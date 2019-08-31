package com.platon.browser.now.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.platon.browser.config.BlockChainConfig;
import com.platon.browser.dao.entity.Block;
import com.platon.browser.dao.entity.NetworkStat;
import com.platon.browser.dao.entity.Proposal;
import com.platon.browser.dao.mapper.ProposalMapper;
import com.platon.browser.dto.CustomProposal;
import com.platon.browser.enums.ErrorCodeEnum;
import com.platon.browser.enums.I18nEnum;
import com.platon.browser.enums.RetEnum;
import com.platon.browser.exception.BusinessException;
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
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
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


    @Override
    public RespPage<ProposalListResp> list(PageReq req) {
        RespPage<ProposalListResp> respPage = new RespPage<>();
        respPage.setTotalCount(0);
        respPage.setTotalPages(0);
        req = req == null ? new PageReq() : req;
        Page<?> page = PageHelper.startPage(req.getPageNo(), req.getPageSize(), true);
        List<Proposal> list = proposalMapper.selectByExample(null);
        /** 分页查询提案数据 */
        if (!CollectionUtils.isEmpty(list)) {
            List<ProposalListResp> listResps = new ArrayList<>(list.size());
            for (Proposal proposal : list) {
                ProposalListResp proposalListResp = BeanConvertUtil.beanConvert(proposal, ProposalListResp.class);
                proposalListResp.setProposalHash(proposal.getHash());
                /** 获取统计的最新块高 */
                NetworkStat networkStatRedis = statisticCacheService.getNetworkStatCache();
                if (networkStatRedis != null) {
                    proposalListResp.setCurBlock(String.valueOf(networkStatRedis.getCurrentNumber()));
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
        proposalDetailsResp.setNodeId(proposal.getVerifier());
        proposalDetailsResp.setNodeName(proposal.getVerifierName());
        List<Block> items = statisticCacheService.getBlockCache(0, 1);
        if (items != null && items.size() > 0) {
            proposalDetailsResp.setCurBlock(items.get(0).getNumber().toString());
        }
        /** 赞成百分比 */
        proposalDetailsResp.setSupportRateThreshold(composeRate(proposal.getYeas(), proposal.getAccuVerifiers()));
        /** 弃权百分比 */
        proposalDetailsResp.setAbstainRateThreshold(composeRate(proposal.getAbstentions(), proposal.getAccuVerifiers()));
        /**反对百分比 */
        proposalDetailsResp.setOpposeRateThreshold(composeRate(proposal.getNays(), proposal.getAccuVerifiers()));
        /** 不为文本提案则有生效时间 */
        if(!CustomProposal.TypeEnum.TEXT.getCode().equals(proposalDetailsResp.getType())){
	        BigDecimal actvieTime = (new BigDecimal(proposalDetailsResp.getActiveBlock()).subtract(new BigDecimal(proposalDetailsResp.getCurBlock()))) 
	        		.multiply(new BigDecimal(blockChainConfig.getBlockInterval())).add(new BigDecimal(new Date().getTime()));
	        proposalDetailsResp.setActiveBlockTime(actvieTime.longValue());
        }
        /** 结束时间预估：（生效区块-当前区块）*出块间隔 + 现有时间 */
        BigDecimal endTime = (new BigDecimal(proposalDetailsResp.getEndVotingBlock()).subtract(new BigDecimal(proposalDetailsResp.getCurBlock()))) 
        		.multiply(new BigDecimal(blockChainConfig.getBlockInterval())).add(new BigDecimal(new Date().getTime()));
        proposalDetailsResp.setEndVotingBlockTime(endTime.longValue());
        return BaseResp.build(RetEnum.RET_SUCCESS.getCode(), i18n.i(I18nEnum.SUCCESS), proposalDetailsResp);
    }

    /**
     * 计算百分比
     *
     * @param divisor  除数
     * @param dividend 被除数
     * @return
     */
    private String composeRate(Long divisor, Long dividend) {
        if (dividend == null || dividend == 0L) {
            throw new BusinessException(i18n.i(I18nEnum.NODE_ERROR_NOT_EXIST));
        }
        if (divisor == null || divisor == 0L) {
            return "0.00%";
        }
        if (divisor == dividend) {
            return "100.00%";
        }
        /** 设置保留位数 */
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        return decimalFormat.format((float) divisor * 100 / dividend) + "%";

    }
}

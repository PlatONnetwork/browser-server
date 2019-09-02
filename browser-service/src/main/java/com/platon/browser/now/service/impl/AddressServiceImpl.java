package com.platon.browser.now.service.impl;

import com.github.pagehelper.PageHelper;
import com.platon.browser.client.PlatonClient;
import com.platon.browser.config.BlockChainConfig;
import com.platon.browser.dao.entity.Address;
import com.platon.browser.dao.entity.NetworkStat;
import com.platon.browser.dao.entity.RpPlan;
import com.platon.browser.dao.entity.RpPlanExample;
import com.platon.browser.dao.mapper.AddressMapper;
import com.platon.browser.dao.mapper.RpPlanMapper;
import com.platon.browser.enums.I18nEnum;
import com.platon.browser.exception.BusinessException;
import com.platon.browser.now.service.AddressService;
import com.platon.browser.now.service.cache.StatisticCacheService;
import com.platon.browser.req.address.QueryDetailRequest;
import com.platon.browser.req.address.QueryRPPlanDetailRequest;
import com.platon.browser.res.address.DetailsRPPlanResp;
import com.platon.browser.res.address.QueryDetailResp;
import com.platon.browser.res.address.QueryRPPlanDetailResp;
import com.platon.browser.util.I18nUtil;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.platon.BaseResponse;
import org.web3j.platon.bean.RestrictingItem;

/**
 * 地址具体逻辑实现方法
 *  @file AddressServiceImpl.java
 *  @description 
 *	@author zhangrj
 *  @data 2019年8月31日
 */
@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    private AddressMapper addressMapper;
    
    @Autowired
    private RpPlanMapper rpPlanMapper;

    @Autowired
    private PlatonClient platonClient;

    @Autowired
    private I18nUtil i18n;
    
    @Autowired
    private BlockChainConfig blockChainConfig;
    
    @Autowired
    private StatisticCacheService statisticCacheService;
    
    @Override
    public QueryDetailResp getDetails(QueryDetailRequest req) {
    	/** 根据主键查询地址信息 */
        Address item = addressMapper.selectByPrimaryKey(req.getAddress());
        QueryDetailResp resp = new QueryDetailResp();
        if (item != null) {
        	BeanUtils.copyProperties(item, resp);
        }
       return resp;
    }

	@Override
	public QueryRPPlanDetailResp rpplanDetail(QueryRPPlanDetailRequest req) {
		QueryRPPlanDetailResp queryRPPlanDetailResp = new QueryRPPlanDetailResp();
		try {
			/**
			 * 链上实时查询对应的锁仓信息
			 */
			BaseResponse<RestrictingItem> baseResponse = platonClient.getRestrictingPlanContract().getRestrictingInfo(req.getAddress()).send();
			if(baseResponse.status) {
				queryRPPlanDetailResp.setRestrictingBalance(baseResponse.data.getBalance().toString());
				queryRPPlanDetailResp.setStakingValue(baseResponse.data.getPledge().toString());
				queryRPPlanDetailResp.setUnderreleaseValue(baseResponse.data.getDebt().toString());
			} 
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusinessException(i18n.i(I18nEnum.SYSTEM_EXCEPTION));
		}
		/**
		 * 查询当前的统计信息
		 */
		NetworkStat networkStat = statisticCacheService.getNetworkStatCache();
		/**
		 * 分页查询对应的锁仓计划
		 */
		RpPlanExample rpPlanExample = new RpPlanExample();
		RpPlanExample.Criteria criteria = rpPlanExample.createCriteria();
		criteria.andAddressEqualTo(req.getAddress());
		List<DetailsRPPlanResp> detailsRPPlanResps = new ArrayList<DetailsRPPlanResp>();
		PageHelper.startPage(req.getPageNo(),req.getPageSize());
		List<RpPlan> rpPlans = rpPlanMapper.selectByExample(rpPlanExample);
		BigDecimal totalValue = new BigDecimal(0);
		for(RpPlan rPlan : rpPlans) {
			DetailsRPPlanResp detailsRPPlanResp = new DetailsRPPlanResp();
			BeanUtils.copyProperties(rPlan, detailsRPPlanResp);
			//锁仓周期对应快高  结算周期 * epoch  
			Long number = blockChainConfig.getSettlePeriodBlockCount()
			.multiply(new BigInteger(String.valueOf(rPlan.getEpoch()))).longValue();
			detailsRPPlanResp.setBlockNumber(number);
			//预计时间：预计块高减去当前块高乘以出块时间再加上现在时间
			detailsRPPlanResp.setEstimateTime(blockChainConfig.getBlockInterval() * (networkStat.getCurrentNumber() - number) 
					+ new Date().getTime());
			detailsRPPlanResps.add(detailsRPPlanResp);
			totalValue = totalValue.add(new BigDecimal(rPlan.getAmount()));
		}
		queryRPPlanDetailResp.setRPPlan(detailsRPPlanResps);
		queryRPPlanDetailResp.setTotalValue(totalValue.toString());
		/**
		 * 获取列表总数
		 */
		queryRPPlanDetailResp.setTotal(rpPlanMapper.countByExample(rpPlanExample));
		return queryRPPlanDetailResp;
	}
}

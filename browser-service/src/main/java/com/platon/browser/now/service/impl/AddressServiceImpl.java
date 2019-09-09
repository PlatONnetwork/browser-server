package com.platon.browser.now.service.impl;

import com.github.pagehelper.PageHelper;
import com.platon.browser.client.PlatonClient;
import com.platon.browser.config.BlockChainConfig;
import com.platon.browser.dao.entity.Address;
import com.platon.browser.dao.entity.Block;
import com.platon.browser.dao.entity.RpPlan;
import com.platon.browser.dao.entity.RpPlanExample;
import com.platon.browser.dao.mapper.AddressMapper;
import com.platon.browser.dao.mapper.BlockMapper;
import com.platon.browser.dao.mapper.CustomRpPlanMapper;
import com.platon.browser.dao.mapper.RpPlanMapper;
import com.platon.browser.enums.I18nEnum;
import com.platon.browser.exception.BusinessException;
import com.platon.browser.now.service.AddressService;
import com.platon.browser.req.address.QueryDetailRequest;
import com.platon.browser.req.address.QueryRPPlanDetailRequest;
import com.platon.browser.res.address.DetailsRPPlanResp;
import com.platon.browser.res.address.QueryDetailResp;
import com.platon.browser.res.address.QueryRPPlanDetailResp;
import com.platon.browser.util.I18nUtil;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
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
    private CustomRpPlanMapper customRpPlanMapper;
    
    @Autowired
    private PlatonClient platonClient;

    @Autowired
    private I18nUtil i18n;
    
    @Autowired
    private BlockChainConfig blockChainConfig;
    
    @Autowired
    private BlockMapper blockMapper;
    
    @Override
    public QueryDetailResp getDetails(QueryDetailRequest req) {
    	/** 根据主键查询地址信息 */
        Address item = addressMapper.selectByPrimaryKey(req.getAddress());
        QueryDetailResp resp = new QueryDetailResp();
        if (item != null) {
        	BeanUtils.copyProperties(item, resp);
        	/** 预先设置是否展示锁仓 */
        	resp.setIsRestricting(0);
        }
        RpPlanExample rpPlanExample = new RpPlanExample();
		RpPlanExample.Criteria criteria = rpPlanExample.createCriteria();
		criteria.andAddressEqualTo(req.getAddress()); 
        List<RpPlan> rpPlans = rpPlanMapper.selectByExample(rpPlanExample);
        /** 有锁仓数据之后就可以返回1 */
        if(rpPlans.size() > 0) {
        	resp.setIsRestricting(1);
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
				/**
				 * 可用余额为balance减去质押金额
				 */
				queryRPPlanDetailResp.setRestrictingBalance(baseResponse.data.getBalance().subtract(baseResponse.data.getPledge()).toString());
				queryRPPlanDetailResp.setStakingValue(baseResponse.data.getPledge().toString());
				queryRPPlanDetailResp.setUnderreleaseValue(baseResponse.data.getDebt().toString());
			} 
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusinessException(i18n.i(I18nEnum.SYSTEM_EXCEPTION));
		}
		/**
		 * 分页查询对应的锁仓计划
		 */
		RpPlanExample rpPlanExample = new RpPlanExample();
		RpPlanExample.Criteria criteria = rpPlanExample.createCriteria();
		criteria.andAddressEqualTo(req.getAddress()); 
		List<DetailsRPPlanResp> detailsRPPlanResps = new ArrayList<DetailsRPPlanResp>();
		PageHelper.startPage(req.getPageNo(),req.getPageSize());
		List<RpPlan> rpPlans = rpPlanMapper.selectByExample(rpPlanExample);
		for(RpPlan rPlan : rpPlans) {
			DetailsRPPlanResp detailsRPPlanResp = new DetailsRPPlanResp();
			BeanUtils.copyProperties(rPlan, detailsRPPlanResp);
			/**
			 * 锁仓周期对应快高  结算周期数 * epoch  + number,如果不是整数倍则为：结算周期 * （epoch-1）  + 多余的数目
			 */
			Long number = 0l;
			long remainder = rPlan.getNumber() % blockChainConfig.getSettlePeriodBlockCount().longValue();
			if(remainder == 0l) {
				number = blockChainConfig.getSettlePeriodBlockCount()
						.multiply(BigInteger.valueOf(rPlan.getEpoch())).add(BigInteger.valueOf(rPlan.getNumber())).longValue();
			} else {
				number = blockChainConfig.getSettlePeriodBlockCount()
						.multiply(BigInteger.valueOf(rPlan.getEpoch() - 1)).add(BigInteger.valueOf(rPlan.getNumber()))
						.add(blockChainConfig.getSettlePeriodBlockCount().subtract(BigInteger.valueOf(remainder))).longValue();
			}

			detailsRPPlanResp.setBlockNumber(number);
			//预计时间：预计块高减去当前块高乘以出块时间再加上区块时间
			Block block = blockMapper.selectByPrimaryKey(rPlan.getNumber());
			detailsRPPlanResp.setEstimateTime(blockChainConfig.getBlockInterval() * (number - rPlan.getNumber()) 
					+ block.getTimestamp().getTime());
			detailsRPPlanResps.add(detailsRPPlanResp);
		}
		queryRPPlanDetailResp.setRPPlan(detailsRPPlanResps);
		/**
		 * 获取计算总数
		 */
		BigDecimal bigDecimal = customRpPlanMapper.selectSumByAddress(req.getAddress());
		if(bigDecimal != null) {
			queryRPPlanDetailResp.setTotalValue(bigDecimal.toString());
		}
		/**
		 * 获取列表总数
		 */
		queryRPPlanDetailResp.setTotal(rpPlanMapper.countByExample(rpPlanExample));
		return queryRPPlanDetailResp;
	}
}

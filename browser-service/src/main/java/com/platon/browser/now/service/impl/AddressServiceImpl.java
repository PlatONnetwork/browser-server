package com.platon.browser.now.service.impl;

import com.github.pagehelper.PageHelper;
import com.platon.browser.client.PlatOnClient;
import com.platon.browser.common.BrowserConst;
import com.platon.browser.config.BlockChainConfig;
import com.platon.browser.dao.entity.Address;
import com.platon.browser.dao.entity.RpPlan;
import com.platon.browser.dao.entity.RpPlanExample;
import com.platon.browser.dao.mapper.AddressMapper;
import com.platon.browser.dao.mapper.CustomRpPlanMapper;
import com.platon.browser.dao.mapper.RpPlanMapper;
import com.platon.browser.elasticsearch.BlockESRepository;
import com.platon.browser.elasticsearch.dto.Block;
import com.platon.browser.elasticsearch.dto.ESResult;
import com.platon.browser.elasticsearch.service.impl.ESQueryBuilderConstructor;
import com.platon.browser.elasticsearch.service.impl.ESQueryBuilders;
import com.platon.browser.enums.I18nEnum;
import com.platon.browser.exception.BusinessException;
import com.platon.browser.now.service.AddressService;
import com.platon.browser.req.address.QueryDetailRequest;
import com.platon.browser.req.address.QueryRPPlanDetailRequest;
import com.platon.browser.res.address.DetailsRPPlanResp;
import com.platon.browser.res.address.QueryDetailResp;
import com.platon.browser.res.address.QueryRPPlanDetailResp;
import com.platon.browser.util.I18nUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.platon.BaseResponse;
import org.web3j.platon.bean.RestrictingItem;
import org.web3j.protocol.core.DefaultBlockParameterName;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * 地址具体逻辑实现方法
 *  @file AddressServiceImpl.java
 *  @description
 *	@author zhangrj
 *  @data 2019年8月31日
 */
@Service
public class AddressServiceImpl implements AddressService {

	private final Logger logger = LoggerFactory.getLogger(AddressServiceImpl.class);

    @Autowired
    private AddressMapper addressMapper;

    @Autowired
    private RpPlanMapper rpPlanMapper;

    @Autowired
    private CustomRpPlanMapper customRpPlanMapper;

    @Autowired
    private PlatOnClient platonClient;

    @Autowired
    private I18nUtil i18n;

    @Autowired
    private BlockChainConfig blockChainConfig;
    
    @Autowired
	private BlockESRepository blockESRepository;

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
        /** 特殊账户余额直接查询链  */
        if(BrowserConst.ACCOUNT.equals(req.getAddress())) {
        	try {
				BigInteger balance = platonClient.getWeb3j().platonGetBalance(req.getAddress(),DefaultBlockParameterName.LATEST).send().getBalance();
				resp.setBalance(balance.toString());
        	} catch (IOException e) {
				logger.error("",e);
			}
        }
        RpPlanExample rpPlanExample = new RpPlanExample();
		RpPlanExample.Criteria criteria = rpPlanExample.createCriteria();
		criteria.andAddressEqualTo(req.getAddress());
        List<RpPlan> rpPlans = rpPlanMapper.selectByExample(rpPlanExample);
        /** 有锁仓数据之后就可以返回1 */
        if(rpPlans != null && !rpPlans.isEmpty()) {
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
			if(baseResponse.isStatusOk()) {
				/**
				 * 可用余额为balance减去质押金额
				 */
				queryRPPlanDetailResp.setRestrictingBalance(baseResponse.data.getBalance().subtract(baseResponse.data.getPledge()).toString());
				queryRPPlanDetailResp.setStakingValue(baseResponse.data.getPledge().toString());
				queryRPPlanDetailResp.setUnderReleaseValue(baseResponse.data.getDebt().toString());
			}
		} catch (Exception e) {
			logger.error("rpplanDetail error", e);
			throw new BusinessException(i18n.i(I18nEnum.SYSTEM_EXCEPTION));
		}
		/**
		 * 分页查询对应的锁仓计划
		 */
		RpPlanExample rpPlanExample = new RpPlanExample();
		RpPlanExample.Criteria criteria = rpPlanExample.createCriteria();
		criteria.andAddressEqualTo(req.getAddress());
		List<DetailsRPPlanResp> detailsRPPlanResps = new ArrayList<>();
		PageHelper.startPage(req.getPageNo(),req.getPageSize());
		List<RpPlan> rpPlans = rpPlanMapper.selectByExample(rpPlanExample);
		for(RpPlan rPlan : rpPlans) {
			DetailsRPPlanResp detailsRPPlanResp = new DetailsRPPlanResp();
			BeanUtils.copyProperties(rPlan, detailsRPPlanResp);
			/**
			 * 锁仓周期对应快高  结算周期数 * epoch  + number,如果不是整数倍则为：结算周期 * （epoch-1）  + 多余的数目
			 */
			Long number;
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
			/** 预计时间：预计块高减去当前块高乘以出块时间再加上区块时间 */
			ESQueryBuilderConstructor constructor = new ESQueryBuilderConstructor();
			constructor.must(new ESQueryBuilders().term("num", rPlan.getNumber()));
			ESResult<Block> blockList = new ESResult<>();
			try {
				blockList = blockESRepository.search(constructor, Block.class, 1, 1);
			} catch (IOException e) {
				logger.error("获取区块错误。", e);
			}
			detailsRPPlanResp.setEstimateTime(blockChainConfig.getBlockInterval().multiply(BigInteger.valueOf(number - rPlan.getNumber()))
					.multiply(BigInteger.valueOf(1000)).add(BigInteger.valueOf(blockList.getRsData().get(0).getTime().getTime())).longValue());
			detailsRPPlanResps.add(detailsRPPlanResp);
		}
		queryRPPlanDetailResp.setRpPlans(detailsRPPlanResps);
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

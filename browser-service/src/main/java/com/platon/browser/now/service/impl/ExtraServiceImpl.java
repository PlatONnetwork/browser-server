package com.platon.browser.now.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.platon.browser.dao.entity.Config;
import com.platon.browser.dao.mapper.ConfigMapper;
import com.platon.browser.now.service.ExtraService;
import com.platon.browser.res.extra.ConfigDetail;
import com.platon.browser.res.extra.ModuleConfig;
import com.platon.browser.res.extra.QueryConfigResp;
import com.platon.browser.util.ConvertUtil;

/**
 * 实现
 *  @file ExtraServiceImpl.java
 *  @description 
 *	@author zhangrj
 *  @data 2019年11月25日
 */
@Service
public class ExtraServiceImpl implements ExtraService {

	@Autowired
	private ConfigMapper configMapper;

	@Override
	public QueryConfigResp queryConfig() {
		QueryConfigResp queryConfigResp = new QueryConfigResp();
		List<ModuleConfig> moduleConfigs = new ArrayList<>();
		List<Config> configs = configMapper.selectByExample(null);
		/**
		 * 设置质押模块的配置
		 */
		ModuleConfig stakingModuleConfig = new ModuleConfig();
		stakingModuleConfig.setModule("Staking");
		/**
		 * 设置惩罚模块的配置
		 */
		ModuleConfig slashingModuleConfig = new ModuleConfig();
		slashingModuleConfig.setModule("Slashing");
		/**
		 * 设置区块模块的配置
		 */
		ModuleConfig blockModuleConfig = new ModuleConfig();
		blockModuleConfig.setModule("Block");
		List<ConfigDetail> stakingConfigDetails = new ArrayList<>();
		List<ConfigDetail> slashingConfigDetails = new ArrayList<>();
		List<ConfigDetail> blockConfigDetails = new ArrayList<>();
		String maxEvidenceAge = "";
		String unStakeFreezeDuration = "";
		for(Config config:configs) {
			ConfigDetail configDetail = new ConfigDetail();
			/**
			 * 正则表达式拆分配置，其中$1=开始的开闭区间。$2=开始区间。$2=结束的区间。$4=结束的开闭区间。
			 */
			String regex = ".*([\\(\\[])(.*),(.*)([\\)\\]]).*";//()表示分组
			String res1 = config.getRangeDesc().replaceAll(regex, "$1");
	        String res2 = config.getRangeDesc().replaceAll(regex, "$2");
	        String res3 = config.getRangeDesc().replaceAll(regex, "$3");
	        String res4 = config.getRangeDesc().replaceAll(regex, "$4");
			configDetail.setStart(res1);
			configDetail.setStartValue(res2);
			configDetail.setEndValue(res3);
			configDetail.setEnd(res4);
			switch (config.getModule()) {
			case "staking":
				BeanUtils.copyProperties(config, configDetail);
				stakingConfigDetails.add(configDetail);
				break;
			case "slashing":
				BeanUtils.copyProperties(config, configDetail);
				slashingConfigDetails.add(configDetail);
				break;
			case "block":
				BeanUtils.copyProperties(config, configDetail);
				blockConfigDetails.add(configDetail);
				break;
			default:
				break;
			}
			configDetail.setName(ConvertUtil.captureName(config.getName()));
			/**
			 * 设置质押和证据的上下区块
			 */
			switch (config.getName()) {
			case "unStakeFreezeDuration":
				unStakeFreezeDuration = config.getValue();
				break;
			case "maxEvidenceAge":
				maxEvidenceAge = config.getValue();
				break;

			default:
				break;
			}
		}
		/**
		 * 设置质押的开始区间值
		 */
		for(ConfigDetail stakingConfig:stakingConfigDetails) {
			if("UnStakeFreezeDuration".equals(stakingConfig.getName())) {
				stakingConfig.setStartValue(maxEvidenceAge);
				break;
			}
		}
		/**
		 * 设置惩罚证据的区间值
		 */
		for(ConfigDetail slashingConfig:slashingConfigDetails) {
			if("MaxEvidenceAge".equals(slashingConfig.getName())) {
				slashingConfig.setEndValue(unStakeFreezeDuration);
				break;
			}
		}
		/**
		 * 设置不同模块的详情
		 */
		stakingModuleConfig.setDetail(stakingConfigDetails);
		slashingModuleConfig.setDetail(slashingConfigDetails);
		blockModuleConfig.setDetail(blockConfigDetails);
		moduleConfigs.add(stakingModuleConfig);
		moduleConfigs.add(slashingModuleConfig);
		moduleConfigs.add(blockModuleConfig);
		queryConfigResp.setConfig(moduleConfigs);
		return queryConfigResp;
	}
	
	

}

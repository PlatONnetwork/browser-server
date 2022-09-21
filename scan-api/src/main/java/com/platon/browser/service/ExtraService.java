package com.platon.browser.service;

import com.platon.browser.constant.Browser;
import com.platon.browser.dao.entity.Config;
import com.platon.browser.dao.mapper.ConfigMapper;
import com.platon.browser.response.extra.ConfigDetail;
import com.platon.browser.response.extra.ModuleConfig;
import com.platon.browser.response.extra.QueryConfigResp;
import com.platon.utils.Convert;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 * 实现
 *
 * @author zhangrj
 * @file ExtraServiceImpl.java
 * @description
 * @data 2019年11月25日
 */
@Service
public class ExtraService {

    @Resource
    private ConfigMapper configMapper;

    private static String stakingValue = "staking";

    private static String slashingValue = "slashing";

    private static String blockValue = "block";

    private static String rewardValue = "reward";

    private static String restrictingValue = "restricting";

    public QueryConfigResp queryConfig() {
        QueryConfigResp queryConfigResp = new QueryConfigResp();
        List<ModuleConfig> moduleConfigs = new ArrayList<>();
        List<Config> configs = configMapper.selectByExample(null);
        /**
         * 设置质押模块的配置
         */
        ModuleConfig stakingModuleConfig = new ModuleConfig();
        stakingModuleConfig.setModule(stakingValue);
        /**
         * 设置惩罚模块的配置
         */
        ModuleConfig slashingModuleConfig = new ModuleConfig();
        slashingModuleConfig.setModule(slashingValue);
        /**
         * 设置区块模块的配置
         */
        ModuleConfig blockModuleConfig = new ModuleConfig();
        blockModuleConfig.setModule(blockValue);
        /**
         * 设置收益比例模块的配置
         */
        ModuleConfig rewardModuleConfig = new ModuleConfig();
        rewardModuleConfig.setModule(rewardValue);
        /**
         * 设置M参数模块的配置
         */
        ModuleConfig restrictingModuleConfig = new ModuleConfig();
        restrictingModuleConfig.setModule(restrictingValue);

        List<ConfigDetail> stakingConfigDetails = new ArrayList<>();
        List<ConfigDetail> slashingConfigDetails = new ArrayList<>();
        List<ConfigDetail> blockConfigDetails = new ArrayList<>();
        List<ConfigDetail> rewardConfigDetails = new ArrayList<>();
        List<ConfigDetail> restrictingConfigDetails = new ArrayList<>();
        String maxEvidenceAge = "";
        String unStakeFreezeDuration = "";
        String zeroProduceCumulativeTime = "";
        String zeroProduceNumberThreshold = "";
        for (Config config : configs) {
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
            configDetail.setEnd(res4);
            BeanUtils.copyProperties(config, configDetail);
            /**
             * lat转换单位
             */
            if (Browser.EXTRA_LAT_PARAM.contains(config.getName())) {
                if (StringUtils.isNotBlank(res2)) {
                    configDetail.setStartValue(Convert.fromVon(res2.trim(), Convert.Unit.KPVON).setScale(18, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString());
                }
                if (StringUtils.isNotBlank(res3)) {
                    configDetail.setEndValue(Convert.fromVon(res3.trim(), Convert.Unit.KPVON).setScale(18, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString());
                }
                if (StringUtils.isNotBlank(config.getValue())) {
                    configDetail.setValue(Convert.fromVon(config.getValue(), Convert.Unit.KPVON).setScale(18, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString());
                }
                if (StringUtils.isNotBlank(config.getInitValue())) {
                    configDetail.setInitValue(Convert.fromVon(config.getInitValue(), Convert.Unit.KPVON).setScale(18, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString());
                }
            } else {
                configDetail.setStartValue(res2.trim());
                configDetail.setEndValue(res3.trim());
            }
            /**
             * 百分比转换
             */
            if (Browser.EXTRA_PECENT_PARAM.contains(config.getName())) {
                configDetail.setValue(new BigDecimal(config.getValue()).setScale(18, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString());
                configDetail.setInitValue(new BigDecimal(config.getInitValue()).setScale(18, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString());
            }
            switch (config.getModule()) {
                case "staking":
                    stakingConfigDetails.add(configDetail);
                    break;
                case "slashing":
                    slashingConfigDetails.add(configDetail);
                    break;
                case "block":
                    blockConfigDetails.add(configDetail);
                    break;
                case "reward":
                    rewardConfigDetails.add(configDetail);
                    break;
                case "restricting":
                    restrictingConfigDetails.add(configDetail);
                    break;
                default:
                    break;
            }

            configDetail.setName(config.getName());
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
                case "zeroProduceCumulativeTime":
                    zeroProduceCumulativeTime = config.getValue();
                    break;
                case "zeroProduceNumberThreshold":
                    zeroProduceNumberThreshold = config.getValue();
                    break;

                default:
                    break;
            }
        }
        /**
         * 设置质押的区间值
         */
        for (ConfigDetail stakingConfig : stakingConfigDetails) {
            if ("unStakeFreezeDuration".equalsIgnoreCase(stakingConfig.getName())) {
                stakingConfig.setStartValue(maxEvidenceAge);
            }
            if ("unDelegateFreezeDuration".equalsIgnoreCase(stakingConfig.getName())) {
                stakingConfig.setStart("(");
                stakingConfig.setStartValue("0");
                stakingConfig.setEndValue("unStakeFreezeDuration");
            }
        }
        /**
         * 设置惩罚证据的区间值
         */
        for (ConfigDetail slashingConfig : slashingConfigDetails) {
            if ("maxEvidenceAge".equals(slashingConfig.getName())) {
                slashingConfig.setEndValue(unStakeFreezeDuration);
            }
            if ("zeroProduceCumulativeTime".equals(slashingConfig.getName())) {
                slashingConfig.setStartValue(zeroProduceNumberThreshold);
            }
            if ("zeroProduceNumberThreshold".equals(slashingConfig.getName())) {
                slashingConfig.setEndValue(zeroProduceCumulativeTime);
            }
            if ("zeroProduceFreezeDuration".equals(slashingConfig.getName())) {
                slashingConfig.setEndValue(unStakeFreezeDuration);
            }
        }
        /**
         * 设置不同模块的详情
         */
        stakingModuleConfig.setDetail(stakingConfigDetails);
        slashingModuleConfig.setDetail(slashingConfigDetails);
        blockModuleConfig.setDetail(blockConfigDetails);
        rewardModuleConfig.setDetail(rewardConfigDetails);
        restrictingModuleConfig.setDetail(restrictingConfigDetails);
        moduleConfigs.add(stakingModuleConfig);
        moduleConfigs.add(slashingModuleConfig);
        moduleConfigs.add(blockModuleConfig);
        moduleConfigs.add(rewardModuleConfig);
        moduleConfigs.add(restrictingModuleConfig);
        queryConfigResp.setConfig(moduleConfigs);
        return queryConfigResp;
    }

}

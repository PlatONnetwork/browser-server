package com.platon.browser.v0150.service;

import com.platon.browser.client.PlatOnClient;
import com.platon.browser.dao.entity.Config;
import com.platon.browser.dao.entity.ConfigExample;
import com.platon.browser.dao.mapper.ConfigMapper;
import com.platon.browser.elasticsearch.dto.Block;
import com.platon.browser.enums.ModifiableGovernParamEnum;
import com.platon.browser.v0150.V0150Config;
import com.platon.contracts.ppos.dto.resp.GovernParam;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

/**
 * 治理参数调整（添加、中途调整）服务
 */
@Service
public class RestrictingMinimumReleaseParamService {

    // alaya版本特殊处理版本升级，在结算周期调用proposalContract的getActiveVersion()方法，看返回值是否与本字段值一致，
    // 如果一致，则把检测数据库中是否存在锁仓最小释放金额配置，如果不存在则插入此数据
    @Resource
    private V0150Config v0150Config;
    @Resource
    private ConfigMapper configMapper;
    @Resource
    private PlatOnClient platOnClient;

    public void checkRestrictingMinimumReleaseParam(Block block) throws Exception {
        ConfigExample example = new ConfigExample();
        String moduleName = ModifiableGovernParamEnum.RESTRICTING_MINIMUM_RELEASE.getModule();
        String paramName = ModifiableGovernParamEnum.RESTRICTING_MINIMUM_RELEASE.getName();
        example.createCriteria().andNameEqualTo(paramName);
        List<Config> configs = configMapper.selectByExample(example);

        if(configs.isEmpty()){
            // 检查链上生效版本大于等于配置文件中指定的版本，则插入锁仓最小释放金额参数
            BigInteger chainVersion = platOnClient.getProposalContract().getActiveVersion().send().getData();
            if(chainVersion.compareTo(v0150Config.getRestrictingMinimumReleaseActiveVersion())>=0) return;

            // 如果不存在minimumRelease则从链上查询指定模块的参数插入
            String restrictingMinimumRelease="80000000000000000000";
            String restrictingMinimumReleaseDesc = "minimum restricting amount to be released in each epoch, range: [80000000000000000000, 100000000000000000000000]";
            List<GovernParam> governParamList = platOnClient
                    .getProposalContract()
                    .getParamList(moduleName)
                    .send()
                    .getData();
            if(governParamList!=null&&!governParamList.isEmpty()){
                for (GovernParam e : governParamList) {
                    if(
                            ModifiableGovernParamEnum.RESTRICTING_MINIMUM_RELEASE.getModule().equals(e.getParamItem().getModule())&&
                                    ModifiableGovernParamEnum.RESTRICTING_MINIMUM_RELEASE.getName().equals(e.getParamItem().getName())
                    ){
                        restrictingMinimumRelease = e.getParamValue().getValue();
                        restrictingMinimumReleaseDesc = e.getParamItem().getDesc();
                    }
                }
            }
            Config config = new Config();
            config.setActiveBlock(block.getNum());
            config.setModule(moduleName);
            config.setName(paramName);
            config.setInitValue(restrictingMinimumRelease);
            config.setStaleValue(restrictingMinimumRelease);
            config.setValue(restrictingMinimumRelease);
            config.setRangeDesc(restrictingMinimumReleaseDesc);
            Date date = new Date();
            config.setCreateTime(date);
            config.setUpdateTime(date);
            configMapper.insert(config);
        }
    }
}

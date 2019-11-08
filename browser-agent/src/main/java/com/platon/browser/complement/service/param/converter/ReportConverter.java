package com.platon.browser.complement.service.param.converter;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.platon.browser.common.collection.dto.CollectionTransaction;
import com.platon.browser.common.complement.dto.ComplementNodeOpt;
import com.platon.browser.common.complement.param.slash.Report;
import com.platon.browser.common.queue.collection.event.CollectionEvent;
import com.platon.browser.complement.mapper.SlashBusinessMapper;
import com.platon.browser.config.BlockChainConfig;
import com.platon.browser.dao.entity.Staking;
import com.platon.browser.dao.entity.StakingKey;
import com.platon.browser.dao.mapper.StakingMapper;
import com.platon.browser.param.ReportParam;

/**
 * @description: 举报验证人业务参数转换器
 * @author: chendongming@juzix.net
 * @create: 2019-11-04 17:58:27
 **/
@Service
public class ReportConverter extends BusinessParamConverter<Optional<ComplementNodeOpt>> {
	
    @Autowired
    private BlockChainConfig chainConfig;
    @Autowired
    private SlashBusinessMapper slashBusinessMapper;

    @Autowired
    private StakingMapper stakingMapper;

    @Override
    public Optional<ComplementNodeOpt> convert(CollectionEvent event, CollectionTransaction tx) {
        // 举报信息
        ReportParam txParam = tx.getTxParam(ReportParam.class);
        Report businessParam= Report.builder()
        		.slashData(txParam.getData())
                .nodeId(txParam.getVerify())
                .txHash(tx.getHash())
                .time(tx.getTime())
                .stakingBlockNum(txParam.getStakingBlockNum())
                .slashRate(chainConfig.getDuplicateSignSlashRate())
                .benefitAddr(tx.getFrom())
                .slash2ReportRate(chainConfig.getDuplicateSignReportRate())
                .settingEpoch(event.getEpochMessage().getSettleEpochRound().intValue())
                .build();
        
        StakingKey stakingKey = new StakingKey();
        stakingKey.setNodeId(businessParam.getNodeId());
        stakingKey.setStakingBlockNum(businessParam.getStakingBlockNum().longValue());
        Staking staking = stakingMapper.selectByPrimaryKey(stakingKey);
        //惩罚的金额
        BigDecimal codeSlashValue = staking.getStakingLocked().multiply(businessParam.getSlashRate());
        //奖励的金额
        BigDecimal codeRewardValue = codeSlashValue.multiply(businessParam.getSlash2ReportRate());
        //当前锁定的
        BigDecimal codeCurStakingLocked = staking.getStakingLocked().subtract(codeSlashValue);
        if(codeCurStakingLocked.compareTo(BigDecimal.ZERO) == 1){
            businessParam.setCodeStatus(2);
            businessParam.setCodeStakingReductionEpoch(businessParam.getSettingEpoch());
        }else {
            businessParam.setCodeStatus(3);
            businessParam.setCodeStakingReductionEpoch(0);
        }
        businessParam.setCodeRewardValue(codeRewardValue);

        slashBusinessMapper.report(businessParam);
        return Optional.ofNullable(null);
    }
}

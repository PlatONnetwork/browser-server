package com.platon.browser.complement.converter.slash;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.platon.browser.common.queue.collection.event.CollectionEvent;
import com.platon.browser.complement.converter.BusinessParamConverter;
import com.platon.browser.complement.dao.mapper.SlashBusinessMapper;
import com.platon.browser.complement.dao.param.slash.Report;
import com.platon.browser.config.BlockChainConfig;
import com.platon.browser.dao.entity.Staking;
import com.platon.browser.dao.entity.StakingKey;
import com.platon.browser.dao.mapper.StakingMapper;
import com.platon.browser.elasticsearch.dto.NodeOpt;
import com.platon.browser.elasticsearch.dto.Transaction;
import com.platon.browser.param.ReportParam;

import lombok.extern.slf4j.Slf4j;

/**
 * @description: 举报验证人业务参数转换器
 * @author: chendongming@juzix.net
 * @create: 2019-11-04 17:58:27
 **/
@Slf4j
@Service
public class ReportConverter extends BusinessParamConverter<Optional<NodeOpt>> {
	
    @Autowired
    private BlockChainConfig chainConfig;
    @Autowired
    private SlashBusinessMapper slashBusinessMapper;

    @Autowired
    private StakingMapper stakingMapper;

    @Override
    public Optional<NodeOpt> convert(CollectionEvent event, Transaction tx) {

        long startTime = System.currentTimeMillis();

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
        businessParam.setCodeCurStakingLocked(codeCurStakingLocked);

        slashBusinessMapper.report(businessParam);

        log.debug("处理耗时:{} ms",System.currentTimeMillis()-startTime);

        return Optional.ofNullable(null);
    }
}

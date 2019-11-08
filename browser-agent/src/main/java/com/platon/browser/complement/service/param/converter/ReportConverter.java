package com.platon.browser.complement.service.param.converter;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import com.platon.browser.dao.entity.Staking;
import com.platon.browser.dao.entity.StakingKey;
import com.platon.browser.dao.mapper.StakingMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.platon.browser.common.collection.dto.CollectionTransaction;
import com.platon.browser.common.complement.dto.slash.Report;
import com.platon.browser.common.queue.collection.event.CollectionEvent;
import com.platon.browser.config.BlockChainConfig;
import com.platon.browser.param.ReportParam;
import com.platon.browser.persistence.dao.mapper.SlashBusinessMapper;

/**
 * @description: 举报验证人业务参数转换器
 * @author: chendongming@juzix.net
 * @create: 2019-11-04 17:58:27
 **/
@Service
public class ReportConverter extends BusinessParamConverter<Report> {
	
    @Autowired
    private BlockChainConfig chainConfig;
    @Autowired
    private SlashBusinessMapper slashBusinessMapper;

    @Autowired
    private StakingMapper stakingMapper;

    @Override
    public Report convert(CollectionEvent event, CollectionTransaction tx) {
        // 举报信息
        ReportParam txParam = tx.getTxParam(ReportParam.class);
        Report businessParam= Report.builder()
        		.slashData(txParam.getData())
                .nodeId(txParam.getVerify())
                .txHash(tx.getHash())
                .bNum(BigInteger.valueOf(tx.getNum()))
                .time(tx.getTime())
                .stakingBlockNum(txParam.getStakingBlockNum())
                .slashRate(chainConfig.getDuplicateSignSlashRate())
                .benefitAddr(tx.getFrom())
                .slash2ReportRate(chainConfig.getDuplicateSignReportRate())
                .settingEpoch(event.getEpochMessage().getSettleEpochRound().intValue())
                .build();
        
        StakingKey stakingKey = new StakingKey();
        Staking staking = stakingMapper.selectByPrimaryKey(stakingKey);
        //惩罚的金额
        BigDecimal codeSlashValue = staking.getStakingLocked().multiply(businessParam.getSlashRate());
        //奖励的金额
        BigDecimal codeRewardValue = codeSlashValue.multiply(businessParam.getSlash2ReportRate());
        //节点操作描述
        String codeNodeoptDesc = "PERCENT|AMOUNT".replace("AMOUNT",codeSlashValue.toString());
        //当前锁定的
        BigDecimal codeCurStakingLocked = staking.getStakingLocked().subtract(codeSlashValue);
        if(codeCurStakingLocked.compareTo(BigDecimal.ZERO) == 1){
            businessParam.setCodeStatus(2);
        }else {
            businessParam.setCodeStatus(3);

        }
        
        
        slashBusinessMapper.report(businessParam);
        return businessParam;
    }
}

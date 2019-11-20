package com.platon.browser.complement.converter.slash;

import com.platon.browser.common.complement.cache.NetworkStatCache;
import com.platon.browser.common.complement.cache.bean.NodeItem;
import com.platon.browser.common.complement.dto.ComplementNodeOpt;
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
import com.platon.browser.exception.NoSuchBeanException;
import com.platon.browser.param.ReportParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

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
    @Autowired
    private NetworkStatCache networkStatCache;

    @Override
    public Optional<NodeOpt> convert(CollectionEvent event, Transaction tx) {
        // 举报信息
        ReportParam txParam = tx.getTxParam(ReportParam.class);
        if(null==txParam) return Optional.ofNullable(null);
        String nodeId = txParam.getVerify();
        try {
            NodeItem nodeItem = nodeCache.getNode(nodeId);
            txParam.setNodeName(nodeItem.getNodeName()).setStakingBlockNum(nodeItem.getStakingBlockNum());
            tx.setInfo(txParam.toJSONString());
        } catch (NoSuchBeanException e) {
            log.warn("缓存中找不到节点[{}]信息,无法补节点名称和质押区块号",nodeId);
        }
        // 失败的交易不分析业务数据
        if(Transaction.StatusEnum.FAILURE.getCode()==tx.getStatus()) return Optional.ofNullable(null);

        long startTime = System.currentTimeMillis();

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
        if(codeCurStakingLocked.compareTo(BigDecimal.ZERO)>0){
            businessParam.setCodeStatus(2);
            businessParam.setCodeStakingReductionEpoch(businessParam.getSettingEpoch());
        }else {
            businessParam.setCodeStatus(3);
            businessParam.setCodeStakingReductionEpoch(0);
        }
        businessParam.setCodeRewardValue(codeRewardValue);
        businessParam.setCodeCurStakingLocked(codeCurStakingLocked);
        businessParam.setCodeSlashValue(codeSlashValue);

        slashBusinessMapper.report(businessParam);


        //操作描述:6【PERCENT|AMOUNT】 
        String desc = NodeOpt.TypeEnum.MULTI_SIGN.getTpl()
                .replace("PERCENT",chainConfig.getDuplicateSignSlashRate().toString())
                .replace("AMOUNT",codeSlashValue.toString());



        NodeOpt nodeOpt = ComplementNodeOpt.newInstance();
        nodeOpt.setId(networkStatCache.getAndIncrementNodeOptSeq());
        nodeOpt.setNodeId(nodeId);
        nodeOpt.setType(Integer.valueOf(NodeOpt.TypeEnum.MULTI_SIGN.getCode()));
        nodeOpt.setDesc(desc);
        nodeOpt.setTxHash(tx.getHash());
        nodeOpt.setBNum(event.getBlock().getNum());
        nodeOpt.setTime(event.getBlock().getTime());

        log.debug("处理耗时:{} ms",System.currentTimeMillis()-startTime);

        return Optional.ofNullable(nodeOpt);
    }
}

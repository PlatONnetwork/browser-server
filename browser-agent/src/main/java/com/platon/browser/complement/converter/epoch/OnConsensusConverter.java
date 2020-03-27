package com.platon.browser.complement.converter.epoch;

import com.platon.browser.common.complement.cache.NetworkStatCache;
import com.platon.browser.common.complement.cache.ReportMultiSignParamCache;
import com.platon.browser.common.complement.dto.ComplementNodeOpt;
import com.platon.browser.common.queue.collection.event.CollectionEvent;
import com.platon.browser.complement.dao.mapper.EpochBusinessMapper;
import com.platon.browser.complement.dao.mapper.SlashBusinessMapper;
import com.platon.browser.complement.dao.param.epoch.Consensus;
import com.platon.browser.complement.dao.param.slash.Report;
import com.platon.browser.config.BlockChainConfig;
import com.platon.browser.dao.entity.Staking;
import com.platon.browser.dao.entity.StakingKey;
import com.platon.browser.dao.mapper.StakingMapper;
import com.platon.browser.elasticsearch.dto.Block;
import com.platon.browser.elasticsearch.dto.NodeOpt;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

@Slf4j
@Service
public class OnConsensusConverter {
	
    @Autowired
    private BlockChainConfig chainConfig;
    @Autowired
    private EpochBusinessMapper epochBusinessMapper;
    @Autowired
    private StakingMapper stakingMapper;
    @Autowired
    private NetworkStatCache networkStatCache;
    @Autowired
    private ReportMultiSignParamCache reportMultiSignParamCache;
    @Autowired
    private SlashBusinessMapper slashBusinessMapper;
	
	public Optional<List<NodeOpt>> convert(CollectionEvent event, Block block) {
        long startTime = System.currentTimeMillis();

        log.debug("Block Number:{}",block.getNum());

        List<String> validatorList = new ArrayList<>();
        event.getEpochMessage().getCurValidatorList().forEach(v->validatorList.add(v.getNodeId()));

        BigInteger expectBlockNum = chainConfig.getConsensusPeriodBlockCount().divide(BigInteger.valueOf(validatorList.size()));
        Consensus consensus = Consensus.builder()
                .expectBlockNum(expectBlockNum)
                .validatorList(validatorList)
                .build();
       
        epochBusinessMapper.consensus(consensus);

        // 取出双签参数缓存中的所有被举报节点ID列表
        List<String> reportedNodeIdList = reportMultiSignParamCache.getNodeIdList();
        // 取出下一共识周期的节点列表
        List<String> nextConsNodeIdList = new ArrayList<>();
        event.getEpochMessage().getCurValidatorList().forEach(n->nextConsNodeIdList.add(n.getNodeId()));
        // 取出本轮应该被处罚的节点列表
        List<String> slashNodeList = new ArrayList<>();
        reportedNodeIdList.forEach(reportedNodeId->{
            if(!nextConsNodeIdList.contains(reportedNodeId)) slashNodeList.add(reportedNodeId);
        });

        List<NodeOpt> nodeOpts = new ArrayList<>();
        if(!slashNodeList.isEmpty()){
            // 查询因为被举报而置为异常,且不参与下一轮共识的节点，进行处罚
            List<Staking> slashList = slashBusinessMapper.getException(slashNodeList);
            if(!slashList.isEmpty()){
                slashList.forEach(slashNode->{
                    List<Report> reportList = reportMultiSignParamCache.getReportList(slashNode.getNodeId());
                    reportList.forEach(report -> {
                        NodeOpt nodeOpt = slash(report,block);
                        nodeOpts.add(nodeOpt);
                    });
                    // 双签处罚后需要删除缓存中的双签参数，防止下一次进来重复处罚
                    reportMultiSignParamCache.remove(slashNode.getNodeId());
                });
            }
        }

        log.debug("处理耗时:{} ms",System.currentTimeMillis()-startTime);

        return Optional.ofNullable(nodeOpts);
	}

	private NodeOpt slash(Report businessParam,Block block){
        /**
         * 处理双签处罚
         * */
        StakingKey stakingKey = new StakingKey();
        stakingKey.setNodeId(businessParam.getNodeId());
        stakingKey.setStakingBlockNum(businessParam.getStakingBlockNum().longValue());
        Staking staking = stakingMapper.selectByPrimaryKey(stakingKey);
        //惩罚的金额  假设锁定的金额为0，则获取待赎回的金额
        BigDecimal stakingAmount = staking.getStakingLocked();
        if(stakingAmount.compareTo(BigDecimal.ZERO) == 0) {
        	stakingAmount = staking.getStakingReduction();
        }
        BigDecimal codeSlashValue = stakingAmount.multiply(businessParam.getSlashRate());
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

        slashBusinessMapper.slashNode(businessParam);


        //操作描述:6【PERCENT|AMOUNT】
        String desc = NodeOpt.TypeEnum.MULTI_SIGN.getTpl()
                .replace("PERCENT",chainConfig.getDuplicateSignSlashRate().toString())
                .replace("AMOUNT",codeSlashValue.toString());

        NodeOpt nodeOpt = ComplementNodeOpt.newInstance();
        nodeOpt.setId(networkStatCache.getAndIncrementNodeOptSeq());
        nodeOpt.setNodeId(businessParam.getNodeId());
        nodeOpt.setType(Integer.valueOf(NodeOpt.TypeEnum.MULTI_SIGN.getCode()));
        nodeOpt.setDesc(desc);
        nodeOpt.setTxHash(businessParam.getTxHash());
        nodeOpt.setBNum(block.getNum());
        nodeOpt.setTime(block.getTime());
        return nodeOpt;
    }

}

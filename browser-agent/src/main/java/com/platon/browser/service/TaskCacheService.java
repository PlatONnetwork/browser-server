package com.platon.browser.service;

import com.platon.browser.dao.entity.NetworkStat;
import com.platon.browser.dto.CustomAddress;
import com.platon.browser.dto.CustomNetworkStat;
import com.platon.browser.dto.CustomProposal;
import com.platon.browser.dto.CustomStaking;
import com.platon.browser.engine.cache.AddressCache;
import com.platon.browser.engine.cache.CacheHolder;
import com.platon.browser.engine.cache.NodeCache;
import com.platon.browser.engine.cache.ProposalCache;
import com.platon.browser.engine.stage.*;
import com.platon.browser.exception.NoSuchBeanException;
import com.platon.browser.task.bean.*;
import com.platon.browser.task.cache.AddressTaskCache;
import com.platon.browser.task.cache.NetworkStatTaskCache;
import com.platon.browser.task.cache.ProposalTaskCache;
import com.platon.browser.task.cache.StakingTaskCache;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.math.BigInteger;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


/**
 * @Auther: Chendongming
 * @Date: 2019/9/26 11:21
 * @Description: 任务缓存服务类
 */
@Service
public class TaskCacheService {
    private static Logger logger = LoggerFactory.getLogger(TaskCacheService.class);
    @Autowired
    private AddressTaskCache addressTaskCache;
    @Autowired
    private ProposalTaskCache proposalTaskCache;
    @Autowired
    private StakingTaskCache stakingTaskCache;
    @Autowired
    private NetworkStatTaskCache networkStatTaskCache;
    @Autowired
    private CacheHolder cacheHolder;

    private BlockChainStage stageData;
    private AddressStage addressStage;
    private ProposalStage proposalStage;
    private StakingStage stakingStage;
    private NetworkStatStage networkStatStage;
    private NodeCache nodeCache;

    @PostConstruct
    private void init(){
        nodeCache = cacheHolder.getNodeCache();
        stageData = cacheHolder.getStageData();
        stakingStage = cacheHolder.getStageData().getStakingStage();
        addressStage = cacheHolder.getStageData().getAddressStage();
        proposalStage = cacheHolder.getStageData().getProposalStage();
        networkStatStage = cacheHolder.getStageData().getNetworkStatStage();
    }

    /**
     * 合并地址任务缓存
     */
    public void mergeTaskAddressCache(){
        Map<String, CustomAddress> addressStageMap=stageData.getAddressStage().exportAddressMap();
        Set<String> stageAddressKeySet = new HashSet<>();
        // 先从暂存区角度合并任务缓存更改
        addressStageMap.forEach((k,ca)->{
            stageAddressKeySet.add(k);
            // 使用任务缓存中的值覆盖暂存区的值
            TaskAddress ta = addressTaskCache.get(k);
            merge(ca,ta);
        });
        // 再更新任务缓存中存在，暂存区不存在的地址，并加入暂存区
        AddressCache addressCache = cacheHolder.getAddressCache();
        Set<String> taskAddressKeySet = addressTaskCache.getKeySet();
        taskAddressKeySet.removeAll(stageAddressKeySet); // 排除掉暂存区的记录
        taskAddressKeySet.forEach(k->{
            try {
                CustomAddress ca = addressCache.getAddress(k);
                TaskAddress ta = addressTaskCache.get(k);
                merge(ca,ta);
            } catch (NoSuchBeanException e) {
                logger.error("地址缓存中找不到地址为[{}]的缓存实体:",k,e);
            }
        });
    }

    /**
     * 合并提案任务缓存
     */
    public void mergeTaskProposalCache(){
        Map<String, CustomProposal> proposalStageMap=stageData.getProposalStage().exportProposalMap();
        Set<String> stageProposalKeySet = new HashSet<>();
        // 先从暂存区角度合并任务缓存更改
        proposalStageMap.forEach((k,cp)->{
            stageProposalKeySet.add(k);
            // 使用任务缓存中的值覆盖暂存区的值
            TaskProposal tp = proposalTaskCache.get(k);
            merge(cp,tp);
        });
        // 再更新任务缓存中存在，暂存区不存在的提案，并加入暂存区
        ProposalCache proposalCache = cacheHolder.getProposalCache();
        Set<String> taskProposalKeySet = proposalTaskCache.getKeySet();
        taskProposalKeySet.removeAll(stageProposalKeySet);
        taskProposalKeySet.forEach(k->{
            try {
                CustomProposal cp = proposalCache.getProposal(k);
                TaskProposal tp = proposalTaskCache.get(k);
                merge(cp,tp);
            } catch (NoSuchBeanException e) {
                logger.error("地址缓存中找不到地址为[{}]的缓存实体:",k,e);
            }
        });
    }

    /**
     * 合并质押任务缓存
     */
    public void mergeTaskStakingCache(){
        Map<String, CustomStaking> stakingStageMap=stageData.getStakingStage().exportStakingMap();
        Set<TaskStakingKey> stageStakingKeySet = new HashSet<>();
        // 先从暂存区角度合并任务缓存更改
        stakingStageMap.forEach((k,cs)->{
            TaskStakingKey key = new TaskStakingKey(cs.getNodeId(),cs.getStakingBlockNum());
            stageStakingKeySet.add(key);
            // 使用任务缓存中的值覆盖暂存区的值
            TaskStaking ts = stakingTaskCache.get(cs.getNodeId(),cs.getStakingBlockNum());
            merge(cs,ts);
        });
        // 再更新任务缓存中存在，暂存区不存在的提案，并加入暂存区
        Set<TaskStakingKey> taskStakingKeySet = stakingTaskCache.getKeySet();
        taskStakingKeySet.removeAll(stageStakingKeySet);
        taskStakingKeySet.forEach(k->{
            try {
                CustomStaking cs = nodeCache.getStaking(k.getNodeId(),k.getBlockNumber());
                TaskStaking ts = stakingTaskCache.get(k.getNodeId(),k.getBlockNumber());
                merge(cs,ts);
            } catch (NoSuchBeanException e) {
                logger.error("质押缓存中找不到[nodeId={},stakingBlockNumber={}]的缓存实体:",k.getNodeId(),k.getBlockNumber(),e);
            }
        });
    }

    /**
     * 合并质押任务缓存
     * @param
     */
    public void mergeTaskNetworkStatCache(){
        CustomNetworkStat cns = cacheHolder.getNetworkStatCache();
        TaskNetworkStat tns = networkStatTaskCache.get();
        if(!tns.isMerged()){
            boolean changed = false;
            if(StringUtils.isNotBlank(tns.getIssueValue())&&!tns.getIssueValue().equals(cns.getIssueValue())){
                cns.setIssueValue(tns.getIssueValue());
                changed=true;
            }
            if(StringUtils.isNotBlank(tns.getTurnValue())&&!tns.getTurnValue().equals(cns.getTurnValue())){
                cns.setTurnValue(tns.getTurnValue());
                changed=true;
            }
            if(tns.getCurrentNumber()!=null&&!tns.getCurrentNumber().equals(cns.getCurrentNumber())){
                cns.setCurrentNumber(tns.getCurrentNumber());
                changed=true;
            }
            if(changed) networkStatStage.updateNetworkStat(cns);
        }
    }

    private void merge(CustomAddress ca,TaskAddress ta){
        if(!ta.isMerged()) {
            boolean changed=false;
            if(StringUtils.isNotBlank(ta.getRestrictingBalance())&&!ta.getRestrictingBalance().equals(ca.getRestrictingBalance())){
                ca.setRestrictingBalance(ta.getRestrictingBalance());
                changed=true;
            }
            if(StringUtils.isNotBlank(ta.getBalance())&&!ta.getBalance().equals(ca.getBalance())) {
                ca.setBalance(ta.getBalance());
                changed=true;
            }
            if(changed) addressStage.insertAddress(ca);
            ta.setMerged(true);
        }
    }

    private void merge(CustomProposal cp,TaskProposal tp){
        if(!tp.isMerged()) {
            boolean changed=false;
            if(StringUtils.isNotBlank(tp.getTopic())&&!tp.getTopic().equals(cp.getTopic())){
                cp.setTopic(tp.getTopic());
                changed=true;
            }
            if(StringUtils.isNotBlank(tp.getDescription())&&!tp.getDescription().equals(cp.getDescription())){
                cp.setDescription(tp.getDescription());
                changed=true;
            }
            if(StringUtils.isNotBlank(tp.getCanceledTopic())&&!tp.getCanceledTopic().equals(cp.getCanceledTopic())){
                cp.setCanceledTopic(tp.getCanceledTopic());
                changed=true;
            }
            //设置参与人数
            if(tp.getAccuVerifiers()!=null&&!tp.getAccuVerifiers().equals(cp.getAccuVerifiers())){
                // 有变更
                cp.setAccuVerifiers(tp.getAccuVerifiers());
                changed=true;
            }

            //设置赞成票
            if(tp.getYeas()!=null&&!tp.getYeas().equals(cp.getYeas())){
                // 有变更
                cp.setYeas(tp.getYeas());
                changed=true;
            }

            //设置反对票
            if(tp.getNays()!=null&&!tp.getNays().equals(cp.getNays())){
                // 有变更
                cp.setNays(tp.getNays());
                changed=true;
            }

            //设置弃权票
            if(tp.getAbstentions()!=null&&!tp.getAbstentions().equals(cp.getAbstentions())){
                // 有变更
                cp.setAbstentions(tp.getAbstentions());
                changed=true;
            }

            //设置状态
            if(tp.getStatus()!=null&&!tp.getStatus().equals(cp.getStatus())){
                // 有变更
                cp.setStatus(tp.getStatus());
                changed=true;
            }
            if(changed) proposalStage.insertProposal(cp);
            tp.setMerged(true);
        }
    }

    private void merge(CustomStaking cs, TaskStaking ts) {
        if(!ts.isMerged()){
            boolean changed=false;
            if(StringUtils.isNotBlank(ts.getStakingIcon())&&!ts.getStakingIcon().equals(cs.getStakingIcon())){
                cs.setStakingIcon(ts.getStakingIcon());
                changed=true;
            }
            if(StringUtils.isNotBlank(ts.getExternalName())&&!ts.getExternalName().equals(cs.getExternalName())){
                cs.setExternalName(ts.getExternalName());
                changed=true;
            }
            if(changed) stakingStage.insertStaking(cs);
            ts.setMerged(true);
        }
    }
}

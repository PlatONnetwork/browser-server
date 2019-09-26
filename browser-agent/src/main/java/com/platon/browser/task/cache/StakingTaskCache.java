package com.platon.browser.task.cache;

import com.platon.browser.task.bean.TaskStaking;
import com.platon.browser.task.bean.TaskStakingKey;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class StakingTaskCache {
    private Map<String, TaskStaking> cacheMap = new ConcurrentHashMap<>();
    public Set<TaskStakingKey> getKeySet(){
        Set<TaskStakingKey> keySet = new HashSet<>();
        cacheMap.values().stream()
                .filter(ts-> StringUtils.isNotBlank(ts.getNodeId())&&ts.getStakingBlockNum()!=null)
                .forEach(ts->keySet.add(new TaskStakingKey(ts.getNodeId(),ts.getStakingBlockNum())));
        return keySet;
    }

    public void update(TaskStaking staking){
        cacheMap.put(staking.getNodeId()+staking.getStakingBlockNum(),staking);
    }
    
    public TaskStaking get(String nodeId,Long stakingBlockNumber){
        String key = nodeId+stakingBlockNumber;
        TaskStaking cache = cacheMap.get(key);
        if(cache==null){
            // 如果缓存为空，则创建一个新的，并把合并状态变为true，防止采集线程合并此无效统计信息
            cache = new TaskStaking();
            cache.setNodeId(nodeId);
            cache.setStakingBlockNum(stakingBlockNumber);
            cache.setMerged(true);
            cacheMap.put(key,cache);
            return cache;
        }
        return cacheMap.get(key);
    }

    /**
     * 清除已经被合并到采块线程的质押统计信息
     */
    private Set<String> mergedStakingKey = new HashSet<>();
    public void sweep(){
        mergedStakingKey.clear();
        cacheMap.values().stream()
                .filter(TaskStaking::isMerged)
                .forEach(e->mergedStakingKey.add(e.getNodeId()+e.getStakingBlockNum()));
        cacheMap.keySet().removeAll(mergedStakingKey);
        mergedStakingKey.clear();
    }
}

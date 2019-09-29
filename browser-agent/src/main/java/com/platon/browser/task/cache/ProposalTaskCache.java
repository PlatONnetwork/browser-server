package com.platon.browser.task.cache;

import com.platon.browser.task.bean.TaskProposal;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ProposalTaskCache {
    private Map<String, TaskProposal> cacheMap = new ConcurrentHashMap<>();
    public Set<String> getKeySet(){
        return new HashSet<>(cacheMap.keySet());
    }

    public void update(TaskProposal proposal){
        cacheMap.put(proposal.getHash(),proposal);
    }

    public TaskProposal get(String key){
        return cacheMap.computeIfAbsent(key,k->{
            // 如果缓存为空，则创建一个新的，并把合并状态变为true，防止采集线程合并此无效统计信息
            TaskProposal tp = new TaskProposal();
            tp.setHash(k);
            tp.setMerged(true);
            return tp;
        });
    }

    /**
     * 清除已经被合并到采块线程的提案统计信息
     */
    private Set<String> mergedProposalKey = new HashSet<>();
    public void sweep(){
        mergedProposalKey.clear();
        cacheMap.values().stream()
                .filter(TaskProposal::isMerged)
                .forEach(e->mergedProposalKey.add(e.getHash()));
        cacheMap.keySet().removeAll(mergedProposalKey);
        mergedProposalKey.clear();
    }
}

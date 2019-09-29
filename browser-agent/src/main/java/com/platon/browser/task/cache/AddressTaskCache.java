package com.platon.browser.task.cache;

import com.platon.browser.task.bean.TaskAddress;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class AddressTaskCache {
    private Map<String, TaskAddress> cacheMap = new ConcurrentHashMap<>();
    public Set<String> getKeySet(){
        return new HashSet<>(cacheMap.keySet());
    }

    public void update(TaskAddress address){
        cacheMap.put(address.getAddress(),address);
    }

    public TaskAddress get(String key){
        return cacheMap.computeIfAbsent(key,k->{
            // 如果缓存为空，则创建一个新的，并把合并状态变为true，防止采集线程合并此无效统计信息
            TaskAddress ta = new TaskAddress();
            ta.setAddress(k);
            ta.setMerged(true);
            return ta;
        });
    }

    /**
     * 清除已经被合并到采块线程的地址统计信息
     */
    private Set<String> mergedAddressKey = new HashSet<>();
    public void sweep(){
        mergedAddressKey.clear();
        cacheMap.values().stream()
            .filter(TaskAddress::isMerged)
            .forEach(e->mergedAddressKey.add(e.getAddress()));
        cacheMap.keySet().removeAll(mergedAddressKey);
        mergedAddressKey.clear();
    }
}

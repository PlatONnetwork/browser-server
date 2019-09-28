package com.platon.browser.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.Set;

@Component
public class TaskManager {
    @Autowired
    private AddressUpdateTask addressUpdateTask;
    @Autowired
    private NetworkStatUpdateTask networkStatUpdateTask;
    @Autowired
    private ProposalUpdateTask proposalUpdateTask;
    @Autowired
    private StakingUpdateTask stakingUpdateTask;

    private final static Set<BaseTask> tasks = new HashSet<>();
    @PostConstruct
    private void init(){
        tasks.add(addressUpdateTask);
        tasks.add(networkStatUpdateTask);
        tasks.add(proposalUpdateTask);
        tasks.add(stakingUpdateTask);
    }

    /**
     * 是否有任务在运行中
     * @return
     */
    public boolean isRunning(){
        for (BaseTask task:tasks) if(task.isRunning()) return true;
        return false;
    }
}

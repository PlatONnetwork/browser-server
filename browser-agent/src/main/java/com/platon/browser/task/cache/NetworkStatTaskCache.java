package com.platon.browser.task.cache;

import com.platon.browser.task.bean.TaskNetworkStat;
import org.springframework.stereotype.Component;

@Component
public class NetworkStatTaskCache {
    private TaskNetworkStat cache = new TaskNetworkStat();
    public TaskNetworkStat get(){return cache;}
}

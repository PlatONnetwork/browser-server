package com.platon.browser.old.task.cache;

import com.platon.browser.old.task.bean.TaskNetworkStat;

//@Component
public class NetworkStatTaskCache {
    private TaskNetworkStat cache = new TaskNetworkStat();
    public TaskNetworkStat get(){return cache;}
}

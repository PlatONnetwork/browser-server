package com.platon.browser.task.bean;

import lombok.Data;

import java.util.Objects;

@Data
public class TaskStakingKey{
    private String nodeId;
    private Long blockNumber;
    public TaskStakingKey(String nodeId,Long blockNumber){
        this.nodeId=nodeId;
        this.blockNumber=blockNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaskStakingKey that = (TaskStakingKey) o;
        return Objects.equals(nodeId, that.nodeId) &&
                Objects.equals(blockNumber, that.blockNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nodeId, blockNumber);
    }
}
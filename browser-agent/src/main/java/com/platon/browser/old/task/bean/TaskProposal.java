package com.platon.browser.old.task.bean;

import lombok.Data;

@Data
public class TaskProposal extends TaskBase {
    private String hash;
    private Long yeas;
    private Long nays;
    private Long abstentions;
    private Long accuVerifiers;
    private Integer status;
    private String topic;
    private String description;
    private String canceledTopic;
}

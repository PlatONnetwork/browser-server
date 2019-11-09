package com.platon.browser.common.task;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Builder
@AllArgsConstructor
@Accessors(chain = true)
public class TaskCacheProposal extends TaskCacheBase {
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

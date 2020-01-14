package com.platon.browser.common.queue.gasestimate.event;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Builder
@Accessors(chain = true)
public class GasEstimateEpoch {
    private String nodeId;
    private String sbn;
    private String addr;
    private Long epoch;
}

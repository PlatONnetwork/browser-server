package com.platon.browser.common.queue.gasestimate.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Builder
@AllArgsConstructor
@Accessors(chain = true)
public class GasEstimateEpoch {
    private String nodeId;
    private String sbn;
    private String addr;
    private Long epoch;
}

package com.platon.browser.queue.event;

import com.platon.browser.dao.entity.GasEstimate;

import lombok.Data;

import java.util.List;

/**
 * @Auther: dongqile
 * @Date: 2019/12/23
 * @Description:
 */
@Data
public class EstimateEvent implements Event{
    private List <GasEstimate> gasEstimates;
}
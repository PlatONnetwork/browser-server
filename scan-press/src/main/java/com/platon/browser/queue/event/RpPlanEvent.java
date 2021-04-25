package com.platon.browser.queue.event;

import com.platon.browser.dao.entity.RpPlan;
import com.platon.browser.dao.entity.Slash;
import lombok.Data;

import java.util.List;

/**
 * @Auther: dongqile
 * @Date: 2019/12/23
 * @Description:
 */
@Data
public class RpPlanEvent implements Event{
    private List <RpPlan> rpPlanList;
}
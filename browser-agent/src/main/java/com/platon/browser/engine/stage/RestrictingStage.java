package com.platon.browser.engine.stage;

import com.platon.browser.dao.entity.RpPlan;

import java.util.HashSet;
import java.util.Set;

/**
 * User: dongqile
 * Date: 2019/8/31
 * Time: 16:48
 */
public class RestrictingStage {

    private Set <RpPlan> insertRpPlanStage = new HashSet <>();

    public void clear () {
        insertRpPlanStage.clear();
    }

    public void insertRpPlan ( RpPlan rpPlan ) {
        insertRpPlanStage.add(rpPlan);
    }

    public Set <RpPlan> exportRpPlan () {
        Set <RpPlan> returnData = new HashSet <>(insertRpPlanStage);
        return returnData;
    }

    public Set <RpPlan> getRpPlan () {
        return insertRpPlanStage;
    }


}
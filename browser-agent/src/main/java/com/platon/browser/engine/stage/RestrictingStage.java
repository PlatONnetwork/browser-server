package com.platon.browser.engine.stage;

import com.platon.browser.dao.entity.RpPlan;

import java.util.HashSet;
import java.util.Set;

/**
 * @Auther: dongqile
 * @Date: 2019/8/31 20:09
 * @Description: 锁仓新增暂存类，入库后各容器需要清空
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
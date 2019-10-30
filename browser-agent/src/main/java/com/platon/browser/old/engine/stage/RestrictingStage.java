package com.platon.browser.old.engine.stage;

import com.platon.browser.dao.entity.RpPlan;
import com.platon.browser.dto.CustomRpPlan;

import java.util.HashSet;
import java.util.Set;

/**
 * @Auther: dongqile
 * @Date: 2019/8/31 20:09
 * @Description: 锁仓新增暂存类，入库后各容器需要清空
 */
public class RestrictingStage {

    private Set <CustomRpPlan> insertRpPlanStage = new HashSet <>();

    public void clear () {
        insertRpPlanStage.clear();
    }

    public void insertRpPlan ( CustomRpPlan rpPlan ) {
        insertRpPlanStage.add(rpPlan);
    }

    public Set <CustomRpPlan> getRpPlan () {
        return insertRpPlanStage;
    }

    public Set <RpPlan> exportRpPlanSet() {
        return new HashSet <>(insertRpPlanStage);
    }
}

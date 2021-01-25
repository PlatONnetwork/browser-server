package com.platon.browser;

import com.platon.contracts.ppos.dto.resp.Node;
import com.platon.utils.Numeric;

import java.math.BigInteger;

/**
 * @Auther: Chendongming
 * @Date: 2019/9/10 13:33
 * @Description:
 */
public class NodeBean extends Node {
    @Override
    public void setShares(String shares) {
        super.setShares(Numeric.toHexStringWithPrefix(new BigInteger(shares)));
    }

    @Override
    public void setReleased(String released) {
        super.setReleased(Numeric.toHexStringWithPrefix(new BigInteger(released)));
    }
    @Override
    public void setReleasedHes(String releasedHes) {
        super.setReleasedHes(Numeric.toHexStringWithPrefix(new BigInteger(releasedHes)));
    }
    @Override
    public void setRestrictingPlan(String restrictingPlan) {
        super.setRestrictingPlan(Numeric.toHexStringWithPrefix(new BigInteger(restrictingPlan)));
    }
    @Override
    public void setRestrictingPlanHes(String restrictingPlanHes) {
        super.setRestrictingPlanHes(Numeric.toHexStringWithPrefix(new BigInteger(restrictingPlanHes)));
    }
}

package com.platon.browserweb.common.req.home;

import javax.validation.constraints.NotNull;

/**
 * User: dongqile
 * Date: 2018/9/6
 * Time: 9:58
 */
public class HomeReq {

    @NotNull
    private long brokerId;

    public long getBrokerId () {
        return brokerId;
    }

    public void setBrokerId ( long brokerId ) {
        this.brokerId = brokerId;
    }
}
package com.platon.browserweb.common.req;

import com.platon.browserweb.common.validate.IdGroup;

import javax.validation.constraints.NotNull;

/**
 * User: dongqile
 * Date: 2018/7/11
 * Time: 15:06
 */
public class IdReq {

    /**
     * id
     */
    @NotNull(groups = IdGroup.class)
    private long id;

    private long userId;

    public long getId () {
        return id;
    }

    public void setId ( long id ) {
        this.id = id;
    }

    public long getUserId () {
        return userId;
    }

    public void setUserId ( long userId ) {
        this.userId = userId;
    }
}
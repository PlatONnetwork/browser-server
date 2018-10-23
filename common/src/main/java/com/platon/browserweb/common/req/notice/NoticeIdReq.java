package com.platon.browserweb.common.req.notice;

import com.platon.browserweb.common.req.PageReq;

import javax.validation.constraints.NotNull;

/**
 * User: dongqile
 * Date: 2018/7/13
 * Time: 10:05
 */
public class NoticeIdReq extends PageReq{
    @NotNull
    private long noticeId;


    private long userId;


    private String createJuserName;

    private Integer createJuserId;

    public long getNoticeId () {
        return noticeId;
    }

    public void setNoticeId ( long noticeId ) {
        this.noticeId = noticeId;
    }

    public long getUserId () {
        return userId;
    }

    public void setUserId ( long userId ) {
        this.userId = userId;
    }

    public String getCreateJuserName () {
        return createJuserName;
    }

    public void setCreateJuserName ( String createJuserName ) {
        this.createJuserName = createJuserName;
    }

    public Integer getCreateJuserId () {
        return createJuserId;
    }

    public void setCreateJuserId ( Integer createJuserId ) {
        this.createJuserId = createJuserId;
    }
}
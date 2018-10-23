package com.platon.browserweb.common.req.notice;

import com.platon.browserweb.common.req.PageReq;
import com.platon.browserweb.common.validate.NoticeIdGroup;

import javax.validation.constraints.NotNull;

/**
 * User: dongqile
 * Date: 2018/7/10
 * Time: 20:37
 */
public class NoticeReq extends PageReq {

    /**
     * 始（用户创建时间）
     */
    private long startTime;

    /**
     * 终（用户创建时间）
     */
    private long endTime;

    /**
     * 查询时间
     */
    private Integer dateType = 1;

    /**
     * 状态：
     1：未发布
     2：已发布
     3：已过期
     */
    private String status;

    private long userId;


    private String createJuserName;

    private Integer createJuserId;

    @NotNull(groups = NoticeIdGroup.class)
    private long noticeId;

    /**
     * 始（用户创建时间）
     */
    private String startTime2;

    /**
     * 终（用户创建时间）
     */
    private String endTime2;

    public long getNoticeId () {
        return noticeId;
    }

    public void setNoticeId ( long noticeId ) {
        this.noticeId = noticeId;
    }

    public long getStartTime () {
        return startTime;
    }

    public void setStartTime ( long startTime ) {
        this.startTime = startTime;
    }

    public long getEndTime () {
        return endTime;
    }

    public void setEndTime ( long endTime ) {
        this.endTime = endTime;
    }

    public Integer getDateType () {
        return dateType;
    }

    public void setDateType ( Integer dateType ) {
        this.dateType = dateType;
    }

    public String getStatus () {
        return status;
    }

    public void setStatus ( String status ) {
        this.status = status;
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

    public String getStartTime2 () {
        return startTime2;
    }

    public void setStartTime2 ( String startTime2 ) {
        this.startTime2 = startTime2;
    }

    public String getEndTime2 () {
        return endTime2;
    }

    public void setEndTime2 ( String endTime2 ) {
        this.endTime2 = endTime2;
    }
}

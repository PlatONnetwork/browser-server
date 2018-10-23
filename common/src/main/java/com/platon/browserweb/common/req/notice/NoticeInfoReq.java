package com.platon.browserweb.common.req.notice;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

/**
 * User: dongqile
 * Date: 2018/7/10
 * Time: 21:10
 */
public class NoticeInfoReq {

    /**
     * 标题
     */
    @NotEmpty
    private String title;

    /**
     * 内容
     */
    @NotEmpty
    private String content;

    /**
     * 有效期
     */
    private long validPeriod;

    /**
     * 操作类型
     1：保存
     2：发布
     */
    @NotEmpty
    private String operationType;

    /**
     * 公告id
     */
    @NotNull
    private long noticeId;

    /**
     * 公告语言
     */
    @NotNull
    private String lang;

    private Integer userId;

    private String createJuserName;

    private Integer createJuserId;

    public String getCreateJuserName () {
        return createJuserName;
    }

    public void setCreateJuserName ( String createJuserName ) {
        this.createJuserName = createJuserName;
    }

    public Integer getUserId () {
        return userId;
    }

    public Integer getCreateJuserId () {
        return createJuserId;
    }

    public void setCreateJuserId ( Integer createJuserId ) {
        this.createJuserId = createJuserId;
    }

    public void setUserId ( Integer userId ) {
        this.userId = userId;
    }

    public String getTitle () {
        return title;
    }

    public void setTitle ( String title ) {
        this.title = title;
    }

    public String getContent () {
        return content;
    }

    public void setContent ( String content ) {
        this.content = content;
    }

    public long getValidPeriod () {
        return validPeriod;
    }

    public void setValidPeriod ( long validPeriod ) {
        this.validPeriod = validPeriod;
    }

    public String getOperationType () {
        return operationType;
    }

    public void setOperationType ( String operationType ) {
        this.operationType = operationType;
    }

    public long getNoticeId () {
        return noticeId;
    }

    public void setNoticeId ( long noticeId ) {
        this.noticeId = noticeId;
    }

    public String getLang () {
        return lang;
    }

    public void setLang ( String lang ) {
        this.lang = lang;
    }
}
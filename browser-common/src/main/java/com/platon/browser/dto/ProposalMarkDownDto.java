package com.platon.browser.dto;

import lombok.Data;

/**
 * User: dongqile
 * Date: 2019/8/21
 * Time: 15:30
 */
@Data
public class ProposalMarkDownDto {
    /**
     * 提案状态
     */
    private String Status;

    /**
     * 提案类型
     */
    private String Type;

    /**
     * 提案类别
     */
    private String Category;

    /**
     * 提案描述
     */
    private String Description;

    /**
     * 提案状pIDID
     */
    private String PIP;

    /**
     * 提案发起人
     */
    private String Author;

    /**
     * 提案主题
     */
    private String Topic;

    /**
     * 提案创建时间
     */
    private String Created;

}
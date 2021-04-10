package com.platon.browser.bean;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * @Auther: dongqile
 * @Date: 2019/8/21 15:30
 * @Description: 提案githubMarkdown文件实体类
 */
@Data
public class ProposalMarkDownDto {
    /**
     * 提案状态
     */
    @JSONField(name = "Status")
    private String status;

    /**
     * 提案类型
     */
    @JSONField(name = "Type")
    private String type;

    /**
     * 提案类别
     */
    @JSONField(name = "Category")
    private String category;

    /**
     * 提案描述
     */
    @JSONField(name = "Description")
    private String description;

    /**
     * 提案状pIDID
     */
    @JSONField(name = "PIP")
    private String pIP;

    /**
     * 提案发起人
     */
    @JSONField(name = "Author")
    private String author;

    /**
     * 提案主题
     */
    @JSONField(name = "Topic")
    private String topic;

    /**
     * 提案创建时间
     */
    @JSONField(name = "Created")
    private String created;

}
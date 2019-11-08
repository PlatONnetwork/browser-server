package com.platon.browser.elasticsearch.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;
@Data
@Accessors(chain = true)
public class NodeOpt {
    private Long id;
    private String nodeId;
    private Integer type;
    private String txHash;
    private Long bNum;
    private Date time;
    private String desc;
    private Date creTime;
    private Date updTime;
}
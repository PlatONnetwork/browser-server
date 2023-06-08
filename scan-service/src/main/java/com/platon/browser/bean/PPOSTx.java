package com.platon.browser.bean;

import lombok.Data;

/**
 * @Author: NieXiang
 * @Date: 2023/6/8
 */
@Data
public class PPOSTx {

    private String from;

    private String to;

    private byte[] input;

    private byte[] result;
}

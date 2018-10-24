package com.platon.browser.common.dto;

import lombok.Data;

/**
 * Stomp消息
 * @param <T>
 */
@Data
public class MessageResp<T> {
    private String errMsg;
    private Integer result;
    private T data;
}

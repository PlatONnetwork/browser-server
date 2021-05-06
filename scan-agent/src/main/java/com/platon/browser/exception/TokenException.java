package com.platon.browser.exception;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * token业务异常
 *
 * @author huangyongpeng@matrixelements.com
 * @date 2021/4/29
 */
@Slf4j
@Data
public class TokenException extends RuntimeException {


    public TokenException() {
        super();
    }

    public TokenException(String message, Object data) {
        super(message);
        log.error("创建token异常，token校验无法通过，token信息为：{}", data);
    }

    public TokenException(String message, Throwable cause, Object data) {
        super(message, cause);
        log.error("创建token异常，token校验无法通过，token信息为：{}", data);
    }

}

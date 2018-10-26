package com.platon.browser.Controller;

import com.platon.browser.common.base.BaseResp;
import com.platon.browser.common.enums.RetEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class ValidateControllerAdvice {
    Logger logger= LoggerFactory.getLogger(ValidateControllerAdvice.class);
    /**
     * 捕获bean校验未通过异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public BaseResp argumentNotValidHandler(MethodArgumentNotValidException e) {
        List<FieldError> fieldErrors=e.getBindingResult().getFieldErrors();
        Map<String,String> msg = new HashMap<>();
        for (FieldError error:fieldErrors){
            msg.put(error.getField(),error.getDefaultMessage());
            logger.error(error.getField()+":"+error.getDefaultMessage());
        }
        return BaseResp.build(RetEnum.RET_PARAM_VALLID.getCode(),RetEnum.RET_PARAM_VALLID.getName(),msg);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseBody
    public BaseResp notReadableHandler(HttpMessageNotReadableException e) {
        return BaseResp.build(RetEnum.RET_PARAM_VALLID.getCode(),RetEnum.RET_PARAM_VALLID.getName(),null);
    }
}
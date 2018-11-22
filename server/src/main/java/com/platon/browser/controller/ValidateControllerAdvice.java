package com.platon.browser.controller;

import com.platon.browser.common.base.BaseResp;
import com.platon.browser.common.enums.RetEnum;
import com.platon.browser.exception.ResponseException;
import com.platon.browser.util.I18nEnum;
import com.platon.browser.util.I18nUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private I18nUtil i18n;

    /**
     * 捕获bean校验未通过异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public BaseResp argumentNotValidHandler(MethodArgumentNotValidException e) {
        List<FieldError> fieldErrors=e.getBindingResult().getFieldErrors();
        Map<String,String> msg = new HashMap<>();
        String errMsg = "";
        for (FieldError error:fieldErrors){
            if(StringUtils.isBlank(errMsg)){
                errMsg=error.getDefaultMessage();
            }
            msg.put(error.getField(),error.getDefaultMessage());
            logger.error(error.getField()+":"+error.getDefaultMessage());
        }
        if(StringUtils.isNotBlank(errMsg)){
            return BaseResp.build(RetEnum.RET_PARAM_VALLID.getCode(),errMsg,msg);
        }
        return BaseResp.build(RetEnum.RET_PARAM_VALLID.getCode(),i18n.i(I18nEnum.REQUEST_INVALID_PARAM),msg);
    }

    /**
     * 响应异常
     * @param e
     * @return
     */
    @ExceptionHandler(ResponseException.class)
    @ResponseBody
    public BaseResp responseExceptionHandler(ResponseException e) {
        return BaseResp.build(RetEnum.RET_SYS_EXCEPTION.getCode(),e.getMessage(),e.getMessage());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseBody
    public BaseResp notReadableHandler(HttpMessageNotReadableException e) {
        return BaseResp.build(RetEnum.RET_PARAM_VALLID.getCode(),i18n.i(I18nEnum.REQUEST_INVALID_PARAM),null);
    }

}
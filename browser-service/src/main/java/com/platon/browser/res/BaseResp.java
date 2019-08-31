package com.platon.browser.res;

import com.platon.browser.util.I18NUtils;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * 单个返回统一封装
 *  @file BaseResp.java
 *  @description 
 *	@author zhangrj
 *  @data 2019年8月31日
 */
@Data
public class BaseResp<T> {
    protected String errMsg;
    protected Integer code;
    protected T data;

    /** 初始化返回对象 */
    private BaseResp(Integer code, String errMsg, T data){
        this.code=code;
        this.errMsg=errMsg;
        this.data=data;
    }

    public String getErrMsg() {
        //return I18NUtils.getInstance().getResource(2000);
        if(code > 0 && StringUtils.isEmpty(errMsg)){
            errMsg = I18NUtils.getInstance().getResource(code);
        }
        return errMsg;
    }

    /** 静态构造返回对象 */
    public static <T> BaseResp<T> build(Integer code, String errMsg, T data){
        return new BaseResp<T>(code,errMsg,data);
    }
}
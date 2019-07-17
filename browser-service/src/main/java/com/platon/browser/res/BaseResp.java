package com.platon.browser.res;

import com.platon.browser.util.I18NUtils;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

@Data
public class BaseResp<T> {
    protected String errMsg;
    protected Integer code;
    protected T data;

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

    public static <T> BaseResp build(Integer code, String errMsg, T data){
        return new BaseResp(code,errMsg,data);
    }
}
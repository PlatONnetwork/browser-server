package com.platon.browser.response;

import com.platon.browser.utils.I18NUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * 单个返回统一封装
 *  @file BaseResp.java
 *  @description 
 *	@author zhangrj
 *  @data 2019年8月31日
 */
public class BaseResp<T> {
    protected String errMsg;
    protected Integer code;
    protected T data;

    public BaseResp() {
    	this.code=0;
    }
    
    /** 初始化返回对象 */
    public BaseResp(Integer code, String errMsg, T data){
        this.code=code;
        this.errMsg=errMsg;
        this.data=data;
    }

    public String getErrMsg() {
        if(code > 0 && StringUtils.isEmpty(errMsg)){
            errMsg = I18NUtils.getInstance().getResource(code);
        }
        return errMsg;
    }

    /** 静态构造返回对象 */
    public static <T> BaseResp<T> build(Integer code, String errMsg, T data){
        return new BaseResp<>(code,errMsg,data);
    }

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}
    
}
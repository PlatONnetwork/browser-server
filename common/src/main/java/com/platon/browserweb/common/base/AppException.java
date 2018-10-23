package com.platon.browserweb.common.base;


import com.platon.browserweb.common.enums.ErrorCodeEnum;

public class AppException extends RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int errorCode = -1;
    private String errorMessage;

    public Integer getErrorCode() {
        return errorCode;
    }


    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public AppException ( String msg) {
        super(msg);
        this.errorMessage = msg;
    }

    public AppException ( Integer errorCode, String msg) {
        super(msg);
        this.errorCode = errorCode;
        this.errorMessage = msg;
    }

    public AppException ( ErrorCodeEnum errorCodeEnum) {
        super(errorCodeEnum.getDesc());
        this.errorCode = errorCodeEnum.getCode();
        this.errorMessage = errorCodeEnum.getDesc();
    }

    /**
     * 构造.
     *
     * @param msg 错误信息
     * @param t   前一异常
     */
    public AppException ( Integer errorCode, String msg, Throwable t) {
        super(msg, t);
        this.setStackTrace(t.getStackTrace());

        this.errorCode = errorCode;
        this.errorMessage = msg;
    }
}

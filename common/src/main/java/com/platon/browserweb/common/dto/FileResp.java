package com.platon.browserweb.common.dto;

/**
 * User: dongqile
 * Date: 2018/6/27
 * Time: 20:31
 */
public class FileResp { private Integer code;
    private String info;
    private FileToken data;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public FileToken getData() {
        return data;
    }

    public void setData(FileToken data) {
        this.data = data;
    }
}
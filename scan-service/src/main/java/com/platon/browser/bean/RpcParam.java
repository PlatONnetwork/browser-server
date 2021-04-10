package com.platon.browser.bean;

import com.alibaba.fastjson.JSON;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Data
public class RpcParam {
    private static final Random r = new Random();
    private int id;
    private String jsonrpc="2.0";
    private String method;
    private List<Long> params=new ArrayList<>();
    public RpcParam(){
        this.id = r.nextInt(65535);
    }
    public String toJsonString(){
        return JSON.toJSONString(this);
    }
}
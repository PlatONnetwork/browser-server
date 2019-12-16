package com.platon.browser.client;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getJsonrpc() {
		return jsonrpc;
	}
	public void setJsonrpc(String jsonrpc) {
		this.jsonrpc = jsonrpc;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public List<Long> getParams() {
		return params;
	}
	public void setParams(List<Long> params) {
		this.params = params;
	}
	public static Random getR() {
		return r;
	}
    
}
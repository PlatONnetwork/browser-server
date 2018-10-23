package com.platon.browserweb.common.thread;

import com.alibaba.fastjson.JSONObject;

import com.platon.browserweb.common.util.HttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SendSMSThread implements Runnable {
    private static Logger log = LoggerFactory.getLogger(SendSMSThread.class);
    private String phone;
    private String code;
    private String content;
    private String url;

    public SendSMSThread(String phone,String code, String content,String url) {
        this.phone = phone;
        this.code = code;
        this.content = content;
        this.url = url;
    }

    @Override
    public void run() {
        try {
            JSONObject param = new JSONObject();
            param.put("phoneNumber", phone);
            param.put("smsContent", content);
            param.put("smsType", "5");
            param.put("valiCode",code);
            log.info("发送短信服务:请求参数:" + param.toJSONString());
            HttpClient.ResponseResult responseResult = HttpClient.newInstance().sendHttpPost(url,param.toJSONString(),null,null);
            System.out.println("验证码："+code);
            System.out.println(responseResult);
            log.info("发送短信服务:响应结果:" + responseResult);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("发送短信抛出异常：",e);
        }
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

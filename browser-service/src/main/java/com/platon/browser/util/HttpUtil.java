package com.platon.browser.util;

import com.alibaba.fastjson.JSON;
import com.platon.browser.exception.HttpRequestException;
import okhttp3.*;

import java.io.IOException;
import java.util.Objects;

/**
 * @Auther: Chendongming
 * @Date: 2019/9/9 14:49
 * @Description:
 */
public class HttpUtil {
    /**
     * 发送POST请求
     * @param url
     * @param param
     * @param clazz
     * @param <T>
     * @return
     * @throws HttpRequestException
     */
    public static  <T> T post(String url,String param,Class<T> clazz) throws HttpRequestException {
        OkHttpClient httpClient = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json;charset=UTF-8");
        Request request = new Request.Builder().post(RequestBody.create(param,mediaType)).url(url).build();
        T result = resolve(httpClient,request,url,clazz);
        return result;
    }

    /**
     * 发送GET请求
     * @param url
     * @param clazz
     * @param <T>
     * @return
     * @throws HttpRequestException
     */
    public static <T> T get(String url,Class<T> clazz) throws HttpRequestException {
        OkHttpClient httpClient = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        T result = resolve(httpClient,request,url,clazz);
        return result;
    }

    /**
     * 解析结果
     * @param httpClient
     * @param request
     * @param url
     * @param clazz
     * @param <T>
     * @return
     * @throws HttpRequestException
     */
    @SuppressWarnings("unchecked")
	private static <T> T resolve(OkHttpClient httpClient,Request request,String url,Class<T> clazz) throws HttpRequestException {
        Response response;
        try {
            response = httpClient.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
            throw new HttpRequestException("请求地址["+url+"]出错:"+e.getMessage());
        }
        if(response.isSuccessful()){
            try {
                String res = Objects.requireNonNull(response.body()).string();
                res = res.replace("\n","");
                if(clazz==String.class) return (T)res;
                T result = JSON.parseObject(res,clazz);
                return result;
            } catch (IOException e) {
                e.printStackTrace();
                throw new HttpRequestException("获取返回内容出错:"+e.getMessage());
            }
        }else{
            throw new HttpRequestException("请求地址["+url+"]失败:"+response.message());
        }
    }

    public static void main(String[] args) throws HttpRequestException {
        String s = get("https://www.baidu.com",String.class);
        System.out.println(s);
    }
}

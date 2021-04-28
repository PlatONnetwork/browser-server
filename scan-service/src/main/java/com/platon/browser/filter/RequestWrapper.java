package com.platon.browser.filter;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.ServletUtil;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Map;

/**
 * 获取request的body，防止body丢失
 *
 * @author huangyongpeng@matrixelements.com
 * @date 2021/4/28
 */
@Slf4j
public class RequestWrapper extends HttpServletRequestWrapper {

    private byte[] body;

    private Map<String, String> paramMap;

    public RequestWrapper(HttpServletRequest request) {
        super(request);
        paramMap = ServletUtil.getParamMap(request);
        body = ServletUtil.getBodyBytes(request);
    }

    /**
     * 重写getInputStream方法
     *
     * @return
     */
    @Override
    public ServletInputStream getInputStream() throws IOException {
        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(body);
        ServletInputStream servletInputStream = new ServletInputStream() {
            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return true;
            }

            @Override
            public void setReadListener(ReadListener readListener) {
                throw new UnsupportedOperationException();
            }

            public int read() throws IOException {
                return byteArrayInputStream.read();
            }
        };
        return servletInputStream;
    }


    /**
     * 重写getReader方法
     *
     * @return
     */
    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(getInputStream()));
    }

    /**
     * 获取请求参数
     *
     * @param
     * @return java.lang.String
     * @author huangyongpeng@matrixelements.com
     * @date 2021/4/23
     */
    public String getParamBody() {
        try {
            if (ServletUtil.isGetMethod(this)) {
                return JSONUtil.toJsonStr(paramMap);
            } else {
                return StrUtil.str(body, Charset.defaultCharset());
            }
        } catch (Exception e) {
            log.error("获取请求参数异常", e);
            return "";
        }
    }

}

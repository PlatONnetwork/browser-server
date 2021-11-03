package com.platon.browser.filter;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.ServletUtil;
import cn.hutool.json.JSONUtil;
import com.platon.browser.bean.CommonConstant;
import com.platon.browser.utils.CommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 接口访问过滤器
 *
 * @author huangyongpeng@matrixelements.com
 * @date 2021/4/4
 */
@Slf4j
@WebFilter(filterName = "webAccessFilter", urlPatterns = "/*")
@Order(0)
@Component
public class WebAccessFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = ((HttpServletResponse) servletResponse);
        long start = System.currentTimeMillis();
        RequestWrapper requestWrapper = new RequestWrapper(request);
        String traceId = CommonUtil.createTraceId();
        CommonUtil.putTraceId(traceId);
        requestParamLog(requestWrapper);
        requestWrapper.setAttribute(CommonConstant.TRACE_ID, traceId);
        chain.doFilter(requestWrapper, response);
        response.setHeader(CommonConstant.TRACE_ID, traceId);
        log.info("[请求接口结束] http状态:{}, 耗时:{}ms", CommonUtil.ofNullable(() -> response.getStatus()).orElse(-1), System.currentTimeMillis() - start);
    }

    @Override
    public void destroy() {
        CommonUtil.removeTraceId();
    }

    /**
     * 获取上游服务的trace-id
     * trace-id一般约束来源：请求头、Attribute、请求参数（json的第一层结构）
     *
     * @param requestWrapper
     * @return java.lang.String
     * @author huangyongpeng@matrixelements.com
     * @date 2021/4/23
     */
    private String getReqTraceId(RequestWrapper requestWrapper) {
        String traceId = "";
        try {
            traceId = CommonUtil.ofNullable(() -> requestWrapper.getHeader(CommonConstant.TRACE_ID)).orElse("");
            if (StrUtil.isBlank(traceId)) {
                traceId = CommonUtil.ofNullable(() -> requestWrapper.getAttribute(CommonConstant.TRACE_ID).toString()).orElse("");
                if (StrUtil.isBlank(traceId)) {
                    String param = StrUtil.blankToDefault(requestWrapper.getParamBody(), "");
                    if (StrUtil.isNotBlank(param) && JSONUtil.isJson(param)) {
                        if (JSONUtil.parseObj(param).containsKey(CommonConstant.REQ_TRACE_ID)) {
                            traceId = JSONUtil.parseObj(param).getStr(CommonConstant.REQ_TRACE_ID);
                        } else {
                            traceId = CommonUtil.createTraceId();
                        }
                    } else {
                        traceId = CommonUtil.createTraceId();
                    }
                }
            }
        } catch (Exception e) {
            log.error("获取上游服务的trace-id异常，将创建新的trace-id", e);
        } finally {
            if (StrUtil.isBlank(traceId)) {
                traceId = CommonUtil.createTraceId();
            }
        }
        return traceId;
    }

    /**
     * 打印请求参数，暂时不兼容文件上传多参数打印
     *
     * @param requestWrapper
     * @return void
     * @author huangyongpeng@matrixelements.com
     * @date 2021/4/23
     */
    private void requestParamLog(RequestWrapper requestWrapper) {
        if (!ServletUtil.isMultipart(requestWrapper)) {
            // 不是文件上传，则打印请求参数
            log.info("[请求接口开始] 路径URL:{}, 请求方式:{}, Content-Type:{}, 请求参数:{}",
                     requestWrapper.getRequestURL(),
                     requestWrapper.getMethod(),
                     StrUtil.blankToDefault(requestWrapper.getContentType(), "无"),
                     StrUtil.blankToDefault(requestWrapper.getParamBody(), "无"));
        } else {
            log.info("[请求接口开始] 路径URL:{}, 请求方式:{}, Content-Type:{}, 请求参数:文件上传", requestWrapper.getRequestURL(), requestWrapper.getMethod(), StrUtil.blankToDefault(requestWrapper.getContentType(), "无"));
        }
    }

}

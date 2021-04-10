package com.platon.browser.filter;

import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.StrUtil;
import com.platon.browser.bean.CommonConstant;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
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
        String traceId = StrUtil.removeAll(UUID.randomUUID().toString(), "-");
        servletRequest.setAttribute(CommonConstant.TRACE_ID, traceId);
        MDC.put(CommonConstant.TRACE_ID, traceId);
        long start = System.currentTimeMillis();
        log.info("[请求接口开始] 路径URL:{}, 请求方式:{}, 起始时间:{}",
                request.getRequestURL(),
                request.getMethod(),
                start);
        chain.doFilter(servletRequest, servletResponse);
        log.info("[请求接口结束] 路径URL:{}, 请求方式:{}, 耗时:{}ms",
                request.getRequestURL(),
                request.getMethod(),
                System.currentTimeMillis() - start);
    }

    @Override
    public void destroy() {
        MDC.clear();
    }

}

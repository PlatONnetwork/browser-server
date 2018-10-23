package com.platon.browserweb.config;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.github.fartherp.framework.exception.SystemException;
import com.platon.browserweb.common.base.AppException;
import com.platon.browserweb.common.base.JsonResp;
import com.platon.browserweb.common.enums.ErrorCodeEnum;
import com.platon.browserweb.common.util.JSONUtil;

/**
 * Spring MVC 配置
 * Author:szuhcc@163.com
 * Date:2018/4/28
 */
@Configuration
@DependsOn("beanLocator")
public class WebMvcConfig extends WebMvcConfigurerAdapter {

    private final Logger logger = LoggerFactory.getLogger(WebMvcConfig.class);

    //统一异常处理
    @Override
    public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> exceptionResolvers) {
        exceptionResolvers.add(new HandlerExceptionResolver() {
            public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception e) {
                response.setContentType(MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8");
                PrintWriter writer = null;
                //    	e.printStackTrace();
                logger.error("thread={}|error", Thread.currentThread().toString(), e);

                //WebApplicationContext context = (WebApplicationContext) servletContext.getAttribute(CONTEXT_ATTRIBUTE);

                @SuppressWarnings("rawtypes")
				JsonResp result = JsonResp.asEmpty().build();

                // 处理unchecked handler
                if (e instanceof AppException) {
                    AppException exception = (AppException) e;
                    result = JsonResp.asEmpty().errorMsg(exception.getErrorCode(), ((AppException) e).getErrorMessage()).build();
                } else {
//                    if ("true".equals(PropertyConfigurer.getIsSendSms())) {
//                        // 线上环境
//                        result = JsonResp.asEmpty().errorMsg(ErrorCodeEnum.Default).build(); //系统错误
//                    } else {
                        // 开发/测试
                        SystemException systemException;
                        if (e instanceof DataIntegrityViolationException) {
                            systemException = new SystemException(e.getCause());
                        } else {
                            systemException = new SystemException(e);
                        }
                        if (systemException.getMessage() != null) {
                            result = JsonResp.asEmpty().errorMsg(ErrorCodeEnum.Default.getCode(), systemException.getMessage()).build(); //系统错误
                        } else {
                            result = JsonResp.asEmpty().errorMsg(ErrorCodeEnum.Default).build(); //系统错误
                        }
//                    }
                }

                try {
                    writer = response.getWriter();
                    writer.write(JSONUtil.bean2Json(result));
                    writer.flush();
                } catch (IOException ex) {
                    logger.error("response writer IOException:", ex);
                } finally {
                    if (null != writer) {
                        writer.close();
                    }
                }
                return new ModelAndView();
            }
        });
    }
}

//package com.platon.browser.config;
//
//import com.alibaba.fastjson.JSON;
//import com.platon.browser.common.base.BaseResp;
//import com.platon.browser.common.enums.RetEnum;
//import com.platon.browser.message.SubscribeService;
//import com.platon.browser.util.I18nEnum;
//import com.platon.browser.util.I18nUtil;
//import org.apache.commons.lang3.StringUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.util.Enumeration;
//
//@Component
//public class PlatonInterceptor extends HandlerInterceptorAdapter {
//
//    private final Logger logger = LoggerFactory.getLogger(SubscribeService.class);
//    @Autowired
//    private ChainsConfig chainsConfig;
//    @Autowired
//    private I18nUtil i18n;
//
//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object obj) throws Exception {
//        String chainId = request.getParameter("cid");
//        Enumeration<String> names = request.getParameterNames();
//        if (StringUtils.isEmpty(chainId)) {
//            logger.error("Chain ID is empty!");
//            response.setContentType("application/json;charset=utf-8");
//            BaseResp resp = BaseResp.build(RetEnum.RET_PARAM_VALLID.getCode(),i18n.i(I18nEnum.REQUEST_INVALID_PARAM),null);
//            response.getWriter().write(JSON.toJSONString(resp));
//            return false;
//        }
//        if(!chainsConfig.getChainIds().contains(chainId)){
//            logger.error("Invalid Chain ID: {}", chainId);
//            response.setContentType("application/json;charset=utf-8");
//            BaseResp resp = BaseResp.build(RetEnum.RET_PARAM_VALLID.getCode(),i18n.i(I18nEnum.CHAIN_ID_ERROR),null);
//            response.getWriter().write(JSON.toJSONString(resp));
//            return false;
//        }
//        return true;
//    }
//}

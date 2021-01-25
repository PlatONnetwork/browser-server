package com.platon.browser.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.platon.browser.bean.CustomToken;
import com.platon.browser.bean.CustomTokenDetail;
import com.platon.browser.dao.mapper.*;
import com.platon.browser.request.token.QueryTokenDetailReq;
import com.platon.browser.request.token.QueryTokenIdDetailReq;
import com.platon.browser.request.token.QueryTokenIdListReq;
import com.platon.browser.request.token.QueryTokenListReq;
import com.platon.browser.response.RespPage;
import com.platon.browser.response.token.QueryTokenDetailResp;
import com.platon.browser.response.token.QueryTokenIdDetailResp;
import com.platon.browser.response.token.QueryTokenIdListResp;
import com.platon.browser.response.token.QueryTokenListResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Token模块实现类
 *
 * @author AgentRJ
 * @create 2020-09-23 16:02
 */
@Slf4j
@Service
public class TokenService {

    @Resource
    private CustomTokenMapper customTokenMapper;

    @Resource
    private CustomTokenInventoryMapper customTokenInventoryMapper;

    public RespPage<QueryTokenListResp> queryTokenList(QueryTokenListReq req) {
        // page params: #{offset}, #{size}
        RespPage<QueryTokenListResp> result = new RespPage<>();
        PageHelper.startPage(req.getPageNo(),req.getPageSize());
        Page<CustomToken> customTokens = customTokenMapper.selectListByType(req.getType());
        List<QueryTokenListResp> data = customTokens.stream().map(customToken -> QueryTokenListResp.fromToken(customToken)).collect(Collectors.toList());
        result.init(customTokens,data);
        return result;

    }

    public QueryTokenDetailResp queryTokenDetail(QueryTokenDetailReq req) {
        CustomTokenDetail customTokenDetail = customTokenMapper.selectDetailByAddress(req.getAddress());
        return  QueryTokenDetailResp.fromTokenDetail(customTokenDetail);
    }


    public RespPage<QueryTokenIdListResp> queryTokenList(QueryTokenIdListReq req) {
        RespPage<QueryTokenIdListResp> result = new RespPage<>();
        PageHelper.startPage(req.getPageNo(), req.getPageSize());
        return result;
    }

    public QueryTokenIdDetailResp queryTokenIdDetail(QueryTokenIdDetailReq req) {
//        QueryTokenIdDetailResp resp = customTokenMapper.selectDetailByAddress(req.getAddress());
//        return  QueryTokenIdDetailResp.fromTokenDetail(customTokenDetail);
        return null;
    }


}

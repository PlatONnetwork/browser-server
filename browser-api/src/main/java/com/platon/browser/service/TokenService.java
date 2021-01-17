package com.platon.browser.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.platon.browser.bean.CustomToken;
import com.platon.browser.bean.CustomTokenDetail;
import com.platon.browser.dao.entity.Erc20Token;
import com.platon.browser.dao.entity.Erc20TokenDetailWithBLOBs;
import com.platon.browser.dao.entity.Token;
import com.platon.browser.dao.entity.TokenExample;
import com.platon.browser.dao.mapper.CustomTokenMapper;
import com.platon.browser.dao.mapper.Erc20TokenDetailMapper;
import com.platon.browser.dao.mapper.Erc20TokenMapper;
import com.platon.browser.dao.mapper.TokenMapper;
import com.platon.browser.request.token.QueryTokenDetailReq;
import com.platon.browser.request.token.QueryTokenListReq;
import com.platon.browser.response.RespPage;
import com.platon.browser.response.token.QueryTokenDetailResp;
import com.platon.browser.response.token.QueryTokenListResp;
import com.platon.browser.service.redis.RedisErc20TokenService;
import com.platon.browser.utils.ConvertUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    private Erc20TokenMapper erc20TokenMapper;
    @Resource
    private Erc20TokenDetailMapper erc20TokenDetailMapper;

    @Resource
    private CustomTokenMapper customTokenMapper;

    public RespPage<QueryTokenListResp> queryTokenList(QueryTokenListReq req) {
        // page params: #{offset}, #{size}
        RespPage<QueryTokenListResp> result = new RespPage<>();
        PageHelper.startPage(req.getPageNo(),req.getPageSize());
        Page<CustomToken> customTokens = customTokenMapper.selectListByType(req.getType());
        List<QueryTokenListResp> data = customTokens.stream().map(customToken -> QueryTokenListResp.fromToken(customToken)).collect(Collectors.toList());
        result.init(customTokens,data);
        return result;

//        // page params: #{offset}, #{size}
//        RespPage<QueryTokenListResp> result = new RespPage<>();
//        PageHelper.PageParams pageParams = PageHelper.buildPageParams(req);
//        Map params = new HashMap<>();
//        params.put("size", pageParams.getSize());
//        params.put("offset", pageParams.getOffset());
//
//        List<Erc20Token> tokenIdList = this.erc20TokenMapper.listErc20TokenIds(params);
//        if (tokenIdList == null || tokenIdList.isEmpty()) {
//            return result;
//        }
//        List<Long> tokenIds = tokenIdList.stream().map(Erc20Token::getId).collect(Collectors.toList());
//        List<Erc20Token> tokenList = this.erc20TokenMapper.listErc20TokenByIds(tokenIds);
//        //int totalCount = this.erc20TokenMapper.totalErc20Token(new HashMap<>());
//        long totalCount = dbHelperCache.getTokenCount();
//        if (null == tokenList) {
//            return result;
//        }
//
//        // 排序：holder id
//        List<Erc20Token> sortedTokenList = tokenList
//                .stream().sorted(Comparator.comparing(Erc20Token::getHolder, Comparator.reverseOrder()).thenComparing(Erc20Token::getId, Comparator.reverseOrder()))
//                .collect(Collectors.toList());
//
//        // convert data
//        List<QueryTokenListResp> queryTokenList = sortedTokenList.parallelStream().filter(p -> p != null).map(p -> {
//            return QueryTokenListResp.fromErc20Token(p);
//        }).collect(Collectors.toList());
//
//        result.init(queryTokenList, totalCount, tokenList.size(),
//            PageHelper.getPageTotal((int)totalCount, pageParams.getSize()));
//        return result;
    }

    public QueryTokenDetailResp queryTokenDetail(QueryTokenDetailReq req) {
        CustomTokenDetail customTokenDetail = customTokenMapper.selectDetailByAddress(req.getAddress());
        return  QueryTokenDetailResp.fromTokenDetail(customTokenDetail);

//        // main info.
//        Erc20Token erc20Token = this.erc20TokenMapper.selectByAddress(req.getAddress());
//
//        // attach info.
//        Erc20TokenDetailWithBLOBs detailWithBLOBs = this.erc20TokenDetailMapper.selectByAddress(req.getAddress());
//        QueryTokenDetailResp response = QueryTokenDetailResp.fromErc20Token(erc20Token);
//        if (detailWithBLOBs != null && response != null) {
//            response.setIcon(detailWithBLOBs.getIcon());
//            response.setWebSite(detailWithBLOBs.getWebSite());
//            response.setAbi(detailWithBLOBs.getAbi());
//            response.setBinCode(detailWithBLOBs.getBinCode());
//            response.setSourceCode(detailWithBLOBs.getSourceCode());
//        }
//        // cal total supply -> decimal
//        if (response != null && erc20Token != null) {
//            BigDecimal totalSupply =
//                    ConvertUtil.convertByFactor(erc20Token.getTotalSupply(), erc20Token.getDecimal());
//            response.setTotalSupply(totalSupply.toString());
//        }
//        return response;
    }

}

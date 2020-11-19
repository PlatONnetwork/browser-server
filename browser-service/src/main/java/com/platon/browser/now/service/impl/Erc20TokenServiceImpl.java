package com.platon.browser.now.service.impl;

import com.platon.browser.dao.entity.Erc20Token;
import com.platon.browser.dao.entity.Erc20TokenDetailWithBLOBs;
import com.platon.browser.dao.mapper.Erc20TokenDetailMapper;
import com.platon.browser.dao.mapper.Erc20TokenMapper;
import com.platon.browser.now.service.Erc20TokenService;
import com.platon.browser.req.token.QueryTokenDetailReq;
import com.platon.browser.req.token.QueryTokenListReq;
import com.platon.browser.res.RespPage;
import com.platon.browser.res.token.QueryTokenDetailResp;
import com.platon.browser.res.token.QueryTokenListResp;
import com.platon.browser.util.ConvertUtil;
import com.platon.browser.util.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
public class Erc20TokenServiceImpl implements Erc20TokenService {

    @Autowired
    private Erc20TokenMapper erc20TokenMapper;

    @Autowired
    private Erc20TokenDetailMapper erc20TokenDetailMapper;

    @Override
    public RespPage<QueryTokenListResp> queryTokenList(QueryTokenListReq req) {
        // page params: #{offset}, #{size}
        RespPage<QueryTokenListResp> result = new RespPage<>();
        PageHelper.PageParams pageParams = PageHelper.buildPageParams(req);
        Map params = new HashMap<>();
        params.put("size", pageParams.getSize());
        params.put("offset", pageParams.getOffset());

        List<Erc20Token> tokenIdList = this.erc20TokenMapper.listErc20TokenIds(params);
        if (tokenIdList == null || tokenIdList.size() == 0) {
            return result;
        }
        List<Long> tokenIds = tokenIdList.stream().map(Erc20Token::getId).collect(Collectors.toList());
        List<Erc20Token> tokenList = this.erc20TokenMapper.listErc20TokenByIds(tokenIds);
        int totalCount = this.erc20TokenMapper.totalErc20Token(new HashMap<>());
        if (null == tokenList) {
            return result;
        }

        // 排序：holder id
        List<Erc20Token> sortedTokenList = tokenList
                .stream().sorted(Comparator.comparing(Erc20Token::getHolder, Comparator.reverseOrder()).thenComparing(Erc20Token::getId, Comparator.reverseOrder()))
                .collect(Collectors.toList());

        // convert data
        List<QueryTokenListResp> queryTokenList = sortedTokenList.parallelStream().filter(p -> p != null).map(p -> {
            return QueryTokenListResp.fromErc20Token(p);
        }).collect(Collectors.toList());

        result.init(queryTokenList, totalCount, tokenList.size(),
            PageHelper.getPageTotal(totalCount, pageParams.getSize()));
        return result;
    }

    @Override
    public QueryTokenDetailResp queryTokenDetail(QueryTokenDetailReq req) {
        // main info.
        Erc20Token erc20Token = this.erc20TokenMapper.selectByAddress(req.getAddress());

        // attach info.
        Erc20TokenDetailWithBLOBs detailWithBLOBs = this.erc20TokenDetailMapper.selectByAddress(req.getAddress());
        QueryTokenDetailResp response = QueryTokenDetailResp.fromErc20Token(erc20Token);
        if (detailWithBLOBs != null && response != null) {
            response.setIcon(detailWithBLOBs.getIcon());
            response.setWebSite(detailWithBLOBs.getWebSite());
            response.setAbi(detailWithBLOBs.getAbi());
            response.setBinCode(detailWithBLOBs.getBinCode());
            response.setSourceCode(detailWithBLOBs.getSourceCode());
        }
        // cal total supply -> decimal
        if (response != null && erc20Token != null) {
            BigDecimal totalSupply =
                    ConvertUtil.convertByFactor(erc20Token.getTotalSupply(), erc20Token.getDecimal());
            response.setTotalSupply(totalSupply.toString());
        }
        return response;
    }

    @Override
    public int save(Erc20Token token) {
        return this.erc20TokenMapper.insert(token);
    }

    @Override
    public int batchSave(List<Erc20Token> list) {
        return this.erc20TokenMapper.batchInsert(list);
    }
}

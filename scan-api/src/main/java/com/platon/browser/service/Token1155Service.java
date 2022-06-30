package com.platon.browser.service;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.platon.browser.bean.CustomToken;
import com.platon.browser.bean.CustomToken1155Inventory;
import com.platon.browser.bean.CustomTokenDetail;
import com.platon.browser.bean.CustomTokenHolder;
import com.platon.browser.config.DownFileCommon;
import com.platon.browser.dao.custommapper.CustomToken1155InventoryMapper;
import com.platon.browser.dao.custommapper.CustomTokenHolderMapper;
import com.platon.browser.dao.custommapper.CustomTokenMapper;
import com.platon.browser.dao.entity.Token1155Inventory;
import com.platon.browser.dao.entity.Token1155InventoryExample;
import com.platon.browser.dao.entity.Token1155InventoryKey;
import com.platon.browser.dao.entity.TokenInventoryKey;
import com.platon.browser.dao.mapper.Token1155InventoryMapper;
import com.platon.browser.enums.I18nEnum;
import com.platon.browser.request.token.QueryTokenDetailReq;
import com.platon.browser.request.token.QueryTokenIdDetailReq;
import com.platon.browser.request.token.QueryTokenIdListReq;
import com.platon.browser.request.token.QueryTokenListReq;
import com.platon.browser.response.RespPage;
import com.platon.browser.response.account.AccountDownload;
import com.platon.browser.response.token.QueryTokenDetailResp;
import com.platon.browser.response.token.QueryTokenIdDetailResp;
import com.platon.browser.response.token.QueryTokenIdListResp;
import com.platon.browser.response.token.QueryTokenListResp;
import com.platon.browser.utils.CommonUtil;
import com.platon.browser.utils.I18nUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Token模块实现类
 *
 * @author AgentRJ
 * @create 2020-09-23 16:02
 */
@Slf4j
@Service
public class Token1155Service {

    @Resource
    private CustomTokenMapper customTokenMapper;


    @Resource
    private CustomTokenHolderMapper customTokenHolderMapper;

    @Resource
    private CustomToken1155InventoryMapper customToken1155InventoryMapper;

    @Resource
    private I18nUtil i18n;

    @Resource
    private DownFileCommon downFileCommon;

    @Resource
    private Token1155InventoryMapper token1155InventoryMapper;

    public RespPage<QueryTokenListResp> queryTokenList(QueryTokenListReq req) {
        // page params: #{offset}, #{size}
        RespPage<QueryTokenListResp> result = new RespPage<>();
        PageHelper.startPage(req.getPageNo(), req.getPageSize());
        Page<CustomToken> customTokens = customTokenMapper.selectListByType(req.getType());
        List<QueryTokenListResp> data = customTokens.stream().map(customToken -> QueryTokenListResp.fromToken(customToken)).collect(Collectors.toList());
        result.init(customTokens, data);
        return result;
    }

    public QueryTokenDetailResp queryTokenDetail(QueryTokenDetailReq req) {
        CustomTokenDetail customTokenDetail = customTokenMapper.selectDetailByAddress(req.getAddress());
        // 总供应量为0，则取值总库存量
        int total = 0;
        if (ObjectUtil.isNotNull(customTokenDetail) && CommonUtil.ofNullable(() -> customTokenDetail.getTotalSupply()).orElse("0").equalsIgnoreCase("0")) {
            Token1155InventoryExample example = new Token1155InventoryExample();
            example.createCriteria().andTokenAddressEqualTo(req.getAddress());
            Page<Token1155Inventory> totalTokenInventory = token1155InventoryMapper.selectByExample(example);
            total = totalTokenInventory.size();
            customTokenDetail.setTotalSupply(Convert.toStr(total));
        }
        return QueryTokenDetailResp.fromTokenDetail(customTokenDetail);
    }

    /**
     * ARC1155 库存列表
     *
     * @param req
     * @return com.platon.browser.response.RespPage<com.platon.browser.response.token.QueryTokenIdListResp>
     * @author huangyongpeng@matrixelements.com
     * @date 2021/1/28
     */
    public RespPage<QueryTokenIdListResp> queryTokenIdList(QueryTokenIdListReq req) {
        RespPage<QueryTokenIdListResp> result = new RespPage<>();
        PageHelper.startPage(req.getPageNo(), req.getPageSize());
        Token1155InventoryExample example = new Token1155InventoryExample();
        Token1155InventoryExample.Criteria criteria = example.createCriteria();
//        //根据地址、合约地址、tokenid去查询列表
//        if (StringUtils.isNotBlank(req.getAddress())) {
//            criteria.andOwnerEqualTo(req.getAddress());
//        }
        if (StringUtils.isNotBlank(req.getContract())) {
            criteria.andTokenAddressEqualTo(req.getContract());
        }
        if (StringUtils.isNotBlank(req.getTokenId())) {
            criteria.andTokenIdEqualTo(req.getTokenId());
        }
        Page<Token1155Inventory> tokenInventorys = token1155InventoryMapper.selectByExample(example);
        List<QueryTokenIdListResp> resps = new ArrayList<>();
        // 获取所有的库存

//
//
//        List<CustomTokenHolder> tokenHolderList = holderPage.getResult();
//
        Map<TokenInventoryKey, Token1155Inventory> tokenInventorMap = new HashMap<>();

        tokenInventorys.forEach(ele -> {
            TokenInventoryKey tokenInventoryKey = new TokenInventoryKey();
            tokenInventoryKey.setTokenId(ele.getTokenId());
            tokenInventoryKey.setTokenAddress(ele.getTokenAddress());
            tokenInventorMap.put(tokenInventoryKey, ele);
        });

        Page<CustomTokenHolder> holderPage = customTokenHolderMapper.selectERC1155Holder(req.getContract());


        holderPage.forEach(tokenHolder -> {
            TokenInventoryKey tokenInventoryKey = new TokenInventoryKey();
            tokenInventoryKey.setTokenId(tokenHolder.getTokenId());
            tokenInventoryKey.setTokenAddress(tokenHolder.getTokenAddress());
            Token1155Inventory token1155Inventory = tokenInventorMap.get(tokenInventoryKey);

            QueryTokenIdListResp resp = QueryTokenIdListResp.fromToken(tokenHolder, token1155Inventory);
            resps.add(resp);
        });
        result.init(tokenInventorys, resps);
        return result;
    }

    public QueryTokenIdDetailResp queryTokenIdDetail(QueryTokenIdDetailReq req) {
        Token1155InventoryKey tokenInventoryKey = new Token1155InventoryKey();
        tokenInventoryKey.setTokenAddress(req.getContract());
        tokenInventoryKey.setTokenId(StrUtil.emptyToDefault(req.getTokenId(), "0"));
        CustomToken1155Inventory customToken1155Inventory = customToken1155InventoryMapper.selectTokenInventory(tokenInventoryKey);
        return QueryTokenIdDetailResp.copy(customToken1155Inventory);
    }

    public AccountDownload exportTokenId(String address, String contract, String tokenId, String local, String timeZone) {
        PageHelper.startPage(1, 3000);
        Token1155InventoryExample example = new Token1155InventoryExample();
        Token1155InventoryExample.Criteria criteria = example.createCriteria();
//        //根据地址、合约地址、tokenid去查询列表
//        if (StringUtils.isNotBlank(address)) {
//            criteria.andOwnerEqualTo(address);
//        }
        if (StringUtils.isNotBlank(contract)) {
            criteria.andTokenAddressEqualTo(contract);
        }
        if (StringUtils.isNotBlank(tokenId)) {
            criteria.andTokenIdEqualTo(tokenId);
        }
        Page<Token1155Inventory> tokenInventorys = token1155InventoryMapper.selectByExample(example);
        String[] headers = {this.i18n.i(I18nEnum.DOWNLOAD_TOKEN_CSV_NAME, local), this.i18n.i(I18nEnum.DOWNLOAD_TOKEN_CSV_TOKEN, local), this.i18n.i(I18nEnum.DOWNLOAD_TOKEN_CSV_ADDRESS, local), this.i18n.i(I18nEnum.DOWNLOAD_TOKEN_CSV_TOKEN_ID,
                local), this.i18n.i(I18nEnum.DOWNLOAD_TOKEN_CSV_TX_COUNT,
                local)};
        List<Object[]> rows = new ArrayList<>();
        tokenInventorys.forEach(tokenInventory -> {
            Object[] row = {tokenInventory.getName(), tokenInventory.getTokenAddress(), tokenInventory.getTokenId(), tokenInventory.getTokenTxQty()};
            rows.add(row);
        });
        return this.downFileCommon.writeDate("Token-Id-" + address + "-" + new Date().getTime() + ".CSV", rows, headers);

    }

}

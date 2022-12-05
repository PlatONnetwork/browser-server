package com.platon.browser.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.platon.browser.bean.TokenIdListBean;
import com.platon.browser.config.DownFileCommon;
import com.platon.browser.dao.custommapper.CustomToken1155HolderMapper;
import com.platon.browser.dao.custommapper.CustomToken1155InventoryMapper;
import com.platon.browser.dao.entity.*;
import com.platon.browser.dao.mapper.Token1155HolderMapper;
import com.platon.browser.enums.I18nEnum;
import com.platon.browser.request.token.QueryTokenIdDetailReq;
import com.platon.browser.request.token.QueryTokenIdListReq;
import com.platon.browser.response.RespPage;
import com.platon.browser.response.account.AccountDownload;
import com.platon.browser.response.token.QueryTokenIdDetailResp;
import com.platon.browser.response.token.QueryTokenIdListResp;
import com.platon.browser.utils.I18nUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

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
    private CustomToken1155HolderMapper customToken1155HolderMapper;

    @Resource
    private Token1155HolderMapper token1155HolderMapper;

    @Resource
    private CustomToken1155InventoryMapper customToken1155InventoryMapper;

    @Resource
    private I18nUtil i18n;

    @Resource
    private DownFileCommon downFileCommon;

    /**
     * ARC1155 库存列表
     *
     * @param req
     * @return com.platon.browser.response.RespPage<com.platon.browser.response.token.QueryTokenIdListResp>
     * @date 2021/1/28
     */
    public RespPage<QueryTokenIdListResp> queryTokenIdList(QueryTokenIdListReq req) {
        RespPage<QueryTokenIdListResp> result = new RespPage<>();
        if (req.getPageNo() * req.getPageSize() > 10000) {
            result.setCode(500);
            result.setErrMsg("请求数据过大，请规范页面请求[PageNo()*PageSize()<=10000]");
            return result;
        }
        Token1155HolderKey key = new Token1155HolderKey();
        if (StrUtil.isNotBlank(req.getContract())) {
            key.setTokenAddress(req.getContract());
        }
        if (StrUtil.isNotBlank(req.getAddress())) {
            key.setAddress(req.getAddress());
        }
        if (StrUtil.isNotBlank(req.getTokenId())) {
            key.setTokenId(req.getTokenId());
        }
        PageHelper.startPage(req.getPageNo(), req.getPageSize());
        Page<TokenIdListBean> tokenIdList = customToken1155HolderMapper.findTokenIdList(key);
        List<QueryTokenIdListResp> list = new ArrayList<>();
        if (CollUtil.isNotEmpty(tokenIdList)) {
            for (TokenIdListBean tokenIdListBean : tokenIdList) {
                QueryTokenIdListResp queryTokenIdListResp = new QueryTokenIdListResp();
                queryTokenIdListResp.setAddress(tokenIdListBean.getAddress());
                queryTokenIdListResp.setContract(tokenIdListBean.getContract());
                queryTokenIdListResp.setTokenId(tokenIdListBean.getTokenId());
                queryTokenIdListResp.setImage(tokenIdListBean.getImage());
                queryTokenIdListResp.setTxCount(tokenIdListBean.getTxCount());
                queryTokenIdListResp.setBalance(tokenIdListBean.getBalance());
                list.add(queryTokenIdListResp);
            }
        }
        result.init(tokenIdList, list);
        return result;
    }

    public QueryTokenIdDetailResp queryTokenIdDetail(QueryTokenIdDetailReq req) {
        Token1155InventoryKey tokenInventoryKey = new Token1155InventoryKey();
        tokenInventoryKey.setTokenAddress(req.getContract());
        tokenInventoryKey.setTokenId(req.getTokenId());
        Token1155InventoryWithBLOBs token1155Inventory = customToken1155InventoryMapper.findOneByUK(tokenInventoryKey);
        QueryTokenIdDetailResp queryTokenIdDetailResp = new QueryTokenIdDetailResp();
        queryTokenIdDetailResp.setAddress("");
        queryTokenIdDetailResp.setContract(token1155Inventory.getTokenAddress());
        queryTokenIdDetailResp.setTokenId(token1155Inventory.getTokenId());
        queryTokenIdDetailResp.setBalance("");
        queryTokenIdDetailResp.setImage(token1155Inventory.getImage());
        queryTokenIdDetailResp.setName(token1155Inventory.getName());
        queryTokenIdDetailResp.setTxCount(token1155Inventory.getTokenTxQty());
        queryTokenIdDetailResp.setTokenName("");
        queryTokenIdDetailResp.setSymbol("");
        return queryTokenIdDetailResp;
    }

    public AccountDownload exportTokenId(String address, String contract, String tokenId, String local, String timeZone) {
        PageHelper.startPage(1, 3000);
        Token1155HolderExample example = new Token1155HolderExample();
        Token1155HolderExample.Criteria criteria = example.createCriteria();
        if (StringUtils.isNotBlank(address)) {
            criteria.andAddressEqualTo(address);
        }
        if (StringUtils.isNotBlank(contract)) {
            criteria.andTokenAddressEqualTo(contract);
        }
        if (StringUtils.isNotBlank(tokenId)) {
            criteria.andTokenIdEqualTo(tokenId);
        }
        Page<Token1155Holder> token1155HolderList = token1155HolderMapper.selectByExample(example);
        String[] headers = {this.i18n.i(I18nEnum.DOWNLOAD_TOKEN_CSV_TOKEN, local),
                            this.i18n.i(I18nEnum.DOWNLOAD_TOKEN_CSV_ADDRESS, local),
                            this.i18n.i(I18nEnum.DOWNLOAD_TOKEN_CSV_TOKEN_ID,
                                        local),
                            this.i18n.i(I18nEnum.DOWNLOAD_TOKEN_CSV_TX_COUNT,
                                        local)};
        List<Object[]> rows = new ArrayList<>();
        token1155HolderList.forEach(tokenInventory -> {
            Object[] row = {tokenInventory.getTokenAddress(),
                            tokenInventory.getAddress(),
                            tokenInventory.getTokenId(),
                            tokenInventory.getTokenOwnerTxQty()};
            rows.add(row);
        });
        return this.downFileCommon.writeDate("Token-Id-" + address + "-" + System.currentTimeMillis() + ".CSV", rows, headers);

    }

}

package com.platon.browser.now.service;

import com.platon.browser.dao.entity.Erc20TokenTransferRecord;
import com.platon.browser.dto.account.AccountDownload;
import com.platon.browser.req.token.QueryHolderTokenListReq;
import com.platon.browser.req.token.QueryTokenHolderListReq;
import com.platon.browser.req.token.QueryTokenTransferRecordListReq;
import com.platon.browser.res.RespPage;
import com.platon.browser.res.token.QueryHolderTokenListResp;
import com.platon.browser.res.token.QueryTokenHolderListResp;
import com.platon.browser.res.token.QueryTokenTransferRecordListResp;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

/**
 * 代币内部转账记录逻辑处理
 *
 * @Author: AgentRJ
 * @Date: 2020/9/23
 * @Version 1.0
 */
public interface Erc20TokenTransferRecordService {

    /**
     * 查询内部交易列表
     *
     * @param req 检索参数（合约地址）
     * @return
     */
    RespPage<QueryTokenTransferRecordListResp> queryTokenRecordList(QueryTokenTransferRecordListReq req);


    /**
     * 导出内部交易列表
     *
     * @return
     */
    AccountDownload exportTokenTransferList(String address, String contract, Long date, String local, String timeZone, String token, HttpServletResponse response);

    /**
     * 合约持有人列表
     *
     * @return
     */
    RespPage<QueryTokenHolderListResp> tokenHolderList(QueryTokenHolderListReq req);

    /**
     * 导出合约持有人列表
     *
     * @return
     */
    AccountDownload exportTokenHolderList(String contract, String local, String timeZone, String token, HttpServletResponse response);

    /**
     * 持有人对应token列表
     *
     * @return
     */
    RespPage<QueryHolderTokenListResp> holderTokenList(@Valid QueryHolderTokenListReq req);

    /**
     * 导出持有人对应token列表
     *
     * @return
     */
    AccountDownload exportHolderTokenList(String address, String local, String timeZone, String token, HttpServletResponse response);

    /**
     * 保存转账记录
     *
     * @param record 转账记录
     * @return
     */
    int save(Erc20TokenTransferRecord record);

    /**
     * 批量保存合约内部转账记录
     *
     * @param list 批量记录
     * @return
     */
    int batchSave(List<Erc20TokenTransferRecord> list);

}

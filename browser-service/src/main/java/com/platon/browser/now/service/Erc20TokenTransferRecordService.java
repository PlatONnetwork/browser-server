package com.platon.browser.now.service;

import com.platon.browser.dao.entity.Erc20TokenTransferRecord;
import com.platon.browser.req.token.QueryTokenTransferRecordListReq;
import com.platon.browser.res.RespPage;
import com.platon.browser.res.token.QueryTokenTransferRecordListResp;

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

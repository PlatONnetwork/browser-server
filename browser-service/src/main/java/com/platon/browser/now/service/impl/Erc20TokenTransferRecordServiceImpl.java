package com.platon.browser.now.service.impl;

import com.platon.browser.dao.entity.Erc20TokenTransferRecord;
import com.platon.browser.dao.mapper.Erc20TokenTransferRecordMapper;
import com.platon.browser.now.service.Erc20TokenTransferRecordService;
import com.platon.browser.req.token.QueryTokenTransferRecordListReq;
import com.platon.browser.res.RespPage;
import com.platon.browser.res.token.QueryTokenTransferRecordListResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 合约内部转账交易记录
 *
 * @author AgentRJ
 * @create 2020-09-23 16:08
 */
@Service
@Slf4j
public class Erc20TokenTransferRecordServiceImpl implements Erc20TokenTransferRecordService {

    @Autowired
    private Erc20TokenTransferRecordMapper erc20TokenTransferRecordMapper;

    @Override
    public RespPage<QueryTokenTransferRecordListResp> queryTokenRecordList(QueryTokenTransferRecordListReq req) {
        // logic:
        // 1、合约内部交易列表中，数据存储于ES，列表的获取走ES获取
        // 2、所有查询直接走ES，不进行DB检索
        return null;
    }

    @Override
    public int save(Erc20TokenTransferRecord record) {
        return erc20TokenTransferRecordMapper.insert(record);
    }

    @Override
    public int batchSave(List<Erc20TokenTransferRecord> list) {
        return erc20TokenTransferRecordMapper.batchInsert(list);
    }
}

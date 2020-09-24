package com.platon.browser.now.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.platon.browser.dao.entity.Erc20TokenTransferRecord;
import com.platon.browser.dao.mapper.Erc20TokenTransferRecordMapper;
import com.platon.browser.dto.elasticsearch.ESResult;
import com.platon.browser.elasticsearch.TokenTransferRecordESRepository;
import com.platon.browser.elasticsearch.dto.ESTokenTransferRecord;
import com.platon.browser.elasticsearch.service.impl.ESQueryBuilderConstructor;
import com.platon.browser.elasticsearch.service.impl.ESQueryBuilders;
import com.platon.browser.now.service.Erc20TokenTransferRecordService;
import com.platon.browser.req.token.QueryTokenTransferRecordListReq;
import com.platon.browser.res.RespPage;
import com.platon.browser.res.token.QueryTokenTransferRecordListResp;
import com.platon.browser.util.EnergonUtil;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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

    @Autowired
    private TokenTransferRecordESRepository esTokenTransferRecordRepository;

    @Override
    public RespPage<QueryTokenTransferRecordListResp> queryTokenRecordList(QueryTokenTransferRecordListReq req) {
        if (log.isDebugEnabled()) {
            log.debug("~ queryTokenRecordList, params: " + JSON.toJSONString(req));
        }
        // logic:
        // 1、合约内部交易列表中，数据存储于ES，列表的获取走ES获取
        // 2、所有查询直接走ES，不进行DB检索
        RespPage<QueryTokenTransferRecordListResp> result = new RespPage<>();

        // construct of params
        ESQueryBuilderConstructor constructor = new ESQueryBuilderConstructor();

        ESResult<ESTokenTransferRecord> queryResultFromES = new ESResult<>();

        // condition: txHash/contract/txFrom/transferTo
        if (null != req.getContract()) {
            constructor.must(new ESQueryBuilders().terms("contract", Collections.singletonList(req.getContract())));
        }
        if (null != req.getAddress()) {
            constructor.buildMust(new BoolQueryBuilder()
                    .should(QueryBuilders.termQuery("txFrom", req.getAddress()))
                    .should(QueryBuilders.termQuery("transferTo", req.getAddress())));
        }
        // Set sort field
        constructor.setDesc("blockNumber");
        // response filed to show.
        constructor.setResult(new String[] { "txHash", "blockNumber", "txFrom", "contract",
                "transferTo", "transferValue", "decimal", "symbol", "blockTimestamp" });
        try {
            queryResultFromES = esTokenTransferRecordRepository.search(constructor, ESTokenTransferRecord.class, req.getPageNo(), req.getPageSize());
        } catch (Exception e) {
            log.error("检索代币交易列表失败", e);
            return result;
        }

        List<ESTokenTransferRecord> records = queryResultFromES.getRsData();
        if (null == records || records.size() == 0) {
            log.debug("未检索到有效数据，参数：" + JSON.toJSONString(req));
            return result;
        }

        List<QueryTokenTransferRecordListResp> recordListResp = records.parallelStream()
                .map(p -> {
                    return toQueryTokenTransferRecordListResp(req.getAddress(), p);
                }).collect(Collectors.toList());

        Page<?> page = new Page<>(req.getPageNo(),req.getPageSize());
        result.init(page, recordListResp);
        result.setTotalCount(queryResultFromES.getTotal());
        return result;
    }

    public QueryTokenTransferRecordListResp toQueryTokenTransferRecordListResp(String address, ESTokenTransferRecord record) {
        QueryTokenTransferRecordListResp resp =  QueryTokenTransferRecordListResp.builder()
                .txHash(record.getTxHash()).blockNumber(record.getBlockNumber())
                .txFrom(record.getTxFrom()).contract(record.getContract())
                .transferTo(record.getTransferTo())
                .decimal(record.getDecimal()).symbol(record.getSymbol())
                .methodSign(record.getMethodSign()).result(record.getResult())
                .blockTimestamp(record.getBlockTimestamp())
                .value(record.getValue())
                .build();
        // Processing accuracy calculation.
        BigDecimal transferValue = record.getTransferValue();
        String actualTransferValue = EnergonUtil.format(transferValue.divide(BigDecimal.valueOf(record.getDecimal())).setScale(12, BigDecimal.ROUND_DOWN), 12);
        resp.setTransferValue(new BigDecimal(actualTransferValue));

        // input or out
        if (address.equals(record.getTxFrom())) {
            resp.setType(QueryTokenTransferRecordListResp.TransferType.INPUT.val());
        } else {
            resp.setType(QueryTokenTransferRecordListResp.TransferType.OUT.val());
        }
        return resp;
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

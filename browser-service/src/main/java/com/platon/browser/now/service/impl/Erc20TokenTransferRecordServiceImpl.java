package com.platon.browser.now.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.platon.browser.common.DownFileCommon;
import com.platon.browser.dao.entity.Erc20TokenAddressRel;
import com.platon.browser.dao.entity.Erc20TokenAddressRelExample;
import com.platon.browser.dao.entity.Erc20TokenTransferRecord;
import com.platon.browser.dao.mapper.Erc20TokenAddressRelMapper;
import com.platon.browser.dao.mapper.Erc20TokenMapper;
import com.platon.browser.dao.mapper.Erc20TokenTransferRecordMapper;
import com.platon.browser.dto.account.AccountDownload;
import com.platon.browser.dto.elasticsearch.ESResult;
import com.platon.browser.elasticsearch.TokenTransferRecordESRepository;
import com.platon.browser.elasticsearch.dto.ESTokenTransferRecord;
import com.platon.browser.elasticsearch.service.impl.ESQueryBuilderConstructor;
import com.platon.browser.elasticsearch.service.impl.ESQueryBuilders;
import com.platon.browser.enums.I18nEnum;
import com.platon.browser.erc.ErcService;
import com.platon.browser.now.service.Erc20TokenTransferRecordService;
import com.platon.browser.req.token.QueryHolderTokenListReq;
import com.platon.browser.req.token.QueryTokenHolderListReq;
import com.platon.browser.req.token.QueryTokenTransferRecordListReq;
import com.platon.browser.res.RespPage;
import com.platon.browser.res.token.QueryHolderTokenListResp;
import com.platon.browser.res.token.QueryTokenHolderListResp;
import com.platon.browser.res.token.QueryTokenTransferRecordListResp;
import com.platon.browser.util.ConvertUtil;
import com.platon.browser.util.DateUtil;
import com.platon.browser.util.I18nUtil;
import com.platon.browser.utils.HexTool;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
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
    private Erc20TokenMapper erc20TokenMapper;

    @Autowired
    private TokenTransferRecordESRepository esTokenTransferRecordRepository;

    @Autowired
    private I18nUtil i18n;

    @Autowired
    private Erc20TokenAddressRelMapper erc20TokenAddressRelMapper;

    @Autowired
    private DownFileCommon downFileCommon;

    @Autowired
    private ErcService ercService;

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
        if (StringUtils.isNotEmpty(req.getContract())) {
            constructor.must(new ESQueryBuilders().terms("contract", Collections.singletonList(req.getContract())));
        }
        if (StringUtils.isNotEmpty(req.getAddress())) {
            constructor.buildMust(new BoolQueryBuilder()
                    .should(QueryBuilders.termQuery("from", req.getAddress()))
                    .should(QueryBuilders.termQuery("tto", req.getAddress())));
        }
        if (StringUtils.isNotEmpty(req.getTxHash())) {
            constructor.must(new ESQueryBuilders().term("hash", req.getTxHash()));
        }
        // Set sort field
        constructor.setDesc("seq");
        // response filed to show.
        constructor.setResult(new String[] { "seq", "hash", "bn", "from", "contract",
            "tto", "tValue", "decimal", "name", "symbol", "result", "bTime", "fromType", "toType"});
        try {
            queryResultFromES = this.esTokenTransferRecordRepository.search(constructor, ESTokenTransferRecord.class,
                req.getPageNo(), req.getPageSize());
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
                .filter(p -> p != null && p.getDecimal() != null)
                .map(p -> {
                    return this.toQueryTokenTransferRecordListResp(req.getAddress(), p);
                }).collect(Collectors.toList());

        Page<?> page = new Page<>(req.getPageNo(), req.getPageSize());
        result.init(page, recordListResp);
        result.setTotalCount(queryResultFromES.getTotal());
        return result;
    }

    @Override
    public AccountDownload exportTokenTransferList(String address, String contract, Long date, String local, String timeZone, String token, HttpServletResponse response) {
        AccountDownload accountDownload = new AccountDownload();
        if (StringUtils.isBlank(address) && StringUtils.isBlank(contract)) {
            return accountDownload;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date currentServerTime = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.log.error("exportTokenTransferList time:{}", format.format(currentServerTime));
        String msg = dateFormat.format(currentServerTime);
        this.log.info("导出地址交易列表数据起始日期：{},结束日期：{}", date, msg);

        // construct of params
        ESQueryBuilderConstructor constructor = new ESQueryBuilderConstructor();
        constructor.must(new ESQueryBuilders().range("bTime", new Date(date).getTime(), currentServerTime.getTime()));
        ESResult<ESTokenTransferRecord> queryResultFromES = new ESResult<>();
        // condition: txHash/contract/txFrom/transferTo
        if (StringUtils.isNotBlank(contract)) constructor.must(new ESQueryBuilders().term("contract", contract));
        if (StringUtils.isNotBlank(address)) constructor.buildMust(new BoolQueryBuilder()
                .should(QueryBuilders.termQuery("from", address))
                .should(QueryBuilders.termQuery("tto", address)));
        // Set sort field
        constructor.setDesc("seq");
        // response filed to show.
        constructor.setResult(new String[]{"seq", "hash", "bn", "from", "contract",
                "tto", "tValue", "decimal", "name", "symbol", "result", "bTime", "fromType", "toType"});
        try {
            queryResultFromES = this.esTokenTransferRecordRepository.search(constructor, ESTokenTransferRecord.class,
                    1, 3000);
        } catch (Exception e) {
            log.error("检索代币交易列表失败", e);
            return accountDownload;
        }
        List<Object[]> rows = new ArrayList<>();
        queryResultFromES.getRsData().stream().forEach(esTokenTransferRecord -> {
            boolean toIsAddress = address.equals(esTokenTransferRecord.getTto());
            String valueIn = toIsAddress ? esTokenTransferRecord.getTValue() : "0";
            String valueOut = !toIsAddress ? esTokenTransferRecord.getTValue() : "0";
            Object[] row = {esTokenTransferRecord.getHash(), esTokenTransferRecord.getBn(),
                    DateUtil.timeZoneTransfer(esTokenTransferRecord.getBTime(), "0", timeZone),
                    esTokenTransferRecord.getFrom(), esTokenTransferRecord.getTto(),
                    /** 数值von转换成lat，并保留十八位精确度 */
                    HexTool.append(ConvertUtil.convertByFactor(new BigDecimal(valueIn), esTokenTransferRecord.getDecimal()).toString()),
                    HexTool.append(ConvertUtil.convertByFactor(new BigDecimal(valueOut), esTokenTransferRecord.getDecimal()).toString()),
            };
            rows.add(row);
        });
        String[] headers = {this.i18n.i(I18nEnum.DOWNLOAD_ACCOUNT_CSV_HASH, local),
                this.i18n.i(I18nEnum.DOWNLOAD_BLOCK_CSV_NUMBER, local),
                this.i18n.i(I18nEnum.DOWNLOAD_BLOCK_CSV_TIMESTAMP, local),
                this.i18n.i(I18nEnum.DOWNLOAD_ACCOUNT_CSV_FROM, local),
                this.i18n.i(I18nEnum.DOWNLOAD_ACCOUNT_CSV_TO, local),
                this.i18n.i(I18nEnum.DOWNLOAD_ACCOUNT_CSV_VALUE_IN, local),
                this.i18n.i(I18nEnum.DOWNLOAD_ACCOUNT_CSV_VALUE_OUT, local)};
        return this.downFileCommon.writeDate("InnerTransaction-" + address + "-" + date + ".CSV", rows, headers);
    }

    @Override
    public RespPage<QueryTokenHolderListResp> tokenHolderList(QueryTokenHolderListReq req) {
        if (log.isDebugEnabled()) {
            log.debug("~ tokenHolderList, params: " + JSON.toJSONString(req));
        }
        RespPage<QueryTokenHolderListResp> result = new RespPage<>();
        /**
         * 倒序查询持有人列表
         */
        Erc20TokenAddressRelExample example = new Erc20TokenAddressRelExample();
        Erc20TokenAddressRelExample.Criteria criteria = example.createCriteria();
        criteria.andContractEqualTo(req.getContract());
        example.setOrderByClause(" update_time desc");
        PageHelper.startPage(req.getPageNo(), req.getPageSize());
        Page<Erc20TokenAddressRel> erc20TokenAddressRels = this.erc20TokenAddressRelMapper.selectByExample(example);
        List<QueryTokenHolderListResp> listResps = new ArrayList<>();
        erc20TokenAddressRels.stream().forEach(erc20TokenAddressRel -> {
            QueryTokenHolderListResp queryTokenHolderListResp = new QueryTokenHolderListResp();
            BigInteger balance = this.getAddressBalance(erc20TokenAddressRel.getContract(), erc20TokenAddressRel.getAddress());
            //金额转换成对应的值
            if (null != balance) {
                BigDecimal actualTransferValue = ConvertUtil.convertByFactor(new BigDecimal(balance), erc20TokenAddressRel.getDecimal());
                queryTokenHolderListResp.setBalance(actualTransferValue);
            } else {
                queryTokenHolderListResp.setBalance(BigDecimal.ZERO);
            }
            queryTokenHolderListResp.setAddress(erc20TokenAddressRel.getAddress());
            queryTokenHolderListResp.setPercent(new BigDecimal(balance).divide(erc20TokenAddressRel.getTotalSupply())
                    .multiply(BigDecimal.valueOf(100)).setScale(2, RoundingMode.HALF_UP).toString() + "%");
            listResps.add(queryTokenHolderListResp);
        });
        Page<?> page = new Page<>(req.getPageNo(), req.getPageSize());
        result.init(page, listResps);
        result.setTotalCount(erc20TokenAddressRels.getTotal());
        return result;
    }

    @Override
    public RespPage<QueryHolderTokenListResp> holderTokenList(@Valid QueryHolderTokenListReq req) {
        if (log.isDebugEnabled()) {
            log.debug("~ tokenHolderList, params: " + JSON.toJSONString(req));
        }
        RespPage<QueryHolderTokenListResp> result = new RespPage<>();
        /**
         * 倒序查询持有人列表
         */
        Erc20TokenAddressRelExample example = new Erc20TokenAddressRelExample();
        Erc20TokenAddressRelExample.Criteria criteria = example.createCriteria();
        criteria.andAddressEqualTo(req.getAddress());
        example.setOrderByClause(" update_time desc");
        PageHelper.startPage(req.getPageNo(), req.getPageSize());
        Page<Erc20TokenAddressRel> erc20TokenAddressRels = this.erc20TokenAddressRelMapper.selectByExample(example);
        List<QueryHolderTokenListResp> listResps = new ArrayList<>();
        erc20TokenAddressRels.stream().forEach(erc20TokenAddressRel -> {
            QueryHolderTokenListResp queryHolderTokenListResp = new QueryHolderTokenListResp();
            BeanUtils.copyProperties(erc20TokenAddressRel, queryHolderTokenListResp);
            BigInteger balance = this.getAddressBalance(erc20TokenAddressRel.getContract(), erc20TokenAddressRel.getAddress());
            //金额转换成对应的值
            if (null != balance) {
                BigDecimal actualTransferValue = ConvertUtil.convertByFactor(new BigDecimal(balance), erc20TokenAddressRel.getDecimal());
                queryHolderTokenListResp.setBalance(actualTransferValue);
            } else {
                queryHolderTokenListResp.setBalance(BigDecimal.ZERO);
            }
            listResps.add(queryHolderTokenListResp);
        });
        Page<?> page = new Page<>(req.getPageNo(), req.getPageSize());
        result.init(page, listResps);
        result.setTotalCount(erc20TokenAddressRels.getTotal());
        return result;
    }

    @Override
    public AccountDownload exportTokenHolderList(String contract, String local, String timeZone, String token, HttpServletResponse response) {
        Erc20TokenAddressRelExample example = new Erc20TokenAddressRelExample();
        Erc20TokenAddressRelExample.Criteria criteria = example.createCriteria();
        criteria.andContractEqualTo(contract);
        example.setOrderByClause(" update_time desc");
        PageHelper.startPage(1, 3000);
        Page<Erc20TokenAddressRel> erc20TokenAddressRels = this.erc20TokenAddressRelMapper.selectByExample(example);
        List<Object[]> rows = new ArrayList<>();
        erc20TokenAddressRels.stream().forEach(erc20TokenAddressRel -> {
            BigInteger balance = this.getAddressBalance(erc20TokenAddressRel.getContract(), erc20TokenAddressRel.getAddress());
            Object[] row = {erc20TokenAddressRel.getAddress(), HexTool.append(ConvertUtil.convertByFactor(new BigDecimal(balance),
                    erc20TokenAddressRel.getDecimal()).toString()),
                    new BigDecimal(balance).divide(erc20TokenAddressRel.getTotalSupply())
                            .multiply(BigDecimal.valueOf(100)).setScale(2, RoundingMode.HALF_UP).toString() + "%"
            };
            rows.add(row);
        });
        String[] headers = {this.i18n.i(I18nEnum.DOWNLOAD_CONTRACT_CSV_ADDRESS, local),
                this.i18n.i(I18nEnum.DOWNLOAD_CONTRACT_CSV_BALANCE, local),
                this.i18n.i(I18nEnum.DOWNLOAD_CONTRACT_CSV_PERCENT, local)};
        return this.downFileCommon.writeDate("TokenHolder-" + contract + "-" + new Date().getTime() + ".CSV", rows, headers);
    }

    @Override
    public AccountDownload exportHolderTokenList(String address, String local, String timeZone, String token, HttpServletResponse response) {

        Erc20TokenAddressRelExample example = new Erc20TokenAddressRelExample();
        Erc20TokenAddressRelExample.Criteria criteria = example.createCriteria();
        criteria.andAddressEqualTo(address);
        example.setOrderByClause(" update_time desc");
        PageHelper.startPage(1, 3000);
        Page<Erc20TokenAddressRel> erc20TokenAddressRels = this.erc20TokenAddressRelMapper.selectByExample(example);

        List<Object[]> rows = new ArrayList<>();
        erc20TokenAddressRels.stream().forEach(erc20TokenAddressRel -> {
            BigInteger balance = this.getAddressBalance(erc20TokenAddressRel.getContract(), erc20TokenAddressRel.getAddress());
            Object[] row = {erc20TokenAddressRel.getName(), erc20TokenAddressRel.getSymbol(),
                    HexTool.append(ConvertUtil.convertByFactor(new BigDecimal(balance), erc20TokenAddressRel.getDecimal()).toString()),
                    erc20TokenAddressRel.getDecimal(), erc20TokenAddressRel.getContract()
            };
            rows.add(row);
        });
        String[] headers = {
                this.i18n.i(I18nEnum.DOWNLOAD_CONTRACT_CSV_NAME, local),
                this.i18n.i(I18nEnum.DOWNLOAD_CONTRACT_CSV_SYMBOL, local),
                this.i18n.i(I18nEnum.DOWNLOAD_CONTRACT_CSV_BALANCE, local),
                this.i18n.i(I18nEnum.DOWNLOAD_CONTRACT_CSV_DECIMALS, local),
                this.i18n.i(I18nEnum.DOWNLOAD_CONTRACT_CSV_CONTRACT, local)
        };
        return this.downFileCommon.writeDate("HolderToken-" + address + "-" + new Date().getTime() + ".CSV", rows, headers);
    }

    public QueryTokenTransferRecordListResp toQueryTokenTransferRecordListResp(String address, ESTokenTransferRecord record) {
        QueryTokenTransferRecordListResp resp = QueryTokenTransferRecordListResp.builder()
                .txHash(record.getHash()).blockNumber(record.getBn())
                .txFrom(record.getFrom()).contract(record.getContract())
                .transferTo(record.getTto()).name(record.getName())
                .decimal(record.getDecimal()).symbol(record.getSymbol())
                .methodSign(record.getSign()).result(record.getResult())
                .blockTimestamp(record.getBTime()).systemTimestamp(new Date().getTime())
                .value(null == record.getValue() ? BigDecimal.ZERO : new BigDecimal(record.getValue()))
                .fromType(record.getFromType()).toType(record.getToType())
                .build();
        // Processing accuracy calculation.
        if (null != record.getTValue()) {
            BigDecimal transferValue = new BigDecimal(record.getTValue());
            BigDecimal actualTransferValue = ConvertUtil.convertByFactor(transferValue, record.getDecimal());
            resp.setTransferValue(actualTransferValue);
        } else {
            resp.setTransferValue(BigDecimal.ZERO);
        }
        // input or out
        if (null != address && address.equals(record.getFrom())) {
            resp.setType(QueryTokenTransferRecordListResp.TransferType.OUT.val());
        } else {
            resp.setType(QueryTokenTransferRecordListResp.TransferType.INPUT.val());
        }
        if (null == address) {
            resp.setType(QueryTokenTransferRecordListResp.TransferType.NONE.val());
        }
        return resp;
    }

    private BigInteger getAddressBalance(String contract, String address) {
        return this.ercService.getBalance(contract, address);
    }

    @Override
    public int save(Erc20TokenTransferRecord record) {
        return this.erc20TokenTransferRecordMapper.insert(record);
    }

    @Override
    public int batchSave(List<Erc20TokenTransferRecord> list) {
        return this.erc20TokenTransferRecordMapper.batchInsert(list);
    }
}

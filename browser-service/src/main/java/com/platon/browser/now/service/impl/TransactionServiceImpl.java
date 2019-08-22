package com.platon.browser.now.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.platon.browser.dao.entity.*;
import com.platon.browser.dao.mapper.BlockMapper;
import com.platon.browser.dao.mapper.TransactionMapper;
import com.platon.browser.dto.CustomProposal.TypeEnum;
import com.platon.browser.dto.RespPage;
import com.platon.browser.dto.account.AccountDownload;
import com.platon.browser.dto.transaction.TransactionDetail;
import com.platon.browser.enums.RetEnum;
import com.platon.browser.exception.BusinessException;
import com.platon.browser.now.service.TransactionService;
import com.platon.browser.now.service.cache.StatisticCacheService;
import com.platon.browser.redis.dto.TransactionRedis;
import com.platon.browser.req.PageReq;
import com.platon.browser.req.newtransaction.TransactionListByAddressRequest;
import com.platon.browser.req.newtransaction.TransactionListByBlockRequest;
import com.platon.browser.req.transaction.TransactionDetailReq;
import com.platon.browser.res.transaction.TransactionListResp;
import com.platon.browser.enums.I18nEnum;
import com.platon.browser.util.EnergonUtil;
import com.platon.browser.util.I18nUtil;
import com.univocity.parsers.csv.CsvWriter;
import com.univocity.parsers.csv.CsvWriterSettings;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.utils.Convert;

import javax.validation.Valid;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.math.RoundingMode;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final Logger logger = LoggerFactory.getLogger(TransactionServiceImpl.class);
    @Autowired
    private TransactionMapper transactionMapper;
    @Autowired
    private I18nUtil i18n;
    @Autowired
    private BlockMapper blockMapper;
    @Autowired
    private StatisticCacheService statisticCacheService;

    private TransactionDetail loadDetail(TransactionDetailReq req){
        TransactionExample condition = new TransactionExample();
        condition.createCriteria().andHashEqualTo(req.getTxHash());
        List<TransactionWithBLOBs> transactions = transactionMapper.selectByExampleWithBLOBs(condition);
        if (transactions.size()>1){
            logger.error("duplicate transaction: transaction hash {}",req.getTxHash());
            throw new BusinessException(RetEnum.RET_FAIL.getCode(), i18n.i(I18nEnum.TRANSACTION_ERROR_DUPLICATE));
        }
        if(transactions.size()==0){
            logger.error("invalid transaction hash {}",req.getTxHash());
            throw new BusinessException(RetEnum.RET_FAIL.getCode(),i18n.i(I18nEnum.TRANSACTION_ERROR_NOT_EXIST));
        }
        TransactionDetail returnData = new TransactionDetail();
        TransactionWithBLOBs initData = transactions.get(0);
        returnData.init(initData);

        returnData.setNodeName("Unknown");
        String nodeId=returnData.getNodeId();
        if(StringUtils.isBlank(nodeId)){
            BlockExample blockExample = new BlockExample();
            blockExample.createCriteria().andNumberEqualTo(initData.getBlockNumber());
            List<Block> blocks = blockMapper.selectByExample(blockExample);
            if(blocks.size()>0){
                Block block = blocks.get(0);
                if(StringUtils.isNotBlank(block.getNodeId())){
                    nodeId=block.getNodeId();
                    returnData.setNodeId(nodeId);
                }
            }
        }

//        if(StringUtils.isNotBlank(nodeId)){
//            Map<String,String> nameMap = nodeService.getNodeNameMap(req.getCid(),Arrays.asList(returnData.getNodeId()));
//            returnData.setNodeName(nameMap.get(returnData.getNodeId()));
//        }

        return returnData;
    }

    @Override
    public TransactionDetail getDetail(TransactionDetailReq req) {
        TransactionDetail transactionDetail = loadDetail(req);
        return transactionDetail;
    }

    @Override
    public RespPage<TransactionListResp> getTransactionList(@Valid PageReq req) {
        RespPage<TransactionListResp> result = new RespPage<>();
        List<TransactionListResp> lists = new LinkedList<>();
        List<TransactionRedis> items = statisticCacheService.getTransactionCache(req.getPageNo(), req.getPageSize());
        TransactionListResp transactionListResp = new TransactionListResp();
        for (TransactionRedis transactionRedis:items) {
            transactionListResp.setTxHash(transactionRedis.getHash());
            transactionListResp.setFrom(transactionRedis.getFrom());
            transactionListResp.setTo(transactionRedis.getTo());
            transactionListResp.setValue(EnergonUtil.format(Convert.fromVon(transactionRedis.getValue(), Convert.Unit.LAT).setScale(18,RoundingMode.DOWN)));
            transactionListResp.setActualTxCost(EnergonUtil.format(Convert.fromVon(transactionRedis.getActualTxCost(), Convert.Unit.LAT).setScale(18,RoundingMode.DOWN)));
            transactionListResp.setTxType(transactionRedis.getTxType());
            transactionListResp.setServerTime(new Date().getTime());
            transactionListResp.setTimestamp(transactionRedis.getTimestamp().getTime());
            transactionListResp.setBlockNumber(transactionRedis.getBlockNumber());
            //TODO
            transactionListResp.setFailReason("");
            transactionListResp.setReceiveType(transactionRedis.getReceiveType());
            lists.add(transactionListResp);
        }
        Page<?> page = new Page<>(req.getPageNo(),req.getPageSize());
        result.init(page, lists);
        return result;
    }

    @Override
    public RespPage<TransactionListResp> getTransactionListByBlock(TransactionListByBlockRequest req) {
        RespPage<TransactionListResp> result = new RespPage<>();
        List<TransactionListResp> lists = new LinkedList<>();
        TransactionExample transactionExample = new TransactionExample();
        TransactionExample.Criteria criteria = transactionExample.createCriteria()
                .andBlockNumberEqualTo(req.getBlockNumber().longValue());
        if (req.getTxType() != null && !req.getTxType().isEmpty()) {
            criteria.andTxTypeEqualTo(req.getTxType());
        }
        PageHelper.startPage(req.getPageNo(),req.getPageSize());
        List<Transaction> items = transactionMapper.selectByExample(transactionExample);
        TransactionListResp transactionListResp = new TransactionListResp();
        for (Transaction transaction:items) {
            transactionListResp.setTxHash(transaction.getHash());
            transactionListResp.setFrom(transaction.getFrom());
            transactionListResp.setTo(transaction.getTo());
            transactionListResp.setValue(EnergonUtil.format(Convert.fromVon(transaction.getValue(), Convert.Unit.LAT).setScale(18,RoundingMode.DOWN)));
            transactionListResp.setActualTxCost(EnergonUtil.format(Convert.fromVon(transaction.getActualTxCost(), Convert.Unit.LAT).setScale(18,RoundingMode.DOWN)));
            transactionListResp.setTxType(transaction.getTxType());
            transactionListResp.setServerTime(new Date().getTime());
            transactionListResp.setTimestamp(transaction.getTimestamp().getTime());
            transactionListResp.setBlockNumber(transaction.getBlockNumber());
            //TODO
            transactionListResp.setFailReason("");
            transactionListResp.setReceiveType(transaction.getReceiveType());
            lists.add(transactionListResp);
        }
        Page<?> page = new Page<>(req.getPageNo(),req.getPageSize());
        result.init(page, lists);
        return result;
    }

    @Override
    public RespPage<TransactionListResp> getTransactionListByAddress(TransactionListByAddressRequest req) {
        RespPage<TransactionListResp> result = new RespPage<>();
        List<TransactionListResp> lists = new LinkedList<>();
        TransactionExample transactionExample = new TransactionExample();
        // TODO
        TransactionExample.Criteria first = transactionExample.createCriteria()
                .andFromEqualTo(req.getAddress());
        TransactionExample.Criteria second = transactionExample.createCriteria()
                .andToEqualTo(req.getAddress());
        if (req.getTxType() != null && !req.getTxType().isEmpty()) {
            first.andTxTypeEqualTo(req.getTxType());
            second.andTxTypeEqualTo(req.getTxType());
        }
        PageHelper.startPage(req.getPageNo(),req.getPageSize());
        transactionExample.or(second);
        List<Transaction> items = transactionMapper.selectByExample(transactionExample);
        TransactionListResp transactionListResp = new TransactionListResp();
        for (Transaction transaction:items) {
            transactionListResp.setTxHash(transaction.getHash());
            transactionListResp.setFrom(transaction.getFrom());
            transactionListResp.setTo(transaction.getTo());
            transactionListResp.setValue(EnergonUtil.format(Convert.fromVon(transaction.getValue(), Convert.Unit.LAT).setScale(18,RoundingMode.DOWN)));
            transactionListResp.setActualTxCost(EnergonUtil.format(Convert.fromVon(transaction.getActualTxCost(), Convert.Unit.LAT).setScale(18,RoundingMode.DOWN)));
            transactionListResp.setTxType(transaction.getTxType());
            transactionListResp.setServerTime(new Date().getTime());
            transactionListResp.setTimestamp(transaction.getTimestamp().getTime());
            transactionListResp.setBlockNumber(transaction.getBlockNumber());
            //TODO
            transactionListResp.setFailReason("");
            transactionListResp.setReceiveType(transaction.getReceiveType());
            lists.add(transactionListResp);
        }
        Page<?> page = new Page<>(req.getPageNo(),req.getPageSize());
        result.init(page, lists);
        return result;
    }

    public AccountDownload transactionListByAddressDownload(String address, String date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date currentServerTime = new Date();
        logger.info("导出地址交易列表数据起始日期：{},结束日期：{}", date, simpleDateFormat.format(currentServerTime));
        TransactionExample transactionExample = new TransactionExample();
        TransactionExample.Criteria first = transactionExample.createCriteria()
                .andFromEqualTo(address);
        TransactionExample.Criteria second = transactionExample.createCriteria()
                .andToEqualTo(address);
        // 限制最多导出3万条记录
        PageHelper.startPage(1,30000);
        transactionExample.or(first);
        transactionExample.or(second);
        List<Transaction> items = transactionMapper.selectByExample(transactionExample);
        List<Object[]> rows = new ArrayList<>();

        items.forEach(transaction -> {

            boolean toIsAddress = address.equals(transaction.getTo());
            String valueIn = toIsAddress? transaction.getValue() : "";
            String valueOut = !toIsAddress? transaction.getValue() : "";

            Object[] row = {
                    transaction.getHash(),
                    transaction.getBlockNumber(),
                    transaction.getTimestamp(),
                    TypeEnum.getEnum(Integer.valueOf(transaction.getTxType())).getDesc(),
                    transaction.getFrom(),
                    transaction.getTo(),
                    EnergonUtil.format(Convert.fromVon(valueIn, Convert.Unit.LAT).setScale(18,RoundingMode.DOWN)),
                    EnergonUtil.format(Convert.fromVon(valueOut, Convert.Unit.LAT).setScale(18,RoundingMode.DOWN)),
                    transaction.getGasUsed()
            };
            rows.add(row);
        });

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Writer outputWriter = new OutputStreamWriter(byteArrayOutputStream, Charset.defaultCharset());
        try {
            outputWriter.write(new String(new byte[] { (byte) 0xEF, (byte) 0xBB,(byte) 0xBF }));
        } catch (IOException e) {
            e.printStackTrace();
        }
        CsvWriter writer = new CsvWriter(outputWriter, new CsvWriterSettings());
        writer.writeHeaders(
                i18n.i(I18nEnum.DOWNLOAD_ACCOUNT_CSV_HASH),
                i18n.i(I18nEnum.DOWNLOAD_BLOCK_CSV_NUMBER),
                i18n.i(I18nEnum.DOWNLOAD_BLOCK_CSV_TIMESTAMP),
                i18n.i(I18nEnum.DOWNLOAD_ACCOUNT_CSV_TYPE),
                i18n.i(I18nEnum.DOWNLOAD_ACCOUNT_CSV_FROM),
                i18n.i(I18nEnum.DOWNLOAD_ACCOUNT_CSV_TO),
                i18n.i(I18nEnum.DOWNLOAD_ACCOUNT_CSV_VALUE_IN),
                i18n.i(I18nEnum.DOWNLOAD_ACCOUNT_CSV_VALUE_OUT),
                i18n.i(I18nEnum.DOWNLOAD_ACCOUNT_CSV_FEE)
        );
        writer.writeRowsAndClose(rows);
        AccountDownload accountDownload = new AccountDownload();
        accountDownload.setData(byteArrayOutputStream.toByteArray());
        accountDownload.setFilename("Transaction-" + address + "-" + date + ".CSV");
        accountDownload.setLength(byteArrayOutputStream.size());
        return accountDownload;
    }
}

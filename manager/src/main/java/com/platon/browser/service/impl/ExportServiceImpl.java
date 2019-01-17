package com.platon.browser.service.impl;

import com.github.pagehelper.PageHelper;
import com.platon.browser.dao.entity.Block;
import com.platon.browser.dto.account.AccountDownload;
import com.platon.browser.dto.account.AddressDetail;
import com.platon.browser.dto.block.BlockDownload;
import com.platon.browser.enums.TransactionStatusEnum;
import com.platon.browser.enums.TransactionTypeEnum;
import com.platon.browser.req.account.AccountDownloadReq;
import com.platon.browser.req.account.AddressDetailReq;
import com.platon.browser.req.block.BlockDownloadReq;
import com.platon.browser.service.AccountService;
import com.platon.browser.service.BlockService;
import com.platon.browser.service.ExportService;
import com.platon.browser.util.EnergonUtil;
import com.platon.browser.util.I18nEnum;
import com.platon.browser.util.I18nUtil;
import com.univocity.parsers.csv.CsvWriter;
import com.univocity.parsers.csv.CsvWriterSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.utils.Convert;

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ExportServiceImpl implements ExportService {
    private final Logger logger = LoggerFactory.getLogger(ExportServiceImpl.class);

    @Autowired
    private AccountService accountService;
    @Autowired
    private BlockService blockService;
    @Autowired
    private I18nUtil i18n;

    @Override
    public AccountDownload exportAccountCsv(AccountDownloadReq req) {
        SimpleDateFormat ymdhms = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        logger.info("导出数据起始日期：{},结束日期：{}",ymdhms.format(req.getStartDate()),ymdhms.format(req.getEndDate()));

        AddressDetailReq accountDetailReq = new AddressDetailReq();
        // 一页取完所有数据
        accountDetailReq.setPageSize(Integer.MAX_VALUE);
        BeanUtils.copyProperties(req,accountDetailReq);

        AddressDetail addressDetail = accountService.getAddressDetail(accountDetailReq);

        List<Object[]> rows = new ArrayList<>();
        addressDetail.getTrades().forEach(transaction->{
            String transactionType;
            try {
                TransactionTypeEnum type = TransactionTypeEnum.getEnum(transaction.getTxType());
                transactionType = i18n.i(I18nEnum.valueOf(type.name()));
            }catch (IllegalArgumentException iae){
                transactionType = i18n.i(I18nEnum.UNKNOWN_TYPE);
            }

            String transactionStatus;
            try{
                TransactionStatusEnum status = TransactionStatusEnum.getEnum(transaction.getTxReceiptStatus());
                transactionStatus = i18n.i(I18nEnum.valueOf(status.name()));
            }catch (IllegalArgumentException iae){
                transactionStatus = i18n.i(I18nEnum.UNKNOWN_STATUS);
            }

            Object[] row = {
                    transaction.getTxHash(),
                    ymdhms.format(new Date(transaction.getBlockTime())),
                    transactionType,
                    transaction.getFrom(),
                    transaction.getTo(),
                    EnergonUtil.format(Convert.fromWei(transaction.getValue(), Convert.Unit.ETHER).setScale(18, RoundingMode.DOWN))+"Energon",
                    EnergonUtil.format(Convert.fromWei(transaction.getActualTxCost(), Convert.Unit.ETHER).setScale(18,RoundingMode.DOWN))+"Energon",
                    transactionStatus
            };
            rows.add(row);
        });

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Writer outputWriter = new OutputStreamWriter(baos);
        CsvWriter writer = new CsvWriter(outputWriter, new CsvWriterSettings());
        writer.writeHeaders(
                i18n.i(I18nEnum.DOWNLOAD_ACCOUNT_CSV_HASH),
                i18n.i(I18nEnum.DOWNLOAD_ACCOUNT_CSV_TIME),
                i18n.i(I18nEnum.DOWNLOAD_ACCOUNT_CSV_TYPE),
                i18n.i(I18nEnum.DOWNLOAD_ACCOUNT_CSV_FROM),
                i18n.i(I18nEnum.DOWNLOAD_ACCOUNT_CSV_TO),
                i18n.i(I18nEnum.DOWNLOAD_ACCOUNT_CSV_VALUE),
                i18n.i(I18nEnum.DOWNLOAD_ACCOUNT_CSV_FEE),
                i18n.i(I18nEnum.DOWNLOAD_ACCOUNT_CSV_STATUS)
        );
        writer.writeRowsAndClose(rows);
        AccountDownload accountDownload = new AccountDownload();
        accountDownload.setData(baos.toByteArray());
        SimpleDateFormat ymd = new SimpleDateFormat("yyyy-MM-dd");
        accountDownload.setFilename("transaction-"+req.getAddress()+"-"+ymd.format(req.getEndDate())+".csv");
        accountDownload.setLength(baos.size());
        return accountDownload;
    }

    @Override
    public BlockDownload exportNodeBlockCsv(BlockDownloadReq req) {
        SimpleDateFormat ymdhms = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        logger.info("导出数据起始日期：{},结束日期：{}",ymdhms.format(req.getStartDate()),ymdhms.format(req.getEndDate()));
        // 限制最多导出6万条记录
        PageHelper.startPage(1,60000);
        List<Block> blockList = blockService.getList(req);

        List<Object[]> rows = new ArrayList<>();
        blockList.forEach(block->{
            Object[] row = {
                    block.getNumber(),
                    ymdhms.format(block.getTimestamp()),
                    block.getTransactionNumber(),
                    EnergonUtil.format(Convert.fromWei(block.getBlockReward(), Convert.Unit.ETHER).setScale(18,RoundingMode.DOWN))
            };
            rows.add(row);
        });

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Writer outputWriter = new OutputStreamWriter(baos);
        CsvWriter writer = new CsvWriter(outputWriter, new CsvWriterSettings());
        writer.writeHeaders(
                i18n.i(I18nEnum.DOWNLOAD_BLOCK_CSV_NUMBER),
                i18n.i(I18nEnum.DOWNLOAD_BLOCK_CSV_TIMESTAMP),
                i18n.i(I18nEnum.DOWNLOAD_BLOCK_CSV_TRANSACTION_COUNT),
                i18n.i(I18nEnum.DOWNLOAD_BLOCK_CSV_REWARD)
        );
        writer.writeRowsAndClose(rows);
        BlockDownload blockDownload = new BlockDownload();
        blockDownload.setData(baos.toByteArray());
        SimpleDateFormat ymd = new SimpleDateFormat("yyyy-MM-dd");
        blockDownload.setFilename("block-"+req.getAddress()+"-"+ymd.format(req.getEndDate())+".csv");
        blockDownload.setLength(baos.size());
        return blockDownload;
    }
}
package com.platon.browser.service.impl;

import com.platon.browser.dto.account.AccountDowload;
import com.platon.browser.dto.transaction.AccTransactionItem;
import com.platon.browser.enums.TransactionStatusEnum;
import com.platon.browser.enums.TransactionTypeEnum;
import com.platon.browser.req.account.AccountDetailReq;
import com.platon.browser.req.account.AccountDownloadReq;
import com.platon.browser.service.AccountService;
import com.platon.browser.service.ExportService;
import com.platon.browser.util.I18nEnum;
import com.platon.browser.util.I18nUtil;
import com.univocity.parsers.csv.CsvWriter;
import com.univocity.parsers.csv.CsvWriterSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.math.BigDecimal;
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
    private I18nUtil i18n;

    @Override
    public AccountDowload exportAccountCsv(AccountDownloadReq req) {
        SimpleDateFormat ymdhms = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        logger.info("导出数据起始日期：{},结束日期：{}",ymdhms.format(req.getStartDate()),ymdhms.format(req.getEndDate()));

        AccountDetailReq accountDetailReq = new AccountDetailReq();
        // 一页取完所有数据
        accountDetailReq.setPageSize(Integer.MAX_VALUE);
        BeanUtils.copyProperties(req,accountDetailReq);

        List<AccTransactionItem> transactionList = accountService.getTransactionList(accountDetailReq);

        List<Object[]> rows = new ArrayList<>();
        transactionList.forEach(transaction->{
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

            BigDecimal txCost = new BigDecimal(transaction.getActualTxCost());
            txCost = txCost.divide(new BigDecimal("1000000000000000000"));
            Object[] row = {
                    transaction.getTxHash(),
                    ymdhms.format(new Date(transaction.getBlockTime())),
                    transactionType,
                    transaction.getFrom(),
                    transaction.getTo(),
                    transaction.getValue()+"Energon",
                    txCost.toString()+"Energon",
                    transactionStatus
            };
            rows.add(row);
        });

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Writer outputWriter = new OutputStreamWriter(baos);
        CsvWriter writer = new CsvWriter(outputWriter, new CsvWriterSettings());
        writer.writeHeaders(
                i18n.i(I18nEnum.DOWNLOAD_CSV_HASH),
                i18n.i(I18nEnum.DOWNLOAD_CSV_TIME),
                i18n.i(I18nEnum.DOWNLOAD_CSV_TYPE),
                i18n.i(I18nEnum.DOWNLOAD_CSV_FROM),
                i18n.i(I18nEnum.DOWNLOAD_CSV_TO),
                i18n.i(I18nEnum.DOWNLOAD_CSV_VALUE),
                i18n.i(I18nEnum.DOWNLOAD_CSV_FEE),
                i18n.i(I18nEnum.DOWNLOAD_CSV_STATUS)
        );
        writer.writeRowsAndClose(rows);
        AccountDowload accountDowload = new AccountDowload();
        accountDowload.setData(baos.toByteArray());
        SimpleDateFormat ymd = new SimpleDateFormat("yyyy-MM-dd");
        accountDowload.setFilename("transaction-"+req.getAddress()+"-"+ymd.format(req.getEndDate())+".csv");
        accountDowload.setLength(baos.size());
        return accountDowload;
    }
}
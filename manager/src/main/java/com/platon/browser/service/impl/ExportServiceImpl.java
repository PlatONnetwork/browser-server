package com.platon.browser.service.impl;

import com.platon.browser.dto.account.AccountDowload;
import com.platon.browser.dto.account.ContractDowload;
import com.platon.browser.dto.transaction.AccTransactionItem;
import com.platon.browser.dto.transaction.ConTransactionItem;
import com.platon.browser.enums.TransactionTypeEnum;
import com.platon.browser.req.account.AccountDetailReq;
import com.platon.browser.req.account.AccountDownloadReq;
import com.platon.browser.req.account.ContractDetailReq;
import com.platon.browser.req.account.ContractDownloadReq;
import com.platon.browser.service.AccountService;
import com.platon.browser.service.ContractService;
import com.platon.browser.service.ExportService;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Service
public class ExportServiceImpl implements ExportService {
    private final Logger logger = LoggerFactory.getLogger(ExportServiceImpl.class);

    @Autowired
    private AccountService accountService;

    @Autowired
    private ContractService contractService;

    @Override
    public AccountDowload exportAccountCsv(AccountDownloadReq req) {

        AccountDetailReq accountDetailReq = new AccountDetailReq();
        accountDetailReq.setPageSize(Integer.MAX_VALUE);
        BeanUtils.copyProperties(req,accountDetailReq);

        List<AccTransactionItem> transactionList = accountService.getTransactionList(accountDetailReq);

        List<Object[]> rows = new ArrayList<>();
        transactionList.forEach(transaction->{
            String transactionType;
            try {
                TransactionTypeEnum type = TransactionTypeEnum.getEnum(transaction.getTxType());
                transactionType = type.desc;
            }catch (IllegalArgumentException iae){
                transactionType = "未知类型";
            }

            Object[] row = {
                    transaction.getTxHash(),
                    transaction.getBlockTime(),
                    transactionType,
                    transaction.getFrom(),
                    transaction.getTo(),
                    transaction.getValue(),
                    transaction.getActualTxCost()
            };
            rows.add(row);
        });

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Writer outputWriter = new OutputStreamWriter(baos);
        CsvWriter writer = new CsvWriter(outputWriter, new CsvWriterSettings());
        writer.writeHeaders("交易哈希值", "确认时间", "类型", "发送方", "接收方","数额","交易费用");
        writer.writeRowsAndClose(rows);
        AccountDowload accountDowload = new AccountDowload();
        accountDowload.setData(baos.toByteArray());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        accountDowload.setFilename("transaction-"+req.getAddress()+"-"+sdf.format(req.getDate())+".csv");
        accountDowload.setLength(baos.size());
        return accountDowload;
    }

    @Override
    public ContractDowload exportContractCsv(ContractDownloadReq req) {
        ContractDetailReq contractDetailReq = new ContractDetailReq();
        contractDetailReq.setPageSize(Integer.MAX_VALUE);
        BeanUtils.copyProperties(req,contractDetailReq);

        List<ConTransactionItem> conTransactionList = contractService.getContractList(contractDetailReq);

        List<Object[]> rows = new ArrayList<>();
        conTransactionList.forEach(transaction->{
            String transactionType;
            try {
                TransactionTypeEnum type = TransactionTypeEnum.getEnum(transaction.getTxType());
                transactionType = type.desc;
            }catch (IllegalArgumentException iae){
                transactionType = "未知类型";
            }

            Object[] row = {
                    transaction.getTxHash(),
                    transaction.getBlockTime(),
                    transactionType,
                    transaction.getFrom(),
                    transaction.getTo(),
                    transaction.getValue(),
                    transaction.getActualTxCost()
            };
            rows.add(row);
        });

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Writer outputWriter = new OutputStreamWriter(baos);
        CsvWriter writer = new CsvWriter(outputWriter, new CsvWriterSettings());
        writer.writeHeaders("交易哈希值", "确认时间", "类型", "发送方", "接收方","数额","交易费用");
        writer.writeRowsAndClose(rows);
        ContractDowload contractDowload = new ContractDowload();
        contractDowload.setData(baos.toByteArray());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        contractDowload.setFilename("contract-"+req.getAddress()+"-"+sdf.format(req.getDate())+".csv");
        contractDowload.setLength(baos.size());
        return contractDowload;
    }
}

package com.platon.browser.service;

import com.platon.protocol.Web3j;
import com.platon.protocol.core.DefaultBlockParameter;
import com.platon.protocol.core.methods.response.PlatonGetBalance;
import com.platon.browser.callback.TransactionHandler;
import com.platon.browser.client.PlatOnClient;
import com.platon.browser.client.SpecialApi;
import com.platon.browser.client.Web3jWrapper;
import com.platon.browser.config.BlockChainConfig;
import com.platon.browser.service.elasticsearch.bean.ESResult;
import com.platon.browser.service.elasticsearch.EsTransactionRepository;
import com.platon.browser.elasticsearch.dto.Transaction;
import com.platon.browser.service.elasticsearch.query.ESQueryBuilderConstructor;
import com.univocity.parsers.csv.CsvWriter;
import com.univocity.parsers.csv.CsvWriterSettings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;
import java.io.*;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
public abstract class ServiceBase {
    @Autowired
    protected EsTransactionRepository ESTransactionRepository;
    @Autowired
    private PlatOnClient platonClient;
    @Autowired
    protected SpecialApi specialApi;
    @Autowired
    protected BlockChainConfig chainConfig;
    @Value("${paging.pageSize}")
    protected int transactionPageSize;

    protected abstract String getFileUrl();

    @PostConstruct
    private void init() {
        File destDir = new File(getFileUrl());
        if (destDir.exists())
            destDir.delete();
        if (!destDir.exists())
            destDir.mkdirs();
    }

    protected void buildFile(String fileName, List<Object[]> rows, String[] headers) {
        try {
            File file = new File(getFileUrl());
            if (!file.exists()) {
                file.mkdir();
            }
            /** 初始化输出流对象 */
            FileOutputStream fis = new FileOutputStream(getFileUrl() + fileName);
            /** 设置返回的头，防止csv乱码 */
            fis.write(new byte[] { (byte) 0xEF, (byte) 0xBB, (byte) 0xBF });
            OutputStreamWriter outputWriter = new OutputStreamWriter(fis, StandardCharsets.UTF_8);
            /** 厨师书writer对象 */
            CsvWriter writer = new CsvWriter(outputWriter, new CsvWriterSettings());
            if (headers != null) {
                writer.writeHeaders(headers);
            }
            writer.writeRowsAndClose(rows);
        } catch (IOException e) {
            log.error("数据输出错误:", e);
            return;
        }
        log.info("导出报表成功，路径：{}", getFileUrl() + fileName);
    }

    /**
     * 遍历交易
     * @param constructor
     * @param handler
     */
    protected void traverseTx(ESQueryBuilderConstructor constructor,TransactionHandler handler){
        // 分页查询区块数据
        ESResult<Transaction> esResult = null;
        for (int pageNo = 1; pageNo <= Integer.MAX_VALUE; pageNo++) {
            try {
                esResult = ESTransactionRepository.search(constructor, Transaction.class, pageNo, transactionPageSize);
            } catch (Exception e) {
                if (e.getMessage().contains("all shards failed")) {
                    break;
                } else {
                    log.error("【syncBlock()】查询ES出错:", e);
                }
            }
            if (esResult == null || esResult.getRsData() == null || esResult.getTotal() == 0
                    || esResult.getRsData().isEmpty()) {
                // 如果查询结果为空则结束
            	log.error("【esResult()】查询数据为空:{}", esResult.getTotal());
                break;
            }
            List<Transaction> txList = esResult.getRsData();
            try {
                txList.forEach(handler::handle);
                log.info("【exportTxh()】第{}页,{}条记录", pageNo, txList.size());
            } catch (Exception e) {
                log.error("【exportTxh()】导出出错:", e);
                throw e;
            }
        }
    }

    protected Set<String> readLines(String filepath){
        Set<String> lines = new HashSet<>();
        try {
            File file = new File(filepath);
            InputStream in = new FileInputStream(file);
            BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
            while (reader.ready()) {
                String line = reader.readLine();
                lines.add(line.trim().toLowerCase());
            }
            reader.close();
        } catch (Exception e) {
            log.error("read error", e);
        }
        return lines;
    }
    
    protected Set<String> readLinesOne(String filepath){
        Set<String> lines = new HashSet<>();
        try {
            File file = new File(filepath);
            InputStream in = new FileInputStream(file);
            BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
            int i = 0;
            while (reader.ready()) {
                String line = reader.readLine();
                if(i == 0) {
                    i++;
                    continue;
                }
                lines.add(line.split(",")[0].trim().toLowerCase());
            }
            reader.close();
        } catch (Exception e) {
            log.error("read error", e);
        }
        return lines;
    }

    /**
     * 取地址余额
     * @param address
     * @return
     */
    protected BigInteger getBalance(String address,DefaultBlockParameter defaultBlockParameter){
        BigInteger balance = BigInteger.ZERO;
        try {
            PlatonGetBalance platonGetBalance = platonClient.getWeb3jWrapper().getWeb3j()
                    .platonGetBalance(address, defaultBlockParameter).send();
            balance = platonGetBalance.getBalance();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return balance;
    }

    protected Web3j getClient(){
        return platonClient.getWeb3jWrapper().getWeb3j();
    }
    
    protected Web3jWrapper getClientWrapper(){
        return platonClient.getWeb3jWrapper();
    }
}

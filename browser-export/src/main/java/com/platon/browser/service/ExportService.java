package com.platon.browser.service;

import com.platon.browser.dao.mapper.AddressMapper;
import com.platon.browser.dao.mapper.NodeMapper;
import com.platon.browser.dao.mapper.RpPlanMapper;
import com.platon.browser.dto.elasticsearch.ESResult;
import com.platon.browser.elasticsearch.NodeOptESRepository;
import com.platon.browser.elasticsearch.TransactionESRepository;
import com.platon.browser.elasticsearch.dto.Transaction;
import com.platon.browser.elasticsearch.service.impl.ESQueryBuilderConstructor;
import com.univocity.parsers.csv.CsvWriter;
import com.univocity.parsers.csv.CsvWriterSettings;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class ExportService {
    @Getter
    @Setter
    private static volatile boolean txHashExportDone =false;
    @Getter
    @Setter
    private static volatile boolean addressExportDone =false;

    @Autowired
    private TransactionESRepository transactionESRepository;
    @Autowired
    private NodeOptESRepository nodeOptESRepository;
    @Autowired
    private AddressMapper addressMapper;
    @Autowired
    private NodeMapper nodeMapper;
    @Autowired
    private RpPlanMapper rpPlanMapper;


    @Value("${paging.transaction.page-size}")
    private int transactionPageSize;
    @Value("${paging.transaction.page-count}")
    private int transactionPageCount;

    /**
     * 导出交易表交易hash
     */
    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE)
    public void exportTxHash(){
        String fileName = "txhash.csv";
        List<Object[]> csvRows = new ArrayList<>();
        ESQueryBuilderConstructor constructor = new ESQueryBuilderConstructor();
        constructor.setDesc("seq");
        // 分页查询区块数据
        ESResult<Transaction> esResult=null;
        long totalCount = 0L;
        for (int pageNo = 0; pageNo*transactionPageSize <= totalCount; pageNo++) {
            try {
                esResult = transactionESRepository.search(constructor, Transaction.class, pageNo, transactionPageSize);
            } catch (Exception e) {
                if(e.getMessage().contains("all shards failed")) {
                    break;
                }else {
                    log.error("【syncBlock()】查询ES出错:",e);
                }
            }
            if(esResult==null||esResult.getRsData()==null||esResult.getTotal()==0){
                // 如果查询结果为空则结束
                break;
            }
            List<Transaction> txList = esResult.getRsData();
            try{
                txList.forEach(tx->csvRows.add(new Object[]{tx.getHash()}));
                totalCount+=txList.size();
                log.info("【exportTxHash()】第{}页,{}条记录",pageNo,txList.size());
            }catch (Exception e){
                log.error("【exportTxHash()】导出出错:",e);
                throw e;
            }
        }
        buildFile(fileName,csvRows,null);
        txHashExportDone=true;
    }

    /**
     * 导出地址表地址
     */
    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE)
    public void exportAddress(){
        long totalCount = 0L;
//        for (int pageNo = 0; pageNo*transactionPageSize <= totalCount; pageNo++) {
//
//            if(esResult==null||esResult.getRsData()==null||esResult.getTotal()==0){
//                // 如果查询结果为空则结束
//                break;
//            }
//            List<Transaction> txList = esResult.getRsData();
//            try{
//                txList.forEach(tx->csvRows.add(new Object[]{tx.getHash()}));
//                log.info("【exportTxHash()】第{}页,{}条记录",pageNo,txList.size());
//            }catch (Exception e){
//                log.error("【exportTxHash()】导出出错:",e);
//                throw e;
//            }
//        }
//        buildFile(fileName,csvRows,null);
    }

    /**
     * 导出rpplan表地址
     */
    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE)
    public void exportRpPlanAddress(){

    }

    /**
     * 导出节点表地址
     */
    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE)
    public void exportNodeId(){

    }

    /**
     * 导出委托表地址
     */
    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE)
    public void exportDelegationAddress(){

    }


    /**
     * 导出委托表节点ID
     */
    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE)
    public void exportDelegationNodeId(){

    }

    @Value("${fileUrl}")
    private int fileUrl;
    public void buildFile(String fileName, List<Object[]> rows, String[] headers) {
        try {
            /** 初始化输出流对象 */
            FileOutputStream fis=new FileOutputStream(fileUrl + fileName);
            /** 设置返回的头，防止csv乱码 */
            fis.write(new byte[] { (byte) 0xEF, (byte) 0xBB, (byte) 0xBF });
            OutputStreamWriter outputWriter = new OutputStreamWriter(fis, StandardCharsets.UTF_8);
            /** 厨师书writer对象 */
            CsvWriter writer = new CsvWriter(outputWriter, new CsvWriterSettings());
            writer.writeHeaders(headers);
            writer.writeRowsAndClose(rows);
        } catch (IOException e) {
            log.error("数据输出错误:", e);
            return;
        }
        log.info("导出报表成功，路径：{}", fileUrl + fileName);
    }
}

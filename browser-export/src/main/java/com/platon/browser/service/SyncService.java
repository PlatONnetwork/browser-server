package com.platon.browser.service;

import com.platon.browser.dto.elasticsearch.ESResult;
import com.platon.browser.elasticsearch.BlockESRepository;
import com.platon.browser.elasticsearch.TransactionESRepository;
import com.platon.browser.elasticsearch.dto.Block;
import com.platon.browser.elasticsearch.dto.Transaction;
import com.platon.browser.elasticsearch.service.impl.ESQueryBuilderConstructor;
import com.platon.browser.service.redis.RedisBlockService;
import com.platon.browser.service.redis.RedisTransactionService;
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
import java.util.HashSet;
import java.util.List;

@Slf4j
@Service
public class SyncService {
    @Getter
    @Setter
    private static volatile boolean blockSyncDone =false;
    @Getter
    @Setter
    private static volatile boolean transactionSyncDone =false;

    @Autowired
    private BlockESRepository blockESRepository;

    @Autowired
    private TransactionESRepository transactionESRepository;

    @Autowired
    private RedisBlockService redisBlockService;
    @Autowired
    private RedisTransactionService redisTransactionService;

    @Value("${paging.block.page-size}")
    private int blockPageSize;
    @Value("${paging.block.page-count}")
    private int blockPageCount;
    @Value("${paging.transaction.page-size}")
    private int transactionPageSize;
    @Value("${paging.transaction.page-count}")
    private int transactionPageCount;
    @Value("${fileUrl}")
    private int fileUrl;

    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE)
    public void syncBlock(){
        ESQueryBuilderConstructor blockConstructor = new ESQueryBuilderConstructor();
        blockConstructor.setDesc("num");
        // 分页查询区块数据
        ESResult<Block> esResult=null;
        for (int pageNo = 0; pageNo <= blockPageCount; pageNo++) {
            try {
                esResult = blockESRepository.search(blockConstructor, Block.class, pageNo, blockPageSize);
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
            List<Block> blocks = esResult.getRsData();
            try{
                redisBlockService.save(new HashSet<>(blocks),false);
                log.info("【syncBlock()】第{}页,{}条记录",pageNo,blocks.size());
            }catch (Exception e){
                log.error("【syncBlock()】同步区块到Redis出错:",e);
                throw e;
            }
            // 所有数据不够一页大小，退出
            if(blocks.size()<blockPageSize) break;
        }
        blockSyncDone=true;
    }

    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE)
    public void syncTransaction(){
        ESQueryBuilderConstructor transactionConstructor = new ESQueryBuilderConstructor();
        transactionConstructor.setDesc("seq");
        // 分页查询区块数据
        ESResult<Transaction> esResult=null;
        for (int pageNo = 0; pageNo <= transactionPageCount; pageNo++) {
            try {
                esResult = transactionESRepository.search(transactionConstructor,Transaction.class,pageNo,transactionPageSize);
            } catch (Exception e) {
                if(e.getMessage().contains("all shards failed")){
                    break;
                }else{
                    log.error("【syncTransaction()】查询ES出错:",e);
                }
            }
            if(esResult==null||esResult.getRsData()==null||esResult.getTotal()==0){
                // 如果查询结果为空则结束
                break;
            }
            List<Transaction> transactions = esResult.getRsData();
            try{
                redisTransactionService.save(new HashSet<>(transactions),false);
                log.info("【syncTransaction()】第{}页,{}条记录",pageNo,transactions.size());
            }catch (Exception e){
                log.error("【syncTransaction()】同步交易到Redis出错:",e);
                throw e;
            }
            // 所有数据不够一页大小，退出
            if(transactions.size()<transactionPageSize) break;
        }
        transactionSyncDone=true;
    }
    
    public void buildFile(String fileName, List<Object[]> rows, String[] headers) {
		try {
			/** 初始化输出流对象 */
			FileOutputStream fis=new FileOutputStream(fileUrl + fileName);
			/** 设置返回的头，防止csv乱码 */
			fis.write(new byte[] { (byte) 0xEF, (byte) 0xBB, (byte) 0xBF });
			OutputStreamWriter  outputWriter = new OutputStreamWriter(fis, StandardCharsets.UTF_8);
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

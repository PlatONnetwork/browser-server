//package com.platon.browser.persistence.service;
//
//import com.platon.browser.dao.entity.NodeOpt;
//import com.platon.browser.dao.entity.TransactionWithBLOBs;
//import com.platon.browser.dto.CustomBlock;
//import com.platon.browser.elasticsearch.BlockESRepository;
//import com.platon.browser.elasticsearch.NodeOptESRepository;
//import com.platon.browser.elasticsearch.TransactionESRepository;
//import lombok.extern.slf4j.Slf4j;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.concurrent.TimeUnit;
//
///**
// * @Auther: Chendongming
// * @Date: 2019/10/25 15:12
// * @Description: ES服务
// */
//@Slf4j
////@Component
//public class EsService {
//    private static Logger logger = LoggerFactory.getLogger(EsService.class);
//
//    @Autowired
//    private BlockESRepository blockESRepository;
//    @Autowired
//    private TransactionESRepository transactionESRepository;
//    @Autowired
//    private NodeOptESRepository nodeOptESRepository;
//
//    public void batchInsertOrUpdate (List<CustomBlock> blocks, List<TransactionWithBLOBs> transactions, List<NodeOpt> nodeOpts){
//
//        if(!blocks.isEmpty()){
//            Map<String,CustomBlock> blockMap = new HashMap<>();
//            blocks.forEach(b->blockMap.put(String.valueOf(b.getNumber()),b));
//
//            while (true)try {
//                blockESRepository.bulkAddOrUpdate(blockMap);
//                break;
//            } catch (Exception e) {
//                log.error("ES批量入库区块出错,将重试!",e);
//                try {
//                    TimeUnit.SECONDS.sleep(1L);
//                } catch (Exception ex) {
//                    log.error("{}",ex);
//                }
//            }
//        }
//
//    }
//}

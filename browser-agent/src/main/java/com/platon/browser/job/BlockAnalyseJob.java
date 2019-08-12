//package com.platon.browser.job;
//
//import com.github.pagehelper.PageHelper;
//import com.platon.browser.client.PlatonClient;
//import com.platon.browser.dao.entity.Block;
//import com.platon.browser.dao.entity.BlockExample;
//import com.platon.browser.dao.mapper.BlockMapper;
//import com.platon.browser.thread.AnalyseThread;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//import org.web3j.protocol.core.DefaultBlockParameter;
//import org.web3j.protocol.core.methods.response.PlatonBlock;
//
//import java.io.IOException;
//import java.math.BigInteger;
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * User: dongqile
// * Date: 2018/10/24
// * Time: 17:28
// */
//@Component
//public class BlockAnalyseJob {
//    private static Logger logger = LoggerFactory.getLogger(BlockAnalyseJob.class);
//    @Autowired
//    private BlockMapper blockMapper;
//    @Value("${platon.block.batch.num}")
//    private int batchNum;
//    @Autowired
//    private PlatonClient platon;
//    @Value("${platon.chain.active}")
//    private String chainId;
//    // 起始区块号
//    private long beginNumber=1;
//    @Autowired
//    private AnalyseThread analyseThread;
//
//    // 起始区块号是否初始化了
//    private boolean isBeginNumberInit=false;
//
//    /**
//     * 分析区块及其内部的交易数据
//     */
//    @Scheduled(cron="0/1 * * * * ?")
//    protected void analyseBlock () {
//
//        if(!isBeginNumberInit){
//            BlockExample condition = new BlockExample();
//            condition.setOrderByClause("number desc");
//            PageHelper.startPage(1, 1);
//            List <Block> blocks = blockMapper.selectByExample(condition);
//            // 1、首先从数据库查询当前链的最高块号，作为采集起始块号
//            // 2、如果查询不到则从0开始
//            if (blocks.size() == 0) {
//                beginNumber = 1L;
//            } else {
//                beginNumber = blocks.get(0).getNumber()+1;
//            }
//            isBeginNumberInit=true;
//        }
//
//
//        logger.debug("*** In the BlockAnalyseJob *** ");
//        try {
//            // 需要并发处理的区块数据
//            List<PlatonBlock> concurrentBlocks = new ArrayList<>();
//            // 结束区块号
//            BigInteger endNumber = platon.getWeb3j().platonBlockNumber().send().getBlockNumber();
//            if(beginNumber>endNumber.intValue()) return;
//            if((endNumber.intValue()-beginNumber)<batchNum){
//                while (beginNumber<=endNumber.longValue()){
//                    PlatonBlock ethBlock = platon.getWeb3j().platonGetBlockByNumber(DefaultBlockParameter.valueOf(BigInteger.valueOf(beginNumber)),true).send();
//                    concurrentBlocks.add(ethBlock);
//                    beginNumber++;
//                }
//                analyseThread.analyse(concurrentBlocks);
//                concurrentBlocks.clear();
//            }else{
//                while (beginNumber<=endNumber.longValue()){
//                    PlatonBlock ethBlock = platon.getWeb3j().platonGetBlockByNumber(DefaultBlockParameter.valueOf(BigInteger.valueOf(beginNumber)),true).send();
//                    concurrentBlocks.add(ethBlock);
//                    long range = endNumber.longValue()-beginNumber+1;
//                    if(
//                        // 如果并发区块数量达到线程处理阈值，开启线程处理
//                        (concurrentBlocks.size()>=batchNum)||
//                        // 追上之后，有多少处理多少
//                        (concurrentBlocks.size()>=range)
//                    ){
//                        analyseThread.analyse(concurrentBlocks);
//                        concurrentBlocks.clear();
//                    }
//                    beginNumber++;
//                }
//            }
//        } catch (IOException e) {
//            logger.error("BlockAnalyseJob Exception:{}", e.getMessage());
//        }
//        logger.debug("*** End the BlockAnalyseJob *** ");
//    }
//}

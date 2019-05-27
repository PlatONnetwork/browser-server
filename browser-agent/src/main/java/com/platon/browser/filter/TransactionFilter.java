package com.platon.browser.filter;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.platon.browser.bean.TransactionBean;
import com.platon.browser.client.PlatonClient;
import com.platon.browser.dao.entity.NodeRanking;
import com.platon.browser.dao.entity.NodeRankingExample;
import com.platon.browser.dao.mapper.CustomNodeRankingMapper;
import com.platon.browser.dao.mapper.NodeRankingMapper;
import com.platon.browser.dto.ticket.TxInfo;
import com.platon.browser.enums.TransactionTypeEnum;
import com.platon.browser.thread.AnalyseThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetCode;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.platon.browser.utils.CacheTool.RECEIVER_TO_TYPE;


/**
 * User: dongqile
 * Date: 2019/1/7
 * Time: 14:28
 */
@Component
public class TransactionFilter {
    private static Logger log = LoggerFactory.getLogger(TransactionFilter.class);
    @Value("${platon.redis.key.transaction}")
    private String transactionCacheKeyTemplate;
    @Autowired
    private PlatonClient platon;
    @Value("${platon.chain.active}")
    private String chainId;

    @Autowired
    private NodeRankingMapper nodeRankingMapper;
    @Autowired
    private CustomNodeRankingMapper customNodeRankingMapper;


    public List <TransactionBean> analyse ( AnalyseThread.AnalyseParam param, long time ) {
        long beginTime = System.currentTimeMillis();
        Map <String, Object> transactionReceiptMap = param.transactionReceiptMap;
        List <TransactionBean> transactions = new ArrayList <>();
        param.transactions.forEach(initData -> {
            if (null != transactionReceiptMap.get(initData.getHash())) {
                TransactionBean bean = new TransactionBean();
                TransactionReceipt receipt = (TransactionReceipt) transactionReceiptMap.get(initData.getHash());
                // Initialize the entity with the raw transaction and receipt
                bean.init(initData, receipt);
                // Convert timestamp into milliseconds

                if (String.valueOf(time).length() == 10) {
                    bean.setTimestamp(new Date(time * 1000L));
                } else {
                    bean.setTimestamp(new Date(time));
                }
                // Setup the chain id
                bean.setChainId(chainId);
                // Setup the receiver type
                //judge `to` address is accountAddress or contractAddress
                if (null != RECEIVER_TO_TYPE.get(initData.getTo())) {
                    bean.setReceiveType(RECEIVER_TO_TYPE.get(initData.getTo()));
                } else {
                    try {
                        EthGetCode ethGetCode = platon.getWeb3j(chainId).ethGetCode(initData.getTo(), DefaultBlockParameterName.LATEST).send();
                        if ("0x".equals(ethGetCode.getCode())) {
                            bean.setReceiveType("account");
                        } else {
                            bean.setReceiveType("contract");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                // Cache the receiver type for later use
                RECEIVER_TO_TYPE.put(initData.getTo(), bean.getReceiveType());
                transactions.add(bean);


                if(TransactionTypeEnum.TRANSACTION_VOTE_TICKET.code.equals(bean.getTxType())){
                    // 投票交易，则从节点中查询质押金并设置到交易信息中
                    TxInfo info = JSON.parseObject(bean.getTxInfo(),TxInfo.class);
                    TxInfo.Parameter tp = info.getParameters();
                    if(tp!=null){
                        // 查询对应节点的质押金，放到txinfo
                        NodeRanking nodeRanking = customNodeRankingMapper.selectByBlockNumber(chainId,bean.getBlockNumber());

                        if(nodeRanking!=null){
                            tp.setDeposit(nodeRanking.getDeposit());
                        }else {
                            tp.setDeposit("0");
                        }
                        bean.setTxInfo(JSON.toJSONString(info));
                    }
                }
            }

        });
        log.debug("Time Consuming: {}ms", System.currentTimeMillis() - beginTime);
        return transactions;
    }

}

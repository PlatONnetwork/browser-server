package com.platon.browser.service;

import com.platon.browser.elasticsearch.dto.Block;
import com.platon.browser.elasticsearch.dto.NodeOpt;
import com.platon.browser.elasticsearch.dto.Transaction;
import com.platon.browser.utils.HexTool;
import lombok.Data;
import org.apache.commons.codec.digest.DigestUtils;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Data
public class BlockResult {
    private Block block;
    private List<Transaction> transactionList=new ArrayList<>();
    private List<NodeOpt> nodeOptList=new ArrayList<>();
    public void buildAssociation(BigInteger blockNumber, String nodeId){
        block.setNum(blockNumber.longValue());
        block.setNodeId(nodeId);
        String blockHash = HexTool.prefix(DigestUtils.sha256Hex(block.toString()));
        block.setHash(blockHash);
        int i = 0;
        for (Transaction tx : transactionList) {
            tx.setBHash(blockHash);
            tx.setNum(blockNumber.longValue());
            String txHash = HexTool.prefix(DigestUtils.sha256Hex(tx.toString()));
            tx.setHash(txHash);
            String from = HexTool.prefix(DigestUtils.sha1Hex(tx.toString()));
            tx.setFrom(from);
            String to = HexTool.prefix(DigestUtils.sha1Hex(tx.toString()));
            tx.setTo(to);
            tx.setIndex(i);
            i++;
        }

        nodeOptList.forEach(nodeOpt -> {
            nodeOpt.setNodeId(nodeId);
            nodeOpt.setBNum(blockNumber.longValue());
        });
    }
}

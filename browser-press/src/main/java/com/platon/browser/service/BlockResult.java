package com.platon.browser.service;

import com.alaya.bech32.Bech32;
import com.alaya.parameters.NetworkParameters;
import com.platon.browser.elasticsearch.dto.Block;
import com.platon.browser.elasticsearch.dto.NodeOpt;
import com.platon.browser.elasticsearch.dto.Transaction;
import com.platon.browser.utils.HexUtil;
import lombok.Data;
import org.apache.commons.codec.digest.DigestUtils;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class BlockResult {


    static class AddressCount {
        private String address;
        private Integer count=0;
        public String get(String newAddress,int addressReusedTimes){
            if(count==0||count>=addressReusedTimes)  {
                address = newAddress;
                count=1;
                return address;
            }
            count++;
            return address;
        }
    }
    private static final AddressCount FROM_ADDRESS = new AddressCount();
    private static final AddressCount TO_ADDRESS = new AddressCount();

    private Block block;
    private List<Transaction> transactionList=new ArrayList<>();
    private List<NodeOpt> nodeOptList=new ArrayList<>();

    public void buildAssociation(BigInteger blockNumber, String nodeId,long nodeOptId,int addressReusedTimes){
        block.setNum(blockNumber.longValue());
        block.setNodeId(nodeId);
        String blockHash = HexUtil.prefix(DigestUtils.sha256Hex(block.toString()));
        block.setHash(blockHash);
        int i = 0;
        for (Transaction tx : transactionList) {
            tx.setBHash(blockHash);
            tx.setNum(blockNumber.longValue());
            String txHash = HexUtil.prefix(DigestUtils.sha256Hex(UUID.randomUUID().toString()));
            tx.setHash(txHash);
            String from = HexUtil.prefix(DigestUtils.sha1Hex(txHash));
            tx.setFrom(FROM_ADDRESS.get(Bech32.addressEncode(NetworkParameters.Hrp.ATX.getHrp(), from),addressReusedTimes));
            String to = HexUtil.prefix(DigestUtils.sha1Hex(from));
            tx.setTo(TO_ADDRESS.get(Bech32.addressEncode(NetworkParameters.Hrp.ATX.getHrp(), to),addressReusedTimes));
            tx.setIndex(i);
            long seq = tx.getNum()*100000+i;
            tx.setSeq(seq);
            i++;
        }

        for (NodeOpt nodeOpt : nodeOptList) {
            nodeOpt.setId(nodeOptId++);
            nodeOpt.setNodeId(nodeId);
            nodeOpt.setBNum(blockNumber.longValue());
        }
    }

}

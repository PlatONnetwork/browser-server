package com.platon.browser.dto.block;

import com.platon.browser.dao.entity.Block;
import com.platon.browser.util.EnergonUtil;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.web3j.utils.Convert;

import java.math.BigDecimal;

@Data
public class BlockPushItem {
    private Long height;
    private Long timestamp;
    private Long serverTime;
    private String node;
    private Integer transaction;
    private String blockReward;
    private String nodeName;
    private String nodeId;

    public void init(Block initData){
        BeanUtils.copyProperties(initData,this);
        this.setNode(initData.getMiner());
        this.setHeight(initData.getNumber());
        this.setTimestamp(initData.getTimestamp().getTime());
        this.setTransaction(initData.getTransactionNumber());
        BigDecimal v = Convert.fromWei(initData.getBlockReward(), Convert.Unit.ETHER);
        this.setBlockReward(EnergonUtil.format(v));
        this.setServerTime(System.currentTimeMillis());
        if(null != initData.getNodeName()) this.setNodeName(initData.getNodeName());
        else this.setNodeName("GenesisNode");
    }
}

package com.platon.browser.dto.block;

import com.platon.browser.dao.entity.Block;
import com.platon.browser.util.EnergonUtil;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.web3j.utils.Convert;

import java.math.BigDecimal;

@Data
public class BlockDetail {
    private long height;
    private long timestamp;
    private long transaction;
    private String hash;
    private String parentHash;
    private String miner;
    private int size;
    private String energonLimit;
    private String energonUsed;
    private String blockReward;
    private String extraData;
    private long timeDiff;
    private String nodeName;
    private String nodeId;
    // /区块内投票交易个数
    private Long blockVoteAmount;
    // //区块中包含的选票张数
    private Long blockVoteNumber;
    //区块内竞选交易个数
    private Long blockCampaignAmount;

    // 是否第一条
    private boolean first;
    // 是否最后一条
    private boolean last;

    public void init(Block initData){
        BeanUtils.copyProperties(initData,this);
        this.setHeight(initData.getNumber());
        this.setTimestamp(initData.getTimestamp().getTime());
        BigDecimal v = Convert.fromWei(initData.getBlockReward(), Convert.Unit.ETHER);
        this.setBlockReward(EnergonUtil.format(v));
    }
}

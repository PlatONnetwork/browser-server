package com.platon.browser.dto;

import com.platon.browser.dao.entity.Block;
import com.platon.browser.enums.TxTypeEnum;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.web3j.protocol.core.methods.response.PlatonBlock;
import org.web3j.protocol.core.methods.response.Transaction;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Auther: Chendongming
 * @Date: 2019/8/10 14:20
 * @Description:
 */
@Data
public class BlockInfo extends Block {

    public BlockInfo(PlatonBlock.Block initData){
        BeanUtils.copyProperties(initData,this);
        // 属性类型转换
        this.setNumber(initData.getNumber().longValue());
        this.setTimestamp(new Date(initData.getTimestamp().longValue()));
        this.setSize(initData.getSize().intValue());
        this.setGasLimit(initData.getGasLimit().toString());
        this.setGasUsed(initData.getGasUsed().toString());
        Date date = new Date();
        this.setCreateTime(date);
        this.setUpdateTime(date);
        // 交易总数
        this.setStatTxQty(initData.getTransactions().size());
        // 区块奖励
        //this.setBlockReward("");
        // 节点名称
        //this.setNodeName("");
        //this.setNodeId("");

        // 交易相关信息处理
        initData.getTransactions().forEach(txResult->{
            TransactionInfo ti = new TransactionInfo(txResult);
            this.getTransactions().add(ti);
        });

        class Stat {
            int transferQty=0;
            int stakingQty=0;
            int proposalQty=0;
            int delegateQty=0;
            BigInteger txFee = BigInteger.ZERO;
            BigInteger gasLimit = BigInteger.ZERO;
        }
        Stat stat = new Stat();
        this.getTransactions().forEach(ti->{
            switch (ti.getTypeEnum()){

            }
        });
    }

    private List<TransactionInfo> transactions = new ArrayList<>();
}

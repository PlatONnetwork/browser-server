package com.platon.browser.dto;

import com.platon.browser.dao.entity.Block;
import com.platon.browser.dao.entity.Transaction;
import com.platon.browser.utils.HexTool;
import com.platon.browser.utils.NodeTool;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.web3j.protocol.core.methods.response.PlatonBlock;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

import static java.util.Comparator.*;

/**
 * @Auther: Chendongming
 * @Date: 2019/8/10 14:20
 * @Description: 区块实体扩展类
 */
@Data
public class CustomBlock extends Block {

    public CustomBlock(){
        /* 初始化默认值 */
        // 区块内交易数（区块所含交易个数）
        this.setStatTxQty(0);
        // 区块内转账交易总数
        this.setStatTransferQty(0);
        // 区块内验证人交易总数
        this.setStatStakingQty(0);
        // 区块内治理交易总数
        this.setStatProposalQty(0);
        // 区块内委托交易总数
        this.setStatDelegateQty(0);
        // 区块中交易能量限制
        this.setStatTxGasLimit(BigInteger.ZERO.toString());
        // 区块中交易实际花费值(手续费)总和，单位：von
        this.setStatTxFee(BigInteger.ZERO.toString());
        // 区块奖励，单位：von
        this.setBlockReward(BigInteger.ZERO.toString());
        // 节点ID
        this.setNodeId("");
        // 节点名称
        this.setNodeName("Unknown");
    }

    /**
     * 使用原生交易信息初始化交易信息
     * @param block 原生区块
     */
    public void updateWithBlock(PlatonBlock.Block block) {
        BeanUtils.copyProperties(block,this);
        // 属性类型转换
        this.setNumber(block.getNumber().longValue());
        this.setTimestamp(new Date(block.getTimestamp().longValue()));
        this.setSize(block.getSize().intValue());
        this.setGasLimit(block.getGasLimit().toString());
        this.setGasUsed(block.getGasUsed().toString());
        Date date = new Date();
        this.setCreateTime(date);
        this.setUpdateTime(date);
        // 交易总数
        this.setStatTxQty(block.getTransactions().size());
        this.setGasLimit(block.getGasLimit().toString());

        String publicKey = NodeTool.calculateNodePublicKey(block);
        if(publicKey!=null) this.setNodeId(HexTool.prefix(publicKey));
        try{
            // 抽取交易信息
            block.getTransactions().forEach(tr -> {
                CustomTransaction transaction = new CustomTransaction();
                transaction.updateWithTransactionResult(tr);
                transaction.setTimestamp(this.getTimestamp());
                transaction.setCreateTime(date);
                transaction.setUpdateTime(date);
                transactionList.add(transaction);
            });
        }catch (Exception ex){
            ex.printStackTrace();
            throw ex;
        }

        class Stat {
            private int transferQty=0,stakingQty=0,proposalQty=0,delegateQty=0,txGasLimit=0;
            private BigDecimal txFee = BigDecimal.ZERO;
        }
        Stat stat = new Stat();
        this.setStatDelegateQty(stat.delegateQty);
        this.setStatProposalQty(stat.proposalQty);
        this.setStatStakingQty(stat.stakingQty);
        this.setStatTransferQty(stat.transferQty);
        this.setStatTxGasLimit(String.valueOf(stat.txGasLimit));
        this.setStatTxFee(stat.txFee.toString());

        // 确保区块内的交易顺序
        transactionList.sort(comparing(Transaction::getTransactionIndex));
    }

    private List<CustomTransaction> transactionList = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomBlock blockInfo = (CustomBlock) o;
        return Objects.equals(getNumber(), blockInfo.getNumber());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getNumber());
    }


    public BigInteger getBlockNumber(){
        return BigInteger.valueOf(this.getNumber());
    }

}

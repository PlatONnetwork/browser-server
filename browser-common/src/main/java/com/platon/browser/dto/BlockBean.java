package com.platon.browser.dto;

import com.platon.browser.dao.entity.Block;
import com.platon.browser.utils.NodeTools;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.web3j.protocol.core.methods.response.PlatonBlock;

import java.math.BigDecimal;
import java.util.*;

/**
 * @Auther: Chendongming
 * @Date: 2019/8/10 14:20
 * @Description:
 */
@Data
public class BlockBean extends Block {

    public BlockBean(){
        // 设置默认值
        this.setNodeId("");
        this.setBlockReward("0");
        this.setNodeName("Unknown");
    }

    /**
     * 使用原生交易信息初始化交易信息
     * @param initData
     */
    public void init(PlatonBlock.Block initData) throws Exception {
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
        this.setGasLimit(initData.getGasLimit().toString());

        String publicKey = NodeTools.calculateNodePublicKey(initData);
        if(publicKey!=null) this.setNodeId(publicKey.startsWith("0x")?publicKey:"0x"+publicKey);

        try{
            // 抽取交易信息
            initData.getTransactions().forEach(tr -> {
                TransactionBean transaction = new TransactionBean();
                transaction.init(tr);
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
            int transferQty=0,stakingQty=0,proposalQty=0,delegateQty=0,txGasLimit=0;
            BigDecimal txFee = BigDecimal.ZERO;
        }
        Stat stat = new Stat();
        this.setStatDelegateQty(stat.delegateQty);
        this.setStatProposalQty(stat.proposalQty);
        this.setStatStakingQty(stat.stakingQty);
        this.setStatTransferQty(stat.transferQty);
        this.setStatTxGasLimit(String.valueOf(stat.txGasLimit));
        this.setStatTxFee(stat.txFee.toString());

        // 确保区块内的交易顺序
        Collections.sort(transactionList,(c1,c2)->{
            if(c1.getTransactionIndex()>c2.getTransactionIndex())return 1;
            if(c1.getTransactionIndex()<c2.getTransactionIndex())return -1;
            return 0;
        });
    }



    private List<TransactionBean> transactionList = new ArrayList<>();


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BlockBean blockInfo = (BlockBean) o;
        return Objects.equals(getNumber(), blockInfo.getNumber());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getNumber());
    }
}

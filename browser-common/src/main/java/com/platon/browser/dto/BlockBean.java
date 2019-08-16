package com.platon.browser.dto;

import com.platon.browser.dao.entity.Block;
import com.platon.browser.utils.HexTool;
import com.platon.browser.utils.NodeTool;
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
        /** 初始化默认值 **/
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
        this.setStatTxGasLimit("0");
        // 区块中交易实际花费值(手续费)总和，单位：von
        this.setStatTxFee("0");
        // 区块奖励，单位：von
        this.setBlockReward("0");
        // 节点ID
        this.setNodeId("");
        // 节点名称
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

        String publicKey = NodeTool.calculateNodePublicKey(initData);
        if(publicKey!=null) this.setNodeId(HexTool.prefix(publicKey));

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

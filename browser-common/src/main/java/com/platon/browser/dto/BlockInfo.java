package com.platon.browser.dto;

import com.platon.browser.dao.entity.Block;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.web3j.protocol.core.methods.response.PlatonBlock;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @Auther: Chendongming
 * @Date: 2019/8/10 14:20
 * @Description:
 */
@Data
public class BlockInfo extends Block {

    /**
     * 使用原生交易信息初始化交易信息
     * @param initData
     */
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
        this.setGasLimit(initData.getGasLimit().toString());
        // TODO:区块奖励
        this.setBlockReward("0");
        // TODO:节点名称
        this.setNodeName("DD");
        // TODO:节点id
        this.setNodeId("0xflsjgsflsdf");

        try{
            // 抽取交易信息
            initData.getTransactions().forEach(transactionResult -> {
                TransactionInfo ti = new TransactionInfo(transactionResult);
                transactionList.add(ti);
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

        /*// 交易信息统计
        class Stat {
            int transferQty=0,stakingQty=0,proposalQty=0,delegateQty=0,txGasLimit=0;
            BigDecimal txFee = BigDecimal.ZERO;
        }
        Stat stat = new Stat();
        this.getTransactions().forEach(ti->{
            switch (ti.getTypeEnum()){
                case TRANSFER:
                    stat.transferQty++;
                    break;
                case CREATEPROPOSALPARAMETER:// 创建参数提案
                case CREATEPROPOSALTEXT:// 创建文本提案
                case CREATEPROPOSALUPGRADE:// 创建升级提案
                case DECLAREVERSION:// 版本声明
                case VOTINGPROPOSAL:// 提案投票
                    stat.proposalQty++;
                    break;
                case DELEGATE:// 发起委托
                case UNDELEGATE:// 撤销委托
                    stat.delegateQty++;
                    break;
                case INCREASESTAKING:// 增加自有质押
                case CREATEVALIDATOR:// 创建验证人
                case EXITVALIDATOR:// 退出验证人
                case REPORTVALIDATOR:// 举报验证人
                case EDITVALIDATOR:// 编辑验证人
                    stat.stakingQty++;
                    break;
            }
            // 累加交易手续费
            stat.txFee = stat.txFee.add(new BigDecimal(ti.getActualTxCost()));
            // 累加交易gasLimit
            stat.txGasLimit = stat.txGasLimit+Integer.valueOf(ti.getGasLimit());
        });
        this.setStatDelegateQty(stat.delegateQty);
        this.setStatProposalQty(stat.proposalQty);
        this.setStatStakingQty(stat.stakingQty);
        this.setStatTransferQty(stat.transferQty);
        this.setStatTxGasLimit(String.valueOf(stat.txGasLimit));
        this.setStatTxFee(stat.txFee.toString());

        */
    }

    private List<TransactionInfo> transactionList = new ArrayList<>();


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BlockInfo blockInfo = (BlockInfo) o;
        return Objects.equals(getNumber(), blockInfo.getNumber());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getNumber());
    }
}

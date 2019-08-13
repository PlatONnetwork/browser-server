package com.platon.browser.dto;

import com.alibaba.fastjson.JSON;
import com.platon.browser.dao.entity.TransactionWithBLOBs;
import com.platon.browser.dto.json.*;
import com.platon.browser.enums.ReceiveStatusEnum;
import com.platon.browser.enums.ReceiveTypeEnum;
import com.platon.browser.enums.TxTypeEnum;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.web3j.protocol.core.methods.response.PlatonBlock;
import org.web3j.protocol.core.methods.response.Transaction;

import java.math.BigDecimal;

/**
 * @Auther: Chendongming
 * @Date: 2019/8/10 13:52
 * @Description:
 */
@Data
public class TransactionInfo<T> extends TransactionWithBLOBs {

    private TxTypeEnum typeEnum;

    /**
     * 使用原生交易信息初始化交易信息
     * @param initData
     */
    public TransactionInfo(PlatonBlock.TransactionResult initData){
        try {
            Transaction transaction = (Transaction)initData;
            BeanUtils.copyProperties(transaction,this);

            // TODO: 需要从input数据中解析出来
            this.setTypeEnum(TxTypeEnum.CREATEVALIDATOR);



            this.setBlockNumber(transaction.getBlockNumber().longValue());
            this.setTransactionIndex(transaction.getTransactionIndex().intValue());
            // TODO: 在区块中设置
//            this.setTimestamp();

            this.setGasPrice(transaction.getGasPrice().toString());
            // TODO：从交易回执中获取
            this.setGasUsed("0");
            this.setGasLimit(transaction.getGas().toString());
            this.setValue(transaction.getValue().toString());
            this.setNonce(transaction.getNonce().toString());
            BigDecimal txCost = new BigDecimal(this.getGasUsed()).multiply(new BigDecimal(this.getGasPrice()));
            this.setActualTxCost(txCost.toString());
            this.setTxType(String.valueOf(typeEnum.code));

            // TODO: 接收者类型
            this.setReceiveType(ReceiveTypeEnum.ACCOUNT.name().toLowerCase());
            // TODO: 在区块中设置
//            this.setCreateTime();
//            this.setUpdateTime();

            // TODO: 从交易回执中获取
            this.setTxReceiptStatus(ReceiveStatusEnum.SUCCESS.code);
            // TODO:
            this.setTxInfo("");
            Long sequence = Long.valueOf(String.valueOf(this.getBlockNumber())+this.getTransactionIndex());
            this.setSequence(sequence);


        }catch (Exception e){
            e.printStackTrace();
        }

    }

    /**
     * 根据类型获取交易参数信息对象
     * @return
     */
    public T getTxJson(){

        switch (typeEnum){
            case CREATEVALIDATOR:
                // 质押交易,txType=1000
                return (T) JSON.parseObject(this.getTxInfo(), CreateValidatorDto.class);
            case EDITVALIDATOR:
                //修改质押信息,txType=1001
                return (T) JSON.parseObject(this.getTxInfo(), EditValidatorDto.class);
            case INCREASESTAKING:
                //增持质押(增加自有质押),txType=1002
                return (T) JSON.parseObject(this.getTxInfo(), IncreaseStakingDto.class);
            case EXITVALIDATOR:
                //撤销质押(退出验证人),tyType=1003
                return (T) JSON.parseObject(this.getTxInfo(), ExitValidatorDto.class);
            case DELEGATE:
                // 委托交易,tyType=1004
                return (T) JSON.parseObject(this.getTxInfo(), DelegateDto.class);
            case  UNDELEGATE:
                //减持/撤销委托(赎回委托),txType=1005
                return (T) JSON.parseObject(this.getTxInfo(),UnDelegateDto.class);
            case CREATEPROPOSALTEXT:
                //提交文本提案(创建提案),tyType=2000
                return (T) JSON.parseObject(this.getTxInfo(),CreateProposalTextDto.class);
            case CREATEPROPOSALUPGRADE:
                //提交升级提案(创建提案),txType=2001
                return (T) JSON.parseObject(this.getTxInfo(),CreateProposalUpgradeDto.class);
            case CREATEPROPOSALPARAMETER:
                //提交参数提案(创建提案),txType=2002
                return (T) JSON.parseObject(this.getTxInfo(),CreateProposalParamDto.class);
            case VOTINGPROPOSAL:
                //提案投票(提案投票),txType=2003
                return (T) JSON.parseObject(this.getTxInfo(),VotingProposalDto.class);
            case DECLAREVERSION:
                //版本声明,txType=2004
                return (T) JSON.parseObject(this.getTxInfo(),DeclareVersionDto.class);
            case REPORTVALIDATOR:
                //举报多签(举报验证人),txType=3000
                return (T) JSON.parseObject(this.getTxInfo(),ReportValidatorDto.class);
            case CREATERESTRICTING:
                //创建锁仓计划(创建锁仓),txType=4000
                return (T) JSON.parseObject(this.getTxInfo(),CreatereStrictingDto.class);
            default:
                return null;
        }
    }
}

package com.platon.browser.dto;

import com.alibaba.fastjson.JSON;
import com.platon.browser.dao.entity.TransactionWithBLOBs;
import com.platon.browser.dto.json.*;
import com.platon.browser.enums.InnerContractAddEnum;
import com.platon.browser.enums.TxTypeEnum;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.web3j.protocol.core.methods.response.PlatonBlock;
import org.web3j.protocol.core.methods.response.Transaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.math.BigInteger;

/**
 * @Auther: Chendongming
 * @Date: 2019/8/10 13:52
 * @Description:
 */
@Data
public class TransactionInfo extends TransactionWithBLOBs {

    private TxTypeEnum typeEnum;

    /**
     * 使用原生交易信息初始化交易信息
     *
     * @param initData
     */
    public TransactionInfo ( PlatonBlock.TransactionResult initData ) {
        try {
            Transaction transaction = (Transaction) initData;
            BeanUtils.copyProperties(transaction, this);
            this.setBlockNumber(transaction.getBlockNumber().longValue());
            this.setTransactionIndex(transaction.getTransactionIndex().intValue());
            this.setGasPrice(transaction.getGasPrice().toString());
            this.setGasLimit(transaction.getGas().toString());
            this.setValue(transaction.getValue().toString());
            this.setNonce(transaction.getNonce().toString());
            Long sequence = Long.valueOf(String.valueOf(this.getBlockNumber()) + this.getTransactionIndex());
            this.setSequence(sequence);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void updateTransactionInfo ( TransactionReceipt receipt, String code ) {
        this.setGasUsed(receipt.getGasUsed().toString());
        this.setActualTxCost(receipt.getGasUsed().multiply(new BigInteger(this.getGasPrice())).toString());
        this.setTxReceiptStatus(receipt.isStatusOK()?1:0);
        //TODO:code判断,
        // 1.首先先判断to的地址是否等于内置合约地址以及code是否不为空
        // a.满足以上条件，则to的类型为合约
        // b.不满足以上条件说明to的地址为外部钱包地址，则为账户
        if (this.getTo().equals(InnerContractAddEnum.LOCKCONTRACT.getAddress()) ||
                this.getTo().equals(InnerContractAddEnum.STAKINGCONTRACT.getAddress()) ||
                this.getTo().equals(InnerContractAddEnum.PUNISHCONTRACT.getAddress()) ||
                this.getTo().equals(InnerContractAddEnum.FOUNDATION.getAddress()) ||
                this.getTo().equals(InnerContractAddEnum.GOVERNMENTCONTRACT.getAddress()) ||
                this.getTo().equals(InnerContractAddEnum.EXCITATIONCONTRACT.getAddress()) ||
                "0x" != code) {
            this.setReceiveType("contract");
        } else
            this.setReceiveType("account");
        //todo:txinfo
        //todo:txType
    }

    /**
     * 根据类型获取交易参数信息对象
     *
     * @return
     */
    public <T> T getTxJson (Class<T> clazz) {

        switch (typeEnum) {
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
            case UNDELEGATE:
                //减持/撤销委托(赎回委托),txType=1005
                return (T) JSON.parseObject(this.getTxInfo(), UnDelegateDto.class);
            case CREATEPROPOSALTEXT:
                //提交文本提案(创建提案),tyType=2000
                return (T) JSON.parseObject(this.getTxInfo(), CreateProposalTextDto.class);
            case CREATEPROPOSALUPGRADE:
                //提交升级提案(创建提案),txType=2001
                return (T) JSON.parseObject(this.getTxInfo(), CreateProposalUpgradeDto.class);
            case CREATEPROPOSALPARAMETER:
                //提交参数提案(创建提案),txType=2002
                return (T) JSON.parseObject(this.getTxInfo(), CreateProposalParamDto.class);
            case VOTINGPROPOSAL:
                //提案投票(提案投票),txType=2003
                return (T) JSON.parseObject(this.getTxInfo(), VotingProposalDto.class);
            case DECLAREVERSION:
                //版本声明,txType=2004
                return (T) JSON.parseObject(this.getTxInfo(), DeclareVersionDto.class);
            case REPORTVALIDATOR:
                //举报多签(举报验证人),txType=3000
                return (T) JSON.parseObject(this.getTxInfo(), ReportValidatorDto.class);
            case CREATERESTRICTING:
                //创建锁仓计划(创建锁仓),txType=4000
                return (T) JSON.parseObject(this.getTxInfo(), CreatereStrictingDto.class);
            default:
                return null;
        }
    }
}

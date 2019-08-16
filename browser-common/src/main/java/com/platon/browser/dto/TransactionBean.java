package com.platon.browser.dto;

import com.alibaba.fastjson.JSON;
import com.platon.browser.dao.entity.TransactionWithBLOBs;
import com.platon.browser.enums.TxTypeEnum;
import com.platon.browser.exception.BeanCreateOrUpdateException;
import com.platon.browser.param.*;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.web3j.platon.BaseResponse;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.PlatonBlock;
import org.web3j.protocol.core.methods.response.Transaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.utils.JSONUtil;
import org.web3j.utils.Numeric;

import java.math.BigInteger;
import java.util.List;

/**
 * @Auther: Chendongming
 * @Date: 2019/8/10 13:52
 * @Description:
 */
@Data
public class TransactionBean extends TransactionWithBLOBs {

    private TxTypeEnum typeEnum;

    /**
     * 使用原生交易信息初始化交易信息
     *
     * @param initData
     */
    public void init (PlatonBlock.TransactionResult initData ) {
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

    public void update (TransactionReceipt receipt) throws BeanCreateOrUpdateException {
        try{
            this.setGasUsed(receipt.getGasUsed().toString());
            this.setActualTxCost(receipt.getGasUsed().multiply(new BigInteger(this.getGasPrice())).toString());
            List <Log> list =  receipt.getLogs();
            BaseResponse response = JSONUtil.parseObject(new String(Numeric.hexStringToByteArray(list.get(0).getData())), BaseResponse.class);
            if(true == response.status){
                this.setTxReceiptStatus(1);
            }else this.setTxReceiptStatus(0);
        }catch (Exception e){
            throw new BeanCreateOrUpdateException("TransactionBean.update() error:"+e.getMessage());
        }
    }

    /**
     * 根据类型获取交易参数信息对象
     *
     * @return
     */
    public <T> T getTxParam (Class<T> clazz) {

        switch (typeEnum) {
            case CREATE_VALIDATOR:
                // 质押交易,txType=1000
                return (T) JSON.parseObject(this.getTxInfo(), CreateValidatorParam.class);
            case EDIT_VALIDATOR:
                //修改质押信息,txType=1001
                return (T) JSON.parseObject(this.getTxInfo(), EditValidatorParam.class);
            case INCREASE_STAKING:
                //增持质押(增加自有质押),txType=1002
                return (T) JSON.parseObject(this.getTxInfo(), IncreaseStakingParam.class);
            case EXIT_VALIDATOR:
                //撤销质押(退出验证人),tyType=1003
                return (T) JSON.parseObject(this.getTxInfo(), ExitValidatorParam.class);
            case DELEGATE:
                // 委托交易,tyType=1004
                return (T) JSON.parseObject(this.getTxInfo(), DelegateParam.class);
            case UN_DELEGATE:
                //减持/撤销委托(赎回委托),txType=1005
                return (T) JSON.parseObject(this.getTxInfo(), UnDelegateParam.class);
            case CREATE_PROPOSAL_TEXT:
                //提交文本提案(创建提案),tyType=2000
                return (T) JSON.parseObject(this.getTxInfo(), CreateProposalTextParam.class);
            case CREATE_PROPOSAL_UPGRADE:
                //提交升级提案(创建提案),txType=2001
                return (T) JSON.parseObject(this.getTxInfo(), CreateProposalUpgradeParam.class);
            case CREATE_PROPOSAL_PARAMETER:
                //提交参数提案(创建提案),txType=2002
                return (T) JSON.parseObject(this.getTxInfo(), CreateProposalParameterParam.class);
            case VOTING_PROPOSAL:
                //提案投票(提案投票),txType=2003
                return (T) JSON.parseObject(this.getTxInfo(), VotingProposalParam.class);
            case DECLARE_VERSION:
                //版本声明,txType=2004
                return (T) JSON.parseObject(this.getTxInfo(), DeclareVersionParam.class);
            case REPORT_VALIDATOR:
                //举报多签(举报验证人),txType=3000
                return (T) JSON.parseObject(this.getTxInfo(), ReportValidatorParam.class);
            case CREATE_RESTRICTING:
                //创建锁仓计划(创建锁仓),txType=4000
                return (T) JSON.parseObject(this.getTxInfo(), CreateRestrictingParam.class);
            default:
                return null;
        }
    }
}

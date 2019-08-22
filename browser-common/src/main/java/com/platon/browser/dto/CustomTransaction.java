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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther: Chendongming
 * @Date: 2019/8/10 13:52
 * @Description:
 */
@Data
public class CustomTransaction extends TransactionWithBLOBs {

    private TxTypeEnum typeEnum;

    /**
     * 使用原生交易信息初始化交易信息
     * @param tr
     */
    public void updateWithTransactionResult (PlatonBlock.TransactionResult tr ) {
        try {
            Transaction transaction = (Transaction) tr;
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

    public void updateWithTransactionReceipt (TransactionReceipt receipt) throws BeanCreateOrUpdateException {
        try{
            this.setGasUsed(receipt.getGasUsed().toString());
            this.setActualTxCost(receipt.getGasUsed().multiply(new BigInteger(this.getGasPrice())).toString());
            // TODO: 需要与后台商榷交易是否成功的判断标准

            this.setTxReceiptStatus(receipt.isStatusOK()?TxReceiptStatusEnum.SUCCESS.code:TxReceiptStatusEnum.FAILURE.code);

            /*
            // 关于交易是否成功的判断(此做法是否合理有待商榷)：
            // 1、交易回执logs为空列表时：
            //     a、根据交易的to地址是否是内置合约地址来判断是否是合约调用；
            //     b、如果to地址不是内置合约地址，则可能是转账或其它未知交易：
            //
            // 2、交易回执logs有内容时：
            //     a、需要解码logs.get(0).getData()来解析出status字段，用于确定交易是否成功；
            //
            // 例子：转账和委托失败，交易回执中的logs都为空
            List <Log> logs =  receipt.getLogs();
            if(logs.size()==0){
                this.setTxReceiptStatus(TxReceiptStatusEnum.FAILURE.code);
            }else {
                BaseResponse response = JSONUtil.parseObject(new String(Numeric.hexStringToByteArray(logs.get(0).getData())), BaseResponse.class);
                if(response==null) this.setTxReceiptStatus(TxReceiptStatusEnum.FAILURE.code);
                if(response!=null){
                    if(response.status) this.setTxReceiptStatus(TxReceiptStatusEnum.SUCCESS.code);
                    else this.setTxReceiptStatus(TxReceiptStatusEnum.FAILURE.code);
                }
            }*/
        }catch (Exception e){
            throw new BeanCreateOrUpdateException("CustomTransaction.update() error:"+e.getMessage());
        }
/*        if(this.getTo().equals(InnerContractAddEnum.LOCKCONTRACT.getAddress()) ||
                this.getTo().equals(InnerContractAddEnum.STAKINGCONTRACT.getAddress()) ||
                this.getTo().equals(InnerContractAddEnum.PUNISHCONTRACT.getAddress()) ||
                this.getTo().equals(InnerContractAddEnum.FOUNDATION.getAddress()) ||
                this.getTo().equals(InnerContractAddEnum.GOVERNMENTCONTRACT.getAddress()) ||
                this.getTo().equals(InnerContractAddEnum.EXCITATIONCONTRACT.getAddress()) ||
                "0x" != code )
        {
            this.setReceiveType("contract");
        }else
            this.setReceiveType("account");*/
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
            case CANCEL_PROPOSAL:
                //取消提案,txType=2005
                return (T) JSON.parseObject(this.getTxInfo(), CancelProposalParam.class);
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

    public enum TxReceiptStatusEnum{
        SUCCESS(1, "成功"),
        FAILURE(0, "失败")
        ;
        public int code;
        public String desc;
        TxReceiptStatusEnum(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }
        public int getCode(){return code;}
        public String getDesc(){return desc;}
        private static Map<Integer, TxReceiptStatusEnum> ENUMS = new HashMap<>();
        static {
            Arrays.asList(TxReceiptStatusEnum.values()).forEach(en->ENUMS.put(en.code,en));}
        public static TxReceiptStatusEnum getEnum(Integer code){
            return ENUMS.get(code);
        }
        public static boolean contains(int code){return ENUMS.containsKey(code);}
        public static boolean contains(TxReceiptStatusEnum en){return ENUMS.containsValue(en);}
    }
}

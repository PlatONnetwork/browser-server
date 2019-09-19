package com.platon.browser.dto;

import com.alibaba.fastjson.JSON;
import com.platon.browser.dao.entity.TransactionWithBLOBs;
import com.platon.browser.exception.BeanCreateOrUpdateException;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.util.encoders.Hex;
import org.springframework.beans.BeanUtils;
import org.web3j.platon.BaseResponse;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.PlatonBlock;
import org.web3j.protocol.core.methods.response.Transaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.rlp.RlpDecoder;
import org.web3j.rlp.RlpList;
import org.web3j.rlp.RlpString;
import org.web3j.utils.JSONUtil;

import java.math.BigInteger;
import java.util.*;

/**
 * @Auther: Chendongming
 * @Date: 2019/8/10 13:52
 * @Description: 交易实体扩张类
 */
@Data
public class CustomTransaction extends TransactionWithBLOBs {

    public CustomTransaction(){
        Date date = new Date();
        this.setCreateTime(date);
        this.setUpdateTime(date);
    }

    /**
     * 使用原生交易信息初始化交易信息
     * @param tr
     */
    @SuppressWarnings("rawtypes")
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
            Long sequence = this.getBlockNumber()*10000 + this.getTransactionIndex();
            this.setSequence(sequence);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据交易回执设置交易的成功标识
     * @param receipt
     * @throws BeanCreateOrUpdateException
     */
    @SuppressWarnings("rawtypes")
	public void updateWithTransactionReceipt (TransactionReceipt receipt,Set<String> innerContractAddr) throws BeanCreateOrUpdateException {
        try{
            this.setGasUsed(receipt.getGasUsed().toString());
            this.setActualTxCost(receipt.getGasUsed().multiply(new BigInteger(this.getGasPrice())).toString());
            if(innerContractAddr.contains(receipt.getTo())){
                // 第一种：ppos内置合约交易类型
                // 成功：logs.get(0).getData()来解析出Status字段，并且为true
                // 失败：非成功
                List<Log> logs =  receipt.getLogs();
                if(logs==null||logs.size()==0){
                    this.setTxReceiptStatus(TxReceiptStatusEnum.FAILURE.code);
                }else {
                    Log log = logs.get(0);
                    String data = log.getData();
                    if(StringUtils.isNotBlank(data)) {
                        data=data.replace("0x","");
                    }else{
                        this.setTxReceiptStatus(TxReceiptStatusEnum.FAILURE.code);
                    }
                    RlpList b = RlpDecoder.decode(Hex.decode(data));
                    RlpList group = (RlpList) b.getValues().get(0);
                    RlpString out = (RlpString) group.getValues().get(0);
                    String res = new String(out.getBytes());
                    BaseResponse response = JSONUtil.parseObject(res, BaseResponse.class);
                    if(response==null) this.setTxReceiptStatus(TxReceiptStatusEnum.FAILURE.code);
                    if(response!=null){
                        if(response.isStatusOk()) {
                            this.setTxReceiptStatus(TxReceiptStatusEnum.SUCCESS.code);
                        }else{
                            this.setTxReceiptStatus(TxReceiptStatusEnum.FAILURE.code);
                            this.setFailReason(response.errMsg);
                        }
                    }
                }
            }else {
                // 第二种：非第一种
                //成功：交易回执的状态： status为空或者status=1，代表成功
                //失败：非成功
                if(receipt.isStatusOK()){
                    this.setTxReceiptStatus(TxReceiptStatusEnum.SUCCESS.code);
                }else {
                    this.setTxReceiptStatus(TxReceiptStatusEnum.FAILURE.code);
                }
            }
        }catch (Exception e){
            throw new BeanCreateOrUpdateException("使用交易回执更新交易出错:"+e.getMessage());
        }
    }

    public TxTypeEnum getTypeEnum(){
        return TxTypeEnum.getEnum(this.getTxType());
    }

    /**
     * 根据类型获取交易参数信息对象
     *
     * @return
     */
    public <T> T getTxParam (Class<T> clazz) {
        return JSON.parseObject(this.getTxInfo(), clazz);
    }

    public enum TxTypeEnum {
        TRANSFER("0", "转账"),
        CONTRACT_CREATION("1","合约发布(合约创建)"),
        CONTRACT_EXECUTION("2","合约调用(合约执行)"),
        OTHERS("4","其他"),
        MPC("5","MPC交易"),
        CREATE_VALIDATOR("1000","发起质押(创建验证人)"),
        EDIT_VALIDATOR("1001","修改质押信息(编辑验证人)"),
        INCREASE_STAKING("1002","增持质押(增加自有质押)"),
        EXIT_VALIDATOR("1003","撤销质押(退出验证人)"),
        DELEGATE("1004","发起委托(委托)"),
        UN_DELEGATE("1005","减持/撤销委托(赎回委托)"),
        CREATE_PROPOSAL_TEXT("2000","提交文本提案(创建提案)"),
        CREATE_PROPOSAL_UPGRADE("2001","提交升级提案(创建提案)"),
        CREATE_PROPOSAL_PARAMETER("2002","提交参数提案(创建提案)"),
        CANCEL_PROPOSAL("2005","提交取消提案"),
        VOTING_PROPOSAL("2003","给提案投票(提案投票)"),
        DECLARE_VERSION("2004","版本声明"),
        REPORT_VALIDATOR("3000","举报多签(举报验证人)"),
        CREATE_RESTRICTING("4000","创建锁仓计划(创建锁仓)"),
        DUPLICATE_SIGN("11","区块双签"),
        ;
        private static Map<String, TxTypeEnum> map = new HashMap<>();
        static {
            Arrays.asList(TxTypeEnum.values()).forEach(typeEnum->map.put(typeEnum.code,typeEnum));
        }
        public static TxTypeEnum getEnum(String code){
            return map.get(code);
        }
        private String code;
        private String desc;
        TxTypeEnum ( String code, String desc) {
            this.code = code;
            this.desc = desc;
        }
        public String getCode() {
            return code;
        }
        public String getDesc() {
            return desc;
        }
    }
    /**
     * 交易结果成败枚举类：
     *  1.成功
     *  2.失败
     */
    public enum TxReceiptStatusEnum{
        SUCCESS(1, "成功"),
        FAILURE(0, "失败")
        ;
        private int code;
        private String desc;
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

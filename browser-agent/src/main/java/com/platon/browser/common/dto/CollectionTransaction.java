package com.platon.browser.common.dto;

import com.alibaba.fastjson.JSON;
import com.platon.browser.client.result.Receipt;
import com.platon.browser.dto.CustomTransaction;
import com.platon.browser.elasticsearch.dto.Transaction;
import com.platon.browser.enums.InnerContractAddrEnum;
import com.platon.browser.exception.BeanCreateOrUpdateException;
import com.platon.browser.util.TxParamResolver;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Data
@Slf4j
public class CollectionTransaction extends Transaction {
    private CollectionTransaction(){}
    static CollectionTransaction newInstance(){
        CollectionTransaction transaction = new CollectionTransaction();
        Date date = new Date();
        transaction.setCreTime(date);
        transaction.setUpdTime(date);
        transaction.setCost(BigDecimal.ZERO);
        transaction.setGasLimit(BigDecimal.ZERO);
        transaction.setGasPrice(BigDecimal.ZERO);
        transaction.setGasUsed(BigDecimal.ZERO);
        transaction.setStatus(StatusEnum.FAILURE.code);
        transaction.setValue(BigDecimal.ZERO);
        transaction.setIndex(0);
        return transaction;
    }

    CollectionTransaction updateWithBlock(CollectionBlock block){
        this.setTime(block.getTime());
        return this;
    }

    CollectionTransaction updateWithRawTransaction(org.web3j.protocol.core.methods.response.Transaction transaction){
        this.setNum(transaction.getBlockNumber().longValue());
        this.setBHash(transaction.getBlockHash());
        this.setHash(transaction.getHash());
        this.setValue(new BigDecimal(transaction.getValue()));
        this.setIndex(transaction.getTransactionIndex().intValue());
        this.setGasPrice(new BigDecimal(transaction.getGasPrice()));
        this.setInput(transaction.getInput());
        this.setTo(transaction.getTo());
        this.setFrom(transaction.getFrom());
        this.setNonce(transaction.getNonce().toString());
        return this;
    }

    CollectionTransaction updateWithBlockAndReceipt(CollectionBlock block,Receipt receipt) throws BeanCreateOrUpdateException {
        this.setGasUsed(new BigDecimal(receipt.getGasUsed()));
        this.setCost(this.getGasUsed().multiply(this.getGasPrice()));
        this.setFailReason(receipt.getFailReason());

        // 默认取状态字段作为交易成功与否的状态
        this.setStatus(receipt.getStatus());
        if (InnerContractAddrEnum.getAddresses().contains(this.getTo())) {
            // 如果接收者为内置合约, 取日志中的状态作为交易成功与否的状态
            this.setStatus(receipt.getLogStatus());
        }

        try {
            TxParamResolver.Result txParams = TxParamResolver.analysis(this.getInput());
            this.setInfo(JSON.toJSONString(txParams.getParam()));
            this.setType(String.valueOf(txParams.getTxTypeEnum().getCode()));
            this.setToType(ToTypeEnum.CONTRACT.code);
            if (this.getValue()!=null&&!InnerContractAddrEnum.getAddresses().contains(this.getTo())) {
                this.setType(String.valueOf(CustomTransaction.TxTypeEnum.TRANSFER.getCode()));
                this.setToType(ToTypeEnum.ACCOUNT.code);
            }
        } catch (Exception e) {
            throw new BeanCreateOrUpdateException("交易[hash:" + this.getHash() + "]的参数解析出错:" + e.getMessage());
        }
        this.setStatus(receipt.getStatus());
        this.setSeq(this.getNum()*10000+this.getIndex());

        // 累加区块中的统计信息
        switch (this.getTypeEnum()){
            case TRANSFER: // 转账交易，from地址转账交易数加一
                block.setTranQty(block.getTranQty()+1);
                break;
            case INCREASE_STAKING:// 增加自有质押
            case CREATE_VALIDATOR:// 创建验证人
            case EXIT_VALIDATOR:// 退出验证人
            case REPORT_VALIDATOR:// 举报验证人
            case EDIT_VALIDATOR:// 编辑验证人
                block.setSQty(block.getSQty()+1);
                break;
            case DELEGATE:// 发起委托
            case UN_DELEGATE:// 撤销委托
                block.setDQty(block.getDQty()+1);
                break;
            case CANCEL_PROPOSAL:// 取消提案
            case CREATE_PROPOSAL_TEXT:// 创建文本提案
            case CREATE_PROPOSAL_UPGRADE:// 创建升级提案
            case DECLARE_VERSION:// 版本声明
            case VOTING_PROPOSAL:// 提案投票
                block.setPQty(block.getPQty()+1);
                break;
            default:
        }
        // 累加当前交易的手续费到当前区块的txFee
        block.setTxFee(block.getTxFee().add(this.getCost()));
        // 累加当前交易的能量限制到当前区块的txGasLimit
        block.setTxGasLimit(block.getTxGasLimit().add(this.getGasLimit()));
        return this;
    }

    public enum TypeEnum {
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
        private static Map<String, TypeEnum> map = new HashMap<>();
        static {
            Arrays.asList(TypeEnum.values()).forEach(typeEnum->map.put(typeEnum.code,typeEnum));
        }
        public static TypeEnum getEnum(String code){
            return map.get(code);
        }
        private String code;
        private String desc;
        TypeEnum ( String code, String desc) {
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
    public enum StatusEnum{
        SUCCESS(1, "成功"),
        FAILURE(0, "失败")
        ;
        private int code;
        private String desc;
        StatusEnum(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }
        public int getCode(){return code;}
        public String getDesc(){return desc;}
        private static final Map<Integer, StatusEnum> ENUMS = new HashMap<>();
        static {
            Arrays.asList(StatusEnum.values()).forEach(en->ENUMS.put(en.code,en));}
        public static StatusEnum getEnum(Integer code){
            return ENUMS.get(code);
        }
        public static boolean contains(int code){return ENUMS.containsKey(code);}
        public static boolean contains(StatusEnum en){return ENUMS.containsValue(en);}
    }

    /**
     * 交易接收者类型(to是合约还是账户):1合约,2账户
     */
    public enum ToTypeEnum{
        CONTRACT(1, "合约"),
        ACCOUNT(2, "账户")
        ;
        private int code;
        private String desc;
        ToTypeEnum(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }
        public int getCode(){return code;}
        public String getDesc(){return desc;}
        private static final Map<Integer, ToTypeEnum> ENUMS = new HashMap<>();
        static {
            Arrays.asList(ToTypeEnum.values()).forEach(en->ENUMS.put(en.code,en));}
        public static ToTypeEnum getEnum(Integer code){
            return ENUMS.get(code);
        }
        public static boolean contains(int code){return ENUMS.containsKey(code);}
        public static boolean contains(ToTypeEnum en){return ENUMS.containsValue(en);}
    }

    /**
     * 获取当前交易的交易类型枚举
     * @return
     */
    public TypeEnum getTypeEnum(){
        return TypeEnum.getEnum(this.getType());
    }

    /**
     * 根据类型获取交易参数信息对象
     * @return
     */
    public <T> T getTxParam (Class<T> clazz) {
        return JSON.parseObject(this.getInfo(), clazz);
    }
}

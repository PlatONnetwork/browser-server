package com.platon.browser.bean;

import com.platon.browser.dao.entity.TransactionWithBLOBs;
import org.web3j.protocol.core.methods.response.Transaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

public class TransactionBean extends TransactionWithBLOBs {
    public void init(Transaction transaction, TransactionReceipt receipt){/*
        // 使用原生交易信息填充交易实体
        this.setHash(transaction.getHash());
        this.setFrom(transaction.getFrom());
        this.setValue(transaction.getValue().toString());
        this.setEnergonPrice(transaction.getGasPrice().toString());
        this.setEnergonLimit(transaction.getGas().toString());
        this.setNonce(transaction.getNonce().toString());
        this.setCreateTime(new Date());
        this.setUpdateTime(new Date());
        this.setTxReceiptStatus(1);
        this.setBlockHash(transaction.getBlockHash());
        this.setBlockNumber(transaction.getBlockNumber().longValue());
        if(transaction.getInput().length() <= 65535){
            this.setInput(transaction.getInput());
        }else {
            this.setInput(transaction.getInput().substring(0,65535));
        }
        if(transaction.getInput().equals("0x") && transaction.getValue() != null){
            this.setTxType("transfer");
        }
        try {
            AnalysisResult analysisResult = TxParamResolver.analysis(transaction.getInput(),false);
            if("1".equals(analysisResult.getType())){
                analysisResult.setFunctionName("contract deploy");
                this.setTo(receipt.getContractAddress());
                this.setReceiveType("contract");
            }else this.setTo(transaction.getTo());
            String type =  TxParamResolver.getTypeName(analysisResult.getType());
            this.setTxType(type == null ? "transfer" : type);
            String txinfo = JSON.toJSONString(analysisResult);
            this.setTxInfo(txinfo);

            // 设置col1-col3
            this.setCol1("");
            this.setCol2("");
            this.setCol3("");
            if(TransactionTypeEnum.TRANSACTION_VOTE_TICKET.code.equals(type)){
                // 投票交易，则把投票参数拆分存储到col1-col5字段，方便查询
                TxInfo bean = JSON.parseObject(txinfo,TxInfo.class);
                TxInfo.Parameter param = bean.getParameters();
                if(param!=null){
                    // 节点ID统一添加0x
                    param.setNodeId(param.getNodeId().startsWith("0x")?param.getNodeId():"0x"+param.getNodeId());
                    // 重新设置tx_info
                    this.setTxInfo(JSON.toJSONString(bean));
                    // 设置col1-col3
                    this.setCol1(param.getNodeId());
                    this.setCol2(param.getPrice().toString());
                    this.setCol3(param.getCount().toString());
                }
            }
        }catch (Exception e){
            this.setReceiveType("unknown");
            this.setTxType("unknown");
            this.setTxInfo("{}");
            this.setTo(transaction.getTo());
            e.getMessage();
        }
        // 使用交易接收者信息填充交易实体
        this.setTransactionIndex(receipt.getTransactionIndex().intValue());

        // 记录序号=交易所在区块号拼上交易索引
        String sequence = this.getBlockNumber().toString()+this.getTransactionIndex().toString();
        this.setSequence(Long.valueOf(sequence));

        this.setEnergonUsed(receipt.getGasUsed().toString());
        this.setActualTxCost(receipt.getGasUsed().multiply(transaction.getGasPrice()).toString());
        if(null == receipt.getBlockNumber() ){
            this.setTxReceiptStatus(0);
        }
    */}
}

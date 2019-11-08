package com.platon.browser.common.collection.dto;

import com.platon.browser.client.result.Receipt;
import com.platon.browser.elasticsearch.dto.Transaction;
import com.platon.browser.enums.InnerContractAddrEnum;
import com.platon.browser.exception.BeanCreateOrUpdateException;
import com.platon.browser.util.decode.DecodedResult;
import com.platon.browser.util.decode.TxInputUtil;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Slf4j
@Accessors(chain = true)
public class CollectionTransaction extends Transaction {
    private CollectionTransaction(){}
    public static CollectionTransaction newInstance(){
        Date date = new Date();
        CollectionTransaction transaction = new CollectionTransaction();
        transaction.setCreTime(date)
            .setUpdTime(date)
            .setCost(BigDecimal.ZERO)
            .setGasLimit(BigDecimal.ZERO)
            .setGasPrice(BigDecimal.ZERO)
            .setGasUsed(BigDecimal.ZERO)
            .setStatus(StatusEnum.FAILURE.getCode())
            .setValue(BigDecimal.ZERO)
            .setIndex(0);
        return transaction;
    }

    CollectionTransaction updateWithBlock(CollectionBlock block){
        this.setTime(block.getTime());
        return this;
    }

    CollectionTransaction updateWithRawTransaction(org.web3j.protocol.core.methods.response.Transaction transaction){
        this.setNum(transaction.getBlockNumber().longValue())
            .setBHash(transaction.getBlockHash())
            .setHash(transaction.getHash())
            .setValue(new BigDecimal(transaction.getValue()))
            .setIndex(transaction.getTransactionIndex().intValue())
            .setGasPrice(new BigDecimal(transaction.getGasPrice()))
            .setInput(transaction.getInput())
            .setTo(transaction.getTo())
            .setFrom(transaction.getFrom())
            .setNonce(transaction.getNonce().toString());
        return this;
    }

    CollectionTransaction updateWithBlockAndReceipt(CollectionBlock block,Receipt receipt) throws BeanCreateOrUpdateException {

        // 默认取状态字段作为交易成功与否的状态
        int status = receipt.getStatus();
        if (InnerContractAddrEnum.getAddresses().contains(getTo())) {
            // 如果接收者为内置合约, 取日志中的状态作为交易成功与否的状态
            status=receipt.getLogStatus();
        }

        try {
            // 解析交易的输入信息
            DecodedResult decodedResult = TxInputUtil.decode(getInput());
            // 参数信息
            String info = decodedResult.getParam().toJSONString();
            int type = decodedResult.getTypeEnum().getCode();
            int toType = ToTypeEnum.CONTRACT.getCode();
            if (getValue()!=null&&!InnerContractAddrEnum.getAddresses().contains(getTo())) {
                type = TypeEnum.TRANSFER.getCode();
                toType = ToTypeEnum.ACCOUNT.getCode();
            }

            this.setGasUsed(new BigDecimal(receipt.getGasUsed()))
                    .setCost(getGasUsed().multiply(getGasPrice()))
                    .setFailReason(receipt.getFailReason())
                    .setStatus(status)
                    .setSeq(getNum()*10000+getIndex())
                    .setInfo(info)
                    .setType(type)
                    .setToType(toType);
        } catch (Exception e) {
            throw new BeanCreateOrUpdateException("交易[hash:" + this.getHash() + "]的参数解析出错:" + e.getMessage());
        }

        // 累加区块中的统计信息
        switch (getTypeEnum()){
            case TRANSFER: // 转账交易，from地址转账交易数加一
                block.setTranQty(block.getTranQty()+1);
                break;
            case STAKE_CREATE:// 创建验证人
            case STAKE_INCREASE:// 增加自有质押
            case STAKE_MODIFY:// 编辑验证人
            case STAKE_EXIT:// 退出验证人
            case REPORT:// 举报验证人
                block.setSQty(block.getSQty()+1);
                break;
            case DELEGATE_CREATE:// 发起委托
            case DELEGATE_EXIT:// 撤销委托
                block.setDQty(block.getDQty()+1);
                break;
            case PROPOSAL_TEXT:// 创建文本提案
            case PROPOSAL_UPGRADE:// 创建升级提案
            case PROPOSAL_VOTE:// 提案投票
            case PROPOSAL_CANCEL:// 取消提案
            case VERSION_DECLARE:// 版本声明

                block.setPQty(block.getPQty()+1);
                break;
            default:
        }
        // 累加当前交易的手续费到当前区块的txFee
        block.setTxFee(block.getTxFee().add(getCost()));
        // 累加当前交易的能量限制到当前区块的txGasLimit
        block.setTxGasLimit(block.getTxGasLimit().add(getGasLimit()));
        return this;
    }
}

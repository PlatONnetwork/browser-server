package com.platon.browser.common.collection.dto;

import com.platon.browser.client.Receipt;
import com.platon.browser.elasticsearch.dto.Transaction;
import com.platon.browser.enums.InnerContractAddrEnum;
import com.platon.browser.exception.BeanCreateOrUpdateException;
import com.platon.browser.param.DelegateExitParam;
import com.platon.browser.param.DelegateRewardClaimParam;
import com.platon.browser.util.decode.DecodedResult;
import com.platon.browser.util.decode.TxInputUtil;
import com.platon.sdk.contracts.ppos.dto.common.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.rlp.RlpDecoder;
import org.web3j.rlp.RlpList;
import org.web3j.rlp.RlpString;
import org.web3j.rlp.RlpType;
import org.web3j.utils.Numeric;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

@Slf4j
public class CollectionTransaction extends Transaction {
    private CollectionTransaction(){}
    public static CollectionTransaction newInstance(){
        Date date = new Date();
        CollectionTransaction transaction = new CollectionTransaction();
        transaction.setCreTime(date)
            .setUpdTime(date)
            .setCost(BigDecimal.ZERO.toString())
            .setGasLimit(BigDecimal.ZERO.toString())
            .setGasPrice(BigDecimal.ZERO.toString())
            .setGasUsed(BigDecimal.ZERO.toString())
            .setStatus(StatusEnum.FAILURE.getCode())
            .setValue(BigDecimal.ZERO.toString())
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
            .setValue(transaction.getValue().toString())
            .setIndex(transaction.getTransactionIndex().intValue())
            .setGasPrice(transaction.getGasPrice().toString())
            .setInput(transaction.getInput())
            .setTo(transaction.getTo())
            .setFrom(transaction.getFrom())
            .setGasLimit(transaction.getGas().toString())
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

        DecodedResult decodedResult;
        try {
            // 解析交易的输入及交易回执log信息
            decodedResult = TxInputUtil.decode(getInput(),receipt.getLogs());
            // 参数信息
            String info = decodedResult.getParam().toJSONString();
            int type = decodedResult.getTypeEnum().getCode();
            int toType = ToTypeEnum.CONTRACT.getCode();
            if (getValue()!=null&&!InnerContractAddrEnum.getAddresses().contains(getTo())) {
                type = TypeEnum.TRANSFER.getCode();
                toType = ToTypeEnum.ACCOUNT.getCode();
            }

            this.setGasUsed(receipt.getGasUsed().toString())
                    .setCost(decimalGasUsed().multiply(decimalGasPrice()).toString())
                    .setFailReason(receipt.getFailReason())
                    .setStatus(status)
                    .setSeq(getNum()*10000+getIndex())
                    .setInfo(info)
                    .setType(type)
                    .setToType(toType);
        } catch (Exception e) {
            throw new BeanCreateOrUpdateException("交易[hash:" + this.getHash() + "]的参数解析出错:" + e.getMessage());
        }

        // 累加总交易数
        block.setTxQty(block.getTxQty()+1);
        // 累加具体业务交易数
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
                block.setDQty(block.getDQty()+1);
                break;
            case DELEGATE_EXIT:// 撤销委托
                if(status==Receipt.getSuccess()){
                    // 成功的领取交易才解析info回填
                    // 设置委托奖励提取额
                    DelegateExitParam param = getTxParam(DelegateExitParam.class);
                    BigDecimal reward = new BigDecimal(getDelegateReward(receipt.getLogs()));
                    param.setReward(reward);
                    setInfo(param.toJSONString());
                }
                block.setDQty(block.getDQty()+1);
                break;
            case CLAIM_REWARDS: // 领取委托奖励
                if(status==Receipt.getSuccess()){
                    // 成功的领取交易才解析info回填
                    DelegateRewardClaimParam fromLog = (DelegateRewardClaimParam) decodedResult.getParam();
                    setInfo(fromLog.toJSONString());
                }
                block.setDQty(block.getDQty()+1);
                break;
            case PROPOSAL_TEXT:// 创建文本提案
            case PROPOSAL_UPGRADE:// 创建升级提案
            case PROPOSAL_PARAMETER:// 创建参数提案
            case PROPOSAL_VOTE:// 提案投票
            case PROPOSAL_CANCEL:// 取消提案
            case VERSION_DECLARE:// 版本声明
                block.setPQty(block.getPQty()+1);
                break;
            default:
        }
        // 累加当前交易的手续费到当前区块的txFee
        block.setTxFee(block.decimalTxFee().add(decimalCost()).toString());
        // 累加当前交易的能量限制到当前区块的txGasLimit
        block.setTxGasLimit(block.decimalTxGasLimit().add(decimalGasLimit()).toString());
        return this;
    }

    /**
     *  获得解除委托时所提取的委托收益
     */
    public BigInteger getDelegateReward(List<Log> logs) {
        if(logs==null||logs.isEmpty()) return BigInteger.ZERO;

        String logData = logs.get(0).getData();
        if(null == logData || "".equals(logData) ) return BigInteger.ZERO;

        RlpList rlp = RlpDecoder.decode(Numeric.hexStringToByteArray(logData));
        List<RlpType> rlpList = ((RlpList)(rlp.getValues().get(0))).getValues();
        String decodedStatus = new String(((RlpString)rlpList.get(0)).getBytes());
        int statusCode = Integer.parseInt(decodedStatus);

        if(statusCode != ErrorCode.SUCCESS) return BigInteger.ZERO;

        return ((RlpString)(RlpDecoder.decode(((RlpString)rlpList.get(1)).getBytes())).getValues().get(0)).asPositiveBigInteger();
    }
}

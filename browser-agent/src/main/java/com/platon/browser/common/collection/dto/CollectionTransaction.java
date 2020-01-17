package com.platon.browser.common.collection.dto;

import com.platon.browser.client.Receipt;
import com.platon.browser.elasticsearch.dto.Transaction;
import com.platon.browser.enums.ContractTypeEnum;
import com.platon.browser.enums.InnerContractAddrEnum;
import com.platon.browser.exception.BeanCreateOrUpdateException;
import com.platon.browser.param.DelegateExitParam;
import com.platon.browser.param.DelegateRewardClaimParam;
import com.platon.browser.util.decode.DecodedResult;
import com.platon.browser.util.decode.TxInputUtil;
import com.platon.sdk.contracts.ppos.dto.common.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.PlatonGetCode;
import org.web3j.rlp.RlpDecoder;
import org.web3j.rlp.RlpList;
import org.web3j.rlp.RlpString;
import org.web3j.rlp.RlpType;
import org.web3j.utils.Numeric;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
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

    CollectionTransaction updateWithBlockAndReceipt(CollectionBlock block, Receipt receipt, Web3j web3j) throws BeanCreateOrUpdateException {

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

            // =========TODO:交易类型&to地址类型判断 START==========
            // 交易类型默认取解码出来的结果
            int type = decodedResult.getTypeEnum().getCode();
            // to地址类型默认设置为合约
            int toType = ToTypeEnum.CONTRACT.getCode();
            // 合约代码
            String binCode = null;
            // 合约方法
            String method = null;
            // 合约类型
            int contractType = ContractTypeEnum.EVM.getCode();

            if(decodedResult.getTypeEnum()==TypeEnum.OTHERS){
                // 交易input无法解析的交易
                if (getValue()!=null&&!InnerContractAddrEnum.getAddresses().contains(getTo())) {
                    // 普通转账
                    // 如果交易value不为空，且to不是内置合约地址，则交易类型设置为转账，to地址设置为账户类型
                    type = TypeEnum.TRANSFER.getCode(); // 由于转账交易没有input信息，在调用TxInputUtil.decode()后，取回来的是OTHERS类型，所以需要明确设置
                    toType = ToTypeEnum.ACCOUNT.getCode();
                }
            }else {
                // 交易input可解析的交易
                if(StringUtils.isNotBlank(receipt.getContractAddress())){
                    // contractAddress不为null，证明是合约创建
                    // 1、普通交易(转账&内置合约调用)：to!=null, 回执contractAddress==null
                    // 2、合约交易：
                    //      创建：to==null, 回执contractAddress!=null
                    //      执行：to!=null, 回执contractAddress==null
                    // 查询合约代码
                    String contractAddr = receipt.getContractAddress();
                    PlatonGetCode platonGetCode = web3j.platonGetCode(contractAddr, DefaultBlockParameterName.LATEST).send();
                    binCode = platonGetCode.getCode();
                }
                if(decodedResult.getTypeEnum()==TypeEnum.CONTRACT_EXEC){
                    // 合约执行，需要解析出被调用的合约方法
                    method = "aaa";
                }
            }
            // =========交易类型&to地址类型判断 END==========

            // 交易信息
            String info = decodedResult.getParam().toJSONString();
            this.setGasUsed(receipt.getGasUsed().toString())
                    .setCost(decimalGasUsed().multiply(decimalGasPrice()).toString())
                    .setFailReason(receipt.getFailReason())
                    .setStatus(status)
                    .setSeq(getNum()*10000+getIndex())
                    .setInfo(info)
                    .setType(type)
                    .setToType(toType)
                    .setContractType(contractType)
                    .setBin(binCode)
                    .setMethod(method);
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
                DelegateRewardClaimParam param = DelegateRewardClaimParam.builder()
                        .rewardList(new ArrayList<>())
                        .build();
                if(status==Receipt.getSuccess()){
                    // 成功的领取交易才解析info回填
                    param = (DelegateRewardClaimParam) decodedResult.getParam();
                }
                setInfo(param.toJSONString());
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
            case CONTRACT_CREATE:// 创建合约
                //TODO: 创建合约
                break;
            case CONTRACT_EXEC:// 合约执行
                //TODO: 合约执行
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

package com.platon.browser.common.collection.dto;

import com.platon.browser.client.Receipt;
import com.platon.browser.elasticsearch.dto.Transaction;
import com.platon.browser.enums.ContractTypeEnum;
import com.platon.browser.enums.InnerContractAddrEnum;
import com.platon.browser.exception.BeanCreateOrUpdateException;
import com.platon.browser.param.DelegateExitParam;
import com.platon.browser.param.DelegateRewardClaimParam;
import com.platon.browser.util.decode.innercontract.InnerContractDecodeUtil;
import com.platon.browser.util.decode.innercontract.InnerContractDecodedResult;
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

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

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

    static class ComplementInfo{
        // 交易类型
        Integer type=TypeEnum.OTHERS.getCode();
        // to地址类型默认设置为合约
        Integer toType=null;
        // 合约代码
        String binCode = null;
        // 合约方法
        String method = null;
        // 合约类型
        Integer contractType = ContractTypeEnum.EVM.getCode();
        // tx info信息
        String info = "{}";
    }
    CollectionTransaction updateWithBlockAndReceipt(CollectionBlock block, Receipt receipt, Web3j web3j, Set<String> generalContractAddressCache) throws BeanCreateOrUpdateException, IOException {

        // 默认取状态字段作为交易成功与否的状态
        int status = receipt.getStatus();
        if (InnerContractAddrEnum.getAddresses().contains(getTo())) {
            // 如果接收者为内置合约, 取日志中的状态作为交易成功与否的状态
            status=receipt.getLogStatus();
        }

        //============需要通过解码补充的交易信息============
        ComplementInfo ci = new ComplementInfo();
        String inputWithoutPrefix = StringUtils.isNotBlank(getInput())?getInput().replace("0x",""):"";
        if(InnerContractAddrEnum.getAddresses().contains(getTo())&&StringUtils.isNotBlank(inputWithoutPrefix)){
            // 如果to地址是内置合约地址，则解码交易输入
            resolveInnerContractInvokeTxComplementInfo(receipt.getLogs(),ci);
        }else{
            if(StringUtils.isBlank(getTo())) {
                // 如果to地址为空则是普通合约创建
                resolveGeneralContractCreateTxComplementInfo(getTo(),web3j,ci);
            }else{
                if(generalContractAddressCache.contains(getTo())&&inputWithoutPrefix.length()>=8){
                    // evm or wasm 合约调用
                    resolveGeneralContractInvokeTxComplementInfo(ci);
                }else {
                    BigInteger value = StringUtils.isNotBlank(getValue())?new BigInteger(getValue()):BigInteger.ZERO;
                    if(value.compareTo(BigInteger.ZERO)>0){
                        // 如果输入为空且value大于0，则是普通转账
                        resolveGeneralTransferTxComplementInfo(ci,generalContractAddressCache);
                    }
                }
            }
        }

        // 交易信息
        this.setGasUsed(receipt.getGasUsed().toString())
                .setCost(decimalGasUsed().multiply(decimalGasPrice()).toString())
                .setFailReason(receipt.getFailReason())
                .setStatus(status)
                .setSeq(getNum()*10000+getIndex())
                .setInfo(ci.info)
                .setType(ci.type)
                .setToType(ci.toType)
                .setContractType(ci.contractType)
                .setBin(ci.binCode)
                .setMethod(ci.method);

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
                    param = getTxParam(DelegateRewardClaimParam.class);
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
    private BigInteger getDelegateReward(List<Log> logs) {
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

    /**
     * 内置合约调用交易,解析补充信息
     */
    private void resolveInnerContractInvokeTxComplementInfo(List<Log> logs,ComplementInfo ci) throws BeanCreateOrUpdateException {
        InnerContractDecodedResult decodedResult;
        try {
            // 解析交易的输入及交易回执log信息
            decodedResult = InnerContractDecodeUtil.decode(getInput(),logs);
            ci.type = decodedResult.getTypeEnum().getCode();
            ci.info = decodedResult.getParam().toJSONString();
            ci.toType = ToTypeEnum.CONTRACT.getCode();
            ci.contractType = ContractTypeEnum.INNER.getCode();
            ci.method = null;
            ci.binCode = null;
        } catch (Exception e) {
            throw new BeanCreateOrUpdateException("交易[hash:" + this.getHash() + "]的参数解析出错:" + e.getMessage());
        }
    }

    /**
     * 创建普通合约交易,解析补充信息
     * @param contractAddress
     * @param web3j
     * @param ci
     * @throws IOException
     */
    private void resolveGeneralContractCreateTxComplementInfo(String contractAddress, Web3j web3j,ComplementInfo ci) throws IOException {
        ci.info="";
        PlatonGetCode platonGetCode = web3j.platonGetCode(contractAddress, DefaultBlockParameterName.LATEST).send();
        ci.binCode = platonGetCode.getCode();

        // TODO: 解析出调用合约方法名
        String txInput = getInput();
        //ci.method = getGeneralContractMethod();
        ci.toType = ToTypeEnum.CONTRACT.getCode();
        ci.type = TypeEnum.CONTRACT_CREATE.getCode();
        // 现阶段默认只有EVM类型的合约
        ci.contractType = ContractTypeEnum.EVM.getCode();
    }

    /**
     * 调用普通合约交易,解析补充信息
     * @param ci
     * @throws IOException
     */
    private void resolveGeneralContractInvokeTxComplementInfo(ComplementInfo ci) {
        ci.info="";
        ci.binCode=null;
        // TODO: 解析出调用合约方法名
        String txInput = getInput();
//        ci.method = getGeneralContractMethod();
        ci.toType = ToTypeEnum.CONTRACT.getCode();
        ci.type = TypeEnum.CONTRACT_EXEC.getCode();
        // 现阶段默认只有EVM类型的合约
        ci.contractType = ContractTypeEnum.EVM.getCode();
    }

    /**
     * 发起普通交易,解析补充信息
     * @param ci
     */
    private void resolveGeneralTransferTxComplementInfo(ComplementInfo ci,Set<String> generalContractAddressCache){
        ci.type = TypeEnum.TRANSFER.getCode();
        ci.contractType = null;
        ci.method = null;
        ci.info = "{}";
        ci.binCode = null;
        // 需要根据交易的to地址是否是什么类型的地址
        ci.toType = generalContractAddressCache.contains(getTo())? ToTypeEnum.CONTRACT.getCode():ToTypeEnum.ACCOUNT.getCode();
    }
}

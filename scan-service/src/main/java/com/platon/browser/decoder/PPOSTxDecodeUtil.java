package com.platon.browser.decoder;

import com.platon.browser.decoder.ppos.*;
import com.platon.browser.elasticsearch.dto.Transaction;
import com.platon.browser.param.OthersTxParam;
import com.platon.rlp.solidity.RlpDecoder;
import com.platon.rlp.solidity.RlpList;
import com.platon.rlp.solidity.RlpString;
import com.platon.rlp.solidity.RlpType;
import com.platon.utils.Numeric;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.util.encoders.Hex;

import java.math.BigInteger;
import java.util.List;

/**
 * 内置合约交易解析工具：
 * 1、根据tx input解得内置合约交易类型及参数
 * 2、根据logs解得内置合约执行结果相关信息
 * User: dongqile
 * Date: 2019/1/9
 * Time: 11:46
 */
@Slf4j
public class PPOSTxDecodeUtil {

    private PPOSTxDecodeUtil() {
    }

    public static PPOSTxDecodeResult decode(String txInput, String logDataHex) {
        PPOSTxDecodeResult result = new PPOSTxDecodeResult();


        result.setTxErrCode(-1);
        List<RlpType> rlpDataList = null;
        // 参考：x/xcom/common_types.go:func AddLogWithRes(state StateDB, blockNumber uint64, contractAddr common.Address, event, code string, datas ...interface{})
        // 可知ppos交易的log data组成和编码规则
        // Log.data字段编码规则:
        // 如果datas为空,  rlp([code]),
        // 如果datas不为空,rlp([code,rlp(data1),rlp(data2)...]),
        if(StringUtils.isNotBlank(logDataHex)){
            RlpList rlp = RlpDecoder.decode(Numeric.hexStringToByteArray(logDataHex));
            rlpDataList = ((RlpList) (rlp.getValues().get(0))).getValues();
            byte[] errCode =((RlpString) rlpDataList.remove(0)).getBytes(); //把errCode从队列中移出，剩余正真的返回值
            result.setTxErrCode(Integer.parseInt(new String(errCode)));
        }

        try {
            if (StringUtils.isNotEmpty(txInput) && !txInput.equals("0x")) {
                RlpList rlpList = RlpDecoder.decode(Hex.decode(txInput.replace("0x", "")));
                List<RlpType> rlpTypes = rlpList.getValues();

                if (rlpTypes.size() == 1 && rlpTypes.get(0).getClass().equals(RlpString.class)) {
                    OthersTxParam txParam = new OthersTxParam();
                    txParam.setData(((RlpString) rlpTypes.get(0)).asString());
                    result.setParam(txParam);
                    return result;
                }

                RlpList rootList = (RlpList) rlpTypes.get(0);

                RlpString rlpString = (RlpString) rootList.getValues().get(0);
                RlpList rlpList2 = RlpDecoder.decode(rlpString.getBytes());
                RlpString rl = (RlpString) rlpList2.getValues().get(0);
                BigInteger txCode = new BigInteger(1, rl.getBytes());

                Transaction.TypeEnum typeEnum = Transaction.TypeEnum.getEnum(txCode.intValue());
                if (typeEnum == null) {
                    OthersTxParam txParam = new OthersTxParam();
                    result.setParam(txParam);
                    return result;
                }
                result.setTypeEnum(typeEnum);
                switch (typeEnum) {
                    case STAKE_CREATE: // 1000 发起质押
                        return result.setParam(StakeCreateDecoder.decode(rootList));
                    case STAKE_MODIFY: // 1001 修改质押
                        return result.setParam(StakeModifyDecoder.decode(rootList));
                    case STAKE_INCREASE: // 1002 增持质押
                        return result.setParam(StakeIncreaseDecoder.decode(rootList));
                    case STAKE_EXIT: // 1003 撤销质押
                        return result.setParam(StakeExitDecoder.decode(rootList));
                    case DELEGATE_CREATE: // 1004 发起委托
                        return result.setParam(DelegateCreateDecoder.decode(rootList));
                    case DELEGATE_EXIT: // 1005 减持/撤销委托
                        return result.setParam(DelegateExitDecoder.decode(rootList, result.getTxErrCode(), rlpDataList));
                    case REDEEM_DELEGATION: // 1006 领取解锁的委托
                        return result.setParam(RedeemDelegationDecoder.decode(rootList, result.getTxErrCode(), rlpDataList));
                    case PROPOSAL_TEXT: // 2000 提交文本提案
                        return result.setParam(ProposalTextDecoder.decode(rootList));
                    case PROPOSAL_UPGRADE: // 2001 提交升级提案
                        return result.setParam(ProposalUpgradeDecoder.decode(rootList));
                    case PROPOSAL_PARAMETER: // 2002 提交升级提案
                        return result.setParam(ProposalParameterDecoder.decode(rootList));
                    case PROPOSAL_CANCEL: // 2005 提交取消提案
                        return result.setParam(ProposalCancelDecoder.decode(rootList));
                    case PROPOSAL_VOTE: // 2003 给提案投票
                        return result.setParam(ProposalVoteDecoder.decode(rootList));
                    case VERSION_DECLARE: // 2004 版本声明
                        return result.setParam(VersionDeclareDecoder.decode(rootList));
                    case REPORT: // 3000 举报双签
                        return result.setParam(ReportDecoder.decode(rootList));
                    case RESTRICTING_CREATE: // 4000 创建锁仓计划
                        return result.setParam(RestrictingCreateDecoder.decode(rootList));
                    case CLAIM_REWARDS: // 5000 领取委托奖励
                        // 如果日志为空则不解析
                        if (StringUtils.isBlank(logDataHex)) {
                            return result;
                        }
                        return result.setParam(DelegateRewardClaimDecoder.decode(rootList, result.getTxErrCode(), rlpDataList));
                    default:
                        break;
                }
            }
        } catch (Exception e) {
            log.error("解析内置合约交易输入出错:", e);
        }
        return result;
    }
/*
    public static PPOSTxDecodeResult decode(ImplicitPPOSTx implicitPPOSTx) {
        PPOSTxDecodeResult result = new PPOSTxDecodeResult();
        Transaction.TypeEnum typeEnum = Transaction.TypeEnum.getEnum(implicitPPOSTx.getFnCode());
        switch (typeEnum) {
            case STAKE_CREATE: // 1000 发起质押
                return result.setParam(StakeCreateDecoder.decode(implicitPPOSTx));
            case STAKE_MODIFY: // 1001 修改质押
                return result.setParam(StakeModifyDecoder.decode(implicitPPOSTx));
           case STAKE_INCREASE: // 1002 增持质押
                return result.setParam(StakeIncreaseDecoder.decode(implicitPPOSTx));
            case STAKE_EXIT: // 1003 撤销质押
                return result.setParam(StakeExitDecoder.decode(implicitPPOSTx));
            case DELEGATE_CREATE: // 1004 发起委托
                return result.setParam(DelegateCreateDecoder.decode(implicitPPOSTx));
            case DELEGATE_EXIT: // 1005 减持/撤销委托
                return result.setParam(DelegateExitDecoder.decode(implicitPPOSTx));
            case REDEEM_DELEGATION: // 1006 领取解锁的委托
                return result.setParam(RedeemDelegationDecoder.decode(implicitPPOSTx));
            case PROPOSAL_TEXT: // 2000 提交文本提案
                return result.setParam(ProposalTextDecoder.decode(implicitPPOSTx));
            case PROPOSAL_UPGRADE: // 2001 提交升级提案
                return result.setParam(ProposalUpgradeDecoder.decode(implicitPPOSTx));
            case PROPOSAL_PARAMETER: // 2002 提交参数提案
                return result.setParam(ProposalParameterDecoder.decode(implicitPPOSTx));
            case PROPOSAL_CANCEL: // 2005 提交取消提案
                return result.setParam(ProposalCancelDecoder.decode(implicitPPOSTx));
            case PROPOSAL_VOTE: // 2003 给提案投票
                return result.setParam(ProposalVoteDecoder.decode(implicitPPOSTx));
            case VERSION_DECLARE: // 2004 版本声明
                return result.setParam(VersionDeclareDecoder.decode(implicitPPOSTx));
            case REPORT: // 3000 举报双签
                return result.setParam(ReportDecoder.decode(implicitPPOSTx));
            case RESTRICTING_CREATE: // 4000 创建锁仓计划
                return result.setParam(RestrictingCreateDecoder.decode(implicitPPOSTx));
            case CLAIM_REWARDS: // 5000 领取委托奖励
                // 如果日志为空则不解析
                if (log == null) {
                    return result;
                }
                return result.setParam(DelegateRewardClaimDecoder.decode(implicitPPOSTx));
            default:
                break;
        }
        return result;
    }*/
}

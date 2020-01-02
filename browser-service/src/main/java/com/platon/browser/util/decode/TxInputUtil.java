package com.platon.browser.util.decode;

import com.platon.browser.elasticsearch.dto.Transaction;
import com.platon.browser.param.OthersTxParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.util.encoders.Hex;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.rlp.RlpDecoder;
import org.web3j.rlp.RlpList;
import org.web3j.rlp.RlpString;
import org.web3j.rlp.RlpType;

import java.math.BigInteger;
import java.util.List;

/**
 * 交易参数解析器
 * User: dongqile
 * Date: 2019/1/9
 * Time: 11:46
 */
@Slf4j
public class TxInputUtil {
    private TxInputUtil(){}
    public static DecodedResult decode(String txInput,List<Log> logs) {
        DecodedResult result = new DecodedResult();
        try {
            if (StringUtils.isNotEmpty(txInput) && !txInput.equals("0x")) {
                RlpList rlpList = RlpDecoder.decode(Hex.decode(txInput.replace("0x", "")));
                List <RlpType> rlpTypes = rlpList.getValues();

                if(rlpTypes.size() == 1 && rlpTypes.get(0).getClass().equals(RlpString.class)) {
                    OthersTxParam txParam=new OthersTxParam();
                    txParam.setData(((RlpString)rlpTypes.get(0)).asString());
                    result.setParam(txParam);
                    return result;
                }

                RlpList rootList = (RlpList) rlpTypes.get(0);

                RlpString rlpString = (RlpString) rootList.getValues().get(0);
                RlpList rlpList2 = RlpDecoder.decode(rlpString.getBytes());
                RlpString rl = (RlpString) rlpList2.getValues().get(0);
                BigInteger txCode = new BigInteger(1, rl.getBytes());



                Transaction.TypeEnum typeEnum = Transaction.TypeEnum.getEnum(txCode.intValue());
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
                        return result.setParam(DelegateExitDecoder.decode(rootList));
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
                        return result.setParam(DelegateRewardClaimDecoder.decode(rootList,logs));
				default:
					break;
                }
            }
        } catch (Exception e) {
            log.error("解析交易输入出错:",e);
        }
        return result;
    }
}

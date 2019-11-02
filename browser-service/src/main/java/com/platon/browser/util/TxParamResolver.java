package com.platon.browser.util;


import com.platon.browser.elasticsearch.dto.Transaction;
import com.platon.browser.param.*;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.util.encoders.Hex;
import org.web3j.rlp.RlpDecoder;
import org.web3j.rlp.RlpList;
import org.web3j.rlp.RlpString;
import org.web3j.rlp.RlpType;
import org.web3j.utils.Numeric;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * 交易参数解析器
 * User: dongqile
 * Date: 2019/1/9
 * Time: 11:46
 */
@Slf4j
public class TxParamResolver {

    @Data
    @Accessors(chain = true)
    public static class Result {
        private TxParam param=OthersTxParam.builder().build();
        private Transaction.TypeEnum typeEnum= Transaction.TypeEnum.OTHERS;
    }

    public static Result analysis ( String input ) {
        Result result = new Result();
        try {
            if (StringUtils.isNotEmpty(input) && !input.equals("0x")) {
                RlpList rlpList = RlpDecoder.decode(Hex.decode(input.replace("0x", "")));
                List <RlpType> rlpTypes = rlpList.getValues();
                RlpList rlpList1 = (RlpList) rlpTypes.get(0);

                RlpString rlpString = (RlpString) rlpList1.getValues().get(0);
                RlpList rlpList2 = RlpDecoder.decode(rlpString.getBytes());
                RlpString rl = (RlpString) rlpList2.getValues().get(0);
                BigInteger txCode = new BigInteger(1, rl.getBytes());

                Transaction.TypeEnum typeEnum = Transaction.TypeEnum.getEnum(txCode.intValue());
                result.setTypeEnum(typeEnum);
                switch (typeEnum) {
                    case STAKE_CREATE: // 1000
                        // 发起质押
                        //typ  表示使用账户自由金额还是账户的锁仓金额做质押 0: 自由金额； 1: 锁仓金额
                        BigInteger type1000 =  bigIntegerResolver((RlpString) rlpList1.getValues().get(1));
                        //用于接受出块奖励和质押奖励的收益账户benefitAddress
                        String address1000 = stringResolver((RlpString) rlpList1.getValues().get(2));
                        //被质押的节点的NodeId
                        String nodeId1000 = stringResolver((RlpString) rlpList1.getValues().get(3));
                        //外部Id externalId
                        String externalId1000 = stringResolver((RlpString) rlpList1.getValues().get(4));
                        externalId1000 = new String(Numeric.hexStringToByteArray(externalId1000));
                        //被质押节点的名称 nodeName
                        String nodeName1000 = stringResolver((RlpString) rlpList1.getValues().get(5));
                        nodeName1000 = new String(Numeric.hexStringToByteArray(nodeName1000));
                        //节点的第三方主页 website
                        String website1000 = stringResolver((RlpString) rlpList1.getValues().get(6));
                        website1000 = new String(Numeric.hexStringToByteArray(website1000));
                        //节点的描述 details
                        String details1000 = stringResolver((RlpString) rlpList1.getValues().get(7));
                        details1000 = new String(Numeric.hexStringToByteArray(details1000));
                        //质押的von amount programVersion
                        BigInteger amount1000 =  bigIntegerResolver((RlpString) rlpList1.getValues().get(8));
                        //程序的真实版本，治理rpc获取
                        BigInteger version1000 =  bigIntegerResolver((RlpString) rlpList1.getValues().get(9));

                        StakeCreateParam param1000 = StakeCreateParam.builder()
                                .type(type1000.intValue())
                                .benefitAddress(address1000)
                                .nodeId(nodeId1000)
                                .externalId("0x".equals(externalId1000)?"":externalId1000)
                                .nodeName(nodeName1000)
                                .website(website1000)
                                .details(details1000)
                                .amount(amount1000.toString())
                                .programVersion(version1000)
                                .build();
                        return result.setParam(param1000);
                    case STAKE_MODIFY: // 1001
                        // 修改质押信息
                        //用于接受出块奖励和质押奖励的收益账户
                        String address1001 = stringResolver((RlpString) rlpList1.getValues().get(1));
                        //被质押的节点的NodeId
                        String nodeId1001 = stringResolver((RlpString) rlpList1.getValues().get(2));
                        //外部Id
                        String externalId1001 = stringResolver((RlpString) rlpList1.getValues().get(3));
                        externalId1001 = new String(Numeric.hexStringToByteArray(externalId1001));
                        //被质押节点的名称
                        String nodeName1001 = stringResolver((RlpString) rlpList1.getValues().get(4));
                        nodeName1001 = new String(Numeric.hexStringToByteArray(nodeName1001));
                        //节点的第三方主页
                        String website1001 = stringResolver((RlpString) rlpList1.getValues().get(5));
                        website1001 = new String(Numeric.hexStringToByteArray(website1001));
                        //节点的描述
                        String detail1001 = stringResolver((RlpString) rlpList1.getValues().get(6));
                        detail1001 = new String(Numeric.hexStringToByteArray(detail1001));

                        StakeModifyParam param1001 = StakeModifyParam.builder()
                                .nodeId(nodeId1001)
                                .benefitAddress(address1001)
                                .externalId("0x".equals(externalId1001)?"":externalId1001)
                                .nodeName(nodeName1001)
                                .website(website1001)
                                .details(detail1001)
                                .build();
                        return result.setParam(param1001);
                    case STAKE_INCREASE: // 1002
                        // 增持质押
                        //被质押的节点的NodeId
                        String nodeId1002 = stringResolver((RlpString) rlpList1.getValues().get(1));
                        //typ  表示使用账户自由金额还是账户的锁仓金额做质押 0: 自由金额； 1: 锁仓金额
                        BigInteger type1002 =  bigIntegerResolver((RlpString) rlpList1.getValues().get(2));
                        //质押的von
                        BigInteger amount1002 =  bigIntegerResolver((RlpString) rlpList1.getValues().get(3));

                        StakeIncreaseParam param1002 = StakeIncreaseParam.builder()
                                .nodeId(nodeId1002)
                                .type(type1002.intValue())
                                .amount(amount1002.toString())
                                .stakingBlockNum("")
                                .build();
                        return result.setParam(param1002);
                    case STAKE_EXIT: // 1003
                        // 撤销质押
                        //被质押的节点的NodeId
                        String nodeId1003 = stringResolver((RlpString) rlpList1.getValues().get(1));
                        StakeExitParam param1003 = StakeExitParam.builder()
                                .nodeId(nodeId1003)
                                .nodeName("")
                                .stakingBlockNum("")
                                .build();
                        return result.setParam(param1003);
                    case DELEGATE_CREATE: // 1004
                        // 发起委托
                        //typ  表示使用账户自由金额还是账户的锁仓金额做质押 0: 自由金额； 1: 锁仓金额
                        BigInteger type1004 =  bigIntegerResolver((RlpString) rlpList1.getValues().get(1));
                        //被质押的节点的NodeId
                        String nodeId1004 = stringResolver((RlpString) rlpList1.getValues().get(2));
                        //委托的金额
                        BigInteger amount1004 =  bigIntegerResolver((RlpString) rlpList1.getValues().get(3));

                        DelegateCreateParam param1004 = DelegateCreateParam.builder()
                                .type(type1004.intValue())
                                .nodeId(nodeId1004)
                                .amount(amount1004.toString())
                                .nodeName("")
                                .stakingBlockNum("")
                                .build();
                        return result.setParam(param1004);
                    case DELEGATE_EXIT: // 1005
                        // 减持/撤销委托
                        //代表着某个node的某次质押的唯一标示
                        String blockNumber1005 = stringResolver((RlpString) rlpList1.getValues().get(1));
                        blockNumber1005=blockNumber1005.replace("0x","");
                        //被质押的节点的NodeId
                        String nodeId1005 = stringResolver((RlpString) rlpList1.getValues().get(2));
                        //减持委托的金额(按照最小单位算，1LAT = 10**18 von)
                        BigInteger amount1005 =  bigIntegerResolver((RlpString) rlpList1.getValues().get(3));

                        DelegateExitParam param1005 = DelegateExitParam.builder()
                                .stakingBlockNum(new BigInteger(blockNumber1005,16).longValue())
                                .nodeId(nodeId1005)
                                .amount(amount1005.toString())
                                .build();
                        return result.setParam(param1005);
                    case PROPOSAL_TEXT: // 2000
                        // 提交文本提案
                        //提交提案的验证人
                        String nodeId2000 = stringResolver((RlpString) rlpList1.getValues().get(1));
                        //pIDID
                        String pIdID2000 = stringResolver((RlpString) rlpList1.getValues().get(2));
                        pIdID2000 =  new String(Numeric.hexStringToByteArray(pIdID2000));

                        ProposalTextParam param2000 = ProposalTextParam.builder()
                                .verifier(nodeId2000)
                                .pIDID(pIdID2000)
                                .build();
                        return result.setParam(param2000);
                    case PROPOSAL_UPGRADE: // 2001
                        // 提交升级提案
                        //提交提案的验证人
                        String nodeId2001 = stringResolver((RlpString) rlpList1.getValues().get(1));
                        //pIDID
                        String pIdID2001 = stringResolver((RlpString) rlpList1.getValues().get(2));
                        pIdID2001 =  new String(Numeric.hexStringToByteArray(pIdID2001));
                        //升级版本
                        BigInteger version2001 =  bigIntegerResolver((RlpString) rlpList1.getValues().get(3));
                        //投票截止区块高度
                        BigInteger round2001 =  bigIntegerResolver((RlpString) rlpList1.getValues().get(4));
                        //结束轮转换结束区块高度

                        ProposalUpgradeParam param2001 = ProposalUpgradeParam.builder()
                                .verifier(nodeId2001)
                                .endVotingRound(new BigDecimal(round2001))
                                .newVersion(version2001.intValue())
                                .pIDID(pIdID2001)
                                .build();
                        return result.setParam(param2001);
                    case PROPOSAL_CANCEL: // 2005
                        // 提交取消提案
                        //提交提案的验证人
                        String nodeId2005 = stringResolver((RlpString) rlpList1.getValues().get(1));
                        //本提案的pIDID
                        String pIdID2005 = stringResolver((RlpString) rlpList1.getValues().get(2));
                        pIdID2005 =  new String(Numeric.hexStringToByteArray(pIdID2005));
                        //投票截止区块高度
                        BigInteger round2005 =  bigIntegerResolver((RlpString) rlpList1.getValues().get(3));
                        //被取消的pIDID
                        String cancelPidID2005 =  stringResolver((RlpString) rlpList1.getValues().get(4));

                        ProposalCancelParam param2005 = ProposalCancelParam.builder()
                                .verifier(nodeId2005)
                                .pIDID(pIdID2005)
                                .endVotingRound(new BigDecimal(round2005))
                                .canceledProposalID(cancelPidID2005)
                                .build();
                        return result.setParam(param2005);
                    case PROPOSAL_VOTE: // 2003
                        // 给提案投票
                        //投票验证人
                        String nodeId2003 = stringResolver((RlpString) rlpList1.getValues().get(1));
                        //提案ID
                        String proposalID2003 = stringResolver((RlpString) rlpList1.getValues().get(2));
                        //投票选项
                        BigInteger option2003 =  bigIntegerResolver((RlpString) rlpList1.getValues().get(3));
                        //节点代码版本，有rpc的getProgramVersion接口获取
                        BigInteger programVersion2003 =  bigIntegerResolver((RlpString) rlpList1.getValues().get(4));
                        //代码版本签名，有rpc的getProgramVersion接口获取
                        String versionSign2003 = stringResolver((RlpString) rlpList1.getValues().get(5));

                        ProposalVoteParam param2003 = ProposalVoteParam.builder()
                                .verifier(nodeId2003)
                                .proposalId(proposalID2003)
                                .option(option2003.toString())
                                .programVersion(programVersion2003.toString())
                                .versionSign(versionSign2003)
                                .build();
                        return result.setParam(param2003);
                    case VERSION_DECLARE: // 2004
                        // 版本声明
                        //声明的节点，只能是验证人/候选人
                        String nodeId2004 = stringResolver((RlpString) rlpList1.getValues().get(1));
                        //声明的版本，有rpc的getProgramVersion接口获取
                        BigInteger version2004 =  bigIntegerResolver((RlpString) rlpList1.getValues().get(2));
                        //声明的版本签名，有rpc的getProgramVersion接口获取
                        String versionSign2004 = stringResolver((RlpString) rlpList1.getValues().get(3));
                        VersionDeclareParam param2004 = VersionDeclareParam.builder()
                                .activeNode(nodeId2004)
                                .version(version2004.intValue())
                                .versionSigns(versionSign2004)
                                .build();
                        return result.setParam(param2004);
                    case REPORT: // 3000
                        // 举报双签
                        //type
                        BigInteger type3000 = bigIntegerResolver((RlpString) rlpList1.getValues().get(1));
                        //data
                        String evidence3000 = stringResolver((RlpString) rlpList1.getValues().get(2));
                        evidence3000 = new String(Numeric.hexStringToByteArray(evidence3000));

                        ReportParam param3000 = ReportParam.builder()
                                .type(type3000)
                                .data(evidence3000)
                                .build();
                        param3000.init();
                        return result.setParam(param3000);
                    case RESTRICTING_CREATE: // 4000
                        //创建锁仓计划
                        //锁仓释放到账账户
                        String account4000 = stringResolver((RlpString) rlpList1.getValues().get(1));
                        // RestrictingPlan 类型的列表（数组）
                        List<RestrictingCreateParam.RestrictingPlan> plans4000 = resolvePlan((RlpString) rlpList1.getValues().get(2));

                        RestrictingCreateParam param4000 = RestrictingCreateParam.builder()
                                .account(account4000)
                                .plans(plans4000)
                                .build();
                        return result.setParam(param4000);
				default:
					break;
                }
            }
        } catch (Exception e) {
            log.error("解析交易输入出错:",e);
        }
        return result;
    }


    public static BigInteger bigIntegerResolver ( RlpString rlpString ) {
        RlpString integers = rlpString;
        RlpList integersList = RlpDecoder.decode(integers.getBytes());
        RlpString integersString = (RlpString) integersList.getValues().get(0);
        return new BigInteger(1, integersString.getBytes());
    }

    public static String stringResolver ( RlpString rlpString ) {
        RlpString strings = rlpString;
        RlpList stringsList = RlpDecoder.decode(strings.getBytes());
        RlpString stringsListString = (RlpString) stringsList.getValues().get(0);
        return Numeric.toHexString(stringsListString.getBytes());
    }

    public static List<RestrictingCreateParam.RestrictingPlan> resolvePlan (RlpString rlpString ) {
        List<RestrictingCreateParam.RestrictingPlan> list = new ArrayList<>();
        RlpList bean = RlpDecoder.decode(rlpString.getBytes());
        List <RlpType> beanList = ((RlpList) bean.getValues().get(0)).getValues();
        for (RlpType beanType : beanList) {
            RlpList beanTypeList = (RlpList) beanType;
            RlpString parama = (RlpString) beanTypeList.getValues().get(0);
            RlpString paramb = (RlpString) beanTypeList.getValues().get(1);
            RestrictingCreateParam.RestrictingPlan planParam = RestrictingCreateParam.RestrictingPlan.builder()
                    .epoch(parama.asPositiveBigInteger().intValue())
                    .amount(paramb.asPositiveBigInteger().toString())
                    .build();
            list.add(planParam);
        }
        return list;
    }

    public static void main ( String[] args ) {
        String date = "0xf858838203ed838215c8b842b8400aa9805681d8f77c05f317efc141c97d5adb511ffb51f5a251d2d7a4a3a96d9a12adf39f06b702f0ccdff9eddc1790eb272dca31b0c47751d49b5931c58701e78b8a021e19e0c9bab2400000";
        Result res = TxParamResolver.analysis(date);
        log.info("{}",res.param.toJSONString());
    }
}

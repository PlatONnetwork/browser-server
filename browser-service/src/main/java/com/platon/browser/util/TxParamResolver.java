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
        private TxParam param;
        private Transaction.TypeEnum typeEnum;
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
                    case CREATE_VALIDATOR: // 1000
                        // 发起质押
                        //typ  表示使用账户自由金额还是账户的锁仓金额做质押 0: 自由金额； 1: 锁仓金额
                        BigInteger type1000 =  Resolver.bigIntegerResolver((RlpString) rlpList1.getValues().get(1));
                        //用于接受出块奖励和质押奖励的收益账户benefitAddress
                        String address1000 = Resolver.stringResolver((RlpString) rlpList1.getValues().get(2));
                        //被质押的节点的NodeId
                        String nodeId1000 = Resolver.stringResolver((RlpString) rlpList1.getValues().get(3));
                        //外部Id externalId
                        String externalId1000 = Resolver.stringResolver((RlpString) rlpList1.getValues().get(4));
                        externalId1000 = new String(Numeric.hexStringToByteArray(externalId1000));
                        //被质押节点的名称 nodeName
                        String nodeName1000 = Resolver.stringResolver((RlpString) rlpList1.getValues().get(5));
                        nodeName1000 = new String(Numeric.hexStringToByteArray(nodeName1000));
                        //节点的第三方主页 website
                        String website1000 = Resolver.stringResolver((RlpString) rlpList1.getValues().get(6));
                        website1000 = new String(Numeric.hexStringToByteArray(website1000));
                        //节点的描述 details
                        String details1000 = Resolver.stringResolver((RlpString) rlpList1.getValues().get(7));
                        details1000 = new String(Numeric.hexStringToByteArray(details1000));
                        //质押的von amount programVersion
                        BigInteger amount1000 =  Resolver.bigIntegerResolver((RlpString) rlpList1.getValues().get(8));
                        //程序的真实版本，治理rpc获取
                        BigInteger version1000 =  Resolver.bigIntegerResolver((RlpString) rlpList1.getValues().get(9));

                        CreateValidatorParam createValidatorParam = CreateValidatorParam.builder()
                                .type(type1000.intValue())
                                .benefitAddress(address1000)
                                .nodeId(nodeId1000)
                                .externalId("0x".equals(externalId1000)?"":externalId1000)
                                .nodeName(nodeName1000)
                                .website(website1000)
                                .details(details1000)
                                .amount(amount1000.toString())
                                .programVersion(version1000.toString())
                                .build();
                        return result.setParam(createValidatorParam);
                    case EDIT_VALIDATOR: // 1001
                        // 修改质押信息
                        //用于接受出块奖励和质押奖励的收益账户
                        String address1001 = Resolver.stringResolver((RlpString) rlpList1.getValues().get(1));
                        //被质押的节点的NodeId
                        String nodeId1001 = Resolver.stringResolver((RlpString) rlpList1.getValues().get(2));
                        //外部Id
                        String externalId1001 = Resolver.stringResolver((RlpString) rlpList1.getValues().get(3));
                        externalId1001 = new String(Numeric.hexStringToByteArray(externalId1001));
                        //被质押节点的名称
                        String nodeName1001 = Resolver.stringResolver((RlpString) rlpList1.getValues().get(4));
                        nodeName1001 = new String(Numeric.hexStringToByteArray(nodeName1001));
                        //节点的第三方主页
                        String website1001 = Resolver.stringResolver((RlpString) rlpList1.getValues().get(5));
                        website1001 = new String(Numeric.hexStringToByteArray(website1001));
                        //节点的描述
                        String detail1001 = Resolver.stringResolver((RlpString) rlpList1.getValues().get(6));
                        detail1001 = new String(Numeric.hexStringToByteArray(detail1001));

                        EditValidatorParam editValidatorParam = EditValidatorParam.builder()
                                .nodeId(nodeId1001)
                                .benefitAddress(address1001)
                                .externalId("0x".equals(externalId1001)?"":externalId1001)
                                .nodeName(nodeName1001)
                                .website(website1001)
                                .details(detail1001)
                                .build();
                        return result.setParam(editValidatorParam);
                    case INCREASE_STAKING: // 1002
                        // 增持质押
                        //被质押的节点的NodeId
                        String nodeId1002 = Resolver.stringResolver((RlpString) rlpList1.getValues().get(1));
                        //typ  表示使用账户自由金额还是账户的锁仓金额做质押 0: 自由金额； 1: 锁仓金额
                        BigInteger type1002 =  Resolver.bigIntegerResolver((RlpString) rlpList1.getValues().get(2));
                        //质押的von
                        BigInteger amount1002 =  Resolver.bigIntegerResolver((RlpString) rlpList1.getValues().get(3));

                        IncreaseStakingParam increaseStakingParam = IncreaseStakingParam.builder()
                                .nodeId(nodeId1002)
                                .type(type1002.intValue())
                                .amount(amount1002.toString())
                                .stakingBlockNum("")
                                .build();
                        return result.setParam(increaseStakingParam);
                    case EXIT_VALIDATOR: // 1003
                        // 撤销质押
                        //被质押的节点的NodeId
                        String nodeId1003 = Resolver.stringResolver((RlpString) rlpList1.getValues().get(1));
                        ExitValidatorParam exitValidatorParam = ExitValidatorParam.builder()
                                .nodeId(nodeId1003)
                                .nodeName("")
                                .stakingBlockNum("")
                                .build();
                        return result.setParam(exitValidatorParam);
                    case DELEGATE: // 1004
                        // 发起委托
                        //typ  表示使用账户自由金额还是账户的锁仓金额做质押 0: 自由金额； 1: 锁仓金额
                        BigInteger type1004 =  Resolver.bigIntegerResolver((RlpString) rlpList1.getValues().get(1));
                        //被质押的节点的NodeId
                        String nodeId1004 = Resolver.stringResolver((RlpString) rlpList1.getValues().get(2));
                        //委托的金额
                        BigInteger amount1004 =  Resolver.bigIntegerResolver((RlpString) rlpList1.getValues().get(3));

                        DelegateParam delegateParam = DelegateParam.builder()
                                .type(type1004.intValue())
                                .nodeId(nodeId1004)
                                .amount(amount1004.toString())
                                .nodeName("")
                                .stakingBlockNum("")
                                .build();
                        return result.setParam(delegateParam);
                    case UN_DELEGATE: // 1005
                        // 减持/撤销委托
                        //代表着某个node的某次质押的唯一标示
                        String blockNumber1005 = Resolver.stringResolver((RlpString) rlpList1.getValues().get(1));
                        blockNumber1005=blockNumber1005.replace("0x","");
                        //被质押的节点的NodeId
                        String nodeId1005 = Resolver.stringResolver((RlpString) rlpList1.getValues().get(2));
                        //减持委托的金额(按照最小单位算，1LAT = 10**18 von)
                        BigInteger amount1005 =  Resolver.bigIntegerResolver((RlpString) rlpList1.getValues().get(3));

                        UnDelegateParam unDelegateParam = UnDelegateParam.builder()
                                .stakingBlockNum(new BigInteger(blockNumber1005).longValue())
                                .nodeId(nodeId1005)
                                .amount(amount1005.toString())
                                .build();
                        return result.setParam(unDelegateParam);
                    case CREATE_PROPOSAL_TEXT: // 2000
                        // 提交文本提案
                        //提交提案的验证人
                        String nodeId2000 = Resolver.stringResolver((RlpString) rlpList1.getValues().get(1));
                        //pIDID
                        String pIdID2000 = Resolver.stringResolver((RlpString) rlpList1.getValues().get(2));
                        pIdID2000 =  new String(Numeric.hexStringToByteArray(pIdID2000));

                        CreateProposalTextParam createProposalTextParam = CreateProposalTextParam.builder()
                                .verifier(nodeId2000)
                                .pIDID(pIdID2000)
                                .build();
                        return result.setParam(createProposalTextParam);
                    case CREATE_PROPOSAL_UPGRADE: // 2001
                        // 提交升级提案
                        //提交提案的验证人
                        String nodeId2001 = Resolver.stringResolver((RlpString) rlpList1.getValues().get(1));
                        //pIDID
                        String pIdID2001 = Resolver.stringResolver((RlpString) rlpList1.getValues().get(2));
                        pIdID2001 =  new String(Numeric.hexStringToByteArray(pIdID2001));
                        //升级版本
                        BigInteger version2001 =  Resolver.bigIntegerResolver((RlpString) rlpList1.getValues().get(3));
                        //投票截止区块高度
                        BigInteger round2001 =  Resolver.bigIntegerResolver((RlpString) rlpList1.getValues().get(4));
                        //结束轮转换结束区块高度

                        CreateProposalUpgradeParam createProposalUpgradeParam = CreateProposalUpgradeParam.builder()
                                .verifier(nodeId2001)
                                .endVotingRound(new BigDecimal(round2001))
                                .newVersion(version2001.intValue())
                                .pIDID(pIdID2001)
                                .build();
                        return result.setParam(createProposalUpgradeParam);
                    case CANCEL_PROPOSAL: // 2005
                        // 提交取消提案
                        //提交提案的验证人
                        String nodeId2005 = Resolver.stringResolver((RlpString) rlpList1.getValues().get(1));
                        //本提案的pIDID
                        String pIdID2005 = Resolver.stringResolver((RlpString) rlpList1.getValues().get(2));
                        pIdID2005 =  new String(Numeric.hexStringToByteArray(pIdID2005));
                        //投票截止区块高度
                        BigInteger round2005 =  Resolver.bigIntegerResolver((RlpString) rlpList1.getValues().get(3));
                        //被取消的pIDID
                        String cancelPidID2005 =  Resolver.stringResolver((RlpString) rlpList1.getValues().get(4));

                        CancelProposalParam cancelProposalParam = CancelProposalParam.builder()
                                .verifier(nodeId2005)
                                .pIDID(pIdID2005)
                                .endVotingRound(new BigDecimal(round2005))
                                .canceledProposalID(cancelPidID2005)
                                .build();
                        return result.setParam(cancelProposalParam);
                    case VOTING_PROPOSAL: // 2003
                        // 给提案投票
                        //投票验证人
                        String nodeId2003 = Resolver.stringResolver((RlpString) rlpList1.getValues().get(1));
                        //提案ID
                        String proposalID2003 = Resolver.stringResolver((RlpString) rlpList1.getValues().get(2));
                        //投票选项
                        BigInteger option2003 =  Resolver.bigIntegerResolver((RlpString) rlpList1.getValues().get(3));
                        //节点代码版本，有rpc的getProgramVersion接口获取
                        BigInteger programVersion2003 =  Resolver.bigIntegerResolver((RlpString) rlpList1.getValues().get(4));
                        //代码版本签名，有rpc的getProgramVersion接口获取
                        String versionSign2003 = Resolver.stringResolver((RlpString) rlpList1.getValues().get(5));

                        VotingProposalParam votingProposalParam = VotingProposalParam.builder()
                                .verifier(nodeId2003)
                                .proposalId(proposalID2003)
                                .option(option2003.toString())
                                .programVersion(programVersion2003.toString())
                                .versionSign(versionSign2003)
                                .build();
                        return result.setParam(votingProposalParam);
                    case DECLARE_VERSION: // 2004
                        // 版本声明
                        //声明的节点，只能是验证人/候选人
                        String nodeId2004 = Resolver.stringResolver((RlpString) rlpList1.getValues().get(1));
                        //声明的版本，有rpc的getProgramVersion接口获取
                        BigInteger version2004 =  Resolver.bigIntegerResolver((RlpString) rlpList1.getValues().get(2));
                        //声明的版本签名，有rpc的getProgramVersion接口获取
                        String versionSign2004 = Resolver.stringResolver((RlpString) rlpList1.getValues().get(3));
                        DeclareVersionParam declareVersionParam = DeclareVersionParam.builder()
                                .activeNode(nodeId2004)
                                .version(version2004.intValue())
                                .versionSigns(versionSign2004)
                                .build();
                        return result.setParam(declareVersionParam);
                    case REPORT_VALIDATOR: // 3000
                        // 举报双签
                        //type
                        BigInteger type3000 = Resolver.bigIntegerResolver((RlpString) rlpList1.getValues().get(1));
                        //data
                        String evidence3000 = Resolver.stringResolver((RlpString) rlpList1.getValues().get(2));
                        evidence3000 = new String(Numeric.hexStringToByteArray(evidence3000));

                        ReportValidatorParam reportValidatorParam = ReportValidatorParam.builder()
                                .type(type3000)
                                .data(evidence3000)
                                .build();
                        reportValidatorParam.init();
                        return result.setParam(reportValidatorParam);
                    case CREATE_RESTRICTING: // 4000
                        //创建锁仓计划
                        //锁仓释放到账账户
                        String account4000 = Resolver.stringResolver((RlpString) rlpList1.getValues().get(1));
                        // RestrictingPlan 类型的列表（数组）
                        List<PlanParam> planParams4000 = Resolver.objectResolver((RlpString) rlpList1.getValues().get(2));

                        CreateRestrictingParam createRestrictingParam = CreateRestrictingParam.builder()
                                .account(account4000)
                                .plan(planParams4000)
                                .build();
                        return result.setParam(createRestrictingParam);
				default:
					break;
                }
            }
        } catch (Exception e) {
            log.error("解析交易输入出错:",e);
        }
        result.setTypeEnum(Transaction.TypeEnum.OTHERS);
        return result;
    }

    public static void main ( String[] args ) {
        String date = "0xf905b883820bb801b905b0b905ad7b227072657061726541223a7b2265706f6368223a302c22766965774e756d626572223a302c22626c6f636b48617368223a22307830646336343036343735313835313165666161313432323431653063636561313264613738623437663730323666303432303065343036646165353837323530222c22626c6f636b4e756d626572223a3530302c22626c6f636b496e646578223a302c2276616c69646174654e6f6465223a7b22696e646578223a302c2261646472657373223a22307863666535316438356639393635663664303331653465336363653638386561623763393565393430222c226e6f64654964223a226266633964363537386261623465353130373535353735653437623764313337666366306164306263663130656434643032333634306466623431623139376239663064383031346534376563626534643531663135646235313430303963626461313039656263663062376166653036363030643664343233626237666266222c22626c735075624b6579223a22623437313337393764323936633966653137343964323265623539623033643936393461623839366237313434396230653664616632643165636233613964336436653963323538623337616362326430376661383262636235356365643134346662346230353664366364313932613530393835393631356230393031323864366535363836653834646634373935316531373831363235363237393037303534393735663736653432376461386433326433663330623961353365363066227d2c227369676e6174757265223a2230783336626665363961393834653161313936646331356230656664626664313534656266346335326665346337303835386234343936613566306334306438313063366162666137386161313435333739616234656435313438353231623430623030303030303030303030303030303030303030303030303030303030303030227d2c227072657061726542223a7b2265706f6368223a302c22766965774e756d626572223a302c22626c6f636b48617368223a22307839633531653736323064333831643061653230326435356332393265386364386465346465333731336633323866373135353131373032393133613434386661222c22626c6f636b4e756d626572223a3530302c22626c6f636b496e646578223a302c2276616c69646174654e6f6465223a7b22696e646578223a302c2261646472657373223a22307863666535316438356639393635663664303331653465336363653638386561623763393565393430222c226e6f64654964223a226266633964363537386261623465353130373535353735653437623764313337666366306164306263663130656434643032333634306466623431623139376239663064383031346534376563626534643531663135646235313430303963626461313039656263663062376166653036363030643664343233626237666266222c22626c735075624b6579223a22623437313337393764323936633966653137343964323265623539623033643936393461623839366237313434396230653664616632643165636233613964336436653963323538623337616362326430376661383262636235356365643134346662346230353664366364313932613530393835393631356230393031323864366535363836653834646634373935316531373831363235363237393037303534393735663736653432376461386433326433663330623961353365363066227d2c227369676e6174757265223a2230786465643936346562393235383235646335333837326361626265613739653632333735653636323337303839643830646263303961613964303830303664326162393461383737643035373232303637303966346564363433306366323439373030303030303030303030303030303030303030303030303030303030303030227d7d";
        Result res = TxParamResolver.analysis(date);
        log.info("{}",res.param.toJSONString());
    }
}

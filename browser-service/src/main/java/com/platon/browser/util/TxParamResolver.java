package com.platon.browser.util;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.platon.browser.dto.AnalysisResult;
import com.platon.browser.dto.json.*;
import com.platon.browser.enums.TxTypeEnum;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.util.encoders.Hex;
import org.web3j.platon.contracts.DelegateContract;
import org.web3j.rlp.RlpDecoder;
import org.web3j.rlp.RlpList;
import org.web3j.rlp.RlpString;
import org.web3j.rlp.RlpType;
import org.web3j.utils.Numeric;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * 交易参数解析器
 * User: dongqile
 * Date: 2019/1/9
 * Time: 11:46
 */
public class TxParamResolver {

    @Data
    public static class Result {
        private Object param;
        private TxTypeEnum txTypeEnum;
        public <T> T convert(Class<T> clazz){
            return (T)param;
        }
    }

    public static Result analysis ( String input) {
        Result result = new Result();
        result.txTypeEnum = TxTypeEnum.OTHERS;
        try {
            if (StringUtils.isNotEmpty(input) && !input.equals("0x")) {
                RlpList rlpList = RlpDecoder.decode(Hex.decode(input.replace("0x", "")));
                List <RlpType> rlpTypes = rlpList.getValues();
                RlpList rlpList1 = (RlpList) rlpTypes.get(0);

                RlpString rlpString = (RlpString) rlpList1.getValues().get(0);
                RlpList rlpList2 = RlpDecoder.decode(rlpString.getBytes());
                RlpString rl = (RlpString) rlpList2.getValues().get(0);
                BigInteger txCode = new BigInteger(1, rl.getBytes());

                TxTypeEnum typeEnum = TxTypeEnum.getEnum(txCode.intValue());
                result.txTypeEnum = typeEnum;

                switch (typeEnum) {
                    case CREATEVALIDATOR: // 1000
                        // 发起质押
                        //typ  表示使用账户自由金额还是账户的锁仓金额做质押 0: 自由金额； 1: 锁仓金额
                        BigInteger stakingTyp =  Resolver.bigIntegerResolver((RlpString) rlpList1.getValues().get(1));
                        //用于接受出块奖励和质押奖励的收益账户
                        String addr = Resolver.StringResolver((RlpString) rlpList1.getValues().get(2));
                        //被质押的节点的NodeId
                        String stakNodeId = Resolver.StringResolver((RlpString) rlpList1.getValues().get(3));
                        //外部Id
                        String extrnaId = Resolver.StringResolver((RlpString) rlpList1.getValues().get(4));
                        //被质押节点的名称
                        String stakNodeName = Resolver.StringResolver((RlpString) rlpList1.getValues().get(5));
                        //节点的第三方主页
                        String webSiteAdd = Resolver.StringResolver((RlpString) rlpList1.getValues().get(6));
                        //节点的描述
                        String deteils = Resolver.StringResolver((RlpString) rlpList1.getValues().get(6));
                        //质押的von
                        BigInteger stakingAmount =  Resolver.bigIntegerResolver((RlpString) rlpList1.getValues().get(8));
                        //程序的真实版本，治理rpc获取
                        BigInteger versions =  Resolver.bigIntegerResolver((RlpString) rlpList1.getValues().get(9));

                        CreateValidatorDto createValidatorDto = new CreateValidatorDto();
                        createValidatorDto.init(stakingTyp.intValue(), addr, stakNodeId, extrnaId, stakNodeName,
                                webSiteAdd, deteils, stakingAmount.toString(), String.valueOf(versions));
                        result.param = createValidatorDto;
                        break;
                    case EDITVALIDATOR: // 1001
                        // 修改质押信息
                        //用于接受出块奖励和质押奖励的收益账户
                        String editAddr = Resolver.StringResolver((RlpString) rlpList1.getValues().get(1));
                        //被质押的节点的NodeId
                        String editNodeId = Resolver.StringResolver((RlpString) rlpList1.getValues().get(2));
                        //外部Id
                        String editExtrId = Resolver.StringResolver((RlpString) rlpList1.getValues().get(3));
                        //被质押节点的名称
                        String editNodeName = Resolver.StringResolver((RlpString) rlpList1.getValues().get(4));
                        //节点的第三方主页
                        String editWebSiteAdd = Resolver.StringResolver((RlpString) rlpList1.getValues().get(5));
                        //节点的描述
                        String editDetail = Resolver.StringResolver((RlpString) rlpList1.getValues().get(6));

                        EditValidatorDto editValidatorDto = new EditValidatorDto();
                        editValidatorDto.init(editAddr,editNodeId,editExtrId,editNodeName,editWebSiteAdd,editDetail);
                        result.param = editValidatorDto;
                        break;
                    case INCREASESTAKING: // 1002
                        // 增持质押

                        //被质押的节点的NodeId
                        String increaseNodeId = Resolver.StringResolver((RlpString) rlpList1.getValues().get(1));
                        //typ  表示使用账户自由金额还是账户的锁仓金额做质押 0: 自由金额； 1: 锁仓金额
                        BigInteger increaseTyp =  Resolver.bigIntegerResolver((RlpString) rlpList1.getValues().get(2));
                        //质押的von
                        BigInteger increaseAmount =  Resolver.bigIntegerResolver((RlpString) rlpList1.getValues().get(3));

                        IncreaseStakingDto increaseStakingDto = new IncreaseStakingDto();
                        increaseStakingDto.init(increaseNodeId,increaseTyp.intValue(),increaseAmount.toString(),"");
                        result.param = increaseStakingDto;
                        break;
                    case EXITVALIDATOR: // 1003
                        // 撤销质押

                        //被质押的节点的NodeId
                        String exitNodeId = Resolver.StringResolver((RlpString) rlpList1.getValues().get(1));
                        ExitValidatorDto exitValidatorDto = new ExitValidatorDto();
                        exitValidatorDto.init(exitNodeId,"","");
                        result.param = exitValidatorDto;
                        break;
                    case DELEGATE: // 1004
                        // 发起委托

                        //typ  表示使用账户自由金额还是账户的锁仓金额做质押 0: 自由金额； 1: 锁仓金额
                        BigInteger type =  Resolver.bigIntegerResolver((RlpString) rlpList1.getValues().get(1));
                        //被质押的节点的NodeId
                        String delegateNodeId = Resolver.StringResolver((RlpString) rlpList1.getValues().get(2));
                        //委托的金额
                        BigInteger delegateAmount =  Resolver.bigIntegerResolver((RlpString) rlpList1.getValues().get(3));

                        DelegateDto delegateDto = new DelegateDto();
                        delegateDto.init(type.intValue(),delegateNodeId,delegateAmount.toString(),"","");
                        result.param = delegateDto;
                        break;
                    case UNDELEGATE: // 1005
                        // 减持/撤销委托

                        //代表着某个node的某次质押的唯一标示
                        String stakingBlockNum = Resolver.StringResolver((RlpString) rlpList1.getValues().get(1));
                        //被质押的节点的NodeId
                        String unDelegateNodeId = Resolver.StringResolver((RlpString) rlpList1.getValues().get(2));
                        //减持委托的金额(按照最小单位算，1LAT = 10**18 von)
                        BigInteger unDelegateAmount =  Resolver.bigIntegerResolver((RlpString) rlpList1.getValues().get(3));

                        UnDelegateDto unDelegateDto = new UnDelegateDto();
                        unDelegateDto.init(stakingBlockNum,unDelegateNodeId,unDelegateAmount.toString(),"");
                        result.param = unDelegateDto;
                        break;

                    case CREATEPROPOSALTEXT: // 2000
                        // 提交文本提案

                        //提交提案的验证人
                        String proposalVerifier = Resolver.StringResolver((RlpString) rlpList1.getValues().get(1));
                        //提案URL，长度不超过512
                        String proposalurl = Resolver.StringResolver((RlpString) rlpList1.getValues().get(2));
                        //投票截至快高
                        BigInteger endVotingRound =  Resolver.bigIntegerResolver((RlpString) rlpList1.getValues().get(3));

                        CreateProposalTextDto createProposalTextDto = new CreateProposalTextDto();
                        createProposalTextDto.init(proposalVerifier,"",proposalurl,endVotingRound.intValue());
                        result.param=createProposalTextDto;
                        break;
                    case CREATEPROPOSALUPGRADE: // 2001
                        // 提交升级提案

                        //提交提案的验证人
                        String upgradeVerifier = Resolver.StringResolver((RlpString) rlpList1.getValues().get(1));
                        //提案URL，长度不超过512
                        String upgradelurl = Resolver.StringResolver((RlpString) rlpList1.getValues().get(2));
                        //升级版本
                        BigInteger newVersion =  Resolver.bigIntegerResolver((RlpString) rlpList1.getValues().get(3));
                        //投票截至快高
                        BigInteger upgradeEndVotingRound =  Resolver.bigIntegerResolver((RlpString) rlpList1.getValues().get(4));
                        //生效块高
                        BigInteger upgradeActive =  Resolver.bigIntegerResolver((RlpString) rlpList1.getValues().get(5));

                        CreateProposalUpgradeDto createProposalUpgradeDto = new CreateProposalUpgradeDto();
                        createProposalUpgradeDto.init(upgradeVerifier,"",upgradelurl,upgradeEndVotingRound.intValue(),
                                newVersion.intValue(),upgradeActive.intValue());
                        result.param = createProposalUpgradeDto;
                        break;
                    case CREATEPROPOSALPARAMETER: // 2002
                        // 提交参数提案

                        //提交提案的验证人
                        String paramVerifier = Resolver.StringResolver((RlpString) rlpList1.getValues().get(1));
                        //提案URL，长度不超过512
                        String paramUrl = Resolver.StringResolver((RlpString) rlpList1.getValues().get(2));
                        //投票截至快高
                        BigInteger paramEndVotingRound =  Resolver.bigIntegerResolver((RlpString) rlpList1.getValues().get(3));
                        //参数名称
                        String paramName = Resolver.StringResolver((RlpString) rlpList1.getValues().get(4));
                        //当前值
                        String currentValue = Resolver.StringResolver((RlpString) rlpList1.getValues().get(5));
                        //新的值
                        String newValue = Resolver.StringResolver((RlpString) rlpList1.getValues().get(6));

                        CreateProposalParamDto createProposalParamDto = new CreateProposalParamDto();
                        createProposalParamDto.init(paramVerifier,"",paramUrl,paramEndVotingRound.intValue(),paramName,currentValue,newValue);
                        result.param = createProposalParamDto;
                        break;
                    case VOTINGPROPOSAL: // 2003
                        // 给提案投票

                        //投票验证人
                        String voteVerifier = Resolver.StringResolver((RlpString) rlpList1.getValues().get(1));
                        //提案ID
                        String proposalID = Resolver.StringResolver((RlpString) rlpList1.getValues().get(2));
                        //投票选项
                        BigInteger option =  Resolver.bigIntegerResolver((RlpString) rlpList1.getValues().get(3));
                        //节点代码版本，有rpc的getProgramVersion接口获取
                        BigInteger programVersions =  Resolver.bigIntegerResolver((RlpString) rlpList1.getValues().get(4));
                        //代码版本签名，有rpc的getProgramVersion接口获取
                        String versionSign = Resolver.StringResolver((RlpString) rlpList1.getValues().get(5));

                        VotingProposalDto votingProposalDto = new VotingProposalDto();
                        votingProposalDto.init(voteVerifier,proposalID,option.toString());
                        result.param = votingProposalDto;
                        break;
                    case DECLAREVERSION: // 2004
                        // 版本声明

                        //声明的节点，只能是验证人/候选人
                        String activeNode = Resolver.StringResolver((RlpString) rlpList1.getValues().get(1));
                        //声明的版本，有rpc的getProgramVersion接口获取
                        BigInteger version =  Resolver.bigIntegerResolver((RlpString) rlpList1.getValues().get(2));
                        //声明的版本签名，有rpc的getProgramVersion接口获取
                        String versionSigns = Resolver.StringResolver((RlpString) rlpList1.getValues().get(3));
                        DeclareVersionDto declareVersionDto = new DeclareVersionDto();
                        declareVersionDto.init(activeNode,version.intValue());
                        result.param = declareVersionDto;
                        break;
                    case REPORTVALIDATOR: // 3000
                        // 举报双签

                        //data
                        String data = Resolver.StringResolver((RlpString) rlpList1.getValues().get(1));
                        List<EvidencesDto> list = JSONArray.parseArray(data,EvidencesDto.class);

                        ReportValidatorDto reportValidatorDto = new ReportValidatorDto();
                        reportValidatorDto.setData(list);
                        result.param = reportValidatorDto;
                        break;
                    case CREATERESTRICTING: // 4000
                        //创建锁仓计划

                        //锁仓释放到账账户
                        String account = Resolver.StringResolver((RlpString) rlpList1.getValues().get(1));

                        // RestrictingPlan 类型的列表（数组）
                        BigInteger[] arrayList = Resolver.ObjectResolver((RlpString) rlpList1.getValues().get(2));
                        PlanDto planDto = new PlanDto();
                        planDto.setEpoch(arrayList[0].intValue());
                        planDto.setAmount(arrayList[1].toString());
                        List<PlanDto> planDtoList = new ArrayList <>();
                        planDtoList.add(planDto);
                        CreatereStrictingDto createreStrictingDto = new CreatereStrictingDto();
                        createreStrictingDto.setPlan(planDtoList);
                        createreStrictingDto.setAccount(account);
                        result.param = createreStrictingDto;
                        break;
                }
            }
        } catch (Exception e) {
            result.txTypeEnum = TxTypeEnum.OTHERS;
            return result;
        }
        return result;
    }

}

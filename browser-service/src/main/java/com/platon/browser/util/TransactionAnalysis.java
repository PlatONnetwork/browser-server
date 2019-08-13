package com.platon.browser.util;


import com.platon.browser.dto.AnalysisResult;
import com.platon.browser.dto.json.*;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.util.encoders.Hex;
import org.web3j.platon.contracts.DelegateContract;
import org.web3j.rlp.RlpDecoder;
import org.web3j.rlp.RlpList;
import org.web3j.rlp.RlpString;
import org.web3j.rlp.RlpType;
import org.web3j.utils.Numeric;

import java.math.BigInteger;
import java.util.List;

/**
 * User: dongqile
 * Date: 2019/1/9
 * Time: 11:46
 */
public class TransactionAnalysis {

    public static class Result<T> {
        private T result;
        private Class <T> clazz;
    }

    public static Result analysis ( String input ,String blockNumber) throws Exception {
        try {
            AnalysisResult analysisResult = new AnalysisResult();
            if (StringUtils.isNotEmpty(input) && !input.equals("0x")) {
                RlpList rlpList = RlpDecoder.decode(Hex.decode(input.replace("0x", "")));
                List <RlpType> rlpTypes = rlpList.getValues();
                RlpList rlpList1 = (RlpList) rlpTypes.get(0);

                RlpString rlpString = (RlpString) rlpList1.getValues().get(0);
                RlpList rlpList2 = RlpDecoder.decode(rlpString.getBytes());
                RlpString rl = (RlpString) rlpList2.getValues().get(0);
                BigInteger txCode = new BigInteger(1, rl.getBytes());
                analysisResult.setTxCode(txCode.toString());

                switch (Integer.valueOf(analysisResult.getTxCode())) {
                    case 1000:
                        // 发起质押
                        Result <CreateValidatorDto> createStaking = new Result <>();
                        createStaking.clazz = CreateValidatorDto.class;

                        //typ  表示使用账户自由金额还是账户的锁仓金额做质押 0: 自由金额； 1: 锁仓金额
                        RlpString stakingType = (RlpString) rlpList1.getValues().get(1);
                        RlpList stakingTypeList = RlpDecoder.decode(stakingType.getBytes());
                        RlpString typeString = (RlpString) stakingTypeList.getValues().get(0);
                        BigInteger stakingTyp = new BigInteger(1, typeString.getBytes());

                        //用于接受出块奖励和质押奖励的收益账户
                        RlpString stakingAdd = (RlpString) rlpList1.getValues().get(2);
                        RlpList stakingAddList = RlpDecoder.decode(stakingAdd.getBytes());
                        RlpString stakingAddString = (RlpString) stakingAddList.getValues().get(0);
                        String addr = Numeric.toHexString(stakingAddString.getBytes());

                        //被质押的节点的NodeId
                        RlpString stakingNodeId = (RlpString) rlpList1.getValues().get(3);
                        RlpList stakingNodeIdList = RlpDecoder.decode(stakingNodeId.getBytes());
                        RlpString nodeIdString = (RlpString) stakingNodeIdList.getValues().get(0);
                        String stakNodeId = Numeric.toHexString(nodeIdString.getBytes());

                        //外部Id
                        RlpString extrId = (RlpString) rlpList1.getValues().get(4);
                        RlpList extrIdList = RlpDecoder.decode(extrId.getBytes());
                        RlpString extrString = (RlpString) extrIdList.getValues().get(0);
                        String extrnaId = Numeric.toHexString(extrString.getBytes());

                        //被质押节点的名称
                        RlpString nodeName = (RlpString) rlpList1.getValues().get(5);
                        RlpList nodeNameList = RlpDecoder.decode(nodeName.getBytes());
                        RlpString nodeNameString = (RlpString) nodeNameList.getValues().get(0);
                        String stakNodeName = Numeric.toHexString(nodeNameString.getBytes());

                        //节点的第三方主页
                        RlpString webSite = (RlpString) rlpList1.getValues().get(6);
                        RlpList webSiteList = RlpDecoder.decode(webSite.getBytes());
                        RlpString webSitString = (RlpString) webSiteList.getValues().get(0);
                        String webSiteAdd = Numeric.toHexString(webSitString.getBytes());

                        //节点的描述
                        RlpString detail = (RlpString) rlpList1.getValues().get(7);
                        RlpList detailsList = RlpDecoder.decode(detail.getBytes());
                        RlpString dateilString = (RlpString) detailsList.getValues().get(0);
                        String deteils = Numeric.toHexString(dateilString.getBytes());

                        //质押的von
                        RlpString stakAmount = (RlpString) rlpList1.getValues().get(8);
                        RlpList stakingAmountList = RlpDecoder.decode(stakAmount.getBytes());
                        RlpString stakingAmountString = (RlpString) stakingAmountList.getValues().get(0);
                        BigInteger stakingAmount = new BigInteger(1, stakingAmountString.getBytes());

                        //程序的真实版本，治理rpc获取
                        RlpString programVersion = (RlpString) rlpList1.getValues().get(9);
                        RlpList programVersionList = RlpDecoder.decode(programVersion.getBytes());
                        RlpString programVersionString = (RlpString) programVersionList.getValues().get(0);
                        BigInteger versions = new BigInteger(1, programVersionString.getBytes());

                        CreateValidatorDto createValidatorDto = new CreateValidatorDto();
                        createValidatorDto.init(stakingTyp.intValue(), addr, stakNodeId, extrnaId, stakNodeName,
                                webSiteAdd, deteils, stakingAmount.toString(), String.valueOf(versions));
                        createStaking.result = createValidatorDto;
                        break;

                    case 1001:
                        // 修改质押信息
                        Result <EditValidatorDto> editValidatorResult = new Result <>();
                        editValidatorResult.clazz = EditValidatorDto.class;

                        //用于接受出块奖励和质押奖励的收益账户
                        RlpString editValidatorAdd = (RlpString) rlpList1.getValues().get(1);
                        RlpList editValidatorAddList = RlpDecoder.decode(editValidatorAdd.getBytes());
                        RlpString editValidatorAddString = (RlpString) editValidatorAddList.getValues().get(0);
                        String editAddr = Numeric.toHexString(editValidatorAddString.getBytes());

                        //被质押的节点的NodeId
                        RlpString editNodeIds = (RlpString) rlpList1.getValues().get(2);
                        RlpList editNodeIdNodeIdList = RlpDecoder.decode(editNodeIds.getBytes());
                        RlpString editNodeIdString = (RlpString) editNodeIdNodeIdList.getValues().get(0);
                        String editNodeId = Numeric.toHexString(editNodeIdString.getBytes());

                        //外部Id
                        RlpString editExtrIds = (RlpString) rlpList1.getValues().get(3);
                        RlpList editExtrIdList = RlpDecoder.decode(editExtrIds.getBytes());
                        RlpString editExtrIdString = (RlpString) editExtrIdList.getValues().get(0);
                        String editExtrId = Numeric.toHexString(editExtrIdString.getBytes());

                        //被质押节点的名称
                        RlpString editNodeNames = (RlpString) rlpList1.getValues().get(4);
                        RlpList editNodeNameList = RlpDecoder.decode(editNodeNames.getBytes());
                        RlpString editNodeNameString = (RlpString) editNodeNameList.getValues().get(0);
                        String editNodeName = Numeric.toHexString(editNodeNameString.getBytes());

                        //节点的第三方主页
                        RlpString editWebSites = (RlpString) rlpList1.getValues().get(5);
                        RlpList editWebSiteList = RlpDecoder.decode(editWebSites.getBytes());
                        RlpString editWebSiteString = (RlpString) editWebSiteList.getValues().get(0);
                        String editWebSiteAdd = Numeric.toHexString(editWebSiteString.getBytes());

                        //节点的描述
                        RlpString editDetails = (RlpString) rlpList1.getValues().get(6);
                        RlpList editDetailsList = RlpDecoder.decode(editDetails.getBytes());
                        RlpString editDetailsListString = (RlpString) editDetailsList.getValues().get(0);
                        String editDetail = Numeric.toHexString(editDetailsListString.getBytes());

                        EditValidatorDto editValidatorDto = new EditValidatorDto();
                        editValidatorDto.init(editAddr,editNodeId,editExtrId,editNodeName,editWebSiteAdd,editDetail);
                        editValidatorResult.result = editValidatorDto;
                        break;

                    case 1002:
                        // 增持质押
                        Result<IncreaseStakingDto> increaseStakingResult = new Result <>();
                        increaseStakingResult.clazz = IncreaseStakingDto.class;

                        //被质押的节点的NodeId
                        RlpString increaseNodeIds = (RlpString) rlpList1.getValues().get(1);
                        RlpList increaseNodeIdsList = RlpDecoder.decode(increaseNodeIds.getBytes());
                        RlpString increaseNodeIdsString = (RlpString) increaseNodeIdsList.getValues().get(0);
                        String increaseNodeId= Numeric.toHexString(increaseNodeIdsString.getBytes());

                        //typ  表示使用账户自由金额还是账户的锁仓金额做质押 0: 自由金额； 1: 锁仓金额
                        RlpString increaseTypes = (RlpString) rlpList1.getValues().get(2);
                        RlpList increaseTypeList = RlpDecoder.decode(increaseTypes.getBytes());
                        RlpString increaseTypeString = (RlpString) increaseTypeList.getValues().get(0);
                        BigInteger increaseTyp = new BigInteger(1, increaseTypeString.getBytes());

                        //质押的von
                        RlpString increaseAmounts = (RlpString) rlpList1.getValues().get(8);
                        RlpList increaseAmountList = RlpDecoder.decode(increaseAmounts.getBytes());
                        RlpString increaseAmountString = (RlpString) increaseAmountList.getValues().get(0);
                        BigInteger increaseAmount = new BigInteger(1, increaseAmountString.getBytes());

                        IncreaseStakingDto increaseStakingDto = new IncreaseStakingDto();
                        increaseStakingDto.init(increaseNodeId,increaseTyp.intValue(),increaseAmount.toString(),"");
                        increaseStakingResult.result = increaseStakingDto;
                        break;

                    case 1003:
                        // 撤销质押

                        Result<ExitValidatorDto> exitValidatorResult = new Result <>();
                        exitValidatorResult.clazz = ExitValidatorDto.class;

                        //被质押的节点的NodeId
                        RlpString exitNodeIds = (RlpString) rlpList1.getValues().get(1);
                        RlpList exitNodeIdsList = RlpDecoder.decode(exitNodeIds.getBytes());
                        RlpString exitNodeIdsString = (RlpString) exitNodeIdsList.getValues().get(0);
                        String exitNodeId= Numeric.toHexString(exitNodeIdsString.getBytes());

                        ExitValidatorDto exitValidatorDto = new ExitValidatorDto();
                        exitValidatorDto.init(exitNodeId,"","");
                        exitValidatorResult.result = exitValidatorDto;
                        break;

                    case 1004:
                        // 发起委托
                        Result <DelegateDto> result = new Result <>();
                        result.clazz = DelegateDto.class;
                        //typ  表示使用账户自由金额还是账户的锁仓金额做质押 0: 自由金额； 1: 锁仓金额
                        RlpString delegateTypes = (RlpString) rlpList1.getValues().get(1);
                        RlpList delegateTypesList = RlpDecoder.decode(delegateTypes.getBytes());
                        RlpString delegateTypesString = (RlpString) delegateTypesList.getValues().get(0);
                        BigInteger type = new BigInteger(1, delegateTypesString.getBytes());

                        //被质押的节点的NodeId
                        RlpString delegateNodeIds = (RlpString) rlpList1.getValues().get(2);
                        RlpList delegateNodeList = RlpDecoder.decode(delegateNodeIds.getBytes());
                        RlpString delegateNodeIdString = (RlpString) delegateNodeList.getValues().get(0);
                        String delegateNodeId = Numeric.toHexString(delegateNodeIdString.getBytes());

                        //委托的金额
                        RlpString delegateAmounts = (RlpString) rlpList1.getValues().get(3);
                        RlpList delegateAmountList = RlpDecoder.decode(delegateAmounts.getBytes());
                        RlpString delegateAmountString = (RlpString) delegateAmountList.getValues().get(0);
                        BigInteger delegateAmount = new BigInteger(1, delegateAmountString.getBytes());

                        DelegateDto delegateDto = new DelegateDto();
                        delegateDto.init(type.intValue(),delegateNodeId,delegateAmount.toString(),"","");
                        result.result = delegateDto;
                        break;
                    case 1005:
                        // 减持/撤销委托
                        Result<UnDelegateDto> unDelegateResult = new Result <>();
                        unDelegateResult.clazz = UnDelegateDto.class;

                        //代表着某个node的某次质押的唯一标示
                        RlpString withdrewDeleBlocks = (RlpString) rlpList1.getValues().get(1);
                        RlpList withdrewDeleBlockList = RlpDecoder.decode(withdrewDeleBlocks.getBytes());
                        RlpString withdrewDelBlockString = (RlpString) withdrewDeleBlockList.getValues().get(0);
                        String stakingBlockNum = Numeric.toHexString(withdrewDelBlockString.getBytes());

                        //被质押的节点的NodeId
                        RlpString unDelegateNodeIds = (RlpString) rlpList1.getValues().get(2);
                        RlpList unDelegateNodeIdList = RlpDecoder.decode(unDelegateNodeIds.getBytes());
                        RlpString unDelegateNodeIdString = (RlpString) unDelegateNodeIdList.getValues().get(0);
                        String unDelegateNodeId = Numeric.toHexString(unDelegateNodeIdString.getBytes());

                        //减持委托的金额(按照最小单位算，1LAT = 10**18 von)
                        RlpString unDelegateAmounts = (RlpString) rlpList1.getValues().get(3);
                        RlpList unDelegateAmountsList = RlpDecoder.decode(unDelegateAmounts.getBytes());
                        RlpString unDelegateAmountsString = (RlpString) unDelegateAmountsList.getValues().get(0);
                        BigInteger unDelegateAmount = new BigInteger(1, unDelegateAmountsString.getBytes());

                        UnDelegateDto unDelegateDto = new UnDelegateDto();
                        unDelegateDto.init(stakingBlockNum,unDelegateNodeId,unDelegateAmount.toString(),"");
                        unDelegateResult.result = unDelegateDto;
                        break;

                    case 2000:
                        // 提交文本提案
                        Result<CreateProposalTextDto> createProposalTextResult = new Result <>();
                        createProposalTextResult.clazz = CreateProposalTextDto.class;

                        //提交提案的验证人
                        RlpString proposalVerifiers = (RlpString) rlpList1.getValues().get(1);
                        RlpList proposalVerifiersList = RlpDecoder.decode(proposalVerifiers.getBytes());
                        RlpString proposalVerifiersString = (RlpString) proposalVerifiersList.getValues().get(0);
                        String proposalVerifier = Numeric.toHexString(proposalVerifiersString.getBytes());

                        //提案URL，长度不超过512
                        RlpString proposalurls = (RlpString) rlpList1.getValues().get(2);
                        RlpList proposalurlsList = RlpDecoder.decode(proposalurls.getBytes());
                        RlpString proposalurlsString = (RlpString) proposalurlsList.getValues().get(0);
                        String proposalurl = Numeric.toHexString(proposalurlsString.getBytes());

                        //投票截至快高
                        RlpString endVotingRounds = (RlpString) rlpList1.getValues().get(3);
                        RlpList endVotingRoundsList = RlpDecoder.decode(endVotingRounds.getBytes());
                        RlpString endVotingRoundsString = (RlpString) endVotingRoundsList.getValues().get(0);
                        BigInteger endVotingRound = new BigInteger(1, endVotingRoundsString.getBytes());

                        CreateProposalTextDto createProposalTextDto = new CreateProposalTextDto();
                        createProposalTextDto.init(proposalVerifier,"",proposalurl,endVotingRound.intValue());
                        break;

                    case 2001:
                        // 提交升级提案
                        Result<CreateProposalUpgradeDto> createProposalUpgradeResult = new Result <>();
                        createProposalUpgradeResult.clazz = CreateProposalUpgradeDto.class;

                        //提交提案的验证人
                        RlpString upgradeVerifiers = (RlpString) rlpList1.getValues().get(1);
                        RlpList upgradeVerifiersList = RlpDecoder.decode(upgradeVerifiers.getBytes());
                        RlpString upgradeVerifiersString = (RlpString) upgradeVerifiersList.getValues().get(0);
                        String upgradeVerifier = Numeric.toHexString(upgradeVerifiersString.getBytes());


                        //提案URL，长度不超过512
                        RlpString upgradelurls = (RlpString) rlpList1.getValues().get(2);
                        RlpList upgradelurlsList = RlpDecoder.decode(upgradelurls.getBytes());
                        RlpString upgradelurlsString = (RlpString) upgradelurlsList.getValues().get(0);
                        String upgradelurl = Numeric.toHexString(upgradelurlsString.getBytes());

                        //升级版本
                        RlpString newVersions = (RlpString) rlpList1.getValues().get(3);
                        RlpList newVersionsList = RlpDecoder.decode(newVersions.getBytes());
                        RlpString newVersionsString = (RlpString) newVersionsList.getValues().get(0);
                        BigInteger newVersion = new BigInteger(1, newVersionsString.getBytes());

                        //投票截至快高
                        RlpString upgradeEndVotingRounds = (RlpString) rlpList1.getValues().get(4);
                        RlpList upgradeEndVotingRoundsList = RlpDecoder.decode(upgradeEndVotingRounds.getBytes());
                        RlpString upgradeEndVotingRoundsListString = (RlpString) upgradeEndVotingRoundsList.getValues().get(0);
                        BigInteger upgradeEndVotingRound = new BigInteger(1, upgradeEndVotingRoundsListString.getBytes());

                        //生效块高
                        RlpString upgradeActives = (RlpString) rlpList1.getValues().get(5);
                        RlpList upgradeActivesList = RlpDecoder.decode(upgradeActives.getBytes());
                        RlpString upgradeActivesString = (RlpString) upgradeActivesList.getValues().get(0);
                        BigInteger upgradeActive = new BigInteger(1, upgradeActivesString.getBytes());

                        CreateProposalUpgradeDto createProposalUpgradeDto = new CreateProposalUpgradeDto();
                        createProposalUpgradeDto.init(upgradeVerifier,"",upgradelurl,upgradeEndVotingRound.intValue(),
                                newVersion.intValue(),upgradeActive.intValue());
                        createProposalUpgradeResult.result = createProposalUpgradeDto;
                        break;

                    case 2002:
                        // 提交参数提案
                        Result<CreateProposalParamDto> createProposalParamResult = new Result <>();
                        createProposalParamResult.clazz = CreateProposalParamDto.class;

                        //提交提案的验证人
                        RlpString paramVerifiers = (RlpString) rlpList1.getValues().get(1);
                        RlpList paramVerifiersList = RlpDecoder.decode(paramVerifiers.getBytes());
                        RlpString paramVerifiersString = (RlpString) paramVerifiersList.getValues().get(0);
                        String paramVerifier = Numeric.toHexString(paramVerifiersString.getBytes());

                        //提案URL，长度不超过512
                        RlpString paramUrls = (RlpString) rlpList1.getValues().get(2);
                        RlpList paramUrlsList = RlpDecoder.decode(paramUrls.getBytes());
                        RlpString paramUrlsString = (RlpString) paramUrlsList.getValues().get(0);
                        String paramUrl = Numeric.toHexString(paramUrlsString.getBytes());

                        //投票截至快高
                        RlpString paramEndVotingRounds = (RlpString) rlpList1.getValues().get(3);
                        RlpList paramEndVotingRoundsList = RlpDecoder.decode(paramEndVotingRounds.getBytes());
                        RlpString paramEndVotingRoundsString = (RlpString) paramEndVotingRoundsList.getValues().get(0);
                        BigInteger paramEndVotingRound = new BigInteger(1, paramEndVotingRoundsString.getBytes());

                        //参数名称
                        RlpString paramNames = (RlpString) rlpList1.getValues().get(4);
                        RlpList paramNamesList = RlpDecoder.decode(paramNames.getBytes());
                        RlpString paramNamesString = (RlpString) paramNamesList.getValues().get(0);
                        String paramName = Numeric.toHexString(paramNamesString.getBytes());

                        //当前值
                        RlpString currentValues = (RlpString) rlpList1.getValues().get(5);
                        RlpList currentValuesList = RlpDecoder.decode(currentValues.getBytes());
                        RlpString currentValuesString = (RlpString) currentValuesList.getValues().get(0);
                        String currentValue = Numeric.toHexString(currentValuesString.getBytes());

                        //新的值
                        RlpString newValues = (RlpString) rlpList1.getValues().get(5);
                        RlpList newValuesList = RlpDecoder.decode(newValues.getBytes());
                        RlpString newValuesString = (RlpString) newValuesList.getValues().get(0);
                        String newValue = Numeric.toHexString(newValuesString.getBytes());


                        CreateProposalParamDto createProposalParamDto = new CreateProposalParamDto();
                        createProposalParamDto.init(paramVerifier,"",paramUrl,paramEndVotingRound.intValue(),paramName,currentValue,newValue);
                        createProposalParamResult.result = createProposalParamDto;
                        break;

                    case 2003:
                        // 给提案投票
                        break;

                    case 2004:
                        // 版本声明
                        break;

                    case 3000:
                        // 举报双签
                        break;

                    case 4000:
                        //创建锁仓计划
                        break;

                }

            }

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return null;
    }


}

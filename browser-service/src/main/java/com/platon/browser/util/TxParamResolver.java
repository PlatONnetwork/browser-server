package com.platon.browser.util;


import com.platon.browser.dto.CustomTransaction;
import com.platon.browser.param.*;
import lombok.Data;
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
public class TxParamResolver {

    @Data
    public static class Result {
        private Object param;
        private CustomTransaction.TxTypeEnum txTypeEnum;
        @SuppressWarnings("unchecked")
		public <T> T convert(Class<T> clazz){
            return (T)param;
        }
    }

    public static Result analysis ( String input ) {
        Result result = new Result();
        result.txTypeEnum = CustomTransaction.TxTypeEnum.OTHERS;
        try {
            if (StringUtils.isNotEmpty(input) && !input.equals("0x")) {
                RlpList rlpList = RlpDecoder.decode(Hex.decode(input.replace("0x", "")));
                List <RlpType> rlpTypes = rlpList.getValues();
                RlpList rlpList1 = (RlpList) rlpTypes.get(0);

                RlpString rlpString = (RlpString) rlpList1.getValues().get(0);
                RlpList rlpList2 = RlpDecoder.decode(rlpString.getBytes());
                RlpString rl = (RlpString) rlpList2.getValues().get(0);
                BigInteger txCode = new BigInteger(1, rl.getBytes());

                CustomTransaction.TxTypeEnum typeEnum = CustomTransaction.TxTypeEnum.getEnum(txCode.toString());
                result.txTypeEnum = typeEnum;

                switch (typeEnum) {
                    case CREATE_VALIDATOR: // 1000
                        // 发起质押
                        //typ  表示使用账户自由金额还是账户的锁仓金额做质押 0: 自由金额； 1: 锁仓金额
                        BigInteger stakingTyp =  Resolver.bigIntegerResolver((RlpString) rlpList1.getValues().get(1));
                        //用于接受出块奖励和质押奖励的收益账户benefitAddress
                        String addr = Resolver.StringResolver((RlpString) rlpList1.getValues().get(2));
                        //被质押的节点的NodeId
                        String stakNodeId = Resolver.StringResolver((RlpString) rlpList1.getValues().get(3));
                        //外部Id externalId
                        String extrnaIdHex = Resolver.StringResolver((RlpString) rlpList1.getValues().get(4));
                        String extrnaId = new String(Numeric.hexStringToByteArray(extrnaIdHex));

                        //被质押节点的名称 nodeName
                        String hexStakNodeName = Resolver.StringResolver((RlpString) rlpList1.getValues().get(5));
                        String stakNodeName = new String(Numeric.hexStringToByteArray(hexStakNodeName));
                        //节点的第三方主页 website
                        String hexWebSiteAdd = Resolver.StringResolver((RlpString) rlpList1.getValues().get(6));
                        String webSiteAdd = new String(Numeric.hexStringToByteArray(hexWebSiteAdd));

                        //节点的描述 details
                        String hexDeteils = Resolver.StringResolver((RlpString) rlpList1.getValues().get(7));
                        String deteils = new String(Numeric.hexStringToByteArray(hexDeteils));

                        //质押的von amount programVersion
                        BigInteger stakingAmount =  Resolver.bigIntegerResolver((RlpString) rlpList1.getValues().get(8));

                        //程序的真实版本，治理rpc获取
                        BigInteger versions =  Resolver.bigIntegerResolver((RlpString) rlpList1.getValues().get(9));

                        //程序的真实版本签名，治理rpc获取 programVersionSign
                        //String proVersion = Resolver.StringResolver((RlpString) rlpList1.getValues().get(10));

                        CreateValidatorParam createValidatorParam = new CreateValidatorParam();
                        createValidatorParam.init(stakingTyp.intValue(), addr, stakNodeId, extrnaId, stakNodeName,
                                webSiteAdd, deteils, stakingAmount.toString(), String.valueOf(versions));
                        result.param = createValidatorParam;
                        break;
                    case EDIT_VALIDATOR: // 1001
                        // 修改质押信息
                        //用于接受出块奖励和质押奖励的收益账户
                        String editAddr = Resolver.StringResolver((RlpString) rlpList1.getValues().get(1));
                        //被质押的节点的NodeId
                        String editNodeId = Resolver.StringResolver((RlpString) rlpList1.getValues().get(2));
                        //外部Id
                        String editExtrId = Resolver.StringResolver((RlpString) rlpList1.getValues().get(3));
                        //被质押节点的名称
                        String hexEditNodeName = Resolver.StringResolver((RlpString) rlpList1.getValues().get(4));
                        String editNodeName = new String(Numeric.hexStringToByteArray(hexEditNodeName));

                        //节点的第三方主页
                        String hexEditWebSiteAdd = Resolver.StringResolver((RlpString) rlpList1.getValues().get(5));
                        String editWebSiteAdd = new String(Numeric.hexStringToByteArray(hexEditWebSiteAdd));

                        //节点的描述
                        String hexEditDetail = Resolver.StringResolver((RlpString) rlpList1.getValues().get(6));
                        String editDetail = new String(Numeric.hexStringToByteArray(hexEditDetail));


                        EditValidatorParam editValidatorParam = new EditValidatorParam();
                        editValidatorParam.init(editAddr,editNodeId,editExtrId,editNodeName,editWebSiteAdd,editDetail);
                        result.param = editValidatorParam;
                        break;
                    case INCREASE_STAKING: // 1002
                        // 增持质押

                        //被质押的节点的NodeId
                        String increaseNodeId = Resolver.StringResolver((RlpString) rlpList1.getValues().get(1));
                        //typ  表示使用账户自由金额还是账户的锁仓金额做质押 0: 自由金额； 1: 锁仓金额
                        BigInteger increaseTyp =  Resolver.bigIntegerResolver((RlpString) rlpList1.getValues().get(2));
                        //质押的von
                        BigInteger increaseAmount =  Resolver.bigIntegerResolver((RlpString) rlpList1.getValues().get(3));

                        IncreaseStakingParam increaseStakingParam = new IncreaseStakingParam();
                        increaseStakingParam.init(increaseNodeId,increaseTyp.intValue(),increaseAmount.toString(),"");
                        result.param = increaseStakingParam;
                        break;
                    case EXIT_VALIDATOR: // 1003
                        // 撤销质押

                        //被质押的节点的NodeId
                        String exitNodeId = Resolver.StringResolver((RlpString) rlpList1.getValues().get(1));
                        ExitValidatorParam exitValidatorParam = new ExitValidatorParam();
                        exitValidatorParam.init(exitNodeId,"","");
                        result.param = exitValidatorParam;
                        break;
                    case DELEGATE: // 1004
                        // 发起委托

                        //typ  表示使用账户自由金额还是账户的锁仓金额做质押 0: 自由金额； 1: 锁仓金额
                        BigInteger type =  Resolver.bigIntegerResolver((RlpString) rlpList1.getValues().get(1));
                        //被质押的节点的NodeId
                        String delegateNodeId = Resolver.StringResolver((RlpString) rlpList1.getValues().get(2));
                        //委托的金额
                        BigInteger delegateAmount =  Resolver.bigIntegerResolver((RlpString) rlpList1.getValues().get(3));

                        DelegateParam delegateParam = new DelegateParam();
                        delegateParam.init(type.intValue(),delegateNodeId,delegateAmount.toString(),"","");
                        result.param = delegateParam;
                        break;
                    case UN_DELEGATE: // 1005
                        // 减持/撤销委托

                        //代表着某个node的某次质押的唯一标示
                        String stakingBlockNum = Resolver.StringResolver((RlpString) rlpList1.getValues().get(1));
                        //被质押的节点的NodeId
                        String unDelegateNodeId = Resolver.StringResolver((RlpString) rlpList1.getValues().get(2));
                        //减持委托的金额(按照最小单位算，1LAT = 10**18 von)
                        BigInteger unDelegateAmount =  Resolver.bigIntegerResolver((RlpString) rlpList1.getValues().get(3));

                        UnDelegateParam unDelegateParam = new UnDelegateParam();
                        unDelegateParam.init(new BigInteger(stakingBlockNum.replace("0x",""),16).longValue(),unDelegateNodeId,unDelegateAmount.toString());
                        result.param = unDelegateParam;
                        break;

                    case CREATE_PROPOSAL_TEXT: // 2000
                        // 提交文本提案

                        //提交提案的验证人
                        String proposalVerifier = Resolver.StringResolver((RlpString) rlpList1.getValues().get(1));
                        //pIDID
                        String proposalPIDID = Resolver.StringResolver((RlpString) rlpList1.getValues().get(2));
                        String PIDID =  new String(Numeric.hexStringToByteArray(proposalPIDID));
                        CreateProposalTextParam createProposalTextParam = new CreateProposalTextParam();
                        //todo:结束块高待补充，需确认计算方法，参数中为轮数，在此换算后为块高
                        createProposalTextParam.init(proposalVerifier,PIDID);
                        result.param=createProposalTextParam;
                        break;
                    case CREATE_PROPOSAL_UPGRADE: // 2001
                        // 提交升级提案

                        //提交提案的验证人
                        String upgradeVerifier = Resolver.StringResolver((RlpString) rlpList1.getValues().get(1));
                        //pIDID
                        String upgradelpIDID = Resolver.StringResolver((RlpString) rlpList1.getValues().get(2));
                        String upgradelpidid =  new String(Numeric.hexStringToByteArray(upgradelpIDID));
                        //升级版本
                        BigInteger newVersion =  Resolver.bigIntegerResolver((RlpString) rlpList1.getValues().get(3));
                        //投票截止区块高度
                        BigInteger endBlockRound =  Resolver.bigIntegerResolver((RlpString) rlpList1.getValues().get(4));
                        //结束轮转换结束区块高度

                        CreateProposalUpgradeParam createProposalUpgradeParam = new CreateProposalUpgradeParam();
                        createProposalUpgradeParam.init(upgradeVerifier,upgradelpidid,new BigDecimal(endBlockRound),newVersion.intValue());
                        result.param = createProposalUpgradeParam;
                        break;
                    case CANCEL_PROPOSAL: // 2005
                        // 提交取消提案

                        //提交提案的验证人
                        String cancelVerifier = Resolver.StringResolver((RlpString) rlpList1.getValues().get(1));
                        //本提案的pIDID
                        String cancelpIDID = Resolver.StringResolver((RlpString) rlpList1.getValues().get(2));
                        String cancelpidid =  new String(Numeric.hexStringToByteArray(cancelpIDID));
                        //投票截止区块高度
                        BigInteger cancelEndBlockRound =  Resolver.bigIntegerResolver((RlpString) rlpList1.getValues().get(3));
                        //被取消的pIDID
                        String canceledProposalID =  Resolver.StringResolver((RlpString) rlpList1.getValues().get(4));


                        CancelProposalParam cancelProposalParam = new CancelProposalParam();
                        cancelProposalParam.init(cancelVerifier,cancelpidid,new BigDecimal(cancelEndBlockRound),canceledProposalID);
                        result.param = cancelProposalParam;
                        break;
                    case VOTING_PROPOSAL: // 2003
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

                        VotingProposalParam votingProposalParam = new VotingProposalParam();
                        votingProposalParam.init(voteVerifier,proposalID,option.toString(),programVersions.toString(),versionSign);
                        result.param = votingProposalParam;
                        break;
                    case DECLARE_VERSION: // 2004
                        // 版本声明

                        //声明的节点，只能是验证人/候选人
                        String activeNode = Resolver.StringResolver((RlpString) rlpList1.getValues().get(1));
                        //声明的版本，有rpc的getProgramVersion接口获取
                        BigInteger version =  Resolver.bigIntegerResolver((RlpString) rlpList1.getValues().get(2));
                        //声明的版本签名，有rpc的getProgramVersion接口获取
                        String versionSigns = Resolver.StringResolver((RlpString) rlpList1.getValues().get(3));
                        DeclareVersionParam declareVersionParam = new DeclareVersionParam();
                        declareVersionParam.init(activeNode,version.intValue(),versionSigns);
                        result.param = declareVersionParam;
                        break;
                    case REPORT_VALIDATOR: // 3000
                        // 举报双签
                        //type
                        BigInteger dupType = Resolver.bigIntegerResolver((RlpString) rlpList1.getValues().get(1));
                        //data
                        String data = Resolver.StringResolver((RlpString) rlpList1.getValues().get(2));
                        String evdences = new String(Numeric.hexStringToByteArray(data));

                        ReportValidatorParam reportValidatorParam = new ReportValidatorParam();
                        reportValidatorParam.init(dupType,evdences);
                        result.param = reportValidatorParam;
                        break;
                    case CREATE_RESTRICTING: // 4000
                        //创建锁仓计划

                        //锁仓释放到账账户
                        String account = Resolver.StringResolver((RlpString) rlpList1.getValues().get(1));

                        // RestrictingPlan 类型的列表（数组）
                        List<PlanParam> planDtoList = Resolver.ObjectResolver((RlpString) rlpList1.getValues().get(2));

                        CreateRestrictingParam createRestrictingParam = new CreateRestrictingParam();
                        createRestrictingParam.setPlan(planDtoList);
                        createRestrictingParam.setAccount(account);
                        result.param = createRestrictingParam;
                        break;
				default:
					break;
                }
            }
        } catch (Exception e) {
            result.txTypeEnum = CustomTransaction.TxTypeEnum.OTHERS;
            e.printStackTrace();
            return result;
        }
        return result;
    }

/*    public static void main ( String[] args ) {
        TxParamResolver.analysis("0xf905c483820bb801b905bcb905b97b22707265706172655f61223a7b2265706f6368223a302c22766965775f6e756d626572223a302c22626c6f636b5f68617368223a22307864303032363463623237393032323761306366313064303132663533326530663061393266316333626333346533343030323730663564323566333633303737222c22626c6f636b5f6e756d626572223a3530302c22626c6f636b5f696e646578223a302c2276616c69646174655f6e6f6465223a7b22696e646578223a302c2261646472657373223a22307866613039373464346636656333343932303663316637636561386464613436356362643137373537222c224e6f64654944223a223336376634663363343538363031303631616563356633643065636639393365653265346539666235373536626438376538383332383461363861386430356466316166646262343762376436633035303466343735613138326332653933396433326235636336396237303733633238346264313037303065626364393434222c22626c735075624b6579223a22366635313634383034313631353764636537636633626563326533303537323565393433366331343763316666353837613966653132656461646662656239643538366636363830333866643435356162313833343163353135333639333037646235653161396365343138613135626132306236613038663536643635333366623031373764633230326665396166643639346536626235303433323933356165316430323364373265613531636636653838393830346137383539333136227d2c227369676e6174757265223a2230786539626334316266326265353265633938333762643532646665636637336361613335623638623837396433356630386664623361346333656238376561636132383262616561393365353134616531643338373865353637313832353539333030303030303030303030303030303030303030303030303030303030303030227d2c22707265706172655f62223a7b2265706f6368223a302c22766965775f6e756d626572223a302c22626c6f636b5f68617368223a22307861643836346466356362663532343762383035356264306635346439393131383965393332356165313331626435633830323062313461613933363938653532222c22626c6f636b5f6e756d626572223a3530302c22626c6f636b5f696e646578223a302c2276616c69646174655f6e6f6465223a7b22696e646578223a302c2261646472657373223a22307866613039373464346636656333343932303663316637636561386464613436356362643137373537222c224e6f64654944223a223336376634663363343538363031303631616563356633643065636639393365653265346539666235373536626438376538383332383461363861386430356466316166646262343762376436633035303466343735613138326332653933396433326235636336396237303733633238346264313037303065626364393434222c22626c735075624b6579223a22366635313634383034313631353764636537636633626563326533303537323565393433366331343763316666353837613966653132656461646662656239643538366636363830333866643435356162313833343163353135333639333037646235653161396365343138613135626132306236613038663536643635333366623031373764633230326665396166643639346536626235303433323933356165316430323364373265613531636636653838393830346137383539333136227d2c227369676e6174757265223a2230783661643562303363666463333035393335343235656461396332313664356564366333393330343837653237616461346137643832313463613336393861366333616135653235313064613132373933333764663939383365306364396230393030303030303030303030303030303030303030303030303030303030303030227d7d");
        ReportValidatorParam reportValidatorParam = new ReportValidatorParam();

        JSONObject jsonObject = JSON.parseObject("{\"prepare_a\":{\"epoch\":0,\"view_number\":0,\"block_hash\":\"0xd00264cb2790227a0cf10d012f532e0f0a92f1c3bc34e3400270f5d25f363077\",\"block_number\":500,\"block_index\":0,\"validate_node\":{\"index\":0,\"address\":\"0xfa0974d4f6ec349206c1f7cea8dda465cbd17757\",\"NodeID\":\"367f4f3c458601061aec5f3d0ecf993ee2e4e9fb5756bd87e883284a68a8d05df1afdbb47b7d6c0504f475a182c2e939d32b5cc69b7073c284bd10700ebcd944\",\"blsPubKey\":\"6f516480416157dce7cf3bec2e305725e9436c147c1ff587a9fe12edadfbeb9d586f668038fd455ab18341c515369307db5e1a9ce418a15ba20b6a08f56d6533fb0177dc202fe9afd694e6bb50432935ae1d023d72ea51cf6e889804a7859316\"},\"signature\":\"0xe9bc41bf2be52ec9837bd52dfecf73caa35b68b879d35f08fdb3a4c3eb87eaca282baea93e514ae1d3878e567182559300000000000000000000000000000000\"},\"prepare_b\":{\"epoch\":0,\"view_number\":0,\"block_hash\":\"0xad864df5cbf5247b8055bd0f54d991189e9325ae131bd5c8020b14aa93698e52\",\"block_number\":500,\"block_index\":0,\"validate_node\":{\"index\":0,\"address\":\"0xfa0974d4f6ec349206c1f7cea8dda465cbd17757\",\"NodeID\":\"367f4f3c458601061aec5f3d0ecf993ee2e4e9fb5756bd87e883284a68a8d05df1afdbb47b7d6c0504f475a182c2e939d32b5cc69b7073c284bd10700ebcd944\",\"blsPubKey\":\"6f516480416157dce7cf3bec2e305725e9436c147c1ff587a9fe12edadfbeb9d586f668038fd455ab18341c515369307db5e1a9ce418a15ba20b6a08f56d6533fb0177dc202fe9afd694e6bb50432935ae1d023d72ea51cf6e889804a7859316\"},\"signature\":\"0x6ad5b03cfdc305935425eda9c216d5ed6c3930487e27ada4a7d8214ca3698a6c3aa5e2510da1279337df9983e0cd9b0900000000000000000000000000000000\"}}");
        JSONObject base = new JSONObject();
        switch (reportValidatorParam.getType().intValue()){
            case 1:
                JSONObject a =  jsonObject.getJSONObject("prepare_a");
                base =  a.getJSONObject("validate_node");
            case 2:
                JSONObject b =  jsonObject.getJSONObject("vote_a");
            case 3:
                JSONObject c =  jsonObject.getJSONObject("view_a");
        }
        String nodeId = base.getString("NodeID");
        System.out.println(nodeId);

    }*/
}

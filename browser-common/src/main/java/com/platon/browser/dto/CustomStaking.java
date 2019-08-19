package com.platon.browser.dto;

import com.platon.browser.dao.entity.Staking;
import com.platon.browser.param.CreateValidatorParam;
import com.platon.browser.param.EditValidatorParam;
import com.platon.browser.param.ExitValidatorParam;
import com.platon.browser.param.IncreaseStakingParam;
import com.platon.browser.utils.HexTool;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: Chendongming
 * @Date: 2019/8/13 14:36
 * @Description:
 */
@Data
public class CustomStaking extends Staking {
    // <质押块高-质押记录> 映射
    private Map<String, CustomDelegation> delegations = new HashMap<>();

    public CustomStaking() {
        Date date = new Date();
        this.setCreateTime(date);
        this.setUpdateTime(date);
        this.setJoinTime(date);
        /** 初始化默认值 **/
        // 质押金额(犹豫期金额)
        this.setStakingHas("0");
        // 质押金额(锁定金额)
        this.setStakingLocked("0");
        // 委托交易总金额(犹豫期金额)
        this.setStatDelegateHas("0");
        // 委托交易总金额(锁定期金额)
        this.setStatDelegateLocked("0");
        // 质押金额(退回中金额)
        this.setStakingReduction("0");
        // 委托交易总金额(退回中金额)
        this.setStatDelegateReduction("0");
        // 节点名称(质押节点名称)
        this.setStakingName("Unknown");
        // 节点头像(关联external_id，第三方软件获取)
        this.setStakingIcon("");
        // 预计年化率
        this.setExpectedIncome("0");
        // 出块奖励
        this.setBlockRewardValue("0");
        // 上个结算周期出块奖励
        this.setPreSetBlockRewardValue("0");
        // 程序版本
        this.setProgramVersion("0");
        // 质押奖励
        this.setStakingRewardValue("0");
        // 结算周期标识
        this.setStakingReductionEpoch(0);
        // 委托交易总数(关联的委托交易总数)
        this.setStatDelegateQty(0);
        // 进入共识验证论次数
        this.setStatVerifierTime(0);
        // 上个共识周期出块数
        this.setPreConsBlockQty(0l);
        // 当前共识周期出块数
        this.setCurConsBlockQty(0l);
        // 节点状态 1：候选中 2：退出中 3：已退出
        this.setStatus(StatusEnum.CANDIDATE.code);
        //是否结算周期验证人
        this.setIsSetting(YesNoEnum.NO.code);
        //是否共识周期验证人
        this.setIsConsensus(YesNoEnum.NO.code);
        // 是否为链初始化时内置的候选人
        this.setIsInit(YesNoEnum.NO.code);
    }

    // staking与delegation的关联键
    public String getStakingMapKey(){
        return this.getNodeId()+this.getStakingBlockNum();
    }

    /**
     * 使用节点信息更新质押信息
     * @param node
     */
    public void updateWithNode(org.web3j.platon.bean.Node node){
        BeanUtils.copyProperties(node,this);
        // 质押区块高度
        if(node.getStakingBlockNum()!=null) this.setStakingBlockNum(node.getStakingBlockNum().longValue());
        // 质押节点地址
        this.setNodeId(HexTool.prefix(this.getNodeId()));
        // 发起质押交易的索引
        if(node.getStakingTxIndex()!=null) this.setStakingTxIndex(node.getStakingTxIndex().intValue());
        // 发起质押的账户地址
        this.setStakingAddr(node.getStakingAddress());
        // 第三方社交软件关联id
        this.setExternalId(node.getExternalId());
        // 收益地址
        this.setDenefitAddr(node.getBenifitAddress());
        // 节点状态 1：候选中 2：退出中 3：已退出
        if(node.getStatus()!=null) this.setStatus(node.getStatus().intValue());
        // 节点名称(质押节点名称)
        this.setStakingName(node.getNodeName());
        // 节点的第三方主页
        this.setWebSite(node.getWebsite());
    }

    /**
     * 使用交易信息更新质押信息
     * @param tx
     */
    public void updateWithCustomTransaction(CustomTransaction tx){
        // 质押块号
        this.setStakingBlockNum(tx.getBlockNumber());
        // 质押交易索引
        this.setStakingTxIndex(tx.getTransactionIndex());
        // 发起质押的账户地址
        this.setStakingAddr(tx.getFrom());
        /**********从交易入参中取相关信息***********/
        CreateValidatorParam param = tx.getTxParam(CreateValidatorParam.class);
        this.setNodeId(param.getNodeId());
        this.setStakingName(param.getNodeName());
        this.setDenefitAddr(param.getBenefitAddress());
        this.setStakingHas(param.getAmount());
        this.setWebSite(param.getWebsite());
        this.setProgramVersion(param.getProgramVersion());
        this.setDetails(param.getDetails());
        this.setExternalId(param.getExternalId());
        this.setJoinTime(tx.getTimestamp());
    }

    /**
     * 使用编辑验证人信息参数更新质押记录
     * @param param
     */
    public void updateWithEditValidatorParam(EditValidatorParam param) {
        this.setExternalId(param.getExternalId());
        this.setStakingName(param.getNodeName());
        this.setDetails(param.getDetails());
        this.setDenefitAddr(param.getBenefitAddress());
        this.setWebSite(param.getWebsite());
        this.setUpdateTime(new Date());
    }

    /**
     * 使用增持验证人参数更新质押记录
     * @param param
     */
    public void updateWithIncreaseStakingParam(IncreaseStakingParam param) {
        if(StringUtils.isNotBlank(param.getAmount())){
            BigInteger addAmount = new BigInteger(param.getAmount());
            BigInteger stakingHas = new BigInteger(this.getStakingHas());
            BigInteger total = stakingHas.add(addAmount);
            this.setStakingHas(total.toString());
        }
    }

    /**
     * 使用退出验证人参数更新质押记录
     * @param param
     */
    public void updateWithExitValidatorParam(ExitValidatorParam param) {
        this.setStakingHas("0");
        this.setLeaveTime(new Date());
        BigInteger stakingLocked = new BigInteger(getStakingLocked());
        if(stakingLocked.compareTo(BigInteger.ZERO)>0){
            this.setStakingReduction(getStakingLocked());
            this.setStakingLocked("0");
            this.setStatus(StatusEnum.EXITING.code);
        }else {
            this.setStakingLocked("0");
            this.setStatus(StatusEnum.EXITED.code);
        }
    }

    public enum StatusEnum{
        CANDIDATE(1, "候选中"),
        EXITING(2, "退出中"),
        EXITED(3, "已退出")
        ;
        public int code;
        public String desc;
        StatusEnum(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }
        public int getCode(){return code;}
        public String getDesc(){return desc;}
        private static Map<Integer, StatusEnum> ENUMS = new HashMap<>();
        static {
            Arrays.asList(StatusEnum.values()).forEach(en->ENUMS.put(en.code,en));}
        public static StatusEnum getEnum(Integer code){
            return ENUMS.get(code);
        }
        public static boolean contains(int code){return ENUMS.containsKey(code);}
        public static boolean contains(StatusEnum en){return ENUMS.containsValue(en);}
    }

    public enum YesNoEnum{
        YES(1, "是"),
        NO(2, "否")
        ;
        public int code;
        public String desc;
        YesNoEnum(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }
        public int getCode(){return code;}
        public String getDesc(){return desc;}
        private static Map<Integer, YesNoEnum> ENUMS = new HashMap<>();
        static {Arrays.asList(YesNoEnum.values()).forEach(en->ENUMS.put(en.code,en));}
        public static YesNoEnum getEnum(Integer code){
            return ENUMS.get(code);
        }
        public static boolean contains(int code){return ENUMS.containsKey(code);}
        public static boolean contains(YesNoEnum en){return ENUMS.containsValue(en);}
    }


}

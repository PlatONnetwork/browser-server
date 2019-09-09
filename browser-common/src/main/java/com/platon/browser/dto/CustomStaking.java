package com.platon.browser.dto;

import com.platon.browser.dao.entity.Staking;
import com.platon.browser.dao.entity.StakingKey;
import com.platon.browser.param.CreateValidatorParam;
import com.platon.browser.param.EditValidatorParam;
import com.platon.browser.param.ExitValidatorParam;
import com.platon.browser.param.IncreaseStakingParam;
import com.platon.browser.utils.HexTool;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

/**
 * @Auther: Chendongming
 * @Date: 2019/8/13 14:36
 * @Description: 质押实体扩展类
 */
@Data
public class CustomStaking extends Staking {
    // <委托交易发送者地址-质押记录> 映射
    private Map<String, CustomDelegation> delegations = new HashMap<>();

    public CustomStaking() {
        super();
        Date date = new Date();
        this.setCreateTime(date);
        this.setUpdateTime(date);
        this.setJoinTime(date);
        /* 初始化默认值 */
        // 质押金额(犹豫期金额)
        this.setStakingHas(BigInteger.ZERO.toString());
        // 质押金额(锁定金额)
        this.setStakingLocked(BigInteger.ZERO.toString());
        // 委托交易总金额(犹豫期金额)
        this.setStatDelegateHas(BigInteger.ZERO.toString());
        // 委托交易总金额(锁定期金额)
        this.setStatDelegateLocked(BigInteger.ZERO.toString());
        // 质押金额(退回中金额)
        this.setStakingReduction(BigInteger.ZERO.toString());
        // 委托交易总金额(退回中金额)
        this.setStatDelegateReduction(BigInteger.ZERO.toString());
        // 节点名称(质押节点名称)
        this.setStakingName("Unknown");
        // 节点头像(关联external_id，第三方软件获取)
        this.setStakingIcon("");
        // 预计年化率
        this.setExpectedIncome(BigInteger.ZERO.toString());
        // 出块奖励
        this.setBlockRewardValue(BigInteger.ZERO.toString());
        // 程序版本
        this.setProgramVersion(BigInteger.ZERO.toString());
        // 质押奖励
        this.setStakingRewardValue(BigInteger.ZERO.toString());
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

    /********把字符串类数值转换为大浮点数的便捷方法********/
    public BigDecimal decimalStakingLocked(){return new BigDecimal(this.getStakingLocked());}
    public BigDecimal decimalStakingHas(){return new BigDecimal(this.getStakingHas());}
    public BigDecimal decimalBlockRewardValue(){return new BigDecimal(this.getBlockRewardValue());}
    public BigDecimal decimalStakingRewardValue(){return new BigDecimal(this.getStakingRewardValue());}
    public BigDecimal decimalStakingReduction(){return new BigDecimal(this.getStakingReduction());}
    public BigDecimal decimalStatDelegateHas(){return new BigDecimal(this.getStatDelegateHas());}
    public BigDecimal decimalStatDelegateLocked(){return new BigDecimal(this.getStatDelegateLocked());}
    public BigDecimal decimalStatDelegateReduction(){return new BigDecimal(this.getStatDelegateReduction());}
    /********把字符串类数值转换为大整数的便捷方法********/
    public BigInteger integerStakingLocked(){return new BigInteger(this.getStakingLocked());}
    public BigInteger integerStakingHas(){return new BigInteger(this.getStakingHas());}
    public BigInteger integerBlockRewardValue(){return new BigInteger(this.getBlockRewardValue());}
    public BigInteger integerStakingRewardValue(){return new BigInteger(this.getStakingRewardValue());}
    public BigInteger integerStakingReduction(){return new BigInteger(this.getStakingReduction());}
    public BigInteger integerStatDelegateHas(){return new BigInteger(this.getStatDelegateHas());}
    public BigInteger integerStatDelegateLocked(){return new BigInteger(this.getStatDelegateLocked());}
    public BigInteger integerStatDelegateReduction(){return new BigInteger(this.getStatDelegateReduction());}
    public BigInteger integerStakingBlockNum(){return BigInteger.valueOf(this.getStakingBlockNum());}

    /**
     * 使用节点信息更新质押信息
     * @param node
     */
    public void updateWithNode(org.web3j.platon.bean.Node node){
        // 质押区块高度
        if(node.getStakingBlockNum()!=null) this.setStakingBlockNum(node.getStakingBlockNum().longValue());
        // 质押节点地址
        this.setNodeId(HexTool.prefix(node.getNodeId()));
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
        this.setDetails(node.getDetails());
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
     * @param curSettingEpoch
     */
    public void updateWithExitValidatorParam(ExitValidatorParam param, BigInteger curSettingEpoch) {
        this.setStakingHas(BigInteger.ZERO.toString());
        this.setLeaveTime(new Date());
        BigInteger stakingLocked = new BigInteger(getStakingLocked());
        if(stakingLocked.compareTo(BigInteger.ZERO)>0){
            this.setStakingReduction(getStakingLocked());
            this.setStakingLocked(BigInteger.ZERO.toString());
            this.setStakingReductionEpoch(curSettingEpoch.intValue());
            this.setStatus(StatusEnum.EXITING.code);
        }else {
            this.setStakingLocked(BigInteger.ZERO.toString());
            this.setStatus(StatusEnum.EXITED.code);
        }
    }

    public StatusEnum getStatusEnum(){
        return StatusEnum.getEnum(this.getStatus());
    }


    /**
     * 质押状态类型枚举类：
     *  1.候选中
     *  2.退出中
     *  3.已退出
     */
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
    /**
     * 质押节点——是否共识周期验证人类型/是否结算周期验证人类型枚举类：
     *  1.是
     *  2.否
     */
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

    @Override
    public boolean equals ( Object o ) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StakingKey that = (StakingKey) o;
        return Objects.equals(this.getStakingBlockNum(), that.getStakingBlockNum()) &&
                Objects.equals(this.getNodeId(), that.getNodeId());
    }

    @Override
    public int hashCode () {
        return Objects.hash(this.getStakingBlockNum(), this.getNodeId());
    }
}

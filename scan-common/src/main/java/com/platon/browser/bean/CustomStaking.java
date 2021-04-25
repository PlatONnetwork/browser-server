package com.platon.browser.bean;

import com.platon.browser.dao.entity.Staking;
import com.platon.browser.dao.entity.StakingKey;
import com.platon.browser.utils.HexUtil;
import com.platon.browser.utils.ChainVersionUtil;
import com.platon.contracts.ppos.dto.resp.Node;
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
    private BigDecimal slashAmount;
    public CustomStaking() {
        super();
        Date date = new Date();
        this.setCreateTime(date);
        this.setUpdateTime(date);
        this.setJoinTime(date);
        /* 初始化默认值 */
        // 质押金额(犹豫期金额)
        this.setStakingHes(BigDecimal.ZERO);
//        // 质押金额(锁定金额)
        this.setStakingLocked(BigDecimal.ZERO);
//        // 委托交易总金额(犹豫期金额)
        this.setStatDelegateHes(BigDecimal.ZERO);
//        // 委托交易总金额(锁定期金额)
        this.setStatDelegateLocked(BigDecimal.ZERO);
        setStatDelegateReleased(BigDecimal.ZERO);
//        // 质押金额(退回中金额)
        this.setStakingReduction(BigDecimal.ZERO);
//        // 节点名称(质押节点名称)
        this.setNodeName("Unknown");
//        // 节点头像(关联external_id，第三方软件获取)
        this.setNodeIcon("");
//        // 预计年化率
        this.setAnnualizedRate(0.0);
//        // 出块奖励
        this.setBlockRewardValue(BigDecimal.ZERO);
//        // 程序版本
        this.setProgramVersion(BigInteger.ZERO.toString());
//        // 质押奖励
        this.setStakingRewardValue(BigDecimal.ZERO);
//        // 结算周期标识
        this.setStakingReductionEpoch(0);
//        // 进入共识验证论次数
        this.setStakingReductionEpoch(0);
//        // 上个共识周期出块数
        this.setPreConsBlockQty(0l);
//        // 当前共识周期出块数
        this.setCurConsBlockQty(0l);
//        // 节点状态 1：候选中 2：退出中 3：已退出
        this.setStatus(StatusEnum.CANDIDATE.code);
//        //是否结算周期验证人
        this.setIsSettle(YesNoEnum.NO.code);
//        //是否共识周期验证人
        this.setIsConsensus(YesNoEnum.NO.code);
//        // 是否为链初始化时内置的候选人
        this.setIsInit(YesNoEnum.NO.code);
//        //节点质押期间手续费
        this.setFeeRewardValue(BigDecimal.ZERO);
    }

    /**
     * 使用节点信息更新质押信息
     * @param verifier
     */
    public void updateWithVerifier(Node verifier){
        // 质押区块高度
        if(verifier.getStakingBlockNum()!=null) this.setStakingBlockNum(verifier.getStakingBlockNum().longValue());
        // 质押节点地址
        this.setNodeId(HexUtil.prefix(verifier.getNodeId()));
        // 发起质押交易的索引
        if(verifier.getStakingTxIndex()!=null) this.setStakingTxIndex(verifier.getStakingTxIndex().intValue());
        // 发起质押的账户地址
        this.setStakingAddr(verifier.getStakingAddress());
        // 第三方社交软件关联id
        this.setExternalId(verifier.getExternalId());
        // 收益地址
        this.setBenefitAddr(verifier.getBenifitAddress());
//        // 节点状态 1：候选中 2：退出中 3：已退出
        if(verifier.getStatus()!=null) this.setStatus(verifier.getStatus().intValue());
//        // 节点名称(质押节点名称)
        this.setNodeName(StringUtils.isBlank(verifier.getNodeName())?this.getNodeName():verifier.getNodeName());
        // 节点的第三方主页
        this.setWebSite(verifier.getWebsite());
        this.setDetails(verifier.getDetails());

        // 程序版本号
        BigInteger programVersion=verifier.getProgramVersion();
        BigInteger bigVersion = ChainVersionUtil.toBigVersion(programVersion);
        this.setProgramVersion(programVersion.toString());
        this.setBigVersion(bigVersion.toString());
    }

    /**
     * 使用节点信息更新质押信息
     * @param candidate
     */
    public void updateWithCandidate(Node candidate){
        // 设置节点名称
        String nodeName = candidate.getNodeName();
        if(StringUtils.isNotBlank(nodeName)) setNodeName(nodeName);
        // 设置程序版本号
        String programVersion=candidate.getProgramVersion().toString();
        if(StringUtils.isNotBlank(programVersion)){
            setProgramVersion(programVersion);
            BigInteger bigVersion = ChainVersionUtil.toBigVersion(candidate.getProgramVersion());
            setBigVersion(bigVersion.toString());
        }
        // 设置外部ID
        String externalId = candidate.getExternalId();
        if(StringUtils.isNotBlank(externalId)) setExternalId(externalId);
        // 设置收益地址
        String benefitAddr = candidate.getBenifitAddress();
        if(StringUtils.isNotBlank(benefitAddr)) setBenefitAddr(benefitAddr);
        // 设置详情
        String details = candidate.getDetails();
        if(StringUtils.isNotBlank(details)) setDetails(details);
        // 设置官网
        String website = candidate.getWebsite();
        if(StringUtils.isNotBlank(website)) setWebSite(website);
        // 设置质押金额
        if(candidate.getShares()!=null&&candidate.getShares().compareTo(BigInteger.ZERO)>0){
            setStakingLocked(new BigDecimal(candidate.getShares()));
        }
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
        EXITED(3, "已退出"),
        LOCKED(4, "已锁定"),
        ;
        private int code;
        private String desc;
        StatusEnum(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }
        public int getCode(){return code;}
        public String getDesc(){return desc;}
        private static final Map<Integer, StatusEnum> ENUMS = new HashMap<>();
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
        private int code;
        private String desc;
        YesNoEnum(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }
        public int getCode(){return code;}
        public String getDesc(){return desc;}
        private static final Map<Integer, YesNoEnum> ENUMS = new HashMap<>();
        static {Arrays.asList(YesNoEnum.values()).forEach(en->ENUMS.put(en.code,en));}
        public static YesNoEnum getEnum(Integer code){
            return ENUMS.get(code);
        }
        public static boolean contains(int code){return ENUMS.containsKey(code);}
        public static boolean contains(YesNoEnum en){return ENUMS.containsValue(en);}
    }

    public enum ExceptionStatusEnum {
        NORMAL(1, "正常"),
        LOW_RATE(2, "低出块异常"),
        MULTI_SIGN(3, "双签异常"),
        LOW_RATE_SLASHED(4, "因低出块率被惩罚"),
        MULTI_SIGN_SLASHED(5, "因双签被处罚");

        private int code;
        private String desc;

        ExceptionStatusEnum(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public int getCode() {
            return code;
        }

        public String getDesc() {
            return desc;
        }
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

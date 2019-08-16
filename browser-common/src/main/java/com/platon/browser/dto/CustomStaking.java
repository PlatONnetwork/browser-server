package com.platon.browser.dto;

import com.platon.browser.dao.entity.Staking;
import com.platon.browser.param.CreateValidatorParam;
import com.platon.browser.utils.HexTool;
import lombok.Data;
import org.springframework.beans.BeanUtils;

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

    public CustomStaking() {
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
        this.setStatus(1);

    }

    // <质押块高-质押记录> 映射
    private Map<String, CustomDelegation> delegations = new HashMap<>();

    // staking与delegation的关联键
    public String getStakingMapKey(){
        return this.getNodeId()+this.getStakingBlockNum();
    }


    public void initWithNode(org.web3j.platon.bean.Node initData){
        Date date = new Date();
        this.setCreateTime(date);
        this.setUpdateTime(date);
        BeanUtils.copyProperties(initData,this);
        // 质押区块高度
        if(initData.getStakingBlockNum()!=null) this.setStakingBlockNum(initData.getStakingBlockNum().longValue());
        // 质押节点地址
        this.setNodeId(HexTool.prefix(this.getNodeId()));
        // 发起质押交易的索引
        if(initData.getStakingTxIndex()!=null) this.setStakingTxIndex(initData.getStakingTxIndex().intValue());
        // 发起质押的账户地址
        this.setStakingAddr(initData.getStakingAddress());
        // 第三方社交软件关联id
        this.setExternalId(initData.getExternalId());
        // 收益地址
        this.setDenefitAddr(initData.getBenifitAddress());
        // 节点状态 1：候选中 2：退出中 3：已退出
        if(initData.getStatus()!=null) this.setStatus(initData.getStatus().intValue());
        // 节点名称(质押节点名称)
        this.setStakingName(initData.getNodeName());
        // 节点的第三方主页
        this.setWebSite(initData.getWebsite());
    }

    public void initWithTransactionBean(CustomTransaction initData){
        BeanUtils.copyProperties(initData,this);
        this.setStakingTxIndex(initData.getTransactionIndex());
        // 发起质押的账户地址
        this.setStakingAddr(initData.getFrom());
        // 质押金额(犹豫期金额)
        this.setStakingHas(initData.getValue());
        this.setStakingName(initData.getTxParam(CreateValidatorParam.class).getNodeName());
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

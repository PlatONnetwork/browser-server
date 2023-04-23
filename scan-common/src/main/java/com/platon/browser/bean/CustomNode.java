package com.platon.browser.bean;

import com.platon.browser.dao.entity.Node;
import com.platon.browser.dao.entity.Staking;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: Chendongming
 * @Date: 2019/8/14 12:02
 * @Description: 节点实体扩展类
 */
@Data
public class CustomNode extends Node {

     public CustomNode(){
         super();
         Date date = new Date();
         this.setUpdateTime(date);
         this.setCreateTime(date);
         setNodeId("");
         setStatSlashLowQty(0);
         setStatBlockQty(0L);
         setStatExpectBlockQty(0L);
         setStatVerifierTime(0);
         setIsRecommend(YesNoEnum.NO.getCode());
         setTotalValue(BigDecimal.ZERO);
         setStakingBlockNum(0L);
         setStakingTxIndex(0);
         setStakingHes(BigDecimal.ZERO);
         setStakingLocked(BigDecimal.ZERO);
         setStakingReduction(BigDecimal.ZERO);
         setStakingReductionEpoch(0);
         setNodeName("");
         setNodeIcon("");
         setExternalId("");
         setExternalName("");
         setStakingAddr("");
         setBenefitAddr("");
         setAnnualizedRate(0.0);
         setProgramVersion(0);
         setBigVersion(0);
         setWebSite("");
         setDetails("");
         setJoinTime(new Date());
         setLeaveTime(null);
         setStatus(Staking.StatusEnum.CANDIDATE.getCode());
         setIsConsensus(Staking.YesNoEnum.NO.getCode());
         setIsSettle(Staking.YesNoEnum.NO.getCode());
         setIsInit(Staking.YesNoEnum.NO.getCode());
         setStatDelegateValue(BigDecimal.ZERO);
         setStatDelegateReleased(BigDecimal.ZERO);
         setStatValidAddrs(0);
         setStatInvalidAddrs(0);
         setStatBlockRewardValue(BigDecimal.ZERO);
         setStatFeeRewardValue(BigDecimal.ZERO);
         setStatStakingRewardValue(BigDecimal.ZERO);
         setStatSlashLowQty(0);
         setStatSlashMultiQty(0);
     }

    public CustomNode updateWithStaking(Staking staking) {
        BeanUtils.copyProperties(staking,this);
        return this;
    }

    /**
     * 节点是否官方推荐类型枚举类：
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

}

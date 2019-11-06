package com.platon.browser.dto;

import com.platon.browser.dao.entity.Node;
import com.platon.browser.exception.NoSuchBeanException;
import com.platon.browser.utils.HexTool;
import lombok.Data;

import java.math.BigDecimal;
import java.util.*;

/**
 * @Auther: Chendongming
 * @Date: 2019/8/14 12:02
 * @Description: 节点实体扩展类
 */
@Data
public class CustomNode extends Node {
    // <质押区块号 - 质押实体>
    private TreeMap<Long, CustomStaking> stakings = new TreeMap<>();

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
         setStatus(CustomStaking.StatusEnum.CANDIDATE.getCode());
         setIsConsensus(CustomStaking.YesNoEnum.NO.getCode());
         setIsSettle(CustomStaking.YesNoEnum.NO.getCode());
         setIsInit(CustomStaking.YesNoEnum.NO.getCode());
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

     public void updateWithNode(org.web3j.platon.bean.Node node){
          this.setNodeId(HexTool.prefix(node.getNodeId()));
     }

    public void updateWithCustomStaking(CustomStaking staking) {
        this.setNodeId(staking.getNodeId());
        // 创建时间与质押节点加入时间一致
        this.setCreateTime(staking.getJoinTime());
    }

    /**
     * 获取指定节点的最新质押记录
     */
    public CustomStaking getLatestStaking() throws NoSuchBeanException {
        Map.Entry<Long, CustomStaking> lastEntry = stakings.lastEntry();
        if(lastEntry==null) throw new NoSuchBeanException("没有质押记录！");
        return lastEntry.getValue();
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

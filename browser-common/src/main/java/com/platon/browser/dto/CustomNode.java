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
          /* 初始化默认值 */
          // 多签举报次数
          this.setStatSlashMultiQty(0);
          // 出块率低举报次数
          this.setStatSlashLowQty(0);
          // 节点处块数统计
          this.setStatBlockQty(0L);
          // 节点期望出块数
//          this.setStatExpectBlockQty(BigDecimal.ZERO.toString());
//          // 节点收益统计(出块奖励 + 质押奖励)
//          this.setStatRewardValue(BigDecimal.ZERO.toString());
          // 进入共识验证轮次数
          this.setStatVerifierTime(0);
          // 官方推荐 1：是 2：否
          this.setIsRecommend(YesNoEnum.NO.getCode());
          //节点手续费
//          this.setStatFeeRewardValue("0");
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

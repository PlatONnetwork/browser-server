package com.platon.browser.dto;

import com.platon.browser.dao.entity.Node;
import com.platon.browser.dao.entity.NodeOpt;
import com.platon.browser.dao.entity.Slash;
import com.platon.browser.utils.HexTool;
import lombok.Data;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;

/**
 * @Auther: Chendongming
 * @Date: 2019/8/14 12:02
 * @Description:
 */
@Data
public class NodeBean extends Node {

     public NodeBean(){
          /** 初始化默认值 **/
          // 多签举报次数
          this.setStatSlashMultiQty(0);
          // 出块率低举报次数
          this.setStatSlashLowQty(0);
          // 节点处块数统计
          this.setStatBlockQty(0l);
          // 节点期望出块数
          this.setStatExpectBlockQty(0l);
          // 节点收益统计(出块奖励 + 质押奖励)
          this.setStatRewardValue("0");
          // 进入共识验证轮次数
          this.setStatVerifierTime(0);
          // 官方推荐 1：是 2：否
          this.setIsRecommend(2);
     }

     public void initWithNode(org.web3j.platon.bean.Node initData){
          Date date = new Date();
          this.setUpdateTime(date);
          this.setCreateTime(date);
          this.setNodeId(HexTool.prefix(initData.getNodeId()));
     }

     private TreeMap<Long, StakingBean> stakings = new TreeMap<>();
     private List<Slash> slashes = new ArrayList<>();
     private List<NodeOpt> nodeOpts = new ArrayList<>();

}

package com.platon.browser.dto;

import com.platon.browser.dao.entity.Node;
import com.platon.browser.dao.entity.NodeOpt;
import com.platon.browser.dao.entity.Slash;
import lombok.Data;

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
          this.setStatRewardValue("0");
          this.setStatSlashLowQty(0);
          this.setStatVerifierTime(0);
          this.setStatExpectBlockQty(0l);
          this.setStatBlockQty(0l);
          this.setStatSlashMultiQty(0);
          this.setIsRecommend(0);
     }

     public void initWithNode(org.web3j.platon.bean.Node initData){
          Date date = new Date();
          this.setUpdateTime(date);
          this.setCreateTime(date);
          this.setNodeId(initData.getNodeId().startsWith("0x")?initData.getNodeId():"0x"+initData.getNodeId());
     }

     private TreeMap<Long, StakingBean> stakings = new TreeMap<>();
     private List<Slash> slashes = new ArrayList<>();
     private List<NodeOpt> nodeOpts = new ArrayList<>();

}

package com.platon.browser.dto;

import com.platon.browser.dao.entity.Node;
import com.platon.browser.dao.entity.NodeOpt;
import com.platon.browser.dao.entity.Slash;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

/**
 * @Auther: Chendongming
 * @Date: 2019/8/14 12:02
 * @Description:
 */
@Data
public class NodeBean extends Node {
     private TreeMap<Long, StakingBean> stakings = new TreeMap<>();
     private List<Slash> slashes = new ArrayList<>();
     private List<NodeOpt> nodeOpts = new ArrayList<>();

}

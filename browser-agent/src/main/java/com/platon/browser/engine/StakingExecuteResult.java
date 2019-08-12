package com.platon.browser.engine;

import com.platon.browser.dao.entity.*;
import lombok.Data;

import java.util.*;

/**
 * @Auther: Chendongming
 * @Date: 2019/8/10 16:47
 * @Description:
 */
@Data
public class StakingExecuteResult {
    // 插入或更新数据
    private Set<UnDelegation> addUnDelegations = new HashSet<>();
    private Set<UnDelegation> updateUnDelegations = new HashSet<>();
    private Set<Delegation> addDelegations = new HashSet<>();
    private Set<Delegation> updateDelegations = new HashSet<>();
    private Set<Staking> addStakings = new HashSet<>();
    private Set<Staking> updateStakings = new HashSet<>();
    private Set<Node> addNodes = new HashSet<>();
    private Set<Node> updateNodes = new HashSet<>();
    private Set<Slash> addSlash = new HashSet<>();
    private Set<NodeOpt> addNodeOpts = new HashSet<>();
}

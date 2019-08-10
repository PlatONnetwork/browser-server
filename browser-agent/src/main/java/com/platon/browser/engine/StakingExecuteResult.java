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
    // 全量数据，需要根据业务变化，保持与数据库一致
    private Map<String, Node> nodes = new HashMap<>();
    private Map<String, Delegation> delegations = new HashMap<>();
    private Map<String, Staking> stakings = new HashMap<>();

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

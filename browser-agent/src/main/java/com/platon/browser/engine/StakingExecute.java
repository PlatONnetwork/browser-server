package com.platon.browser.engine;

import com.platon.browser.dao.entity.Delegation;
import com.platon.browser.dao.entity.Node;
import com.platon.browser.dao.entity.Staking;
import com.platon.browser.dao.mapper.DelegationMapper;
import com.platon.browser.dao.mapper.NodeMapper;
import com.platon.browser.dao.mapper.StakingMapper;
import com.platon.browser.dto.TransactionInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther: Chendongming
 * @Date: 2019/8/10 16:12
 * @Description:
 */
@Component
public class StakingExecute {

    @Autowired
    private NodeMapper nodeMapper;
    @Autowired
    private DelegationMapper delegationMapper;
    @Autowired
    private StakingMapper stakingMapper;

    // 全量数据，需要根据业务变化，保持与数据库一致
    private Map<String, Node> nodes = new HashMap<>();
    private Map<String, Delegation> delegations = new HashMap<>();
    private Map<String, Staking> stakings = new HashMap<>();

    private StakingExecuteResult executeResult= new StakingExecuteResult();

    @PostConstruct
    private void init(){
        // 初始化全量数据

        // 节点汇总列表
        List<Node> nodeList = nodeMapper.selectByExample(null);
        nodeList.forEach(node ->nodes.put(node.getNodeId(),node));

        // 质押列表,节点ID+质押区块号唯一确定一条记录
        List<Staking> stakingList = stakingMapper.selectByExample(null);
        stakingList.forEach(staking -> stakings.put(staking.getNodeId()+staking.getStakingBlockNum(),staking));

        // 委托列表
        List<Delegation> delegationList = delegationMapper.selectByExample(null);
        delegationList.forEach(delegation -> delegations.put(delegation.getDelegateAddr(),delegation));
    }

    /**
     * 执行交易
     * @param trans
     * @param bc
     */
    public void execute(TransactionInfo trans, BlockChain bc){
        switch (trans.getTypeEnum()){
            case CREATEVALIDATOR:
                execute1000(trans);
                break;
            case EDITVALIDATOR:
                execute1001(trans);
                break;
            case INCREASESTAKING:
                execute1002(trans);
                break;
            case EXITVALIDATOR:
                execute1003(trans);
                break;
            case UNDELEGATE:
                execute1005(trans);
                break;
            case REPORTVALIDATOR:
                execute3000(trans);
                break;
        }

        updateTxInfo(trans,bc);
    }

    public StakingExecuteResult exportResult(){
        return executeResult;
    }

    public void commitResult(){
        executeResult.getAddDelegations().clear();

    }

    /**
     * 进入新的结算周期
     */
    public void onNewSettingEpoch(){

    }

    /**
     * 进入新的共识周期变更
     */
    public void onNewConsEpoch(){

    }

    /**
     * 进行选择验证人时触发
     */
    public void onElectionDistance(){

    }

    private void updateTxInfo(TransactionInfo trans,BlockChain bc){
    }

    private void execute1000(TransactionInfo trans){

    }
    private void execute1001(TransactionInfo trans){

    }
    private void execute1002(TransactionInfo trans){

    }
    private void execute1003(TransactionInfo trans){

    }
    private void execute1005(TransactionInfo trans){

    }
    private void execute3000(TransactionInfo trans){

    }
}

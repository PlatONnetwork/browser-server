package com.platon.browser.engine;

import com.platon.browser.client.PlatonClient;
import com.platon.browser.dao.entity.Delegation;
import com.platon.browser.dao.entity.Node;
import com.platon.browser.dao.entity.Staking;
import com.platon.browser.dao.mapper.DelegationMapper;
import com.platon.browser.dao.mapper.NodeMapper;
import com.platon.browser.dao.mapper.StakingMapper;
import com.platon.browser.dto.StakingBean;
import com.platon.browser.dto.TransactionBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static Logger logger = LoggerFactory.getLogger(StakingExecute.class);

    @Autowired
    private NodeMapper nodeMapper;
    @Autowired
    private DelegationMapper delegationMapper;
    @Autowired
    private StakingMapper stakingMapper;

    @Autowired
    private PlatonClient client;

    // 全量数据，需要根据业务变化，保持与数据库一致
    private Map<String, Node> nodes = new HashMap<>(); // 节点统计表
    private Map<String, Delegation> delegations = new HashMap<>(); // 委托表
    private Map<String, Staking> stakings = new HashMap<>(); // 验证人列表

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
     * @param tx
     * @param bc
     */
    public void execute(TransactionBean tx, BlockChain bc){
        switch (tx.getTypeEnum()){
            case CREATEVALIDATOR:
                execute1000(tx,bc);
                break;
            case EDITVALIDATOR:
                execute1001(tx,bc);
                break;
            case INCREASESTAKING:
                execute1002(tx,bc);
                break;
            case EXITVALIDATOR:
                execute1003(tx,bc);
                break;
            case UNDELEGATE:
                execute1005(tx,bc);
                break;
            case REPORTVALIDATOR:
                execute3000(tx,bc);
                break;
        }

        updateTxInfo(tx,bc);
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

    private void updateTxInfo(TransactionBean tx, BlockChain bc){

    }

    //发起质押(创建验证人)
    private void execute1000(TransactionBean tx, BlockChain bc){
        StakingBean staking = new StakingBean();
        staking.initWithCreateValidatorDto(tx);

        // TODO: 修改验证人列表
        // 修改验证人列表
    }
    //修改质押信息(编辑验证人)
    private void execute1001(TransactionBean tx, BlockChain bc){

        // TODO: 修改验证人列表
        // 修改验证人列表
    }
    //增持质押(增加自有质押)
    private void execute1002(TransactionBean tx, BlockChain bc){

        // TODO: 修改验证人列表
        // 修改验证人列表
    }
    //撤销质押(退出验证人)
    private void execute1003(TransactionBean tx, BlockChain bc){

        // TODO: 修改验证人列表
        // 修改验证人列表
    }
    //减持/撤销委托(赎回委托)
    private void execute1005(TransactionBean tx, BlockChain bc){

        // TODO: 修改验证人列表
        // 修改验证人列表
    }
    //举报多签(举报验证人)
    private void execute3000(TransactionBean tx, BlockChain bc){

        // TODO: 修改验证人列表
        // 修改验证人列表
    }
}

package com.platon.browser.engine;

import com.platon.browser.dao.entity.Node;
import com.platon.browser.dto.BlockInfo;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: Chendongming
 * @Date: 2019/8/10 16:11
 * @Description:
 */
@Component
@Data
public class BlockChain {

    @Autowired
    private BlockChainConfig chainConfig;
    @Autowired
    private StakingExecute stakingExecute;
    @Autowired
    private ProposalExecute proposalExecute;
    private long curSettingEpoch;
    private long curConsensusEpoch;
    private BlockInfo curBlock;
    // 当前结算周期验证人
    private Map<String, Node> curVerifier = new HashMap<>();
    // 上轮结算周期验证人
    private Map<String, Node> preVerifier = new HashMap<>();
    // 当前共识周期验证人
    private Map<String, Node> curValidator = new HashMap<>();
    // 上轮共识周期验证人
    private Map<String, Node> preValidator = new HashMap<>();


    private void init(){

    }

    /**
     * 执行区块
     * @param block
     */
    public void execute(BlockInfo block){
        init();


    }

    /**
     * 导出需要入库的数据
     * @return
     */
    public BlockInfo exportResult(){
        return curBlock;
    }

    public void commitResult(){

    }

    /**
     * 进入新的结算周期
     */
    private void onNewSettingEpoch(){
        stakingExecute.onNewSettingEpoch();
    }

    /**
     * 进入新的共识周期变更
     */
    private void onNewConsEpoch(){
        stakingExecute.onNewConsEpoch();
    }

    /**
     * 进行选择验证人时触发
     */
    private void onElectionDistance(){
        stakingExecute.onElectionDistance();
    }



}

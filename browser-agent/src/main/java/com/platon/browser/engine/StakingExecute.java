package com.platon.browser.engine;

import com.platon.browser.dto.TransactionInfo;
import org.springframework.stereotype.Component;

/**
 * @Auther: Chendongming
 * @Date: 2019/8/10 16:12
 * @Description:
 */
@Component
public class StakingExecute {

    private StakingExecuteResult executeResult;

    private void init(){

    }

    /**
     * 执行交易
     * @param trans
     * @param bc
     */
    public void execute(TransactionInfo trans, BlockChain bc){
        init();

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

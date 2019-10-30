package com.platon.browser.old.engine.stage;

/**
 * @Auther: Chendongming
 * @Date: 2019/8/10 16:47
 * @Description: 区块链业务信息新增或修改暂存类，入库后各容器需要清空
 */
public class BlockChainStage {
    private ProposalStage proposalStage=new ProposalStage();
    private StakingStage stakingStage=new StakingStage();
    private AddressStage addressStage =new AddressStage();
    private NetworkStatStage networkStatStage =new NetworkStatStage();
    private RestrictingStage restrictingStage = new RestrictingStage();

    public void clear(){
        proposalStage.clear();
        stakingStage.clear();
        addressStage.clear();
        networkStatStage.clear();
        restrictingStage.clear();
    }

    public ProposalStage getProposalStage() {
        return proposalStage;
    }

    public StakingStage getStakingStage() {
        return stakingStage;
    }

    public AddressStage getAddressStage() {
        return addressStage;
    }

    public NetworkStatStage getNetworkStatStage() {
        return networkStatStage;
    }

    public RestrictingStage getRestrictingStage(){ return restrictingStage;}

}
